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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.bankpay.bankpayacademy.utils.SharedPrefsHelper;

public class RedeemPointsConfirmActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private EditText txtSenha;
    private String chave_pix;
    private int idUsuario;
    private Button btnConfirmarTroca;
    private String nome, email, cpf , token;
    private ArrayList<Integer> itensIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_redeem_points_confirm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        txtSenha = findViewById(R.id.txtSenha);
        btnConfirmarTroca = findViewById(R.id.btnConfirmarTroca);

        // Recupera o token
        token = SharedPrefsHelper.getToken(this);

        // Verificação se o token é null, se for voltar para tela de login
        if (token == null) {
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Chama o metodo para buscar dados do usuario
        buscarDadosUsuario();

        // Recebendo os dados da Tela que foram enviados pelo Intent:
        Bundle bundle = getIntent().getExtras();
        chave_pix = bundle.getString("chave_pix");

        // Recuperar lista de IDs dos itens
        itensIds = bundle.getIntegerArrayList("itens_ids");

        // Recupera o ID do usuário a partir do SharedPrefsHelper
        idUsuario = SharedPrefsHelper.getUsuarioId(this);

        // Botão para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        // Quando o botão btnConfirmarTroca for clicado, verifica se o campo de senha está preenchido
        btnConfirmarTroca.setOnClickListener(v -> {
            String senha = txtSenha.getText().toString().trim();
            if (senha.isEmpty()) {
                Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chama a função confirmar pagamento do itens da cantina
            confirmarPagamentoProduto(chave_pix, senha, itensIds);
        });

    }
    // Função para enviar uma requisição POST para a rota /produto/pagarProdutoComPontos, confirmando o pagamento
    private void confirmarPagamentoProduto(String chavePix, String senha, ArrayList<Integer> itensIds) {
        String url = "http://10.0.2.2:3000/produto/pagarProdutoComPontos";

        JSONObject json = new JSONObject();
        try {
            json.put("usuario_id", idUsuario);
            json.put("chave_pix", chavePix);
            json.put("senha", senha);

            JSONArray itensArray = new JSONArray();
            for (Integer id : itensIds) {
                itensArray.put(id);
            }
            json.put("itens_ids", itensArray);

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Criando requisição POST para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    try {
                        String mensagem = response.getString("mensagem");
                        String descricao = response.getString("descricao");
                        int pontosTotal = response.getInt("pontos_gastos");
                        int senhaPedido = response.getInt("senha_pedido");
                        String data = response.getString("data");
                        JSONArray itensArrayResponse = response.getJSONArray("itens");

                        // Envia todos os dados via Intent
                        Intent intent = new Intent(RedeemPointsConfirmActivity.this, RedeemPointsReceiptActivity.class);
                        intent.putExtra("nome", nome);
                        intent.putExtra("email", email);
                        intent.putExtra("senha_pedido", senhaPedido);
                        intent.putExtra("cpf", mascararCpf(cpf));
                        intent.putExtra("mensagem", mensagem);
                        intent.putExtra("descricao", descricao);
                        intent.putExtra("pontos_gastos", pontosTotal);
                        intent.putExtra("data", data);
                        intent.putExtra("chave_pix", chave_pix);
                        intent.putExtra("itens", itensArrayResponse.toString());

                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao processar resposta da API.", Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao confirmar pagamento", Toast.LENGTH_SHORT).show();
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

    // Função para consultar os dados do usuario
    private void buscarDadosUsuario() {
        String url = "http://10.0.2.2:3000/api/perfil";

        // Criando requisição GET para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        nome = response.getString("nome");
                        cpf = response.getString("cpf");
                        email = response.getString("email");

                        if (!validarCPF(cpf)) {
                            Toast.makeText(this, "CPF retornado inválido.", Toast.LENGTH_SHORT).show();
                            return;
                        }

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
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    // Função para mascarar o cpf
    private String mascararCpf(String cpf) {
        // Valida se o CPF tem o formato correto (11 dígitos)
        if (cpf != null && cpf.length() == 11) {
            return cpf.substring(0, 3) + ".***.***-" + cpf.substring(9);
        }
        return cpf; // Caso não seja um CPF válido, retorna o CPF original
    }

    // Função para validar cpf
    private boolean validarCPF(String cpf) {
        return cpf != null && cpf.length() == 11 && !cpf.matches("(\\d)\\1{10}");
    }

}