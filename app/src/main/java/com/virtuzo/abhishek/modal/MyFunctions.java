package com.virtuzo.abhishek.modal;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.utils.MyVolleySingleton;
import com.virtuzo.abhishek.utils.URL;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class MyFunctions {

    public static final String ADMINLOGIN_PIN = "818508092017"; // 8185 is Ravi sir's car no, 08092017 is date when tcpl was formed

    public static final long PRODUCT_CATEGORY_ID_TRADES = 71;
    public static final long PRODUCT_CATEGORY_ID_SERVICES = 68;

    public static final String NO_API_CALL = "NoApiCall";
    public static final String API_CALL = "ApiCall";
    public static final String SUCCESS_MESSAGE = "Success";

    public static final String CLASS_NAME = "ClassName";
    public static final String CLASS_DASHBOARD = "Dashboard";
    public static final String CLASS_NEW_INVOICE = "NewInvoice";
    public static final String CLASS_NEW_INVOICE_FOR_DO = "NewInvoiceForDO";
    public static final String CLASS_PURCHASE_INVOICE = "PurchaseInvoice";
    public static final String CLASS_DELIVERY_ORDER = "DeliveryOrder";
    public static final String CLASS_PRODUCT_LIST = "ProductList";
    public static final String CLASS_PRODUCT_VIEW = "ProductView";
    public static final String CLASS_CUSTOMER_LIST = "CustomerList";
    public static final String CLASS_CUSTOMER_VIEW = "CustomerView";
    public static final String CLASS_SUPPLIER_LIST = "SupplierList";
    public static final String CLASS_SUPPLIER_VIEW = "SupplierView";
    public static final String CLASS_INVOICELIST_FOR_PAYMENT = "InvoiceListForPayment";
    public static final String CLASS_NEW_CREDITNOTE = "NewCreditNote";
    public static final String CLASS_NEW_PRODUCT = "NewProduct";

    public static final String HideDashboardDetails = "HideDashboardDetails";

    public static final String PREFS_DraftInvoices = "DraftInvoicesPrefs";
    public static final String DraftInvoices = "DraftInvoices";

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyFunctions.context = context;
    }

    public static String lowerCase(String str) {
        if (str == null) {
            return "";
        }
        return str.toLowerCase();
    }

    public static int StringLength(String s) {
        if(s == null) {
            return 0;
        }
        return s.length();
    }

    public static String makeStringCentreAlign(String s, int lengthOfScreen) {
        if(s == null) {
            return "";
        }
        if(s.length() > lengthOfScreen) {
            return MyFunctions.insertPeriodically(s, "\n", lengthOfScreen);
        }
        StringBuilder builder = new StringBuilder();
        int length = lengthOfScreen - s.length();
        int halfLength = length/2;
        for (int i = 0; i < halfLength; i++) {
            builder.append(" ");
        }
        builder.append(s);
        for (int i = 0; i < length - halfLength; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    public static String insertPeriodically(String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length()/period)+1);

        int index = 0;
        String prefix = "";
        while (index < text.length())
        {
            // Don't put the insert in the very first iteration.
            // This is easier than appending it *after* each substring
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }

    public static String makeStringLeftAlign(String s, int lengthOfScreen) {
        if(s == null) {
            return "";
        }
        if(s.length() >= lengthOfScreen)
            return s;
        StringBuilder builder = new StringBuilder();
        builder.append(s);
        for (int i = 0; i < lengthOfScreen; i++) {
            builder.append(" ");
        }
        String result = builder.toString().substring(0, lengthOfScreen);
        return result;
    }

    public static String makeStringRightAlign(String s, int lengthOfScreen) {
        if(s == null) {
            return "";
        }
        if(s.length() >= lengthOfScreen)
            return s;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lengthOfScreen; i++) {
            builder.append(" ");
        }
        builder.append(s);
        String result = builder.toString().substring(builder.toString().length() - lengthOfScreen);
        return result;
    }

    public static String drawLine(String s, int lengthOfScreeen) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lengthOfScreeen; i++) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static double parseDouble(String s) {
        if(s == null) {
            return 0;
        }
        s = s.trim();
        if(s.equals(".") || s.equals("")) {
            return 0;
        }
        return Double.parseDouble(s);
    }

    public static int parseInt(String s) {
        if(s == null || s.equals("")) {
            return 0;
        }
        s = s.trim();
        return Integer.parseInt(s);
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String datetime = df.format(c.getTime());
        return datetime;
    }

    public static String getCurrentDateAndMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM");
        String datetime = df.format(c.getTime());
        return datetime;
    }

    public static String getCurrentDateAndMonthForPayment() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM");
        String datetime = df.format(c.getTime());
        return datetime;
    }

    public static String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM");
        String datetime = df.format(c.getTime());
        return datetime;
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String convertServerReceievedDateToDateFormat(String date) {
        date = date.substring(0, date.length() - 2);
        date  = date.substring(6, date.length());
        return MyFunctions.getDate(Long.parseLong(date), "dd MMM yyyy");
    }

    public static void toggleKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static void GetBusinessLogo(String BusinessId, String OutletId) {

        String url = Uri.parse(URL.GSTGetOutletLogoURL).buildUpon()
                .appendQueryParameter("BusinessId", BusinessId)
                .appendQueryParameter("OutletID", OutletId).toString();

        StringRequest imageLogoRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences setting = context.getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);

                String json = response;
                json = StringEscapeUtils.unescapeJson(json);
                json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
                try {
                    JSONObject jsonResponse = new JSONObject(json);
                    JSONArray dataJsonArray = jsonResponse.getJSONArray("Data");
                    if (dataJsonArray.length() > 0) {
                        JSONObject jsonObject = dataJsonArray.getJSONObject(0);
                        String logoUrl = jsonObject.getString("Logo");
                        Dowloadimage(logoUrl);
                        return;
                    }
                    Dowloadimage(setting.getString("Logo",null));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Dowloadimage(setting.getString("Logo",null));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SharedPreferences setting = context.getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredVolley(origin, error);
                Dowloadimage(setting.getString("Logo",null));
            }
        });

        MyVolleySingleton.getInstance(context).addToRequestQueue(imageLogoRequest);

    }

    public static void Dowloadimage(String url ){

        RequestQueue requestQueue= Volley.newRequestQueue(context);

        //TODO change image URL
        // String url=setting.getString("Logo",null);

        // String url="http://brandmark.io/logo-rank/random/apple.png";

        if(!(url.contains("https://")||url.contains("http://"))) {
            url = "https://" + url;
        }
        try {
            int pos = url.lastIndexOf('/') + 1;
            URI uri = new URI(url.substring(0, pos) + URLEncoder.encode(url.substring(pos)));
            url = uri.toString();

            final String finalUrl = url;
            ImageRequest request = new ImageRequest(url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            //logoView.setImageBitmap(bitmap);
                            MyFunctions.saveImage(bitmap, finalUrl);

                            // mImageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            //mImageView.setImageResource(R.drawable.image_load_error);
                            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                            String className = Thread.currentThread().getStackTrace()[2].getClassName();
                            String origin = methodName + " - " + className;
                            MyFunctions.errorOccuredVolley(origin, error);
                        }
                    });

            if (url!=null) {
                requestQueue.add(request);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public static void deletePreviousImage() {
        File sdcard = Environment.getExternalStorageDirectory();
        File myDir = new File(sdcard,"/GSTMadeEasy");
        myDir.mkdirs();
        File outputFile = new File(myDir, "logo.png");
        if(outputFile.exists()){
            outputFile.delete();
        }
    }

    public static void saveImage(Bitmap image, String url){

        File sdcard = Environment.getExternalStorageDirectory();
        File myDir = new File(sdcard,"/GSTMadeEasy");
        myDir.mkdirs();
        File outputFile = new File(myDir, "logo.png");
        if(outputFile.exists()){
            outputFile.delete();
        }

        SharedPreferences setting = context.getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString("Logo", url);
        editor.commit();

        /*-----------*/
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        try{
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        /*-----------------*/
//
//        OutputStream outputStream = new FileOutputStream(outputFile);
//        image.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
//        outputStream.flush();
//        outputStream.close();


    }

    public static synchronized Bitmap getImage(){
        Bitmap image;
        File sdcard = Environment.getExternalStorageDirectory();
        File myDir = new File(sdcard,"/GSTMadeEasy/logo.png");
        try {
            image= BitmapFactory.decodeStream(new FileInputStream(myDir));
            return image;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCurrentDateNumeric() {
        Calendar calendar = Calendar.getInstance();
        int date = (calendar.get(Calendar.YEAR) * 10000) + ((calendar.get(Calendar.MONTH)+ 1) * 100) + calendar.get(Calendar.DATE);
        return date;
    }

    public static int getPreviousDateNumeric() { // used to delete previous logs only
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE , -14);
        int date = (calendar.get(Calendar.YEAR) * 10000) + ((calendar.get(Calendar.MONTH)+ 1) * 100) + calendar.get(Calendar.DATE);
        return date;
    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String datetime = df.format(c.getTime());
        return datetime;
    }
//        private String allowCharacterSet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM ";
//            Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show();

//            if (source != null){
//                for (int i = start; i < end; i++) {
//                    String str = Character.toString(source.charAt(i));
//                    if (blockCharacterSet.contains(str)) {
//                        MyToast.showToast("Invalid input : " + str, context);
//                        return str.replaceAll(blockCharacterSet, "");
//                    }
//                }
//            }

    public static void errorOccuredVolley(String origin, VolleyError e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        ErrorLog errorLog = ErrorLog.createErrorLog(ErrorLog.ERRORCODE_VOLLEY, origin, errors.toString());
        DatabaseHandler.getInstance().addNewErrorLog(errorLog);
    }

    public static void errorOccuredAppCrash(String exceptionString) {
        ErrorLog errorLog = ErrorLog.createErrorLog(ErrorLog.ERRORCODE_UNHANDLED, "App Crash", exceptionString);
        DatabaseHandler.getInstance().addNewErrorLog(errorLog);
    }

    public static void errorOccuredInApiCall(String origin, String apiResponse) {
        ErrorLog errorLog = ErrorLog.createErrorLog(ErrorLog.ERRORCODE_APICALL, origin, apiResponse);
        DatabaseHandler.getInstance().addNewErrorLog(errorLog);
    }

    public static void errorOccuredCatchInSavingDbBackup(Exception e, String origin) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        ErrorLog errorLog = ErrorLog.createErrorLog(ErrorLog.ERRORCODE_SAVING_DB_BACKUP, origin, errors.toString());
        DatabaseHandler.getInstance().addNewErrorLog(errorLog);
    }

    public static void errorOccuredCatchInDatabase(Exception e, String origin) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        ErrorLog errorLog = ErrorLog.createErrorLog(ErrorLog.ERRORCODE_DATABASE, origin, errors.toString());
        DatabaseHandler.getInstance().addNewErrorLog(errorLog);
    }

    public static void errorOccuredInsertCatchInDatabase(Object obj, Exception e, String origin) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        Gson gson = new Gson();
        String string = gson.toJson(obj);
        ErrorLog errorLog = ErrorLog.createErrorLog(ErrorLog.ERRORCODE_DATABASE, origin, string + "\n" + errors.toString());
        DatabaseHandler.getInstance().addNewErrorLog(errorLog);
    }

    public static void errorOccuredRecordNotInsertedInDatabase(Object obj, String origin) {
        Gson gson = new Gson();
        String jsonString = "Record Not inserted\n" + gson.toJson(obj);
        ErrorLog errorLog = ErrorLog.createErrorLog(ErrorLog.ERRORCODE_DATABASE, origin, jsonString);
        DatabaseHandler.getInstance().addNewErrorLog(errorLog);
    }

    public static String getAppVersion() {
        PackageInfo pInfo;
        String version = "";
        try {
            pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static InputFilter filter = new InputFilter() {

//        private String blockCharacterSet = "~#^|$%&*!\"\\-+()=':;?`/_,<>\\{\\}\\[\\]%";
        private String blockCharacterSet = "~*!+=':;?`,<>\\{\\}\\[\\]";

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                MyToast.showToast("Invalid input : " +source, context);
                return "";
            }
            return null;
        }
    };

    public static void refreshDrafts(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyFunctions.PREFS_DraftInvoices, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String drafts = sharedPreferences.getString(MyFunctions.DraftInvoices, null);
        Gson gson = new Gson();
        if (drafts != null) {
            try {
                // got previous drafts
                JSONObject jsonObject = new JSONObject(drafts);
                JSONArray jsonArray = jsonObject.getJSONArray("Drafts");

                // filter out today's drafts
                String currentDate = getCurrentDateAndMonth();
                ArrayList<DraftInvoice> draftInvoices = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    DraftInvoice draftInvoice = gson.fromJson(jsonObject1.toString(), DraftInvoice.class);
                    if (draftInvoice.getCurrentDate().contains(currentDate)) {
                        draftInvoices.add(draftInvoice);
                    }
                }

                // save only today's drafts
                if (jsonArray.length() != draftInvoices.size()) {
                    jsonArray = new JSONArray();
                    JSONObject draftInvoicesJson = new JSONObject();
                    for (DraftInvoice draftInvoice : draftInvoices) {
                        String jsonString = gson.toJson(draftInvoice);
                        JSONObject jsonObj = new JSONObject(jsonString);
                        jsonArray.put(jsonObj);
                    }
                    draftInvoicesJson.put("Drafts", jsonArray);
                    String draftInvoicesString = draftInvoicesJson.toString();

                    editor.putString(MyFunctions.DraftInvoices, draftInvoicesString);
                    editor.commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public static void removeDraft(DraftInvoice draftInvoice, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyFunctions.PREFS_DraftInvoices, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String drafts = sharedPreferences.getString(MyFunctions.DraftInvoices, null);
        Gson gson = new Gson();
        if (drafts != null) {
            try {
                // got previous drafts
                JSONObject jsonObject = new JSONObject(drafts);
                JSONArray jsonArray = jsonObject.getJSONArray("Drafts");

                // Find current draft
                ArrayList<DraftInvoice> draftInvoices = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    DraftInvoice invoice = gson.fromJson(jsonObject1.toString(), DraftInvoice.class);
                    if (draftInvoice.getDraftID() != invoice.getDraftID()) {
                        draftInvoices.add(invoice);
                    }
                }

                if (jsonArray.length() != draftInvoices.size()) {
                    jsonArray = new JSONArray();
                    JSONObject draftInvoicesJson = new JSONObject();
                    for (DraftInvoice invoice : draftInvoices) {
                        String jsonString = gson.toJson(invoice);
                        JSONObject jsonObj = new JSONObject(jsonString);
                        jsonArray.put(jsonObj);
                    }
                    draftInvoicesJson.put("Drafts", jsonArray);
                    String draftInvoicesString = draftInvoicesJson.toString();

                    editor.putString(MyFunctions.DraftInvoices, draftInvoicesString);
                    editor.commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public static int countDrafts(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyFunctions.PREFS_DraftInvoices, context.MODE_PRIVATE);
        String drafts = sharedPreferences.getString(MyFunctions.DraftInvoices, null);
        int count = 0;
        if (drafts != null) {
            try {
                // got previous drafts
                JSONObject jsonObject = new JSONObject(drafts);
                JSONArray jsonArray = jsonObject.getJSONArray("Drafts");

                count = jsonArray.length();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return count;

    }

    public static Calendar getCalendarObject(String inputDate) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        try {
            cal.setTime(df.parse(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

}
