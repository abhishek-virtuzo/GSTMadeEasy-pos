package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.TransactionPurchase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductListInPurchaseReturnAdapter extends RecyclerView.Adapter<ProductListInPurchaseReturnAdapter.MyViewHolder> {

    private List<TransactionPurchase> ProductList;
    private ProductListInPurchaseReturnAdapter.OnClickListener listener;

    public ProductListInPurchaseReturnAdapter(ArrayList<TransactionPurchase> stateList, ProductListInPurchaseReturnAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductListInPurchaseReturnAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_purchase_return_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductListInPurchaseReturnAdapter.MyViewHolder holder, final int position) {

        TransactionPurchase product = ProductList.get(position);
        holder.bind(product);

        String rupeeSymbol = "\u20B9";
        String unitPrice = String.format("%.2f",product.getUnitPrice());
        int quantity = product.getQuantity();

        holder.productNameTextView.setText(product.getProductName());
        holder.productUnitPriceTextView.setText(rupeeSymbol + unitPrice + " X " + quantity);

    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }


    public interface OnClickListener {
        void onItemClick(TransactionPurchase product);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView productUnitPriceTextView;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.productNameTextView);
            productUnitPriceTextView = (TextView) itemView.findViewById(R.id.productUnitPriceTextView);

            myView = itemView;
        }

        public void bind(final TransactionPurchase product) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onItemClick(product);
                }
            });
        }
    }

    public void setFilter(ArrayList<TransactionPurchase> newList) {
        this.ProductList = new ArrayList<>();
        this.ProductList.addAll(newList);
        notifyDataSetChanged();
    }

}
