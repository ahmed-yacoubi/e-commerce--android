package ahmed.yacoubi.e_commerce.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.TempControlPanel;
import ahmed.yacoubi.e_commerce.adapter.HomeViewPagerAdapter;
import ahmed.yacoubi.e_commerce.fragment.CartFragment;
import ahmed.yacoubi.e_commerce.fragment.FavoriteFragment;
import ahmed.yacoubi.e_commerce.fragment.HomeFragment;
import ahmed.yacoubi.e_commerce.fragment.ProfileFragment;
import ahmed.yacoubi.e_commerce.fragment.ProductFragment;
import ahmed.yacoubi.e_commerce.interfaces.ClickFragment;
import ahmed.yacoubi.e_commerce.model.Product;

public class MainPage extends AppCompatActivity implements ClickFragment, HomeFragment.SendDataToActivity {
    private static final int PERMISSON_REQ_CODE = 5;

    public ViewPager2 mainPageViewpager;
    public TabLayout mainPageTablayout;
    public HomeViewPagerAdapter adapter;
    List<Fragment> fragmentList = new ArrayList<>();
    private String userId;
    //main
    //  user
    //      u1(id , name , password , image , email)
    //          cart(totalPrice)
    //              p1((id, name, color, desc, amount,  price , category,  image,  rateAverage, numOfRate))
    //          favorite
    //              p1(id, name, color, desc, amount,  price , category,  image,  rateAverage, numOfRate)
    //  shop
    //      category (id , name , image)
//          category (id1 ,name2,image1 , isSmall)
    //              product
    //                      p1((id, name, color, desc, amount,  price , category,  image,  rateAverage, numOfRate))
    //                      p2((id, name, color, desc, amount,  price , category,  image,  rateAverage, numOfRate))

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        userId = getSharedPreferences("main", MODE_PRIVATE).getString("id", null);
        initView();
        initViewPager();
        initTabLayout();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSON_REQ_CODE);

        }


//        TempControlPanel.setTempData(this);
    }

    private void initView() {
        mainPageViewpager = findViewById(R.id.mainPage_viewpager);
        mainPageTablayout = findViewById(R.id.mainPage_tablayout);
    }

    private void initTabLayout() {


        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mainPageTablayout, mainPageViewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setIcon(R.drawable.home);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.search);

                        break;
                    case 2:
                        tab.setIcon(R.drawable.cart);

                        break;

                    case 3:
                        tab.setIcon(R.drawable.favorite);

                        break;
                    case 4:
                        tab.setIcon(R.drawable.profile);

                        break;

                }

            }
        });
        tabLayoutMediator.attach();
    }


    private void initViewPager() {
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(ProductFragment.newInstance());
        fragmentList.add(CartFragment.newInstance());
        fragmentList.add(FavoriteFragment.newInstance());
        fragmentList.add(ProfileFragment.newInstance());
        adapter = new HomeViewPagerAdapter(this, fragmentList);
        mainPageViewpager.setAdapter(adapter);
    }


    @Override
    public void onClickProduct(Product product) {
        Intent intent = new Intent(getApplicationContext(), ProductDetails.class);
//        product.setBitmap(null);
        product.setBitmap(null);
        intent.putExtra("product", product);
//        Log.d("eeeeee", "onCreate: "+product.getName());

        startActivity(intent);
    }

    @Override
    public void sendCategoryId(String categoryId) {
        Intent intent = new Intent(getBaseContext(), ProductItem.class);
        intent.putExtra("id", categoryId);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSON_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {


                }
        }
    }
}

