package com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls;

import android.os.AsyncTask;
import android.util.Log;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.ConfirmRechargeActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.PlanClass;
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

public class ConfirmRecharge {

    public String responseMessage;
    public int responseCode;


    String paymnetId;
    OperatorClass operator;
    PlanClass plan;
    String mobileno;
    String email;
    String zipcode;
    String paypalPaymentId;
    int rechargeVia;
    String distributorId;
    String loginId;
    int iswallet;


    public ConfirmRecharge(String paymnetId, OperatorClass operator, PlanClass plan, String mobileno, String email, String zipcode,
                           String paypalPaymentId, int rechargeVia, String distributorId, String loginId, int iswallet) {

        this.paymnetId = paymnetId;
        this.operator = operator;
        this.plan = plan;
        this.mobileno = mobileno;
        this.email = email;
        this.zipcode = zipcode;
        this.paypalPaymentId = paypalPaymentId;
        this.rechargeVia = rechargeVia;
        this.distributorId = distributorId;
        this.loginId = loginId;
        this.iswallet = iswallet;
        new ConfirmRecharge.apitask().execute();

    }


    public class apitask extends AsyncTask<Object, Object, Void> {

        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            responseCode = -1;

            String planDescription = plan.getProductDescription();

            try {
                planDescription = URLEncoder.encode(planDescription, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            url = URL.Server + "/Recharge" +
                    "?PaymentID=" + paymnetId +
                    "&NetworkID=" + operator.getVendorID() +
                    "&TariffCode=" + plan.getTariffCode() +
                    "&MobileNo=" + mobileno +
                    "&TotalAmount=" + plan.getTotalAmount() +
                    "&EmailID=" + email +
                    "&RechargeAmount=" + plan.getRechargeAmount() +
                    "&State=" +
                    "&ZIPCode=" + zipcode +
                    "&TxnID=" + paypalPaymentId +
                    "&Tax=0" +
                    "&Regulatery=" + plan.getRegulatry() +
                    "&DistributorID=" + distributorId +
                    "&LoginID=" + loginId +
                    "&RechargeVia=" + rechargeVia +
                    "&PlanDescription=" + planDescription +
                    "&IsWalet=" + iswallet;

            responseMessage = "Something went wromng!! Please try again later";

            Log.d("check", "inside API" + url);

        }

        @Override
        protected Void doInBackground(Object... params) {

            Log.d("check", "on doinbackground...");

            try {

                JSONObject jsonObject = getJSONFromUrlPost(url);

                JSONArray responseArray = jsonObject.getJSONArray("Response");
                JSONObject response = responseArray.getJSONObject(0);
                responseCode = response.getInt("Responsecode");
                responseMessage = response.getString("Response");



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            ConfirmRechargeActivity.progressDialog.dismiss();

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
            JSONObject jObj = null;

            try {
                jObj = new JSONObject(json.replace("\\", ""));
            } catch (JSONException e) {
                Log.e("check", "Error parsing data " + e.toString());
            }
            return jObj;

        }

    }


}
