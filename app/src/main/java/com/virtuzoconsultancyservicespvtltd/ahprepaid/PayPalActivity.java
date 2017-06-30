package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText editTextAmount;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        buttonPay = (Button) findViewById(R.id.buttonPay);

        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        DistributorID=getIntent().getStringExtra("DistributorID");
        ClientTypeID=getIntent().getStringExtra("ClientTypeID");
        DateAndTime=getIntent().getStringExtra("DateAndTime");
        LoginID=getIntent().getStringExtra("LoginID");
        FirstName=getIntent().getStringExtra("FirstName");
        TotalTopup=getIntent().getStringExtra("Topup");
        Log.d("ahprepaid","paypal topup"+TotalTopup);
        final Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);


        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPayment();
                paymentAmount=editTextAmount.getText().toString();
                if (StringUtils.isBlank(paymentAmount)) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else if (paymentAmount.matches("0")){
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intentNewTopUp=new Intent(PayPalActivity.this,NewTopup.class);
                    intentNewTopUp.putExtra("FirstName",FirstName);
                    intentNewTopUp.putExtra("paymentAmount",paymentAmount);
                    intentNewTopUp.putExtra("ClientTypeID",ClientTypeID);
                    intentNewTopUp.putExtra("DistributorID",DistributorID);
                    intentNewTopUp.putExtra("DateAndTime",DateAndTime);
                    intentNewTopUp.putExtra("LoginID",LoginID);

                    startActivity(intentNewTopUp);

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
