package ahmed.yacoubi.e_commerce.caching;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseShop;
import ahmed.yacoubi.e_commerce.model.Category;
import ahmed.yacoubi.e_commerce.model.Product;

public class CachingImage {
    FirebaseShop firebaseShop;
    private StorageReference storageReference;
    private Database database;
    private DatabaseReference shopReference;

    private Activity activity;

    public CachingImage(Activity activity, String userId) {
        this.activity = activity;
        firebaseShop = FirebaseShop.getInstance(activity, userId);
        this.storageReference = FirebaseStorage.getInstance().getReference();
        shopReference = FirebaseDatabase.getInstance().getReference().child("main").child("shop").child("category");

        database = new Database(activity);

    }

    public void categoryCache() {

        storageCategoryImage();
    }

    public void productCache() {
        storageProductImage();

    }

    public void storageCategoryImage() {
        shopReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.child("info").getValue(Category.class);
                    try {
                        if (!database.isFound(category.getName())) {
                            storageReference.child
                                    ("categoryImages").
                                    child(category.getName()).
                                    getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Log.d("qqqqqqq", "run: " + "dounededededededewd");
                                                try {
                                                    Bitmap bitmap = Picasso.with(activity).load(task.getResult()).get();
                                                    database.addImage(bitmap, category.getImage());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }).start();
                                    }
                                }
                            });
                        }


                    } catch (Exception e) {

                    }

                }
                shopReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void storageProductImage() {

        shopReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnap : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot : dataSnap.child("product").getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);

                        if (!database.isFound(product.getName())) {
                            storageReference.child
                                    ("product").child(product.getCategory())
                                    .child(product.getName()).
                                    getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Log.d("qqqqqqq", "run: " + "dounededededededewd");
                                                try {
                                                    Bitmap bitmap = Picasso.with(activity).load(task.getResult()).get();
                                                    database.addImage(bitmap, product.getName());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }).start();
                                    }
                                }
                            });
                        }
                    }


                }
                shopReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
