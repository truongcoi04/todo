package com.vti.todo.controller;

import com.vti.todo.dto.request.LoginRequest;
import com.vti.todo.dto.request.RegisterAccountRequest;
import com.vti.todo.dto.request.ResetPasswordRequest;
import com.vti.todo.dto.response.AccountResponse;
import com.vti.todo.dto.response.JwtResponse;
import com.vti.todo.entity.AccountEntity;
import com.vti.todo.entity.Department;
import com.vti.todo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/accounts")
public class AccountsController {
    @Autowired
    private AccountService accountService;




//    @PostMapping("/register")
//    public AccountEntity registerNewAccount(@Valid @RequestBody RegisterAccountRequest registerAccountRequest) {
//        return accountService.registerNewAccount(registerAccountRequest);
//    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return accountService.login(loginRequest);
    }


    @GetMapping("/principal")
    public UserDetails principal(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }


    @PostMapping("/forgot/{email}")
    public void forgotPassword(@PathVariable String email) {
        accountService.forgotPassword(email);
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return accountService.resetPassword(request);
    }


    // tạo mới account
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> registerNewAccount(@RequestBody @Valid RegisterAccountRequest registerAccountRequest) {
        return accountService.registerNewAccount(registerAccountRequest);
    }

    // search account theo departmentID of role of ten nhan vien
    @GetMapping
    private Page<AccountEntity> searchAccount(Integer departmentId, String role, String search, Pageable pageable) {
        return accountService.searchAccount(departmentId, role, search, pageable);
    }

    //update account
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Optional<AccountEntity> updateAccount(@PathVariable Integer id, @RequestBody @Valid AccountResponse accountResponse) {
        return accountService.updateAccount(id, accountResponse);
    }

    //delete nhiêu account/1 lượt
    @DeleteMapping
    public void deleteAccounts(@RequestParam(name = "id") List<Integer> id) {
        accountService.deleteAccounts(id);
    }




}
