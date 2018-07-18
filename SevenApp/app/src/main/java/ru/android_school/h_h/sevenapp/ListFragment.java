package ru.android_school.h_h.sevenapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;

public class ListFragment extends Fragment {

    ArrayList<Bridge> listOfBridges;

    public static ListFragment newInstance() {
        ListFragment list = new ListFragment();
        return list;
    }

    public void replaceFragment(ViewGroup container,Bridge bridge){
        Intent intent = new Intent(getContext(), BridgePage.class);
        intent.putExtra(BridgePage.BRIDGE_TAG,bridge);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bridge_list, container, false);
        RecyclerView list = view.findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list.setAdapter(new ListAdapter(listOfBridges, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Bridge bridge) {
                replaceFragment(container,bridge);
            }
        }));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeInterval Interval1, Interval2, Interval3, Interval4, Interval5, Interval6;
        try {
            Interval1 = new TimeInterval("18:00", "20:00");
            Interval2 = new TimeInterval("15:00", "16:00");
            Interval3 = new TimeInterval("21:00", "23:00");
            Interval4 = new TimeInterval("20:00","9:00");
            Interval5 = new TimeInterval("21:00","23:00");
            Interval6 = new TimeInterval("17:00","9:00");
            listOfBridges = new ArrayList();
            listOfBridges.add(new Bridge(0, "Отладочный мост", "Мост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\nМост\nХост\n", "http://gdemost.handh.ru/photos/Alexandra_Nevskogo_closed.png", "http://gdemost.handh.ru/photos/Birzhevoy_closed.png", false, Interval1));
            listOfBridges.add(new Bridge(0, "Отладочный мост", "Не паттерн, ни северный, ни южный, ни разводной - просто отладочный мост", "http://gdemost.handh.ru/photos/Alexandra_Nevskogo_closed.png", "http://gdemost.handh.ru/photos/Birzhevoy_closed.png", true, Interval2));
            listOfBridges.add(new Bridge(0, "Отладочный мост", "Не паттерн, ни северный, ни южный, ни разводной - просто отладочный мост", "http://gdemost.handh.ru/photos/Alexandra_Nevskogo_closed.png", "http://gdemost.handh.ru/photos/Birzhevoy_closed.png", true, Interval3));
            listOfBridges.add(new Bridge(0, "Отладочный мост", "Не паттерн, ни северный, ни южный, ни разводной - просто отладочный мост", "http://gdemost.handh.ru/photos/Alexandra_Nevskogo_closed.png", "http://gdemost.handh.ru/photos/Birzhevoy_closed.png", true, Interval4));
            listOfBridges.add(new Bridge(0, "Отладочный мост", "Не паттерн, ни северный, ни южный, ни разводной - просто отладочный мост", "http://gdemost.handh.ru/photos/Alexandra_Nevskogo_closed.png", "http://gdemost.handh.ru/photos/Birzhevoy_closed.png", true, Interval5));
            listOfBridges.add(new Bridge(0, "Отладочный мост", "Не паттерн, ни северный, ни южный, ни разводной - просто отладочный мост", "http://gdemost.handh.ru/photos/Alexandra_Nevskogo_closed.png", "http://gdemost.handh.ru/photos/Birzhevoy_closed.png", true, Interval6));
        } catch (TimeInterval.TimeIntervalInputException e) {
            e.printStackTrace();
        }
    }
}
