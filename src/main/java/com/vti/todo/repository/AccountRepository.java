package com.vti.todo.repository;

import com.vti.todo.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    @Query("SELECT (count(a) = 0) from AccountEntity a where a.email = :email")    // SQL
    boolean isEmailNotExists(String email);


    AccountEntity findByEmail(String email);



    @Query (value = "select * " +
            "from accounts " +
            "where (:search is null OR (full_name like :search or email like :search)) " +
            "AND (:role is null OR role = :role) " +
            "AND (:departmentId is null OR department_id = :departmentId) " +
            "ORDER BY created_date DESC " , nativeQuery = true)
    Page<AccountEntity> searchAccount(Integer departmentId, String role, String search, Pageable pageable);


    @Modifying
    @Transactional
    @Query("DELETE FROM AccountEntity WHERE id IN(:id)")  //HQL trong hibernate
    void deleteById(@Param("id") List<Integer> id);


}
