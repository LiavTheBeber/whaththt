package com.example.whaththt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.whaththt.normal_classes.NormalRegisterFragment;

public class AuthenticationActivity extends AppCompatActivity {
    private Button BtnSignUp, BtnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        BtnSignIn=findViewById(R.id.btnSignIn);
        BtnSignUp=findViewById(R.id.btnSignUp);



        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new NormalRegisterFragment());
            }
        });
        BtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new LoginFragment());
            }
        });


    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.auth_frame_layout,fragment).commit();

    }
}