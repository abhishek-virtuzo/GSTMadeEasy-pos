package com.virtuzoconsultancyservicespvtltd.ahprepaid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.PortSimPaymentActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.SelectActivationPlanActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity.SelectOperatorActivity;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.ActivationPlanClass;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;

public class Portln extends AppCompatActivity {

    final int OPERATOR_SELECT_REQUEST_CODE = 101;
    final int PLAN_SELECT_REQUEST_CODE = 102;
    Button PortIn;
    EditText simCardNO;
    EditText operatorEditText;
    EditText zipcodeEditText;
    EditText planEditText;
    EditText amountEditText;
    EditText cityEditText;
    EditText Emailid;
    EditText phonePort;
    EditText Pin;
    EditText accountEditText;
    EditText currentServiceProviderEditText;
    EditText state;
    EditText customer;
    EditText address;
    RelativeLayout cityRelativeLayout;

    LinearLayout planMessageLinearLayout;


    String vendorId;
    LinearLayout hide;

    TextView simNoTextView;
    TextView operatorTextView;
    TextView zipcodeTextView;
    TextView planTextView;
    TextView amountNoTextView;
    TextView emailTextView;
    TextView customerTextView;
    TextView cityTextView;
    TextView phonetoPortTextView;
    TextView pinTextView;
    TextView accountTextView;
    TextView currentServiceProviderTextView;
    TextView stateTextView;
    TextView addressTextView;

    View operatorSelectView;
    View planSelectView;
    String operator, simCard, zipCode, Amount, email, clienttypeid, distrbutorid, loginid, month;


    Boolean isOperatorSelected;
    Boolean isPlanSelected;
    Boolean isZipcodeEntered;

    ActivationPlanClass selectedPlan;
    OperatorClass selectedOperator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portln);


        initGUI();
        initVariables();
        setGUIBehaviour();


    }

    private void initGUI() {

        email = getIntent().getStringExtra("strEmailId");
        clienttypeid = getIntent().getStringExtra("ClientTypeID");
        distrbutorid = getIntent().getStringExtra("DistributorID");
        loginid = getIntent().getStringExtra("LoginID");

        operatorEditText = (EditText) findViewById(R.id.operatorEditText1);
        zipcodeEditText = (EditText) findViewById(R.id.zipcodeEditText1);
        customer = (EditText) findViewById(R.id.customernameid1);
        state = (EditText) findViewById(R.id.stateid1);
        address = (EditText) findViewById(R.id.addressid1);
        planEditText = (EditText) findViewById(R.id.planEditText1);
        amountEditText = (EditText) findViewById(R.id.amountEditText1);
        cityEditText = (EditText) findViewById(R.id.cityid1);
        simCardNO = (EditText) findViewById(R.id.simcardno);
        Pin = (EditText) findViewById(R.id.pinEditText1);
        accountEditText = (EditText) findViewById(R.id.acountid1);
        currentServiceProviderEditText = (EditText) findViewById(R.id.currentserviceproviderid1);

        hide = (LinearLayout) findViewById(R.id.hide);

        PortIn = (Button) findViewById(R.id.proceed1);
        PortIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plan = null, tarriffid = null, month = null;

                String zipcode = String.valueOf(zipcodeEditText.getText().toString());
                String simcard = simCardNO.getText().toString();
                if (selectedPlan != null) {
                    plan = selectedPlan.getTariffPlan();
                    tarriffid = String.valueOf(selectedPlan.getTariffID());
                    month = selectedPlan.getMonths();
                }


                if (!isOperatorSelected) {
                    showAlert("Please select an operator");
                } else if (simCardNO.length() != 19 && selectedOperator.getVendorID() == 13) {
                    showAlert("Enter a valid 19 digit simcard no");
                } else if (simCardNO.length() != 20 && selectedOperator.getVendorID() != 13) {//change later to 20 after testing
                    showAlert("Enter a valid 20 digit simcard no");
                } else if (!isPlanSelected) {
                    showAlert("Please select a plan");
                } else if (zipcode.length() != 5) {
                    showAlert("Please enter a valid 5-digit zipcode");
                } else if (Pin.getText().toString().isEmpty()) {
                    showAlert("Please enter a valid pincode");

                } else if (phonePort.getText().toString().isEmpty() && phonePort.getText().length() != 10) {
                    showAlert("Please enter the no you want to port");
                } else if (accountEditText.getText().toString().isEmpty()) {
                    showAlert("Please enter your Account number");
                } else if (selectedOperator.getVendorID() != 13 && currentServiceProviderEditText.getText().toString().isEmpty()) {
                    showAlert("Please enter you service provider");
                } else if (selectedOperator.getVendorID() != 13 && state.getText().toString().isEmpty()) {
                    showAlert("Please enter your state name");
                } else if (selectedOperator.getVendorID() != 13 && cityEditText.getText().toString().isEmpty()) {
                    showAlert("Please enter your city name");
                } else if (selectedOperator.getVendorID() != 13 && customer.getText().toString().isEmpty()) {
                    showAlert("Please enter your name");
                } else if (selectedOperator.getVendorID() != 13 && address.getText().toString().isEmpty()) {
                    showAlert("Please enter your address");
                } else {
                    if ((new ConnectionDetector(getApplicationContext())).isConnectingToInternet()) {
                        Intent intent = new Intent(getApplicationContext(), PortSimPaymentActivity.class);
                        String pincode = String.valueOf(Pin.getText().toString());
                        String phoneport = String.valueOf(phonePort.getText().toString());
                        String accounts = String.valueOf(accountEditText.getText().toString());
                        String states = String.valueOf(state.getText().toString());
                        String cities = String.valueOf(cityEditText.getText().toString());
                        String customers = String.valueOf(customer.getText().toString());
                        String addres = String.valueOf(address.getText().toString());
                        String serviceProvider = String.valueOf(currentServiceProviderEditText.getText().toString());
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
                        intent.putExtra("vendorid", vendorId);
                        intent.putExtra("pin", pincode);
                        intent.putExtra("phoneport", phoneport);
                        intent.putExtra("account", accounts);
                        intent.putExtra("state", states);
                        intent.putExtra("city", cities);
                        intent.putExtra("customer", customers);
                        intent.putExtra("address", addres);
                        intent.putExtra("serviceprovider", serviceProvider);


                        startActivity(intent);
                        //          progressDialog.show();
                        //         ActivateSimApi= new ActivateSimApi(getApplicationContext(),plan,Amount,email,operator,zipcode,simCard);
                    } else {
                        showAlert("You are not connected to the Internet");
                    }
                }

            }
        });

//        simCardNO.addTextChangedListener(new NumberTextWatcher(simCardNO));
        Emailid = (EditText) findViewById(R.id.emailid1);
        phonePort = (EditText) findViewById(R.id.PhonetoPortEditText1);
        stateTextView = (TextView) findViewById(R.id.stateTextView1);
        simNoTextView = (TextView) findViewById(R.id.simCardNoTextView);
        operatorTextView = (TextView) findViewById(R.id.operatorTextView1);
        zipcodeTextView = (TextView) findViewById(R.id.zipcodeTextView1);
        planTextView = (TextView) findViewById(R.id.planTextView1);
        addressTextView = (TextView) findViewById(R.id.addressTextView1);
        amountNoTextView = (TextView) findViewById(R.id.amountTextView1);
        emailTextView = (TextView) findViewById(R.id.emailTextView1);
        cityTextView = (TextView) findViewById(R.id.cityTextView1);
        operatorSelectView = (View) findViewById(R.id.operatorSelectView);
        planSelectView = (View) findViewById(R.id.planSelectView);
        customerTextView = (TextView) findViewById(R.id.customernameTextView1);
        phonetoPortTextView = (TextView) findViewById(R.id.phonetoPortTextView1);
        pinTextView = (TextView) findViewById(R.id.pinTextView1);
        accountTextView = (TextView) findViewById(R.id.acountTextView1);
        currentServiceProviderTextView = (TextView) findViewById(R.id.currentserviceproviderTextView1);

        planMessageLinearLayout = (LinearLayout) findViewById(R.id.planMessageLinearLayout1);


    }

    private void setGUIBehaviour() {

        simCardNO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    simNoTextView.setVisibility(View.INVISIBLE);
                else
                    simNoTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        operatorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    operatorTextView.setVisibility(View.INVISIBLE);
                else
                    operatorTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    addressTextView.setVisibility(View.INVISIBLE);
                else
                    addressTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        zipcodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    zipcodeTextView.setVisibility(View.INVISIBLE);
                else
                    zipcodeTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    customerTextView.setVisibility(View.INVISIBLE);
                else
                    customerTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    stateTextView.setVisibility(View.INVISIBLE);
                else
                    stateTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    cityTextView.setVisibility(View.INVISIBLE);
                else
                    cityTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phonePort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    phonetoPortTextView.setVisibility(View.INVISIBLE);
                else
                    phonetoPortTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    pinTextView.setVisibility(View.INVISIBLE);
                else
                    pinTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        planEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    planTextView.setVisibility(View.INVISIBLE);
                    planMessageLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    planTextView.setVisibility(View.VISIBLE);
                    planMessageLinearLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    amountNoTextView.setVisibility(View.INVISIBLE);
                else
                    amountNoTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Emailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    emailTextView.setVisibility(View.INVISIBLE);
                else
                    emailTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        accountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    accountTextView.setVisibility(View.INVISIBLE);
                else
                    accountTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        currentServiceProviderEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    currentServiceProviderTextView.setVisibility(View.INVISIBLE);
                else
                    currentServiceProviderTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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

                    vendorId = String.valueOf(selectedOperator.getVendorID());
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
                if (selectedOperator.getVendorID() != 13) {
                    hide.setVisibility(View.VISIBLE);
                }

                amountEditText.setText("");
                planEditText.setText("");
                cityEditText.setText("");
                simCardNO.setText("");

                isPlanSelected = false;


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
