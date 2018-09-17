package com.virtuzo.abhishek.ApiCalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.UserLoginApi;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.CreditDebitNote;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Outlet;
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
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Aman Bansal on 14-01-2018.
 */

public class GetBusinessLogo {

    String LogoURL;
    private GetBusinessLogo.CallBack listener;
    String businessID, OutletId;
    public String resultMessage = "";
    public String errorCode = "G Logo";
    public String apiResponse = "";
    Context context;
    SharedPreferences setting;

    public GetBusinessLogo(String businessID, String OutletId, GetBusinessLogo.CallBack listener, Context context){
        this.businessID = businessID;
        this.OutletId = OutletId;
        this.listener = listener;
        this.context = context;
        new GetBusinessLogo.apitask().execute();
    }

    public interface CallBack {
        void afterGetBusinessLogo();
    }

    public class apitask extends AsyncTask<Object, Object, Void> {

        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setting = context.getSharedPreferences(UserLoginApi.PREFS_NAME, 0);

            String userNameStored = setting.getString("UserName", "");
            String passwordStored = setting.getString("Password", "");
            String clientCodeStored = setting.getString("ClientCode", "");

            url = Uri.parse(URL.GSTUserLoginURL).buildUpon()
                    .appendQueryParameter("UserName", userNameStored)
                    .appendQueryParameter("Pwd", passwordStored)
                    .appendQueryParameter("IMEI", "123") // dummy
                    .appendQueryParameter("ClientCode", clientCodeStored).toString();

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
                    if (MyFunctions.SUCCESS_MESSAGE.equals(resultMessage)) {
                        jsonObject = jsonObject.getJSONObject("Data").getJSONArray("UserDetail").getJSONObject(0);

                        Gson gson = new Gson();
                        Outlet outlet = gson.fromJson(jsonObject.toString(), Outlet.class);
                        SharedPreferences.Editor editor = setting.edit();

                        editor.putString("StateID", outlet.getStateID());
                        editor.putString("StateName", outlet.getStateName());
                        editor.putString("ContactNumber", outlet.getContactNumber());
                        editor.putString("FaxNumber", outlet.getFaxNumber());
                        editor.putString("OutletName", outlet.getOutletName());
                        editor.putString("Logo", outlet.getLogo());
                        editor.putString("isSendSMS", outlet.getIsSendSMS());
                        editor.putString("TermsAndConditions", outlet.getTermsAndConditions());
                        editor.putString("OutletCode", outlet.getOutletCode());
                        editor.putString("GSTCategory", outlet.getGSTCategory());
                        editor.putString("UserID", outlet.getUserID());
                        editor.putString("GSTCategoryID", outlet.getGSTCategoryID());
                        editor.putString("EmployeeCode", outlet.getEmployeeCode());
                        editor.putString("InvoiceSeries", outlet.getInvoiceSeries());
                        editor.putString("OutletID", outlet.getOutletID());
                        editor.putString("Address", outlet.getAddress());
                        editor.putString("FirstName", outlet.getFirstName());
                        editor.putString("GSTNumber", outlet.getGSTNumber());
                        editor.putString("RoleID", outlet.getRoleID());
                        editor.putString("OutletTypeID", outlet.getOutletTypeID());
                        editor.putString("SalesMTD", outlet.getSalesMTD());
                        editor.putString("SalesToday", outlet.getSalesToday());
                        editor.putString("CollectionsMTD", outlet.getCollectionsMTD());
                        editor.putString("CollectionsToday", outlet.getCollectionsToday());
                        editor.putString("InvoicesMTD", outlet.getInvoicesMTD());
                        editor.putString("InvoicesToday", outlet.getInvoicesToday());

                        String tnC = outlet.getTermsAndConditions();
                        String tNC = Html.fromHtml(tnC).toString();
                        tNC = Html.fromHtml(tNC).toString();
//                        tNC = MyFunctions.html2text(tNC);

                        editor.putString("TermsAndConditions", tNC);

                        editor.commit();
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
            listener.afterGetBusinessLogo();
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
