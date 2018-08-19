package ru.android_school.h_h.sevenapp.MainActivity.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeDatabase;
import ru.android_school.h_h.sevenapp.BridgePage.BridgePageActivity;
import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.MainActivity.RecyclerViewAdapter;
import ru.android_school.h_h.sevenapp.R;

public class ListFragment extends Fragment {

    ArrayList<Bridge> listOfBridges;
    BridgeDatabase database;

    public static ListFragment newInstance(ArrayList<Bridge> receivedList) {
        ListFragment list = new ListFragment();
        list.listOfBridges = receivedList;
        return list;
    }

    public static ListFragment newInstance(BridgeDatabase database){
        ListFragment list = new ListFragment();
        list.database = database;
        list.database
                .bridgeDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Bridge>>() {
                    @Override
                    public void accept(ArrayList<Bridge> bridges) throws Exception {
                        list.listOfBridges = bridges;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        return list;
    }

    public void launchBridgeInfo(Bridge bridge) {
        Intent intent = new Intent(getContext(), BridgePageActivity.class);
        intent.setAction(BridgePageActivity.START_WITH_BRIDGE);
        intent.putExtra(BridgePageActivity.BRIDGE_INTENT, bridge);
        startActivity(intent);
    }

    public void launchBridgeInfo(int bridgeId) {
        Intent intent = new Intent(getContext(),BridgePageActivity.class);
        intent.setAction(BridgePageActivity.START_WITH_ID);
        intent.putExtra(BridgePageActivity.BRIDGE_ID_INTENT,bridgeId);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView list = view.findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(ListFragment.this.getContext()));
        list.setAdapter(new RecyclerViewAdapter(listOfBridges, bridge -> launchBridgeInfo(bridge.getId())));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
