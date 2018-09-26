package org.taskscheduler.domain.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.taskscheduler.domain.interfaces.Executor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "tasks")
public class Task {

    private int id;
    private Type type;
    private Priority priority;
    private String caption;
    private String description;
    private User creator;
    private List<User> executors;
    private Executor responsible;
    private Date deadlineAt;
    private Date completedAt;
    private Date createdAt;
    private Date updatedAt;

    @Id
    @GeneratedValue(generator = "increment")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne(targetEntity = Type.class)
    @JoinColumn( name = "typeId", referencedColumnName = "id")
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Column(name = "caption")
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @ManyToMany
    @JoinTable
    public List<User> getExecutors() {
        return executors;
    }

    public void setExecutors(List<User> executors) {
        this.executors = executors;
    }

    @OneToOne( targetEntity = User.class)
    @JoinColumn( name = "responsibleId" ,referencedColumnName = "id")
    public Executor getResponsible() {
        return responsible;
    }

    public void setResponsible(Executor responsible) {
        this.responsible = responsible;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deadlineAt")
    public Date getDeadlineAt() {
        return deadlineAt;
    }

    public void setDeadlineAt(Date deadlineAt) {
        this.deadlineAt = deadlineAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "completedAt")
    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updatedAt")
    public Date getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


}
