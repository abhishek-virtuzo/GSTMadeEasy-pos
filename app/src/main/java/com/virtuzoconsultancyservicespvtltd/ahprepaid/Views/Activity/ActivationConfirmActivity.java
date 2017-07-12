package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.ActivateSimApiPaypalH20;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.ActivateSimApiPaypalLycaa;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.ActivateSimApiWalletH20;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.ActivateSimApiWalletLycaa;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ConnectionDetector;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.DashBoardScreen;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;

public class ActivationConfirmActivity extends AppCompatActivity {
    public static ProgressDialog progressDialog;
    public static ProgressDialog progressDialog2;
    ImageView setImageResponse;
    String month;
    String operators;
    String simCard;
    String zipCode;
    String plans;
    String Amount;
    String email;
    String clienttypeid;
    String distrbutorid;
    String loginid;
    String paymenttype;
    String iswallet;
    String tariffid;
    String paypalStatus;
    String paymentid;
    String city;
    String vendorid;

    ActivateSimApiWalletLycaa activateSimApiWalletLycaa;
    ActivateSimApiPaypalLycaa activateSimApiPaypalLycaa;


    ActivateSimApiPaypalH20 activateSimApiPaypalH20;
    ActivateSimApiWalletH20 activateSimApiWalletH20;

    TextView messageShow;

    LinearLayout linearLayout;


    TextView amountShowPaypal;
    TextView statusShowPaypal;
    TextView idShowPaypal;

    Button gobackDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_confirm);

        city = getIntent().getStringExtra("city");
        vendorid = getIntent().getStringExtra("vendorid");
        month = getIntent().getStringExtra("months");
        operators = getIntent().getStringExtra("operator");
        simCard = getIntent().getStringExtra("simCard");
        zipCode = getIntent().getStringExtra("zipcode");
        plans = getIntent().getStringExtra("plan");
        Amount = getIntent().getStringExtra("Amount");
        email = getIntent().getStringExtra("email");
        clienttypeid = getIntent().getStringExtra("ClientTypeID");
        distrbutorid = getIntent().getStringExtra("DistributorID");
        loginid = getIntent().getStringExtra("LoginID");
        paymenttype = getIntent().getStringExtra("paymenttype");
        tariffid = getIntent().getStringExtra("tariffid");

        progressDialog = new ProgressDialog(this);
        progressDialog2 = new ProgressDialog(this);


        statusShowPaypal = (TextView) findViewById(R.id.paymentStatus4);
        amountShowPaypal = (TextView) findViewById(R.id.paymentAmount4);
        idShowPaypal = (TextView) findViewById(R.id.paymentId4);
        amountShowPaypal.setText(Amount);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayouthide);
        gobackDashboard = (Button) findViewById(R.id.dashBoardButton4);


        setImageResponse = (ImageView) findViewById(R.id.messageImageView4);
        messageShow = (TextView) findViewById(R.id.messageTextView4);

        if (paymenttype.compareTo("wallet") == 0) {
            iswallet = "1";

            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                if (vendorid.equals("13")) {
                    activateSimApiWalletLycaa = new ActivateSimApiWalletLycaa(getApplicationContext(), plans, Amount, email, operators,
                            zipCode, simCard, clienttypeid, loginid, distrbutorid,
                            iswallet, month, tariffid);
                } else {
                    activateSimApiWalletH20 = new ActivateSimApiWalletH20(getApplicationContext(), plans, Amount, email, operators,
                            zipCode, simCard, clienttypeid, loginid, distrbutorid,
                            iswallet, city, tariffid);
                }
            } else {
                showAlert("You are not connected to the Internet");
            }
        }
        if (paymenttype.compareTo("paypal") == 0) {
            iswallet = "2";
            progressDialog2.show();
            progressDialog2.setCancelable(false);
            progressDialog2.setCanceledOnTouchOutside(false);
            linearLayout.setVisibility(View.VISIBLE);
            paymentid = getIntent().getStringExtra("paymentId");
            idShowPaypal.setText(paymentid);

            if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                if (vendorid.equals("13")) {
                    activateSimApiPaypalLycaa = new ActivateSimApiPaypalLycaa(getApplicationContext(), plans, Amount, email, operators,
                            zipCode, simCard, clienttypeid, loginid, distrbutorid,
                            iswallet, month, tariffid, paymentid);
                } else {
                    activateSimApiPaypalH20 = new ActivateSimApiPaypalH20(getApplicationContext(), plans, Amount, email, operators,
                            zipCode, simCard, clienttypeid, loginid, distrbutorid,
                            iswallet, city, tariffid, paymentid);
                }
            } else {
                showAlert("You are not connected to the Internet");
            }
        }

        gobackDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashBoardScreen.class);
                startActivity(intent);
            }
        });


        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                int response;
                if (vendorid.equals("13")) {
                    messageShow.setText(activateSimApiWalletLycaa.response);
                    response = activateSimApiWalletLycaa.responsecode;
                } else {
                    messageShow.setText(activateSimApiWalletH20.response);
                    response = activateSimApiWalletH20.responsecode;
                }
                idShowPaypal.setVisibility(View.INVISIBLE);
                amountShowPaypal.setVisibility(View.INVISIBLE);


                if (response == 0) {
                    setImageResponse.setImageResource(R.drawable.smile);

                } else {
                    setImageResponse.setImageResource(R.drawable.sad);

                }
            }
        });

        progressDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {


            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                int response;
                if (vendorid.equals("13")) {
                    messageShow.setText(activateSimApiPaypalLycaa.response);
                    response = activateSimApiPaypalLycaa.responsecode;

                } else {
                    messageShow.setText(activateSimApiPaypalH20.response);
                    response = activateSimApiPaypalH20.responsecode;

                }
                if (response == 0) {
                    setImageResponse.setImageResource(R.drawable.smile);
                    statusShowPaypal.setText("Success");

                } else {
                    setImageResponse.setImageResource(R.drawable.sad);
                    statusShowPaypal.setText("Failure");

                }
            }

        });
    }

    private void showAlert(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("A H P R E P A I D")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .setIcon(R.drawable.ahprepaidlogo)
                .show();

    }

    @Override
    public void onBackPressed() {
    }
}

