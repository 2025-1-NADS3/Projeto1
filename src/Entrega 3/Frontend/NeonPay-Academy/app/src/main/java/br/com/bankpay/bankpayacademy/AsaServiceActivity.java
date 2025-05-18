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

import br.com.bankpay.bankpayacademy.adapter.ServicoAdapter;
import br.com.bankpay.bankpayacademy.model.Servico;

public class AsaServiceActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private RecyclerView recyclerServicos;
    private ServicoAdapter adapter;
    private List<Servico> listaServicos;
    private List<Servico> listaCarrinho = new ArrayList<>();
    private ImageView imgVoltar;
    private ImageView imgCarrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asa_service);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        recyclerServicos = findViewById(R.id.recyclerServicos);
        recyclerServicos.setLayoutManager(new GridLayoutManager(this, 2));
        imgCarrinho = findViewById(R.id.imgCarrinho);

        // Inicialização da lista de serviços e carrega os dados via API
        listaServicos = new ArrayList<>();
        carregarServicos();

        // Configura o adapter com a lista de serviços e o evento de compra
        adapter = new ServicoAdapter(this, listaServicos, this::onComprarClick);
        recyclerServicos.setAdapter(adapter);

        // Botão para retornar à HomeActivity
        imgVoltar.setOnClickListener(view -> {
            // Invoca a "HomeActivity"
            Intent intent = new Intent(this, FecapServicesActivity.class);
            startActivity(intent);
            finish();
        });

        // Cliando no icone de carrinho, leva para a tela de Carrinho (AsaServiceCartActivity)
        imgCarrinho.setOnClickListener(v -> {
            Intent intent = new Intent(AsaServiceActivity.this, AsaServiceCartActivity.class);
            intent.putExtra("carrinho", new ArrayList<>(listaCarrinho));
            startActivity(intent);
        });

    }

    // Função para quando o item é comprado
    public void onComprarClick(Servico servico) {
        if (!listaCarrinho.isEmpty()) {
            listaCarrinho.clear();  // Remove o produto que ja está no carrinho
            Toast.makeText(this, "Item anterior removido. ", Toast.LENGTH_SHORT).show();
        }
        listaCarrinho.add(servico);
        Toast.makeText(this, servico.getNome() + " adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
    }

    // Método para carregar serviços atraves da api /asa/listarServicoAsa
    private void carregarServicos() {
        String url = "http://10.0.2.2:3000/asa/listarServicoAsa";
        //String url = "https://plyhcd-3000.csb.app/api/perfil";

        RequestQueue queue = Volley.newRequestQueue(this);

        // Criando requisição GET para o servidor
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    listaServicos.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String nome = obj.getString("titulo");
                            int id = obj.getInt("id");
                            DecimalFormat df = new DecimalFormat("R$#.00");
                            String preco = df.format(obj.getDouble("preco"));
                            String imagem = obj.getString("imagem");
                            listaServicos.add(new Servico(id, imagem, nome, preco));
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