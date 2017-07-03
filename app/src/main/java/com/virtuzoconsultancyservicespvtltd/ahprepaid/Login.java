package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.StringUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    EditText username, password;
    TextInputLayout usernamelayout, passwordlayout;
    Button loginbutton;
    ConnectionDetector connectionDetector;
    public static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = Login.class.getSimpleName();
    String URL = com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.URL.LoginServiceURL;

    String UserName = "", Password = "", strResult = "", TotalTopup = "", LastName = "", FirstName = "", Post = "", Message = "", ClientTypeID = "", DistributorID = "", LoginID = "", MobileNumber = "", EmailID = "", strMessage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
        if (setting.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(Login.this, DashBoardScreen.class);
            startActivity(intent);
        }
        username = (EditText) findViewById(R.id.user_id);
        loginbutton = (Button) findViewById(R.id.loginbutton);
        usernamelayout = (TextInputLayout) findViewById(R.id.user_id_layout);
        password = (EditText) findViewById(R.id.password);
        passwordlayout = (TextInputLayout) findViewById(R.id.password_layout);
        connectionDetector = new ConnectionDetector(getApplicationContext());

        if (!connectionDetector.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), "Please Connect the Internet", Toast.LENGTH_SHORT).show();
        }


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = username.getText().toString();
                Password = password.getText().toString();

                if (StringUtils.isBlank(UserName)) {
                    Toast.makeText(getApplicationContext(), "Please Enter the UserName", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(Password)) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Password", Toast.LENGTH_SHORT).show();
                    return;
                }


                new PostData().execute(UserName, Password);

            }
        });

    }


    public class PostData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String strUserName = params[0];
            String UserName = strUserName.replace(" ", "%20");
            String strPassword = params[1];
            String Password = strPassword.replace(" ", "%20");
            Log.d(TAG, "Username data is:" + strUserName);
            Log.d(TAG, "Password data is:" + strPassword);
            Log.d(TAG, "URL data is:" + URL);
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            String PostUrL = URL + "?UserName=" + UserName + "&Pwd=" + Password;

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(PostUrL);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d(TAG, "URL data is:" + new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                strResult = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Result data is " + strResult);

            try {
                JSONArray jsonArray = new JSONArray(strResult);
                Log.d(TAG, "json array data:" + jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Message = jsonObject.getString("Message");
                    Log.d(TAG, "Message Pass:" + Message);
                    ClientTypeID = jsonObject.getString("ClientTypeID");
                    Log.d(TAG, "ClientTypeID Pass:" + ClientTypeID);
                    DistributorID = jsonObject.getString("DistributorID");
                    Log.d(TAG, "DistributorID Pass:" + DistributorID);
                    LoginID = jsonObject.getString("CreatedBy");
                    Log.d(TAG, "LoginID Pass:" + LoginID);
                    MobileNumber = jsonObject.getString("MobileNumber");
                    Log.d(TAG, "Mobile Number :" + MobileNumber);
                    EmailID = jsonObject.getString("EmailID");
                    Log.d(TAG, "Email id :" + EmailID);
                    FirstName = jsonObject.getString("FirstName");
                    Log.d(TAG, "FirstName is :" + FirstName);
                    Post = jsonObject.getString("UserName");
                    Log.d(TAG, "Post Data is:" + Post);
                    TotalTopup = jsonObject.getString("TotalTopup");
                    Log.d(TAG, "TotalTopup data is:" + TotalTopup);
                    LastName = jsonObject.getString("LastName");



                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return Message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "Shailesh data :" + strResult);
            strMessage = s;
            Log.d(TAG, "Message data is:" + s);
            SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("logged", "logged");
            editor.putString("LoginID", LoginID);
            editor.putString("ClientTypeID", ClientTypeID);
            editor.putString("DistributorID", DistributorID);
            editor.putString("MobileNumber", MobileNumber);
            editor.putString("EmailID", EmailID);
            editor.putString("FirstName", FirstName);
            editor.putString("Post", Post);
            editor.putString("TotalTopup", TotalTopup);
            editor.putString("LastName", LastName);
            editor.commit();
            if (s != null) {
                if (s.equalsIgnoreCase("Success")) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, DashBoardScreen.class);
                    intent.putExtra("LoginID", LoginID);
                    intent.putExtra("DistributorID", DistributorID);
                    intent.putExtra("ClientTypeID", ClientTypeID);
                    intent.putExtra("MobileNumber", MobileNumber);
                    intent.putExtra("EmailID", EmailID);
                    intent.putExtra("FirstName", FirstName);
                    intent.putExtra("Post", Post);
                    intent.putExtra("TotalTopup", TotalTopup);
                    intent.putExtra("LastName", LastName);
                    startActivity(intent);
                    username.setText(null);
                    password.setText(null);
                } else if (s.equalsIgnoreCase("Invalid User")) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    username.setText(null);
                    password.setText(null);
                    password.setFocusable(false);
                    password.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            password.setFocusableInTouchMode(true);
                            return false;
                        }
                    });
                    SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editorpre = setting.edit();
                    editor.remove("logged");
                    editor.commit();

                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        this.finish();
        System.exit(0);

        SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
        if (setting.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(Login.this, DashBoardScreen.class);
            startActivity(intent);
        } else {
            return;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}

