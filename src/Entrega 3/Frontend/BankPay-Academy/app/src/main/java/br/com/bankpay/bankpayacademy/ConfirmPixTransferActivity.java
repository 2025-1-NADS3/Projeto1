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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.bankpay.bankpayacademy.utils.SharedPrefsHelper;

public class ConfirmPixTransferActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private TextView txtValor, txtNomeDestinatario, txtDocumento, txtChavePix;
    private Button btnAvancar;
    private String chavePix;
    private double valor;
    private String nomeDestinatario;
    private ImageView imgVoltar;
    private String token;

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

        // Recupera o token
        token = SharedPrefsHelper.getToken(this);

        if (token == null) {
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Inicialização dos componentes
        txtValor = findViewById(R.id.txtValor);
        txtNomeDestinatario = findViewById(R.id.txtNomeDestinatario);
        txtDocumento = findViewById(R.id.txtDocumento);
        txtChavePix = findViewById(R.id.txtChavePix);
        imgVoltar = findViewById(R.id.imgVoltar);
        btnAvancar = findViewById(R.id.btnAvancar);

        // Recupera dados do Intent
        Bundle bundle = getIntent().getExtras();
        chavePix = bundle.getString("chave_pix");
        valor = bundle.getDouble("valor", 0);

        // Exibe dados na tela
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtValor.setText(format.format(valor));
        txtChavePix.setText(chavePix);

        // Busca os dados do destinatário
        buscarDadosDestinatario(chavePix);

        imgVoltar.setOnClickListener(view -> finish());

        btnAvancar.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmPixTransferActivity.this, ConfirmPixPasswordActivity.class);
            intent.putExtra("chave_pix", chavePix);
            intent.putExtra("valor", valor);
            intent.putExtra("nome_destinatario", nomeDestinatario);
            startActivity(intent);
        });
    }

    // Função para consultar os dados do usuario através da sua chave pix
    private void buscarDadosDestinatario(String chavePix) {
        //String url = "http://10.0.2.2:3000/api/usuarios/buscar-por-chave-pix/" + chavePix;
        String url = "https://plyhcd-3000.csb.app/api/usuarios/buscar-por-chave-pix/" + chavePix;

        // Criando requisição GET para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        nomeDestinatario = response.getString("nome");
                        String cpf = response.getString("cpf");
                        String cpfMascarado = mascararCpf(cpf);

                        txtNomeDestinatario.setText(nomeDestinatario);
                        txtDocumento.setText(cpfMascarado);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao processar dados do destinatário", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Chave Pix inválida ou não encontrada!", Toast.LENGTH_SHORT).show();

                    // Redireciona para a tela PixTransferActivity
                    Intent intent = new Intent(ConfirmPixTransferActivity.this, PixTransferActivity.class);
                    startActivity(intent);
                    finish(); // Finaliza a atividade atual para evitar que o usuário volte

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

    private String mascararCpf(String cpf) {
        // Valida se o CPF tem o formato correto (11 dígitos)
        if (cpf != null && cpf.length() == 11) {
            return cpf.substring(0, 3) + ".***.***-" + cpf.substring(9);
        }
        return cpf; // Caso não seja um CPF válido, retorna o CPF original
    }
}