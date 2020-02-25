package com.example.jssbs.RegisterLogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jssbs.Main.MainActivity;
import com.example.jssbs.Main.MainActivityAdmin;
import com.example.jssbs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email_login,password_login;
    private Button login_btn;
    private TextView new_user;
    private RadioGroup userType;
    private RadioButton userOption;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        login_btn = findViewById(R.id.login_btn);
        new_user = findViewById(R.id.new_user);
        userType = findViewById(R.id.radioGrpUser);

        userType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                userOption = userType.findViewById(i);

                switch (i){
                    case R.id.radioBtnUser:
                        login_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginProcessUser();
                            }
                        });
                        break;

                    case R.id.radioBtnAdmin:
                        login_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginProcessAdmin();
                            }
                        });
                        break;

                     default:
                }
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void loginProcessUser() {
        String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Please re-login", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void loginProcessAdmin(){
        String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        if(email.equals("admin@gmail.com") && password.equals("999999")){
            startActivity(new Intent(LoginActivity.this, MainActivityAdmin.class));
        }
        else{
            Toast.makeText(LoginActivity.this, "Please re-login", Toast.LENGTH_SHORT).show();
        }
    }
}
