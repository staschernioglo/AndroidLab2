package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_ADD_REMINDER = 1;
    final static int REQUEST_UPDATE_REMINDER = 2;
    static final  String FILE_NAME = "bd.jason";
    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reminderAdapter = new ReminderAdapter(this);
        ReadFromFile();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(reminderAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                reminderAdapter.reminderSortedList.removeItemAt(viewHolder.getAdapterPosition());
                WriteToFile();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    void WriteToFile() {
        File file = new File(getFilesDir().getAbsolutePath() + File.separator + FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            ArrayList<Reminder> reminders = new ArrayList<>(reminderAdapter.reminderSortedList.size());
            for (int i = 0; i<reminderAdapter.reminderSortedList.size(); i++){
                reminders.add(reminderAdapter.reminderSortedList.get(i));

            }
            String json = gson.toJson(reminders);
            Log.i("JSON", json);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     void ReadFromFile(){
     File file = new File (getFilesDir().getAbsolutePath()+ File.separator + FILE_NAME);
        try {
        Scanner scanner = new Scanner(file);
            Gson gson = new Gson();
            if (scanner.hasNextLine()) {
                String json = scanner.nextLine();
                Reminder[] reminders = gson.fromJson(json,Reminder[].class);
                reminderAdapter.reminderSortedList.addAll(reminders);
            }
            scanner.close();
           } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

     }


    public void onAdd(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, REQUEST_ADD_REMINDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD_REMINDER:
                if (resultCode == RESULT_OK) {
                    Reminder reminder = data.getParcelableExtra(AddActivity.KEY_REMINDER);
                    reminderAdapter.reminderSortedList.add(reminder);
                    WriteToFile();
                }
                break;
            case REQUEST_UPDATE_REMINDER:
                if (resultCode == RESULT_OK) {
                    Reminder reminder = data.getParcelableExtra(AddActivity.KEY_REMINDER);
                    int position = data.getIntExtra(AddActivity.KEY_POSITION, -1);
                    if (position != -1) {
                        reminderAdapter.reminderSortedList.updateItemAt(position, reminder);

                        reminderAdapter.notifyItemChanged(position);
                        WriteToFile();
                    }
                }
        }

    }
}