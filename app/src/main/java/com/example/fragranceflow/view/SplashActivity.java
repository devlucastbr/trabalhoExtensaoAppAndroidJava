package com.example.fragranceflow.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.fragranceflow.MainActivity;
import com.example.fragranceflow.R;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Definir tempo de espera (ex: 3 segundos)
        new Handler().postDelayed(() -> {
            // Iniciar a MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finaliza a Splash Activity
        }, 3000); // 3000 milissegundos = 3 segundos
    }
}
