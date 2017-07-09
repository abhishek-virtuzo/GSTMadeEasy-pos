package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.ApiCalls.GetOperators;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.OperatorClass;

import java.util.ArrayList;
import java.util.List;

public class SelectOperatorActivity extends AppCompatActivity {

    public static ProgressDialog progressDialog;

    GetOperators getOperators;

    List<OperatorClass> operatorList;

    TextView Lyca, MobileH20, EasyGo, UltraMobie;
    RechargeActivity rechargeActivity;
    CardView lyca, h20, easy, ultra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_operator);

        operatorList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Lyca = (TextView) findViewById(R.id.textView88);
        MobileH20 = (TextView) findViewById(R.id.textView8);
        EasyGo = (TextView) findViewById(R.id.textView9);
        UltraMobie = (TextView) findViewById(R.id.textView86);

        lyca = (CardView) findViewById(R.id.lycaMobile);
        h20 = (CardView) findViewById(R.id.HtwoO);
        easy = (CardView) findViewById(R.id.easy);
        ultra = (CardView) findViewById(R.id.UltraMobile);

        getOperators = new GetOperators();

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                operatorList = new ArrayList<>();
                operatorList.addAll(getOperators.operatorList);
            }
        });


        lyca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                OperatorClass operator = operatorList.get(0);

                Gson gson = new Gson();
                String json = gson.toJson(operator);

                Intent intent = new Intent();
                intent.putExtra("vendor", json);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

        h20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OperatorClass operator = operatorList.get(1);

                Gson gson = new Gson();
                String json = gson.toJson(operator);

                Intent intent = new Intent();
                intent.putExtra("vendor", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OperatorClass operator = operatorList.get(2);

                Gson gson = new Gson();
                String json = gson.toJson(operator);

                Intent intent = new Intent();
                intent.putExtra("vendor", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        ultra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OperatorClass operator = operatorList.get(3);

                Gson gson = new Gson();
                String json = gson.toJson(operator);

                Intent intent = new Intent();
                intent.putExtra("vendor", json);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });


    }
}
