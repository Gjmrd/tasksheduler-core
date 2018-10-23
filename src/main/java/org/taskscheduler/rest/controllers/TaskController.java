package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ResponseEntity<?> store(@AuthenticationPrincipal JwtUser userDetails, @RequestBody TaskDto request) throws Exception{
        Task task = taskService.createNew(userDetails.getUserEntity(), request);
        //todo async logging
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasPermission(#taskId, 'TASK_CREATOR')")
    @RequestMapping(value = "/task/freeze", method = RequestMethod.POST)
    public ResponseEntity<?> freeze(@AuthenticationPrincipal JwtUser userDetails,
                                    @RequestParam long taskId) {
        Task task = taskService.getById(taskId);
        if (task == null)
            throw new EntityNotFoundException("Invalid Task Id");
        taskService.freeze(userDetails.getUserEntity(), task);
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
    public ResponseEntity<?> getMyTasks(@AuthenticationPrincipal JwtUser userDetails,
                                        @RequestParam(name = "createdFrom", required = false) Date createdForm,
                                        @RequestParam(name = "createdTo", required = false) Date createdTo,
                                        Pageable pageable) {
        //return ResponseEntity.ok(userDetails.getUserEntity());
        return ResponseEntity.ok(taskService.getUsersTasks(userDetails.getUserEntity(), pageable));
    }

    @RequestMapping(value = "task/created", method = RequestMethod.GET)
    public ResponseEntity<?> getCreatedTasks(@AuthenticationPrincipal JwtUser userDetails,
                                             @RequestParam(name = "createdFrom", required = false) Date createdForm,
                                             @RequestParam(name = "createdTo", required = false) Date createdTo,
                                             Pageable pageable) {
        return ResponseEntity.ok(taskService.getCreatedBetween(createdForm, createdTo, userDetails.getUserEntity(), pageable));
    }


    @PreAuthorize("hasPermission(#taskId, 'TASK_OWNER')")
    @RequestMapping(value = "/task/complete", method = RequestMethod.POST)
    public ResponseEntity<?> completeTask(@AuthenticationPrincipal JwtUser userDetails,
                                          @RequestParam("id") long taskId) {
        Task task = taskService.getById(taskId);
        if (task == null)
            throw new EntityNotFoundException("Invalid Task Id");
        taskService.close(userDetails.getUserEntity(), task, CloseReason.COMPLETED);
        //todo send notifications

        return ResponseEntity.ok("ok");
    }


    //todo implement all request select parameters

}
