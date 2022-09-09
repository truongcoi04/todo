package com.vti.todo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class DepartmentRequest {

    private int id;
    private String name;
    private LocalDate createdDate;


}
