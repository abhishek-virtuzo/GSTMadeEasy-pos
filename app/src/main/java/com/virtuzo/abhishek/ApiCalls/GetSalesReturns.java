package com.virtuzo.abhishek.ApiCalls;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterSalesReturn;
import com.virtuzo.abhishek.modal.TransactionSalesReturn;
import com.virtuzo.abhishek.utils.URL;

import org.apache.commons.lang3.StringEscapeUtils;
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

/**
 * Created by Aman Bansal on 14-01-2018.
 */

public class GetSalesReturns {

    public ArrayList<MasterSalesReturn> masterSalesReturnArrayList;
    public ArrayList<TransactionSalesReturn> transactionSalesReturnArrayList;
    private GetSalesReturns.CallBack listener;
    String businessID, OutletId, RoleID, OutletTypeID;
    public String resultMessage = "";
    public String errorCode = "G SR";
    public String apiResponse = "";

    public GetSalesReturns(String businessID, String OutletId, String RoleID, String OutletTypeID, GetSalesReturns.CallBack listener){
        this.businessID = businessID;
        this.OutletId = OutletId;
        this.RoleID = RoleID;
        this.OutletTypeID = OutletTypeID;
        this.listener = listener;
        new GetSalesReturns.apitask().execute();
    }

    public interface CallBack {
        void afterGetSalesReturn();
    }

    public class apitask extends AsyncTask<Object, Object, Void> {

        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            masterSalesReturnArrayList = new ArrayList<>();
            transactionSalesReturnArrayList = new ArrayList<>();

            if(RoleID.equals("6") && OutletTypeID.equals("1")) {
                url = Uri.parse(URL.GSTGetSalesReturnsURL).buildUpon()
                        .appendQueryParameter("BusinessId", "" + businessID)
                        .appendQueryParameter("OutletId", "0").toString();
            } else {
                url = Uri.parse(URL.GSTGetSalesReturnsURL).buildUpon()
                        .appendQueryParameter("BusinessId", "" + businessID)
                        .appendQueryParameter("OutletId", "" + OutletId).toString();
            }

            Log.d("check", "API : " + url);
        }

        @Override
        protected Void doInBackground(Object... params) {
            Log.d("check", "on doinbackground...");
            try {
                JSONObject jsonObject = getJSONFromUrlPost(url);
                Log.d("check", jsonObject+"");

                if (jsonObject != null) {

                    resultMessage = jsonObject.getJSONArray("Response").getJSONObject(0).getString("Response");
                    JSONObject dataJsonObject = jsonObject.getJSONObject("Data");

                    JSONArray jsonArray = dataJsonObject.getJSONArray("SalesReturn");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MasterSalesReturn masterSalesReturn;
                        Gson gson = new Gson();
                        String jsonString = jsonArray.getJSONObject(i).toString();
                        masterSalesReturn = gson.fromJson(jsonString, MasterSalesReturn.class);

                        // SalesReturn date
//                        masterSalesReturn.setSalesReturnDate(MyFunctions.convertServerReceievedDateToDateFormat(masterSalesReturn.getSalesReturnDate()));
                        masterSalesReturn.setSynced(DatabaseHandler.M_PURCHASE_SYNCED_CODE_Synced);
                        masterSalesReturnArrayList.add(masterSalesReturn);
                    }
                    jsonArray = dataJsonObject.getJSONArray("TransactionSalesReturn");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TransactionSalesReturn transactionSalesReturn;
                        Gson gson = new Gson();
                        String jsonString = jsonArray.getJSONObject(i).toString();
                        transactionSalesReturn = gson.fromJson(jsonString, TransactionSalesReturn.class);
                        transactionSalesReturnArrayList.add(transactionSalesReturn);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            listener.afterGetSalesReturn();
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
                apiResponse = url + "\n" + sb.toString();
                Log.e("check", "response is " + sb);
                json = sb.toString();
                json = StringEscapeUtils.unescapeJson(json);
                json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
                Log.i("check", "hsn_json is " + json);
                return new JSONObject(json);
            } catch (Exception e) {
                Log.e("check", "Error converting result " + e.toString());
                apiResponse = url + "\n" + "Error converting result " + e.toString();
            }
            // try parse the string to a JSON object
            JSONObject jObj = null;

            try {
                jObj = new JSONObject(StringEscapeUtils.unescapeJson(json));
            } catch (JSONException e) {
                Log.e("check", "Error parsing data " + e.toString());
            }
            return jObj;

        }

    }
}
