package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.MasterPurchaseReturn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class PurchaseReturnListAdapter extends RecyclerView.Adapter<PurchaseReturnListAdapter.MyViewHolder> {

    private List<MasterPurchaseReturn> masterPurchaseReturnList;
    private PurchaseReturnListAdapter.OnClickListener listener;

    public PurchaseReturnListAdapter(ArrayList<MasterPurchaseReturn> stateList, PurchaseReturnListAdapter.OnClickListener onClickListener) {
        this.masterPurchaseReturnList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public PurchaseReturnListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PurchaseReturnListAdapter.MyViewHolder holder, final int position) {

        MasterPurchaseReturn masterPurchaseReturn = masterPurchaseReturnList.get(position);

        String rupeeSymbol = "\u20B9";
        String invoiceNumber = "Purchase Return Number - " + masterPurchaseReturn.getPurchaseReturnNumber();
        String invoiceDate = "Invoice Number - " + masterPurchaseReturn.getInvoiceNumber();
        String amountText = "Date - " + masterPurchaseReturn.getPurchaseReturnDate();
        String Tax_GST = "Debit Amount - " + rupeeSymbol + String.format("%.2f", masterPurchaseReturn.getDebitAmount());

        holder.bind(masterPurchaseReturn);
        holder.customerNameTextView.setText(masterPurchaseReturn.getSupplierName());
        holder.invoiceNumberTextView.setText(invoiceNumber);
        holder.invoiceDateTextView.setText(invoiceDate);
        holder.taxGSTTextView.setText(Tax_GST);
        holder.afterAmountTextView.setText(amountText);
        holder.afterAmountTextView.setVisibility(View.VISIBLE);
        holder.amountFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return masterPurchaseReturnList.size();
    }

    public void setFilter(ArrayList<MasterPurchaseReturn> newList) {
        this.masterPurchaseReturnList = new ArrayList<>();
        this.masterPurchaseReturnList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onItemClick(MasterPurchaseReturn masterPurchaseReturn);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView customerNameTextView;
        public TextView invoiceNumberTextView;
        public TextView invoiceDateTextView;
        public TextView taxGSTTextView;
        public TextView amountTextView;
        public TextView afterAmountTextView;
        public FrameLayout amountFrameLayout;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            customerNameTextView = (TextView) itemView.findViewById(R.id.productDescriptionTextView);
            invoiceNumberTextView = (TextView) itemView.findViewById(R.id.rechargeAmountTextView);
            invoiceDateTextView = (TextView) itemView.findViewById(R.id.regulatoryTextView);
            taxGSTTextView = (TextView) itemView.findViewById(R.id.discountTextView);
            amountTextView = (TextView) itemView.findViewById(R.id.totalAmountTextView);
            afterAmountTextView = (TextView) itemView.findViewById(R.id.afterTotalAmountTextView);
            amountFrameLayout = (FrameLayout) itemView.findViewById(R.id.amountFrameLayout);

            myView = itemView;
        }

        public void bind(final MasterPurchaseReturn masterPurchaseReturn) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(masterPurchaseReturn);
                }
            });
        }

    }
}
