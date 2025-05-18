package br.com.bankpay.bankpayacademy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.bankpay.bankpayacademy.R;
import br.com.bankpay.bankpayacademy.model.Product;

// ProductAdapter é um Adapter responsável por exibir os itens de produto para troca
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    // Interface para capturar o clique no botão trocar
    public interface OnTrocarClickListener {
        void onTrocarClick(Product item);
    }

    private Context context;
    private List<Product> listaProduct;
    private OnTrocarClickListener listener;

    // Construtor do ProductAdapter
    public ProductAdapter(Context context, List<Product> listaProduct, OnTrocarClickListener listener) {
        this.context = context;
        this.listaProduct = listaProduct;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product item = listaProduct.get(position);

        // Recupera o ID da imagem com base no nome salvo no objeto
        int resId = context.getResources().getIdentifier(item.getImagemNome(), "drawable", context.getPackageName());
        holder.imgServico.setImageResource(resId);
        holder.txtNome.setText(item.getNome());
        holder.txtPontos.setText(item.getPontos());
        holder.txtSubtitulo.setText(item.getDescricao());

        // Ao clicar no botão de trocar o produto, chamara a interface para comprar o produto
        holder.btnTrocar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTrocarClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProduct.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        // Declaração das variáveis dos elementos da interface
        ImageView imgServico;
        TextView txtNome, txtPontos, txtSubtitulo;
        Button btnTrocar;

        public ProductViewHolder(@NonNull View itemView) {
            // Inicialização dos componentes da interface
            super(itemView);
            imgServico = itemView.findViewById(R.id.imgServico);
            txtNome = itemView.findViewById(R.id.txtTituloServico);
            txtPontos = itemView.findViewById(R.id.txtPontos);
            txtSubtitulo = itemView.findViewById(R.id.txtSubtitulo);
            btnTrocar = itemView.findViewById(R.id.btnTrocar);
        }
    }
}
