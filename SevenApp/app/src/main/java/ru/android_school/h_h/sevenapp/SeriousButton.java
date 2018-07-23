package ru.android_school.h_h.sevenapp;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class SeriousButton extends Observable{

    private ArrayList<Observer> listOfSubscribers;
    private TextView title;
    private Context context;

    public SeriousButton(View view, Context context){
        title = view.findViewById(R.id.reminderButtonText);
        ((ImageView) view.findViewById(R.id.reminderButtonBell)).setImageResource(R.drawable.ic_bell_white);
        listOfSubscribers = new ArrayList<>();
    }

    @Override
    protected void subscribeActual(Observer observer) {

    }
}
