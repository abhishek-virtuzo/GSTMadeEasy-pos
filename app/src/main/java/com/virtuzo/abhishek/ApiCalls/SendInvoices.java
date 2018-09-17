package com.virtuzo.abhishek.ApiCalls;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterSales;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Payment;
import com.virtuzo.abhishek.modal.TransactionSales;
import com.virtuzo.abhishek.utils.URL;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Abhishek Aggarwal on 12/21/2017.
 */

public class SendInvoices {

    private String TabCode;
    private SendInvoices.CallBack listener;
    public String errorCode = "SSI";
    public String apiResponse = "";
    String businessID;
    String jsonString;
    ArrayList<MasterSales> masterSalesArrayList;
    public String resultMessage = "";
    public String urlHit = "";

    public SendInvoices(String businessID, String TabCode, SendInvoices.CallBack listener) {
        this.TabCode = TabCode;
        this.businessID = businessID;
        this.listener = listener;
        this.jsonString = isApiCall();
        new SendInvoices.apitask().execute();
    }

    private String isApiCall() {
        masterSalesArrayList = DatabaseHandler.getInstance().getUnsyncedInvoiceListFromDB();

        // optimization
        if(masterSalesArrayList.size() == 0) {
            return MyFunctions.NO_API_CALL;
        } else {
            return MyFunctions.API_CALL;
        }
    }

    public interface CallBack {
        void afterSendInvoices();
    }

    public class apitask extends AsyncTask<Object, Object, String> {
        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("check", "API : " + url);
        }

        @Override
        protected String doInBackground(Object... params) {
            Log.d("check", "on doinbackground...");
            JSONObject jsonResponseObject;
            String message = "";
            if(jsonString.equals(MyFunctions.NO_API_CALL)) {
                return MyFunctions.SUCCESS_MESSAGE;
            } else {
                try {
                    // optimization
                    for (MasterSales masterSales : masterSalesArrayList) {
                        // change address format
                        jsonString = createOneJSONObject(masterSales);
                        jsonResponseObject = sendDataToServer(url, jsonString);
                        JSONObject responseObject = jsonResponseObject.getJSONArray("Response").getJSONObject(0);
                        message = (String) responseObject.get("Response");
                        if(message.equals(MyFunctions.SUCCESS_MESSAGE)) {
                            DatabaseHandler.getInstance().singleSalesInvoiceSyncSuccessful(masterSales.getReferenceNumber());
                        } else {
                            break;
                        }
                        Log.i("Send Invoice Message", message);
                    }
                } catch (Exception e) {

                }
            }
            return message;
        }

        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            resultMessage = message;
            listener.afterSendInvoices();
        }

    }

    public String createOneJSONObject(MasterSales masterSales) throws JSONException {
        String address = masterSales.getOutletAddress().replace("###", ",");
        masterSales.setOutletAddress(address);
        Gson gson = new Gson();
        String jsonString = gson.toJson(masterSales);
        JSONObject exportJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonMasterObject = new JSONObject(jsonString);

        ArrayList<TransactionSales> transactionSalesArrayList =
                DatabaseHandler.getInstance().getInvoiceDetailsFromDB(masterSales.getReferenceNumber());
        JSONArray jsonTransactionArray = new JSONArray();
        for (TransactionSales transactionSales : transactionSalesArrayList) {
            Gson gson1 = new Gson();
            String jsonString1 = gson1.toJson(transactionSales);
            JSONObject jsonTransactionObject = new JSONObject(jsonString1);
            jsonTransactionArray.put(jsonTransactionObject);
        }
        jsonMasterObject.put("TransactionSales", jsonTransactionArray);

        ArrayList<Payment> paymentArrayList =
                DatabaseHandler.getInstance().getPaymentListDetailsFromDB(masterSales.getReferenceNumber());
        JSONArray jsonPaymentsArray = new JSONArray();
        for (Payment payment : paymentArrayList) {
            Gson gson1 = new Gson();
            String jsonString1 = gson1.toJson(payment);
            JSONObject jsonPaymentObject = new JSONObject(jsonString1);
            jsonPaymentsArray.put(jsonPaymentObject);
        }
        jsonMasterObject.put("Payments", jsonPaymentsArray);

        Log.i("Invoice", jsonMasterObject.toString());
        jsonArray.put(jsonMasterObject);
        Log.i("Invoices", jsonArray.toString());
        exportJson.put("Invoices", jsonArray);

        String exportString = exportJson.toString();
        Log.i("Export JSON", exportString);
        return exportString;
    }

    public JSONObject sendDataToServer(String url, String jsonString){
        InputStream inputStream = null;
        JSONObject result = new JSONObject();
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            url = Uri.parse(URL.GSTSendInvoicesURL).buildUpon()
                    .appendQueryParameter("BusinessID", businessID)
                    .appendQueryParameter("Invoices", jsonString).toString();

            urlHit = Uri.parse(URL.GSTSendInvoicesURL).buildUpon()
                    .appendQueryParameter("BusinessID", businessID).toString();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. build jsonObject
                // already built

            // 4. convert JSONObject to JSON to String
                // already built

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // hsn_json = mapper.writeValueAsString(person);

            // 5. set hsn_json to StringEntity

            // 6. set httpPost Entity

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/hsn_json");
            httpPost.setHeader("Content-type", "application/hsn_json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToResponseObject(inputStream);

        } catch (Exception e) {
            Log.d("InputStream", result+"");
        }

        // 11. return result
        return result;
    }

    private JSONObject convertInputStreamToResponseObject(InputStream is) throws IOException{
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
            apiResponse = urlHit + "\n" + jsonString + "\n\n" + sb.toString();
            Log.e("check", "response is " + sb);
            json = sb.toString();
            json = StringEscapeUtils.unescapeJson(json);
            json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
            Log.e("check", "hsn_json is " + json);
            return new JSONObject(json);
        } catch (Exception e) {
            Log.e("check", "Error converting result " + e.toString());
            apiResponse = urlHit + "\n" + jsonString + "\n\n" + "Error converting result " + e.toString();
        }
        // try parse the string to a JSON object
        JSONObject jObj = new JSONObject();

        try {
            jObj = new JSONObject(StringEscapeUtils.unescapeJson(json));
        } catch (JSONException e) {
            Log.e("check", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
