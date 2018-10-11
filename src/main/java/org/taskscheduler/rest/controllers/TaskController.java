package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public String createTask(@RequestBody TaskDto request) {
        //todo
        return "1234";
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ResponseEntity<?> getTask(@RequestParam("id") int id) throws Exception {
        return ResponseEntity.ok(taskService.getById(id));
    }

}
