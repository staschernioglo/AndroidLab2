package com.example.lab2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    Activity context;
    public SortedList<Reminder> reminderSortedList;


    public ReminderAdapter(Activity context) {
        this.context = context;
        reminderSortedList = new SortedList<>(Reminder.class, new SortedListAdapterCallback<Reminder>(this) {
            @Override
            public int compare(Reminder o1, Reminder o2) {
                if (o1.time < o2.time)
                    return -1;
                if (o1.time > o2.time)
                    return 1;
                return 0;
            }

            @Override
            public boolean areContentsTheSame(Reminder oldItem, Reminder newItem) {
                return oldItem.time == newItem.time && newItem.name == oldItem.name;
            }

            @Override
            public boolean areItemsTheSame(Reminder item1, Reminder item2) {
                return item1 == item2;
            }
        });
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reminderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_view_holder, parent, false);
        return new ReminderViewHolder(reminderView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, final int position) {
        holder.fillData(reminderSortedList.get(position));

    }

    @Override
    public int getItemCount() {
        return reminderSortedList.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        TextView nameTextView;
        Button updateButton;
        Reminder reminder;
        Activity context;

        public ReminderViewHolder(View view, Activity context) {
            super(view);
            this.context = context;
            timeTextView = view.findViewById(R.id.reminder_time);
            nameTextView = view.findViewById(R.id.reminder_name);
            updateButton = view.findViewById(R.id.button5);

        }


        public void fillData(final Reminder reminder) {
            this.reminder = reminder;
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddActivity.class);
                    intent.putExtra(AddActivity.KEY_REMINDER,reminder);
                    intent.putExtra(AddActivity.KEY_POSITION,getAdapterPosition());
                    context.startActivityForResult(intent,MainActivity.REQUEST_UPDATE_REMINDER);
                }
            });
            Date date = new Date(reminder.time);
            timeTextView.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date));
            nameTextView.setText(reminder.name);

        }
    }
}
