package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ConnectionDetector;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.PlanClass;

public class RechargeActivity extends AppCompatActivity {

    final int OPERATOR_SELECT_REQUEST_CODE = 101;
    final int PLAN_SELECT_REQUEST_CODE = 102;

    EditText mobileNoEditText;
    EditText confirmMobileNoEditText;
    EditText operatorEditText;
    EditText zipcodeEditText;
    EditText planEditText;
    EditText amountEditText;

    View operatorSelectView;
    View planSelectView;
    View amountSelectView;

    Button proceed;

    OperatorClass selectedOperator;
    PlanClass selectedPlan;

    String TotalTopup;

    Boolean isMobileNoEntered;
    Boolean isMobileNoConfired;
    Boolean isOperatorSelected;
    Boolean isZipcodeEntered;
    Boolean isPlanSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        setTitle("SIM Recharge");

        initGUI();
        initVariables();

        setGUIBehaviour();

    }


    private void initGUI() {

        mobileNoEditText = (EditText) findViewById(R.id.mobileNoEditText);
        confirmMobileNoEditText = (EditText) findViewById(R.id.confirmMobileNoEditText);
        operatorEditText = (EditText) findViewById(R.id.operatorEditText);
        zipcodeEditText = (EditText) findViewById(R.id.zipcodeEditText);
        planEditText = (EditText) findViewById(R.id.planEditText);
        amountEditText = (EditText) findViewById(R.id.amountEditText);

        operatorSelectView = (View) findViewById(R.id.operatorSelectView);
        planSelectView = (View) findViewById(R.id.planSelectView);
        amountSelectView = (View) findViewById(R.id.amountSelectView);

        proceed = (Button) findViewById(R.id.proceed);

    }

    private void initVariables() {

        isMobileNoEntered = false;
        isMobileNoConfired = false;
        isOperatorSelected = false;
        isZipcodeEntered = false;
        isPlanSelected = false;

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

                        Intent intent = new Intent(getApplicationContext(), SelectPlanActivity.class);
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

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobileno = String.valueOf(mobileNoEditText.getText());
                String confirmMobileno = String.valueOf(confirmMobileNoEditText.getText());
                String zipcode = String.valueOf(zipcodeEditText.getText());


                if (mobileno.length() != 10) {
                    showAlert("Enter a valid mobile no");
                } else if (mobileno.compareTo(confirmMobileno) != 0) {
                    showAlert("Mobile Numbers do not match");
                } else if (!isOperatorSelected) {
                    showAlert("Please select an operator");
                } else if (zipcode.length() != 5) {
                    showAlert("Please enter a valid zipcode");
                } else if (!isPlanSelected) {
                    showAlert("Please select a plan");
                } else {

                    if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {

                        Intent intent = new Intent(getApplicationContext(), RechargePaymentActivity.class);

                        Gson gson = new Gson();

                        String operatorString = gson.toJson(selectedOperator);
                        String planString = gson.toJson(selectedPlan);

                        intent.putExtra("mobileno", mobileno);
                        intent.putExtra("operator", operatorString);
                        intent.putExtra("plan", planString);
                        intent.putExtra("zipcode", zipcode);
                        intent.putExtra("TotalTopup", TotalTopup);
                        startActivity(intent);

                    } else {
                        showAlert("You are not connected to the Internet");
                    }

                }


            }
        });

        operatorEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //operatorEditText.foc
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

                isOperatorSelected = true;

            }
        }

        if (requestCode == PLAN_SELECT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                String json = data.getStringExtra("plan");
                Gson gson = new Gson();
                selectedPlan = gson.fromJson(json, PlanClass.class);
                planEditText.setText(selectedPlan.getProductDescription());
                amountEditText.setText(Double.toString(selectedPlan.getTotalAmount()));

                isPlanSelected = true;

            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}