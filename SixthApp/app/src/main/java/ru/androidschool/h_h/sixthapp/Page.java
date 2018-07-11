package ru.androidschool.h_h.sixthapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

    private static final String ARGUMENT_PAGE_PICTURE = "page_picture";
    private static final String ARGUMENT_PAGE_PICTURE_ID = "page_picture_id";
    private static final String ARGUMENT_PAGE_TITLE = "page_title";

    //Просто решил попробовать передать Drawable без посредников.
    //Видимо, у меня пока что маловато опыта чтобы понять почему Drawable при
    //превращении в BitmapDrawable и обратно сжимается до невероятных размеров
    public static Page newInstance(Drawable picture, String title) {
        Page page = new Page();
        Bundle arguments = new Bundle();
        Bitmap convertedPicture = ((BitmapDrawable) picture).getBitmap();
        arguments.putParcelable(ARGUMENT_PAGE_PICTURE,convertedPicture);
        arguments.putString(ARGUMENT_PAGE_TITLE,title);
        page.setArguments(arguments);
        return page;
    }

    public static Page newInstance(int pictureId, String title) {
        Page page = new Page();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_PICTURE_ID,pictureId);
        arguments.putString(ARGUMENT_PAGE_TITLE,title);
        page.setArguments(arguments);
        return page;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View page = inflater.inflate(R.layout.fragment_page, container,false);
        ImageView picture =  page.findViewById(R.id.image_picture);
        TextView title = page.findViewById(R.id.text_pageTitle);
        if (getArguments().getParcelable(ARGUMENT_PAGE_PICTURE)!=null) {
            Drawable receivedPicture = new BitmapDrawable((Bitmap) getArguments().getParcelable(ARGUMENT_PAGE_PICTURE));
            picture.setImageDrawable(receivedPicture);
        } else {
            picture.setImageResource(getArguments().getInt(ARGUMENT_PAGE_PICTURE_ID));
        }
        title.setText(getArguments().getString(ARGUMENT_PAGE_TITLE));
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),((TextView)getView().findViewById(R.id.text_pageTitle)).getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        return page;
    }
}
