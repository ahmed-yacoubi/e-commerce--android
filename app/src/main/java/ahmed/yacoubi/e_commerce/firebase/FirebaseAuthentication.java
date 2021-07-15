package ahmed.yacoubi.e_commerce.firebase;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import ahmed.yacoubi.e_commerce.callback.CallBack;
import ahmed.yacoubi.e_commerce.callback.CallBackUser;
import ahmed.yacoubi.e_commerce.model.User;

public class FirebaseAuthentication {
    private Activity activity;
    private static FirebaseAuthentication instance;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private String userId;
    private FirebaseUser currentUser;

    private FirebaseAuthentication(Activity activity, String userId) {
        this.activity = activity;
        this.mAuth = FirebaseAuth.getInstance();
        this.rootNode = FirebaseDatabase.getInstance();
        this.userId = userId;
        if (userId != null)
            this.currentUser = mAuth.getCurrentUser();
    }


    public static FirebaseAuthentication getInstance(Activity activity, String userId) {
        if (instance == null) {
            instance = new FirebaseAuthentication(activity, userId);

        }
        return instance;
    }


    public void signUp(final User user, CallBack callBack) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            currentUser = mAuth.getCurrentUser();
                            userId = mAuth.getUid();
                            user.setId(userId);
                            rootNode.getReference("main").child("user").child(userId).child("info").setValue(user);
                            callBack.getResult("done");
                        } else {
                            callBack.getResult("fail");
                        }

                    }
                });
    }

    public void updaterUser(FirebaseUser user) {// space = 32
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName("anyname").setPhotoUri(Uri.parse("anyData"))
                .build();
        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

    }

    public void getCurrentUser(String userId, CallBackUser callBackUser) {
        DatabaseReference reference = rootNode.getReference("main").child("user").child(userId).child("info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                callBackUser.getUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void logIn(String email, String password, CallBack callBack) {
        this.mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                currentUser = mAuth.getCurrentUser();
                callBack.getResult(currentUser.getUid());

            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.getResult("fail");

            }
        });
    }

    public void changePassword(String userId, String oldPassword, String newPassword, CallBack callBack) {
        DatabaseReference reference = rootNode.getReference("main").child("user").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (oldPassword.equals(user.getPassword())) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.setPassword(newPassword);
                                reference.setValue(user);
                                callBack.getResult("done");

                            } else {
                                callBack.getResult("fail");

                            }

                        }
                    });
                    reference.removeEventListener(this);// ممكن تعمل ايرور

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void changeName(String userId, String name, CallBack callBack) {
        DatabaseReference reference = rootNode.getReference("main").child("user").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.setName(name);
                reference.setValue(user);
                reference.removeEventListener(this);// ممكن تعمل ايرور
                callBack.getResult("done");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void changePhone(String userId, String phone, CallBack callBack) {
        DatabaseReference reference = rootNode.getReference("main").child("user").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.setPhone(phone);
                reference.setValue(user);
                reference.removeEventListener(this);// ممكن تعمل ايرور
                callBack.getResult("done");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
