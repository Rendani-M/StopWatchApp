package com.stopwatchfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView tvSubSplash;
    private Button btnget;
    private Animation atg, btgone, btgtwo;
    private ImageView ivSplash;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSubSplash= findViewById(R.id.tvSubSplash);
        btnget= findViewById(R.id.btnget);
        ivSplash= findViewById(R.id.ivSplash);

        atg= AnimationUtils.loadAnimation(this, R.anim.atg);
        btgone= AnimationUtils.loadAnimation(this, R.anim.btgone);
        btgtwo= AnimationUtils.loadAnimation(this, R.anim.btgtwo);

        ivSplash.startAnimation(atg);
        tvSubSplash.startAnimation(btgone);
        btnget.startAnimation(btgtwo);

        Typeface MLight= Typeface.createFromAsset(getAssets(), "Fonts/MLight.ttf");
        Typeface MMedium= Typeface.createFromAsset(getAssets(), "Fonts/MMedium.ttf");
        Typeface MRegular= Typeface.createFromAsset(getAssets(), "Fonts/MRegular.ttf");

        tvSubSplash.setTypeface(MLight);
        btnget.setTypeface(MMedium);

        mAuth= FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            btnget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(getApplicationContext(), StopWatch.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

            });
        }
        if(currentUser == null){
            mAuth.signOut();
            btnget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent= new Intent(getApplicationContext(), StopWatch.class);
//                                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
        }
    }
    // Add Auth state listener in onStart method.
    public void onStart() {
        super.onStart();
    }

    //
}

//btnget.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //FirebaseUser user= mAuth.getCurrentUser();
//                            //updateUI(user);
//                            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if(task.isSuccessful()){
//                                        Intent intent= new Intent(getApplicationContext(), StopWatch.class);
//                                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(intent);
//
//                                    }else{
//                                        //currentUser=null;
//                                    }
//                                }
//                            });
//                        }
//                    });