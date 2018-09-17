package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.MasterSalesReturn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class SalesReturnListAdapter extends RecyclerView.Adapter<SalesReturnListAdapter.MyViewHolder> {

    private List<MasterSalesReturn> masterSalesReturnList;
    private SalesReturnListAdapter.OnClickListener listener;

    public SalesReturnListAdapter(ArrayList<MasterSalesReturn> stateList, SalesReturnListAdapter.OnClickListener onClickListener) {
        this.masterSalesReturnList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public SalesReturnListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SalesReturnListAdapter.MyViewHolder holder, final int position) {

        MasterSalesReturn masterSalesReturn = masterSalesReturnList.get(position);

        String rupeeSymbol = "\u20B9";
        String invoiceNumber = "Sales Return Number - " + masterSalesReturn.getSalesReturnNumber();
        String invoiceDate = "Invoice Number - " + masterSalesReturn.getInvoiceNumber();
        String amountText = "Date - " + masterSalesReturn.getSalesReturnDate();
        String Tax_GST = "Credit Amount - " + rupeeSymbol + String.format("%.2f", masterSalesReturn.getCreditAmount());

        holder.bind(masterSalesReturn);
        holder.customerNameTextView.setText(masterSalesReturn.getCustomerName());
        holder.invoiceNumberTextView.setText(invoiceNumber);
        holder.invoiceDateTextView.setText(invoiceDate);
        holder.taxGSTTextView.setText(Tax_GST);
        holder.afterAmountTextView.setText(amountText);
        holder.afterAmountTextView.setVisibility(View.VISIBLE);
        holder.amountFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return masterSalesReturnList.size();
    }

    public void setFilter(ArrayList<MasterSalesReturn> newList) {
        this.masterSalesReturnList = new ArrayList<>();
        this.masterSalesReturnList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onItemClick(MasterSalesReturn masterSalesReturn);
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

        public void bind(final MasterSalesReturn masterSalesReturn) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(masterSalesReturn);
                }
            });
        }

    }
}
