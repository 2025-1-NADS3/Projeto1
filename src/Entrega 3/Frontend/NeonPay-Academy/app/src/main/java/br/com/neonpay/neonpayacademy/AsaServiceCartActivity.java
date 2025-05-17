package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

import br.com.neonpay.neonpayacademy.adapter.CarrinhoAdapter;
import br.com.neonpay.neonpayacademy.model.Servico;

public class AsaServiceCartActivity extends AppCompatActivity implements CarrinhoAdapter.OnItemDeleteClickListener {

    // Declaração das variáveis dos elementos da interface
    private RecyclerView recyclerCarrinho;
    private CarrinhoAdapter adapter;
    private ArrayList<Servico> listaCarrinho;
    private TextView txtTotalCarrinho;
    private Button btnPagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asa_service_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        recyclerCarrinho = findViewById(R.id.recyclerCarrinho);
        recyclerCarrinho.setLayoutManager(new LinearLayoutManager(this));
        txtTotalCarrinho = findViewById(R.id.txtTotalCarrinho);
        btnPagamento = findViewById(R.id.btnPagamento);

        // Recebe a lista do carrinho pela Intent
        listaCarrinho = (ArrayList<Servico>) getIntent().getSerializableExtra("carrinho");

        if (listaCarrinho == null) {
            listaCarrinho = new ArrayList<>();
        }

        // Configura o adapter para exibir os itens no RecyclerView
        adapter = new CarrinhoAdapter(this, listaCarrinho, this);
        recyclerCarrinho.setAdapter(adapter);

        // Chama a função para atualizar o total do carrinho ao abrir a tela
        atualizarTotalCarrinho();

        // Ao clicar no botão de pagamento, é levado para tela de confirmar senha
        btnPagamento.setOnClickListener(view -> {
            Intent intent = new Intent(AsaServiceCartActivity.this, AsaServiceConfirmPasswordActivity.class);

            // ArrayList para armazenar os IDs dos serviços
            ArrayList<Integer> servicoIds = new ArrayList<>();
            for (Servico s : listaCarrinho) {
                servicoIds.add(s.getId());
            }

            // Passa os IDs dos serviços e a chave PIX para a próxima tela
            intent.putIntegerArrayListExtra("servico_ids", servicoIds);
            intent.putExtra("chave_pix", "asa@edu.fecap.br");

            startActivity(intent);
        });
    }

    // Método chamado quando o usuario quiser remover um item do carrinho
    @Override
    public void onItemDeleteClick(int position) {
        Servico removido = listaCarrinho.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, removido.getNome() + " removido do carrinho", Toast.LENGTH_SHORT).show();
        atualizarTotalCarrinho();
    }

    // Função para atualizar o valor do carrinho
    private void atualizarTotalCarrinho() {
        double total = 0.0;
        for (Servico servico : listaCarrinho) {
            try {
                // Tira os R$ e vírgulas
                String precoFormatado = servico.getPreco().replace("R$", "").replace(",", ".").trim();
                total += Double.parseDouble(precoFormatado);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Formata o valor total do carrinho
        txtTotalCarrinho.setText(String.format("R$ %.2f", total));
    }
}