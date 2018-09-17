package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 12/4/2017.
 */

public class TransactionSales {

    double SalesSubID;
    double SalesID;
    double SalesPersonID;
    int ItemTypeID;
    long ItemID;
    int Qty;
    String RefNumber;
    double BaseAmount;
    double Discount;
    double TaxAmount;
    double TotalAmount;
    double SalesCommission;
    int CommissionTypeID;
    double Outlet_SalesSubID;
    int DiscountType; // 10 for %  & 11 for Rs
    double CGST;
    double SGST;
    double IGST;
    double CESS;
    String ProductCode;
    String ItemName;
    double TaxRate;
    int synced = 1;

    // for group products
    long GroupID = 0;

    public double getSalesSubID() {
        return SalesSubID;
    }

    public void setSalesSubID(double salesSubID) {
        SalesSubID = salesSubID;
    }

    public double getSalesID() {
        return SalesID;
    }

    public void setSalesID(double salesID) {
        SalesID = salesID;
    }

    public double getSalesPersonID() {
        return SalesPersonID;
    }

    public void setSalesPersonID(double salesPersonID) {
        SalesPersonID = salesPersonID;
    }

    public int getItemTypeID() {
        return ItemTypeID;
    }

    public void setItemTypeID(int itemTypeID) {
        ItemTypeID = itemTypeID;
    }

    public long getItemID() {
        return ItemID;
    }

    public void setItemID(long itemID) {
        ItemID = itemID;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public String getRefNumber() {
        return RefNumber;
    }

    public void setRefNumber(String refNumber) {
        RefNumber = refNumber;
    }

    public double getBaseAmount() {
        return BaseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        BaseAmount = baseAmount;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        TaxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getSalesCommission() {
        return SalesCommission;
    }

    public void setSalesCommission(double salesCommission) {
        SalesCommission = salesCommission;
    }

    public int getCommissionTypeID() {
        return CommissionTypeID;
    }

    public void setCommissionTypeID(int commissionTypeID) {
        CommissionTypeID = commissionTypeID;
    }

    public double getOutlet_SalesSubID() {
        return Outlet_SalesSubID;
    }

    public void setOutlet_SalesSubID(double outlet_SalesSubID) {
        Outlet_SalesSubID = outlet_SalesSubID;
    }

    public int getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(int discountType) {
        DiscountType = discountType;
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

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public double getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(double taxRate) {
        TaxRate = taxRate;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public double getDiscountAmount() {
        if(DiscountType == 11) {
            return this.getDiscount() * this.getQty();
        } else {
            return (this.getBaseAmount() * this.getDiscount() * this.getQty()) / 100;
        }
    }

    public double getSalesPriceWithDiscount() {
        return getBaseAmount() - (getDiscountAmount() / getQty());
    }

    public double getUnitTaxRate() {
        return (this.getTaxAmount() * 100) / (this.getTotalAmount() * this.getQty());
    }

    public double getUnitTaxAmount() {
        return getTaxAmount() / getQty();
    }

    // for sales return only
    double salesReturnUnitPrice = 0.0;

    public double getSalesReturnUnitPrice() {
        return salesReturnUnitPrice;
    }

    public void setSalesReturnUnitPrice(double salesReturnUnitPrice) {
        this.salesReturnUnitPrice = salesReturnUnitPrice;
    }

    public long getGroupID() {
        return GroupID;
    }

    public void setGroupID(long groupID) {
        GroupID = groupID;
    }
}
