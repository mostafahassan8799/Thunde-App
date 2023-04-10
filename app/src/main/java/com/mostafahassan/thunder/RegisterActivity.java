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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText email, pass, confirmPass;
    Button register;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.register_et_email);
        pass = findViewById(R.id.register_et_password);
        confirmPass = findViewById(R.id.register_et_confirmPassword);
        register = findViewById(R.id.register_button_register);
        progressBar = findViewById(R.id.register_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });

    }

    private void CreateNewAccount() {
        progressBar.setVisibility(View.VISIBLE);
        String inEmail = email.getText().toString();
        String inPass = pass.getText().toString();
        String inPassConfirm = confirmPass.getText().toString();

        if (TextUtils.isEmpty(inEmail)) {
            Toast.makeText(this, "Please enter your email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(inPass)) {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(inPassConfirm)) {
            Toast.makeText(this, "Please confirm your password...", Toast.LENGTH_SHORT).show();
        } else if (!inPass.equals(inPassConfirm)) {
            Toast.makeText(this, "Your password do not match with your confirm password...", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(inEmail, inPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("username","");
                        hashMap.put("email","");
                        hashMap.put("country","");
                        hashMap.put("status","Iam using...");
                        hashMap.put("gender","");
                        hashMap.put("relationshi_pstatus","android");
                        hashMap.put("profileImage","");
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                     startActivity(new Intent(RegisterActivity.this,
                                             SetupActivity.class));
                                    Toast.makeText(RegisterActivity.this, "Your account is created successfully", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    String message =task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error :" +message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Toast.makeText(RegisterActivity.this, "successful", Toast.LENGTH_SHORT).show();
                        sendUserToSetupActivity();
                        progressBar.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void sendUserToSetupActivity() {
        Intent toSetup = new Intent(RegisterActivity.this, SetupActivity.class);
        toSetup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toSetup);
        finish();


    }
}


