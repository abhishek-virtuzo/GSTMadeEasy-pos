package com.virtuzo.abhishek.Views.Activity.ViewActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterPurchase;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.TransactionPurchase;

import java.util.ArrayList;

public class PurchaseViewActivity extends AppCompatActivity {

    TextView tv_purchase,tv_purchaseDetails;
    ArrayList<TransactionPurchase> listOfTransactionPurchase;
    String purchaseID, invoiceNumber;
    MasterPurchase masterPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_view);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        listOfTransactionPurchase = new ArrayList<>();

        tv_purchase=(TextView)findViewById(R.id.tv_purchase);
        tv_purchaseDetails=(TextView)findViewById(R.id.tv_purchaseDetails);
        //purchaseID=getIntent().getStringExtra("purchaseId");
        purchaseID= String.valueOf(getIntent().getDoubleExtra("purchaseID",0));

        listOfTransactionPurchase.clear();
        masterPurchase= DatabaseHandler.getInstance().getMasterPurchaseRecord(purchaseID);
       // purchaseID= String.valueOf(masterPurchase.getPurchaseID());

        listOfTransactionPurchase=DatabaseHandler.getInstance().getPurchaseListDetailsFromDB(purchaseID);
        printpurchase();
        printPurchaseDetails();

    }

    private void printPurchaseDetails() {
        StringBuilder builder = new StringBuilder();
        String nextLine = "\n";
        builder.append(nextLine);

        for(TransactionPurchase transactionPurchase :listOfTransactionPurchase) {

            String product= transactionPurchase.getProductName();
            String qty= String.valueOf(transactionPurchase.getQuantity());
            String price= /*String.valueOf(transactionPurchase.getUnitPrice());  */String.format("%.2f",transactionPurchase.getUnitPrice());
            String amount= /*String.valueOf(transactionPurchase.getAmount()); */ String.format("%.2f",transactionPurchase.getAmount());

            builder.append(doSpan("Product: ",product));
            builder.append(nextLine);
            builder.append(doSpan("Quantity: ",qty));
            builder.append(nextLine);
            builder.append(doSpan("Price: ",price));
            builder.append(nextLine);
            builder.append(doSpan( "Amount: ",amount));
            builder.append(nextLine);
            builder.append("-------------------------");
            builder.append(nextLine);

        }
        tv_purchaseDetails.setText(builder.toString());

    }

    private void printpurchase(){
        StringBuilder builder = new StringBuilder();
        String nextLine = "\n";
        builder.append(nextLine);
        builder.append(doSpan("Date of Purchase:  ",masterPurchase.getPurchaseDate()));
        builder.append(nextLine);
        builder.append(doSpan("Invoice No.:  ",masterPurchase.getInvoiceNumber()));
        builder.append(nextLine);
        builder.append(doSpan("Supplier Name:  ", String.valueOf(masterPurchase.getSupplierName())));
        builder.append(nextLine);
        builder.append("-------------------------");
        builder.append(nextLine);
        builder.append(doSpan("SUB TOTAL:  ", String.format("%.2f",masterPurchase.getSubTotal())));
        builder.append(nextLine);
        builder.append(doSpan("Transport Charge:  ", String.format("%.2f",masterPurchase.getTransportCharge())));
        builder.append(nextLine);
        builder.append(doSpan("Insurance Charge:  ", String.format("%.2f",masterPurchase.getInsuranceCharge())));
        builder.append(nextLine);
        builder.append(doSpan("Packing Charge:  ", String.format("%.2f",masterPurchase.getPackingCharge())));
        builder.append(nextLine);
        builder.append(doSpan("CGST:  ", String.format("%.2f",masterPurchase.getCGST())));
        builder.append(nextLine);
        builder.append(doSpan("SGST:  ", String.format("%.2f",masterPurchase.getSGST())));
        builder.append(nextLine);
        builder.append(doSpan("IGST:  ", String.format("%.2f",masterPurchase.getIGST())));
        builder.append(nextLine);
//        builder.append(doSpan("CESS:  ", String.format("%.2f",masterPurchase.getCESS())));
//        builder.append(nextLine);
        builder.append("-------------------------");
        builder.append(nextLine);
        builder.append(doSpan("GRAND TOTAL:  ", String.format("%.2f",masterPurchase.getGrandTotal())));
        builder.append(nextLine);

        tv_purchase.setText(builder.toString());

    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    private String doSpan(String LeftText, String RightText){
////        final String resultText = LeftText , " " + RightText;
////        final SpannableString styledResultText = new SpannableString(resultText);
////        styledResultText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), LeftText.length() , LeftText.length() +RightText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        final String resultText = LeftText + "  " + RightText;
//        final SpannableString styledResultText = new SpannableString(resultText);
//        styledResultText.setSpan((new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)), LeftText.length() + 2, LeftText.length() + 2 +RightText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return  (MyFunctions.makeStringLeftAlign(LeftText,20)+MyFunctions.makeStringRightAlign(RightText, 28));

    }
}
