package com.example.whaththt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userId;
    DocumentReference userDocRef;
    String normalUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth=FirebaseAuth.getInstance();
        Handler handler = new Handler();



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAuth.getCurrentUser() != null){
                    // user is logged in
                    mAuth = FirebaseAuth.getInstance();
                    firestore = FirebaseFirestore.getInstance();
                    userId = mAuth.getCurrentUser().getUid();
                    userDocRef = firestore.collection("UsersDB").document(userId);
                    userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                // User document found
                                    normalUser="1";
                                    Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                                    intent.putExtra("normalUser","1");
                                    startActivity(intent);
                                    finish();

                            }
                            else {
                                // User document does not exist
                                normalUser="2";
                                Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                                intent.putExtra("normalUser","2");
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }

                else if (mAuth.getCurrentUser() == null){
                    startActivity(new Intent(SplashScreenActivity.this,AuthenticationActivity.class));
                    finish();
                }
            }
        }, 5000);
    }

}