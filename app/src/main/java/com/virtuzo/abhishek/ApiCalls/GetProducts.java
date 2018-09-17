package com.virtuzo.abhishek.ApiCalls;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.virtuzo.abhishek.modal.Brand;
import com.virtuzo.abhishek.modal.Product;
import com.virtuzo.abhishek.modal.ProductCategory;
import com.virtuzo.abhishek.modal.Unit;
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

public class GetProducts {

    public ArrayList<Product> productList;
    public ArrayList<ProductCategory> productCategoryArrayList;
    public ArrayList<Brand> brandArrayList;
    public ArrayList<Unit> unitArrayList;
    private GetProducts.CallBack listener;
    public String resultMessage = "";
    public String errorCode = "GPD";
    public String apiResponse = "";
    int businessID;

    public GetProducts(int businessID, GetProducts.CallBack listener) {//, CallbackAfterListsRecieved callback){

        this.businessID = businessID;
        this.listener = listener;

        new GetProducts.apitask().execute();

    }

    public interface CallBack {
        void afterGetProducts();
    }


    public class apitask extends AsyncTask<Object, Object, Void> {

        private String url;

//        CallbackAfterListsRecieved callback;
//
//        public apitask(CallbackAfterListsRecieved callback) {
//            this.callback = callback;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            productList = new ArrayList<>();
            productCategoryArrayList = new ArrayList<>();
            brandArrayList = new ArrayList<>();
            unitArrayList = new ArrayList<>();
            url = Uri.parse(URL.GSTGetProductsURL).buildUpon().appendQueryParameter("BusinessId", ""+businessID).toString();
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

                    jsonObject = jsonObject.getJSONObject("Data");

                    JSONArray jsonarray = jsonObject.getJSONArray("Products");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Product product;
                        Gson gson = new Gson();
                        String jsonString = jsonarray.getJSONObject(i).toString();
                        product = gson.fromJson(jsonString, Product.class);
                        product.setSynced(1);
                        productList.add(product);
                    }
                    jsonarray = jsonObject.getJSONArray("ProductCategory");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        ProductCategory productCategory;
                        Gson gson = new Gson();
                        String jsonString = jsonarray.getJSONObject(i).toString();
                        productCategory = gson.fromJson(jsonString, ProductCategory.class);
                        productCategory.setSynced(1);
                        productCategoryArrayList.add(productCategory);
                    }
                    jsonarray = jsonObject.getJSONArray("Brand");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Brand brand;
                        Gson gson = new Gson();
                        String jsonString = jsonarray.getJSONObject(i).toString();
                        brand = gson.fromJson(jsonString, Brand.class);
                        brand.setSynced(1);
                        brandArrayList.add(brand);
                    }
                    jsonarray = jsonObject.getJSONArray("Unit");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Unit unit;
                        Gson gson = new Gson();
                        String jsonString = jsonarray.getJSONObject(i).toString();
                        unit = gson.fromJson(jsonString, Unit.class);
                        unit.setSynced(1);
                        unitArrayList.add(unit);
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
            listener.afterGetProducts();
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
