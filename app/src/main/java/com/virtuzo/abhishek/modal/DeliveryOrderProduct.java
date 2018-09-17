package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/2/2018.
 */

public class DeliveryOrderProduct {
    private Product product;
    private int quantity = 0;

    public DeliveryOrderProduct(Product product) {
        this.product = product;
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

    public void incrementQuantity() {
        quantity++;
    }
}
