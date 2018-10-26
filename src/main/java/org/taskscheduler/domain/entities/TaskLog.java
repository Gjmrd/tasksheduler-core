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
    private long subjectId;
    private Task task;
    private String field;
    private String oldValue;
    private String newValue;
    private Date date;

    public TaskLog(long subjectId, Task task, String field, String oldValue, String newValue) {
        this.subjectId = subjectId;
        this.task = task;
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.date = new Date();
    }

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


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateTime")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "field")
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Column(name = "oldValue")
    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    @Column(name = "newValue")
    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Column(name = "subjectId")
    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }
}
