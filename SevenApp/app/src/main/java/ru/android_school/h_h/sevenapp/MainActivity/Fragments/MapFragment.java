package ru.android_school.h_h.sevenapp.MainActivity.Fragments;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;

public class MapFragment extends Fragment {

    ArrayList<Bridge> listOfBridges;

    public static MapFragment newInstance(ArrayList<Bridge> listOfBridges) {
        MapFragment instance = new MapFragment();
        instance.listOfBridges = listOfBridges;
        return instance;
    }
}
