package com.virtuzo.abhishek;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.virtuzo.abhishek.Views.Activity.Others.WifiActivity;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.utils.StringUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 11/24/2017.
 */

public class UserLoginApi {

    public static final String PREFS_NAME = "LoginPrefs";

    public static class Login extends AppCompatActivity {
        public static final String PREFS_NAME = "LoginPrefs";
        private static final String TAG = Login.class.getSimpleName();
        EditText username, password, tabCode, clientCode;
        TextInputLayout usernamelayout, passwordlayout, tabCodelayout, businessIdlayout;
        Button loginbutton;
        ConnectionDetector connectionDetector;
        String URL = com.virtuzo.abhishek.utils.URL.GSTUserLoginURL;
        SharedPreferences setting;
        ProgressDialog progressDialog;
        String UserName = "", Password = "", TabCode = "", ClientCode = "", BusinessID = "", IMEI = "123", Message = "Offline";//, strResult = "", TotalTopup = "", LastName = "", FirstName = "", Post = "", ClientTypeID = "", DistributorID = "", LoginID = "", MobileNumber = "", EmailID = "", strMessage = "";
        String OutletName = "", Logo = "", isSendSMS = "", TermsAndConditions = "", OutletCode = "", GSTCategory = "", UserID = "",
                GSTCategoryID = "", EmployeeCode = "", InvoiceSeries = "", OutletID = "", Address = "", FirstName = "", GSTNumber = "",
                SalesMTD = "", SalesToday = "", StateID = "", StateName = "", ContactNumber = "", FaxNumber = "", RoleID = "",
                OutletTypeID = "", CollectionsMTD = "", CollectionsToday = "", InvoicesMTD = "", InvoicesToday = "";

        CustomViewGroup disableView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_login);

//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setting = getSharedPreferences(UserLoginApi.PREFS_NAME, 0);
            if (setting.getBoolean("logged", false)) {
                Intent intent = new Intent(Login.this, DashBoardScreen.class);
                startActivity(intent);
                this.finish();
            }
            loginbutton = (Button) findViewById(R.id.userLoginButton);
            username = (EditText) findViewById(R.id.userIdEditText);
            usernamelayout = (TextInputLayout) findViewById(R.id.userIdLayout);
            password = (EditText) findViewById(R.id.passwordEditText);
            passwordlayout = (TextInputLayout) findViewById(R.id.passwordLayout);
            tabCode = (EditText) findViewById(R.id.tabCodeEditText);
            tabCodelayout = (TextInputLayout) findViewById(R.id.tabCodeLayout);
            clientCode = (EditText) findViewById(R.id.businessIdEditText);
            businessIdlayout = (TextInputLayout) findViewById(R.id.businessIdLayout);
            connectionDetector = new ConnectionDetector(getApplicationContext());
            if(!setting.getBoolean("firstrun", true)) {
                clientCode.setText(setting.getString("ClientCode", "Invalid Client Code"));
                clientCode.setEnabled(false);
                tabCode.setText(setting.getString("TabCode", "99"));
                tabCode.setEnabled(false);
            } else {
                String cCode = setting.getString("ClientCode", "");
                if(cCode.length() != 0) {
                    clientCode.setText(cCode);
                    clientCode.setEnabled(false);
                }
                String tCode = setting.getString("TabCode", "");
                if(tCode.length() != 0) {
                    tabCode.setText(setting.getString("TabCode", "Invalid Tab Code"));
                    tabCode.setEnabled(false);
                }
                SharedPreferences.Editor editor = setting.edit();
                editor.putBoolean("firstrun", true);
                editor.commit();
            }
            if (!connectionDetector.isConnectingToInternet()) {
                Toast.makeText(getApplicationContext(), "Please Connect the Internet", Toast.LENGTH_SHORT).show();
            }

            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            loginbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClientCode = clientCode.getText().toString();
                    TabCode = tabCode.getText().toString();
                    UserName = username.getText().toString();
                    Password = password.getText().toString();

                    if (StringUtils.isBlank(ClientCode)) {
                        Toast.makeText(getApplicationContext(), "Please Enter the Client Code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isBlank(TabCode)) {
                        Toast.makeText(getApplicationContext(), "Please Enter the TabCode", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(MyFunctions.StringLength(TabCode) != 2) {
                        Toast.makeText(getApplicationContext(), "Please Enter valid TabCode", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isBlank(UserName)) {
                        Toast.makeText(getApplicationContext(), "Please Enter the UserName", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isBlank(Password)) {
                        Toast.makeText(getApplicationContext(), "Please Enter the Password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userNameStored = setting.getString("UserName", "");
                    String passwordStored = setting.getString("Password", "");
                    SharedPreferences.Editor editor = setting.edit();
                    if(UserName.equalsIgnoreCase(userNameStored) && Password.equalsIgnoreCase(passwordStored)) {
                        Toast.makeText(getApplicationContext(), "Offline login successful", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("logged", true);
                        editor.commit();
                        Intent intent = new Intent(Login.this, DashBoardScreen.class);
                        progressDialog.dismiss();
                        startActivity(intent);
                        username.setText("");
                        password.setText("");
                    } else {
                        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            new PostData().execute(UserName, Password, ClientCode);
                        } else {
                            showOkDialog("Unable to login offline\nConnect to the Internet");
                        }
                    }

                }

//                    LoginUser user = new LoginUser();
//                    user.setUserName(UserName);
//                    user.setPassword(Password);
//                    SharedPreferences setting = getSharedPreferences(UserLoginApi.PREFS_NAME, 0);
//                    SharedPreferences.Editor editor = setting.edit();
//                    if(DatabaseHandler.getInstance().isUserExists(user)) {
//                        String userID = DatabaseHandler.getInstance().getUserID(user);
//                        editor.putString("UserID", userID);
//                        editor.putBoolean("logged", true);
//                        editor.commit();
//                        Toast.makeText(getApplicationContext(), "Offline login successful", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Login.this, DashBoardScreen.class);
//                        progressDialog.dismiss();
//                        startActivity(intent);

            });


        }

        private void disableStatusBar() {

        /*---------------------------For Status Bar----------------------------*/

            WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

            WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
            localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            localLayoutParams.gravity = Gravity.TOP;
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                    // this is to enable the notification to receive touch events
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                    // Draws over status bar
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

            localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            localLayoutParams.height = (int) (40 * getResources().getDisplayMetrics().scaledDensity);
            localLayoutParams.format = PixelFormat.TRANSPARENT;

            disableView = new CustomViewGroup(this);
            manager.addView(disableView, localLayoutParams);

          /*---------------------------For Navigation Bar----------------------------*/

//        WindowManager manager2 = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
//
//        WindowManager.LayoutParams localLayoutParams2 = new WindowManager.LayoutParams();
//        localLayoutParams2.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        localLayoutParams2.gravity = Gravity.BOTTOM;
//        localLayoutParams2.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//
//                // this is to enable the notification to receive touch events
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//
//                // Draws over status bar
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//
//        localLayoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
//        localLayoutParams2.height = (int) (50 * getResources().getDisplayMetrics().scaledDensity);
//        localLayoutParams2.format = PixelFormat.TRANSPARENT;
//
//        blockingNavigationBarView = new CustomViewGroup(this);
//        manager2.addView(blockingNavigationBarView, localLayoutParams2);
//
//
//
//        MyFunctions.setWindowFull(DashBoardScreen.this);



        }

        @Override
        public void onBackPressed() {
        }

        @Override
        protected void onRestart() {
            super.onRestart();
        }

        @Override
        protected void onResume() {
            super.onResume();
            disableStatusBar();
            if (setting.getBoolean("logged", false)) {
                Intent intent = new Intent(Login.this, DashBoardScreen.class);
                startActivity(intent);
                this.finish();
            }
        }

        @Override
        protected void onPause() {
            super.onPause();
            if (disableView !=null) {
                WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
                manager.removeView(disableView);
            }
        }

        public void wifiButtomClick(View view) {
            //startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));

            startActivity(new Intent(this, WifiActivity.class));
        }


        public void switchKeyboardButton(View view) {
            MyFunctions.toggleKeyboard(this);
        }

        public void setupButtomClick(View view) {
            adminlogin();
        }

        private void adminlogin() {
            SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
            final String pin= setting.getString("AdminPin", MyFunctions.ADMINLOGIN_PIN);
            final EditText adminLoginPin = new EditText(this);
            adminLoginPin.setInputType(InputType.TYPE_CLASS_NUMBER);

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(adminLoginPin);
            alert.setTitle("Admin Login")
                    .setMessage("Enter Pin to Login")
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String enteredPin= adminLoginPin.getText().toString();
                            if(enteredPin.equals(pin)){
                                showAdminDialog();
                            } else {
                                Toast.makeText(UserLoginApi.Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    })
                    .show();
        }

        public void showAdminDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserLoginApi.Login.this);
            final View view = getLayoutInflater().inflate(R.layout.admin_login_dialog, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button removeKioskButton = (Button) view.findViewById(R.id.removeKioskButton);
            Button goToSettingsButton = (Button) view.findViewById(R.id.goToSettingsButton);
            Button backButton = (Button) view.findViewById(R.id.backButton);
            removeKioskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPackageManager().clearPackagePreferredActivities(getPackageName());
                    Toast.makeText(UserLoginApi.Login.this, "Kiosk Mode Disabled", Toast.LENGTH_SHORT).show();
                }
            });
            goToSettingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                }
            });
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        public class PostData extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                String strUserName = params[0];
                String UserName = strUserName.replace(" ", "%20");
                String strPassword = params[1];
                String Password = strPassword.replace(" ", "%20");
                String strClientCode = params[2];
                String ClientCode = strClientCode.replace(" ", "%20");
                Log.d(TAG, "Username data is:" + strUserName);
                Log.d(TAG, "Password data is:" + strPassword);
                Log.d(TAG, "ClientCode data is:" + ClientCode);
                Log.d(TAG, "URL data is:" + URL);
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                String PostUrL = Uri.parse(URL).buildUpon()
                        .appendQueryParameter("UserName", UserName)
                        .appendQueryParameter("Pwd", Password)
                        .appendQueryParameter("ClientCode", ClientCode)
                        .appendQueryParameter("IMEI", IMEI).toString();

                JSONObject jsonObject = null;
                try {
                    JSONObject jsonObjectTemp = getJSONFromUrlPost(PostUrL);
                    Log.d("check", jsonObjectTemp+"");
                    if (jsonObjectTemp != null) {

                        jsonObject = jsonObjectTemp.getJSONObject("Data").getJSONArray("UserDetail").getJSONObject(0);
                        Log.d("check", jsonObject+"");
                        Message = jsonObjectTemp.getJSONArray("Response").getJSONObject(0).getString("Response");
                        BusinessID = jsonObject.getString("BusinessID");
                        OutletName = jsonObject.getString("OutletName");
                        OutletID = jsonObject.getString("OutletID");
                        GSTCategoryID = jsonObject.getString("GSTCategoryID");
                        StateID = jsonObject.getString("StateID");
                        UserID = jsonObject.getString("UserID");
                        GSTNumber = jsonObject.getString("GSTNumber");
                        isSendSMS = jsonObject.getString("isSendSMS");
                        StateName = jsonObject.getString("StateName");
                        Logo = jsonObject.getString("Logo");
                        ContactNumber = jsonObject.getString("ContactNumber");
                        FaxNumber = jsonObject.getString("FaxNumber");
                        TermsAndConditions = jsonObject.getString("TermsAndConditions");
                        OutletCode = jsonObject.getString("OutletCode");
                        GSTCategory = jsonObject.getString("GSTCategory");
                        EmployeeCode = jsonObject.getString("EmployeeCode");
                        InvoiceSeries = jsonObject.getString("InvoiceSeries");
                        Address = jsonObject.getString("Address");
                        FirstName = jsonObject.getString("FirstName");
                        RoleID = jsonObject.getString("RoleID");
                        OutletTypeID = jsonObject.getString("OutletTypeID");
                        SalesMTD = jsonObject.getString("SalesMTD");
                        SalesToday = jsonObject.getString("SalesToday");
                        CollectionsMTD = jsonObject.getString("CollectionsMTD");
                        CollectionsToday = jsonObject.getString("CollectionsToday");
                        InvoicesMTD = jsonObject.getString("InvoicesMTD");
                        InvoicesToday = jsonObject.getString("InvoicesToday");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Message;
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

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d(TAG, "Message data is:" + s);
                progressDialog.dismiss();

                SharedPreferences setting = getSharedPreferences(UserLoginApi.PREFS_NAME, 0);
                SharedPreferences.Editor editor = setting.edit();
                    if (s.equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                        editor.putBoolean("logged", true);
                        editor.putString("ClientCode", ClientCode);
                        editor.putString("BusinessId", BusinessID);
                        editor.putString("TabCode", TabCode);
                        editor.putString("IMEI", IMEI);
                        editor.putString("UserName", UserName);
                        editor.putString("Password", Password);
                        editor.putString("StateID", StateID);
                        editor.putString("StateName", StateName);
                        editor.putString("ContactNumber", ContactNumber);
                        editor.putString("FaxNumber", FaxNumber);
                        editor.putString("OutletName", OutletName);
                        editor.putString("Logo", Logo);
                        editor.putString("isSendSMS", isSendSMS);
                        editor.putString("TermsAndConditions", TermsAndConditions);
                        editor.putString("OutletCode", OutletCode);
                        editor.putString("GSTCategory", GSTCategory);
                        editor.putString("UserID", UserID);
                        editor.putString("GSTCategoryID", GSTCategoryID);
                        editor.putString("EmployeeCode", EmployeeCode);
                        editor.putString("InvoiceSeries", InvoiceSeries);
                        editor.putString("OutletID", OutletID);
                        editor.putString("Address", Address);
                        editor.putString("FirstName", FirstName);
                        editor.putString("GSTNumber", GSTNumber);
                        editor.putString("RoleID", RoleID);
                        editor.putString("OutletTypeID", OutletTypeID);
                        editor.putString("SalesMTD", SalesMTD);
                        editor.putString("SalesToday", SalesToday);
                        editor.putString("CollectionsMTD", CollectionsMTD);
                        editor.putString("CollectionsToday", CollectionsToday);
                        editor.putString("InvoicesMTD", InvoicesMTD);
                        editor.putString("InvoicesToday", InvoicesToday);
                        editor.putBoolean("sync", true);
                        editor.commit();

                        // add user in database
//                        LoginUser user = new LoginUser();
//                        user.setUserId(UserID);
//                        user.setUserName(UserName);
//                        user.setPassword(Password);
//                        DatabaseHandler.getInstance().addNewUser(user);

                        Intent intent = new Intent(Login.this, DashBoardScreen.class);
                        intent.putExtra("UserID", UserID);
                        intent.putExtra("UserName", UserName);
                        intent.putExtra("BusinessId", BusinessID);
                        intent.putExtra("GSTNumber", GSTNumber);
                        intent.putExtra("FirstName", FirstName);
                        intent.putExtra("SalesMTD", SalesMTD);
                        intent.putExtra("SalesToday", SalesToday);
                        startActivity(intent);
                        username.setText("");
                        password.setText("");
                    } else {
                        showOkDialog("Login unsuccessful");
                    }
//                        if (s.equalsIgnoreCase("Offline")) {
//                            String userNameStored = setting.getString("UserName", "");
//                            String passwordStored = setting.getString("Password", "");
//                            if(UserName.equals(userNameStored)) {
//                                if(Password.equals(passwordStored)) {
//                                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                                    editor.putBoolean("logged", true);
//                                    editor.commit();
//                                    Intent intent = new Intent(Login.this, DashBoardScreen.class);
//                                    progressDialog.dismiss();
//                                    startActivity(intent);
//                                    username.setText("");
//                                    password.setText("");
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "Invalid Password during offline mode", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Invalid UserName during offline mode", Toast.LENGTH_SHORT).show();
//                            }
//                            progressDialog.dismiss();
//                        }

                }
            }

        public void showOkDialog(String heading) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserLoginApi.Login.this);
            final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button okButton = (Button) view.findViewById(R.id.okClick);
            TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
            okMessageTextView.setText(heading);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

    }

}
