package com.app.todo.todoMain.ui.alarmManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmTask implements Runnable{
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager alarmManager;
    // Your context to retrieve the alarm manager from
    private final Context context;

    public AlarmTask(Context context, Calendar date) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
    }

    @Override
    public void run() {
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
    }
}
