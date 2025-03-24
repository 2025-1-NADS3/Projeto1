package br.com.neonpay.neonpayacademy;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText txtNome1, txtEmail1, txtTelefone1, txtSenha1;
    private Button btnUpdate, btnDelete;

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
        btnDelete = findViewById(R.id.btnDelete);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarPerfil();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarExclusao();
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

        carregarPerfil();

    }

    private void carregarPerfil() {
        if (token == null) {
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:3000/api/perfil";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String nome = response.getString("nome");
                        String email = response.getString("email");
                        String telefone = response.getString("telefone");

                        txtNome1.setText(nome);
                        txtEmail1.setText(email);
                        txtTelefone1.setText(telefone);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Ocorreu um erro ao atualizar as informações. Verifique sua conexão e tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Erro", error.toString());
                    Toast.makeText(this, "Erro ao carregar perfil", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
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

        JSONObject params = new JSONObject();
        try {
            if (!nome.isEmpty()) params.put("nome", nome);
            if (!email.isEmpty()) params.put("email", email);
            if (!telefone.isEmpty()) params.put("telefone", telefone);
            if (!senha.isEmpty()) params.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (params.length() == 0) {
            Toast.makeText(this, "Nenhuma alteração foi feita.", Toast.LENGTH_SHORT).show();
            return;
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
    private void confirmarExclusao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar exclusão");
        builder.setMessage("Tem certeza de que deseja deletar sua conta? Esta ação não pode ser desfeita.");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletarConta();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {

            int black = ContextCompat.getColor(this, R.color.black);

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(black);

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(black);
        });
        dialog.show();
    }

    private void deletarConta() {
        if (token == null) {
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:3000/api/deletar-perfil";

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Toast.makeText(this, "Conta deletada com sucesso!", Toast.LENGTH_LONG).show();
                    logout();
                },
                error -> {
                    Log.e("Erro", error.toString());
                    Toast.makeText(this, "Erro ao deletar conta", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("TOKEN");
        editor.apply();

        Intent intent = new Intent(EditProfileActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private String obterToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("TOKEN", null);
    }
}
