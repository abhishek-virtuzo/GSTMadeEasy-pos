package com.virtuzo.abhishek.Views.Activity.ListActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ViewActivity.CustomerViewActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewCustomerActivity;
import com.virtuzo.abhishek.Views.Adapters.CustomerListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.Customer;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SharedPreferences setting;
    ArrayList<Customer> listOfCustomers;
    RecyclerView recyclerView;
    private CustomerListAdapter customerListAdapter;
    private final int CHOOSE_CUSTOMER_RESPONSE_CODE = 101;
    final int NUMBER_OF_COLUMNS = 3;
    boolean refreshButtonClicked = false;
    String BusinessId, TabCode;
    SearchView searchView;
    String callingActivity;
    Button guestCustomer;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        setTitle("Products");
        getSupportActionBar().hide();
        Toolbar toolbar_in_customer_list = (Toolbar) findViewById(R.id.toolbar_in_customer_list);
        toolbar_in_customer_list.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        toolbar_in_customer_list.setContentInsetsAbsolute(0, 0);
        TextView heading = (TextView) toolbar_in_customer_list.findViewById(R.id.actionBarTitle);
        heading.setText("Customer List");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        guestCustomer = (Button) findViewById(R.id.guestCustomer);
        callingActivity = getIntent().getExtras().getString(MyFunctions.CLASS_NAME);
        if(callingActivity.equals(MyFunctions.CLASS_DASHBOARD)
                || callingActivity.equals(MyFunctions.CLASS_DELIVERY_ORDER)
                || callingActivity.equals(MyFunctions.CLASS_NEW_CREDITNOTE)) {
            guestCustomer.setVisibility(View.GONE);
        }

        // search bar for product list
        searchView = (SearchView) findViewById(R.id.searchCustomerListSearchView);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Customer by name, email ID and mobile number");
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        listOfCustomers = new ArrayList<>();
        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        BusinessId = setting.getString("BusinessId", "");
        TabCode = setting.getString("TabCode", "");

        recyclerView = (RecyclerView) findViewById(R.id.customerListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking a product
        customerListAdapter = new CustomerListAdapter(listOfCustomers, new CustomerListAdapter.OnClickListener() {
            @Override
            public void onItemClick(Customer customer) {

                Gson gson = new Gson();
                String json = gson.toJson(customer);

                if(callingActivity.equals(MyFunctions.CLASS_DASHBOARD)) {
                    startActivity(new Intent(CustomerListActivity.this, CustomerViewActivity.class).putExtra("customer", json));
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("customer", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        recyclerView.setAdapter(customerListAdapter);

//        refreshSupplierList();
    }

    public void refreshCustomerList() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        listOfCustomers.clear();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                listOfCustomers.addAll(DatabaseHandler.getInstance().getCustomersListFromDB());
                customerListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    public void addNewCustomer(View view){
        Intent intentShowCustomerList = new Intent(getApplicationContext(), NewCustomerActivity.class);
        if(callingActivity.equals(MyFunctions.CLASS_DASHBOARD)){
            intentShowCustomerList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
        } else if(callingActivity.equals(MyFunctions.CLASS_NEW_INVOICE)){
            intentShowCustomerList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_NEW_INVOICE);
        } else if(callingActivity.equals(MyFunctions.CLASS_DELIVERY_ORDER)){
            intentShowCustomerList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DELIVERY_ORDER);
        } else if(callingActivity.equals(MyFunctions.CLASS_NEW_CREDITNOTE)) {
            intentShowCustomerList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_NEW_CREDITNOTE);
        }
        startActivityForResult(intentShowCustomerList, CHOOSE_CUSTOMER_RESPONSE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_CUSTOMER_RESPONSE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String json = data.getStringExtra("customer");
                Intent intent = new Intent();
                intent.putExtra("customer", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    private void setSearchViewHeight(SearchView searchView, int height) {
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(height);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = MyFunctions.lowerCase(newText.trim());
        ArrayList<Customer> newList = new ArrayList<>();
        for (Customer customer : listOfCustomers) {
            String entityName = MyFunctions.lowerCase(customer.getEntityName());
            String contactPerson = MyFunctions.lowerCase(customer.getContactPerson());
            String contactNumber = MyFunctions.lowerCase(customer.getContactNumber());
            String contactEmailId = MyFunctions.lowerCase(customer.getContactEmailId());
            if (contactPerson.contains(newText) || entityName.contains(newText) || contactNumber.contains(newText) || contactEmailId.contains(newText)) {
                newList.add(customer);
            }
        }
        customerListAdapter.setFilter(newList);
        return true;
    }

    public void chooseGuestCustomer(View view) {
        Customer customer = Customer.getGuestCustomer();
        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        String StateID = setting.getString("StateID", "");
        String StateName = setting.getString("StateName", "");
        customer.setStateId(Integer.parseInt(StateID));
        customer.setStateName(StateName);
        
        Gson gson = new Gson();
        String json = gson.toJson(customer);

        Intent intent = new Intent();
        intent.putExtra("customer", json);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        //TODO update list
//        listOfCustomers.clear();
//        listOfCustomers.addAll(DatabaseHandler.getInstance().getCustomersListFromDB());
//        customerListAdapter.notifyDataSetChanged();
        refreshCustomerList();

        super.onResume();
    }


    @Override
    public void onPause(){
        super.onPause();
    }
}