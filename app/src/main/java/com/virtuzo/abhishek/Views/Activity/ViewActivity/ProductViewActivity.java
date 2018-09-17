package com.virtuzo.abhishek.Views.Activity.ViewActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewProductActivity;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Product;

public class ProductViewActivity extends AppCompatActivity {

    String productJSON;
    int EDIT_PRODUCT_RESPONSE_CODE = 100;
    TextView productCodeLabel, productNameLabel, productDescriptionLabel, brandLabel, categoryLabel,
            subCategoryLabel, barCodeLabel, taxLabel, salesPriceLabel, unitLabel, hscCodeLabel,
            productCode, productName, productDescription, brand, category, subCategory, barCode,
            tax, salesPrice, unit, hscCode;
    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar.findViewById(R.id.actionBarTitle);
        heading.setText("Product Details");

        productCode= (TextView)findViewById(R.id.productCode);
        productName= (TextView)findViewById(R.id.productName);
        productDescription= (TextView)findViewById(R.id.productDescription);
        brand= (TextView)findViewById(R.id.brand);
        category= (TextView)findViewById(R.id.category);
        subCategory= (TextView)findViewById(R.id.subCategory);
        barCode= (TextView)findViewById(R.id.barCode);
        tax= (TextView)findViewById(R.id.tax);
        salesPrice= (TextView)findViewById(R.id.salesPrice);
        unit= (TextView)findViewById(R.id.unit);
        hscCode= (TextView)findViewById(R.id.hscCode);

        productCodeLabel = (TextView) findViewById(R.id.productCodeLabel);
        productNameLabel = (TextView) findViewById(R.id.productNameLabel);
        productDescriptionLabel = (TextView) findViewById(R.id.productDescriptionLabel);
        brandLabel = (TextView) findViewById(R.id.brandLabel);
        categoryLabel = (TextView) findViewById(R.id.categoryLabel);
        subCategoryLabel = (TextView) findViewById(R.id.subCategoryLabel);
        barCodeLabel = (TextView) findViewById(R.id.barCodeLabel);
        taxLabel = (TextView) findViewById(R.id.taxLabel);
        salesPriceLabel = (TextView) findViewById(R.id.salesPriceLabel);
        unitLabel = (TextView) findViewById(R.id.unitLabel);
        hscCodeLabel = (TextView) findViewById(R.id.hscCodeLabel);

        productJSON= getIntent().getExtras().getString("product");
        Gson gson= new Gson();
        currentProduct= gson.fromJson(productJSON, Product.class);
        setProduct(currentProduct);
    }

    public void setProduct(Product product) {
        productCode.setText(product.getProductCode());
        productName.setText(product.getProductName());
        if(MyFunctions.StringLength(product.getDescription()) == 0) {
            productDescription.setVisibility(View.GONE);
            productDescriptionLabel.setVisibility(View.GONE);
        } else {
            productDescription.setVisibility(View.VISIBLE);
            productDescriptionLabel.setVisibility(View.VISIBLE);
            productDescription.setText(product.getDescription());
        }
        brand.setText(product.getBrand());
        category.setText(DatabaseHandler.getInstance().getProductCategoryName(product.getProductCategoryId()));
        if(product.getProductSubCategoryId() == 0) {
            subCategory.setVisibility(View.GONE);
            subCategoryLabel.setVisibility(View.GONE);
        } else {
            subCategory.setVisibility(View.VISIBLE);
            subCategoryLabel.setVisibility(View.VISIBLE);
            subCategory.setText(DatabaseHandler.getInstance().getProductCategoryName(product.getProductSubCategoryId()));
        }
        if(MyFunctions.StringLength(product.getBarcode()) == 0) {
            barCode.setVisibility(View.GONE);
            barCodeLabel.setVisibility(View.GONE);
        } else {
            barCode.setVisibility(View.VISIBLE);
            barCodeLabel.setVisibility(View.VISIBLE);
            barCode.setText(product.getBarcode());
        }
        tax.setText(String.format("%.2f", product.getTAX()) + " %");
        String rupeeSymbol = "\u20B9";
        salesPrice.setText(rupeeSymbol + String.format("%.2f", product.getSalesPrice()));
        unit.setText(product.getUnit());
        hscCode.setText(product.getHSNCode());
    }

    public void onEdit(View view){
        Intent intent=new Intent(this, NewProductActivity.class);
        intent.putExtra("product", productJSON);
        intent.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_PRODUCT_VIEW);
        startActivityForResult(intent, EDIT_PRODUCT_RESPONSE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PRODUCT_RESPONSE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String json = data.getStringExtra("product");
                Gson gson = new Gson();
                Product product = gson.fromJson(json, Product.class);
                setProduct(product);
                productJSON = gson.toJson(product);
            }
        }
    }

    public void onOK(View view){
        super.onBackPressed();
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

}

