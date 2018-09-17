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
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hoin.usbsdk.PrintPic;
import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Adapters.ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.Customer;
import com.virtuzo.abhishek.modal.EwayBill;
import com.virtuzo.abhishek.modal.EwayBillReason;
import com.virtuzo.abhishek.modal.GSTCategory;
import com.virtuzo.abhishek.modal.MasterDeliveryOrder;
import com.virtuzo.abhishek.modal.MasterSales;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Product;
import com.virtuzo.abhishek.modal.ProductWithQuantity;
import com.virtuzo.abhishek.modal.State;
import com.virtuzo.abhishek.modal.StockLedger;
import com.virtuzo.abhishek.modal.TransactionDeliveryOrder;
import com.virtuzo.abhishek.modal.TransactionSales;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class NewSalesInvoiceFromDOActivity extends AppCompatActivity {

    RecyclerView productsSelectedInNewInvoiceRecyclerView;
    final int NUMBER_OF_COLUMNS_IN_PRODUCT_LIST = 4, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED = 2;
    private ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter productsSelectedAdapter;
    private ArrayList<ProductWithQuantity> productsSelected;
    Button customerButtonForNewInvoice, invoiceDatePickerButton;
    ImageButton backButton;
    String LoginID, BusinessId, OutletID, TermsAndConditions, OutletAddress, OutletLogo, GSTCategoryID,
            OutletGSTNumber, InvoiceSeries, TabCode, StateID, OutletName, FirstName, ContactNumber;
    private final int /*CHOOSE_CUSTOMER_RESPONSE_CODE = 101,*/ INVOICE_PAYMENT_CODE = 102, DATE_PICKER_DIALOG_ID = 201;
    Customer customerSelected;
    int invoiceDay, invoiceMonth, invoiceYear;
    boolean validInvoiceDateSelected;
    double transportCharges = 0, insuranceCharges = 0, packingCharges = 0, overallDiscount = 0;
    int overallDiscountType = 0;
    TextView subTotalTextView, taxTextView, freightChargesTextView, totalAmountTextView, discountInNewInvoiceTextView;
    String invoiceDateString;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
    String invoiceNumber;
    TextView invoiceNumberTextView, productSelectedTitleTextView;
    String termsAndConditionsString = "";
    String billAddressString = "";
    String shipAddressString = "";
    int stateId = 0;
    int eWayBillRequired = 0;
    MasterDeliveryOrder masterDeliveryOrder;
    ArrayList<TransactionDeliveryOrder> listOfTransactionDeliveryOrder;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(NewSalesInvoiceFromDOActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    String StateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sales_invoice_from_do);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        LoginID = setting.getString("UserID", "");
        OutletID = setting.getString("OutletID", "");
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

        // get master DO
        String DO_ID = getIntent().getExtras().getString("MasterDO_ID");
        masterDeliveryOrder = DatabaseHandler.getInstance().getMasterDeliveryOrderRecord(DO_ID);
        listOfTransactionDeliveryOrder = DatabaseHandler.getInstance().getDeliveryOrderListDetailsFromDB(masterDeliveryOrder.getID()+"");

//        setTitle("New Invoice");
        getSupportActionBar().hide();
        Toolbar toolbar_in_new_invoice = (Toolbar) findViewById(R.id.toolbar_in_new_invoice);
        toolbar_in_new_invoice.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_new_invoice.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_new_invoice.findViewById(R.id.actionBarTitle);
        heading.setText("Sales Invoice");
        invoiceNumberTextView = (TextView) toolbar_in_new_invoice.findViewById(R.id.invoiceNumberTextViewInNewInvoice);

        SharedPreferences data = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        BusinessId = data.getString("BusinessId", "");

        subTotalTextView = (TextView) findViewById(R.id.subTotalInNewInvoiceTextView);
        freightChargesTextView = (TextView) findViewById(R.id.freightChargesInNewInvoiceTextView);
        taxTextView = (TextView) findViewById(R.id.totalTaxInNewInvoiceTextView);
        totalAmountTextView = (TextView) findViewById(R.id.totalAmountInNewInvoiceTextView);
        discountInNewInvoiceTextView = (TextView) findViewById(R.id.discountInNewInvoiceTextView);
        String rupeeSymbol = "\u20B9";
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        discountInNewInvoiceTextView.setText("-" + rupeeSymbol + String.format("%.2f", 0d));
        freightChargesTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        taxTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f", 0d));

        // products selected
        productSelectedTitleTextView = (TextView) findViewById(R.id.productSelectedTitleTextView);
        productsSelected = new ArrayList<ProductWithQuantity>();
        productsSelectedInNewInvoiceRecyclerView = (RecyclerView) findViewById(R.id.productsSelectedInNewInvoiceRecyclerView);
        RecyclerView.LayoutManager nLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED);
        productsSelectedInNewInvoiceRecyclerView.setLayoutManager(nLayoutManager);
        productsSelectedInNewInvoiceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productsSelectedAdapter = new ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter(productsSelected, new ProductsSelectedInNewInvoiceFromDeliveryOrderAdapter.OnClickListener() {
            @Override
            public void onItemClick(final ProductWithQuantity productWithQuantity) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.product_selected_details_dialog, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                final TextView productNameTitle = (TextView) view.findViewById(R.id.productNameTitle);
                final EditText unitPriceEditText = (EditText) view.findViewById(R.id.unitPriceProductSelectedInNewInvoice);
                final EditText quantityEditText = (EditText) view.findViewById(R.id.quantityProductSelectedInNewInvoice);
                final EditText discountEditText = (EditText) view.findViewById(R.id.discountProductSelectedInNewInvoiceEditText);
                final Spinner discountTypeSpinner = (Spinner) view.findViewById(R.id.discountTypeSpinner);

                // default values
                productNameTitle.setText(productWithQuantity.getProduct().getProductName());
                unitPriceEditText.setText(String.format("%.2f", productWithQuantity.getProduct().getSalesPrice()));
                quantityEditText.setText(productWithQuantity.getQuantity() + "");
                quantityEditText.setEnabled(false);
                if (productWithQuantity.getDiscount() > 0) {
                    discountEditText.setText(String.format("%.2f", productWithQuantity.getDiscount()));
                }

                String rupeeSymbol = "\u20B9";
                String percentSymbol = "%";
                ArrayList<String> discountTypeList = new ArrayList<String>();
                discountTypeList.add("          "+rupeeSymbol);
                discountTypeList.add("          "+percentSymbol);
                ArrayAdapter adapter = new ArrayAdapter(NewSalesInvoiceFromDOActivity.this, android.R.layout.simple_spinner_item, discountTypeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                discountTypeSpinner.setAdapter(adapter);
                discountTypeSpinner.setSelection(productWithQuantity.getDiscountType());

                final Button okButton = (Button) view.findViewById(R.id.okButtonInProductSelectedDetailsInNewInvoice);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String unitPriceString = unitPriceEditText.getText().toString();
                        String quantity = quantityEditText.getText().toString();
                        String discountString = discountEditText.getText().toString();
                        int discountType = discountTypeSpinner.getSelectedItemPosition();
                        if(unitPriceString.length() == 0) {
                            showOkDialog("Enter valid unit price");
                        } else if(quantity.length() == 0 || quantity.length() > 4) {
                            showOkDialog("Enter valid quantity");
                        } else {
                            double unitPrice = MyFunctions.parseDouble(unitPriceString);
                            double discount = 0d;
                            if(discountString.length() > 0) {
                                discount = MyFunctions.parseDouble(discountString);
                            }
                            if(discountType == 1) {
                                if(discount > 100) {
                                    showOkDialog("Invalid Percent discount");
                                    return;
                                }
                            } else if(discount > unitPrice) {
                                showOkDialog("Discount should be less than unit price");
                                return;
                            }
                            productWithQuantity.getProduct().setSalesPrice(unitPrice);
                            productWithQuantity.setQuantity(Integer.parseInt(quantity));
                            productWithQuantity.setDiscount(discount);
                            productWithQuantity.setDiscountType(discountType);
                            dialog.dismiss();
                        }
                        productsSelectedAdapter.notifyDataSetChanged();
                        refreshSubTotalAndTax();
                    }
                });
            }

//            @Override
//            public void onCancelItem(int position) {
//                productsSelected.remove(position);
//                productsSelectedAdapter.notifyDataSetChanged();
//                setProductSelectedTitle(productsSelected.size());
//                refreshSubTotalAndTax();
//            }

        });
        productsSelectedInNewInvoiceRecyclerView.setAdapter(productsSelectedAdapter);

        // choose customer
        customerSelected = DatabaseHandler.getInstance().getCustomerRecordFromDeliveryOrder(masterDeliveryOrder);
        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        StateName = setting.getString("StateName", "");
        stateId = customerSelected.getStateId();

        customerButtonForNewInvoice = (Button) findViewById(R.id.customerButtonForNewInvoice);

//        customerButtonForNewInvoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentShowCustomerList = new Intent(getApplicationContext(), CustomerListActivity.class);
//                intentShowCustomerList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_NEW_INVOICE);
//                startActivityForResult(intentShowCustomerList, CHOOSE_CUSTOMER_RESPONSE_CODE);
//            }
//        });
        setCustomerOnScreen();

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

        // add products
        billAddressString = masterDeliveryOrder.getCustomerBillAddress();
        shipAddressString = masterDeliveryOrder.getCustomerShipAddress();
        stateId = masterDeliveryOrder.getCustomerShipStateID();
        for(TransactionDeliveryOrder transactionDeliveryOrder : listOfTransactionDeliveryOrder) {
            Product product = DatabaseHandler.getInstance().getProductRecordFromDeliveryOrder(transactionDeliveryOrder);
            selectProduct(product, transactionDeliveryOrder.getQuantity());
        }

        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "Barcode Scanner detected", Toast.LENGTH_LONG).show();
        if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showInputMethodPicker();
            Toast.makeText(this, "Barcode Scanner detected. Please turn OFF Hardware/Physical keyboard to enable softkeyboard to function.", Toast.LENGTH_SHORT).show();
        }
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

    public void updateInvoiceNumber() {
        invoiceNumber = createInvoiceNumber();
        invoiceNumberTextView.setText(invoiceNumber);
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
                    Toast.makeText(NewSalesInvoiceFromDOActivity.this, dateString, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == CHOOSE_CUSTOMER_RESPONSE_CODE) {
//            if(resultCode == Activity.RESULT_OK) {
//                String json = data.getStringExtra("customer");
//                Gson gson = new Gson();
//                customerSelected = gson.fromJson(json, Customer.class);
//                setCustomerOnScreen();
//                refreshSubTotalAndTax();
//            }
//        } else
        if(requestCode == INVOICE_PAYMENT_CODE) {
            if(resultCode == Activity.RESULT_OK) {
//                double masterSalesID = data.getDoubleExtra("masterSalesID", 0.0);
                double cashTendered = data.getDoubleExtra("cashTendered", 0.0);
//                double balanceReturned = data.getDoubleExtra("balanceReturned", 0.0);
                extractFromDatabaseAndPrint(invoiceNumber, cashTendered);
            }
        }
    }

    public void setCustomerOnScreen() {
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
        this.stateId = customerSelected.getStateId();
        for(ProductWithQuantity productWithQuantity : productsSelected) {
            if(isTaxFree()) {
                productWithQuantity.getProduct().setTaxApplicable(false);
            } else {
                productWithQuantity.getProduct().setTaxApplicable(true);
            }
        }
        productsSelectedAdapter.notifyDataSetChanged();
    }

    public void selectProduct(Product product, int quantity) {
        if(customerSelected == null) {
            showOkDialog("Choose customer");
            return;
        }
        // when a product is clicked
//        MyToast.showToast(product.getProductName() + " selected", getApplicationContext());

        // New product added
        if(isTaxFree()) {
            product.setTaxApplicable(false);
        }
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(product);
        productWithQuantity.setQuantity(quantity);
        productsSelected.add(productWithQuantity);

        productsSelectedAdapter.notifyDataSetChanged();
        setProductSelectedTitle(productsSelected.size());
        refreshSubTotalAndTax();
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
        for(ProductWithQuantity productWithQuantity : productsSelected) {
            double salesPrice = productWithQuantity.getSalesPriceWithDiscount() * productWithQuantity.getQuantity();
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

    public double getOverallDiscountInRupees() {
        switch (this.overallDiscountType) {
            case 1:
                return ((this.overallDiscount * getTotalAmountWithoutOverallDiscount())/100);
            default:
                return this.overallDiscount;
        }
    }

    public double getTotalAmountWithoutOverallDiscount() {
        return totalAmount();
    }

    public double getTotalAmountWithOverallDiscount() {
        return totalAmount() - getOverallDiscountInRupees();
    }

    public double getOverallDiscountInPercentage() {
        switch (this.overallDiscountType) {
            case 1:
                return this.overallDiscount;
            default:
                return (overallDiscount * 100d) / getTotalAmountWithoutOverallDiscount();
        }
    }

    public double taxCalculationOfProducts() {
        double tax = 0;
        for(ProductWithQuantity productWithQuantity : productsSelected) {
            tax += (productWithQuantity.getTaxAmount() * productWithQuantity.getQuantity());
        }
        return tax;
    }

    public double taxCalculation() {
        double tax = taxCalculationOfProducts();
        if(isTaxFree() == false) {
            tax += ((18 * getFreightCharges()) / 100);
        }
        return tax;
    }

    public double totalAmount() {
        return subTotalWithoutDiscount() + taxCalculation() + getFreightCharges();
    }

    public void refreshSubTotalAndTax() {
        String rupeeSymbol = "\u20B9";
        double subTotal = subTotalWithoutDiscount();
        double discount = getOverallDiscountInRupees();
        double freightCharges = getFreightCharges();
        double tax = taxCalculation();
        double totalAmount = getTotalAmountWithOverallDiscount();
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f",subTotal));
        discountInNewInvoiceTextView.setText("-" + rupeeSymbol + String.format("%.2f",discount));
        freightChargesTextView.setText(rupeeSymbol + String.format("%.2f",freightCharges));
        taxTextView.setText(rupeeSymbol + String.format("%.2f",tax));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f",totalAmount));
    }

    public void otherChargesButtonClick(View v) {
        if(productsSelected.isEmpty()) {
            showOkDialog("No product selected");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.other_charges_dialog_new_invoice, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText transportChargesEditText = (EditText) view.findViewById(R.id.transportChargesEditText);
        final EditText insuranceChargesEditText = (EditText) view.findViewById(R.id.insuranceChargesEditText);
        final EditText packingChargesEditText = (EditText) view.findViewById(R.id.packingChargesEditText);

        if(transportCharges != 0.0)
            transportChargesEditText.setText(String.format("%.2f", transportCharges));
        if(insuranceCharges != 0.0)
            insuranceChargesEditText.setText(String.format("%.2f", insuranceCharges));
        if(packingCharges != 0.0)
            packingChargesEditText.setText(String.format("%.2f", packingCharges));

        final Button okButton = (Button) view.findViewById(R.id.okButtonOtherChargesInNewInvoice);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transportChargesString = transportChargesEditText.getText().toString();
                String insuranceChargesString = insuranceChargesEditText.getText().toString();
                String packingChargesString = packingChargesEditText.getText().toString();
                double transportCharges, insuranceCharges, packingCharges;
                if(transportChargesString.length() == 0) {
                    transportCharges = 0d;
                } else {
                    transportCharges = MyFunctions.parseDouble(transportChargesString);
                }
                if(insuranceChargesString.length() == 0) {
                    insuranceCharges = 0d;
                } else {
                    insuranceCharges = MyFunctions.parseDouble(insuranceChargesString);
                }
                if(packingChargesString.length() == 0) {
                    packingCharges = 0d;
                } else {
                    packingCharges = MyFunctions.parseDouble(packingChargesString);
                }

                setOtherCharges(transportCharges, insuranceCharges, packingCharges);
                dialog.dismiss();
                refreshSubTotalAndTax();
            }
        });
    }

    public void discountButtonClick(View v) {
        if(productsSelected.isEmpty()) {
            showOkDialog("No product selected");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.discount_dialog_new_invoice, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText discountEditText = (EditText) view.findViewById(R.id.discountInNewInvoiceEditText);
        final double totalAmount = getTotalAmountWithoutOverallDiscount();
        final Spinner discountTypeSpinner = (Spinner) view.findViewById(R.id.discountTypeSpinner);
        String rupeeSymbol = "\u20B9";
        String percentSymbol = "%";
        ArrayList<String> discountTypeList = new ArrayList<String>();
        discountTypeList.add("          "+rupeeSymbol);
        discountTypeList.add("          "+percentSymbol);
        ArrayAdapter adapter = new ArrayAdapter(NewSalesInvoiceFromDOActivity.this, android.R.layout.simple_spinner_item, discountTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        discountTypeSpinner.setAdapter(adapter);

        if(overallDiscount > 0) {
            discountEditText.setText(String.format("%.2f", overallDiscount));
        }
        discountTypeSpinner.setSelection(this.overallDiscountType);

        final Button okButton = (Button) view.findViewById(R.id.okButtonOtherChargesInNewInvoice);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discountString = discountEditText.getText().toString();
                double discount = 0;
                int discountType = discountTypeSpinner.getSelectedItemPosition();
                if(discountString.length() > 0) {
                    discount = MyFunctions.parseDouble(discountString);
                    switch (discountType) {
                        case 0:
                            if(discount > totalAmount) {
                                showOkDialog("Discount should be less than Sub-Total");
                                return;
                            }
                            break;

                        case 1:
                            if(discount > 100.0) {
                                showOkDialog("Invalid discount percent");
                                return;
                            }
                            break;
                    }
                }
                setOverallDiscount(discount);
                overallDiscountType = discountType;
                dialog.dismiss();
                refreshSubTotalAndTax();
            }
        });

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

    private String getInvoiceNumber() {
        if(MyFunctions.StringLength(this.invoiceNumber) == 0) {
            this.invoiceNumber = createInvoiceNumber();
        }
        return this.invoiceNumber;
    }

    private String createInvoiceNumber() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.InvoiceSeries);
        builder.append(this.TabCode);
        builder.append("/");
        builder.append(this.dateFormatInInvoiceNumber());
        builder.append("/");
        String previousInvoiceNumber = DatabaseHandler.getInstance().getPreviousInvoiceNumber(builder.toString());
        if(previousInvoiceNumber == null) {
            builder.append("001");
        } else {
            String seriesNumberString = previousInvoiceNumber.substring(previousInvoiceNumber.length() - 3);
            int seriesNumber = Integer.parseInt(seriesNumberString);
            seriesNumber++;
            seriesNumberString = "000" + seriesNumber;
            seriesNumberString = seriesNumberString.substring(seriesNumberString.length() - 3);
            builder.append("" + seriesNumberString);
        }
        return builder.toString();
    }

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
        masterSales.setTotalDiscountAmount(this.getOverallDiscountInRupees());
//        private static final int INDEX_M_SALES_TaxAmount = 5;
        masterSales.setSubTotal(subTotalWithoutDiscount() + taxCalculationOfProducts());
        masterSales.setTaxAmount(taxCalculation());
//        private static final int INDEX_M_SALES_TotalAmount = 6;
        masterSales.setTotalAmount(getTotalAmountWithOverallDiscount());
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
        if(this.StateID.equals(this.stateId+"")) {
            masterSales.setCGST(masterSales.getTaxAmount() / 2);
            masterSales.setSGST(masterSales.getTaxAmount() / 2);
            masterSales.setIGST(0d);
        } else {
            masterSales.setCGST(0d);
            masterSales.setSGST(0d);
            masterSales.setIGST(masterSales.getTaxAmount());
        }
//        private static final int INDEX_M_SALES_DiscountTypeID = 21; // % id 10 or 11
        switch (this.overallDiscountType) {
            case 0: // rupees
                masterSales.setDiscountTypeID(1); // discount type 10 or 11
            case 1: // %age
                masterSales.setDiscountTypeID(0); // discount type 10 or 11
        }
//        masterSales.setDiscountTypeID(0); // discount type 10 or 11
//        private static final int INDEX_M_SALES_Discount = 22; // amount or %age
        masterSales.setDiscount(this.overallDiscount);
//        private static final int INDEX_M_SALES_CESS = 23; // 0 default
        masterSales.setCESS(0); // hard coded
//        private static final int INDEX_M_SALES_DueDate = 24; // invoice date
        masterSales.setDueDate(this.invoiceDateString);
//        private static final int INDEX_M_SALES_CustomerName = 25; // entity name
        if(customerSelected.getEntityType().equalsIgnoreCase("Individual")) {
            masterSales.setCustomerName(customerSelected.getContactPerson());
        } else {
            masterSales.setCustomerName(customerSelected.getEntityName());
        }
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
        masterSales.setShippingStateID(this.stateId);
        masterSales.seteWayBillRequired(this.eWayBillRequired);
        masterSales.setDONumber(masterDeliveryOrder.getDONumber());

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
        if(productWithQuantity.getDiscountType() == 0) { // Rs
            transactionSales.setDiscountType(11); // Rs
            transactionSales.setDiscount(productWithQuantity.getDiscount());
        } else { // %
            transactionSales.setDiscountType(10);
            transactionSales.setDiscount(productWithQuantity.getDiscount());
        }
//        private static final int INDEX_T_SALES_CGST = 15; // tax
        if(this.StateID.equals(this.stateId+"")) {
            transactionSales.setCGST(transactionSales.getTaxAmount() / 2);
            transactionSales.setSGST(transactionSales.getTaxAmount() / 2);
            transactionSales.setIGST(0);
        } else {
            transactionSales.setCGST(0);
            transactionSales.setSGST(0);
            transactionSales.setIGST(transactionSales.getTaxAmount());
        }
//        private static final int INDEX_T_SALES_SGST = 16; // tax
//        private static final int INDEX_T_SALES_IGST = 17; // tax
//        private static final int INDEX_T_SALES_CESS = 18; // 0
        transactionSales.setCESS(0d);
//        private static final int INDEX_T_SALES_ItemName= 19; // product name
        transactionSales.setItemName(productWithQuantity.getProduct().getProductName());
        transactionSales.setProductCode(productWithQuantity.getProduct().getProductCode());
        transactionSales.setTaxRate(productWithQuantity.getProduct().getTAX());
        transactionSales.setSynced(productWithQuantity.getProduct().getSynced());
        return transactionSales;
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

    private String createDateOfInvoice() {
        StringBuilder builder = new StringBuilder();
        String months[] = new DateFormatSymbols().getShortMonths();
        builder.append("" + invoiceYear);
        builder.append("-");
        builder.append(months[invoiceMonth-1]);
        builder.append("-");
        String invoiceDayString = "00" + invoiceDay;
        invoiceDayString = invoiceDayString.substring(invoiceDayString.length() - 2);
        builder.append(invoiceDayString);
        return builder.toString();
    }

    private String getReceipt(MasterSales masterSales, ArrayList<TransactionSales> transactionSalesArrayList,
                              double cashTendered) {
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
        int OutletGSTCategoryID = MyFunctions.parseInt(this.GSTCategoryID);
        if (OutletGSTCategoryID == GSTCategory.COMPOSITE_SCHEME) {
            builder.append(MyFunctions.makeStringCentreAlign(getString(R.string.composite_scheme_receipt_heading), 48));
        } else {
            builder.append(MyFunctions.makeStringCentreAlign("Tax Invoice/Bill Of Supply", 48));
        }
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("Invoice No. : " + masterSales.getReferenceNumber());
        builder.append(nextLine);
        builder.append("Date : " + masterSales.getSalesDate());
        builder.append(nextLine);
        builder.append("Cashier : " + this.FirstName);
        builder.append(nextLine);
        builder.append("Customer : " + masterSales.getCustomerName());
        if(MyFunctions.StringLength(masterSales.getCustomerMobile()) > 0) {
            builder.append(" ( " + masterSales.getCustomerMobile() + " )");
        }
        builder.append(nextLine);
        if(!masterSales.getCustomerID().equals("0")) {
            if (MyFunctions.StringLength(masterSales.getBillAddress()) > 0) {
                builder.append(masterSales.getBillAddress());
                builder.append(nextLine);
            }
            if(MyFunctions.StringLength(masterSales.getCustomerGSTN()) > 0) {
                builder.append("GST Number : " + masterSales.getCustomerGSTN());
                builder.append(nextLine);
            }
        }
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        String itemNameHeading = "ITEM NAME";
        int itemNameHeadingLength = 14;
        builder.append(MyFunctions.makeStringLeftAlign(itemNameHeading, itemNameHeadingLength));

        String qtyHeading = "QTY";
        int qtyHeadingLength = 5;
        builder.append(MyFunctions.makeStringRightAlign(qtyHeading, qtyHeadingLength));

        String priceHeading = "PRICE";
        int priceHeadingLength = 10;
        builder.append(MyFunctions.makeStringRightAlign(priceHeading, priceHeadingLength));

        String gstHeading = "GST";
        int gstHeadingLength = 8;
        builder.append(MyFunctions.makeStringRightAlign(gstHeading, gstHeadingLength));

        String amountHeading = "AMOUNT";
        int amountHeadingLength = 11;
        builder.append(MyFunctions.makeStringRightAlign(amountHeading, amountHeadingLength));

        double savedDiscount=0;

        builder.append(nextLine);
        for(TransactionSales transactionSales : transactionSalesArrayList) {
            builder.append(transactionSales.getItemName());
            builder.append(nextLine);

            savedDiscount+= transactionSales.getDiscountAmount();
            builder.append(MyFunctions.makeStringLeftAlign("  ", itemNameHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(transactionSales.getQty()+"", qtyHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getSalesPriceWithDiscount()), priceHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getTaxAmount()
                    / transactionSales.getQty()), gstHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getTotalAmount()), amountHeadingLength));
            builder.append(nextLine);

//            builder.append(MyFunctions.makeStringRightAlign("(" + Math.round((transactionSales.getTaxAmount() / transactionSales.getQty()
//                            * 100)
//                            / transactionSales.getSalesPriceWithDiscount())
//                            + "%)"
//                    , 48 - amountHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign("(" + String.format("%.2f", transactionSales.getTaxRate()) + "%)"
                    , 48 - amountHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(" ", amountHeadingLength));
            builder.append(nextLine);
        }

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        int leftAlignLength = 36;
        int rightAlignLength = 12;
        builder.append(MyFunctions.makeStringLeftAlign("Total Amt.", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getSubTotal()), rightAlignLength));
        builder.append(nextLine);

//        builder.append(MyFunctions.makeStringLeftAlign("Discount", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
//        builder.append(nextLine);

        if(masterSales.getOtherCharges() > 0) {
            builder.append(MyFunctions.makeStringLeftAlign("Other Charges", leftAlignLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getOtherCharges()), rightAlignLength));
            builder.append(nextLine);
        }

        builder.append(MyFunctions.makeStringLeftAlign("Total GST", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getTaxAmount()), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("You Saved", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign( String.format("%.2f", savedDiscount), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Cash Discount", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Total (incl. GST)", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getTotalAmount()), rightAlignLength));
        builder.append(nextLine);

//        builder.append(MyFunctions.makeStringLeftAlign("Discount", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
//        builder.append(nextLine);

        double roundOffTotalAmount = Math.round(masterSales.getTotalAmount());

        double roundOffAmount = roundOffTotalAmount - masterSales.getTotalAmount();

        builder.append(MyFunctions.makeStringLeftAlign("Rounding of Amt.", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", roundOffAmount), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Net Payable Amt.", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", roundOffTotalAmount), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Cash Tendered", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", cashTendered), rightAlignLength));
        builder.append(nextLine);

        if(cashTendered - roundOffTotalAmount > 0) {
            builder.append(MyFunctions.makeStringLeftAlign("Balance Returned", leftAlignLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", cashTendered - roundOffTotalAmount), rightAlignLength));
            builder.append(nextLine);
        }

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        int GST_Length = 16; // 48/3
        builder.append("GST Summary");
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("CGST", GST_Length));
        builder.append(MyFunctions.makeStringCentreAlign("SGST", GST_Length));
        builder.append(MyFunctions.makeStringCentreAlign("IGST", GST_Length));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getCGST()), GST_Length));
        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getSGST()), GST_Length));
        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getIGST()), GST_Length));
        builder.append(nextLine);
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("Terms and Conditions :- " + masterSales.getTermsCondition(), 48));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your visit***",48));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append(nextLine);

        return builder.toString();
    }

//    private String getReceipt(MasterSales masterSales, ArrayList<TransactionSales> transactionSalesArrayList) {
//        StringBuilder builder = new StringBuilder();
//        String nextLine = "\n";
//        builder.append(nextLine);
//        builder.append(this.OutletName);
//        builder.append(nextLine);
//        String address[] = this.OutletAddress.split("### ");
//        for(String str : address) {
//            builder.append(str);
//            builder.append(nextLine);
//        }
//        builder.append("Tel : " + this.ContactNumber);
//        builder.append(nextLine);
//
//        builder.append("Outlet State: " +  StateName);
//        builder.append(nextLine);
//        builder.append("GST No. " + this.OutletGSTNumber);
//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append(MyFunctions.makeStringCentreAlign("Tax Invoice/Bill Of Supply", 48));
//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append("Cashier : " + this.FirstName);
//        builder.append(nextLine);
//        builder.append("Invoice No. : " + masterSales.getReferenceNumber());
//        builder.append(nextLine);
//        builder.append("Date : " + masterSales.getSalesDate());
//        builder.append(nextLine);
//        builder.append("Customer : " + masterSales.getCustomerName() + " ( " + masterSales.getCustomerMobile() + " )");
//        builder.append(nextLine);
//        builder.append(MyFunctions.drawLine("-", 48));
//        builder.append(nextLine);
//
//        String itemNameHeading = "ITEM NAME";
//        int itemNameHeadingLength = 16;
//        builder.append(MyFunctions.makeStringLeftAlign(itemNameHeading, itemNameHeadingLength));
//
//        String qtyHeading = "QTY";
//        int qtyHeadingLength = 5;
//        builder.append(MyFunctions.makeStringRightAlign(qtyHeading, qtyHeadingLength));
//
//        String priceHeading = "PRICE";
//        int priceHeadingLength = 9;
//        builder.append(MyFunctions.makeStringRightAlign(priceHeading, priceHeadingLength));
//
//        String gstHeading = "GST";
//        int gstHeadingLength = 8;
//        builder.append(MyFunctions.makeStringRightAlign(gstHeading, gstHeadingLength));
//
//        String amountHeading = "AMOUNT";
//        int amountHeadingLength = 10;
//        builder.append(MyFunctions.makeStringRightAlign(amountHeading, amountHeadingLength));
//
//        double savedDiscount=0;
//
//
//
//        builder.append(nextLine);
//        for(TransactionSales transactionSales : transactionSalesArrayList) {
//            builder.append(transactionSales.getItemName());
//            builder.append(nextLine);
//
//
//            savedDiscount+= transactionSales.getDiscount();
//            builder.append(MyFunctions.makeStringLeftAlign("  ", itemNameHeadingLength));
//            builder.append(MyFunctions.makeStringRightAlign(transactionSales.getQty()+"", qtyHeadingLength));
//            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getSalesPriceWithDiscount()), priceHeadingLength));
//            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getTaxAmount()
//                    / transactionSales.getQty()), gstHeadingLength));
//            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getTotalAmount()), amountHeadingLength));
//            builder.append(nextLine);
//
//            builder.append(MyFunctions.makeStringRightAlign("(" + Math.round((transactionSales.getTaxAmount() / transactionSales.getQty()
//                            * 100)
//                            / transactionSales.getSalesPriceWithDiscount())
//                            + "%)"
//                    , 48 - amountHeadingLength));
//            builder.append(MyFunctions.makeStringRightAlign(" ", amountHeadingLength));
//            builder.append(nextLine);
//        }
//
//        builder.append(MyFunctions.drawLine("-", 48));
//        builder.append(nextLine);
//
//        int leftAlignLength = 36;
//        int rightAlignLength = 12;
//        builder.append(MyFunctions.makeStringLeftAlign("Sub Total Amt.", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getSubTotal()), rightAlignLength));
//        builder.append(nextLine);
//
////        builder.append(MyFunctions.makeStringLeftAlign("Discount", leftAlignLength));
////        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
////        builder.append(nextLine);
//
//        builder.append(MyFunctions.makeStringLeftAlign("Other Charges", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getOtherCharges()), rightAlignLength));
//        builder.append(nextLine);
//
//        builder.append(MyFunctions.makeStringLeftAlign("Payable Tax Amount", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getTaxAmount()), rightAlignLength));
//        builder.append(nextLine);
//
//        builder.append(MyFunctions.makeStringLeftAlign("Discount", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
//        builder.append(nextLine);
//
//        double totalSalesAmount = masterSales.getBaseAmount()
//                - masterSales.getTotalDiscountAmount()
//                + masterSales.getTaxAmount()
//                + masterSales.getOtherCharges();
//
//        builder.append(MyFunctions.makeStringLeftAlign("Total Sales (incl. GST)", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", totalSalesAmount), rightAlignLength));
//        builder.append(nextLine);
//
//        builder.append(MyFunctions.makeStringLeftAlign("You Saved", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign( String.format("%.2f", savedDiscount), rightAlignLength));
//        builder.append(nextLine);
//
//
////        builder.append(MyFunctions.makeStringLeftAlign("Discount", leftAlignLength));
////        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
////        builder.append(nextLine);
//
//        double roundOffAmount = masterSales.getTotalAmount() - totalSalesAmount;
//
//        builder.append(MyFunctions.makeStringLeftAlign("Rounding of Amt.", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", roundOffAmount), rightAlignLength));
//        builder.append(nextLine);
//
//        builder.append(MyFunctions.makeStringLeftAlign("Net Payable Amt.", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getTotalAmount()), rightAlignLength));
//        builder.append(nextLine);
//
//        builder.append(MyFunctions.makeStringLeftAlign("Cash", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getPaidAmount()), rightAlignLength));
//        builder.append(nextLine);
//
//        builder.append(MyFunctions.makeStringLeftAlign("Balance", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getTotalAmount() - masterSales.getPaidAmount()), rightAlignLength));
//        builder.append(nextLine);
//
//        builder.append(MyFunctions.drawLine("-", 48));
//        builder.append(nextLine);
//
//        int GST_Length = 16; // 48/3
//        builder.append("GST Summary");
//        builder.append(nextLine);
//        builder.append(MyFunctions.makeStringCentreAlign("CGST", GST_Length));
//        builder.append(MyFunctions.makeStringCentreAlign("SGST", GST_Length));
//        builder.append(MyFunctions.makeStringCentreAlign("IGST", GST_Length));
//        builder.append(nextLine);
//        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getCGST()), GST_Length));
//        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getSGST()), GST_Length));
//        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getIGST()), GST_Length));
//        builder.append(nextLine);
//        builder.append(MyFunctions.drawLine("-", 48));
//        builder.append(nextLine);
//        builder.append(MyFunctions.makeStringCentreAlign("Terms and Conditions :- " + masterSales.getTermsCondition(), 48));
//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your visit***",48));
//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append(nextLine);
//
//        return builder.toString();
//    }

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(NewSalesInvoiceFromDOActivity.this,mHandler);
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

        Toast.makeText(NewSalesInvoiceFromDOActivity.this, getString(R.string.usb_msg_conn_state),
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
                Toast.makeText(NewSalesInvoiceFromDOActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
//                printThroughPrinter(receiptString);
            }
        }
    }

    private void printThroughPrinter(String receiptString, boolean openCashDrawer) {
        byte isHasPaper;
        byte[] cmd = null;
        if( dev != null ){
            if( usbCtrl.isHasPermission(dev)){
                Toast.makeText(NewSalesInvoiceFromDOActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(NewSalesInvoiceFromDOActivity.this, "The printer has no paper",
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
                    if(openCashDrawer) {
                        usbCtrl.openCashBox(dev);
                    }
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

    private boolean checkConfirmValidation() {
        if(customerSelected == null) {
            showOkDialog("Choose Customer");
        } else if(productsSelected.size() == 0) {
            showOkDialog("No product selected");
        } else {
            return true;
        }
        return false;
    }

    public void confirmInvoice() {
        // confirm invoice dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
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

//    public void confirmInvoice() {
//        // confirm invoice dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
//        final View view = getLayoutInflater().inflate(R.layout.invoice_confirm_dialog, null);
//        builder.setView(view);
//        final AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//        Button yesButton = (Button) view.findViewById(R.id.yesClick);
//        Button skipButton = (Button) view.findViewById(R.id.skipClick);
//        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
//        yesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                eWayBillRequired = 1;
//                // invoice confirmed
//                InvoiceInDatabase();
//            }
//        });
//        skipButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                eWayBillRequired = 0;
//                // invoice confirmed
//                InvoiceInDatabase();
//            }
//        });
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }

    public void InvoiceInDatabase() {
        String invoiceNumber = getInvoiceNumber();
        MasterSales masterSales = createMasterSalesObject(invoiceNumber);
        double masterSalesID = DatabaseHandler.getInstance().insertMasterSales(masterSales);
        ArrayList<TransactionSales> transactionSalesArrayList = new ArrayList<>();
        ArrayList<StockLedger> stockLedgerArrayList = new ArrayList<>();
        for(ProductWithQuantity productWithQuantity : productsSelected) {
            transactionSalesArrayList.add(createTransactionSales(masterSalesID, invoiceNumber, productWithQuantity));
        }
        for(TransactionSales transactionSales : transactionSalesArrayList) {
            stockLedgerArrayList.add(createStockLedger(masterSales, transactionSales));
        }
        DatabaseHandler.getInstance().addTransactionSalesList(transactionSalesArrayList);
        DatabaseHandler.getInstance().addStockLedgerList(stockLedgerArrayList);

        // update Eway Bill, if present - enter salesReturnInvoiceNumber in ewaybill record
        updateEwayBill(invoiceNumber);
        Toast.makeText(getApplicationContext(), "Invoice completed", Toast.LENGTH_SHORT).show();
        double roundOffTotalAmount = Math.round(masterSales.getTotalAmount());
        String invoiceDate = masterSales.getSalesDate();
//        if(eWayBillRequired == 1) {
//            successfulEWayBillDialog(salesReturnInvoiceNumber, masterSalesID, roundOffTotalAmount, invoiceDate);
//        } else {
//        }
        doPayment(masterSalesID, roundOffTotalAmount, invoiceDate);
    }

    private void updateEwayBill(String invoiceNumber) {
        EwayBill ewayBill = DatabaseHandler.getInstance().getEwayBillRecordFromDO(masterDeliveryOrder.getDONumber());
        if(ewayBill != null) {
            ewayBill.setInvoiceNumber(invoiceNumber);
            if(ewayBill.getSynced() != 0) {
                ewayBill.setSynced(2); // for update
            } else {
                ewayBill.setSynced(0);
            }
            DatabaseHandler.getInstance().updatePreviousEWayBill(ewayBill);
        }
    }

    private void doPayment(double masterSalesID, double totalAmount, String invoiceDate) {
        Intent intentDoPayment = new Intent(getApplicationContext(), MultipleInvoicePaymentActivity.class);
        intentDoPayment.putExtra("salesReturnInvoiceNumber", invoiceNumber);
        intentDoPayment.putExtra("totalAmount", totalAmount);
        intentDoPayment.putExtra("invoiceDate", invoiceDate);
        intentDoPayment.putExtra("masterSalesID", masterSalesID);
        intentDoPayment.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_NEW_INVOICE_FOR_DO);
        startActivityForResult(intentDoPayment, INVOICE_PAYMENT_CODE);
    }

    public void extractFromDatabaseAndPrint(String invoiceNumber, double cashTendered) {
        MasterSales msSales = DatabaseHandler.getInstance().getMasterSalesRecord(invoiceNumber);
        ArrayList<TransactionSales> tArray = DatabaseHandler.getInstance().getInvoiceDetailsFromDB(msSales.getReferenceNumber());
        Log.i("master", "Invoice Number - " + invoiceNumber);
        for (TransactionSales tSales : tArray) {
            Log.i("sales", tSales.getRefNumber() + " " + tSales.getItemName());
        }

        // print
        final String receiptString = getReceipt(msSales, tArray, cashTendered);
        Log.i("Print", receiptString);
        boolean openCashDrawer = false;
        if(cashTendered > 0) {
            openCashDrawer = true;
        }
        printThroughPrinter(receiptString, openCashDrawer);

        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
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
                NewSalesInvoiceFromDOActivity.super.onBackPressed();
            }
        });
    }

    public void onConfirmButtom(View v) {
        if(checkConfirmValidation()) {
            confirmInvoice();
        }
    }

    public void termsAndConditionsButtonClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
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
        final Spinner addressStateSpinner = (Spinner) view.findViewById(R.id.addressStateSpinner);

        ArrayList<State> state_list= new ArrayList<State>();
        state_list.add(new State(0, "Choose State"));
        state_list.addAll(DatabaseHandler.getInstance().getStateListFromDB());

        ArrayAdapter state_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, state_list);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressStateSpinner.setAdapter(state_adapter);

        billAddressEditText.setText(this.billAddressString);
        shipAddressEditText.setText(this.shipAddressString);
        for (int i=0; i<state_list.size(); i++) {
            if (stateId == state_list.get(i).getStateID()) {
                addressStateSpinner.setSelection(i);
                break;
            }
        }
        Button saveButton = (Button) view.findViewById(R.id.saveClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressStateSpinner.getSelectedItemPosition() == 0) {
                    showOkDialog("Select Shipping state");
                    return;
                }
                billAddressString = billAddressEditText.getText().toString();
                shipAddressString = shipAddressEditText.getText().toString();
                State state = (State)addressStateSpinner.getSelectedItem();
                stateId = state.getStateID();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
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

    public void successfulEWayBillDialog(final String invoiceNumber, final double masterSalesID, final double totalAmount, final String invoiceDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSalesInvoiceFromDOActivity.this);
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
        ArrayAdapter adapter = new ArrayAdapter(NewSalesInvoiceFromDOActivity.this, android.R.layout.simple_spinner_item, reasonList);
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
                ewayBill.setInvoiceNumber(invoiceNumber);
                EwayBillReason reason = (EwayBillReason) reasonSpinner.getSelectedItem();
                ewayBill.setReasonID(reason.getID());
                ewayBill.setDocumentNo(documentNumberEditText.getText().toString());
                ewayBill.setVehicleNo(vehicleNumberEditText.getText().toString());
                ewayBill.setEWayBillNo(ewayBillNumberEditText.getText().toString());
                ewayBill.setCreatedBy(MyFunctions.parseDouble(LoginID));
                ewayBill.setCreatedDtTm(MyFunctions.getCurrentDateTime());
                ewayBill.setSynced(0);
                DatabaseHandler.getInstance().addNewEWayBill(ewayBill);

                doPayment(masterSalesID, totalAmount, invoiceDate);
                dialog.dismiss();
            }
        });
    }
}
