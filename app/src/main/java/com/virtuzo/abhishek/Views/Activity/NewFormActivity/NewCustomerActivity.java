package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.Customer;
import com.virtuzo.abhishek.modal.GSTCategory;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.State;

import java.util.ArrayList;
import java.util.Arrays;

public class NewCustomerActivity extends AppCompatActivity  {

    ArrayList entity_type_list, gst_category_list, state_list;
    ArrayAdapter entity_adapter, gst_adapter, state_adapter;
    Spinner entity_spinner, gst_spinner, state_spinner;
    EditText  entityname, contactperson, con_number, con_landlineno, emailid, gst_no, et_city, et_address, et_pincode;
    TextView entityNameAsteriskTextView, gstNumberAsteriskTextView;
    LinearLayout gst_linear, gst_no_linear;
    Toolbar toolbar;
    String existingCustomerId = "";
    boolean edittingSyncedCustomer = false;

    String entityType="", entityName="", contactPerson="", contactNumber="", contactLandline="", emailID="", gstNumber="", gstCategory="", addresss="", state="", city="" , pincode="";
    int gstCategoryID, stateID;

    final int PROPRIETORSHIP=1, PVT_LTD=2, LTD=3,LLP=4, PARTNERSHIP=5, INDIVIDUAL=6;


    String callingActivity;

    Customer newCustomer;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        TextView title = (TextView) findViewById(R.id.actionBarTitle);
        title.setText("New Customer");

        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);

        entity_type_list= new ArrayList<String>(Arrays.asList("Choose Type", "Proprietorship", "Pvt. Ltd.", "Ltd.", "LLP", "Partnership", "Individual"));

        gst_category_list= new ArrayList<GSTCategory>();
        gst_category_list.add(new GSTCategory(0, "Choose Category"));
        gst_category_list.addAll(DatabaseHandler.getInstance().getGSTCategoryListForCustomerFromDB());

        state_list= new ArrayList<State>();
        state_list.add(new State(0, "Choose State"));
        state_list.addAll(DatabaseHandler.getInstance().getStateListFromDB());

        entity_spinner=(Spinner) findViewById(R.id.spinner_entitytype);
        gst_spinner=(Spinner)findViewById(R.id.spinner_gst_category);
        state_spinner=(Spinner)findViewById(R.id.spinner_state);

        entityname=(EditText)findViewById(R.id.et_entityname);
        entityname.setFilters(new InputFilter[] { MyFunctions.filter });
        contactperson=(EditText)findViewById(R.id.et_contactperson);
        contactperson.setFilters(new InputFilter[] { MyFunctions.filter });
        con_number=(EditText)findViewById(R.id.et_contactnumber);
        con_landlineno=(EditText)findViewById(R.id.et_landlinenumber);
        emailid=(EditText)findViewById(R.id.et_emailid);
        emailid.setFilters(new InputFilter[] { MyFunctions.filter });
        gst_no=(EditText)findViewById(R.id.et_gst_number);
        gst_no.setFilters(new InputFilter[] { MyFunctions.filter });
        et_city=(EditText)findViewById(R.id.et_city);
        et_city.setFilters(new InputFilter[] { MyFunctions.filter });
        et_address=(EditText)findViewById(R.id.et_address);
        et_address.setFilters(new InputFilter[] { MyFunctions.filter });
        et_pincode=(EditText)findViewById(R.id.et_pincode);
        gst_linear= (LinearLayout)findViewById(R.id.gst_linear);
        gst_no_linear= (LinearLayout)findViewById(R.id.gst_number_linear);
        entityNameAsteriskTextView = (TextView) findViewById(R.id.entityNameAsterisk);
        gstNumberAsteriskTextView = (TextView) findViewById(R.id.gstNumberAsterisk);

        newCustomer= new Customer();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        entity_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, entity_type_list);
        entity_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        entity_spinner.setAdapter(entity_adapter);
        entity_spinner.setSelection(INDIVIDUAL);

        gst_adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item, gst_category_list);
        gst_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gst_spinner.setAdapter(gst_adapter);

        state_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, state_list);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(state_adapter);
        int stateId = Integer.parseInt(setting.getString("StateID", "0"));
        for(int i=0; i<state_list.size(); i++) {
            State state = (State) state_list.get(i);
            if(state.getStateID() == stateId) {
                state_spinner.setSelection(i);
            }
        }

        entity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    onEntityTypeSelect(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        gst_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    onGSTCategorySelect(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        callingActivity = getIntent().getExtras().getString(MyFunctions.CLASS_NAME, "");

        if(callingActivity.equals(MyFunctions.CLASS_CUSTOMER_VIEW)) {
            title.setText("Edit Customer");
            setField(getIntent().getExtras().getString("customer"));
        }
    }

    private void setField(String customerJSON) {
        Gson gson= new Gson();
        Customer customer= gson.fromJson(customerJSON, Customer.class);

        existingCustomerId = customer.getCustomerId();
        if (customer.getEntityType() != null) {
            // int spinnerPosition = entity_adapter.getPosition(customer.getEntityType());
            entity_spinner.setSelection( entity_adapter.getPosition( customer.getEntityType()));
        }
        entityname.setText(customer.getEntityName());
        contactperson.setText(customer.getContactPerson());
        con_number.setText(customer.getContactNumber());
        con_landlineno.setText(customer.getContactLandline());
        emailid.setText(customer.getContactEmailId());

        if (customer.getCategoryId() != 0) {
            // int spinnerPosition = entity_adapter.getPosition(customer.getEntityType());
            gst_spinner.setSelection(customer.getCategoryId());
        }

        if (customer.getCategoryId() != 0) {
            gst_no.setText(customer.getGSTNumber());
        }
        et_address.setText(customer.getAddress());
        state_spinner.setSelection(customer.getStateId());
        et_city.setText(customer.getCity());
        et_pincode.setText(customer.getPinCode());
        if(customer.getSynced() != 0) {
            edittingSyncedCustomer = true;
        }
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    private void  onEntityTypeSelect(int s){
        switch (s){
            case INDIVIDUAL :
                gst_linear.setVisibility(View.GONE);
                entityNameAsteriskTextView.setVisibility(View.GONE);
                entityname.setEnabled(false);
                gst_spinner.setSelection(GSTCategory.GST_UNREGISTERED);
                break;

            default:
                gst_linear.setVisibility(View.VISIBLE);
                entityNameAsteriskTextView.setVisibility(View.VISIBLE);
                entityname.setEnabled(true);
                break;
        }
    }

    private void onGSTCategorySelect(int s) {
        switch (s){
            case GSTCategory.GST_UNREGISTERED:
                gst_no_linear.setVisibility(View.GONE);
                break;

            default:
                gst_no_linear.setVisibility(View.VISIBLE);
                break;
        }
        switch(s) {
            case GSTCategory.SEZ:
            case GSTCategory.IMPORTER:
                gstNumberAsteriskTextView.setVisibility(View.GONE);
                break;
            default:
                gstNumberAsteriskTextView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void save(View view){
        //Validating Form
        if (entity_spinner.getSelectedItemPosition()!=0){
            entityType=entity_spinner.getSelectedItem().toString();
        }else{
            Toast.makeText(this, "Select Entity type", Toast.LENGTH_SHORT).show();
            entity_spinner.requestFocus();
            return;
        }

        if(!TextUtils.isEmpty(entityname.getText().toString())){
            entityName=entityname.getText().toString();
        } else {
            if(entity_spinner.getSelectedItemPosition()!=INDIVIDUAL) {
                entityname.setError("Enter valid name");
                entityname.requestFocus();
                return;
            }
        }

        if (!TextUtils.isEmpty(contactperson.getText().toString())){
            contactPerson=contactperson.getText().toString();
        }else{
            contactperson.setError("Enter valid name");
            contactperson.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(con_number.getText().toString())  ){
            if(con_number.getText().toString().length()!=10 ){
                con_number.setError("Enter valid number");
                con_number.requestFocus();
                return;
            }else {
                contactNumber = con_number.getText().toString();
            }
        }else{
            con_number.setError("Enter valid number");
            con_number.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(con_landlineno.getText().toString())  ){
            if(con_landlineno.getText().toString().length() > 20){
                con_landlineno.setError("Enter valid number");
                con_landlineno.requestFocus();
                return;
            }else {
                contactLandline = con_landlineno.getText().toString();
            }
        }

        if (!TextUtils.isEmpty(emailid.getText().toString())   ){
            if(isEmailvalid(emailid.getText().toString())){
                emailID=emailid.getText().toString();
            }else{
                Toast.makeText(this, "Enter valid email id", Toast.LENGTH_SHORT).show();
                emailid.requestFocus();
                return;
            }

        }
//
//        else{
//            emailid.setError("Enter email id");
//            emailid.requestFocus();
//            return;
//        }

        if(entity_spinner.getSelectedItemPosition()!= INDIVIDUAL) {
            if (gst_spinner.getSelectedItemPosition() != 0) {
                gstCategory = gst_spinner.getSelectedItem().toString();
                gstCategoryID = ((GSTCategory)gst_spinner.getSelectedItem()).getID();
            } else {
                Toast.makeText(this, "Select GST Category", Toast.LENGTH_SHORT).show();
                gst_spinner.requestFocus();
                return;
            }

            if (!TextUtils.isEmpty(gst_no.getText().toString())) {
                if(gst_spinner.getSelectedItemPosition() == GSTCategory.GST_UNREGISTERED) {
                    gstNumber = "";
                } else {
                    gstNumber = gst_no.getText().toString();
                }
            } else {
                switch (gst_spinner.getSelectedItemPosition()) {
                    case GSTCategory.GST_UNREGISTERED:
                    case GSTCategory.SEZ:
                    case GSTCategory.IMPORTER:
                        break;
                    default:
                        gst_no.setError("Enter valid number");
                        gst_no.requestFocus();
                        return;
                }
            }
        } else {
            gst_spinner.setSelection(GSTCategory.GST_UNREGISTERED);
            gstCategory = gst_spinner.getSelectedItem().toString();
            gstCategoryID = ((GSTCategory)gst_spinner.getSelectedItem()).getID();
        }

        if (state_spinner.getSelectedItemPosition()!=0){
            state=state_spinner.getSelectedItem().toString();
            stateID = ((State)state_spinner.getSelectedItem()).getStateID();
        }else{
            Toast.makeText(this, "Select State", Toast.LENGTH_SHORT).show();
            state_spinner.requestFocus();
            return;
        }

        if(et_pincode.getText().toString().length() != 0) {
            if (et_pincode.getText().toString().length() != 6) {
                et_pincode.setError("Enter valid Pin Code");
                et_pincode.requestFocus();
                return;
            } else {
                pincode = et_pincode.getText().toString();
            }
        }

        addresss=et_address.getText().toString();
        city = et_city.getText().toString();

        newCustomer.setEntityType(entityType);
        newCustomer.setEntityName(entityName);
        newCustomer.setContactPerson(contactPerson);
        newCustomer.setContactNumber(contactNumber);
        newCustomer.setContactLandline(contactLandline);
        newCustomer.setContactEmailId(emailID);
        newCustomer.setCategoryId(gstCategoryID);
        newCustomer.setCategoryName(gstCategory);
        newCustomer.setGSTNumber(gstNumber);
        newCustomer.setAddress(addresss);
        newCustomer.setStateId(stateID);
        newCustomer.setStateName(state);
        newCustomer.setCity(city);
        newCustomer.setPinCode(pincode);

        //TODO customerID and categoryID
        //customerid
        //categoryid

        if(callingActivity.equals(MyFunctions.CLASS_CUSTOMER_VIEW)) {
            newCustomer.setCustomerId(existingCustomerId);
            if(DatabaseHandler.getInstance().isCustomerContactNumberAlreadyExists(newCustomer)) {
                con_number.setError("Contact number already exists");
                con_number.requestFocus();
                return;
            }
            if(edittingSyncedCustomer) {
                newCustomer.setSynced(DatabaseHandler.CUSTOMER_SYNCED_CODE_Edit);
            }

            progressDialog.show();

            DatabaseHandler.getInstance().updateCurrentCustomer(newCustomer);
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(NewCustomerActivity.this);
            final View v = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
            builder.setView(v);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button okButton = (Button) v.findViewById(R.id.okClick);
            TextView okMessageTextView = (TextView) v.findViewById(R.id.okMessage);
            okMessageTextView.setText("Customer Updated!!");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String json = gson.toJson(newCustomer);

                    Intent intent = new Intent();
                    intent.putExtra("customer", json);
                    setResult(Activity.RESULT_OK, intent);
                    dialog.dismiss();
                    finish();
                }
            });
        } else {
            if(DatabaseHandler.getInstance().isCustomerContactNumberAlreadyExists(newCustomer)) {
                con_number.setError("Contact number already exists");
                con_number.requestFocus();
                return;
            }

            progressDialog.show();

            SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
            String TabCode = setting.getString("TabCode", "");
            String LatestCustomerID = setting.getString(DashBoardScreen.SharedPref_LatestCustomerID, "");
            String newCustomerID = createNewCustomerID(LatestCustomerID);
            newCustomerID = TabCode + newCustomerID;

            newCustomer.setCustomerId(newCustomerID);
            DatabaseHandler.getInstance().addNewCustomer(newCustomer);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString(DashBoardScreen.SharedPref_LatestCustomerID, newCustomerID);
            editor.commit();
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(NewCustomerActivity.this);
            final View v = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
            builder.setView(v);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button okButton = (Button) v.findViewById(R.id.okClick);
            TextView okMessageTextView = (TextView) v.findViewById(R.id.okMessage);
            okMessageTextView.setText("New Customer Created!!");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (callingActivity.equals(MyFunctions.CLASS_NEW_INVOICE)
                            || callingActivity.equals(MyFunctions.CLASS_NEW_CREDITNOTE)
                            || callingActivity.equals(MyFunctions.CLASS_DELIVERY_ORDER)) {
                        Gson gson = new Gson();
                        String json = gson.toJson(newCustomer);

                        Intent intent = new Intent();
                        intent.putExtra("customer", json);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        NewCustomerActivity.this.onBackPressed();
                    }
                }
            });
        }
    }

    private String createNewCustomerID(String latestTabCode) {
        String previousCustomerSeriesString = latestTabCode.substring(latestTabCode.length() - 5);
        int newCustomerSeries = Integer.parseInt(previousCustomerSeriesString) + 1;
        String newCustomerSeriesString = "00000" + newCustomerSeries;
        newCustomerSeriesString = newCustomerSeriesString.substring(newCustomerSeriesString.length() - 5);
        return newCustomerSeriesString;
    }

    private boolean isEmailvalid(String s) {

        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        //String email1 = "user@domain.com";
        Boolean b = s.matches(EMAIL_REGEX);

        return b;
    }

    public void reset(View view){

        entityname.setText("");
        contactperson.setText("");
        con_number.setText("");
        con_landlineno.setText("");
        emailid.setText("");
        gst_no.setText("");
        et_city.setText("");
        et_address.setText("");
        et_pincode.setText("");
        entity_spinner.setSelection(0);
        gst_spinner.setSelection(0);
        state_spinner.setSelection(0);
        entity_spinner.requestFocus();

    }

    public void back(View view){

        onBackPressed();

    }



//    public String getDeviceIMEI() {
//        String deviceUniqueIdentifier = null;
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        if (null != tm) {
//
//            deviceUniqueIdentifier = tm.getDeviceId();
//        }
//        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
//            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        }
//        return deviceUniqueIdentifier;
    //   }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//        Toast.makeText(this, ""+entity_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//
//    }

}


