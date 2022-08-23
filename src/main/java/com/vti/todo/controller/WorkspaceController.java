package com.vti.todo.controller;

import com.vti.todo.dto.request.WorkspaceRequest;
import com.vti.todo.dto.response.WorkspaceResponse;
import com.vti.todo.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;


    @PostMapping
    public WorkspaceResponse createWorkspace(@RequestBody @Valid WorkspaceRequest request) {

        return workspaceService.createWorkspace(request);
    }

    @GetMapping
    public List<WorkspaceResponse> getCurrentWorkspace() {
        return workspaceService.getCurrentWorkspace();
    }


}
