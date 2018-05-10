package com.timer.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.liyuehu.com.sqlitedemo.SqliteHelp;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.timer.AlarmReceiver.AlarmReceiver;

import java.util.Date;

public class TimerService extends Service {
    private SharedPreferences sharedPreferences;
    private boolean needFresh = false;
    public static final String TAG = "TimerService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPreferences = getSharedPreferences("myHealth", Context.MODE_PRIVATE);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int five = 1000 * 60 * 60; // 1h检查一次1000*60*60*2
        long triggerAtTime = SystemClock.elapsedRealtime() + five;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        if (manager != null) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
        String timeInterval = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date());
        int currentTime = Integer.valueOf(timeInterval.substring(timeInterval.indexOf("-") + 1, timeInterval.indexOf(":")));

        String firstEnterTime = sharedPreferences.getString("date", "");
        int firstTime = Integer.valueOf(firstEnterTime.substring(firstEnterTime.indexOf("-") + 1, firstEnterTime.indexOf(":")));

        int interval = currentTime - firstTime;
        Log.v("lilea", "interval===" + interval + " currentTime===" + currentTime + " firstTime==" + firstTime);
        if (interval >= 12 && currentTime >= 23) {//间隔大于12小时
            needFresh = true;
        } else {
            needFresh = false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("lilea", "更新时间: " + new Date().toString());
                if (needFresh) {
                    updateData();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    void clearSharedPreference() {
        sharedPreferences.edit().clear().apply();
        SqliteHelp.needClear = false;
        Log.i(TAG, "clearSharedPreference");
        stopSelf();
//        Process.killProcess(Process.myPid());
    }

    void updateData() {
        boolean result = SqliteHelp.getSqliteHelp().insertForHealth(sharedPreferences, this);
        if (result) {
            clearSharedPreference();
        } else {
            sharedPreferences.edit().putLong("firstEnterTime", -1).apply();
            Log.v("lilea", "更新失败！三餐某个为空");
        }
    }
}
