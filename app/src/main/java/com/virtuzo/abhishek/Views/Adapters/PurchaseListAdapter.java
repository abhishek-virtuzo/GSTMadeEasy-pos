package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.MasterPurchase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman Bansal on 13-01-2018.
 */

public class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.MyViewHolder> {


    private List<MasterPurchase> masterPurchaseList;
    private PurchaseListAdapter.OnClickListener listener;

    public PurchaseListAdapter(ArrayList<MasterPurchase> stateList, PurchaseListAdapter.OnClickListener onClickListener) {
        this.masterPurchaseList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new PurchaseListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MasterPurchase masterPurchase = masterPurchaseList.get(position);

        String rupeeSymbol = "\u20B9";
        String purchaseNumber = "Invoice Number - " + masterPurchase.getInvoiceNumber();
        String purchaseDate = "Purchase Date - " + masterPurchase.getPurchaseDate();
        String amountText = "Total Amount - " + rupeeSymbol + String.format("%.2f", masterPurchase.getGrandTotal())
                + "  TAX (IGST) - " + rupeeSymbol + String.format("%.2f", masterPurchase.getIGST());


        String Tax_GST = "CGST : " + rupeeSymbol + String.format("%.2f", masterPurchase.getCGST())
                + " SGST : " + rupeeSymbol + String.format("%.2f", masterPurchase.getSGST());

        holder.bind(masterPurchase);
        holder.customerNameTextView.setText(masterPurchase.getSupplierName());
        holder.purchaseNumberTextView.setText(purchaseNumber);
        holder.purchaseDateTextView.setText(purchaseDate);
        holder.taxGSTTextView.setText(Tax_GST);
        holder.afterAmountTextView.setText(amountText);
        holder.afterAmountTextView.setVisibility(View.VISIBLE);
        holder.amountFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return masterPurchaseList.size();
    }


    public void setFilter(ArrayList<MasterPurchase> newList) {
        this.masterPurchaseList = new ArrayList<>();
        this.masterPurchaseList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onItemClick(MasterPurchase masterSales);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView customerNameTextView;
        public TextView purchaseNumberTextView;
        public TextView purchaseDateTextView;
        public TextView taxGSTTextView;
        public TextView amountTextView;
        public TextView afterAmountTextView;
        public FrameLayout amountFrameLayout;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            customerNameTextView = (TextView) itemView.findViewById(R.id.productDescriptionTextView);
            purchaseNumberTextView = (TextView) itemView.findViewById(R.id.rechargeAmountTextView);
            purchaseDateTextView = (TextView) itemView.findViewById(R.id.regulatoryTextView);
            taxGSTTextView = (TextView) itemView.findViewById(R.id.discountTextView);
            amountTextView = (TextView) itemView.findViewById(R.id.totalAmountTextView);
            afterAmountTextView = (TextView) itemView.findViewById(R.id.afterTotalAmountTextView);
            amountFrameLayout = (FrameLayout) itemView.findViewById(R.id.amountFrameLayout);

            myView = itemView;
        }

        public void bind(final MasterPurchase masterPurchase) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(masterPurchase);
                }
            });
        }

    }


}


