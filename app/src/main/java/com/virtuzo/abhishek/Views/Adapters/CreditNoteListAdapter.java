package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.CreditDebitNote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class CreditNoteListAdapter extends RecyclerView.Adapter<CreditNoteListAdapter.MyViewHolder> {

    private List<CreditDebitNote> creditNoteList;
    private CreditNoteListAdapter.OnClickListener listener;

    public CreditNoteListAdapter(ArrayList<CreditDebitNote> stateList, CreditNoteListAdapter.OnClickListener onClickListener) {
        this.creditNoteList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public CreditNoteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CreditNoteListAdapter.MyViewHolder holder, final int position) {

        CreditDebitNote creditNote = creditNoteList.get(position);

        String rupeeSymbol = "\u20B9";
        String invoiceNumber = "Credit Note Number - " + creditNote.getNoteNumber();
        String invoiceDate = "Sales Return Number - " + creditNote.getSalesReturnNumber();
        String amountText = "Date - " + creditNote.getNoteDate();
        String Tax_GST = "Credit Amount - " + rupeeSymbol + String.format("%.2f", creditNote.getAmount());
        if (creditNote.getNoteType().equalsIgnoreCase("Debit Note")) {
            invoiceNumber = "Debit Note Number - " + creditNote.getNoteNumber();
            invoiceDate = "Purchase Return Number - " + creditNote.getSalesReturnNumber();
            amountText = "Date - " + creditNote.getNoteDate();
            Tax_GST = "Debit Amount - " + rupeeSymbol + String.format("%.2f", creditNote.getAmount());
        }

        holder.bind(creditNote);
        holder.customerNameTextView.setText(creditNote.getCustomerName());
        holder.invoiceNumberTextView.setText(invoiceNumber);
        holder.invoiceDateTextView.setText(invoiceDate);
        holder.taxGSTTextView.setText(Tax_GST);
        holder.afterAmountTextView.setText(amountText);
        holder.afterAmountTextView.setVisibility(View.VISIBLE);
        holder.amountFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return creditNoteList.size();
    }

    public void setFilter(ArrayList<CreditDebitNote> newList) {
        this.creditNoteList = new ArrayList<>();
        this.creditNoteList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onItemClick(CreditDebitNote creditNote);
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

        public void bind(final CreditDebitNote creditNote) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(creditNote);
                }
            });
        }

    }
}
