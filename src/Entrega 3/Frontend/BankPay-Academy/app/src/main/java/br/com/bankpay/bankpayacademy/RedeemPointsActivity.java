package br.com.bankpay.bankpayacademy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.bankpay.bankpayacademy.adapter.ProductAdapter;
import br.com.bankpay.bankpayacademy.model.Product;
import br.com.bankpay.bankpayacademy.utils.SharedPrefsHelper;

public class RedeemPointsActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private RecyclerView recyclerProdutos;
    private ProductAdapter adapter;
    private List<Product> listaProduto;
    private ImageView imgVoltar;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_redeem_points);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        recyclerProdutos = findViewById(R.id.recyclerProdutos);
        recyclerProdutos.setLayoutManager(new GridLayoutManager(this, 2));

        // Recupera o ID do usuário a partir do SharedPrefsHelper
        idUsuario = SharedPrefsHelper.getUsuarioId(this);

        // Validação para caso não encontre o usuario
        if (idUsuario == -1) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela se o usuario não estiver logado
        }

        // Inicialização da lista de itens dos produto e carrega os dados via API
        listaProduto = new ArrayList<>();
        carregarProdutos();

        // Configura o adapter com a lista de serviços e o evento de troca
        adapter = new ProductAdapter(this, listaProduto, this::onTrocarClick);
        recyclerProdutos.setAdapter(adapter);

        // Cliando no icone (imagem) de voltar, leva para a tela Home
        imgVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Função para quando o item é trocado
    public void onTrocarClick(Product product) {
        mostrarDialogTrocarPontos(product, idUsuario);
        Toast.makeText(this, product.getNome() + " selecionado para troca!", Toast.LENGTH_SHORT).show();
    }

    // Método para carregar produtos da atraves da api /produto/listarProduto
    private void carregarProdutos() {
        //String url = "http://10.0.2.2:3000/produto/listarProduto";
        String url = "https://plyhcd-3000.csb.app/produto/listarProduto";

        RequestQueue queue = Volley.newRequestQueue(this);

        // Criando requisição GET para o servidor
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    listaProduto.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            // Recebe os objetos retornados pela API, atribui eles a variaveis e os adiciona à lista
                            JSONObject obj = response.getJSONObject(i);

                            int id = obj.getInt("id");
                            String nome = obj.getString("titulo");
                            String descricao = obj.getString("descricao");
                            int pontosProduto = obj.getInt("pontos");
                            String imagem = obj.getString("imagem");

                            // Adiciona item à lista
                            listaProduto.add(new Product(id, nome, String.valueOf(pontosProduto), descricao, imagem));
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

    // Método para exibir um diálogo dpara trocar pontos por produto
    public void mostrarDialogTrocarPontos(Product product, int idUsuario) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        // Infla o layout de poup
        View dialogView = inflater.inflate(R.layout.dialog_confirm_redeem_point_product, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        TextView txtDialogPontos = dialogView.findViewById(R.id.txtDialogPontos);
        MaterialButton btnTrocarProduto = dialogView.findViewById(R.id.btnTrocarProduto);
        MaterialButton btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        TextView txtDialogMensagem = dialogView.findViewById(R.id.txtDialogMensagem);

        //String url = "http://10.0.2.2:3000/pix/pontos/" + idUsuario;
        String url = "https://plyhcd-3000.csb.app/pix/pontos/" + idUsuario;


        // Criando requisição GET para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        int pontosUsuario = response.getInt("pontos");
                        int pontosProduto = Integer.parseInt(product.getPontos());

                        txtDialogPontos.setText(pontosUsuario + " Pontos");

                        // Verificação se o usuario possui pontos suficientes para realizar a troca
                        if (pontosUsuario >= pontosProduto) {
                            txtDialogMensagem.setText("Após a confirmação, serão debitados " + pontosProduto + " pontos do seu saldo.");

                            // Botão de Trocar Pontos
                            btnTrocarProduto.setOnClickListener(v -> {
                                ArrayList<Integer> itensIds = new ArrayList<>();
                                itensIds.add(product.getId()); // Adiciona apenas o produto selecionado

                                Intent intent = new Intent(this, RedeemPointsConfirmActivity.class);
                                intent.putIntegerArrayListExtra("itens_ids", itensIds);
                                intent.putExtra("chave_pix", "atleticafecap@edu.fecap.br");

                                startActivity(intent);
                                dialog.dismiss();
                            });

                        } else {
                            // Usuario não possui pontos suficientes
                            txtDialogMensagem.setText("Você não tem pontos suficientes para este produto.");
                            btnTrocarProduto.setEnabled(false); // Desativa o botão
                            btnTrocarProduto.setBackgroundColor(ContextCompat.getColor(this, R.color.appColorCinza)); // Deixa o botão na cor cinza
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        txtDialogPontos.setText("Erro ao carregar seus pontos. Tente novamente.");
                    }
                },
                error -> {
                    error.printStackTrace();
                    txtDialogPontos.setText("Erro ao carregar");
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        // Botão de Cancelar
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
    }

}