package com.mostafahassan.thunder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
    Animation bottom_anim,top_anim;
    TextView textView;
    ImageView imageView;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        bottom_anim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        top_anim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        textView=findViewById(R.id.tv_splash);
        imageView=findViewById(R.id.iv_splash);
        imageView.setAnimation(top_anim);
        textView.setAnimation(bottom_anim);
        firebaseAuth=FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser= firebaseAuth.getCurrentUser();
                if(currentUser == null){
                    SendUserToLoginActivity();
                }
                else
                {
                    SendUserToMainActivity();
                }

                finish();
            }
        },5000);

    }





    private void SendUserToMainActivity() {
        Intent toMain = new Intent(Splash.this,MainActivity.class);
        toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toMain);
    }

    private void SendUserToLoginActivity() {
        Intent toLogin = new Intent(Splash.this,LoginActivity.class);
        toLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toLogin);
        finish();
    }
}

