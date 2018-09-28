package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.services.AsyncTaskService;
import org.taskscheduler.domain.services.AsyncUserService;

import java.util.List;


@RestController
public class HomeController {

    private AsyncUserService asyncUserService;
    private AsyncTaskService asyncTaskService;

    @Autowired
    public HomeController(AsyncUserService _Async_userService,
                          AsyncTaskService _asyncTaskService) {
        asyncUserService = _Async_userService;
        asyncTaskService = _asyncTaskService;
    }

    @RequestMapping(value = "/qwe", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() throws Exception{
        Task task = new Task();
        task.setCaption("waqdwqdqdwqd");
        asyncTaskService.save(task);
        throw new Exception("checking exceptions");

        //todo complete controllers

    }


    @RequestMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        User user = new User();
        return "it works " + Long.toString(user.getId());
    }
}
