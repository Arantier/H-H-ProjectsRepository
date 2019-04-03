package ru.androidschool.h_h.fourthapp;

import android.content.res.Resources;
import android.support.annotation.NonNull;
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


    public interface OnItemClickListener {
        void onItemClick(MainActivity.MenuElementData item);
    }

    private OnItemClickListener clickListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        ImageView icon;
        TextView title;

        private ViewHolder(@NonNull View cardView) {
            super(cardView);
            this.cardView = cardView;
            icon = cardView.findViewById(R.id.cardView_icon);
            title = cardView.findViewById(R.id.cardView_title);
        }

        public void bind(final MainActivity.MenuElementData item, final OnItemClickListener clickListener) {
            icon.setImageResource(item.getIconId());
            title.setText(item.getTitle());
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(item);
                }
            });
        }
    }

    class DescribedViewHolder extends ViewHolder {
        TextView description;

        DescribedViewHolder(@NonNull View cardView) {
            super(cardView);
            description = cardView.findViewById(R.id.cardView_description);
        }

        public void bind(final MainActivity.MenuElementData item, OnItemClickListener clickListener) {
            super.bind(item, clickListener);
            description.setText(item.getDescription());
            try {
                description.setTextColor(item.getDescriptionColor());
            } catch (Resources.NotFoundException e) {
                Log.e("VH.b", "Color id not found");
                description.setTextColor(0x888888);
            }
        }
    }

    Adapter(List<MainActivity.MenuElementData> menuElements, OnItemClickListener clickListener) {
        this.clickListener = clickListener;
        this.menuElements = menuElements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SQUARE_ITEM) {
            return new DescribedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_square, parent, false));
        } else if (viewType == WIDE_ITEM_DESCRIBED) {
            return new DescribedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_wide_described, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_wide, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.bind(menuElements.get(i), clickListener);
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