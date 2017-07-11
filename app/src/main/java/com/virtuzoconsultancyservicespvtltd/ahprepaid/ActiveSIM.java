package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.ActivateSimPaymentActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.SelectActivationPlanActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.SelectOperatorActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.ActivationPlanClass;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;

import java.util.ArrayList;
public class ActiveSIM extends AppCompatActivity {

    private static final String TAG = ActiveSIM.class.getSimpleName();
    public static ProgressDialog progressDialog;
    final int OPERATOR_SELECT_REQUEST_CODE = 101;
    final int PLAN_SELECT_REQUEST_CODE = 102;
    Button btnaActiveUsingAccountBalance;
    EditText simCardNO, SelectNetwork, SelectPlan, PlanAmount, Emailid, etSimCard, etEmail, etAmountToPay, etZipCode;
    //ActivateSimApi ActivateSimApi;
    Spinner spinTariff;
    Button ActivateSim;
    EditText operatorEditText;
    EditText zipcodeEditText;
    EditText planEditText;
    EditText amountEditText;
    EditText cityEditText;
    View operatorSelectView;
    View planSelectView;
    ActivationPlanClass selectedPlan;
    OperatorClass selectedOperator;
    Boolean isOperatorSelected;
    Boolean isPlanSelected;
    Boolean isZipcodeEntered;
    RelativeLayout relativecityEdit;
    String operator, simCard, zipCode, Amount, email, clienttypeid, distrbutorid, loginid, month;
    ArrayList<String> listTraiffcode;
    ArrayList<String> listTraiffId;
    ArrayAdapter<String> adapter;
    String TraiffID = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_sim);

        btnaActiveUsingAccountBalance = (Button) findViewById(R.id.btnaActiveUsingAccountBalance);
        simCardNO = (EditText) findViewById(R.id.simcardno);


        Emailid = (EditText) findViewById(R.id.emailid);
        email = getIntent().getStringExtra("strEmailId");
        clienttypeid = getIntent().getStringExtra("ClientTypeID");
        distrbutorid = getIntent().getStringExtra("DistributorID");
        loginid = getIntent().getStringExtra("LoginID");

        // relativecityEdit=(RelativeLayout)findViewById(R.id.relativehide);
        //   progressDialog = new ProgressDialog(this);
        initGUI();
        setGUIBehaviour();
        initVariables();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActiveSIM.this, DashBoardScreen.class);
        startActivity(intent);

    }

    private void initGUI() {


        operatorEditText = (EditText) findViewById(R.id.operatorEditText);
        zipcodeEditText = (EditText) findViewById(R.id.zipcodeEditText);
        planEditText = (EditText) findViewById(R.id.planEditText);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        cityEditText = (EditText) findViewById(R.id.city);

        operatorSelectView = (View) findViewById(R.id.operatorSelectView);
        planSelectView = (View) findViewById(R.id.planSelectView);
//        amountSelectView = (View) findViewById(R.id.amountSelectView);
//
        ActivateSim = (Button) findViewById(R.id.proceed);

    }

    private void setGUIBehaviour() {

        operatorSelectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                    Intent intent = new Intent(getApplicationContext(), SelectOperatorActivity.class);

                    startActivityForResult(intent, OPERATOR_SELECT_REQUEST_CODE);

                } else {
                    showAlert("You are not connected to the Internet");
                }
            }
        });


        planSelectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOperatorSelected) {

                    if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {

                        Intent intent = new Intent(getApplicationContext(), SelectActivationPlanActivity.class);
                        intent.putExtra("vendorId", selectedOperator.getVendorID());

                        startActivityForResult(intent, PLAN_SELECT_REQUEST_CODE);

                    } else {
                        showAlert("You are not connected to the Internet");
                    }

                } else {
                    showAlert("Please select an operator");
                }
            }
        });
        ActivateSim.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String zipcode = String.valueOf(zipcodeEditText.getText().toString());
                String simcard = simCardNO.getText().toString();
                String plan = selectedPlan.getTariffPlan();
                String tarriffid = String.valueOf(selectedPlan.getTariffTypeID());
                String month = selectedPlan.getMonths();

                if (!isOperatorSelected) {
                    showAlert("Please select an operator");
                } else if (simCardNO.length() != 19) {
                    showAlert("Enter a valid 19 digit simcard no");
                } else if (!isPlanSelected) {
                    showAlert("Please select a plan");
                } else if (zipcode.length() != 5) {
                    showAlert("Please enter a valid zipcode");

                } else if (selectedOperator.getVendorID() != 13) {
                    if (cityEditText.getText().toString().isEmpty()) {
                        showAlert("Please enter city Name");
                    }
                } else {
                    if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                        Intent intent = new Intent(ActiveSIM.this, ActivateSimPaymentActivity.class);
//                        if(selectedOperator.getVendorID()!=13){
//                            intent.putExtra("city",cityEditText.getText().toString());
//                        }
                        intent.putExtra("plan", plan);
                        intent.putExtra("Amount", Amount);
                        intent.putExtra("email", email);
                        intent.putExtra("operator", operator);
                        intent.putExtra("zipcode", zipcode);
                        intent.putExtra("simCard", simcard);
                        intent.putExtra("ClientTypeID", clienttypeid);
                        intent.putExtra("DistributorID", distrbutorid);
                        intent.putExtra("LoginID", loginid);
                        intent.putExtra("month", month);
                        intent.putExtra("tariffid", tarriffid);

                        startActivity(intent);
                        //          progressDialog.show();
                        //         ActivateSimApi= new ActivateSimApi(getApplicationContext(),plan,Amount,email,operator,zipcode,simCard);
                    } else {
                        showAlert("You are not connected to the Internet");
                    }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPERATOR_SELECT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                String json = data.getStringExtra("vendor");
                Gson gson = new Gson();
                selectedOperator = gson.fromJson(json, OperatorClass.class);
                operatorEditText.setText(selectedOperator.getVendorName());
                operator = selectedOperator.getVendorName();
//                if(selectedOperator.getVendorID()==13){
//                   relativecityEdit.setVisibility(View.INVISIBLE);
//                }
//                else{
//                    relativecityEdit.setVisibility(View.VISIBLE);
//                }

                isOperatorSelected = true;

            }
        }
        if (requestCode == PLAN_SELECT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                String json = data.getStringExtra("plan");
                Gson gson = new Gson();

                selectedPlan = gson.fromJson(json, ActivationPlanClass.class);
                planEditText.setText(selectedPlan.getTariffPlan());
                amountEditText.setText(Double.toString(selectedPlan.getAmount()));
                Emailid.setText(email);
//                plan = selectedPlan.getPlanDescription();
                Amount = String.valueOf(selectedPlan.getAmount());

                isPlanSelected = true;

            }
        }

    }

    private void initVariables() {

//        isMobileNoEntered = false;
//        isMobileNoConfired = false;
        isOperatorSelected = false;
        isZipcodeEntered = false;
        isPlanSelected = false;

    }
}
