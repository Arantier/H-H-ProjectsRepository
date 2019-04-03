package ru.android_school.h_h.sevenapp.MainActivity.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.android_school.h_h.sevenapp.R;

public class ErrorFragment extends Fragment {

    public interface Refreshable{
        public void refresh();
    }

    //Не очень понимаю как это сделать если честно
    private Refreshable refreshable;

    public ErrorFragment() {
        // Required empty public constructor
    }

    public static ErrorFragment newInstance() {
        ErrorFragment fragment = new ErrorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        view.findViewById(R.id.button_errorRefresh)
                .setOnClickListener(view1 -> {

                });
        return view;
    }

}
