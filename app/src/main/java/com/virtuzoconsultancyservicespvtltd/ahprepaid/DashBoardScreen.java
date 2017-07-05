package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashBoardScreen extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = DashBoardScreen.class.getSimpleName();
    FrameLayout frameText,frameSimActive, PortIn, framedateAndTime, frameSimReplacement, frameTopupBalance, frameRechargeSim;
    TextView dateAndTime,txtName,txtPost,txtTopUp, currentBalance;
    String strTopUp="",LastName="",DateAndTime="", FirstName="",Post="",LoginID = "", DistributorID = "", ClientTypeID = "", strMobileNumber = "", strEmailId = "", strTraiffResult = "", strTraiffId = "", strTraiffCode = "", responseCode = "",strLoginId="",strDistributorID="",emailid="",strFirstname="",strPost="",TotalTopup="";
    String TariffServiceURL = com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.URL.TariffServiceURL;
    ArrayList<String> listTraiffCode = new ArrayList<String>();
    ArrayList<String> listTraiffid = new ArrayList<String>();
    Button btnLogout;
    ImageView imgDateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_screen);

        getSupportActionBar().hide();

        Toolbar toolbar_dashbord=(Toolbar)findViewById(R.id.toolbar_dashboard);
        btnLogout=(Button)toolbar_dashbord.findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences setting=getSharedPreferences(PREFS_NAME,0);
                SharedPreferences.Editor editor=setting.edit();
                editor.remove("logged");
                editor.remove("LoginID");
                editor.remove("ClientTypeID");
                editor.remove("DistributorID");
                editor.remove("MobileNumber");
                editor.remove("EmailID");
                editor.remove("FirstName");
                editor.remove("Post");
                editor.remove("TotalTopup");
                editor.remove("LastName");
                editor.commit();
                Intent intentLogin=new Intent(DashBoardScreen.this,Login.class);
                startActivity(intentLogin);
                /*finish();*/

            }
        });

        frameSimActive = (FrameLayout) findViewById(R.id.frameSimActive);
        PortIn = (FrameLayout) findViewById(R.id.PortIn);
        framedateAndTime = (FrameLayout) findViewById(R.id.framedateAndTime);
        frameSimReplacement = (FrameLayout) findViewById(R.id.frameSimReplacement);
        frameTopupBalance = (FrameLayout) findViewById(R.id.frameTopupBalance);
        frameRechargeSim = (FrameLayout) findViewById(R.id.frameRechargeSim);
        frameText=(FrameLayout)findViewById(R.id.frameText);


        txtName=(TextView)frameText.findViewById(R.id.txtName);
        txtPost=(TextView)frameText.findViewById(R.id.txtPost);
      //  txtTopUp=(TextView)frameText .findViewById(R.id.txtTopUp);
        imgDateAndTime=(ImageView)framedateAndTime.findViewById(R.id.imgDateAndTime);
        currentBalance =(TextView)frameText.findViewById(R.id.textView2);


        SharedPreferences data=getSharedPreferences(PREFS_NAME,0);
        if (data.contains("LoginID")){
            LoginID=data.getString("LoginID","");
            Log.d(TAG,"shared login id:"+strLoginId);
        }
        if (data.contains("DistributorID")){
            DistributorID=data.getString("DistributorID","");
            Log.d(TAG,"shared distributor id:"+strDistributorID);
        }if (data.contains("MobileNumber")){
            strMobileNumber=data.getString("MobileNumber","");
            Log.d(TAG,"shared mobile no:"+strMobileNumber);
        }if (data.contains("EmailID")){
            strEmailId=data.getString("EmailID","");
            Log.d(TAG,"shared emailid:"+emailid);
        }if (data.contains("FirstName")){
            FirstName=data.getString("FirstName","");
            Log.d(TAG,"shared firstname:"+strFirstname);
        }if (data.contains("Post")) {
            Post = data.getString("Post", "");
            Log.d(TAG, "shared post data:" + strPost);
        }if (data.contains("ClientTypeID")) {
            ClientTypeID = data.getString("ClientTypeID", "");
            Log.d(TAG, "shared post data:" + strPost);
        }if (data.contains("TotalTopup")) {
            TotalTopup = data.getString("TotalTopup", "");
            Log.d(TAG, "shared post topup data is:" + TotalTopup);
        }if (data.contains("LastName")){
            LastName=data.getString("LastName","");
            Log.d(TAG,"shared post LastName data is:"+LastName);
        }else {
            LoginID = getIntent().getStringExtra("LoginID");
            Log.d(TAG, "LoginId" + LoginID);
            DistributorID = getIntent().getStringExtra("DistributorID");
            Log.d(TAG, "DistributorID" + DistributorID);
            ClientTypeID = getIntent().getStringExtra("ClientTypeID");
            Log.d(TAG, "listtraiff data :" + listTraiffCode);
            strEmailId = getIntent().getStringExtra("strEmailId");
            strMobileNumber = getIntent().getStringExtra("strMobileNumber");
            Log.d(TAG, "Mobile No :" + strMobileNumber);
            FirstName=getIntent().getStringExtra("FirstName");
            Post=getIntent().getStringExtra("Post");
            TotalTopup=getIntent().getStringExtra("TotalTopup");
            Toast.makeText(getApplicationContext(),TotalTopup,Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Balance is:"+TotalTopup);
            LastName=getIntent().getStringExtra("LastName");

        }


        txtName.setText(FirstName);

            txtPost.setText(strEmailId);



        if(!currentBalance.equals("")){
            currentBalance.setText("Amount"+"\n"+"$"+TotalTopup);
        }
        if (!ClientTypeID.matches("1")){
            strTopUp=getIntent().getStringExtra("Topup");
            SharedPreferences setting=getSharedPreferences(PREFS_NAME,0);
            SharedPreferences.Editor editor=setting.edit();
            editor.putString("strTopUpBalance",strTopUp);
            editor.commit();
            Log.d(TAG,"strTopUP balance is:"+strTopUp);
            if (strTopUp!=null){
              //  txtTopUp.setText("$"+strTopUp);
            }else {
               // txtTopUp.setText("$"+TotalTopup);
            }
        }else {
           // txtTopUp.setText("");
        }

        new PostData().execute(LoginID, DistributorID, ClientTypeID);


        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        dateAndTime = (TextView) framedateAndTime.findViewById(R.id.dateAndTime);
        dateAndTime.setText(date);
        DateAndTime=dateAndTime.getText().toString();

        framedateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Today "+DateAndTime,Toast.LENGTH_SHORT).show();
            }
        });

        frameRechargeSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RechargeActivity.class);
                startActivity(intent);
            }
        });


        frameSimActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSimActive = new Intent(DashBoardScreen.this, ActiveSIM.class);
                intentSimActive.putExtra("listTraiffCode", listTraiffCode);
                intentSimActive.putExtra("listTraiffid", listTraiffid);
                intentSimActive.putExtra("LoginID", LoginID);
                intentSimActive.putExtra("DistributorID", DistributorID);
                intentSimActive.putExtra("ClientTypeID", ClientTypeID);
                intentSimActive.putExtra("strEmailId", strEmailId);
                intentSimActive.putExtra("strMobileNumber", strMobileNumber);

                startActivity(intentSimActive);
            }
        });

        PortIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPortLn = new Intent(DashBoardScreen.this, Portln.class);
                intentPortLn.putExtra("ClientTypeID", ClientTypeID);
                intentPortLn.putExtra("DistributorID", DistributorID);
                intentPortLn.putExtra("listTraiffid", listTraiffid);
                intentPortLn.putExtra("LoginID", LoginID);
                intentPortLn.putExtra("strEmailId", strEmailId);
                intentPortLn.putExtra("listTraiffCode", listTraiffCode);
                startActivity(intentPortLn);
            }
        });

        frameSimReplacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSimReplacement = new Intent(DashBoardScreen.this, SimReplacement.class);
                intentSimReplacement.putExtra("DistributorID",DistributorID);
                intentSimReplacement.putExtra("LoginID",LoginID);
                startActivity(intentSimReplacement);
            }
        });

        frameTopupBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTopUpBalance = new Intent(DashBoardScreen.this, PayPalActivity.class);
                intentTopUpBalance.putExtra("DistributorID",DistributorID);
                intentTopUpBalance.putExtra("LoginID",LoginID);
                intentTopUpBalance.putExtra("ClientTypeID",ClientTypeID);
                intentTopUpBalance.putExtra("DateAndTime",DateAndTime);
                intentTopUpBalance.putExtra("FirstName",FirstName);
                intentTopUpBalance.putExtra("TotalTopup",TotalTopup);
                startActivity(intentTopUpBalance);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private class PostData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String strLoginId = params[0];
            String strDistributionID = params[1];
            String strClientTypeId = params[2];
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            String url = TariffServiceURL + "?LoginID=" + strLoginId + "&DistributorID=" + strDistributionID + "&ClientTypeID=" + strClientTypeId;
            Log.e(TAG, "doInBackground: url " + url);
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                strTraiffResult = EntityUtils.toString(httpResponse.getEntity());
                Log.e(TAG, "Traiff Result " + strTraiffResult);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(strTraiffResult);
                for (int i = 0; i <jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    strTraiffId = jsonObject.getString("TariffID");
                    Log.d(TAG, "StrTraiff id :" + strTraiffId);
                    strTraiffCode = jsonObject.getString("TariffCode");
                    Log.d(TAG, "strTraiffcode is:" + strTraiffCode);
                    responseCode = jsonObject.getString("ResponseCode");
                    Log.d(TAG, "Responce code is:" + responseCode);
                    listTraiffCode.add(strTraiffCode);
                    listTraiffid.add(strTraiffId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return strTraiffResult;
        }
    }
}
