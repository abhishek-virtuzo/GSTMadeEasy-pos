package com.virtuzo.abhishek.Views.Activity.Others;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.Views.Activity.ListActivity.ErrorLogsListActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewProductActivity;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.Brand;
import com.virtuzo.abhishek.modal.MyFunctions;

import com.virtuzo.abhishek.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    Switch dashboardDetailsSwitch;
    SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dashboardDetailsSwitch = (Switch) findViewById(R.id.dashboardDetailsSwitch);
        dashboardDetailsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor;
                editor = setting.edit();
                if (isChecked) {
                    editor.putBoolean(MyFunctions.HideDashboardDetails, false);
                } else {
                    editor.putBoolean(MyFunctions.HideDashboardDetails, true);
                }
                editor.commit();
            }
        });

        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        boolean isDashboardDetailsHidden = setting.getBoolean(MyFunctions.HideDashboardDetails, false);
        if (isDashboardDetailsHidden) {
            dashboardDetailsSwitch.setChecked(false);
        } else {
            dashboardDetailsSwitch.setChecked(true);
        }

        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar.findViewById(R.id.actionBarTitle);
        heading.setText("Settings");

        Button logsButton = (Button) toolbar.findViewById(R.id.logsButton);
        logsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ErrorLogsListActivity.class));
            }
        });

    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

}
