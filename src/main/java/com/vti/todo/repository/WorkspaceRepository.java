package com.vti.todo.repository;

import com.vti.todo.dto.response.WorkspaceResponse;
import com.vti.todo.entity.TaskEntity;
import com.vti.todo.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Integer> {


//    @Query("SELECT NEW com.vti.todo.dto.response.WorkspaceResponse(w.id, w.name, 0) FROM WorkspaceEntity w WHERE w.account.email = :email")
//    List<WorkspaceResponse> getWorkspaceResponseByUserEmail(String email);


    @Query("    SELECT NEW com.vti.todo.dto.response.WorkspaceResponse(w.id, w.name, count(t))\n" +
            "    FROM WorkspaceEntity w left join TaskEntity t ON w = t.workspace\n" +
            "    where w.account.email = :email\n" +
            "    GROUP BY w")
    List<WorkspaceResponse> getWorkspaceResponseByUserEmail(String email);


//    List<TaskEntity> findByWorkspaceId(Integer workspaceId);


}
