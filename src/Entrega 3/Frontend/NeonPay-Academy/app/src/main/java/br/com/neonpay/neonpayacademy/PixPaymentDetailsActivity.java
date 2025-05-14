package br.com.neonpay.neonpayacademy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class PixPaymentDetailsActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private TextView txtValor;
    private TextView txtCopiaCola;
    private double valor;
    private String chavePix;
    private int transacaoId;
    private Button btnConfirmarPagamento;
    private ImageView imgCopiaCola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_payment_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes
        txtValor = findViewById(R.id.txtValor);
        txtCopiaCola = findViewById(R.id.txtCopiaCola);
        btnConfirmarPagamento = findViewById(R.id.btnConfirmarPagamento);
        imgCopiaCola = findViewById(R.id.imgCopiaCola);

        btnConfirmarPagamento.setEnabled(false);

        // Recupera dados do Intent
        Bundle bundle = getIntent().getExtras();
        valor = bundle.getDouble("valor", 0);
        chavePix = bundle.getString("chave_pix");
        transacaoId = bundle.getInt("transacao_id", 0);

        // Mostra valor formatado
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtValor.setText(format.format(valor));

        txtCopiaCola.setText(chavePix);

        imgCopiaCola.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("PIX", chavePix);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Código copiado!", Toast.LENGTH_SHORT).show();

            // Habilita o botão
            btnConfirmarPagamento.setEnabled(true);
            btnConfirmarPagamento.setBackgroundColor(ContextCompat.getColor(this, R.color.appColorRoxo));
        });

        btnConfirmarPagamento.setOnClickListener(v -> confirmarPagamento(transacaoId));

    }

    private void confirmarPagamento(int transacaoId) {
        String url = "http://10.0.2.2:3000/pix/webhook";

        JSONObject json = new JSONObject();
        try {
            json.put("transacao_id", transacaoId);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao preparar requisição", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    Toast.makeText(getApplicationContext(), "Transação confirmada com sucesso.", Toast.LENGTH_LONG).show();

                    // Vai para a tela de comprovante
                    Intent intent = new Intent(PixPaymentDetailsActivity.this, PixAddAmountReceiptActivity.class);
                    intent.putExtra("valor", valor);
                    startActivity(intent);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro ao se conectar com a API", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}