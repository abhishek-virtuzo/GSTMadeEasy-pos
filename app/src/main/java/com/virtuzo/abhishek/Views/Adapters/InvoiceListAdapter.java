package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.MasterSales;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.MyViewHolder> {

    private List<MasterSales> masterSalesList;
    private InvoiceListAdapter.OnClickListener listener;

    public InvoiceListAdapter(ArrayList<MasterSales> stateList, InvoiceListAdapter.OnClickListener onClickListener) {
        this.masterSalesList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public InvoiceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InvoiceListAdapter.MyViewHolder holder, final int position) {

        MasterSales masterSales = masterSalesList.get(position);
        double balance = Math.round(masterSales.getTotalAmount()) - masterSales.getPaidAmount();

        String rupeeSymbol = "\u20B9";
        String invoiceNumber = "Invoice Number - " + masterSales.getReferenceNumber();
        String invoiceDate = "Invoice Date - " + masterSales.getSalesDate();
        String amountText = "Total Amount - " + rupeeSymbol + String.format("%.2f", masterSales.getTotalAmount())
                + " TAX (GST) - " + rupeeSymbol + String.format("%.2f", masterSales.getTaxAmount());
        String Tax_GST = "Paid : " + rupeeSymbol + String.format("%.2f", masterSales.getPaidAmount())
                + " Balance : " + rupeeSymbol + String.format("%.2f", balance);

        holder.bind(masterSales);
        holder.customerNameTextView.setText(masterSales.getCustomerName());
        holder.invoiceNumberTextView.setText(invoiceNumber);
        holder.invoiceDateTextView.setText(invoiceDate);
        holder.taxGSTTextView.setText(Tax_GST);
        holder.afterAmountTextView.setText(amountText);
        holder.afterAmountTextView.setVisibility(View.VISIBLE);
        holder.amountFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return masterSalesList.size();
    }

    public void setFilter(ArrayList<MasterSales> newList) {
        this.masterSalesList = new ArrayList<>();
        this.masterSalesList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onItemClick(MasterSales masterSales);
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

        public void bind(final MasterSales masterSales) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(masterSales);
                }
            });
        }

    }
}
