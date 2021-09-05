package com.example.student_management.specification;

import com.example.student_management.domain.Class;
import com.example.student_management.domain.*;
import com.example.student_management.request.ClassFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClassSpecification {

    public static Specification<Class> filterByName(String value) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(Class_.NAME)).as(String.class), "%" + value.toLowerCase() + "%");
        };
    }

    public static Specification<Class> filterByDate(String propertyName, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null && toDate == null) {
            return null;
        }
        if (fromDate != null && toDate != null) {
            Specification<Class> fromDateSpec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(propertyName).as(LocalDate.class), fromDate);
            Specification<Class> endDateSpec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(propertyName).as(LocalDate.class), toDate);
            return Specification.where(fromDateSpec).and(endDateSpec);
        } else if (fromDate == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(propertyName).as(LocalDate.class), toDate);
        } else {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(propertyName).as(LocalDate.class), fromDate);
        }
    }

    public static Specification<Class> filterByStatus(String value) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (value == null) {
                return null;
            }
            return criteriaBuilder.like(root.get(Class_.STATUS).as(String.class), value);
        };
    }

    public static Specification<Class> filterByCourse(Long value) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (value == null) {
                return null;
            }
            Join<Class, Course> join = root.join(Class_.COURSE);
            return criteriaBuilder.equal(join.get(Course_.ID).as(Long.class), value);
        };
    }

    public static Specification<Class> filterByTeacher(Long value) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (value == null) {
                return null;
            }
            Join<Class, Teacher> join = root.join(Class_.TEACHER);
            return criteriaBuilder.equal(join.get(Teacher_.ID).as(Long.class), value);

        };
    }

    public static Specification<Class> buildSpecification(ClassFilterRequest filterRequest) {
        List<Specification<Class>> specificationList = new ArrayList<>();
        // filter by name if not null
        if (filterRequest.getName() != null && filterRequest.getName().length > 0) {
            Specification<Class> specification = filterByName(filterRequest.getName()[0]);
            for (int i = 1; i < filterRequest.getName().length; i++) {
                // apply or operation
                specification = Specification.where(specification).or(filterByName(filterRequest.getName()[i]));
            }
            specificationList.add(specification);
        }
        // filter by start date if not null
        if (filterRequest.getStartDateFrom() != null || filterRequest.getStartDateTo() != null) {
            specificationList.add(filterByDate(Class_.START_DATE, filterRequest.getStartDateFrom(), filterRequest.getStartDateTo()));
        }
        // filter by end date if not null
        if (filterRequest.getEndDateFrom() != null || filterRequest.getEndDateTo() != null) {
            specificationList.add(filterByDate(Class_.END_DATE, filterRequest.getEndDateFrom(), filterRequest.getEndDateTo()));
        }
        // filter by status if not null
        if (filterRequest.getStatus() != null && filterRequest.getStatus().length > 0) {
            Specification<Class> specification = filterByStatus(filterRequest.getStatus()[0]);
            for (int i = 1; i < filterRequest.getStatus().length; i++) {
                specification = Specification.where(specification).or(filterByStatus(filterRequest.getStatus()[i]));
            }
            specificationList.add(specification);
        }
        // filter by course if not null
        if (filterRequest.getCourseId() != null) {
            specificationList.add(filterByCourse(filterRequest.getCourseId()));
        }

        // filter by teacher if not null
        if (filterRequest.getTeacherId() != null) {
            specificationList.add(filterByTeacher(filterRequest.getTeacherId()));
        }

        Specification<Class> finalSpecification = null;
        if (!specificationList.isEmpty()) {
            finalSpecification = specificationList.get(0);
            for (int i = 1; i < specificationList.size(); i++) {
                finalSpecification = Specification.where(finalSpecification).and(specificationList.get(i));
            }
        }

        return finalSpecification;
    }
}
