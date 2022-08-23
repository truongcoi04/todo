package com.vti.todo.repository;

import com.vti.todo.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    int countByWorkspaceId(Integer id);

    List<TaskEntity> findByWorkspaceId(Integer workspaceId);



}
