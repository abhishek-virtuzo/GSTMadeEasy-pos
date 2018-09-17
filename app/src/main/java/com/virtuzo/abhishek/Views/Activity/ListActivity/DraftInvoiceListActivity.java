package com.virtuzo.abhishek.Views.Activity.ListActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewInvoiceActivity;
import com.virtuzo.abhishek.Views.Adapters.DraftInvoiceListAdapter;
import com.virtuzo.abhishek.modal.DraftInvoice;
import com.virtuzo.abhishek.modal.MyFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class DraftInvoiceListActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ArrayList<DraftInvoice> draftInvoices;
    RecyclerView recyclerView;
    DraftInvoiceListAdapter draftInvoiceListAdapter;
    TextView noDraftTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_invoice_list);

        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar.findViewById(R.id.actionBarTitle);
        heading.setText("Draft Invoices");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        noDraftTextView = (TextView) findViewById(R.id.noDraftTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        MyFunctions.refreshDrafts(this);

        draftInvoices = new ArrayList<>();
        draftInvoiceListAdapter = new DraftInvoiceListAdapter(draftInvoices, new DraftInvoiceListAdapter.OnClickListener() {
            @Override
            public void onItemClick(DraftInvoice draftInvoice) {
                //Toast.makeText(DraftInvoiceListActivity.this, draftInvoice.getMasterSales().getCustomerName(), Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                String jsonString = gson.toJson(draftInvoice);

                Bundle bundle = new Bundle();
                bundle.putBoolean("IsDraft", true);
                bundle.putString("Draft", jsonString);

                Intent intent = new Intent(DraftInvoiceListActivity.this, NewInvoiceActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            public void onOptionClick(final DraftInvoice draftInvoice, View imageView) {
                removeItem(draftInvoice);
//                PopupMenu popupMenu = new PopupMenu(DraftInvoiceListActivity.this, imageView);
//                popupMenu.inflate(R.menu.menu_for_draft_list_item);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.removeItem:
//                                removeItem(draftInvoice);
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
            }
        });
        recyclerView.setAdapter(draftInvoiceListAdapter);

        loadData();
    }

    private void removeItem(final DraftInvoice draftInvoice) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DraftInvoiceListActivity.this);
        builder.setMessage("Remove this Draft ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyFunctions.removeDraft(draftInvoice, DraftInvoiceListActivity.this);
                        loadData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyFunctions.PREFS_DraftInvoices, MODE_PRIVATE);
        String drafts = sharedPreferences.getString(MyFunctions.DraftInvoices, null);
        Gson gson = new Gson();
        draftInvoices.clear();
        if (drafts != null) {
            try {
                // got previous drafts
                JSONObject jsonObject = new JSONObject(drafts);
                JSONArray jsonArray = jsonObject.getJSONArray("Drafts");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    DraftInvoice draftInvoice = gson.fromJson(jsonObject1.toString(), DraftInvoice.class);
                    draftInvoices.add(draftInvoice);
                }
                Collections.reverse(draftInvoices);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        progressBar.setVisibility(View.GONE);
        draftInvoiceListAdapter.notifyDataSetChanged();
        if (draftInvoices.size() == 0) {
            noDraftTextView.setVisibility(View.VISIBLE);
        }

    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

}
