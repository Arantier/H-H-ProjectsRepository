package ru.androidschool.h_h.sixthapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Page extends Fragment {

    private static final String ARGUMENT_PAGE_NUMBER = "page_number";

    public static Page newInstance(int position) {
        Page page = new Page();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER,position);
        page.setArguments(arguments);
        return page;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View page = inflater.inflate(R.layout.fragment_page, null);
        ImageView picture =  page.findViewById(R.id.image_picture);
        TextView title = page.findViewById(R.id.text_pageTitle);
        switch (getArguments().getInt(ARGUMENT_PAGE_NUMBER)) {
            case 0:
                picture.setImageResource(R.drawable.backg1);
                title.setText("Картинка 1");
                break;
            case 1:
                picture.setImageResource(R.drawable.backg2);
                title.setText("Картинка 2");
                break;
            case 2:
                picture.setImageResource(R.drawable.backg3);
                title.setText("Картинка 3");
                break;
        }
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),((TextView)getView().findViewById(R.id.text_pageTitle)).getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        return page;
    }
    //Почему происходят фризы при перелистывании?
}
