package com.vti.todo.service;

import com.vti.todo.dto.request.LoginRequest;
import com.vti.todo.dto.request.RegisterAccountRequest;
import com.vti.todo.dto.request.ResetPasswordRequest;
import com.vti.todo.dto.response.AccountResponse;
import com.vti.todo.dto.response.JwtResponse;
import com.vti.todo.entity.AccountEntity;
import com.vti.todo.entity.Department;
import com.vti.todo.entity.OtpAccount;
import com.vti.todo.repository.AccountRepository;
import com.vti.todo.repository.DepartmentRepository;
import com.vti.todo.repository.OtpAccountRepository;
import com.vti.todo.security.JwtTokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SendMailService sendMailService;


    public JwtResponse login(LoginRequest loginRequest) {
        try {
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            String token = jwtTokenProvider.createToken(authenticate.getName(), authenticate.getAuthorities());
            return new JwtResponse(token);
        } catch (Exception e) {
            return new JwtResponse();
        }
    }

    @Autowired
    OtpAccountRepository otpAccountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void forgotPassword(String email) {

        AccountEntity account = accountRepository.findByEmail(email);

        if (account != null) {

            Random rd = new SecureRandom();
            int random = rd.nextInt(999_999);// random tu 0 -> 999_999
            String randomStr = String.format("%06d", random);   // %06d  -> VD:1 -> 000001, 10 ->000010

            OtpAccount otp = new OtpAccount();
            otp.setOtp(randomStr);
            otp.setEmail(email);
            otp.setExpire(LocalDateTime.now().plusMinutes(30)); // otp có hạn trong 30 phút
            otpAccountRepository.save(otp);   // lưu otp vào data
            sendMailService.sendForgotPassword(account.getEmail(), randomStr);
        }
    }

    @Transactional   // chạy từ trên xuống,gặp lỗi dừng lại k chạy
    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {
        OtpAccount otp = otpAccountRepository.findByEmailAndOtp(request.getEmail(), request.getOtp());
        if (otp != null && otp.getExpire().isAfter(LocalDateTime.now())) { //nếu otp tồn tại và chưa hết hạn (> now)
            AccountEntity accountEntity = accountRepository.findByEmail(request.getEmail());
            accountEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            accountRepository.save(accountEntity);
            otpAccountRepository.delete(otp);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    public Page<AccountEntity> searchAccount(Integer departmentId, String role, String search, Pageable pageable) {
        if (search != null) {
            search = "%" + search + "%";
        }
        return accountRepository.searchAccount(departmentId, role, search, pageable);
    }


    public ResponseEntity registerNewAccount(RegisterAccountRequest registerAccountRequest) {
        AccountEntity account = new AccountEntity();
        account.setEmail(registerAccountRequest.getEmail());
        account.setFullName(registerAccountRequest.getFullName());
        account.setPassword(passwordEncoder.encode(registerAccountRequest.getPassword()));
        Locale locale = LocaleContextHolder.getLocale();
        account.setRole(registerAccountRequest.getRole());
        Department department = departmentRepository.getReferenceById(registerAccountRequest.getDepartmentId());
        account.setDepartment(department);
        account.setCreatedDate(LocalDateTime.now());
        accountRepository.save(account);

        Department department1 = new Department();
        department1.setName("Nhân Viên");
        departmentRepository.save(department1);
        return ResponseEntity.ok().build();
    }






    public AccountResponse updateAccount(Integer id, AccountResponse accountResponse) {
        Optional<AccountEntity> account = accountRepository.findById(id);

        if (account.isPresent()){
            AccountEntity account1 = account.get();

            Department referenceById = departmentRepository.getReferenceById(accountResponse.getDepartmentId());
            account1.setDepartment(referenceById);
            account1.setRole(accountResponse.getRole());
            accountRepository.save(account1);

            AccountResponse response = new AccountResponse();
            BeanUtils.copyProperties(account1, response);
            return response;
        }
        return null;
    }


    public Optional<AccountEntity> deleteAccount(Integer id) {
        Optional<AccountEntity> account = accountRepository.findById(id);
        account.ifPresent(account1 -> accountRepository.delete(account1));
        return account;
    }

    public void deleteAccounts(List<Integer> id) {
        accountRepository.deleteById(id);
    }



}
