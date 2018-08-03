package ru.androidschool.h_h.eightapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.widget.TextView;

public class NoteLayout extends CardView {

    TextView titleView;
    TextView textView;

    public NoteLayout(@NonNull Context context) {
        super(context);
    }

    public void bind(String title, String text, int color){
        titleView = findViewById(R.id.noteTitle);
        textView = findViewById(R.id.noteText);
        this.setBackgroundColor(color);
        int textColor;
        if (color!=0xFFFFFF){
            textColor = 0xFFFFFF;
        } else {
            textColor = 0x000000;
        }
        titleView.setText(title);
        titleView.setTextColor(textColor);
        textView.setText(text);
        textView.setTextColor(textColor);
    }
}
