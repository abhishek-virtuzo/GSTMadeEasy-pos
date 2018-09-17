package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private List<Product> ProductList;
    private ProductListAdapter.OnClickListener listener;

    public ProductListAdapter(ArrayList<Product> stateList, ProductListAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductListAdapter.MyViewHolder holder, final int position) {

        Product product = ProductList.get(position);

        String brandName = "Brand - " + product.getBrand();
        String hsnCode = "HSN - " + product.getHSNCode();
        String tax = "Tax - " + product.getTAX() + "%";
        String rupeeSymbol = "\u20B9";
        String salesPrice = String.format("%.2f",product.getSalesPrice());

        holder.bind(product);
        holder.productNameTextView.setText(product.getProductName());
        holder.brandTextView.setText(brandName);
        holder.hsnCodeTextView.setText(hsnCode);
        holder.taxTextView.setText(tax);
        holder.salesPriceTextView.setText(rupeeSymbol + " " + salesPrice);

    }

    @Override
    public int getItemCount() {

        return ProductList.size();
    }

    public void setFilter(ArrayList<Product> newList) {
        this.ProductList = new ArrayList<>();
        this.ProductList.addAll(newList);
        notifyDataSetChanged();
    }


    public interface OnClickListener {
        void onItemClick(Product product);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView brandTextView;
        public TextView hsnCodeTextView;
        public TextView taxTextView;
        public TextView salesPriceTextView;
        public FrameLayout amountFrameLayout;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.productDescriptionTextView);
            brandTextView = (TextView) itemView.findViewById(R.id.rechargeAmountTextView);
            hsnCodeTextView = (TextView) itemView.findViewById(R.id.regulatoryTextView);
            taxTextView = (TextView) itemView.findViewById(R.id.discountTextView);
            salesPriceTextView = (TextView) itemView.findViewById(R.id.totalAmountTextView);
            amountFrameLayout = (FrameLayout) itemView.findViewById(R.id.amountFrameLayout);

            myView = itemView;
        }

        public void bind(final Product hotelid) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(hotelid);
                }
            });
        }

    }
}
