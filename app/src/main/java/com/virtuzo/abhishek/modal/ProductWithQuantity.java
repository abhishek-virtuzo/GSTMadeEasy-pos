package com.virtuzo.abhishek.modal;

import java.util.ArrayList;

/**
 * Created by Abhishek Aggarwal on 11/29/2017.
 */

public class ProductWithQuantity {
    Product product;
    int quantity = 1;
    double discount = 0.0d; // in rupees
    int discountType = 0;

    boolean isGroupProductsShown = false;
    ArrayList<SalesGroupProductItem> salesGroupProductItems;

    public ProductWithQuantity(Product product) {
        this.product = product;
        salesGroupProductItems = new ArrayList<>();
    }

    public ProductWithQuantity(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public double getDiscountAmount() {
        if(discountType == 0) {
            return discount;
        } else {
            return (product.getSalesPrice() * (discount)) / 100;
        }
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void incrementQuantity() {
        if (product.getIsGroupProduct() == 1) {
            for (SalesGroupProductItem salesGroupProductItem : salesGroupProductItems) {
                salesGroupProductItem.setNewQuantity(salesGroupProductItem.getNewQuantity() + salesGroupProductItem.getGroupProductItem().getQuantity());
            }
        } else {
            quantity++;
        }
    }

    public double getTotalAmountWithDiscountAndTAX() {
        return this.getSalesPriceWithDiscount() + this.getTaxAmount();
    }

    public double getSalesPriceWithDiscount() {
        switch (discountType) {
            case 1:
                return (product.getSalesPrice() * (100 - discount)) / 100;
            default:
                return product.getSalesPrice() - discount;
        }
    }

    public double getTaxAmount() {
        return (this.getSalesPriceWithDiscount() * product.getTAX()) / 100;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public ArrayList<SalesGroupProductItem> getSalesGroupProductItems() {
        return salesGroupProductItems;
    }

    public void setSalesGroupProductItems(ArrayList<SalesGroupProductItem> salesGroupProductItems) {
        this.salesGroupProductItems = salesGroupProductItems;
    }

    public boolean isGroupProductsShown() {
        return isGroupProductsShown;
    }

    public void setGroupProductsShown(boolean groupProductsShown) {
        isGroupProductsShown = groupProductsShown;
    }
}
