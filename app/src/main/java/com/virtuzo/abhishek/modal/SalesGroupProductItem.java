package com.virtuzo.abhishek.modal;

/**
 * Created by virtuzo on 6/15/2018.
 */

public class SalesGroupProductItem {

    private GroupProductItem groupProductItem;
    private int newQuantity;
    private double unitPrice;

    private boolean IsTaxApplicable = true;

    public SalesGroupProductItem(GroupProductItem groupProductItem) {
        this.groupProductItem = groupProductItem;
        newQuantity = groupProductItem.getQuantity();
        unitPrice = groupProductItem.getPrice() / groupProductItem.getQuantity();
    }

    public GroupProductItem getGroupProductItem() {
        return groupProductItem;
    }

    public void setGroupProductItem(GroupProductItem groupProductItem) {
        this.groupProductItem = groupProductItem;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setTaxApplicable(boolean taxApplicable) {
        IsTaxApplicable = taxApplicable;
    }

    public double getTaxPercentage() {
        if (IsTaxApplicable) {
            return groupProductItem.getTax();
        } else {
            return 0.0;
        }
    }

    public double getTaxAmount() {
            return (getUnitPrice() * getTaxPercentage()) / 100;
    }

}
