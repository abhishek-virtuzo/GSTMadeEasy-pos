package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.ConfirmRecharge;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.DashBoardScreen;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.PlanClass;

public class ConfirmRechargeActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;

    ConfirmRecharge confirmRecharge;

    TextView messageTextView;
    ImageView messageImageView;
    TextView paymentAmountTextView;
    TextView paymentStatusTextView;
    TextView paymentIdTextView;

    Button goToDashboardButton;

    String paymentId;
    String paypalPaymentId;
    String distributorId;
    String loginId;
    String paymentType;
    String zipcode;
    String mobileno;
    String email;
    OperatorClass operator;
    PlanClass plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_recharge);

        initVariables();

        initGui();

        initGuiBehaviour();

        if (paymentType.compareTo("wallet") == 0) {
            confirmRecharge = new ConfirmRecharge(paymentId, operator, plan, mobileno, email, zipcode, "", 29, distributorId, loginId, 1);
        }
        if (paymentType.compareTo("paypal") == 0) {
            confirmRecharge = new ConfirmRecharge(paymentId, operator, plan, mobileno, email, zipcode, paypalPaymentId, 31, distributorId, loginId, 2);
        }

    }

    private void initVariables() {

        Gson gson = new Gson();

        String operatorString = getIntent().getStringExtra("operator");
        String planString = getIntent().getStringExtra("plan");

        operator = gson.fromJson(operatorString, OperatorClass.class);
        plan = gson.fromJson(planString, PlanClass.class);

        email = getIntent().getStringExtra("email");
        zipcode = getIntent().getStringExtra("zipcode");
        mobileno = getIntent().getStringExtra("mobileno");
        paymentId = getIntent().getStringExtra("paymentId");
        paymentType = getIntent().getStringExtra("paymentType");

        loginId = getSharedPreferences("LoginPrefs", 0).getString("LoginID", "null");
        distributorId = getSharedPreferences("LoginPrefs", 0).getString("DistributorID", "null");


        if (paymentType.compareTo("paypal") == 0) {
            paypalPaymentId = getIntent().getStringExtra("PaypalPaymentId");
        }

    }

    private void initGui() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        messageImageView = (ImageView) findViewById(R.id.messageImageView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        paymentAmountTextView = (TextView) findViewById(R.id.paymentAmountTextView);
        paymentStatusTextView = (TextView) findViewById(R.id.paymentStatusTextView);
        paymentIdTextView = (TextView) findViewById(R.id.paymentIdTextView);

        goToDashboardButton = (Button) findViewById(R.id.goToDashboardButton);

    }

    private void initGuiBehaviour() {

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if (confirmRecharge.responseCode == 0) {

                    paymentStatusTextView.setText("Success");
                    messageImageView.setImageResource(R.drawable.smile);
                    messageTextView.setText("Conratulations, Your recharge was successful");

                } else {

                    paymentStatusTextView.setText("Failure");
                    messageImageView.setImageResource(R.drawable.sad);
                    messageTextView.setText(confirmRecharge.responseMessage);

                }

                paymentAmountTextView.setText(Double.toString(plan.getTotalAmount()));
                paymentIdTextView.setText(paymentId);

                Log.i("Abhishek", confirmRecharge.responseMessage + confirmRecharge.responseCode);//0 success smiley 1 sad

            }
        });

        goToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DashBoardScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
