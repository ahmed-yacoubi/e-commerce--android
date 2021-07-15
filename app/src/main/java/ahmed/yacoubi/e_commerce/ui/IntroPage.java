package ahmed.yacoubi.e_commerce.ui;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.adapter.IntroViewPagerAdapter;
import ahmed.yacoubi.e_commerce.fragment.IntroFragment;
import ahmed.yacoubi.e_commerce.model.IntroItem;

public class IntroPage extends FragmentActivity {
    ViewPager2 viewPager;
    IntroViewPagerAdapter adapter;
    TabLayout tabLayout;
    Button btn_getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);
        viewPager = findViewById(R.id.intro_page_viewPager);
        tabLayout = findViewById(R.id.intro_page_tablayout);
        btn_getStarted = findViewById(R.id.intro_page_btn_start);
        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),SignUp.class));
            }
        });
        installViewPager();
        installTapLayout();
    }

    private void installTapLayout() {
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setIcon(R.drawable.dot_color);
            }
        });
        tabLayoutMediator.attach();
    }

    private void installViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(IntroFragment.newInstance(new IntroItem(R.drawable.svg_into_image, "View product 360 degrees", "You can see the product with all angles, true and convenient")));
        fragments.add(IntroFragment.newInstance(new IntroItem(R.drawable.svg_intro_image1, "Find products easily", "You just need to take a photo or upload and we will find similar products for you.")));
        fragments.add(IntroFragment.newInstance(new IntroItem(R.drawable.ic_svg_intro_image2, "Payment is easy", "You just need to take a photo or upload and we will find similar products for you.")));
        adapter = new IntroViewPagerAdapter(this, getBaseContext(), fragments);
        viewPager.setAdapter(adapter);
    }

}