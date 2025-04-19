package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ConfirmPixPasswordActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private EditText txtSenha;
    private Button btnConfirmarPagamento;
    private String chavePix;
    private double valor;
    private String nomeDestinatario;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_pix_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        txtSenha = findViewById(R.id.txtSenha);
        btnConfirmarPagamento = findViewById(R.id.btnConfirmarPagamento);

        // Recebendo os dados da Tela que foram enviados pelo Intent:
        Bundle bundle = getIntent().getExtras();
        chavePix = bundle.getString("chave_pix");
        valor = bundle.getDouble("valor", 0);
        nomeDestinatario = bundle.getString("nome_destinatario");

        // Pega o ID do usuário do shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("id_usuario", -1);

        // Botão para retornar à ConfirmPixTransferActivity
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        // Quando o botão btnConfirmarPagamento for clicado, verifica se o campo de senha está preenchido
        btnConfirmarPagamento.setOnClickListener(v -> {
            String senha = txtSenha.getText().toString().trim();
            if (senha.isEmpty()) {
                Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chama a função para confirmar o Pix com os dados inseridos, caso o campo senha esteja preenchido
            confirmarPix(chavePix, valor, senha);
        });
    }

    // Função para enviar uma requisição POST para a rota /pix/enviar, confirmando a transferência Pix
    private void confirmarPix(String chavePix, double valor, String senha) {
        String url = "http://10.0.2.2:3000/pix/enviar";

        JSONObject json = new JSONObject();
        try {
            json.put("usuario_id", idUsuario);
            json.put("valor", valor);
            json.put("chave_pix_destino", chavePix);
            json.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Criando requisição POST para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    Toast.makeText(this, "Pix enviado com sucesso!", Toast.LENGTH_LONG).show();

                    // Avança para a tela de comprovante, passando o valor e o nome destinatario
                    Intent intent = new Intent(ConfirmPixPasswordActivity.this, PixTransferReceiptActivity.class);
                    intent.putExtra("valor", valor);
                    intent.putExtra("nome_destinatario", nomeDestinatario);
                    startActivity(intent);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao enviar Pix", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}