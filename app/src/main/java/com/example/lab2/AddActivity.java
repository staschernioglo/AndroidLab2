package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    final static String KEY_REMINDER = "KEY_REMINDER";
    final static String KEY_POSITION = "KEY_POSITION";
    CalendarView calendarView;
    TimePicker timePicker;
    EditText editText;
    int position;
    Calendar calendar = Calendar.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        calendarView = findViewById(R.id.calendarView);
        timePicker = findViewById(R.id.timePicker);
        editText = findViewById(R.id.editTextTextPersonName);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
            }
        });
        Reminder reminder = getIntent().getParcelableExtra(KEY_REMINDER);
        position = getIntent().getIntExtra(KEY_POSITION, -1);
        if (reminder != null && position != -1) {
            calendar.setTimeInMillis(reminder.time);
            calendarView.setDate(reminder.time);
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
            editText.setText(reminder.name);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickSave(View view) {

        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());

        Reminder reminder = new Reminder(editText.getText().toString(), calendar.getTimeInMillis());
        Intent intent = new Intent();
        intent.putExtra(KEY_POSITION,position);
        intent.putExtra(KEY_REMINDER, reminder);
        setResult(RESULT_OK, intent);
        finish();
    }
}