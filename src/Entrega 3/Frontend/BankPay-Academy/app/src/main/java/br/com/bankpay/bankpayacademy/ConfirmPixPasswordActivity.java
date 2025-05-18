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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.bankpay.bankpayacademy.utils.SharedPrefsHelper;

public class ConfirmPixPasswordActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
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

            // Chama a função para confirmar o Pix com os dados inseridos, caso o campo senha esteja preenchido
            confirmarPix(chavePix, valor, senha);
        });
    }

    // Função para enviar uma requisição POST para a rota /pix/enviar, confirmando a transferência Pix
    private void confirmarPix(String chavePix, double valor, String senha) {
        // String url = "http://10.0.2.2:3000/pix/enviar";
        String url = "https://plyhcd-3000.csb.app/pix/enviar";

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    try {
                        String data = response.getString("data"); // Recebendo data como resposta da rota
                        Toast.makeText(this, "Pix enviado com sucesso!", Toast.LENGTH_LONG).show();
                        buscarDadosUsuario(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao processar resposta do servidor.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao enviar Pix", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    // Função para consultar os dados do usuario, passando data como parametro
    private void buscarDadosUsuario(String data) {
        //String url = "http://10.0.2.2:3000/api/perfil";
        String url = "https://plyhcd-3000.csb.app/api/perfil";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String nomeRemetente = response.getString("nome");
                        String cpfRemetente = response.getString("cpf");
                        String chavePixRemetente = response.getString("chave_pix");

                        // Vai para a tela de comprovante com todos os dados
                        Intent intent = new Intent(ConfirmPixPasswordActivity.this, PixTransferReceiptActivity.class);
                        intent.putExtra("valor", valor);
                        intent.putExtra("nome_destinatario", nomeDestinatario);
                        intent.putExtra("chave_pix_destinatario", chavePix);
                        intent.putExtra("data_transacao", data);
                        intent.putExtra("nome_remetente", nomeRemetente);
                        intent.putExtra("cpf_remetente", cpfRemetente);
                        intent.putExtra("chave_pix_remetente", chavePixRemetente);
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao processar dados do usuário.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao buscar dados do perfil.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SharedPrefsHelper.getToken(ConfirmPixPasswordActivity.this));
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}