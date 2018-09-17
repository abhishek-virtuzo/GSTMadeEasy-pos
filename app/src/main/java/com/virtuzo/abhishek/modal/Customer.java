package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 11/23/2017.
 */

public class Customer {

//    {"CustomerID":10042,"EntityType":"Propritership","EntityName":"Shadab","CategoryName":"GST Registered","ContactPerson":null,"ContactLandline":null,
// "ContactNumber":"8010501336","ContactEmailId":"shadab.a@virtuzo.in","GSTNumber":"","Address":""}
//{"Response":"Success","Responsecode":"0"}],"Data":[{"ContactLandline":null,"Age":null,"DOB":"\/Date(1028271600000)\/",
// "CityName":"","EntityName":"","IsSynced":null,"PreviousBalance":-1852.15,"IsActive":true,"StateId":14,"zipCode":"",
// "GSTNumber":"","LandlineNumber":null,"CustomerType":"N","ContactNumber":"8010501336","ContactPerson":null,
// "AnniversaryDate":"\/Date(1499324400000)\/","ModifiedBy":null,"CustomerName":"Shadab","CreatedDtTm":"\/Date(1499457480000)\/",
// "TabCustID":null,"CategoryID":1,"ModifiedDtTm":"\/Date(1499457480000)\/","Gender":"M","Address":"","CustomerID":10042,
// "CreatedBy":7,"EntityType":"Propritership","EmailID":"shadab.a@virtuzo.in"}


    private String CustomerId = ""; // 0
    private String EntityType = ""; // Individual
    private String EntityName = ""; // Guest
    private int CategoryId; // 2
    private String CategoryName = ""; // Unregistered
    private String ContactPerson = ""; // Guest
    private String ContactNumber = ""; // ""
    private String ContactEmailId = ""; // ""
    private String ContactLandline = ""; // ""
    private String GSTNumber = ""; // ""
    private String Address = ""; // ""
    private int StateId; // Outlet ID
    private String StateName = ""; // Outlet StateName
    private String Gender = "N"; // ""
    private String City = ""; // ""
    private String PinCode = ""; // ""
    private int synced = 0;

//    {"PinCode":"","ContactLandline":null,"CustomerType":"N","Age":null,"ContactNumber":"8010501336",
// "CategoryName":"GST Registered","ContactPerson":null,"DOB":"\/Date(1028271600000)\/","EntityName":"",
// "CustomerName":"Shadab","City":"","StateName":"Delhi","PreviousBalance":-1852.15,"CategoryId":1,
// "ContactEmailId":"shadab.a@virtuzo.in","CustomerId":10042,"StateId":14,"Gender":"M","Address":"",
// "EntityType":"Propritership","GSTNumber":""}

    public Customer() {
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
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
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
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
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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
        return City;
    }

    public void setCity(String city) {
        City = city;
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

    public static Customer getGuestCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId("0");
        customer.setEntityType("Individual");
        customer.setEntityName("Guest");
        customer.setContactPerson("Guest");
        customer.setCategoryId(GSTCategory.GST_UNREGISTERED);
        customer.setCategoryName("GST Unregistered");
        return customer;
    }

}
