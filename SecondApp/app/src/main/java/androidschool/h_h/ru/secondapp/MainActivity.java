package androidschool.h_h.ru.secondapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button fb = findViewById(R.id.button);
        fb.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               startActivity(SecondActivity.createIntent(MainActivity.this));
            }
        });
    }
}
