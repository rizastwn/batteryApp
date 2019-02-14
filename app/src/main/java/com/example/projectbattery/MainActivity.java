package com.example.projectbattery;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    TextView tv_battery;
    Handler handler;
    Runnable runnable;
    Switch full, low;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        full = (Switch)findViewById(R.id.fullBattery);
        low = (Switch)findViewById(R.id.lowBattery);

        notificationManager = NotificationManagerCompat.from(this);
        tv_battery = (TextView) findViewById(R.id.tvBattery);
        runnable = new Runnable() {
            @Override
            public void run() {
                int level = (int) batterLevel();
                tv_battery.setText(level+"%");
                handler.postDelayed(runnable,5000);
                if (level==100){
                    fullBattery();
                }
                if (level <=30)
                {
                    lowBattery();
                }

            }
        };
        handler = new Handler();
        handler.postDelayed(runnable,0);

        full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int level = (int) batterLevel();
                if (b ==true   )
                {

                    Toast.makeText(getBaseContext(),"remind full is on",Toast.LENGTH_SHORT).show();
                    if (level==100)
                    {
                        fullBattery();
                    }
                }
            }
        });


        low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int level = (int) batterLevel();
                if (b ==true  )
                {

                    Toast.makeText(getBaseContext(),"remind low is on",Toast.LENGTH_SHORT).show();
                    if (level <=30)
                    {
                        lowBattery();
                    }
                }
            }
        });

    }

    public float batterLevel(){

        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level== -1 || scale == -1){
            return 50.0f;
        }
        return((float) level/(float) scale)*100.0f;
    }

    public void fullBattery(){
        String title = "Battere anda penuh!";
        String message = "cabut dari charger sekarang!";
        Notification notification = new NotificationCompat.Builder(this,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_batteryfull)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();
        notificationManager.notify(1,notification);

    }
    public void lowBattery()
    {
        String title = "Battere anda tinggal sedikit!";
        String message = "colok dari charger sekarang!";
        Notification notification = new NotificationCompat.Builder(this,App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_lowbattery)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();
        notificationManager.notify(2,notification);
    }
}
