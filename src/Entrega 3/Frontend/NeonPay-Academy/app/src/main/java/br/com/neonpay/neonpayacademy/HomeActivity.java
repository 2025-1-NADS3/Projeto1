package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import br.com.neonpay.neonpayacademy.utils.SharedPrefsHelper;

public class HomeActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private String token;
    private ImageView imgUsuario;
    private BarChart barChart;
    private TextView txtValorSaldo;
    private TextView txtTotalPontos;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        txtValorSaldo = findViewById(R.id.txtValorSaldo);
        txtTotalPontos = findViewById(R.id.txtTotalPontos);
        barChart = findViewById(R.id.barChart);
        imgUsuario = findViewById(R.id.imgUsuario);

        // Botão para ir a tela de editar perfil
        imgUsuario.setOnClickListener(view -> {
            // Invoca a "EditProfileActivity"
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Recupera o token a partir do SharedPrefsHelper
        token = SharedPrefsHelper.getToken(this);

        // Recupera o ID do usuário a partir do SharedPrefsHelper
        idUsuario = SharedPrefsHelper.getUsuarioId(this);

        // Validação para caso não encontre o usuario
        if (idUsuario == -1) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela se o usuario não estiver logado
        }

        // Validação para caso não encontre o token, o usuario realizar o login novamente
        if (token == null) {
            Toast.makeText(this, "Token não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        carregarGraficoHistoricoPontos(); // Carregamento do gráfico
        consultarSaldo();  // Chama a função para mostrar o saldo
        consultarPontos(); // Chama a função para mostrar o pontos
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarGraficoHistoricoPontos(); // Atualiza grafico ao voltar para a tela
        consultarSaldo();  // Atualiza saldo ao voltar para a tela
        consultarPontos(); // Atualiza pontos ao voltar para a tela
    }


    // Função para carregar o grafico do historico de pontos do usuario
    private void carregarGraficoHistoricoPontos() {
        String url = "http://10.0.2.2:3000/api/historico-pontos/" + idUsuario;

        // Criando requisição GET para obter o histórico de pontos
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    ArrayList<String> labels = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String mes = obj.getString("mes");
                            int pontos = Integer.parseInt(obj.getString("total_pontos"));

                            // Transformar "YYYY-MM" em "MES"
                            String[] partes = mes.split("-");
                            int numeroMes = Integer.parseInt(partes[1]);
                            String[] nomesMeses = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};
                            String nomeMes = nomesMeses[numeroMes - 1];

                            entries.add(new BarEntry(i, pontos)); // Adicionando pontos na barra
                            labels.add(nomeMes); // Adicionando nome dos meses

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Configurações visuais do gráfico
                    BarDataSet dataSet = new BarDataSet(entries, "");
                    dataSet.setColors(new int[]{
                            Color.rgb(0, 122, 255),
                            Color.rgb(0, 102, 255),
                            Color.rgb(0, 82, 255),
                            Color.rgb(0, 62, 255),
                            Color.rgb(0, 42, 255),
                            Color.rgb(0, 22, 255),
                            Color.rgb(0, 0, 204),
                            Color.rgb(0, 0, 153)
                    });
                    dataSet.setValueTextColor(Color.BLACK);
                    dataSet.setValueTextSize(12f);
                    dataSet.setDrawValues(true); // Mostra os valores que estiverem acima das barras

                    BarData data = new BarData(dataSet);
                    barChart.setData(data);
                    barChart.setFitBars(true);

                    // Eixo X (meses)
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(12f);
                    xAxis.setDrawGridLines(false);
                    xAxis.setDrawAxisLine(false);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelRotationAngle(0f);

                    // Eixo Y
                    barChart.getAxisRight().setEnabled(false);
                    barChart.getAxisLeft().setDrawGridLines(false);
                    barChart.getAxisLeft().setDrawAxisLine(false);
                    barChart.getAxisLeft().setTextSize(12f);
                    barChart.getAxisLeft().setAxisMinimum(0f);
                    // Encontrar o maior valor de pontos
                    float maxPontos = 0f;
                    for (BarEntry entry : entries) {
                        if (entry.getY() > maxPontos) {
                            maxPontos = entry.getY();
                        }
                    }
                    // Ajustar o eixo Y com base no maior valor +20%
                    barChart.getAxisLeft().setAxisMaximum(maxPontos * 1.2f);

                    barChart.getDescription().setEnabled(false);
                    barChart.getLegend().setEnabled(false); // Remove a legenda
                    barChart.setDrawGridBackground(false);
                    barChart.setDrawBorders(false);
                    barChart.setExtraOffsets(8, 8, 8, 16);
                    barChart.animateY(5000);
                    barChart.invalidate();

                }, error -> {
            // Exibe mensagem de erro, caso o grafico não seja carregado
            Toast.makeText(this, "Erro ao carregar gráfico. Verifique sua conexão.", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // É feito o envio do token no cabeçalho da requisição
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    // Função para chama a Tela Home Pix
    public void abrirTelaPix(View view) {
        // Invoca a "PixHomeActivity"
        Intent intent = new Intent(this, PixHomeActivity.class);
        startActivity(intent);
    }

    // Função para chama a Tela Extrato
    public void abrirTelaExtrato(View view) {
        // Invoca a "ExtractActivity"
        Intent intent = new Intent(this, ExtractActivity.class);
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

    // Função para consultar o pontos através da rota pix/pontos/idUsuario
    private void consultarPontos() {
        String url = "http://10.0.2.2:3000/pix/pontos/" + idUsuario;

        // Criando requisição GET para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        int pontos = response.getInt("pontos");
                        txtTotalPontos.setText(String.format("%d", pontos));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao ler pontos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erro ao consultar pontos", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}