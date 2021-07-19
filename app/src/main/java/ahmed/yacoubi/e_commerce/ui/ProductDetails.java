package ahmed.yacoubi.e_commerce.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseShop;
import ahmed.yacoubi.e_commerce.firebase.FirebaseUserShopping;
import ahmed.yacoubi.e_commerce.model.Product;

public class ProductDetails extends AppCompatActivity {
    private ImageView productItemsImg;
    private ImageView productItemsImgBack;
    private TextView productTvItemsTitle;
    private RatingBar productItemsRate;
    private TextView productItemsTvRateAverage;
    private TextView productItemsTvRateNumber;
    private TextView productItemsTvDesc;
    private Button productItemsBtnAddToCart;
    private Product product;
    private String userId;
    private FirebaseUserShopping firebaseShop;
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        userId = getSharedPreferences("main", MODE_PRIVATE).getString("id", null);
        product = (Product) getIntent().getSerializableExtra("product");
        database = new Database(this);
        product.setBitmap(database.getImage(product.getName()));
        initView();
        setDataView();
        firebaseShop = FirebaseUserShopping.getInstance(this, userId);
        productItemsBtnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setBitmap(null);
                firebaseShop.setCartProduct(product);
                Toast.makeText(ProductDetails.this, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        productItemsImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    //
    private void setDataView() {

        productItemsImg.setImageBitmap(product.getBitmap());

        productTvItemsTitle.setText(product.getName());
        productItemsRate.setMax(5);
        productItemsRate.setRating(product.getRateAverage());
        productItemsTvRateAverage.setText(product.getRateAverage() + "");
        productItemsTvRateNumber.setText("(" + product.getNumOfRate() + ")");
        productItemsTvDesc.setText(product.getDesc());

    }

    private void initView() {
        productItemsImg = findViewById(R.id.product_items_img);
        productItemsImgBack = findViewById(R.id.product_items_img_back);
        productTvItemsTitle = findViewById(R.id.product_tv_items_title);
        productItemsRate = findViewById(R.id.product_items_rate);
        productItemsTvRateAverage = findViewById(R.id.product_items_tv_rate_average);
        productItemsTvRateNumber = findViewById(R.id.product_items_tv_rate_number);
        productItemsTvDesc = findViewById(R.id.product_items_tv_desc);
        productItemsBtnAddToCart = findViewById(R.id.product_items_btn_add_toCart);
    }

}
