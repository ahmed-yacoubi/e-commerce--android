package ahmed.yacoubi.e_commerce.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.adapter.RecycleProductItem;
import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseShop;
import ahmed.yacoubi.e_commerce.firebase.FirebaseUserShopping;
import ahmed.yacoubi.e_commerce.interfaces.*;
import ahmed.yacoubi.e_commerce.model.Product;


public class FavoriteFragment extends Fragment {


    private RecyclerView fragmentCartRecycle;
    private RecycleProductItem adapter;
    private ClickFragment clickFragment;
    private FirebaseUserShopping firebaseShop;
    private String userId;
    private Database database;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickFragment = (ClickFragment) context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseShop = FirebaseUserShopping.getInstance(getActivity(), userId);
        userId = getActivity().getSharedPreferences("main", getActivity().MODE_PRIVATE).getString("id", null);
        database=new Database(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView(view);
        installAdapter();

        return view;
    }

    private void installAdapter() {
        adapter = new RecycleProductItem(getActivity(), "favorite", new ClickProduct() {
            @Override
            public void onClickProduct(Product product, int type) {
                if (type == 1) {
                    clickFragment.onClickProduct(product);
                } else if (type == 2) {
                    firebaseShop.setCartProduct(product);
                } else if (type == 3) {
                    firebaseShop.deleteFavoriteProduct(product);

                }
            }
        });
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 1);
        fragmentCartRecycle.setLayoutManager(lm);
        fragmentCartRecycle.setHasFixedSize(true);
        fragmentCartRecycle.setAdapter(adapter);
        firebaseShop.getFavoriteProducts(new CallBackProduct() {
            @Override
            public void getProducts(List<Product> productList) {

                for (int i = 0; i < productList.size(); i++) {
                    try {
                        Bitmap bitmap = database.getImage(productList.get(i).getName());
                        productList.get(i).setBitmap(bitmap);
                    }catch (Exception e){}

                }
                adapter.setList(productList);
            }
        });

    }

    private void initView(View view) {
        fragmentCartRecycle = view.findViewById(R.id.fragment_cart_recycle);
    }


}