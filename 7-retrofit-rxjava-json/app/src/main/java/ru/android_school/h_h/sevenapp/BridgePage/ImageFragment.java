package ru.android_school.h_h.sevenapp.BridgePage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ru.android_school.h_h.sevenapp.R;

public class ImageFragment extends Fragment {

    private String URL;

    public static ImageFragment newInstance(String URL) {
        Log.i("ImageFragment","Instance was created, url:"+URL);
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.URL = URL;
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bridge_page_image, container, false);
        Glide.with(this)
                .load(URL)
                .into(((ImageView) view.findViewById(R.id.bridge_image)));
        return view;
    }
}
