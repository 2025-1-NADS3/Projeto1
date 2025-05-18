package br.com.bankpay.bankpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FecapServicesActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fecap_services);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão (imagem) para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> {
            finish();
        });
    }

    // Função para levar a tela CanteenIntroActivity
    public void servicosCantina(View view) {
        // Invoca a "CanteenIntroActivity"
        Intent intent = new Intent(this, CanteenIntroActivity.class);
        startActivity(intent);
    }

    // Função para levar a tela AsaIntroActivity
    public void servicosAsa(View view) {
        // Invoca a "AsaIntroActivity"
        Intent intent = new Intent(this, AsaIntroActivity.class);
        startActivity(intent);
    }
}