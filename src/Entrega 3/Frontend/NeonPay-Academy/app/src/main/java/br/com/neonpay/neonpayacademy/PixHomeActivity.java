package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.neonpay.neonpayacademy.utils.SharedPrefsHelper;

public class PixHomeActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private int idUsuario;
    private String token;

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

        // Recupera o ID do usuário a partir do SharedPrefsHelper
        idUsuario = SharedPrefsHelper.getUsuarioId(this);

        // Recupera o token
        token = SharedPrefsHelper.getToken(this);

        // Verificação se o token é null, se for voltar para tela de login
        if (token == null) {
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Botão (imagem) para retornar à tela Home
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

    // Função para levar a tela PixIntroSendReceiveActivity
    public void cadastrarChavePix(View view) {
        // Invoca a "PixIntroSendReceiveActivity"
        Intent intent = new Intent(this, PixIntroSendReceiveActivity.class);
        startActivity(intent);
    }

    // Função para enviar uma requisição POST para a rota /api/buscarChavePix/" + idUsuario, para checar a chave pix do usuario com base no seu id
    public void checarChavePix(View view) {
        String url = "http://10.0.2.2:3000/api/buscarChavePix/" + idUsuario;

        RequestQueue queue = Volley.newRequestQueue(this);

        // Criando requisição GET para o servidor
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    ArrayList<String> tipos = new ArrayList<>();
                    ArrayList<String> valores = new ArrayList<>();

                    // Adiciona o tipo e valor da chave Pix conforme a resposta da API
                    String tipoPix = response.optString("tipo_chave_pix", "");
                    String valorPix = response.optString("chave_pix", "");

                    // Verficação se o tipoPix e valorPix não estão vazias, caso não esteja adiciona as listas
                    if (!tipoPix.isEmpty() && !valorPix.isEmpty()) {
                        tipos.add(tipoPix.toUpperCase());
                        valores.add(valorPix);
                    }

                    // Se não houver chave pix, redireciona para tela de criação
                    if (tipos.isEmpty()) {
                        startActivity(new Intent(PixHomeActivity.this, PixKeyListActivity.class));
                    } else {
                        // Caso o usuario possua chave pix, redireciona para tela de visualizar chave pix
                        Intent intent = new Intent(PixHomeActivity.this, PixKeyListActivity.class);
                        intent.putStringArrayListExtra("tipos", tipos);
                        intent.putStringArrayListExtra("valores", valores);
                        startActivity(intent);
                    }

                },
                error -> {
                    startActivity(new Intent(PixHomeActivity.this, NoPixKeyActivity.class));
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(jsonRequest);
    }
}
