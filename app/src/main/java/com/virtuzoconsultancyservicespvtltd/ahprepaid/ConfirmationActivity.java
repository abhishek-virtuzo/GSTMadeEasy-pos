package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.URL;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {

    String TOPUPURL= URL.TOPUPService;
    String Message="",Response="",ResponseCode="",TotalTopup="",PaymentAmount="",ClientTypeID="",DistributorID="",DateAndTime="",LoginID="",Status="",strResult="",FirstName="",TxnID="";
    private static final String TAG=ConfirmationActivity.class.getSimpleName();
    public static final String PREFS_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent = getIntent();
        PaymentAmount=intent.getStringExtra("PaymentAmount");
        Log.d(TAG,"PaymentAmount is:"+PaymentAmount);
        ClientTypeID=intent.getStringExtra("ClientTypeID");
        DistributorID=intent.getStringExtra("DistributorID");
        DateAndTime=intent.getStringExtra("DateAndTime");
        LoginID=intent.getStringExtra("LoginID");
        FirstName=intent.getStringExtra("FirstName");

        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        new postData().execute(TOPUPURL,PaymentAmount,ClientTypeID,DistributorID,DateAndTime,LoginID,Status);

        ((Button) findViewById(R.id.dashBoardButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext() , DashBoardScreen.class);
                startActivity(intent1);
            }
        });


    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        Status=textViewStatus.getText().toString();
        Log.d(TAG,"Status data is:"+Status);
        textViewAmount.setText(paymentAmount+" USD");
        TxnID=textViewId.getText().toString();

    }

    private class postData  extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            String URL=params[0];
            String strAmount=params[1];
            String strClientId=params[2];
            String strDistributeId=params[3];
            String strDateAndTime=params[4];
            String Date=strDateAndTime.replace(" ","%20");
            String strLoginId=params[5];
            String strStatus=params[6];

            List<NameValuePair> nameValuePairs = new ArrayList<>(7);
            String url = URL + "?DistributorID=" + strDistributeId + "&LoginID=" + strLoginId + "&Amount=" + strAmount + "&Status=" + strStatus + "&TxnID=" + TxnID + "&TxnDate=" + Date;

            Log.e(TAG, "doInBackground: url " + url);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                strResult = EntityUtils.toString(httpResponse.getEntity());
                Log.e(TAG, "Confirmation Result " + strResult);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try{

                JSONArray jsonArray=new JSONArray(strResult);
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Message=jsonObject.getString("Message");
                    Response=jsonObject.getString("Response");
                    ResponseCode=jsonObject.getString("ResponseCode");
                    TotalTopup=jsonObject.getString("TotalTopup");

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return Message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ConfirmationActivity.this,PayPalActivity.class);
        intent.putExtra("Topup",TotalTopup);
        startActivity(intent);
    }
}
