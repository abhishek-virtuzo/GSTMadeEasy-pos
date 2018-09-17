package com.virtuzo.abhishek.modal;

/**
 * Created by Aman Bansal on 20-12-2017.
 */

public class GSTCategory {

    private int ID;
    private String CategoryName;

    public static final int GST_REGISTERED=1, GST_UNREGISTERED=2, COMPOSITE_SCHEME=3, SEZ=4, IMPORTER=5, EXPORTER=6;

    public GSTCategory() {
    }

    public GSTCategory(int ID, String categoryName) {
        this.ID = ID;
        CategoryName = categoryName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    @Override
    public String toString() {
        return getCategoryName();
    }
}
