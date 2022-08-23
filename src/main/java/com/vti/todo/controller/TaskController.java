package com.vti.todo.controller;

import com.vti.todo.dto.request.TaskRequest;
import com.vti.todo.dto.response.TaskResponse;
import com.vti.todo.entity.TaskEntity;
import com.vti.todo.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;


    @GetMapping("/workspaces/{workspaceId}")
    List<TaskResponse> getTasksByWorkspace(@PathVariable Integer workspaceId) {
        return taskService.getTasksByWorkspace(workspaceId);
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TaskResponse updateTaskResponse(@PathVariable Integer id, @RequestBody @Valid TaskRequest request) {
        return taskService.updateTaskResponse(id, request);
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public TaskEntity addTask(@RequestBody @Valid TaskEntity taskEntity) {
        return taskService.addTask(taskEntity);
    }


}
