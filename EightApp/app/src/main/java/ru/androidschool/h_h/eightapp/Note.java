package ru.androidschool.h_h.eightapp;

public class Note {

    public String title;
    public String text;
    public int backgroundColor;

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
        backgroundColor = 0xFFFFFF;
    }

    public Note(String title, String text, int backgroundColor){
        this(title,text);
        this.backgroundColor = backgroundColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color){
        backgroundColor = color;
    }
}
