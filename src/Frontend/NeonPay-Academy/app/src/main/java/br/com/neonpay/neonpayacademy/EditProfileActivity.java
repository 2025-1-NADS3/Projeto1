package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {

    private EditText txtNome1, txtEmail1, txtTelefone1, txtSenha1;
    private Button btnUpdate;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        token = obterToken();

        txtNome1 = findViewById(R.id.txtNome1);
        txtEmail1 = findViewById(R.id.txtEmail1);
        txtTelefone1 = findViewById(R.id.txtTelefone1);
        txtSenha1 = findViewById(R.id.txtSenha1);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarPerfil();
            }
        });

        ImageView imgVoltar = (ImageView) findViewById(R.id.imgVoltar1);
        imgVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void atualizarPerfil() {
        if (token == null) {
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nome = txtNome1.getText().toString().trim();
        String email = txtEmail1.getText().toString().trim();
        String telefone = txtTelefone1.getText().toString().trim();
        String senha = txtSenha1.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject params = new JSONObject();
        try {
            params.put("nome", nome);
            params.put("email", email);
            params.put("telefone", telefone);
            if (!senha.isEmpty()) params.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://10.0.2.2:3000/api/atualizar-perfil";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, params,
                response -> Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_LONG).show(),
                error -> {
                    Log.e("Erro", error.toString());
                    Toast.makeText(this, "Erro ao atualizar perfil", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private String obterToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("TOKEN", null);
    }
}
