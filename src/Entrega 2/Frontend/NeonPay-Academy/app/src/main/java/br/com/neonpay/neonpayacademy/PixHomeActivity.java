package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.neonpay.neonpayacademy.utils.SharedPrefsHelper;

public class PixHomeActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private TextView txtValorSaldo;
    private EditText txtChavePix;
    private EditText txtValor;
    private ImageView imgVoltar;
    private int idUsuario;

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
        txtValorSaldo = findViewById(R.id.txtValorSaldo);
        txtChavePix = findViewById(R.id.txtChavePix);
        txtValor = findViewById(R.id.txtValor);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Recupera o ID do usuário a partir do SharedPrefsHelper
        idUsuario = SharedPrefsHelper.getUsuarioId(this);

        // Validação para caso não encontre o usuario
        if (idUsuario == -1) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela se não estiver logado
        }

        // Botão para retornar à tela Home
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        consultarSaldo(); // Chama a função para mostrar o saldo
    }

    // Função para validar os campos de chave pix, valor e encaminhar os dados para a próxima tela de confirmação
    public void confirmar(View view) {
        String chavePix = txtChavePix.getText().toString().trim();
        String valorString = txtValor.getText().toString().trim();

        // Verifica se o campo de chave pix ou valor está vazio, caso esteja ele vai usar um return para o usuario não prosseguir sem digitar
        if (chavePix.isEmpty() || valorString.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Transformar o valorString em um valor double
        double valor;
        try {
            valor = Double.parseDouble(valorString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Passa os dados para a tela ConfirmPixTransferActivity
        Intent intent = new Intent(PixHomeActivity.this, ConfirmPixTransferActivity.class);
        intent.putExtra("id_usuario", idUsuario);
        intent.putExtra("chave_pix", chavePix);
        intent.putExtra("valor", valor);
        startActivity(intent);
    }

    // Função para consultar o saldo através da rota pix/saldo/idUsuario
    private void consultarSaldo() {
        String url = "http://10.0.2.2:3000/pix/saldo/" + idUsuario;

        // Criando requisição GET para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        double saldo = response.getDouble("saldo");

                        // Formata o saldo para o formato em Reais
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                        txtValorSaldo.setText(format.format(saldo));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao ler saldo", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao consultar saldo", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}