package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.exceptions.EntityNotFoundException;
import org.taskscheduler.domain.security.JwtUser;
import org.taskscheduler.services.TaskService;
import org.taskscheduler.services.UserService;
import org.taskscheduler.rest.dto.TaskDto;

import java.util.Date;

@RestController
public class TaskController {

    private TaskService taskService;
    private UserService userService;

    @Autowired
    public TaskController(UserService userService,
                          TaskService taskService) {
        this.taskService = taskService;
        this.userService = userService;
    }


    //todo commenting task changes

    @PostMapping("/tasks")
    public ResponseEntity<?> store(@AuthenticationPrincipal JwtUser userDetails, @RequestBody TaskDto request) throws Exception{
        Task task = taskService.createNew(userDetails.getId(), request);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasPermission(#taskId, 'TASK_CREATOR')")
    @PostMapping("/tasks/{id}/freeze")
    public ResponseEntity<?> freeze(@AuthenticationPrincipal JwtUser userDetails,
                                    @PathVariable("id") long taskId,
                                    @RequestParam(name = "comment", required = false) String comment) {
        Task task = taskService.getById(taskId);
        if (task == null)
            throw new EntityNotFoundException("Invalid Task Id");
        taskService.freeze(userDetails.getId(), task);
        return ResponseEntity.ok("ok");
    }


    @PreAuthorize("hasPermission(#id, 'TASK_OWNER')")
    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") long id) {
        Task task = taskService.getById(id);
        if (task == null)
            throw new EntityNotFoundException("Invalid Task Id");
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasPermission(#id, 'TASK_OWNER')")
    @GetMapping("/task/{id}/executors")
    public ResponseEntity<?> getExecutorsByTaskId(@PathVariable("id") long id, Pageable pageable) {
        return ResponseEntity.ok(taskService.getExecutorsByTaskId(id, pageable));
    }

    @GetMapping("/tasks/all")
    public ResponseEntity<?> getMyTasks(@AuthenticationPrincipal JwtUser userDetails,
                                        @RequestParam(name = "from", required = false) Date from,
                                        @RequestParam(name = "to", required = false) Date to,
                                        Pageable pageable) {
        return ResponseEntity.ok(taskService.getUsersTasks(from, to, userService.getByUsername(userDetails.getUsername()), pageable));
    }

    @GetMapping("tasks/created")
    public ResponseEntity<?> getCreatedTasks(@AuthenticationPrincipal JwtUser userDetails,
                                             @RequestParam(name = "from", required = false) Date from,
                                             @RequestParam(name = "to", required = false) Date to,
                                             Pageable pageable) {
        return ResponseEntity.ok(taskService.getCreatedBetween(from, to, userDetails.getId(), pageable));
    }


    @PreAuthorize("hasPermission(#taskId, 'TASK_OWNER')")
    @PostMapping("/tasks/{id}/complete")
    public ResponseEntity<?> completeTask(@AuthenticationPrincipal JwtUser userDetails,
                                          @PathVariable("id") long taskId) {
        Task task = taskService.getById(taskId);
        if (task == null)
            throw new EntityNotFoundException("Invalid Task Id");
        taskService.close(userDetails.getId(), task, CloseReason.COMPLETED);
        //todo send notifications

        return ResponseEntity.ok("ok");
    }


    //todo implement all request select parameters

}
