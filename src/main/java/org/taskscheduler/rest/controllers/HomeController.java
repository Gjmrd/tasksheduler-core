package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.services.AsyncUserService;




@RestController
public class HomeController {

    private AsyncUserService asyncUserService;

    @Autowired
    public HomeController(AsyncUserService _Async_userService) {
        asyncUserService = _Async_userService;
    }

    @RequestMapping(value = "/qwe", produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() throws Exception{
        User user = asyncUserService.getUserById(1).get();
        user.setNickname("Test");
        asyncUserService.save(user);
        throw new Exception("checking exceptions");

    }


    @RequestMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        User user = new User();
        user.setNickname("gjmrd");
        return "it works " + Integer.toString(user.getId());
    }
}
