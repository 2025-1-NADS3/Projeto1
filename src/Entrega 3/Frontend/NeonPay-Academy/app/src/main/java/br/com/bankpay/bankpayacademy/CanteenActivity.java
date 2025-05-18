package br.com.bankpay.bankpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.bankpay.bankpayacademy.adapter.CanteenAdapter;
import br.com.bankpay.bankpayacademy.model.Canteen;

public class CanteenActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private RecyclerView recyclerCantina;
    private CanteenAdapter adapter;
    private List<Canteen> listaCantina;
    private List<Canteen> listaCarrinho = new ArrayList<>();
    private ImageView imgVoltar;
    private ImageView imgCarrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_canteen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        recyclerCantina = findViewById(R.id.recyclerCantina);
        recyclerCantina.setLayoutManager(new GridLayoutManager(this, 2));
        imgCarrinho = findViewById(R.id.imgCarrinho);

        // Inicialização da lista de itens da cantina e carrega os dados via API
        listaCantina = new ArrayList<>();
        carregarItensCantina();

        // Configura o adapter com a lista de serviços e o evento de compra
        adapter = new CanteenAdapter(this, listaCantina, this::onComprarClick);
        recyclerCantina.setAdapter(adapter);

        // Botão para retornar à HomeActivity
        imgVoltar.setOnClickListener(view -> {
            // Invoca a "HomeActivity"
            Intent intent = new Intent(this, FecapServicesActivity.class);
            startActivity(intent);
            finish();
        });

        // Cliando no icone de carrinho, leva para a tela de Carrinho (CanteenCartActivity)
        imgCarrinho.setOnClickListener(v -> {
            Intent intent = new Intent(CanteenActivity.this, CanteenCartActivity.class);
            intent.putExtra("carrinho", new ArrayList<>(listaCarrinho));
            startActivity(intent);
        });

    }

    // Função para quando o item é comprado
    public void onComprarClick(Canteen canteen) {
        listaCarrinho.add(canteen);
        Toast.makeText(this, canteen.getNome() + " adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
    }

    // Método para carregar itens da cantina atraves da api /cantina/listarCantina
    private void carregarItensCantina() {
        String url = "http://10.0.2.2:3000/cantina/listarCantina";

        RequestQueue queue = Volley.newRequestQueue(this);

        // Criando requisição GET para o servidor
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    listaCantina.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            // Recebe os objetos retornados pela API, atribui eles a variaveis e os adiciona à lista
                            JSONObject obj = response.getJSONObject(i);
                            String nome = obj.getString("titulo");
                            String descricao = obj.getString("descricao");
                            int id = obj.getInt("id");

                            // Formata o preço
                            DecimalFormat df = new DecimalFormat("R$#.00");
                            String preco = df.format(obj.getDouble("preco"));

                            String imagem = obj.getString("imagem");
                            listaCantina.add(new Canteen(id, nome, preco, descricao, imagem)); // Adiciona item à lista
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        queue.add(request);
    }
}