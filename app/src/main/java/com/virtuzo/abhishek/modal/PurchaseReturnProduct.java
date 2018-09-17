package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/5/2018.
 */

public class PurchaseReturnProduct {

    private TransactionPurchase transactionPurchase;
    private double returnPrice;
    private int returnQuantity;
    private double returnAmount;
//    private int returnType = 0; // 0 - product return, 1 - change price only

    public PurchaseReturnProduct(TransactionPurchase transactionPurchase) {
        this.transactionPurchase = transactionPurchase;
        this.returnPrice = transactionPurchase.getUnitPrice();
        this.returnQuantity = transactionPurchase.getQuantity();
    }

    public TransactionPurchase getTransactionPurchase() {
        return transactionPurchase;
    }

    public void setTransactionPurchase(TransactionPurchase transactionPurchase) {
        this.transactionPurchase = transactionPurchase;
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

    public double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public double getReturnCGST() {
        return (getReturnQuantity()*getTransactionPurchase().getiCGST())/getTransactionPurchase().getQuantity();
    }

    public double getReturnSGST() {
        return (getReturnQuantity()*getTransactionPurchase().getiSGST())/getTransactionPurchase().getQuantity();
    }

    public double getReturnIGST() {
        return (getReturnQuantity()*getTransactionPurchase().getiIGST())/getTransactionPurchase().getQuantity();
    }

    public double getReturnCESS() {
        return (getReturnQuantity()*getTransactionPurchase().getiCESS())/getTransactionPurchase().getQuantity();
    }

    public double getOriginalTotalTax() {
        return getTransactionPurchase().getiCGST()
                + getTransactionPurchase().getiSGST()
                + getTransactionPurchase().getiIGST()
                + getTransactionPurchase().getiCESS();
    }

    public double getReturnTotalTax() {
        return getReturnCGST() + getReturnSGST() + getReturnIGST() + getReturnCESS();
    }

}
