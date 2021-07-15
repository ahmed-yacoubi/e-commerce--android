package ahmed.yacoubi.e_commerce.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.caching.CachingImage;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 2500;

    ImageView imageView;
    TextView textView1, textView2;
    Animation top, bottom;
    LinearLayout l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String userId = (getSharedPreferences("main", MODE_PRIVATE).getString("id", null) );

        CachingImage cachingImage = new CachingImage(this, userId);

                cachingImage.categoryCache();


                cachingImage.productCache();

        imageView = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.textView);
        l2 = findViewById(R.id.l2);


        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        imageView.setAnimation(top);
        textView1.setAnimation(bottom);
        l2.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}
