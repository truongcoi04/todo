package com.vti.todo.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;

    @ManyToOne
    private TaskStatusEntity taskStatus;

    @ManyToOne
    private WorkspaceEntity workspace;

}
