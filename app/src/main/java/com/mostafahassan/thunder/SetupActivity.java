package com.mostafahassan.thunder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mostafahassan.thunder.Moduel.User;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    private static final int GALLERY_PICK = 1;
    CircleImageView profileImage;
    EditText username, email, country;
    Button save;
    ProgressDialog progressDialog;
    String CURRENT_USER_ID;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference UserProfileImageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        progressDialog = new ProgressDialog(this);
        profileImage =findViewById(R.id.setup_iv_profilePic);
        username =findViewById(R.id.setup_et_username);
        email =findViewById(R.id.setup_et_email);
        country =findViewById(R.id.setup_et_country);
        firebaseAuth =FirebaseAuth.getInstance();
        CURRENT_USER_ID = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        UserProfileImageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");
        save =findViewById(R.id.setuo_button_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAccountSetupInformation();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.
                            READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , 1);

                    } else {

                        CropImage.activity().setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .start(SetupActivity.this);
                    }
                }


            }
        });
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    User user=snapshot.getValue(User.class);
                    Toast.makeText(SetupActivity.this, "Loading image ", Toast.LENGTH_SHORT).show();
                    Log.i("GGGG", "onDataChange: "+user.getProfileImage()+"");
                  Glide.with(SetupActivity.this).load(user.getProfileImage()).apply(new RequestOptions().placeholder(R.drawable.profile)).into(profileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);

               Uri resultUri= result.getUri();
               StorageReference filePath = UserProfileImageReference.child(UUID.randomUUID().toString() + ".jpg");
               filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                       if(task.isSuccessful())
                       {
                           progressDialog.setMessage("Loading...");
                           progressDialog.show();

                           filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   Log.i("GGGG", "onSuccess: "+uri);
                                   HashMap<String,Object> hashMap=new HashMap<>();
                                   hashMap.put("profileImage",uri.toString());
                                   FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                           updateChildren(hashMap)
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   Toast.makeText(SetupActivity.this, "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                   progressDialog.dismiss();
                                               }
                                           }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           String message =e.getMessage();
                                           Toast.makeText(SetupActivity.this, "Error occured : "+message, Toast.LENGTH_SHORT).show();
                                           progressDialog.dismiss();
                                       }
                                   });
                               }
                           });

                       }

                   }
               });
           }

       else 
       {
           Toast.makeText(this, "Error Image can't be cropped", Toast.LENGTH_SHORT).show();
       }

        }



    private void saveAccountSetupInformation() {
        String name =username.getText().toString();
        String userEmail =email.getText().toString();
        String userCountry =country.getText().toString();
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please enter username...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userEmail))
        {
            Toast.makeText(this, "Please enter email...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userCountry))
        {
            Toast.makeText(this, "Please enter country...", Toast.LENGTH_SHORT).show();
        }
        else
            {
                progressDialog.setTitle("Saving Information");
                progressDialog.setMessage("Please wait, while we register your account");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(true);
                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("username",name);
                hashMap.put("email",userEmail);
                hashMap.put("country",userCountry);
                hashMap.put("status","Iam using...");
                hashMap.put("gender","");
                hashMap.put("relationshi_pstatus","android");
                hashMap.put("profileImage","");

                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                           sendUserToMainActivity();
                            Toast.makeText(SetupActivity.this, "Your account is created successfully", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String message =task.getException().getMessage();
                            Toast.makeText(SetupActivity.this, "Error :" +message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

            }

    }

    private void sendUserToMainActivity() {
        Intent toMain = new Intent(SetupActivity.this,MainActivity.class);
        toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toMain);
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    CropImage.activity().setAspectRatio(1,1)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .start(SetupActivity.this);

                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), "Can't change image profile..", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}