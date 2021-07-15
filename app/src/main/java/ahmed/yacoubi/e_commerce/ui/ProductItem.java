package ahmed.yacoubi.e_commerce.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.adapter.RecycleProductItem;
import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseShop;
import ahmed.yacoubi.e_commerce.firebase.FirebaseUserShopping;
import ahmed.yacoubi.e_commerce.interfaces.ClickProduct;
import ahmed.yacoubi.e_commerce.model.Product;

public class ProductItem extends AppCompatActivity {
    private RecyclerView productItem_recycle;
    private TextView fragmentCartTvTotalPrice;
    private Button fragmentCartBtnCheckout;
    private RecycleProductItem adapter;
    private FirebaseShop firebaseShop;
    private String categoryId;
    private String userId;
    private FirebaseUserShopping firebaseUserShopping;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item);
        userId = getSharedPreferences("main", MODE_PRIVATE).getString("id", null);
        firebaseUserShopping = FirebaseUserShopping.getInstance(this, userId);
        productItem_recycle = findViewById(R.id.productItem_recycle);
        firebaseShop = FirebaseShop.getInstance(this, userId);
        database = new Database(this);
        categoryId = getIntent().getStringExtra("id");
        installAdapter();


    }

    private void installAdapter() {
        adapter = new RecycleProductItem(ProductItem.this, "product", new
                ClickProduct() {
                    @Override
                    public void onClickProduct(Product product, int type) {
                        if (type == 1) {
                            Intent intent = new Intent(getBaseContext(), ProductDetails.class);
                            product.setBitmap(null);
                            intent.putExtra("product", product);
                            startActivity(intent);

                        } else if (type == 2) {
                            firebaseUserShopping.deleteFavoriteProduct(product);

                        } else {
                            firebaseUserShopping.setFavoriteProduct(product);

                        }
                    }
                });
        RecyclerView.LayoutManager lm = new GridLayoutManager(this, 2);
        productItem_recycle.setLayoutManager(lm);
        productItem_recycle.setHasFixedSize(true);
        productItem_recycle.setAdapter(adapter);

        firebaseShop.getProductsByCategory(categoryId, new CallBackProduct() {
            @Override
            public void getProducts(List<Product> productList) {

                for (int i = 0; i < productList.size(); i++) {

                    Bitmap bitmap = database.getImage(productList.get(i).getName());
                    productList.get(i).setBitmap(bitmap);
                }
                adapter.setList(productList);
            }
        });

    }


}