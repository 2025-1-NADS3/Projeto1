package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class ConfirmPixTransferActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private TextView txtValor, txtNomeDestinatario, txtDocumento, txtChavePix;
    private Button btnAvancar;
    private String chavePix;
    private double valor;
    private String nomeDestinatario;
    private ImageView imgVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_pix_transfer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        txtValor = findViewById(R.id.txtValor);
        txtNomeDestinatario = findViewById(R.id.txtNomeDestinatario);
        txtDocumento = findViewById(R.id.txtDocumento);
        txtChavePix = findViewById(R.id.txtChavePix);
        imgVoltar = findViewById(R.id.imgVoltar);
        btnAvancar = findViewById(R.id.btnAvancar);

        // Recebendo os dados da Tela que foram enviados pelo Intent:
        Bundle bundle = getIntent().getExtras();
        chavePix = bundle.getString("chave_pix");
        valor = bundle.getDouble("valor", 0);
        nomeDestinatario = "Hebert Esteves";

        // Define os dados na tela
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); // Formata o valor para o formato em Reais
        txtValor.setText(format.format(valor));
        txtChavePix.setText(chavePix);
        txtNomeDestinatario.setText(nomeDestinatario); // Simulando Nome
        txtDocumento.setText("123.456.789-00");     // Simulando Documento

        // Botão para retornar à PixTransferActivity
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        // Avançar para tela de confirmar sua senha e passando os dados da chave pix, valor e nome do destinatario
        btnAvancar.setOnClickListener(v -> {
            Intent intent1 = new Intent(ConfirmPixTransferActivity.this, ConfirmPixPasswordActivity.class);
            intent1.putExtra("chave_pix", chavePix);
            intent1.putExtra("valor", valor);
            intent1.putExtra("nome_destinatario", nomeDestinatario);
            startActivity(intent1);
        });
    }
}