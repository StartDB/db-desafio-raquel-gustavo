package com.cuidarmais.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cuidarmais.demo.DTO.TaskDTO.TaskDTO;
import com.cuidarmais.demo.Entities.Task;
import com.cuidarmais.demo.Entities.EntityObjects.Enums.Status;
import com.cuidarmais.demo.Entities.EntityObjects.Enums.SupportType;
import com.cuidarmais.demo.Services.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/task")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @PostMapping("/save")
    public ResponseEntity<Object> saveTask(@RequestBody Task task) {

        return taskService.saveTask(task);
    }

    @GetMapping("/listAll")
    public List<TaskDTO> getAllTasks() {
        return taskService.listAll();
    }

    @GetMapping("/status-type-filter")
    public ResponseEntity<Object> getTasksStatusTypeFilter(@RequestParam(required = false) SupportType supportType, 
                                        @RequestParam (required = false) Status status) {
        return taskService.getStatusTypeFilter(supportType, status);
    }

    @GetMapping("/getTask")
    public ResponseEntity<Object> getTaskById(@RequestParam Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/updateStatus")
    public ResponseEntity<Object> updateTaskStatus(@RequestParam Long id, @RequestParam String button, @RequestParam (required = false) Long volunteerId) {
        return taskService.updateTaskStatus(id, button, volunteerId);
    }
    
    
    
}
