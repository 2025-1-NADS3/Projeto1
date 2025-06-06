package br.com.bankpay.bankpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PixIntroManageKeysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_intro_manage_keys);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Função para levar a tela PixIntroCreateKeyActivity
    public void pularTela(View view) {
        // Invoca a "PixIntroCreateKeyActivity"
        Intent intent = new Intent(this, PixIntroCreateKeyActivity.class);
        startActivity(intent);
        finish();
    }

    // Função para levar a tela PixIntroCreateKeyActivity
    public void avancarProximaTela(View view) {
        // Invoca a "PixIntroCreateKeyActivity"
        Intent intent = new Intent(this, PixIntroCreateKeyActivity.class);
        startActivity(intent);
        finish();
    }
}