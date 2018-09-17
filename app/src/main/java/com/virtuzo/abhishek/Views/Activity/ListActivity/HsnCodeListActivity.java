package com.virtuzo.abhishek.Views.Activity.ListActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzo.abhishek.ApiCalls.GetHSCCodes;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.UserLoginApi;
import com.virtuzo.abhishek.Views.Activity.ViewActivity.SupplierViewActivity;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.HSCCode;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.util.ArrayList;

public class HsnCodeListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, GetHSCCodes.CallBack {

    ListView hsnCodeListView;
    ArrayAdapter<HSCCode> hsnCodeAdapter;
    ArrayList<HSCCode> hscCodeList;
    SearchView searchView;
    String callingActivity;
    ProgressBar progressBar;
    TextView lastSyncedTextView;
    String BusinessId = "";
    SharedPreferences setting;
    ProgressDialog progressDialog;
    GetHSCCodes getHSCCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsn_code_list);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        hsnCodeListView = (ListView) findViewById(R.id.hsnCodeListView);
        lastSyncedTextView = (TextView) findViewById(R.id.lastSyncedTextView);

        getSupportActionBar().hide();
        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        BusinessId = setting.getString("BusinessId", "");
        TextView heading = (TextView) findViewById(R.id.actionBarTitle);
        heading.setText("HSN Code List");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        callingActivity = getIntent().getExtras().getString(MyFunctions.CLASS_NAME);
        LinearLayout refreshButton = (LinearLayout) findViewById(R.id.refreshButton);

        if(callingActivity.equals(MyFunctions.CLASS_NEW_PRODUCT)) {
            refreshButton.setVisibility(View.GONE);
        }

        hscCodeList = new ArrayList<>();
        hsnCodeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hscCodeList);
        hsnCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hsnCodeAdapter.notifyDataSetChanged();
        hsnCodeListView.setAdapter(hsnCodeAdapter);
        hsnCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HSCCode hscCodeSelected = (HSCCode) parent.getItemAtPosition(position);
//                Toast.makeText(HsnCodeListActivity.this, hscCodeSelected.toString(), Toast.LENGTH_SHORT).show();

                if(callingActivity.equals(MyFunctions.CLASS_DASHBOARD)) {
                    return;
                }

                Gson gson = new Gson();
                String json = gson.toJson(hscCodeSelected);

                Intent intent = new Intent();
                intent.putExtra("hscCode", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        searchView = (SearchView) findViewById(R.id.searchHsnCodeSearchView);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search HSN Code by name and code");
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        new PopulateList().execute();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        hsnCodeListView.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        hsnCodeListView.setVisibility(View.VISIBLE);
    }

    private void setSearchViewHeight(SearchView searchView, int height) {
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(height);
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void refreshButtonPressed(View view) {
        progressDialog.show();
        getHSCCodes = new GetHSCCodes(BusinessId, this);
    }

    @Override
    public void afterGetHSCCodes() {
        if (getHSCCodes.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
            successDialog();
        } else {
            errorCodeDialog(getHSCCodes.errorCode, getHSCCodes.apiResponse);
        }
        hscCodeList.clear();
        progressDialog.dismiss();
        new PopulateList().execute();
    }

    private void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HsnCodeListActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        progressDialog.dismiss();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText("Synced Successfully!!");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void errorCodeDialog(String errorCode, String exception) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HsnCodeListActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.error_code_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        progressDialog.dismiss();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        TextView errorCodeTextView = (TextView) view.findViewById(R.id.errorCodeTextView);
        errorCodeTextView.setMovementMethod(new ScrollingMovementMethod());
        okMessageTextView.setText("Error occured");
        errorCodeTextView.setText("Error Code : " + errorCode + "\n" + exception);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = MyFunctions.lowerCase(newText.trim());
        hsnCodeAdapter.getFilter().filter(newText);
        return true;
    }

    private class PopulateList extends AsyncTask<Integer, Void, String> {

        protected void onPreExecute() {
            Log.i("HSN", "show progress");
            showProgressBar();
        }

        @Override
        protected String doInBackground(Integer... params) {
            hscCodeList.addAll(DatabaseHandler.getInstance().getHSCCodeList());
            return "";
        }

        protected void onPostExecute(String msg) {
            Log.i("HSN", "hide progress");
            hsnCodeAdapter.notifyDataSetChanged();
            lastSyncedTextView.setText(hscCodeList.size()+"");
            hideProgressBar();
        }
    }

}
