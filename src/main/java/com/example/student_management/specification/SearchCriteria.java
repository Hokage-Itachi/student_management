package com.example.student_management.specification;

import com.example.student_management.enums.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private SearchOperation option;
    private String value;
    private boolean orPredicate;

    public SearchCriteria(String key, String operation, String value){
        this.key = key;
        this.option = SearchOperation.getSearchOperation(operation);
        this.value = value;
    }
}
