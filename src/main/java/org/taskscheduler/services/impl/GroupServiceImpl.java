package org.taskscheduler.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.taskscheduler.domain.entities.Group;
import org.taskscheduler.domain.entities.PageInfo;
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

    private Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PageInfo<User> getMembersByGroupId(long id, Pageable pageable) {
        return new PageInfo<>(groupRepository.getMembersByGroupId(id, pageable));
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
                .thenAccept(group::setMembers);
        group.setName(groupDto.getName());
        future.get();
        groupRepository.save(group);
        logger.info("group %s has been created", group.getName());
        return group;
    }



    @Override
    public Group addUserToGroup(Group group, User user) {
        group.getMembers().add(user);
        logger.info("user %s has been added to group %s", user.getUsername(), group.getName());
        return groupRepository.save(group);
    }

    @Override
    public Group removeUserFromGroup(Group group, User user) {
        group.getMembers().remove(group);
        logger.info("user %s has been removed from group %s", user.getUsername(), group.getName());
        return groupRepository.save(group);
    }


}
