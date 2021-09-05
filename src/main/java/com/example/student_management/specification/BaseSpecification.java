package com.example.student_management.specification;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.Class_;
import com.example.student_management.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BaseSpecification {
    public static <T> Specification<T> filterNumberValue(String key, String operator, Long value){
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                switch (SearchOperation.getSearchOperation(operator)){

                }
                return criteriaBuilder.greaterThan(root.get(key).as(Long.class), value);
            }
        };
    }
}
