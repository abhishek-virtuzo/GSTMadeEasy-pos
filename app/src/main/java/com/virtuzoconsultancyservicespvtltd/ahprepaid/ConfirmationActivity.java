package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmationActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = ConfirmationActivity.class.getSimpleName();
    //  String TOPUPURL= URL.TOPUPService;
    String Message = "", Response = "", ResponseCode = "", TotalTopup = "", PaymentAmount = "", ClientTypeID = "", DistributorID = "", DateAndTime = "", LoginID = "", Status = "", strResult = "", FirstName = "", TxnID = "";
    String result, PaymentId;

    Button goTodash;
    ConfirmTransactionApi confirmTransactionApi;
    TextView pay, paypal, Statustwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView setMessage = (TextView) findViewById(R.id.setmessage);
        pay = (TextView) findViewById(R.id.paymentAmount);
        paypal = (TextView) findViewById(R.id.paymentId);
        Statustwo = (TextView) findViewById(R.id.paymentStatus);
        goTodash = (Button) findViewById(R.id.dashBoardButton);

        goTodash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmationActivity.this, DashBoardScreen.class);
                intent.putExtra("Topup", TotalTopup);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();

        PaymentAmount = intent.getStringExtra("PaymentAmount");
        Log.d(TAG, "PaymentAmount is:" + PaymentAmount);
        pay.setText(PaymentAmount);
        TotalTopup = intent.getStringExtra("TotalTopup");
        Log.d(TAG, "PaymentAmount is:" + TotalTopup);

        ClientTypeID = intent.getStringExtra("ClientTypeID");
        DistributorID = intent.getStringExtra("DistributorID");
        DateAndTime = intent.getStringExtra("DateAndTime");
        LoginID = intent.getStringExtra("LoginID");
        PaymentId = intent.getStringExtra("paymentId");
        Log.d(TAG, "PaymentID is:" + PaymentId);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        FirstName = intent.getStringExtra("FirstName");
        result = intent.getStringExtra("result");


        TextView Result = (TextView) findViewById(R.id.textView6);
        paypal.setText(PaymentId);
        Result.setText(result);
        String success = "success";

        if (result.equals(success)) {

            String response = success;

            String transactionID = getIntent().getExtras().getString("TransactionId");
            String paypalPaymentID = getIntent().getExtras().getString("PaypalPaymentId");

            Statustwo.setText("success");

            imageView.setImageResource(R.drawable.smile);
            setMessage.setText("Congratulations, Your transaction was successful");
            Result.setVisibility(View.INVISIBLE);

            confirmTransactionApi = new ConfirmTransactionApi(response, paypalPaymentID, 24, PaymentId, LoginID, DistributorID, PaymentAmount);

        } else {

            imageView.setImageResource(R.drawable.sad);
            setMessage.setText("Sorry, Your transaction was unsuccessful");
            Result.setVisibility(View.INVISIBLE);

            linearLayout.setVisibility(View.INVISIBLE);
            paypal.setText("fail");
            Statustwo.setText("fail");
            String response = "fail";
            String TxnId = "0";
            confirmTransactionApi = new ConfirmTransactionApi(response, TxnId, 25, PaymentId, LoginID, DistributorID, PaymentAmount);


        }


    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus = (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object

        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        Status = textViewStatus.getText().toString();
        Log.d(TAG, "Status data is:" + Status);
        textViewAmount.setText(paymentAmount + " USD");
        TxnID = textViewId.getText().toString();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(ConfirmationActivity.this, TopUpActivity.class);
//        intent.putExtra("Topup", TotalTopup);
//        startActivity(intent);
    }


}
