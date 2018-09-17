package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductListInNewInvoiceAdapter extends RecyclerView.Adapter<ProductListInNewInvoiceAdapter.MyViewHolder> {

    private List<Product> ProductList;
    private ProductListInNewInvoiceAdapter.OnClickListener listener;

    public ProductListInNewInvoiceAdapter(ArrayList<Product> stateList, ProductListInNewInvoiceAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductListInNewInvoiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_new_invoice_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductListInNewInvoiceAdapter.MyViewHolder holder, final int position) {

        Product product = ProductList.get(position);
        holder.bind(product);

        String salesPrice = String.format("%.2f",product.getSalesPrice());
        String rupeeSymbol = "\u20B9";
        holder.productNameTextView.setText(product.getProductName() + "\n(" + product.getProductCode() + ")");
        holder.salesPriceTextView.setText(rupeeSymbol + " " + salesPrice);

    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }


    public interface OnClickListener {
        void onItemClick(Product product);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView salesPriceTextView;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.productNameInNewInvoiceTextView);
            salesPriceTextView = (TextView) itemView.findViewById(R.id.productSalesPriceInNewInvoiceTextView);

            myView = itemView;
        }

        public void bind(final Product product) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onItemClick(product);
                }
            });
        }
    }

    public void setFilter(ArrayList<Product> newList) {
        this.ProductList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public Product removeItem(int position) {
        final Product model = ProductList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Product model) {
        ProductList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Product model = ProductList.remove(fromPosition);
        ProductList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Product> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Product> newModels) {
        for (int i = ProductList.size() - 1; i >= 0; i--) {
            final Product model = ProductList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Product> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Product model = newModels.get(i);
            if (!ProductList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Product> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Product model = newModels.get(toPosition);
            final int fromPosition = ProductList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
