package com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.SelectOperatorActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiraj on 7/4/2017.
 */

public class GetOperators {

    public List<OperatorClass> operatorList;

    public GetOperators() {

        new apitask().execute();

    }


    public class apitask extends AsyncTask<Object, Object, Void> {

        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            operatorList = new ArrayList<>();

            url = com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.URL.Server + "/GetNetwork";//"http://virtuzo.in/AhprepaidService/AhprepaidAPI_Json.svc/InitiateTopupPayment?loginID="+loginid+"&DistributorID="+distributorid+"&ChargedAmount="+total;//http://virtuzo.in/AhprepaidService/AhprepaidAPI_Json.svc/InitiateTopupPayment?loginID=1&DistributorID=1&ChargedAmount=1
            Log.d("check", "inside API" + url);

        }

        @Override
        protected Void doInBackground(Object... params) {

            Log.d("check", "on doinbackground...");

            try {

                JSONObject jsonObject = getJSONFromUrlPost(url);

                if (jsonObject != null) {

                    JSONArray jsonarray = jsonObject.getJSONArray("Data");

                    for (int i = 0; i < jsonarray.length(); i++) {

                        OperatorClass operator;
                        Gson gson = new Gson();

                        String jsonString = jsonarray.getJSONObject(i).toString();

                        operator = gson.fromJson(jsonString, OperatorClass.class);

                        operatorList.add(operator);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            SelectOperatorActivity.progressDialog.dismiss();

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
