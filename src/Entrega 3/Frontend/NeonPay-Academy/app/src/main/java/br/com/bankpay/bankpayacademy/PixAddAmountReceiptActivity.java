package br.com.bankpay.bankpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class PixAddAmountReceiptActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private TextView txtValor;
    private double valor;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_add_amount_receipt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        txtValor = findViewById(R.id.txtValor);
        btnMenu = findViewById(R.id.btnMenu);

        // Recebendo os dados da Tela que foram enviados pelo Intent:
        Bundle bundle = getIntent().getExtras();
        valor = bundle.getDouble("valor", 0);

        // Mostrando dados na tela
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); // Formata o valor para o formato em Reais
        txtValor.setText(format.format(valor));

        // Botão para retornar à ConfirmPixTransferActivity
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        btnMenu.setOnClickListener(v -> {
            // Vai para a tela Home
            Intent intent = new Intent(PixAddAmountReceiptActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

    }
}