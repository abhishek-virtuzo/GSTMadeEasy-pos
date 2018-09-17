package com.virtuzo.abhishek.Views.Activity.ListActivity;

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
import android.widget.Toast;

import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ViewActivity.SalesReturnViewActivity;
import com.virtuzo.abhishek.Views.Adapters.SalesReturnListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterSalesReturn;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.util.ArrayList;

public class SalesReturnListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ArrayList<MasterSalesReturn> listOfSalesReturns;
    RecyclerView recyclerView;
    private SalesReturnListAdapter salesReturnListAdapter;
    final int NUMBER_OF_COLUMNS = 3;
    DatabaseHandler db;
    boolean refreshButtonClicked = false;
    String BusinessId;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesreturn_list);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setTitle("Products");
        getSupportActionBar().hide();
        Toolbar toolbar_in_invoice_list = (Toolbar) findViewById(R.id.toolbar_in_invoice_list);
        toolbar_in_invoice_list.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_invoice_list.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_invoice_list.findViewById(R.id.actionBarTitle);
        heading.setText("Invoice List");

        // search bar for product list
        searchView = (SearchView) findViewById(R.id.searchInvoiceListSearchView);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Sales Return by sales return number, customer name, mobile number and email Id");
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        listOfSalesReturns = new ArrayList<>();
        BusinessId = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("BusinessId", "");

        recyclerView = (RecyclerView) findViewById(R.id.invoiceListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking an invoice
        salesReturnListAdapter = new SalesReturnListAdapter(listOfSalesReturns, new SalesReturnListAdapter.OnClickListener() {
            @Override
            public void onItemClick(MasterSalesReturn masterSalesReturn) {
                Intent intentShowInvoiceDetails = new Intent(getApplicationContext(), SalesReturnViewActivity.class);
                intentShowInvoiceDetails.putExtra("CN_ID", masterSalesReturn.getSaleReturnID()+"");
                startActivity(intentShowInvoiceDetails);

            }
        });

        recyclerView.setAdapter(salesReturnListAdapter);

        listOfSalesReturns.clear();
        listOfSalesReturns.addAll(DatabaseHandler.getInstance().getMasterSalesReturnList());
        salesReturnListAdapter.notifyDataSetChanged();

    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    public void refreshButtonPressed(View view) {
        // refresh
        Toast.makeText(this, "Currently unavailable", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = MyFunctions.lowerCase(newText.trim());
        ArrayList<MasterSalesReturn> newList = new ArrayList<>();
        for(MasterSalesReturn masterSalesReturn : listOfSalesReturns) {
            String salesReturnNumber = MyFunctions.lowerCase(masterSalesReturn.getSalesReturnNumber());
            String entityName = MyFunctions.lowerCase(masterSalesReturn.getCustomerName());
            String mobileNumber = MyFunctions.lowerCase(masterSalesReturn.getCustomerMobile());
            String email = MyFunctions.lowerCase(masterSalesReturn.getCustomerEmail());
            if(salesReturnNumber.contains(newText) || entityName.contains(newText) || mobileNumber.contains(newText) || email.contains(newText)){
                newList.add(masterSalesReturn);
            }
        }
        salesReturnListAdapter.setFilter(newList);
        return true;
    }

    private void setSearchViewHeight(SearchView searchView, int height) {
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(height);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.refresh_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        int id = item.getItemId();
////        if(id == R.id.refreshMenuBar) {
////            refreshButtonClicked = true;
////            Toast.makeText(this, "Refresh Customer List", Toast.LENGTH_SHORT).show();
////            this.progressDialog.show();
////            getCustomers = new GetCustomers(1);
////            return true;
////        }
//        return super.onOptionsItemSelected(item);
//    }
}
