package com.todoapp.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Sonu Verma on 2/6/2017.
 */
public class TodoModel implements Serializable {


    String id, name, type, description, status, dueDate, createdDate;

    public TodoModel( String name, String type, String description, String status, String dueDate, String createdDate) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type=type;
        this.dueDate = dueDate;
        this.createdDate = createdDate;
    }

    public TodoModel( String id,String name, String type, String description, String status, String dueDate, String createdDate) {
        this.id = id;
        this.name = name;
        this.type=type;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.createdDate = createdDate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
