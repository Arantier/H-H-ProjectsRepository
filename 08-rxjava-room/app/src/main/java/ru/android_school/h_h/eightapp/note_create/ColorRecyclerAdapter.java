package ru.android_school.h_h.eightapp.note_create;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import ru.android_school.h_h.eightapp.R;
import ru.android_school.h_h.eightapp.note_trio.Note;

public class ColorRecyclerAdapter extends RecyclerView.Adapter<ColorRecyclerAdapter.ColorHolder> {

    Note noteUnderEdit;
    Context currentContext;
    ColorHolder selectedColor;

    int[] availableColors = {
            R.color.red, R.color.pink, R.color.purple, R.color.deepPurple,
            R.color.blue, R.color.lightBlue, R.color.biruseBlue, R.color.seaGreen,
            R.color.green, R.color.grassGreen, R.color.yellow, R.color.orange,
            R.color.orangeRed, R.color.grey, R.color.blueGrey, R.color.white
    };

    public class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int selectedColorInt;
        int selectedColorResource;
        ImageView circle;
        ImageView selectIndicator;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            circle = itemView.findViewById(R.id.color_circle);
            selectIndicator = itemView.findViewById(R.id.select_indicator);
            itemView.setOnClickListener(this);
        }

        public void bind(int selectedColorInt, int selectedColorResource) {
            this.selectedColorInt = selectedColorInt;
            this.selectedColorResource = selectedColorResource;
            circle.getDrawable()
                    .mutate()
                    .setColorFilter(selectedColorInt, PorterDuff.Mode.SRC_IN);
            if (noteUnderEdit.backgroundColorResource==selectedColorResource) {
                selectedColor = this;
                selectIndicator.setVisibility(View.VISIBLE);
                if (selectedColorResource==R.color.white){
                    selectIndicator.getDrawable()
                            .mutate()
                            .setColorFilter(Color.BLACK,PorterDuff.Mode.SRC_IN);
                }
            }
        }

        @Override
        public void onClick(View view) {
            selectedColor.selectIndicator.setVisibility(View.GONE);
            selectedColor = this;
            selectedColor.selectIndicator.setVisibility(View.VISIBLE);
            noteUnderEdit.backgroundColorResource = selectedColorResource;
        }
    }

    public ColorRecyclerAdapter(Note noteUnderEdit, Context currentContext) {
        this.noteUnderEdit = noteUnderEdit;
        this.currentContext = currentContext;
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i("ColorAdapter","onCreateVH triggered");
        return new ColorHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.imageview_color_circle, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder colorHolder, int i) {
        Log.i("ColorAdapter","onBindVH triggered");
        colorHolder.bind(currentContext.getResources().getColor(availableColors[i]),availableColors[i]);
    }

    @Override
    public int getItemCount() {
        Log.i("ColorAdapter","Size of array:"+availableColors.length);
        return availableColors.length;
    }

}
