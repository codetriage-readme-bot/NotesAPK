package com.gmail.lusersks.notes.data;

public class NotesItem {
    private int id;
    private String title, content;
    private String type;

    public NotesItem(int id, String title, String content, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }
}
