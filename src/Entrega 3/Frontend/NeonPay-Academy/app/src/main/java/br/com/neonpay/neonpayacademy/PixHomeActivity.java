package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PixHomeActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão para retornar à tela Home
        imgVoltar.setOnClickListener(view -> {
            finish();
        });
    }

    // Função para levar a tela PixTransferActivity
    public void transferirPix(View view) {
        // Invoca a "PixTransferActivity"
        Intent intent = new Intent(this, PixTransferActivity.class);
        startActivity(intent);
    }

    // Função para levar a tela PixAddAmountActivity
    public void adicionarDinheiro(View view) {
        // Invoca a "PixAddAmountActivity"
        Intent intent = new Intent(this, PixAddAmountActivity.class);
        startActivity(intent);
    }
}