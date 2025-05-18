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
import br.com.bankpay.bankpayacademy.model.Canteen;

// CanteenAdapter é um Adapter responsável por exibir os itens da cantina da faculdade em uma RecyclerView
public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.CanteenViewHolder> {

    // Interface para capturar o clique no botão comprar
    public interface OnComprarClickListener {
        void onComprarClick(Canteen item);
    }

    private Context context;
    private List<Canteen> listaCanteen;
    private OnComprarClickListener listener;

    // Construtor do CanteenAdapter
    public CanteenAdapter(Context context, List<Canteen> listaCanteen, OnComprarClickListener listener) {
        this.context = context;
        this.listaCanteen = listaCanteen;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CanteenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_canteen, parent, false);
        return new CanteenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CanteenViewHolder holder, int position) {
        Canteen item = listaCanteen.get(position);

        // Recupera o ID da imagem com base no nome salvo no objeto
        int resId = context.getResources().getIdentifier(item.getImagemNome(), "drawable", context.getPackageName());
        holder.imgServico.setImageResource(resId);
        holder.txtNome.setText(item.getNome());
        holder.txtPreco.setText(item.getPreco());
        holder.txtSubtitulo.setText(item.getDescricao());

        // Ao clicar no botão de comprar o item, chamara a interface para comprar item da cantina
        holder.btnComprar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComprarClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCanteen.size();
    }

    public static class CanteenViewHolder extends RecyclerView.ViewHolder {

        // Declaração das variáveis dos elementos da interface
        ImageView imgServico;
        TextView txtNome, txtPreco, txtSubtitulo;
        Button btnComprar;

        public CanteenViewHolder(@NonNull View itemView) {
            // Inicialização dos componentes da interface
            super(itemView);
            imgServico = itemView.findViewById(R.id.imgServico);
            txtNome = itemView.findViewById(R.id.txtTituloServico);
            txtPreco = itemView.findViewById(R.id.txtPreco);
            txtSubtitulo = itemView.findViewById(R.id.txtSubtitulo);
            btnComprar = itemView.findViewById(R.id.btnComprar);
        }
    }
}
