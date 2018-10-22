package org.taskscheduler.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.taskscheduler.domain.entities.enums.CloseReason;
import org.taskscheduler.domain.entities.enums.Status;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "taskLogs")
public class TaskLog {
    private long id;
    private User subject;
    private Task task;
    private Status status;
    private CloseReason closeReason;
    private Date date;

    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "subjectId", referencedColumnName = "id")
    public User getSubject() {
        return subject;
    }

    public void setSubject(User subject) {
        this.subject = subject;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(name = "closeReason")
    @Enumerated(EnumType.STRING)
    public CloseReason getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(CloseReason closeReason) {
        this.closeReason = closeReason;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateTime")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
