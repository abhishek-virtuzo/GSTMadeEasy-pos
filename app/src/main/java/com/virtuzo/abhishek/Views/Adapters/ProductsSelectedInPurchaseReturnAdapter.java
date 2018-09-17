package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.PurchaseReturnProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductsSelectedInPurchaseReturnAdapter extends RecyclerView.Adapter<ProductsSelectedInPurchaseReturnAdapter.MyViewHolder> {

    private List<PurchaseReturnProduct> ProductList;
    private ProductsSelectedInPurchaseReturnAdapter.OnClickListener listener;

    public ProductsSelectedInPurchaseReturnAdapter(ArrayList<PurchaseReturnProduct> stateList, ProductsSelectedInPurchaseReturnAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductsSelectedInPurchaseReturnAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_selected_purchase_return_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductsSelectedInPurchaseReturnAdapter.MyViewHolder holder, final int position) {

        PurchaseReturnProduct purchaseReturnProduct = ProductList.get(position);
        holder.bind(purchaseReturnProduct);

        String salesPrice = String.format("%.2f",purchaseReturnProduct.getReturnPrice());
        String finalSalesPrice = String.format("%.2f",purchaseReturnProduct.getReturnPrice()*purchaseReturnProduct.getReturnQuantity());
        String rupeeSymbol = "\u20B9";

        holder.productNameTextView.setText(purchaseReturnProduct.getTransactionPurchase().getProductName());

        holder.rateQtyTextView.setText(rupeeSymbol + salesPrice + " X " + purchaseReturnProduct.getReturnQuantity() + " = ");
        holder.unitPriceTextView.setText(rupeeSymbol + finalSalesPrice);

        holder.taxTextTextView.setText("Tax =");
        holder.taxValueTextView.setText(rupeeSymbol + String.format("%.2f",
                                                                        purchaseReturnProduct.getReturnTotalTax()));

    }

    @Override
    public int getItemCount() {

        return ProductList.size();
    }

    public interface OnClickListener {
        void onItemClick(PurchaseReturnProduct product);
        void onCancelItem(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView unitPriceTextView;
        public ImageButton productCancelButton;
        public TextView rateQtyTextView;
        public TextView taxTextTextView;
        public TextView taxValueTextView;

        public View myView;

        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.selectedProductNameTextView);
            unitPriceTextView = (TextView) itemView.findViewById(R.id.selectedProductUnitPriceTextView);
            rateQtyTextView = (TextView) itemView.findViewById(R.id.rateQtyTextView);
            taxTextTextView = (TextView) itemView.findViewById(R.id.taxTextTextView);
            taxValueTextView = (TextView) itemView.findViewById(R.id.taxValueTextView);

            productCancelButton = (ImageButton) itemView.findViewById(R.id.selectedProductCancelButton);
            productCancelButton.setMinimumWidth(productCancelButton.getHeight());
            productCancelButton.setMaxWidth(productCancelButton.getHeight());

            myView = itemView;
        }

        public void bind(final PurchaseReturnProduct product) {
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
