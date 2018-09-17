package com.virtuzo.abhishek.ApiCalls;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.Brand;
import com.virtuzo.abhishek.modal.MyFunctions;
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

public class SendBrands {

    private String TabCode;
    private SendBrands.CallBack listener;
    public String errorCode = "SBR";
    public String apiResponse = "";
    String businessID;
    String jsonString;
    public String resultMessage = "";
    public String urlHit = "";

    public SendBrands(String businessID, SendBrands.CallBack listener) {
        this.TabCode = TabCode;
        this.businessID = businessID;
        this.listener = listener;
        this.jsonString = createJSONObject();
        new SendBrands.apitask().execute();
    }

    private String createJSONObject() {
        try {
            JSONObject exportJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            ArrayList<Brand> brandArrayList = DatabaseHandler.getInstance().getUnsyncedBrands();

            // optimization
            if(brandArrayList.size() == 0) {
                return MyFunctions.NO_API_CALL;
            }
            // TODO hsn_json work
            for(Brand brand : brandArrayList) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(brand);
                JSONObject jsonObject = new JSONObject(jsonString);
                jsonArray.put(jsonObject);
            }
            Log.i("Brands", jsonArray.toString());
            exportJson.put("Brands", jsonArray);

            String exportString = exportJson.toString();
            Log.i("Export JSON", exportString);

            return exportString;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface CallBack {
        void afterSendBrands();
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
                jsonResponseObject = sendDataToServer(url, jsonString);
                JSONObject responseObject = jsonResponseObject.getJSONArray("Response").getJSONObject(0);
                message = (String) responseObject.get("Response");
                Log.i("Send Customer Message", message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return message;
        }

        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            resultMessage = message;
            listener.afterSendBrands();
        }

    }

    public JSONObject sendDataToServer(String url, String jsonString){
        InputStream inputStream = null;
        JSONObject result = new JSONObject();
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            url = Uri.parse(URL.GSTSendBrandsURL).buildUpon()
                    .appendQueryParameter("BusinessID", businessID)
                    .appendQueryParameter("Brands", jsonString).toString();

            urlHit = Uri.parse(URL.GSTSendBrandsURL).buildUpon()
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
        JSONObject jObj = null;

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
