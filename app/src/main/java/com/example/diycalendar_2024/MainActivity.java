package com.example.diycalendar_2024;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import java.io.FileInputStream;
import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import java.io.FileOutputStream;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import adapter.ScheduleAdapter;
import adapter.Schedule;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button signup_button;
    Button signin_button;

    public String readDay = null;
    public String str = null;
    public CalendarView calendar;
    public Button cha_Btn, del_Btn, save_Btn, addButton;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;
    public RecyclerView recyclerView;

    public ScheduleAdapter adapter;
    public ArrayList<Schedule> scheduleList;
    public String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup_button = findViewById(R.id.signup_button);
        signin_button = findViewById(R.id.signin_button);
        calendarView = findViewById(R.id.calendar);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        // textView2 = findViewById(R.id.textView2);
        contextEditText = findViewById(R.id.contextEditText);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);

        scheduleList = new ArrayList<>();
        adapter = new ScheduleAdapter(scheduleList);

        // 회원가입 버튼
        signup_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼
        signin_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 일정 추가 버튼
        addButton.setOnClickListener(v -> showAddScheduleDialog());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                // diaryTextView.setVisibility(View.VISIBLE);
                // save_Btn.setVisibility(View.VISIBLE);
                // contextEditText.setVisibility(View.VISIBLE);
                // textView2.setVisibility(View.INVISIBLE);
                // cha_Btn.setVisibility(View.INVISIBLE);
                // del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                contextEditText.setText("");
                checkDay(year, month, dayOfMonth);
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                loadSchedulesForDate(selectedDate);
            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveDiary(readDay);
                str = contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });
    }

    private void showAddScheduleDialog() {
        if (selectedDate.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("날짜를 선택하세요.")
                    .setMessage("캘린더에서 날짜를 선택해주세요.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        EditText taskInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("일정 추가")
                .setMessage("일정을 입력하세요.")
                .setView(taskInput)
                .setPositiveButton("추가", (dialog, which) -> {
                    String task = taskInput.getText().toString();
                    if (!task.isEmpty()) {
                        scheduleList.add(new Schedule(scheduleList.size() + 1, selectedDate, task, false));
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void loadSchedulesForDate(String date) {
        ArrayList<Schedule> filteredList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            if (schedule.getDate().equals(date)) {
                filteredList.add(schedule);
            }
        }
        adapter.updateList(filteredList);
    }

    public void checkDay(int cYear, int cMonth, int cDay)
    {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
        FileInputStream fis;

        try
        {
            fis = openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);

                    textView2.setText(contextEditText.getText());
                }

            });

            del_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(readDay);
                }
            });
            if (textView2.getText() == null)
            {
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}




