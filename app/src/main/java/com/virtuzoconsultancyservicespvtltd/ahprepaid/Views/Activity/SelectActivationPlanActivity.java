package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.GetActivationPlans;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Adapters.SelectActionPlanAdapter;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.ActivationPlanClass;

import java.util.ArrayList;

public class SelectActivationPlanActivity extends AppCompatActivity {
    public static ProgressDialog progressDialog;
    ArrayList<ActivationPlanClass> listOfPlans;
    RecyclerView recyclerView;
    GetActivationPlans getActivationPlans;
    private SelectActionPlanAdapter selectPlanAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Select Plan");

        setContentView(R.layout.activity_select_activation_plan);
        listOfPlans = new ArrayList<>();

        int vendorID = getIntent().getExtras().getInt("vendorId");
        String distributorID = getSharedPreferences("LoginPrefs", 0).getString("DistributorID", "");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        selectPlanAdapter = new SelectActionPlanAdapter(listOfPlans, new SelectActionPlanAdapter.OnClickListener() {
            @Override
            public void onItemClick(ActivationPlanClass plan) {

                Gson gson = new Gson();
                String json = gson.toJson(plan);

                Intent intent = new Intent();
                intent.putExtra("plan", json);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });


        recyclerView.setAdapter(selectPlanAdapter);

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                listOfPlans.addAll(getActivationPlans.plansList);
                selectPlanAdapter.notifyDataSetChanged();

            }
        });

        getActivationPlans = new GetActivationPlans(distributorID, vendorID);

    }
}
