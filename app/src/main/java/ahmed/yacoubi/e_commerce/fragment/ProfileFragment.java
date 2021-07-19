package ahmed.yacoubi.e_commerce.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.adapter.RecycleProductItem;
import ahmed.yacoubi.e_commerce.callback.CallBack;
import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseAuthentication;
import ahmed.yacoubi.e_commerce.firebase.FirebaseUserShopping;
import ahmed.yacoubi.e_commerce.model.Product;


public class ProfileFragment extends Fragment {

    private String userId;
    private Button btn_changePassword, btn_changeName, btn_changePhone, btn_ShowMyOrder;
    private Button btn_doChangePassword, btn_doChangeName, btn_doChangePhone;
    private EditText et_newPassword, et_oldPassword, et_newPhone, et_newName;
    private RecyclerView recycle;
    private FirebaseAuthentication firebaseAuthentication;
    private FirebaseUserShopping firebaseUserShopping;
    private Database database;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getActivity().getSharedPreferences("main", getActivity().MODE_PRIVATE).getString("id", null);
        firebaseAuthentication = FirebaseAuthentication.getInstance(getActivity(), userId);
        firebaseUserShopping = FirebaseUserShopping.getInstance(getActivity(), userId);
        database = new Database(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);

        btn_changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });

        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        btn_changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhone();
            }
        });
        btn_ShowMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrder();
            }
        });
        return view;
    }


    private void initView(View view) {
        btn_changeName = view.findViewById(R.id.profile_btn_changeName);
        btn_changePassword = view.findViewById(R.id.profile_btn_changePassword);
        btn_changePhone = view.findViewById(R.id.profile_btn_changePhone);
        btn_ShowMyOrder = view.findViewById(R.id.profile_btn_ShowMyOrder);
        btn_doChangePassword = view.findViewById(R.id.profile_btn_doChangePassword);
        btn_doChangeName = view.findViewById(R.id.profile_btn_doChangeName);
        btn_doChangePhone = view.findViewById(R.id.profile_btn_doChangePhone);
        et_newPassword = view.findViewById(R.id.profile_et_newPassword);
        et_oldPassword = view.findViewById(R.id.profile_et_oldPassword);
        et_newPhone = view.findViewById(R.id.profile_et_newPhone);
        et_newName = view.findViewById(R.id.profile_et_newName);
        recycle = view.findViewById(R.id.profile_recycle);
    }


    private void showOrder() {
        btn_doChangePassword.setVisibility(View.GONE);
        btn_doChangeName.setVisibility(View.GONE);
        btn_doChangePhone.setVisibility(View.GONE);
        et_newPassword.setVisibility(View.GONE);
        et_oldPassword.setVisibility(View.GONE);
        et_newPhone.setVisibility(View.GONE);
        et_newName.setVisibility(View.GONE);
        recycle.setVisibility(View.VISIBLE);
        RecycleProductItem adapter = new RecycleProductItem(getActivity(), "buyed", null);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 1);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(lm);
        recycle.setAdapter(adapter);
        firebaseUserShopping.getBuyedProduct(new CallBackProduct() {
            @Override
            public void getProducts(List<Product> productList) {
                for (int i = 0; i < productList.size(); i++) {
                    Product p = productList.get(i);
                    productList.get(i).setBitmap(database.getImage(p.getName()));

                }
                adapter.setList(productList);
            }
        });


    }

    private void changePassword() {
        recycle.setVisibility(View.GONE);
        btn_doChangePassword.setVisibility(View.VISIBLE);
        btn_doChangeName.setVisibility(View.GONE);
        btn_doChangePhone.setVisibility(View.GONE);
        et_newPassword.setVisibility(View.VISIBLE);
        et_oldPassword.setVisibility(View.VISIBLE);
        et_newPhone.setVisibility(View.GONE);
        et_newName.setVisibility(View.GONE);
        btn_doChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuthentication.changePassword(userId, et_oldPassword.getText().toString(), et_newPassword.getText().toString(), new CallBack() {
                    @Override
                    public void getResult(String result) {
                        if (result.equals("done")) {
                            Toast.makeText(getActivity(), "changed ...", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getActivity(), "old password is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

    }

    private void changeName() {
        recycle.setVisibility(View.GONE);
        btn_doChangePassword.setVisibility(View.GONE);
        btn_doChangeName.setVisibility(View.VISIBLE);
        btn_doChangePhone.setVisibility(View.GONE);
        et_newPassword.setVisibility(View.GONE);
        et_oldPassword.setVisibility(View.GONE);
        et_newPhone.setVisibility(View.GONE);
        et_newName.setVisibility(View.VISIBLE);
        btn_doChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuthentication.changeName(userId, et_newName.getText().toString(), new CallBack() {
                    @Override
                    public void getResult(String result) {
                        if (result.equals("done")) {
                            Toast.makeText(getActivity(), "changed ...", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
            }
        });

    }

    private void changePhone() {
        recycle.setVisibility(View.GONE);
        btn_doChangePassword.setVisibility(View.GONE);
        btn_doChangeName.setVisibility(View.GONE);
        btn_doChangePhone.setVisibility(View.VISIBLE);
        et_newPassword.setVisibility(View.GONE);
        et_oldPassword.setVisibility(View.GONE);
        et_newPhone.setVisibility(View.VISIBLE);
        et_newName.setVisibility(View.GONE);
        btn_doChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuthentication.changePhone(userId, et_newPhone.getText().toString(), new CallBack() {
                    @Override
                    public void getResult(String result) {
                        if (result.equals("done")) {
                            Toast.makeText(getActivity(), "changed ...", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
            }
        });

    }
}
