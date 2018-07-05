package com.example.tanvigupta.todolist3;

public class Note {
    String title;
    String description;
    String date;
    String time;
    String category;
    long id;

    public Note(String title, String description, String date, String time,String category) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
