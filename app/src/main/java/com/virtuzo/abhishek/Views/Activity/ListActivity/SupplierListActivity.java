package com.virtuzo.abhishek.Views.Activity.ListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewSupplierActivity;
import com.virtuzo.abhishek.Views.Activity.ViewActivity.SupplierViewActivity;
import com.virtuzo.abhishek.Views.Adapters.SupplierListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Supplier;

import java.util.ArrayList;

public class SupplierListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ArrayList<Supplier> listOfSuppliers;
    RecyclerView recyclerView;
    private SupplierListAdapter supplierListAdapter;
    private final int CHOOSE_SUPPLIER_RESPONSE_CODE = 101;
    final int NUMBER_OF_COLUMNS = 3;
    SearchView searchView;
    String callingActivity;
    String BusinessId, TabCode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_list);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        Toolbar toolbar_in_supplier_list = (Toolbar) findViewById(R.id.toolbar_in_supplier_list);
        toolbar_in_supplier_list.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_supplier_list.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_supplier_list.findViewById(R.id.actionBarTitle);
        heading.setText("Supplier List");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        callingActivity = getIntent().getExtras().getString(MyFunctions.CLASS_NAME);

        // search bar for product list
        searchView = (SearchView) findViewById(R.id.searchSupplierListSearchView);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Supplier by contact person, entity name, contact number and email Id");
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        listOfSuppliers = new ArrayList<>();
        listOfSuppliers.addAll(DatabaseHandler.getInstance().getSuppliersListFromDB());
        BusinessId = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("BusinessId", "");
        TabCode = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("TabCode", "");

        recyclerView = (RecyclerView) findViewById(R.id.supplierListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking a product
        supplierListAdapter = new SupplierListAdapter(listOfSuppliers, new SupplierListAdapter.OnClickListener() {
            @Override
            public void onItemClick(Supplier supplier) {
                Gson gson = new Gson();
                String json = gson.toJson(supplier);
                if(callingActivity.equals(MyFunctions.CLASS_DASHBOARD)) {
                    startActivity(new Intent(SupplierListActivity.this, SupplierViewActivity.class).putExtra("supplier", json));
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("supplier", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(supplierListAdapter);

    }

    public void refreshSupplierList() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        listOfSuppliers.clear();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                listOfSuppliers.addAll(DatabaseHandler.getInstance().getSuppliersListFromDB());
                supplierListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = MyFunctions.lowerCase(newText.trim());
        ArrayList<Supplier> newList = new ArrayList<>();
        for(Supplier supplier : listOfSuppliers) {
            String entityName = MyFunctions.lowerCase(supplier.getEntityName());
            String contactPerson = MyFunctions.lowerCase(supplier.getContactPerson());
            String contactNumber = MyFunctions.lowerCase(supplier.getContactNumber());
            String contactEmailId = MyFunctions.lowerCase(supplier.getContactEmailId());
            if(contactPerson.contains(newText) || entityName.contains(newText) || contactNumber.contains(newText) || contactEmailId.contains(newText)){
                newList.add(supplier);
            }
        }
        supplierListAdapter.setFilter(newList);
        return true;
    }

    private void setSearchViewHeight(SearchView searchView, int height) {
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(height);
    }

    public void addNewSupplier(View view) {
        Intent intentShowSupplierList = new Intent(getApplicationContext(), NewSupplierActivity.class);
        if(callingActivity.equals(MyFunctions.CLASS_DASHBOARD)){
            intentShowSupplierList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
        } else if(callingActivity.equals(MyFunctions.CLASS_PURCHASE_INVOICE)){
            intentShowSupplierList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_PURCHASE_INVOICE);
        } else if(callingActivity.equals(MyFunctions.CLASS_NEW_CREDITNOTE)) {
            intentShowSupplierList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_NEW_CREDITNOTE);
        }
        startActivityForResult(intentShowSupplierList, CHOOSE_SUPPLIER_RESPONSE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_SUPPLIER_RESPONSE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String json = data.getStringExtra("supplier");
                Intent intent = new Intent();
                intent.putExtra("supplier", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        //TODO update list
        refreshSupplierList();

        super.onResume();
    }

}
