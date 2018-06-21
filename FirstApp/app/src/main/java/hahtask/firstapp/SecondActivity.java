package hahtask.firstapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondActivity extends AppCompatActivity {

    protected class Student implements Comparable<Student>{
        private int ID;
        private String Surname;
        private String Name;
        private String Group;
        private String BirthYear;

        @Override
        public String toString(){
            return ID+"\t"+Surname+"\t"+Name+"\t"+Group+"\t"+BirthYear+"\n";
        }

        public Student(String Surname, String Name, String Group, String BirthYear){
            this.Surname = Surname;
            this.Name = Name;
            this.Group = Group;
            this.BirthYear = BirthYear;
            this.ID = (int)System.currentTimeMillis();
        }

        @Override
        public int compareTo(@NonNull Student o) {
            if (this.ID==o.ID)
                return 0;
            String f = this.Group+this.Surname+this.Name+this.BirthYear;
            String s = o.Group+o.Surname+o.Name+o.BirthYear;
            return f.compareTo(s);
        }
    }

    protected int NewStudentNumber = 0;

    TreeSet<Student> List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        //Где-то тут жопа
        List = new TreeSet();
        final EditText InputText = findViewById(R.id.Name);
        final Button ShowList = findViewById(R.id.Show);
        InputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode == KeyEvent.KEYCODE_ENTER)){
                    String RawStudent = InputText.getText().toString();
                    //Имя Фамилия Класс Дата рождения
                    if (RawStudent.matches("\\w*\\s\\w*\\s((\\d)|(\\d\\d))\\w\\s\\d{4}")){
                        String[] CookedStudent = RawStudent.split("\\s");
                        List.add(new Student(CookedStudent[0],CookedStudent[1],CookedStudent[2],CookedStudent[3]));
                        //Как реагировать с внешними объектами через слушатели? Передача ссылок на них через параметры или что?
//                        AddStudent.setText("Вывести список (Студентов в очереди:"+(++NewStudentNumber)+")");
                    }
                    return true;
                }
                return false;
            }
        });
        ShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView TextDisplay = findViewById(R.id.List);
                String NewText = "ID\tФамилия\tИмя\tГруппа\tГод рождения\n";
                int Number = 1;
                for (Student Person : List){
                    NewText+=Person.toString();
                }
                TextDisplay.setText(NewText);
            }
        });
    }
}
