package com.vti.todo.repository;

import com.vti.todo.entity.OtpAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpAccountRepository extends JpaRepository<OtpAccount, Long> {

    OtpAccount findByEmailAndOtp(String email, String otp);

}
