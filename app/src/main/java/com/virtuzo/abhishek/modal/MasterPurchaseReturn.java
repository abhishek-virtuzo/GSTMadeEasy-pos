package com.virtuzo.abhishek.modal;

/**
 * Created by AbhishekAggarwal on 3/23/2018.
 */

public class MasterPurchaseReturn {

    private long PurchaseReturnID;
    private String PurchaseReturnNumber;
    private String InvoiceNumber; // Purchase Inv.No.
    private String PurchaseReturnDate;
    private double CreatedBy; // CreatedBy - LoginID
    private String CreatedDtTm;
    private double OutletID;
    private double CGST;
    private double SGST;
    private double IGST;
    private double CESS;
    private double SubTotal;
    private double DebitAmount;
    private String SupplierID;
    private String SupplierName;
    private String SupplierEmail;
    private String SupplierMobile;
    private String Notes;
    private long PurchaseID = 0;
    private int synced = 0;

    public long getPurchaseReturnID() {
        return PurchaseReturnID;
    }

    public void setPurchaseReturnID(long purchaseReturnID) {
        PurchaseReturnID = purchaseReturnID;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(String supplierID) {
        SupplierID = supplierID;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public double getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(double createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDtTm() {
        return CreatedDtTm;
    }

    public void setCreatedDtTm(String createdDtTm) {
        CreatedDtTm = createdDtTm;
    }

    public double getOutletID() {
        return OutletID;
    }

    public void setOutletID(double outletID) {
        OutletID = outletID;
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

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double subTotal) {
        SubTotal = subTotal;
    }

    public double getDebitAmount() {
        return DebitAmount;
    }

    public void setDebitAmount(double debitAmount) {
        DebitAmount = debitAmount;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getSupplierEmail() {
        return SupplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        SupplierEmail = supplierEmail;
    }

    public String getSupplierMobile() {
        return SupplierMobile;
    }

    public void setSupplierMobile(String supplierMobile) {
        SupplierMobile = supplierMobile;
    }

    public String getPurchaseReturnNumber() {
        return PurchaseReturnNumber;
    }

    public void setPurchaseReturnNumber(String purchaseReturnNumber) {
        PurchaseReturnNumber = purchaseReturnNumber;
    }

    public long getPurchaseID() {
        return PurchaseID;
    }

    public void setPurchaseID(long purchaseID) {
        PurchaseID = purchaseID;
    }

    public String getPurchaseReturnDate() {
        return PurchaseReturnDate;
    }

    public void setPurchaseReturnDate(String purchaseReturnDate) {
        PurchaseReturnDate = purchaseReturnDate;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}
