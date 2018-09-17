package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 12/12/2017.
 */

public class StockLedger {
    long StockLedgerID;
    long ProductID;
    String ReferenceNumber;
    String TransactionType;
    String DateOfTransaction;
    int Quantity;
    String DateOfCreation;
    int InOut;

    public long getStockLedgerID() {
        return StockLedgerID;
    }

    public void setStockLedgerID(long stockLedgerID) {
        StockLedgerID = stockLedgerID;
    }

    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        ProductID = productID;
    }

    public String getReferenceNumber() {
        return ReferenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        ReferenceNumber = referenceNumber;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getDateOfTransaction() {
        return DateOfTransaction;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        DateOfTransaction = dateOfTransaction;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getDateOfCreation() {
        return DateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        DateOfCreation = dateOfCreation;
    }

    public int getInOut() {
        return InOut;
    }

    public void setInOut(int inOut) {
        InOut = inOut;
    }
}
