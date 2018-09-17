package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/14/2018.
 */

public class Brand {

//            "BrandID" : 24,
//            "Brand" : Dettol12345,
//            "IsBrandActive" : true,
//            "CreatedDtTm" : \/Date(1499386980000)\/,
//            "ModifiedDtTm" : \/Date(1499386980000)\/,
//            "ModifiedBy" : 7,
//            "BusinessID" : 1,
//            "CreatedBy" : 7,
//            "IsOwnedBrand" : true

    private int BrandID;
    private String Brand;
    private boolean isBrandActive = true;
    private int CreatedBy;
    private String CreatedDtTm;
    private int synced = 0;

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

    public boolean isBrandActive() {
        return isBrandActive;
    }

    public void setBrandActive(boolean brandActive) {
        isBrandActive = brandActive;
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

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    @Override
    public String toString() {
        return this.getBrand();
    }
}
