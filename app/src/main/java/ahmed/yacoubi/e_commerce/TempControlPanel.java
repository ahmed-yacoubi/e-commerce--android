package ahmed.yacoubi.e_commerce;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ahmed.yacoubi.e_commerce.firebase.FirebaseAdminShopping;
import ahmed.yacoubi.e_commerce.firebase.FirebaseShop;
import ahmed.yacoubi.e_commerce.model.Category;
import ahmed.yacoubi.e_commerce.model.Product;

public class TempControlPanel {

    public static void setTempData(Activity activity) {
        addCategory(activity);
        addProducts(activity);
    }

    public static void setImage(String productId, Uri uriImage) {

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference("image").child("product").
                child(productId);

        storageRef.putFile(uriImage);
//                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//
//            @Override
////            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
////                if (task.isSuccessful()) {
////
////                }
////            }
////
////
//        });

    }

    public static void addCategory(Activity activity) {
        FirebaseAdminShopping firebase = FirebaseAdminShopping.getInstance(activity, "321");

        for (int i = 0; i < 20; i++) {

            Category category = new Category();
            category.setDesc("desc cat");
//            category.setImage(R.drawable.ss);
            category.setName("cat" + i);
            category.setId("cat" + i);
            if (i % 2 == 0)
                category.setSmall(true);
            else
                category.setSmall(false);

            firebase.addCategory(category);
        }

    }

    public static void addProducts(Activity activity) {
        FirebaseAdminShopping firebase = FirebaseAdminShopping.getInstance(activity, "32");

        for (int i = 0; i < 100; i++) {
            Product product = new Product();

            product.setAmount(4);
            product.setDesc("desc desc desc desc desc");
            product.setFavorite(true);
            product.setName("product" + i);
            product.setNumOfRate(40);
            product.setRateAverage(4.5f);
            product.setPrice(50.3 + i);
//            product.setImage(R.drawable.ss);
            product.setId(i+"");
            if (i % 2 == 0)
                product.setCategory("cat1");
            else
                product.setCategory("cat2");

            firebase.addProduct(product);
        }


    }


}
