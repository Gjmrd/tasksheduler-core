package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.exceptions.EntityNotFoundException;
import org.taskscheduler.services.TaskService;
import org.taskscheduler.services.UserService;
import org.taskscheduler.rest.dto.TaskDto;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

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

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ResponseEntity<?> store(@AuthenticationPrincipal UserDetails user, @RequestBody TaskDto request) throws Exception{
        Task task = taskService.createNew(userService.getByUsername(user.getUsername()), request);
        //todo async logging
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasPermission(#taskId, 'TASK_CREATOR')")
    @RequestMapping(value = "/task/freeze", method = RequestMethod.POST)
    public ResponseEntity<?> freeze(@AuthenticationPrincipal UserDetails userDetails, @RequestParam long taskId) {
        Task task = taskService.getById(taskId);
        if (task == null)
            throw new EntityNotFoundException("Invalid Task Id");
        taskService.freeze(task);
        return ResponseEntity.ok("ok");
    }

    @PreAuthorize("hasPermission(#id, 'TASK_OWNER')")
    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ResponseEntity<?> getTask(@RequestParam("id") long id) {
        Task task = taskService.getById(id);
        if (task == null)
            throw new EntityNotFoundException("Invalid Task Id");
        return ResponseEntity.ok(task);
    }

    @RequestMapping(value = "/task/all", method = RequestMethod.GET)
    public ResponseEntity<?> getMyTasks(@AuthenticationPrincipal  UserDetails userDetails,
                                        @RequestParam(name = "createdFrom", required = false) Date createdForm,
                                        @RequestParam(name = "createdTo", required = false) Date createdTo,
                                        Pageable pageable) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(taskService.getUsersTasks(user, pageable));
    }

    @RequestMapping(value = "task/created", method = RequestMethod.GET)
    public ResponseEntity<?> getCreatedTasks(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestParam(name = "createdFrom", required = false) Date createdForm,
                                             @RequestParam(name = "createdTo", required = false) Date createdTo,
                                             Pageable pageable) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(taskService.getCreatedBetween(createdForm, createdTo, user, pageable));
    }


    @PreAuthorize("hasPermission(#taskId, 'TASK_OWNER')")
    @RequestMapping(value = "/task/complete", method = RequestMethod.POST)
    public ResponseEntity<?> completeTask(@RequestParam("id") long taskId) {
        Task task = taskService.getById(taskId);
        if (task == null)
            throw new EntityNotFoundException("Invalid Task Id");
        taskService.close(task, CloseReason.COMPLETED);
        //todo send notifications

        return ResponseEntity.ok("ok");
    }


    //todo implement all request select parameters

}
