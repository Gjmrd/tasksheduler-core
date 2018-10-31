package org.taskscheduler.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.entities.enums.Priority;
import org.taskscheduler.domain.entities.enums.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "tasks")
public class Task implements Serializable{

    private long id;
    private Status status;
    private Priority priority;
    private String caption;
    private String description;
    private User creator;
    private long creatorId;
    private List<User> executors;
    private User responsible;
    private CloseReason closeReason = CloseReason.NONE;
    private Date deadlineAt;
    private Date createdAt;
    private Date updatedAt;

    @Id
    @GeneratedValue(generator = "increment")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
    @JoinColumn(name = "creatorId", referencedColumnName = "id", insertable = false, updatable = false)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable
    @JsonIgnore
    public List<User> getExecutors() {
        return executors;
    }

    public void setExecutors(List<User> executors) {
        this.executors = executors;
    }

    @OneToOne( targetEntity = User.class)
    @JoinColumn( name = "responsibleId" ,referencedColumnName = "id")
    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
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


    @Enumerated(EnumType.STRING)
    @Column(name = "closeReason", nullable = false)
    @ColumnDefault("NONE")
    public CloseReason getCloseReason() {
        return closeReason == null ? CloseReason.NONE : closeReason;
    }

    public void setCloseReason(CloseReason closeReason) {
        this.closeReason = closeReason == null ? CloseReason.NONE : closeReason;
    }


    //cant create isClosed method
    public boolean closed() {
        return status == Status.CLOSED;
    }

    @Column(name = "creatorId")
    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
