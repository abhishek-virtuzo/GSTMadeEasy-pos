package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 11/23/2017.
 */

public class Supplier {

//    CREATE TABLE [dbo].[mSupplier](
//            [SupplierId] [int] IDENTITY(1,1) NOT NULL,
//[SupplierName] [nvarchar](50) NULL,
//            [ContactPerson] [nvarchar](50) NULL,
//            [Phone] [nvarchar](50) NULL,
//            [Mobile] [nvarchar](50) NULL,
//            [EmailId] [nvarchar](50) NULL,
//            [IsActive] [bit] NULL,
//            [CreatedBy] [int] NULL,
//            [CreatedDtTm] [smalldatetime] NULL,
//            [ModifiedBy] [int] NULL,
//            [ModifiedDtTm] [smalldatetime] NULL,
//            [BusinessID] [bigint] NULL,
//            [EntityType] [varchar](100) NULL,
//            [EntityName] [varchar](100) NULL,
//            [GSTNumber] [varchar](100) NULL,
//            [StateName] [int] NULL,
//            [CityName] [varchar](100) NULL,
//            [zipCode] [varchar](100) NULL,
//            [Addresss] [varchar](100) NULL,
//            [CategoryID] [int] NULL,
//            [StateID] [int] NULL,
//

//    {"Response":[{"Response":"Success","Responsecode":"0"}],"Data":[{
// "ContactLandline":"","ContactNumber":"1223343333","CategoryName":"GST Unregistered",
// "StateID":3,"ContactEmailId":"","ContactPerson":"Dharma","CategoryID":2,"EntityType":"Ltd.",
// "EntityName":"Dharma u0026 Sons","Addresss":"","GSTNumber":"","SupplierId":2047,"StateName":null}]}
    private String SupplierId;
    private String EntityType;
    private String EntityName;
    private String CategoryName;
    private String ContactPerson;
    private String ContactNumber;
    private String ContactEmailId;
    private String ContactLandline;
    private String GSTNumber;
    private String Addresss;
    private String StateName = ""; // Outlet StateName
    private String Gender = ""; // ""
    private String CityName = ""; // ""
    private String PinCode = ""; // ""
    private int synced = 0;
    private int CategoryID; // 2
    private int StateId; // Outlet ID

//    {"Response":[{"Response":"Success","Responsecode":"0"}],"Data":[{
// "ContactLandline":"","ContactNumber":"1223343333","CategoryName":"GST Unregistered",
// "StateID":3,"ContactEmailId":"","ContactPerson":"Dharma","CategoryID":2,"EntityType":"Ltd.",
// "EntityName":"Dharma u0026 Sons","Addresss":"","GSTNumber":"","SupplierId":2047,"StateName":null}]}

    public Supplier() {
    }

    public String getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(String supplierId) {
        SupplierId = supplierId;
    }

    public String getEntityType() {
        return EntityType;
    }

    public void setEntityType(String entityType) {
        EntityType = entityType;
    }

    public String getEntityName() {
        return EntityName;
    }

    public void setEntityName(String entityName) {
        EntityName = entityName;
    }

    public int getCategoryId() {
        return CategoryID;
    }

    public void setCategoryId(int categoryId) {
        CategoryID = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getContactEmailId() {
        return ContactEmailId;
    }

    public void setContactEmailId(String contactEmailId) {
        ContactEmailId = contactEmailId;
    }

    public String getContactLandline() {
        return ContactLandline;
    }

    public void setContactLandline(String contactLandline) {
        ContactLandline = contactLandline;
    }

    public String getGSTNumber() {
        return GSTNumber;
    }

    public void setGSTNumber(String GSTNumber) {
        this.GSTNumber = GSTNumber;
    }

    public String getAddress() {
        return Addresss;
    }

    public void setAddress(String address) {
        Addresss = address;
    }

    public int getStateId() {
        return StateId;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getCity() {
        return CityName;
    }

    public void setCity(String city) {
        CityName = city;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}
