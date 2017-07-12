package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ProofOfPayment;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.InitiatePayPalRechargeForActivation;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.InitiateWalletRecharge;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ConnectionDetector;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.PlanClass;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.utils.PayPalConfig;

import java.math.BigDecimal;
public class ActivateSimPaymentActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 123;
    public static ProgressDialog walletProgressDialog;
    public static ProgressDialog paypalProgressDialog;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfig.ENVIRONMENT)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
    InitiateWalletRecharge initiateWalletRecharge;
    InitiatePayPalRechargeForActivation initiatePayPalRechargeForActivation;
    String balance;
    double amountToBePaid;
    String zipcode;
    String mobileno;
    OperatorClass operator;
    PlanClass plan;
    String loginID;
    String distributorID;
    TextView walletAmountTextView;
    TextView rechargeAmountTextView;
    Button PayPalPaymentButton;
    Button walletPaymentButton;
    String operators;
    String simCard;
    String zipCode;
    String plans;
    String Amount;
    String email;
    String clienttypeid, distrbutorid, loginid;
    String month;
    String tariffid;
    String city;
    String vendorid;


    OperatorClass selectedOperator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_payment);


        initVariables();
//
//        if(selectedOperator.getVendorID()!=13){
//            city=getIntent().getStringExtra("city");
//        }

        vendorid = getIntent().getStringExtra("vendorid");
        month = getIntent().getStringExtra("month");
        operators = getIntent().getStringExtra("operator");
        simCard = getIntent().getStringExtra("simCard");
        zipCode = getIntent().getStringExtra("zipcode");
        plans = getIntent().getStringExtra("plan");
        Amount = getIntent().getStringExtra("Amount");
        email = getIntent().getStringExtra("email");
        clienttypeid = getIntent().getStringExtra("ClientTypeID");
        distrbutorid = getIntent().getStringExtra("DistributorID");
        loginid = getIntent().getStringExtra("LoginID");
        tariffid = getIntent().getStringExtra("tariffid");
        city = getIntent().getStringExtra("city");

        walletProgressDialog = new ProgressDialog(this);
        walletProgressDialog.setCancelable(false);
        walletProgressDialog.setCanceledOnTouchOutside(false);

        paypalProgressDialog = new ProgressDialog(this);
        paypalProgressDialog.setCancelable(false);
        paypalProgressDialog.setCanceledOnTouchOutside(false);


        walletAmountTextView = (TextView) findViewById(R.id.textView44);
        PayPalPaymentButton = (Button) findViewById(R.id.btnPayPalRecharge);
        walletPaymentButton = (Button) findViewById(R.id.btnWalletRecharge);
        rechargeAmountTextView = (TextView) findViewById(R.id.rechargeAmountTextView);

        walletAmountTextView.setText("$ " + balance);
        rechargeAmountTextView.setText("$ " + Amount);

        paypalProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (initiatePayPalRechargeForActivation.status == 0) {

                    PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amountToBePaid)), "USD", "Something", PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                    startActivityForResult(intent, PAYPAL_REQUEST_CODE);

                } else if (initiatePayPalRechargeForActivation.status == 1) {
                    showAlert("Something went wrong. Please try later!!");
                } else {
                    showAlert("Something went wrong. Please try later!!");
                }
            }
        });

        PayPalPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paypalProgressDialog.show();
                //  amountToBePaid= Double.parseDouble(Amount);

                if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                    initiatePayPalRechargeForActivation = new InitiatePayPalRechargeForActivation(loginid, distrbutorid, (float) amountToBePaid, tariffid);
                } else {
                    showAlert("You are not connected to the Internet");
                }
            }


        });
        paypalProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getPayment();
            }

            private void getPayment() {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(Amount)), "USD", "Something", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, PAYPAL_REQUEST_CODE);
            }
        });

//        walletProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//
//                if (initiateWalletRecharge.status == 0) {
//
//                    Intent intent = new Intent(getApplicationContext(), ConfirmRechargeActivity.class);
//
//                    Gson gson = new Gson();
//
//                    String operatorString = gson.toJson(operator);
//                    String planString = gson.toJson(plan);
//
//                    intent.putExtra("paymentId", initiateWalletRecharge.paymentID);
//                    intent.putExtra("mobileno", mobileno);
//                    intent.putExtra("operator", operatorString);
//                    intent.putExtra("plan", planString);
//                    intent.putExtra("zipcode", zipcode);
//                    intent.putExtra("distributorId", distributorID);
//                    intent.putExtra("paymentType", "wallet");
//                    startActivity(intent);
//
//
//                } else if (initiateWalletRecharge.status == 1) {
//                    showAlert("Your wallet has insufficient balance");
//                } else {
//                    showAlert("Something went wrong. Please try later!!");
//                }
//
//
//            }
//        });


        walletPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountToBePaid = Double.parseDouble(Amount);
                //    initiateWalletRecharge = new InitiateWalletRecharge(clienttypeid,distrbutorid,simCard, (float) amountToBePaid,loginid,email,zipcode,month,1,plan);
                // walletProgressDialog.show();

                String paymenttype = "wallet";
                Intent intent = new Intent(getApplicationContext(), ActivationConfirmActivity.class);
                intent.putExtra("plan", plans);
                intent.putExtra("city", city);
                intent.putExtra("vendorid", vendorid);
                intent.putExtra("Amount", Amount);
                intent.putExtra("email", email);
                intent.putExtra("operator", operators);
                intent.putExtra("zipcode", zipCode);
                intent.putExtra("simCard", simCard);
                intent.putExtra("ClientTypeID", clienttypeid);
                intent.putExtra("DistributorID", distrbutorid);
                intent.putExtra("LoginID", loginid);
                intent.putExtra("months", month);
                intent.putExtra("paymenttype", paymenttype);
                intent.putExtra("tariffid", tariffid);

                startActivity(intent);
            }
        });


    }

    private void initVariables() {

//        Gson gson = new Gson();
//
        balance = String.valueOf(getSharedPreferences("LoginPrefs", 0).getFloat("Balance", (float) 0.0));
//
//        String operatorString = getIntent().getStringExtra("operator");
//        String planString = getIntent().getStringExtra("plan");
//
//        operator = gson.fromJson(operatorString, OperatorClass.class);
//        plan = gson.fromJson(planString, PlanClass.class);
//
//        zipcode = getIntent().getStringExtra("zipcode");
//        mobileno = getIntent().getStringExtra("mobileno");
//
        //   amountToBePaid = Double.parseDouble(Amount);
//
//        loginID = getSharedPreferences("LoginPrefs", 0).getString("LoginID", "null");
//        distributorID = getSharedPreferences("LoginPrefs", 0).getString("DistributorID", "null");


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String paymenttype = "paypal";
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {

                    try {
                        ProofOfPayment proofOfPayment = confirm.getProofOfPayment();

                        String transactionId;
                        String payPalPaymentId;

                        if (proofOfPayment != null) {
                            transactionId = proofOfPayment.getTransactionId();
                            payPalPaymentId = proofOfPayment.getPaymentId();
                        } else {
                            transactionId = "123";
                            payPalPaymentId = "123";
                        }


                        Intent intent = new Intent(getApplicationContext(), ActivationConfirmActivity.class);

                        Gson gson = new Gson();

                        String operatorString = gson.toJson(operator);
                        String planString = gson.toJson(plan);

                        intent.putExtra("paymentId", initiatePayPalRechargeForActivation.paymentID);
                        intent.putExtra("plan", plans);
                        intent.putExtra("city", city);
                        intent.putExtra("vendorid", vendorid);
                        intent.putExtra("Amount", Amount);
                        intent.putExtra("email", email);
                        intent.putExtra("operator", operators);
                        intent.putExtra("zipcode", zipCode);
                        intent.putExtra("simCard", simCard);
                        intent.putExtra("ClientTypeID", clienttypeid);
                        intent.putExtra("DistributorID", distrbutorid);
                        intent.putExtra("LoginID", loginid);
                        intent.putExtra("months", month);
                        intent.putExtra("paymenttype", paymenttype);
                        intent.putExtra("tariffid", tariffid);
                        intent.putExtra("PaypalPaymentId", payPalPaymentId);
                        intent.putExtra("paymentType", "paypal");
                        startActivity(intent);
                    } catch (Exception c) {
                        showAlert("Something went wrong. PLease try again later");
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Log.i("paymentExample", "The user canceled.");

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");

            }
        }
    }

//        @Override
//        public void onStart() {
//            super.onStart();
//
//            // ATTENTION: This was auto-generated to implement the App Indexing API.
//            // See https://g.co/AppIndexing/AndroidStudio for more information.
//            client.connect();
//            Action viewAction = Action.newAction(
//                    Action.TYPE_VIEW, // TODO: choose an action type.
//                    "PayPal Page", // TODO: Define a title for the content shown.
//                    // TODO: If you have web page content that matches this app activity's content,
//                    // make sure this auto-generated web page URL is correct.
//                    // Otherwise, set the URL to null.
//                    Uri.parse("http://host/path"),
//                    // TODO: Make sure this auto-generated app URL is correct.
//                    Uri.parse("android-app://com.virtuzoconsultancyservicespvtltd.ahprepaid/http/host/path")
//            );
//            AppIndex.AppIndexApi.start(client, viewAction);
//        }

    private void showAlert(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("A H P R E P A I D")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .setIcon(R.drawable.ahprepaidlogo)
                .show();

    }

}
