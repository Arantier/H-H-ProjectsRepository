package ru.androidschool.h_h.eightapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ColorRecyclerAdapter extends RecyclerView.Adapter<ColorRecyclerAdapter.ColorRecyclerHolder> {

    class ColorRecyclerHolder extends RecyclerView.ViewHolder{

        public ColorRecyclerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    interface OnColorPickListener {
        public int onColorPick();
    }

    private OnColorPickListener onColorPickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ColorRecyclerHolder colorRecyclerHolder, int i) {
        colorRecyclerHolder.bind()
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public ColorRecyclerAdapter(OnColorPickListener onColorPickListener){

    }
}
