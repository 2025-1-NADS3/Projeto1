package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private EditText txtCPF, txtSenha;
    private Button btnLogar;
    private ImageView imgVoltar;
    private TextView txtCrie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        txtCPF = findViewById(R.id.txtCPF);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLogar);
        imgVoltar = findViewById(R.id.imgVoltar);
        txtCrie = findViewById(R.id.txtCrie);

        // Quando clicar no botão vai chamar a função logarUsuario()
        btnLogar.setOnClickListener(view -> {
            logarUsuario();
        });

        // Botão para retornar à tela de boas-vindas
        imgVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        // Texto para quando for clicado levar para a tela de cadastro
        txtCrie.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    // Função para realizar o login do usuário através de uma requisição POST
    private void logarUsuario() {
        String cpf = txtCPF.getText().toString().trim();
        String senha = txtSenha.getText().toString().trim();

        // Realiza validações antes fazer requisição ao servidor
        if (cpf.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realiza a validação do CPF chamando a função validarCPF()
        if (!validarCPF(cpf)) {
            Toast.makeText(this, "CPF inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criando objeto JSON para enviar na requisição
        JSONObject dadosUsuario = new JSONObject();
        try {
            dadosUsuario.put("cpf", cpf);
            dadosUsuario.put("senha", senha);
        } catch (JSONException e) {
            Log.e("Registro", "Erro ao criar JSON", e);
            return;
        }

        String url = "http://10.0.2.2:3000/api/login";

        // Criando requisição POST para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dadosUsuario,
                response -> {
                    try {
                        String tokenRecebido = response.getString("token");
                        armazenarToken(tokenRecebido);
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish(); // Fecha a tela de login
                    } catch (JSONException e) {
                        Log.e("Registro", "Erro ao ler o token do JSON", e);
                        Toast.makeText(LoginActivity.this, "Erro no servidor.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("Registro", "Erro ao logar: " + error.toString());
                    Toast.makeText(LoginActivity.this, "CPF ou Senha Incorreta!!", Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    // Validação de CPF (removendo caracteres especiais e verificando se é válido)
    private boolean validarCPF(String cpf) {

        // Vai remover pontos e traços digitados pelo usuario
        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;
        int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int digito1 = calcularDigito(cpf.substring(0, 9), pesos1);
        int digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesos2);

        return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
    }

    // Calcula os dígitos verificadores do CPF
    private int calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += (str.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    // Função para armazenar o token de login no SharedPreferences
    private void armazenarToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        sharedPreferences.edit().putString("TOKEN", token).apply();
    }
}