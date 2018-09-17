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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.ApiCalls.GetCustomers;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewCreditNoteActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewDebitNoteActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewPurchaseReturnActivity;
import com.virtuzo.abhishek.Views.Activity.ViewActivity.PurchaseViewActivity;
import com.virtuzo.abhishek.Views.Adapters.PurchaseListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterPurchase;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.util.ArrayList;

public class PurchaseListForPurchaseReturnActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static ProgressDialog progressDialog;
    ArrayList<MasterPurchase> listOfPurchases;
    RecyclerView recyclerView;
    GetCustomers getCustomers;
    private PurchaseListAdapter purchaseListAdapter;
    final int NUMBER_OF_COLUMNS = 3;
    DatabaseHandler db;
    boolean refreshButtonClicked = false;
    String BusinessId;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list_for_purchase_return);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        Toolbar toolbar_in_purchase_list = (Toolbar) findViewById(R.id.toolbar_in_purchase_list);
        toolbar_in_purchase_list.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_purchase_list.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_purchase_list.findViewById(R.id.actionBarTitle);
        heading.setText("Create New Purchase Return");
        Button createNewDebitNote = (Button) toolbar_in_purchase_list.findViewById(R.id.createNewDebitNote);
        createNewDebitNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseListForPurchaseReturnActivity.this, NewDebitNoteActivity.class);
                startActivity(intent);
            }
        });

        // search bar for product list
        searchView = (SearchView) findViewById(R.id.searchPurchaseListSearchView);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Purchase by invoice number, purchase date");
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        listOfPurchases = new ArrayList<>();
        BusinessId = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("BusinessId", "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.purchaseListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking an purchase
        purchaseListAdapter = new PurchaseListAdapter(listOfPurchases, new PurchaseListAdapter.OnClickListener() {
            @Override
            public void onItemClick(MasterPurchase masterPurchase) {
                startActivity(new Intent(PurchaseListForPurchaseReturnActivity.this, NewPurchaseReturnActivity.class)
                        .putExtra("purchaseID",masterPurchase.getPurchaseID()));
                return;
            }
        });

        recyclerView.setAdapter(purchaseListAdapter);

        listOfPurchases.clear();
        listOfPurchases.addAll(DatabaseHandler.getInstance().getPurchaseListFromDB());
        purchaseListAdapter.notifyDataSetChanged();
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
        ArrayList<MasterPurchase> newList = new ArrayList<>();
        for(MasterPurchase masterPurchase : listOfPurchases) {
            String invoiceNumber = MyFunctions.lowerCase(masterPurchase.getInvoiceNumber());
           // String entityName = MyFunctions.lowerCase(String.valueOf(masterPurchase.getGrandTotal()));
            String purchaseDate = MyFunctions.lowerCase(masterPurchase.getPurchaseDate());
           // String email = MyFunctions.lowerCase(String.valueOf(masterPurchase.getPurchaseID()));
            if(invoiceNumber.contains(newText) ||  purchaseDate.contains(newText)  ){
                newList.add(masterPurchase);
            }
        }
        purchaseListAdapter.setFilter(newList);
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
