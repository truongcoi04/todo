package com.vti.todo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private  String password;

    @NotNull
    private String fullName;

    private String role;

    @ManyToOne
    private Department department;

    private LocalDateTime createdDate;


}
