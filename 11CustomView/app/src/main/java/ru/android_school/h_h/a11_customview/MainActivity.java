package ru.android_school.h_h.a11_customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ColumnDiagramView view = findViewById(R.id.custom);
        ArrayList<DateWithValue> list = new ArrayList<>();
        Calendar first = Calendar.getInstance(),
                second = Calendar.getInstance(),
                third = Calendar.getInstance(),
                fourth = Calendar.getInstance(),
                fifth = Calendar.getInstance(),
                sixth = Calendar.getInstance(),
                seven = Calendar.getInstance(),
                eight = Calendar.getInstance(),
                nine = Calendar.getInstance(),
                ten = Calendar.getInstance();
        first.set(Calendar.DAY_OF_MONTH, 1);
        second.set(Calendar.DAY_OF_MONTH, 2);
        third.set(Calendar.DAY_OF_MONTH, 3);
        fourth.set(Calendar.DAY_OF_MONTH, 4);
        fifth.set(Calendar.DAY_OF_MONTH, 5);
        sixth.set(Calendar.DAY_OF_MONTH,6);
        seven.set(Calendar.DAY_OF_MONTH,7);
        eight.set(Calendar.DAY_OF_MONTH,8);
        nine.set(Calendar.DAY_OF_MONTH,9);
        ten.set(Calendar.DAY_OF_MONTH,10);
        list.add(new DateWithValue(100, first));
        list.add(new DateWithValue(90, second));
        list.add(new DateWithValue(1, third));
        list.add(new DateWithValue(60, fourth));
        list.add(new DateWithValue(150, fifth));
        list.add(new DateWithValue(0, sixth));
        list.add(new DateWithValue(12, seven));
        list.add(new DateWithValue(44, eight));
        list.add(new DateWithValue(10, nine));
        list.add(new DateWithValue(75, ten));
        view.setData(list);
    }
}
