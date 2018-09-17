package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ListActivity.HsnCodeListActivity;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.Brand;
import com.virtuzo.abhishek.modal.HSCCode;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Product;
import com.virtuzo.abhishek.modal.ProductCategory;
import com.virtuzo.abhishek.modal.Unit;

import java.util.ArrayList;

public class NewProductActivity extends AppCompatActivity{

//    Spinner brandSpinner;
    Spinner categorySpinner, subCategorySpinner, unitSpinner;
    EditText productCodeEditText,productNameEditText, itemBarCodeNumberEditText, productDescriptionEditText, salesPriceEditText, mrpEditText;
    EditText taxEditText;
    Button hscCodeButton;
    CheckBox salesPriceCheckBox;
    ArrayAdapter<ProductCategory> categorySpinnerAdapter, subCategorySpinnerAdapter;
    ArrayAdapter<Unit> unitSpinnerAdapter;
//    ArrayAdapter<Brand> brandSpinnerAdapter;

    ArrayList<ProductCategory> categorylist, subCategoryList;
//    ArrayList<Brand> brandSpinnerlist;
    ArrayList<Unit> unitSpinnerList;
    String callingActivity;
    long existingProductId;
    long SubCategoryId = 0;
    String SubCategoryName = "";
    SharedPreferences setting;
    String LoginID, OutletID;
    private final int CHOOSE_HSN_RESPONSE_CODE = 121;
    boolean edittingSyncedProduct = false;
    HSCCode hscCodeSelected = null;

    Button chooseBrandBtn;
    Brand chosenBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        chooseBrandBtn = (Button) findViewById(R.id.chooseBrandBtn);
        chooseBrandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBrand();
            }
        });

        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar.findViewById(R.id.actionBarTitle);
        heading.setText("New Product");

        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        LoginID = setting.getString("UserID", "");
        OutletID = setting.getString("OutletID", "");

        categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        subCategorySpinner = (Spinner)findViewById(R.id.subCategorySpinner);
//        brandSpinner = (Spinner)findViewById(R.id.brandSpinner);
        productCodeEditText = (EditText)findViewById(R.id.productCodeEditText);
        productNameEditText = (EditText)findViewById(R.id.productNameEditText);
        itemBarCodeNumberEditText = (EditText)findViewById(R.id.itemBarCodeNumberEditText);
        productDescriptionEditText = (EditText)findViewById(R.id.productDescriptionEditText);
        salesPriceEditText = (EditText)findViewById(R.id.salesPriceEditText);
        mrpEditText = (EditText)findViewById(R.id.mrpEditText);
        unitSpinner = (Spinner)findViewById(R.id.unitSpinner);
        hscCodeButton = (Button) findViewById(R.id.hscCodeButton);
        taxEditText = (EditText)findViewById(R.id.taxEditText);
        salesPriceCheckBox = (CheckBox) findViewById(R.id.salesPriceCheckBox);

        // filters
        productCodeEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        productNameEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        itemBarCodeNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        productDescriptionEditText.setFilters(new InputFilter[] { MyFunctions.filter });

        categorylist = new ArrayList<>();
        ProductCategory demoProductCategory = new ProductCategory();
        demoProductCategory.setProductCategoryName("-- Choose Category --");
        categorylist.add(demoProductCategory);
        categorylist.addAll(DatabaseHandler.getInstance().getProductCategories(0));
        categorySpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorylist);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerAdapter.notifyDataSetChanged();
        categorySpinner.setAdapter(categorySpinnerAdapter);

        subCategoryList = new ArrayList<>();
        subCategorySpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoryList);
        subCategorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategorySpinnerAdapter.notifyDataSetChanged();
        subCategorySpinner.setAdapter(subCategorySpinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductCategory productCategory = categorylist.get(position);
                subCategoryList.clear();
                ProductCategory demoProductSubCategory = new ProductCategory();
                demoProductSubCategory.setProductCategoryId(0);
                demoProductSubCategory.setProductCategoryName("-- Choose Sub Category --");
                subCategoryList.add(demoProductSubCategory);
                if (position != 0) {
                    subCategoryList.addAll(DatabaseHandler.getInstance().getProductCategories(productCategory.getProductCategoryId()));
                }
                subCategorySpinnerAdapter.notifyDataSetChanged();
                if(SubCategoryId != 0) {
                    for (int i = 0; i < subCategoryList.size(); i++) {
                        if(SubCategoryId == subCategoryList.get(i).getProductCategoryId()) {
                            subCategorySpinner.setSelection(i);
                            SubCategoryId = 0;
                            break;
                        }
                    }
                } else {
                    subCategorySpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subCategoryList.clear();
                ProductCategory demoProductSubCategory = new ProductCategory();
                demoProductSubCategory.setProductCategoryName("-- Choose Sub Category --");
                subCategoryList.add(demoProductSubCategory);
                subCategorySpinnerAdapter.notifyDataSetChanged();
            }
        });

        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        unitSpinnerList = new ArrayList<>();
        Unit demoUnit = new Unit();
        demoUnit.setUnit("-- Choose Unit --");
        unitSpinnerList.add(demoUnit);
        unitSpinnerList.addAll(DatabaseHandler.getInstance().getProductUnits());
        unitSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitSpinnerList);
        unitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinnerAdapter.notifyDataSetChanged();
        unitSpinner.setAdapter(unitSpinnerAdapter);

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        brandSpinnerlist = new ArrayList<>();
//        Brand demoBrand = new Brand();
//        demoBrand.setBrand("-- Choose Brands --");
//        brandSpinnerlist.add(demoBrand);
//        brandSpinnerlist.addAll(DatabaseHandler.getInstance().getBrands());
//        brandSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brandSpinnerlist);
//        brandSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        brandSpinnerAdapter.notifyDataSetChanged();
//        brandSpinner.setAdapter(brandSpinnerAdapter);
//
//        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });

        hscCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewProductActivity.this, HsnCodeListActivity.class);
                intent.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_NEW_PRODUCT);
                startActivityForResult(intent, CHOOSE_HSN_RESPONSE_CODE);
            }
        });

        salesPriceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mrpEditText.setEnabled(false);
                    salesPriceEditText.setEnabled(true);
                    salesPriceEditText.selectAll();
                    salesPriceEditText.setFocusableInTouchMode(true);
                    salesPriceEditText.requestFocus();
                } else {
                    salesPriceEditText.setEnabled(false);
                    mrpEditText.setEnabled(true);
                    mrpEditText.selectAll();
                    mrpEditText.setFocusableInTouchMode(true);
                    mrpEditText.requestFocus();
                }
                MyFunctions.toggleKeyboard(NewProductActivity.this);
            }
        });

        mrpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                refreshSalesPrice();
            }
        });

        callingActivity = getIntent().getExtras().getString(MyFunctions.CLASS_NAME);

        if(callingActivity.equals(MyFunctions.CLASS_PRODUCT_VIEW)) {
            heading.setText("Edit Product");
            setField(getIntent().getExtras().getString("product"));
        }
    }

    private void setField(String productJSON) {
        Gson gson= new Gson();
        Product product= gson.fromJson(productJSON, Product.class);

        existingProductId = product.getProductID();
        for (int i = 0; i < categorylist.size(); i++) {
            if(product.getProductCategoryId() == categorylist.get(i).getProductCategoryId()) {
                categorySpinner.setSelection(i);
                break;
            }
        }
        SubCategoryId = product.getProductSubCategoryId();

//        for (int i = 0; i < brandSpinnerlist.size(); i++) {
//            if(product.getBrandID() == brandSpinnerlist.get(i).getBrandID()) {
//                brandSpinner.setSelection(i);
//                break;
//            }
//        }
        chooseBrandBtn.setText(product.getBrand());
        chosenBrand = new Brand();
        chosenBrand.setBrandID(product.getBrandID());
        chosenBrand.setBrand(product.getBrand());

        productCodeEditText.setText(product.getProductCode());
        productNameEditText.setText(product.getProductName());
        itemBarCodeNumberEditText.setText(product.getBarcode());
        productDescriptionEditText.setText(product.getDescription());
        hscCodeSelected = DatabaseHandler.getInstance().getHSNCode(product.getHSNCode());
        hscCodeButton.setText(String.format("%.2f", product.getTAX()) + "% - " + product.getHSNCode());
        if(product.getTAX() != 0) {
            taxEditText.setText(String.format("%.2f", product.getTAX()));
        }
        if(product.getSalesPrice() != 0) {
            salesPriceEditText.setText(String.format("%.2f", product.getSalesPrice()));
        }
        for (int i = 0; i < unitSpinnerList.size(); i++) {
            if(product.getUnitID() == unitSpinnerList.get(i).getID()) {
                unitSpinner.setSelection(i);
                break;
            }
        }
        if(product.getSynced() != 0) {
            edittingSyncedProduct = true;
            productCodeEditText.setEnabled(false);
        }
    }

    ArrayAdapter<Brand> Brand_Adapter;

    private void chooseBrand() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewProductActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.choose_item_list_layout, null);
        builder.setView(view);
        final android.app.AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final EditText searchEditText = (EditText) view.findViewById(R.id.searchEditText);
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        titleTextView.setText("Choose Brand");

        final ArrayList<Brand> Brand_list = new ArrayList<>();
        Brand_list.addAll(DatabaseHandler.getInstance().getBrands());
        final ArrayList<Brand> tempList = new ArrayList<>();
        tempList.addAll(Brand_list);

        final ListView listView = (ListView) view.findViewById(R.id.listView);

        Brand_Adapter = new ArrayAdapter<Brand>(this, android.R.layout.simple_list_item_1, tempList);
        Brand_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Brand_Adapter.notifyDataSetChanged();
        listView.setAdapter(Brand_Adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newText = searchEditText.getText().toString();
                newText = MyFunctions.lowerCase(newText.trim());

                tempList.clear();
                for(Brand brand : Brand_list) {
                    String name = MyFunctions.lowerCase(brand.getBrand());
                    if (name.contains(newText)) {
                        tempList.add(brand);
                    }
                }
                Brand_Adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenBrand = (Brand) parent.getItemAtPosition(position);
                chooseBrandBtn.setText(chosenBrand.getBrand());
                dialog.dismiss();
            }
        });

    }

    private void refreshSalesPrice() {
        double mrpPrice = MyFunctions.parseDouble(mrpEditText.getText().toString());
        double tax = MyFunctions.parseDouble(taxEditText.getText().toString());
        double salesPrice = (mrpPrice * 100) / (100 + tax);
        salesPriceEditText.setText(String.format("%.2f", salesPrice));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_HSN_RESPONSE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String json = data.getStringExtra("hscCode");
                Gson gson = new Gson();
                hscCodeSelected = gson.fromJson(json, HSCCode.class);
                hscCodeButton.setText(hscCodeSelected.toString());
                taxEditText.setText(String.format("%.2f", hscCodeSelected.getIGST()));
                refreshSalesPrice();
            }
        }
    }

    public void add_SubCategory(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewProductActivity.this);
        final View v = getLayoutInflater().inflate(R.layout.add_product_subcategory_dialog, null);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button addButton = (Button) v.findViewById(R.id.add_Button);
        Button cancel_Button = (Button) v.findViewById(R.id.cancel_Button);
        final EditText add_EditText = (EditText) v.findViewById(R.id.add_EditText);
        add_EditText.setFilters(new InputFilter[] { MyFunctions.filter });
        final Spinner newCategorySpinner = (Spinner) v.findViewById(R.id.newCategorySpinner);

        ArrayList<ProductCategory> newCategorylist = new ArrayList<>();
        ProductCategory demoProductCategory = new ProductCategory();
        demoProductCategory.setProductCategoryName("-- Choose Category --");
        newCategorylist.add(demoProductCategory);
        newCategorylist.addAll(DatabaseHandler.getInstance().getProductCategories(0));
        ArrayAdapter<ProductCategory> newCategorySpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, newCategorylist);
        newCategorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newCategorySpinnerAdapter.notifyDataSetChanged();
        newCategorySpinner.setAdapter(newCategorySpinnerAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newCategorySpinner.getSelectedItemPosition() == 0) {
                    showOkDialog("Select Category");
                    return;
                }
                String subCategoryName = add_EditText.getText().toString();
                if(MyFunctions.StringLength(subCategoryName) == 0) {
                    showOkDialog("Enter Sub Category Name");
                    return;
                }
                ProductCategory productSubCategory = createNewProductSubCategory((ProductCategory)newCategorySpinner.getSelectedItem(), subCategoryName);
                DatabaseHandler.getInstance().addNewProductCategory(productSubCategory);
                categorySpinner.setSelection(0);
                dialog.dismiss();
            }
        });

        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private ProductCategory createNewProductSubCategory(ProductCategory parentProductCategory, String subCategoryName) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(0);
        productCategory.setParentCategoryID(parentProductCategory.getProductCategoryId());
        productCategory.setProductCategoryName(subCategoryName);
        productCategory.setCreatedBy(Integer.parseInt(this.LoginID));
        productCategory.setCreatedDtTm(MyFunctions.getCurrentDateTime());
        productCategory.setOutletID(Integer.parseInt(this.OutletID));
        return productCategory;
    }

    public void add_Brand(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewProductActivity.this);
        final View v = getLayoutInflater().inflate(R.layout.add_new_field_dialog, null);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        Button addButton = (Button) v.findViewById(R.id.add_Button);
        Button cancel_Button = (Button) v.findViewById(R.id.cancel_Button);
        final EditText add_EditText = (EditText) v.findViewById(R.id.add_EditText);
        add_EditText.setFilters(new InputFilter[] { MyFunctions.filter });
        dialogTitle.setText("New Brand");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brandName = add_EditText.getText().toString();
                if(MyFunctions.StringLength(brandName) == 0) {
                    showOkDialog("Enter Brand Name");
                    return;
                }
                Brand brand = createNewBrand(brandName);
                DatabaseHandler.getInstance().addNewBrand(brand);
//                refreshBrandSpinner();
                dialog.dismiss();
            }
        });

        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

//    public void refreshBrandSpinner() {
//        brandSpinnerlist.clear();
//        Brand demoBrand = new Brand();
//        demoBrand.setBrand("-- Choose Brands --");
//        brandSpinnerlist.add(demoBrand);
//        brandSpinnerlist.addAll(DatabaseHandler.getInstance().getBrands());
//        brandSpinnerAdapter.notifyDataSetChanged();
////    }

    private Brand createNewBrand(String brandName) {
        Brand brand = new Brand();
        brand.setBrandID(0);
        brand.setBrand(brandName);
        brand.setCreatedBy(Integer.parseInt(this.LoginID));
        brand.setCreatedDtTm(MyFunctions.getCurrentDateTime());
        return brand;
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    public void saveButton(View view) {
        if(categorySpinner.getSelectedItemPosition() == 0) {
            showOkDialog("Choose Category");
            return;
        }
//        if(brandSpinner.getSelectedItemPosition() == 0) {
//            showOkDialog("Choose Brand");
//            return;
//        }
        if(chosenBrand == null) {
            showOkDialog("Choose Brand");
            return;
        }
        if(productCodeEditText.getText().toString().length() == 0) {
            showOkDialog("Enter Product Code");
            return;
        }
        if(productNameEditText.getText().toString().length() == 0) {
            showOkDialog("Enter Product Name");
            return;
        }
        if(unitSpinner.getSelectedItemPosition() == 0) {
            showOkDialog("Choose Unit");
            return;
        }
        if(hscCodeSelected == null) {
            showOkDialog("Choose HSN Code");
            return;
        }
        String productCode = productCodeEditText.getText().toString();

        // create product object
        final Product product = new Product();

        product.setProductCode(productCode);
        product.setProductName(productNameEditText.getText().toString());
        product.setDescription(productDescriptionEditText.getText().toString());

//        Brand brand = (Brand) brandSpinner.getSelectedItem();
        Brand brand = chosenBrand;
        product.setBrandID(brand.getBrandID());
        product.setBrand(brand.getBrand());

        String barCode = itemBarCodeNumberEditText.getText().toString().trim().replace("\n", "");
        product.setBarcode(barCode);

        double salesPrice = 0;
        String salesPriceString = salesPriceEditText.getText().toString();
        if(MyFunctions.StringLength(salesPriceString) > 0) {
            salesPrice = MyFunctions.parseDouble(salesPriceString);
        }
        if(salesPrice == 0) {
            showOkDialog("Sales Price cannot be zero");
            return;
        }
        product.setSalesPrice(salesPrice);

        product.setHSNCode(hscCodeSelected.getHSCCode());
        product.setHSNID(hscCodeSelected.getID());

        Unit unit = (Unit) unitSpinner.getSelectedItem();
        product.setUnitID(unit.getID());
        product.setUnit(unit.getUnit());

        double tax = 0;
        String taxString = taxEditText.getText().toString();
        if(MyFunctions.StringLength(taxString) > 0) {
            tax = MyFunctions.parseDouble(taxString);
        }
        product.setTAX(tax);

        ProductCategory productCategory = (ProductCategory) categorySpinner.getSelectedItem();
        product.setProductCategoryId(productCategory.getProductCategoryId());

        if(subCategorySpinner.getSelectedItemPosition() == 0) {
            product.setProductSubCategoryId(0);
            product.setProductSubCategoryName("");
        } else {
            ProductCategory subProductCategory = (ProductCategory) subCategorySpinner.getSelectedItem();
            product.setProductSubCategoryId(subProductCategory.getProductCategoryId());
            product.setProductSubCategoryName(subProductCategory.getProductCategoryName());
        }

        product.setCreatedBy(Integer.parseInt(this.LoginID));
        product.setCreatedDtTm(MyFunctions.getCurrentDateTime());
        product.setOutletID(Integer.parseInt(this.OutletID));

        // create product id
        if(callingActivity.equals(MyFunctions.CLASS_PRODUCT_VIEW)) {
            product.setProductID(existingProductId);
            if(DatabaseHandler.getInstance().isProductCodeAlreadyExists(product)) {
                showOkDialog("Product Code already exists");
                return;
            }
            if(edittingSyncedProduct) {
                product.setSynced(DatabaseHandler.PRODUCT_SYNCED_CODE_Edit);
            }

            DatabaseHandler.getInstance().updateCurrentProduct(product);
            AlertDialog.Builder builder = new AlertDialog.Builder(NewProductActivity.this);
            final View v = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
            builder.setView(v);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button okButton = (Button) v.findViewById(R.id.okClick);
            TextView okMessageTextView = (TextView) v.findViewById(R.id.okMessage);
            okMessageTextView.setText("Product Updated!!");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String json = gson.toJson(product);

                    Intent intent = new Intent();
                    intent.putExtra("product", json);
                    setResult(Activity.RESULT_OK, intent);
                    dialog.dismiss();
                    finish();
                }
            });
        } else {
            SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
            String TabCode = setting.getString("TabCode", "");
            String LatestProductID = setting.getString(DashBoardScreen.SharedPref_LatestProductID, "");
            String newProductID = createNewProductID(LatestProductID);

            product.setProductID(Integer.parseInt(newProductID));
            if(DatabaseHandler.getInstance().isProductCodeAlreadyExists(product)) {
                showOkDialog("Product Code already exists");
                return;
            }
            DatabaseHandler.getInstance().addNewProduct(product);

            SharedPreferences.Editor editor = setting.edit();
            editor.putString(DashBoardScreen.SharedPref_LatestProductID, TabCode + newProductID);
            editor.commit();
            AlertDialog.Builder builder = new AlertDialog.Builder(NewProductActivity.this);
            final View v = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
            builder.setView(v);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button okButton = (Button) v.findViewById(R.id.okClick);
            TextView okMessageTextView = (TextView) v.findViewById(R.id.okMessage);
            okMessageTextView.setText("New Product Created!!");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (callingActivity.equals(MyFunctions.CLASS_NEW_INVOICE)) {
                        Gson gson = new Gson();
                        String json = gson.toJson(product);

                        Intent intent = new Intent();
                        intent.putExtra("product", json);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        NewProductActivity.this.onBackPressed();
                    }
                }
            });
        }
    }

    private String createNewProductID(String latestTabCode) {
        String previousProductSeriesString = latestTabCode.substring(latestTabCode.length() - 5);
        int newProductSeries = Integer.parseInt(previousProductSeriesString) + 1;
        String newProductSeriesString = "00000" + newProductSeries;
        newProductSeriesString = newProductSeriesString.substring(newProductSeriesString.length() - 5);
        return newProductSeriesString;
    }

    public void showOkDialog(String heading) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewProductActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final android.app.AlertDialog dialog = builder.create();
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

    public void backButton(View view) {
        super.onBackPressed();
    }
}
