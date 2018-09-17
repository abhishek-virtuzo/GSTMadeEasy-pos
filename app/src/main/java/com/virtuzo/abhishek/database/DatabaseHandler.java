package com.virtuzo.abhishek.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.virtuzo.abhishek.modal.Brand;
import com.virtuzo.abhishek.modal.CreditDebitNote;
import com.virtuzo.abhishek.modal.Customer;
import com.virtuzo.abhishek.modal.ErrorLog;
import com.virtuzo.abhishek.modal.EwayBill;
import com.virtuzo.abhishek.modal.EwayBillReason;
import com.virtuzo.abhishek.modal.GSTCategory;
import com.virtuzo.abhishek.modal.GroupProductDetailInInvoice;
import com.virtuzo.abhishek.modal.GroupProductItem;
import com.virtuzo.abhishek.modal.HSCCode;
import com.virtuzo.abhishek.modal.LoginUser;
import com.virtuzo.abhishek.modal.MasterDeliveryOrder;
import com.virtuzo.abhishek.modal.MasterPurchase;
import com.virtuzo.abhishek.modal.MasterPurchaseReturn;
import com.virtuzo.abhishek.modal.MasterSales;
import com.virtuzo.abhishek.modal.MasterSalesReturn;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Payment;
import com.virtuzo.abhishek.modal.PaymentMode;
import com.virtuzo.abhishek.modal.Product;
import com.virtuzo.abhishek.modal.ProductCategory;
import com.virtuzo.abhishek.modal.State;
import com.virtuzo.abhishek.modal.StockLedger;
import com.virtuzo.abhishek.modal.Supplier;
import com.virtuzo.abhishek.modal.TransactionDeliveryOrder;
import com.virtuzo.abhishek.modal.TransactionPurchase;
import com.virtuzo.abhishek.modal.TransactionPurchaseReturn;
import com.virtuzo.abhishek.modal.TransactionSales;
import com.virtuzo.abhishek.modal.TransactionSalesReturn;
import com.virtuzo.abhishek.modal.Unit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abhishek Aggarwal on 11/21/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static DatabaseHandler INSTANCE = null;
    public static Context context;

    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 7;

    // Database Name
    public static final String DATABASE_NAME = "gstdb.db";

    // Product table name
    private static final String TABLE_LOGS = "errorlogs";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_PRODUCT_CATEGORY = "productCategory";
    private static final String TABLE_BRANDS = "brands";
    private static final String TABLE_UNITS = "units";
    private static final String TABLE_HSNCODE = "hsnCode";
    private static final String TABLE_CUSTOMERS = "customers";
    private static final String TABLE_SUPPLIERS = "suppliers";
    private static final String TABLE_M_SALES_REGISTER = "mSalesRegister";
    private static final String TABLE_T_SALES_REGISTER = "tSalesRegister";
    private static final String TABLE_EWAY_BILL_NUMBER = "eWayBillNumber";
    private static final String TABLE_EWAY_BILL_REASON = "eWayBillReason";
    private static final String TABLE_PAYMENTS = "payments";
    private static final String TABLE_LAST_SYNCED = "lastSynced";
    private static final String TABLE_STATES = "states";
    private static final String TABLE_GST_CATEGORY = "gstCategory";
    private static final String TABLE_PAYMENT_MODES = "paymentMode";
    private static final String TABLE_STOCKLEDGER = "stockLedger";
    private static final String TABLE_M_PURCHASE_REGISTER = "mPurchaseRegister";
    private static final String TABLE_T_PURCHASE_REGISTER = "tPurchaseRegister";
    private static final String TABLE_M_SALES_RETURN = "mSalesReturn";
    private static final String TABLE_T_SALES_RETURN = "tSalesReturn";
    private static final String TABLE_M_DELIVERY_ORDER = "mDeliveryOrder";
    private static final String TABLE_T_DELIVERY_ORDER = "tDeliveryOrder";
    private static final String TABLE_CREDIT_DEBIT_NOTE = "CreditDebitNote";
    private static final String TABLE_M_PURCHASE_RETURN = "mPurchaseReturn";
    private static final String TABLE_T_PURCHASE_RETURN = "tPurchaseReturn";
    private static final String TABLE_GROUP_PRODUCT_ITEM = "groupProductItem";
    private static final String TABLE_GROUP_PRODUCT_DETAIL_IN_INVOICE = "groupDetailsInInvoice";

    // users
    private static final String USERS_UserID = "UserID"; // 0
    private static final String USERS_UserName = "UserName"; // 0
    private static final String USERS_Password = "Password"; // 0
    private static final int INDEX_USERS_UserID = 0;
    private static final int INDEX_USERS_UserName = 1;
    private static final int INDEX_USERS_Password = 2;

    // Product Table Columns names
    private static final String PRODUCT_ProductID = "ProductID"; // 0
    private static final String PRODUCT_ProductCode = "ProductCode"; // 1
    private static final String PRODUCT_ProductName = "ProductName"; // 2
    private static final String PRODUCT_BrandID = "BrandID"; // 3
    private static final String PRODUCT_Brand = "Brand"; // 4
    private static final String PRODUCT_BarCode = "Barcode"; // 5
    private static final String PRODUCT_SalesPrice = "SalesPrice"; // 6
    private static final String PRODUCT_PriceGSTInclusive = "PriceGSTInclusive"; // 7
    private static final String PRODUCT_IsProductActive = "isProductActive"; // 8
    private static final String PRODUCT_HSNCode = "HSNCode"; // 9
    private static final String PRODUCT_HSNID = "HSNID"; // 10
    private static final String PRODUCT_UnitID = "UnitID"; // 11
    private static final String PRODUCT_Unit = "Unit"; // 12
    private static final String PRODUCT_Tax = "TAX"; // 13
    private static final String PRODUCT_BalanceQty = "BalanceQty"; // 14
    private static final String PRODUCT_ProductCategoryId = "ProductCategoryId"; // 15
    private static final String PRODUCT_ProductSubCategoryId = "ProductSubCategoryId"; // 16
    private static final String PRODUCT_Description = "Description"; // 17
    private static final String PRODUCT_ProductSubCategoryName = "ProductSubCategoryName"; // 18
    private static final String PRODUCT_CreatedBy = "CreatedBy"; // 19
    private static final String PRODUCT_CreatedDtTm = "CreatedDtTm"; // 20
    private static final String PRODUCT_OutletID = "OutletID"; // 21
    private static final String PRODUCT_IsGroupProduct = "IsGroupProduct"; // 22
    private static final String PRODUCT_Synced = "synced"; // 23
    private static final int INDEX_PRODUCT_ProductID = 0;
    private static final int INDEX_PRODUCT_ProductCode = 1;
    private static final int INDEX_PRODUCT_ProductName = 2;
    private static final int INDEX_PRODUCT_BrandID = 3;
    private static final int INDEX_PRODUCT_Brand = 4;
    private static final int INDEX_PRODUCT_BarCode = 5;
    private static final int INDEX_PRODUCT_SalesPrice = 6;
    private static final int INDEX_PRODUCT_PriceGSTInclusive = 7;
    private static final int INDEX_PRODUCT_IsProductActive = 8;
    private static final int INDEX_PRODUCT_HSNCode = 9;
    private static final int INDEX_PRODUCT_HSNID = 10;
    private static final int INDEX_PRODUCT_UnitID = 11;
    private static final int INDEX_PRODUCT_Unit = 12;
    private static final int INDEX_PRODUCT_Tax = 13;
    private static final int INDEX_PRODUCT_BalanceQty = 14;
    private static final int INDEX_PRODUCT_ProductCategoryId = 15;
    private static final int INDEX_PRODUCT_ProductSubCategoryId = 16;
    private static final int INDEX_PRODUCT_Description = 17;
    private static final int INDEX_PRODUCT_ProductSubCategoryName = 18;
    private static final int INDEX_PRODUCT_CreatedBy = 19;
    private static final int INDEX_PRODUCT_CreatedDtTm = 20;
    private static final int INDEX_PRODUCT_OutletID = 21;
    private static final int INDEX_PRODUCT_IsGroupProduct = 22;
    private static final int INDEX_PRODUCT_Synced = 23;
    public static final int PRODUCT_CODE_NotAGroupProduct = 0;
    public static final int PRODUCT_CODE_IsAGroupProduct = 1;
    public static final int PRODUCT_SYNCED_CODE_Unsynced = 0;
    public static final int PRODUCT_SYNCED_CODE_Synced = 1;
    public static final int PRODUCT_SYNCED_CODE_Edit = 2;

    // Product Category
    private static final String PRODUCT_CATEGORY_ProductCategoryId = "ProductCategoryId"; // 0
    private static final String PRODUCT_CATEGORY_ParentCategoryID = "ParentCategoryID"; // 0
    private static final String PRODUCT_CATEGORY_ProductCategoryName = "ProductCategoryName"; // 0
    private static final String PRODUCT_CATEGORY_IsProductCategoryActive = "IsProductCategoryActive"; // 0
    private static final String PRODUCT_CATEGORY_CreatedBy = "CreatedBy"; // 0
    private static final String PRODUCT_CATEGORY_CreatedDtTm = "CreatedDtTm"; // 0
    private static final String PRODUCT_CATEGORY_OutletID = "OutletID"; // 0
    private static final String PRODUCT_CATEGORY_Synced = "synced"; // 0
    private static final int INDEX_PRODUCTCATEGORY_ProductCategoryId = 0;
    private static final int INDEX_PRODUCTCATEGORY_ParentCategoryID = 1;
    private static final int INDEX_PRODUCTCATEGORY_ProductCategoryName = 2;
    private static final int INDEX_PRODUCTCATEGORY_IsProductCategoryActive = 3;
    private static final int INDEX_PRODUCTCATEGORY_CreatedBy = 4;
    private static final int INDEX_PRODUCTCATEGORY_CreatedDtTm = 5;
    private static final int INDEX_PRODUCTCATEGORY_OutletID = 6;
    private static final int INDEX_PRODUCTCATEGORY_Synced = 7;
    public static final int PRODUCTCATEGORY_SYNCED_CODE_Unsynced = 0;
    public static final int PRODUCTCATEGORY_SYNCED_CODE_Synced = 1;

    // brand
    private static final String BRAND_BrandID = "BrandID"; // 0
    private static final String BRAND_Brand = "Brand"; // 0
    private static final String BRAND_isBrandActive = "isBrandActive"; // 0
    private static final String BRAND_CreatedBy = "CreatedBy"; // 0
    private static final String BRAND_CreatedDtTm = "CreatedDtTm"; // 0
    private static final String BRAND_Synced = "synced"; // 0
    private static final int INDEX_BRAND_BrandID = 0;
    private static final int INDEX_BRAND_Brand = 1;
    private static final int INDEX_BRAND_isBrandActive = 2;
    private static final int INDEX_BRAND_CreatedBy = 3;
    private static final int INDEX_BRAND_CreatedDtTm = 4;
    private static final int INDEX_BRAND_Synced = 5;
    public static final int BRAND_SYNCED_CODE_Unsynced = 0;
    public static final int BRAND_SYNCED_CODE_Synced = 1;

    private static final String PRODUCT_UNIT_ID = "ID"; // 0
    private static final String PRODUCT_UNIT_isActive = "isActive"; // 0
    private static final String PRODUCT_UNIT_Unit = "Unit"; // 0
    private static final String PRODUCT_UNIT_Synced = "synced"; // 0
    private static final int INDEX_PRODUCT_UNIT_Id = 0;
    private static final int INDEX_PRODUCT_UNIT_isActive = 1;
    private static final int INDEX_PRODUCT_UNIT_Unit = 2;
    private static final int INDEX_PRODUCT_UNIT_Synced = 3;

    private static final String HSC_ID = "ID"; // 0
    private static final String HSC_HSCCode = "HSCCode"; // 0
    private static final String HSC_Description = "Description"; // 0
    private static final String HSC_CGST = "CGST"; // 0
    private static final String HSC_SGST = "SGST"; // 0
    private static final String HSC_IGST = "IGST"; // 0
    private static final String HSC_CESS = "CESS"; // 0
    private static final String HSC_IsActive = "IsActive"; // 0
    private static final String HSC_CreatedBy = "CreatedBy"; // 0
    private static final String HSC_Condition = "Condition"; // 0
    private static final String HSC_Remarks = "Remarks"; // 0
    private static final String HSC_RedioHSN = "RedioHSN"; // 0
    private static final String HSC_Synced = "synced"; // 0
    private static final int INDEX_HSC_ID = 0;
    private static final int INDEX_HSC_HSCCode = 1;
    private static final int INDEX_HSC_Description = 2;
    private static final int INDEX_HSC_CGST = 3;
    private static final int INDEX_HSC_SGST = 4;
    private static final int INDEX_HSC_IGST = 5;
    private static final int INDEX_HSC_CESS = 6;
    private static final int INDEX_HSC_IsActive = 7;
    private static final int INDEX_HSC_CreatedBy = 8;
    private static final int INDEX_HSC_Condition = 9;
    private static final int INDEX_HSC_Remarks = 10;
    private static final int INDEX_HSC_RedioHSN = 11;
    private static final int INDEX_HSC_Synced = 12;

    // Customer Table Columns names
    public static final String CUSTOMER_CustomerId = "CustomerId"; // 0
    public static final String CUSTOMER_EntityType = "EntityType"; // 1
    public static final String CUSTOMER_EntityName = "EntityName"; // 2
    public static final String CUSTOMER_CategoryId = "CategoryId"; // 3
    public static final String CUSTOMER_CategoryName = "CategoryName"; // 4
    public static final String CUSTOMER_ContactPerson = "ContactPerson"; // 5
    public static final String CUSTOMER_ContactNumber = "ContactNumber"; // 6
    public static final String CUSTOMER_ContactEmailId = "ContactEmailId"; // 7
    public static final String CUSTOMER_ContactLandline = "ContactLandline"; // 8
    public static final String CUSTOMER_GSTNumber = "GSTNumber"; // 9
    public static final String CUSTOMER_Address = "Address"; // 10
    public static final String CUSTOMER_StateId = "StateId"; // 11
    public static final String CUSTOMER_StateName = "StateName"; // 12
    public static final String CUSTOMER_Gender = "Gender"; // 13
    public static final String CUSTOMER_City = "City"; // 14
    public static final String CUSTOMER_PinCode = "PinCode"; // 15
    public static final String CUSTOMER_Synced = "synced"; // 16
    public static final int INDEX_CUSTOMER_CustomerId = 0;
    public static final int INDEX_CUSTOMER_EntityType = 1;
    public static final int INDEX_CUSTOMER_EntityName = 2;
    public static final int INDEX_CUSTOMER_CategoryId = 3;
    public static final int INDEX_CUSTOMER_CategoryName = 4;
    public static final int INDEX_CUSTOMER_ContactPerson = 5;
    public static final int INDEX_CUSTOMER_ContactNumber = 6;
    public static final int INDEX_CUSTOMER_ContactEmailId = 7;
    public static final int INDEX_CUSTOMER_ContactLandline = 8;
    public static final int INDEX_CUSTOMER_GSTNumber = 9;
    public static final int INDEX_CUSTOMER_Address = 10;
    public static final int INDEX_CUSTOMER_StateId = 11;
    public static final int INDEX_CUSTOMER_StateName = 12;
    public static final int INDEX_CUSTOMER_Gender = 13;
    public static final int INDEX_CUSTOMER_City = 14;
    public static final int INDEX_CUSTOMER_PinCode = 15;
    public static final int INDEX_CUSTOMER_Synced = 16;
    public static final int CUSTOMER_SYNCED_CODE_Unsynced = 0;
    public static final int CUSTOMER_SYNCED_CODE_Synced = 1;
    public static final int CUSTOMER_SYNCED_CODE_Edit = 2;

    // Supplier Table Columns names
    public static final String SUPPLIER_SupplierId = "SupplierId"; // 0
    public static final String SUPPLIER_EntityType = "EntityType"; // 1
    public static final String SUPPLIER_EntityName = "EntityName"; // 2
    public static final String SUPPLIER_CategoryId = "CategoryId"; // 3
    public static final String SUPPLIER_CategoryName = "CategoryName"; // 4
    public static final String SUPPLIER_ContactPerson = "ContactPerson"; // 5
    public static final String SUPPLIER_ContactNumber = "ContactNumber"; // 6
    public static final String SUPPLIER_ContactEmailId = "ContactEmailId"; // 7
    public static final String SUPPLIER_ContactLandline = "ContactLandline"; // 8
    public static final String SUPPLIER_GSTNumber = "GSTNumber"; // 9
    public static final String SUPPLIER_Address = "Address"; // 10
    public static final String SUPPLIER_StateId = "StateId"; // 11
    public static final String SUPPLIER_StateName = "StateName"; // 12
    public static final String SUPPLIER_Gender = "Gender"; // 13
    public static final String SUPPLIER_City = "City"; // 14
    public static final String SUPPLIER_PinCode = "PinCode"; // 15
    public static final String SUPPLIER_Synced = "synced"; // 16
    public static final int INDEX_SUPPLIER_SupplierId = 0;
    public static final int INDEX_SUPPLIER_EntityType = 1;
    public static final int INDEX_SUPPLIER_EntityName = 2;
    public static final int INDEX_SUPPLIER_CategoryId = 3;
    public static final int INDEX_SUPPLIER_CategoryName = 4;
    public static final int INDEX_SUPPLIER_ContactPerson = 5;
    public static final int INDEX_SUPPLIER_ContactNumber = 6;
    public static final int INDEX_SUPPLIER_ContactEmailId = 7;
    public static final int INDEX_SUPPLIER_ContactLandline = 8;
    public static final int INDEX_SUPPLIER_GSTNumber = 9;
    public static final int INDEX_SUPPLIER_Address = 10;
    public static final int INDEX_SUPPLIER_StateId = 11;
    public static final int INDEX_SUPPLIER_StateName = 12;
    public static final int INDEX_SUPPLIER_Gender = 13;
    public static final int INDEX_SUPPLIER_City = 14;
    public static final int INDEX_SUPPLIER_PinCode = 15;
    public static final int INDEX_SUPPLIER_Synced = 16;
    public static final int SUPPLIER_SYNCED_CODE_Unsynced = 0;
    public static final int SUPPLIER_SYNCED_CODE_Synced = 1;
    public static final int SUPPLIER_SYNCED_CODE_Edit = 2;

    //mSalesRegister
    public static final String M_SALES_SalesID = "SalesID"; // 0
    public static final String M_SALES_SalesReceiptNumber = "SalesReceiptNumber"; // 1
    public static final String M_SALES_CustomerID = "CustomerID"; // 2
    public static final String M_SALES_BaseAmount = "BaseAmount"; // 3
    public static final String M_SALES_TotalDiscountAmount = "TotalDiscountAmount"; // 4
    public static final String M_SALES_TaxAmount = "TaxAmount"; // 5
    public static final String M_SALES_TotalAmount = "TotalAmount"; // 6
    public static final String M_SALES_ReferenceNumber = "ReferenceNumber"; // 7
    public static final String M_SALES_LoginID = "LoginID"; // 8
    public static final String M_SALES_SalesDtTm = "SalesDtTm"; // 9
    public static final String M_SALES_Gender = "Gender"; // 10
    public static final String M_SALES_isHold = "isHold"; // 11
    public static final String M_SALES_isActive = "isActive"; // 12
    public static final String M_SALES_OutletID = "OutletID"; // 13
    public static final String M_SALES_CustomerType = "CustomerType"; // 14
    public static final String M_SALES_Balance = "Balance"; // 15
    public static final String M_SALES_PaidAmount = "PaidAmount"; // 16
    public static final String M_SALES_TaxOnPaidAmount = "TaxOnPaidAmount"; // 17
    public static final String M_SALES_CGST = "CGST"; // 18
    public static final String M_SALES_SGST = "SGST"; // 19
    public static final String M_SALES_IGST = "IGST"; // 20
    public static final String M_SALES_DiscountTypeID = "DiscountTypeID"; // 21
    public static final String M_SALES_Discount = "Discount"; // 22
    public static final String M_SALES_CESS = "CESS"; // 23
    public static final String M_SALES_DueDate = "DueDate"; // 24
    public static final String M_SALES_CustomerName = "CustomerName"; // 25
    public static final String M_SALES_TermsCondition = "TermsCondition"; // 26
    public static final String M_SALES_CustomerGSTN = "CustomerGSTN"; // 27
    public static final String M_SALES_CustomerEmail = "CustomerEmail"; // 28
    public static final String M_SALES_CustomerMobile = "CustomerMobile"; // 29
    public static final String M_SALES_CustomerAddress = "CustomerAddress"; // 30
    public static final String M_SALES_OutletAddress = "OutletAddress"; // 31
    public static final String M_SALES_OutletLogo = "OutletLogo"; // 32
    public static final String M_SALES_OutletGSTN = "OutletGSTN"; // 33
    public static final String M_SALES_ShipAddress = "ShipAddress"; // 34
    public static final String M_SALES_TransportCharge = "TransportCharge"; // 35
    public static final String M_SALES_InsuranceCharge = "InsuranceCharge"; // 36
    public static final String M_SALES_PackingCharge = "PackingCharge"; // 37
    public static final String M_SALES_BillAddress = "BillAddress"; // 38
    public static final String M_SALES_SalesDate = "SalesDate"; // 39
    public static final String M_SALES_SubTotal = "SubTotal"; // 40
    public static final String M_SALES_eWayBillRequired = "eWayBillRequired"; // 41
    public static final String M_SALES_ShippingStateID = "ShippingStateID"; // 41
    public static final String M_SALES_DONumber = "DONumber"; // 41
    public static final String M_SALES_Synced = "Synced"; // 41
    public static final int INDEX_M_SALES_SalesID = 0; // auto increment
    public static final int INDEX_M_SALES_SalesReceiptNumber = 1; // invoice number
    public static final int INDEX_M_SALES_CustomerID = 2;
    public static final int INDEX_M_SALES_BaseAmount = 3;
    public static final int INDEX_M_SALES_TotalDiscountAmount = 4; // discount at end
    public static final int INDEX_M_SALES_TaxAmount = 5;
    public static final int INDEX_M_SALES_TotalAmount = 6;
    public static final int INDEX_M_SALES_ReferenceNumber = 7; // invoice number
    public static final int INDEX_M_SALES_LoginID = 8; //  at login id
    public static final int INDEX_M_SALES_SalesDtTm = 9; // current date
    public static final int INDEX_M_SALES_Gender = 10; //
    public static final int INDEX_M_SALES_isHold = 11; // 0
    public static final int INDEX_M_SALES_isActive = 12; // 1
    public static final int INDEX_M_SALES_OutletID = 13; // at login
    public static final int INDEX_M_SALES_CustomerType = 14; // 'N'
    public static final int INDEX_M_SALES_Balance = 15; // 0
    public static final int INDEX_M_SALES_PaidAmount = 16; // amount paid
    public static final int INDEX_M_SALES_TaxOnPaidAmount = 17; // only on paid amount
    public static final int INDEX_M_SALES_CGST = 18; // 50%
    public static final int INDEX_M_SALES_SGST = 19; // 50%
    public static final int INDEX_M_SALES_IGST = 20; // 100%
    public static final int INDEX_M_SALES_DiscountTypeID = 21; // % id 10 or 11
    public static final int INDEX_M_SALES_Discount = 22; // amount or %age
    public static final int INDEX_M_SALES_CESS = 23; // 0 default
    public static final int INDEX_M_SALES_DueDate = 24; // invoice date
    public static final int INDEX_M_SALES_CustomerName = 25; // entity name
    public static final int INDEX_M_SALES_TermsCondition = 26; // at login
    public static final int INDEX_M_SALES_CustomerGSTN = 27;
    public static final int INDEX_M_SALES_CustomerEmail = 28;
    public static final int INDEX_M_SALES_CustomerMobile = 29;
    public static final int INDEX_M_SALES_CustomerAddress = 30;
    public static final int INDEX_M_SALES_OutletAddress = 31; //  at login
    public static final int INDEX_M_SALES_OutletLogo = 32; //  at login
    public static final int INDEX_M_SALES_OutletGSTN = 33; // at login
    public static final int INDEX_M_SALES_ShipAddress = 34; // same as customer or custom
    public static final int INDEX_M_SALES_TransportCharge = 35;
    public static final int INDEX_M_SALES_InsuranceCharge = 36;
    public static final int INDEX_M_SALES_PackingCharge = 37;
    public static final int INDEX_M_SALES_BillAddress = 38; // customer address
    public static final int INDEX_M_SALES_SalesDate = 39; // invoice date SubTotal
    public static final int INDEX_M_SALES_SubTotal = 40; // invoice date eWayBillRequired
    public static final int INDEX_M_SALES_eWayBillRequired = 41; // invoice date
    private static final int INDEX_M_SALES_ShippingStateID = 42; //
    private static final int INDEX_M_SALES_DONumber = 43; //
    private static final int INDEX_M_SALES_Synced = 44; // invoice date
    public static final int M_SALES_SYNCED_CODE_Unsynced = 0;
    public static final int M_SALES_SYNCED_CODE_Synced = 1;

    //            tSalesRegister
    public static final String T_SALES_SalesSubID = "SalesSubID"; // 0
    public static final String T_SALES_SalesID = "SalesID"; // 1
    public static final String T_SALES_SalesPersonID = "SalesPersonID"; // 2
    public static final String T_SALES_ItemTypeID = "ItemTypeID"; // 3
    public static final String T_SALES_ItemID = "ItemID"; // 4
    public static final String T_SALES_Qty = "Qty"; // 5
    public static final String T_SALES_RefNumber = "RefNumber"; // 6
    public static final String T_SALES_BaseAmount = "BaseAmount"; // 7
    public static final String T_SALES_Discount = "Discount"; // 8
    public static final String T_SALES_TaxAmount = "TaxAmount"; // 9
    public static final String T_SALES_TotalAmount = "TotalAmount"; // 10
    public static final String T_SALES_SalesCommission = "SalesCommission"; // 11
    public static final String T_SALES_CommissionTypeID = "CommissionTypeID"; // 12
    public static final String T_SALES_Outlet_SalesSubID = "Outlet_SalesSubID"; // 13
    public static final String T_SALES_DiscountType = "DiscountType"; // 14
    public static final String T_SALES_CGST = "CGST"; // 15
    public static final String T_SALES_SGST = "SGST"; // 16
    public static final String T_SALES_IGST = "IGST"; // 17
    public static final String T_SALES_CESS = "CESS"; // 18
    public static final String T_SALES_ProductCode = "ProductCode"; // 19
    public static final String T_SALES_ItemName = "ItemName"; // 20
    public static final String T_SALES_TaxRate = "TaxRate"; // 21
    public static final String T_SALES_GroupID = "GroupID"; // 22
    public static final String T_SALES_Synced = "synced"; // 22
    public static final int INDEX_T_SALES_SalesSubID = 0; // autoincrement
    public static final int INDEX_T_SALES_SalesID = 1; // as master
    public static final int INDEX_T_SALES_SalesPersonID = 2; // at login id
    public static final int INDEX_T_SALES_ItemTypeID = 3; // 5
    public static final int INDEX_T_SALES_ItemID = 4; // product id
    public static final int INDEX_T_SALES_Qty = 5; // product qty
    public static final int INDEX_T_SALES_RefNumber = 6; // invoice number
    public static final int INDEX_T_SALES_BaseAmount = 7; // sales price
    public static final int INDEX_T_SALES_Discount = 8; // discount per item
    public static final int INDEX_T_SALES_TaxAmount = 9; // tax per item
    public static final int INDEX_T_SALES_TotalAmount = 10; // TOTAL
    public static final int INDEX_T_SALES_SalesCommission = 11; // 0
    public static final int INDEX_T_SALES_CommissionTypeID = 12; // 0
    public static final int INDEX_T_SALES_Outlet_SalesSubID = 13; // 0
    public static final int INDEX_T_SALES_DiscountType = 14; // 10  or 11
    public static final int INDEX_T_SALES_CGST = 15; // tax
    public static final int INDEX_T_SALES_SGST = 16; // tax
    public static final int INDEX_T_SALES_IGST = 17; // tax
    public static final int INDEX_T_SALES_CESS = 18; // 0
    public static final int INDEX_T_SALES_ProductCode= 19; // product code
    public static final int INDEX_T_SALES_ItemName= 20; // product name
    public static final int INDEX_T_SALES_TaxRate= 21; //
    public static final int INDEX_T_SALES_GroupID= 22; //
    public static final int INDEX_T_SALES_Synced= 23; //

    //mPurchaseRegister
    public static final String M_PURCHASE_PurchaseID = "PurchaseID"; // 0
    public static final String M_PURCHASE_SupplierID = "SupplierID"; // 1
    public static final String M_PURCHASE_InvoiceNumber = "InvoiceNumber"; // 2
    public static final String M_PURCHASE_CreatedBy = "CreatedBy"; // 3
    public static final String M_PURCHASE_CreatedDtTm = "CreatedDtTm"; // 4
    public static final String M_PURCHASE_OutletID = "OutletID"; // 5
    public static final String M_PURCHASE_TransportCharge = "TransportCharge"; // 6
    public static final String M_PURCHASE_InsuranceCharge = "InsuranceCharge"; // 7
    public static final String M_PURCHASE_PackingCharge = "PackingCharge"; // 8
    public static final String M_PURCHASE_PurchaseDate = "PurchaseDate"; // 9
    public static final String M_PURCHASE_CGST = "CGST"; // 10
    public static final String M_PURCHASE_SGST = "SGST"; // 11
    public static final String M_PURCHASE_IGST = "IGST"; // 12
    public static final String M_PURCHASE_CESS = "CESS"; // 13
    public static final String M_PURCHASE_SubTotal = "SubTotal"; // 14
    public static final String M_PURCHASE_GrandTotal = "GrandTotal"; // 15
    public static final String M_PURCHASE_SupplierName = "SupplierName"; //
    public static final String M_PURCHASE_SupplierEmail = "SupplierEmail"; // 13
    public static final String M_PURCHASE_SupplierMobile = "SupplierMobile"; // 13
    public static final String M_PURCHASE_Synced = "Synced"; // 16
    public static final int INDEX_M_PURCHASE_PurchaseID = 0; // auto increment
    public static final int INDEX_M_PURCHASE_SupplierID = 1;
    public static final int INDEX_M_PURCHASE_InvoiceNumber = 2; // invoice number
    public static final int INDEX_M_PURCHASE_CreatedBy = 3; //  at login id
    public static final int INDEX_M_PURCHASE_CreatedDtTm = 4; // current date
    public static final int INDEX_M_PURCHASE_OutletID = 5; // at login
    public static final int INDEX_M_PURCHASE_TransportCharge = 6;
    public static final int INDEX_M_PURCHASE_InsuranceCharge = 7;
    public static final int INDEX_M_PURCHASE_PackingCharge = 8;
    public static final int INDEX_M_PURCHASE_PurchaseDate = 9; // invoice date
    public static final int INDEX_M_PURCHASE_CGST = 10; // 50%
    public static final int INDEX_M_PURCHASE_SGST = 11; // 50%
    public static final int INDEX_M_PURCHASE_IGST = 12; // 100%
    public static final int INDEX_M_PURCHASE_CESS = 13; // 0 default
    public static final int INDEX_M_PURCHASE_SubTotal = 14; // 0 default
    public static final int INDEX_M_PURCHASE_GrandTotal = 15; // 0 default
    public static final int INDEX_M_PURCHASE_SupplierName = 16;
    public static final int INDEX_M_PURCHASE_SupplierEmail = 17;
    public static final int INDEX_M_PURCHASE_SupplierMobile = 18;
    public static final int INDEX_M_PURCHASE_Synced = 19;
    public static final int M_PURCHASE_SYNCED_CODE_Unsynced = 0;
    public static final int M_PURCHASE_SYNCED_CODE_Synced = 1;

//            tPurchaseRegister
    public static final String T_PURCHASE_PurchaseDetailID = "PurchaseDetailID"; // 0
    public static final String T_PURCHASE_PurchaseID = "PurchaseID"; // 1
    public static final String T_PURCHASE_ProductID = "ProductID"; // 1
    public static final String T_PURCHASE_UnitID = "UnitID"; // 2
    public static final String T_PURCHASE_Quantity = "Quantity"; // 3
    public static final String T_PURCHASE_UnitPrice = "UnitPrice"; // 4
    public static final String T_PURCHASE_Amount = "Amount"; // 5
    public static final String T_PURCHASE_Status = "Status"; // 6
    public static final String T_PURCHASE_ProductName = "ProductName"; // 6
    public static final String T_PURCHASE_Unit = "Unit"; // 6
    public static final String T_PURCHASE_iCGST = "iCGST"; // 6
    public static final String T_PURCHASE_iSGST = "iSGST"; // 6
    public static final String T_PURCHASE_iIGST = "iIGST"; // 6
    public static final String T_PURCHASE_iCESS = "iCESS"; // 6
    public static final String T_PURCHASE_ProductCode = "ProductCode"; // 6
    public static final String T_PURCHASE_Synced = "synced"; // 6
    public static final int INDEX_T_PURCHASE_PurchaseDetailID = 0; // autoincrement
    public static final int INDEX_T_PURCHASE_PurchaseID = 1; // as master
    public static final int INDEX_T_PURCHASE_ProductID = 2; // product id
    public static final int INDEX_T_PURCHASE_UnitID = 3; // product id
    public static final int INDEX_T_PURCHASE_Quantity = 4; // product qty
    public static final int INDEX_T_PURCHASE_UnitPrice = 5; // sales price
    public static final int INDEX_T_PURCHASE_Amount = 6; // TOTAL
    public static final int INDEX_T_PURCHASE_Status = 7; // status
    public static final int INDEX_T_PURCHASE_ProductName = 8; // status
    public static final int INDEX_T_PURCHASE_Unit = 9; // status
    public static final int INDEX_T_PURCHASE_iCGST = 10;
    public static final int INDEX_T_PURCHASE_iSGST = 11;
    public static final int INDEX_T_PURCHASE_iIGST = 12;
    public static final int INDEX_T_PURCHASE_iCESS = 13;
    public static final int INDEX_T_PURCHASE_ProductCode = 14;
    public static final int INDEX_T_PURCHASE_Synced = 15;

    // sales return
    public static final String M_SALESRETURN_SaleReturnID = "SaleReturnID";
    public static final String M_SALESRETURN_SalesReturnDate = "SalesReturnDate";
    public static final String M_SALESRETURN_CustomerID = "CustomerID";
    public static final String M_SALESRETURN_OutletID = "OutletID";
    public static final String M_SALESRETURN_CreditAmount = "CreditAmount";
    public static final String M_SALESRETURN_IsActive = "IsActive";
    public static final String M_SALESRETURN_CreatedBy = "CreatedBy";
    public static final String M_SALESRETURN_CreatedDttm = "CreatedDttm";
    public static final String M_SALESRETURN_CustomerName = "CustomerName";
    public static final String M_SALESRETURN_CustomerEmail = "CustomerEmail";
    public static final String M_SALESRETURN_CustomerMobile = "CustomerMobile";
    public static final String M_SALESRETURN_CustomerLandline = "CustomerLandline";
    public static final String M_SALESRETURN_InvoiceNumber = "InvoiceNumber";
    public static final String M_SALESRETURN_SalesReturnNumber = "SalesReturnNumber";
    public static final String M_SALESRETURN_Notes = "Notes";
    public static final String M_SALESRETURN_SalesID = "SalesID";
    public static final String M_SALESRETURN_Synced = "synced";
    public static final int INDEX_M_SALESRETURN_SaleReturnID = 0; //
    public static final int INDEX_M_SALESRETURN_SalesReturnDate = 1; //
    public static final int INDEX_M_SALESRETURN_CustomerID = 2; //
    public static final int INDEX_M_SALESRETURN_OutletID = 3; //
    public static final int INDEX_M_SALESRETURN_CreditAmount = 4; //
    public static final int INDEX_M_SALESRETURN_IsActive = 5; //
    public static final int INDEX_M_SALESRETURN_CreatedBy = 6; //
    public static final int INDEX_M_SALESRETURN_CreatedDttm = 7; //
    public static final int INDEX_M_SALESRETURN_CustomerName = 8;
    public static final int INDEX_M_SALESRETURN_CustomerEmail = 9;
    public static final int INDEX_M_SALESRETURN_CustomerMobile = 10;
    public static final int INDEX_M_SALESRETURN_CustomerLandline = 11;
    public static final int INDEX_M_SALESRETURN_InvoiceNumber = 12;
    public static final int INDEX_M_SALESRETURN_SalesReturnNumber = 13;
    public static final int INDEX_M_SALESRETURN_Notes = 14;
    public static final int INDEX_M_SALESRETURN_SalesID = 15;
    public static final int INDEX_M_SALESRETURN_Synced = 16;
    public static final int M_SALESRETURN_SYNCED_CODE_Unsynced = 0;
    public static final int M_SALESRETURN_SYNCED_CODE_Synced = 1;

    public static final String T_SALESRETURN_SalesReturnDetailID = "SalesReturnDetailID";
    public static final String T_SALESRETURN_SalesReturnID = "SalesReturnID";
    public static final String T_SALESRETURN_ProductID = "ProductID";
    public static final String T_SALESRETURN_ProductCode = "ProductCode";
    public static final String T_SALESRETURN_UnitID = "UnitID";
    public static final String T_SALESRETURN_ReturnPrice = "ReturnPrice";
    public static final String T_SALESRETURN_ReturnQty = "ReturnQty";
    public static final String T_SALESRETURN_ReturnTaxRate = "ReturnTaxRate";
    public static final String T_SALESRETURN_ReturnTaxAmount = "ReturnTaxAmount";
    public static final String T_SALESRETURN_ReturnAmount = "ReturnAmount";
    public static final String T_SALESRETURN_ProductName = "ProductName";
    public static final String T_SALESRETURN_ReturnType = "ReturnType";
    public static final String T_SALESRETURN_Synced = "synced";
    public static final int INDEX_T_SALESRETURN_SalesReturnDetailID = 0;
    public static final int INDEX_T_SALESRETURN_SalesReturnID = 1;
    public static final int INDEX_T_SALESRETURN_ProductID = 2;
    public static final int INDEX_T_SALESRETURN_ProductCode = 3;
    public static final int INDEX_T_SALESRETURN_UnitID = 4;
    public static final int INDEX_T_SALESRETURN_ReturnPrice = 5;
    public static final int INDEX_T_SALESRETURN_ReturnQty = 6;
    public static final int INDEX_T_SALESRETURN_ReturnTaxRate = 7;
    public static final int INDEX_T_SALESRETURN_ReturnTaxAmount = 8;
    public static final int INDEX_T_SALESRETURN_ReturnAmount = 9;
    public static final int INDEX_T_SALESRETURN_ProductName = 10;
    public static final int INDEX_T_SALESRETURN_ReturnType = 11;
    public static final int INDEX_T_SALESRETURN_Synced = 12;

    // payment
    public static final String PAYMENT_PaymentID = "PaymentID"; // 0
    public static final String PAYMENT_InvoiceNumber = "InvoiceNumber"; // 1
    public static final String PAYMENT_ReceiptNumber = "ReceiptNumber"; // 2
    public static final String PAYMENT_PaymentMode = "PaymentMode"; // 3
    public static final String PAYMENT_Amount = "Amount"; // 4
    public static final String PAYMENT_ReferenceNumber = "ReferenceNumber"; // 5
    public static final String PAYMENT_PaymentTypeID = "PaymentTypeID"; // 6
    public static final String PAYMENT_CreditCardNumber = "CreditCardNumber"; // 7
    public static final String PAYMENT_CreditCardType = "CreditCardType"; // 8
    public static final String PAYMENT_Synced = "Synced"; // 9
    public static final String PAYMENT_PaymentDtTm = "PaymentDtTm"; // 10
    public static final int INDEX_PAYMENT_PaymentID = 0;
    public static final int INDEX_PAYMENT_InvoiceNumber = 1;
    public static final int INDEX_PAYMENT_ReceiptNumber = 2;
    public static final int INDEX_PAYMENT_PaymentMode = 3;
    public static final int INDEX_PAYMENT_Amount = 4;
    public static final int INDEX_PAYMENT_ReferenceNumber = 5;
    public static final int INDEX_PAYMENT_PaymentTypeID = 6;
    public static final int INDEX_PAYMENT_CreditCardNumber = 7;
    public static final int INDEX_PAYMENT_CreditCardType = 8;
    public static final int INDEX_PAYMENT_Synced = 9;
    public static final int INDEX_PAYMENT_PaymentDtTm = 10;
    public static final int PAYMENT_SYNCED_CODE_Unsynced = 0;
    public static final int PAYMENT_SYNCED_CODE_Synced = 1;
    public static final int PAYMENT_SYNCED_CODE_UnsyncedAgainstSyncedInvoices = 2;

    // last synced
    public static final String LAST_SYNCED_ListCode = "ListCode"; // 0
    public static final String LAST_SYNCED_DateTime = "DateTime"; // 1
    public static final int INDEX_LAST_SYNCED_ListCode = 0;
    public static final int INDEX_LAST_SYNCED_DateTime = 1;
    public static final int LIST_CODE_PRODUCT_LIST = 0;
    public static final int LIST_CODE_CUSTOMER_LIST = 1;
    public static final int LIST_CODE_SUPPLIER_LIST = 2;
    public static final int LIST_CODE_INVOICE_LIST = 3;

    // states
    public static final String STATE_StateID = "StateID"; // 0
    public static final String STATE_StateName = "StateName"; // 1
    public static final int INDEX_STATE_StateID = 0;
    public static final int INDEX_STATE_StateName = 1;

    // gst category
    public static final String GSTCATEGORY_ID = "ID"; // 0
    public static final String GSTCATEGORY_CategoryName = "CategoryName"; // 1
    public static final int INDEX_GSTCATEGORY_ID = 0;
    public static final int INDEX_GSTCATEGORY_CategoryName = 1;

    // payment modes
//    private int PaymentTypeID;
//    private String PaymentTypeName;
    public static final String PAYMENTMODE_PaymentTypeID = "PaymentTypeID"; // 0
    public static final String PAYMENTMODE_PaymentTypeName = "PaymentTypeName"; // 0
    public static final int INDEX_PAYMENTMODE_PaymentTypeID = 0;
    public static final int INDEX_PAYMENTMODE_PaymentTypeName = 1;

    // stock ledger
    public static final String STOCKLEDGER_StockLedgerID = "StockLedgerID"; // 0
    public static final String STOCKLEDGER_ProductID = "ProductID"; // 1
    public static final String STOCKLEDGER_ReferenceNumber = "ReferenceNumber"; // 2
    public static final String STOCKLEDGER_TransactionType = "TransactionType"; // 3
    public static final String STOCKLEDGER_DateOfTransaction = "DateOfTransaction"; // 4
    public static final String STOCKLEDGER_Quantity = "Quantity"; // 5
    public static final String STOCKLEDGER_DateOfCreation = "DateOfCreation"; // 6
    public static final String STOCKLEDGER_InOut = "InOut"; // 7
    public static final int INDEX_STOCKLEDGER_StockLedgerID = 0;
    public static final int INDEX_STOCKLEDGER_ProductID = 1;
    public static final int INDEX_STOCKLEDGER_ReferenceNumber = 2;
    public static final int INDEX_STOCKLEDGER_TransactionType = 3;
    public static final int INDEX_STOCKLEDGER_DateOfTransaction = 4;
    public static final int INDEX_STOCKLEDGER_Quantity = 5;
    public static final int INDEX_STOCKLEDGER_DateOfCreation = 6;
    public static final int INDEX_STOCKLEDGER_InOut = 7;

    // logs
    private static final String LOGS_ID = "ID"; // 0
    private static final String LOGS_LogDate = "LogDate"; // 0
    private static final String LOGS_LogTime = "LogTime"; // 0
    private static final String LOGS_TabID = "TabID"; // 0
    private static final String LOGS_Origin = "Origin"; // 0
    private static final String LOGS_TypeOfError = "TypeOfError"; // 0
    private static final String LOGS_LogMessage = "LogMessage"; // 0
    private static final String LOGS_Synced = "Synced"; // 0
    public static final int INDEX_LOGS_ID = 0;
    public static final int INDEX_LOGS_LogDate = 1;
    public static final int INDEX_LOGS_LogTime = 2;
    public static final int INDEX_LOGS_TabID = 3;
    public static final int INDEX_LOGS_Origin = 4;
    public static final int INDEX_LOGS_TypeOfError = 5;
    public static final int INDEX_LOGS_LogMessage = 6;
    public static final int INDEX_LOGS_Synced = 7;
    public static final int LOGS_SYNCED_CODE_Unsynced = 0;
    public static final int LOGS_SYNCED_CODE_Synced = 1;

    // eway bill
//    private long ID;
//    private long SalesID;
//    private String InvoiceNumber;
//    private int ReasonID;
//    private String DocumentNo;
//    private String VehicleNo;
//    private boolean isActive;
    private static final String EWAYBILL_ID = "ID";
    private static final String EWAYBILL_SalesID = "SalesID";
    private static final String EWAYBILL_InvoiceNumber = "InvoiceNumber";
    private static final String EWAYBILL_ReasonID = "ReasonID";
    private static final String EWAYBILL_DocumentNo = "DocumentNo";
    private static final String EWAYBILL_VehicleNo = "VehicleNo";
    private static final String EWAYBILL_isActive = "isActive";
    private static final String EWAYBILL_CreatedBy = "CreatedBy";
    private static final String EWAYBILL_CreatedDtTm = "CreatedDtTm";
    private static final String EWAYBILL_EWayBillNo = "EWayBillNo";
    private static final String EWAYBILL_EWayDocumentPath = "EWayDocumentPath";
    private static final String EWAYBILL_DONumber = "DONumber";
    private static final String EWAYBILL_Synced = "synced";
    public static final int INDEX_EWAYBILL_ID = 0;
    public static final int INDEX_EWAYBILL_SalesID = 1;
    public static final int INDEX_EWAYBILL_InvoiceNumber = 2;
    public static final int INDEX_EWAYBILL_ReasonID = 3;
    public static final int INDEX_EWAYBILL_DocumentNo = 4;
    public static final int INDEX_EWAYBILL_VehicleNo = 5;
    public static final int INDEX_EWAYBILL_isActive = 6;
    public static final int INDEX_EWAYBILL_CreatedBy = 7;
    public static final int INDEX_EWAYBILL_CreatedDtTm = 8;
    public static final int INDEX_EWAYBILL_EWayBillNo = 9;
    public static final int INDEX_EWAYBILL_EWayDocumentPath = 10;
    public static final int INDEX_EWAYBILL_DONumber = 11;
    public static final int INDEX_EWAYBILL_Synced = 12;
    public static final int EWAYBILL_SYNCED_CODE_Unsynced = 0;
    public static final int EWAYBILL_SYNCED_CODE_Synced = 1;

    // eway bill reason
    private static final String EWAYBILLREASON_ID = "ID";
    private static final String EWAYBILLREASON_Reason = "Reason";
    public static final int INDEX_EWAYBILLREASON_ID = 0;
    public static final int INDEX_EWAYBILLREASON_Reason = 1;

    // delivery order
    private static final String M_DELIVERYORDER_ID = "ID";
    private static final String M_DELIVERYORDER_DeliveryDate = "DeliveryDate";
    private static final String M_DELIVERYORDER_DONumber = "DONumber";
    private static final String M_DELIVERYORDER_Customer_ID = "Customer_ID";
    private static final String M_DELIVERYORDER_OutletID = "OutletID";
    private static final String M_DELIVERYORDER_IsActive = "IsActive";
    private static final String M_DELIVERYORDER_CreatedBy = "CreatedBy";
    private static final String M_DELIVERYORDER_CreatedDtTm = "CreatedDtTm";
    private static final String M_DELIVERYORDER_Status = "Status";
    private static final String M_DELIVERYORDER_CustomerName = "CustomerName";
    private static final String M_DELIVERYORDER_CustomerEmail = "CustomerEmail";
    private static final String M_DELIVERYORDER_CustomerContactPerson = "CustomerContactPerson";
    private static final String M_DELIVERYORDER_CustomerMobile = "CustomerMobile";
    private static final String M_DELIVERYORDER_CustomerLandline = "CustomerLandline";
    private static final String M_DELIVERYORDER_CustomerBillAddress = "CustomerBillAddress";
    private static final String M_DELIVERYORDER_CustomerBillStateID = "CustomerBillStateID";
    private static final String M_DELIVERYORDER_CustomerShipAddress = "CustomerShipAddress";
    private static final String M_DELIVERYORDER_CustomerShipStateID = "CustomerShipStateID";
    private static final String M_DELIVERYORDER_TotalQuantity = "TotalQuantity";
    private static final String M_DELIVERYORDER_TotalAmount = "TotalAmount";
    private static final String M_DELIVERYORDER_Synced = "synced";
    public static final int M_DELIVERYORDER_SYNCED_CODE_Unsynced = 0;
    public static final int M_DELIVERYORDER_SYNCED_CODE_Synced = 1;
    private static final int INDEX_M_DELIVERYORDER_ID = 0;
    private static final int INDEX_M_DELIVERYORDER_DeliveryDate = 1;
    private static final int INDEX_M_DELIVERYORDER_DONumber = 2;
    private static final int INDEX_M_DELIVERYORDER_Customer_ID = 3;
    private static final int INDEX_M_DELIVERYORDER_OutletID = 4;
    private static final int INDEX_M_DELIVERYORDER_IsActive = 5;
    private static final int INDEX_M_DELIVERYORDER_CreatedBy = 6;
    private static final int INDEX_M_DELIVERYORDER_CreatedDtTm = 7;
    private static final int INDEX_M_DELIVERYORDER_Status = 8;
    private static final int INDEX_M_DELIVERYORDER_CustomerName = 9;
    private static final int INDEX_M_DELIVERYORDER_CustomerEmail = 10;
    private static final int INDEX_M_DELIVERYORDER_CustomerContactPerson = 11;
    private static final int INDEX_M_DELIVERYORDER_CustomerMobile = 12;
    private static final int INDEX_M_DELIVERYORDER_CustomerLandline = 13;
    private static final int INDEX_M_DELIVERYORDER_CustomerBillAddress = 14;
    private static final int INDEX_M_DELIVERYORDER_CustomerBillStateID = 15;
    private static final int INDEX_M_DELIVERYORDER_CustomerShipAddress = 16;
    private static final int INDEX_M_DELIVERYORDER_CustomerShipStateID = 17;
    private static final int INDEX_M_DELIVERYORDER_TotalQuantity = 18;
    private static final int INDEX_M_DELIVERYORDER_TotalAmount = 19;
    private static final int INDEX_M_DELIVERYORDER_Synced = 20;

    private static final String T_DELIVERYORDER_ID = "ID";
    private static final String T_DELIVERYORDER_DOID = "DOID";
    private static final String T_DELIVERYORDER_ProductID = "ProductID";
    private static final String T_DELIVERYORDER_Quantity = "Quantity";
    private static final String T_DELIVERYORDER_UnitID = "UnitID";
    private static final String T_DELIVERYORDER_IsActive = "IsActive";
    private static final String T_DELIVERYORDER_CreatedBy = "CreatedBy";
    private static final String T_DELIVERYORDER_CreatedDtTm = "CreatedDtTm";
    private static final String T_DELIVERYORDER_Status = "Status";
    private static final String T_DELIVERYORDER_ProductCode = "ProductCode";
    private static final String T_DELIVERYORDER_ProductName = "ProductName";
    private static final String T_DELIVERYORDER_Amount = "Amount";
    private static final String T_DELIVERYORDER_Synced = "synced";
    private static final int INDEX_T_DELIVERYORDER_ID = 0;
    private static final int INDEX_T_DELIVERYORDER_DOID = 1;
    private static final int INDEX_T_DELIVERYORDER_ProductID = 2;
    private static final int INDEX_T_DELIVERYORDER_Quantity = 3;
    private static final int INDEX_T_DELIVERYORDER_UnitID = 4;
    private static final int INDEX_T_DELIVERYORDER_IsActive = 5;
    private static final int INDEX_T_DELIVERYORDER_CreatedBy = 6;
    private static final int INDEX_T_DELIVERYORDER_CreatedDtTm = 7;
    private static final int INDEX_T_DELIVERYORDER_Status = 8;
    private static final int INDEX_T_DELIVERYORDER_ProductCode = 9;
    private static final int INDEX_T_DELIVERYORDER_ProductName = 10;
    private static final int INDEX_T_DELIVERYORDER_Amount = 11;
    private static final int INDEX_T_DELIVERYORDER_Synced = 12;

    // credit note
    private static final String CREDITNOTE_CreditNoteID = "CreditNoteID";
    private static final String CREDITNOTE_CreditNoteNumber = "CreditNoteNumber";
    private static final String CREDITNOTE_CustomerID = "CustomerID";
    private static final String CREDITNOTE_CustomerName = "CustomerName";
    private static final String CREDITNOTE_CustomerEmail = "CustomerEmail";
    private static final String CREDITNOTE_CustomerMobile = "CustomerMobile";
    private static final String CREDITNOTE_CustomerLandline = "CustomerLandline";
    private static final String CREDITNOTE_InvoiceNumber = "InvoiceNumber";
    private static final String CREDITNOTE_SalesReturnNumber = "SalesReturnNumber";
    private static final String CREDITNOTE_OutletID = "OutletID";
    private static final String CREDITNOTE_CreditAmount = "CreditAmount";
    private static final String CREDITNOTE_Reason = "Reason";
    private static final String CREDITNOTE_CreatedBy = "CreatedBy";
    private static final String CREDITNOTE_CreatedDttm = "CreatedDttm";
    private static final String CREDITNOTE_ModifiedBy = "ModifiedBy";
    private static final String CREDITNOTE_ModifiedDttm = "ModifiedDttm";
    private static final String CREDITNOTE_SalesID = "SalesID";
    private static final String CREDITNOTE_NoteType = "NoteType";
    private static final String CREDITNOTE_NoteDate = "NoteDate";
    private static final String CREDITNOTE_Synced = "Synced";
    public static final int CREDITNOTE_SYNCED_CODE_Unsynced = 0;
    public static final int CREDITNOTE_SYNCED_CODE_Synced = 1;
    private static final int INDEX_CREDITNOTE_CreditNoteID = 0;
    private static final int INDEX_CREDITNOTE_CreditNoteNumber = 1;
    private static final int INDEX_CREDITNOTE_CustomerID = 2;
    private static final int INDEX_CREDITNOTE_CustomerName = 3;
    private static final int INDEX_CREDITNOTE_CustomerEmail = 4;
    private static final int INDEX_CREDITNOTE_CustomerMobile = 5;
    private static final int INDEX_CREDITNOTE_CustomerLandline = 6;
    private static final int INDEX_CREDITNOTE_InvoiceNumber = 7;
    private static final int INDEX_CREDITNOTE_SalesReturnNumber = 8;
    private static final int INDEX_CREDITNOTE_OutletID = 9;
    private static final int INDEX_CREDITNOTE_CreditAmount = 10;
    private static final int INDEX_CREDITNOTE_Reason = 11;
    private static final int INDEX_CREDITNOTE_CreatedBy = 12;
    private static final int INDEX_CREDITNOTE_CreatedDttm = 13;
    private static final int INDEX_CREDITNOTE_ModifiedBy = 14;
    private static final int INDEX_CREDITNOTE_ModifiedDttm = 15;
    private static final int INDEX_CREDITNOTE_SalesID = 16;
    private static final int INDEX_CREDITNOTE_NoteType = 17;
    private static final int INDEX_CREDITNOTE_NoteDate = 18;
    private static final int INDEX_CREDITNOTE_Synced = 19;

    // m purchase return
    private static final String M_PURCHASERETURN_PurchaseReturnID = "PurchaseReturnID";
    private static final String M_PURCHASERETURN_PurchaseReturnNumber = "PurchaseReturnNumber";
    private static final String M_PURCHASERETURN_InvoiceNumber = "InvoiceNumber";
    private static final String M_PURCHASERETURN_PurchaseReturnDate = "PurchaseReturnDate";
    private static final String M_PURCHASERETURN_CreatedBy = "CreatedBy";
    private static final String M_PURCHASERETURN_CreatedDtTm = "CreatedDtTm";
    private static final String M_PURCHASERETURN_OutletID = "OutletID";
    private static final String M_PURCHASERETURN_CGST = "CGST";
    private static final String M_PURCHASERETURN_SGST = "SGST";
    private static final String M_PURCHASERETURN_IGST = "IGST";
    private static final String M_PURCHASERETURN_CESS = "CESS";
    private static final String M_PURCHASERETURN_SubTotal = "SubTotal";
    private static final String M_PURCHASERETURN_DebitAmount = "DebitAmount";
    private static final String M_PURCHASERETURN_SupplierID = "SupplierID";
    private static final String M_PURCHASERETURN_SupplierName = "SupplierName";
    private static final String M_PURCHASERETURN_SupplierEmail = "SupplierEmail";
    private static final String M_PURCHASERETURN_SupplierMobile = "SupplierMobile";
    private static final String M_PURCHASERETURN_Notes = "Notes";
    private static final String M_PURCHASERETURN_PurchaseID = "PurchaseID";
    private static final String M_PURCHASERETURN_Synced = "Synced";
    public static final int M_PURCHASERETURN_SYNCED_CODE_Unsynced = 0;
    public static final int M_PURCHASERETURN_SYNCED_CODE_Synced = 1;
    private static final int INDEX_M_PURCHASERETURN_PurchaseReturnID = 0;
    private static final int INDEX_M_PURCHASERETURN_PurchaseReturnNumber = 1;
    private static final int INDEX_M_PURCHASERETURN_InvoiceNumber = 2;
    private static final int INDEX_M_PURCHASERETURN_PurchaseReturnDate = 3;
    private static final int INDEX_M_PURCHASERETURN_CreatedBy = 4;
    private static final int INDEX_M_PURCHASERETURN_CreatedDtTm = 5;
    private static final int INDEX_M_PURCHASERETURN_OutletID = 6;
    private static final int INDEX_M_PURCHASERETURN_CGST = 7;
    private static final int INDEX_M_PURCHASERETURN_SGST = 8;
    private static final int INDEX_M_PURCHASERETURN_IGST = 9;
    private static final int INDEX_M_PURCHASERETURN_CESS = 10;
    private static final int INDEX_M_PURCHASERETURN_SubTotal = 11;
    private static final int INDEX_M_PURCHASERETURN_DebitAmount = 12;
    private static final int INDEX_M_PURCHASERETURN_SupplierID = 13;
    private static final int INDEX_M_PURCHASERETURN_SupplierName = 14;
    private static final int INDEX_M_PURCHASERETURN_SupplierEmail = 15;
    private static final int INDEX_M_PURCHASERETURN_SupplierMobile = 16;;
    private static final int INDEX_M_PURCHASERETURN_Notes = 17;
    private static final int INDEX_M_PURCHASERETURN_PurchaseID = 18;
    private static final int INDEX_M_PURCHASERETURN_Synced = 19;

    private static final String T_PURCHASERETURN_PurchaseDetailID = "PurchaseDetailID";
    private static final String T_PURCHASERETURN_PurchaseReturnID = "PurchaseReturnID";
    private static final String T_PURCHASERETURN_ProductID = "ProductID";
    private static final String T_PURCHASERETURN_ProductCode = "ProductCode";
    private static final String T_PURCHASERETURN_UnitID = "UnitID";
    private static final String T_PURCHASERETURN_ReturnQty = "ReturnQty";
    private static final String T_PURCHASERETURN_UnitPrice = "UnitPrice";
    private static final String T_PURCHASERETURN_ReturnAmount = "ReturnAmount";
    private static final String T_PURCHASERETURN_ReturnTaxAmount = "ReturnTaxAmount";
    private static final String T_PURCHASERETURN_ProductName = "ProductName";
    private static final String T_PURCHASERETURN_CGST = "CGST";
    private static final String T_PURCHASERETURN_SGST = "SGST";
    private static final String T_PURCHASERETURN_IGST = "IGST";
    private static final String T_PURCHASERETURN_CESS = "CESS";
    private static final int INDEX_T_PURCHASERETURN_PurchaseDetailID = 0;
    private static final int INDEX_T_PURCHASERETURN_PurchaseReturnID = 1;
    private static final int INDEX_T_PURCHASERETURN_ProductID = 2;
    private static final int INDEX_T_PURCHASERETURN_ProductCode = 3;
    private static final int INDEX_T_PURCHASERETURN_UnitID = 4;
    private static final int INDEX_T_PURCHASERETURN_ReturnQty = 5;
    private static final int INDEX_T_PURCHASERETURN_UnitPrice = 6;
    private static final int INDEX_T_PURCHASERETURN_ReturnAmount = 7;
    private static final int INDEX_T_PURCHASERETURN_ReturnTaxAmount = 8;
    private static final int INDEX_T_PURCHASERETURN_ProductName = 9;
    private static final int INDEX_T_PURCHASERETURN_CGST = 10;
    private static final int INDEX_T_PURCHASERETURN_SGST = 11;
    private static final int INDEX_T_PURCHASERETURN_IGST = 12;
    private static final int INDEX_T_PURCHASERETURN_CESS = 13;


    // Group Product Item
    private static final String GROUPPRODUCTITEM_ID = "ID";
    private static final String GROUPPRODUCTITEM_ProductID = "ProductID";
    private static final String GROUPPRODUCTITEM_ItemID = "ItemID";
    private static final String GROUPPRODUCTITEM_ItemName = "ItemName";
    private static final String GROUPPRODUCTITEM_ItemCode = "ItemCode";
    private static final String GROUPPRODUCTITEM_Quantity = "Quantity";
    private static final String GROUPPRODUCTITEM_UnitID = "UnitID";
    private static final String GROUPPRODUCTITEM_Price = "Price";
    private static final String GROUPPRODUCTITEM_Weightage = "Weightage";
    private static final String GROUPPRODUCTITEM_isActive = "isActive";
    private static final String GROUPPRODUCTITEM_Tax = "Tax";
    private static final String GROUPPRODUCTITEM_Synced = "Synced";
    private static final int INDEX_GROUPPRODUCTITEM_ID = 0;
    private static final int INDEX_GROUPPRODUCTITEM_ProductID = 1;
    private static final int INDEX_GROUPPRODUCTITEM_ItemID = 2;
    private static final int INDEX_GROUPPRODUCTITEM_ItemName = 3;
    private static final int INDEX_GROUPPRODUCTITEM_ItemCode = 4;
    private static final int INDEX_GROUPPRODUCTITEM_Quantity = 5;
    private static final int INDEX_GROUPPRODUCTITEM_UnitID = 6;
    private static final int INDEX_GROUPPRODUCTITEM_Price = 7;
    private static final int INDEX_GROUPPRODUCTITEM_Weightage = 8;
    private static final int INDEX_GROUPPRODUCTITEM_isActive = 9;
    private static final int INDEX_GROUPPRODUCTITEM_Tax = 10;
    private static final int INDEX_GROUPPRODUCTITEM_Synced = 11;


    // Group product details in invoice
    private static final String GROUPPRODUCTDETAILININVOICE_ID = "ID";
    private static final String GROUPPRODUCTDETAILININVOICE_SalesID = "SalesID";
    private static final String GROUPPRODUCTDETAILININVOICE_InvoiceNumber = "InvoiceNumber";
    private static final String GROUPPRODUCTDETAILININVOICE_GroupProductID = "GroupProductID";
    private static final String GROUPPRODUCTDETAILININVOICE_GroupProductQuantity = "GroupProductQuantity";
    private static final String GROUPPRODUCTDETAILININVOICE_Synced = "Synced";
    private static final int INDEX_GROUPPRODUCTDETAILININVOICE_ID = 0;
    private static final int INDEX_GROUPPRODUCTDETAILININVOICE_SalesID = 1;
    private static final int INDEX_GROUPPRODUCTDETAILININVOICE_InvoiceNumber = 2;
    private static final int INDEX_GROUPPRODUCTDETAILININVOICE_GroupProductID = 3;
    private static final int INDEX_GROUPPRODUCTDETAILININVOICE_GroupProductQuantity = 4;
    private static final int INDEX_GROUPPRODUCTDETAILININVOICE_Synced = 5;


    // Sales MTD, Today 0,3
    // Collection MTD, Today 1,4
    // Invoices MTD, Today 2,5
    public static final int DASHBOARD_DATA_SalesMTD = 0;
    public static final int DASHBOARD_DATA_CollectionMTD = 1;
    public static final int DASHBOARD_DATA_InvoicesMTD = 2;
    public static final int DASHBOARD_DATA_SalesToday = 3;
    public static final int DASHBOARD_DATA_CollectionToday = 4;
    public static final int DASHBOARD_DATA_InvoicesToday = 5;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + "("
                + PRODUCT_ProductID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PRODUCT_ProductCode + " TEXT,"
                + PRODUCT_ProductName + " TEXT,"
                + PRODUCT_BrandID + " INTEGER,"
                + PRODUCT_Brand + " TEXT,"
                + PRODUCT_BarCode + " TEXT,"
                + PRODUCT_SalesPrice + " NUMERIC,"
                + PRODUCT_PriceGSTInclusive + " NUMERIC,"
                + PRODUCT_IsProductActive + " NUMERIC,"
                + PRODUCT_HSNCode + " TEXT,"
                + PRODUCT_HSNID + " INTEGER,"
                + PRODUCT_UnitID + " INTEGER,"
                + PRODUCT_Unit + " TEXT,"
                + PRODUCT_Tax + " NUMERIC,"
                + PRODUCT_BalanceQty + " NUMERIC,"
                + PRODUCT_ProductCategoryId + " TEXT,"
                + PRODUCT_ProductSubCategoryId + " TEXT,"
                + PRODUCT_Description + " TEXT,"
                + PRODUCT_ProductSubCategoryName + " TEXT,"
                + PRODUCT_CreatedBy + " TEXT,"
                + PRODUCT_CreatedDtTm + " TEXT,"
                + PRODUCT_OutletID + " TEXT,"
                + PRODUCT_IsGroupProduct + " INTEGER,"
                + PRODUCT_Synced + " INTEGER"
                + ");";
        String CREATE_PRODUCTCATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_CATEGORY + "("
                + PRODUCT_CATEGORY_ProductCategoryId + " TEXT,"
                + PRODUCT_CATEGORY_ParentCategoryID + " TEXT,"
                + PRODUCT_CATEGORY_ProductCategoryName + " TEXT,"
                + PRODUCT_CATEGORY_IsProductCategoryActive + " TEXT,"
                + PRODUCT_CATEGORY_CreatedBy + " TEXT,"
                + PRODUCT_CATEGORY_CreatedDtTm + " TEXT,"
                + PRODUCT_CATEGORY_OutletID + " TEXT,"
                + PRODUCT_CATEGORY_Synced + " INTEGER"
                + ");";
        String CREATE_BRAND_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BRANDS + "("
                + BRAND_BrandID + " TEXT,"
                + BRAND_Brand + " TEXT,"
                + BRAND_isBrandActive + " TEXT,"
                + BRAND_CreatedBy + " TEXT,"
                + BRAND_CreatedDtTm + " TEXT,"
                + BRAND_Synced + " INTEGER"
                + ");";
        String CREATE_UNIT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_UNITS + "("
                + PRODUCT_UNIT_ID + " TEXT PRIMARY KEY,"
                + PRODUCT_UNIT_isActive + " TEXT,"
                + PRODUCT_UNIT_Unit + " TEXT,"
                + PRODUCT_UNIT_Synced + " INTEGER"
                + ");";
        String CREATE_HSC_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HSNCODE + "("
                + HSC_ID + " TEXT PRIMARY KEY,"
                + HSC_HSCCode + " TEXT,"
                + HSC_Description + " TEXT,"
                + HSC_CGST + " TEXT,"
                + HSC_SGST + " TEXT,"
                + HSC_IGST + " TEXT,"
                + HSC_CESS + " TEXT,"
                + HSC_IsActive + " TEXT,"
                + HSC_CreatedBy + " TEXT,"
                + HSC_Condition + " TEXT,"
                + HSC_Remarks + " TEXT,"
                + HSC_RedioHSN + " TEXT,"
                + HSC_Synced + " INTEGER"
                + ");";
        String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMERS + "("
                + CUSTOMER_CustomerId + " TEXT PRIMARY KEY,"
                + CUSTOMER_EntityType + " TEXT,"
                + CUSTOMER_EntityName + " TEXT,"
                + CUSTOMER_CategoryId + " INTEGER,"
                + CUSTOMER_CategoryName + " TEXT,"
                + CUSTOMER_ContactPerson + " TEXT,"
                + CUSTOMER_ContactNumber + " TEXT,"
                + CUSTOMER_ContactEmailId + " TEXT,"
                + CUSTOMER_ContactLandline + " TEXT,"
                + CUSTOMER_GSTNumber + " TEXT,"
                + CUSTOMER_Address + " TEXT,"
                + CUSTOMER_StateId + " TEXT,"
                + CUSTOMER_StateName + " TEXT,"
                + CUSTOMER_Gender + " TEXT,"
                + CUSTOMER_City + " TEXT,"
                + CUSTOMER_PinCode + " TEXT,"
                + CUSTOMER_Synced + " INTEGER"
                + ");";
        String CREATE_SUPPLIERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SUPPLIERS + "("
                + SUPPLIER_SupplierId + " TEXT PRIMARY KEY,"
                + SUPPLIER_EntityType + " TEXT,"
                + SUPPLIER_EntityName + " TEXT,"
                + SUPPLIER_CategoryId + " INTEGER,"
                + SUPPLIER_CategoryName + " TEXT,"
                + SUPPLIER_ContactPerson + " TEXT,"
                + SUPPLIER_ContactNumber + " TEXT,"
                + SUPPLIER_ContactEmailId + " TEXT,"
                + SUPPLIER_ContactLandline + " TEXT,"
                + SUPPLIER_GSTNumber + " TEXT,"
                + SUPPLIER_Address + " TEXT,"
                + SUPPLIER_StateId + " TEXT,"
                + SUPPLIER_StateName + " TEXT,"
                + SUPPLIER_Gender + " TEXT,"
                + SUPPLIER_City + " TEXT,"
                + SUPPLIER_PinCode + " TEXT,"
                + SUPPLIER_Synced + " INTEGER"
                + ");";
        String CREATE_M_SALES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_M_SALES_REGISTER + "("
                + M_SALES_SalesID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + M_SALES_SalesReceiptNumber + " TEXT,"
                + M_SALES_CustomerID + " TEXT,"
                + M_SALES_BaseAmount + " TEXT,"
                + M_SALES_TotalDiscountAmount + " TEXT,"
                + M_SALES_TaxAmount + " TEXT,"
                + M_SALES_TotalAmount + " TEXT,"
                + M_SALES_ReferenceNumber + " TEXT,"
                + M_SALES_LoginID + " TEXT,"
                + M_SALES_SalesDtTm + " TEXT,"
                + M_SALES_Gender + " TEXT,"
                + M_SALES_isHold + " TEXT,"
                + M_SALES_isActive + " TEXT,"
                + M_SALES_OutletID + " TEXT,"
                + M_SALES_CustomerType + " TEXT,"
                + M_SALES_Balance + " TEXT,"
                + M_SALES_PaidAmount + " TEXT,"
                + M_SALES_TaxOnPaidAmount + " TEXT,"
                + M_SALES_CGST + " TEXT,"
                + M_SALES_SGST + " TEXT,"
                + M_SALES_IGST + " TEXT,"
                + M_SALES_DiscountTypeID + " TEXT,"
                + M_SALES_Discount + " TEXT,"
                + M_SALES_CESS + " TEXT,"
                + M_SALES_DueDate + " TEXT,"
                + M_SALES_CustomerName + " TEXT,"
                + M_SALES_TermsCondition + " TEXT,"
                + M_SALES_CustomerGSTN + " TEXT,"
                + M_SALES_CustomerEmail + " TEXT,"
                + M_SALES_CustomerMobile + " TEXT,"
                + M_SALES_CustomerAddress + " TEXT,"
                + M_SALES_OutletAddress + " TEXT,"
                + M_SALES_OutletLogo + " TEXT,"
                + M_SALES_OutletGSTN + " TEXT,"
                + M_SALES_ShipAddress + " TEXT,"
                + M_SALES_TransportCharge + " TEXT,"
                + M_SALES_InsuranceCharge + " TEXT,"
                + M_SALES_PackingCharge + " TEXT,"
                + M_SALES_BillAddress + " TEXT,"
                + M_SALES_SalesDate + " TEXT,"
                + M_SALES_SubTotal + " TEXT,"
                + M_SALES_eWayBillRequired + " TEXT,"
                + M_SALES_ShippingStateID + " INTEGER,"
                + M_SALES_DONumber + " TEXT,"
                + M_SALES_Synced + " INTEGER"
                + ");";
        String CREATE_T_SALES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_T_SALES_REGISTER + "("
                + T_SALES_SalesSubID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T_SALES_SalesID + " TEXT,"
                + T_SALES_SalesPersonID + " TEXT,"
                + T_SALES_ItemTypeID + " TEXT,"
                + T_SALES_ItemID + " TEXT,"
                + T_SALES_Qty + " TEXT,"
                + T_SALES_RefNumber + " TEXT,"
                + T_SALES_BaseAmount + " TEXT,"
                + T_SALES_Discount + " TEXT,"
                + T_SALES_TaxAmount + " TEXT,"
                + T_SALES_TotalAmount + " TEXT,"
                + T_SALES_SalesCommission + " TEXT,"
                + T_SALES_CommissionTypeID + " TEXT,"
                + T_SALES_Outlet_SalesSubID + " TEXT,"
                + T_SALES_DiscountType + " TEXT,"
                + T_SALES_CGST + " TEXT,"
                + T_SALES_SGST + " TEXT,"
                + T_SALES_IGST + " TEXT,"
                + T_SALES_CESS + " TEXT,"
                + T_SALES_ProductCode  + " TEXT,"
                + T_SALES_ItemName  + " TEXT,"
                + T_SALES_TaxRate  + " TEXT,"
                + T_SALES_GroupID  + " INTEGER,"
                + T_SALES_Synced  + " INTEGER"
                + ");";
        String CREATE_M_PURCHASE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_M_PURCHASE_REGISTER + "("
                + M_PURCHASE_PurchaseID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + M_PURCHASE_SupplierID + " TEXT,"
                + M_PURCHASE_InvoiceNumber + " TEXT,"
                + M_PURCHASE_CreatedBy + " TEXT,"
                + M_PURCHASE_CreatedDtTm + " TEXT,"
                + M_PURCHASE_OutletID + " TEXT,"
                + M_PURCHASE_TransportCharge + " TEXT,"
                + M_PURCHASE_InsuranceCharge + " TEXT,"
                + M_PURCHASE_PackingCharge + " TEXT,"
                + M_PURCHASE_PurchaseDate + " TEXT,"
                + M_PURCHASE_CGST + " TEXT,"
                + M_PURCHASE_SGST + " TEXT,"
                + M_PURCHASE_IGST + " TEXT,"
                + M_PURCHASE_CESS + " TEXT,"
                + M_PURCHASE_SubTotal + " TEXT,"
                + M_PURCHASE_GrandTotal + " TEXT,"
                + M_PURCHASE_SupplierName + " TEXT,"
                + M_PURCHASE_SupplierEmail + " TEXT,"
                + M_PURCHASE_SupplierMobile + " TEXT,"
                + M_PURCHASE_Synced + " INTEGER"
                + ");";
        String CREATE_T_PURCHASE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_T_PURCHASE_REGISTER + "("
                + T_PURCHASE_PurchaseDetailID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T_PURCHASE_PurchaseID + " TEXT,"
                + T_PURCHASE_ProductID + " TEXT,"
                + T_PURCHASE_UnitID + " TEXT,"
                + T_PURCHASE_Quantity + " TEXT,"
                + T_PURCHASE_UnitPrice + " TEXT,"
                + T_PURCHASE_Amount + " TEXT,"
                + T_PURCHASE_Status + " INTEGER,"
                + T_PURCHASE_ProductName + " TEXT,"
                + T_PURCHASE_Unit + " TEXT,"
                + T_PURCHASE_iCGST + " TEXT,"
                + T_PURCHASE_iSGST + " TEXT,"
                + T_PURCHASE_iIGST + " TEXT,"
                + T_PURCHASE_iCESS + " TEXT,"
                + T_PURCHASE_ProductCode + " TEXT,"
                + T_PURCHASE_Synced + " INTEGER"
                + ");";
        String CREATE_M_SALESRETURN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_M_SALES_RETURN + "("
                + M_SALESRETURN_SaleReturnID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + M_SALESRETURN_SalesReturnDate + " TEXT,"
                + M_SALESRETURN_CustomerID + " TEXT,"
                + M_SALESRETURN_OutletID + " TEXT,"
                + M_SALESRETURN_CreditAmount + " TEXT,"
                + M_SALESRETURN_IsActive + " TEXT,"
                + M_SALESRETURN_CreatedBy + " INTEGER,"
                + M_SALESRETURN_CreatedDttm + " TEXT,"
                + M_SALESRETURN_CustomerName + " TEXT,"
                + M_SALESRETURN_CustomerEmail + " TEXT,"
                + M_SALESRETURN_CustomerMobile + " TEXT,"
                + M_SALESRETURN_CustomerLandline + " TEXT,"
                + M_SALESRETURN_InvoiceNumber + " TEXT,"
                + M_SALESRETURN_SalesReturnNumber + " TEXT,"
                + M_SALESRETURN_Notes + " TEXT,"
                + M_SALESRETURN_SalesID + " INTEGER,"
                + M_SALESRETURN_Synced + " INTEGER"
                + ");";
        String CREATE_T_SALESRETURN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_T_SALES_RETURN + "("
                + T_SALESRETURN_SalesReturnDetailID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T_SALESRETURN_SalesReturnID + " TEXT,"
                + T_SALESRETURN_ProductID + " TEXT,"
                + T_SALESRETURN_ProductCode + " TEXT,"
                + T_SALESRETURN_UnitID + " TEXT,"
                + T_SALESRETURN_ReturnPrice + " TEXT,"
                + T_SALESRETURN_ReturnQty + " INTEGER,"
                + T_SALESRETURN_ReturnTaxRate + " TEXT,"
                + T_SALESRETURN_ReturnTaxAmount + " TEXT,"
                + T_SALESRETURN_ReturnAmount + " TEXT,"
                + T_SALESRETURN_ProductName + " TEXT,"
                + T_SALESRETURN_ReturnType + " INTEGER,"
                + T_SALESRETURN_Synced + " INTEGER"
                + ");";
        String CREATE_PAYMENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENTS + "("
                + PAYMENT_PaymentID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PAYMENT_InvoiceNumber + " TEXT,"
                + PAYMENT_ReceiptNumber + " TEXT,"
                + PAYMENT_PaymentMode + " TEXT,"
                + PAYMENT_Amount + " TEXT,"
                + PAYMENT_ReferenceNumber + " TEXT,"
                + PAYMENT_PaymentTypeID + " INTEGER,"
                + PAYMENT_CreditCardNumber + " TEXT,"
                + PAYMENT_CreditCardType + " TEXT,"
                + PAYMENT_Synced + " INTEGER,"
                + PAYMENT_PaymentDtTm + " TEXT"
                + ");";
        String CREATE_LAST_SYNCED_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LAST_SYNCED + "("
                + LAST_SYNCED_ListCode + " INTEGER,"
                + LAST_SYNCED_DateTime + " TEXT"
                + ");";
        String CREATE_STATES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STATES + "("
                + STATE_StateID + " INTEGER,"
                + STATE_StateName + " TEXT"
                + ");";
        String CREATE_GSTCATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GST_CATEGORY + "("
                + GSTCATEGORY_ID + " INTEGER,"
                + GSTCATEGORY_CategoryName + " TEXT"
                + ");";
        String CREATE_PAYMENT_MODE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENT_MODES + "("
                + PAYMENTMODE_PaymentTypeID + " INTEGER,"
                + PAYMENTMODE_PaymentTypeName + " TEXT"
                + ");";
        String CREATE_STOCKLEDGER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STOCKLEDGER + "("
                + STOCKLEDGER_StockLedgerID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STOCKLEDGER_ProductID + " TEXT,"
                + STOCKLEDGER_ReferenceNumber + " TEXT,"
                + STOCKLEDGER_TransactionType + " TEXT,"
                + STOCKLEDGER_DateOfTransaction + " TEXT,"
                + STOCKLEDGER_Quantity + " TEXT,"
                + STOCKLEDGER_DateOfCreation + " TEXT,"
                + STOCKLEDGER_InOut + " INTEGER"
                + ");";
        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
                + USERS_UserID + " TEXT,"
                + USERS_UserName + " TEXT,"
                + USERS_Password + " TEXT"
                + ");";
        String CREATE_ERRORLOGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGS + "("
                + LOGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LOGS_LogDate + " TEXT,"
                + LOGS_LogTime + " TEXT,"
                + LOGS_TabID + " TEXT,"
                + LOGS_Origin + " TEXT,"
                + LOGS_TypeOfError + " TEXT,"
                + LOGS_LogMessage + " TEXT,"
                + LOGS_Synced + " INTEGER"
                + ");";
        String CREATE_EWAYBILL_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EWAY_BILL_NUMBER + "("
                + EWAYBILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EWAYBILL_SalesID + " TEXT,"
                + EWAYBILL_InvoiceNumber + " TEXT,"
                + EWAYBILL_ReasonID + " TEXT,"
                + EWAYBILL_DocumentNo + " TEXT,"
                + EWAYBILL_VehicleNo + " TEXT,"
                + EWAYBILL_isActive + " TEXT,"
                + EWAYBILL_CreatedBy + " TEXT,"
                + EWAYBILL_CreatedDtTm + " TEXT,"
                + EWAYBILL_EWayBillNo + " TEXT,"
                + EWAYBILL_EWayDocumentPath + " TEXT,"
                + EWAYBILL_DONumber + " TEXT,"
                + EWAYBILL_Synced + " INTEGER"
                + ");";
        String CREATE_EWAYBILLREASON_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EWAY_BILL_REASON + "("
                + EWAYBILLREASON_ID + " INTEGER PRIMARY KEY,"
                + EWAYBILLREASON_Reason + " TEXT"
                + ");";

        String CREATE_M_DELIVERYORDER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_M_DELIVERY_ORDER + "("
                + M_DELIVERYORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + M_DELIVERYORDER_DeliveryDate + " TEXT,"
                + M_DELIVERYORDER_DONumber + " TEXT,"
                + M_DELIVERYORDER_Customer_ID + " TEXT,"
                + M_DELIVERYORDER_OutletID + " INTEGER,"
                + M_DELIVERYORDER_IsActive + " TEXT,"
                + M_DELIVERYORDER_CreatedBy + " INTEGER,"
                + M_DELIVERYORDER_CreatedDtTm + " TEXT,"
                + M_DELIVERYORDER_Status + " INTEGER,"
                + M_DELIVERYORDER_CustomerName + " TEXT,"
                + M_DELIVERYORDER_CustomerEmail + " TEXT,"
                + M_DELIVERYORDER_CustomerContactPerson + " TEXT,"
                + M_DELIVERYORDER_CustomerMobile + " TEXT,"
                + M_DELIVERYORDER_CustomerLandline + " TEXT,"
                + M_DELIVERYORDER_CustomerBillAddress + " TEXT,"
                + M_DELIVERYORDER_CustomerBillStateID + " INTEGER,"
                + M_DELIVERYORDER_CustomerShipAddress + " TEXT,"
                + M_DELIVERYORDER_CustomerShipStateID + " INTEGER,"
                + M_DELIVERYORDER_TotalQuantity + " TEXT,"
                + M_DELIVERYORDER_TotalAmount + " TEXT,"
                + M_DELIVERYORDER_Synced + " INTEGER"
                + ");";

        String CREATE_T_DELIVERYORDER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_T_DELIVERY_ORDER + "("
                + T_DELIVERYORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T_DELIVERYORDER_DOID + " TEXT,"
                + T_DELIVERYORDER_ProductID + " TEXT,"
                + T_DELIVERYORDER_Quantity + " TEXT,"
                + T_DELIVERYORDER_UnitID + " INTEGER,"
                + T_DELIVERYORDER_IsActive + " TEXT,"
                + T_DELIVERYORDER_CreatedBy + " INTEGER,"
                + T_DELIVERYORDER_CreatedDtTm + " TEXT,"
                + T_DELIVERYORDER_Status + " INTEGER,"
                + T_DELIVERYORDER_ProductCode + " TEXT,"
                + T_DELIVERYORDER_ProductName + " TEXT,"
                + T_DELIVERYORDER_Amount + " TEXT,"
                + T_DELIVERYORDER_Synced + " INTEGER"
                + ");";

        String CREATE_CREDITNOTE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CREDIT_DEBIT_NOTE + "("
                + CREDITNOTE_CreditNoteID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CREDITNOTE_CreditNoteNumber + " TEXT,"
                + CREDITNOTE_CustomerID + " TEXT,"
                + CREDITNOTE_CustomerName + " TEXT,"
                + CREDITNOTE_CustomerEmail + " TEXT,"
                + CREDITNOTE_CustomerMobile + " TEXT,"
                + CREDITNOTE_CustomerLandline + " TEXT,"
                + CREDITNOTE_InvoiceNumber + " TEXT,"
                + CREDITNOTE_SalesReturnNumber + " TEXT,"
                + CREDITNOTE_OutletID + " TEXT,"
                + CREDITNOTE_CreditAmount + " TEXT,"
                + CREDITNOTE_Reason + " TEXT,"
                + CREDITNOTE_CreatedBy + " TEXT,"
                + CREDITNOTE_CreatedDttm + " TEXT,"
                + CREDITNOTE_ModifiedBy + " TEXT,"
                + CREDITNOTE_ModifiedDttm + " TEXT,"
                + CREDITNOTE_SalesID + " INTEGER,"
                + CREDITNOTE_NoteType + " TEXT,"
                + CREDITNOTE_NoteDate + " TEXT,"
                + CREDITNOTE_Synced + " INTEGER"
                + ");";

        String CREATE_M_PURCHASERETURN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_M_PURCHASE_RETURN + "("
                + M_PURCHASERETURN_PurchaseReturnID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + M_PURCHASERETURN_PurchaseReturnNumber + " TEXT,"
                + M_PURCHASERETURN_InvoiceNumber + " TEXT,"
                + M_PURCHASERETURN_PurchaseReturnDate + " TEXT,"
                + M_PURCHASERETURN_CreatedBy + " TEXT,"
                + M_PURCHASERETURN_CreatedDtTm + " TEXT,"
                + M_PURCHASERETURN_OutletID + " TEXT,"
                + M_PURCHASERETURN_CGST + " TEXT,"
                + M_PURCHASERETURN_SGST + " TEXT,"
                + M_PURCHASERETURN_IGST + " TEXT,"
                + M_PURCHASERETURN_CESS + " TEXT,"
                + M_PURCHASERETURN_SubTotal + " TEXT,"
                + M_PURCHASERETURN_DebitAmount + " TEXT,"
                + M_PURCHASERETURN_SupplierID + " TEXT,"
                + M_PURCHASERETURN_SupplierName + " TEXT,"
                + M_PURCHASERETURN_SupplierEmail + " TEXT,"
                + M_PURCHASERETURN_SupplierMobile + " TEXT,"
                + M_PURCHASERETURN_Notes + " TEXT,"
                + M_PURCHASERETURN_PurchaseID + " TEXT,"
                + M_PURCHASERETURN_Synced + " INTEGER"
                + ");";

        String CREATE_T_PURCHASERETURN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_T_PURCHASE_RETURN + "("
                + T_PURCHASERETURN_PurchaseDetailID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + T_PURCHASERETURN_PurchaseReturnID + " TEXT,"
                + T_PURCHASERETURN_ProductID + " TEXT,"
                + T_PURCHASERETURN_ProductCode + " TEXT,"
                + T_PURCHASERETURN_UnitID + " TEXT,"
                + T_PURCHASERETURN_ReturnQty + " TEXT,"
                + T_PURCHASERETURN_UnitPrice + " TEXT,"
                + T_PURCHASERETURN_ReturnAmount + " TEXT,"
                + T_PURCHASERETURN_ReturnTaxAmount + " TEXT,"
                + T_PURCHASERETURN_ProductName + " TEXT,"
                + T_PURCHASERETURN_CGST + " TEXT,"
                + T_PURCHASERETURN_SGST + " TEXT,"
                + T_PURCHASERETURN_IGST + " TEXT,"
                + T_PURCHASERETURN_CESS + " TEXT"
                + ");";

        String CREATE_GROUPPRODUCTITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP_PRODUCT_ITEM + "("
                + GROUPPRODUCTITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + GROUPPRODUCTITEM_ProductID + " TEXT,"
                + GROUPPRODUCTITEM_ItemID + " TEXT,"
                + GROUPPRODUCTITEM_ItemName + " TEXT,"
                + GROUPPRODUCTITEM_ItemCode + " TEXT,"
                + GROUPPRODUCTITEM_Quantity + " TEXT,"
                + GROUPPRODUCTITEM_UnitID + " TEXT,"
                + GROUPPRODUCTITEM_Price + " TEXT,"
                + GROUPPRODUCTITEM_Weightage + " TEXT,"
                + GROUPPRODUCTITEM_isActive + " TEXT,"
                + GROUPPRODUCTITEM_Tax + " TEXT,"
                + GROUPPRODUCTITEM_Synced + " INTEGER"
                + ");";

        String CREATE_GROUPPRODUCTDETAILININVOICE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP_PRODUCT_DETAIL_IN_INVOICE + "("
                + GROUPPRODUCTDETAILININVOICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + GROUPPRODUCTDETAILININVOICE_SalesID + " INTEGER,"
                + GROUPPRODUCTDETAILININVOICE_InvoiceNumber + " TEXT,"
                + GROUPPRODUCTDETAILININVOICE_GroupProductID + " INTEGER,"
                + GROUPPRODUCTDETAILININVOICE_GroupProductQuantity + " INTEGER,"
                + GROUPPRODUCTDETAILININVOICE_Synced + " INTEGER"
                + ");";


        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_PRODUCTCATEGORY_TABLE);
        db.execSQL(CREATE_BRAND_TABLE);
        db.execSQL(CREATE_UNIT_TABLE);
        db.execSQL(CREATE_HSC_TABLE);
        db.execSQL(CREATE_CUSTOMERS_TABLE);
        db.execSQL(CREATE_SUPPLIERS_TABLE);
        db.execSQL(CREATE_M_SALES_TABLE);
        db.execSQL(CREATE_T_SALES_TABLE);
        db.execSQL(CREATE_M_PURCHASE_TABLE);
        db.execSQL(CREATE_T_PURCHASE_TABLE);
        db.execSQL(CREATE_M_SALESRETURN_TABLE);
        db.execSQL(CREATE_T_SALESRETURN_TABLE);
        db.execSQL(CREATE_PAYMENTS_TABLE);
        db.execSQL(CREATE_LAST_SYNCED_TABLE);
        db.execSQL(CREATE_STATES_TABLE);
        db.execSQL(CREATE_GSTCATEGORY_TABLE);
        db.execSQL(CREATE_PAYMENT_MODE_TABLE);
        db.execSQL(CREATE_STOCKLEDGER_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ERRORLOGS_TABLE);
        db.execSQL(CREATE_EWAYBILL_TABLE);
        db.execSQL(CREATE_EWAYBILLREASON_TABLE);
        db.execSQL(CREATE_M_DELIVERYORDER_TABLE);
        db.execSQL(CREATE_T_DELIVERYORDER_TABLE);
        db.execSQL(CREATE_CREDITNOTE_TABLE);
        db.execSQL(CREATE_M_PURCHASERETURN_TABLE);
        db.execSQL(CREATE_T_PURCHASERETURN_TABLE);
        db.execSQL(CREATE_GROUPPRODUCTITEM_TABLE);
        db.execSQL(CREATE_GROUPPRODUCTDETAILININVOICE_TABLE);

        Product product1 = new Product(1, "Mi", "Redmi Note 4", 11, "Xiaomi", "Bar code", 12999, false, false, "HSN Code", 101,
                21, "Unit", 10, 1000);
        Product product2 = new Product(2, "Micromax", "Micromax A67", 12, "Micromax", "Bar code", 6999, true, false, "HSN Code", 102,
                22, "Unit", 10, 1000);
        Product product3 = new Product(3, "Samsung", "Samsung Guru", 13, "Samsung", "Bar code", 2499, false, true, "HSN Code", 103,
                23, "Unit", 10, 1000);
//        addProduct(product1, db);
//        addProduct(product2, db);
//        addProduct(product3, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("abhishek" , String.valueOf(oldVersion));
        Log.i("abhishek" , String.valueOf(newVersion));

        Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type IS 'table'" +
                        " AND name NOT IN ('sqlite_master', 'sqlite_sequence', '" + TABLE_LOGS + "', '" + TABLE_HSNCODE + "')",
                null
        );
        if(c.moveToFirst()){
            do{
                db.execSQL("DROP TABLE " + c.getString(c.getColumnIndex("name")));
            }while(c.moveToNext());
        }

        onCreate(db);
    }

    // Add new product
    public void addProduct(Product product, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            if(product.getSynced() == 1) {
                values.put(PRODUCT_ProductID, product.getProductID());
            }
            values.put(PRODUCT_ProductCode, product.getProductCode());
            values.put(PRODUCT_ProductName, product.getProductName());
            values.put(PRODUCT_BrandID, product.getBrandID());
            values.put(PRODUCT_Brand, product.getBrand());
            values.put(PRODUCT_BarCode, product.getBarcode());
            values.put(PRODUCT_SalesPrice, product.getSalesPrice());
            values.put(PRODUCT_PriceGSTInclusive, product.isPriceGSTInclusive());
            values.put(PRODUCT_IsProductActive, product.isProductActive());
            values.put(PRODUCT_HSNCode, product.getHSNCode());
            values.put(PRODUCT_HSNID, product.getHSNID());
            values.put(PRODUCT_UnitID, product.getUnitID());
            values.put(PRODUCT_Unit, product.getUnit());
            values.put(PRODUCT_Tax, product.getTAX());
            values.put(PRODUCT_BalanceQty, product.getBalanceQty());
            values.put(PRODUCT_ProductCategoryId, product.getProductCategoryId());
            values.put(PRODUCT_ProductSubCategoryId, product.getProductSubCategoryId());
            values.put(PRODUCT_Description, product.getDescription());
            values.put(PRODUCT_ProductSubCategoryName, product.getProductSubCategoryName());
            values.put(PRODUCT_CreatedBy, product.getCreatedBy());
            values.put(PRODUCT_CreatedDtTm, product.getCreatedDtTm());
            values.put(PRODUCT_OutletID, product.getOutletID());
            values.put(PRODUCT_IsGroupProduct, product.getIsGroupProduct());
            values.put(PRODUCT_Synced, product.getSynced());

            // insert into product table
            long num = db.insert(TABLE_PRODUCTS, null, values);
            if(num != -1) {
                Log.d("database", "new product inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(product, origin);
            }
//            productList.add(product);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(product, e, origin);
            Log.e("insert products", "" + e);
        }
    }

    public ArrayList<Product> getTradeProductsListFromDB() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
//            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS
//                    + " WHERE " + PRODUCT_ProductCategoryId + " = " + MyFunctions.PRODUCT_CATEGORY_ID_TRADES + ";";
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS
                    + " WHERE " + PRODUCT_ProductCategoryId + " in (" + MyFunctions.PRODUCT_CATEGORY_ID_TRADES
                    + "," + MyFunctions.PRODUCT_CATEGORY_ID_SERVICES + ");";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setProductID(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductID)));
                    product.setProductCode(cursor.getString(INDEX_PRODUCT_ProductCode));
                    product.setProductName(cursor.getString(INDEX_PRODUCT_ProductName));
                    product.setBrandID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_BrandID)));
                    product.setBrand(cursor.getString(INDEX_PRODUCT_Brand));
                    product.setBarcode(cursor.getString(INDEX_PRODUCT_BarCode));
                    product.setSalesPrice(Double.parseDouble(cursor.getString(INDEX_PRODUCT_SalesPrice)));
                    product.setPriceGSTInclusive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_PriceGSTInclusive)));
                    product.setProductActive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_IsProductActive)));
                    product.setHSNCode(cursor.getString(INDEX_PRODUCT_HSNCode));
                    product.setHSNID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_HSNID)));
                    product.setUnitID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_UnitID)));
                    product.setUnit(cursor.getString(INDEX_PRODUCT_Unit));
                    product.setTAX(Double.parseDouble(cursor.getString(INDEX_PRODUCT_Tax)));
                    product.setBalanceQty(Double.parseDouble(cursor.getString(INDEX_PRODUCT_BalanceQty)));
                    product.setProductCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductCategoryId)));
                    product.setProductSubCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductSubCategoryId)));
                    product.setDescription(cursor.getString(INDEX_PRODUCT_Description));
                    product.setProductSubCategoryName(cursor.getString(INDEX_PRODUCT_ProductSubCategoryName));
                    product.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_PRODUCT_CreatedBy)));
                    product.setCreatedDtTm(cursor.getString(INDEX_PRODUCT_CreatedDtTm));
                    product.setOutletID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_OutletID)));
                    product.setIsGroupProduct(Integer.parseInt(cursor.getString(INDEX_PRODUCT_IsGroupProduct)));
                    product.setSynced(Integer.parseInt(cursor.getString(INDEX_PRODUCT_Synced)));

                    products.add(product);
                } while (cursor.moveToNext());
            }
            return products;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_products", "" + e);
        }
        return products;
    }

    public ArrayList<Product> getProductsListFromDB() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<Product> products = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setProductID(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductID)));
                    product.setProductCode(cursor.getString(INDEX_PRODUCT_ProductCode));
                    product.setProductName(cursor.getString(INDEX_PRODUCT_ProductName));
                    product.setBrandID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_BrandID)));
                    product.setBrand(cursor.getString(INDEX_PRODUCT_Brand));
                    product.setBarcode(cursor.getString(INDEX_PRODUCT_BarCode));
                    product.setSalesPrice(Double.parseDouble(cursor.getString(INDEX_PRODUCT_SalesPrice)));
                    product.setPriceGSTInclusive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_PriceGSTInclusive)));
                    product.setProductActive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_IsProductActive)));
                    product.setHSNCode(cursor.getString(INDEX_PRODUCT_HSNCode));
                    product.setHSNID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_HSNID)));
                    product.setUnitID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_UnitID)));
                    product.setUnit(cursor.getString(INDEX_PRODUCT_Unit));
                    product.setTAX(Double.parseDouble(cursor.getString(INDEX_PRODUCT_Tax)));
                    product.setBalanceQty(Double.parseDouble(cursor.getString(INDEX_PRODUCT_BalanceQty)));
                    product.setProductCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductCategoryId)));
                    product.setProductSubCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductSubCategoryId)));
                    product.setDescription(cursor.getString(INDEX_PRODUCT_Description));
                    product.setProductSubCategoryName(cursor.getString(INDEX_PRODUCT_ProductSubCategoryName));
                    product.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_PRODUCT_CreatedBy)));
                    product.setCreatedDtTm(cursor.getString(INDEX_PRODUCT_CreatedDtTm));
                    product.setOutletID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_OutletID)));
                    product.setIsGroupProduct(Integer.parseInt(cursor.getString(INDEX_PRODUCT_IsGroupProduct)));
                    product.setSynced(Integer.parseInt(cursor.getString(INDEX_PRODUCT_Synced)));

                    products.add(product);
                } while (cursor.moveToNext());
            }
            return products;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_products", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<Product> getUnsyncedProductsListFromDB() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS
                    + " WHERE " + PRODUCT_Synced + " <> " + PRODUCT_SYNCED_CODE_Synced;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<Product> products = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setProductID(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductID)));
                    product.setProductCode(cursor.getString(INDEX_PRODUCT_ProductCode));
                    product.setProductName(cursor.getString(INDEX_PRODUCT_ProductName));
                    product.setBrandID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_BrandID)));
                    product.setBrand(cursor.getString(INDEX_PRODUCT_Brand));
                    product.setBarcode(cursor.getString(INDEX_PRODUCT_BarCode));
                    product.setSalesPrice(Double.parseDouble(cursor.getString(INDEX_PRODUCT_SalesPrice)));
                    product.setPriceGSTInclusive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_PriceGSTInclusive)));
                    product.setProductActive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_IsProductActive)));
                    product.setHSNCode(cursor.getString(INDEX_PRODUCT_HSNCode));
                    product.setHSNID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_HSNID)));
                    product.setUnitID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_UnitID)));
                    product.setUnit(cursor.getString(INDEX_PRODUCT_Unit));
                    product.setTAX(Double.parseDouble(cursor.getString(INDEX_PRODUCT_Tax)));
                    product.setBalanceQty(Double.parseDouble(cursor.getString(INDEX_PRODUCT_BalanceQty)));
                    product.setProductCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductCategoryId)));
                    product.setProductSubCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductSubCategoryId)));
                    product.setDescription(cursor.getString(INDEX_PRODUCT_Description));
                    product.setProductSubCategoryName(cursor.getString(INDEX_PRODUCT_ProductSubCategoryName));
                    product.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_PRODUCT_CreatedBy)));
                    product.setCreatedDtTm(cursor.getString(INDEX_PRODUCT_CreatedDtTm));
                    product.setOutletID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_OutletID)));
                    product.setIsGroupProduct(Integer.parseInt(cursor.getString(INDEX_PRODUCT_IsGroupProduct)));
                    product.setSynced(Integer.parseInt(cursor.getString(INDEX_PRODUCT_Synced)));

                    products.add(product);
                } while (cursor.moveToNext());
            }
            return products;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_products", "" + e);
        }
        return new ArrayList<>();
    }

    public void updateProductList(ArrayList<Product> productList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_PRODUCTS, null, null);
//            db.delete(DATABASE_TABLE, KEY_ROWID + "=" + row, null);
            for (Product product : productList) {
                addProduct(product, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void addNewProduct(Product product) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addProduct(product, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("add new product", "" + e);
        }
    }

    public void updateProduct(Product product, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(PRODUCT_ProductID, product.getProductID());
            values.put(PRODUCT_ProductCode, product.getProductCode());
            values.put(PRODUCT_ProductName, product.getProductName());
            values.put(PRODUCT_BrandID, product.getBrandID());
            values.put(PRODUCT_Brand, product.getBrand());
            values.put(PRODUCT_BarCode, product.getBarcode());
            values.put(PRODUCT_SalesPrice, product.getSalesPrice());
            values.put(PRODUCT_PriceGSTInclusive, product.isPriceGSTInclusive());
            values.put(PRODUCT_IsProductActive, product.isProductActive());
            values.put(PRODUCT_HSNCode, product.getHSNCode());
            values.put(PRODUCT_HSNID, product.getHSNID());
            values.put(PRODUCT_UnitID, product.getUnitID());
            values.put(PRODUCT_Unit, product.getUnit());
            values.put(PRODUCT_Tax, product.getTAX());
            values.put(PRODUCT_BalanceQty, product.getBalanceQty());
            values.put(PRODUCT_ProductCategoryId, product.getProductCategoryId());
            values.put(PRODUCT_ProductSubCategoryId, product.getProductSubCategoryId());
            values.put(PRODUCT_Description, product.getDescription());
            values.put(PRODUCT_ProductSubCategoryName, product.getProductSubCategoryName());
            values.put(PRODUCT_CreatedBy, product.getCreatedBy());
            values.put(PRODUCT_CreatedDtTm, product.getCreatedDtTm());
            values.put(PRODUCT_OutletID, product.getOutletID());
            values.put(PRODUCT_IsGroupProduct, product.getIsGroupProduct());
            values.put(PRODUCT_Synced, product.getSynced());

            // insert into product table
            int id = db.update(TABLE_PRODUCTS, values,
                    PRODUCT_ProductID + " = '" + product.getProductID() + "'",null);
            if(id != 0)
                Log.d("database", "update product");
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("insert customers", "" + e);
        }
    }

    public void updateCurrentProduct(Product product) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            updateProduct(product, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_product", "" + e);
        }
    }

    public boolean isProductCodeAlreadyExists(Product product) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS
                    + " WHERE " + PRODUCT_ProductCode + " = '" + product.getProductCode() + "'"
                    + " AND " + PRODUCT_ProductID + " <> '" + product.getProductID() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("check product", "" + e);
        }
        return false;
    }

    public void singleProductSyncSuccessful(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_Synced, PRODUCT_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_PRODUCTS, values,
                PRODUCT_ProductID + "=" + id ,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void productListSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_Synced, PRODUCT_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_PRODUCTS, values,
                PRODUCT_Synced + "<>" + PRODUCT_SYNCED_CODE_Synced ,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public Product getProductRecordFromDeliveryOrder(TransactionDeliveryOrder transactionDeliveryOrder) {
        Product product = new Product();
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS
                    + " WHERE " + PRODUCT_ProductID + " = " + transactionDeliveryOrder.getProductID();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() == 0) {
                selectQuery = "SELECT * FROM " + TABLE_PRODUCTS
                        + " WHERE " + PRODUCT_ProductCode + " = '" + transactionDeliveryOrder.getProductCode() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }

            cursor.moveToLast();

            product.setProductID(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductID)));
            product.setProductCode(cursor.getString(INDEX_PRODUCT_ProductCode));
            product.setProductName(cursor.getString(INDEX_PRODUCT_ProductName));
            product.setBrandID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_BrandID)));
            product.setBrand(cursor.getString(INDEX_PRODUCT_Brand));
            product.setBarcode(cursor.getString(INDEX_PRODUCT_BarCode));
            product.setSalesPrice(Double.parseDouble(cursor.getString(INDEX_PRODUCT_SalesPrice)));
            product.setPriceGSTInclusive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_PriceGSTInclusive)));
            product.setProductActive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_IsProductActive)));
            product.setHSNCode(cursor.getString(INDEX_PRODUCT_HSNCode));
            product.setHSNID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_HSNID)));
            product.setUnitID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_UnitID)));
            product.setUnit(cursor.getString(INDEX_PRODUCT_Unit));
            product.setTAX(Double.parseDouble(cursor.getString(INDEX_PRODUCT_Tax)));
            product.setBalanceQty(Double.parseDouble(cursor.getString(INDEX_PRODUCT_BalanceQty)));
            product.setProductCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductCategoryId)));
            product.setProductSubCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductSubCategoryId)));
            product.setDescription(cursor.getString(INDEX_PRODUCT_Description));
            product.setProductSubCategoryName(cursor.getString(INDEX_PRODUCT_ProductSubCategoryName));
            product.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_PRODUCT_CreatedBy)));
            product.setCreatedDtTm(cursor.getString(INDEX_PRODUCT_CreatedDtTm));
            product.setOutletID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_OutletID)));
            product.setIsGroupProduct(Integer.parseInt(cursor.getString(INDEX_PRODUCT_IsGroupProduct)));
            product.setSynced(Integer.parseInt(cursor.getString(INDEX_PRODUCT_Synced)));

        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        return product;

    }

    public Product getProductRecord(long productID) {
        Product product = new Product();
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS
                    + " WHERE " + PRODUCT_ProductID + " = " + productID;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToLast();

            product.setProductID(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductID)));
            product.setProductCode(cursor.getString(INDEX_PRODUCT_ProductCode));
            product.setProductName(cursor.getString(INDEX_PRODUCT_ProductName));
            product.setBrandID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_BrandID)));
            product.setBrand(cursor.getString(INDEX_PRODUCT_Brand));
            product.setBarcode(cursor.getString(INDEX_PRODUCT_BarCode));
            product.setSalesPrice(Double.parseDouble(cursor.getString(INDEX_PRODUCT_SalesPrice)));
            product.setPriceGSTInclusive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_PriceGSTInclusive)));
            product.setProductActive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_IsProductActive)));
            product.setHSNCode(cursor.getString(INDEX_PRODUCT_HSNCode));
            product.setHSNID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_HSNID)));
            product.setUnitID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_UnitID)));
            product.setUnit(cursor.getString(INDEX_PRODUCT_Unit));
            product.setTAX(Double.parseDouble(cursor.getString(INDEX_PRODUCT_Tax)));
            product.setBalanceQty(Double.parseDouble(cursor.getString(INDEX_PRODUCT_BalanceQty)));
            product.setProductCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductCategoryId)));
            product.setProductSubCategoryId(Long.parseLong(cursor.getString(INDEX_PRODUCT_ProductSubCategoryId)));
            product.setDescription(cursor.getString(INDEX_PRODUCT_Description));
            product.setProductSubCategoryName(cursor.getString(INDEX_PRODUCT_ProductSubCategoryName));
            product.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_PRODUCT_CreatedBy)));
            product.setCreatedDtTm(cursor.getString(INDEX_PRODUCT_CreatedDtTm));
            product.setOutletID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_OutletID)));
            product.setIsGroupProduct(Integer.parseInt(cursor.getString(INDEX_PRODUCT_IsGroupProduct)));
            product.setSynced(Integer.parseInt(cursor.getString(INDEX_PRODUCT_Synced)));

        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        return product;

    }

    // Add new customer
    public void addCustomer(Customer customer, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(CUSTOMER_CustomerId, customer.getCustomerId());
            values.put(CUSTOMER_EntityType, customer.getEntityType());
            values.put(CUSTOMER_EntityName, customer.getEntityName());
            values.put(CUSTOMER_CategoryId, customer.getCategoryId());
            values.put(CUSTOMER_CategoryName, customer.getCategoryName());
            values.put(CUSTOMER_ContactPerson, customer.getContactPerson());
            values.put(CUSTOMER_ContactNumber, customer.getContactNumber());
            values.put(CUSTOMER_ContactEmailId, customer.getContactEmailId());
            values.put(CUSTOMER_ContactLandline, customer.getContactLandline());
            values.put(CUSTOMER_GSTNumber, customer.getGSTNumber());
            values.put(CUSTOMER_Address, customer.getAddress());
            values.put(CUSTOMER_StateId, customer.getStateId());
            values.put(CUSTOMER_StateName, customer.getStateName());
            values.put(CUSTOMER_Gender, customer.getGender());
            values.put(CUSTOMER_City, customer.getCity());
            values.put(CUSTOMER_PinCode, customer.getPinCode());
            values.put(CUSTOMER_Synced, customer.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_CUSTOMERS, null, values);
            if(num != -1) {
                Log.d("database", "new customer inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(customer, origin);
            }
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(customer, e, origin);
            Log.e("insert customers", "" + e);
        }
    }

    public ArrayList<Customer> getCustomersListFromDB() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //     String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS + ";";

            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS +" ORDER BY "+CUSTOMER_EntityName+" , "+CUSTOMER_ContactPerson+ ";";

            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<Customer> customers = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setCustomerId(cursor.getString(INDEX_CUSTOMER_CustomerId));
                    customer.setEntityType(cursor.getString(INDEX_CUSTOMER_EntityType));
                    customer.setEntityName(cursor.getString(INDEX_CUSTOMER_EntityName));
                    customer.setCategoryId(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_CategoryId)));
                    customer.setCategoryName(cursor.getString(INDEX_CUSTOMER_CategoryName));
                    customer.setContactPerson(cursor.getString(INDEX_CUSTOMER_ContactPerson));
                    customer.setContactNumber(cursor.getString(INDEX_CUSTOMER_ContactNumber));
                    customer.setContactEmailId(cursor.getString(INDEX_CUSTOMER_ContactEmailId));
                    customer.setContactLandline(cursor.getString(INDEX_CUSTOMER_ContactLandline));
                    customer.setGSTNumber(cursor.getString(INDEX_CUSTOMER_GSTNumber));
                    customer.setAddress(cursor.getString(INDEX_CUSTOMER_Address));
                    customer.setStateId(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_StateId)));
                    customer.setStateName(cursor.getString(INDEX_CUSTOMER_StateName));
                    customer.setGender(cursor.getString(INDEX_CUSTOMER_Gender));
                    customer.setCity(cursor.getString(INDEX_CUSTOMER_City));
                    customer.setPinCode(cursor.getString(INDEX_CUSTOMER_PinCode));
                    customer.setSynced(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_Synced)));

                    customers.add(customer);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return customers;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_customers", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<Customer> getUnsyncedCustomersListFromDB() {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS
                    + " WHERE " + CUSTOMER_Synced + " <> " + CUSTOMER_SYNCED_CODE_Synced;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setCustomerId(cursor.getString(INDEX_CUSTOMER_CustomerId));
                    customer.setEntityType(cursor.getString(INDEX_CUSTOMER_EntityType));
                    customer.setEntityName(cursor.getString(INDEX_CUSTOMER_EntityName));
                    customer.setCategoryId(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_CategoryId)));
                    customer.setCategoryName(cursor.getString(INDEX_CUSTOMER_CategoryName));
                    customer.setContactPerson(cursor.getString(INDEX_CUSTOMER_ContactPerson));
                    customer.setContactNumber(cursor.getString(INDEX_CUSTOMER_ContactNumber));
                    customer.setContactEmailId(cursor.getString(INDEX_CUSTOMER_ContactEmailId));
                    customer.setContactLandline(cursor.getString(INDEX_CUSTOMER_ContactLandline));
                    customer.setGSTNumber(cursor.getString(INDEX_CUSTOMER_GSTNumber));
                    customer.setAddress(cursor.getString(INDEX_CUSTOMER_Address));
                    customer.setStateId(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_StateId)));
                    customer.setStateName(cursor.getString(INDEX_CUSTOMER_StateName));
                    customer.setGender(cursor.getString(INDEX_CUSTOMER_Gender));
                    customer.setCity(cursor.getString(INDEX_CUSTOMER_City));
                    customer.setPinCode(cursor.getString(INDEX_CUSTOMER_PinCode));
                    customer.setSynced(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_Synced)));

                    customers.add(customer);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return customers;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_customers", "" + e);
        }
        return customers;
    }

    public void updateCustomerList(ArrayList<Customer> customerList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_CUSTOMERS, null, null);
            for (Customer customer : customerList) {
                addCustomer(customer, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void addNewCustomer(Customer customer) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addCustomer(customer, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        Log.d("check", customer+"");
    }

    public void updateCustomer(Customer customer, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(CUSTOMER_CustomerId, customer.getCustomerId());
            values.put(CUSTOMER_EntityType, customer.getEntityType());
            values.put(CUSTOMER_EntityName, customer.getEntityName());
            values.put(CUSTOMER_CategoryId, customer.getCategoryId());
            values.put(CUSTOMER_CategoryName, customer.getCategoryName());
            values.put(CUSTOMER_ContactPerson, customer.getContactPerson());
            values.put(CUSTOMER_ContactNumber, customer.getContactNumber());
            values.put(CUSTOMER_ContactEmailId, customer.getContactEmailId());
            values.put(CUSTOMER_ContactLandline, customer.getContactLandline());
            values.put(CUSTOMER_GSTNumber, customer.getGSTNumber());
            values.put(CUSTOMER_Address, customer.getAddress());
            values.put(CUSTOMER_StateId, customer.getStateId());
            values.put(CUSTOMER_StateName, customer.getStateName());
            values.put(CUSTOMER_Gender, customer.getGender());
            values.put(CUSTOMER_City, customer.getCity());
            values.put(CUSTOMER_PinCode, customer.getPinCode());
            values.put(CUSTOMER_Synced, customer.getSynced());

            // insert into customer table
            int id = db.update(TABLE_CUSTOMERS, values,
                    CUSTOMER_CustomerId + " = '" + customer.getCustomerId() + "'",null);
            if(id != 0)
                Log.d("database", "new customer inserted");
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("insert customers", "" + e);
        }
    }

    public void updateCurrentCustomer(Customer customer) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            updateCustomer(customer, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        Log.d("check", customer+"");
    }

    public boolean isCustomerContactNumberAlreadyExists(Customer customer) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS
                    + " WHERE " + CUSTOMER_ContactNumber + " = '" + customer.getContactNumber() + "'"
                    + " AND " + CUSTOMER_CustomerId + " <> '" + customer.getCustomerId() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        return false;
    }

    public void singleCustomerSyncSuccessful(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_Synced, CUSTOMER_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_CUSTOMERS, values,
                CUSTOMER_CustomerId + "='" + id + "'" ,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void customerListSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_Synced, CUSTOMER_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_CUSTOMERS, values,
                CUSTOMER_Synced + "<>" + CUSTOMER_SYNCED_CODE_Synced ,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public Customer getCustomerRecordFromDeliveryOrder(MasterDeliveryOrder masterDeliveryOrder) {
        Customer customer = new Customer();
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS
                    + " WHERE " + CUSTOMER_CustomerId + " = '" + masterDeliveryOrder.getCustomer_ID() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() == 0) {
                selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS
                        + " WHERE " + CUSTOMER_ContactNumber + " = '" + masterDeliveryOrder.getCustomerMobile() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }

            cursor.moveToLast();

            customer.setCustomerId(cursor.getString(INDEX_CUSTOMER_CustomerId));
            customer.setEntityType(cursor.getString(INDEX_CUSTOMER_EntityType));
            customer.setEntityName(cursor.getString(INDEX_CUSTOMER_EntityName));
            customer.setCategoryId(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_CategoryId)));
            customer.setCategoryName(cursor.getString(INDEX_CUSTOMER_CategoryName));
            customer.setContactPerson(cursor.getString(INDEX_CUSTOMER_ContactPerson));
            customer.setContactNumber(cursor.getString(INDEX_CUSTOMER_ContactNumber));
            customer.setContactEmailId(cursor.getString(INDEX_CUSTOMER_ContactEmailId));
            customer.setContactLandline(cursor.getString(INDEX_CUSTOMER_ContactLandline));
            customer.setGSTNumber(cursor.getString(INDEX_CUSTOMER_GSTNumber));
            customer.setAddress(cursor.getString(INDEX_CUSTOMER_Address));
            customer.setStateId(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_StateId)));
            customer.setStateName(cursor.getString(INDEX_CUSTOMER_StateName));
            customer.setGender(cursor.getString(INDEX_CUSTOMER_Gender));
            customer.setCity(cursor.getString(INDEX_CUSTOMER_City));
            customer.setPinCode(cursor.getString(INDEX_CUSTOMER_PinCode));
            customer.setSynced(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_Synced)));

        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        return customer;
    }

    public Customer getCustomerRecordFromSales(MasterSales masterSales) {

        Customer customer = new Customer();
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS
                    + " WHERE " + CUSTOMER_CustomerId + " = '" + masterSales.getCustomerID() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() == 0 && MyFunctions.StringLength(masterSales.getCustomerMobile()) > 0) {
                selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS
                        + " WHERE " + CUSTOMER_ContactNumber + " = '" + masterSales.getCustomerMobile() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }

            if (cursor.moveToLast()) {
                customer.setCustomerId(cursor.getString(INDEX_CUSTOMER_CustomerId));
                customer.setEntityType(cursor.getString(INDEX_CUSTOMER_EntityType));
                customer.setEntityName(cursor.getString(INDEX_CUSTOMER_EntityName));
                customer.setCategoryId(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_CategoryId)));
                customer.setCategoryName(cursor.getString(INDEX_CUSTOMER_CategoryName));
                customer.setContactPerson(cursor.getString(INDEX_CUSTOMER_ContactPerson));
                customer.setContactNumber(cursor.getString(INDEX_CUSTOMER_ContactNumber));
                customer.setContactEmailId(cursor.getString(INDEX_CUSTOMER_ContactEmailId));
                customer.setContactLandline(cursor.getString(INDEX_CUSTOMER_ContactLandline));
                customer.setGSTNumber(cursor.getString(INDEX_CUSTOMER_GSTNumber));
                customer.setAddress(cursor.getString(INDEX_CUSTOMER_Address));
                customer.setStateId(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_StateId)));
                customer.setStateName(cursor.getString(INDEX_CUSTOMER_StateName));
                customer.setGender(cursor.getString(INDEX_CUSTOMER_Gender));
                customer.setCity(cursor.getString(INDEX_CUSTOMER_City));
                customer.setPinCode(cursor.getString(INDEX_CUSTOMER_PinCode));
                customer.setSynced(Integer.parseInt(cursor.getString(INDEX_CUSTOMER_Synced)));
            } else {
                return null;
            }

        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        return customer;
    }

    // Add new supplier
    public void addSupplier(Supplier supplier, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(SUPPLIER_SupplierId, supplier.getSupplierId());
            values.put(SUPPLIER_EntityType, supplier.getEntityType());
            values.put(SUPPLIER_EntityName, supplier.getEntityName());
            values.put(SUPPLIER_CategoryId, supplier.getCategoryId());
            values.put(SUPPLIER_CategoryName, supplier.getCategoryName());
            values.put(SUPPLIER_ContactPerson, supplier.getContactPerson());
            values.put(SUPPLIER_ContactNumber, supplier.getContactNumber());
            values.put(SUPPLIER_ContactEmailId, supplier.getContactEmailId());
            values.put(SUPPLIER_ContactLandline, supplier.getContactLandline());
            values.put(SUPPLIER_GSTNumber, supplier.getGSTNumber());
            values.put(SUPPLIER_Address, supplier.getAddress());
            values.put(SUPPLIER_StateId, supplier.getStateId());
            values.put(SUPPLIER_StateName, supplier.getStateName());
            values.put(SUPPLIER_Gender, supplier.getGender());
            values.put(SUPPLIER_City, supplier.getCity());
            values.put(SUPPLIER_PinCode, supplier.getPinCode());
            values.put(SUPPLIER_Synced, supplier.getSynced());

            // insert into supplier table
            long num = db.insert(TABLE_SUPPLIERS, null, values);
            if(num != -1) {
                Log.d("database", "new product inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(supplier, origin);
            }
//            supplierList.add(supplier);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(supplier, e, origin);
            Log.e("insert suppliers", "" + e);
        }
    }

    public ArrayList<Supplier> getSuppliersListFromDB() {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_SUPPLIERS + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    Supplier supplier = new Supplier();
                    supplier.setSupplierId(cursor.getString(INDEX_SUPPLIER_SupplierId));
                    supplier.setEntityType(cursor.getString(INDEX_SUPPLIER_EntityType));
                    supplier.setEntityName(cursor.getString(INDEX_SUPPLIER_EntityName));
                    supplier.setCategoryId(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_CategoryId)));
                    supplier.setCategoryName(cursor.getString(INDEX_SUPPLIER_CategoryName));
                    supplier.setContactPerson(cursor.getString(INDEX_SUPPLIER_ContactPerson));
                    supplier.setContactNumber(cursor.getString(INDEX_SUPPLIER_ContactNumber));
                    supplier.setContactEmailId(cursor.getString(INDEX_SUPPLIER_ContactEmailId));
                    supplier.setContactLandline(cursor.getString(INDEX_SUPPLIER_ContactLandline));
                    supplier.setGSTNumber(cursor.getString(INDEX_SUPPLIER_GSTNumber));
                    supplier.setAddress(cursor.getString(INDEX_SUPPLIER_Address));
                    supplier.setStateId(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_StateId)));
                    supplier.setStateName(cursor.getString(INDEX_SUPPLIER_StateName));
                    supplier.setGender(cursor.getString(INDEX_SUPPLIER_Gender));
                    supplier.setCity(cursor.getString(INDEX_SUPPLIER_City));
                    supplier.setPinCode(cursor.getString(INDEX_SUPPLIER_PinCode));
                    supplier.setSynced(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_Synced)));

                    suppliers.add(supplier);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return suppliers;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_suppliers", "" + e);
        }
        return suppliers;
    }

    public ArrayList<Supplier> getUnsyncedSuppliersListFromDB() {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_SUPPLIERS
                    + " WHERE " + SUPPLIER_Synced + " <> " + SUPPLIER_SYNCED_CODE_Synced;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    Supplier supplier = new Supplier();
                    supplier.setSupplierId(cursor.getString(INDEX_SUPPLIER_SupplierId));
                    supplier.setEntityType(cursor.getString(INDEX_SUPPLIER_EntityType));
                    supplier.setEntityName(cursor.getString(INDEX_SUPPLIER_EntityName));
                    supplier.setCategoryId(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_CategoryId)));
                    supplier.setCategoryName(cursor.getString(INDEX_SUPPLIER_CategoryName));
                    supplier.setContactPerson(cursor.getString(INDEX_SUPPLIER_ContactPerson));
                    supplier.setContactNumber(cursor.getString(INDEX_SUPPLIER_ContactNumber));
                    supplier.setContactEmailId(cursor.getString(INDEX_SUPPLIER_ContactEmailId));
                    supplier.setContactLandline(cursor.getString(INDEX_SUPPLIER_ContactLandline));
                    supplier.setGSTNumber(cursor.getString(INDEX_SUPPLIER_GSTNumber));
                    supplier.setAddress(cursor.getString(INDEX_SUPPLIER_Address));
                    supplier.setStateId(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_StateId)));
                    supplier.setStateName(cursor.getString(INDEX_SUPPLIER_StateName));
                    supplier.setGender(cursor.getString(INDEX_SUPPLIER_Gender));
                    supplier.setCity(cursor.getString(INDEX_SUPPLIER_City));
                    supplier.setPinCode(cursor.getString(INDEX_SUPPLIER_PinCode));
                    supplier.setSynced(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_Synced)));

                    suppliers.add(supplier);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return suppliers;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_suppliers", "" + e);
        }
        return suppliers;
    }

    public void updateSupplier(Supplier supplier, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(SUPPLIER_SupplierId, supplier.getSupplierId());
            values.put(SUPPLIER_EntityType, supplier.getEntityType());
            values.put(SUPPLIER_EntityName, supplier.getEntityName());
            values.put(SUPPLIER_CategoryId, supplier.getCategoryId());
            values.put(SUPPLIER_CategoryName, supplier.getCategoryName());
            values.put(SUPPLIER_ContactPerson, supplier.getContactPerson());
            values.put(SUPPLIER_ContactNumber, supplier.getContactNumber());
            values.put(SUPPLIER_ContactEmailId, supplier.getContactEmailId());
            values.put(SUPPLIER_ContactLandline, supplier.getContactLandline());
            values.put(SUPPLIER_GSTNumber, supplier.getGSTNumber());
            values.put(SUPPLIER_Address, supplier.getAddress());
            values.put(SUPPLIER_StateId, supplier.getStateId());
            values.put(SUPPLIER_StateName, supplier.getStateName());
            values.put(SUPPLIER_Gender, supplier.getGender());
            values.put(SUPPLIER_City, supplier.getCity());
            values.put(SUPPLIER_PinCode, supplier.getPinCode());
            values.put(SUPPLIER_Synced, supplier.getSynced());

            // insert into customer table
            int id = db.update(TABLE_SUPPLIERS, values,
                    SUPPLIER_SupplierId + " = '" + supplier.getSupplierId() + "'",null);
            if(id != 0)
                Log.d("database", "new customer inserted");
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("insert customers", "" + e);
        }
    }

    public void updateCurrentSupplier(Supplier supplier) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            updateSupplier(supplier, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        Log.d("check", supplier+"");
    }

    public void addNewSupplier(Supplier supplier) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addSupplier(supplier, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_supplier", "" + e);
        }
        Log.d("check", supplier+"");
    }

    public void updateSupplierList(ArrayList<Supplier> supplierList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_SUPPLIERS, null, null);
            for (Supplier supplier : supplierList) {
                addSupplier(supplier, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_suppliers", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public boolean isSupplierContactNumberAlreadyExists(Supplier supplier) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_SUPPLIERS
                    + " WHERE " + SUPPLIER_ContactNumber + " = '" + supplier.getContactNumber() + "'"
                    + " AND " + SUPPLIER_SupplierId + " <> '" + supplier.getSupplierId() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        return false;
    }

    public void singleSupplierSyncSuccessful(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUPPLIER_Synced, SUPPLIER_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_SUPPLIERS, values,
                SUPPLIER_Synced + "='" + id +"'" ,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void supplierListSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUPPLIER_Synced, SUPPLIER_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_SUPPLIERS, values,
                SUPPLIER_Synced + "<>" + SUPPLIER_SYNCED_CODE_Synced ,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public Supplier getSupplierRecordFromPurchase(MasterPurchase masterPurchase) {

        Supplier supplier = new Supplier();
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_SUPPLIERS
                    + " WHERE " + SUPPLIER_SupplierId + " = '" + masterPurchase.getSupplierID() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() == 0 && MyFunctions.StringLength(masterPurchase.getSupplierMobile()) > 0) {
                selectQuery = "SELECT * FROM " + TABLE_SUPPLIERS
                        + " WHERE " + SUPPLIER_ContactNumber + " = '" + masterPurchase.getSupplierMobile() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }

            if (cursor.moveToLast()) {
                supplier.setSupplierId(cursor.getString(INDEX_SUPPLIER_SupplierId));
                supplier.setEntityType(cursor.getString(INDEX_SUPPLIER_EntityType));
                supplier.setEntityName(cursor.getString(INDEX_SUPPLIER_EntityName));
                supplier.setCategoryId(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_CategoryId)));
                supplier.setCategoryName(cursor.getString(INDEX_SUPPLIER_CategoryName));
                supplier.setContactPerson(cursor.getString(INDEX_SUPPLIER_ContactPerson));
                supplier.setContactNumber(cursor.getString(INDEX_SUPPLIER_ContactNumber));
                supplier.setContactEmailId(cursor.getString(INDEX_SUPPLIER_ContactEmailId));
                supplier.setContactLandline(cursor.getString(INDEX_SUPPLIER_ContactLandline));
                supplier.setGSTNumber(cursor.getString(INDEX_SUPPLIER_GSTNumber));
                supplier.setAddress(cursor.getString(INDEX_SUPPLIER_Address));
                supplier.setStateId(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_StateId)));
                supplier.setStateName(cursor.getString(INDEX_SUPPLIER_StateName));
                supplier.setGender(cursor.getString(INDEX_SUPPLIER_Gender));
                supplier.setCity(cursor.getString(INDEX_SUPPLIER_City));
                supplier.setPinCode(cursor.getString(INDEX_SUPPLIER_PinCode));
                supplier.setSynced(Integer.parseInt(cursor.getString(INDEX_SUPPLIER_Synced)));

                return supplier;
            } else {
                return null;
            }

        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_customers", "" + e);
        }
        return null;
    }

    public static DatabaseHandler getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseHandler(DatabaseHandler.context);
        }
        return INSTANCE;
    }

    // sales
    public long addMasterSales(MasterSales masterSales, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            values.put(M_SALES_SalesID, masterSales.getSalesID());
            values.put(M_SALES_SalesReceiptNumber, masterSales.getSalesReceiptNumber());
            values.put(M_SALES_CustomerID, masterSales.getCustomerID());
            values.put(M_SALES_BaseAmount, masterSales.getBaseAmount());
            values.put(M_SALES_TotalDiscountAmount, masterSales.getTotalDiscountAmount());
            values.put(M_SALES_TaxAmount, masterSales.getTaxAmount());
            values.put(M_SALES_TotalAmount, masterSales.getTotalAmount());
            values.put(M_SALES_ReferenceNumber, masterSales.getReferenceNumber());
            values.put(M_SALES_LoginID, masterSales.getLoginID());
            values.put(M_SALES_SalesDtTm, masterSales.getSalesDtTm());
            values.put(M_SALES_Gender, masterSales.getGender());
            values.put(M_SALES_isHold, masterSales.isHold());
            values.put(M_SALES_isActive, masterSales.isActive());
            values.put(M_SALES_OutletID, masterSales.getOutletID());
            values.put(M_SALES_CustomerType, masterSales.getCustomerType());
            values.put(M_SALES_Balance, masterSales.getBalance());
            values.put(M_SALES_PaidAmount, masterSales.getPaidAmount());
            values.put(M_SALES_TaxOnPaidAmount, masterSales.getTaxOnPaidAmount());
            values.put(M_SALES_CGST, masterSales.getCGST());
            values.put(M_SALES_SGST, masterSales.getSGST());
            values.put(M_SALES_IGST, masterSales.getIGST());
            values.put(M_SALES_DiscountTypeID, masterSales.getDiscountTypeID());
            values.put(M_SALES_Discount, masterSales.getDiscount());
            values.put(M_SALES_CESS, masterSales.getCESS());
            values.put(M_SALES_DueDate, masterSales.getDueDate());
            values.put(M_SALES_CustomerName, masterSales.getCustomerName());
            values.put(M_SALES_TermsCondition, masterSales.getTermsCondition());
            values.put(M_SALES_CustomerGSTN, masterSales.getCustomerGSTN());
            values.put(M_SALES_CustomerEmail, masterSales.getCustomerEmail());
            values.put(M_SALES_CustomerMobile, masterSales.getCustomerMobile());
            values.put(M_SALES_CustomerAddress, masterSales.getCustomerAddress());
            values.put(M_SALES_OutletAddress, masterSales.getOutletAddress());
            values.put(M_SALES_OutletLogo, masterSales.getOutletLogo());
            values.put(M_SALES_OutletGSTN, masterSales.getOutletGSTN());
            values.put(M_SALES_ShipAddress, masterSales.getShipAddress());
            values.put(M_SALES_TransportCharge, masterSales.getTransportCharge());
            values.put(M_SALES_InsuranceCharge, masterSales.getInsuranceCharge());
            values.put(M_SALES_PackingCharge, masterSales.getPackingCharge());
            values.put(M_SALES_BillAddress, masterSales.getBillAddress());
            values.put(M_SALES_SalesDate, masterSales.getSalesDate());
            values.put(M_SALES_SubTotal, masterSales.getSubTotal());
            values.put(M_SALES_eWayBillRequired, masterSales.geteWayBillRequired());
            values.put(M_SALES_ShippingStateID, masterSales.getShippingStateID());
            values.put(M_SALES_DONumber, masterSales.getDONumber());
            values.put(M_SALES_Synced, masterSales.getSynced());

            // insert into master sales table
            long num = db.insert(TABLE_M_SALES_REGISTER, null, values);
            if(num != -1) {
                Log.d("database", "new master sales record inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(masterSales, origin);
            }
            return num;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(masterSales, e, origin);
            Log.e("insert master sales", "" + e);
        }
        return -1;
    }

    public void addTransactionSales (TransactionSales transactionSales, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            values.put(T_SALES_SalesSubID, transactionSales.getSalesSubID());
            values.put(T_SALES_SalesID, transactionSales.getSalesID());
            values.put(T_SALES_SalesPersonID, transactionSales.getSalesPersonID());
            values.put(T_SALES_ItemTypeID, transactionSales.getItemTypeID());
            values.put(T_SALES_ItemID, transactionSales.getItemID());
            values.put(T_SALES_Qty, transactionSales.getQty());
            values.put(T_SALES_RefNumber, transactionSales.getRefNumber());
            values.put(T_SALES_BaseAmount, transactionSales.getBaseAmount());
            values.put(T_SALES_Discount, transactionSales.getDiscount());
            values.put(T_SALES_TaxAmount, transactionSales.getTaxAmount());
            values.put(T_SALES_TotalAmount, transactionSales.getTotalAmount());
            values.put(T_SALES_SalesCommission, transactionSales.getSalesCommission());
            values.put(T_SALES_CommissionTypeID, transactionSales.getCommissionTypeID());
            values.put(T_SALES_Outlet_SalesSubID, transactionSales.getOutlet_SalesSubID());
            values.put(T_SALES_DiscountType, transactionSales.getDiscountType());
            values.put(T_SALES_CGST, transactionSales.getCGST());
            values.put(T_SALES_SGST, transactionSales.getSGST());
            values.put(T_SALES_IGST, transactionSales.getIGST());
            values.put(T_SALES_CESS, transactionSales.getCESS());
            values.put(T_SALES_ProductCode, transactionSales.getProductCode());
            values.put(T_SALES_ItemName, transactionSales.getItemName());
            values.put(T_SALES_TaxRate, transactionSales.getTaxRate());
            values.put(T_SALES_GroupID, transactionSales.getGroupID());
            values.put(T_SALES_Synced, transactionSales.getSynced());

            // insert into t sales table
            long num = db.insert(TABLE_T_SALES_REGISTER, null, values);
            if(num != -1) {
                Log.d("database", "new transaction sales record inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(transactionSales, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(transactionSales, e, origin);
            Log.e("insert t sales", "" + e);
        }
    }

    public void updateMasterSalesList(ArrayList<MasterSales> masterSalesArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_M_SALES_REGISTER, M_SALES_Synced + " = " + M_SALES_SYNCED_CODE_Synced, null);
            for (MasterSales masterSales : masterSalesArrayList) {
                addMasterSales(masterSales, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_mastersales", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void singleSalesInvoiceSyncSuccessful(String invoiceNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_SALES_Synced, M_SALES_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_SALES_REGISTER, values,
                M_SALES_ReferenceNumber + "='" + invoiceNumber + "'",null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void salesInvoiceSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_SALES_Synced, M_SALES_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_SALES_REGISTER, values,
                M_SALES_Synced + "=" + M_SALES_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public ArrayList<MasterSales> getInvoiceListFromDB() {
        ArrayList<MasterSales> masterSalesArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_REGISTER + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToLast()) {
                do {
                    MasterSales masterSales = new MasterSales();
                    masterSales.setSalesID(Double.parseDouble(cursor.getString(INDEX_M_SALES_SalesID)));
                    masterSales.setSalesReceiptNumber(cursor.getString(INDEX_M_SALES_SalesReceiptNumber));
                    masterSales.setCustomerID(cursor.getString(INDEX_M_SALES_CustomerID));
                    masterSales.setBaseAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_BaseAmount)));
                    masterSales.setTotalDiscountAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TotalDiscountAmount)));
                    masterSales.setTaxAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TaxAmount)));
                    masterSales.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TotalAmount)));
                    masterSales.setReferenceNumber(cursor.getString(INDEX_M_SALES_ReferenceNumber));
                    masterSales.setLoginID(Double.parseDouble(cursor.getString(INDEX_M_SALES_LoginID)));
                    masterSales.setSalesDtTm(cursor.getString(INDEX_M_SALES_SalesDtTm));
                    masterSales.setGender(cursor.getString(INDEX_M_SALES_Gender));
                    masterSales.setHold(Boolean.parseBoolean(cursor.getString(INDEX_M_SALES_isHold)));
                    masterSales.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_SALES_isActive)));
                    masterSales.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_SALES_OutletID)));
                    masterSales.setCustomerType(cursor.getString(INDEX_M_SALES_CustomerType));
                    masterSales.setBalance(Double.parseDouble(cursor.getString(INDEX_M_SALES_Balance)));
                    masterSales.setPaidAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_PaidAmount)));
                    masterSales.setTaxOnPaidAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TaxOnPaidAmount)));
                    masterSales.setCGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_CGST)));
                    masterSales.setSGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_SGST)));
                    masterSales.setIGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_IGST)));
                    masterSales.setDiscountTypeID(Integer.parseInt(cursor.getString(INDEX_M_SALES_DiscountTypeID)));
                    masterSales.setDiscount(Double.parseDouble(cursor.getString(INDEX_M_SALES_Discount)));
                    masterSales.setCESS(Double.parseDouble(cursor.getString(INDEX_M_SALES_CESS)));
                    masterSales.setDueDate(cursor.getString(INDEX_M_SALES_DueDate));
                    masterSales.setCustomerName(cursor.getString(INDEX_M_SALES_CustomerName));
                    masterSales.setTermsCondition(cursor.getString(INDEX_M_SALES_TermsCondition));
                    masterSales.setCustomerGSTN(cursor.getString(INDEX_M_SALES_CustomerGSTN));
                    masterSales.setCustomerEmail(cursor.getString(INDEX_M_SALES_CustomerEmail));
                    masterSales.setCustomerMobile(cursor.getString(INDEX_M_SALES_CustomerMobile));
                    masterSales.setCustomerAddress(cursor.getString(INDEX_M_SALES_CustomerAddress));
                    masterSales.setOutletAddress(cursor.getString(INDEX_M_SALES_OutletAddress));
                    masterSales.setOutletLogo(cursor.getString(INDEX_M_SALES_OutletLogo));
                    masterSales.setOutletGSTN(cursor.getString(INDEX_M_SALES_OutletGSTN));
                    masterSales.setShipAddress(cursor.getString(INDEX_M_SALES_ShipAddress));
                    masterSales.setTransportCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_TransportCharge)));
                    masterSales.setInsuranceCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_InsuranceCharge)));
                    masterSales.setPackingCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_PackingCharge)));
                    masterSales.setBillAddress(cursor.getString(INDEX_M_SALES_BillAddress));
                    masterSales.setSalesDate(cursor.getString(INDEX_M_SALES_SalesDate));
                    masterSales.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_SALES_SubTotal)));
                    masterSales.seteWayBillRequired(Integer.parseInt(cursor.getString(INDEX_M_SALES_eWayBillRequired)));
                    masterSales.setShippingStateID(Integer.parseInt(cursor.getString(INDEX_M_SALES_ShippingStateID)));
                    masterSales.setDONumber(cursor.getString(INDEX_M_SALES_DONumber));
                    masterSales.setSynced(Integer.parseInt(cursor.getString(INDEX_M_SALES_Synced)));

                    masterSalesArrayList.add(masterSales);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return masterSalesArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_sales", "" + e);
        }
        return masterSalesArrayList;
    }

    public ArrayList<MasterSales> getUnsyncedInvoiceListFromDB() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_REGISTER
            + " WHERE " + M_SALES_Synced + " = " + DatabaseHandler.M_SALES_SYNCED_CODE_Unsynced + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<MasterSales> masterSalesArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    MasterSales masterSales = new MasterSales();
                    masterSales.setSalesID(Double.parseDouble(cursor.getString(INDEX_M_SALES_SalesID)));
                    masterSales.setSalesReceiptNumber(cursor.getString(INDEX_M_SALES_SalesReceiptNumber));
                    masterSales.setCustomerID(cursor.getString(INDEX_M_SALES_CustomerID));
                    masterSales.setBaseAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_BaseAmount)));
                    masterSales.setTotalDiscountAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TotalDiscountAmount)));
                    masterSales.setTaxAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TaxAmount)));
                    masterSales.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TotalAmount)));
                    masterSales.setReferenceNumber(cursor.getString(INDEX_M_SALES_ReferenceNumber));
                    masterSales.setLoginID(Double.parseDouble(cursor.getString(INDEX_M_SALES_LoginID)));
                    masterSales.setSalesDtTm(cursor.getString(INDEX_M_SALES_SalesDtTm));
                    masterSales.setGender(cursor.getString(INDEX_M_SALES_Gender));
                    masterSales.setHold(Boolean.parseBoolean(cursor.getString(INDEX_M_SALES_isHold)));
                    masterSales.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_SALES_isActive)));
                    masterSales.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_SALES_OutletID)));
                    masterSales.setCustomerType(cursor.getString(INDEX_M_SALES_CustomerType));
                    masterSales.setBalance(Double.parseDouble(cursor.getString(INDEX_M_SALES_Balance)));
                    masterSales.setPaidAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_PaidAmount)));
                    masterSales.setTaxOnPaidAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TaxOnPaidAmount)));
                    masterSales.setCGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_CGST)));
                    masterSales.setSGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_SGST)));
                    masterSales.setIGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_IGST)));
                    masterSales.setDiscountTypeID(Integer.parseInt(cursor.getString(INDEX_M_SALES_DiscountTypeID)));
                    masterSales.setDiscount(Double.parseDouble(cursor.getString(INDEX_M_SALES_Discount)));
                    masterSales.setCESS(Double.parseDouble(cursor.getString(INDEX_M_SALES_CESS)));
                    masterSales.setDueDate(cursor.getString(INDEX_M_SALES_DueDate));
                    masterSales.setCustomerName(cursor.getString(INDEX_M_SALES_CustomerName));
                    masterSales.setTermsCondition(cursor.getString(INDEX_M_SALES_TermsCondition));
                    masterSales.setCustomerGSTN(cursor.getString(INDEX_M_SALES_CustomerGSTN));
                    masterSales.setCustomerEmail(cursor.getString(INDEX_M_SALES_CustomerEmail));
                    masterSales.setCustomerMobile(cursor.getString(INDEX_M_SALES_CustomerMobile));
                    masterSales.setCustomerAddress(cursor.getString(INDEX_M_SALES_CustomerAddress));
                    masterSales.setOutletAddress(cursor.getString(INDEX_M_SALES_OutletAddress));
                    masterSales.setOutletLogo(cursor.getString(INDEX_M_SALES_OutletLogo));
                    masterSales.setOutletGSTN(cursor.getString(INDEX_M_SALES_OutletGSTN));
                    masterSales.setShipAddress(cursor.getString(INDEX_M_SALES_ShipAddress));
                    masterSales.setTransportCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_TransportCharge)));
                    masterSales.setInsuranceCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_InsuranceCharge)));
                    masterSales.setPackingCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_PackingCharge)));
                    masterSales.setBillAddress(cursor.getString(INDEX_M_SALES_BillAddress));
                    masterSales.setSalesDate(cursor.getString(INDEX_M_SALES_SalesDate));
                    masterSales.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_SALES_SubTotal)));
                    masterSales.seteWayBillRequired(Integer.parseInt(cursor.getString(INDEX_M_SALES_eWayBillRequired)));
                    masterSales.setShippingStateID(Integer.parseInt(cursor.getString(INDEX_M_SALES_ShippingStateID)));
                    masterSales.setDONumber(cursor.getString(INDEX_M_SALES_DONumber));
                    masterSales.setSynced(Integer.parseInt(cursor.getString(INDEX_M_SALES_Synced)));

                    masterSalesArrayList.add(masterSales);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return masterSalesArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_sales", "" + e);
        }
        return new ArrayList<MasterSales>();
    }

    public ArrayList<TransactionSales> getInvoiceDetailsFromDB(String invoiceNumber) {
        ArrayList<TransactionSales> transactionSalesArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_T_SALES_REGISTER
                    + " WHERE " + T_SALES_RefNumber + " = '" + invoiceNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    TransactionSales transactionSales = new TransactionSales();
                    transactionSales.setSalesSubID(Double.parseDouble(cursor.getString(INDEX_T_SALES_SalesSubID)));
                    transactionSales.setSalesID(Double.parseDouble(cursor.getString(INDEX_T_SALES_SalesID)));
                    transactionSales.setSalesPersonID(Double.parseDouble(cursor.getString(INDEX_T_SALES_SalesPersonID)));
                    transactionSales.setItemTypeID(Integer.parseInt(cursor.getString(INDEX_T_SALES_ItemTypeID)));
                    transactionSales.setItemID(Long.parseLong(cursor.getString(INDEX_T_SALES_ItemID)));
                    transactionSales.setQty(Integer.parseInt(cursor.getString(INDEX_T_SALES_Qty)));
                    transactionSales.setRefNumber(cursor.getString(INDEX_T_SALES_RefNumber));
                    transactionSales.setBaseAmount(Double.parseDouble(cursor.getString(INDEX_T_SALES_BaseAmount)));
                    transactionSales.setDiscount(Double.parseDouble(cursor.getString(INDEX_T_SALES_Discount)));
                    transactionSales.setTaxAmount(Double.parseDouble(cursor.getString(INDEX_T_SALES_TaxAmount)));
                    transactionSales.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_T_SALES_TotalAmount)));
                    transactionSales.setSalesCommission(Double.parseDouble(cursor.getString(INDEX_T_SALES_SalesCommission)));
                    transactionSales.setCommissionTypeID(Integer.parseInt(cursor.getString(INDEX_T_SALES_CommissionTypeID)));
                    transactionSales.setOutlet_SalesSubID(Double.parseDouble(cursor.getString(INDEX_T_SALES_Outlet_SalesSubID)));
                    transactionSales.setDiscountType(Integer.parseInt(cursor.getString(INDEX_T_SALES_DiscountType)));
                    transactionSales.setCGST(Double.parseDouble(cursor.getString(INDEX_T_SALES_CGST)));
                    transactionSales.setSGST(Double.parseDouble(cursor.getString(INDEX_T_SALES_SGST)));
                    transactionSales.setIGST(Double.parseDouble(cursor.getString(INDEX_T_SALES_IGST)));
                    transactionSales.setCESS(Double.parseDouble(cursor.getString(INDEX_T_SALES_CESS)));
                    transactionSales.setProductCode(cursor.getString(INDEX_T_SALES_ProductCode));
                    transactionSales.setItemName(cursor.getString(INDEX_T_SALES_ItemName));
                    transactionSales.setTaxRate(Double.parseDouble(cursor.getString(INDEX_T_SALES_TaxRate)));
                    transactionSales.setGroupID(Integer.parseInt(cursor.getString(INDEX_T_SALES_GroupID)));
                    transactionSales.setSynced(Integer.parseInt(cursor.getString(INDEX_T_SALES_Synced)));

                    transactionSalesArrayList.add(transactionSales);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return transactionSalesArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_transaction_sales", "" + e);
        }
        return transactionSalesArrayList;
    }

    public String getPreviousInvoiceNumber(String currentInvoiceNumberInitialString) {
        try {
            String query = "SELECT " + M_SALES_SalesReceiptNumber + " FROM " + TABLE_M_SALES_REGISTER
                    + " WHERE " + M_SALES_SalesReceiptNumber + " LIKE '" + currentInvoiceNumberInitialString + "%'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0) {
                db.close();
                return null;
            } else {
                cursor.moveToLast();
                String previousInvoiceNumber = cursor.getString(0);
                cursor.close();
                db.close();
                return previousInvoiceNumber;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_invoice_number", "" + e);
        }
        // it is okay to send null
        return null;
    }

    public double getRecentMasterSalesID(String invoiceNumber, SQLiteDatabase db) {
        try {
            String query = "SELECT " + M_SALES_SalesID + " FROM " + TABLE_M_SALES_REGISTER
                    + " WHERE " + M_SALES_SalesReceiptNumber + " = '" + invoiceNumber + "'";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToLast();
            String SalesID = cursor.getString(0);
            cursor.close();
            return Double.parseDouble(SalesID);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_invoice_number", "" + e);
        }
        return 0;
    }

    public void addTransactionSalesList(ArrayList<TransactionSales> transactionSalesArrayList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (TransactionSales tSales : transactionSalesArrayList) {
                addTransactionSales(tSales, db);
            }
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
        Log.d("check", transactionSalesArrayList+"");
    }

    public void updateTransactionSalesList(ArrayList<TransactionSales> transactionSalesArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_T_SALES_REGISTER, null, null);
            for (TransactionSales tSales : transactionSalesArrayList) {
                addTransactionSales(tSales, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public double insertMasterSales(MasterSales masterSales) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addMasterSales(masterSales, db);
            double SalesID = getRecentMasterSalesID(masterSales.getReferenceNumber(), db);
            db.close();
            return SalesID;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
        Log.d("check", masterSales+"");
        return 0;
    }

    public MasterSales getMasterSalesRecord(String invoiceNumber) {
        MasterSales masterSales = new MasterSales();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_REGISTER + " WHERE " + M_SALES_ReferenceNumber + " = '" + invoiceNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                masterSales.setSalesID(Double.parseDouble(cursor.getString(INDEX_M_SALES_SalesID)));
                masterSales.setSalesReceiptNumber(cursor.getString(INDEX_M_SALES_SalesReceiptNumber));
                masterSales.setCustomerID(cursor.getString(INDEX_M_SALES_CustomerID));
                masterSales.setBaseAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_BaseAmount)));
                masterSales.setTotalDiscountAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TotalDiscountAmount)));
                masterSales.setTaxAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TaxAmount)));
                masterSales.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TotalAmount)));
                masterSales.setReferenceNumber(cursor.getString(INDEX_M_SALES_ReferenceNumber));
                masterSales.setLoginID(Double.parseDouble(cursor.getString(INDEX_M_SALES_LoginID)));
                masterSales.setSalesDtTm(cursor.getString(INDEX_M_SALES_SalesDtTm));
                masterSales.setGender(cursor.getString(INDEX_M_SALES_Gender));
                masterSales.setHold(Boolean.parseBoolean(cursor.getString(INDEX_M_SALES_isHold)));
                masterSales.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_SALES_isActive)));
                masterSales.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_SALES_OutletID)));
                masterSales.setCustomerType(cursor.getString(INDEX_M_SALES_CustomerType));
                masterSales.setBalance(Double.parseDouble(cursor.getString(INDEX_M_SALES_Balance)));
                masterSales.setPaidAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_PaidAmount)));
                masterSales.setTaxOnPaidAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TaxOnPaidAmount)));
                masterSales.setCGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_CGST)));
                masterSales.setSGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_SGST)));
                masterSales.setIGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_IGST)));
                masterSales.setDiscountTypeID(Integer.parseInt(cursor.getString(INDEX_M_SALES_DiscountTypeID)));
                masterSales.setDiscount(Double.parseDouble(cursor.getString(INDEX_M_SALES_Discount)));
                masterSales.setCESS(Double.parseDouble(cursor.getString(INDEX_M_SALES_CESS)));
                masterSales.setDueDate(cursor.getString(INDEX_M_SALES_DueDate));
                masterSales.setCustomerName(cursor.getString(INDEX_M_SALES_CustomerName));
                masterSales.setTermsCondition(cursor.getString(INDEX_M_SALES_TermsCondition));
                masterSales.setCustomerGSTN(cursor.getString(INDEX_M_SALES_CustomerGSTN));
                masterSales.setCustomerEmail(cursor.getString(INDEX_M_SALES_CustomerEmail));
                masterSales.setCustomerMobile(cursor.getString(INDEX_M_SALES_CustomerMobile));
                masterSales.setCustomerAddress(cursor.getString(INDEX_M_SALES_CustomerAddress));
                masterSales.setOutletAddress(cursor.getString(INDEX_M_SALES_OutletAddress));
                masterSales.setOutletLogo(cursor.getString(INDEX_M_SALES_OutletLogo));
                masterSales.setOutletGSTN(cursor.getString(INDEX_M_SALES_OutletGSTN));
                masterSales.setShipAddress(cursor.getString(INDEX_M_SALES_ShipAddress));
                masterSales.setTransportCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_TransportCharge)));
                masterSales.setInsuranceCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_InsuranceCharge)));
                masterSales.setPackingCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_PackingCharge)));
                masterSales.setBillAddress(cursor.getString(INDEX_M_SALES_BillAddress));
                masterSales.setSalesDate(cursor.getString(INDEX_M_SALES_SalesDate));
                masterSales.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_SALES_SubTotal)));
                masterSales.seteWayBillRequired(Integer.parseInt(cursor.getString(INDEX_M_SALES_eWayBillRequired)));
                masterSales.setShippingStateID(Integer.parseInt(cursor.getString(INDEX_M_SALES_ShippingStateID)));
                masterSales.setDONumber(cursor.getString(INDEX_M_SALES_DONumber));
                masterSales.setSynced(Integer.parseInt(cursor.getString(INDEX_M_SALES_Synced)));
            }
            cursor.close();
            db.close();
            return masterSales;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_sales", "" + e);
        }
        return masterSales;
    }

    public MasterSales getMasterSalesRecordFromDO(String doNumber) {
        MasterSales masterSales = new MasterSales();
        try {
            boolean masterSalesPresent = false;
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_REGISTER + " WHERE " + M_SALES_DONumber + " = '" + doNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                masterSalesPresent = true;
                masterSales.setSalesID(Double.parseDouble(cursor.getString(INDEX_M_SALES_SalesID)));
                masterSales.setSalesReceiptNumber(cursor.getString(INDEX_M_SALES_SalesReceiptNumber));
                masterSales.setCustomerID(cursor.getString(INDEX_M_SALES_CustomerID));
                masterSales.setBaseAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_BaseAmount)));
                masterSales.setTotalDiscountAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TotalDiscountAmount)));
                masterSales.setTaxAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TaxAmount)));
                masterSales.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TotalAmount)));
                masterSales.setReferenceNumber(cursor.getString(INDEX_M_SALES_ReferenceNumber));
                masterSales.setLoginID(Double.parseDouble(cursor.getString(INDEX_M_SALES_LoginID)));
                masterSales.setSalesDtTm(cursor.getString(INDEX_M_SALES_SalesDtTm));
                masterSales.setGender(cursor.getString(INDEX_M_SALES_Gender));
                masterSales.setHold(Boolean.parseBoolean(cursor.getString(INDEX_M_SALES_isHold)));
                masterSales.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_SALES_isActive)));
                masterSales.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_SALES_OutletID)));
                masterSales.setCustomerType(cursor.getString(INDEX_M_SALES_CustomerType));
                masterSales.setBalance(Double.parseDouble(cursor.getString(INDEX_M_SALES_Balance)));
                masterSales.setPaidAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_PaidAmount)));
                masterSales.setTaxOnPaidAmount(Double.parseDouble(cursor.getString(INDEX_M_SALES_TaxOnPaidAmount)));
                masterSales.setCGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_CGST)));
                masterSales.setSGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_SGST)));
                masterSales.setIGST(Double.parseDouble(cursor.getString(INDEX_M_SALES_IGST)));
                masterSales.setDiscountTypeID(Integer.parseInt(cursor.getString(INDEX_M_SALES_DiscountTypeID)));
                masterSales.setDiscount(Double.parseDouble(cursor.getString(INDEX_M_SALES_Discount)));
                masterSales.setCESS(Double.parseDouble(cursor.getString(INDEX_M_SALES_CESS)));
                masterSales.setDueDate(cursor.getString(INDEX_M_SALES_DueDate));
                masterSales.setCustomerName(cursor.getString(INDEX_M_SALES_CustomerName));
                masterSales.setTermsCondition(cursor.getString(INDEX_M_SALES_TermsCondition));
                masterSales.setCustomerGSTN(cursor.getString(INDEX_M_SALES_CustomerGSTN));
                masterSales.setCustomerEmail(cursor.getString(INDEX_M_SALES_CustomerEmail));
                masterSales.setCustomerMobile(cursor.getString(INDEX_M_SALES_CustomerMobile));
                masterSales.setCustomerAddress(cursor.getString(INDEX_M_SALES_CustomerAddress));
                masterSales.setOutletAddress(cursor.getString(INDEX_M_SALES_OutletAddress));
                masterSales.setOutletLogo(cursor.getString(INDEX_M_SALES_OutletLogo));
                masterSales.setOutletGSTN(cursor.getString(INDEX_M_SALES_OutletGSTN));
                masterSales.setShipAddress(cursor.getString(INDEX_M_SALES_ShipAddress));
                masterSales.setTransportCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_TransportCharge)));
                masterSales.setInsuranceCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_InsuranceCharge)));
                masterSales.setPackingCharge(Double.parseDouble(cursor.getString(INDEX_M_SALES_PackingCharge)));
                masterSales.setBillAddress(cursor.getString(INDEX_M_SALES_BillAddress));
                masterSales.setSalesDate(cursor.getString(INDEX_M_SALES_SalesDate));
                masterSales.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_SALES_SubTotal)));
                masterSales.seteWayBillRequired(Integer.parseInt(cursor.getString(INDEX_M_SALES_eWayBillRequired)));
                masterSales.setShippingStateID(Integer.parseInt(cursor.getString(INDEX_M_SALES_ShippingStateID)));
                masterSales.setDONumber(cursor.getString(INDEX_M_SALES_DONumber));
                masterSales.setSynced(Integer.parseInt(cursor.getString(INDEX_M_SALES_Synced)));
            }

            cursor.close();
            db.close();
            if(masterSalesPresent) {
                return masterSales;
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("ewaybill", "" + e);
        }
        return null;
    }

    // PAYMENT
    public void addPayment(Payment payment, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            public static final String  = "PaymentID"; // 0
//            public static final String PAYMENT_InvoiceNumber = "InvoiceNumber"; // 1
            values.put(PAYMENT_InvoiceNumber, payment.getInvoiceNumber());
//            public static final String PAYMENT_ReceiptNumber = "ReceiptNumber"; // 2
            values.put(PAYMENT_ReceiptNumber, payment.getReceiptNumber());
//            public static final String PAYMENT_PaymentMode = "PaymentMode"; // 3
            values.put(PAYMENT_PaymentMode, payment.getPaymentMode());
//            public static final String PAYMENT_AmountPaid = "AmountPaid"; // 4
            values.put(PAYMENT_Amount, payment.getAmount());
//            public static final String PAYMENT_ReferenceNumber = "ReferenceNumber"; // 5
            values.put(PAYMENT_ReferenceNumber, payment.getReferenceNumber());
            values.put(PAYMENT_PaymentTypeID, payment.getPaymentTypeID());
            values.put(PAYMENT_CreditCardNumber, payment.getCreditCardNumber());
            values.put(PAYMENT_CreditCardType, payment.getCreditCardType());
            values.put(PAYMENT_Synced, payment.getSynced());
            values.put(PAYMENT_PaymentDtTm, payment.getPaymentDtTm());

            // insert into payment table
            long num = db.insert(TABLE_PAYMENTS, null, values);
            if(num != -1) {
                Log.d("database", "new payment inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(payment, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(payment, e, origin);
            Log.e("insert payment", "" + e);
        }
    }

    public String getPreviousPaymentReceiptNumber(String currentReceiptNumberInitialString) {
        try {
            String query = "SELECT " + PAYMENT_ReceiptNumber + " FROM " + TABLE_PAYMENTS
                    + " WHERE " + PAYMENT_ReceiptNumber + " LIKE '" + currentReceiptNumberInitialString + "%'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0) {
                db.close();
                return null;
            } else {
                cursor.moveToLast();
                String previousReceiptNumber = cursor.getString(0);
                cursor.close();
                db.close();
                return previousReceiptNumber;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_receipt_number", "" + e);
        }
        // it is okay to send null
        return null;
    }

    public void addNewPayment(Payment payment) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addPayment(payment, db);
            updateInvoicePaidAmount(payment, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
    }

    public void updatePaymentList(ArrayList<Payment> paymentArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_PAYMENTS, null, null);
            for(Payment payment : paymentArrayList) {
                addPayment(payment, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Payment> getPaymentListDetailsFromDB(String invoiceNumber) {
        ArrayList<Payment> payments = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PAYMENTS
                    + " WHERE " + PAYMENT_InvoiceNumber + " = '" + invoiceNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    Payment payment = new Payment();
                    payment.setPaymentID(Long.parseLong(cursor.getString(INDEX_PAYMENT_PaymentID)));
                    payment.setInvoiceNumber(cursor.getString(INDEX_PAYMENT_InvoiceNumber));
                    payment.setReceiptNumber(cursor.getString(INDEX_PAYMENT_ReceiptNumber));
                    payment.setPaymentMode(cursor.getString(INDEX_PAYMENT_PaymentMode));
                    payment.setAmount(Double.parseDouble(cursor.getString(INDEX_PAYMENT_Amount)));
                    payment.setReferenceNumber(cursor.getString(INDEX_PAYMENT_ReferenceNumber));
                    payment.setPaymentTypeID(Integer.parseInt(cursor.getString(INDEX_PAYMENT_PaymentTypeID)));
                    payment.setCreditCardNumber(cursor.getString(INDEX_PAYMENT_CreditCardNumber));
                    payment.setCreditCardType(cursor.getString(INDEX_PAYMENT_CreditCardType));
                    payment.setSynced(Integer.parseInt(cursor.getString(INDEX_PAYMENT_Synced)));
                    payment.setPaymentDtTm(cursor.getString(INDEX_PAYMENT_PaymentDtTm));

                    payments.add(payment);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return payments;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_payments", "" + e);
        }
        return payments;
    }

    public ArrayList<Payment> getUnsyncedPaymentsAgainstSyncedInvoices() {
        ArrayList<Payment> payments = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PAYMENTS
                    + " WHERE " + PAYMENT_Synced + " = " + PAYMENT_SYNCED_CODE_UnsyncedAgainstSyncedInvoices + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    Payment payment = new Payment();
                    payment.setPaymentID(Long.parseLong(cursor.getString(INDEX_PAYMENT_PaymentID)));
                    payment.setInvoiceNumber(cursor.getString(INDEX_PAYMENT_InvoiceNumber));
                    payment.setReceiptNumber(cursor.getString(INDEX_PAYMENT_ReceiptNumber));
                    payment.setPaymentMode(cursor.getString(INDEX_PAYMENT_PaymentMode));
                    payment.setAmount(Double.parseDouble(cursor.getString(INDEX_PAYMENT_Amount)));
                    payment.setReferenceNumber(cursor.getString(INDEX_PAYMENT_ReferenceNumber));
                    payment.setPaymentTypeID(Integer.parseInt(cursor.getString(INDEX_PAYMENT_PaymentTypeID)));
                    payment.setCreditCardNumber(cursor.getString(INDEX_PAYMENT_CreditCardNumber));
                    payment.setCreditCardType(cursor.getString(INDEX_PAYMENT_CreditCardType));
                    payment.setSynced(Integer.parseInt(cursor.getString(INDEX_PAYMENT_Synced)));
                    payment.setPaymentDtTm(cursor.getString(INDEX_PAYMENT_PaymentDtTm));

                    payments.add(payment);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return payments;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_UNSYNCED_payments", "" + e);
        }
        return payments;
    }

    public void updateInvoicePaidAmount(Payment payment, SQLiteDatabase db) {
        try {
            String query = "SELECT * FROM " + TABLE_M_SALES_REGISTER
                    + " WHERE " + M_SALES_SalesReceiptNumber + " = '" + payment.getInvoiceNumber() + "'";
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() > 0) {
                cursor.moveToLast();
                double paidAmount = Double.parseDouble(cursor.getString(INDEX_M_SALES_PaidAmount));
                paidAmount += payment.getAmount();
                double balanceAmount = Double.parseDouble(cursor.getString(INDEX_M_SALES_Balance));
                balanceAmount -= payment.getAmount();
                cursor.close();

                ContentValues values = new ContentValues();
                values.put(M_SALES_PaidAmount, paidAmount);
                values.put(M_SALES_Balance, balanceAmount);
                int id = db.update(TABLE_M_SALES_REGISTER, values,
                        M_SALES_SalesReceiptNumber + " = '" + payment.getInvoiceNumber() + "'",null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_invoice_number", "" + e);
        }
    }

    public void singlePaymentSyncSuccessful(long paymentID) { // only payments sent in SavePayment api
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PAYMENT_Synced, PAYMENT_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_PAYMENTS, values,
                PAYMENT_PaymentID + "=" + paymentID,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void paymentsSyncSuccessful() { // only payments sent in SavePayment api
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PAYMENT_Synced, PAYMENT_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_PAYMENTS, values,
                PAYMENT_Synced + "=" + PAYMENT_SYNCED_CODE_UnsyncedAgainstSyncedInvoices,null);
        Log.i("records changed", recordsUpdated+"");
    }

    // lastsynced
    public String insertLastSynced(int ListCode) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_LAST_SYNCED
                    + " WHERE " + LAST_SYNCED_ListCode + " = " + ListCode;
            Cursor cursor = db.rawQuery(query, null);
            String date = "";
            if(cursor.getCount() == 0) {
                date = addLastSynced(ListCode, db);
            } else {
                date = updateLastSynced(ListCode, db);
            }
//            int result = db.delete(TABLE_PRODUCTS, null, null);
            db.close();
            return date;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_last_synced", "" + e);
        }
        Log.d("check", "last synced");
        return "";
    }

    public String addLastSynced(int ListCode, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            String date = MyFunctions.getCurrentDateTime();
            values.put(LAST_SYNCED_ListCode, ListCode);
            values.put(LAST_SYNCED_DateTime, date);

            // insert into payment table
            long num = db.insert(TABLE_LAST_SYNCED, null, values);
            if(num != -1)
                Log.d("database", "new last sync inserted");
            return date;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("insert last sync", "" + e);
        }
        return "";
    }

    public String updateLastSynced(int ListCode, SQLiteDatabase db) {
        try {
            String date = MyFunctions.getCurrentDateTime();
            ContentValues values = new ContentValues();
            values.put(LAST_SYNCED_DateTime, date);
            int id = db.update(TABLE_LAST_SYNCED, values,
                    LAST_SYNCED_ListCode + " = " + ListCode,null);
            Log.d("database", "new last sync inserted");
            return date;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("insert last sync", "" + e);
        }
        return "";
    }

    public String getTableLastSynced(int ListCode) {
        try {
            String query = "SELECT " + LAST_SYNCED_DateTime + " FROM " + TABLE_LAST_SYNCED
                    + " WHERE " + LAST_SYNCED_ListCode + " = " + ListCode;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0) {
                db.close();
                return "";
            } else {
                cursor.moveToLast();
                String lastSync = cursor.getString(0);
                cursor.close();
                db.close();
                return lastSync;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_receipt_number", "" + e);
        }
        return "";
    }

    // stock ledger
    public void addStockLedgerList(ArrayList<StockLedger> stockLedgerArrayList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for(StockLedger stockLedger : stockLedgerArrayList) {
                addStockLedger(stockLedger, db);
            }
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_stock_ledger", "" + e);
        }
    }

    public void addStockLedger(StockLedger stockLedger, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(STOCKLEDGER_ProductID, stockLedger.getProductID());
            values.put(STOCKLEDGER_ReferenceNumber, stockLedger.getReferenceNumber());
            values.put(STOCKLEDGER_TransactionType, stockLedger.getTransactionType());
            values.put(STOCKLEDGER_DateOfTransaction, stockLedger.getDateOfTransaction());
            values.put(STOCKLEDGER_Quantity, stockLedger.getQuantity());
            values.put(STOCKLEDGER_DateOfCreation, stockLedger.getDateOfCreation());
            values.put(STOCKLEDGER_InOut, stockLedger.getInOut());

            // insert into payment table
            long num = db.insert(TABLE_STOCKLEDGER, null, values);
            if(num != -1) {
                Log.d("database", "new stock ledger inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(stockLedger, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(stockLedger, e, origin);
            Log.e("insert stock ledger", "" + e);
        }
    }

    // purchase
    public long addMasterPurchase(MasterPurchase masterPurchase, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            if(masterPurchase.getSynced() == 1) {
                values.put(M_PURCHASE_PurchaseID, masterPurchase.getPurchaseID());
            }
            values.put(M_PURCHASE_SupplierID, masterPurchase.getSupplierID());
            values.put(M_PURCHASE_InvoiceNumber, masterPurchase.getInvoiceNumber());
            values.put(M_PURCHASE_CreatedBy, masterPurchase.getCreatedBy());
            values.put(M_PURCHASE_CreatedDtTm, masterPurchase.getCreatedDtTm());
            values.put(M_PURCHASE_OutletID, masterPurchase.getOutletID());
            values.put(M_PURCHASE_TransportCharge, masterPurchase.getTransportCharge());
            values.put(M_PURCHASE_InsuranceCharge, masterPurchase.getInsuranceCharge());
            values.put(M_PURCHASE_PackingCharge, masterPurchase.getPackingCharge());
            values.put(M_PURCHASE_PurchaseDate, masterPurchase.getPurchaseDate());
            values.put(M_PURCHASE_CGST, masterPurchase.getCGST());
            values.put(M_PURCHASE_SGST, masterPurchase.getSGST());
            values.put(M_PURCHASE_IGST, masterPurchase.getIGST());
            values.put(M_PURCHASE_CESS, masterPurchase.getCESS());
            values.put(M_PURCHASE_SubTotal, masterPurchase.getSubTotal());
            values.put(M_PURCHASE_GrandTotal, masterPurchase.getGrandTotal());
            values.put(M_PURCHASE_SupplierName, masterPurchase.getSupplierName());
            values.put(M_PURCHASE_SupplierEmail, masterPurchase.getSupplierEmail());
            values.put(M_PURCHASE_SupplierMobile, masterPurchase.getSupplierMobile());
            values.put(M_PURCHASE_Synced, masterPurchase.getSynced());

            // insert into master sales table
            long num = db.insert(TABLE_M_PURCHASE_REGISTER, null, values);
            if(num != -1) {
                Log.d("database", "new master purchase record inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(masterPurchase, origin);
            }
            return num;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(masterPurchase, e, origin);
            Log.e("insert master purchase", "" + e);
        }
        return -1;
    }

    public void addTransactionPurchase(TransactionPurchase transactionPurchase, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            values.put(T_PURCHASE_PurchaseDetailID, transactionPurchase.getPurchaseDetailID());
            values.put(T_PURCHASE_PurchaseID, transactionPurchase.getPurchaseID());
            values.put(T_PURCHASE_ProductID, transactionPurchase.getProductID());
            values.put(T_PURCHASE_UnitID, transactionPurchase.getUnitID());
            values.put(T_PURCHASE_Quantity, transactionPurchase.getQuantity());
            values.put(T_PURCHASE_UnitPrice, transactionPurchase.getUnitPrice());
            values.put(T_PURCHASE_Amount, transactionPurchase.getAmount());
            values.put(T_PURCHASE_Status, transactionPurchase.getStatus());
            values.put(T_PURCHASE_ProductName, transactionPurchase.getProductName());
            values.put(T_PURCHASE_Unit, transactionPurchase.getUnit());
            values.put(T_PURCHASE_iCGST, transactionPurchase.getiCGST());
            values.put(T_PURCHASE_iSGST, transactionPurchase.getiSGST());
            values.put(T_PURCHASE_iIGST, transactionPurchase.getiIGST());
            values.put(T_PURCHASE_iCESS, transactionPurchase.getiCESS());
            values.put(T_PURCHASE_ProductCode, transactionPurchase.getProductCode());
            values.put(T_PURCHASE_Synced, transactionPurchase.getSynced());

            // insert into t sales table
            long num = db.insert(TABLE_T_PURCHASE_REGISTER, null, values);
            if(num != -1) {
                Log.d("database", "new transaction purchase record inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(transactionPurchase, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(transactionPurchase, e, origin);
            Log.e("insert t purchase", "" + e);
        }
    }

    public MasterPurchase getMasterPurchaseRecord(String PurchaseID) {
        MasterPurchase masterPurchase = new MasterPurchase();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_REGISTER + " where "+M_PURCHASE_PurchaseID+"='"+PurchaseID+ "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    masterPurchase.setPurchaseID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_PurchaseID)));
                    masterPurchase.setSupplierID(cursor.getString(INDEX_M_PURCHASE_SupplierID));
                    masterPurchase.setInvoiceNumber(cursor.getString(INDEX_M_PURCHASE_InvoiceNumber));
                    masterPurchase.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CreatedBy)));
                    masterPurchase.setCreatedDtTm(cursor.getString(INDEX_M_PURCHASE_CreatedDtTm));
                    masterPurchase.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_OutletID)));
                    masterPurchase.setTransportCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_TransportCharge)));
                    masterPurchase.setInsuranceCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_InsuranceCharge)));
                    masterPurchase.setPackingCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_PackingCharge)));
                    masterPurchase.setPurchaseDate(cursor.getString(INDEX_M_PURCHASE_PurchaseDate));
                    masterPurchase.setCGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CGST)));
                    masterPurchase.setSGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_SGST)));
                    masterPurchase.setIGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_IGST)));
                    masterPurchase.setCESS(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CESS)));
                    masterPurchase.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_SubTotal)));
                    masterPurchase.setGrandTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_GrandTotal)));
                    masterPurchase.setSupplierName(cursor.getString(INDEX_M_PURCHASE_SupplierName));
                    masterPurchase.setSupplierEmail(cursor.getString(INDEX_M_PURCHASE_SupplierEmail));
                    masterPurchase.setSupplierMobile(cursor.getString(INDEX_M_PURCHASE_SupplierMobile));
                    masterPurchase.setSynced(Integer.parseInt(cursor.getString(INDEX_M_PURCHASE_Synced)));


                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return masterPurchase;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_sales", "" + e);
        }
        return masterPurchase;
    }

    public ArrayList<MasterPurchase> getPurchaseListFromDB() {
        ArrayList<MasterPurchase> masterPurchaseArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_REGISTER + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToLast()) {
                do {
                    MasterPurchase masterPurchase = new MasterPurchase();
                    masterPurchase.setPurchaseID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_PurchaseID)));
                    masterPurchase.setSupplierID(cursor.getString(INDEX_M_PURCHASE_SupplierID));
                    masterPurchase.setInvoiceNumber(cursor.getString(INDEX_M_PURCHASE_InvoiceNumber));
                    masterPurchase.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CreatedBy)));
                    masterPurchase.setCreatedDtTm(cursor.getString(INDEX_M_PURCHASE_CreatedDtTm));
                    masterPurchase.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_OutletID)));
                    masterPurchase.setTransportCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_TransportCharge)));
                    masterPurchase.setInsuranceCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_InsuranceCharge)));
                    masterPurchase.setPackingCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_PackingCharge)));
                    masterPurchase.setPurchaseDate(cursor.getString(INDEX_M_PURCHASE_PurchaseDate));
                    masterPurchase.setCGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CGST)));
                    masterPurchase.setSGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_SGST)));
                    masterPurchase.setIGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_IGST)));
                    masterPurchase.setCESS(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CESS)));
                    masterPurchase.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_SubTotal)));
                    masterPurchase.setGrandTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_GrandTotal)));
                    masterPurchase.setSupplierName(cursor.getString(INDEX_M_PURCHASE_SupplierName));
                    masterPurchase.setSupplierEmail(cursor.getString(INDEX_M_PURCHASE_SupplierEmail));
                    masterPurchase.setSupplierMobile(cursor.getString(INDEX_M_PURCHASE_SupplierMobile));
                    masterPurchase.setSynced(Integer.parseInt(cursor.getString(INDEX_M_PURCHASE_Synced)));

                    masterPurchaseArrayList.add(masterPurchase);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return masterPurchaseArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_sales", "" + e);
        }
        return masterPurchaseArrayList;
    }

    public void purchaseSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_PURCHASE_Synced, M_PURCHASE_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_PURCHASE_REGISTER, values,
                M_PURCHASE_Synced + "=" + M_PURCHASE_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void singlePurchaseSyncSuccessful(String purchaseID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_PURCHASE_Synced, M_PURCHASE_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_PURCHASE_REGISTER, values,
                T_PURCHASE_PurchaseID + "=" + purchaseID,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public ArrayList<MasterPurchase> getUnsyncedPurchaseListFromDB() {
        ArrayList<MasterPurchase> masterPurchaseArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_REGISTER
            + " WHERE " + M_PURCHASE_Synced + " = " + DatabaseHandler.M_PURCHASE_SYNCED_CODE_Unsynced + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    MasterPurchase masterPurchase = new MasterPurchase();
                    masterPurchase.setPurchaseID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_PurchaseID)));
                    masterPurchase.setSupplierID(cursor.getString(INDEX_M_PURCHASE_SupplierID));
                    masterPurchase.setInvoiceNumber(cursor.getString(INDEX_M_PURCHASE_InvoiceNumber));
                    masterPurchase.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CreatedBy)));
                    masterPurchase.setCreatedDtTm(cursor.getString(INDEX_M_PURCHASE_CreatedDtTm));
                    masterPurchase.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_OutletID)));
                    masterPurchase.setTransportCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_TransportCharge)));
                    masterPurchase.setInsuranceCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_InsuranceCharge)));
                    masterPurchase.setPackingCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_PackingCharge)));
                    masterPurchase.setPurchaseDate(cursor.getString(INDEX_M_PURCHASE_PurchaseDate));
                    masterPurchase.setCGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CGST)));
                    masterPurchase.setSGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_SGST)));
                    masterPurchase.setIGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_IGST)));
                    masterPurchase.setCESS(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CESS)));
                    masterPurchase.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_SubTotal)));
                    masterPurchase.setGrandTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_GrandTotal)));
                    masterPurchase.setSupplierName(cursor.getString(INDEX_M_PURCHASE_SupplierName));
                    masterPurchase.setSupplierEmail(cursor.getString(INDEX_M_PURCHASE_SupplierEmail));
                    masterPurchase.setSupplierMobile(cursor.getString(INDEX_M_PURCHASE_SupplierMobile));
                    masterPurchase.setSynced(Integer.parseInt(cursor.getString(INDEX_M_PURCHASE_Synced)));

                    masterPurchaseArrayList.add(masterPurchase);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return masterPurchaseArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_sales", "" + e);
        }
        return masterPurchaseArrayList;
    }

    public ArrayList<TransactionPurchase> getPurchaseListDetailsFromDB(String PurchaseID) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_T_PURCHASE_REGISTER
                    + " WHERE " + T_PURCHASE_PurchaseID + " = " + PurchaseID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<TransactionPurchase> transactionPurchaseArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    TransactionPurchase transactionPurchase = new TransactionPurchase();
                    transactionPurchase.setPurchaseDetailID(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_PurchaseDetailID)));
                    transactionPurchase.setPurchaseID(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_PurchaseID)));
                    transactionPurchase.setProductID(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_ProductID)));
                    transactionPurchase.setUnitID(Long.parseLong(cursor.getString(INDEX_T_PURCHASE_UnitID)));
                    transactionPurchase.setQuantity(Integer.parseInt(cursor.getString(INDEX_T_PURCHASE_Quantity)));
                    transactionPurchase.setUnitPrice(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_UnitPrice)));
                    transactionPurchase.setAmount(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_Amount)));
                    transactionPurchase.setStatus(Integer.parseInt(cursor.getString(INDEX_T_PURCHASE_Status)));
                    transactionPurchase.setProductName(cursor.getString(INDEX_T_PURCHASE_ProductName));
                    transactionPurchase.setUnit(cursor.getString(INDEX_T_PURCHASE_Unit));
                    transactionPurchase.setiCGST(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_iCGST)));
                    transactionPurchase.setiSGST(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_iSGST)));
                    transactionPurchase.setiIGST(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_iIGST)));
                    transactionPurchase.setiCESS(Double.parseDouble(cursor.getString(INDEX_T_PURCHASE_iCESS)));
                    transactionPurchase.setProductCode(cursor.getString(INDEX_T_PURCHASE_ProductCode));
                    transactionPurchase.setSynced(Integer.parseInt(cursor.getString(INDEX_T_PURCHASE_Synced)));

                    transactionPurchaseArrayList.add(transactionPurchase);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return transactionPurchaseArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_transaction_sales", "" + e);
        }
        return new ArrayList<>();
    }

    public double getRecentMasterPurchaseID(String invoiceNumber, SQLiteDatabase db) {
        try {
            String query = "SELECT " + M_PURCHASE_PurchaseID + " FROM " + TABLE_M_PURCHASE_REGISTER
                    + " WHERE " + M_PURCHASE_InvoiceNumber + " = '" + invoiceNumber + "'";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToLast();
            String PurchaseID = cursor.getString(0);
            cursor.close();
            return Double.parseDouble(PurchaseID);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_invoice_number", "" + e);
        }
        return 0;
    }

    public void updateTransactionPurchaseList(ArrayList<TransactionPurchase> transactionPurchaseArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_T_PURCHASE_REGISTER, null, null);
            for (TransactionPurchase tPurchase : transactionPurchaseArrayList) {
                addTransactionPurchase(tPurchase, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void addTransactionPurchaseList(ArrayList<TransactionPurchase> transactionPurchaseArrayList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (TransactionPurchase tPurchase : transactionPurchaseArrayList) {
                addTransactionPurchase(tPurchase, db);
            }
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
        Log.d("check", transactionPurchaseArrayList+"");
    }

    public double insertMasterPurchase(MasterPurchase masterPurchase) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
//            int result = db.delete(TABLE_PRODUCTS, null, null);
            addMasterPurchase(masterPurchase, db);
            double masterPurchaseID = getRecentMasterPurchaseID(masterPurchase.getInvoiceNumber(), db);
            db.close();
            return masterPurchaseID;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
        Log.d("check", masterPurchase+"");
        return 0;
    }

    public void updateMasterPurchaseList(ArrayList<MasterPurchase> masterPurchaseArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_M_PURCHASE_REGISTER, M_PURCHASE_Synced + " = " + M_PURCHASE_SYNCED_CODE_Synced, null);
            for (MasterPurchase masterPurchase : masterPurchaseArrayList) {
                addMasterPurchase(masterPurchase, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_masterpurchase", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public MasterPurchase getMasterPurchaseRecord(int masterPurchaseID) {
        MasterPurchase masterPurchase = new MasterPurchase();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_REGISTER + " WHERE " + M_PURCHASE_PurchaseID + " = " + masterPurchaseID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                masterPurchase.setPurchaseID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_PurchaseID)));
                masterPurchase.setSupplierID(cursor.getString(INDEX_M_PURCHASE_SupplierID));
                masterPurchase.setInvoiceNumber(cursor.getString(INDEX_M_PURCHASE_InvoiceNumber));
                masterPurchase.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CreatedBy)));
                masterPurchase.setCreatedDtTm(cursor.getString(INDEX_M_PURCHASE_CreatedDtTm));
                masterPurchase.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_OutletID)));
                masterPurchase.setTransportCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_TransportCharge)));
                masterPurchase.setInsuranceCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_InsuranceCharge)));
                masterPurchase.setPackingCharge(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_PackingCharge)));
                masterPurchase.setPurchaseDate(cursor.getString(INDEX_M_PURCHASE_PurchaseDate));
                masterPurchase.setCGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CGST)));
                masterPurchase.setSGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_SGST)));
                masterPurchase.setIGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_IGST)));
                masterPurchase.setCESS(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_CESS)));
                masterPurchase.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_SubTotal)));
                masterPurchase.setGrandTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASE_GrandTotal)));
                masterPurchase.setSupplierName(cursor.getString(INDEX_M_PURCHASE_SupplierName));
                masterPurchase.setSupplierEmail(cursor.getString(INDEX_M_PURCHASE_SupplierEmail));
                masterPurchase.setSupplierMobile(cursor.getString(INDEX_M_PURCHASE_SupplierMobile));
                masterPurchase.setSynced(Integer.parseInt(cursor.getString(INDEX_M_PURCHASE_Synced)));
            }
            cursor.close();
            db.close();
            return masterPurchase;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_sales", "" + e);
        }
        return masterPurchase;
    }

    // dashboard
    public ArrayList<Double> getDashboardDetails() {
        ArrayList<Double> dataArray = new ArrayList<>();
        String currentDateMonth = MyFunctions.getCurrentDateAndMonth();
        String currentDateMonthForPayment = MyFunctions.getCurrentDateAndMonthForPayment();
        String currentMonth = MyFunctions.getCurrentMonth();
        for (int i = 0; i < 6; i++) {
            dataArray.add(0.0);
        }
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            double salesMTD = 0.0;
            double salesToday = 0.0;
            double collectionMTD = 0.0;
            double collectionToday = 0.0;

            // Sales MTD & Invoice MTD
            String query = "SELECT " + M_SALES_TotalAmount + " FROM " + TABLE_M_SALES_REGISTER
                    + " WHERE " + M_SALES_SalesDate + " LIKE '%" + currentDateMonth + "%'";
            Cursor cursor = db.rawQuery(query, null);
            double invoiceToday = cursor.getCount();
            if(cursor.moveToFirst()) {
                do {
                    salesToday += Double.parseDouble(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            dataArray.set(DASHBOARD_DATA_SalesToday, salesToday);
            dataArray.set(DASHBOARD_DATA_InvoicesToday, invoiceToday);

            // Sales MTD & Invoice MTD
            query = "SELECT " + M_SALES_TotalAmount + " FROM " + TABLE_M_SALES_REGISTER
                    + " WHERE " + M_SALES_SalesDate + " LIKE '%" + currentMonth + "%'";
            cursor = db.rawQuery(query, null);
            double invoiceMTD = cursor.getCount();
            if(cursor.moveToFirst()) {
                do {
                    salesMTD += Double.parseDouble(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            dataArray.set(DASHBOARD_DATA_SalesMTD, salesMTD);
            dataArray.set(DASHBOARD_DATA_InvoicesMTD, invoiceMTD);

//            // Collection MTD
            query = "SELECT " + PAYMENT_Amount + " FROM " + TABLE_PAYMENTS
                    + " WHERE " + PAYMENT_PaymentDtTm + " LIKE '%" + currentDateMonthForPayment + "%'";
            cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                do {
                    collectionToday += Double.parseDouble(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            // Collection Today
            query = "SELECT " + PAYMENT_Amount + " FROM " + TABLE_PAYMENTS
                    + " WHERE " + PAYMENT_PaymentDtTm + " LIKE '%" + currentMonth + "%'";
            cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()) {
                do {
                    collectionMTD += Double.parseDouble(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            dataArray.set(DASHBOARD_DATA_CollectionMTD, collectionMTD);
            dataArray.set(DASHBOARD_DATA_CollectionToday, collectionToday);
            cursor.close();

            db.close();
            return dataArray;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
        Log.d("check", dataArray+"");
        return dataArray;
    }

    // STATE
    public void addState(State state, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(STATE_StateID, state.getStateID());
            values.put(STATE_StateName, state.getStateName());

            // insert into states table
            long num = db.insert(TABLE_STATES, null, values);
            if(num != -1) {
                Log.d("database", "new STATES inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(state, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(state, e, origin);
            Log.e("insert states", "" + e);
        }
    }

    public ArrayList<State> getStateListFromDB() {
        ArrayList<State> states = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_STATES + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    State state = new State();
                    state.setStateID(Integer.parseInt(cursor.getString(INDEX_STATE_StateID)));
                    state.setStateName(cursor.getString(INDEX_STATE_StateName));

                    states.add(state);
                } while (cursor.moveToNext());
                return states;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_states", "" + e);
        }
        return states;
    }

    public void updateStateList(ArrayList<State> states) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_STATES, null, null);
            for (State state : states) {
                addState(state, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_states", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public State getStateFromId(int stateId) {
        State state = new State();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_STATES
                    + " WHERE " + STATE_StateID + "=" + stateId + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    state.setStateID(Integer.parseInt(cursor.getString(INDEX_STATE_StateID)));
                    state.setStateName(cursor.getString(INDEX_STATE_StateName));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_states", "" + e);
        }
        return state;
    }

    // GST Category
    public void addGSTCategory(GSTCategory gstCategory, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(GSTCATEGORY_ID, gstCategory.getID());
            values.put(GSTCATEGORY_CategoryName, gstCategory.getCategoryName());

            // insert into gst category table
            long num = db.insert(TABLE_GST_CATEGORY, null, values);
            if(num != -1) {
                Log.d("database", "new gst category inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(gstCategory, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(gstCategory, e, origin);
            Log.e("insert gst category", "" + e);
        }
    }

    public ArrayList<GSTCategory> getGSTCategoryListFromDB() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_GST_CATEGORY + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<GSTCategory> gstCategoryArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    GSTCategory gstCategory = new GSTCategory();
                    gstCategory.setID(Integer.parseInt(cursor.getString(INDEX_GSTCATEGORY_ID)));
                    gstCategory.setCategoryName(cursor.getString(INDEX_GSTCATEGORY_CategoryName));

                    gstCategoryArrayList.add(gstCategory);
                } while (cursor.moveToNext());
                return gstCategoryArrayList;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_gstcategory", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<GSTCategory> getGSTCategoryListForCustomerFromDB() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_GST_CATEGORY
                    + " WHERE " + GSTCATEGORY_ID + " <> " + GSTCategory.EXPORTER + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<GSTCategory> gstCategoryArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    GSTCategory gstCategory = new GSTCategory();
                    gstCategory.setID(Integer.parseInt(cursor.getString(INDEX_GSTCATEGORY_ID)));
                    gstCategory.setCategoryName(cursor.getString(INDEX_GSTCATEGORY_CategoryName));

                    gstCategoryArrayList.add(gstCategory);
                } while (cursor.moveToNext());
                return gstCategoryArrayList;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_gstcategory", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<GSTCategory> getGSTCategoryListForSupplierFromDB() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_GST_CATEGORY
                    + " WHERE " + GSTCATEGORY_ID + " <> " + GSTCategory.IMPORTER + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<GSTCategory> gstCategoryArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    GSTCategory gstCategory = new GSTCategory();
                    gstCategory.setID(Integer.parseInt(cursor.getString(INDEX_GSTCATEGORY_ID)));
                    gstCategory.setCategoryName(cursor.getString(INDEX_GSTCATEGORY_CategoryName));

                    gstCategoryArrayList.add(gstCategory);
                } while (cursor.moveToNext());
                return gstCategoryArrayList;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_gstcategory", "" + e);
        }
        return new ArrayList<>();
    }

    public void updateGSTCategoryList(ArrayList<GSTCategory> gstCategories) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_GST_CATEGORY, null, null);
            for (GSTCategory gstCategory : gstCategories) {
                addGSTCategory(gstCategory, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_gstcategories", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public String getGSTCategoryName(int gstCategoryID) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT " + GSTCATEGORY_CategoryName + " FROM " + TABLE_GST_CATEGORY
                    + " WHERE " + GSTCATEGORY_ID + "=" + gstCategoryID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                String name = cursor.getString(0);
                cursor.close();
                db.close();
                return name;
            } else {
                cursor.close();
                db.close();
                return "";
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("get_gstcategory", "" + e);
        }
        return "";
    }

    // Add new payment mode
    public void addPaymentMode(PaymentMode paymentMode, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(PAYMENTMODE_PaymentTypeID, paymentMode.getPaymentTypeID());
            values.put(PAYMENTMODE_PaymentTypeName, paymentMode.getPaymentTypeName());

            // insert into gst category table
            long num = db.insert(TABLE_PAYMENT_MODES, null, values);
            if(num != -1) {
                Log.d("database", "new payment mode inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(paymentMode, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(paymentMode, e, origin);
            Log.e("insert payment mode", "" + e);
        }
    }

    public ArrayList<PaymentMode> getPaymentModeListFromDB() {
        ArrayList<PaymentMode> paymentModes = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PAYMENT_MODES + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    PaymentMode paymentMode = new PaymentMode();
                    paymentMode.setPaymentTypeID(Integer.parseInt(cursor.getString(INDEX_PAYMENTMODE_PaymentTypeID)));
                    paymentMode.setPaymentTypeName(cursor.getString(INDEX_PAYMENTMODE_PaymentTypeName));

                    paymentModes.add(paymentMode);
                } while (cursor.moveToNext());
                return paymentModes;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_payment modes", "" + e);
        }
        return paymentModes;
    }

    public void updatePaymentModeList(ArrayList<PaymentMode> paymentModes) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_PAYMENT_MODES, null, null);
            for (PaymentMode paymentMode : paymentModes) {
                addPaymentMode(paymentMode, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_payment modes", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public boolean isInvoiceSynced(String invoiceNumber) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT " + M_SALES_Synced + " FROM " + TABLE_M_SALES_REGISTER
                    + " WHERE " + M_SALES_ReferenceNumber + " = '" + invoiceNumber + "'";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToLast();
            int synced = Integer.parseInt(cursor.getString(0));
            cursor.close();
            if(synced == 1) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_invoice_number", "" + e);
        }
        return false;
    }

    // product category
    public void addNewProductCategory(ProductCategory productCategory) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addProductCategory(productCategory, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_product category", "" + e);
        }
        Log.d("check", productCategory+"");
    }

    public void addProductCategory(ProductCategory productCategory, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(PRODUCT_CATEGORY_ProductCategoryId, productCategory.getProductCategoryId());
            values.put(PRODUCT_CATEGORY_ParentCategoryID, productCategory.getParentCategoryID());
            values.put(PRODUCT_CATEGORY_ProductCategoryName, productCategory.getProductCategoryName());
            values.put(PRODUCT_CATEGORY_IsProductCategoryActive, productCategory.isProductCategoryActive());
            values.put(PRODUCT_CATEGORY_CreatedBy, productCategory.getCreatedBy());
            values.put(PRODUCT_CATEGORY_CreatedDtTm, productCategory.getCreatedDtTm());
            values.put(PRODUCT_CATEGORY_OutletID, productCategory.getOutletID());
            values.put(PRODUCT_CATEGORY_Synced, productCategory.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_PRODUCT_CATEGORY, null, values);
            if(num != -1) {
                Log.d("database", "new product category inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(productCategory, origin);
            }
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(productCategory, e, origin);
            Log.e("insert product category", "" + e);
        }
    }

    public void updateProductCategoryList(ArrayList<ProductCategory> productCategoryArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_PRODUCT_CATEGORY, null, null);
            for (ProductCategory productCategory : productCategoryArrayList) {
                addProductCategory(productCategory, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_product category", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ProductCategory> getUnsyncedProductCategories() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_CATEGORY
                    + " WHERE " + PRODUCT_CATEGORY_Synced + " = " + PRODUCTCATEGORY_SYNCED_CODE_Unsynced + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<ProductCategory> productCategoryArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setProductCategoryId(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_ProductCategoryId)));
                    productCategory.setParentCategoryID(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_ParentCategoryID)));
                    productCategory.setProductCategoryName(cursor.getString(INDEX_PRODUCTCATEGORY_ProductCategoryName));
                    productCategory.setProductCategoryActive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCTCATEGORY_IsProductCategoryActive)));
                    productCategory.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_CreatedBy)));
                    productCategory.setCreatedDtTm(cursor.getString(INDEX_PRODUCTCATEGORY_CreatedDtTm));
                    productCategory.setOutletID(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_OutletID)));
                    productCategory.setSynced(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_Synced)));

                    productCategoryArrayList.add(productCategory);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return productCategoryArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_UNSYNCED_payments", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<ProductCategory> getProductCategories(int ParentCategoryID) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_CATEGORY
                    + " WHERE " + PRODUCT_CATEGORY_ParentCategoryID + " = " + ParentCategoryID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<ProductCategory> productCategoryArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setProductCategoryId(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_ProductCategoryId)));
                    productCategory.setParentCategoryID(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_ParentCategoryID)));
                    productCategory.setProductCategoryName(cursor.getString(INDEX_PRODUCTCATEGORY_ProductCategoryName));
                    productCategory.setProductCategoryActive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCTCATEGORY_IsProductCategoryActive)));
                    productCategory.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_CreatedBy)));
                    productCategory.setCreatedDtTm(cursor.getString(INDEX_PRODUCTCATEGORY_CreatedDtTm));
                    productCategory.setOutletID(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_OutletID)));
                    productCategory.setSynced(Integer.parseInt(cursor.getString(INDEX_PRODUCTCATEGORY_Synced)));

                    productCategoryArrayList.add(productCategory);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return productCategoryArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_UNSYNCED_payments", "" + e);
        }
        return new ArrayList<>();
    }

    public String getProductCategoryName(long CategoryId) {
        String name = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT " + PRODUCT_CATEGORY_ProductCategoryName + " FROM " + TABLE_PRODUCT_CATEGORY
                    + " WHERE " + PRODUCT_CATEGORY_ProductCategoryId + " = " + CategoryId + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                name = cursor.getString(0);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_UNSYNCED_payments", "" + e);
        }
        return name;
    }

    public void productCategoriesSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CATEGORY_Synced, PRODUCTCATEGORY_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_PRODUCT_CATEGORY, values,
                PRODUCT_CATEGORY_Synced + "=" + PRODUCTCATEGORY_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    // brand
    public void addNewBrand(Brand brand) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addBrand(brand, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_brand", "" + e);
        }
        Log.d("check", brand+"");
    }

    public void addBrand(Brand brand, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(BRAND_BrandID, brand.getBrandID());
            values.put(BRAND_Brand, brand.getBrand());
            values.put(BRAND_isBrandActive, brand.isBrandActive());
            values.put(BRAND_CreatedBy, brand.getCreatedBy());
            values.put(BRAND_CreatedDtTm, brand.getCreatedDtTm());
            values.put(BRAND_Synced, brand.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_BRANDS, null, values);
            if(num != -1) {
                Log.d("database", "new BRANDS inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(brand, origin);
            }
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(brand, e, origin);
            Log.e("insert brand", "" + e);
        }
    }

    public void updateBrandList(ArrayList<Brand> brandArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_BRANDS, null, null);
            for (Brand brand : brandArrayList) {
                addBrand(brand, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_brand", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Brand> getUnsyncedBrands() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_BRANDS +
                    " WHERE " + BRAND_Synced + " = " + BRAND_SYNCED_CODE_Unsynced;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<Brand> brandArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    Brand brand = new Brand();
                    brand.setBrandID(Integer.parseInt(cursor.getString(INDEX_BRAND_BrandID)));
                    brand.setBrand(cursor.getString(INDEX_BRAND_Brand));
                    brand.setBrandActive(Boolean.parseBoolean(cursor.getString(INDEX_BRAND_isBrandActive)));
                    brand.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_BRAND_CreatedBy)));
                    brand.setCreatedDtTm(cursor.getString(INDEX_BRAND_CreatedDtTm));
                    brand.setSynced(Integer.parseInt(cursor.getString(INDEX_BRAND_Synced)));

                    brandArrayList.add(brand);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return brandArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_brand", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<Brand> getBrands() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_BRANDS ;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<Brand> brandArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    Brand brand = new Brand();
                    brand.setBrandID(Integer.parseInt(cursor.getString(INDEX_BRAND_BrandID)));
                    brand.setBrand(cursor.getString(INDEX_BRAND_Brand));
                    brand.setBrandActive(Boolean.parseBoolean(cursor.getString(INDEX_BRAND_isBrandActive)));
                    brand.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_BRAND_CreatedBy)));
                    brand.setCreatedDtTm(cursor.getString(INDEX_BRAND_CreatedDtTm));
                    brand.setSynced(Integer.parseInt(cursor.getString(INDEX_BRAND_Synced)));

                    brandArrayList.add(brand);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return brandArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_brand", "" + e);
        }
        return new ArrayList<>();
    }

    public void brandsSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BRAND_Synced, BRAND_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_BRANDS, values,
                BRAND_Synced + "=" + BRAND_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    // product unit
    public void addNewProductUnit(Unit unit) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addProductUnit(unit, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_unit", "" + e);
        }
    }

    public void addProductUnit(Unit unit, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(PRODUCT_UNIT_ID, unit.getID());
            values.put(PRODUCT_UNIT_isActive, unit.isActive());
            values.put(PRODUCT_UNIT_Unit, unit.getUnit());
            values.put(PRODUCT_UNIT_Synced, unit.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_UNITS, null, values);
            if(num != -1) {
                Log.d("database", "new units inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(unit, origin);
            }
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(unit, e, origin);
            Log.e("insert unit", "" + e);
        }
    }

    public void updateProductUnitList(ArrayList<Unit> unitArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_UNITS, null, null);
            for (Unit unit : unitArrayList) {
                addProductUnit(unit, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_unit", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Unit> getProductUnits() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_UNITS ;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<Unit> unitArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    Unit unit = new Unit();
                    unit.setID(Integer.parseInt(cursor.getString(INDEX_PRODUCT_UNIT_Id)));
                    unit.setActive(Boolean.parseBoolean(cursor.getString(INDEX_PRODUCT_UNIT_isActive)));
                    unit.setUnit(cursor.getString(INDEX_PRODUCT_UNIT_Unit));
                    unit.setSynced(Integer.parseInt(cursor.getString(INDEX_PRODUCT_UNIT_Synced)));

                    unitArrayList.add(unit);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return unitArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_brand", "" + e);
        }
        return new ArrayList<>();
    }

    // hsc COde
    public void addNewHSCCode(HSCCode hscCode) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addHSCCode(hscCode, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_hsc", "" + e);
        }
    }

    public void addHSCCode(HSCCode hscCode, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(HSC_ID, hscCode.getID());
            values.put(HSC_HSCCode, hscCode.getHSCCode());
            values.put(HSC_Description, hscCode.getDescription());
            values.put(HSC_CGST, hscCode.getCGST());
            values.put(HSC_SGST, hscCode.getSGST());
            values.put(HSC_IGST, hscCode.getIGST());
            values.put(HSC_CESS, hscCode.getCESS());
            values.put(HSC_IsActive, hscCode.isActive());
            values.put(HSC_CreatedBy, hscCode.getCreatedBy());
            values.put(HSC_Condition, hscCode.getCondition());
            values.put(HSC_Remarks, hscCode.getRemarks());
            values.put(HSC_RedioHSN, hscCode.getRedioHSN());
            values.put(HSC_Synced, hscCode.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_HSNCODE, null, values);
            if(num != -1) {
                Log.d("database", "new hsc inserted " + num);
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(hscCode, origin);
            }
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(hscCode, e, origin);
            Log.e("insert hsc", "" + e);
        }
    }

    public void addHSCCodeFromJson(JSONObject jsonObject, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(HSC_ID, jsonObject.getString(HSC_ID));
            values.put(HSC_HSCCode, jsonObject.getString(HSC_HSCCode));
            values.put(HSC_Description, jsonObject.getString(HSC_Description));
            values.put(HSC_CGST, jsonObject.getString(HSC_CGST));
            values.put(HSC_SGST, jsonObject.getString(HSC_SGST));
            values.put(HSC_IGST, jsonObject.getString(HSC_IGST));
            values.put(HSC_CESS, jsonObject.getString(HSC_CESS));
            values.put(HSC_IsActive, jsonObject.getString(HSC_IsActive));
            values.put(HSC_CreatedBy, jsonObject.getString(HSC_CreatedBy));
            values.put(HSC_Condition, jsonObject.getString(HSC_Condition));
            values.put(HSC_Remarks, jsonObject.getString(HSC_Remarks));
            values.put(HSC_RedioHSN, jsonObject.getString(HSC_RedioHSN));
            values.put(HSC_Synced, 1);

            // insert into customer table
            long num = db.insert(TABLE_HSNCODE, null, values);
            if(num != -1) {
                Log.d("database", "new hsc inserted " + num);
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
//                MyFunctions.errorOccuredRecordNotInsertedInDatabase(hscCode, origin);
            }
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
//            MyFunctions.errorOccuredInsertCatchInDatabase(hscCode, e, origin);
            Log.e("insert hsc", "" + e);
        }
    }

    public void updateHSCCodeList(ArrayList<HSCCode> hscCodeArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_HSNCODE, null, null);
            for (HSCCode hscCode : hscCodeArrayList) {
                addHSCCode(hscCode, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_hsc", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void updateHSCCodeListFromJsonList(JSONArray jsonArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_HSNCODE, null, null);

//            HSCCode hscCode;
//            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                addHSCCodeFromJson(jsonObject, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_hsc", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public HSCCode getHSNCode(String hsnCode) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_HSNCODE
                    + " WHERE " + HSC_HSCCode + "='" + hsnCode + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            HSCCode hscCode = new HSCCode();
            if(cursor.moveToFirst()) {
                    hscCode.setID(Integer.parseInt(cursor.getString(INDEX_HSC_ID)));
                    hscCode.setHSCCode(cursor.getString(INDEX_HSC_HSCCode));
                    hscCode.setDescription(cursor.getString(INDEX_HSC_Description));
                    hscCode.setCGST(Double.parseDouble(cursor.getString(INDEX_HSC_CGST)));
                    hscCode.setSGST(Double.parseDouble(cursor.getString(INDEX_HSC_SGST)));
                    hscCode.setIGST(Double.parseDouble(cursor.getString(INDEX_HSC_IGST)));
                    hscCode.setCESS(Double.parseDouble(cursor.getString(INDEX_HSC_CESS)));
                    hscCode.setActive(Boolean.parseBoolean(cursor.getString(INDEX_HSC_IsActive)));
                    hscCode.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_HSC_CreatedBy)));
                    hscCode.setCondition(cursor.getString(INDEX_HSC_Condition));
                    hscCode.setRemarks(cursor.getString(INDEX_HSC_Remarks));
                    hscCode.setRedioHSN(cursor.getString(INDEX_HSC_RedioHSN));
                    hscCode.setSynced(Integer.parseInt(cursor.getString(INDEX_HSC_Synced)));
            } else {
                return null;
            }
            cursor.close();
            db.close();
            return hscCode;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_hsc", "" + e);
        }
        return null;
    }

    public ArrayList<HSCCode> getHSCCodeList() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_HSNCODE ;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<HSCCode> hscCodeArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    HSCCode hscCode = new HSCCode();
                    hscCode.setID(Integer.parseInt(cursor.getString(INDEX_HSC_ID)));
                    hscCode.setHSCCode(cursor.getString(INDEX_HSC_HSCCode));
                    hscCode.setDescription(cursor.getString(INDEX_HSC_Description));
                    hscCode.setCGST(Double.parseDouble(cursor.getString(INDEX_HSC_CGST)));
                    hscCode.setSGST(Double.parseDouble(cursor.getString(INDEX_HSC_SGST)));
                    hscCode.setIGST(Double.parseDouble(cursor.getString(INDEX_HSC_IGST)));
                    hscCode.setCESS(Double.parseDouble(cursor.getString(INDEX_HSC_CESS)));
                    hscCode.setActive(Boolean.parseBoolean(cursor.getString(INDEX_HSC_IsActive)));
                    hscCode.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_HSC_CreatedBy)));
                    hscCode.setCondition(cursor.getString(INDEX_HSC_Condition));
                    hscCode.setRemarks(cursor.getString(INDEX_HSC_Remarks));
                    hscCode.setRedioHSN(cursor.getString(INDEX_HSC_RedioHSN));
                    hscCode.setSynced(Integer.parseInt(cursor.getString(INDEX_HSC_Synced)));

                    hscCodeArrayList.add(hscCode);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return hscCodeArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_hsc", "" + e);
        }
        return new ArrayList<>();
    }

    // user
    public void addNewUser(LoginUser user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addUser(user, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_user", "" + e);
        }
    }

    public void addUser(LoginUser user, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(USERS_UserID, user.getUserId());
            values.put(USERS_UserName, user.getUserName());
            values.put(USERS_Password, user.getPassword());

            // insert into user table
            long num = db.insert(TABLE_USERS, null, values);
            if(num != -1) {
                Log.d("database", "new user inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(user, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(user, e, origin);
            Log.e("insert user", "" + e);
        }
    }

    public boolean isUserExists(LoginUser user) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_USERS
                    + " WHERE " + USERS_UserName + " = '" + user.getUserName() + "'"
                    + " AND " + USERS_Password + " = '" + user.getPassword() + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0) {
                cursor.close();
                db.close();
                return true;
            } else {
                cursor.close();
                db.close();
                return false;
            }
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("user validate", e+"");
        }
        return false;
    }

    public String getUserID(LoginUser user) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT " + USERS_UserID + " FROM " + TABLE_USERS
                    + " WHERE " + USERS_UserName + " = '" + user.getUserName() + "'"
                    + " AND " + USERS_Password + " = '" + user.getPassword() + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()) {
                String userID = cursor.getString(0);
                cursor.close();
                db.close();
                return userID;
            }
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("user validate", e+"");
        }
        return "0";
    }

    // error log
    public void addNewErrorLog(ErrorLog errorLog) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addErrorLog(errorLog, db);
            int num = db.delete(TABLE_LOGS, LOGS_LogDate + "<" + MyFunctions.getPreviousDateNumeric(), null);
            db.close();
        } catch (Exception e) {
            Log.e("add errorlog", "" + e);
        }
    }

    public void addErrorLog(ErrorLog errorLog, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            values.put(LOGS_ID, errorLog.getID());
            values.put(LOGS_LogDate, errorLog.getLogDate());
            values.put(LOGS_LogTime, errorLog.getLogTime());
            values.put(LOGS_TabID, errorLog.getTabID());
            values.put(LOGS_Origin, errorLog.getOrigin());
            values.put(LOGS_TypeOfError, errorLog.getTypeOfError());
            values.put(LOGS_LogMessage, errorLog.getLogMessage());
            values.put(LOGS_Synced, errorLog.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_LOGS, null, values);
            if(num != -1)
                Log.d("database", "new errorlog inserted");
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("insert errorlog", "" + e);
        }
    }

    public void singleErrorLogSyncSuccessful(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOGS_Synced, LOGS_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_LOGS, values,
                LOGS_ID + "=" + id,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void errorLogsSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOGS_Synced, LOGS_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_LOGS, values,
                LOGS_Synced + "=" + LOGS_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public ArrayList<ErrorLog> getUnsyncedErrorLogList() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_LOGS
                    + " WHERE " + LOGS_Synced + "=" + LOGS_SYNCED_CODE_Unsynced;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<ErrorLog> errorLogs = new ArrayList<>();
            if(cursor.moveToLast()) {
                do {
                    ErrorLog log = new ErrorLog();
                    log.setID(Integer.parseInt(cursor.getString(INDEX_LOGS_ID)));
                    log.setLogDate(Integer.parseInt(cursor.getString(INDEX_LOGS_LogDate)));
                    log.setLogTime(cursor.getString(INDEX_LOGS_LogTime));
                    log.setTabID(cursor.getString(INDEX_LOGS_TabID));
                    log.setOrigin(cursor.getString(INDEX_LOGS_Origin));
                    log.setTypeOfError(cursor.getString(INDEX_LOGS_TypeOfError));
                    log.setLogMessage(cursor.getString(INDEX_LOGS_LogMessage));
                    log.setSynced(Integer.parseInt(cursor.getString(INDEX_LOGS_Synced)));

                    errorLogs.add(log);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return errorLogs;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_logs", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<ErrorLog> getErrorLogList() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_LOGS;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<ErrorLog> errorLogs = new ArrayList<>();
            if(cursor.moveToLast()) {
                do {
                    ErrorLog log = new ErrorLog();
                    log.setID(Integer.parseInt(cursor.getString(INDEX_LOGS_ID)));
                    log.setLogDate(Integer.parseInt(cursor.getString(INDEX_LOGS_LogDate)));
                    log.setLogTime(cursor.getString(INDEX_LOGS_LogTime));
                    log.setTabID(cursor.getString(INDEX_LOGS_TabID));
                    log.setOrigin(cursor.getString(INDEX_LOGS_Origin));
                    log.setTypeOfError(cursor.getString(INDEX_LOGS_TypeOfError));
                    log.setLogMessage(cursor.getString(INDEX_LOGS_LogMessage));
                    log.setSynced(Integer.parseInt(cursor.getString(INDEX_LOGS_Synced)));

                    errorLogs.add(log);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return errorLogs;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_logs", "" + e);
        }
        return new ArrayList<>();
    }

    // eway bill
    public void addNewEWayBill(EwayBill ewayBill) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addEWayBill(ewayBill, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_eway", "" + e);
        }
    }

    public void addEWayBill(EwayBill ewayBill, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            values.put(EWAYBILL_ID, ewayBill.getID());
            values.put(EWAYBILL_SalesID, ewayBill.getSalesID());
            values.put(EWAYBILL_InvoiceNumber, ewayBill.getInvoiceNumber());
            values.put(EWAYBILL_ReasonID, ewayBill.getReasonID());
            values.put(EWAYBILL_DocumentNo, ewayBill.getDocumentNo());
            values.put(EWAYBILL_VehicleNo, ewayBill.getVehicleNo());
            values.put(EWAYBILL_isActive, ewayBill.isActive());
            values.put(EWAYBILL_CreatedBy, ewayBill.getCreatedBy());
            values.put(EWAYBILL_CreatedDtTm, ewayBill.getCreatedDtTm());
            values.put(EWAYBILL_EWayBillNo, ewayBill.getEWayBillNo());
            values.put(EWAYBILL_EWayDocumentPath, ewayBill.getEWayDocumentPath());
            values.put(EWAYBILL_DONumber, ewayBill.getDONumber());
            values.put(EWAYBILL_Synced, ewayBill.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_EWAY_BILL_NUMBER, null, values);
            if(num != -1) {
                Log.d("database", "new ewaybill inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(ewayBill, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(ewayBill, e, origin);
            Log.e("insert eway", "" + e);
        }
    }

    public void updatePreviousEWayBill(EwayBill ewayBill) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
//            values.put(EWAYBILL_ID, ewayBill.getID());
            values.put(EWAYBILL_SalesID, ewayBill.getSalesID());
            values.put(EWAYBILL_InvoiceNumber, ewayBill.getInvoiceNumber());
            values.put(EWAYBILL_ReasonID, ewayBill.getReasonID());
            values.put(EWAYBILL_DocumentNo, ewayBill.getDocumentNo());
            values.put(EWAYBILL_VehicleNo, ewayBill.getVehicleNo());
            values.put(EWAYBILL_isActive, ewayBill.isActive());
            values.put(EWAYBILL_CreatedBy, ewayBill.getCreatedBy());
            values.put(EWAYBILL_CreatedDtTm, ewayBill.getCreatedDtTm());
            values.put(EWAYBILL_EWayBillNo, ewayBill.getEWayBillNo());
            values.put(EWAYBILL_EWayDocumentPath, ewayBill.getEWayDocumentPath());
            values.put(EWAYBILL_DONumber, ewayBill.getDONumber());
            values.put(EWAYBILL_Synced, ewayBill.getSynced());

            // insert into customer table
            int recordsUpdated = db.update(TABLE_EWAY_BILL_NUMBER, values,
                    EWAYBILL_ID + "=" + ewayBill.getID(),null);
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("insert eway", "" + e);
        }
    }

    public void updateEwayBillsList(ArrayList<EwayBill> ewayBillArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_EWAY_BILL_NUMBER, null, null);
            for (EwayBill ewayBill : ewayBillArrayList) {
                addEWayBill(ewayBill, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_eway", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void singleEWayBillSyncSuccessful(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EWAYBILL_Synced, EWAYBILL_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_EWAY_BILL_NUMBER, values,
                EWAYBILL_ID + "=" + id,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void eWayBillsSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EWAYBILL_Synced, EWAYBILL_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_EWAY_BILL_NUMBER, values,
                EWAYBILL_Synced + "<>" + EWAYBILL_SYNCED_CODE_Synced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public ArrayList<EwayBill> getUnsyncedEWayBillList() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_EWAY_BILL_NUMBER
                    + " WHERE " + EWAYBILL_Synced + "<>" + EWAYBILL_SYNCED_CODE_Synced;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<EwayBill> ewayBills = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    EwayBill bill = new EwayBill();
                    bill.setID(Long.parseLong(cursor.getString(INDEX_EWAYBILL_ID)));
                    bill.setSalesID(Long.parseLong(cursor.getString(INDEX_EWAYBILL_SalesID)));
                    bill.setInvoiceNumber(cursor.getString(INDEX_EWAYBILL_InvoiceNumber));
                    bill.setReasonID(Integer.parseInt(cursor.getString(INDEX_EWAYBILL_ReasonID)));
                    bill.setDocumentNo(cursor.getString(INDEX_EWAYBILL_DocumentNo));
                    bill.setVehicleNo(cursor.getString(INDEX_EWAYBILL_VehicleNo));
                    bill.setActive(Boolean.parseBoolean(cursor.getString(INDEX_EWAYBILL_isActive)));
                    bill.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_EWAYBILL_CreatedBy)));
                    bill.setCreatedDtTm(cursor.getString(INDEX_EWAYBILL_CreatedDtTm));
                    bill.setEWayBillNo(cursor.getString(INDEX_EWAYBILL_EWayBillNo));
                    bill.setEWayDocumentPath(cursor.getString(INDEX_EWAYBILL_EWayDocumentPath));
                    bill.setDONumber(cursor.getString(INDEX_EWAYBILL_DONumber));
                    bill.setSynced(Integer.parseInt(cursor.getString(INDEX_EWAYBILL_Synced)));

                    ewayBills.add(bill);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return ewayBills;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_eway", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<EwayBill> getEWayBillList() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_EWAY_BILL_NUMBER;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<EwayBill> ewayBills = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    EwayBill bill = new EwayBill();
                    bill.setID(Long.parseLong(cursor.getString(INDEX_EWAYBILL_ID)));
                    bill.setSalesID(Long.parseLong(cursor.getString(INDEX_EWAYBILL_SalesID)));
                    bill.setInvoiceNumber(cursor.getString(INDEX_EWAYBILL_InvoiceNumber));
                    bill.setReasonID(Integer.parseInt(cursor.getString(INDEX_EWAYBILL_ReasonID)));
                    bill.setDocumentNo(cursor.getString(INDEX_EWAYBILL_DocumentNo));
                    bill.setVehicleNo(cursor.getString(INDEX_EWAYBILL_VehicleNo));
                    bill.setActive(Boolean.parseBoolean(cursor.getString(INDEX_EWAYBILL_isActive)));
                    bill.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_EWAYBILL_CreatedBy)));
                    bill.setCreatedDtTm(cursor.getString(INDEX_EWAYBILL_CreatedDtTm));
                    bill.setEWayBillNo(cursor.getString(INDEX_EWAYBILL_EWayBillNo));
                    bill.setEWayDocumentPath(cursor.getString(INDEX_EWAYBILL_EWayDocumentPath));
                    bill.setDONumber(cursor.getString(INDEX_EWAYBILL_DONumber));
                    bill.setSynced(Integer.parseInt(cursor.getString(INDEX_EWAYBILL_Synced)));

                    ewayBills.add(bill);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return ewayBills;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_eway", "" + e);
        }
        return new ArrayList<>();
    }

    public EwayBill getEwayBillRecordFromSalesInvoice(String invoiceNumber) {
        EwayBill bill = new EwayBill();
        try {
            boolean ewayBillPresent = false;
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_EWAY_BILL_NUMBER + " WHERE " + EWAYBILL_InvoiceNumber + " = '" + invoiceNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                ewayBillPresent = true;
                bill.setID(Long.parseLong(cursor.getString(INDEX_EWAYBILL_ID)));
                bill.setSalesID(Long.parseLong(cursor.getString(INDEX_EWAYBILL_SalesID)));
                bill.setInvoiceNumber(cursor.getString(INDEX_EWAYBILL_InvoiceNumber));
                bill.setReasonID(Integer.parseInt(cursor.getString(INDEX_EWAYBILL_ReasonID)));
                bill.setDocumentNo(cursor.getString(INDEX_EWAYBILL_DocumentNo));
                bill.setVehicleNo(cursor.getString(INDEX_EWAYBILL_VehicleNo));
                bill.setActive(Boolean.parseBoolean(cursor.getString(INDEX_EWAYBILL_isActive)));
                bill.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_EWAYBILL_CreatedBy)));
                bill.setCreatedDtTm(cursor.getString(INDEX_EWAYBILL_CreatedDtTm));
                bill.setEWayBillNo(cursor.getString(INDEX_EWAYBILL_EWayBillNo));
                bill.setEWayDocumentPath(cursor.getString(INDEX_EWAYBILL_EWayDocumentPath));
                bill.setDONumber(cursor.getString(INDEX_EWAYBILL_DONumber));
                bill.setSynced(Integer.parseInt(cursor.getString(INDEX_EWAYBILL_Synced)));
            }

            cursor.close();
            db.close();
            if(ewayBillPresent) {
                return bill;
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("ewaybill", "" + e);
        }
        return null;
    }

    public EwayBill getEwayBillRecordFromDO(String doNumber) {
        EwayBill bill = new EwayBill();
        try {
            boolean ewayBillPresent = false;
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_EWAY_BILL_NUMBER + " WHERE " + EWAYBILL_DONumber + " = '" + doNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                ewayBillPresent = true;
                bill.setID(Long.parseLong(cursor.getString(INDEX_EWAYBILL_ID)));
                bill.setSalesID(Long.parseLong(cursor.getString(INDEX_EWAYBILL_SalesID)));
                bill.setInvoiceNumber(cursor.getString(INDEX_EWAYBILL_InvoiceNumber));
                bill.setReasonID(Integer.parseInt(cursor.getString(INDEX_EWAYBILL_ReasonID)));
                bill.setDocumentNo(cursor.getString(INDEX_EWAYBILL_DocumentNo));
                bill.setVehicleNo(cursor.getString(INDEX_EWAYBILL_VehicleNo));
                bill.setActive(Boolean.parseBoolean(cursor.getString(INDEX_EWAYBILL_isActive)));
                bill.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_EWAYBILL_CreatedBy)));
                bill.setCreatedDtTm(cursor.getString(INDEX_EWAYBILL_CreatedDtTm));
                bill.setEWayBillNo(cursor.getString(INDEX_EWAYBILL_EWayBillNo));
                bill.setEWayDocumentPath(cursor.getString(INDEX_EWAYBILL_EWayDocumentPath));
                bill.setDONumber(cursor.getString(INDEX_EWAYBILL_DONumber));
                bill.setSynced(Integer.parseInt(cursor.getString(INDEX_EWAYBILL_Synced)));
            }

            cursor.close();
            db.close();
            if(ewayBillPresent) {
                return bill;
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("ewaybill", "" + e);
        }
        return null;
    }

    // eway bill REASON
    public void addNewEWayBillReason(EwayBillReason ewayBillReason) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addEWayBillReason(ewayBillReason, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_ewayreason", "" + e);
        }
    }

    public void addEWayBillReason(EwayBillReason ewayBillReason, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(EWAYBILLREASON_ID, ewayBillReason.getID());
            values.put(EWAYBILLREASON_Reason, ewayBillReason.getReason());

            // insert into customer table
            long num = db.insert(TABLE_EWAY_BILL_REASON, null, values);
            if(num != -1) {
                Log.d("database", "new ewaybillreason inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(ewayBillReason, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(ewayBillReason, e, origin);
            Log.e("insert ewayreason", "" + e);
        }
    }

    public void updateEwayBillReasonsList(ArrayList<EwayBillReason> ewayBillReasons) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_EWAY_BILL_REASON, null, null);
            for (EwayBillReason ewayBillReason : ewayBillReasons) {
                addEWayBillReason(ewayBillReason, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_ewayreason", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<EwayBillReason> getEWayBillReasonsList() {
        ArrayList<EwayBillReason> ewayBillReasons = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_EWAY_BILL_REASON;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    EwayBillReason ewayBillReason = new EwayBillReason();
                    ewayBillReason.setID(Integer.parseInt(cursor.getString(INDEX_EWAYBILLREASON_ID)));
                    ewayBillReason.setReason(cursor.getString(INDEX_EWAYBILLREASON_Reason));

                    ewayBillReasons.add(ewayBillReason);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return ewayBillReasons;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_ewayreasons", "" + e);
        }
        return ewayBillReasons;
    }

    public String getEWayBillReason(int id) {
        String reason = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT " + EWAYBILLREASON_Reason + " FROM " + TABLE_EWAY_BILL_REASON
                    + " WHERE " + EWAYBILLREASON_ID + " = " + id;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                reason = cursor.getString(0);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_ewayreasons", "" + e);
        }
        return reason;
    }

    // delivery order
    public long addMasterDeliveryOrder(MasterDeliveryOrder masterDeliveryOrder, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            if(masterDeliveryOrder.getSynced() == 1) {
                values.put(M_DELIVERYORDER_ID, masterDeliveryOrder.getID());
            }
            values.put(M_DELIVERYORDER_DeliveryDate, masterDeliveryOrder.getDeliveryDate());
            values.put(M_DELIVERYORDER_DONumber, masterDeliveryOrder.getDONumber());
            values.put(M_DELIVERYORDER_Customer_ID, masterDeliveryOrder.getCustomer_ID());
            values.put(M_DELIVERYORDER_OutletID, masterDeliveryOrder.getOutletID());
            values.put(M_DELIVERYORDER_IsActive, masterDeliveryOrder.isActive());
            values.put(M_DELIVERYORDER_CreatedBy, masterDeliveryOrder.getCreatedBy());
            values.put(M_DELIVERYORDER_CreatedDtTm, masterDeliveryOrder.getCreatedDtTm());
            values.put(M_DELIVERYORDER_Status, masterDeliveryOrder.getStatus());
            values.put(M_DELIVERYORDER_CustomerName, masterDeliveryOrder.getCustomerName());
            values.put(M_DELIVERYORDER_CustomerEmail, masterDeliveryOrder.getCustomerEmail());
            values.put(M_DELIVERYORDER_CustomerContactPerson, masterDeliveryOrder.getCustomerContactPerson());
            values.put(M_DELIVERYORDER_CustomerMobile, masterDeliveryOrder.getCustomerMobile());
            values.put(M_DELIVERYORDER_CustomerLandline, masterDeliveryOrder.getCustomerLandline());
            values.put(M_DELIVERYORDER_CustomerBillAddress, masterDeliveryOrder.getCustomerBillAddress());
            values.put(M_DELIVERYORDER_CustomerBillStateID, masterDeliveryOrder.getCustomerBillStateID());
            values.put(M_DELIVERYORDER_CustomerShipAddress, masterDeliveryOrder.getCustomerShipAddress());
            values.put(M_DELIVERYORDER_CustomerShipStateID, masterDeliveryOrder.getCustomerShipStateID());
            values.put(M_DELIVERYORDER_TotalQuantity, masterDeliveryOrder.getTotalQuantity());
            values.put(M_DELIVERYORDER_TotalAmount, masterDeliveryOrder.getTotalAmount());
            values.put(M_DELIVERYORDER_Synced, masterDeliveryOrder.getSynced());

            // insert into master sales table
            long num = db.insert(TABLE_M_DELIVERY_ORDER, null, values);
            if(num != -1) {
                Log.d("database", "new master deliveryorder record inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(masterDeliveryOrder, origin);
            }
            return num;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(masterDeliveryOrder, e, origin);
            Log.e("insert mdeliveryorder", "" + e);
        }
        return -1;
    }

    public void addTransactionDeliveryOrder(TransactionDeliveryOrder transactionDeliveryOrder, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            values.put(T_DELIVERYORDER_ID, transactionDeliveryOrder.getID());
            values.put(T_DELIVERYORDER_DOID, transactionDeliveryOrder.getDOID());
            values.put(T_DELIVERYORDER_ProductID, transactionDeliveryOrder.getProductID());
            values.put(T_DELIVERYORDER_Quantity, transactionDeliveryOrder.getQuantity());
            values.put(T_DELIVERYORDER_UnitID, transactionDeliveryOrder.getUnitID());
            values.put(T_DELIVERYORDER_IsActive, transactionDeliveryOrder.isActive());
            values.put(T_DELIVERYORDER_CreatedBy, transactionDeliveryOrder.getCreatedBy());
            values.put(T_DELIVERYORDER_CreatedDtTm, transactionDeliveryOrder.getCreatedDtTm());
            values.put(T_DELIVERYORDER_Status, transactionDeliveryOrder.getStatus());
            values.put(T_DELIVERYORDER_ProductCode, transactionDeliveryOrder.getProductCode());
            values.put(T_DELIVERYORDER_ProductName, transactionDeliveryOrder.getProductName());
            values.put(T_DELIVERYORDER_Amount, transactionDeliveryOrder.getAmount());
            values.put(T_DELIVERYORDER_Synced, transactionDeliveryOrder.getSynced());

            // insert into t sales table
            long num = db.insert(TABLE_T_DELIVERY_ORDER, null, values);
            if(num != -1) {
                Log.d("database", "new transaction delivery order record inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(transactionDeliveryOrder, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(transactionDeliveryOrder, e, origin);
            Log.e("insert t dorder", "" + e);
        }
    }

    public MasterDeliveryOrder getMasterDeliveryOrderRecord(String do_id) {
        MasterDeliveryOrder masterDeliveryOrder = new MasterDeliveryOrder();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_DELIVERY_ORDER + " where "+M_DELIVERYORDER_ID + "=" + do_id;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    masterDeliveryOrder.setID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_ID)));
                    masterDeliveryOrder.setDeliveryDate(cursor.getString(INDEX_M_DELIVERYORDER_DeliveryDate));
                    masterDeliveryOrder.setDONumber(cursor.getString(INDEX_M_DELIVERYORDER_DONumber));
                    masterDeliveryOrder.setCustomer_ID(cursor.getString(INDEX_M_DELIVERYORDER_Customer_ID));
                    masterDeliveryOrder.setOutletID(Long.parseLong(cursor.getString(INDEX_M_DELIVERYORDER_OutletID)));
                    masterDeliveryOrder.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_DELIVERYORDER_IsActive)));
                    masterDeliveryOrder.setCreatedBy(Long.parseLong(cursor.getString(INDEX_M_DELIVERYORDER_CreatedBy)));
                    masterDeliveryOrder.setCreatedDtTm(cursor.getString(INDEX_M_DELIVERYORDER_CreatedDtTm));
                    masterDeliveryOrder.setStatus(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_Status)));
                    masterDeliveryOrder.setCustomerName(cursor.getString(INDEX_M_DELIVERYORDER_CustomerName));
                    masterDeliveryOrder.setCustomerEmail(cursor.getString(INDEX_M_DELIVERYORDER_CustomerEmail));
                    masterDeliveryOrder.setCustomerContactPerson(cursor.getString(INDEX_M_DELIVERYORDER_CustomerContactPerson));
                    masterDeliveryOrder.setCustomerMobile(cursor.getString(INDEX_M_DELIVERYORDER_CustomerMobile));
                    masterDeliveryOrder.setCustomerLandline(cursor.getString(INDEX_M_DELIVERYORDER_CustomerLandline));
                    masterDeliveryOrder.setCustomerBillAddress(cursor.getString(INDEX_M_DELIVERYORDER_CustomerBillAddress));
                    masterDeliveryOrder.setCustomerBillStateID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_CustomerBillStateID)));
                    masterDeliveryOrder.setCustomerShipAddress(cursor.getString(INDEX_M_DELIVERYORDER_CustomerShipAddress));
                    masterDeliveryOrder.setCustomerShipStateID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_CustomerShipStateID)));
                    masterDeliveryOrder.setTotalQuantity(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_TotalQuantity)));
                    masterDeliveryOrder.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_M_DELIVERYORDER_TotalAmount)));
                    masterDeliveryOrder.setSynced(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_Synced)));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return masterDeliveryOrder;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterDeliveryOrder;
    }

    public ArrayList<MasterDeliveryOrder> getDeliveryOrderListFromDB() {
        ArrayList<MasterDeliveryOrder> masterDeliveryOrderArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_DELIVERY_ORDER + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToLast()) {
                do {
                    MasterDeliveryOrder masterDeliveryOrder = new MasterDeliveryOrder();
                    masterDeliveryOrder.setID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_ID)));
                    masterDeliveryOrder.setDeliveryDate(cursor.getString(INDEX_M_DELIVERYORDER_DeliveryDate));
                    masterDeliveryOrder.setDONumber(cursor.getString(INDEX_M_DELIVERYORDER_DONumber));
                    masterDeliveryOrder.setCustomer_ID(cursor.getString(INDEX_M_DELIVERYORDER_Customer_ID));
                    masterDeliveryOrder.setOutletID(Long.parseLong(cursor.getString(INDEX_M_DELIVERYORDER_OutletID)));
                    masterDeliveryOrder.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_DELIVERYORDER_IsActive)));
                    masterDeliveryOrder.setCreatedBy(Long.parseLong(cursor.getString(INDEX_M_DELIVERYORDER_CreatedBy)));
                    masterDeliveryOrder.setCreatedDtTm(cursor.getString(INDEX_M_DELIVERYORDER_CreatedDtTm));
                    masterDeliveryOrder.setStatus(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_Status)));
                    masterDeliveryOrder.setCustomerName(cursor.getString(INDEX_M_DELIVERYORDER_CustomerName));
                    masterDeliveryOrder.setCustomerEmail(cursor.getString(INDEX_M_DELIVERYORDER_CustomerEmail));
                    masterDeliveryOrder.setCustomerContactPerson(cursor.getString(INDEX_M_DELIVERYORDER_CustomerContactPerson));
                    masterDeliveryOrder.setCustomerMobile(cursor.getString(INDEX_M_DELIVERYORDER_CustomerMobile));
                    masterDeliveryOrder.setCustomerLandline(cursor.getString(INDEX_M_DELIVERYORDER_CustomerLandline));
                    masterDeliveryOrder.setCustomerBillAddress(cursor.getString(INDEX_M_DELIVERYORDER_CustomerBillAddress));
                    masterDeliveryOrder.setCustomerBillStateID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_CustomerBillStateID)));
                    masterDeliveryOrder.setCustomerShipAddress(cursor.getString(INDEX_M_DELIVERYORDER_CustomerShipAddress));
                    masterDeliveryOrder.setCustomerShipStateID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_CustomerShipStateID)));
                    masterDeliveryOrder.setTotalQuantity(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_TotalQuantity)));
                    masterDeliveryOrder.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_M_DELIVERYORDER_TotalAmount)));
                    masterDeliveryOrder.setSynced(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_Synced)));

                    masterDeliveryOrderArrayList.add(masterDeliveryOrder);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return masterDeliveryOrderArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master dorder", "" + e);
        }
        return masterDeliveryOrderArrayList;
    }

    public void deliveryOrderSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_DELIVERYORDER_Synced, M_DELIVERYORDER_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_DELIVERY_ORDER, values,
                M_DELIVERYORDER_Synced + "=" + M_DELIVERYORDER_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void singleDeliveryOrderSyncSuccessful(String deliveryOrderID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_DELIVERYORDER_Synced, M_DELIVERYORDER_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_DELIVERY_ORDER, values,
                M_DELIVERYORDER_ID + "=" + deliveryOrderID,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public ArrayList<MasterDeliveryOrder> getUnsyncedDeliveryOrderListFromDB() {
        ArrayList<MasterDeliveryOrder> masterDeliveryOrderArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_DELIVERY_ORDER
                    + " WHERE " + M_DELIVERYORDER_Synced + " = " + DatabaseHandler.M_DELIVERYORDER_SYNCED_CODE_Unsynced + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    MasterDeliveryOrder masterDeliveryOrder = new MasterDeliveryOrder();
                    masterDeliveryOrder.setID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_ID)));
                    masterDeliveryOrder.setDeliveryDate(cursor.getString(INDEX_M_DELIVERYORDER_DeliveryDate));
                    masterDeliveryOrder.setDONumber(cursor.getString(INDEX_M_DELIVERYORDER_DONumber));
                    masterDeliveryOrder.setCustomer_ID(cursor.getString(INDEX_M_DELIVERYORDER_Customer_ID));
                    masterDeliveryOrder.setOutletID(Long.parseLong(cursor.getString(INDEX_M_DELIVERYORDER_OutletID)));
                    masterDeliveryOrder.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_DELIVERYORDER_IsActive)));
                    masterDeliveryOrder.setCreatedBy(Long.parseLong(cursor.getString(INDEX_M_DELIVERYORDER_CreatedBy)));
                    masterDeliveryOrder.setCreatedDtTm(cursor.getString(INDEX_M_DELIVERYORDER_CreatedDtTm));
                    masterDeliveryOrder.setStatus(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_Status)));
                    masterDeliveryOrder.setCustomerName(cursor.getString(INDEX_M_DELIVERYORDER_CustomerName));
                    masterDeliveryOrder.setCustomerEmail(cursor.getString(INDEX_M_DELIVERYORDER_CustomerEmail));
                    masterDeliveryOrder.setCustomerContactPerson(cursor.getString(INDEX_M_DELIVERYORDER_CustomerContactPerson));
                    masterDeliveryOrder.setCustomerMobile(cursor.getString(INDEX_M_DELIVERYORDER_CustomerMobile));
                    masterDeliveryOrder.setCustomerLandline(cursor.getString(INDEX_M_DELIVERYORDER_CustomerLandline));
                    masterDeliveryOrder.setCustomerBillAddress(cursor.getString(INDEX_M_DELIVERYORDER_CustomerBillAddress));
                    masterDeliveryOrder.setCustomerBillStateID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_CustomerBillStateID)));
                    masterDeliveryOrder.setCustomerShipAddress(cursor.getString(INDEX_M_DELIVERYORDER_CustomerShipAddress));
                    masterDeliveryOrder.setCustomerShipStateID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_CustomerShipStateID)));
                    masterDeliveryOrder.setTotalQuantity(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_TotalQuantity)));
                    masterDeliveryOrder.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_M_DELIVERYORDER_TotalAmount)));
                    masterDeliveryOrder.setSynced(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_Synced)));

                    masterDeliveryOrderArrayList.add(masterDeliveryOrder);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return masterDeliveryOrderArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterDeliveryOrderArrayList;
    }

    public ArrayList<TransactionDeliveryOrder> getDeliveryOrderListDetailsFromDB(String DeliveryOrderID) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_T_DELIVERY_ORDER
                    + " WHERE " + T_DELIVERYORDER_DOID + " = " + DeliveryOrderID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    TransactionDeliveryOrder transactionDeliveryOrder = new TransactionDeliveryOrder();
                    transactionDeliveryOrder.setID(Integer.parseInt(cursor.getString(INDEX_T_DELIVERYORDER_ID)));
                    transactionDeliveryOrder.setDOID(Integer.parseInt(cursor.getString(INDEX_T_DELIVERYORDER_DOID)));
                    transactionDeliveryOrder.setProductID(Long.parseLong(cursor.getString(INDEX_T_DELIVERYORDER_ProductID)));
                    transactionDeliveryOrder.setQuantity(Integer.parseInt(cursor.getString(INDEX_T_DELIVERYORDER_Quantity)));
                    transactionDeliveryOrder.setUnitID(Integer.parseInt(cursor.getString(INDEX_T_DELIVERYORDER_UnitID)));
                    transactionDeliveryOrder.setActive(Boolean.parseBoolean(cursor.getString(INDEX_T_DELIVERYORDER_IsActive)));
                    transactionDeliveryOrder.setCreatedBy(Integer.parseInt(cursor.getString(INDEX_T_DELIVERYORDER_CreatedBy)));
                    transactionDeliveryOrder.setCreatedDtTm(cursor.getString(INDEX_T_DELIVERYORDER_CreatedDtTm));
                    transactionDeliveryOrder.setStatus(Integer.parseInt(cursor.getString(INDEX_T_DELIVERYORDER_Status)));
                    transactionDeliveryOrder.setProductCode(cursor.getString(INDEX_T_DELIVERYORDER_ProductCode));
                    transactionDeliveryOrder.setProductName(cursor.getString( INDEX_T_DELIVERYORDER_ProductName));
                    transactionDeliveryOrder.setAmount(Double.parseDouble(cursor.getString(INDEX_T_DELIVERYORDER_Amount)));
                    transactionDeliveryOrder.setSynced(Integer.parseInt(cursor.getString(INDEX_T_DELIVERYORDER_Synced)));

                    transactionDeliveryOrderArrayList.add(transactionDeliveryOrder);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return transactionDeliveryOrderArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_t dorder", "" + e);
        }
        return new ArrayList<>();
    }

    public String getPreviousDONumber(String currentDONumberInitialString) {
        try {
            String query = "SELECT " + M_DELIVERYORDER_DONumber + " FROM " + TABLE_M_DELIVERY_ORDER
                    + " WHERE " + M_DELIVERYORDER_DONumber + " LIKE '" + currentDONumberInitialString + "%'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0) {
                db.close();
                return null;
            } else {
                cursor.moveToLast();
                String previousInvoiceNumber = cursor.getString(0);
                cursor.close();
                db.close();
                return previousInvoiceNumber;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_invoice_number", "" + e);
        }
        // it is okay to send null
        return null;
    }

    public int getRecentMasterDeliveryOrderID(String doNumber, SQLiteDatabase db) {
        try {
            String query = "SELECT " + M_DELIVERYORDER_ID + " FROM " + TABLE_M_DELIVERY_ORDER
                    + " WHERE " + M_DELIVERYORDER_DONumber + " = '" + doNumber + "'";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToLast();
            String DOID = cursor.getString(0);
            cursor.close();
            return Integer.parseInt(DOID);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_do_number", "" + e);
        }
        return 0;
    }

    public void updateTransactionDeliveryOrderList(ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_T_DELIVERY_ORDER, null, null);
            for (TransactionDeliveryOrder transactionDeliveryOrder : transactionDeliveryOrderArrayList) {
                addTransactionDeliveryOrder(transactionDeliveryOrder, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_t dorder", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void addTransactionDeliveryOrderList(ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (TransactionDeliveryOrder transactionDeliveryOrder : transactionDeliveryOrderArrayList) {
                addTransactionDeliveryOrder(transactionDeliveryOrder, db);
            }
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
    }

    public int insertMasterDeliveryOrder(MasterDeliveryOrder masterDeliveryOrder) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addMasterDeliveryOrder(masterDeliveryOrder, db);
            int masterDOID = getRecentMasterDeliveryOrderID(masterDeliveryOrder.getDONumber(), db);
            db.close();
            return masterDOID;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_do number", "" + e);
        }
        return 0;
    }

    public MasterDeliveryOrder getMasterDeliveryOrderRecord(int DOID) {
        MasterDeliveryOrder masterDeliveryOrder = new MasterDeliveryOrder();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_DELIVERY_ORDER + " WHERE " + M_DELIVERYORDER_ID + " = " + DOID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                masterDeliveryOrder.setID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_ID)));
                masterDeliveryOrder.setDeliveryDate(cursor.getString(INDEX_M_DELIVERYORDER_DeliveryDate));
                masterDeliveryOrder.setDONumber(cursor.getString(INDEX_M_DELIVERYORDER_DONumber));
                masterDeliveryOrder.setCustomer_ID(cursor.getString(INDEX_M_DELIVERYORDER_Customer_ID));
                masterDeliveryOrder.setOutletID(Long.parseLong(cursor.getString(INDEX_M_DELIVERYORDER_OutletID)));
                masterDeliveryOrder.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_DELIVERYORDER_IsActive)));
                masterDeliveryOrder.setCreatedBy(Long.parseLong(cursor.getString(INDEX_M_DELIVERYORDER_CreatedBy)));
                masterDeliveryOrder.setCreatedDtTm(cursor.getString(INDEX_M_DELIVERYORDER_CreatedDtTm));
                masterDeliveryOrder.setStatus(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_Status)));
                masterDeliveryOrder.setCustomerName(cursor.getString(INDEX_M_DELIVERYORDER_CustomerName));
                masterDeliveryOrder.setCustomerEmail(cursor.getString(INDEX_M_DELIVERYORDER_CustomerEmail));
                masterDeliveryOrder.setCustomerContactPerson(cursor.getString(INDEX_M_DELIVERYORDER_CustomerContactPerson));
                masterDeliveryOrder.setCustomerMobile(cursor.getString(INDEX_M_DELIVERYORDER_CustomerMobile));
                masterDeliveryOrder.setCustomerLandline(cursor.getString(INDEX_M_DELIVERYORDER_CustomerLandline));
                masterDeliveryOrder.setCustomerBillAddress(cursor.getString(INDEX_M_DELIVERYORDER_CustomerBillAddress));
                masterDeliveryOrder.setCustomerBillStateID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_CustomerBillStateID)));
                masterDeliveryOrder.setCustomerShipAddress(cursor.getString(INDEX_M_DELIVERYORDER_CustomerShipAddress));
                masterDeliveryOrder.setCustomerShipStateID(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_CustomerShipStateID)));
                masterDeliveryOrder.setTotalQuantity(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_TotalQuantity)));
                masterDeliveryOrder.setTotalAmount(Double.parseDouble(cursor.getString(INDEX_M_DELIVERYORDER_TotalAmount)));
                masterDeliveryOrder.setSynced(Integer.parseInt(cursor.getString(INDEX_M_DELIVERYORDER_Synced)));
            }
            cursor.close();
            db.close();
            return masterDeliveryOrder;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterDeliveryOrder;
    }

    public void updateMasterDeliveryOrderList(ArrayList<MasterDeliveryOrder> masterDeliveryOrderArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_M_DELIVERY_ORDER, M_DELIVERYORDER_Synced + " = " + M_DELIVERYORDER_SYNCED_CODE_Synced, null);
            for (MasterDeliveryOrder masterDeliveryOrder : masterDeliveryOrderArrayList) {
                addMasterDeliveryOrder(masterDeliveryOrder, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_masterpurchase", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    // brand
    public void addNewCreditNote(CreditDebitNote creditNote) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addCreditNote(creditNote, db);
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_creditnote", "" + e);
        }
        Log.d("check", creditNote+"");
    }

    public void addCreditNote(CreditDebitNote creditNote, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            if (creditNote.getSynced() == 1) {
//                values.put(CREDITNOTE_CreditNoteID, creditNote.getCreditNoteID());
//            }
            values.put(CREDITNOTE_CreditNoteNumber, creditNote.getNoteNumber());
            values.put(CREDITNOTE_CustomerID, creditNote.getCustomerID());
            values.put(CREDITNOTE_CustomerName, creditNote.getCustomerName());
            values.put(CREDITNOTE_CustomerEmail, creditNote.getCustomerEmail());
            values.put(CREDITNOTE_CustomerMobile, creditNote.getCustomerMobile());
            values.put(CREDITNOTE_CustomerLandline, creditNote.getCustomerLandline());
            values.put(CREDITNOTE_InvoiceNumber, creditNote.getInvoiceNumber());
            values.put(CREDITNOTE_SalesReturnNumber, creditNote.getSalesReturnNumber());
            values.put(CREDITNOTE_OutletID, creditNote.getOutletID());
            values.put(CREDITNOTE_CreditAmount, creditNote.getAmount());
            values.put(CREDITNOTE_Reason, creditNote.getReason());
            values.put(CREDITNOTE_CreatedBy, creditNote.getCreatedBy());
            values.put(CREDITNOTE_CreatedDttm, creditNote.getCreatedDtTm());
            values.put(CREDITNOTE_ModifiedBy, creditNote.getModifiedBy());
            values.put(CREDITNOTE_ModifiedDttm, creditNote.getModifiedDtTm());
            values.put(CREDITNOTE_SalesID, creditNote.getSalesID());
            values.put(CREDITNOTE_NoteType, creditNote.getNoteType());
            values.put(CREDITNOTE_NoteDate, creditNote.getNoteDate());
            values.put(CREDITNOTE_Synced, creditNote.getSynced());

            // insert into credit note table
            long num = db.insert(TABLE_CREDIT_DEBIT_NOTE, null, values);
            if(num != -1) {
                Log.d("database", "new CREDIT NOTE inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(creditNote, origin);
            }
//            customerList.add(customer);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(creditNote, e, origin);
            Log.e("insert creditnote", "" + e);
        }
    }

    public void updateCreditNoteList(ArrayList<CreditDebitNote> creditNoteArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_CREDIT_DEBIT_NOTE, null, null);
            for (CreditDebitNote creditNote : creditNoteArrayList) {
                addCreditNote(creditNote, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_CreditNote", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<CreditDebitNote> getUnsyncedCreditNotes() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CREDIT_DEBIT_NOTE +
                    " WHERE " + CREDITNOTE_Synced + " = " + CREDITNOTE_SYNCED_CODE_Unsynced;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<CreditDebitNote> creditNoteArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    CreditDebitNote creditNote = new CreditDebitNote();
                    creditNote.setCreditNoteID(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreditNoteID)));
                    creditNote.setNoteNumber(cursor.getString(INDEX_CREDITNOTE_CreditNoteNumber));
                    creditNote.setCustomerID(cursor.getString(INDEX_CREDITNOTE_CustomerID));
                    creditNote.setCustomerName(cursor.getString(INDEX_CREDITNOTE_CustomerName));
                    creditNote.setCustomerEmail(cursor.getString(INDEX_CREDITNOTE_CustomerEmail));
                    creditNote.setCustomerMobile(cursor.getString(INDEX_CREDITNOTE_CustomerMobile));
                    creditNote.setCustomerLandline(cursor.getString(INDEX_CREDITNOTE_CustomerLandline));
                    creditNote.setInvoiceNumber(cursor.getString(INDEX_CREDITNOTE_InvoiceNumber));
                    creditNote.setSalesReturnNumber(cursor.getString(INDEX_CREDITNOTE_SalesReturnNumber));
                    creditNote.setOutletID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_OutletID)));
                    creditNote.setAmount(Double.parseDouble(cursor.getString(INDEX_CREDITNOTE_CreditAmount)));
                    creditNote.setReason(cursor.getString(INDEX_CREDITNOTE_Reason));
                    creditNote.setCreatedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreatedBy)));
                    creditNote.setCreatedDtTm(cursor.getString(INDEX_CREDITNOTE_CreatedDttm));
                    creditNote.setModifiedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_ModifiedBy)));
                    creditNote.setModifiedDtTm(cursor.getString(INDEX_CREDITNOTE_ModifiedDttm));
                    creditNote.setSalesID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_SalesID)));
                    creditNote.setNoteType(cursor.getString(INDEX_CREDITNOTE_NoteType));
                    creditNote.setNoteDate(cursor.getString(INDEX_CREDITNOTE_NoteDate));
                    creditNote.setSynced(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_Synced)));

                    creditNoteArrayList.add(creditNote);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return creditNoteArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_creditnote", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<CreditDebitNote> getCreditNotes() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CREDIT_DEBIT_NOTE;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<CreditDebitNote> creditNoteArrayList = new ArrayList<>();
            if(cursor.moveToLast()) {
                do {
                    CreditDebitNote creditNote = new CreditDebitNote();
                    creditNote.setCreditNoteID(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreditNoteID)));
                    creditNote.setNoteNumber(cursor.getString(INDEX_CREDITNOTE_CreditNoteNumber));
                    creditNote.setCustomerID(cursor.getString(INDEX_CREDITNOTE_CustomerID));
                    creditNote.setCustomerName(cursor.getString(INDEX_CREDITNOTE_CustomerName));
                    creditNote.setCustomerEmail(cursor.getString(INDEX_CREDITNOTE_CustomerEmail));
                    creditNote.setCustomerMobile(cursor.getString(INDEX_CREDITNOTE_CustomerMobile));
                    creditNote.setCustomerLandline(cursor.getString(INDEX_CREDITNOTE_CustomerLandline));
                    creditNote.setInvoiceNumber(cursor.getString(INDEX_CREDITNOTE_InvoiceNumber));
                    creditNote.setSalesReturnNumber(cursor.getString(INDEX_CREDITNOTE_SalesReturnNumber));
                    creditNote.setOutletID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_OutletID)));
                    creditNote.setAmount(Double.parseDouble(cursor.getString(INDEX_CREDITNOTE_CreditAmount)));
                    creditNote.setReason(cursor.getString(INDEX_CREDITNOTE_Reason));
                    creditNote.setCreatedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreatedBy)));
                    creditNote.setCreatedDtTm(cursor.getString(INDEX_CREDITNOTE_CreatedDttm));
                    creditNote.setModifiedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_ModifiedBy)));
                    creditNote.setModifiedDtTm(cursor.getString(INDEX_CREDITNOTE_ModifiedDttm));
                    creditNote.setSalesID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_SalesID)));
                    creditNote.setNoteType(cursor.getString(INDEX_CREDITNOTE_NoteType));
                    creditNote.setNoteDate(cursor.getString(INDEX_CREDITNOTE_NoteDate));
                    creditNote.setSynced(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_Synced)));

                    creditNoteArrayList.add(creditNote);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return creditNoteArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_creditnote", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<CreditDebitNote> getCreditNotes(String noteType) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CREDIT_DEBIT_NOTE +
                    " WHERE " + CREDITNOTE_NoteType + " = '" + noteType + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<CreditDebitNote> creditNoteArrayList = new ArrayList<>();
            if(cursor.moveToLast()) {
                do {
                    CreditDebitNote creditNote = new CreditDebitNote();
                    creditNote.setCreditNoteID(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreditNoteID)));
                    creditNote.setNoteNumber(cursor.getString(INDEX_CREDITNOTE_CreditNoteNumber));
                    creditNote.setCustomerID(cursor.getString(INDEX_CREDITNOTE_CustomerID));
                    creditNote.setCustomerName(cursor.getString(INDEX_CREDITNOTE_CustomerName));
                    creditNote.setCustomerEmail(cursor.getString(INDEX_CREDITNOTE_CustomerEmail));
                    creditNote.setCustomerMobile(cursor.getString(INDEX_CREDITNOTE_CustomerMobile));
                    creditNote.setCustomerLandline(cursor.getString(INDEX_CREDITNOTE_CustomerLandline));
                    creditNote.setInvoiceNumber(cursor.getString(INDEX_CREDITNOTE_InvoiceNumber));
                    creditNote.setSalesReturnNumber(cursor.getString(INDEX_CREDITNOTE_SalesReturnNumber));
                    creditNote.setOutletID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_OutletID)));
                    creditNote.setAmount(Double.parseDouble(cursor.getString(INDEX_CREDITNOTE_CreditAmount)));
                    creditNote.setReason(cursor.getString(INDEX_CREDITNOTE_Reason));
                    creditNote.setCreatedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreatedBy)));
                    creditNote.setCreatedDtTm(cursor.getString(INDEX_CREDITNOTE_CreatedDttm));
                    creditNote.setModifiedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_ModifiedBy)));
                    creditNote.setModifiedDtTm(cursor.getString(INDEX_CREDITNOTE_ModifiedDttm));
                    creditNote.setSalesID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_SalesID)));
                    creditNote.setNoteType(cursor.getString(INDEX_CREDITNOTE_NoteType));
                    creditNote.setNoteDate(cursor.getString(INDEX_CREDITNOTE_NoteDate));
                    creditNote.setSynced(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_Synced)));

                    creditNoteArrayList.add(creditNote);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return creditNoteArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_creditnote", "" + e);
        }
        return new ArrayList<>();
    }

    public CreditDebitNote getCreditNoteFromSalesReturn(String salesReturnNumber) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CREDIT_DEBIT_NOTE +
                    " WHERE " + CREDITNOTE_SalesReturnNumber + " = '" + salesReturnNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);
            CreditDebitNote creditNote = new CreditDebitNote();
            if(cursor.moveToFirst()) {
                creditNote.setCreditNoteID(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreditNoteID)));
                creditNote.setNoteNumber(cursor.getString(INDEX_CREDITNOTE_CreditNoteNumber));
                creditNote.setCustomerID(cursor.getString(INDEX_CREDITNOTE_CustomerID));
                creditNote.setCustomerName(cursor.getString(INDEX_CREDITNOTE_CustomerName));
                creditNote.setCustomerEmail(cursor.getString(INDEX_CREDITNOTE_CustomerEmail));
                creditNote.setCustomerMobile(cursor.getString(INDEX_CREDITNOTE_CustomerMobile));
                creditNote.setCustomerLandline(cursor.getString(INDEX_CREDITNOTE_CustomerLandline));
                creditNote.setInvoiceNumber(cursor.getString(INDEX_CREDITNOTE_InvoiceNumber));
                creditNote.setSalesReturnNumber(cursor.getString(INDEX_CREDITNOTE_SalesReturnNumber));
                creditNote.setOutletID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_OutletID)));
                creditNote.setAmount(Double.parseDouble(cursor.getString(INDEX_CREDITNOTE_CreditAmount)));
                creditNote.setReason(cursor.getString(INDEX_CREDITNOTE_Reason));
                creditNote.setCreatedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreatedBy)));
                creditNote.setCreatedDtTm(cursor.getString(INDEX_CREDITNOTE_CreatedDttm));
                creditNote.setModifiedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_ModifiedBy)));
                creditNote.setModifiedDtTm(cursor.getString(INDEX_CREDITNOTE_ModifiedDttm));
                creditNote.setSalesID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_SalesID)));
                creditNote.setNoteType(cursor.getString(INDEX_CREDITNOTE_NoteType));
                creditNote.setNoteDate(cursor.getString(INDEX_CREDITNOTE_NoteDate));
                creditNote.setSynced(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_Synced)));
            } else {
                return null;
            }
            cursor.close();
            db.close();
            return creditNote;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_creditnote", "" + e);
        }
        return null;
    }

    public CreditDebitNote getCreditNoteRecord(String cnId) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_CREDIT_DEBIT_NOTE +
                    " WHERE " + CREDITNOTE_CreditNoteID + " = '" + cnId + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);
            CreditDebitNote creditNote = new CreditDebitNote();
            if(cursor.moveToFirst()) {
                creditNote.setCreditNoteID(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreditNoteID)));
                creditNote.setNoteNumber(cursor.getString(INDEX_CREDITNOTE_CreditNoteNumber));
                creditNote.setCustomerID(cursor.getString(INDEX_CREDITNOTE_CustomerID));
                creditNote.setCustomerName(cursor.getString(INDEX_CREDITNOTE_CustomerName));
                creditNote.setCustomerEmail(cursor.getString(INDEX_CREDITNOTE_CustomerEmail));
                creditNote.setCustomerMobile(cursor.getString(INDEX_CREDITNOTE_CustomerMobile));
                creditNote.setCustomerLandline(cursor.getString(INDEX_CREDITNOTE_CustomerLandline));
                creditNote.setInvoiceNumber(cursor.getString(INDEX_CREDITNOTE_InvoiceNumber));
                creditNote.setSalesReturnNumber(cursor.getString(INDEX_CREDITNOTE_SalesReturnNumber));
                creditNote.setOutletID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_OutletID)));
                creditNote.setAmount(Double.parseDouble(cursor.getString(INDEX_CREDITNOTE_CreditAmount)));
                creditNote.setReason(cursor.getString(INDEX_CREDITNOTE_Reason));
                creditNote.setCreatedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_CreatedBy)));
                creditNote.setCreatedDtTm(cursor.getString(INDEX_CREDITNOTE_CreatedDttm));
                creditNote.setModifiedBy(Long.parseLong(cursor.getString(INDEX_CREDITNOTE_ModifiedBy)));
                creditNote.setModifiedDtTm(cursor.getString(INDEX_CREDITNOTE_ModifiedDttm));
                creditNote.setSalesID(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_SalesID)));
                creditNote.setNoteType(cursor.getString(INDEX_CREDITNOTE_NoteType));
                creditNote.setNoteDate(cursor.getString(INDEX_CREDITNOTE_NoteDate));
                creditNote.setSynced(Integer.parseInt(cursor.getString(INDEX_CREDITNOTE_Synced)));
            } else {
                return null;
            }
            cursor.close();
            db.close();
            return creditNote;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_creditnote", "" + e);
        }
        return null;
    }

    public void creditNotesSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CREDITNOTE_Synced, CREDITNOTE_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_CREDIT_DEBIT_NOTE, values,
                CREDITNOTE_Synced + "=" + CREDITNOTE_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void singleCreditNoteSyncSuccessful(long creditNoteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CREDITNOTE_Synced, CREDITNOTE_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_CREDIT_DEBIT_NOTE, values,
                CREDITNOTE_CreditNoteID + "=" + creditNoteID,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public String getPreviousCreditNoteNumber(String currentCreditNoteNumberInitialString) {
        try {
            String query = "SELECT " + CREDITNOTE_CreditNoteNumber + " FROM " + TABLE_CREDIT_DEBIT_NOTE
                    + " WHERE " + CREDITNOTE_CreditNoteNumber + " LIKE '" + currentCreditNoteNumberInitialString + "%'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0) {
                db.close();
                return null;
            } else {
                cursor.moveToLast();
                String previousCreditNoteNumber = cursor.getString(0);
                cursor.close();
                db.close();
                return previousCreditNoteNumber;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_cn_number", "" + e);
        }
        // it is okay to send null
        return null;
    }

    // sales return
    public void addMasterSalesReturn(MasterSalesReturn masterSalesReturn, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            if (masterSalesReturn.getSynced() == 1) {
                values.put(M_SALESRETURN_SaleReturnID, masterSalesReturn.getSaleReturnID());
            }
            values.put(M_SALESRETURN_SalesReturnDate, masterSalesReturn.getSalesReturnDate());
            values.put(M_SALESRETURN_CustomerID, masterSalesReturn.getCustomerID());
            values.put(M_SALESRETURN_OutletID, masterSalesReturn.getOutletID());
            values.put(M_SALESRETURN_CreditAmount, masterSalesReturn.getCreditAmount());
            values.put(M_SALESRETURN_IsActive, masterSalesReturn.isActive());
            values.put(M_SALESRETURN_CreatedBy, masterSalesReturn.getCreatedBy());
            values.put(M_SALESRETURN_CreatedDttm, masterSalesReturn.getCreatedDttm());
            values.put(M_SALESRETURN_CustomerName, masterSalesReturn.getCustomerName());
            values.put(M_SALESRETURN_CustomerEmail, masterSalesReturn.getCustomerEmail());
            values.put(M_SALESRETURN_CustomerMobile, masterSalesReturn.getCustomerMobile());
            values.put(M_SALESRETURN_CustomerLandline, masterSalesReturn.getCustomerLandline());
            values.put(M_SALESRETURN_InvoiceNumber, masterSalesReturn.getInvoiceNumber());
            values.put(M_SALESRETURN_SalesReturnNumber, masterSalesReturn.getSalesReturnNumber());
            values.put(M_SALESRETURN_Notes, masterSalesReturn.getNotes());
            values.put(M_SALESRETURN_SalesID, masterSalesReturn.getSalesID());
            values.put(M_SALESRETURN_Synced, masterSalesReturn.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_M_SALES_RETURN, null, values);
            if(num != -1) {
                Log.d("database", "new sales return");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(masterSalesReturn, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(masterSalesReturn, e, origin);
            Log.e("insert sales return", "" + e);
        }
    }

    public ArrayList<MasterSalesReturn> getMasterSalesReturnList() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_RETURN ;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<MasterSalesReturn> masterSalesReturnArrayList = new ArrayList<>();
            if(cursor.moveToLast()) {
                do {
                    MasterSalesReturn masterSalesReturn = new MasterSalesReturn();
                    masterSalesReturn.setSaleReturnID(Long.parseLong(cursor.getString(INDEX_M_SALESRETURN_SaleReturnID)));
                    masterSalesReturn.setSalesReturnDate(cursor.getString(INDEX_M_SALESRETURN_SalesReturnDate));
                    masterSalesReturn.setCustomerID(cursor.getString(INDEX_M_SALESRETURN_CustomerID));
                    masterSalesReturn.setOutletID(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_OutletID)));
                    masterSalesReturn.setCreditAmount(Double.parseDouble(cursor.getString(INDEX_M_SALESRETURN_CreditAmount)));
                    masterSalesReturn.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_SALESRETURN_IsActive)));
                    masterSalesReturn.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_SALESRETURN_CreatedBy)));
                    masterSalesReturn.setCreatedDttm(cursor.getString(INDEX_M_SALESRETURN_CreatedDttm));
                    masterSalesReturn.setCustomerName(cursor.getString(INDEX_M_SALESRETURN_CustomerName));
                    masterSalesReturn.setCustomerEmail(cursor.getString(INDEX_M_SALESRETURN_CustomerEmail));
                    masterSalesReturn.setCustomerMobile(cursor.getString(INDEX_M_SALESRETURN_CustomerMobile));
                    masterSalesReturn.setCustomerLandline(cursor.getString(INDEX_M_SALESRETURN_CustomerLandline));
                    masterSalesReturn.setInvoiceNumber(cursor.getString(INDEX_M_SALESRETURN_InvoiceNumber));
                    masterSalesReturn.setSalesReturnNumber(cursor.getString(INDEX_M_SALESRETURN_SalesReturnNumber));
                    masterSalesReturn.setNotes(cursor.getString(INDEX_M_SALESRETURN_Notes));
                    masterSalesReturn.setSalesID(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_SalesID)));
                    masterSalesReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_Synced)));

                    masterSalesReturnArrayList.add(masterSalesReturn);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return masterSalesReturnArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_m sales_return", "" + e);
        }
        return new ArrayList<>();
    }

    public void addTransactionSalesReturn(TransactionSalesReturn transactionSalesReturn, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            values.put(T_SALESRETURN_SalesReturnDetailID, transactionSalesReturn.getSalesReturnDetailID());
            values.put(T_SALESRETURN_SalesReturnID, transactionSalesReturn.getSalesReturnID());
            values.put(T_SALESRETURN_ProductID, transactionSalesReturn.getProductID());
            values.put(T_SALESRETURN_ProductCode, transactionSalesReturn.getProductCode());
            values.put(T_SALESRETURN_UnitID, transactionSalesReturn.getUnitID());
            values.put(T_SALESRETURN_ReturnPrice, transactionSalesReturn.getReturnPrice());
            values.put(T_SALESRETURN_ReturnQty, transactionSalesReturn.getReturnQty());
            values.put(T_SALESRETURN_ReturnTaxRate, transactionSalesReturn.getReturnTaxRate());
            values.put(T_SALESRETURN_ReturnTaxAmount, transactionSalesReturn.getReturnTaxAmount());
            values.put(T_SALESRETURN_ReturnAmount, transactionSalesReturn.getReturnAmount());
            values.put(T_SALESRETURN_ProductName, transactionSalesReturn.getProductName());
            values.put(T_SALESRETURN_ReturnType, transactionSalesReturn.getReturnType());
            values.put(T_SALESRETURN_Synced, transactionSalesReturn.getSynced());

            // insert into t sales table
            long num = db.insert(TABLE_T_SALES_RETURN, null, values);
            if(num != -1) {
                Log.d("database", "new transaction d order record inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(transactionSalesReturn, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(transactionSalesReturn, e, origin);
            Log.e("insert t dorder", "" + e);
        }
    }

    public MasterSalesReturn getMasterSalesReturnRecord(String sr_id) {
        MasterSalesReturn masterSalesReturn = new MasterSalesReturn();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_RETURN + " where " + M_SALESRETURN_SaleReturnID + "=" + sr_id;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    masterSalesReturn.setSaleReturnID(Long.parseLong(cursor.getString(INDEX_M_SALESRETURN_SaleReturnID)));
                    masterSalesReturn.setSalesReturnDate(cursor.getString(INDEX_M_SALESRETURN_SalesReturnDate));
                    masterSalesReturn.setCustomerID(cursor.getString(INDEX_M_SALESRETURN_CustomerID));
                    masterSalesReturn.setOutletID(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_OutletID)));
                    masterSalesReturn.setCreditAmount(Double.parseDouble(cursor.getString(INDEX_M_SALESRETURN_CreditAmount)));
                    masterSalesReturn.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_SALESRETURN_IsActive)));
                    masterSalesReturn.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_SALESRETURN_CreatedBy)));
                    masterSalesReturn.setCreatedDttm(cursor.getString(INDEX_M_SALESRETURN_CreatedDttm));
                    masterSalesReturn.setCustomerName(cursor.getString(INDEX_M_SALESRETURN_CustomerName));
                    masterSalesReturn.setCustomerEmail(cursor.getString(INDEX_M_SALESRETURN_CustomerEmail));
                    masterSalesReturn.setCustomerMobile(cursor.getString(INDEX_M_SALESRETURN_CustomerMobile));
                    masterSalesReturn.setCustomerLandline(cursor.getString(INDEX_M_SALESRETURN_CustomerLandline));
                    masterSalesReturn.setInvoiceNumber(cursor.getString(INDEX_M_SALESRETURN_InvoiceNumber));
                    masterSalesReturn.setSalesReturnNumber(cursor.getString(INDEX_M_SALESRETURN_SalesReturnNumber));
                    masterSalesReturn.setNotes(cursor.getString(INDEX_M_SALESRETURN_Notes));
                    masterSalesReturn.setSalesID(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_SalesID)));
                    masterSalesReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_Synced)));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterSalesReturn;
    }

    public void salesReturnSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_SALESRETURN_Synced, M_SALESRETURN_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_SALES_RETURN, values,
                M_SALESRETURN_Synced + "=" + M_SALESRETURN_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void singleSalesReturnSyncSuccessful(String sr_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_SALESRETURN_Synced, M_SALESRETURN_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_SALES_RETURN, values,
                M_SALESRETURN_SaleReturnID + "=" + sr_ID,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public ArrayList<MasterSalesReturn> getUnsyncedSalesReturnListFromDB() {
        ArrayList<MasterSalesReturn> masterSalesReturnArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_RETURN
                    + " WHERE " + M_SALESRETURN_Synced + " = " + DatabaseHandler.M_SALESRETURN_SYNCED_CODE_Unsynced + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    MasterSalesReturn masterSalesReturn = new MasterSalesReturn();
                    masterSalesReturn.setSaleReturnID(Long.parseLong(cursor.getString(INDEX_M_SALESRETURN_SaleReturnID)));
                    masterSalesReturn.setSalesReturnDate(cursor.getString(INDEX_M_SALESRETURN_SalesReturnDate));
                    masterSalesReturn.setCustomerID(cursor.getString(INDEX_M_SALESRETURN_CustomerID));
                    masterSalesReturn.setOutletID(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_OutletID)));
                    masterSalesReturn.setCreditAmount(Double.parseDouble(cursor.getString(INDEX_M_SALESRETURN_CreditAmount)));
                    masterSalesReturn.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_SALESRETURN_IsActive)));
                    masterSalesReturn.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_SALESRETURN_CreatedBy)));
                    masterSalesReturn.setCreatedDttm(cursor.getString(INDEX_M_SALESRETURN_CreatedDttm));
                    masterSalesReturn.setCustomerName(cursor.getString(INDEX_M_SALESRETURN_CustomerName));
                    masterSalesReturn.setCustomerEmail(cursor.getString(INDEX_M_SALESRETURN_CustomerEmail));
                    masterSalesReturn.setCustomerMobile(cursor.getString(INDEX_M_SALESRETURN_CustomerMobile));
                    masterSalesReturn.setCustomerLandline(cursor.getString(INDEX_M_SALESRETURN_CustomerLandline));
                    masterSalesReturn.setInvoiceNumber(cursor.getString(INDEX_M_SALESRETURN_InvoiceNumber));
                    masterSalesReturn.setSalesReturnNumber(cursor.getString(INDEX_M_SALESRETURN_SalesReturnNumber));
                    masterSalesReturn.setNotes(cursor.getString(INDEX_M_SALESRETURN_Notes));
                    masterSalesReturn.setSalesID(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_SalesID)));
                    masterSalesReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_Synced)));

                    masterSalesReturnArrayList.add(masterSalesReturn);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterSalesReturnArrayList;
    }

    public ArrayList<TransactionSalesReturn> getSalesReturnListDetailsFromDB(String SalesReturnID) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_T_SALES_RETURN
                    + " WHERE " + T_SALESRETURN_SalesReturnID + " = " + SalesReturnID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<TransactionSalesReturn> transactionSalesReturnArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    TransactionSalesReturn transactionSalesReturn = new TransactionSalesReturn();
                    transactionSalesReturn.setSalesReturnDetailID(Long.parseLong(cursor.getString(INDEX_T_SALESRETURN_SalesReturnDetailID)));
                    transactionSalesReturn.setSalesReturnID(Long.parseLong(cursor.getString(INDEX_T_SALESRETURN_SalesReturnID)));
                    transactionSalesReturn.setProductID(Long.parseLong(cursor.getString(INDEX_T_SALESRETURN_ProductID)));
                    transactionSalesReturn.setProductCode(cursor.getString(INDEX_T_SALESRETURN_ProductCode));
                    transactionSalesReturn.setUnitID(Integer.parseInt(cursor.getString(INDEX_T_SALESRETURN_UnitID)));
                    transactionSalesReturn.setReturnPrice(Double.parseDouble(cursor.getString(INDEX_T_SALESRETURN_ReturnPrice)));
                    transactionSalesReturn.setReturnQty(Integer.parseInt(cursor.getString(INDEX_T_SALESRETURN_ReturnQty)));
                    transactionSalesReturn.setReturnTaxRate(Double.parseDouble(cursor.getString(INDEX_T_SALESRETURN_ReturnTaxRate)));
                    transactionSalesReturn.setReturnTaxAmount(Double.parseDouble(cursor.getString(INDEX_T_SALESRETURN_ReturnTaxAmount)));
                    transactionSalesReturn.setReturnAmount(Double.parseDouble(cursor.getString(INDEX_T_SALESRETURN_ReturnAmount)));
                    transactionSalesReturn.setProductName(cursor.getString(INDEX_T_SALESRETURN_ProductName));
                    transactionSalesReturn.setReturnType(Integer.parseInt(cursor.getString(INDEX_T_SALESRETURN_ReturnType)));
                    transactionSalesReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_T_SALESRETURN_Synced)));

                    transactionSalesReturnArrayList.add(transactionSalesReturn);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return transactionSalesReturnArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_t dorder", "" + e);
        }
        return new ArrayList<>();
    }

    public String getPreviousSalesReturnNumber(String currentSalesReturnNumberInitialString) {
        try {
            String query = "SELECT " + M_SALESRETURN_SalesReturnNumber + " FROM " + TABLE_M_SALES_RETURN
                    + " WHERE " + M_SALESRETURN_SalesReturnNumber + " LIKE '" + currentSalesReturnNumberInitialString + "%'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0) {
                db.close();
                return null;
            } else {
                cursor.moveToLast();
                String previousInvoiceNumber = cursor.getString(0);
                cursor.close();
                db.close();
                return previousInvoiceNumber;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_invoice_number", "" + e);
        }
        // it is okay to send null
        return null;
    }

    public int getRecentMasterSalesReturnID(String srNumber, SQLiteDatabase db) {
        try {
            String query = "SELECT " + M_SALESRETURN_SaleReturnID + " FROM " + TABLE_M_SALES_RETURN
                    + " WHERE " + M_SALESRETURN_SalesReturnNumber + " = '" + srNumber + "'";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToLast();
            String SR_ID = cursor.getString(0);
            cursor.close();
            return Integer.parseInt(SR_ID);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_do_number", "" + e);
        }
        return 0;
    }

    public void updateTransactionSalesReturnList(ArrayList<TransactionSalesReturn> transactionSalesReturnArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_T_SALES_RETURN, null, null);
            for (TransactionSalesReturn transactionSalesReturn : transactionSalesReturnArrayList) {
                addTransactionSalesReturn(transactionSalesReturn, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_t dorder", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void addTransactionSalesReturnList(ArrayList<TransactionSalesReturn> transactionSalesReturnArrayList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (TransactionSalesReturn transactionSalesReturn : transactionSalesReturnArrayList) {
                addTransactionSalesReturn(transactionSalesReturn, db);
            }
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
    }

    public int insertMasterSalesReturn(MasterSalesReturn masterSalesReturn) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addMasterSalesReturn(masterSalesReturn, db);
            int masterSRID = getRecentMasterSalesReturnID(masterSalesReturn.getSalesReturnNumber(), db);
            db.close();
            return masterSRID;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_do number", "" + e);
        }
        return 0;
    }

    public MasterSalesReturn getMasterSalesReturnRecord(int SR_ID) {
        MasterSalesReturn masterSalesReturn = new MasterSalesReturn();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_RETURN + " WHERE " + M_SALESRETURN_SaleReturnID + " = " + SR_ID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                masterSalesReturn.setSaleReturnID(Long.parseLong(cursor.getString(INDEX_M_SALESRETURN_SaleReturnID)));
                masterSalesReturn.setSalesReturnDate(cursor.getString(INDEX_M_SALESRETURN_SalesReturnDate));
                masterSalesReturn.setCustomerID(cursor.getString(INDEX_M_SALESRETURN_CustomerID));
                masterSalesReturn.setOutletID(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_OutletID)));
                masterSalesReturn.setCreditAmount(Double.parseDouble(cursor.getString(INDEX_M_SALESRETURN_CreditAmount)));
                masterSalesReturn.setActive(Boolean.parseBoolean(cursor.getString(INDEX_M_SALESRETURN_IsActive)));
                masterSalesReturn.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_SALESRETURN_CreatedBy)));
                masterSalesReturn.setCreatedDttm(cursor.getString(INDEX_M_SALESRETURN_CreatedDttm));
                masterSalesReturn.setCustomerName(cursor.getString(INDEX_M_SALESRETURN_CustomerName));
                masterSalesReturn.setCustomerEmail(cursor.getString(INDEX_M_SALESRETURN_CustomerEmail));
                masterSalesReturn.setCustomerMobile(cursor.getString(INDEX_M_SALESRETURN_CustomerMobile));
                masterSalesReturn.setCustomerLandline(cursor.getString(INDEX_M_SALESRETURN_CustomerLandline));
                masterSalesReturn.setInvoiceNumber(cursor.getString(INDEX_M_SALESRETURN_InvoiceNumber));
                masterSalesReturn.setSalesReturnNumber(cursor.getString(INDEX_M_SALESRETURN_SalesReturnNumber));
                masterSalesReturn.setNotes(cursor.getString(INDEX_M_SALESRETURN_Notes));
                masterSalesReturn.setSalesID(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_SalesID)));
                masterSalesReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_M_SALESRETURN_Synced)));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterSalesReturn;
    }

    public void updateMasterSalesReturnList(ArrayList<MasterSalesReturn> masterSalesReturnArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_M_SALES_RETURN, M_SALESRETURN_Synced + " = " + M_SALESRETURN_SYNCED_CODE_Synced, null);
            for (MasterSalesReturn masterSalesReturn : masterSalesReturnArrayList) {
                addMasterSalesReturn(masterSalesReturn, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_masterpurchase", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public boolean isMasterSalesReturnPresent(String invoiceNumber) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_SALES_RETURN
                    + " WHERE " + M_SALESRETURN_InvoiceNumber + " = '" + invoiceNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                cursor.close();
                db.close();
                return true;
            } else {
                cursor.close();
                db.close();
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return false;
    }

    // purchase return
    public void addMasterPurchaseReturn(MasterPurchaseReturn masterPurchaseReturn, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            if (masterPurchaseReturn.getSynced() == 1) {
                values.put(M_PURCHASERETURN_PurchaseReturnID, masterPurchaseReturn.getPurchaseReturnID());
            }
            values.put(M_PURCHASERETURN_PurchaseReturnNumber, masterPurchaseReturn.getPurchaseReturnNumber());
            values.put(M_PURCHASERETURN_InvoiceNumber, masterPurchaseReturn.getInvoiceNumber());
            values.put(M_PURCHASERETURN_PurchaseReturnDate, masterPurchaseReturn.getPurchaseReturnDate());
            values.put(M_PURCHASERETURN_CreatedBy, masterPurchaseReturn.getCreatedBy());
            values.put(M_PURCHASERETURN_CreatedDtTm, masterPurchaseReturn.getCreatedDtTm());
            values.put(M_PURCHASERETURN_OutletID, masterPurchaseReturn.getOutletID());
            values.put(M_PURCHASERETURN_CGST, masterPurchaseReturn.getCGST());
            values.put(M_PURCHASERETURN_SGST, masterPurchaseReturn.getSGST());
            values.put(M_PURCHASERETURN_IGST, masterPurchaseReturn.getIGST());
            values.put(M_PURCHASERETURN_CESS, masterPurchaseReturn.getCESS());
            values.put(M_PURCHASERETURN_SubTotal, masterPurchaseReturn.getSubTotal());
            values.put(M_PURCHASERETURN_DebitAmount, masterPurchaseReturn.getDebitAmount());
            values.put(M_PURCHASERETURN_SupplierID, masterPurchaseReturn.getSupplierID());
            values.put(M_PURCHASERETURN_SupplierName, masterPurchaseReturn.getSupplierName());
            values.put(M_PURCHASERETURN_SupplierEmail, masterPurchaseReturn.getSupplierEmail());
            values.put(M_PURCHASERETURN_SupplierMobile, masterPurchaseReturn.getSupplierMobile());
            values.put(M_PURCHASERETURN_Notes, masterPurchaseReturn.getNotes());
            values.put(M_PURCHASERETURN_PurchaseID, masterPurchaseReturn.getPurchaseID());
            values.put(M_PURCHASERETURN_Synced, masterPurchaseReturn.getSynced());

            // insert into customer table
            long num = db.insert(TABLE_M_PURCHASE_RETURN, null, values);
            if(num != -1) {
                Log.d("database", "new PURCHASE return");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(masterPurchaseReturn, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(masterPurchaseReturn, e, origin);
            Log.e("insert purchase return", "" + e);
        }
    }

    public ArrayList<MasterPurchaseReturn> getMasterPurchaseReturnList() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_RETURN ;
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<MasterPurchaseReturn> masterPurchaseReturnArrayList = new ArrayList<>();
            if(cursor.moveToLast()) {
                do {
                    MasterPurchaseReturn masterPurchaseReturn = new MasterPurchaseReturn();
                    masterPurchaseReturn.setPurchaseReturnID(Long.parseLong(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnID)));
                    masterPurchaseReturn.setPurchaseReturnNumber(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnNumber));
                    masterPurchaseReturn.setInvoiceNumber(cursor.getString(INDEX_M_PURCHASERETURN_InvoiceNumber));
                    masterPurchaseReturn.setPurchaseReturnDate(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnDate));
                    masterPurchaseReturn.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CreatedBy)));
                    masterPurchaseReturn.setCreatedDtTm(cursor.getString(INDEX_M_PURCHASERETURN_CreatedDtTm));
                    masterPurchaseReturn.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_OutletID)));
                    masterPurchaseReturn.setCGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CGST)));
                    masterPurchaseReturn.setSGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_SGST)));
                    masterPurchaseReturn.setIGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_IGST)));
                    masterPurchaseReturn.setCESS(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CESS)));
                    masterPurchaseReturn.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_SubTotal)));
                    masterPurchaseReturn.setDebitAmount(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_DebitAmount)));
                    masterPurchaseReturn.setSupplierID(cursor.getString(INDEX_M_PURCHASERETURN_SupplierID));
                    masterPurchaseReturn.setSupplierName(cursor.getString(INDEX_M_PURCHASERETURN_SupplierName));
                    masterPurchaseReturn.setSupplierEmail(cursor.getString(INDEX_M_PURCHASERETURN_SupplierEmail));
                    masterPurchaseReturn.setSupplierMobile(cursor.getString(INDEX_M_PURCHASERETURN_SupplierMobile));
                    masterPurchaseReturn.setNotes(cursor.getString(INDEX_M_PURCHASERETURN_Notes));
                    masterPurchaseReturn.setPurchaseID(Long.parseLong(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseID)));
                    masterPurchaseReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_M_PURCHASERETURN_Synced)));

                    masterPurchaseReturnArrayList.add(masterPurchaseReturn);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            db.close();
            return masterPurchaseReturnArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_m sales_return", "" + e);
        }
        return new ArrayList<>();
    }

    public void addTransactionPurchaseReturn(TransactionPurchaseReturn transactionPurchaseReturn, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
//            values.put(T_PURCHASERETURN_PurchaseDetailID, transactionPurchaseReturn.getPurchaseDetailID());
            values.put(T_PURCHASERETURN_PurchaseReturnID, transactionPurchaseReturn.getPurchaseReturnID());
            values.put(T_PURCHASERETURN_ProductID, transactionPurchaseReturn.getProductID());
            values.put(T_PURCHASERETURN_ProductCode, transactionPurchaseReturn.getProductCode());
            values.put(T_PURCHASERETURN_UnitID, transactionPurchaseReturn.getUnitID());
            values.put(T_PURCHASERETURN_ReturnQty, transactionPurchaseReturn.getReturnQty());
            values.put(T_PURCHASERETURN_UnitPrice, transactionPurchaseReturn.getUnitPrice());
            values.put(T_PURCHASERETURN_ReturnAmount, transactionPurchaseReturn.getReturnAmount());
            values.put(T_PURCHASERETURN_ReturnTaxAmount, transactionPurchaseReturn.getReturnTaxAmount());
            values.put(T_PURCHASERETURN_ProductName, transactionPurchaseReturn.getProductName());
            values.put(T_PURCHASERETURN_CGST, transactionPurchaseReturn.getCGST());
            values.put(T_PURCHASERETURN_SGST, transactionPurchaseReturn.getSGST());
            values.put(T_PURCHASERETURN_IGST, transactionPurchaseReturn.getIGST());
            values.put(T_PURCHASERETURN_CESS, transactionPurchaseReturn.getCESS());

            // insert into t sales table
            long num = db.insert(TABLE_T_PURCHASE_RETURN, null, values);
            if(num != -1) {
                Log.d("database", "new transaction p return record inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(transactionPurchaseReturn, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(transactionPurchaseReturn, e, origin);
            Log.e("insert t p return", "" + e);
        }
    }

    public MasterPurchaseReturn getMasterPurchaseReturnRecord(String pr_id) {
        MasterPurchaseReturn masterPurchaseReturn = new MasterPurchaseReturn();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_RETURN + " where " + M_PURCHASERETURN_PurchaseReturnID + "=" + pr_id;
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    masterPurchaseReturn.setPurchaseReturnID(Long.parseLong(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnID)));
                    masterPurchaseReturn.setPurchaseReturnNumber(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnNumber));
                    masterPurchaseReturn.setInvoiceNumber(cursor.getString(INDEX_M_PURCHASERETURN_InvoiceNumber));
                    masterPurchaseReturn.setPurchaseReturnDate(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnDate));
                    masterPurchaseReturn.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CreatedBy)));
                    masterPurchaseReturn.setCreatedDtTm(cursor.getString(INDEX_M_PURCHASERETURN_CreatedDtTm));
                    masterPurchaseReturn.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_OutletID)));
                    masterPurchaseReturn.setCGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CGST)));
                    masterPurchaseReturn.setSGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_SGST)));
                    masterPurchaseReturn.setIGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_IGST)));
                    masterPurchaseReturn.setCESS(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CESS)));
                    masterPurchaseReturn.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_SubTotal)));
                    masterPurchaseReturn.setDebitAmount(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_DebitAmount)));
                    masterPurchaseReturn.setSupplierID(cursor.getString(INDEX_M_PURCHASERETURN_SupplierID));
                    masterPurchaseReturn.setSupplierName(cursor.getString(INDEX_M_PURCHASERETURN_SupplierName));
                    masterPurchaseReturn.setSupplierEmail(cursor.getString(INDEX_M_PURCHASERETURN_SupplierEmail));
                    masterPurchaseReturn.setSupplierMobile(cursor.getString(INDEX_M_PURCHASERETURN_SupplierMobile));
                    masterPurchaseReturn.setNotes(cursor.getString(INDEX_M_PURCHASERETURN_Notes));
                    masterPurchaseReturn.setPurchaseID(Long.parseLong(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseID)));
                    masterPurchaseReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_M_PURCHASERETURN_Synced)));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterPurchaseReturn;
    }

    public void purchaseReturnSyncSuccessful() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_PURCHASERETURN_Synced, M_PURCHASERETURN_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_PURCHASE_RETURN, values,
                M_PURCHASERETURN_Synced + "=" + M_PURCHASERETURN_SYNCED_CODE_Unsynced,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public void singlePurchaseReturnSyncSuccessful(String pr_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(M_PURCHASERETURN_Synced, M_PURCHASERETURN_SYNCED_CODE_Synced);
        int recordsUpdated = db.update(TABLE_M_PURCHASE_RETURN, values,
                M_PURCHASERETURN_PurchaseReturnID + "=" + pr_ID,null);
        Log.i("records changed", recordsUpdated+"");
    }

    public ArrayList<MasterPurchaseReturn> getUnsyncedPurchaseReturnListFromDB() {
        ArrayList<MasterPurchaseReturn> masterPurchaseReturnArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_RETURN
                    + " WHERE " + M_PURCHASERETURN_Synced + " = " + DatabaseHandler.M_PURCHASERETURN_SYNCED_CODE_Unsynced + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    MasterPurchaseReturn masterPurchaseReturn = new MasterPurchaseReturn();
                    masterPurchaseReturn.setPurchaseReturnID(Long.parseLong(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnID)));
                    masterPurchaseReturn.setPurchaseReturnNumber(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnNumber));
                    masterPurchaseReturn.setInvoiceNumber(cursor.getString(INDEX_M_PURCHASERETURN_InvoiceNumber));
                    masterPurchaseReturn.setPurchaseReturnDate(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnDate));
                    masterPurchaseReturn.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CreatedBy)));
                    masterPurchaseReturn.setCreatedDtTm(cursor.getString(INDEX_M_PURCHASERETURN_CreatedDtTm));
                    masterPurchaseReturn.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_OutletID)));
                    masterPurchaseReturn.setCGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CGST)));
                    masterPurchaseReturn.setSGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_SGST)));
                    masterPurchaseReturn.setIGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_IGST)));
                    masterPurchaseReturn.setCESS(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CESS)));
                    masterPurchaseReturn.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_SubTotal)));
                    masterPurchaseReturn.setDebitAmount(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_DebitAmount)));
                    masterPurchaseReturn.setSupplierID(cursor.getString(INDEX_M_PURCHASERETURN_SupplierID));
                    masterPurchaseReturn.setSupplierName(cursor.getString(INDEX_M_PURCHASERETURN_SupplierName));
                    masterPurchaseReturn.setSupplierEmail(cursor.getString(INDEX_M_PURCHASERETURN_SupplierEmail));
                    masterPurchaseReturn.setSupplierMobile(cursor.getString(INDEX_M_PURCHASERETURN_SupplierMobile));
                    masterPurchaseReturn.setNotes(cursor.getString(INDEX_M_PURCHASERETURN_Notes));
                    masterPurchaseReturn.setPurchaseID(Long.parseLong(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseID)));
                    masterPurchaseReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_M_PURCHASERETURN_Synced)));

                    masterPurchaseReturnArrayList.add(masterPurchaseReturn);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterPurchaseReturnArrayList;
    }

    public ArrayList<TransactionPurchaseReturn> getPurchaseReturnListDetailsFromDB(String PurchaseReturnID) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_T_PURCHASE_RETURN
                    + " WHERE " + T_PURCHASERETURN_PurchaseReturnID + " = " + PurchaseReturnID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<TransactionPurchaseReturn> transactionPurchaseReturnArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    TransactionPurchaseReturn transactionPurchaseReturn = new TransactionPurchaseReturn();
                    transactionPurchaseReturn.setPurchaseDetailID(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_PurchaseDetailID)));
                    transactionPurchaseReturn.setPurchaseReturnID(Long.parseLong(cursor.getString(INDEX_T_PURCHASERETURN_PurchaseReturnID)));
                    transactionPurchaseReturn.setProductID(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_ProductID)));
                    transactionPurchaseReturn.setProductCode(cursor.getString(INDEX_T_PURCHASERETURN_ProductCode));
                    transactionPurchaseReturn.setUnitID(Long.parseLong(cursor.getString(INDEX_T_PURCHASERETURN_UnitID)));
                    transactionPurchaseReturn.setReturnQty(Integer.parseInt(cursor.getString(INDEX_T_PURCHASERETURN_ReturnQty)));
                    transactionPurchaseReturn.setUnitPrice(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_UnitPrice)));
                    transactionPurchaseReturn.setReturnAmount(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_ReturnAmount)));
                    transactionPurchaseReturn.setReturnTaxAmount(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_ReturnTaxAmount)));
                    transactionPurchaseReturn.setProductName(cursor.getString(INDEX_T_PURCHASERETURN_ProductName));
                    transactionPurchaseReturn.setCGST(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_CGST)));
                    transactionPurchaseReturn.setSGST(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_SGST)));
                    transactionPurchaseReturn.setIGST(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_IGST)));
                    transactionPurchaseReturn.setCESS(Double.parseDouble(cursor.getString(INDEX_T_PURCHASERETURN_CESS)));

                    transactionPurchaseReturnArrayList.add(transactionPurchaseReturn);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return transactionPurchaseReturnArrayList;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_t dorder", "" + e);
        }
        return new ArrayList<>();
    }

    public String getPreviousPurchaseReturnNumber(String currentPurchaseReturnNumberInitialString) {
        try {
            String query = "SELECT " + M_PURCHASERETURN_PurchaseReturnNumber + " FROM " + TABLE_M_PURCHASE_RETURN
                    + " WHERE " + M_PURCHASERETURN_PurchaseReturnNumber + " LIKE '" + currentPurchaseReturnNumberInitialString + "%'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0) {
                db.close();
                return null;
            } else {
                cursor.moveToLast();
                String previousInvoiceNumber = cursor.getString(0);
                cursor.close();
                db.close();
                return previousInvoiceNumber;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_invoice_number", "" + e);
        }
        // it is okay to send null
        return null;
    }

    public int getRecentMasterPurchaseReturnID(String srNumber, SQLiteDatabase db) {
        try {
            String query = "SELECT " + M_PURCHASERETURN_PurchaseReturnID + " FROM " + TABLE_M_PURCHASE_RETURN
                    + " WHERE " + M_PURCHASERETURN_PurchaseReturnNumber + " = '" + srNumber + "'";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToLast();
            String SR_ID = cursor.getString(0);
            cursor.close();
            return Integer.parseInt(SR_ID);
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("previous_do_number", "" + e);
        }
        return 0;
    }

    public void updateTransactionPurchaseReturnList(ArrayList<TransactionPurchaseReturn> transactionPurchaseReturnArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_T_PURCHASE_RETURN, null, null);
            for (TransactionPurchaseReturn transactionPurchaseReturn : transactionPurchaseReturnArrayList) {
                addTransactionPurchaseReturn(transactionPurchaseReturn, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_t dorder", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void addTransactionPurchaseReturnList(ArrayList<TransactionPurchaseReturn> transactionPurchaseReturnArrayList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (TransactionPurchaseReturn transactionPurchaseReturn : transactionPurchaseReturnArrayList) {
                addTransactionPurchaseReturn(transactionPurchaseReturn, db);
            }
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
    }

    public int insertMasterPurchaseReturn(MasterPurchaseReturn masterPurchaseReturn) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            addMasterPurchaseReturn(masterPurchaseReturn, db);
            int masterSRID = getRecentMasterPurchaseReturnID(masterPurchaseReturn.getPurchaseReturnNumber(), db);
            db.close();
            return masterSRID;
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_do number", "" + e);
        }
        return 0;
    }

    public MasterPurchaseReturn getMasterPurchaseReturnRecord(int PR_ID) {
        MasterPurchaseReturn masterPurchaseReturn = new MasterPurchaseReturn();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_RETURN + " WHERE " + M_PURCHASERETURN_PurchaseReturnID + " = " + PR_ID + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                masterPurchaseReturn.setPurchaseReturnID(Long.parseLong(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnID)));
                masterPurchaseReturn.setPurchaseReturnNumber(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnNumber));
                masterPurchaseReturn.setInvoiceNumber(cursor.getString(INDEX_M_PURCHASERETURN_InvoiceNumber));
                masterPurchaseReturn.setPurchaseReturnDate(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseReturnDate));
                masterPurchaseReturn.setCreatedBy(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CreatedBy)));
                masterPurchaseReturn.setCreatedDtTm(cursor.getString(INDEX_M_PURCHASERETURN_CreatedDtTm));
                masterPurchaseReturn.setOutletID(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_OutletID)));
                masterPurchaseReturn.setCGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CGST)));
                masterPurchaseReturn.setSGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_SGST)));
                masterPurchaseReturn.setIGST(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_IGST)));
                masterPurchaseReturn.setCESS(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_CESS)));
                masterPurchaseReturn.setSubTotal(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_SubTotal)));
                masterPurchaseReturn.setDebitAmount(Double.parseDouble(cursor.getString(INDEX_M_PURCHASERETURN_DebitAmount)));
                masterPurchaseReturn.setSupplierID(cursor.getString(INDEX_M_PURCHASERETURN_SupplierID));
                masterPurchaseReturn.setSupplierName(cursor.getString(INDEX_M_PURCHASERETURN_SupplierName));
                masterPurchaseReturn.setSupplierEmail(cursor.getString(INDEX_M_PURCHASERETURN_SupplierEmail));
                masterPurchaseReturn.setSupplierMobile(cursor.getString(INDEX_M_PURCHASERETURN_SupplierMobile));
                masterPurchaseReturn.setNotes(cursor.getString(INDEX_M_PURCHASERETURN_Notes));
                masterPurchaseReturn.setPurchaseID(Long.parseLong(cursor.getString(INDEX_M_PURCHASERETURN_PurchaseID)));
                masterPurchaseReturn.setSynced(Integer.parseInt(cursor.getString(INDEX_M_PURCHASERETURN_Synced)));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return masterPurchaseReturn;
    }

    public void updateMasterPurchaseReturnList(ArrayList<MasterPurchaseReturn> masterPurchaseReturnArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_M_PURCHASE_RETURN, M_PURCHASERETURN_Synced + " = " + M_PURCHASERETURN_SYNCED_CODE_Synced, null);
            for (MasterPurchaseReturn masterPurchaseReturn : masterPurchaseReturnArrayList) {
                addMasterPurchaseReturn(masterPurchaseReturn, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_masterpurchase", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public boolean isMasterPurchaseReturnPresent(String invoiceNumber) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_M_PURCHASE_RETURN
                    + " WHERE " + M_PURCHASERETURN_InvoiceNumber + " = '" + invoiceNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                cursor.close();
                db.close();
                return true;
            } else {
                cursor.close();
                db.close();
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_master_dorder", "" + e);
        }
        return false;
    }


    // Group Product Item
    public void addGroupProductItem(GroupProductItem groupProductItem, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(GROUPPRODUCTITEM_ID, groupProductItem.getID());
            values.put(GROUPPRODUCTITEM_ProductID, groupProductItem.getProductID());
            values.put(GROUPPRODUCTITEM_ItemID, groupProductItem.getItemID());
            values.put(GROUPPRODUCTITEM_ItemName, groupProductItem.getItemName());
            values.put(GROUPPRODUCTITEM_ItemCode, groupProductItem.getItemCode());
            values.put(GROUPPRODUCTITEM_Quantity, groupProductItem.getQuantity());
            values.put(GROUPPRODUCTITEM_UnitID, groupProductItem.getUnitID());
            values.put(GROUPPRODUCTITEM_Price, groupProductItem.getPrice());
            values.put(GROUPPRODUCTITEM_Weightage, groupProductItem.getWeightage());
            values.put(GROUPPRODUCTITEM_isActive, groupProductItem.getIsActive());
            values.put(GROUPPRODUCTITEM_Tax, groupProductItem.getTax());
            values.put(GROUPPRODUCTITEM_Synced, groupProductItem.getSynced());

            // insert into Group Product Item table
            long num = db.insert(TABLE_GROUP_PRODUCT_ITEM, null, values);
            if(num != -1) {
                Log.d("database", "new Group Product Item inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(groupProductItem, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(groupProductItem, e, origin);
            Log.e("insert GroupProductItem", "" + e);
        }
    }

    public ArrayList<GroupProductItem> getGroupProductItemListFromDB() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_GROUP_PRODUCT_ITEM + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<GroupProductItem> groupProductItemArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    GroupProductItem groupProductItem = new GroupProductItem();
                    groupProductItem.setID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTITEM_ID)));
                    groupProductItem.setProductID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTITEM_ProductID)));
                    groupProductItem.setItemID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTITEM_ItemID)));
                    groupProductItem.setItemName(cursor.getString(INDEX_GROUPPRODUCTITEM_ItemName));
                    groupProductItem.setItemCode(cursor.getString(INDEX_GROUPPRODUCTITEM_ItemCode));
                    groupProductItem.setQuantity(Integer.parseInt(cursor.getString(INDEX_GROUPPRODUCTITEM_Quantity)));
                    groupProductItem.setUnitID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTITEM_UnitID)));
                    groupProductItem.setPrice(MyFunctions.parseDouble(cursor.getString(INDEX_GROUPPRODUCTITEM_Price)));
                    groupProductItem.setWeightage(MyFunctions.parseDouble(cursor.getString(INDEX_GROUPPRODUCTITEM_Weightage)));
                    groupProductItem.setIsActive(Integer.parseInt(cursor.getString(INDEX_GROUPPRODUCTITEM_isActive)));
                    groupProductItem.setTax(MyFunctions.parseDouble(cursor.getString(INDEX_GROUPPRODUCTITEM_Tax)));
                    groupProductItem.setSynced(Integer.parseInt(cursor.getString(INDEX_GROUPPRODUCTITEM_Synced)));

                    groupProductItemArrayList.add(groupProductItem);
                } while (cursor.moveToNext());
                return groupProductItemArrayList;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_groupproductitem", "" + e);
        }
        return new ArrayList<>();
    }

    public ArrayList<GroupProductItem> getGroupProductItemList(long GroupProductId) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_GROUP_PRODUCT_ITEM
                    + " WHERE " + GROUPPRODUCTITEM_ProductID + " = " + GroupProductId + ";";
            Cursor cursor = db.rawQuery(selectQuery, null);

            ArrayList<GroupProductItem> groupProductItemArrayList = new ArrayList<>();
            if(cursor.moveToFirst()) {
                do {
                    GroupProductItem groupProductItem = new GroupProductItem();
                    groupProductItem.setID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTITEM_ID)));
                    groupProductItem.setProductID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTITEM_ProductID)));
                    groupProductItem.setItemID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTITEM_ItemID)));
                    groupProductItem.setItemName(cursor.getString(INDEX_GROUPPRODUCTITEM_ItemName));
                    groupProductItem.setItemCode(cursor.getString(INDEX_GROUPPRODUCTITEM_ItemCode));
                    groupProductItem.setQuantity(Integer.parseInt(cursor.getString(INDEX_GROUPPRODUCTITEM_Quantity)));
                    groupProductItem.setUnitID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTITEM_UnitID)));
                    groupProductItem.setPrice(MyFunctions.parseDouble(cursor.getString(INDEX_GROUPPRODUCTITEM_Price)));
                    groupProductItem.setWeightage(MyFunctions.parseDouble(cursor.getString(INDEX_GROUPPRODUCTITEM_Weightage)));
                    groupProductItem.setIsActive(Integer.parseInt(cursor.getString(INDEX_GROUPPRODUCTITEM_isActive)));
                    groupProductItem.setTax(MyFunctions.parseDouble(cursor.getString(INDEX_GROUPPRODUCTITEM_Tax)));
                    groupProductItem.setSynced(Integer.parseInt(cursor.getString(INDEX_GROUPPRODUCTITEM_Synced)));

                    groupProductItemArrayList.add(groupProductItem);
                } while (cursor.moveToNext());
                return groupProductItemArrayList;
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_groupproductitem", "" + e);
        }
        return new ArrayList<>();
    }

    public void updateGroupProductItemList(ArrayList<GroupProductItem> groupProductItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_GROUP_PRODUCT_ITEM, null, null);
            for (GroupProductItem groupProductItem : groupProductItems) {
                addGroupProductItem(groupProductItem, db);
                markProductAsGroupProduct(groupProductItem.getProductID());
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_groupproduct", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void markProductAsGroupProduct(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_IsGroupProduct, PRODUCT_CODE_IsAGroupProduct);
        int recordsUpdated = db.update(TABLE_PRODUCTS, values,
                PRODUCT_ProductID + "=" + id ,null);
        Log.i("records changed", recordsUpdated+"");
    }


    // group product details in invoice
    public void addGroupProductDetailInInvoice(GroupProductDetailInInvoice groupProductDetail, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
       //     values.put(GROUPPRODUCTDETAILININVOICE_ID, groupProductDetail.getID());
            values.put(GROUPPRODUCTDETAILININVOICE_SalesID, groupProductDetail.getSalesID());
            values.put(GROUPPRODUCTDETAILININVOICE_InvoiceNumber, groupProductDetail.getInvoiceNumber());
            values.put(GROUPPRODUCTDETAILININVOICE_GroupProductID, groupProductDetail.getGroupProductID());
            values.put(GROUPPRODUCTDETAILININVOICE_GroupProductQuantity, groupProductDetail.getGroupProductQuantity());
            values.put(GROUPPRODUCTDETAILININVOICE_Synced, groupProductDetail.getSynced());

            // insert into Group Product Item table
            long num = db.insert(TABLE_GROUP_PRODUCT_DETAIL_IN_INVOICE, null, values);
            if(num != -1) {
                Log.d("database", "new Group Product detail inserted");
            } else {
                String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                String origin = methodName + " - " + className;
                MyFunctions.errorOccuredRecordNotInsertedInDatabase(groupProductDetail, origin);
            }
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredInsertCatchInDatabase(groupProductDetail, e, origin);
            Log.e("insert GrpProductDetail", "" + e);
        }
    }

    public void updateGroupProductDetailsList(ArrayList<GroupProductDetailInInvoice> groupProductDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(TABLE_GROUP_PRODUCT_DETAIL_IN_INVOICE, null, null);
            for (GroupProductDetailInInvoice groupProductDetail : groupProductDetails) {
                addGroupProductDetailInInvoice(groupProductDetail, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_groupproduct", "" + e);
        } finally {
            db.endTransaction();
        }
    }

    public void addGroupProductDetailsList(ArrayList<GroupProductDetailInInvoice> groupProductDetails) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (GroupProductDetailInInvoice groupProductDetail : groupProductDetails) {
                addGroupProductDetailInInvoice(groupProductDetail, db);
            }
            db.close();
        } catch (Exception e) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("update_products", "" + e);
        }
        Log.d("check", groupProductDetails+"");
    }

    public ArrayList<GroupProductDetailInInvoice> getGroupProductDetails(String invoiceNumber) {
        ArrayList<GroupProductDetailInInvoice> groupProductDetails = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_GROUP_PRODUCT_DETAIL_IN_INVOICE
                    + " WHERE " + GROUPPRODUCTDETAILININVOICE_InvoiceNumber + " = '" + invoiceNumber + "';";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    GroupProductDetailInInvoice groupProductDetail = new GroupProductDetailInInvoice();
                    groupProductDetail.setID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTDETAILININVOICE_ID)));
                    groupProductDetail.setSalesID(Double.parseDouble(cursor.getString(INDEX_GROUPPRODUCTDETAILININVOICE_SalesID)));
                    groupProductDetail.setInvoiceNumber(cursor.getString(INDEX_GROUPPRODUCTDETAILININVOICE_InvoiceNumber));
                    groupProductDetail.setGroupProductID(Long.parseLong(cursor.getString(INDEX_GROUPPRODUCTDETAILININVOICE_GroupProductID)));
                    groupProductDetail.setGroupProductQuantity(Integer.parseInt(cursor.getString(INDEX_GROUPPRODUCTDETAILININVOICE_GroupProductQuantity)));
                    groupProductDetail.setSynced(Integer.parseInt(cursor.getString(INDEX_GROUPPRODUCTDETAILININVOICE_Synced)));

                    groupProductDetails.add(groupProductDetail);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return groupProductDetails;
        } catch (Exception e) {
            // TODO: handle exception
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String origin = methodName + " - " + className;
            MyFunctions.errorOccuredCatchInDatabase(e, origin);
            Log.e("all_groupProductDetail", "" + e);
        }
        return groupProductDetails;
    }


}
