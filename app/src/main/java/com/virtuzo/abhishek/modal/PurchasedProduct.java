package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/2/2018.
 */

public class PurchasedProduct {
    private Product product;
    private double unitPrice = 0.0d;
    private int quantity = 0;
    private double CGST = 0;
    private double SGST = 0;
    private double IGST = 0;
    private double CESS = 0;

    public PurchasedProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCGST() {
        return CGST;
    }

    public void setCGST(double CGST) {
        this.CGST = CGST;
    }

    public double getSGST() {
        return SGST;
    }

    public void setSGST(double SGST) {
        this.SGST = SGST;
    }

    public double getIGST() {
        return IGST;
    }

    public void setIGST(double IGST) {
        this.IGST = IGST;
    }

    public double getCESS() {
        return CESS;
    }

    public void setCESS(double CESS) {
        this.CESS = CESS;
    }

    public double getTotalTax() {
        return getCGST() + getSGST() + getIGST() + getCESS();
    }

    public void incrementQuantity() {
        quantity++;
    }

    public double getTotalAmount() {
        return (getUnitPrice()*getQuantity()) + getTotalTax() ;
    }
}
