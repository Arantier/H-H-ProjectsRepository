package ru.androidschool.h_h.eightapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PickColorDialog extends DialogFragment {

    public static final String TAG = "PickColorDialog";

    Note currentNote;

    private RecyclerView recyclerView;

    public static PickColorDialog newInstance(Note note){
        PickColorDialog newDialog = new PickColorDialog();
        newDialog.currentNote = note;
        return newDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_color_picker,null);
        recyclerView = dialogView.findViewById(R.id.colorRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        recyclerView.setAdapter(new ColorRecyclerAdapter());
        builder.setView(dialogView)
                .setNegativeButton(R.string.decline, (listener,i) ->{
                    PickColorDialog.this.getDialog().cancel();
                });
        return builder.create();
    }
}
