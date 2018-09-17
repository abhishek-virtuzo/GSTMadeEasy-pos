package com.virtuzo.abhishek.Views.Activity.ListActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ViewActivity.DeliveryOrderViewActivity;
import com.virtuzo.abhishek.Views.Adapters.DeliveryOrderListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterDeliveryOrder;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.util.ArrayList;

public class DeliveryOrderListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static ProgressDialog progressDialog;
    ArrayList<MasterDeliveryOrder> listOfDeliveryOrders;
    RecyclerView recyclerView;
    private DeliveryOrderListAdapter deliveryOrderListAdapter;
    final int NUMBER_OF_COLUMNS = 3;
    String BusinessId;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_list);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        Toolbar toolbar_in_do_list = (Toolbar) findViewById(R.id.toolbar_in_do_list);
        toolbar_in_do_list.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_do_list.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_do_list.findViewById(R.id.actionBarTitle);
        heading.setText("Delivery Order List");

        // search bar for product list
        searchView = (SearchView) findViewById(R.id.searchInvoiceListSearchView);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Delivery Orders by do number, entity name, mobile number or email Id");
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        listOfDeliveryOrders = new ArrayList<>();
        BusinessId = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("BusinessId", "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.invoiceListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking an invoice
        deliveryOrderListAdapter = new DeliveryOrderListAdapter(listOfDeliveryOrders, new DeliveryOrderListAdapter.OnClickListener() {

            @Override
            public void onItemClick(MasterDeliveryOrder masterDeliveryOrder) {
                Intent intentDoView = new Intent(getApplicationContext(), DeliveryOrderViewActivity.class);
                intentDoView.putExtra("DO_ID", masterDeliveryOrder.getID()+"");
                startActivity(intentDoView);
            }
        });

        recyclerView.setAdapter(deliveryOrderListAdapter);

        listOfDeliveryOrders.clear();
        listOfDeliveryOrders.addAll(DatabaseHandler.getInstance().getDeliveryOrderListFromDB());
        deliveryOrderListAdapter.notifyDataSetChanged();
        progressDialog.dismiss();

    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = MyFunctions.lowerCase(newText.trim());
        ArrayList<MasterDeliveryOrder> newList = new ArrayList<>();
        for(MasterDeliveryOrder masterDeliveryOrder : listOfDeliveryOrders) {
            String doNumber = MyFunctions.lowerCase(masterDeliveryOrder.getDONumber());
            String entityName = MyFunctions.lowerCase(masterDeliveryOrder.getCustomerName());
            String mobileNumber = MyFunctions.lowerCase(masterDeliveryOrder.getCustomerMobile());
            String email = MyFunctions.lowerCase(masterDeliveryOrder.getCustomerEmail());
            if(doNumber.contains(newText) || entityName.contains(newText) || mobileNumber.contains(newText) || email.contains(newText)){
                newList.add(masterDeliveryOrder);
            }
        }
        deliveryOrderListAdapter.setFilter(newList);
        return true;
    }

    private void setSearchViewHeight(SearchView searchView, int height) {
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(height);
    }

}
