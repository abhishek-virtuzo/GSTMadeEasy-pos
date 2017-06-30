package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.StringUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SimReplacement extends AppCompatActivity {

    EditText etCurrentSIMNumber, etNewSIMNumber, etCurrentMobileNumber;
    Button btnReplace;
    private static final String TAG = SimReplacement.class.getSimpleName();
    private static final String URL = "http://virtuzo.in/AhprepaidTestService/AhprepaidAPI_Json.svc/SimReplacementSerive";
    String LoginID = "", DistributorID = "";
    String strResult = "",Message="",Response="",ResponseCode="";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_replacement);

        etCurrentSIMNumber = (EditText) findViewById(R.id.etCurrentSIMNumber);
        etNewSIMNumber = (EditText) findViewById(R.id.etNewSIMNumber);
        etCurrentMobileNumber = (EditText) findViewById(R.id.etCurrentMobileNumber);
        btnReplace = (Button) findViewById(R.id.btnReplace);
        LoginID=getIntent().getStringExtra("LoginID");
        DistributorID=getIntent().getStringExtra("DistributorID");
        Log.d(TAG, "Bundle Login data :" + LoginID);
        Log.d(TAG, "Bundle Distributor data :" + DistributorID);


        btnReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCurrentSimNumber = etCurrentSIMNumber.getText().toString();
                String strNewSimNumber = etNewSIMNumber.getText().toString();
                String strCurrentMobileNumber = etCurrentMobileNumber.getText().toString();

                if (StringUtils.isBlank(strCurrentSimNumber)) {
                    Toast.makeText(getApplicationContext(), " Current SIM Number can't be blank", Toast.LENGTH_SHORT).show();
                    return;
                }else if (StringUtils.isBlank(strCurrentMobileNumber)) {
                    Toast.makeText(getApplicationContext(), " Current Mobile Number can't be blank", Toast.LENGTH_SHORT).show();
                    return;
                } else if (StringUtils.isBlank(strNewSimNumber)) {
                    Toast.makeText(getApplicationContext(), "New SIM Number can't be blank", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new PostData().execute(strCurrentSimNumber, strNewSimNumber, strCurrentMobileNumber);

                }
            }
        });
    }

    private class PostData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(SimReplacement.this);
            pd.setIndeterminate(false);
            pd.setMessage("Please Wait..");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String currentSimNumber = params[0];
            String currentMobileNumber = params[1];
            String newSimNumber = params[2];
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            String strURL = URL + "?CurrentSimNumber=" + currentSimNumber + "&CurrentMobileNumber=" + currentMobileNumber + "&NewSimNumber=" + newSimNumber + "&DistributorID=" + DistributorID + "&LoginID=" + LoginID;
            Log.d(TAG,"SimReplacement URL:"+strURL);

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strURL);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                strResult = EntityUtils.toString(httpResponse.getEntity());
                Log.d(TAG, "SimReplacement Data is:" + strResult);
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
                pd.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SimReplacement.this,DashBoardScreen.class);
        startActivity(intent);

    }
}
