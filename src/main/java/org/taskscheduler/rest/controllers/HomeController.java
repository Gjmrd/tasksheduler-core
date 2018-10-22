package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.services.TaskService;
import org.taskscheduler.services.UserService;


@RestController
public class HomeController {

    private UserService userService;
    private TaskService taskService;

    @Autowired
    public HomeController(UserService userService,
                          TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @RequestMapping(value = "/qwe", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() throws Exception{
        Task task = new Task();
        task.setCaption("waqdwqdqdwqd");
        taskService.save(task);
        throw new Exception("checking exceptions");

        //todo complete controllers

    }


    @RequestMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        User user = new User();
        return "it works " + Long.toString(user.getId());
    }
}
