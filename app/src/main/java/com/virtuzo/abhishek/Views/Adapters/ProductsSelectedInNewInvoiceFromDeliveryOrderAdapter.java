package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.ProductWithQuantity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter extends RecyclerView.Adapter<ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter.MyViewHolder> {

    private List<ProductWithQuantity> ProductList;
    private ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter.OnClickListener listener;

    public ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter(ArrayList<ProductWithQuantity> stateList, ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_selected_new_invoice_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter.MyViewHolder holder, final int position) {

        ProductWithQuantity productWithQuantity = ProductList.get(position);
        holder.bind(productWithQuantity);

        String salesPrice = String.format("%.2f",productWithQuantity.getProduct().getSalesPrice());
        String finalSalesPrice = String.format("%.2f",productWithQuantity.getProduct().getSalesPrice()*productWithQuantity.getQuantity());
        String rupeeSymbol = "\u20B9";

        holder.productNameTextView.setText(productWithQuantity.getProduct().getProductName());

        holder.rateQtyTextView.setText(rupeeSymbol + salesPrice + " X " + productWithQuantity.getQuantity() + " = ");
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

        holder.taxTextInNewInvoiceTextView.setText("Tax(" + String.format("%.2f",productWithQuantity.getProduct().getTAX()) + "%)");
        holder.taxValueInNewInvoiceTextView.setText(rupeeSymbol + String.format("%.2f",
                                                                (priceAfterDiscount
                                                                * productWithQuantity.getQuantity()
                                                                * productWithQuantity.getProduct().getTAX())/100d));
//        holder.productQuantityEditText.setText(""+productWithQuantity.getQuantity());
//        holder.finalSalesPriceTextView.setText(rupeeSymbol + " " + finalSalesPrice);

    }

    @Override
    public int getItemCount() {

        return ProductList.size();
    }

    public interface OnClickListener {
        void onItemClick(ProductWithQuantity product);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView salesPriceTextView;
        public TextView rateQtyTextView;
        public TextView discountTextInNewInvoiceTextView;
        public TextView discountValueInNewInvoiceTextView;
        public TextView taxTextInNewInvoiceTextView;
        public TextView taxValueInNewInvoiceTextView;

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

            myView = itemView;
        }

        public void bind(final ProductWithQuantity product) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
        }
    }
}
