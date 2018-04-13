package com.toanutc.todolist;

/**
 * Created by sev_user on 09-Apr-18.
 */

public class ToDo {

    private int id;
    private Boolean check;
    private String name;
    private String time;
    private String categories;
    private String description;
    private int imageAlert;

    public ToDo() {
    }

    public ToDo(int id, Boolean check, String name, String time, String categories, String description, int imageAlert) {
        this.id = id;
        this.check = check;
        this.name = name;
        this.time = time;
        this.categories = categories;
        this.description = description;
        this.imageAlert = imageAlert;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageAlert() {
        return imageAlert;
    }

    public void setImageAlert(int imageAlert) {
        this.imageAlert = imageAlert;
    }
}
