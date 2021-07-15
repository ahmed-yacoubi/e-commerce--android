package ahmed.yacoubi.e_commerce.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.adapter.RecycleProductItem;
import ahmed.yacoubi.e_commerce.callback.CallBack;
import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.callback.CallBackUser;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseAuthentication;
import ahmed.yacoubi.e_commerce.firebase.FirebaseShop;
import ahmed.yacoubi.e_commerce.firebase.FirebaseUserShopping;
import ahmed.yacoubi.e_commerce.interfaces.*;
import ahmed.yacoubi.e_commerce.model.Product;
import ahmed.yacoubi.e_commerce.model.User;

public class CartFragment extends Fragment {


    private RecyclerView fragmentCartRecycle;
    private TextView fragmentCartTvTotalPrice;
    private Button fragmentCartBtnCheckout;
    private RecycleProductItem adapter;
    private FirebaseUserShopping firebaseShop;
    private String userId;
    private User currentUser;
    private double totalPriceCart = 0;
    private FirebaseAuthentication firebaseAuthentication;
    Database database;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userId = getActivity().getSharedPreferences("main", getActivity().MODE_PRIVATE).getString("id", null);
        firebaseShop = FirebaseUserShopping.getInstance(getActivity(), userId);
        firebaseAuthentication = FirebaseAuthentication.getInstance(getActivity(), userId);
        firebaseAuthentication.getCurrentUser(userId, new CallBackUser() {
            @Override
            public void getUser(User user) {
                currentUser = user;
            }
        });
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new Database(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        installAdapter();
        fragmentCartBtnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> productList = adapter.getList();

                firebaseShop.buyProduct(productList, currentUser, new CallBack() {
                    @Override
                    public void getResult(String result) {
                        if (result.equals("done")) {
                            Toast.makeText(getActivity(), "buy successfully", Toast.LENGTH_SHORT).show();
                            fragmentCartTvTotalPrice.setText(0 + "$");
                        } else if (result.equals("money")) {
                            Toast.makeText(getActivity(), "You don't have enough money", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        adapter.setAddCounter(new ClickCounter() {
            @Override
            public void addCount(double price) {
                fragmentCartTvTotalPrice.setText(adapter.round(price, 3) + "");
            }
        });
        return view;
    }

    private void installAdapter() {

        adapter = new RecycleProductItem(getActivity(), "cart", new ClickProduct() {
            @Override
            public void onClickProduct(Product product, int type) {
                firebaseShop.deleteCartProduct(product);

            }
        });
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 1);
        fragmentCartRecycle.setLayoutManager(lm);
        fragmentCartRecycle.setHasFixedSize(true);
        fragmentCartRecycle.setAdapter(adapter);
        firebaseShop.getCartProducts(new CallBackProduct() {
            @Override
            public void getProducts(List<Product> productList) {
                for (int i = 0; i < productList.size(); i++) {
                    try {
                        Bitmap bitmap = database.getImage(productList.get(i).getName());
                        productList.get(i).setBitmap(bitmap);
                    }catch (Exception e){}

                }
                adapter.setList(productList);
                fragmentCartTvTotalPrice.setText(adapter.getTotalPrice() + "$");

            }
        });
    }

    private void initView(View view) {
        fragmentCartRecycle = view.findViewById(R.id.fragment_cart_recycle);
        fragmentCartTvTotalPrice = view.findViewById(R.id.fragment_cart_tv_totalPrice);
        fragmentCartBtnCheckout = view.findViewById(R.id.fragment_cart_btn_checkout);
    }


}