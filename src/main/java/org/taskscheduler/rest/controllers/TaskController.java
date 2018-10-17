package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.bind.annotation.*;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.AuthorityName;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.entities.enums.Status;
import org.taskscheduler.domain.services.TaskService;
import org.taskscheduler.domain.services.UserService;
import org.taskscheduler.rest.controllers.dto.TaskDto;

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
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasPermission(#taskId, 'TASK_CREATOR')")
    @RequestMapping(value = "/task/freeze", method = RequestMethod.POST)
    public ResponseEntity<?> freeze(@AuthenticationPrincipal UserDetails userDetails, @RequestParam long taskId) {
        Task task = taskService.getById(taskId);
        if (task == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid task ID");
        taskService.freeze(task);
        return ResponseEntity.ok("ok");
    }

    @PreAuthorize("hasPermission(#id, 'TASK_OWNER')")
    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ResponseEntity<?> getTask(@RequestParam("id") int id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @RequestMapping(value = "/task/all", method = RequestMethod.GET)
    public ResponseEntity<?> getMyTasks(@AuthenticationPrincipal  UserDetails userDetails, Pageable pageable) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(taskService.getUsersTasks(user, pageable));
    }

    @RequestMapping(value = "task/created", method = RequestMethod.GET)
    public ResponseEntity<?> getCreatedTasks(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(taskService.getCreated(user, pageable));
    }

    @PreAuthorize("hasPermission(#taskId, 'TASK_OWNER')")
    @RequestMapping(value = "/task/complete", method = RequestMethod.POST)
    public ResponseEntity<?> completeTask(@RequestParam("id") long taskId) {
        Task task = taskService.getById(taskId);
        if (task == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid task ID");

        taskService.close(task, CloseReason.COMPLETED);
        //todo send notifications
        //todo: paginate results
        return ResponseEntity.ok("ok");
    }

}
