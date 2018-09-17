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
import com.virtuzo.abhishek.modal.GSTCategory;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.State;
import com.virtuzo.abhishek.modal.Supplier;

import java.util.ArrayList;
import java.util.Arrays;

public class NewSupplierActivity extends AppCompatActivity {

    ArrayList entity_type_list, state_list;
    ArrayList<GSTCategory> gst_category_list;
    ArrayAdapter entity_adapter, gst_adapter, state_adapter;
    Spinner entity_spinner, gst_spinner, state_spinner;
    EditText entityname, contactperson, con_number, con_landlineno, emailid, gst_no, et_city, et_address, et_pincode;
    TextView entityNameAsteriskTextView, gstNumberAsteriskTextView;
    LinearLayout gst_linear, gst_no_linear;
    Toolbar toolbar;
    String entityType="", entityName="", contactPerson="", contactNumber="", contactLandline="", emailID="", gstNumber="", gstCategory="", addresss="", state="", city="" , pincode="";
    int gstCategoryID, stateID;
    boolean edittingSyncedSupplier = false;

    final int PROPRIETORSHIP=1, PVT_LTD=2, LTD=3,LLP=4, PARTNERSHIP=5, INDIVIDUAL=6;
    String existingSupplierId = "";

    Supplier newSupplier;
    ProgressDialog progressDialog;
    String callingActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_supplier);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        TextView title = (TextView) findViewById(R.id.actionBarTitle);
        title.setText("New Supplier");

        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);

        entity_type_list= new ArrayList<String>(Arrays.asList("Choose Type", "Proprietorship", "Pvt. Ltd.", "Ltd.", "LLP", "Partnership", "Individual"));

        //TODO change supplier gstcategory and statelist from DB
        gst_category_list= new ArrayList<>();
        gst_category_list.add(new GSTCategory(0, "Choose Category"));
        gst_category_list.addAll(DatabaseHandler.getInstance().getGSTCategoryListForSupplierFromDB());

        state_list= new ArrayList<State>();
        state_list.add(new State(0, "Choose State"));
        state_list.addAll(DatabaseHandler.getInstance().getStateListFromDB());

        entity_spinner=(Spinner) findViewById(R.id.spinner_entitytype);
        gst_spinner=(Spinner)findViewById(R.id.spinner_gst_category);
        state_spinner=(Spinner)findViewById(R.id.spinner_state);

        entityname=(EditText)findViewById(R.id.et_entityname);
        contactperson=(EditText)findViewById(R.id.et_contactperson);
        con_number=(EditText)findViewById(R.id.et_contactnumber);
        con_landlineno=(EditText)findViewById(R.id.et_landlinenumber);
        emailid=(EditText)findViewById(R.id.et_emailid);
        gst_no=(EditText)findViewById(R.id.et_gst_number);
        et_city=(EditText)findViewById(R.id.et_city);
        et_address=(EditText)findViewById(R.id.et_address);
        et_pincode=(EditText)findViewById(R.id.et_pincode);
        gst_linear= (LinearLayout)findViewById(R.id.gst_linear);
        gst_no_linear= (LinearLayout)findViewById(R.id.gst_number_linear);
        entityNameAsteriskTextView = (TextView) findViewById(R.id.entityNameAsterisk);
        gstNumberAsteriskTextView = (TextView) findViewById(R.id.gstNumberAsterisk);

        // filters
        entityname.setFilters(new InputFilter[] { MyFunctions.filter });
        contactperson.setFilters(new InputFilter[] { MyFunctions.filter });
        emailid.setFilters(new InputFilter[] { MyFunctions.filter });
        gst_no.setFilters(new InputFilter[] { MyFunctions.filter });
        et_city.setFilters(new InputFilter[] { MyFunctions.filter });
        et_address.setFilters(new InputFilter[] { MyFunctions.filter });

        newSupplier = new Supplier();
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
                    switch (i){
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        gst_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    switch (i){
                        case GSTCategory.GST_UNREGISTERED:
                            gst_no_linear.setVisibility(View.GONE);
                            break;

                        default:
                            gst_no_linear.setVisibility(View.VISIBLE);
                            break;
                    }
                    switch(i) {
                        case GSTCategory.SEZ:
                        case GSTCategory.IMPORTER: // it is actually an exporter, but importer by index
                            gstNumberAsteriskTextView.setVisibility(View.GONE);
                            break;
                        default:
                            gstNumberAsteriskTextView.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        callingActivity = getIntent().getExtras().getString(MyFunctions.CLASS_NAME);

        if(callingActivity.equals(MyFunctions.CLASS_SUPPLIER_VIEW)) {
            title.setText("Edit Supplier");
            setField(getIntent().getExtras().getString("supplier"));
        }
    }

    private void setField(String customerJSON) {
        Gson gson= new Gson();
        Supplier supplier= gson.fromJson(customerJSON, Supplier.class);

        existingSupplierId = supplier.getSupplierId();
        if (supplier.getEntityType() != null) {
            // int spinnerPosition = entity_adapter.getPosition(customer.getEntityType());
            entity_spinner.setSelection( entity_adapter.getPosition(supplier.getEntityType()));
        }
        entityname.setText(supplier.getEntityName());
        contactperson.setText(supplier.getContactPerson());
        con_number.setText(supplier.getContactNumber());
        con_landlineno.setText(supplier.getContactLandline());
        emailid.setText(supplier.getContactEmailId());

        if (supplier.getCategoryId() != 0) {
//            String categoryName= supplier.getCategoryName();
//             int spinnerPosition =gst_adapter.getPosition(categoryName);
            for (int i = 0; i < gst_category_list.size(); i++) {
                GSTCategory gstCategory = gst_category_list.get(i);
                if (gstCategory.getID() == supplier.getCategoryId()) {
                    gst_spinner.setSelection(i);
                }
            }
//            gst_spinner.setSelection((supplier.getCategoryId()==6?5:supplier.getCategoryId()));//  gst_adapter.getPosition(supplier.getCategoryName()));
        }

        if (supplier.getCategoryId() != 0) {
            gst_no.setText(supplier.getGSTNumber());
        }
        et_address.setText(supplier.getAddress());
        state_spinner.setSelection(supplier.getStateId());
        et_city.setText(supplier.getCity());
        et_pincode.setText(supplier.getPinCode());
        if(supplier.getSynced() != 0) {
            edittingSyncedSupplier = true;
        }
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
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
        }else{
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
                    case GSTCategory.IMPORTER: // it is actually an exporter, but importer by index
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

        newSupplier.setEntityType(entityType);
        newSupplier.setEntityName(entityName);
        newSupplier.setContactPerson(contactPerson);
        newSupplier.setContactNumber(contactNumber);
        newSupplier.setContactLandline(contactLandline);
        newSupplier.setContactEmailId(emailID);
        newSupplier.setCategoryId(gstCategoryID);
        newSupplier.setCategoryName(gstCategory);
        newSupplier.setGSTNumber(gstNumber);
        newSupplier.setAddress(addresss);
        newSupplier.setStateId(stateID);
        newSupplier.setStateName(state);
        newSupplier.setCity(city);
        newSupplier.setPinCode(pincode);



        if(callingActivity.equals(MyFunctions.CLASS_SUPPLIER_VIEW)) {
            newSupplier.setSupplierId(existingSupplierId);
            if(DatabaseHandler.getInstance().isSupplierContactNumberAlreadyExists(newSupplier)) {
                con_number.setError("Contact number already exists");
                con_number.requestFocus();
                return;
            }
            if(edittingSyncedSupplier) {
                newSupplier.setSynced(DatabaseHandler.SUPPLIER_SYNCED_CODE_Edit);
            }

            progressDialog.show();

            DatabaseHandler.getInstance().updateCurrentSupplier(newSupplier);
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(NewSupplierActivity.this);
            final View v = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
            builder.setView(v);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button okButton = (Button) v.findViewById(R.id.okClick);
            TextView okMessageTextView = (TextView) v.findViewById(R.id.okMessage);
            okMessageTextView.setText("Supplier Updated!!");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
               }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Gson gson = new Gson();
                    String json = gson.toJson(newSupplier);

                    Intent intent = new Intent();
                    intent.putExtra("supplier", json);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        } else {
            if(DatabaseHandler.getInstance().isSupplierContactNumberAlreadyExists(newSupplier)) {
                con_number.setError("Contact number already exists");
                con_number.requestFocus();
                return;
            }

            progressDialog.show();

            SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
            String TabCode = setting.getString("TabCode", "");
            String LatestSupplierID = setting.getString(DashBoardScreen.SharedPref_LatestSupplierID, "");
            String newSupplierID = createNewSupplierID(LatestSupplierID);
            newSupplierID = TabCode + newSupplierID;

            newSupplier.setSupplierId(newSupplierID);
            DatabaseHandler.getInstance().addNewSupplier(newSupplier);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString(DashBoardScreen.SharedPref_LatestSupplierID, newSupplierID);
            editor.commit();
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(NewSupplierActivity.this);
            final View v = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
            builder.setView(v);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button okButton = (Button) v.findViewById(R.id.okClick);
            TextView okMessageTextView = (TextView) v.findViewById(R.id.okMessage);
            okMessageTextView.setText("New Supplier Created!!");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (callingActivity.equals(MyFunctions.CLASS_PURCHASE_INVOICE)) {
                        Gson gson = new Gson();
                        String json = gson.toJson(newSupplier);

                        Intent intent = new Intent();
                        intent.putExtra("supplier", json);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        NewSupplierActivity.this.onBackPressed();
                    }
                }
            });
        }
    }

    private String createNewSupplierID(String latestTabCode) {
        String previousSupplierSeriesString = latestTabCode.substring(latestTabCode.length() - 5);
        int newSupplierSeries = Integer.parseInt(previousSupplierSeriesString) + 1;
        String newSupplierSeriesString = "00000" + newSupplierSeries;
        newSupplierSeriesString = newSupplierSeriesString.substring(newSupplierSeriesString.length() - 5);
        return newSupplierSeriesString;
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

    private boolean isEmailvalid(String s) {

        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        //String email1 = "user@domain.com";
        Boolean b = s.matches(EMAIL_REGEX);

        return b;
    }

    public void back(View view){
        onBackPressed();
    }

}
