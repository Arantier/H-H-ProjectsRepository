package ru.androidschool.h_h.fourthapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView RecyclerView;

    private List<MenuElementData> menu_Items;

    class MenuElementData {
        private String title;
        private String description;
        //        private int descriptionColor;
        private int descriptionColorId;
        private int iconId;

        public MenuElementData(String title, int iconId) {
            this.title = title;
            this.iconId = iconId;
            description = null;
            descriptionColorId = R.color.warm_grey;
        }

        public MenuElementData(String title, String description, int iconId) {
            this(title, iconId);
            this.description = description;
        }

        public MenuElementData(String title, String description, int descriptionColorId, int iconId) {
            this(title, description, iconId);
            this.descriptionColorId = descriptionColorId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getIconId() {
            return iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public int getDescriptionColor() {
            return ContextCompat.getColor(getApplicationContext(), descriptionColorId);
        }

        public void setDescriptionColorId(int descriptionColorId) {
            this.descriptionColorId = descriptionColorId;
        }
    }

    private List<MenuElementData> imitationOfReceivingDataPackages() {
        //Поскольку данные как бы получаются от сервера, я решил не прибегать
        // к использованию ресурсов и добавить текст вручную
        List<MenuElementData> dataPackage = new ArrayList<>();
        dataPackage.add(new MenuElementData("Квитанции", "- 40 080,55 \u20BD ", R.color.coral, R.drawable.ic_bill));
        dataPackage.add(new MenuElementData("Счётчики", "Подайте показания", R.color.coral, R.drawable.ic_counter));
        dataPackage.add(new MenuElementData("Рассрочка", "Сл. платеж 25.02.2018", R.drawable.ic_installment));
        dataPackage.add(new MenuElementData("Страхование", "Полис до 12.01.2019", R.drawable.ic_insurance));
        dataPackage.add(new MenuElementData("Интернет и ТВ", "Баланс 350 \u20BD", R.drawable.ic_tv_2));
        dataPackage.add(new MenuElementData("Домофон", "Подключен", R.drawable.ic_homephone));
        dataPackage.add(new MenuElementData("Охрана", "Нет", R.drawable.ic_guard));
        dataPackage.add(new MenuElementData("Контакты УК и служб", R.drawable.ic_uk_contacts));
        dataPackage.add(new MenuElementData("Мои заявки", R.drawable.ic_request));
        dataPackage.add(new MenuElementData("Памятка жителя А101", R.drawable.ic_about));
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
                Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        RecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager customGL = new GridLayoutManager(this, 2);
        customGL.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (customGL.findViewByPosition(position).getItem) {
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
