package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.Supplier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class SupplierListAdapter extends RecyclerView.Adapter<SupplierListAdapter.MyViewHolder> {

    private List<Supplier> SupplierList;
    private SupplierListAdapter.OnClickListener listener;

    public SupplierListAdapter(ArrayList<Supplier> stateList, SupplierListAdapter.OnClickListener onClickListener) {
        this.SupplierList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public SupplierListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SupplierListAdapter.MyViewHolder holder, final int position) {

        Supplier supplier = SupplierList.get(position);

        String entityName = "Entity Type - " + supplier.getEntityType();
        String emailId = "EmailId - " + supplier.getContactEmailId();
        String ContactNumber = "Contact Number - " + supplier.getContactNumber();

        holder.bind(supplier);
        if(supplier.getEntityType().equalsIgnoreCase( "Individual")) {
            holder.supplierNameTextView.setText(supplier.getContactPerson());
        }else {
            holder.supplierNameTextView.setText(supplier.getEntityName());
        }
        holder.entityNameTextView.setText(entityName);
        holder.emailIdTextView.setText(emailId);
        holder.GSTNumberTextView.setText(ContactNumber);
        holder.typeTextView.setText("C");

        holder.amountFrameLayout.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {

        return SupplierList.size();
    }

    public void setFilter(ArrayList<Supplier> newList) {
        this.SupplierList = new ArrayList<>();
        this.SupplierList.addAll(newList);
        notifyDataSetChanged();
    }


    public interface OnClickListener {
        void onItemClick(Supplier supplier);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView supplierNameTextView;
        public TextView entityNameTextView;
        public TextView emailIdTextView;
        public TextView GSTNumberTextView;
        public TextView typeTextView;
        public FrameLayout amountFrameLayout;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            supplierNameTextView = (TextView) itemView.findViewById(R.id.productDescriptionTextView);
            entityNameTextView = (TextView) itemView.findViewById(R.id.rechargeAmountTextView);
            emailIdTextView = (TextView) itemView.findViewById(R.id.regulatoryTextView);
            GSTNumberTextView = (TextView) itemView.findViewById(R.id.discountTextView);
            typeTextView = (TextView) itemView.findViewById(R.id.totalAmountTextView);
            amountFrameLayout = (FrameLayout) itemView.findViewById(R.id.amountFrameLayout);

            myView = itemView;
        }

        public void bind(final Supplier hotelid) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(hotelid);
                }
            });
        }

    }
}
