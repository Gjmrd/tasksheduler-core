package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.taskscheduler.domain.services.AsyncTaskService;
import org.taskscheduler.domain.services.AsyncUserService;
import org.taskscheduler.rest.controllers.dto.TaskDto;

@RestController
public class TaskController {

    private AsyncTaskService asyncTaskService;
    private AsyncUserService asyncUserService;

    @Autowired
    public TaskController(AsyncUserService asyncUserService,
                          AsyncTaskService asyncTaskService) {
        this.asyncTaskService = asyncTaskService;
        this.asyncUserService = asyncUserService;
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public String createTask(@RequestBody TaskDto request) {
        //todo
        return "1234";
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ResponseEntity<?> getTask(@RequestParam("id") int id) throws Exception {
        return ResponseEntity.ok(asyncTaskService.getById(id).get());
    }

}
