package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.ConfirmRecharge;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.PlanClass;

public class ConfirmRechargeActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;

    ConfirmRecharge confirmRecharge;

    TextView messageTextView;

    String paymentId;
    String paypalPaymentId;
    String distributorId;
    String loginId;
    String paymentType;
    String zipcode;
    String mobileno;
    OperatorClass operator;
    PlanClass plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_recharge);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        initVariables();

        if (paymentType.compareTo("wallet") == 0) {
            confirmRecharge = new ConfirmRecharge(paymentId, operator, plan, mobileno, zipcode, "", 29, distributorId, loginId);
        }
        if (paymentType.compareTo("paypal") == 0) {
            confirmRecharge = new ConfirmRecharge(paymentId, operator, plan, mobileno, zipcode, paypalPaymentId, 31, distributorId, loginId);
        }

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                Log.i("Abhishek", "responseCode = " + confirmRecharge.responseCode);


            }
        });

    }

    private void initVariables() {

        Gson gson = new Gson();

        String operatorString = getIntent().getStringExtra("operator");
        String planString = getIntent().getStringExtra("plan");

        operator = gson.fromJson(operatorString, OperatorClass.class);
        plan = gson.fromJson(planString, PlanClass.class);

        zipcode = getIntent().getStringExtra("zipcode");
        mobileno = getIntent().getStringExtra("mobileno");
        paymentId = getIntent().getStringExtra("paymentId");
        paymentType = getIntent().getStringExtra("paymentType");

        loginId = getSharedPreferences("LoginPrefs", 0).getString("LoginID", "null");
        distributorId = getSharedPreferences("LoginPrefs", 0).getString("DistributorID", "null");

        if (paymentType.compareTo("paypal") == 0) {
            paypalPaymentId = getIntent().getStringExtra("paypalPaymentId");
        }

    }

}
