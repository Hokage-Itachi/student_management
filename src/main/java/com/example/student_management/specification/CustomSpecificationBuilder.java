package com.example.student_management.specification;

import com.example.student_management.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSpecificationBuilder<Object> {
    private final List<SearchCriteria> parameters;

    public CustomSpecificationBuilder(List<SearchCriteria> parameters) {
        this.parameters = parameters;
    }

    public CustomSpecificationBuilder() {
        this.parameters = new ArrayList<>();
    }

    public CustomSpecificationBuilder<Object> with(String key, String option, String value) {
        parameters.add(new SearchCriteria(key, option, value));
        return this;
    }

    public Specification<Object> build() {
        if (parameters == null || parameters.size() == 0) {
            return null;
        }
        List<Specification<Object>> specs = parameters.stream().map(CustomSpecification<Object>::new).collect(Collectors.toList());
        Specification<Object> specification = specs.get(0);
        for (int i = 1; i < parameters.size(); i++) {
            specification = parameters.get(i).isOrPredicate() ? Specification.where(specification).or(specs.get(i)) : Specification.where(specification).and(specs.get(i));
        }
        return specification;
    }
}
