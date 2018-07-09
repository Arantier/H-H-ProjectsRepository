package ru.androidschool.h_h.sixthapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment1 extends Fragment {

    public static final String TAG = "fragment1";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Вижу что ругается, не понимаю как это исправить. Если подсунуть вместо рута, логично, container, то вылетает
        //oшибка, мол, контейнер уже имеет чайлда
        return inflater.inflate(R.layout.fragment1, null);
    }

}
