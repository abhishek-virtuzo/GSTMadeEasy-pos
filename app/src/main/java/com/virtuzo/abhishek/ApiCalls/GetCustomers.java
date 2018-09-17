package com.virtuzo.abhishek.ApiCalls;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.Customer;
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
 * Created by Abhishek Aggarwal on 09-07-2017.
 */

public class GetCustomers {

    public ArrayList<Customer> customerList;
    private GetCustomers.CallBack listener;
    String businessID;
    public String errorCode = "GCT";
    public String apiResponse = "";
    public String resultMessage = "";

    public GetCustomers(String businessID, GetCustomers.CallBack listener){//}, DatabaseHandler dbHandler) {

        this.businessID = businessID;
        this.listener = listener;

//        this.dbHandler = dbHandler;
//        ProductListActivity.progressDialog.dismiss();
//        productList = new ArrayList<>(this.dbHandler.getProductsListFromDB());
//        productList = new ArrayList<>(this.dbHandler.getProductList());

        new GetCustomers.apitask().execute();

    }

    public interface CallBack {
        void afterGetCustomers();
    }

    public class apitask extends AsyncTask<Object, Object, Void> {

        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            customerList = new ArrayList<>();
            url = Uri.parse(URL.GSTGetCustomersURL).buildUpon().appendQueryParameter("BusinessId", ""+businessID).toString();

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

                    JSONArray jsonarray = jsonObject.getJSONArray("Data");

                    for (int i = 0; i < jsonarray.length(); i++) {

                        Customer customer;
                        Gson gson = new Gson();

                        String jsonString = jsonarray.getJSONObject(i).toString();
                        customer = gson.fromJson(jsonString, Customer.class);
                        customer.setSynced(DatabaseHandler.CUSTOMER_SYNCED_CODE_Synced);
                        customerList.add(customer);

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
            listener.afterGetCustomers();
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
                Log.e("check", "hsn_json is " + json);
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