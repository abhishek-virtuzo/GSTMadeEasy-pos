package com.virtuzo.abhishek.Views.Activity.ListActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Adapters.ErrorLogListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.ErrorLog;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ErrorLogsListActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;
    ArrayList<ErrorLog> listOfErrorLogs;
    RecyclerView recyclerView;
    private ErrorLogListAdapter errorLogListAdapter;
    String BusinessId;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_logs_list);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setTitle("Products");
        getSupportActionBar().hide();
        Toolbar toolbar_in_invoice_list = (Toolbar) findViewById(R.id.toolbar_in_errorlogs_list);
        toolbar_in_invoice_list.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_invoice_list.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_invoice_list.findViewById(R.id.actionBarTitle);
        heading.setText("Error logs List");

        listOfErrorLogs = new ArrayList<>();
        BusinessId = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("BusinessId", "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.invoiceListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking an invoice
        errorLogListAdapter = new ErrorLogListAdapter(listOfErrorLogs, new ErrorLogListAdapter.OnClickListener() {
            @Override
            public void onItemClick(ErrorLog errorLog) {
                // on click
            }
        });

        recyclerView.setAdapter(errorLogListAdapter);

        listOfErrorLogs.clear();
        listOfErrorLogs.addAll(DatabaseHandler.getInstance().getErrorLogList());
        errorLogListAdapter.notifyDataSetChanged();
        progressDialog.dismiss();

    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    public void saveInFileButton(View v) {
        // save db file
        AlertDialog.Builder builder = new AlertDialog.Builder(ErrorLogsListActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView confirmMessageTextView = (TextView) view.findViewById(R.id.confirmMessage);
        confirmMessageTextView.setText("Do you want to take database backup ?");
        Button yesButton = (Button) view.findViewById(R.id.yesClick);
        Button noButton = (Button) view.findViewById(R.id.noClick);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // logout
                saveInFile();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void saveInFile() {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/" + DatabaseHandler.DATABASE_NAME;
                String backupDBPath = "backupname.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                showOkDialog("Saved");
            }
        } catch (Exception e) {
            showOkDialog("Error");
            MyFunctions.errorOccuredCatchInSavingDbBackup(e, "Error Logs Screen");
        }
    }

    public void showOkDialog(String heading) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ErrorLogsListActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText(heading);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}
