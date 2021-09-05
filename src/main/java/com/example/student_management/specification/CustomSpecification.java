package com.example.student_management.specification;

import com.sun.xml.bind.v2.TODO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class CustomSpecification<Object> implements Specification<Object> {
    private final SearchCriteria criteria;

    public CustomSpecification(SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // TODO: 7/22/2021 separate specification
       Expression expression = root.get(criteria.getKey()).as(criteria.getValue().getClass());
        if (criteria.getJoinColumn() != null && !criteria.getJoinColumn().isBlank()) {
            Join<Object, Object> joinParent = root.join(criteria.getJoinColumn());
            expression = joinParent.get(criteria.getKey()).as(criteria.getValue().getClass());
        }
        switch (criteria.getOption()) {
            case EQUALITY:
                return builder.equal(builder.lower(expression), criteria.getValue());
            case NEGATION:
                return builder.notEqual(builder.lower(expression), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(builder.lower(expression), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(builder.lower(expression), criteria.getValue().toString().toLowerCase());
            case LIKE:
                return builder.like(builder.lower(expression), criteria.getValue().toString().toLowerCase());
            case STARTS_WITH:
                return builder.like(builder.lower(expression), criteria.getValue().toString().toLowerCase() + "%");
            case ENDS_WITH:
                return builder.like(builder.lower(expression), "%" + criteria.getValue().toString().toLowerCase());
            case CONTAINS:
                return builder.like(builder.lower(expression), "%" + criteria.getValue().toString().toLowerCase() + "%");
            case NOT_CONTAINS:
                return builder.notLike(builder.lower(expression), "%" + criteria.getValue().toString().toLowerCase() + "%");
            default:
                return null;
        }
    }
}
