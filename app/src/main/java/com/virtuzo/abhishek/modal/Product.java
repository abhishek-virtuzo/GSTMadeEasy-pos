package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 11/21/2017.
 */

public class Product {

    //    {"TAX":18,"Barcode":"123","Brand":"MINT","HSNCode":"1011010","ProductCode":"1234","BalanceQty":1,"ProductID":164,
// "PriceGSTInclusive":false,"SalesPrice":575,"BrandID":27,"isProductActive":true,"ProductName":"melafade cream","UnitID":1,"HSNID":1}
    private long ProductID;
    private String ProductCode;
    private String ProductName;
    private int BrandID;
    private String Brand;
    private String Barcode;
    private double SalesPrice;
    private boolean PriceGSTInclusive = false;
    private boolean isProductActive = true;
    private String HSNCode;
    private int HSNID;
    private int UnitID;
    private String Unit;
    private double TAX;
    private double BalanceQty = 0;
    private long ProductCategoryId;
    private long ProductSubCategoryId;
    private boolean isTaxApplicable = true;
    private String Description;
    private String ProductSubCategoryName; // CreatedBy,CreatedDtTm,OutletID
    private int CreatedBy;
    private String CreatedDtTm;
    private int OutletID;

    private int IsGroupProduct = 0;

    private int synced = 0;

//    {"TAX":18,"Barcode":"","Description":"","ProductDiscount":0,"ProductCode":"sdfh3265256","ModifiedBy":0,
// "ProductID":177,"ProductCategoryId":71,"IsProductActive":true,"LoyalityPoints":0,"PriceGSTInclusive":false,
// "SalesPrice":0,"HSCID":3,"BrandID":141,"CreatedDtTm":"\/Date(1514879520000)\/","SalesCommissionUnit":"2",
// "ModifiedDtTm":"\/Date(1514879520000)\/","ReorderLevel":0,"CreatedBy":7,"ProductSubCategoryId":0,
// "UnitCostPrice":0,"ProductName":"dynamic2","UnitID":2,"SalesCommission":0,"NeedRefill":false}

    public Product() {
    }

    public Product(long productID, String productCode, String productName, int brandID, String brand, String barcode, double salesPrice, boolean priceGSTInclusive, boolean isProductActive, String HSNCode, int HSNID, int unitID, String unit, double TAX, double balanceQty) {
        ProductID = productID;
        ProductCode = productCode;
        ProductName = productName;
        BrandID = brandID;
        Brand = brand;
        Barcode = barcode;
        SalesPrice = salesPrice;
        PriceGSTInclusive = priceGSTInclusive;
        this.isProductActive = isProductActive;
        this.HSNCode = HSNCode;
        this.HSNID = HSNID;
        UnitID = unitID;
        Unit = unit;
        this.TAX = TAX;
        BalanceQty = balanceQty;
    }

    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        ProductID = productID;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getBrandID() {
        return BrandID;
    }

    public void setBrandID(int brandID) {
        BrandID = brandID;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public double getSalesPrice() {
        return SalesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        SalesPrice = salesPrice;
    }

    public boolean isPriceGSTInclusive() {
        return PriceGSTInclusive;
    }

    public void setPriceGSTInclusive(boolean priceGSTInclusive) {
        PriceGSTInclusive = priceGSTInclusive;
    }

    public boolean isProductActive() {
        return isProductActive;
    }

    public void setProductActive(boolean productActive) {
        isProductActive = productActive;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

    public int getHSNID() {
        return HSNID;
    }

    public void setHSNID(int HSNID) {
        this.HSNID = HSNID;
    }

    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public double getTAX() {
        if(this.isTaxApplicable()) {
            return TAX;
        }
        else {
            return 0d;
        }
    }

    public void setTAX(double TAX) {
        this.TAX = TAX;
    }

    public double getBalanceQty() {
        return BalanceQty;
    }

    public void setBalanceQty(double balanceQty) {
        BalanceQty = balanceQty;
    }

    public boolean isTaxApplicable() {
        return isTaxApplicable;
    }

    public void setTaxApplicable(boolean taxApplicable) {
        isTaxApplicable = taxApplicable;
    }

    public long getProductCategoryId() {
        return ProductCategoryId;
    }

    public void setProductCategoryId(long productCategoryId) {
        ProductCategoryId = productCategoryId;
    }

    public long getProductSubCategoryId() {
        return ProductSubCategoryId;
    }

    public void setProductSubCategoryId(long productSubCategoryId) {
        ProductSubCategoryId = productSubCategoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProductSubCategoryName() {
        return ProductSubCategoryName;
    }

    public void setProductSubCategoryName(String productSubCategoryName) {
        ProductSubCategoryName = productSubCategoryName;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public int getOutletID() {
        return OutletID;
    }

    public void setOutletID(int outletID) {
        OutletID = outletID;
    }

    public String getCreatedDtTm() {
        return CreatedDtTm;
    }

    public void setCreatedDtTm(String createdDtTm) {
        CreatedDtTm = createdDtTm;
    }

    public int getIsGroupProduct() {
        return IsGroupProduct;
    }

    public void setIsGroupProduct(int isGroupProduct) {
        IsGroupProduct = isGroupProduct;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public double getTotalAmountWithTAX() {
        return this.getSalesPrice() + ((this.getSalesPrice() * this.getTAX()) / 100d);
    }

}
