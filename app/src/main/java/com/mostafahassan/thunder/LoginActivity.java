package com.mostafahassan.thunder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText email,pass;
    Button forgetPass, login, loginWithFace, loginWithGmail, signUp;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email =findViewById(R.id.login_et_email);
        pass =findViewById(R.id.login_et_password);
        progressBar =findViewById(R.id.login_progressBar);
        forgetPass =findViewById(R.id.login_button_forget_password);
        login =findViewById(R.id.login_button_login);
        loginWithFace =findViewById(R.id.login_facebook);
        loginWithGmail =findViewById(R.id.login_gmail);
        signUp =findViewById(R.id.login_button_signUp);
        firebaseAuth =FirebaseAuth.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegister =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(toRegister);


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loginWithEmail();


            }
        });
    }

    private void fromLoginToMain() {
        Intent fromLoginToMain = new Intent(LoginActivity.this,MainActivity.class);
        fromLoginToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(fromLoginToMain);
        finish();
    }



    private void loginWithEmail()
    {
        String loginEmail = email.getText().toString();
        String loginPass = pass.getText().toString();

        if (TextUtils.isEmpty(loginEmail) || TextUtils.isEmpty(loginPass))
        {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
        }
        else
            {
                firebaseAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "You are logged in successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            fromLoginToMain();
                        }
                        else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : "+message , Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }


    }
}