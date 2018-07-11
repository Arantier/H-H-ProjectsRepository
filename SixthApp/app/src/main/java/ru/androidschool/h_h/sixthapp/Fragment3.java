package ru.androidschool.h_h.sixthapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class Fragment3 extends Fragment {

    public static final String TAG = "fragment3";

    private Banner banner;

    public static Fragment3 newInstance() {
        return new Fragment3();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        banner = Banner.newInstance();
        View bannerView = inflater.inflate(R.layout.fragment3, container, false);
        ToggleButton bannerTrigger = bannerView.findViewById(R.id.button_controlBanner);
        bannerTrigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                if (b) {
                    transaction.add(R.id.layout_viewPagerContainer, banner);
                } else {
                    transaction.remove(banner);
                }
                transaction.commit();
            }
        });
        return bannerView;
    }
}
