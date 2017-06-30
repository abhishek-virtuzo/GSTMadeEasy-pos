package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.paypal.android.sdk.ar;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.StringUtils;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.ValidationUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActiveSIM extends AppCompatActivity {

    Button btnaActiveUsingAccountBalance;
    EditText etSimCard, etEmail, etAmountToPay, etZipCode;
    Spinner spinTariff;
    String strSimCard = "", strAmountToPay = "", strZipCode = "", strResult = "", strTraiff = "", strMobileNumber = "", strEmailId = "";
    private static final String TAG = ActiveSIM.class.getSimpleName();
    private static final String URL = "http://virtuzo.in/AhprepaidTestService/AhprepaidAPI_Json.svc/ActivateSIMService";
    private static final String TraiffPriceURL = "http://virtuzo.in/AhprepaidTestService/AhprepaidAPI_Json.svc/GetTariffDetailService";
    ArrayList<String> listTraiffcode;
    ArrayList<String> listTraiffId;
    ArrayAdapter<String> adapter;
    String TraiffID = "";
    ProgressDialog pd;
    String LoginID = "", DistributorID = "", ClientTypeID = "", strTraiffResult = "", strLycaAmount = "", strActivationResult = "", strMessage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_sim);

        btnaActiveUsingAccountBalance = (Button) findViewById(R.id.btnaActiveUsingAccountBalance);
        etSimCard = (EditText) findViewById(R.id.etSimCard);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAmountToPay = (EditText) findViewById(R.id.etAmountToPay);
        etZipCode = (EditText) findViewById(R.id.etZipCode);
        spinTariff = (Spinner) findViewById(R.id.spinTariff);


        listTraiffcode = (ArrayList<String>) getIntent().getSerializableExtra("listTraiffCode");
        listTraiffId = (ArrayList<String>) getIntent().getSerializableExtra("listTraiffid");
        listTraiffcode.add(0, "Please Select Tariff");
        LoginID = getIntent().getStringExtra("LoginID");
        Log.d(TAG, "LoginId" + LoginID);
        DistributorID = getIntent().getStringExtra("DistributorID");
        Log.d(TAG, "DistributorID" + DistributorID);
        ClientTypeID = getIntent().getStringExtra("ClientTypeID");
        Log.d(TAG, "listtraiff data :" + listTraiffcode);
        strEmailId = getIntent().getStringExtra("strEmailId");
        //etEmail.setText(strEmailId);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listTraiffcode);
        spinTariff.setAdapter(adapter);
        spinTariff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listTraiffcode.contains("Please Select Tariff")) {
                    listTraiffcode.remove(0);
                }
                TraiffID = listTraiffId.get(position);
                Log.d(TAG, "Tariff id data is:" + TraiffID);
                new PostPricedata().execute(TraiffPriceURL, TraiffID, LoginID, DistributorID, ClientTypeID);
                etAmountToPay.setText(strLycaAmount);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), " Tariff can't be blank", Toast.LENGTH_SHORT).show();
            }

        });

        btnaActiveUsingAccountBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strAmountToPay = etAmountToPay.getText().toString();
                strSimCard = etSimCard.getText().toString();
                strEmailId = etEmail.getText().toString();
                strZipCode = etZipCode.getText().toString();
                strTraiff = spinTariff.getSelectedItem().toString();

                if (strTraiff.contains("Please Select Tariff")) {
                    Toast.makeText(getApplicationContext(), " Tariff can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strAmountToPay)) {
                    Toast.makeText(getApplicationContext(), "Tariff can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strSimCard)) {
                    Toast.makeText(getApplicationContext(), "SIM CARD can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strZipCode)) {
                    Toast.makeText(getApplicationContext(), "ZIP Code can't be blank", Toast.LENGTH_SHORT).show();
                }else if (StringUtils.isBlank(strEmailId)) {
                    Toast.makeText(getApplicationContext(), "Email can't be blank", Toast.LENGTH_SHORT).show();
                    return;
                } else if (StringUtils.isBlank(strEmailId) || !ValidationUtils.checkEmail(strEmailId)) {
                    Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new PostData().execute(URL, ClientTypeID, DistributorID, TraiffID, strSimCard, strAmountToPay, LoginID, strEmailId, strZipCode);

                }

            }
        });
    }

    private class PostData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(ActiveSIM.this);
            pd.setMessage("Please Wait...");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL = params[0];
            String ClientTypeID = params[1];
            String DistributorID = params[2];
            String TraiffID = params[3];
            String strSimCard = params[4];
            String strAmountToPay = params[5];
            String LoginID = params[6];
            String strEmailId = params[7];
            String ZipCode = params[8];
            List<NameValuePair> nameValuePairs = new ArrayList<>(9);

            String strURL = URL + "?ClientTypeID=" + ClientTypeID + "&DistributorID=" + DistributorID + "&TariffID=" + TraiffID + "&SimNumber=" + strSimCard + "&TariffAmount=" + strAmountToPay + "&LoginID=" + LoginID + "&EmailID=" + strEmailId + "&ZipCode=" + ZipCode;
            Log.d(TAG, "String Url:" + strURL);

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strURL);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                strActivationResult = EntityUtils.toString(httpResponse.getEntity());
                Log.d(TAG, "Activation Result is:" + strActivationResult);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(strActivationResult);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    strMessage = jsonObject.getString("Message");
                    Log.d(TAG, " strMessage is:" + strMessage);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return strMessage;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class PostPricedata extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String TraiffUrl = params[0];
            String TraiffID = params[1];
            String LoginId = params[2];
            String DistributorID = params[3];
            String ClientTypeID = params[4];

            List<NameValuePair> nameValuePairs = new ArrayList<>(5);
            String PostUrL = TraiffUrl + "?LoginID=" + LoginId + "&DistributorID=" + DistributorID + "&ClientTypeID=" + ClientTypeID + "&TariffID=" + TraiffID;
            Log.d(TAG, "Post URL is:" + PostUrL);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(PostUrL);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                strTraiffResult = EntityUtils.toString(httpResponse.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "strTraiffResult is:" + strTraiffResult);

            try {
                JSONArray jsonArray = new JSONArray(strTraiffResult);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String strDescription = jsonObject.getString("Description");
                    strLycaAmount = jsonObject.getString("LycaAmount");
                    String Message = jsonObject.getString("Message");
                    String Month = jsonObject.getString("Months");
                    String Rental = jsonObject.getString("Rental");
                    String Response = jsonObject.getString("Response");
                    String TariffCode = jsonObject.getString("TariffCode");
                    String TariffTypeID = jsonObject.getString("TariffTypeID");
                    String ValidityDays = jsonObject.getString("ValidityDays");
                    String isActive = jsonObject.getString("isActive");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return strLycaAmount;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActiveSIM.this, DashBoardScreen.class);
        startActivity(intent);

    }
}
