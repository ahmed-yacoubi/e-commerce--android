package ahmed.yacoubi.e_commerce.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.adapter.RecycleProductItem;
import ahmed.yacoubi.e_commerce.callback.CallBackImage;
import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseShop;
import ahmed.yacoubi.e_commerce.firebase.FirebaseUserShopping;
import ahmed.yacoubi.e_commerce.interfaces.*;
import ahmed.yacoubi.e_commerce.model.Product;
import ahmed.yacoubi.e_commerce.ui.ProductDetails;


public class ProductFragment extends Fragment {

    private RecyclerView fragmentCartRecycle;
    private TextView fragmentCartTvTotalPrice;
    private Button fragmentCartBtnCheckout;
    private RecycleProductItem adapter;
    private FirebaseShop firebaseShop;
    private FirebaseUserShopping firebaseUserShopping;
    private String userId;
    private ClickFragment clickFragment;
    Database database;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickFragment = (ClickFragment) context;

    }

    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getActivity().getSharedPreferences("main", getActivity().MODE_PRIVATE).getString("id", null);
        database = new Database(getActivity());
        firebaseShop = FirebaseShop.getInstance(getActivity(), userId);
        firebaseUserShopping = FirebaseUserShopping.getInstance(getActivity(), userId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        fragmentCartRecycle = view.findViewById(R.id.fragment_cart_recycle);
        installAdapter();

        return view;
    }

    private void installAdapter() {
        adapter = new RecycleProductItem(getActivity(), "product", new
                ClickProduct() {
                    @Override
                    public void onClickProduct(Product product, int type) {
                        if (type == 1) {
                            clickFragment.onClickProduct(product);

                        } else if (type == 2) {
                            firebaseUserShopping.deleteFavoriteProduct(product);

                        } else {
                            firebaseUserShopping.setFavoriteProduct(product);

                        }
                    }
                });
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 2);
        fragmentCartRecycle.setLayoutManager(lm);
        fragmentCartRecycle.setHasFixedSize(true);
        fragmentCartRecycle.setAdapter(adapter);

        firebaseShop.getAllProducts(new CallBackProduct() {
            @Override
            public void getProducts(List<Product> productList) {

                for (int i = 0; i < productList.size(); i++) {
                    int finalI = i;

                    try {
                        byte[] image = database.getImageFromaDataBase(productList.get(finalI).getName());

                        Bitmap b = BitmapFactory.decodeByteArray(image, 0, image.length);
                        productList.get(i).setBitmap(b);
                    } catch (Exception e) {
                    }

//
                }
                adapter.setList(productList);

            }
        });

    }

}