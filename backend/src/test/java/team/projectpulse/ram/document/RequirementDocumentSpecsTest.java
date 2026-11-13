package team.projectpulse.ram.document;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RequirementDocumentSpecsTest {

    private Locale originalLocale;

    // Locale.setDefault changes the whole JVM. Save it and put it back,
    // or every test that runs after this one runs in Turkish.
    @BeforeEach
    void saveLocale() {
        originalLocale = Locale.getDefault();
    }

    @AfterEach
    void restoreLocale() {
        Locale.setDefault(originalLocale);
    }

    @Test
    @SuppressWarnings("unchecked")
    void hasTitleLikeBuildsTheSamePatternOnATurkishLocaleServer() {
        // Simulate a server whose default locale is Turkish.
        Locale.setDefault(Locale.forLanguageTag("tr"));

        // Fake JPA objects: we only care what string the spec hands to like().
        Root<RequirementDocument> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<Object> titlePath = mock(Path.class);
        Expression<String> loweredTitle = mock(Expression.class);

        doReturn(titlePath).when(root).get("title");
        doReturn(loweredTitle).when(cb).lower(any());

        // Run the spec's lambda, as Spring Data would when building the query.
        Specification<RequirementDocument> spec = RequirementDocumentSpecs.hasTitleLike("Initial");
        spec.toPredicate(root, query, cb);

        // Capture the pattern passed to like(lower(title), pattern).
        ArgumentCaptor<String> pattern = ArgumentCaptor.forClass(String.class);
        verify(cb).like(eq(loweredTitle), pattern.capture());

        // The database lowercases "I" to "i"; the pattern must match that.
        assertThat(pattern.getValue()).isEqualTo("%initial%");
    }
}