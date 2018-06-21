package ru.androidschool.h_h.thirdapp;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    View.OnClickListener editTextFieldEnable = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(),"EditButton", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Return Icon",Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setTitle(R.string.current_activity);
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.getMenu().findItem(R.id.button_edit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(),"Menu Edit",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        TextView userInfo = findViewById(R.id.textView_userInfo);
        userInfo.setText("Карта №7898769\nСпециалист");
        TextView userName = findViewById(R.id.user_name);
        userName.setText("Анастасия");
        TextView userSurname = findViewById(R.id.user_surname);
        userSurname.setText("Антонина");
        TextView userEmail = findViewById(R.id.user_email);
        userEmail.setText("anykee.box@gmail.ru");
        TextView userLogin = findViewById(R.id.user_login);
        userLogin.setText("HIE023UOI88");
        TextView userRegion = findViewById(R.id.user_region);
        userRegion.setText("Санкт-Петербург");
        findViewById(R.id.userNameEdit).setOnClickListener(editTextFieldEnable);
        findViewById(R.id.userSurnameEdit).setOnClickListener(editTextFieldEnable);
        findViewById(R.id.userEmailEdit).setOnClickListener(editTextFieldEnable);
        findViewById(R.id.userLoginEdit).setOnClickListener(editTextFieldEnable);
        findViewById(R.id.userRegionEdit).setOnClickListener(editTextFieldEnable);
        findViewById(R.id.textView_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Exit",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
