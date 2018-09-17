package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoin.usbsdk.PrintPic;
import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Adapters.ProductListInPurchaseReturnAdapter;
import com.virtuzo.abhishek.Views.Adapters.ProductsSelectedInPurchaseReturnAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.CreditDebitNote;
import com.virtuzo.abhishek.modal.GSTCategory;
import com.virtuzo.abhishek.modal.MasterPurchase;
import com.virtuzo.abhishek.modal.MasterPurchaseReturn;
import com.virtuzo.abhishek.modal.MasterSales;
import com.virtuzo.abhishek.modal.MasterSalesReturn;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.PurchaseReturnProduct;
import com.virtuzo.abhishek.modal.SalesReturnProduct;
import com.virtuzo.abhishek.modal.StockLedger;
import com.virtuzo.abhishek.modal.Supplier;
import com.virtuzo.abhishek.modal.TransactionPurchase;
import com.virtuzo.abhishek.modal.TransactionPurchaseReturn;
import com.virtuzo.abhishek.modal.TransactionSalesReturn;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class NewPurchaseReturnActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView productListRecyclerView, productsSelectedRecyclerView;
    final int NUMBER_OF_COLUMNS_IN_PRODUCT_LIST = 2, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED = 1;
    private ProductListInPurchaseReturnAdapter productListAdapter;
    private ProductsSelectedInPurchaseReturnAdapter productsSelectedAdapter;
    MasterPurchase masterPurchase;
    private ArrayList<TransactionPurchase> transactionPurchaseList;
    private ArrayList<PurchaseReturnProduct> productsSelected;
    TextView supplierInPurchaseReturn, invoiceDate;
    ImageButton backButton;
    String LoginID, BusinessId, OutletID, TermsAndConditions, OutletAddress, OutletLogo, GSTCategoryID,
            OutletGSTNumber, InvoiceSeries, TabCode, StateID, OutletName, FirstName, ContactNumber, StateName;
    private final int CHOOSE_SUPPLIER_RESPONSE_CODE = 101, INVOICE_PAYMENT_CODE = 102, DATE_PICKER_DIALOG_ID = 201;
    Supplier supplierSelected;
    Button invoiceDatePickerButton;
    int invoiceDay, invoiceMonth, invoiceYear;
    boolean validInvoiceDateSelected;
    double transportCharges = 0, insuranceCharges = 0, packingCharges = 0, overallDiscount = 0;
    SearchView searchViewForProductList;
    TextView subTotalTextView, taxTextView, totalAmountTextView;
    String invoiceDateString;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
    String purchaseReturnInvoiceNumber;
    String saleInvoiceNumber;
    TextView invoiceNumberTextView, productSelectedTitleTextView;
    String termsAndConditionsString = "";
    String noteString = "";
    String billAddressString = "";
    String shipAddressString = "";
    String purchaseID;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(NewPurchaseReturnActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_purchase_return);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        LoginID = setting.getString("UserID", "");
        OutletID = setting.getString("OutletID", "");
        StateName = setting.getString("StateName", "");
        TermsAndConditions = setting.getString("TermsAndConditions", "");
        OutletLogo = setting.getString("Logo", "");
        InvoiceSeries = setting.getString("InvoiceSeries", "");
        TabCode = setting.getString("TabCode", "");
        StateID = setting.getString("StateID", "");
        GSTCategoryID = setting.getString("GSTCategoryID", "");
        FirstName = setting.getString("FirstName", "");
        OutletName = setting.getString("OutletName", "");
        OutletAddress = setting.getString("Address", "");
        ContactNumber = setting.getString("ContactNumber", "");
        OutletGSTNumber = setting.getString("GSTNumber","");
        termsAndConditionsString = TermsAndConditions;
        
        // GET INVOICE DATA
        purchaseID= String.valueOf(getIntent().getDoubleExtra("purchaseID",0));

        transactionPurchaseList = new ArrayList<>();
        masterPurchase= DatabaseHandler.getInstance().getMasterPurchaseRecord(purchaseID);

        transactionPurchaseList=DatabaseHandler.getInstance().getPurchaseListDetailsFromDB(purchaseID);

//        setTitle("New Invoice");
        getSupportActionBar().hide();
        Toolbar toolbar_in_new_invoice = (Toolbar) findViewById(R.id.toolbar_in_purchase_return);
        toolbar_in_new_invoice.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_new_invoice.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_new_invoice.findViewById(R.id.actionBarTitle);
        heading.setText("Purchase Return");
        toolbar_in_new_invoice.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_bright)));

        invoiceNumberTextView = (TextView) toolbar_in_new_invoice.findViewById(R.id.invoiceNumberTextView);
        invoiceNumberTextView.setText(purchaseReturnInvoiceNumber);

        SharedPreferences data = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        BusinessId = data.getString("BusinessId", "");

        subTotalTextView = (TextView) findViewById(R.id.subTotalTextView);
        taxTextView = (TextView) findViewById(R.id.totalTaxTextView);
        totalAmountTextView = (TextView) findViewById(R.id.totalAmountTextView);
        String rupeeSymbol = "\u20B9";
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        taxTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f", 0d));

        // products list
        productListRecyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_PRODUCT_LIST);
        productListRecyclerView.setLayoutManager(mLayoutManager);
        productListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productListAdapter = new ProductListInPurchaseReturnAdapter(transactionPurchaseList, new ProductListInPurchaseReturnAdapter.OnClickListener() {
            @Override
            public void onItemClick(TransactionPurchase product) {
                selectProduct(product);
            }
        });
        productListRecyclerView.setAdapter(productListAdapter);
        Log.i("New Invoice", "Before database access");
        Log.i("New Invoice", "After database access");
        productListAdapter.notifyDataSetChanged();

        // search bar for product list
        searchViewForProductList = (SearchView) findViewById(R.id.searchProductListSearchView);
        searchViewForProductList.setOnQueryTextListener(this);
        searchViewForProductList.setQueryHint("Search Product");
        setSearchViewHeight(searchViewForProductList, 25);
        searchViewForProductList.setIconified(false);

        // products selected
        productSelectedTitleTextView = (TextView) findViewById(R.id.productSelectedTitleTextView);
        productsSelected = new ArrayList<>();
        productsSelectedRecyclerView = (RecyclerView) findViewById(R.id.productsSelectedRecyclerView);
        RecyclerView.LayoutManager nLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED);
        productsSelectedRecyclerView.setLayoutManager(nLayoutManager);
        productsSelectedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productsSelectedAdapter = new ProductsSelectedInPurchaseReturnAdapter(productsSelected, new ProductsSelectedInPurchaseReturnAdapter.OnClickListener() {
            @Override
            public void onItemClick(final PurchaseReturnProduct purchaseReturnProduct) {
                updateProductDetails(purchaseReturnProduct);
            }

            @Override
            public void onCancelItem(int position) {
                productsSelected.remove(position);
                productsSelectedAdapter.notifyDataSetChanged();
                setProductSelectedTitle(productsSelected.size());
                refreshSubTotalAndTax();
            }
        });
        productsSelectedRecyclerView.setAdapter(productsSelectedAdapter);

        // choose customer
        supplierSelected = DatabaseHandler.getInstance().getSupplierRecordFromPurchase(masterPurchase);
        if (supplierSelected == null) {
            supplierSelected.setSupplierId(masterPurchase.getSupplierID());
            supplierSelected.setEntityName(masterPurchase.getSupplierName());
            supplierSelected.setContactPerson(masterPurchase.getSupplierName());
            supplierSelected.setContactEmailId(masterPurchase.getSupplierEmail());
            supplierSelected.setContactNumber(masterPurchase.getSupplierMobile());
        }
        supplierInPurchaseReturn = (TextView) findViewById(R.id.supplierInPurchaseReturn);
        String showCustomerEntityName = supplierSelected.getEntityName();
        if(supplierSelected.getEntityType().equalsIgnoreCase("Individual")) {
            showCustomerEntityName = supplierSelected.getContactPerson();
        }
        supplierInPurchaseReturn.setText(showCustomerEntityName
                + " (" + supplierSelected.getContactNumber() + ")");

        // invoice date
        validInvoiceDateSelected = false;
        invoiceDatePickerButton = (Button) findViewById(R.id.invoiceDatePickerButton);
        Calendar calendar = Calendar.getInstance();
        invoiceYear = calendar.get(Calendar.YEAR);
        invoiceMonth = calendar.get(Calendar.MONTH) + 1;
        invoiceDay = calendar.get(Calendar.DAY_OF_MONTH);
        String invoiceMonthString = "";
        {
            String months[] = new DateFormatSymbols().getShortMonths();
            invoiceMonthString = months[invoiceMonth-1];
        }
        String invoiceDayString = "00" + invoiceDay;
        invoiceDayString = invoiceDayString.substring(invoiceDayString.length() - 2);
        invoiceDateString = invoiceDayString + " " + invoiceMonthString + " " + invoiceYear;
        invoiceDatePickerButton.setText(invoiceDateString);
        validInvoiceDateSelected = true;
        invoiceDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG_ID);
            }
        });
        updateInvoiceNumber();

        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();

    }

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(NewPurchaseReturnActivity.this,mHandler);
        u_infor = new int[5][2];
        u_infor[0][0] = 0x1CBE;
        u_infor[0][1] = 0x0003;
        u_infor[1][0] = 0x1CB0;
        u_infor[1][1] = 0x0003;
        u_infor[2][0] = 0x0483;
        u_infor[2][1] = 0x5740;
        u_infor[3][0] = 0x0493;
        u_infor[3][1] = 0x8760;
        u_infor[4][0] = 0x0471;
        u_infor[4][1] = 0x0055;
    }

    private boolean CheckUsbPermission(){
        if( dev != null ){
            if( usbCtrl.isHasPermission(dev)){
                return true;
            }
        }

        Toast.makeText(NewPurchaseReturnActivity.this, getString(R.string.usb_msg_conn_state),
                Toast.LENGTH_SHORT).show();
        return false;
    }

    private void connectThroughPrinter() {
        usbCtrl.close();
        int  i = 0;
        for( i = 0 ; i < 5 ; i++ ){
            dev = usbCtrl.getDev(u_infor[i][0],u_infor[i][1]);
            if(dev != null)
                break;
        }
        if( dev != null ){
            if( !(usbCtrl.isHasPermission(dev))){
                usbCtrl.getPermission(dev);
            }else{
                Toast.makeText(NewPurchaseReturnActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
//                printThroughPrinter(receiptString);
            }
        }
    }

    private void printThroughPrinter(String receiptString1, String receiptString2) {
        byte isHasPaper;
        byte[] cmd = null;
        if( dev != null ){
            if( usbCtrl.isHasPermission(dev)){
                Toast.makeText(NewPurchaseReturnActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(NewPurchaseReturnActivity.this, "The printer has no paper",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String txt_msg = receiptString1;
                String txt_msg2 = receiptString2;
                if( CheckUsbPermission() == true ){

//                    if(null!=MyFunctions.getImage()) {
//
//                        String imagePath = Environment.getExternalStoragePublicDirectory
//                                (
//                                        // Environment.DIRECTORY_PICTURES
//                                        Environment.DIRECTORY_DOCUMENTS + "/GSTMadeEasy/"
//                                ) + "/logo.png";
//                        printImage(imagePath);
//                    }
                    usbCtrl.sendMsg(txt_msg, "GBK", dev);
                    usbCtrl.cutPaper(dev, 100);
                    usbCtrl.sendMsg(txt_msg2, "GBK", dev);
                    usbCtrl.cutPaper(dev, 100);
//                    if(openCashDrawer) {
//                        usbCtrl.openCashBox(dev);
//                    }
                }
            }
        }
        usbCtrl.close();
    }

    private void printImage(String imagePath) {
        int i = 0,s = 0,j = 0,index = 0;
        byte[] temp = new byte[56];
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(100, 0, imagePath);
        sendData = pg.printDraw();

        for( i = 0 ; i < pg.getLength() ; i++ ){  //Ã¿¸ôÒ»ÐÐ¼ÓÉÏ°üÍ·£¬·¢ËÍÒ»´ÎÊý¾Ý
            s = 0;
            temp[s++] = 0x1D;
            temp[s++] = 0x76;
            temp[s++] = 0x30;
            temp[s++] = 0x00;
            temp[s++] = (byte)(pg.getWidth() / 8);
            temp[s++] = 0x00;
            temp[s++] = 0x01;
            temp[s++] = 0x00;
            for( j = 0 ; j < (pg.getWidth() / 8) ; j++ )
                temp[s++] = sendData[index++];
            usbCtrl.sendByte(temp, dev);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DATE_PICKER_DIALOG_ID) {
            DatePickerDialog dpDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    invoiceYear = year;
                    invoiceMonth = monthOfYear + 1;
                    invoiceDay = dayOfMonth;
                    String invoiceMonthString = "";
                    {
                        String months[] = new DateFormatSymbols().getShortMonths();
                        invoiceMonthString = months[invoiceMonth-1];
                    }
                    String invoiceDayString = "00" + invoiceDay;
                    invoiceDayString = invoiceDayString.substring(invoiceDayString.length() - 2);
                    String dateString = invoiceDayString + " " + invoiceMonthString + " " + invoiceYear;
                    Toast.makeText(NewPurchaseReturnActivity.this, dateString, Toast.LENGTH_SHORT).show();
                    invoiceDatePickerButton.setText(dateString);
                    invoiceDateString = dateString;
                    validInvoiceDateSelected = true;
                    updateInvoiceNumber();
                }
            }, invoiceYear, invoiceMonth, invoiceDay);
            DatePicker datePicker = dpDialog.getDatePicker();
            datePicker.setCalendarViewShown(false);
            Calendar cal = Calendar.getInstance();
            datePicker.setMaxDate(cal.getTimeInMillis());
            cal.set(invoiceYear, invoiceMonth - 1, 1);
            if(invoiceDay < 6) {
                cal.add(Calendar.MONTH, -1);
            }
            datePicker.setMinDate(cal.getTimeInMillis());
            return dpDialog;
        }
        return null;
    }

    public void updateInvoiceNumber() {
        purchaseReturnInvoiceNumber = createInvoiceNumber();
        invoiceNumberTextView.setText(purchaseReturnInvoiceNumber);
    }

    private String createInvoiceNumber() {
        StringBuilder builder = new StringBuilder();
        builder.append("PR");
//        builder.append(this.InvoiceSeries);
        builder.append(this.TabCode);
        builder.append("/");
        builder.append(this.dateFormatInInvoiceNumber());
        builder.append("/");
        String previousSalesReturnNumber = DatabaseHandler.getInstance().getPreviousSalesReturnNumber(builder.toString());
        if(previousSalesReturnNumber == null) {
            builder.append("001");
        } else {
            String seriesNumberString = previousSalesReturnNumber.substring(previousSalesReturnNumber.length() - 3);
            int seriesNumber = Integer.parseInt(seriesNumberString);
            seriesNumber++;
            seriesNumberString = "000" + seriesNumber;
            seriesNumberString = seriesNumberString.substring(seriesNumberString.length() - 3);
            builder.append("" + seriesNumberString);
        }
        return builder.toString();
    }

    @NonNull
    private String dateFormatInInvoiceNumber() {
        StringBuilder builder = new StringBuilder();
        int invoiceYearTwoDigit = invoiceYear % 100;
        builder.append("" + invoiceYearTwoDigit);

        String invoiceMonthString = "00" + invoiceMonth;
        invoiceMonthString = invoiceMonthString.substring(invoiceMonthString.length() - 2);
        builder.append(invoiceMonthString);

        String invoiceDayString = "00" + invoiceDay;
        invoiceDayString = invoiceDayString.substring(invoiceDayString.length() - 2);
        builder.append(invoiceDayString);

        return builder.toString();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    private void setSearchViewHeight(SearchView searchView, int height) {
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(height);
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_SUPPLIER_RESPONSE_CODE) {
            if(resultCode == Activity.RESULT_OK) {
            }
        } else if(requestCode == INVOICE_PAYMENT_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                double masterSalesID = data.getDoubleExtra("masterSalesID", 0.0);
                extractFromDatabaseAndPrint(purchaseReturnInvoiceNumber, masterSalesID);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = MyFunctions.lowerCase(newText.trim());
        if(MyFunctions.StringLength(newText) == 0) {
            productListAdapter.setFilter(transactionPurchaseList);
            return true;
        }
        ArrayList<TransactionPurchase> newList = new ArrayList<>();
        for(TransactionPurchase product : transactionPurchaseList) {
            String name = MyFunctions.lowerCase(product.getProductName());
            if(name.contains(newText)){
                newList.add(product);
            }
        }
        productListAdapter.setFilter(newList);
        return true;
    }

    public void selectProduct(TransactionPurchase product) {
        // when a product is clicked
        boolean newProductSelected = true;
//        for (int i = 0; i < productsSelected.size(); i++) {
        for(PurchaseReturnProduct purchaseReturnProduct : productsSelected){
            if(purchaseReturnProduct.getTransactionPurchase().getProductID() == product.getProductID()
                    && purchaseReturnProduct.getTransactionPurchase().getProductName().equalsIgnoreCase(product.getProductName())
                    && purchaseReturnProduct.getTransactionPurchase().getProductCode().equalsIgnoreCase(product.getProductCode())) {
                newProductSelected = false;
                updateProductDetails(purchaseReturnProduct);
                break;
            }
        }
        if(newProductSelected) {
            // New product added
            PurchaseReturnProduct purchaseReturnProduct = new PurchaseReturnProduct(product);
            addNewProductDetails(purchaseReturnProduct);
        }
    }

    public void addNewProductDetails(final PurchaseReturnProduct purchaseReturnProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.product_selected_details_purchase_return_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final TextView titleTextView = (TextView) view.findViewById(R.id.productNameTitle);
        final TextView originalQuantitySold = (TextView) view.findViewById(R.id.originalQuantitySold);
        final EditText returnQuantityEditText = (EditText) view.findViewById(R.id.returnQuantityEditText);
        final TextView originalUnitPrice = (TextView) view.findViewById(R.id.originalUnitPrice);
//        final EditText returnUnitPriceEditText = (EditText) view.findViewById(R.id.returnUnitPriceEditText);
        final TextView originalTaxAmount = (TextView) view.findViewById(R.id.originalTaxAmount);

        titleTextView.setText(purchaseReturnProduct.getTransactionPurchase().getProductName());
        originalQuantitySold.setText(purchaseReturnProduct.getTransactionPurchase().getQuantity()+"");
        returnQuantityEditText.setText(purchaseReturnProduct.getTransactionPurchase().getQuantity()+"");
        originalUnitPrice.setText(String.format("%.2f", purchaseReturnProduct.getTransactionPurchase().getUnitPrice()));
//        returnUnitPriceEditText.setText(String.format("%.2f", purchaseReturnProduct.getReturnPrice()));
        originalTaxAmount.setText(String.format("%.2f", purchaseReturnProduct.getOriginalTotalTax()));

        returnQuantityEditText.selectAll();

        // default values
        final Button okButton = (Button) view.findViewById(R.id.okButton);
        final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String returnQuantityString = returnQuantityEditText.getText().toString();
                String returnUnitPriceString = originalUnitPrice.getText().toString();
                if(returnQuantityString.length() == 0 || returnQuantityString.length() > 4) {
                    showOkDialog("Enter valid quantity");
                } else {
                    double returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
                    int returnQuantity = Integer.parseInt(returnQuantityString);
                    if(returnQuantity == 0 || returnQuantity > purchaseReturnProduct.getTransactionPurchase().getQuantity()) {
                        showOkDialog("Enter valid quantity");
                    } else {
                        purchaseReturnProduct.setReturnPrice(returnUnitPrice);
                        purchaseReturnProduct.setReturnQuantity(returnQuantity);

                        double totalAmount = (purchaseReturnProduct.getReturnPrice() * purchaseReturnProduct.getReturnQuantity())
                                                + purchaseReturnProduct.getReturnTotalTax();
                        purchaseReturnProduct.setReturnAmount(totalAmount);
                        productsSelected.add(purchaseReturnProduct);
                        productsSelectedAdapter.notifyDataSetChanged();
                        setProductSelectedTitle(productsSelected.size());
                        refreshSubTotalAndTax();
                        dialog.dismiss();

//                        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
//                        final View view = getLayoutInflater().inflate(R.layout.invoice_payment_confirm_dialog, null);
//                        builder.setView(view);
//                        final AlertDialog innerdialog = builder.create();
//                        innerdialog.setCanceledOnTouchOutside(false);
//                        innerdialog.show();
//                        Button yesButton = (Button) view.findViewById(R.id.yesClick);
//                        Button noButton = (Button) view.findViewById(R.id.noClick);
//                        TextView paymentAmountTextView = (TextView) view.findViewById(R.id.paymentAmount);
//                        String rupeeSymbol = "\u20B9";
//                        paymentAmountTextView.setText(rupeeSymbol + " " + String.format("%.2f", creditAmount));
//                        yesButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                double returnTaxRate = (returnTaxAmount*100) / (returnUnitPrice * returnQuantity);
//                                innerdialog.dismiss();
//                            }
//                        });
//                        noButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                innerdialog.dismiss();
//                            }
//                        });
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void updateProductDetails(final PurchaseReturnProduct purchaseReturnProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.product_selected_details_purchase_return_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final TextView titleTextView = (TextView) view.findViewById(R.id.productNameTitle);
        final TextView originalQuantitySold = (TextView) view.findViewById(R.id.originalQuantitySold);
        final EditText returnQuantityEditText = (EditText) view.findViewById(R.id.returnQuantityEditText);
        final TextView originalUnitPrice = (TextView) view.findViewById(R.id.originalUnitPrice);
//        final EditText returnUnitPriceEditText = (EditText) view.findViewById(R.id.returnUnitPriceEditText);
        final TextView originalTaxAmount = (TextView) view.findViewById(R.id.originalTaxAmount);

        titleTextView.setText(purchaseReturnProduct.getTransactionPurchase().getProductName());
        originalQuantitySold.setText(purchaseReturnProduct.getTransactionPurchase().getQuantity()+"");
        returnQuantityEditText.setText(purchaseReturnProduct.getReturnQuantity()+"");
        originalUnitPrice.setText(String.format("%.2f", purchaseReturnProduct.getReturnPrice()));
//        returnUnitPriceEditText.setText(String.format("%.2f", purchaseReturnProduct.getReturnPrice()));
        originalTaxAmount.setText(String.format("%.2f", purchaseReturnProduct.getOriginalTotalTax()));

        returnQuantityEditText.selectAll();

        // default values

        final Button okButton = (Button) view.findViewById(R.id.okButton);
        final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String returnQuantityString = returnQuantityEditText.getText().toString();
                String returnUnitPriceString = originalUnitPrice.getText().toString();
                if(returnQuantityString.length() == 0 || returnQuantityString.length() > 4) {
                    showOkDialog("Enter valid quantity");
                } else {
                    double returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
                    int returnQuantity = Integer.parseInt(returnQuantityString);
                    if(returnQuantity == 0 || returnQuantity > purchaseReturnProduct.getTransactionPurchase().getQuantity()) {
                        showOkDialog("Enter valid quantity");
                    } else {
                        purchaseReturnProduct.setReturnPrice(returnUnitPrice);
                        purchaseReturnProduct.setReturnQuantity(returnQuantity);

                        double totalAmount = (purchaseReturnProduct.getReturnPrice() * purchaseReturnProduct.getReturnQuantity())
                                + purchaseReturnProduct.getReturnTotalTax();
                        purchaseReturnProduct.setReturnAmount(totalAmount);
                        productsSelectedAdapter.notifyDataSetChanged();
                        setProductSelectedTitle(productsSelected.size());
                        refreshSubTotalAndTax();
                        dialog.dismiss();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void setProductSelectedTitle(int size) {
        productSelectedTitleTextView.setText("Products Selected (" + size + ")");
    }

    public boolean isTaxFree() {
        int OutletGSTCategoryID = Integer.parseInt(this.GSTCategoryID);
        switch (OutletGSTCategoryID) {
            case GSTCategory.GST_REGISTERED:
                switch (supplierSelected.getCategoryId()) {
                    case GSTCategory.SEZ:
                    case GSTCategory.IMPORTER:
                        return true;
                    default:
                        return false;
                }
            default:
                return true;
        }
    }

    public double subTotalWithoutDiscount() {
        double subTotal = 0;
        for(PurchaseReturnProduct product : productsSelected) {
            double salesPrice = product.getReturnPrice() * product.getReturnQuantity();
            subTotal += salesPrice;
        }
        return subTotal;
    }

    public double getFreightCharges() {
        return (transportCharges + insuranceCharges + packingCharges);
    }

    public void setOtherCharges(double transportCharges, double insuranceCharges, double packingCharges) {
        this.transportCharges = transportCharges;
        this.insuranceCharges = insuranceCharges;
        this.packingCharges = packingCharges;
    }

    public void setOverallDiscount(double overallDiscount) {
        this.overallDiscount = overallDiscount;
    }

    public double getOverallDiscount() {
        return this.overallDiscount;
    }

    public double subTotalWithDiscount() {
        return (subTotalWithoutDiscount() - overallDiscount);
    }

    public double getTotalAmountWithoutOverallDiscount() {
        return subTotalWithoutDiscount() + getFreightCharges() + taxCalculation();
    }

    public double overallDiscountInPercentage() {
        if(subTotalWithoutDiscount() != 0) {
            return (overallDiscount * 100d) / getTotalAmountWithoutOverallDiscount();
        }
        return 0;
    }

    public double taxCalculation() {
        double tax = 0;
        for(PurchaseReturnProduct product : productsSelected) {
            tax += product.getReturnTotalTax();
        }
        return tax;
    }

    public double getTotalCGST() {
        double cgst = 0;
        for(PurchaseReturnProduct product : productsSelected) {
            cgst += product.getReturnCGST();
        }
        return cgst;
    }

    public double getTotalSGST() {
        double sgst = 0;
        for(PurchaseReturnProduct product : productsSelected) {
            sgst += product.getReturnSGST();
        }
        return sgst;
    }

    public double getTotalIGST() {
        double igst = 0;
        for(PurchaseReturnProduct product : productsSelected) {
            igst += product.getReturnIGST();
        }
        return igst;
    }

    public double getTotalCESS() {
        double cess = 0;
        for(PurchaseReturnProduct product : productsSelected) {
            cess += product.getReturnCESS();
        }
        return cess;
    }

    public double totalAmount() {
        return subTotalWithDiscount() + taxCalculation() + getFreightCharges();
    }

    public void refreshSubTotalAndTax() {
        String rupeeSymbol = "\u20B9";
        double subTotal = subTotalWithoutDiscount();
        double freightCharnges = getFreightCharges();
        double tax = taxCalculation();
        double totalAmount = totalAmount();
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f",subTotal));
//        freightChargesTextView.setText(rupeeSymbol + String.format("%.2f",freightCharnges));
        taxTextView.setText(rupeeSymbol + String.format("%.2f",tax));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f",totalAmount));
    }

//    public void otherChargesButtonClick(View v) {
//        if(productsSelected.isEmpty()) {
//            showOkDialog("No product selected");
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
//        final View view = getLayoutInflater().inflate(R.layout.other_charges_dialog_new_invoice, null);
//        builder.setView(view);
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//        final EditText transportChargesEditText = (EditText) view.findViewById(R.id.transportChargesEditText);
//        final EditText insuranceChargesEditText = (EditText) view.findViewById(R.id.insuranceChargesEditText);
//        final EditText packingChargesEditText = (EditText) view.findViewById(R.id.packingChargesEditText);
//
//        if(transportCharges != 0.0)
//            transportChargesEditText.setText(String.format("%.2f", transportCharges));
//        if(insuranceCharges != 0.0)
//            insuranceChargesEditText.setText(String.format("%.2f", insuranceCharges));
//        if(packingCharges != 0.0)
//            packingChargesEditText.setText(String.format("%.2f", packingCharges));
//
//        final Button okButton = (Button) view.findViewById(R.id.okButtonOtherChargesInNewInvoice);
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String transportChargesString = transportChargesEditText.getText().toString();
//                String insuranceChargesString = insuranceChargesEditText.getText().toString();
//                String packingChargesString = packingChargesEditText.getText().toString();
//                double transportCharges, insuranceCharges, packingCharges;
//                if(transportChargesString.length() == 0) {
//                    transportCharges = 0d;
//                } else {
//                    transportCharges = MyFunctions.parseDouble(transportChargesString);
//                }
//                if(insuranceChargesString.length() == 0) {
//                    insuranceCharges = 0d;
//                } else {
//                    insuranceCharges = MyFunctions.parseDouble(insuranceChargesString);
//                }
//                if(packingChargesString.length() == 0) {
//                    packingCharges = 0d;
//                } else {
//                    packingCharges = MyFunctions.parseDouble(packingChargesString);
//                }
//
//                setOtherCharges(transportCharges, insuranceCharges, packingCharges);
//                dialog.dismiss();
//                refreshSubTotalAndTax();
//            }
//        });
//    }

    //
//    public void discountButtonClick(View v) {
//        if(productsSelected.isEmpty()) {
//            showOkDialog("No product selected");
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
//        final View view = getLayoutInflater().inflate(R.layout.discount_dialog_new_invoice, null);
//        builder.setView(view);
//        final AlertDialog dialog = builder.create();
//        dialog.show();
////        final EditText discountInRupeesEditText = (EditText) view.findViewById(R.id.discountInRupeesOtherChargesInNewInvoiceEditText);
////        final EditText discountInPercentageEditText = (EditText) view.findViewById(R.id.discountInPercentageOtherChargesInNewInvoiceEditText);
//
//        if(overallDiscount != 0.0) {
//            discountInRupeesEditText.setText(String.format("%.2f", overallDiscount));
//            discountInPercentageEditText.setText(String.format("%.2f",
//                    (overallDiscount
//                            / getTotalAmountWithoutOverallDiscount())
//                            * 100d));
//        }
//
//        final double totalAmount = getTotalAmountWithoutOverallDiscount();
//
//        discountInRupeesEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(totalAmount == 0) {
//                    if(discountInRupeesEditText.getText().toString().length() > 0)
//                        discountInRupeesEditText.setText("");
//                    return;
//                }
//                if(discountInRupeesEditText.isFocused()) {
//                    String text = discountInRupeesEditText.getText().toString();
//                    if(text.length() == 0) {
//                        discountInPercentageEditText.setText("");
//                    } else {
//                        double discountInRupees = MyFunctions.parseDouble(text);
//                        if(discountInRupees > totalAmount) {
//                            discountInRupeesEditText.setText(text.substring(0, text.length() - 1));
//                            discountInRupeesEditText.selectAll();
//                            text = discountInRupeesEditText.getText().toString();
//                            if(text.length() > 0) {
//                                discountInRupees = MyFunctions.parseDouble(text);
//                            } else {
//                                discountInRupees = 0;
//                            }
//                        }
//                        double discountInPercentage = 0;
//                        if(totalAmount > 0) {
//                            discountInPercentage = (discountInRupees * 100d) / totalAmount;
//                        }
//                        discountInPercentageEditText.setText(String.format("%.2f", discountInPercentage));
//                    }
//                }
//            }
//        });
//        discountInPercentageEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(totalAmount == 0) {
//                    if(discountInPercentageEditText.getText().toString().length() > 0)
//                        discountInPercentageEditText.setText("");
//                    return;
//                }
//                if(discountInPercentageEditText.isFocused()) {
//                    String text = discountInPercentageEditText.getText().toString();
//                    String rupeeSymbol = "\u20B9";
//                    if(text.length() == 0) {
//                        discountInRupeesEditText.setText("");
//                    } else {
//                        double discountInPercentage  = MyFunctions.parseDouble(text);
//                        if(discountInPercentage > 100) {
//                            discountInPercentageEditText.setText(text.substring(0, text.length() - 1));
//                            discountInPercentageEditText.selectAll();
//                            discountInPercentage = MyFunctions.parseDouble(discountInPercentageEditText.getText().toString());
//                        }
//                        double discountInRupees = (discountInPercentage * totalAmount) / 100d;
//                        discountInRupeesEditText.setText(String.format("%.2f", discountInRupees));
//                    }
//                }
//            }
//        });
//        final Button okButton = (Button) view.findViewById(R.id.okButtonOtherChargesInNewInvoice);
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String discountInRupeesString = discountInRupeesEditText.getText().toString();
//                double discountInRupees;
//                if(discountInRupeesString.length() == 0) {
//                    discountInRupees = 0d;
//                } else {
//                    discountInRupees = MyFunctions.parseDouble(discountInRupeesString);
//                    if(discountInRupees > totalAmount) {
//                        showOkDialog("Invalid input");
//                    }
//                }
//                setOverallDiscount(discountInRupees);
//                dialog.dismiss();
//                refreshSubTotalAndTax();
//            }
//        });
//
//
//    }

    private MasterSalesReturn createMasterSalesReturnObject() {
        MasterSalesReturn masterSalesReturn = new MasterSalesReturn();
        masterSalesReturn.setSalesReturnDate(invoiceDateString);
        masterSalesReturn.setCustomerID(supplierSelected.getSupplierId());
        masterSalesReturn.setOutletID(MyFunctions.parseInt(OutletID));
        masterSalesReturn.setCreditAmount(totalAmount());
        masterSalesReturn.setCreatedBy(MyFunctions.parseDouble(LoginID));
        masterSalesReturn.setCreatedDttm(MyFunctions.getCurrentDateTime());
        if(supplierSelected.getEntityType().equalsIgnoreCase("Individual")) {
            masterSalesReturn.setCustomerName(supplierSelected.getContactPerson());
        } else {
            masterSalesReturn.setCustomerName(supplierSelected.getEntityName());
        }
        masterSalesReturn.setCustomerEmail(supplierSelected.getContactEmailId());
        masterSalesReturn.setCustomerMobile(supplierSelected.getContactNumber());
        masterSalesReturn.setCustomerLandline(supplierSelected.getContactLandline());
        masterSalesReturn.setInvoiceNumber(saleInvoiceNumber);
        masterSalesReturn.setSalesReturnNumber(purchaseReturnInvoiceNumber);
        masterSalesReturn.setNotes(noteString);
        masterSalesReturn.setSynced(0);
        return masterSalesReturn;
    }

    private TransactionSalesReturn createTransactionSalesReturnObject(long salesReturnID, SalesReturnProduct salesReturnProduct) {
        TransactionSalesReturn transactionSalesReturn = new TransactionSalesReturn();
        transactionSalesReturn.setSalesReturnID(salesReturnID);
        transactionSalesReturn.setProductID(salesReturnProduct.getTransactionSales().getItemID());
        transactionSalesReturn.setProductCode(salesReturnProduct.getTransactionSales().getProductCode());
        transactionSalesReturn.setUnitID(0); // unit id
        transactionSalesReturn.setReturnPrice(salesReturnProduct.getReturnPrice());
        transactionSalesReturn.setReturnQty(salesReturnProduct.getReturnQuantity());
        transactionSalesReturn.setReturnTaxRate(salesReturnProduct.getReturnTaxRate());
        transactionSalesReturn.setReturnTaxAmount(salesReturnProduct.getReturnTax());
        transactionSalesReturn.setReturnAmount(salesReturnProduct.getReturnAmount());
        transactionSalesReturn.setProductName(salesReturnProduct.getTransactionSales().getItemName());
        transactionSalesReturn.setReturnType(salesReturnProduct.getReturnType());
        return transactionSalesReturn;
    }

    private MasterPurchaseReturn createMasterPurchaseReturnObject() {
        MasterPurchaseReturn masterPurchaseReturn = new MasterPurchaseReturn();
        masterPurchaseReturn.setPurchaseReturnNumber(this.purchaseReturnInvoiceNumber);
        masterPurchaseReturn.setInvoiceNumber(masterPurchase.getInvoiceNumber());
        masterPurchaseReturn.setPurchaseReturnDate(this.invoiceDateString);
        masterPurchaseReturn.setCreatedBy(MyFunctions.parseDouble(this.LoginID));
        masterPurchaseReturn.setCreatedDtTm(MyFunctions.getCurrentDateTime());
        masterPurchaseReturn.setOutletID(MyFunctions.parseDouble(this.OutletID));
        masterPurchaseReturn.setCGST(getTotalCGST());
        masterPurchaseReturn.setSGST(getTotalSGST());
        masterPurchaseReturn.setIGST(getTotalIGST());
        masterPurchaseReturn.setCESS(getTotalCESS());
        masterPurchaseReturn.setSubTotal(subTotalWithoutDiscount());
        masterPurchaseReturn.setDebitAmount(totalAmount());
        masterPurchaseReturn.setSupplierID(supplierSelected.getSupplierId());
        if (supplierSelected.getEntityType().equalsIgnoreCase("Individual")) {
            masterPurchaseReturn.setSupplierName(supplierSelected.getContactPerson());
        } else {
            masterPurchaseReturn.setSupplierName(supplierSelected.getEntityName());
        }
        masterPurchaseReturn.setSupplierEmail(supplierSelected.getContactEmailId());
        masterPurchaseReturn.setSupplierMobile(supplierSelected.getContactNumber());
        masterPurchaseReturn.setNotes(this.noteString);
        return masterPurchaseReturn;
    }

    private TransactionPurchaseReturn createTransactionPurchaseReturnObject(long PR_ID, PurchaseReturnProduct purchaseReturnProduct) {
        TransactionPurchaseReturn transactionPurchaseReturn = new TransactionPurchaseReturn();
        transactionPurchaseReturn.setPurchaseReturnID(PR_ID);
        transactionPurchaseReturn.setProductID(purchaseReturnProduct.getTransactionPurchase().getProductID());
        transactionPurchaseReturn.setProductCode(purchaseReturnProduct.getTransactionPurchase().getProductCode());
        transactionPurchaseReturn.setUnitID(purchaseReturnProduct.getTransactionPurchase().getUnitID());
        transactionPurchaseReturn.setReturnQty(purchaseReturnProduct.getReturnQuantity());
        transactionPurchaseReturn.setUnitPrice(purchaseReturnProduct.getReturnPrice());
        transactionPurchaseReturn.setReturnAmount(purchaseReturnProduct.getReturnAmount());
        transactionPurchaseReturn.setReturnTaxAmount(purchaseReturnProduct.getReturnTotalTax());
        transactionPurchaseReturn.setProductName(purchaseReturnProduct.getTransactionPurchase().getProductName());
        transactionPurchaseReturn.setCGST(purchaseReturnProduct.getReturnCGST());
        transactionPurchaseReturn.setSGST(purchaseReturnProduct.getReturnSGST());
        transactionPurchaseReturn.setIGST(purchaseReturnProduct.getReturnIGST());
        transactionPurchaseReturn.setCESS(purchaseReturnProduct.getReturnCESS());
        return transactionPurchaseReturn;
    }

    private CreditDebitNote createDebitNoteObject(MasterPurchaseReturn masterPurchaseReturn) {
        CreditDebitNote creditNote = new CreditDebitNote();
        creditNote.setNoteNumber(createNewCreditNoteNumber());
        creditNote.setNoteType("Debit Note");
        creditNote.setCustomerID(masterPurchaseReturn.getSupplierID());
        creditNote.setCustomerName(masterPurchaseReturn.getSupplierName());
        creditNote.setCustomerEmail(masterPurchaseReturn.getSupplierEmail());
        creditNote.setCustomerMobile(masterPurchaseReturn.getSupplierMobile());
        creditNote.setCustomerLandline(supplierSelected.getContactLandline());
        creditNote.setInvoiceNumber(masterPurchaseReturn.getInvoiceNumber());
        creditNote.setSalesReturnNumber(masterPurchaseReturn.getPurchaseReturnNumber());
        creditNote.setOutletID((int) masterPurchaseReturn.getOutletID());
        creditNote.setAmount(masterPurchaseReturn.getDebitAmount());
        creditNote.setReason(masterPurchaseReturn.getNotes());
        creditNote.setCreatedBy(MyFunctions.parseInt(LoginID));
        creditNote.setCreatedDtTm(masterPurchaseReturn.getCreatedDtTm());
        creditNote.setNoteDate(masterPurchaseReturn.getCreatedDtTm());
        creditNote.setModifiedBy(0);
        creditNote.setSalesID(0);
        creditNote.setSynced(0);
        return creditNote;
    }

    private String createNewCreditNoteNumber() {
        StringBuilder builder = new StringBuilder();
        builder.append("DN");
//        builder.append(this.InvoiceSeries);
        builder.append(this.TabCode);
        builder.append("/");
        builder.append(this.dateFormatInInvoiceNumber());
        builder.append("/");
        String previousCreditNoteNumber = DatabaseHandler.getInstance().getPreviousCreditNoteNumber(builder.toString());
        if(previousCreditNoteNumber == null) {
            builder.append("001");
        } else {
            String seriesNumberString = previousCreditNoteNumber.substring(previousCreditNoteNumber.length() - 3);
            int seriesNumber = Integer.parseInt(seriesNumberString);
            seriesNumber++;
            seriesNumberString = "000" + seriesNumber;
            seriesNumberString = seriesNumberString.substring(seriesNumberString.length() - 3);
            builder.append("" + seriesNumberString);
        }
        return builder.toString();
    }

    private StockLedger createStockLedger(MasterSales masterSales, TransactionPurchase transactionPurchase) {
        StockLedger stockLedger = new StockLedger();
        stockLedger.setProductID(Math.round(transactionPurchase.getProductID()));
        stockLedger.setReferenceNumber(masterSales.getReferenceNumber());
        stockLedger.setTransactionType("S");
        stockLedger.setDateOfTransaction(masterSales.getSalesDate());
        stockLedger.setQuantity(transactionPurchase.getQuantity());
        stockLedger.setDateOfCreation(masterSales.getSalesDtTm());
        stockLedger.setInOut(-1);
        return stockLedger;
    }

    private boolean checkConfirmValidation() {
//        if(supplierSelected == null) {
//            showOkDialog("Choose Customer");
//        } else
            if(productsSelected.size() == 0) {
            showOkDialog("No product selected");
        } else {
            return true;
        }
        return false;
    }

    public void confirmInvoice() {
        // confirm invoice dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button yesButton = (Button) view.findViewById(R.id.yesClick);
        Button noButton = (Button) view.findViewById(R.id.noClick);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // invoice confirmed
                InvoiceInDatabase();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void InvoiceInDatabase() {
        MasterPurchaseReturn masterPurchaseReturn = createMasterPurchaseReturnObject();
        long masterPR_ID = DatabaseHandler.getInstance().insertMasterPurchaseReturn(masterPurchaseReturn);
        ArrayList<TransactionPurchaseReturn> transactionPurchaseReturnArrayList = new ArrayList<>();
        for (PurchaseReturnProduct purchaseReturnProduct : productsSelected) {
            transactionPurchaseReturnArrayList.add(createTransactionPurchaseReturnObject(masterPR_ID, purchaseReturnProduct));
        }
        DatabaseHandler.getInstance().addTransactionPurchaseReturnList(transactionPurchaseReturnArrayList);

        // generate credit note
        CreditDebitNote debitNote = createDebitNoteObject(masterPurchaseReturn);
        DatabaseHandler.getInstance().addNewCreditNote(debitNote);

        String salesReturnReceipt = getPurchaseReturnReceipt(masterPurchaseReturn, transactionPurchaseReturnArrayList);
        String creditNoteReceipt = getCreditNoteReceipt(debitNote);
        printThroughPrinter(salesReturnReceipt, creditNoteReceipt);

        // sales return save
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText("Record Saved Successfully!!");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NewPurchaseReturnActivity.super.onBackPressed();
            }
        });
    }

    private String getCreditNoteReceipt(CreditDebitNote creditNote) {
        int lengthOfScreen = 46;
        String rupeeSymbol = "Rs.";
//        String rupeeSymbol = "\u20B9";

        StringBuilder builder = new StringBuilder();
        String nextLine = "\n";
        String borderLine = "|" + MyFunctions.drawLine(" ", lengthOfScreen) + "|";

//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append(nextLine);
        // 1
        builder.append(" ");
        builder.append(MyFunctions.drawLine("-", lengthOfScreen));
        builder.append(" ");
        builder.append(nextLine);
        // 2
        builder.append(borderLine);
        builder.append(nextLine);
        // 3
        builder.append("|");
        builder.append(MyFunctions.makeStringCentreAlign("**" + creditNote.getNoteType() + "**", lengthOfScreen));
        builder.append("|");
        builder.append(nextLine);
        // 4
        builder.append(borderLine);
        builder.append(nextLine);
        // 5
        builder.append("|");
        builder.append(MyFunctions.makeStringCentreAlign("Voucher No. - " + creditNote.getNoteNumber(), lengthOfScreen));
        builder.append("|");
        builder.append(nextLine);
        // 6
        builder.append("|");
        builder.append(MyFunctions.makeStringCentreAlign("Amount - " + rupeeSymbol + String.format("%.2f", creditNote.getAmount()), lengthOfScreen));
        builder.append("|");
        builder.append(nextLine);
        // 7
        builder.append(borderLine);
        builder.append(nextLine);
        // 8
        builder.append(" ");
        builder.append(MyFunctions.drawLine("-", lengthOfScreen));
        builder.append(" ");
        builder.append(nextLine);
//
//        builder.append(nextLine);
//        builder.append(nextLine);

        return builder.toString();
    }

    private String getPurchaseReturnReceipt(MasterPurchaseReturn masterPurchaseReturn, ArrayList<TransactionPurchaseReturn> transactionPurchaseReturns) {
        StringBuilder builder = new StringBuilder();
        String nextLine = "\n";
        builder.append(nextLine);
        builder.append(this.OutletName);
        builder.append(nextLine);
        String address[] = this.OutletAddress.split("### ");
        for(String str : address) {
            builder.append(str);
            builder.append(nextLine);
        }
        builder.append("Tel : " + this.ContactNumber);
        builder.append(nextLine);

        builder.append("Outlet State: " + StateName);
        builder.append(nextLine);
        if(MyFunctions.StringLength(this.OutletGSTNumber) > 0) {
            builder.append("GST No. " + this.OutletGSTNumber);
            builder.append(nextLine);
        }
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("Purchase Return", 48));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("Purchase Return No. : " + masterPurchaseReturn.getPurchaseReturnNumber());
        builder.append(nextLine);
        builder.append("Purchase Invoice No. : " + masterPurchaseReturn.getInvoiceNumber());
        builder.append(nextLine);
        builder.append("Date : " + masterPurchaseReturn.getPurchaseReturnDate());
        builder.append(nextLine);
        builder.append("Cashier : " + this.FirstName);
        builder.append(nextLine);
        builder.append("Supplier : " + masterPurchaseReturn.getSupplierName());
        if(MyFunctions.StringLength(masterPurchaseReturn.getSupplierMobile()) > 0) {
            builder.append(" ( " + masterPurchaseReturn.getSupplierMobile() + " )");
        }
        builder.append(nextLine);
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        String itemNameHeading = "ItemName";
        int itemNameHeadingLength = 11;
        builder.append(MyFunctions.makeStringLeftAlign(itemNameHeading, itemNameHeadingLength));

        String qtyHeading = "Rtn Qty";
        int qtyHeadingLength = 7;
        builder.append(MyFunctions.makeStringRightAlign(qtyHeading, qtyHeadingLength));

        String priceHeading = "Price";
        int priceHeadingLength = 10;
        builder.append(MyFunctions.makeStringRightAlign(priceHeading, priceHeadingLength));

        String gstHeading = "Tax";
        int gstHeadingLength = 9;
        builder.append(MyFunctions.makeStringRightAlign(gstHeading, gstHeadingLength));

        String amountHeading = "Amount";
        int amountHeadingLength = 11;
        builder.append(MyFunctions.makeStringRightAlign(amountHeading, amountHeadingLength));

        builder.append(nextLine);
        for(TransactionPurchaseReturn transactionPurchaseReturn : transactionPurchaseReturns) {
            builder.append(transactionPurchaseReturn.getProductName());
            builder.append(nextLine);

            builder.append(MyFunctions.makeStringLeftAlign("  ", itemNameHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(transactionPurchaseReturn.getReturnQty() + "", qtyHeadingLength));

            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionPurchaseReturn.getUnitPrice()), priceHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionPurchaseReturn.getReturnTaxAmount()), gstHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionPurchaseReturn.getReturnAmount()), amountHeadingLength));
            builder.append(nextLine);

        }

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        int leftAlignLength = 36;
        int rightAlignLength = 12;
        builder.append(MyFunctions.makeStringLeftAlign("Debit Amount", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterPurchaseReturn.getDebitAmount()), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Note :- " + masterPurchaseReturn.getNotes(), 48));
        builder.append(nextLine);
        builder.append(nextLine);
//        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your visit***",48));
//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append(nextLine);

        return builder.toString();
    }

    public void extractFromDatabaseAndPrint(String invoiceNumber, double masterSalesID) {
        MasterSales msSales = DatabaseHandler.getInstance().getMasterSalesRecord(invoiceNumber);
//        ArrayList<TransactionSales> tArray = DatabaseHandler.getInstance().getInvoiceDetailsFromDB(msSales.getReferenceNumber());
//        Log.i("master", "Invoice Number - " + invoiceNumber);
//        for (TransactionSales tSales : tArray) {
//            Log.i("sales", tSales.getRefNumber() + " " + tSales.getItemName());
//        }

        // print
//        final String receiptString = getReceipt(creditNote, tArray);
//        Log.i("bill", receiptString);
//        printThroughPrinter(receiptString);

        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText("Record Saved Successfully!!");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NewPurchaseReturnActivity.super.onBackPressed();
            }
        });
    }

    public void onConfirmButtom(View v) {
//        showOkDialog("Under development");
        if(checkConfirmValidation()) {
            confirmInvoice();
        }
    }

    public void termsAndConditionsButtonClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.terms_and_conditions_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final EditText termsAndConditionsEditText = (EditText) view.findViewById(R.id.termsAndConditionsEditText);
        termsAndConditionsEditText.setText(this.termsAndConditionsString);
        Button saveButton = (Button) view.findViewById(R.id.saveClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsAndConditionsString = termsAndConditionsEditText.getText().toString();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void addressButtonClick(View v) {
        if(supplierSelected == null) {
            showOkDialog("Choose Supplier");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.invoice_address_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(1000, 400);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final EditText billAddressEditText = (EditText) view.findViewById(R.id.billAddressEditText);
        final EditText shipAddressEditText = (EditText) view.findViewById(R.id.shipAddressEditText);
        billAddressEditText.setText(this.billAddressString);
        shipAddressEditText.setText(this.shipAddressString);
        Button saveButton = (Button) view.findViewById(R.id.saveClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billAddressString = billAddressEditText.getText().toString();
                shipAddressString = shipAddressEditText.getText().toString();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void showOkDialog(String heading) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText(heading);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void noteButtonClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.terms_and_conditions_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final EditText termsAndConditionsEditText = (EditText) view.findViewById(R.id.termsAndConditionsEditText);
        final TextView headingTextView = (TextView) view.findViewById(R.id.headingTextView);
        headingTextView.setText("Enter a note");
        termsAndConditionsEditText.setText(this.noteString);
        Button saveButton = (Button) view.findViewById(R.id.saveClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteString = termsAndConditionsEditText.getText().toString();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
