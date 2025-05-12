package br.com.neonpay.neonpayacademy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import br.com.neonpay.neonpayacademy.R;
import br.com.neonpay.neonpayacademy.model.ExtractItem;

import java.util.List;

public class ExtractAdapter extends RecyclerView.Adapter<ExtractAdapter.ViewHolder> {

    private List<ExtractItem> lista;
    private Context context;

    public ExtractAdapter(Context context, List<ExtractItem> lista) {
        this.context = context;
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDescricao, textData, textValor, textChavePix;
        ImageView imgIcone;

        public ViewHolder(View itemView) {
            super(itemView);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textData = itemView.findViewById(R.id.textData);
            textValor = itemView.findViewById(R.id.textValor);
            imgIcone = itemView.findViewById(R.id.imgIcone);
            textChavePix = itemView.findViewById(R.id.textChavePix);
        }
    }

    @Override
    public ExtractAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_extract, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExtractAdapter.ViewHolder holder, int position) {
        ExtractItem item = lista.get(position);
        holder.textDescricao.setText(item.getDescricao());
        holder.textData.setText(item.getData());
        holder.textValor.setText(item.getValor());
        holder.textChavePix.setText(item.getChavePix());

        if (item.getTipo() == ExtractItem.Tipo.ENTRADA) {
            holder.textValor.setTextColor(ContextCompat.getColor(context, R.color.appColorVerde));
            holder.imgIcone.setImageResource(R.drawable.ic_entrada);
        } else {
            holder.textValor.setTextColor(ContextCompat.getColor(context, R.color.appColorVermelho));
            holder.imgIcone.setImageResource(R.drawable.ic_saida);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

