package ahmed.yacoubi.e_commerce.firebase;

import android.app.Activity;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import ahmed.yacoubi.e_commerce.callback.CallBackCategory;
import ahmed.yacoubi.e_commerce.callback.CallBackImage;
import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.model.Category;
import ahmed.yacoubi.e_commerce.model.Product;

public class FirebaseShop {
    private Activity activity;
    private static FirebaseShop instance;
    private FirebaseDatabase rootNode;
    private DatabaseReference shopReference;
    private String userId;

    private FirebaseShop(Activity activity, String userId) {
        this.userId = userId;
        this.activity = activity;
        this.rootNode = FirebaseDatabase.getInstance();
        shopReference = rootNode.getReference().child("main").child("shop").child("category");
    }


    public static FirebaseShop getInstance(Activity activity, String userId) {
        if (instance == null) {
            instance = new FirebaseShop(activity, userId);

        }
        return instance;
    }

    public void getProductsByCategory(String idCategory, CallBackProduct callback) {
        FirebaseUserShopping firebaseUserShopping = FirebaseUserShopping.getInstance(activity, userId);
        firebaseUserShopping.getFavoriteProducts(new CallBackProduct() {
            @Override
            public void getProducts(List<Product> loveProduct) {
                shopReference.child(idCategory).child("product").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Product> productList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Product product = dataSnapshot.getValue(Product.class);
                            Log.d("wwwwwww", "onDataChange: " + product.getName() + "idCategory, " + idCategory);

                            for (Product p : loveProduct) {

                                if (p.getName().equals(product.getName())) {

                                    product.setFavorite(true);
                                    break;
                                } else {

                                    product.setFavorite(false);

                                }
                            }

                            productList.add(product);


                        }
                        callback.getProducts(productList);
                        shopReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    public void getCategories(CallBackCategory callback) {
        shopReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> categoryList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Category category = dataSnapshot.child("info").getValue(Category.class);

                    categoryList.add(category);
                }
                callback.getCategories(categoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }


    public void getAllProducts(CallBackProduct callBack) {
        FirebaseUserShopping firebaseUserShopping = FirebaseUserShopping.getInstance(activity, userId);
        firebaseUserShopping.getFavoriteProducts(new CallBackProduct() {
            @Override
            public void getProducts(List<Product> loveProduct) {


                shopReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Product> productList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot dataSnap : dataSnapshot.child("product").getChildren()) {


                                Product product = dataSnap.getValue(Product.class);
                                Log.d("qqqqqq", "onDataChange: " + product.getName());
                                for (Product p : loveProduct) {

                                    if (p.getName().equals(product.getName())) {

                                        product.setFavorite(true);
                                        break;
                                    } else {
//
                                        product.setFavorite(false);
//
                                    }
                                }

                                productList.add(product);


                            }
                        }
                        callBack.getProducts(productList);
                        shopReference.removeEventListener(this);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

}
