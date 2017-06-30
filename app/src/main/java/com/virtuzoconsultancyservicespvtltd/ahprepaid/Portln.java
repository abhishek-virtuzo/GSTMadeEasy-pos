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

public class Portln extends AppCompatActivity {

    Button btnaActiveUsingAccountBalance;
    EditText etSimCard, etEmail, etAmountToPay, etZipCode, etPhoneToPort, etPin, etAccount;
    Spinner spinTariff;
    ProgressDialog pd;
    String strMessage="",strSimCard = "", strEmail = "", strAmountToPay = "", strZipCode = "", strResult = "", strTraiff = "", strPhoneToPort = "", strPin = "", strAccount = "";
    private static final String TAG = Portln.class.getSimpleName();
    private static final String URL = "http://virtuzo.in/AhprepaidTestService/AhprepaidAPI_Json.svc/PortInService";
    ArrayList<String> listTraiffid;
    ArrayList<String> listTraiffCode;
    ArrayAdapter<String> adapter;
    String TraiffID = "", strTraiffResult = "", strLycaAmount = "";
    String Message="",Response="",ResponseCode="",ClientTypeID = "", DistributorID = "", LoginID = "", strEmailId = "", SimNumber = "", TariffAmount = "", ZipCode = "", Pin = "", Account = "", PhoneToPort = "";
    private static final String TraiffPriceURL = "http://virtuzo.in/AhprepaidTestService/AhprepaidAPI_Json.svc/GetTariffDetailService";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portln);

        btnaActiveUsingAccountBalance = (Button) findViewById(R.id.btnaActiveUsingAccountBalance);
        etSimCard = (EditText) findViewById(R.id.etSimCard);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAmountToPay = (EditText) findViewById(R.id.etAmountToPay);
        etZipCode = (EditText) findViewById(R.id.etZipCode);
        spinTariff = (Spinner) findViewById(R.id.spinTariff);
        etPhoneToPort = (EditText) findViewById(R.id.etPhoneToPort);
        etPin = (EditText) findViewById(R.id.etPin);
        etAccount = (EditText) findViewById(R.id.etAccount);
        ClientTypeID = getIntent().getStringExtra("ClientTypeID");
        DistributorID = getIntent().getStringExtra("DistributorID");
        LoginID = getIntent().getStringExtra("LoginID");
        strEmailId = getIntent().getStringExtra("strEmailId");
        //etEmail.setText(strEmailId);

        listTraiffid = (ArrayList<String>) getIntent().getSerializableExtra("listTraiffid");
        listTraiffCode = (ArrayList<String>) getIntent().getSerializableExtra("listTraiffCode");

        listTraiffCode.add(0, "Please Select Tariff");

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listTraiffCode);
        spinTariff.setAdapter(adapter);
        spinTariff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listTraiffCode.contains("Please Select Tariff")){
                    listTraiffCode.remove(0);
                }
                TraiffID = listTraiffid.get(position);
                Log.d(TAG, "Traiff id data is:" + TraiffID);
                new PostPricedata().execute(TraiffPriceURL, TraiffID, LoginID, DistributorID, ClientTypeID);
                etAmountToPay.setText(strLycaAmount);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        btnaActiveUsingAccountBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strAmountToPay = etAmountToPay.getText().toString();
                strSimCard = etSimCard.getText().toString();
                strEmail = etEmail.getText().toString();
                strZipCode = etZipCode.getText().toString();
                strTraiff = spinTariff.getSelectedItem().toString();
                Log.d(TAG,"Traiff default item is:"+strTraiff);
                strPhoneToPort = etPhoneToPort.getText().toString();
                strPin = etPin.getText().toString();
                strAccount = etAccount.getText().toString();
                strPin = etPin.getText().toString();
                strAccount = etAccount.getText().toString();
                strPhoneToPort = etPhoneToPort.getText().toString();

                if (listTraiffCode.contains("Please Select Tariff")) {
                    Toast.makeText(getApplicationContext(), "Tariff can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strAmountToPay)) {
                    Toast.makeText(getApplicationContext(), "Tariff can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strSimCard)) {
                    Toast.makeText(getApplicationContext(), "SIM CARD can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strPhoneToPort)) {
                    Toast.makeText(getApplicationContext(), "PHONE TO PORT Code can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strPin)) {
                    Toast.makeText(getApplicationContext(), "PIN can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strZipCode)) {
                    Toast.makeText(getApplicationContext(), "ZIP Code can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strAccount)) {
                    Toast.makeText(getApplicationContext(), "ACCOUNT can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strEmail)) {
                    Toast.makeText(getApplicationContext(), "Email can't be blank", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strEmail) || !ValidationUtils.checkEmail(strEmail)) {
                    Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    new PostData().execute(URL, strAmountToPay, strSimCard, strEmail, strZipCode, strTraiff, strPhoneToPort, strPin, strAccount, strPhoneToPort, ClientTypeID, DistributorID, TraiffID, LoginID);
                }


            }

        });
    }

    private class PostData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Portln.this);
            pd.setMessage("Please Wait...");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL = params[0];
            String AmountToPay = params[1];
            String SimCard = params[2];
            String Email = params[3];
            String ZipCode = params[4];
            String Traiff = params[5];
            String PhoneTOPort = params[6];
            String strPhoneToPort=PhoneTOPort.replace(" ","%20");
            String Pin = params[7];
            String Account = params[8];
            String strAccount=Account.replace(" ","%20");
            String PhoneToPort = params[9];
            String ClientTypeID = params[10];
            String DistributorID = params[11];
            String TariffID = params[12];
            String LoginID = params[13];


            String strURL = URL + "?ClientTypeID=" + ClientTypeID + "&DistributorID=" + DistributorID + "&TariffID=" + TariffID + "&SimNumber=" + SimCard + "&TariffAmount=" + AmountToPay + "&LoginID=" + LoginID + "&EmailID=" + Email + "&ZipCode=" + ZipCode + "&Pin=" + Pin + "&Account=" + strAccount + "&PhoneToPort=" + strPhoneToPort;
            Log.d(TAG, "String Url:" + strURL);
            List<NameValuePair> nameValuePairs = new ArrayList<>(11);
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strURL);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                strResult = EntityUtils.toString(httpResponse.getEntity());
                Log.d(TAG, "Result Data is:" + strResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray = new JSONArray(strResult);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    strMessage=jsonObject.getString("Message");
                    Response=jsonObject.getString("Response");
                    ResponseCode=jsonObject.getString("ResponseCode");
                }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return strMessage;
            }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){
                pd.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }
        }
    }

        private class PostPricedata extends AsyncTask<String, String, String> {
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
                        Message = jsonObject.getString("Message");
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
        Intent intent=new Intent(Portln.this,DashBoardScreen.class);
        startActivity(intent);

    }
    }
