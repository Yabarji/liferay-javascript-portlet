package com.hannikkala.jsliferay.rest.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 25/02/16
 * Time: 14:36
 */
@Entity
public class Todo {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Column
    private boolean done;

    @Column
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
