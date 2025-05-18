package br.com.bankpay.bankpayacademy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.bankpay.bankpayacademy.utils.SharedPrefsHelper;

public class PixKeyConfirmDeleteActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private EditText txtSenha;
    private int idUsuario;
    private Button btnConfirmarExclusao;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_key_confirm_delete);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        txtSenha = findViewById(R.id.txtSenha);
        btnConfirmarExclusao = findViewById(R.id.btnConfirmarExclusao);

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

        // Botão (imagem) para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        // Quando o botão btnConfirmarExclusao for clicado, verifica se o campo de senha está preenchido
        btnConfirmarExclusao.setOnClickListener(v -> {
            String senha = txtSenha.getText().toString().trim();
            if (senha.isEmpty()) {
                Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chama metodo excluir chave pix passando senha como parametro
            excluirChavePix(senha);
        });

    }


    // Função para enviar uma requisição POST para a rota /api/excluirChavePix, confirmando a exclusão da chave pix
    private void excluirChavePix(String senha) {
        String url = "http://10.0.2.2:3000/api/excluirChavePix";

        JSONObject json = new JSONObject();
        try {
            json.put("usuario_id", idUsuario);
            json.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Criando requisição POST para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    // Mostra sucesso e exibe o AlertDialog
                    Toast.makeText(this, "Chave Pix deletada com sucesso.", Toast.LENGTH_LONG).show();
                    mostrarDialogConclusao();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao deletar chave Pix", Toast.LENGTH_SHORT).show();
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

    // Método para exibir um diálogo de conclusão da exclusão da chave pix
    private void mostrarDialogConclusao() {

        // Infla o layout de poup
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_conclusion_delete_key_pix, null);

        // Cria o AlertDialog com o layout personalizado
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // Deixa o fundo do AlertDialog transparente
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Botão de Concluir
        MaterialButton btnConcluir = dialogView.findViewById(R.id.btnConcluir);
        btnConcluir.setOnClickListener(v -> {
            alertDialog.dismiss();
            // Após clicar em Concluir, leva para tela de Home
            startActivity(new Intent(PixKeyConfirmDeleteActivity.this, HomeActivity.class));
            finish();
        });

        alertDialog.show();
    }
}

