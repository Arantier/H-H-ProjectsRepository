package ru.androidschool.h_h.fourthapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView RecyclerView;

    private List<MenuElementData> menu_Items;

    class MenuElementData {
        //Лень делать инкапсуляцию
        public String title;
        public String description;
        public int colorId;
        public int iconId;

        public MenuElementData(String title, int iconId){
            this.title=title;
            this.iconId = iconId;
            description=null;
            colorId =R.color.warm_grey;
        }

        public MenuElementData(String title,String description, int iconId){
            this.title = title;
            this.iconId = iconId;
            this.description=description;
            colorId =R.color.warm_grey;
        }

        public MenuElementData(String title, String description, int colorId, int iconId){
            this.title = title;
            this.iconId = iconId;
            this.description=description;
            this.colorId =colorId;
        }
    }

    class Adapter extends RecyclerView.Adapter<MainActivity.Adapter.ViewHolder>{

        public static final int SQUARE_ITEM = 1;
        public static final int WIDE_ITEM = 2;
        public static final int WIDE_ITEM_DESCRIBED = 3;

        List<MenuElementData> menuElements;

        class ViewHolder extends RecyclerView.ViewHolder{
            CardView cardView;
            ImageView icon;
            TextView title;
            TextView description;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.cardView);
                icon = cardView.findViewById(R.id.cardView_icon);
                title = cardView.findViewById(R.id.cardView_title);
                try {
                    description = cardView.findViewById(R.id.cardView_description);
                } catch (NullPointerException e) {}
            }
        }

        Adapter(List<MenuElementData> menuElements){
            this.menuElements = menuElements;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType==SQUARE_ITEM) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_square, parent, false));
            } else if (viewType==WIDE_ITEM_DESCRIBED) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_wide_described, parent, false));
            } else {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_element_wide, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
            viewHolder.title.setText(menuElements.get(i).title);
            if (menuElements.get(i).description!=null) {
                viewHolder.description.setText(menuElements.get(i).description);
                viewHolder.description.setTextColor(getResources().getColor(menuElements.get(i).colorId));
            }
            viewHolder.icon.setImageResource(menuElements.get(i).iconId);
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(findViewById(R.id.activity_main),viewHolder.title.getText(),Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return menuElements.size();
        }

        @Override
        public int getItemViewType(int position){
            //Если описание не пусто и элемент или не является последним c описанием, или номер элемента чётный
            if ((menu_Items.get(position).description!=null)&&
                    ((position%2==1)||
                            (menu_Items.get(position+1).description!=null))){
                return SQUARE_ITEM;
            } else if (menu_Items.get(position).description==null){
                return WIDE_ITEM;
            } else {
                return WIDE_ITEM_DESCRIBED;
            }

        }
    };

    private List<MenuElementData> imitationOfReceivingDataPackages(){
        //Поскольку данные как бы получаются от сервера, я решил не прибегать
        // к использованию ресурсов и добавить текст вручную
        List<MenuElementData> dataPackage = new ArrayList();
        dataPackage.add(new MenuElementData("Квитанции","- 40 080,55 \u20BD ",R.color.coral,R.drawable.ic_bill));
        dataPackage.add(new MenuElementData("Счётчики","Подайте показания",R.color.coral,R.drawable.ic_counter));
        dataPackage.add(new MenuElementData("Рассрочка","Сл. платеж 25.02.2018",R.drawable.ic_installment));
        dataPackage.add(new MenuElementData("Страхование","Полис до 12.01.2019",R.drawable.ic_insurance));
        dataPackage.add(new MenuElementData("Интернет и ТВ","Баланс 350 \u20BD",R.drawable.ic_tv_2));
        dataPackage.add(new MenuElementData("Домофон","Подключен",R.drawable.ic_homephone));
        dataPackage.add(new MenuElementData("Охрана","Нет",R.drawable.ic_guard));
        dataPackage.add(new MenuElementData("Контакты УК и служб",R.drawable.ic_uk_contacts));
        dataPackage.add(new MenuElementData("Мои заявки",R.drawable.ic_request));
        dataPackage.add(new MenuElementData("Памятка жителя А101",R.drawable.ic_about));
        return dataPackage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu_Items = imitationOfReceivingDataPackages();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.current_screen);
        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.getMenu().findItem(R.id.menu_getInfo).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Похоже на диалог")
                        .setPositiveButton("И не поспоришь",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        toolbar.getMenu().findItem(R.id.menu_home).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(),"Toast",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        RecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager customGL = new GridLayoutManager(this,2);
        customGL.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((menu_Items.get(position).description!=null)&&
                        ((position%2==1)||
                                (menu_Items.get(position+1).description!=null))){
                    return 1;
                } else {
                    return 2;
                }
            }
        });
        RecyclerView.setLayoutManager(customGL);
        RecyclerView.setAdapter(new Adapter(menu_Items));
    }
}
