package com.toanutc.todolist;

public class ToDo {

    private int id;
    private Boolean check;
    private String name;
    private String time;
    private String categories;
    private String description;
    private int imageAlert;

    ToDo() {
    }

    ToDo(int id, Boolean check, String name, String time, String categories, String description, int imageAlert) {
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

    Boolean getCheck() {
        return check;
    }

    void setCheck(Boolean check) {
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

    String getCategories() {
        return categories;
    }

    void setCategories(String categories) {
        this.categories = categories;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    int getImageAlert() {
        return imageAlert;
    }

    void setImageAlert(int imageAlert) {
        this.imageAlert = imageAlert;
    }
}
