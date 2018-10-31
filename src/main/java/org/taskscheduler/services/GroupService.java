package org.taskscheduler.services;

import org.springframework.data.domain.Pageable;
import org.taskscheduler.domain.entities.Group;
import org.taskscheduler.domain.entities.PageInfo;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.rest.dto.GroupDto;


public interface GroupService {
    Group getById(long id);
    Group addUserToGroup(Group group, User user);
    Group removeUserFromGroup(Group group, User user);
    Group create(GroupDto groupDto) throws Exception;
    PageInfo<User> getMembersByGroupId(long id, Pageable pageable);
}

