package com.virtuzo.abhishek.Views.Activity.ListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewProductActivity;
import com.virtuzo.abhishek.Views.Activity.ViewActivity.ProductViewActivity;
import com.virtuzo.abhishek.Views.Adapters.ProductListAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Product;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ArrayList<Product> listOfProducts;
    RecyclerView recyclerView;
    String BusinessId;
    private ProductListAdapter productListAdapter;
    private final int CHOOSE_PRODUCT_RESPONSE_CODE = 101;
    final int NUMBER_OF_COLUMNS = 3;
    boolean refreshButtonClicked = false;
    SearchView searchView;
    String callingActivity;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setTitle("Products");
        getSupportActionBar().hide();
        Toolbar toolbar_in_product_list = (Toolbar) findViewById(R.id.toolbar_in_product_list);
        toolbar_in_product_list.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar_in_product_list.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar_in_product_list.findViewById(R.id.actionBarTitle);
        heading.setText("Products List");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // search bar for product list
        searchView = (SearchView) findViewById(R.id.searchProductListSearchView);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Product by name and brand");
        setSearchViewHeight(searchView, 25);
        searchView.setIconified(false);

        callingActivity = getIntent().getExtras().getString(MyFunctions.CLASS_NAME);

        listOfProducts = new ArrayList<>();
        BusinessId = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0).getString("BusinessId", "");

        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // on clicking a product
        productListAdapter = new ProductListAdapter(listOfProducts, new ProductListAdapter.OnClickListener() {
            @Override
            public void onItemClick(Product product) {

                Gson gson = new Gson();
                String json = gson.toJson(product);

                if(callingActivity.equals(MyFunctions.CLASS_DASHBOARD)) {
                    startActivity(new Intent(ProductListActivity.this, ProductViewActivity.class).putExtra("product", json));
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("product", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(productListAdapter);

    }

    public void refreshProductList() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        listOfProducts.clear();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                listOfProducts.addAll(DatabaseHandler.getInstance().getProductsListFromDB());
                productListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }, 1000);
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = MyFunctions.lowerCase(newText.trim());
        ArrayList<Product> newList = new ArrayList<>();
        for(Product product : listOfProducts) {
            String name = MyFunctions.lowerCase(product.getProductName());
            String brand = MyFunctions.lowerCase(product.getBrand());
            if(name.contains(newText) || brand.contains(newText)){
                newList.add(product);
            }
        }
        productListAdapter.setFilter(newList);
        return true;
    }

    @Override
    protected void onResume() {
        //TODO update list
        refreshProductList();

        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void addNewProduct(View view) {
        Intent intentNewProduct = new Intent(getApplicationContext(), NewProductActivity.class);
        if(callingActivity.equals(MyFunctions.CLASS_DASHBOARD)){
            intentNewProduct.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
//        }else if(callingActivity.equals(MyFunctions.CLASS_NEW_INVOICE)){
//            intentShowCustomerList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_NEW_INVOICE);
        }
        startActivityForResult(intentNewProduct, CHOOSE_PRODUCT_RESPONSE_CODE);
    }

}
