package com.virtuzo.abhishek.ApiCalls;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Payment;
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

public class SendPayments {

    private String TabCode;
    private SendPayments.CallBack listener;
    public String errorCode = "SPM";
    public String apiResponse = "";
    String businessID;
    String jsonString;
    public String resultMessage = "";
    public String urlHit = "";
    ArrayList<Payment> paymentArrayList;

    public SendPayments(String businessID, String TabCode, SendPayments.CallBack listener) {
        this.TabCode = TabCode;
        this.businessID = businessID;
        this.listener = listener;
        this.jsonString = isApiCall();
        new SendPayments.apitask().execute();
    }

    private String isApiCall() {
        paymentArrayList = DatabaseHandler.getInstance().getUnsyncedPaymentsAgainstSyncedInvoices();

        // optimization
        if(paymentArrayList.size() == 0) {
            return MyFunctions.NO_API_CALL;
        } else {
            return MyFunctions.API_CALL;
        }
    }

    private String createOneJSONObject(Payment payment) {
        try {
            JSONObject exportJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            // optimization
            Gson gson = new Gson();
            String jsonString = gson.toJson(payment);
            JSONObject jsonObject = new JSONObject(jsonString);
            jsonArray.put(jsonObject);

            Log.i("Payments", jsonArray.toString());
            exportJson.put("Payments", jsonArray);

            String exportString = exportJson.toString();
            Log.i("Export JSON", exportString);
            return exportString;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface CallBack {
        void afterSendPayments();
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
            if(jsonString.equals(MyFunctions.NO_API_CALL)) {
                return MyFunctions.SUCCESS_MESSAGE;
            }
            JSONObject jsonResponseObject;
            String message = "";
            try {
                for(Payment payment : paymentArrayList) {
                    jsonString = createOneJSONObject(payment);
                    jsonResponseObject = sendDataToServer(url, jsonString);
                    JSONObject responseObject = jsonResponseObject.getJSONArray("Response").getJSONObject(0);
                    message = (String) responseObject.get("Response");
                    if(message.equals(MyFunctions.SUCCESS_MESSAGE)) {
                        DatabaseHandler.getInstance().singlePaymentSyncSuccessful(payment.getPaymentID());
                    } else {
                        break;
                    }
                }
                Log.i("Send Invoice Message", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return message;
        }

        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            resultMessage = message;
            listener.afterSendPayments();
        }

    }

    public JSONObject sendDataToServer(String url, String jsonString){
        InputStream inputStream = null;
        JSONObject result = new JSONObject();
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            url = Uri.parse(URL.GSTSendPaymentsURL).buildUpon()
                    .appendQueryParameter("BusinessID", businessID)
                    .appendQueryParameter("Payments", jsonString).toString();

            urlHit = Uri.parse(URL.GSTSendPaymentsURL).buildUpon()
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
