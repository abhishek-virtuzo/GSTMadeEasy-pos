package com.virtuzo.abhishek.ApiCalls;

import android.app.Activity;
import android.util.Log;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.HSCCode;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Abhishek Aggarwal on 2/2/2018.
 */

public class GetHSNfromFile {

    Activity activity;
    GetHSNfromFile.CallBack callBack;

    public GetHSNfromFile(Activity activity, GetHSNfromFile.CallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
        new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                getHSN();
            }
        }).start();
    }

    public ArrayList<HSCCode> hscCodeArrayList;

    public void getHSN() {

        hscCodeArrayList = new ArrayList<>();

        //Get Data From Text Resource File Contains Json Data.
        InputStream inputStream = activity.getResources().openRawResource(R.raw.hsn_json_2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("Text Data", byteArrayOutputStream.toString());
        try {
            // Parse the data into jsonobject to get original data in form of hsn_json.
//            JSONObject jsonObject = convertInputStreamToResponseObject(inputStream);
            JSONObject jsonObject = new JSONObject(byteArrayOutputStream.toString());
            Log.d("check", jsonObject+"");

            if (jsonObject != null) {
//                    resultMessage = jsonObject.getJSONArray("Response").getJSONObject(0).getString("Response");
                JSONArray jsonarray = jsonObject.getJSONArray("Data");
                Log.e("HSN Timer", "start converting json array to arraylist. size of json array : " + jsonarray.length());
                DatabaseHandler.getInstance().updateHSCCodeListFromJsonList(jsonarray);
//                for (int i = 0; i < jsonarray.length(); i++) {
//
//                    HSCCode hscCode;
//                    Gson gson = new Gson();
//
//                    String jsonString = jsonarray.getJSONObject(i).toString();
//                    hscCode = gson.fromJson(jsonString, HSCCode.class);
//                    hscCode.setSynced(1);
//                    hscCodeArrayList.add(hscCode);
//                }
            }
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.e("HSNError", errors.toString());
        }
//        Log.e("HSN Timer", "completed json array to arraylist size of arraylist : " + hscCodeArrayList.size());
//        DatabaseHandler.getInstance().updateHSCCodeList(hscCodeArrayList);
        callBack.afterGetHSNCodes();
    }

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        return data_json;
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
            Log.e("check", "response is " + sb);
            json = sb.toString();
            json = StringEscapeUtils.unescapeJson(json);
            json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
            Log.e("check", "hsn_json is " + json);
            return new JSONObject(json);
        } catch (Exception e) {
            Log.e("check", "Error converting result " + e.toString());
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

    public interface CallBack {
        void afterGetHSNCodes();
    }

}
