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
import com.virtuzo.abhishek.Views.Activity.ViewActivity.CreditNoteViewActivity;
import com.virtuzo.abhishek.Views.Adapters.CreditNoteListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.CreditDebitNote;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.util.ArrayList;

public class CreditNoteListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ArrayList<CreditDebitNote> creditNoteArrayList;
    RecyclerView recyclerView;
    private CreditNoteListAdapter creditNoteListAdapter;
    final int NUMBER_OF_COLUMNS = 3;
    DatabaseHandler db;
    boolean refreshButtonClicked = false;
    String BusinessId;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditnote_list);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setTitle("Products");
        getSupportActionBar().hide();
        Toolbar toolbar_in_invoice_list = (Toolbar) findViewById(R.id.toolbar_in_invoice_list);
        toolbar_in_invoice_list.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_invoice_list.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_invoice_list.findViewById(R.id.actionBarTitle);
        String noteType = getIntent().getExtras().getString("NoteType", "");
        if (noteType.equalsIgnoreCase("Credit Note")) {
            heading.setText("Credit Notes List");
        } else {
            heading.setText("Debit Notes List");
        }

        // search bar for product list
        searchView = (SearchView) findViewById(R.id.searchInvoiceListSearchView);
        searchView.setOnQueryTextListener(this);
        if (noteType.equalsIgnoreCase("Credit Note")) {
            searchView.setQueryHint("Search Credit Note by sales return no., credit note no., customer name, mobile no. and email Id");
        } else {
            searchView.setQueryHint("Search Debit Note by purchase return no., debit note no., supplier name, mobile no. and email Id");
        }
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        creditNoteArrayList = new ArrayList<>();
        BusinessId = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("BusinessId", "");

        recyclerView = (RecyclerView) findViewById(R.id.invoiceListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking an invoice
        creditNoteListAdapter = new CreditNoteListAdapter(creditNoteArrayList, new CreditNoteListAdapter.OnClickListener() {
            @Override
            public void onItemClick(CreditDebitNote creditNote) {
                Intent intentShowInvoiceDetails = new Intent(getApplicationContext(), CreditNoteViewActivity.class);
                intentShowInvoiceDetails.putExtra("CN_ID", creditNote.getCreditNoteID()+"");
                startActivity(intentShowInvoiceDetails);
            }
        });

        recyclerView.setAdapter(creditNoteListAdapter);

        creditNoteArrayList.clear();
        creditNoteArrayList.addAll(DatabaseHandler.getInstance().getCreditNotes(noteType));
        creditNoteListAdapter.notifyDataSetChanged();

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
        ArrayList<CreditDebitNote> newList = new ArrayList<>();
        for(CreditDebitNote creditNote : creditNoteArrayList) {
            String salesReturnNumber = MyFunctions.lowerCase(creditNote.getSalesReturnNumber());
            String creditNoteNumber = MyFunctions.lowerCase(creditNote.getNoteNumber());
            String entityName = MyFunctions.lowerCase(creditNote.getCustomerName());
            String mobileNumber = MyFunctions.lowerCase(creditNote.getCustomerMobile());
            String email = MyFunctions.lowerCase(creditNote.getCustomerEmail());
            if(salesReturnNumber.contains(newText)
                    || creditNoteNumber.contains(newText)
                    || entityName.contains(newText)
                    || mobileNumber.contains(newText)
                    || email.contains(newText)){
                newList.add(creditNote);
            }
        }
        creditNoteListAdapter.setFilter(newList);
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
