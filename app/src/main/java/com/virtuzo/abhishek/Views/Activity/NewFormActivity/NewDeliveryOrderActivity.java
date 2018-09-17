package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hoin.usbsdk.PrintPic;
import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ListActivity.CustomerListActivity;
import com.virtuzo.abhishek.Views.Adapters.ProductListInNewDeliveryOrderAdapter;
import com.virtuzo.abhishek.Views.Adapters.ProductsSelectedInDeliveryOrderAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.Customer;
import com.virtuzo.abhishek.modal.DeliveryOrderProduct;
import com.virtuzo.abhishek.modal.EwayBill;
import com.virtuzo.abhishek.modal.EwayBillReason;
import com.virtuzo.abhishek.modal.MasterDeliveryOrder;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Product;
import com.virtuzo.abhishek.modal.State;
import com.virtuzo.abhishek.modal.TransactionDeliveryOrder;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class NewDeliveryOrderActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView productListInNewInvoiceRecyclerView, productsSelectedInNewInvoiceRecyclerView;
    final int NUMBER_OF_COLUMNS_IN_PRODUCT_LIST = 3, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED = 1;
    private ProductListInNewDeliveryOrderAdapter productListAdapter;
    private ProductsSelectedInDeliveryOrderAdapter productsSelectedAdapter;
    private ArrayList<Product> listOfAllProducts;
    private ArrayList<DeliveryOrderProduct> productsSelected;
    Button customerButtonForNewInvoice, invoiceDatePickerButton;
    String LoginID, BusinessId, OutletID, TermsAndConditions, OutletAddress, OutletLogo, GSTCategoryID,
            OutletGSTNumber, InvoiceSeries, TabCode, StateID, OutletName, FirstName, ContactNumber;
    private final int CHOOSE_CUSTOMER_RESPONSE_CODE = 103, DATE_PICKER_DIALOG_ID = 201;
    Customer customerSelected;
    int invoiceDay, invoiceMonth, invoiceYear;
    boolean validInvoiceDateSelected;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
//    SearchView searchViewForProductList;
    EditText searchViewForProductList;
    String invoiceDateString;
    TextView productSelectedTitleTextView;
    TextView deliveryOrderNumberTextView;
    String termsAndConditionsString = "";
    String StateName;
    String billAddressString = "";
    String shipAddressString = "";
    int eWayBillRequired = 0;
    int billStateId = 0;
    int shipStateId = 0;
    String doNumber;
    ProgressBar progressBar;
    ArrayList<Product> newList;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(NewDeliveryOrderActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        LoginID = setting.getString("UserID", "");
        OutletID = setting.getString("OutletID", "");
        TermsAndConditions = setting.getString("TermsAndConditions", "");
        OutletAddress = setting.getString("Address", "");
        OutletLogo = setting.getString("Logo", "");
        OutletGSTNumber = setting.getString("GSTNumber","");
        InvoiceSeries = setting.getString("InvoiceSeries", "");
        TabCode = setting.getString("TabCode", "");
        StateID = setting.getString("StateID", "");
        StateName = setting.getString("StateName", "");
        OutletName = setting.getString("OutletName", "");
        FirstName = setting.getString("FirstName", "");
        GSTCategoryID = setting.getString("GSTCategoryID", "");
        ContactNumber = setting.getString("ContactNumber", "");
        termsAndConditionsString = TermsAndConditions;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_delivery_order);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setTitle("New Invoice");
        getSupportActionBar().hide();
        Toolbar toolbar_in_new_delivery_order = (Toolbar) findViewById(R.id.toolbar_in_new_delivery_order);
        toolbar_in_new_delivery_order.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_new_delivery_order.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_new_delivery_order.findViewById(R.id.actionBarTitle);
        heading.setText("Delivery Order");

        deliveryOrderNumberTextView = (TextView) findViewById(R.id.deliveryOrderNumberTextView);

        SharedPreferences data = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        BusinessId = data.getString("BusinessId", "");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // products list
        listOfAllProducts = new ArrayList<>();
        productListInNewInvoiceRecyclerView = (RecyclerView) findViewById(R.id.productListInNewInvoiceRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_PRODUCT_LIST);
        productListInNewInvoiceRecyclerView.setLayoutManager(mLayoutManager);
        productListInNewInvoiceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productListInNewInvoiceRecyclerView.setHasFixedSize(true);
        newList = new ArrayList<Product>();
        productListAdapter = new ProductListInNewDeliveryOrderAdapter(newList, new ProductListInNewDeliveryOrderAdapter.OnClickListener() {
            @Override
            public void onItemClick(Product product) {
                selectProduct(product);
            }
        });
        productListInNewInvoiceRecyclerView.setAdapter(productListAdapter);
        Log.i("New Invoice", "Before database access");
//        listOfAllProducts.addAll(DatabaseHandler.getInstance().getTradeProductsListFromDB());
//        Log.i("New Invoice", "After database access");
        productListAdapter.notifyDataSetChanged();

        // search bar for product list
//        searchViewForProductList = (SearchView) findViewById(R.id.searchProductListSearchViewInNewInvoice);
//        searchViewForProductList.setOnQueryTextListener(this);
//        searchViewForProductList.setQueryHint("Search Product");
//        setSearchViewHeight(searchViewForProductList, 25);
//        searchViewForProductList.setFocusableInTouchMode(true);
//        searchViewForProductList.setClickable(true);
//        searchViewForProductList.setIconified(false);
//        searchViewForProductList.requestFocus();
//        searchViewForProductList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
//        });
        searchViewForProductList = (EditText) findViewById(R.id.searchProductListSearchViewInNewInvoice);
        searchViewForProductList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.contains("\n")) {
                    str = str.trim();
                    if (MyFunctions.StringLength(str) > 0) {
                        searchBarCode();
                    } else {
                        searchViewForProductList.setText("");
                    }
                }
            }
        });

        // products selected
        productSelectedTitleTextView = (TextView) findViewById(R.id.productSelectedTitleTextView);
        productsSelected = new ArrayList<>();
        productsSelectedInNewInvoiceRecyclerView = (RecyclerView) findViewById(R.id.productsSelectedInNewInvoiceRecyclerView);
        RecyclerView.LayoutManager nLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED);
        productsSelectedInNewInvoiceRecyclerView.setLayoutManager(nLayoutManager);
        productsSelectedInNewInvoiceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productsSelectedAdapter = new ProductsSelectedInDeliveryOrderAdapter(productsSelected, new ProductsSelectedInDeliveryOrderAdapter.OnClickListener() {
            @Override
            public void onItemClick(final DeliveryOrderProduct deliveryOrderProduct) {
                updateProductDetails(deliveryOrderProduct);
            }

            @Override
            public void onCancelItem(int position) {
                productsSelected.remove(position);
                productsSelectedAdapter.notifyDataSetChanged();
                setProductSelectedTitle(productsSelected.size());
            }
        });
        productsSelectedInNewInvoiceRecyclerView.setAdapter(productsSelectedAdapter);

        // choose customer
        customerSelected = null;
        customerButtonForNewInvoice = (Button) findViewById(R.id.customerButtonForNewInvoice);
        customerButtonForNewInvoice.setText("Choose Customer");
        customerButtonForNewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShowCustomerList = new Intent(getApplicationContext(), CustomerListActivity.class);
                intentShowCustomerList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DELIVERY_ORDER);
                startActivityForResult(intentShowCustomerList, CHOOSE_CUSTOMER_RESPONSE_CODE);
            }
        });

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
        updateDONumber();

        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();

        Log.i("New Invoice", "onCreate complete");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                listOfAllProducts.addAll(DatabaseHandler.getInstance().getTradeProductsListFromDB());
                Log.i("New Invoice", "After database access");
                renderProductList(listOfAllProducts);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "Barcode Scanner detected", Toast.LENGTH_LONG).show();
        if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showInputMethodPicker();
            Toast.makeText(this, "Barcode Scanner detected. Please turn OFF Hardware/Physical keyboard to enable softkeyboard to function.", Toast.LENGTH_LONG).show();
        }
    }

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(NewDeliveryOrderActivity.this,mHandler);
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

        Toast.makeText(NewDeliveryOrderActivity.this, getString(R.string.usb_msg_conn_state),
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
                Toast.makeText(NewDeliveryOrderActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
//                printThroughPrinter(receiptString);
            }
        }
    }

    private void printThroughPrinter(String receiptString) {
        byte isHasPaper;
        byte[] cmd = null;
        if( dev != null ){
            if( usbCtrl.isHasPermission(dev)){
                Toast.makeText(NewDeliveryOrderActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(NewDeliveryOrderActivity.this, "The printer has no paper",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String txt_msg = receiptString;
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

    private String getReceipt(MasterDeliveryOrder masterDeliveryOrder, ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList, String eWayBillNo) {
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

        builder.append("Outlet State: " + this.StateName);
        builder.append(nextLine);
        if(MyFunctions.StringLength(this.OutletGSTNumber) > 0) {
            builder.append("GST No. " + this.OutletGSTNumber);
            builder.append(nextLine);
        }
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("Delivery Order", 48));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("DO No. : " + masterDeliveryOrder.getDONumber());
        builder.append(nextLine);
        builder.append("Date : " + masterDeliveryOrder.getDeliveryDate());
        builder.append(nextLine);
        if(MyFunctions.StringLength(eWayBillNo) > 0) {
            builder.append("E-way Bill No. : " + eWayBillNo);
            builder.append(nextLine);
        }
        builder.append("Cashier : " + this.FirstName);
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("Customer : " + masterDeliveryOrder.getCustomerName());
        if(MyFunctions.StringLength(masterDeliveryOrder.getCustomerMobile()) > 0) {
            builder.append(" ( " + masterDeliveryOrder.getCustomerMobile() + " )");
        }
        builder.append(nextLine);
        builder.append("Bill Address : " + masterDeliveryOrder.getCustomerBillAddress());
        builder.append(nextLine);
        State billState = DatabaseHandler.getInstance().getStateFromId(masterDeliveryOrder.getCustomerBillStateID());
        builder.append("Bill State : " + billState.getStateName());
        builder.append(nextLine);
        builder.append("Ship Address : " + masterDeliveryOrder.getCustomerShipAddress());
        builder.append(nextLine);
        State shipState = DatabaseHandler.getInstance().getStateFromId(masterDeliveryOrder.getCustomerShipStateID());
        builder.append("Ship State : " + shipState.getStateName());
        builder.append(nextLine);
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        String itemNameHeading = "ItemName(ItemCode)";
        int itemNameHeadingLength = 28;
        builder.append(MyFunctions.makeStringLeftAlign(itemNameHeading, itemNameHeadingLength));

        String qtyHeading = "QTY";
        int qtyHeadingLength = 6;
        builder.append(MyFunctions.makeStringRightAlign(qtyHeading, qtyHeadingLength));

        String amountHeading = "AMOUNT";
        int amountHeadingLength = 14;
        builder.append(MyFunctions.makeStringRightAlign(amountHeading, amountHeadingLength));

        builder.append(nextLine);

        String rupeeSymbol = "\u20B9";
        int totalQuantity = 0;

        for(TransactionDeliveryOrder transactionDeliveryOrder : transactionDeliveryOrderArrayList) {
            totalQuantity += transactionDeliveryOrder.getQuantity();
            Product product = DatabaseHandler.getInstance().getProductRecordFromDeliveryOrder(transactionDeliveryOrder);

            builder.append(transactionDeliveryOrder.getProductName());
            builder.append(nextLine);
            builder.append(MyFunctions.makeStringLeftAlign("  ", itemNameHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(transactionDeliveryOrder.getQuantity()+"", qtyHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionDeliveryOrder.getAmount()), amountHeadingLength));
            builder.append(nextLine);

        }

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        int leftAlignLength = 32;
        int rightAlignLength = 16;

        builder.append(MyFunctions.makeStringLeftAlign("Total Quantity ", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(masterDeliveryOrder.getTotalQuantity()+"", rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Total Amount ", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterDeliveryOrder.getTotalAmount()), rightAlignLength));
        builder.append(nextLine);

//        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your visit***",48));
//        builder.append(nextLine);

        builder.append(nextLine);
        builder.append(nextLine);

        return builder.toString();
    }

    private void setSearchViewHeight(SearchView searchView, int height) {
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextSize(height);
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void updateDONumber() {
        doNumber = createDONumber();
        deliveryOrderNumberTextView.setText(doNumber);
    }

    private String getDONumber() {
        if(MyFunctions.StringLength(this.doNumber) == 0) {
            this.doNumber = createDONumber();
        }
        return this.doNumber;
    }

    private String createDONumber() {
        StringBuilder builder = new StringBuilder();
        builder.append("DO");
//        builder.append(this.InvoiceSeries);
        builder.append(this.TabCode);
        builder.append("/");
        builder.append(this.dateFormatInDONumber());
        builder.append("/");
        String previousDONumber = DatabaseHandler.getInstance().getPreviousDONumber(builder.toString());
        if(previousDONumber == null) {
            builder.append("001");
        } else {
            String seriesNumberString = previousDONumber.substring(previousDONumber.length() - 3);
            int seriesNumber = Integer.parseInt(seriesNumberString);
            seriesNumber++;
            seriesNumberString = "000" + seriesNumber;
            seriesNumberString = seriesNumberString.substring(seriesNumberString.length() - 3);
            builder.append("" + seriesNumberString);
        }
        return builder.toString();
    }

    @NonNull
    private String dateFormatInDONumber() {
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
                    Toast.makeText(NewDeliveryOrderActivity.this, dateString, Toast.LENGTH_SHORT).show();
                    invoiceDatePickerButton.setText(dateString);
                    invoiceDateString = dateString;
                    validInvoiceDateSelected = true;
                    updateDONumber();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_CUSTOMER_RESPONSE_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                String json = data.getStringExtra("customer");
                Gson gson = new Gson();
                customerSelected = gson.fromJson(json, Customer.class);

                int MaximumEntityNameLength = 15;
                String showCustomerEntityName = customerSelected.getEntityName();
                if(customerSelected.getEntityType().equalsIgnoreCase("Individual")) {
                    showCustomerEntityName = customerSelected.getContactPerson();
                }
                if(showCustomerEntityName.length() > MaximumEntityNameLength) {
                    showCustomerEntityName = showCustomerEntityName.substring(0,
                            Math.min(showCustomerEntityName.length(), MaximumEntityNameLength - 2));
                    showCustomerEntityName = showCustomerEntityName + "...";
                }
                customerButtonForNewInvoice.setText(showCustomerEntityName
                        + " (" + customerSelected.getContactNumber() + ")");
                this.billAddressString = customerSelected.getAddress();
                this.shipAddressString = customerSelected.getAddress();
                this.billStateId = customerSelected.getStateId();
                this.shipStateId = customerSelected.getStateId();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        newText = MyFunctions.lowerCase(newText.trim());
//        if(MyFunctions.StringLength(newText) == 0) {
//            productListAdapter.setFilter(listOfAllProducts);
//            return true;
//        }
//        ArrayList<Product> newList = new ArrayList<>();
//        for(Product product : listOfAllProducts) {
//            String barCode = MyFunctions.lowerCase(product.getBarcode());
//            if(barCode.equals(newText)) {
//                selectProduct(product);
//                productListAdapter.setFilter(listOfAllProducts);
//                searchViewForProductList.setQuery("", true);
//                return false;
//            }
//
//            String name = MyFunctions.lowerCase(product.getProductName());
//            if(name.contains(newText)){
//                newList.add(product);
//            }
//        }
//        productListAdapter.setFilter(newList);
        return true;
    }

    public void searchBarCode() {
        String newText = searchViewForProductList.getText().toString();
        newText = MyFunctions.lowerCase(newText.trim());
        for(Product product : listOfAllProducts) {
            String barCode = MyFunctions.lowerCase(product.getBarcode());
            if(barCode.equals(newText)) {
                selectProduct(product);
                renderProductList(listOfAllProducts);
                searchViewForProductList.setText("");
                return;
            }
        }
        searchViewForProductList.setText(newText);
        onSearchButtonClick(null);
    }

    public void renderProductList(ArrayList<Product> ProductList) {
        newList = new ArrayList<>();
        int listSize = 18;
        for (int i=0; i<listSize && i<ProductList.size(); i++) {
            newList.add(ProductList.get(i));
        }
//        productListAdapter.setFilter(newList);
        productListAdapter.animateTo(newList);
        productListInNewInvoiceRecyclerView.scrollToPosition(0);
    }

    public void onSearchButtonClick(View view) {
        String newText = searchViewForProductList.getText().toString();
        newText = MyFunctions.lowerCase(newText.trim());
        Log.i("Search", newText);
        if(MyFunctions.StringLength(newText) == 0) {
            renderProductList(listOfAllProducts);
            return;
        }
        ArrayList<Product> newList = new ArrayList<>();
        for(Product product : listOfAllProducts) {
            String name = MyFunctions.lowerCase(product.getProductName());
//            String barCode = MyFunctions.lowerCase(product.getBarcode());
//            if(barCode.equals(newText)) {
//                selectProduct(product);
//                renderProductList(listOfAllProducts);
//                searchViewForProductList.setText("");
//                return;
//            }
            if(name.contains(newText)){
                newList.add(product);
            }
        }
        renderProductList(newList);
        searchViewForProductList.selectAll();
    }

    public void selectProduct(Product product) {
        if(customerSelected == null) {
            showOkDialog("Choose customer");
            return;
        }
        // when a product is clicked
        boolean newProductSelected = true;
        for (int i = 0; i < productsSelected.size(); i++) {
            if(productsSelected.get(i).getProduct().getProductID() == product.getProductID()) {
                newProductSelected = false;
                updateProductDetails(productsSelected.get(i));
                break;
            }
        }
        if(newProductSelected) {
            // New product added
            DeliveryOrderProduct deliveryOrderProduct = new DeliveryOrderProduct(product);
            addNewProductDetails(deliveryOrderProduct);
        }
    }

    public void addNewProductDetails(final DeliveryOrderProduct deliveryOrderProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDeliveryOrderActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.product_selected_details_delivery_order_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final TextView titleTextView = (TextView) view.findViewById(R.id.productNameTitle);
        final EditText quantityEditText = (EditText) view.findViewById(R.id.quantityProductSelectedEditText);

        titleTextView.setText(deliveryOrderProduct.getProduct().getProductName());

        // default values
        final Button okButton = (Button) view.findViewById(R.id.okButtonInProductSelectedDetails);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityString = quantityEditText.getText().toString();
                if(quantityString.length() == 0 || quantityString.length() > 4) {
                    showOkDialog("Enter valid quantity");
                } else {
                    int quantity = Integer.parseInt(quantityString);
                    if(quantity == 0) {
                        showOkDialog("Enter valid quantity");
                    } else {
                        deliveryOrderProduct.setQuantity(quantity);
                        productsSelected.add(deliveryOrderProduct);
                        productsSelectedAdapter.notifyDataSetChanged();
                        setProductSelectedTitle(productsSelected.size());
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    public void updateProductDetails(final DeliveryOrderProduct deliveryOrderProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDeliveryOrderActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.product_selected_details_delivery_order_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView titleTextView = (TextView) view.findViewById(R.id.productNameTitle);
        final EditText quantityEditText = (EditText) view.findViewById(R.id.quantityProductSelectedEditText);

        // default values
        titleTextView.setText(deliveryOrderProduct.getProduct().getProductName());
        quantityEditText.setText(deliveryOrderProduct.getQuantity()+"");
        quantityEditText.selectAll();
        final Button okButton = (Button) view.findViewById(R.id.okButtonInProductSelectedDetails);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityString = quantityEditText.getText().toString();
                if(quantityString.length() == 0 || quantityString.length() > 4) {
                    showOkDialog("Enter valid quantity");
                } else {
                    int quantity = Integer.parseInt(quantityString);
                    if(quantity == 0) {
                        showOkDialog("Enter valid quantity");
                    } else {
                        deliveryOrderProduct.setQuantity(quantity);
                        productsSelectedAdapter.notifyDataSetChanged();
                        setProductSelectedTitle(productsSelected.size());
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    private void setProductSelectedTitle(int size) {
        productSelectedTitleTextView.setText("Products Selected (" + size + ")");
    }

    private MasterDeliveryOrder createMasterDeliveryOrderObject(String invoiceNumber, int totalQuantity, double totalAmount) {
        MasterDeliveryOrder masterDeliveryOrder = new MasterDeliveryOrder();
        masterDeliveryOrder.setDeliveryDate(this.invoiceDateString);
        masterDeliveryOrder.setDONumber(invoiceNumber);
        masterDeliveryOrder.setCustomer_ID(customerSelected.getCustomerId());
        masterDeliveryOrder.setOutletID(Long.parseLong(this.OutletID));
        masterDeliveryOrder.setCreatedBy(Long.parseLong(this.LoginID));
        masterDeliveryOrder.setCreatedDtTm(MyFunctions.getCurrentDateTime());
        masterDeliveryOrder.setStatus(7);
        if(customerSelected.getEntityType().equalsIgnoreCase("Individual")) {
            masterDeliveryOrder.setCustomerName(customerSelected.getContactPerson());
        } else {
            masterDeliveryOrder.setCustomerName(customerSelected.getEntityName());
        }
        masterDeliveryOrder.setCustomerEmail(customerSelected.getContactEmailId());
        masterDeliveryOrder.setCustomerContactPerson(customerSelected.getContactPerson());
        masterDeliveryOrder.setCustomerMobile(customerSelected.getContactNumber());
        masterDeliveryOrder.setCustomerLandline(customerSelected.getContactLandline());
        masterDeliveryOrder.setCustomerBillAddress(this.billAddressString);
        masterDeliveryOrder.setCustomerBillStateID(this.billStateId);
        masterDeliveryOrder.setCustomerShipAddress(this.shipAddressString);
        masterDeliveryOrder.setCustomerShipStateID(this.shipStateId);
        masterDeliveryOrder.setTotalQuantity(totalQuantity);
        masterDeliveryOrder.setTotalAmount(totalAmount);
        return masterDeliveryOrder;
    }

    private TransactionDeliveryOrder createTransactionDeliveryOrder(int masterDOID, DeliveryOrderProduct deliveryOrderProduct) {
        TransactionDeliveryOrder transactionDeliveryOrder = new TransactionDeliveryOrder();
        transactionDeliveryOrder.setDOID(masterDOID);
        transactionDeliveryOrder.setProductID(deliveryOrderProduct.getProduct().getProductID());
        transactionDeliveryOrder.setQuantity(deliveryOrderProduct.getQuantity());
        transactionDeliveryOrder.setUnitID(deliveryOrderProduct.getProduct().getUnitID());
        transactionDeliveryOrder.setCreatedBy(Integer.parseInt(this.LoginID));
        transactionDeliveryOrder.setCreatedDtTm(MyFunctions.getCurrentDateTime());
        transactionDeliveryOrder.setStatus(7);
        transactionDeliveryOrder.setProductCode(deliveryOrderProduct.getProduct().getProductCode());
        transactionDeliveryOrder.setProductName(deliveryOrderProduct.getProduct().getProductName());
        transactionDeliveryOrder.setAmount(deliveryOrderProduct.getProduct().getSalesPrice() * deliveryOrderProduct.getQuantity());
        transactionDeliveryOrder.setSynced(deliveryOrderProduct.getProduct().getSynced());
        return transactionDeliveryOrder;
    }

    private boolean checkConfirmValidation() {
        if(customerSelected == null) {
            showOkDialog("Select Customer");
        } else if(productsSelected.size() == 0) {
            showOkDialog("No product selected");
        } else {
            return true;
        }
        return false;
    }

    public void confirmInvoice() {
        // confirm invoice dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDeliveryOrderActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.invoice_confirm_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button yesButton = (Button) view.findViewById(R.id.yesClick);
        Button skipButton = (Button) view.findViewById(R.id.skipClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                eWayBillRequired = 1;
                // invoice confirmed
                InvoiceInDatabase();
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                eWayBillRequired = 0;
                // invoice confirmed
                InvoiceInDatabase();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void InvoiceInDatabase() {
        String doNumber = getDONumber();
        int totalQuantity = 0;
        double totalAmount = 0.0;
        for(DeliveryOrderProduct deliveryOrderProduct : productsSelected) {
            totalQuantity += deliveryOrderProduct.getQuantity();
            totalAmount += (deliveryOrderProduct.getProduct().getSalesPrice() * deliveryOrderProduct.getQuantity());
        }
        MasterDeliveryOrder masterDeliveryOrder = createMasterDeliveryOrderObject(doNumber, totalQuantity, totalAmount);
        int masterDOID = DatabaseHandler.getInstance().insertMasterDeliveryOrder(masterDeliveryOrder);
        ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList = new ArrayList<>();
        for(DeliveryOrderProduct deliveryOrderProduct : productsSelected) {
            transactionDeliveryOrderArrayList.add(createTransactionDeliveryOrder(masterDOID, deliveryOrderProduct));
        }
        DatabaseHandler.getInstance().addTransactionDeliveryOrderList(transactionDeliveryOrderArrayList);

        if(eWayBillRequired == 1) {
            successfulEWayBillDialog(doNumber, masterDeliveryOrder, transactionDeliveryOrderArrayList);
        } else {
            String blankEwayBillNo = "";
            doSaved(masterDeliveryOrder, transactionDeliveryOrderArrayList, blankEwayBillNo);
        }
    }

    public void successfulEWayBillDialog(final String doNumber, final MasterDeliveryOrder masterDeliveryOrder, final ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDeliveryOrderActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ewaybill_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button nextButton = (Button) view.findViewById(R.id.nextClick);
        final EditText ewayBillNumberEditText = (EditText) view.findViewById(R.id.ewayBillNumberEditText);
        final EditText documentNumberEditText = (EditText) view.findViewById(R.id.documentNumberEditText);
        final EditText vehicleNumberEditText = (EditText) view.findViewById(R.id.vehicleNumberEditText);
        ewayBillNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        documentNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        vehicleNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        final Spinner reasonSpinner = (Spinner) view.findViewById(R.id.reasonSpinner);
        ArrayList<EwayBillReason> reasonList = new ArrayList<>();
        EwayBillReason demoEwayBillReason = new EwayBillReason();
        demoEwayBillReason.setReason("-- Choose Reason --");
        reasonList.add(demoEwayBillReason);
        reasonList.addAll(DatabaseHandler.getInstance().getEWayBillReasonsList());
        ArrayAdapter adapter = new ArrayAdapter(NewDeliveryOrderActivity.this, android.R.layout.simple_spinner_item, reasonList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(adapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save eway bill form
                if(reasonSpinner.getSelectedItemPosition() == 0) {
                    showOkDialog("Select Reason");
                    return;
                }
                EwayBill ewayBill = new EwayBill();
                ewayBill.setDONumber(doNumber);
                EwayBillReason reason = (EwayBillReason) reasonSpinner.getSelectedItem();
                ewayBill.setReasonID(reason.getID());
                ewayBill.setDocumentNo(documentNumberEditText.getText().toString());
                ewayBill.setVehicleNo(vehicleNumberEditText.getText().toString());
                ewayBill.setEWayBillNo(ewayBillNumberEditText.getText().toString());
                ewayBill.setCreatedBy(MyFunctions.parseDouble(LoginID));
                ewayBill.setCreatedDtTm(MyFunctions.getCurrentDateTime());
                ewayBill.setSynced(0);
                DatabaseHandler.getInstance().addNewEWayBill(ewayBill);
                doSaved(masterDeliveryOrder, transactionDeliveryOrderArrayList, ewayBill.getEWayBillNo());
                dialog.dismiss();
            }
        });
    }

    public void doSaved(MasterDeliveryOrder masterDeliveryOrder, ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList, String ewayBillNo) {

        final String receiptString = getReceipt(masterDeliveryOrder, transactionDeliveryOrderArrayList, ewayBillNo);
        printThroughPrinter(receiptString);

        // purchase saved
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDeliveryOrderActivity.this);
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
                NewDeliveryOrderActivity.super.onBackPressed();
            }
        });
    }

    public void onConfirmButtom(View v) {
        if(checkConfirmValidation()) {
            confirmInvoice();
        }
    }

    public void showOkDialog(String heading) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDeliveryOrderActivity.this);
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

    public void addressButtonClick(View v) {
        if(customerSelected == null) {
            showOkDialog("Choose Customer");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDeliveryOrderActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.delivery_order_address_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(1000, 400);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final EditText billAddressEditText = (EditText) view.findViewById(R.id.billAddressEditText);
        final EditText shipAddressEditText = (EditText) view.findViewById(R.id.shipAddressEditText);
        final Spinner shipAddressStateSpinner = (Spinner) view.findViewById(R.id.shipAddressStateSpinner);
        final Spinner billAddressStateSpinner = (Spinner) view.findViewById(R.id.billAddressStateSpinner);

        ArrayList<State> state_list= new ArrayList<>();
        state_list.add(new State(0, "Choose State"));
        state_list.addAll(DatabaseHandler.getInstance().getStateListFromDB());

        ArrayAdapter state_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, state_list);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipAddressStateSpinner.setAdapter(state_adapter);
        billAddressStateSpinner.setAdapter(state_adapter);

        billAddressEditText.setText(this.billAddressString);
        shipAddressEditText.setText(this.shipAddressString);
        billAddressStateSpinner.setSelection(this.billStateId);
        shipAddressStateSpinner.setSelection(this.shipStateId);
        Button saveButton = (Button) view.findViewById(R.id.saveClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(billAddressStateSpinner.getSelectedItemPosition() == 0) {
                    showOkDialog("Select Billing state");
                    return;
                }
                if(shipAddressStateSpinner.getSelectedItemPosition() == 0) {
                    showOkDialog("Select Shipping state");
                    return;
                }
                billAddressString = billAddressEditText.getText().toString();
                shipAddressString = shipAddressEditText.getText().toString();
                State billState = (State)billAddressStateSpinner.getSelectedItem();
                State shipState = (State)shipAddressStateSpinner.getSelectedItem();
                billStateId = billState.getStateID();
                shipStateId = shipState.getStateID();
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
