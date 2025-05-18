package br.com.bankpay.bankpayacademy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.bankpay.bankpayacademy.PixKeyDetailsActivity;
import br.com.bankpay.bankpayacademy.R;
import br.com.bankpay.bankpayacademy.model.PixKey;

// PixKeyAdapter é um Adapter responsável por exibir a chave pix do usuario
public class PixKeyAdapter extends RecyclerView.Adapter<PixKeyAdapter.PixKeyViewHolder> {

    private final List<PixKey> pixKeys;
    private final Context context;

    // Construtor do PixKeyAdapter
    public PixKeyAdapter(Context context, List<PixKey> pixKeys) {
        this.context = context;
        this.pixKeys = pixKeys;
    }

    @NonNull
    @Override
    public PixKeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pix_key, parent, false);
        return new PixKeyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PixKeyViewHolder holder, int position) {
        PixKey pixKey = pixKeys.get(position);
        holder.txtTipoPix.setText(pixKey.getTipo());
        holder.txtValorPix.setText(pixKey.getValor());

        // Quando clicado passa para a tela PixKeyDetailsActivity, com os dados do tipo_pix e chave_pix
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PixKeyDetailsActivity.class);
            intent.putExtra("tipo_pix", pixKey.getTipo()); // Tipo da chave pix (email, cpf, celular, chave_aleatoria)
            intent.putExtra("valor_pix", pixKey.getValor()); // Valor da chave pix do usuario
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pixKeys.size();
    }

    static class PixKeyViewHolder extends RecyclerView.ViewHolder {

        // Declaração das variáveis dos elementos da interface
        TextView txtTipoPix, txtValorPix;
        ImageView imgSeta;

        public PixKeyViewHolder(@NonNull View itemView) {
            // Inicialização dos componentes da interface
            super(itemView);
            txtTipoPix = itemView.findViewById(R.id.txtTipoPix);
            txtValorPix = itemView.findViewById(R.id.txtValorPix);
            imgSeta = itemView.findViewById(R.id.imgSeta);
        }
    }
}

