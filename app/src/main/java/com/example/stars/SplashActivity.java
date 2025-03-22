package com.example.stars;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        LottieAnimationView lottieAnimationView = findViewById(R.id.shootings);
        lottieAnimationView.setAnimation(R.raw.shootingstar);
        lottieAnimationView.playAnimation();


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
        }, 4000);

    }
}