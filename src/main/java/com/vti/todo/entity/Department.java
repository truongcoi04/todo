package com.vti.todo.entity;

import lombok.Data;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


    private LocalDate createdDate;



}
