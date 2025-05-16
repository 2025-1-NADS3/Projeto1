package br.com.neonpay.neonpayacademy.adapter;

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
import br.com.neonpay.neonpayacademy.R;
import br.com.neonpay.neonpayacademy.model.Servico;

// ServicoAdapter é um Adapter responsável por exibir os itens do Servico do ASA em uma RecyclerView
public class ServicoAdapter extends RecyclerView.Adapter<ServicoAdapter.ServicoViewHolder> {

    // Interface para capturar o clique de comprar de item de servicos do ASA
    public interface OnComprarClickListener {
        void onComprarClick(Servico servico);
    }

    private Context context;
    private List<Servico> listaServicos;
    private OnComprarClickListener listener;

    // Construtor do ServicoAdapter
    public ServicoAdapter(Context context, List<Servico> listaServicos, OnComprarClickListener listener) {
        this.context = context;
        this.listaServicos = listaServicos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_servico, parent, false);
        return new ServicoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicoViewHolder holder, int position) {
        Servico servico = listaServicos.get(position);

        // Recupera o ID da imagem com base no nome salvo no objeto
        int resId = context.getResources().getIdentifier(servico.getImagemNome(), "drawable", context.getPackageName());
        holder.imgServico.setImageResource(resId);
        holder.txtNome.setText(servico.getNome());
        holder.txtPreco.setText(servico.getPreco());

        // Ao clicar no botão de comprar o item, chamara a interface para comprar item de servicos do ASA
        holder.btnComprar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComprarClick(servico);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaServicos.size();
    }

    public static class ServicoViewHolder extends RecyclerView.ViewHolder {

        // Declaração das variáveis dos elementos da interface
        ImageView imgServico;
        TextView txtNome, txtPreco;
        Button btnComprar;

        public ServicoViewHolder(@NonNull View itemView) {
            // Inicialização dos componentes da interface
            super(itemView);
            imgServico = itemView.findViewById(R.id.imgServico);
            txtNome = itemView.findViewById(R.id.txtTituloServico);
            txtPreco = itemView.findViewById(R.id.txtPreco);
            btnComprar = itemView.findViewById(R.id.btnComprar);
        }
    }
}

