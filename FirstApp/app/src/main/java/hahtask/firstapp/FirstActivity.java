package hahtask.firstapp;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.TreeMap;
import java.util.TreeSet;

public class FirstActivity extends AppCompatActivity {

    TreeSet<String> ListOfStudents;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ListOfStudents = new TreeSet();
        Button AddStudent = findViewById(R.id.AddStudent);
        AddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Input = findViewById(R.id.StudentName);
                ListOfStudents.add(Input.getText().toString());
            }
        });
        Button ShowList = findViewById(R.id.ShowList);
        ShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView List = findViewById(R.id.ListOfStudents);
                String CurrentList = "";
                int Number = 1;
                for (String Person : ListOfStudents){
                    CurrentList+=Number+++Person+"\n";
                }
                List.setText(CurrentList);
            }
        });
    }
}
