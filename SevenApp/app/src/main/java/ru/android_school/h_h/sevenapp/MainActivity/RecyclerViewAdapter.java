package ru.android_school.h_h.sevenapp.MainActivity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeManager;
import ru.android_school.h_h.sevenapp.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static final String TAG = "list_bridges";

    List<Bridge> listOfBridges;

    public interface OnItemClickListener {
        public void onItemClick(Bridge bridge);
    }

    OnItemClickListener onItemClickListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        int bridgeId;
        ImageView bridgeState;
        TextView bridgeName;
        TextView bridgeTime;
        ImageView bridgeReminder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            bridgeState = itemView.findViewById(R.id.image_bridgeState);
            bridgeName = itemView.findViewById(R.id.bridgeName);
            bridgeTime = itemView.findViewById(R.id.bridgeTime);
            bridgeReminder = itemView.findViewById(R.id.image_isSubscribed);
        }

        public void bind(Bridge bridge, OnItemClickListener listener) {
            BridgeManager.makeBridgeBar(bridge,view);
            view.setOnClickListener(AnonListener -> listener.onItemClick(bridge));
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.i(TAG,"list item was created");
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bridge_bar,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Bridge bridge = listOfBridges.get(i);
        viewHolder.bind(bridge, onItemClickListener);
        Log.i(TAG,"Bridge "+ bridge.getName() +" has been binded");
    }

    @Override
    public int getItemCount() {
        if (listOfBridges!=null) {
            return listOfBridges.size();
        } else {
            return -1;
        }
    }

    public RecyclerViewAdapter(List<Bridge> listOfBridges, OnItemClickListener onItemClickListener) {
        this.listOfBridges = listOfBridges;
        this.onItemClickListener = onItemClickListener;
        Log.i(TAG,"Adapter has been created");
    }
}