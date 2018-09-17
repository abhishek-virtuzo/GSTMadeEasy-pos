package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 12/11/2017.
 */

public class Payment {
    private long PaymentID;
    private String InvoiceNumber;
    private String ReceiptNumber;
    private String PaymentMode;
    private double Amount;
    private String ReferenceNumber;
    private int PaymentTypeID;
    private String CreditCardNumber;
    private String CreditCardType;
    private int synced = 0;
    private String PaymentDtTm;
    // 0 - new payment for unsynced invoice
    // 1 - synced payment
    // 2 = new payment for synced invoice

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getInvoiceNumber() + "--");
        builder.append(getReceiptNumber() + "--");
        builder.append(getPaymentMode() + "--");
        builder.append(getAmount() + "--");
        builder.append(getReferenceNumber());
        return builder.toString();
    }

    public long getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(long paymentID) {
        PaymentID = paymentID;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public String getReceiptNumber() {
        return ReceiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        ReceiptNumber = receiptNumber;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getReferenceNumber() {
        return ReferenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        ReferenceNumber = referenceNumber;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getPaymentTypeID() {
        return PaymentTypeID;
    }

    public void setPaymentTypeID(int paymentTypeID) {
        PaymentTypeID = paymentTypeID;
    }

    public String getCreditCardNumber() {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        CreditCardNumber = creditCardNumber;
    }

    public String getCreditCardType() {
        return CreditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        CreditCardType = creditCardType;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public String getPaymentDtTm() {
        return PaymentDtTm;
    }

    public void setPaymentDtTm(String paymentDtTm) {
        PaymentDtTm = paymentDtTm;
    }

}
