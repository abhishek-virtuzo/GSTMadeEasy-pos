package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.Payment;
import com.virtuzo.abhishek.modal.PaymentMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class PaymentAddedAdapter extends RecyclerView.Adapter<PaymentAddedAdapter.MyViewHolder> {

    private List<Payment> PaymentList;
    private PaymentAddedAdapter.OnClickListener listener;

    public PaymentAddedAdapter(ArrayList<Payment> stateList, PaymentAddedAdapter.OnClickListener onClickListener) {
        this.PaymentList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public PaymentAddedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_added_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaymentAddedAdapter.MyViewHolder holder, final int position) {

        Payment payment = PaymentList.get(position);
        holder.bind(payment);

        String rupeeSymbol = "\u20B9";
        String amount = String.format("%.2f", payment.getAmount());

        holder.paymentMode.setText(payment.getPaymentMode());
        holder.cardNumber.setText(payment.getCreditCardNumber());
        holder.referenceNumber.setText(payment.getReferenceNumber());
        holder.amount.setText(rupeeSymbol + " " + amount);
        switch (payment.getPaymentTypeID()) {
            case PaymentMode.CASH_OPTION:
                holder.referenceNumber.setVisibility(View.GONE);
                holder.cardNumber.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return PaymentList.size();
    }

    public interface OnClickListener {
        void onItemClick(Payment payment);
        void onCancelItem(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView paymentMode;
        public TextView amount;
        public ImageButton productCancelButton;
        public TextView cardNumber;
        public TextView referenceNumber;

        public View myView;

        public MyViewHolder(View itemView) {
            super(itemView);

            paymentMode = (TextView) itemView.findViewById(R.id.paymentMode);
            amount = (TextView) itemView.findViewById(R.id.amount);
            cardNumber = (TextView) itemView.findViewById(R.id.cardNumber);
            referenceNumber = (TextView) itemView.findViewById(R.id.referenceNumber);

//            productCancelButton = (ImageButton) itemView.findViewById(R.id.selectedProductCancelButton);
//            productCancelButton.setMinimumWidth(productCancelButton.getHeight());
//            productCancelButton.setMaxWidth(productCancelButton.getHeight());

            myView = itemView;
        }

        public void bind(final Payment payment) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(payment);
                }
            });
//            productCancelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    listener.onCancelItem(position);
//                }
//            });
        }
    }
}
