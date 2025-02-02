package com.cuidarmais.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cuidarmais.demo.Entities.Task;
import com.cuidarmais.demo.Entities.EntityObjects.Enums.Status;
import com.cuidarmais.demo.Entities.EntityObjects.Enums.SupportType;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findBySupportTypeAndStatusOrderByDateAsc(SupportType supportType, Status status);

    List<Task> findByStatusOrderByDateAsc(Status status);

    List<Task> findBySupportTypeOrderByDateAsc(SupportType supportType);

}
