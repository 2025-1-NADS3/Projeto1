package br.com.bankpay.bankpayacademy;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.bankpay.bankpayacademy.adapter.ExtractAdapter;
import br.com.bankpay.bankpayacademy.model.ExtractItem;
import br.com.bankpay.bankpayacademy.utils.SharedPrefsHelper;

public class ExtractActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private RecyclerView recyclerExtrato;
    private ImageView imgVoltar;
    private TextView txtValorSaldo;
    private TextView txtMes;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        // Inicialização dos componentes da interface
        txtValorSaldo = findViewById(R.id.txtValorSaldo);
        recyclerExtrato = findViewById(R.id.recyclerExtrato);
        imgVoltar = findViewById(R.id.imgVoltar);
        txtMes = findViewById(R.id.txtMes);

        // Recupera o ID do usuário a partir do SharedPrefsHelper
        idUsuario = SharedPrefsHelper.getUsuarioId(this);

        // Validação para caso não encontre o usuario
        if (idUsuario == -1) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerExtrato.setLayoutManager(new LinearLayoutManager(this));
        consultarExtrato(); // Extrato é carregado da API
        consultarSaldo();   // Saldo é carregado da API

        imgVoltar.setOnClickListener(v -> finish());
    }

    // Função para consultar o extrato a partir do id do usuario
    private void consultarExtrato() {
        //String url = "http://10.0.2.2:3000/pix/extrato/" + idUsuario;
        String url = "https://plyhcd-3000.csb.app/pix/extrato/" + idUsuario;

        // Criando requisição GET para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray extratoArray = response.getJSONArray("extrato");
                        List<ExtractItem> lista = new ArrayList<>();

                        for (int i = 0; i < extratoArray.length(); i++) {
                            JSONObject obj = extratoArray.getJSONObject(i);

                            String descricao = obj.getString("descricao");
                            String valor = obj.getString("valor");
                            String chavePix = obj.getString("chave_pix");
                            String tipoStr = obj.getString("tipo");
                            ExtractItem.Tipo tipo = tipoStr.equalsIgnoreCase("entrada") ?
                                    ExtractItem.Tipo.ENTRADA : ExtractItem.Tipo.SAIDA;

                            // Formatar valor com sinal
                            double valorDouble = Double.parseDouble(valor);
                            String valorFormatado = (tipo == ExtractItem.Tipo.ENTRADA ? "R$ +" : "R$ -") +
                                    String.format(Locale.getDefault(), "%.2f", valorDouble).replace(".", ",");

                            // Array dos meses
                            String[] nomesMeses = {
                                    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                                    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
                            };

                            // Formatar data para dd/MM/yyyy
                            String dataOriginal = obj.getString("data");
                            String[] partes = dataOriginal.substring(0, 10).split("-");
                            String dataFormatada = partes[2] + "/" + partes[1] + "/" + partes[0];

                            // Definir nome do mês
                            int mesIndex = Integer.parseInt(partes[1]) - 1;
                            String nomeMes = nomesMeses[mesIndex];

                            if (i == 0) {
                                txtMes.setText(nomeMes);
                            }

                            // Adiciona o item à lista
                            lista.add(new ExtractItem(descricao, dataFormatada, valorFormatado, tipo, chavePix));
                        }

                        // Atualiza RecyclerView
                        recyclerExtrato.setAdapter(new ExtractAdapter(this, lista));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao ler extrato", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao consultar extrato", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    // Função para consultar o saldo através da rota pix/saldo/idUsuario
    private void consultarSaldo() {
        //String url = "http://10.0.2.2:3000/pix/saldo/" + idUsuario;
        String url = "https://plyhcd-3000.csb.app/pix/saldo/" + idUsuario;


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