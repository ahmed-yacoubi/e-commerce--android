package ahmed.yacoubi.e_commerce.firebase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.callback.CallBack;
import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.model.Product;
import ahmed.yacoubi.e_commerce.model.User;

public class FirebaseUserShopping {
    private Activity activity;
    private static FirebaseUserShopping instance;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private String userId;
    private FirebaseUser currentUser;
    private DatabaseReference userReference;
    private DatabaseReference shopReference;
    private StorageReference storageReference;
    private Database database;

    private FirebaseUserShopping(Activity activity, String userId) {
        this.activity = activity;
        this.mAuth = FirebaseAuth.getInstance();
        this.rootNode = FirebaseDatabase.getInstance();
        this.userId = userId;
        this.currentUser = mAuth.getCurrentUser();
        this.storageReference = FirebaseStorage.getInstance().getReference();
        database = new Database(activity);
        if (userId != null)
            userReference = rootNode.getReference().child("main").child("user").child(userId);
        shopReference = rootNode.getReference().child("main").child("shop").child("category");
    }


    public static FirebaseUserShopping getInstance(Activity activity, String userId) {
        if (instance == null) {
            instance = new FirebaseUserShopping(activity, userId);

        }
        return instance;
    }

    public void getFavoriteProducts(CallBackProduct callback) {
        userReference.child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    try {
                        storageReference.child
                                ("product").child(product.getCategory()).child(product.getId()).
                                getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!database.isFound(product.getName())) {
                                                Log.d("qqqqqqq", "run: " + "dounededededededewd");
                                                try {
                                                    Bitmap bitmap = Picasso.with(activity).load(task.getResult()).get();
                                                    database.addImage(bitmap, product.getName());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        }
                                    }).start();
                                }
                            }
                        });
                    } catch (Exception e) {

                    }

                    productList.add(product);
                }
                callback.getProducts(productList);
                userReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getCartProducts(CallBackProduct callback) {
        userReference.child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);


                    productList.add(product);
                }
                callback.getProducts(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setFavoriteProduct(Product product) {
        product.setBitmap(null);
        userReference.child("favorite").child(product.getId() + "").setValue(product);

    }

    public void setCartProduct(Product product) {
        product.setBitmap(null);
        userReference.child("cart").child(product.getName() + "").setValue(product);
    }

    public void buyProduct(List<Product> productList, User user, CallBack callBack) {

        if (checkBalanceAndAmount(productList, user)) {

            for (Product product : productList) {
                product.setAmount((product.getAmount() - product.getCount()));
                user.setMoney((user.getMoney() - (product.getPrice() * product.getCount())));
                user.setSpentMoney(user.getSpentMoney() + (product.getPrice() * product.getCount()));
                userReference.child("info").setValue(user);
                userReference.child("cart").child(product.getId() + "").removeValue();
                product.setBitmap(null);
                userReference.child("buyed").child(product.getId() + "").setValue(product);
                product.setCount(0);

                shopReference.child(product.getCategory()).child("product").child(product.getId() + "").setValue(product);
            }

            callBack.getResult("done");

        } else {

            callBack.getResult("money");

        }
    }

    public boolean checkBalanceAndAmount(List<Product> list, User user) {
        double userMoney = user.getMoney();
        double totalMoney = 0;
        for (Product product : list) {
            totalMoney += (product.getPrice() * product.getCount());
        }
        if (userMoney >= totalMoney) {
            return true;

        } else {
            return false;

        }
    }

    public void deleteCartProduct(Product product) {
        product.setCount(0);
        userReference.child("cart").child(product.getId() + "").removeValue();
    }

    public void deleteFavoriteProduct(Product product) {
        userReference.child("favorite").child(product.getId() + "").removeValue();
    }
}

/*
*     public void buyProduct(Product product, User user, CallBack callBack) {

        if (user.getMoney() >= product.getPrice() * product.getCount()) {
            if (product.getAmount() >= product.getAmount()) {
                product.setAmount(product.getAmount() - product.getCount());
                user.setMoney(user.getMoney() - (product.getPrice() * product.getCount()));
                user.setSpentMoney(user.getSpentMoney() + (product.getPrice() * product.getCount()));
                userReference.child("info").setValue(user);
                userReference.child("cart").child(product.getId() + "").removeValue();
                userReference.child("buyed").child(product.getId() + "").setValue(product);
                product.setCount(0);
                shopReference.child(product.getId() + "").setValue(product);
                callBack.getResult("done");
            } else {
                callBack.getResult("amount");
            }
        } else {
            callBack.getResult("money");
        }

    }
*/
