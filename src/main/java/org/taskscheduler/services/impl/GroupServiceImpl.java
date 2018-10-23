package org.taskscheduler.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.Group;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.repositories.GroupRepository;
import org.taskscheduler.domain.repositories.UserRepository;
import org.taskscheduler.rest.dto.GroupDto;
import org.taskscheduler.services.GroupService;

import java.util.concurrent.CompletableFuture;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupRepository groupRepository;
    private UserRepository userRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Group getById(long id) {
        return groupRepository.findById(id).orElse(null);
    }

    @Override
    public Group create(GroupDto groupDto) throws Exception{
        Group group = new Group();
        CompletableFuture future = CompletableFuture
                .supplyAsync(() -> userRepository.findByUsernameArray(groupDto.getMembers()))
                .thenAccept(members -> group.setMembers(members));
        group.setName(groupDto.getName());
        future.get();
        return groupRepository.save(group);
    }

    @Override
    public Group addUserToGroup(Group group, User user) {
        group.getMembers().add(user);
        return groupRepository.save(group);
    }

    @Override
    public Group removeUserFromGroup(Group group, User user) {
        group.getMembers().remove(group);
        return groupRepository.save(group);
    }


}
