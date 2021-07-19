package ahmed.yacoubi.e_commerce.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.adapter.RecycleCategory;
import ahmed.yacoubi.e_commerce.adapter.RecycleProductItem;
import ahmed.yacoubi.e_commerce.callback.CallBackCategory;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseShop;
import ahmed.yacoubi.e_commerce.interfaces.*;
import ahmed.yacoubi.e_commerce.model.Category;

public class HomeFragment extends Fragment {
    private RecycleCategory adapter;
    private RecyclerView fragmentHomeRec;
    private Database database;
    private FirebaseShop firebaseShop;
    private SendDataToActivity sendDataToActivity;
    private String userId;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sendDataToActivity = (SendDataToActivity) context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new Database(getActivity());
        userId = getActivity().getSharedPreferences("main", getActivity().MODE_PRIVATE).getString("id", null);
        firebaseShop = FirebaseShop.getInstance(getActivity(), userId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fragmentHomeRec = view.findViewById(R.id.fragment_home_rec);
        installCategoryAdapter();
        return view;
    }

    private void installCategoryAdapter() {

        adapter = new RecycleCategory(getActivity(), new ClickCategory() {
            @Override
            public void onClickCategory(String categoryId) {
                sendDataToActivity.sendCategoryId(categoryId);
            }
        });


        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 1);
        fragmentHomeRec.setHasFixedSize(true);
        fragmentHomeRec.setLayoutManager(lm);
        fragmentHomeRec.setAdapter(adapter);
        firebaseShop.getCategories(new CallBackCategory() {
            @Override
            public void getCategories(List<Category> categoryList) {
                for (int i = 0; i < categoryList.size(); i++) {

                    Bitmap bitmap = database.getImage(categoryList.get(i).getName());
                    categoryList.get(i).setBitmap(bitmap);

                }

                adapter.setList(categoryList);
            }
        });

    }


    public interface SendDataToActivity {

        void sendCategoryId(String categoryId);
    }

}