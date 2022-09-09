package com.vti.todo.repository;


import com.vti.todo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Department WHERE id IN(:id)")
    void deleteById(@Param("id") List<Integer> id);
}

