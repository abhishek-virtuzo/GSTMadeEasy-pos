package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.virtuzo.abhishek.Views.Adapters.ProductListInSalesReturnAdapter;
import com.virtuzo.abhishek.Views.Adapters.ProductsSelectedInSalesReturnAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.CreditDebitNote;
import com.virtuzo.abhishek.modal.Customer;
import com.virtuzo.abhishek.modal.GSTCategory;
import com.virtuzo.abhishek.modal.MasterSales;
import com.virtuzo.abhishek.modal.MasterSalesReturn;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.ProductWithQuantity;
import com.virtuzo.abhishek.modal.SalesReturnProduct;
import com.virtuzo.abhishek.modal.StockLedger;
import com.virtuzo.abhishek.modal.TransactionSales;
import com.virtuzo.abhishek.modal.TransactionSalesReturn;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class NewSalesReturnActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView productListInSalesReturnRecyclerView, productsSelectedInSalesReturnRecyclerView;
    final int NUMBER_OF_COLUMNS_IN_PRODUCT_LIST = 2, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED = 1;
    private ProductListInSalesReturnAdapter productListAdapter;
    private ProductsSelectedInSalesReturnAdapter productsSelectedAdapter;
    MasterSales masterSales;
    private ArrayList<TransactionSales> transactionSalesList;
    private ArrayList<SalesReturnProduct> productsSelected;
    TextView customerInSalesReturn, invoiceDateInSalesReturn;
    ImageButton backButton;
    String LoginID, BusinessId, OutletID, TermsAndConditions, OutletAddress, OutletLogo, GSTCategoryID,
            OutletGSTNumber, InvoiceSeries, TabCode, StateID, OutletName, FirstName, ContactNumber, StateName;
    private final int CHOOSE_CUSTOMER_RESPONSE_CODE = 101, INVOICE_PAYMENT_CODE = 102, DATE_PICKER_DIALOG_ID = 201;
    Customer customerSelected;
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
    String salesReturnInvoiceNumber;
    String saleInvoiceNumber;
    TextView invoiceNumberTextView, productSelectedTitleTextView;
    String termsAndConditionsString = "";
    String noteString = "";
    String billAddressString = "";
    String shipAddressString = "";

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(NewSalesReturnActivity.this, getString(R.string.usb_msg_getpermission),
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
        setContentView(R.layout.activity_sales_return);

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
        saleInvoiceNumber = getIntent().getExtras().getString("salesReturnInvoiceNumber");
        masterSales = DatabaseHandler.getInstance().getMasterSalesRecord(saleInvoiceNumber);

//        setTitle("New Invoice");
        getSupportActionBar().hide();
        Toolbar toolbar_in_new_invoice = (Toolbar) findViewById(R.id.toolbar_in_sales_return);
        toolbar_in_new_invoice.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_new_invoice.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_new_invoice.findViewById(R.id.actionBarTitle);
        heading.setText("Sales Return");
        toolbar_in_new_invoice.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_bright)));

        invoiceNumberTextView = (TextView) toolbar_in_new_invoice.findViewById(R.id.invoiceNumberTextViewInSalesReturn);
        invoiceNumberTextView.setText(salesReturnInvoiceNumber);

        SharedPreferences data = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        BusinessId = data.getString("BusinessId", "");

        subTotalTextView = (TextView) findViewById(R.id.subTotalInNewInvoiceTextView);
        taxTextView = (TextView) findViewById(R.id.totalTaxInNewInvoiceTextView);
        totalAmountTextView = (TextView) findViewById(R.id.totalAmountInNewInvoiceTextView);
        String rupeeSymbol = "\u20B9";
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        taxTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f", 0d));

        // products list
        transactionSalesList = new ArrayList<>();
        productListInSalesReturnRecyclerView = (RecyclerView) findViewById(R.id.productListInSalesReturnRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_PRODUCT_LIST);
        productListInSalesReturnRecyclerView.setLayoutManager(mLayoutManager);
        productListInSalesReturnRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productListAdapter = new ProductListInSalesReturnAdapter(transactionSalesList, new ProductListInSalesReturnAdapter.OnClickListener() {
            @Override
            public void onItemClick(TransactionSales product) {
                selectProduct(product);
            }
        });
        productListInSalesReturnRecyclerView.setAdapter(productListAdapter);
        Log.i("New Invoice", "Before database access");
        transactionSalesList.addAll(DatabaseHandler.getInstance().getInvoiceDetailsFromDB(saleInvoiceNumber));
        calculateSalesPriceAfterOverallDiscount();
        Log.i("New Invoice", "After database access");
        productListAdapter.notifyDataSetChanged();

        // search bar for product list
        searchViewForProductList = (SearchView) findViewById(R.id.searchProductListSearchViewInSalesReturn);
        searchViewForProductList.setOnQueryTextListener(this);
        searchViewForProductList.setQueryHint("Search Product");
        setSearchViewHeight(searchViewForProductList, 25);
        searchViewForProductList.setIconified(false);

        // products selected
        productSelectedTitleTextView = (TextView) findViewById(R.id.productSelectedTitleTextView);
        productsSelected = new ArrayList<>();
        productsSelectedInSalesReturnRecyclerView = (RecyclerView) findViewById(R.id.productsSelectedInSalesReturnRecyclerView);
        RecyclerView.LayoutManager nLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED);
        productsSelectedInSalesReturnRecyclerView.setLayoutManager(nLayoutManager);
        productsSelectedInSalesReturnRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productsSelectedAdapter = new ProductsSelectedInSalesReturnAdapter(productsSelected, new ProductsSelectedInSalesReturnAdapter.OnClickListener() {
            @Override
            public void onItemClick(final SalesReturnProduct salesReturnProduct) {
                updateProductDetails(salesReturnProduct);
            }

            @Override
            public void onCancelItem(int position) {
                productsSelected.remove(position);
                productsSelectedAdapter.notifyDataSetChanged();
                setProductSelectedTitle(productsSelected.size());
                refreshSubTotalAndTax();
            }
        });
        productsSelectedInSalesReturnRecyclerView.setAdapter(productsSelectedAdapter);

        // choose customer
        customerSelected = DatabaseHandler.getInstance().getCustomerRecordFromSales(masterSales);
        if (customerSelected == null) {
            customerSelected = Customer.getGuestCustomer();
        }
        customerInSalesReturn = (TextView) findViewById(R.id.customerInSalesReturn);
        String showCustomerEntityName = customerSelected.getEntityName();
        if(customerSelected.getEntityType().equalsIgnoreCase("Individual")) {
            showCustomerEntityName = customerSelected.getContactPerson();
        }
        customerInSalesReturn.setText(showCustomerEntityName
                + " (" + customerSelected.getContactNumber() + ")");

        // invoice date
        validInvoiceDateSelected = false;
        invoiceDatePickerButton = (Button) findViewById(R.id.invoiceDatePickerButtonInNewInvoice);
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

    private void calculateSalesPriceAfterOverallDiscount() {
        double totalAmount = 0.0;
        double overallDiscount = masterSales.getTotalDiscountAmount();
        for (TransactionSales transactionSales : transactionSalesList ) {
            totalAmount += (transactionSales.getQty() * transactionSales.getSalesPriceWithDiscount());
        }
        for (TransactionSales transactionSales : transactionSalesList ) {
            double salesReturnUnitPrice = (transactionSales.getSalesPriceWithDiscount() * overallDiscount) / totalAmount;
            salesReturnUnitPrice = transactionSales.getSalesPriceWithDiscount() - salesReturnUnitPrice;
            transactionSales.setSalesReturnUnitPrice(salesReturnUnitPrice);
        }
    }

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(NewSalesReturnActivity.this,mHandler);
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

        Toast.makeText(NewSalesReturnActivity.this, getString(R.string.usb_msg_conn_state),
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
                Toast.makeText(NewSalesReturnActivity.this, getString(R.string.usb_msg_getpermission),
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
                Toast.makeText(NewSalesReturnActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(NewSalesReturnActivity.this, "The printer has no paper",
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
                    Toast.makeText(NewSalesReturnActivity.this, dateString, Toast.LENGTH_SHORT).show();
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
        salesReturnInvoiceNumber = createInvoiceNumber();
        invoiceNumberTextView.setText(salesReturnInvoiceNumber);
    }

    private String createInvoiceNumber() {
        StringBuilder builder = new StringBuilder();
        builder.append("SR");
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

        if(requestCode == CHOOSE_CUSTOMER_RESPONSE_CODE) {
            if(resultCode == Activity.RESULT_OK) {
            }
        } else if(requestCode == INVOICE_PAYMENT_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                double masterSalesID = data.getDoubleExtra("masterSalesID", 0.0);
                extractFromDatabaseAndPrint(salesReturnInvoiceNumber, masterSalesID);
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
            productListAdapter.setFilter(transactionSalesList);
            return true;
        }
        ArrayList<TransactionSales> newList = new ArrayList<>();
        for(TransactionSales product : transactionSalesList) {
            String name = MyFunctions.lowerCase(product.getItemName());
            if(name.contains(newText)){
                newList.add(product);
            }
        }
        productListAdapter.setFilter(newList);
        return true;
    }

    public void selectProduct(TransactionSales product) {
        // when a product is clicked
        boolean newProductSelected = true;
//        for (int i = 0; i < productsSelected.size(); i++) {
        for(SalesReturnProduct salesReturnProduct : productsSelected){
            if(salesReturnProduct.getTransactionSales().getItemID() == product.getItemID()) {
                newProductSelected = false;
                updateProductDetails(salesReturnProduct);
                break;
            }
        }
        if(newProductSelected) {
            // New product added
            SalesReturnProduct salesReturnProduct = new SalesReturnProduct(product);
            addNewProductDetails(salesReturnProduct);
        }
    }

    public void addNewProductDetails(final SalesReturnProduct salesReturnProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.product_selected_details_sales_return_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final TextView titleTextView = (TextView) view.findViewById(R.id.productNameTitle);
        final TextView originalQuantitySold = (TextView) view.findViewById(R.id.originalQuantitySold);
        final EditText returnQuantityEditText = (EditText) view.findViewById(R.id.returnQuantityEditText);
        final TextView originalUnitPrice = (TextView) view.findViewById(R.id.originalUnitPrice);
        final EditText returnUnitPriceEditText = (EditText) view.findViewById(R.id.returnUnitPriceEditText);
        final TextView originalTaxRate = (TextView) view.findViewById(R.id.originalTaxRate);
//        final EditText returnTaxRateEditText = (EditText) view.findViewById(R.id.returnTaxRateEditText);
        final CheckBox changePriceOnlyCheckBox = (CheckBox) view.findViewById(R.id.changePriceOnlyCheckBox);
//        final TextView originalTaxAmount = (TextView) view.findViewById(R.id.originalTaxAmount);
//        final TextView returnTaxAmount = (TextView) view.findViewById(R.id.returnTaxAmount);
//        final TextView finalCreditAmount = (TextView) view.findViewById(R.id.finalCreditAmount);

        titleTextView.setText(salesReturnProduct.getTransactionSales().getItemName());
        originalQuantitySold.setText(salesReturnProduct.getTransactionSales().getQty()+"");
        returnQuantityEditText.setText(salesReturnProduct.getTransactionSales().getQty()+"");
        originalUnitPrice.setText(String.format("%.2f", salesReturnProduct.getTransactionSales().getSalesReturnUnitPrice()));
        returnUnitPriceEditText.setText(String.format("%.2f", salesReturnProduct.getReturnPrice()));
        originalTaxRate.setText(String.format("%.2f", salesReturnProduct.getTransactionSales().getTaxRate()));
//        originalTaxAmount.setText(String.format("%.2f", salesReturnProduct.getTransactionPurchase().getUnitTaxAmount()));
//        returnTaxRateEditText.setText(String.format("%.2f", salesReturnProduct.getReturnTaxRate()));
//        returnTaxAmount.setText(String.format("%.2f", salesReturnProduct.getReturnTax()));
//        finalCreditAmount.setText(String.format("%.2f", 0d));

        returnQuantityEditText.selectAll();

//        returnQuantityEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String returnTaxRateString = returnTaxRateEditText.getText().toString();
//                String returnQuantityString = returnQuantityEditText.getText().toString();
//                double taxRate = 0;
//                int returnQuantity = 0;
//                if(returnTaxRateString.length() > 0) {
//                    taxRate = MyFunctions.parseDouble(returnTaxRateString);
//                }
//                if(returnQuantityString.length() > 0) {
//                    returnQuantity = Integer.parseInt(returnQuantityString);
//                }
//                String returnUnitPriceString = returnUnitPriceEditText.getText().toString();
//                double returnUnitPrice = 0;
//                if(returnUnitPriceString.length() > 0) {
//                    returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
//                }
//                double taxAmount = 0;
//                if(returnUnitPrice > 0) {
//                    taxAmount = (taxRate * returnQuantity * returnUnitPrice) / 100;
//                }
//                returnTaxAmount.setText(String.format("%.2f", taxAmount));
//            }
//        });
//        returnUnitPriceEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String returnTaxRateString = returnTaxRateEditText.getText().toString();
//                String returnQuantityString = returnQuantityEditText.getText().toString();
//                double taxRate = 0;
//                int returnQuantity = 0;
//                if(returnTaxRateString.length() > 0) {
//                    taxRate = MyFunctions.parseDouble(returnTaxRateString);
//                }
//                if(returnQuantityString.length() > 0) {
//                    returnQuantity = Integer.parseInt(returnQuantityString);
//                }
//                String returnUnitPriceString = returnUnitPriceEditText.getText().toString();
//                double returnUnitPrice = 0;
//                if(returnUnitPriceString.length() > 0) {
//                    returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
//                }
//                double taxAmount = 0;
//                if(returnUnitPrice > 0) {
//                    taxAmount = (taxRate * returnQuantity * returnUnitPrice) / 100;
//                }
//                returnTaxAmount.setText(String.format("%.2f", taxAmount));
//            }
//        });
//        returnTaxRateEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String returnTaxRateString = returnTaxRateEditText.getText().toString();
//                String returnQuantityString = returnQuantityEditText.getText().toString();
//                double taxRate = 0;
//                int returnQuantity = 0;
//                if(returnTaxRateString.length() > 0) {
//                    taxRate = MyFunctions.parseDouble(returnTaxRateString);
//                }
//                if(returnQuantityString.length() > 0) {
//                    returnQuantity = Integer.parseInt(returnQuantityString);
//                }
//                String returnUnitPriceString = returnUnitPriceEditText.getText().toString();
//                double returnUnitPrice = 0;
//                if(returnUnitPriceString.length() > 0) {
//                    returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
//                }
//                double taxAmount = 0;
//                if(returnUnitPrice > 0) {
//                    taxAmount = (taxRate * returnQuantity * returnUnitPrice) / 100;
//                }
//                returnTaxAmount.setText(String.format("%.2f", taxAmount));
//            }
//        });

        // default values
        final Button okButton = (Button) view.findViewById(R.id.okButton);
        final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

        changePriceOnlyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    returnQuantityEditText.setText(originalQuantitySold.getText().toString());
                    returnQuantityEditText.setEnabled(false);
                    returnUnitPriceEditText.setFocusableInTouchMode(true);
                    returnUnitPriceEditText.requestFocus();
                } else {
                    returnQuantityEditText.setFocusableInTouchMode(true);
                    returnQuantityEditText.setText(originalQuantitySold.getText().toString());
                    returnQuantityEditText.setEnabled(true);
                    returnQuantityEditText.requestFocus();
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String returnQuantityString = returnQuantityEditText.getText().toString();
                String returnUnitPriceString = returnUnitPriceEditText.getText().toString();
//                String returnTaxAmountString = returnTaxAmount.getText().toString();
                String returnTaxRateString = originalTaxRate.getText().toString();
                if(returnUnitPriceString.length() == 0) {
                    showOkDialog("Enter valid price");
                } else if(returnQuantityString.length() == 0 || returnQuantityString.length() > 4) {
                    showOkDialog("Enter valid quantity");
                } else {
                    double returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
                    int returnQuantity = Integer.parseInt(returnQuantityString);
                    double returnTaxRate = MyFunctions.parseDouble(returnTaxRateString);
                    if(returnUnitPrice == 0 || returnUnitPrice > salesReturnProduct.getTransactionSales().getSalesReturnUnitPrice()) {
                            showOkDialog("Enter valid price");
                    } else if(returnQuantity == 0 || returnQuantity > salesReturnProduct.getTransactionSales().getQty()) {
                        showOkDialog("Enter valid quantity");
                    } else if(returnTaxRate > 100) {
                        showOkDialog("Enter valid tax");
                    } else {
                        double returnTaxAmount = (returnTaxRate * returnQuantity * returnUnitPrice) / 100;
                        double creditAmount = (returnUnitPrice * returnQuantity) + returnTaxAmount;

                        if (changePriceOnlyCheckBox.isChecked()) {
                            double originalUnitSalePrice = MyFunctions.parseDouble(originalUnitPrice.getText().toString());
                            returnUnitPrice = originalUnitSalePrice - returnUnitPrice;

                            if (returnUnitPrice == 0) {
                                showOkDialog("Price should be different during price change only mode");
                                return;
                            }

                            returnTaxAmount = (returnTaxRate * returnQuantity * returnUnitPrice) / 100;
                            creditAmount = (returnUnitPrice * returnQuantity) + returnTaxAmount;
                        }

                        if (changePriceOnlyCheckBox.isChecked()) {
                            salesReturnProduct.setReturnType(1);
                        } else {
                            salesReturnProduct.setReturnType(0);
                        }
                        salesReturnProduct.setReturnPrice(returnUnitPrice);
                        salesReturnProduct.setReturnQuantity(returnQuantity);
                        salesReturnProduct.setReturnTaxRate(returnTaxRate);
                        salesReturnProduct.setReturnTax(returnTaxAmount);
                        salesReturnProduct.setReturnAmount(creditAmount);
                        productsSelected.add(salesReturnProduct);
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

    public void updateProductDetails(final SalesReturnProduct salesReturnProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.product_selected_details_sales_return_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final TextView titleTextView = (TextView) view.findViewById(R.id.productNameTitle);
        final TextView originalQuantitySold = (TextView) view.findViewById(R.id.originalQuantitySold);
        final EditText returnQuantityEditText = (EditText) view.findViewById(R.id.returnQuantityEditText);
        final TextView originalUnitPrice = (TextView) view.findViewById(R.id.originalUnitPrice);
        final EditText returnUnitPriceEditText = (EditText) view.findViewById(R.id.returnUnitPriceEditText);
        final TextView originalTaxRate = (TextView) view.findViewById(R.id.originalTaxRate);
//        final TextView originalTaxAmount = (TextView) view.findViewById(R.id.originalTaxAmount);
        final CheckBox changePriceOnlyCheckBox = (CheckBox) view.findViewById(R.id.changePriceOnlyCheckBox);
//        final EditText returnTaxRateEditText = (EditText) view.findViewById(R.id.returnTaxRateEditText);
//        final TextView returnTaxAmount = (TextView) view.findViewById(R.id.returnTaxAmount);
//        final TextView finalCreditAmount = (TextView) view.findViewById(R.id.finalCreditAmount);

        titleTextView.setText(salesReturnProduct.getTransactionSales().getItemName());
        originalQuantitySold.setText(salesReturnProduct.getTransactionSales().getQty()+"");
        returnQuantityEditText.setText(salesReturnProduct.getReturnQuantity()+"");
        originalUnitPrice.setText(String.format("%.2f", salesReturnProduct.getTransactionSales().getSalesReturnUnitPrice()));
        returnUnitPriceEditText.setText(String.format("%.2f", salesReturnProduct.getReturnPrice()));
        originalTaxRate.setText(String.format("%.2f", salesReturnProduct.getTransactionSales().getTaxRate()));
        if (salesReturnProduct.getReturnType() == 1 ) {
            changePriceOnlyCheckBox.setChecked(true);
            returnQuantityEditText.setEnabled(false);
            returnUnitPriceEditText.setText(String.format("%.2f", salesReturnProduct.getTransactionSales().getSalesReturnUnitPrice() - salesReturnProduct.getReturnPrice()));
        }

//        originalTaxAmount.setText(String.format("%.2f", salesReturnProduct.getTransactionPurchase().getTaxAmount()));
//        returnTaxRateEditText.setText(String.format("%.2f", salesReturnProduct.getReturnTaxRate()));
//        returnTaxAmount.setText(String.format("%.2f", salesReturnProduct.getReturnTax()));
//        finalCreditAmount.setText(String.format("%.2f", 0d));

        returnQuantityEditText.selectAll();

//        returnQuantityEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String returnTaxRateString = returnTaxRateEditText.getText().toString();
//                String returnQuantityString = returnQuantityEditText.getText().toString();
//                double taxRate = 0;
//                int returnQuantity = 0;
//                if(returnTaxRateString.length() > 0) {
//                    taxRate = MyFunctions.parseDouble(returnTaxRateString);
//                }
//                if(returnQuantityString.length() > 0) {
//                    returnQuantity = Integer.parseInt(returnQuantityString);
//                }
//                String returnUnitPriceString = returnUnitPriceEditText.getText().toString();
//                double returnUnitPrice = 0;
//                if(returnUnitPriceString.length() > 0) {
//                    returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
//                }
//                double taxAmount = 0;
//                if(returnUnitPrice > 0) {
//                    taxAmount = (taxRate * returnQuantity * returnUnitPrice) / 100;
//                }
//                returnTaxAmount.setText(String.format("%.2f", taxAmount));
//            }
//        });
//        returnUnitPriceEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String returnTaxRateString = returnTaxRateEditText.getText().toString();
//                String returnQuantityString = returnQuantityEditText.getText().toString();
//                double taxRate = 0;
//                int returnQuantity = 0;
//                if(returnTaxRateString.length() > 0) {
//                    taxRate = MyFunctions.parseDouble(returnTaxRateString);
//                }
//                if(returnQuantityString.length() > 0) {
//                    returnQuantity = Integer.parseInt(returnQuantityString);
//                }
//                String returnUnitPriceString = returnUnitPriceEditText.getText().toString();
//                double returnUnitPrice = 0;
//                if(returnUnitPriceString.length() > 0) {
//                    returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
//                }
//                double taxAmount = 0;
//                if(returnUnitPrice > 0) {
//                    taxAmount = (taxRate * returnQuantity * returnUnitPrice) / 100;
//                }
//                returnTaxAmount.setText(String.format("%.2f", taxAmount));
//            }
//        });
//        returnTaxRateEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String returnTaxRateString = returnTaxRateEditText.getText().toString();
//                String returnQuantityString = returnQuantityEditText.getText().toString();
//                double taxRate = 0;
//                int returnQuantity = 0;
//                if(returnTaxRateString.length() > 0) {
//                    taxRate = MyFunctions.parseDouble(returnTaxRateString);
//                }
//                if(returnQuantityString.length() > 0) {
//                    returnQuantity = Integer.parseInt(returnQuantityString);
//                }
//                String returnUnitPriceString = returnUnitPriceEditText.getText().toString();
//                double returnUnitPrice = 0;
//                if(returnUnitPriceString.length() > 0) {
//                    returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
//                }
//                double taxAmount = 0;
//                if(returnUnitPrice > 0) {
//                    taxAmount = (taxRate * returnQuantity * returnUnitPrice) / 100;
//                }
//                returnTaxAmount.setText(String.format("%.2f", taxAmount));
//            }
//        });

        // default values

        final Button okButton = (Button) view.findViewById(R.id.okButton);
        final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

        changePriceOnlyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    returnQuantityEditText.setText(originalQuantitySold.getText().toString());
                    returnQuantityEditText.setEnabled(false);
                    returnUnitPriceEditText.requestFocus();
                } else {
                    returnQuantityEditText.setText(originalQuantitySold.getText().toString());
                    returnQuantityEditText.setEnabled(true);
                    returnQuantityEditText.requestFocus();
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String returnQuantityString = returnQuantityEditText.getText().toString();
                String returnUnitPriceString = returnUnitPriceEditText.getText().toString();
//                String returnTaxAmountString = returnTaxAmount.getText().toString();
                String returnTaxRateString = originalTaxRate.getText().toString();
                if(returnUnitPriceString.length() == 0) {
                    showOkDialog("Enter valid price");
                } else if(returnQuantityString.length() == 0 || returnQuantityString.length() > 4) {
                    showOkDialog("Enter valid quantity");
                } else {
                    double returnUnitPrice = MyFunctions.parseDouble(returnUnitPriceString);
                    int returnQuantity = Integer.parseInt(returnQuantityString);
                    double returnTaxRate = MyFunctions.parseDouble(returnTaxRateString);
                    if(returnUnitPrice == 0 || returnUnitPrice > salesReturnProduct.getTransactionSales().getSalesReturnUnitPrice()) {
                        showOkDialog("Enter valid price");
                    } else if(returnQuantity == 0 || returnQuantity > salesReturnProduct.getTransactionSales().getQty()) {
                        showOkDialog("Enter valid quantity");
                    } else if(returnTaxRate > 100) {
                        showOkDialog("Enter valid tax");
                    } else {
                        double returnTaxAmount = (returnTaxRate * returnQuantity * returnUnitPrice) / 100;
                        double creditAmount = (returnUnitPrice * returnQuantity) + returnTaxAmount;

                        if (changePriceOnlyCheckBox.isChecked()) {
                            double originalUnitSalePrice = MyFunctions.parseDouble(originalUnitPrice.getText().toString());
                            returnUnitPrice = originalUnitSalePrice - returnUnitPrice;

                            if (returnUnitPrice == 0) {
                                showOkDialog("Price should be different during price change only mode");
                                return;
                            }

                            returnTaxAmount = (returnTaxRate * returnQuantity * returnUnitPrice) / 100;
                            creditAmount = (returnUnitPrice * returnQuantity) + returnTaxAmount;
                        }

                        if (changePriceOnlyCheckBox.isChecked()) {
                            salesReturnProduct.setReturnType(1);
                        } else {
                            salesReturnProduct.setReturnType(0);
                        }
                        salesReturnProduct.setReturnPrice(returnUnitPrice);
                        salesReturnProduct.setReturnQuantity(returnQuantity);
                        salesReturnProduct.setReturnTaxRate(returnTaxRate);
                        salesReturnProduct.setReturnTax(returnTaxAmount);
                        salesReturnProduct.setReturnAmount(creditAmount);
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
                switch (customerSelected.getCategoryId()) {
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
        for(SalesReturnProduct product : productsSelected) {
            double salesPrice = product.getReturnPrice() * product.getReturnQuantity();
            if (product.getReturnQuantity() == 0) {
                salesPrice = product.getReturnPrice() * product.getTransactionSales().getQty();
            }
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
        for(SalesReturnProduct product : productsSelected) {
            tax += product.getReturnTax();
        }
        return tax;
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

    private MasterSales createMasterSalesObject(String invoiceNumber) {
        MasterSales masterSales = new MasterSales();
//        private static final int INDEX_M_SALES_SalesID = 0; // auto increment
//        private static final int INDEX_M_SALES_SalesReceiptNumber = 1; // invoice number
        masterSales.setSalesReceiptNumber(invoiceNumber);
//        private static final int INDEX_M_SALES_CustomerID = 2;
        masterSales.setCustomerID(customerSelected.getCustomerId());
//        private static final int INDEX_M_SALES_BaseAmount = 3;
        masterSales.setBaseAmount(subTotalWithoutDiscount());
//        private static final int INDEX_M_SALES_TotalDiscountAmount = 4; // discount at end
        masterSales.setTotalDiscountAmount(overallDiscount);
//        private static final int INDEX_M_SALES_TaxAmount = 5;
        masterSales.setTaxAmount(taxCalculation());
//        private static final int INDEX_M_SALES_TotalAmount = 6;
        masterSales.setTotalAmount(Math.ceil(totalAmount()));
//        private static final int INDEX_M_SALES_ReferenceNumber = 7; // invoice number
        masterSales.setReferenceNumber(invoiceNumber);
//        private static final int INDEX_M_SALES_LoginID = 8; //  at login id
        masterSales.setLoginID(MyFunctions.parseDouble(this.LoginID));
//        private static final int INDEX_M_SALES_SalesDtTm = 9; // invoice date
        masterSales.setSalesDtTm(MyFunctions.getCurrentDateTime());
//        private static final int INDEX_M_SALES_Gender = 10; //
        masterSales.setGender(customerSelected.getGender()); // customer gender, check entitytype (switch case)
//        private static final int INDEX_M_SALES_isHold = 11; // 0
        masterSales.setHold(false);
//        private static final int INDEX_M_SALES_isActive = 12; // 1
        masterSales.setActive(true);
//        private static final int INDEX_M_SALES_OutletID = 13; // at login
        masterSales.setOutletID(MyFunctions.parseDouble(this.OutletID));
//        private static final int INDEX_M_SALES_CustomerType = 14; // 'N'
        masterSales.setCustomerType("N");
//        private static final int INDEX_M_SALES_Balance = 15; // 0
        masterSales.setBalance(masterSales.getTotalAmount());
//        private static final int INDEX_M_SALES_PaidAmount = 16; // amount paid
        masterSales.setPaidAmount(0); // hard coded
//        private static final int INDEX_M_SALES_TaxOnPaidAmount = 17; // only on paid amount
        masterSales.setTaxOnPaidAmount(0); // hard coded
//        private static final int INDEX_M_SALES_CGST = 18; // 50%
//        private static final int INDEX_M_SALES_SGST = 19; // 50%
//        private static final int INDEX_M_SALES_IGST = 20; // 100%
        if(this.StateID.equals(customerSelected.getStateId())) {
            masterSales.setCGST(masterSales.getTaxAmount() / 2);
            masterSales.setSGST(masterSales.getTaxAmount() / 2);
            masterSales.setIGST(0d);
        } else {
            masterSales.setCGST(0d);
            masterSales.setSGST(0d);
            masterSales.setIGST(masterSales.getTaxAmount());
        }
//        private static final int INDEX_M_SALES_DiscountTypeID = 21; // % id 10 or 11
        masterSales.setDiscountTypeID(10); // discount type 10 or 11
//        private static final int INDEX_M_SALES_Discount = 22; // amount or %age
        masterSales.setDiscount(overallDiscountInPercentage());
//        private static final int INDEX_M_SALES_CESS = 23; // 0 default
        masterSales.setCESS(0); // hard coded
//        private static final int INDEX_M_SALES_DueDate = 24; // invoice date
        masterSales.setDueDate(this.invoiceDateString);
//        private static final int INDEX_M_SALES_CustomerName = 25; // entity name
        masterSales.setCustomerName(customerSelected.getEntityName());
//        private static final int INDEX_M_SALES_TermsCondition = 26; // at login
        masterSales.setTermsCondition(this.termsAndConditionsString);
//        private static final int INDEX_M_SALES_CustomerGSTN = 27;
        masterSales.setCustomerGSTN(customerSelected.getGSTNumber());
//        private static final int INDEX_M_SALES_CustomerEmail = 28;
        masterSales.setCustomerEmail(customerSelected.getContactEmailId());
//        private static final int INDEX_M_SALES_CustomerMobile = 29;
        masterSales.setCustomerMobile(customerSelected.getContactNumber());
//        private static final int INDEX_M_SALES_CustomerAddress = 30;
        masterSales.setCustomerAddress(customerSelected.getAddress());
//        private static final int INDEX_M_SALES_OutletAddress = 31; //  at login
        masterSales.setOutletAddress(this.OutletAddress);
//        private static final int INDEX_M_SALES_OutletLogo = 32; //  at login
        masterSales.setOutletLogo(this.OutletLogo);
//        private static final int INDEX_M_SALES_OutletGSTN = 33; // at login
        masterSales.setOutletGSTN(this.OutletGSTNumber);
//        private static final int INDEX_M_SALES_ShipAddress = 34; // same as customer or custom
        masterSales.setShipAddress(this.shipAddressString);
//        private static final int INDEX_M_SALES_TransportCharge = 35;
        masterSales.setTransportCharge(this.transportCharges);
//        private static final int INDEX_M_SALES_InsuranceCharge = 36;
        masterSales.setInsuranceCharge(this.insuranceCharges);
//        private static final int INDEX_M_SALES_PackingCharge = 37;
        masterSales.setPackingCharge(this.packingCharges);
//        private static final int INDEX_M_SALES_BillAddress = 38; // customer address
        masterSales.setBillAddress(this.billAddressString);
        masterSales.setSalesDate(this.invoiceDateString);

        return masterSales;
    }

    private TransactionSales createTransactionSales(Double masterSalesID, String invoiceNumber, ProductWithQuantity productWithQuantity) {
        TransactionSales transactionSales = new TransactionSales();
//        private static final int INDEX_T_SALES_SalesSubID = 0; // autoincrement

//        private static final int INDEX_T_SALES_SalesID = 1; // as master
        transactionSales.setSalesID(masterSalesID); // add master sales id
//        private static final int INDEX_T_SALES_SalesPersonID = 2; // at login id
        transactionSales.setSalesPersonID(MyFunctions.parseDouble(this.LoginID));
//        private static final int INDEX_T_SALES_ItemTypeID = 3; // 5
        transactionSales.setItemTypeID(5);
//        private static final int INDEX_T_SALES_ItemID = 4; // product id
        transactionSales.setItemID(productWithQuantity.getProduct().getProductID());
//        private static final int INDEX_T_SALES_Qty = 5; // product qty
        transactionSales.setQty(productWithQuantity.getQuantity());
//        private static final int INDEX_T_SALES_RefNumber = 6; // invoice number
        transactionSales.setRefNumber(invoiceNumber);
//        private static final int INDEX_T_SALES_BaseAmount = 7; // sales price
        transactionSales.setBaseAmount(productWithQuantity.getProduct().getSalesPrice());
//        private static final int INDEX_T_SALES_Discount = 8; // discount per item
        transactionSales.setDiscount(productWithQuantity.getDiscount() * productWithQuantity.getQuantity());
//        private static final int INDEX_T_SALES_TaxAmount = 9; // tax per item
        transactionSales.setTaxAmount(productWithQuantity.getTaxAmount() * productWithQuantity.getQuantity()); // switch type of customer
//        private static final int INDEX_T_SALES_TotalAmount = 10; // TOTAL
        transactionSales.setTotalAmount(productWithQuantity.getTotalAmountWithDiscountAndTAX() * productWithQuantity.getQuantity());
//        private static final int INDEX_T_SALES_SalesCommission = 11; // 0
        transactionSales.setSalesCommission(0d);
//        private static final int INDEX_T_SALES_CommissionTypeID = 12; // 0
        transactionSales.setCommissionTypeID(0);
//        private static final int INDEX_T_SALES_Outlet_SalesSubID = 13; // 0
        transactionSales.setOutlet_SalesSubID(0);
//        private static final int INDEX_T_SALES_DiscountType = 14; // 10  or 11
        transactionSales.setDiscountType(10);
//        private static final int INDEX_T_SALES_CGST = 15; // tax
        transactionSales.setCGST(transactionSales.getTaxAmount() / 2);
//        private static final int INDEX_T_SALES_SGST = 16; // tax
        transactionSales.setSGST(transactionSales.getTaxAmount() / 2);
//        private static final int INDEX_T_SALES_IGST = 17; // tax
        transactionSales.setIGST(0);
//        private static final int INDEX_T_SALES_CESS = 18; // 0
        transactionSales.setCESS(0d);
//        private static final int INDEX_T_SALES_ItemName= 19; // product name
        transactionSales.setItemName(productWithQuantity.getProduct().getProductName());
        transactionSales.setProductCode(productWithQuantity.getProduct().getProductCode());
        transactionSales.setTaxRate(productWithQuantity.getProduct().getTAX());
        return transactionSales;
    }

    private MasterSalesReturn createMasterSalesReturnObject() {
        MasterSalesReturn masterSalesReturn = new MasterSalesReturn();
        masterSalesReturn.setSalesReturnDate(invoiceDateString);
        masterSalesReturn.setCustomerID(customerSelected.getCustomerId());
        masterSalesReturn.setOutletID(MyFunctions.parseInt(OutletID));
        masterSalesReturn.setCreditAmount(totalAmount());
        masterSalesReturn.setCreatedBy(MyFunctions.parseDouble(LoginID));
        masterSalesReturn.setCreatedDttm(MyFunctions.getCurrentDateTime());
        if(customerSelected.getEntityType().equalsIgnoreCase("Individual")) {
            masterSalesReturn.setCustomerName(customerSelected.getContactPerson());
        } else {
            masterSalesReturn.setCustomerName(customerSelected.getEntityName());
        }
        masterSalesReturn.setCustomerEmail(customerSelected.getContactEmailId());
        masterSalesReturn.setCustomerMobile(customerSelected.getContactNumber());
        masterSalesReturn.setCustomerLandline(customerSelected.getContactLandline());
        masterSalesReturn.setInvoiceNumber(saleInvoiceNumber);
        masterSalesReturn.setSalesReturnNumber(salesReturnInvoiceNumber);
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

    private CreditDebitNote createCreditNoteObject(MasterSalesReturn masterSalesReturn) {
        CreditDebitNote creditNote = new CreditDebitNote();
        creditNote.setNoteNumber(createNewCreditNoteNumber());
        creditNote.setNoteType("Credit Note");
        creditNote.setCustomerID(masterSalesReturn.getCustomerID());
        creditNote.setCustomerName(masterSalesReturn.getCustomerName());
        creditNote.setCustomerEmail(masterSalesReturn.getCustomerEmail());
        creditNote.setCustomerMobile(masterSalesReturn.getCustomerMobile());
        creditNote.setCustomerLandline(masterSalesReturn.getCustomerLandline());
        creditNote.setInvoiceNumber(masterSalesReturn.getInvoiceNumber());
        creditNote.setSalesReturnNumber(masterSalesReturn.getSalesReturnNumber());
        creditNote.setOutletID(masterSalesReturn.getOutletID());
        creditNote.setAmount(masterSalesReturn.getCreditAmount());
        creditNote.setReason(masterSalesReturn.getNotes());
        creditNote.setCreatedBy(MyFunctions.parseInt(LoginID));
        creditNote.setCreatedDtTm(masterSalesReturn.getCreatedDttm());
        creditNote.setNoteDate(masterSalesReturn.getCreatedDttm());
        creditNote.setModifiedBy(0);
        creditNote.setSalesID(0);
        creditNote.setSynced(0);
        return creditNote;
    }

    private String createNewCreditNoteNumber() {
        StringBuilder builder = new StringBuilder();
        builder.append("CN");
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

    private StockLedger createStockLedger(MasterSales masterSales, TransactionSales transactionSales) {
        StockLedger stockLedger = new StockLedger();
        stockLedger.setProductID(transactionSales.getItemID());
        stockLedger.setReferenceNumber(masterSales.getReferenceNumber());
        stockLedger.setTransactionType("S");
        stockLedger.setDateOfTransaction(masterSales.getSalesDate());
        stockLedger.setQuantity(transactionSales.getQty());
        stockLedger.setDateOfCreation(masterSales.getSalesDtTm());
        stockLedger.setInOut(-1);
        return stockLedger;
    }

    private boolean checkConfirmValidation() {
//        if(customerSelected == null) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
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
//        String salesReturnInvoiceNumber = getInvoiceNumber();
//        MasterSales masterSales = createMasterSalesObject(salesReturnInvoiceNumber);
//        double masterSalesID = DatabaseHandler.getInstance().insertMasterSales(masterSales);
//        ArrayList<TransactionSales> transactionSalesArrayList = new ArrayList<>();
//        ArrayList<StockLedger> stockLedgerArrayList = new ArrayList<>();
////        for(SalesReturnProduct product : productsSelected) {
////            transactionSalesArrayList.add(createTransactionSales(masterSalesID, salesReturnInvoiceNumber, product));
////        }
//        for(TransactionSales transactionSales : transactionSalesArrayList) {
//            stockLedgerArrayList.add(createStockLedger(masterSales, transactionSales));
//        }
//        DatabaseHandler.getInstance().addTransactionSalesList(transactionSalesArrayList);
//        DatabaseHandler.getInstance().addStockLedgerList(stockLedgerArrayList);
//        Toast.makeText(getApplicationContext(), "Invoice completed", Toast.LENGTH_SHORT).show();
//        doPayment(masterSalesID, masterSales.getTotalAmount());

        MasterSalesReturn masterSalesReturn = createMasterSalesReturnObject();
        long masterSR_ID = DatabaseHandler.getInstance().insertMasterSalesReturn(masterSalesReturn);
        ArrayList<TransactionSalesReturn> transactionSalesReturnArrayList = new ArrayList<>();
        for (SalesReturnProduct salesReturnProduct : productsSelected) {
            transactionSalesReturnArrayList.add(createTransactionSalesReturnObject(masterSR_ID, salesReturnProduct));
        }
        DatabaseHandler.getInstance().addTransactionSalesReturnList(transactionSalesReturnArrayList);
        Toast.makeText(getApplicationContext(), "Invoice completed", Toast.LENGTH_SHORT).show();

        // generate credit note
        CreditDebitNote creditNote = createCreditNoteObject(masterSalesReturn);
        DatabaseHandler.getInstance().addNewCreditNote(creditNote);

        String salesReturnReceipt = getSalesReturnReceipt(masterSalesReturn, transactionSalesReturnArrayList);
        String creditNoteReceipt = getCreditNoteReceipt(creditNote);
        printThroughPrinter(salesReturnReceipt, creditNoteReceipt);

        // sales return save
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
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
                NewSalesReturnActivity.super.onBackPressed();
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
        builder.append(MyFunctions.makeStringCentreAlign("**Credit Note**", lengthOfScreen));
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

    private String getSalesReturnReceipt(MasterSalesReturn masterSalesReturn, ArrayList<TransactionSalesReturn> transactionSalesReturns) {
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
        builder.append(MyFunctions.makeStringCentreAlign("Sales Return", 48));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("Sales Return No. : " + masterSalesReturn.getSalesReturnNumber());
        builder.append(nextLine);
        builder.append("Invoice No. : " + masterSalesReturn.getInvoiceNumber());
        builder.append(nextLine);
        builder.append("Date : " + masterSalesReturn.getSalesReturnDate());
        builder.append(nextLine);
        builder.append("Cashier : " + this.FirstName);
        builder.append(nextLine);
        builder.append("Customer : " + masterSalesReturn.getCustomerName());
        if(MyFunctions.StringLength(masterSalesReturn.getCustomerMobile()) > 0) {
            builder.append(" ( " + masterSalesReturn.getCustomerMobile() + " )");
        }
        builder.append(nextLine);
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        String itemNameHeading = "ItemName";
        int itemNameHeadingLength = 12;
        builder.append(MyFunctions.makeStringLeftAlign(itemNameHeading, itemNameHeadingLength));

        String qtyHeading = "Rtn Qty";
        int qtyHeadingLength = 7;
        builder.append(MyFunctions.makeStringRightAlign(qtyHeading, qtyHeadingLength));

        String priceHeading = "Price";
        int priceHeadingLength = 10;
        builder.append(MyFunctions.makeStringRightAlign(priceHeading, priceHeadingLength));

        String gstHeading = "GST";
        int gstHeadingLength = 8;
        builder.append(MyFunctions.makeStringRightAlign(gstHeading, gstHeadingLength));

        String amountHeading = "Amount";
        int amountHeadingLength = 11;
        builder.append(MyFunctions.makeStringRightAlign(amountHeading, amountHeadingLength));

        builder.append(nextLine);
        for(TransactionSalesReturn transactionSalesReturn : transactionSalesReturns) {
            builder.append(transactionSalesReturn.getProductName());
            builder.append(nextLine);

            builder.append(MyFunctions.makeStringLeftAlign("  ", itemNameHeadingLength));
            if (transactionSalesReturn.getReturnType() == 1) {
                builder.append(MyFunctions.makeStringRightAlign("0", qtyHeadingLength));
            } else {
                builder.append(MyFunctions.makeStringRightAlign(transactionSalesReturn.getReturnQty() + "", qtyHeadingLength));
            }
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSalesReturn.getReturnPrice()), priceHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSalesReturn.getReturnTaxAmount()
                    / transactionSalesReturn.getReturnQty()), gstHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSalesReturn.getReturnAmount()), amountHeadingLength));
            builder.append(nextLine);

            if (transactionSalesReturn.getReturnType() == 1) {
                builder.append(MyFunctions.makeStringRightAlign("(Price Differential)", itemNameHeadingLength + qtyHeadingLength + priceHeadingLength));
            } else {
                builder.append(MyFunctions.drawLine(" ", itemNameHeadingLength + qtyHeadingLength + priceHeadingLength));
            }
            builder.append(MyFunctions.makeStringRightAlign("(" + String.format("%.2f", transactionSalesReturn.getReturnTaxRate()) + "%)"
                    ,  gstHeadingLength + 2));
//            builder.append(MyFunctions.makeStringRightAlign(" ", amountHeadingLength));
            builder.append(nextLine);
        }

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        int leftAlignLength = 36;
        int rightAlignLength = 12;
        builder.append(MyFunctions.makeStringLeftAlign("Credit Amount", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSalesReturn.getCreditAmount()), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Note :- " + masterSalesReturn.getNotes(), 48));
        builder.append(nextLine);
        builder.append(nextLine);
//        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your visit***",48));
//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append(nextLine);

        return builder.toString();
    }

    private void doPayment(double masterSalesID, double totalAmount) {
        Intent intentDoPayment = new Intent(getApplicationContext(), InvoicePaymentActivity.class);
        intentDoPayment.putExtra("salesReturnInvoiceNumber", salesReturnInvoiceNumber);
        intentDoPayment.putExtra("totalAmount", totalAmount);
        intentDoPayment.putExtra("masterSalesID", masterSalesID);
        startActivityForResult(intentDoPayment, INVOICE_PAYMENT_CODE);
    }

    public void extractFromDatabaseAndPrint(String invoiceNumber, double masterSalesID) {
        MasterSales msSales = DatabaseHandler.getInstance().getMasterSalesRecord(invoiceNumber);
        ArrayList<TransactionSales> tArray = DatabaseHandler.getInstance().getInvoiceDetailsFromDB(msSales.getReferenceNumber());
        Log.i("master", "Invoice Number - " + invoiceNumber);
        for (TransactionSales tSales : tArray) {
            Log.i("sales", tSales.getRefNumber() + " " + tSales.getItemName());
        }

        // print
//        final String receiptString = getReceipt(creditNote, tArray);
//        Log.i("bill", receiptString);
//        printThroughPrinter(receiptString);

        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
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
                NewSalesReturnActivity.super.onBackPressed();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
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
        if(customerSelected == null) {
            showOkDialog("Choose Customer");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesReturnActivity.this);
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
