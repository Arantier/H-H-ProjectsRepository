package ru.androidschool.h_h.sixthapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Banner extends Fragment {

    private static final int PAGE_COUNT = 3;

    public static Banner newInstance(){
        return new Banner();
    }

    class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return Page.newInstance(R.drawable.img_1,"Картинка 1");
                case 1:
                    return Page.newInstance(R.drawable.img_2,"Картинка 2");
                case 2:
                    return Page.newInstance(R.drawable.img_3,"Картинка 3");
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View banner = inflater.inflate(R.layout.fragment_viewpager, container,false);
        ViewPager viewPager = banner.findViewById(R.id.viewPager);
        MyAdapter adapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        return banner;
    }
}
