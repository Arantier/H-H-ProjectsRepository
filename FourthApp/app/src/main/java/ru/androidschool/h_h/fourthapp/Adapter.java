package ru.androidschool.h_h.fourthapp;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public static final int SQUARE_ITEM = 1;
    public static final int WIDE_ITEM = 2;
    public static final int WIDE_ITEM_DESCRIBED = 3;

    List<MainActivity.MenuElementData> menuElements;

    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView icon;
        TextView title;

        public ViewHolder(@NonNull View cardView) {
            super(cardView);
            icon = cardView.findViewById(R.id.cardView_icon);
            title = cardView.findViewById(R.id.cardView_title);
        }

        public void bind(int position) {
            icon.setImageResource(menuElements.get(position).getIconId());
            title.setText(menuElements.get(position).getTitle());
        }
    }

    class DescribedViewHolder extends ViewHolder {
        TextView description;

        public DescribedViewHolder(@NonNull View cardView) {
            super(cardView);
            description = cardView.findViewById(R.id.cardView_description);
        }

        public void bind(int position) {
            super.bind(position);
            description.setText(menuElements.get(position).getDescription());
            try {
                description.setTextColor(menuElements.get(position).getDescriptionColor());
            } catch (Resources.NotFoundException e){
                Log.e("VH.b","Color id not found");
                description.setTextColor(0x888888);
            }
        }
    }

    Adapter(List<MainActivity.MenuElementData> menuElements) {
        this.menuElements = menuElements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
/*        if (viewType == SQUARE_ITEM) {
            return new DescribedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_square, parent, false));
        } else if (viewType == WIDE_ITEM_DESCRIBED) {
            return new DescribedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_wide_described, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_wide, parent, false));
        }*/
        if (viewType == SQUARE_ITEM) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_square, parent, false);
            item.setOnClickListener(mOnClickListener);
            return new DescribedViewHolder(item);
        } else if (viewType == WIDE_ITEM_DESCRIBED) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_wide_described, parent, false);
            item.setOnClickListener(mOnClickListener);
            return new DescribedViewHolder(item);
        } else {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_wide, parent, false);
            item.setOnClickListener(mOnClickListener);
            return new ViewHolder(item);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return menuElements.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Если описание не пусто и элемент или не является последним c описанием, или номер элемента чётный
        if ((menuElements.get(position).getDescription() != null) &&
                ((position % 2 == 1) ||
                        (menuElements.get(position + 1).getDescription() != null))) {
            return SQUARE_ITEM;
        } else if (menuElements.get(position).getDescription() == null) {
            return WIDE_ITEM;
        } else {
            return WIDE_ITEM_DESCRIBED;
        }

    }
}