package com.vti.todo.service;

import com.vti.todo.dto.request.LoginRequest;
import com.vti.todo.dto.request.RegisterAccountRequest;
import com.vti.todo.dto.request.ResetPasswordRequest;
import com.vti.todo.dto.response.AccountResponse;
import com.vti.todo.dto.response.JwtResponse;
import com.vti.todo.dto.response.TaskResponse;
import com.vti.todo.entity.AccountEntity;
import com.vti.todo.entity.Department;
import com.vti.todo.entity.OtpAccount;
import com.vti.todo.repository.AccountRepository;
import com.vti.todo.repository.DepartmentRepository;
import com.vti.todo.repository.OtpAccountRepository;
import com.vti.todo.security.JwtTokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
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


//    public AccountEntity registerNewAccount(RegisterAccountRequest registerAccountRequest) {
//        AccountEntity account = new AccountEntity();
//        account.setEmail(registerAccountRequest.getEmail());
//        account.setPassword(registerAccountRequest.getPassword());
//        account.setFullName(registerAccountRequest.getFullName());
//        accountRepository.save(account);
//        return account;
//    }

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
        return ResponseEntity.ok().build();
    }


    public Optional<AccountEntity> updateAccount(Integer id, AccountResponse accountResponse) {
        Optional<AccountEntity> account = accountRepository.findById(id);
        account.ifPresent(acc -> {
            acc.setRole(accountResponse.getRole());
            Department referenceById = departmentRepository.getReferenceById(accountResponse.getDepartmentId());
            acc.setDepartment(referenceById);
            accountRepository.save(acc);
        });
        return account;
    }



    public void deleteAccounts(List<Integer> id) {
        accountRepository.deleteById(id);
    }

}
