package ahmed.yacoubi.e_commerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.callback.CallBack;
import ahmed.yacoubi.e_commerce.firebase.FirebaseAuthentication;
import ahmed.yacoubi.e_commerce.model.User;

public class SignUp extends AppCompatActivity {

    private ImageView signupImgBack;
    private EditText signupEtEmail;
    private EditText signupEtFName;
    private EditText et_phoneNo;
    private EditText signupEtPassword;
    private Button signupBtnSignup;
    private TextView signupTvLogin;
    private FirebaseAuthentication firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        firebaseAuthentication = FirebaseAuthentication.getInstance(this, null);
        signupBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signupEtEmail != null && signupEtFName != null && et_phoneNo != null && signupEtPassword != null) {

                    if (!signupEtEmail.getText().toString().isEmpty() && !signupEtFName.getText().toString().isEmpty() && !et_phoneNo.getText().toString().isEmpty() && !signupEtPassword.getText().toString().isEmpty()) {

                        User user = new User();
                        user.setMoney(1000);
                        user.setPassword(signupEtPassword.getText().toString());
                        user.setEmail(signupEtEmail.getText().toString());
                        user.setName(signupEtFName.getText().toString());
                        user.setPhone(et_phoneNo.getText().toString());
                        firebaseAuthentication.signUp(user, new CallBack() {
                            @Override
                            public void getResult(String result) {
                                if (result.equals("done")) {

                                    finish();
                                    startActivity(new Intent(getBaseContext(), LogIn.class));
                                } else {
                                    Toast.makeText(SignUp.this, "Email used or password short", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUp.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signupTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), LogIn.class));
            }
        });
        signupImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        signupImgBack = findViewById(R.id.signup_img_back);
        signupEtEmail = findViewById(R.id.signup_et_email);
        signupEtFName = findViewById(R.id.signup_et_fName);
        et_phoneNo = findViewById(R.id.signup_et_phoneNo);
        signupEtPassword = findViewById(R.id.signup_et_password);
        signupBtnSignup = findViewById(R.id.signup_btn_signup);
        signupTvLogin = findViewById(R.id.signup_tv_login);
    }
}