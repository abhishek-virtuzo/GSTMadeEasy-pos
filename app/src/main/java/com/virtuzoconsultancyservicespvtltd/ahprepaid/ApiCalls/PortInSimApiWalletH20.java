package com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.PortConfirmActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Abhishek on 09-07-2017.
 */
public class PortInSimApiWalletH20 {


    public String response;
    public int responsecode;
    String clientid;
    String tariffid;
    String distributorid;
    String loginid;
    String iswallet;
    String month;
    String plan, amount, email, operator, zipcode, simcardno;
    String PhonePort;
    String balance;
    String Pin;
    String AccountNumber;
    String city;
    String serviceProvider;
    String state;
    String customerName;
    String Address;
    Context context;

    public PortInSimApiWalletH20(Context context, String plan, String amount, String email, String operator, String zipcode, String simcardno,
                                 String clientid, String loginid, String distributorid, String iswallet,
                                 String PhonePort, String tariffid, String accountNo, String pin,
                                 String city, String serviceProvider, String state, String customerName, String Address) {

        this.context = context;
        this.plan = plan;
        this.amount = amount;
        this.email = email;
        this.operator = operator;
        this.zipcode = zipcode;
        this.simcardno = simcardno;
        this.clientid = clientid;
        this.loginid = loginid;
        this.distributorid = distributorid;
        this.iswallet = iswallet;
        this.PhonePort = PhonePort;
        this.tariffid = tariffid;
        this.Pin = pin;
        this.AccountNumber = accountNo;
        this.city = city;
        this.serviceProvider = serviceProvider;
        this.state = state;
        this.customerName = customerName;
        this.Address = Address;
        new ApiCall().execute();
    }

    public class ApiCall extends AsyncTask<Object, Object, Void> {
        String errormesage;
        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            try {
                plan = URLEncoder.encode(plan, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            url = URL.Server + "/PortInSIMForH2O?" +
                    "ClientTypeID=" + clientid +
                    "&DistributorID=" + distributorid +
                    "&TariffID=" + tariffid +
                    "&SimNumber=" + simcardno +
                    "&TariffAmount=" + amount +
                    "&LoginID=" + loginid +
                    "&EmailID=" + email +
                    "&ZipCode=" + zipcode +
                    "&City=" + city +
                    "&PhoneToPort=" + PhonePort +
                    "&AccountNumber=" + AccountNumber +
                    "&PIN=" + Pin +
                    "&ServiceProvider=" + serviceProvider +
                    "&State=" + state +
                    "&CustomerName=" + customerName +
                    "&Address=" + Address +
                    "&IsWalet=" + iswallet;
// http://virtuzo.in/AHPrepaidMultipleNetworkService/AhprepaidAPI_Json.svc
// /PortInSIMForH2O?ClientTypeID=2&DistributorID=3173&TariffID=11&SimNumber=8919601000180902427&TariffAmount=10&
// LoginID=30196&EmailID=shadab.a@virtuzo.in&ZipCode=95222&City=Newyark&PhoneToPort=8756565656&PIN=23232323&
// ServiceProvider=63443&State=California&CustomerName=HARJEET singh&Address=New Yark&IsWalet=1


            Log.d("check", "inside API" + url);

        }

        @Override
        protected Void doInBackground(Object... params) {
            Log.d("check", "on doinbackground...");
            try {
                JSONObject jsonObject = getJSONFromUrlPost(url);
                if (jsonObject != null) {

                    JSONArray jsonarray = jsonObject.getJSONArray("Response");

                    JSONObject jsonObject1 = jsonarray.getJSONObject(0);
                    response = jsonObject1.getString("Message");
                    responsecode = jsonObject1.getInt("Responsecode");

                    Log.d("eiraj", "value of json object is..." + response);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            PortConfirmActivity.progressDialog.dismiss();
        }


        public JSONObject getJSONFromUrlPost(String url) {

            // Making HTTP request
            InputStream is = null;

            try {
                // defaultHttpClient
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 25000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 25000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                HttpPost httpPost = new HttpPost(url);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                Log.d("check", "before http response is " + httpClient.toString());
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.d("check", "http response is " + httpResponse.toString());
                HttpEntity httpEntity = httpResponse.getEntity();
                Log.d("check", "http entity is " + httpEntity.toString());
                is = httpEntity.getContent();
                Log.d("check", "response is " + is);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e("check", "Error unsupported encoding exception  " + e.toString());
            } catch (ClientProtocolException e) {
                Log.e("check", "Error client protocol exception  " + e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("check", "Error io exception  " + e.toString());
                e.printStackTrace();
            }

            String json = "";
            try {
                Log.e("check", "after is get " + is);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                Log.e("check", "response is " + sb);
                json = sb.toString();
                json = json.replace("\\", "");
                json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
                Log.e("check", "json is " + json);
                return new JSONObject(json);
            } catch (Exception e) {
                Log.e("check", "Error converting result " + e.toString());
            }
            // try parse the string to a JSON object
            JSONObject jObj = new JSONObject();
            try {
                jObj = new JSONObject(json.replace("\\", ""));
            } catch (JSONException e) {
                Log.e("check", "Error parsing data " + e.toString());
            }
            return jObj;


        }

    }


}