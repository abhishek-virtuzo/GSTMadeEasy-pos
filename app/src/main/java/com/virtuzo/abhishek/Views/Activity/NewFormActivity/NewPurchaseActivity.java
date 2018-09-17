package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ListActivity.SupplierListActivity;
import com.virtuzo.abhishek.Views.Adapters.ProductListInNewPurchaseAdapter;
import com.virtuzo.abhishek.Views.Adapters.ProductsSelectedInNewPurchaseAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MasterPurchase;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Product;
import com.virtuzo.abhishek.modal.PurchasedProduct;
import com.virtuzo.abhishek.modal.StockLedger;
import com.virtuzo.abhishek.modal.Supplier;
import com.virtuzo.abhishek.modal.TransactionPurchase;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class NewPurchaseActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView productListInNewInvoiceRecyclerView, productsSelectedInNewInvoiceRecyclerView;
    final int NUMBER_OF_COLUMNS_IN_PRODUCT_LIST = 3, NUMBER_OF_COLUMNS_IN_PRODUCTS_SELECTED = 1;
    private ProductListInNewPurchaseAdapter productListAdapter;
    private ProductsSelectedInNewPurchaseAdapter productsSelectedAdapter;
    private ArrayList<Product> listOfAllProducts;
    private ArrayList<PurchasedProduct> productsSelected;
    Button supplierButtonForNewInvoice, invoiceDatePickerButton;
    String LoginID, BusinessId, OutletID, TermsAndConditions, OutletAddress, OutletLogo, GSTCategoryID,
            OutletGSTNumber, InvoiceSeries, TabCode, StateID, OutletName, FirstName, ContactNumber;
    private final int CHOOSE_SUPPLIER_RESPONSE_CODE = 101, DATE_PICKER_DIALOG_ID = 201;
    Supplier supplierSelected;
    int invoiceDay, invoiceMonth, invoiceYear;
    boolean validInvoiceDateSelected;
    double transportCharges = 0, insuranceCharges = 0, packingCharges = 0, overallDiscount = 0;
//    SearchView searchViewForProductList;
    EditText searchViewForProductList;
    TextView subTotalTextView, taxTextView, freightChargesTextView, totalAmountTextView, discountInNewInvoiceTextView;
    String invoiceDateString;
    EditText invoiceNumberEditText;
    TextView productSelectedTitleTextView;
    String termsAndConditionsString = "";
    double cgst=0, sgst=0, igst=0, cess=0;
    double cgstOtherCharges=0, sgstOtherCharges=0, igstOtherCharges=0, cessOtherCharges=0;
    double subTotal = 0;
    double totalAmount = 0;
    ProgressBar progressBar;
    ArrayList<Product> newList;

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
        OutletName = setting.getString("OutletName", "");
        FirstName = setting.getString("FirstName", "");
        GSTCategoryID = setting.getString("GSTCategoryID", "");
        ContactNumber = setting.getString("ContactNumber", "");
        termsAndConditionsString = TermsAndConditions;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_purchase);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setTitle("New Invoice");
        getSupportActionBar().hide();
        Toolbar toolbar_in_new_purchase = (Toolbar) findViewById(R.id.toolbar_in_new_purchase);
        toolbar_in_new_purchase.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_new_purchase.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_new_purchase.findViewById(R.id.actionBarTitle);
        heading.setText("Purchase Invoice");
        invoiceNumberEditText = (EditText) toolbar_in_new_purchase.findViewById(R.id.invoiceNumberEditTextInNewPurchase);
        invoiceNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });

        SharedPreferences data = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        BusinessId = data.getString("BusinessId", "");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        subTotalTextView = (TextView) findViewById(R.id.subTotalInNewInvoiceTextView);
        freightChargesTextView = (TextView) findViewById(R.id.freightChargesInNewInvoiceTextView);
        taxTextView = (TextView) findViewById(R.id.totalTaxInNewInvoiceTextView);
        totalAmountTextView = (TextView) findViewById(R.id.totalAmountInNewInvoiceTextView);
//        discountInNewInvoiceTextView = (TextView) findViewById(R.id.discountInNewInvoiceTextView);
        String rupeeSymbol = "\u20B9";
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
//        discountInNewInvoiceTextView.setText("-" + rupeeSymbol + String.format("%.2f", 0d));
        freightChargesTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        taxTextView.setText(rupeeSymbol + String.format("%.2f", 0d));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f", 0d));

        // products list
        listOfAllProducts = new ArrayList<>();
        productListInNewInvoiceRecyclerView = (RecyclerView) findViewById(R.id.productListInNewInvoiceRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS_IN_PRODUCT_LIST);
        productListInNewInvoiceRecyclerView.setLayoutManager(mLayoutManager);
        productListInNewInvoiceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productListInNewInvoiceRecyclerView.setHasFixedSize(true);
        newList = new ArrayList<Product>();
        productListAdapter = new ProductListInNewPurchaseAdapter(newList, new ProductListInNewPurchaseAdapter.OnClickListener() {
            @Override
            public void onItemClick(Product product) {
                selectProduct(product);
            }
        });
        productListInNewInvoiceRecyclerView.setAdapter(productListAdapter);
        Log.i("New Invoice", "Before database access");
//        listOfAllProducts.addAll(DatabaseHandler.getInstance().getProductsListFromDB());
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
        productsSelectedAdapter = new ProductsSelectedInNewPurchaseAdapter(productsSelected, new ProductsSelectedInNewPurchaseAdapter.OnClickListener() {
            @Override
            public void onItemClick(final PurchasedProduct purchasedProduct) {
                updateProductDetails(purchasedProduct);
            }

            @Override
            public void onCancelItem(int position) {
                productsSelected.remove(position);
                productsSelectedAdapter.notifyDataSetChanged();
                setProductSelectedTitle(productsSelected.size());
                refreshSubTotalTaxAndTotalAmount();
            }
        });
        productsSelectedInNewInvoiceRecyclerView.setAdapter(productsSelectedAdapter);

        // choose customer
        supplierSelected = null;
        supplierButtonForNewInvoice = (Button) findViewById(R.id.supplierButtonForNewInvoice);
        supplierButtonForNewInvoice.setText("Choose Supplier");
        supplierButtonForNewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShowSupplierList = new Intent(getApplicationContext(), SupplierListActivity.class);
                intentShowSupplierList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_PURCHASE_INVOICE);
                startActivityForResult(intentShowSupplierList, CHOOSE_SUPPLIER_RESPONSE_CODE);
            }
        });

        // invoice date
        validInvoiceDateSelected = false;
        invoiceDatePickerButton = (Button) findViewById(R.id.invoiceDatePickerButtonInNewInvoice);
        Calendar calendar = Calendar.getInstance();
        invoiceYear = calendar.get(Calendar.YEAR);
        invoiceMonth = calendar.get(Calendar.MONTH) + 1;
        invoiceDay = calendar.get(Calendar.DAY_OF_MONTH);

        //TODO purchase date change
//        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
////
////        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.clear();
//        calendar2.set(Calendar.DATE, invoiceDay);
//        calendar2.set(Calendar.MONTH, invoiceMonth);
//        calendar2.set(Calendar.YEAR, invoiceYear);
//        Date date = calendar2.getTime();
//
////        invoiceDateString = formatter.format(date);
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

        Log.i("New Purchase", "onCreate complete");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                listOfAllProducts.addAll(DatabaseHandler.getInstance().getProductsListFromDB());
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

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DATE_PICKER_DIALOG_ID) {
//            DatePickerDialog dpDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                    invoiceYear = year;
//                    invoiceMonth = monthOfYear;
//                    invoiceDay = dayOfMonth;
//
//                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
//
////
////        // Create a calendar object that will convert the date and time value in milliseconds to date.
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.clear();
//                    calendar.set(Calendar.DATE, invoiceDay);
//                    calendar.set(Calendar.MONTH, invoiceMonth);
//                    calendar.set(Calendar.YEAR, invoiceYear);
//                    Date date = calendar.getTime();
//
//                   String dateString= formatter.format(date);
//
//                    //String dateString = invoiceDay + "/" + invoiceMonth + "/" + invoiceYear;
//
//                    Toast.makeText(NewPurchaseActivity.this, dateString, Toast.LENGTH_SHORT).show();
//                    invoiceDatePickerButton.setText(dateString);
//                    invoiceDateString = dateString;
//                    validInvoiceDateSelected = true;
//                }
//            }, invoiceYear, invoiceMonth, invoiceDay);
//            dpDialog.setCanceledOnTouchOutside(false);
//            DatePicker datePicker = dpDialog.getDatePicker();
//            datePicker.setCalendarViewShown(false);
//            Calendar cal = Calendar.getInstance();
//            datePicker.setMaxDate(cal.getTimeInMillis());
//            cal.set(invoiceYear, invoiceMonth - 1, 1);
//            if(invoiceDay < 6) {
//                cal.add(Calendar.MONTH, -1);
//            }
//            datePicker.setMinDate(cal.getTimeInMillis());
//            return dpDialog;
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
                    Toast.makeText(NewPurchaseActivity.this, dateString, Toast.LENGTH_SHORT).show();
                    invoiceDatePickerButton.setText(dateString);
                    invoiceDateString = dateString;
                    validInvoiceDateSelected = true;
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

        if(requestCode == CHOOSE_SUPPLIER_RESPONSE_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                String json = data.getStringExtra("supplier");
                Gson gson = new Gson();
                supplierSelected = gson.fromJson(json, Supplier.class);

                int MaximumEntityNameLength = 15;
                String showSupplierEntityName = supplierSelected.getEntityName();
                if(supplierSelected.getEntityType().equalsIgnoreCase("Individual")) {
                    showSupplierEntityName = supplierSelected.getContactPerson();
                }
                if(showSupplierEntityName.length() > MaximumEntityNameLength) {
                    showSupplierEntityName = showSupplierEntityName.substring(0,
                            Math.min(showSupplierEntityName.length(), MaximumEntityNameLength - 2));
                    showSupplierEntityName = showSupplierEntityName + "...";
                }
                supplierButtonForNewInvoice.setText(showSupplierEntityName
                        + " (" + supplierSelected.getContactNumber() + ")");
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
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
        if(supplierSelected == null) {
            showOkDialog("Choose supplier");
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
            PurchasedProduct purchasedProduct = new PurchasedProduct(product);
            addNewProductDetails(purchasedProduct);
        }
    }

    public void addNewProductDetails(final PurchasedProduct purchasedProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.product_selected_details_purchase_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final TextView titleTextView = (TextView) view.findViewById(R.id.productNameTitle);
        final EditText priceEditText = (EditText) view.findViewById(R.id.unitPriceProductSelectedInNewPurchase);
        final EditText quantityEditText = (EditText) view.findViewById(R.id.quantityProductSelectedInNewPurchaseEditText);
        final EditText cgstEditText = (EditText) view.findViewById(R.id.cgstEditText);
        final EditText sgstEditText = (EditText) view.findViewById(R.id.sgstEditText);
        final EditText igstEditText = (EditText) view.findViewById(R.id.igstEditText);
        final EditText cessEditText = (EditText) view.findViewById(R.id.cessEditText);

        titleTextView.setText(purchasedProduct.getProduct().getProductName());
        if(purchasedProduct.getCGST() != 0) {
            cgstEditText.setText(String.format("%.2f", purchasedProduct.getCGST()));
        }
        if(purchasedProduct.getSGST() != 0) {
            sgstEditText.setText(String.format("%.2f", purchasedProduct.getSGST()));
        }
        if(purchasedProduct.getIGST() != 0) {
            igstEditText.setText(String.format("%.2f", purchasedProduct.getIGST()));
        }
        if(purchasedProduct.getCESS() != 0) {
            cessEditText.setText(String.format("%.2f", purchasedProduct.getCESS()));
        }

        // default values
        final Button okButton = (Button) view.findViewById(R.id.okButtonInProductSelectedDetailsInNewInvoice);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitPriceString = priceEditText.getText().toString();
                String quantityString = quantityEditText.getText().toString();
                if(unitPriceString.length() == 0) {
                    showOkDialog("Enter valid price");
                } else if(quantityString.length() == 0 || quantityString.length() > 4) {
                    showOkDialog("Enter valid quantity");
                } else {
                    double unitPrice = MyFunctions.parseDouble(unitPriceString);
                    int quantity = Integer.parseInt(quantityString);
                    if(unitPrice == 0) {
                        showOkDialog("Enter valid price");
                    } else if(quantity == 0) {
                        showOkDialog("Enter valid quantity");
                    } else {
//                        if(isTaxFree()) {
//                            purchasedProduct.getProduct().setTaxApplicable(false);
//                        }
                        purchasedProduct.setUnitPrice(unitPrice);
                        purchasedProduct.setQuantity(quantity);
                        purchasedProduct.setCGST(MyFunctions.parseDouble(cgstEditText.getText().toString()));
                        purchasedProduct.setSGST(MyFunctions.parseDouble(sgstEditText.getText().toString()));
                        purchasedProduct.setIGST(MyFunctions.parseDouble(igstEditText.getText().toString()));
                        purchasedProduct.setCESS(MyFunctions.parseDouble(cessEditText.getText().toString()));
                        productsSelected.add(purchasedProduct);
                        productsSelectedAdapter.notifyDataSetChanged();
                        setProductSelectedTitle(productsSelected.size());
                        refreshSubTotalTaxAndTotalAmount();
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    public void updateProductDetails(final PurchasedProduct purchasedProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.product_selected_details_purchase_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView titleTextView = (TextView) view.findViewById(R.id.productNameTitle);
        final EditText priceEditText = (EditText) view.findViewById(R.id.unitPriceProductSelectedInNewPurchase);
        final EditText quantityEditText = (EditText) view.findViewById(R.id.quantityProductSelectedInNewPurchaseEditText);
        final EditText cgstEditText = (EditText) view.findViewById(R.id.cgstEditText);
        final EditText sgstEditText = (EditText) view.findViewById(R.id.sgstEditText);
        final EditText igstEditText = (EditText) view.findViewById(R.id.igstEditText);
        final EditText cessEditText = (EditText) view.findViewById(R.id.cessEditText);

        // default values
        titleTextView.setText(purchasedProduct.getProduct().getProductName());
        if(purchasedProduct.getCGST() != 0) {
            cgstEditText.setText(String.format("%.2f", purchasedProduct.getCGST()));
        }
        if(purchasedProduct.getSGST() != 0) {
            sgstEditText.setText(String.format("%.2f", purchasedProduct.getSGST()));
        }
        if(purchasedProduct.getIGST() != 0) {
            igstEditText.setText(String.format("%.2f", purchasedProduct.getIGST()));
        }
        if(purchasedProduct.getCESS() != 0) {
            cessEditText.setText(String.format("%.2f", purchasedProduct.getCESS()));
        }
        priceEditText.setText(String.format("%.2f", purchasedProduct.getUnitPrice()));
        quantityEditText.setText(purchasedProduct.getQuantity()+"");
        priceEditText.selectAll();
        final Button okButton = (Button) view.findViewById(R.id.okButtonInProductSelectedDetailsInNewInvoice);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitPriceString = priceEditText.getText().toString();
                String quantityString = quantityEditText.getText().toString();
                if(unitPriceString.length() == 0) {
                    showOkDialog("Enter valid price");
                } else if(quantityString.length() == 0 || quantityString.length() > 4) {
                    showOkDialog("Enter valid quantity");
                } else {
                    double unitPrice = MyFunctions.parseDouble(unitPriceString);
                    int quantity = Integer.parseInt(quantityString);
                    if(unitPrice == 0) {
                        showOkDialog("Enter valid price");
                    } else if(quantity == 0) {
                        showOkDialog("Enter valid quantity");
                    } else {
//                        if(isTaxFree()) {
//                            purchasedProduct.getProduct().setTaxApplicable(false);
//                        }
                        purchasedProduct.setUnitPrice(unitPrice);
                        purchasedProduct.setQuantity(quantity);
                        purchasedProduct.setCGST(MyFunctions.parseDouble(cgstEditText.getText().toString()));
                        purchasedProduct.setSGST(MyFunctions.parseDouble(sgstEditText.getText().toString()));
                        purchasedProduct.setIGST(MyFunctions.parseDouble(igstEditText.getText().toString()));
                        purchasedProduct.setCESS(MyFunctions.parseDouble(cessEditText.getText().toString()));
                        productsSelectedAdapter.notifyDataSetChanged();
                        setProductSelectedTitle(productsSelected.size());
                        refreshSubTotalTaxAndTotalAmount();
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    private void setProductSelectedTitle(int size) {
        productSelectedTitleTextView.setText("Products Selected (" + size + ")");
    }

    public boolean isTaxFree() {
        int GSTCategoryID = Integer.parseInt(this.GSTCategoryID);
        if(GSTCategoryID == 4) {
                return true;
        }
        if(supplierSelected != null) {
            switch (supplierSelected.getCategoryId()) {
                case 2:
                case 3:
                    return true;
            }
        }
        return false;
    }

    public double subTotalWithoutDiscount() {
        double subTotal = 0;
        for(PurchasedProduct purchasedProduct : productsSelected) {
            double salesPrice = purchasedProduct.getUnitPrice() * purchasedProduct.getQuantity();
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
        if(subTotalWithoutDiscount() != 0) {
            return this.overallDiscount;
        }
        return 0;
    }

    public double subTotalWithDiscount() {
        if(subTotalWithoutDiscount() < overallDiscount) {
            overallDiscount = 0;
        }
        return (subTotalWithoutDiscount() - overallDiscount);
    }

    public double overallDiscountInPercentage() {
        if(subTotalWithoutDiscount() != 0) {
            return (overallDiscount * 100d) / subTotalWithoutDiscount();
        }
        return 0;
    }

    public double updateTaxCalculation() {
        double tax = 0;
        cgst = 0;
        sgst = 0;
        igst = 0;
        cess = 0;
        for(PurchasedProduct purchasedProduct : productsSelected) {
            cgst += purchasedProduct.getCGST();
            sgst += purchasedProduct.getSGST();
            igst += purchasedProduct.getIGST();
            cess += purchasedProduct.getCESS();
        }
        cgst += cgstOtherCharges;
        sgst += sgstOtherCharges;
        igst += igstOtherCharges;
        cess += cessOtherCharges;
        tax += cgst;
        tax += sgst;
        tax += igst;
        tax += cess;
        return tax;
    }

    public double getTaxCalculation() {
        double tax = 0;
        tax += cgst;
        tax += sgst;
        tax += igst;
        tax += cess;
        return tax;
    }

    public void refreshSubTotalTaxAndTotalAmount() {
        String rupeeSymbol = "\u20B9";
        double subTotal = subTotalWithoutDiscount();
        double freightCharnges = getFreightCharges();
        double tax = updateTaxCalculation();
        double totalAmount = subTotal + freightCharnges + tax;
        this.subTotal = subTotal;
        this.totalAmount = totalAmount;
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f",subTotal));
        freightChargesTextView.setText(rupeeSymbol + String.format("%.2f",freightCharnges));
        taxTextView.setText(rupeeSymbol + String.format("%.2f",tax));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f",totalAmount));
    }

    public void refreshOtherChargesTaxAndTotalAmount() {
        String rupeeSymbol = "\u20B9";
        double freightCharnges = getFreightCharges();
        double tax = updateTaxCalculation();
        double totalAmount = subTotal + freightCharnges + tax;
        this.totalAmount = totalAmount;
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f",subTotal));
        freightChargesTextView.setText(rupeeSymbol + String.format("%.2f",freightCharnges));
        taxTextView.setText(rupeeSymbol + String.format("%.2f",tax));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f",totalAmount));
    }

    public void refreshTaxAndTotalAmount() {
        String rupeeSymbol = "\u20B9";
        double freightCharnges = getFreightCharges();
        double tax = getTaxCalculation();
        double totalAmount = subTotal + freightCharnges + tax;
        this.totalAmount = totalAmount;
        subTotalTextView.setText(rupeeSymbol + String.format("%.2f",subTotal));
        freightChargesTextView.setText(rupeeSymbol + String.format("%.2f",freightCharnges));
        taxTextView.setText(rupeeSymbol + String.format("%.2f",tax));
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f",totalAmount));
    }

    public void refreshTotalAmount() {
        String rupeeSymbol = "\u20B9";
        totalAmountTextView.setText(rupeeSymbol + String.format("%.2f",totalAmount));
    }

    public void otherChargesButtonClick(View v) {
        if(productsSelected.isEmpty()) {
            Toast.makeText(NewPurchaseActivity.this, "No product selected", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.other_charges_dialog_new_purchase, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText transportChargesEditText = (EditText) view.findViewById(R.id.transportChargesEditText);
        final EditText insuranceChargesEditText = (EditText) view.findViewById(R.id.insuranceChargesEditText);
        final EditText packingChargesEditText = (EditText) view.findViewById(R.id.packingChargesEditText);
        final EditText cgstEditText = (EditText) view.findViewById(R.id.cgstEditText);
        final EditText sgstEditText = (EditText) view.findViewById(R.id.sgstEditText);
        final EditText igstEditText = (EditText) view.findViewById(R.id.igstEditText);
        final EditText cessEditText = (EditText) view.findViewById(R.id.cessEditText);
        transportChargesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                double totalOtherCharges = transportCharges + insuranceCharges + packingCharges;
                double cgst = 0, sgst = 0;
                double taxPercent = .09;
                cgst = taxPercent * totalOtherCharges;
                sgst = taxPercent * totalOtherCharges;
                cgstEditText.setText(String.format("%.2f", cgst));
                sgstEditText.setText(String.format("%.2f", sgst));
                igstEditText.setText("");
                cessEditText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        insuranceChargesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                double totalOtherCharges = transportCharges + insuranceCharges + packingCharges;
                double cgst = 0, sgst = 0;
                double taxPercent = .09;
                cgst = taxPercent * totalOtherCharges;
                sgst = taxPercent * totalOtherCharges;
                cgstEditText.setText(String.format("%.2f", cgst));
                sgstEditText.setText(String.format("%.2f", sgst));
                igstEditText.setText("");
                cessEditText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        packingChargesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                double totalOtherCharges = transportCharges + insuranceCharges + packingCharges;
                double cgst = 0, sgst = 0;
                double taxPercent = .09;
                cgst = taxPercent * totalOtherCharges;
                sgst = taxPercent * totalOtherCharges;
                cgstEditText.setText(String.format("%.2f", cgst));
                sgstEditText.setText(String.format("%.2f", sgst));
                igstEditText.setText("");
                cessEditText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        if(transportCharges != 0.0)
            transportChargesEditText.setText(String.format("%.2f", transportCharges));
        if(insuranceCharges != 0.0)
            insuranceChargesEditText.setText(String.format("%.2f", insuranceCharges));
        if(packingCharges != 0.0)
            packingChargesEditText.setText(String.format("%.2f", packingCharges));
        if(cgstOtherCharges != 0) {
            cgstEditText.setText(String.format("%.2f", this.cgstOtherCharges));
        }
        if(sgstOtherCharges != 0) {
            sgstEditText.setText(String.format("%.2f", this.sgstOtherCharges));
        }
        if(igstOtherCharges != 0) {
            igstEditText.setText(String.format("%.2f", this.igstOtherCharges));
        }
        if(cessOtherCharges != 0) {
            cessEditText.setText(String.format("%.2f", this.cessOtherCharges));
        }

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
                cgstOtherCharges = MyFunctions.parseDouble(cgstEditText.getText().toString());
                sgstOtherCharges = MyFunctions.parseDouble(sgstEditText.getText().toString());
                igstOtherCharges = MyFunctions.parseDouble(igstEditText.getText().toString());
                cessOtherCharges = MyFunctions.parseDouble(cessEditText.getText().toString());

                dialog.dismiss();
                refreshOtherChargesTaxAndTotalAmount();
            }
        });
    }

    private MasterPurchase createMasterPurchaseObject(String invoiceNumber) {
        MasterPurchase masterPurchase = new MasterPurchase();
        if(supplierSelected.getSynced() == 1) {
            masterPurchase.setSupplierID(supplierSelected.getSupplierId());
        } else {
            masterPurchase.setSupplierID("0");
        }
        masterPurchase.setInvoiceNumber(invoiceNumber);
        masterPurchase.setCreatedBy(MyFunctions.parseDouble(this.LoginID));
        masterPurchase.setCreatedDtTm(MyFunctions.getCurrentDateTime());
        masterPurchase.setOutletID(MyFunctions.parseDouble(this.OutletID));
        masterPurchase.setTransportCharge(this.transportCharges);
        masterPurchase.setInsuranceCharge(this.insuranceCharges);
        masterPurchase.setPackingCharge(this.packingCharges);
        masterPurchase.setCGST(this.cgst);
        masterPurchase.setSGST(this.sgst);
        masterPurchase.setIGST(this.igst);
        masterPurchase.setCESS(this.cess);
        masterPurchase.setSubTotal(this.subTotal);
        masterPurchase.setGrandTotal(this.totalAmount);
        masterPurchase.setPurchaseDate(this.invoiceDateString);
        if(supplierSelected.getEntityType().equalsIgnoreCase("Individual")) {
            masterPurchase.setSupplierName(supplierSelected.getContactPerson());
        } else {
            masterPurchase.setSupplierName(supplierSelected.getEntityName());
        }
        masterPurchase.setSupplierEmail(supplierSelected.getContactEmailId());
        masterPurchase.setSupplierMobile(supplierSelected.getContactNumber());
        return masterPurchase;
    }

    private TransactionPurchase createTransactionPurchase(Double masterPurchaseID, PurchasedProduct purchasedProduct) {
        TransactionPurchase transactionPurchase = new TransactionPurchase();
        transactionPurchase.setPurchaseID(masterPurchaseID); // add master sales id
        transactionPurchase.setProductID(purchasedProduct.getProduct().getProductID());
        transactionPurchase.setUnitID(purchasedProduct.getProduct().getUnitID());
        transactionPurchase.setQuantity(purchasedProduct.getQuantity());
        transactionPurchase.setUnitPrice(purchasedProduct.getUnitPrice());
        transactionPurchase.setAmount(purchasedProduct.getTotalAmount());
        transactionPurchase.setProductName(purchasedProduct.getProduct().getProductName());
        transactionPurchase.setProductCode(purchasedProduct.getProduct().getProductCode());
        transactionPurchase.setUnit(purchasedProduct.getProduct().getUnit());
        transactionPurchase.setiCGST(purchasedProduct.getCGST());
        transactionPurchase.setiSGST(purchasedProduct.getSGST());
        transactionPurchase.setiIGST(purchasedProduct.getIGST());
        transactionPurchase.setiCESS(purchasedProduct.getCESS());
        transactionPurchase.setSynced(purchasedProduct.getProduct().getSynced());
        return transactionPurchase;
    }

    private StockLedger createStockLedger(MasterPurchase masterPurchase, TransactionPurchase transactionPurchase) {
        StockLedger stockLedger = new StockLedger();
        stockLedger.setProductID(transactionPurchase.getUnitID());
        stockLedger.setReferenceNumber(masterPurchase.getInvoiceNumber());
        stockLedger.setTransactionType("P");
        stockLedger.setDateOfTransaction(masterPurchase.getPurchaseDate());
        stockLedger.setQuantity(transactionPurchase.getQuantity());
        stockLedger.setDateOfCreation(masterPurchase.getCreatedDtTm());
        stockLedger.setInOut(-1);
        return stockLedger;
    }

    private boolean checkConfirmValidation() {
        if(supplierSelected == null) {
            showOkDialog("Select Supplier");
        } else if(MyFunctions.StringLength(invoiceNumberEditText.getText().toString()) == 0) {
            showOkDialog("Enter Invoice Number");
        } else if(productsSelected.size() == 0) {
            showOkDialog("No product selected");
        } else {
            return true;
        }
        return false;
    }

    public void confirmInvoice() {
        // confirm invoice dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
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

    public void InvoiceInDatabase() {
        String invoiceNumber = invoiceNumberEditText.getText().toString();
        MasterPurchase masterPurchase = createMasterPurchaseObject(invoiceNumber);
        double masterPurchaseID = DatabaseHandler.getInstance().insertMasterPurchase(masterPurchase);
        ArrayList<TransactionPurchase> transactionPurchaseArrayList = new ArrayList<>();
        ArrayList<StockLedger> stockLedgerArrayList = new ArrayList<>();
        for(PurchasedProduct purchasedProduct : productsSelected) {
            transactionPurchaseArrayList.add(createTransactionPurchase(masterPurchaseID, purchasedProduct));
        }
        for(TransactionPurchase transactionPurchase : transactionPurchaseArrayList) {
            stockLedgerArrayList.add(createStockLedger(masterPurchase, transactionPurchase));
        }
        DatabaseHandler.getInstance().addTransactionPurchaseList(transactionPurchaseArrayList);
        DatabaseHandler.getInstance().addStockLedgerList(stockLedgerArrayList);

        // purchase saved
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
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
                NewPurchaseActivity.super.onBackPressed();
            }
        });
    }

    public void onConfirmButtom(View v) {
        if(checkConfirmValidation()) {
             confirmInvoice();
        }
    }

    public void showOkDialog(String heading) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
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

    public void taxDialogClick(View v) {
        if(supplierSelected == null) {
            showOkDialog("Choose Supplier");
            return;
        } else if(productsSelected.size() == 0) {
            showOkDialog("No products selected");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.purchase_tax_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(800, 400);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final EditText cgstEditText = (EditText) view.findViewById(R.id.cgstEditText);
        final EditText sgstEditText = (EditText) view.findViewById(R.id.sgstEditText);
        final EditText igstEditText = (EditText) view.findViewById(R.id.igstEditText);
        final EditText cessEditText = (EditText) view.findViewById(R.id.cessEditText);
        if(cgst != 0) {
            cgstEditText.setText(String.format("%.2f", this.cgst));
        }
        if(sgst != 0) {
            sgstEditText.setText(String.format("%.2f", this.sgst));
        }
        if(igst != 0) {
            igstEditText.setText(String.format("%.2f", this.igst));
        }
        if(cess != 0) {
            cessEditText.setText(String.format("%.2f", this.cess));
        }
        Button saveButton = (Button) view.findViewById(R.id.saveClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cgst = MyFunctions.parseDouble(cgstEditText.getText().toString());
                sgst = MyFunctions.parseDouble(sgstEditText.getText().toString());
                igst = MyFunctions.parseDouble(igstEditText.getText().toString());
                cess = MyFunctions.parseDouble(cessEditText.getText().toString());
                refreshTaxAndTotalAmount();
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

    public void totalAmountClick(View v) {
        if(supplierSelected == null) {
            showOkDialog("Choose Supplier");
            return;
        } else if(productsSelected.size() == 0) {
            showOkDialog("No products selected");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.purchase_total_amount_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(1000, 400);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final EditText totalAmountEditText = (EditText) view.findViewById(R.id.totalAmountEditText);
        if(this.totalAmount != 0) {
            totalAmountEditText.setText(String.format("%.2f", this.totalAmount));
        }
        Button saveButton = (Button) view.findViewById(R.id.saveClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalAmount = MyFunctions.parseDouble(totalAmountEditText.getText().toString());
                refreshTotalAmount();
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

    public void subTotalClick(View v) {
        if(supplierSelected == null) {
            showOkDialog("Choose Supplier");
            return;
        } else if(productsSelected.size() == 0) {
            showOkDialog("No products selected");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPurchaseActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.purchase_subtotal_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(1000, 400);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final EditText subTotalEditText = (EditText) view.findViewById(R.id.subTotalEditText);
        if(this.subTotal != 0) {
            subTotalEditText.setText(String.format("%.2f", this.subTotal));
        }
        Button saveButton = (Button) view.findViewById(R.id.saveClick);
        Button cancelButton = (Button) view.findViewById(R.id.cancelClick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subTotal = MyFunctions.parseDouble(subTotalEditText.getText().toString());
                refreshTaxAndTotalAmount();
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
