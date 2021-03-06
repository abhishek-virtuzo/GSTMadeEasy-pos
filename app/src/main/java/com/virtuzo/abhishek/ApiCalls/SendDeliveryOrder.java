package com.virtuzo.abhishek.ApiCalls;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterDeliveryOrder;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.TransactionDeliveryOrder;
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

public class SendDeliveryOrder {

    private String TabCode;
    private SendDeliveryOrder.CallBack listener;
    public String errorCode = "S DO";
    String businessID;
    String jsonString;
    ArrayList<MasterDeliveryOrder> masterDeliveryOrderArrayList;
    public String resultMessage = "";
    public String apiResponse = "";
    public String urlHit = "";

    public SendDeliveryOrder(String businessID, String TabCode, SendDeliveryOrder.CallBack listener) {
        this.TabCode = TabCode;
        this.businessID = businessID;
        this.listener = listener;
        this.jsonString = isApiCall();
        new SendDeliveryOrder.apitask().execute();
    }

    private String isApiCall() {
        masterDeliveryOrderArrayList = DatabaseHandler.getInstance().getUnsyncedDeliveryOrderListFromDB();

        // optimization
        if(masterDeliveryOrderArrayList.size() == 0) {
            return MyFunctions.NO_API_CALL;
        } else {
            return MyFunctions.API_CALL;
        }
    }

    public interface CallBack {
        void afterSendDeliveryOrder();
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
                for(MasterDeliveryOrder masterDeliveryOrder : masterDeliveryOrderArrayList) {
                    jsonString = createOneJSONObject(masterDeliveryOrder);
                    jsonResponseObject = sendDataToServer(url, jsonString);
                    JSONObject responseObject = jsonResponseObject.getJSONArray("Response").getJSONObject(0);
                    message = (String) responseObject.get("Response");
                    if(message.equals(MyFunctions.SUCCESS_MESSAGE)) {
                        DatabaseHandler.getInstance().singleDeliveryOrderSyncSuccessful(masterDeliveryOrder.getID()+"");
                    } else {
                        break;
                    }
                    Log.i("Send DO Message", message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return message;
        }

        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            resultMessage = message;
            listener.afterSendDeliveryOrder();
        }

    }

    public String createOneJSONObject(MasterDeliveryOrder masterDeliveryOrder) throws JSONException {
        JSONObject exportJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // change address format

        Gson gson = new Gson();
        String jsonString = gson.toJson(masterDeliveryOrder);
        JSONObject jsonMasterObject = new JSONObject(jsonString);

        ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList =
                DatabaseHandler.getInstance().getDeliveryOrderListDetailsFromDB(masterDeliveryOrder.getID()+"");
        JSONArray jsonTransactionArray = new JSONArray();
        for(TransactionDeliveryOrder transactionDeliveryOrder : transactionDeliveryOrderArrayList) {
            Gson gson1 = new Gson();
            String jsonString1 = gson1.toJson(transactionDeliveryOrder);
            JSONObject jsonTransactionObject = new JSONObject(jsonString1);
            jsonTransactionArray.put(jsonTransactionObject);
        }
        jsonMasterObject.put("TransactionDO", jsonTransactionArray);

        Log.i("DeliveryOrders", jsonMasterObject.toString());
        jsonArray.put(jsonMasterObject);

        Log.i("DeliveryOrders", jsonArray.toString());
        exportJson.put("DeliveryOrders", jsonArray);

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
            url = Uri.parse(URL.GSTSendDeliveryOrdersURL).buildUpon()
                    .appendQueryParameter("BusinessId", businessID)
                    .appendQueryParameter("DeliveryOrders", jsonString).toString();

            urlHit = Uri.parse(URL.GSTSendDeliveryOrdersURL).buildUpon()
                    .appendQueryParameter("BusinessId", businessID).toString();

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
