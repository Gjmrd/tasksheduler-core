package org.taskscheduler.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.taskscheduler.domain.entities.Group;
import org.taskscheduler.domain.exceptions.EntityNotFoundException;
import org.taskscheduler.rest.dto.GroupDto;
import org.taskscheduler.services.GroupService;


@RestController
public class GroupController {

    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @RequestMapping(name = "group/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getGroupById(@PathVariable("id") long id) {
        Group group = groupService.getById(id);
        if (group == null)
            throw new EntityNotFoundException("Invalid Group Id");

        return ResponseEntity.ok(group);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(name = "group", method = RequestMethod.POST)
    ResponseEntity<?> store(@RequestBody GroupDto groupDto) throws Exception{
        Group group = groupService.create(groupDto);
        return ResponseEntity.ok(group);
    }
}
