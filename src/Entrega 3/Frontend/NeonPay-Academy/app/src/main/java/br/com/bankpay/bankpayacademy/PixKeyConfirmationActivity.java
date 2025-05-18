package br.com.bankpay.bankpayacademy;

import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.bankpay.bankpayacademy.utils.SharedPrefsHelper;

public class PixKeyConfirmationActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private EditText txtSenha;
    private String chave_pix, tipo_chave_pix;
    private int idUsuario;
    private Button btnConfirmarPagamento;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_key_confirmation);
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
        chave_pix = bundle.getString("chave_pix");
        tipo_chave_pix = bundle.getString("tipo_chave_pix");

        // Recupera o token
        token = SharedPrefsHelper.getToken(this);

        if (token == null) {
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Recupera o ID do usuário a partir do SharedPrefsHelper
        idUsuario = SharedPrefsHelper.getUsuarioId(this);

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

            cadastrarChavePix(chave_pix, senha);
        });
    }

    // Função para enviar uma requisição POST para a rota /pix/enviar, confirmando a transferência Pix
    private void cadastrarChavePix(String chavePix, String senha) {
        String url = "http://10.0.2.2:3000/api/cadastrarChavePix";

        JSONObject json = new JSONObject();
        try {
            json.put("usuario_id", idUsuario);
            json.put("chave_pix", chavePix);
            json.put("tipo_chave_pix", tipo_chave_pix);
            json.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    Toast.makeText(this, "Chave Pix cadastrada com sucesso.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PixKeyConfirmationActivity.this, PixKeySuccessActivity.class);
                    startActivity(intent);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao cadastrar chave Pix", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}