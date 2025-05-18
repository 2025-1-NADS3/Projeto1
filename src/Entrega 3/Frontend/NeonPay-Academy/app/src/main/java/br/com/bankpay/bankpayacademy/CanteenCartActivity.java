package br.com.bankpay.bankpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.bankpay.bankpayacademy.adapter.CarrinhoCanteenAdapter;
import br.com.bankpay.bankpayacademy.model.Canteen;

public class CanteenCartActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private RecyclerView recyclerCarrinho;
    private CarrinhoCanteenAdapter adapter;
    private ArrayList<Canteen> listaCarrinho;
    private TextView txtTotalCarrinho;
    private Button btnPagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_canteen_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        recyclerCarrinho = findViewById(R.id.recyclerCarrinho);
        recyclerCarrinho.setLayoutManager(new LinearLayoutManager(this));
        txtTotalCarrinho = findViewById(R.id.txtTotalCarrinho);
        btnPagamento = findViewById(R.id.btnPagamento);

        // Botão (imagem) para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        // Recebe a lista do carrinho pela Intent
        listaCarrinho = (ArrayList<Canteen>) getIntent().getSerializableExtra("carrinho");

        if (listaCarrinho == null) {
            listaCarrinho = new ArrayList<>();
        }

        // Configura o adapter para exibir os itens no RecyclerView
        adapter = new CarrinhoCanteenAdapter(this, listaCarrinho, this::onItemDeleteClick);
        recyclerCarrinho.setAdapter(adapter);

        // Chama a função para atualizar o total do carrinho ao abrir a tela
        atualizarTotalCarrinho();

        // Ao clicar no botão de pagamento, é levado para tela de confirmar senha
        btnPagamento.setOnClickListener(view -> {
            Intent intent = new Intent(CanteenCartActivity.this, CanteenConfirmPasswordActivity.class);

            // ArrayList para armazenar os IDs dos itens
            ArrayList<Integer> itensIds = new ArrayList<>();
            for (Canteen c : listaCarrinho) {
                itensIds.add(c.getId());
            }

            // Passa os IDs dos itens e a chave PIX para a próxima tela
            intent.putIntegerArrayListExtra("itens_ids", itensIds);
            intent.putExtra("chave_pix", "cantina@edu.fecap.br");

            startActivity(intent);
        });

    }

    // Método chamado quando o usuario quiser remover um item do carrinho
    public void onItemDeleteClick(int position) {
        Canteen removido = listaCarrinho.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, removido.getNome() + " removido do carrinho", Toast.LENGTH_SHORT).show();
        atualizarTotalCarrinho();
    }

    // Função para atualizar o valor do carrinho
    private void atualizarTotalCarrinho() {
        double total = 0.0;
        for (Canteen canteen : listaCarrinho) {
            try {
                // Tira os R$ e vírgulas
                String precoFormatado = canteen.getPreco().replace("R$", "").replace(",", ".").trim();
                total += Double.parseDouble(precoFormatado);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Formata o valor total do carrinho
        txtTotalCarrinho.setText(String.format("R$ %.2f", total));
    }

}