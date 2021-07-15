package ahmed.yacoubi.e_commerce.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.callback.CallBack;
import ahmed.yacoubi.e_commerce.database.Database;
import ahmed.yacoubi.e_commerce.firebase.FirebaseAuthentication;
import ahmed.yacoubi.e_commerce.model.User;

public class LogIn extends AppCompatActivity {

    private ImageView logInImgBack;
    private EditText logInEtEmail;
    private EditText logInEtPassword;
    private Button logInBtnLogIn;
    private TextView logInTvLogin;
    private FirebaseAuthentication firebaseAuthentication;
    private Database database;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        if (getSharedPreferences("main", MODE_PRIVATE).getString("id", null) != null) {
            finish();
            startActivity(new Intent(getBaseContext(), MainPage.class));
        }
        firebaseAuthentication = FirebaseAuthentication.getInstance(LogIn.this, null);
        initView();
        logInBtnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logInEtPassword != null && logInEtEmail != null) {
                    if (!logInEtEmail.getText().toString().isEmpty() && !logInEtPassword.getText().toString().isEmpty()) {

                        firebaseAuthentication.logIn(logInEtEmail.getText().toString(), logInEtPassword.getText().toString(), new CallBack() {
                            @Override
                            public void getResult(String result) {
                                if (!result.equals("fail") && result.length() > 5) {
                                    database = new Database(LogIn.this);


                                    getSharedPreferences("main", MODE_PRIVATE).edit().putString("id", result).apply();
                                    finish();
                                    startActivity(new Intent(getBaseContext(), MainPage.class));
                                } else {
                                    Toast.makeText(LogIn.this, "Email or password invalid", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    } else {

                        Toast.makeText(LogIn.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(LogIn.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        logInTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), SignUp.class));
            }
        });


    }

    private void initView() {
        logInImgBack = findViewById(R.id.logIn_img_back);
        logInEtEmail = findViewById(R.id.logIn_et_email);
        logInEtPassword = findViewById(R.id.logIn_et_password);
        logInBtnLogIn = findViewById(R.id.logIn_btn_logIn);
        logInTvLogin = findViewById(R.id.logIn_tv_login);
    }
}