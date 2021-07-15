package ahmed.yacoubi.e_commerce.firebase;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.model.Category;
import ahmed.yacoubi.e_commerce.model.Product;


public class FirebaseAdminShopping {
    private Activity activity;
    private static FirebaseAdminShopping instance;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private String userId;
    private FirebaseUser currentUser;
    private DatabaseReference shopReference;
    private DatabaseReference adminReference;

    private FirebaseAdminShopping(Activity activity, String userId) {
        this.activity = activity;
        this.mAuth = FirebaseAuth.getInstance();
        this.rootNode = FirebaseDatabase.getInstance();
        this.userId = userId;
        this.currentUser = mAuth.getCurrentUser();
        shopReference = rootNode.getReference().child("main").child("shop").child("category");
//        adminReference = rootNode.getReference("control").child("user").child(currentUser.getUid());

    }


    public static FirebaseAdminShopping getInstance(Activity activity, String userId) {
        if (instance == null) {
            instance = new FirebaseAdminShopping(activity, userId);

        }
        return instance;
    }


    public void addProduct(Product product) {
//        adminReference.child(product.getId() + "").setValue(product.getAmount() + "");
        shopReference.child(product.getCategory()).child("product").child(product.getId() + "").setValue(product);

    }

    public void addCategory(Category category) {
        shopReference.child(category.getName()).setValue(category);

    }

    public void updateProduct(Product product) {

        shopReference.child(product.getId() + "").setValue(product);
    }

    public void deleteProduct(String productId) {

        shopReference.child(productId + "").removeValue();
        adminReference.child(productId).removeValue();

    }

    public void getMyProducts(CallBackProduct callBack) {
        List<String> productId = new ArrayList<>();
        adminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot d :
                        snapshot.getChildren()) {
                    productId.add(d.getKey());
                }
                shopReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        List<Product> productList = new ArrayList<>();
                        for (String s :
                                productId) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (s.equals(dataSnapshot.getKey())) {
                                    productList.add(dataSnapshot.getValue(Product.class));

                                }
                            }

                        }
                        callBack.getProducts(productList);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}
