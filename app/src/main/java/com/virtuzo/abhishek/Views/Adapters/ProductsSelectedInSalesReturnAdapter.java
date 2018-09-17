package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.SalesReturnProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductsSelectedInSalesReturnAdapter extends RecyclerView.Adapter<ProductsSelectedInSalesReturnAdapter.MyViewHolder> {

    private List<SalesReturnProduct> ProductList;
    private ProductsSelectedInSalesReturnAdapter.OnClickListener listener;

    public ProductsSelectedInSalesReturnAdapter(ArrayList<SalesReturnProduct> stateList, ProductsSelectedInSalesReturnAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductsSelectedInSalesReturnAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_selected_sales_return_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductsSelectedInSalesReturnAdapter.MyViewHolder holder, final int position) {

        SalesReturnProduct salesReturnProduct = ProductList.get(position);
        holder.bind(salesReturnProduct);

        String salesPrice = String.format("%.2f",salesReturnProduct.getReturnPrice());
        String finalSalesPrice = String.format("%.2f",salesReturnProduct.getReturnPrice()*salesReturnProduct.getReturnQuantity());
        String rupeeSymbol = "\u20B9";

        holder.productNameTextView.setText(salesReturnProduct.getTransactionSales().getItemName());

        if (salesReturnProduct.getReturnType() == 0) {
            holder.returnTypeInSalesReturnTextView.setText("(Product Returned)");
        } else {
            holder.returnTypeInSalesReturnTextView.setText("(Price Change Only)");
        }

        holder.rateQtyTextView.setText(rupeeSymbol + salesPrice + " X " + salesReturnProduct.getReturnQuantity() + " = ");
        holder.salesPriceTextView.setText(rupeeSymbol + finalSalesPrice);

        holder.taxTextTextView.setText("Tax(" + String.format("%.2f", salesReturnProduct.getReturnTaxRate()) + "%)");
        holder.taxValueTextView.setText(rupeeSymbol + String.format("%.2f",
                                                                        salesReturnProduct.getReturnTax()));

    }

    @Override
    public int getItemCount() {

        return ProductList.size();
    }

    public interface OnClickListener {
        void onItemClick(SalesReturnProduct product);
        void onCancelItem(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView returnTypeInSalesReturnTextView;
        public TextView salesPriceTextView;
        public ImageButton productCancelButton;
        public TextView rateQtyTextView;
        public TextView taxTextTextView;
        public TextView taxValueTextView;

        public View myView;

        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.selectedProductNameInSalesReturnTextView);
            returnTypeInSalesReturnTextView = (TextView) itemView.findViewById(R.id.returnTypeInSalesReturnTextView);
            salesPriceTextView = (TextView) itemView.findViewById(R.id.selectedProductSalesPriceInSalesReturnTextView);
            rateQtyTextView = (TextView) itemView.findViewById(R.id.rateQtyInSalesReturnTextView);
            taxTextTextView = (TextView) itemView.findViewById(R.id.taxTextInSalesReturnTextView);
            taxValueTextView = (TextView) itemView.findViewById(R.id.taxValueInSalesReturnTextView);

            productCancelButton = (ImageButton) itemView.findViewById(R.id.selectedProductCancelButton);
            productCancelButton.setMinimumWidth(productCancelButton.getHeight());
            productCancelButton.setMaxWidth(productCancelButton.getHeight());

            myView = itemView;
        }

        public void bind(final SalesReturnProduct product) {
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
