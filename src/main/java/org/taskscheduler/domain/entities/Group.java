package org.taskscheduler.domain.entities;

import org.taskscheduler.domain.interfaces.Executor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group implements Executor{

    private int id;
    private List<User> members;
    private String name;

    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "group")
    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
