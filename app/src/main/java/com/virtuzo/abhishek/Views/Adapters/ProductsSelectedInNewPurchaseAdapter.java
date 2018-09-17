package com.virtuzo.abhishek.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.PurchasedProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Aggarwal on 22/11/2017.
 */

public class ProductsSelectedInNewPurchaseAdapter extends RecyclerView.Adapter<ProductsSelectedInNewPurchaseAdapter.MyViewHolder> {

    private List<PurchasedProduct> ProductList;
    private ProductsSelectedInNewPurchaseAdapter.OnClickListener listener;

    public ProductsSelectedInNewPurchaseAdapter(ArrayList<PurchasedProduct> stateList, ProductsSelectedInNewPurchaseAdapter.OnClickListener onClickListener) {
        this.ProductList = stateList;
        this.listener = onClickListener;
    }

    @Override
    public ProductsSelectedInNewPurchaseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_selected_new_purchase_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductsSelectedInNewPurchaseAdapter.MyViewHolder holder, final int position) {

        PurchasedProduct purchasedProduct = ProductList.get(position);
        holder.bind(purchasedProduct);

        String salesPrice = String.format("%.2f",purchasedProduct.getUnitPrice());
        String finalSalesPrice = String.format("%.2f",purchasedProduct.getUnitPrice()*purchasedProduct.getQuantity());
        String rupeeSymbol = "\u20B9";
        double totalTax = purchasedProduct.getCGST() + purchasedProduct.getSGST() + purchasedProduct.getIGST() + purchasedProduct.getCESS();
        String totalTaxString = String.format("%.2f", totalTax);

        holder.productNameTextView.setText(purchasedProduct.getProduct().getProductName());

        holder.rateQtyTextView.setText(rupeeSymbol + salesPrice + " X " + purchasedProduct.getQuantity() + " = ");
        holder.salesPriceTextView.setText(rupeeSymbol + finalSalesPrice);
        holder.totalTaxTextView.setText(rupeeSymbol + totalTaxString);

//        holder.discountTextInNewInvoiceTextView.setText("Discount(" + String.format("%.2f",
//                                                                (productWithQuantity.getDiscount()
//                                                                / productWithQuantity.getProduct().getSalesPrice())
//                                                                *100d) + "%)");
//        holder.discountValueInNewInvoiceTextView.setText("- " + rupeeSymbol
//                                                                + String.format("%.2f",productWithQuantity.getDiscount()
//                                                                * productWithQuantity.getQuantity()));

//        double priceAfterDiscount = purchasedProduct.getProduct().getSalesPrice()
//                                                                - purchasedProduct.getDiscount();

//        holder.taxTextInNewInvoiceTextView.setText("Tax(" + String.format("%.2f",productWithQuantity.getProduct().getTAX()) + "%)");
//        holder.taxValueInNewInvoiceTextView.setText(rupeeSymbol + String.format("%.2f",
//                                                                (priceAfterDiscount
//                                                                * productWithQuantity.getQuantity()
//                                                                * productWithQuantity.getProduct().getTAX())/100d));
//        holder.productQuantityEditText.setText(""+productWithQuantity.getQuantity());
//        holder.finalSalesPriceTextView.setText(rupeeSymbol + " " + finalSalesPrice);

    }

    @Override
    public int getItemCount() {

        return ProductList.size();
    }

    public interface OnClickListener {
        void onItemClick(PurchasedProduct product);
        void onCancelItem(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView salesPriceTextView;
        public ImageButton productCancelButton;
        public TextView rateQtyTextView;
        public TextView totalTaxTextView;
//        public TextView discountTextInNewInvoiceTextView;
//        public TextView discountValueInNewInvoiceTextView;
//        public TextView taxTextInNewInvoiceTextView;
//        public TextView taxValueInNewInvoiceTextView;

        public View myView;

        public MyViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.selectedProductNameInNewInvoiceTextView);
            salesPriceTextView = (TextView) itemView.findViewById(R.id.selectedProductSalesPriceInNewInvoiceTextView);
            rateQtyTextView = (TextView) itemView.findViewById(R.id.rateQtyInNewInvoiceTextView); // selectedProductTotalTaxInNewInvoiceTextView
            totalTaxTextView = (TextView) itemView.findViewById(R.id.selectedProductTotalTaxInNewInvoiceTextView);
//            discountTextInNewInvoiceTextView = (TextView) itemView.findViewById(R.id.discountTextInNewInvoiceTextView);
//            discountValueInNewInvoiceTextView = (TextView) itemView.findViewById(R.id.discountValueInNewInvoiceTextView);
//            taxTextInNewInvoiceTextView = (TextView) itemView.findViewById(R.id.taxTextInNewInvoiceTextView);
//            taxValueInNewInvoiceTextView = (TextView) itemView.findViewById(R.id.taxValueInNewInvoiceTextView);

            productCancelButton = (ImageButton) itemView.findViewById(R.id.selectedProductCancelButton);
            productCancelButton.setMinimumWidth(productCancelButton.getHeight());
            productCancelButton.setMaxWidth(productCancelButton.getHeight());

            myView = itemView;
        }

        public void bind(final PurchasedProduct product) {
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
