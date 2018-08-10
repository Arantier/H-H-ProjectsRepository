package ru.android_school.h_h.eightapp.note_create;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.android_school.h_h.eightapp.R;
import ru.android_school.h_h.eightapp.note_trio.Note;

public class PickColorDialog extends DialogFragment {

    public static final String TAG = "PickColorDialog";
    private Note note;

    public static PickColorDialog newInstance(Note note){
        PickColorDialog newDialog = new PickColorDialog();
        newDialog.note = note;
        return newDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_color_picker,null);
        RecyclerView colorRecycler = dialogView.findViewById(R.id.colorRecycler);
        ColorRecyclerAdapter colorRecyclerAdapter = new ColorRecyclerAdapter(note,getContext());
        colorRecycler.setAdapter(colorRecyclerAdapter);
        colorRecycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        return builder.setView(dialogView)
                .setNegativeButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PickColorDialog.this.getDialog().cancel();
                    }
                }).create();
    }
}
