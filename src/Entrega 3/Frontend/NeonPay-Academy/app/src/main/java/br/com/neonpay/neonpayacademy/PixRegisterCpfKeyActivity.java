package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import br.com.neonpay.neonpayacademy.utils.CPFTextWatcher;
import br.com.neonpay.neonpayacademy.utils.SharedPrefsHelper;

public class PixRegisterCpfKeyActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private TextView txtCPF;
    private Button btnRegistrarCPF;
    private ImageView imgVoltar;
    private String token;
    private String cpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_register_cpf_key);
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

        // Inicialização dos componentes da interface
        txtCPF = findViewById(R.id.txtCPF);
        btnRegistrarCPF = findViewById(R.id.btnRegistrarCPF);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> finish());

        buscarCpfDoUsuario();

        // Quando clicar no botão vai passar a chave pix para a proxima tela
        btnRegistrarCPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PixRegisterCpfKeyActivity.this, PixKeyConfirmationActivity.class);
                intent.putExtra("chave_pix", cpf);
                startActivity(intent);
            }
        });
    }

    // Busca o CPF do usuário logado
    private void buscarCpfDoUsuario() {
        String url = "http://10.0.2.2:3000/api/perfil";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        cpf = response.getString("cpf");
                        String cpfFormatado = formatarCpf(cpf);

                        if (!validarCPF(cpf)) {
                            Toast.makeText(this, "CPF retornado inválido.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        txtCPF.setText(cpfFormatado);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao processar os dados do usuário.", Toast.LENGTH_SHORT).show();
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

    // Validação de CPF (sem caracteres especiais)
    private boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int digito1 = calcularDigito(cpf.substring(0, 9), pesos1);
        int digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesos2);

        return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
    }

    private int calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += (str.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    // Função para formatar o cpf XXX.XXX.XXX-XX
    private String formatarCpf(String cpf) {
        // Valida se o CPF tem exatamente 11 dígitos
        if (cpf != null && cpf.length() == 11) {
            return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
        }
        return cpf; // Caso não seja válido, retorna sem formatação
    }


}