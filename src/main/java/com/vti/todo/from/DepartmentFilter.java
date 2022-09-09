package com.vti.todo.from;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentFilter {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxDate;


}
