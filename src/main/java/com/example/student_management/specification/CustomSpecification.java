package com.example.student_management.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CustomSpecification<Object> implements Specification<Object> {
    private final SearchCriteria criteria;

    public CustomSpecification(SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOption()) {
            case EQUALITY:
                return builder.equal(builder.lower(root.get(criteria.getKey())), criteria.getValue().toLowerCase());
            case NEGATION:
                return builder.notEqual(builder.lower(root.get(criteria.getKey())), criteria.getValue().toLowerCase());
            case GREATER_THAN:
                return builder.greaterThan(builder.lower(root.get(criteria.getKey())), criteria.getValue().toLowerCase());
            case LESS_THAN:
                return builder.lessThan(builder.lower(root.get(criteria.getKey())), criteria.getValue().toLowerCase());
            case LIKE:
                return builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().toLowerCase());
            case STARTS_WITH:
                return builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().toLowerCase() + "%");
            case ENDS_WITH:
                return builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toLowerCase());
            case CONTAINS:
                return builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toLowerCase() + "%");
            case NOT_CONTAINS:
                return builder.notLike(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toLowerCase() + "%");
            default:
                return null;
        }
    }
}
