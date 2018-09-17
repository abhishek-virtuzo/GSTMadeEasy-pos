package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.MyViewHolder> {

    private List<Customer> CustomerList;
    private CustomerListAdapter.OnClickListener listener;

    public CustomerListAdapter(ArrayList<Customer> stateList, CustomerListAdapter.OnClickListener onClickListener) {
        this.CustomerList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public CustomerListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomerListAdapter.MyViewHolder holder, final int position) {

        Customer customer = CustomerList.get(position);

        String entityType = "Entity Type - " + customer.getEntityType();
        String emailId = "EmailId - " + customer.getContactEmailId();
        String ContactNumber = "Contact Number - " + customer.getContactNumber();
        String s = customer.getGender();

        holder.bind(customer);

        if(customer.getEntityType().equalsIgnoreCase( "Individual")) {
            holder.customerNameTextView.setText(customer.getContactPerson());
        }else {
            holder.customerNameTextView.setText(customer.getEntityName());
        }

        holder.entityNameTextView.setText(entityType);
        holder.emailIdTextView.setText(emailId);
        holder.GSTNumberTextView.setText(ContactNumber);
//        holder.afterTotalAmountTextView.setVisibility(View.VISIBLE);
        holder.afterTotalAmountTextView.setText(s);
        holder.typeTextView.setText("");

        holder.amountFrameLayout.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {

        return CustomerList.size();
    }

    public void setFilter(ArrayList<Customer> newList) {
        this.CustomerList = new ArrayList<>();
        this.CustomerList.addAll(newList);
        notifyDataSetChanged();
    }


    public interface OnClickListener {
        void onItemClick(Customer customer);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView customerNameTextView;
        public TextView entityNameTextView;
        public TextView emailIdTextView;
        public TextView GSTNumberTextView;
        public TextView afterTotalAmountTextView;
        public TextView typeTextView;
        public FrameLayout amountFrameLayout;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            customerNameTextView = (TextView) itemView.findViewById(R.id.productDescriptionTextView);
            entityNameTextView = (TextView) itemView.findViewById(R.id.rechargeAmountTextView);
            emailIdTextView = (TextView) itemView.findViewById(R.id.regulatoryTextView);
            GSTNumberTextView = (TextView) itemView.findViewById(R.id.discountTextView);
            typeTextView = (TextView) itemView.findViewById(R.id.totalAmountTextView);
            afterTotalAmountTextView = (TextView) itemView.findViewById(R.id.afterTotalAmountTextView);
            amountFrameLayout = (FrameLayout) itemView.findViewById(R.id.amountFrameLayout);

            myView = itemView;
        }

        public void bind(final Customer hotelid) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(hotelid);
                }
            });
        }

    }
}
