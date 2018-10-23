package org.taskscheduler.services;

import org.taskscheduler.domain.entities.Group;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.rest.dto.GroupDto;

public interface GroupService {
    Group getById(long id);
    Group addUserToGroup(Group group, User user);
    Group removeUserFromGroup(Group group, User user);
    Group create(GroupDto groupDto) throws Exception;
}

