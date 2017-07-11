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
import android.widget.TextView;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.ActivateSimApi;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.ActivateSimApiPaypal;
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

    ActivateSimApi activateSimApi;
    ActivateSimApiPaypal activateSimApiPaypal;
    TextView messageShow;


    TextView amountShowPaypal;
    TextView statusShowPaypal;
    TextView idShowPaypal;

    Button gobackDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_confirm);
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

        gobackDashboard = (Button) findViewById(R.id.dashBoardButton4);


        setImageResponse = (ImageView) findViewById(R.id.messageImageView4);
        messageShow = (TextView) findViewById(R.id.messageTextView4);

        if (paymenttype.compareTo("wallet") == 0) {
            iswallet = "1";

            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                activateSimApi = new ActivateSimApi(getApplicationContext(), plans, Amount, email, operators,
                        zipCode, simCard, clienttypeid, loginid, distrbutorid,
                        iswallet, month, tariffid);
            } else {
                showAlert("You are not connected to the Internet");
            }
        }
        if (paymenttype.compareTo("paypal") == 0) {
            iswallet = "2";
            progressDialog2.show();
            progressDialog2.setCancelable(false);
            progressDialog2.setCanceledOnTouchOutside(false);

            paymentid = getIntent().getStringExtra("paymentId");

            if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                activateSimApiPaypal = new ActivateSimApiPaypal(getApplicationContext(), plans, Amount, email, operators,
                        zipCode, simCard, clienttypeid, loginid, distrbutorid,
                        iswallet, month, tariffid, paymentid);
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
                messageShow.setText(activateSimApi.response);
                idShowPaypal.setVisibility(View.INVISIBLE);
                amountShowPaypal.setVisibility(View.INVISIBLE);
                if (activateSimApi.responsecode == 0) {
                    setImageResponse.setImageResource(R.drawable.smile);

                } else {
                    setImageResponse.setImageResource(R.drawable.sad);

                }
            }
        });

        progressDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {


            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                //  idShowPaypal.setText(paymentid);
                //    amountShowPaypal.setText(Amount);
                //  messageShow.setText(activateSimApi.response);
                if (activateSimApiPaypal.responsecode == 0) {
                    setImageResponse.setImageResource(R.drawable.smile);
                    messageShow.setText(activateSimApiPaypal.response);
                    idShowPaypal.setText(paymentid);
                    statusShowPaypal.setText("Success");
                    amountShowPaypal.setText(Amount);

                } else {
                    setImageResponse.setImageResource(R.drawable.sad);
                    messageShow.setText(activateSimApiPaypal.response);
                    idShowPaypal.setText(paymentid);
                    statusShowPaypal.setText("Failure");
                    amountShowPaypal.setText(Amount);
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

