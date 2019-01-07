package org.taskshedulertest.controllers;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.taskscheduler.Application;
import org.taskscheduler.domain.config.MethodSecurityConfig;
import org.taskscheduler.domain.config.WebSecurityConfig;


import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.enums.Status;
import org.taskscheduler.rest.controllers.TaskController;
import org.taskscheduler.services.TaskService;
import org.taskscheduler.services.UserService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class, MethodSecurityConfig.class})
public class ExampleMvcTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(userService, taskService)).build();
        Task taskMock = new Task();
        taskMock.setStatus(Status.FREEZED);
        taskMock.setCaption("sosi chlen");
        when(taskService.getById(anyLong())).thenReturn(taskMock);
    }

    @Test
    public void testServices() throws Exception {
        Assert.assertEquals(taskService.getById(1).getCaption(), "sosi chlen");

    }

    @Test
    public void testMvc() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk());
    }

}
