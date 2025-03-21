package br.com.neonpay.neonpayacademy;

import android.content.Intent;
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

    // Campos do Formulario de Cadastro
    private EditText txtCPF, txtSenha;
    private Button btnLogar;

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

        txtCPF = findViewById(R.id.txtCPF);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logarUsuario();
            }
        });

        ImageView imgVoltar = (ImageView) findViewById(R.id.imgVoltar);
        imgVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        TextView txtCrie = (TextView) findViewById(R.id.txtCrie);
        txtCrie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void logarUsuario() {
        String cpf = txtCPF.getText().toString().trim();
        String senha = txtSenha.getText().toString().trim();

        if (cpf.isEmpty()  || senha.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validarCPF(cpf)) {
            Toast.makeText(this, "CPF inválido!", Toast.LENGTH_SHORT).show();
            return;
        }


        // Continuar com o cadastro após a validação
        Log.d("Registro", "Login: ");
        Log.d("Registro", "CPF: " + cpf);
        Log.d("Registro", "Senha: " + senha);

        JSONObject postData = new JSONObject();
        try {
            postData.put("cpf", cpf);
            postData.put("senha", senha);
            Log.d("Registro", "JSON criado: " + postData.toString());
        } catch (JSONException e) {
            Log.e("Registro", "Erro ao criar JSON", e);
            return;
        }

        String url = "http://10.0.2.2:3000/api/login";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    Log.d("Registro", "Resposta da API: " + response.toString());
                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                },
                error -> {
                    Log.e("Registro", "Erro ao cadastrar: " + error.toString());
                    Toast.makeText(LoginActivity.this, "CPF ou Senha Incorreta!!", Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    // Validação de CPF
    private boolean validarCPF(String cpf) {

        // Vai remover pontos e traços digitados pelo usuario
        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

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


}