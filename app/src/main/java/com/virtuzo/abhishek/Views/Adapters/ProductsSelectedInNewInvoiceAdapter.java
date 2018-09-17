package com.virtuzo.abhishek.Views.Adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.GroupProductItem;
import com.virtuzo.abhishek.modal.Product;
import com.virtuzo.abhishek.modal.ProductWithQuantity;
import com.virtuzo.abhishek.modal.SalesGroupProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductsSelectedInNewInvoiceAdapter extends RecyclerView.Adapter<ProductsSelectedInNewInvoiceAdapter.MyViewHolder> {

    private List<ProductWithQuantity> ProductList;
    private ProductsSelectedInNewInvoiceAdapter.OnClickListener listener;
    Context context;

    public ProductsSelectedInNewInvoiceAdapter(Context context, ArrayList<ProductWithQuantity> stateList, ProductsSelectedInNewInvoiceAdapter.OnClickListener onClickListener) {
        this.context = context;
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductsSelectedInNewInvoiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_selected_new_invoice_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductsSelectedInNewInvoiceAdapter.MyViewHolder holder, final int position) {

        final ProductWithQuantity productWithQuantity = ProductList.get(position);
        holder.bind(productWithQuantity);

        refreshGroupProductSalesPrice(productWithQuantity);

        String salesPrice = String.format("%.2f",productWithQuantity.getProduct().getSalesPrice());
        String finalSalesPrice = String.format("%.2f",productWithQuantity.getProduct().getSalesPrice()*productWithQuantity.getQuantity());
        String rupeeSymbol = "\u20B9";

        holder.productNameTextView.setText(productWithQuantity.getProduct().getProductName());

        if (productWithQuantity.getProduct().getIsGroupProduct() == 1) {
            holder.rateQtyTextView.setText("Sub Total Amount = ");
        } else {
            holder.rateQtyTextView.setText(rupeeSymbol + salesPrice + " X " + productWithQuantity.getQuantity() + " = ");
        }
        holder.salesPriceTextView.setText(rupeeSymbol + finalSalesPrice);

        switch (productWithQuantity.getDiscountType()) {
            case 0:
                holder.discountTextInNewInvoiceTextView.setText("Discount(" + String.format("%.2f",
                        (productWithQuantity.getDiscount()
                                / productWithQuantity.getProduct().getSalesPrice())
                                *100d) + "%)");
                holder.discountValueInNewInvoiceTextView.setText("- " + rupeeSymbol
                        + String.format("%.2f",productWithQuantity.getDiscount()
                        * productWithQuantity.getQuantity()));
                break;
            case 1:
                holder.discountTextInNewInvoiceTextView.setText("Discount(" + String.format("%.2f",
                        productWithQuantity.getDiscount()) + "%)");
                holder.discountValueInNewInvoiceTextView.setText("- " + rupeeSymbol
                        + String.format("%.2f",
                         (productWithQuantity.getDiscount()
                        * productWithQuantity.getProduct().getSalesPrice()
                        * productWithQuantity.getQuantity())
                        / 100));
                break;
        }

        double priceAfterDiscount = productWithQuantity.getProduct().getSalesPrice()
                                                                - productWithQuantity.getDiscountAmount();

        if (productWithQuantity.getProduct().getIsGroupProduct() == 1) {
            holder.taxTextInNewInvoiceTextView.setText("Tax Amount =");
            double totalTaxAmount = 0.0;
            for (SalesGroupProductItem salesGroupProductItem : productWithQuantity.getSalesGroupProductItems()) {
                totalTaxAmount += (salesGroupProductItem.getTaxAmount() * salesGroupProductItem.getNewQuantity());
            }
            holder.taxValueInNewInvoiceTextView.setText(rupeeSymbol + String.format("%.2f", totalTaxAmount));
        } else {
            holder.taxTextInNewInvoiceTextView.setText("Tax(" + String.format("%.2f", productWithQuantity.getProduct().getTAX()) + "%)");
            holder.taxValueInNewInvoiceTextView.setText(rupeeSymbol + String.format("%.2f",
                    (priceAfterDiscount
                            * productWithQuantity.getQuantity()
                            * productWithQuantity.getProduct().getTAX()) / 100d));
        }
        // reset group product layout
        if (productWithQuantity.isGroupProductsShown()) {
            holder.groupProductsLayout.setVisibility(View.VISIBLE);
        } else {
            holder.groupProductsLayout.setVisibility(View.GONE);
        }

        if (productWithQuantity.getProduct().getIsGroupProduct() == 1) {
            holder.salesGroupProductItemListAdapter = new SalesGroupProductItemListAdapter(productWithQuantity.getSalesGroupProductItems(), new SalesGroupProductItemListAdapter.OnClickListener() {
                @Override
                public void onGroupItemClick(SalesGroupProductItem salesGroupProductItem) {
                    listener.onGroupItemClick(salesGroupProductItem, productWithQuantity);
                }

            });
            holder.groupItemsRecyclerView.setAdapter(holder.salesGroupProductItemListAdapter);
            holder.salesGroupProductItemListAdapter.notifyDataSetChanged();

            holder.discountTextInNewInvoiceTextView.setVisibility(View.GONE);
            holder.discountValueInNewInvoiceTextView.setVisibility(View.GONE);
        } else {
            holder.discountTextInNewInvoiceTextView.setVisibility(View.VISIBLE);
            holder.discountValueInNewInvoiceTextView.setVisibility(View.VISIBLE);
        }

    }

    public void hideAllGroupProductDetails() {
        for (ProductWithQuantity productWithQuantity : ProductList) {
            productWithQuantity.setGroupProductsShown(false);
        }
    }

    public void hideGroupProductDetails(ProductWithQuantity productWithQuantity, LinearLayout groupProductsLayout) {
        productWithQuantity.setGroupProductsShown(false);
        groupProductsLayout.setVisibility(View.GONE);
    }

    public void showGroupProductDetails(ProductWithQuantity productWithQuantity, LinearLayout groupProductsLayout) {
        productWithQuantity.setGroupProductsShown(true);
        groupProductsLayout.setVisibility(View.VISIBLE);
    }

    private void refreshGroupProductSalesPrice(ProductWithQuantity productWithQuantity) {
        if (productWithQuantity.getProduct().getIsGroupProduct() == 1) {
            double salesPrice = 0.0;
            for (SalesGroupProductItem salesGroupProductItem : productWithQuantity.getSalesGroupProductItems()) {
                salesPrice += (salesGroupProductItem.getUnitPrice() * salesGroupProductItem.getNewQuantity());
            }
            productWithQuantity.getProduct().setSalesPrice(salesPrice);
        }
    }

    @Override
    public int getItemCount() {

        return ProductList.size();
    }

    public interface OnClickListener {
        void onItemClick(ProductWithQuantity product, LinearLayout groupProductsLayout);
        void onGroupItemClick(SalesGroupProductItem salesGroupProductItem, ProductWithQuantity groupProduct);
        void onCancelItem(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView salesPriceTextView;
        public ImageButton productCancelButton;
        public TextView rateQtyTextView;
        public TextView discountTextInNewInvoiceTextView;
        public TextView discountValueInNewInvoiceTextView;
        public TextView taxTextInNewInvoiceTextView;
        public TextView taxValueInNewInvoiceTextView;

        public LinearLayout groupProductsLayout;
        public RecyclerView groupItemsRecyclerView;
        public SalesGroupProductItemListAdapter salesGroupProductItemListAdapter;

        public View myView;

        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.selectedProductNameInNewInvoiceTextView);
            salesPriceTextView = (TextView) itemView.findViewById(R.id.selectedProductSalesPriceInNewInvoiceTextView);
            rateQtyTextView = (TextView) itemView.findViewById(R.id.rateQtyInNewInvoiceTextView);
            discountTextInNewInvoiceTextView = (TextView) itemView.findViewById(R.id.discountTextInNewInvoiceTextView);
            discountValueInNewInvoiceTextView = (TextView) itemView.findViewById(R.id.discountValueInNewInvoiceTextView);
            taxTextInNewInvoiceTextView = (TextView) itemView.findViewById(R.id.taxTextInNewInvoiceTextView);
            taxValueInNewInvoiceTextView = (TextView) itemView.findViewById(R.id.taxValueInNewInvoiceTextView);

            groupProductsLayout = (LinearLayout) itemView.findViewById(R.id.groupProductsLayout);
            groupItemsRecyclerView = (RecyclerView) itemView.findViewById(R.id.groupItemsRecyclerView);
            groupItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            groupItemsRecyclerView.setItemAnimator(new DefaultItemAnimator());

            productCancelButton = (ImageButton) itemView.findViewById(R.id.selectedProductCancelButton);
            productCancelButton.setMinimumWidth(productCancelButton.getHeight());
            productCancelButton.setMaxWidth(productCancelButton.getHeight());

            myView = itemView;
        }

        public void bind(final ProductWithQuantity product) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product, groupProductsLayout);
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
