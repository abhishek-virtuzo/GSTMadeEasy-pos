package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/5/2018.
 */

public class SalesReturnProduct {

    private TransactionSales transactionSales;
    private double returnPrice;
    private int returnQuantity;
    private double returnTax;
    private double returnTaxRate;
    private double returnAmount;
    private int returnType = 0; // 0 - product return, 1 - change price only

    public SalesReturnProduct(TransactionSales transactionSales) {
        this.transactionSales = transactionSales;
        this.returnPrice = transactionSales.getSalesReturnUnitPrice();
        this.returnQuantity = 0;
        this.returnTax = 0d;
        this.returnTaxRate = transactionSales.getTaxRate();
    }

    public TransactionSales getTransactionSales() {
        return transactionSales;
    }

    public void setTransactionSales(TransactionSales transactionSales) {
        this.transactionSales = transactionSales;
    }

    public double getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(double returnPrice) {
        this.returnPrice = returnPrice;
    }

    public int getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(int returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public double getReturnTax() {
        return returnTax;
    }

    public void setReturnTax(double returnTax) {
        this.returnTax = returnTax;
    }

    public double getReturnTaxRate() {
        return returnTaxRate;
    }

    public void setReturnTaxRate(double returnTaxRate) {
        this.returnTaxRate = returnTaxRate;
    }

    public double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }
}
