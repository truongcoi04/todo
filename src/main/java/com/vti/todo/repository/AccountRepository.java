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

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    @Query("SELECT (count(a) = 0) from AccountEntity a where a.email = :email")
    boolean isEmailNotExists(String email);


    AccountEntity findByEmail(String email);

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM accounts WHERE id = :id")
//    void deleteById(@Param("id") List<Integer> id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Account WHERE id IN(:id)")
    void deleteById(@Param("id") List<Integer> id);



    @Query(value = "select * from accounts where (:search is null or (full_name like :search of email like :search))" +
            " and (:role is null or role = :role) " +
            "and (:departmentId is null or department_id = :departmentId) order by created_date desc", nativeQuery = true)
    Page<AccountEntity> searchAccount(Integer departmentId, String role, String search, Pageable pageable);



}
