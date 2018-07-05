package ru.android_school.h_h.fifthapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
        findViewById(R.id.button_deliver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText textField = findViewById(R.id.TextInput);
                String message = textField.getText().toString();
                if (!message.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("message", message);
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
    }
}
