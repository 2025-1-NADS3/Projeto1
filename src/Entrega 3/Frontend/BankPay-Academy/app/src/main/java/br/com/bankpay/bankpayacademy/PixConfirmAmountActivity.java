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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class PixConfirmAmountActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private TextView txtValor;
    private int idUsuario;
    private double valor;
    private ImageView imgEditarTexto;
    private Button btnAvancarAddConfirm;
    private ImageView imgVoltar;
    private String chavePix;
    private int transacaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_confirm_amount);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes
        txtValor = findViewById(R.id.txtValor);
        imgEditarTexto = findViewById(R.id.imgEditarTexto);
        btnAvancarAddConfirm = findViewById(R.id.btnAvancarAddConfirm);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão (imagem) para retornar para editar o valor
        imgEditarTexto.setOnClickListener(view -> {
            finish();
        });

        // Botão (imagem) para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        // Recupera dados do Intent
        Bundle bundle = getIntent().getExtras();
        idUsuario = bundle.getInt("id_usuario");
        valor = bundle.getDouble("valor", 0);

        // Exibe dados na tela
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtValor.setText(format.format(valor));

        btnAvancarAddConfirm.setOnClickListener(v -> confirmar(valor));
    }

    private void confirmar(double valor) {
        //String url = "http://10.0.2.2:3000/pix/gerar-cobranca";
        String url = "https://plyhcd-3000.csb.app/pix/gerar-cobranca";

        JSONObject json = new JSONObject();
        try {
            json.put("usuario_id", idUsuario);
            json.put("valor", valor);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao preparar requisição", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    try {
                        // Lê dados retornados pela API
                        String qrCode = response.getString("qr_code");
                        chavePix = response.getString("chave_pix");
                        transacaoId = response.getInt("transacao_id");

                        Toast.makeText(getApplicationContext(), "Pix gerado com sucesso!", Toast.LENGTH_LONG).show();

                        // Abre próxima tela com os dados da transação
                        Intent intent = new Intent(PixConfirmAmountActivity.this, PixPaymentDetailsActivity.class);
                        intent.putExtra("valor", valor);
                        intent.putExtra("chave_pix", chavePix);
                        intent.putExtra("transacao_id", transacaoId);
                        intent.putExtra("qr_code", qrCode);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Erro ao processar resposta da API", Toast.LENGTH_SHORT).show();
                    }
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