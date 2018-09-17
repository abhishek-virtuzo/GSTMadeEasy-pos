package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/14/2018.
 */

public class ProductCategory {

//        "ProductCategoryId" : 69
//        "ParentCategoryID" : 0,
//        "ProductCategoryName" : Raw Material,
//        "IsProductCategoryActive" : true,
//        "CreatedBy" : 7,
//        "CreatedDtTm" : \/Date(1499387220000)\/,
//        "OutletID" : 5,
//        "ModifiedDtTm" : \/Date(1499387220000)\/,
//        "ModifiedBy" : ,

    private int ProductCategoryId;
    private int ParentCategoryID = 0;
    private String ProductCategoryName;
    private boolean IsProductCategoryActive = true;
    private int CreatedBy;
    private String CreatedDtTm;
    private int OutletID;
    private int synced = 0;

    public int getProductCategoryId() {
        return ProductCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        ProductCategoryId = productCategoryId;
    }

    public int getParentCategoryID() {
        return ParentCategoryID;
    }

    public void setParentCategoryID(int parentCategoryID) {
        ParentCategoryID = parentCategoryID;
    }

    public String getProductCategoryName() {
        return ProductCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        ProductCategoryName = productCategoryName;
    }

    public boolean isProductCategoryActive() {
        return IsProductCategoryActive;
    }

    public void setProductCategoryActive(boolean productCategoryActive) {
        IsProductCategoryActive = productCategoryActive;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDtTm() {
        return CreatedDtTm;
    }

    public void setCreatedDtTm(String createdDtTm) {
        CreatedDtTm = createdDtTm;
    }

    public int getOutletID() {
        return OutletID;
    }

    public void setOutletID(int outletID) {
        OutletID = outletID;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    @Override
    public String toString() {
        return this.getProductCategoryName();
    }
}
