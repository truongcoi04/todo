package com.vti.todo.controller;

import com.vti.todo.dto.request.DepartmentRequest;
import com.vti.todo.dto.request.RegisterAccountRequest;
import com.vti.todo.dto.response.AccountResponse;
import com.vti.todo.entity.AccountEntity;
import com.vti.todo.entity.Department;
import com.vti.todo.from.DepartmentFilter;
import com.vti.todo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

// delete department theo ID
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Optional<Department> deleteDepartment(@PathVariable Integer id) {
        return departmentService.deleteDepartment(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public void deleteAllDepartment(@RequestParam(name = "id") List<Integer> id) {
        departmentService.deleteAllDepartment(id);
    }


    @GetMapping
    public Page<Department> getAllDepartments(Pageable pageable , @RequestParam(required = false) String search, DepartmentFilter filter){
        return departmentService.getAllDepartments(pageable , search, filter);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DepartmentRequest updateDepartment(@PathVariable Integer id, @RequestBody @Valid DepartmentRequest departmentRequest) {
        return departmentService.updateDepartment(id, departmentRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> registerNewDepartment(@RequestBody @Valid DepartmentRequest departmentRequest) {
        return departmentService.registerNewDepartment(departmentRequest);
    }



}
