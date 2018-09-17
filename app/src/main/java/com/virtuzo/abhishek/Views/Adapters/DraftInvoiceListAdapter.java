package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.DraftInvoice;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.ProductWithQuantity;
import com.virtuzo.abhishek.modal.SalesGroupProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class DraftInvoiceListAdapter extends RecyclerView.Adapter<DraftInvoiceListAdapter.MyViewHolder> {

    private List<DraftInvoice> draftInvoices;
    private DraftInvoiceListAdapter.OnClickListener listener;

    public DraftInvoiceListAdapter(ArrayList<DraftInvoice> stateList, DraftInvoiceListAdapter.OnClickListener onClickListener) {
        this.draftInvoices = stateList;
        this.listener = onClickListener;
    }

    @Override
    public DraftInvoiceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.draft_recycler_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DraftInvoiceListAdapter.MyViewHolder holder, final int position) {

        DraftInvoice draftInvoice = draftInvoices.get(position);
        holder.bind(draftInvoice);
        String rupeeSymbol = "\u20B9";
        String nextLine = "\n";
        String tabSpace = "\t";
        holder.dateTextView.setText(draftInvoice.getMasterSales().getSalesDtTm());

        StringBuilder customerBuilder = new StringBuilder();
        customerBuilder.append(nextLine);
        customerBuilder.append(tabSpace);
        customerBuilder.append("Customer : ");
        customerBuilder.append(draftInvoice.getMasterSales().getCustomerName());
        if (MyFunctions.StringLength(draftInvoice.getMasterSales().getCustomerMobile()) > 0) {
            customerBuilder.append("(" + draftInvoice.getMasterSales().getCustomerMobile() + ")");
        }
        holder.customerTextView.setText(customerBuilder.toString());

        StringBuilder productsBuilder = new StringBuilder();
        ArrayList<ProductWithQuantity> products = draftInvoice.getProductList();
        int index = 0;
        int noOfProductsToShow = 2;
        for (ProductWithQuantity product : products) {
            productsBuilder.append(nextLine);
            index++;
            productsBuilder.append(index+". ");
            productsBuilder.append(product.getProduct().getProductName());
            if (product.getProduct().getIsGroupProduct() == 1) {
                for (SalesGroupProductItem salesGroupProductItem : product.getSalesGroupProductItems()) {
                    String unitPrice = String.format("%.2f",salesGroupProductItem.getUnitPrice());

                    productsBuilder.append(nextLine);
                    productsBuilder.append(tabSpace);
                    productsBuilder.append(tabSpace);
                    productsBuilder.append(tabSpace);
                    productsBuilder.append(salesGroupProductItem.getGroupProductItem().getItemName());
                    productsBuilder.append(" (");
                    productsBuilder.append(rupeeSymbol + unitPrice + " X " + salesGroupProductItem.getNewQuantity());
                    productsBuilder.append(")");
                }
            } else {
                String salesPrice = String.format("%.2f",product.getProduct().getSalesPrice());

                productsBuilder.append(" (");
                productsBuilder.append(rupeeSymbol + salesPrice + " X " + product.getQuantity());
                productsBuilder.append(")");
            }
            productsBuilder.append(nextLine);
            if (index == noOfProductsToShow && index < products.size()) {
                productsBuilder.append(tabSpace);
                productsBuilder.append(tabSpace);
                productsBuilder.append("...");
                productsBuilder.append(nextLine);
                break;
            }
        }
        holder.productsTextView.setText(productsBuilder.toString());

    }

    @Override
    public int getItemCount() {
        return draftInvoices.size();
    }

    public interface OnClickListener {
        void onItemClick(DraftInvoice draftInvoice);
        void onOptionClick(DraftInvoice draftInvoice, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView dateTextView;
        public TextView customerTextView;
        public TextView productsTextView;
        public ImageView optionMenuImageView;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            customerTextView = (TextView) itemView.findViewById(R.id.customerTextView);
            productsTextView = (TextView) itemView.findViewById(R.id.productsTextView);
            optionMenuImageView = (ImageView) itemView.findViewById(R.id.optionMenuImageView);

            myView = itemView;
        }

        public void bind(final DraftInvoice draftInvoice) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(draftInvoice);
                }
            });
            optionMenuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOptionClick(draftInvoice, optionMenuImageView);
                }
            });
        }

    }
}
