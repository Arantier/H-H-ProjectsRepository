package ru.android_school.h_h.sevenapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

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
            bridgeId = bridge.id;
            bridgeName.setText(bridge.name);
            int state = 0;
            //TODO: Замени дефис на тире
            String formattedTime="";
            for (TimeInterval i : bridge.bridgeIntervals){
                formattedTime+=i+"\t";
                int newState = i.whatPosition(new Date());
                if (state<newState){
                    state = newState;
                }
            }
            switch (state){
                case (TimeInterval.BRIDGE_CONNECTED):
                    bridgeState.setImageResource(R.drawable.ic_bridge_normal);
                    break;
                case (TimeInterval.BRIDGE_SOON):
                    bridgeState.setImageResource(R.drawable.ic_bridge_soon);
                    break;
                case (TimeInterval.BRIDGE_RAISED):
                    bridgeState.setImageResource(R.drawable.ic_bridge_late);
                    break;
            }
            bridgeTime.setText(formattedTime);
            bridgeReminder.setImageResource((bridge.isSubscribed ? R.drawable.ic_bell_on : R.drawable.ic_bell_off));
            view.setOnClickListener(AnonListener -> listener.onItemClick(bridge));
        }
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.i(TAG,"list item was created");
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bridge_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder viewHolder, int i) {
        Bridge bridge = listOfBridges.get(i);
        viewHolder.bind(bridge, onItemClickListener);
        Log.i(TAG,"Bridge "+bridge.name+" has been binded");
    }

    @Override
    public int getItemCount() {
        return listOfBridges.size();
    }

    public ListAdapter(List<Bridge> listOfBridges, OnItemClickListener onItemClickListener) {
        this.listOfBridges = listOfBridges;
        this.onItemClickListener = onItemClickListener;
        Log.i(TAG,"Adapter has been created");
    }
}