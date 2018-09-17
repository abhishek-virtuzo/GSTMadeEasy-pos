package com.virtuzo.abhishek.Views.Activity.ViewActivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewCustomerActivity;
import com.virtuzo.abhishek.modal.Customer;
import com.virtuzo.abhishek.modal.MyFunctions;

public class CustomerViewActivity extends AppCompatActivity {

    TextView tv_entityType, tv_entityName, tv_contactperson, tv_contactno, tv_landlineno, tv_email, tv_gstcategory,
            tv_gstno, tv_address, tv_state, tv_city, tv_pincode;
    Customer customer;
    String customerJSON;
    int EDIT_CUSTOMER_RESPONSE_CODE = 100;
    TextView entityTypeLabel, entityNameLabel, contactPersonLabel, contactNumberLabel, contactLandlineNumberLabel,
             emailIdLabel, gstCategoryLabel, gstNumberLabel, addressLabel, stateLabel, cityLabel, pinCodeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar.findViewById(R.id.actionBarTitle);
        heading.setText("Customer Details");

        entityTypeLabel = (TextView) findViewById(R.id.entityTypeLabel);
        entityNameLabel = (TextView) findViewById(R.id.entityNameLabel);
        contactPersonLabel = (TextView) findViewById(R.id.contactPersonLabel);
        contactNumberLabel = (TextView) findViewById(R.id.contactNumberLabel);
        contactLandlineNumberLabel = (TextView) findViewById(R.id.contactLandlineNumberLabel);
        emailIdLabel = (TextView) findViewById(R.id.emailIdLabel);
        gstCategoryLabel = (TextView) findViewById(R.id.gstCategoryLabel);
        gstNumberLabel = (TextView) findViewById(R.id.gstNumberLabel);
        addressLabel = (TextView) findViewById(R.id.addressLabel);
        stateLabel = (TextView) findViewById(R.id.stateLabel);
        cityLabel = (TextView) findViewById(R.id.cityLabel);
        pinCodeLabel = (TextView) findViewById(R.id.pinCodeLabel);

        tv_entityType= (TextView)findViewById(R.id.entity_type);
        tv_entityName= (TextView)findViewById(R.id.entity_name);
        tv_contactperson= (TextView)findViewById(R.id.contact_person);
        tv_contactno= (TextView)findViewById(R.id.contact_number);
        tv_landlineno= (TextView)findViewById(R.id.contact_landline_number);
        tv_email= (TextView)findViewById(R.id.email_id);
        tv_gstno= (TextView)findViewById(R.id.gst_number);
        tv_gstcategory= (TextView)findViewById(R.id.gst_category);
        tv_address= (TextView)findViewById(R.id.address);
        tv_state= (TextView)findViewById(R.id.state);
        tv_city= (TextView)findViewById(R.id.city);
        tv_pincode= (TextView)findViewById(R.id.pincode);

        customerJSON= getIntent().getExtras().getString("customer");
        Gson gson= new Gson();

        customer= gson.fromJson(customerJSON, Customer.class);
        setCustomer(customer);
    }

    public void setCustomer(Customer customer) {
        tv_entityName.setText(customer.getEntityName());
        tv_entityType.setText(customer.getEntityType());
        tv_contactperson.setText(customer.getContactPerson());
        tv_contactno.setText(customer.getContactNumber());
        tv_landlineno.setText(customer.getContactLandline());
        tv_email.setText(customer.getContactEmailId());
        tv_gstno.setText(customer.getGSTNumber());
        tv_gstcategory.setText(customer.getCategoryName());
        tv_address.setText(customer.getAddress());
        tv_state.setText(customer.getStateName());
        tv_city.setText(customer.getCity());
        tv_pincode.setText(customer.getPinCode());

        if(customer.getEntityType().toString().equalsIgnoreCase("Individual")) {
            entityNameLabel.setVisibility(View.GONE);
            tv_entityName.setVisibility(View.GONE);
            gstCategoryLabel.setVisibility(View.GONE);
            tv_gstcategory.setVisibility(View.GONE);
            gstNumberLabel.setVisibility(View.GONE);
            tv_gstno.setVisibility(View.GONE);
        } else {
            entityNameLabel.setVisibility(View.VISIBLE);
            tv_entityName.setVisibility(View.VISIBLE);
            gstCategoryLabel.setVisibility(View.VISIBLE);
            tv_gstcategory.setVisibility(View.VISIBLE);
            gstNumberLabel.setVisibility(View.VISIBLE);
            tv_gstno.setVisibility(View.VISIBLE);
        }
    }

    public void onEdit(View view){
        Intent intent=new Intent(this, NewCustomerActivity.class);
        intent.putExtra("customer", customerJSON);
        intent.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_CUSTOMER_VIEW);
        startActivityForResult(intent, EDIT_CUSTOMER_RESPONSE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CUSTOMER_RESPONSE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String json = data.getStringExtra("customer");
                Gson gson = new Gson();
                Customer customer = gson.fromJson(json, Customer.class);
                setCustomer(customer);
                customerJSON = gson.toJson(customer);
            }
        }
    }

    public void onOK(View view){
        super.onBackPressed();
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

}

