package br.com.neonpay.neonpayacademy;

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

public class PixTransferReceiptActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private double valor;
    private TextView txtValor;
    private Button btnMenu;
    private TextView txtNomeDestinatario;
    private String nomeDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_transfer_receipt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        txtValor = findViewById(R.id.txtValor);
        txtNomeDestinatario = findViewById(R.id.txtNomeDestinatario);
        btnMenu = findViewById(R.id.btnMenu);

        // Recebendo os dados da Tela que foram enviados pelo Intent:
        Bundle bundle = getIntent().getExtras();
        valor = bundle.getDouble("valor", 0);
        nomeDestinatario = bundle.getString("nome_destinatario");

        // Mostrando dados na tela
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); // Formata o valor para o formato em Reais
        txtValor.setText(format.format(valor));
        txtNomeDestinatario.setText(nomeDestinatario);

        // Botão para retornar à ConfirmPixTransferActivity
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        btnMenu.setOnClickListener(v -> {
            // Vai para a tela Home
            Intent intent = new Intent(PixTransferReceiptActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

    }
}