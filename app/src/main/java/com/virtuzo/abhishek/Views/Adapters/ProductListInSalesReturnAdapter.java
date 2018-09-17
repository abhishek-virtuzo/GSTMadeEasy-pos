package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.TransactionSales;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductListInSalesReturnAdapter extends RecyclerView.Adapter<ProductListInSalesReturnAdapter.MyViewHolder> {

    private List<TransactionSales> ProductList;
    private ProductListInSalesReturnAdapter.OnClickListener listener;

    public ProductListInSalesReturnAdapter(ArrayList<TransactionSales> stateList, ProductListInSalesReturnAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductListInSalesReturnAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_sales_return_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductListInSalesReturnAdapter.MyViewHolder holder, final int position) {

        TransactionSales product = ProductList.get(position);
        holder.bind(product);

        String rupeeSymbol = "\u20B9";
        String salesPrice = String.format("%.2f",product.getSalesReturnUnitPrice());
        int quantity = product.getQty();

        holder.productNameTextView.setText(product.getItemName());
        holder.salesPriceTextView.setText(rupeeSymbol + salesPrice + " X " + quantity);

    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }


    public interface OnClickListener {
        void onItemClick(TransactionSales product);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView salesPriceTextView;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.productNameInSalesReturnTextView);
            salesPriceTextView = (TextView) itemView.findViewById(R.id.productSalesPriceInSalesReturnTextView);

            myView = itemView;
        }

        public void bind(final TransactionSales product) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onItemClick(product);
                }
            });
        }
    }

    public void setFilter(ArrayList<TransactionSales> newList) {
        this.ProductList = new ArrayList<>();
        this.ProductList.addAll(newList);
        notifyDataSetChanged();
    }

}
