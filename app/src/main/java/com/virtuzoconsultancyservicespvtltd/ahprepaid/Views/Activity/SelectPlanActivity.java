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
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.GetPlans;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Adapters.SelectPlanAdapter;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.PlanClass;

import java.util.ArrayList;

public class SelectPlanActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;
    ArrayList<PlanClass> listOfPlans;
    RecyclerView recyclerView;
    GetPlans getPlans;
    private SelectPlanAdapter selectPlanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_plan);

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

        selectPlanAdapter = new SelectPlanAdapter(listOfPlans, new SelectPlanAdapter.OnClickListener() {
            @Override
            public void onItemClick(PlanClass plan) {

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

                listOfPlans.addAll(getPlans.plansList);
                selectPlanAdapter.notifyDataSetChanged();

            }
        });

        getPlans = new GetPlans(distributorID, vendorID);


    }
}
