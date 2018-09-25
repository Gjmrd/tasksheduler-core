package org.taskscheduler.domain.entities;

import javax.persistence.*;

@Entity
@Table(name="types")
public class Type {

    private int id;
    private String description;

    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
