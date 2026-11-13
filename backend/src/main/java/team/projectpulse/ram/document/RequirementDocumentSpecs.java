package team.projectpulse.ram.document;

import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

public final class RequirementDocumentSpecs {

    private RequirementDocumentSpecs() {
        // static factory methods only; not meant to be instantiated
    }

    public static Specification<RequirementDocument> hasType(DocumentType providedType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), providedType);
    }

    public static Specification<RequirementDocument> hasTeamId(Integer providedTeamId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("team").get("teamId"), providedTeamId);
    }

    public static Specification<RequirementDocument> hasTitleLike(String providedTitle) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + providedTitle.toLowerCase(Locale.ROOT) + "%");
    }

}
