package br.com.bankpay.bankpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.UUID;

public class PixRegisterRandomKeyActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private TextView txtChaveAleatoria;
    private Button btnRegistrarChaveAleatoria;
    private ImageView imgVoltar;
    private String chaveAleatoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_register_random_key);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        txtChaveAleatoria = findViewById(R.id.txtChaveAleatoria);
        btnRegistrarChaveAleatoria = findViewById(R.id.btnRegistrarChaveAleatoria);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> finish());

        // Gera e mostra a chave aleatória Pix
        chaveAleatoria = UUID.randomUUID().toString();
        txtChaveAleatoria.setText(chaveAleatoria);

        // Botão para registrar a chave
        btnRegistrarChaveAleatoria.setOnClickListener(v -> registrarChaveAleatoria());
    }

    // Função para registrar chave aleatoria
    private void registrarChaveAleatoria() {
        if (chaveAleatoria == null || chaveAleatoria.isEmpty()) {
            Toast.makeText(this, "Erro ao gerar chave aleatória.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(PixRegisterRandomKeyActivity.this, PixKeyConfirmationActivity.class);
        intent.putExtra("chave_pix", chaveAleatoria);
        intent.putExtra("tipo_chave_pix", "chave_aleatoria");
        startActivity(intent);
    }
}