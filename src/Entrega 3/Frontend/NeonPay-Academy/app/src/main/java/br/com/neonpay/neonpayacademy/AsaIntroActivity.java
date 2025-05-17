package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AsaIntroActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private Button btnAvancar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asa_intro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        btnAvancar = findViewById(R.id.btnAvancar);

        // Quando o botão btnAvancar for clicado, vai para tela de serviços do Asa
        btnAvancar.setOnClickListener(v -> {
            Intent intent = new Intent(AsaIntroActivity.this, AsaServiceActivity.class);
            startActivity(intent);
        });
    }
}