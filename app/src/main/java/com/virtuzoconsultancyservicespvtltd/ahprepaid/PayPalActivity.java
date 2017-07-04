package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.StringUtils;

import org.json.JSONException;

import java.math.BigDecimal;

public class PayPalActivity extends AppCompatActivity {

    private Button buttonPay;
    Button btnPayPal;
    private EditText editTextAmount;
    public LinearLayout linearLayout;
    private TextView currentBalance;
    public String Total;
    private String paymentAmount;
    String DistributorID="",LoginID="",ClientTypeID="",DateAndTime="",FirstName="",TotalTopup="";
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
 public TextView chargedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);
        setTitle("Top Up");
        btnPayPal=(Button)findViewById(R.id.btnPayPal);
        currentBalance=(TextView)findViewById(R.id.textView4);
        buttonPay = (Button) findViewById(R.id.buttonPay);
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout1);
      //  currentBalance=(TextView)findViewById(R.id.textView3);

        chargedAmount=(TextView)findViewById(R.id.txtUSD);
        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               chargedAmount.setText("$"+""+"-"+"-");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(chargedAmount.toString().trim().length()>0) {
                    paymentAmount = editTextAmount.getText().toString();
                    if(paymentAmount.length()>0) {
                        final double amount = Double.parseDouble(paymentAmount);

                    double res = (amount * 3) / 100.0f;
                    Log.d("amount", "3% percentage:" + res);
                    Total = String.valueOf(Integer.parseInt(paymentAmount) + res);
                    Log.d("total", "Total value is:" + Total);
                    chargedAmount.setText("$" + "" + Total);
                    linearLayout.setVisibility(View.VISIBLE);
                }}
                editTextAmount.setFilters(new InputFilter[] {
                        new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                            int beforeDecimal = 6, afterDecimal = 2;

                            @Override
                            public CharSequence filter(CharSequence source, int start, int end,
                                                       Spanned dest, int dstart, int dend) {
                                String temp = editTextAmount.getText() + source.toString();

                                if (temp.equals(".")) {
                                    return "0.";
                                }
                                else if (temp.toString().indexOf(".") == -1) {
                                    // no decimal point placed yet
                                    if (temp.length() > beforeDecimal) {
                                        return "";
                                    }
                                } else {
                                    temp = temp.substring(temp.indexOf(".") + 1);
                                    if (temp.length() > afterDecimal) {
                                        return "";
                                    }
                                }

                                return super.filter(source, start, end, dest, dstart, dend);
                            }
                        }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(chargedAmount.toString().trim().length()>0){
                    if(paymentAmount.length()>0) {
                    paymentAmount=editTextAmount.getText().toString();
                    final double amount=Double.parseDouble(paymentAmount);
                    double res=(amount *3)/100.0f;
                    Log.d("amount","3% percentage:"+res);
                    Total=String.valueOf(Integer.parseInt(paymentAmount)+res);
                    Log.d("total","Total value is:"+Total);
                    chargedAmount.setText("$"+""+Total);
                    linearLayout.setVisibility(View.VISIBLE);
                    //do your stuff here
                }}

            }
        });
        DistributorID=getIntent().getStringExtra("DistributorID");
        ClientTypeID=getIntent().getStringExtra("ClientTypeID");

        DateAndTime=getIntent().getStringExtra("DateAndTime");
        LoginID=getIntent().getStringExtra("LoginID");
        FirstName=getIntent().getStringExtra("FirstName");
        TotalTopup=getIntent().getStringExtra("TotalTopup");
        Log.d("ahprepaid","paypal topup"+TotalTopup);

        currentBalance.setText("$"+" "+TotalTopup);
        final Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }

            private void getPayment() {
                if (StringUtils.isBlank(paymentAmount)) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else if (paymentAmount.matches("0")){
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else{

                PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(Total)), "USD","Something", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
                startActivityForResult(intent,PAYPAL_REQUEST_CODE);
            }}
        });

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPayment();

                if (StringUtils.isBlank(paymentAmount)) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else if (paymentAmount.matches("0")){
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else {
                  // Intent intentNewTopUp=new Intent(PayPalActivity.this,NewTopup.class);

                    final double amount=Double.parseDouble(paymentAmount);
                    double res=(amount *3)/100.0f;
                    Log.d("amount","3% percentage:"+res);
                    Total=String.valueOf(Integer.parseInt(paymentAmount)+res);
                    Log.d("total","Total value is:"+Total);
                    chargedAmount.setText("USD"+""+Total);
                    linearLayout.setVisibility(View.VISIBLE);
//                    intentNewTopUp.putExtra("FirstName",FirstName);
//                    intentNewTopUp.putExtra("paymentAmount",paymentAmount);
//                    intentNewTopUp.putExtra("ClientTypeID",ClientTypeID);
//                    intentNewTopUp.putExtra("DistributorID",DistributorID);
//                    intentNewTopUp.putExtra("DateAndTime",DateAndTime);
//                    intentNewTopUp.putExtra("LoginID",LoginID);
//
//                    startActivity(intentNewTopUp);

                }

            }

            private void getPayment() {
             //   paymentAmount = editTextAmount.getText().toString();
                if (StringUtils.isBlank(paymentAmount)) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else if (paymentAmount.matches("0")){
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else {
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD",FirstName, PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
                    startActivityForResult(intent,PAYPAL_REQUEST_CODE);
                }




            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PAYPAL_REQUEST_CODE){
            if (resultCode== Activity.RESULT_OK){
                PaymentConfirmation confirm=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm!=null){
                    try{
                        String paymentDetails=confirm.toJSONObject().toString(4);
                        Log.i("paymentExample",paymentDetails);

                        startActivity(new Intent(getApplicationContext(),ConfirmationActivity.class)
                                .putExtra("PaymentDetails",paymentDetails)
                                .putExtra("PaymentAmount",paymentAmount)
                                .putExtra("ClientTypeID",ClientTypeID)
                                .putExtra("DistributorID",DistributorID)
                                .putExtra("DateAndTime",DateAndTime)
                                .putExtra("FirstName",FirstName)
                                .putExtra("LoginID",LoginID));


                    }catch (JSONException e){
                        Log.e("paymentAmount","an extremely unlikely failure occurred: ",e);
                    }
                }
            }else if (resultCode==Activity.RESULT_CANCELED){
                Log.i("paymentExample","The user canceled.");

            }else if (resultCode==PaymentActivity.RESULT_EXTRAS_INVALID){
                Log.i("paymentExample","An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");

            }
        }
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PayPal Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.virtuzoconsultancyservicespvtltd.ahprepaid/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
/*
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PayPal Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.virtuzoconsultancyservicespvtltd.ahprepaid/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent=new Intent(PayPalActivity.this,DashBoardScreen.class);
        intent.putExtra("Topup",TotalTopup);
        startActivity(intent);

    }


}
