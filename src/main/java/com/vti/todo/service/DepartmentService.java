package com.vti.todo.service;


import com.vti.todo.Specification.DepartmentSpecification;
import com.vti.todo.dto.request.DepartmentRequest;
import com.vti.todo.dto.request.RegisterAccountRequest;
import com.vti.todo.dto.response.AccountResponse;
import com.vti.todo.entity.AccountEntity;
import com.vti.todo.entity.Department;
import com.vti.todo.from.DepartmentFilter;
import com.vti.todo.repository.AccountRepository;
import com.vti.todo.repository.DepartmentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public Optional<Department> deleteDepartment(Integer id) {
        Optional<Department> department = departmentRepository.findById(id);
        department.ifPresent(d -> departmentRepository.delete(d));
        return department;

    }


    public Page<Department> getAllDepartments(Pageable pageable, String search, DepartmentFilter filter) {

        Specification<Department> where = null;
        if (!StringUtils.isEmpty(search)) {
            DepartmentSpecification searchSpecification = new DepartmentSpecification("name", "like", search);
            where = Specification.where(searchSpecification);
        }


        if (filter != null && filter.getMinDate() != null) {
            DepartmentSpecification minDateSpecification = new DepartmentSpecification("createdDate", ">=", filter.getMinDate());
            if (where == null) {
                where = Specification.where(minDateSpecification);
            } else {
                where = where.and(minDateSpecification);
            }
        }
        if (filter != null && filter.getMaxDate() != null) {
            DepartmentSpecification maxDateSpecification = new DepartmentSpecification("createdDate", "<=", filter.getMaxDate());
            if (where == null) {
                where = Specification.where(maxDateSpecification);
            } else {
                where = where.and(maxDateSpecification);
            }
        }
        return departmentRepository.findAll(where, pageable);

    }

    public DepartmentRequest updateDepartment(Integer id, DepartmentRequest departmentRequest) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            Department department1 = department.get();
            department1.setName(departmentRequest.getName());
            departmentRepository.save(department1);

            DepartmentRequest request = new DepartmentRequest();
            BeanUtils.copyProperties(department1, request);
            return request;
        }
        return null;
    }


    public void deleteAllDepartment(List<Integer> id) {
        departmentRepository.deleteById(id);
    }

    public ResponseEntity<?> registerNewDepartment(DepartmentRequest departmentRequest) {
        Department department = new Department();
        department.setName(departmentRequest.getName());
        department.setCreatedDate(LocalDate.now());
        departmentRepository.save(department);
        return ResponseEntity.ok().build();
    }


}



