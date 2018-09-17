package com.virtuzo.abhishek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.exceptionhandler.MyExceptionHandler;
import com.virtuzo.abhishek.modal.MyFunctions;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MyFunctions.setContext(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(getApplicationContext()));

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        db.getWritableDatabase();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, UserLoginApi.Login.class);
//                Intent i = new Intent(SplashScreen.this, NewPurchaseActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}

