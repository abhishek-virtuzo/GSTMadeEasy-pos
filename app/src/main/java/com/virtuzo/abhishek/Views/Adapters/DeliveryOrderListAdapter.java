package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.MasterDeliveryOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class DeliveryOrderListAdapter extends RecyclerView.Adapter<DeliveryOrderListAdapter.MyViewHolder> {

    private List<MasterDeliveryOrder> masterDeliveryOrderList;
    private DeliveryOrderListAdapter.OnClickListener listener;

    public DeliveryOrderListAdapter(ArrayList<MasterDeliveryOrder> stateList, DeliveryOrderListAdapter.OnClickListener onClickListener) {
        this.masterDeliveryOrderList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public DeliveryOrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_order_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DeliveryOrderListAdapter.MyViewHolder holder, final int position) {

        MasterDeliveryOrder masterDeliveryOrder = masterDeliveryOrderList.get(position);

        String invoiceNumber = "DO Number - " + masterDeliveryOrder.getDONumber();
        String invoiceDate = "Delivery Date - " + masterDeliveryOrder.getDeliveryDate();
        String Tax_GST = "Email Id - " + masterDeliveryOrder.getCustomerEmail();

        holder.bind(masterDeliveryOrder);
        holder.customerNameTextView.setText(masterDeliveryOrder.getCustomerName());
        holder.invoiceNumberTextView.setText(invoiceNumber);
        holder.invoiceDateTextView.setText(invoiceDate);
        holder.taxGSTTextView.setText(Tax_GST);
        holder.amountFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return masterDeliveryOrderList.size();
    }

    public void setFilter(ArrayList<MasterDeliveryOrder> newList) {
        this.masterDeliveryOrderList = new ArrayList<>();
        this.masterDeliveryOrderList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onItemClick(MasterDeliveryOrder masterDeliveryOrder);
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

        public void bind(final MasterDeliveryOrder masterDeliveryOrder) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(masterDeliveryOrder);
                }
            });
        }

    }
}
