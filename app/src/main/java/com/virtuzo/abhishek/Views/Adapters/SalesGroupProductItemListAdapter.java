package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.SalesGroupProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class SalesGroupProductItemListAdapter extends RecyclerView.Adapter<SalesGroupProductItemListAdapter.MyViewHolder> {

    private List<SalesGroupProductItem> salesGroupProductItems;
    private SalesGroupProductItemListAdapter.OnClickListener listener;

    public SalesGroupProductItemListAdapter(ArrayList<SalesGroupProductItem> stateList, SalesGroupProductItemListAdapter.OnClickListener onClickListener) {
        this.salesGroupProductItems = stateList;
        this.listener = onClickListener;
    }

    @Override
    public SalesGroupProductItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_product_item_in_product_selected_new_invoice_view_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SalesGroupProductItemListAdapter.MyViewHolder holder, final int position) {

        SalesGroupProductItem salesGroupProductItem = salesGroupProductItems.get(position);
        String rupeeSymbol = "\u20B9";

        String itemRate = rupeeSymbol + String.format("%.2f",salesGroupProductItem.getUnitPrice()) + " X " + salesGroupProductItem.getNewQuantity() + " =";
        String itemAmount = rupeeSymbol + String.format("%.2f", salesGroupProductItem.getUnitPrice()*salesGroupProductItem.getNewQuantity());

        holder.bind(salesGroupProductItem);
        holder.groupItemNameTextView.setText(salesGroupProductItem.getGroupProductItem().getItemName());
        holder.groupItemRateTextView.setText(itemRate);
        holder.groupItemAmountTextView.setText(itemAmount);

        holder.taxRateTextView.setText("Tax(" + String.format("%.2f", salesGroupProductItem.getTaxPercentage()) + "%)");
        holder.taxAmountTextView.setText(rupeeSymbol + String.format("%.2f", salesGroupProductItem.getTaxAmount() * salesGroupProductItem.getNewQuantity()));

    }

    @Override
    public int getItemCount() {
        return salesGroupProductItems.size();
    }

    public interface OnClickListener {
        void onGroupItemClick(SalesGroupProductItem salesGroupProductItem);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView groupItemNameTextView;
        public TextView groupItemRateTextView;
        public TextView groupItemAmountTextView;
        public TextView taxRateTextView, taxAmountTextView;
        public View myView;

        public MyViewHolder(View itemView) {
            super(itemView);

            groupItemNameTextView = (TextView) itemView.findViewById(R.id.groupItemNameTextView);
            groupItemRateTextView = (TextView) itemView.findViewById(R.id.groupItemRateTextView);
            groupItemAmountTextView = (TextView) itemView.findViewById(R.id.groupItemAmountTextView);

            taxRateTextView = (TextView) itemView.findViewById(R.id.taxRateTextView);
            taxAmountTextView = (TextView) itemView.findViewById(R.id.taxAmountTextView);

            myView = itemView;
        }

        public void bind(final SalesGroupProductItem salesGroupProductItem) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGroupItemClick(salesGroupProductItem);
                }
            });
        }

    }
}
