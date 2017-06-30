package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout one,two;
    Intent intent;
    String LoginID="",DistributorID="",ClientTypeID="",strTraiffResult="",strTraiffId="",strTraiffCode="",responseCode="",strMobileNumber="",strEmailId="";
    private static String TAG=MainActivity.class.getSimpleName();
    String URL="http://virtuzo.in/AhprepaidTestService/AhprepaidAPI_Json.svc/GetTariffService";
    ArrayList<String>listTraiffCode=new ArrayList<String>();
    ArrayList<String>listTraiffid=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent=getIntent();
        LoginID=intent.getStringExtra("LoginID");
        Log.d(TAG,"Login id :"+LoginID);
        DistributorID=intent.getStringExtra("DistributorID");
        Log.d(TAG,"DistributorID data:"+DistributorID);
        ClientTypeID=intent.getStringExtra("ClientTypeID");
        Log.d(TAG,"ClientTypeID data :"+ClientTypeID);
        strMobileNumber=intent.getStringExtra("MobileNumber");
        strEmailId=intent.getStringExtra("EmailID");

        new PostData().execute(LoginID,DistributorID,ClientTypeID);

        one=(RelativeLayout)findViewById(R.id.one);
        two=(RelativeLayout)findViewById(R.id.two);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActiveSIM.class);
                intent.putExtra("listTraiffCode",listTraiffCode);
                intent.putExtra("listTraiffid",listTraiffid);
                intent.putExtra("LoginID",LoginID);
                intent.putExtra("DistributorID",DistributorID);
                intent.putExtra("ClientTypeID",ClientTypeID);
                intent.putExtra("strMobileNumber",strMobileNumber);
                intent.putExtra("strEmailId",strEmailId);
                startActivity(intent);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,Portln.class);
                intent1.putExtra("ClientTypeID",ClientTypeID);
                intent1.putExtra("DistributorID",DistributorID);
                intent1.putExtra("listTraiffid",listTraiffid);
                intent1.putExtra("LoginID",LoginID);
                intent1.putExtra("strEmailId",strEmailId);
                intent1.putExtra("listTraiffCode",listTraiffCode);
                startActivity(intent1);
            }
        });

        FloatingActionsMenu floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        View float_activate = findViewById(R.id.float_activate);
        View float_portin = findViewById(R.id.float_portin);
        View float_replacement = findViewById(R.id.float_replacement);

        float_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentactive=new Intent(MainActivity.this,ActiveSIM.class);
                intentactive.putExtra("listTraiffCode",listTraiffCode);
                intentactive.putExtra("listTraiffid",listTraiffid);
                intentactive.putExtra("LoginID",LoginID);
                intentactive.putExtra("DistributorID",DistributorID);
                intentactive.putExtra("ClientTypeID",ClientTypeID);
                intentactive.putExtra("strMobileNumber",strMobileNumber);
                intentactive.putExtra("strEmailId",strEmailId);
                startActivity(intentactive);
            }
        });

        float_portin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPortin=new Intent(MainActivity.this,Portln.class);
                intentPortin.putExtra("ClientTypeID",ClientTypeID);
                intentPortin.putExtra("DistributorID",DistributorID);
                intentPortin.putExtra("listTraiffid",listTraiffid);
                intentPortin.putExtra("LoginID",LoginID);
                intentPortin.putExtra("strEmailId",strEmailId);
                intentPortin.putExtra("listTraiffCode",listTraiffCode);
                startActivity(intentPortin);
            }
        });
        float_replacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReplacement=new Intent(MainActivity.this,SimReplacement.class);
                startActivity(intentReplacement);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        View header=navigationView.getHeaderView(0);
        TextView txtDashBoard=(TextView)header.findViewById(R.id.txtDashBoard);
        txtDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDashBoard=new Intent(MainActivity.this,DashBoardScreen.class);
                intentDashBoard.putExtra("listTraiffCode",listTraiffCode);
                intentDashBoard.putExtra("listTraiffid",listTraiffid);
                intentDashBoard.putExtra("LoginID",LoginID);
                intentDashBoard.putExtra("DistributorID",DistributorID);
                intentDashBoard.putExtra("ClientTypeID",ClientTypeID);
                intentDashBoard.putExtra("strMobileNumber",strMobileNumber);
                intentDashBoard.putExtra("strEmailId",strEmailId);
                startActivity(intentDashBoard);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.activate_sim) {
            Intent intentActiveSim=new Intent(MainActivity.this,ActiveSIM.class);
            intentActiveSim.putExtra("listTraiffCode",listTraiffCode);
            intentActiveSim.putExtra("listTraiffid",listTraiffid);
            intentActiveSim.putExtra("LoginID",LoginID);
            intentActiveSim.putExtra("DistributorID",DistributorID);
            intentActiveSim.putExtra("ClientTypeID",ClientTypeID);
            intentActiveSim.putExtra("strMobileNumber",strMobileNumber);
            intentActiveSim.putExtra("strEmailId",strEmailId);

            startActivity(intentActiveSim);
            // Handle the camera action
        } else if (id == R.id.portin) {
            Intent intentPoIntent=new Intent(MainActivity.this,Portln.class);
            intentPoIntent.putExtra("ClientTypeID",ClientTypeID);
            intentPoIntent.putExtra("DistributorID",DistributorID);
            intentPoIntent.putExtra("listTraiffid",listTraiffid);
            intentPoIntent.putExtra("LoginID",LoginID);
            intentPoIntent.putExtra("strEmailId",strEmailId);
            intentPoIntent.putExtra("listTraiffCode",listTraiffCode);
            startActivity(intentPoIntent);

        } else if (id == R.id.sim_replacement) {
            Intent intentSimReplacement=new Intent(MainActivity.this,SimReplacement.class);
            startActivity(intentSimReplacement);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class PostData  extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            String strLoginId=params[0];
            String strDistributionID=params[1];
            String strClientTypeId=params[2];
            List<NameValuePair> nameValuePairs=new ArrayList<>(3);
            String url = URL +"?LoginID="+ strLoginId +"&DistributorID=" + strDistributionID + "&ClientTypeID=" +strClientTypeId;
            Log.e(TAG, "doInBackground: url " + url);
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse=httpClient.execute(httpPost);
                strTraiffResult= EntityUtils.toString(httpResponse.getEntity());
                Log.e(TAG, "Traiff Result " + strTraiffResult);
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                JSONArray jsonArray=new JSONArray(strTraiffResult);
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    strTraiffId=jsonObject.getString("TariffID");
                    Log.d(TAG,"StrTraiff id :"+strTraiffId);
                    strTraiffCode=jsonObject.getString("TariffCode");
                    Log.d(TAG,"strTraiffcode is:"+strTraiffCode);
                    responseCode=jsonObject.getString("ResponseCode");
                    Log.d(TAG,"Responce code is:"+responseCode);
                    listTraiffCode.add(strTraiffCode);
                    listTraiffid.add(strTraiffId);


                }

            }catch (Exception  e){
                e.printStackTrace();
            }
            return strTraiffResult;
        }
    }
}
