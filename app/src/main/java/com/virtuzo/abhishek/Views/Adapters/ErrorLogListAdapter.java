package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.ErrorLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ErrorLogListAdapter extends RecyclerView.Adapter<ErrorLogListAdapter.MyViewHolder> {

    private List<ErrorLog> errorLogs;
    private ErrorLogListAdapter.OnClickListener listener;

    public ErrorLogListAdapter(ArrayList<ErrorLog> stateList, ErrorLogListAdapter.OnClickListener onClickListener) {
        this.errorLogs = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ErrorLogListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.error_log_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ErrorLogListAdapter.MyViewHolder holder, final int position) {

        ErrorLog errorLog = errorLogs.get(position);
//        double balance = Math.round(masterSales.getTotalAmount()) - masterSales.getPaidAmount();

        String invoiceNumber = errorLog.getLogDate() + " " + errorLog.getLogTime();
        String invoiceDate = errorLog.getTabID();
        String Tax_GST = errorLog.getOrigin();
        String amountText = errorLog.getLogMessage();

        holder.bind(errorLog);
        holder.customerNameTextView.setText(errorLog.getTypeOfError());
        holder.invoiceNumberTextView.setText(invoiceNumber);
        holder.invoiceDateTextView.setText(invoiceDate);
        holder.taxGSTTextView.setText(Tax_GST);
        holder.afterAmountTextView.setText(amountText);
        holder.syncedTextView.setText(errorLog.getSynced()+"");
        holder.afterAmountTextView.setVisibility(View.VISIBLE);
        holder.amountFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return errorLogs.size();
    }

    public interface OnClickListener {
        void onItemClick(ErrorLog errorLog);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView customerNameTextView;
        public TextView invoiceNumberTextView;
        public TextView invoiceDateTextView;
        public TextView taxGSTTextView;
        public TextView amountTextView, syncedTextView;
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
            syncedTextView = (TextView) itemView.findViewById(R.id.syncedTextView);
            afterAmountTextView = (TextView) itemView.findViewById(R.id.afterTotalAmountTextView);
            amountFrameLayout = (FrameLayout) itemView.findViewById(R.id.amountFrameLayout);

            myView = itemView;
        }

        public void bind(final ErrorLog errorLog) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(errorLog);
                }
            });
        }

    }
}
