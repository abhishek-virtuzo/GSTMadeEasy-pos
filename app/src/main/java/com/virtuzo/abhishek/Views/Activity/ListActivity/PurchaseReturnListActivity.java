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
import com.virtuzo.abhishek.Views.Activity.ViewActivity.PurchaseReturnViewActivity;
import com.virtuzo.abhishek.Views.Adapters.PurchaseReturnListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterPurchaseReturn;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.util.ArrayList;

public class PurchaseReturnListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ArrayList<MasterPurchaseReturn> listOfPurchaseReturns;
    RecyclerView recyclerView;
    private PurchaseReturnListAdapter purchaseReturnListAdapter;
    final int NUMBER_OF_COLUMNS = 3;
    DatabaseHandler db;
    boolean refreshButtonClicked = false;
    String BusinessId;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasereturn_list);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setTitle("Products");
        getSupportActionBar().hide();
        Toolbar toolbar_in_invoice_list = (Toolbar) findViewById(R.id.toolbar_in_invoice_list);
        toolbar_in_invoice_list.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_invoice_list.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_invoice_list.findViewById(R.id.actionBarTitle);
        heading.setText("Purchase Return List");

        // search bar for product list
        searchView = (SearchView) findViewById(R.id.searchInvoiceListSearchView);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Purchase Return by purchase return number, supplier name, mobile number and email Id");
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        listOfPurchaseReturns = new ArrayList<>();
        BusinessId = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("BusinessId", "");

        recyclerView = (RecyclerView) findViewById(R.id.invoiceListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking an invoice
        purchaseReturnListAdapter = new PurchaseReturnListAdapter(listOfPurchaseReturns, new PurchaseReturnListAdapter.OnClickListener() {
            @Override
            public void onItemClick(MasterPurchaseReturn masterPurchaseReturn) {
                Intent intentShowInvoiceDetails = new Intent(getApplicationContext(), PurchaseReturnViewActivity.class);
                intentShowInvoiceDetails.putExtra("CN_ID", masterPurchaseReturn.getPurchaseReturnID()+"");
                startActivity(intentShowInvoiceDetails);
            }
        });

        recyclerView.setAdapter(purchaseReturnListAdapter);

        listOfPurchaseReturns.clear();
        listOfPurchaseReturns.addAll(DatabaseHandler.getInstance().getMasterPurchaseReturnList());
        purchaseReturnListAdapter.notifyDataSetChanged();

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
        ArrayList<MasterPurchaseReturn> newList = new ArrayList<>();
        for(MasterPurchaseReturn masterPurchaseReturn : listOfPurchaseReturns) {
            String purchaseReturnNumber = MyFunctions.lowerCase(masterPurchaseReturn.getPurchaseReturnNumber());
            String entityName = MyFunctions.lowerCase(masterPurchaseReturn.getSupplierName());
            String mobileNumber = MyFunctions.lowerCase(masterPurchaseReturn.getSupplierMobile());
            String email = MyFunctions.lowerCase(masterPurchaseReturn.getSupplierEmail());
            if(purchaseReturnNumber.contains(newText) || entityName.contains(newText) || mobileNumber.contains(newText) || email.contains(newText)){
                newList.add(masterPurchaseReturn);
            }
        }
        purchaseReturnListAdapter.setFilter(newList);
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
