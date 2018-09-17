package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.DeliveryOrderProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductsSelectedInDeliveryOrderAdapter extends RecyclerView.Adapter<ProductsSelectedInDeliveryOrderAdapter.MyViewHolder> {

    private List<DeliveryOrderProduct> ProductList;
    private ProductsSelectedInDeliveryOrderAdapter.OnClickListener listener;

    public ProductsSelectedInDeliveryOrderAdapter(ArrayList<DeliveryOrderProduct> stateList, ProductsSelectedInDeliveryOrderAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductsSelectedInDeliveryOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_selected_new_delivery_order_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductsSelectedInDeliveryOrderAdapter.MyViewHolder holder, final int position) {

        DeliveryOrderProduct deliveryOrderProduct = ProductList.get(position);
        holder.bind(deliveryOrderProduct);

        holder.productNameTextView.setText(deliveryOrderProduct.getProduct().getProductName());
        holder.quantityTextView.setText(deliveryOrderProduct.getQuantity()+" ");

    }

    @Override
    public int getItemCount() {

        return ProductList.size();
    }

    public interface OnClickListener {
        void onItemClick(DeliveryOrderProduct product);
        void onCancelItem(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView quantityTextView;
        public ImageButton productCancelButton;

        public View myView;

        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.selectedProductNameTextView);
            quantityTextView = (TextView) itemView.findViewById(R.id.quantityTextView);

            productCancelButton = (ImageButton) itemView.findViewById(R.id.selectedProductCancelButton);
            productCancelButton.setMinimumWidth(productCancelButton.getHeight());
            productCancelButton.setMaxWidth(productCancelButton.getHeight());

            myView = itemView;
        }

        public void bind(final DeliveryOrderProduct product) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
            productCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onCancelItem(position);
                }
            });
        }
    }
}
