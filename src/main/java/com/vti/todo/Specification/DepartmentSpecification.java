package com.vti.todo.Specification;

import com.vti.todo.entity.Department;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class DepartmentSpecification implements Specification<Department> {

    private String field;
    private String operator;
    private Object value;

    public DepartmentSpecification(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    // search theo departmentName
    @Override
    public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (operator.equalsIgnoreCase("like")) {
            return builder.like(root.get(field), "%" + value.toString() + "%");
        }


//        Filter theo MinCreatedDate & MaxCreatedDate
        if (operator.equalsIgnoreCase(">=")) {
            if (value instanceof Date) {
                return builder.greaterThan(root.get(field), (Date) value);
            }
        }
        if (operator.equalsIgnoreCase("<=")) {
            if (value instanceof Date) {
                return builder.lessThan(root.get(field), (Date) value);
            }
        }
        return null;

    }
}