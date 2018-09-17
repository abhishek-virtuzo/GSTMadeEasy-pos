package com.virtuzo.abhishek.exceptionhandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.virtuzo.abhishek.SplashScreen;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Abhishek Aggarwal on 2/3/2018.
 */

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context activity;
    SharedPreferences setting;
    public static final String PREFS_NAME = "LoginPrefs";
    public MyExceptionHandler(Context a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = new Intent(activity, SplashScreen.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        setting = activity.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = setting.edit();

        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        MyFunctions.errorOccuredAppCrash(errors.toString());
        Log.e("My exception", errors.toString());
        editor.putString("crashReport", errors.toString());
        editor.commit();

        PendingIntent pendingIntent = PendingIntent.getActivity(MyFunctions.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) MyFunctions.getContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        System.exit(2);
    }

}
