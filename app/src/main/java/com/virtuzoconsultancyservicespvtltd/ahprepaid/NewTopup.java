package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class NewTopup extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    TextView ChargedAmount;
    Button btnCancle, btnPayPal;
    String FirstName = "", paymentAmount = "", amount = "", ClientTypeID = "", DistributorID = "", DateAndTime = "", LoginID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_topup);

        ChargedAmount= (TextView) findViewById(R.id.ChargedAmount);

        btnCancle=(Button)findViewById(R.id.btnCancle);
        btnPayPal=(Button)findViewById(R.id.btnPayPal);

        FirstName=getIntent().getStringExtra("FirstName");
        paymentAmount=getIntent().getStringExtra("paymentAmount");
        ClientTypeID=getIntent().getStringExtra("ClientTypeID");
        DistributorID=getIntent().getStringExtra("DistributorID");
        DateAndTime=getIntent().getStringExtra("DateAndTime");
        LoginID=getIntent().getStringExtra("LoginID");


        final double amount=Double.parseDouble(paymentAmount);

        double res=(amount *3)/100.0f;
        Log.d("amount","3% percentage:"+res);

        final String Total=String.valueOf(Integer.parseInt(paymentAmount)+res);
        Log.d("total","Total value is:"+Total);


        ChargedAmount.setText(Total);


        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancle=new Intent(NewTopup.this,DashBoardScreen.class);
                startActivity(intentCancle);
            }
        });

        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }

            private void getPayment() {

                PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(Total)), "USD","Something", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
                startActivityForResult(intent,PAYPAL_REQUEST_CODE);

            }
        });

    }

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
}
