package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/task/freeze", method = RequestMethod.POST)
    public ResponseEntity<?> freeze(@AuthenticationPrincipal UserDetails userDetails)

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ResponseEntity<?> getTask(@RequestParam("id") int id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @RequestMapping(value = "/task/complete", method = RequestMethod.POST)
    public ResponseEntity<?> completeTask(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("id") long taskId) {
        Task task = taskService.getById(taskId);
        User user = userService.getByUsername(userDetails.getUsername());
        if (task == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid task ID");

        if ((task.getCreator() != user) && (!task.getExecutors().contains(user)))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        taskService.close(task, CloseReason.COMPLETED);
        //todo send notifications
        return ResponseEntity.ok("ok");
    }


    //todo spring attribute based acess control
}
