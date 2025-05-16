package br.com.neonpay.neonpayacademy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import br.com.neonpay.neonpayacademy.R;
import br.com.neonpay.neonpayacademy.model.Servico;

// CarrinhoAdapter é um Adapter responsável por exibir os itens do carrinho em uma RecyclerView
public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder> {

    // Interface para capturar o clique de exclusão de item do carrinho
    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(int position);
    }

    private Context context;
    private List<Servico> listaCarrinho;
    private OnItemDeleteClickListener listener;

    // Construtor do CarrinhoAdapter
    public CarrinhoAdapter(Context context, List<Servico> listaCarrinho, OnItemDeleteClickListener listener) {
        this.context = context;
        this.listaCarrinho = listaCarrinho;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false);
        return new CarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        Servico servico = listaCarrinho.get(position);

        // Recupera o ID da imagem com base no nome salvo no objeto
        int resId = context.getResources().getIdentifier(servico.getImagemNome(), "drawable", context.getPackageName());
        holder.imgCarrinho.setImageResource(resId);
        holder.txtNome.setText(servico.getNome());
        holder.txtPreco.setText(servico.getPreco());

        // Ao clicar no botão de deletar o item, chamara a interface para deletar item do carrinho
        holder.btnDeletar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemDeleteClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaCarrinho.size();
    }

    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder {

        // Declaração das variáveis dos elementos da interface
        ImageView imgCarrinho;
        TextView txtNome, txtPreco;
        FrameLayout btnDeletar;

        public CarrinhoViewHolder(@NonNull View itemView) {
            // Inicialização dos componentes da interface
            super(itemView);
            imgCarrinho = itemView.findViewById(R.id.imgCarrinho);
            txtNome = itemView.findViewById(R.id.txtNomeCarrinho);
            txtPreco = itemView.findViewById(R.id.txtPrecoCarrinho);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }
    }
}
