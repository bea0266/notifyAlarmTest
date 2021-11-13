package com.test.notificationtimepicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    TimePicker tp;
    Button regist, cancle;
    int hour, min, year, mon, day, sec=0;
    private AlarmManager alarmManager;
    private Calendar mCalender;
    static int pendingId = 0;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tp = (TimePicker) findViewById(R.id.tp);
        regist = (Button) findViewById(R.id.regist);
        cancle = (Button) findViewById(R.id.cancle);
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        tp.setIs24HourView(true);

        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                hour = hourOfDay;
                min = minute;
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), --pendingId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                Toast.makeText(getApplicationContext(), "최근 알람이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCalender = Calendar.getInstance();



                Intent receiverIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, pendingId++, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH시 mm분 ss초");



                mCalender.set(Calendar.HOUR_OF_DAY, hour);
                mCalender.set(Calendar.MINUTE, min);
                mCalender.set(Calendar.SECOND, sec);
                
                Log.d("hour value", String.valueOf(hour));
                Log.d("mCalender Value", String.valueOf(mCalender.get(Calendar.HOUR_OF_DAY)));
                Log.d("date value",dateFormat.format(mCalender.getTime()));


                Toast.makeText(getApplicationContext(), dateFormat.format(mCalender.getTime()), Toast.LENGTH_SHORT).show();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalender.getTimeInMillis(), pendingIntent);
                } else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalender.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, mCalender.getTimeInMillis(), pendingIntent);
                    }

                }



            }
        });


    }
}