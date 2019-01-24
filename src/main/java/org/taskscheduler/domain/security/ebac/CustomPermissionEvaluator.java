package org.taskscheduler.domain.security.ebac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.taskscheduler.domain.entities.Task;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.enums.AuthorityName;
import org.taskscheduler.domain.services.TaskService;
import org.taskscheduler.domain.services.UserService;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private UserService userService;
    private TaskService taskService;

    public CustomPermissionEvaluator() { };

    @Autowired
    public CustomPermissionEvaluator (TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }


    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        //String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

        return hasPrivilege(auth, targetDomainObject, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {

        return hasPrivilege(auth, targetId,
                permission.toString().toUpperCase());
    }

    public boolean hasPrivilege(Authentication auth, Object target, String permission) {
        Task task = taskService.getById((long) target);
        User user = userService.getByUsername(auth.getName());
        //admin can do everything
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(AuthorityName.ROLE_ADMIN.toString())))
            return true;
        switch (permission) {
            case "TASK_OWNER" :
                if (!task.getCreator().equals(user) && (!task.getExecutors().contains(user)))
                    return false;
                break;

            case "TASK_CREATOR" :
                if (!task.getCreator().equals(user))
                    return false;
                break;
            default:
                return false;
        }
        return true;
    }
}
