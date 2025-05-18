package br.com.bankpay.bankpayacademy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class PixKeyDetailsActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private Button btnExcluirChavePix;
    private TextView txtTituloChavePix, txtChavePix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_key_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        btnExcluirChavePix = findViewById(R.id.btnExcluirChavePix);
        txtTituloChavePix = findViewById(R.id.txtTituloChavePix);
        txtChavePix = findViewById(R.id.txtChavePix);

        // Botão (imagem) para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        // Recebe os dados da Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String tipoPix = bundle.getString("tipo_pix");
            String valorPix = bundle.getString("valor_pix");

            // Exibe o tipo da chave pix, e a chave pix do usuario
            txtTituloChavePix.setText(tipoPix);
            txtChavePix.setText(valorPix);
        }

        // Quando clicado no botão de excluir chave pix, chama o metodo mostrarDialogDeletarPix()
        btnExcluirChavePix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogDeletarPix();
            }
        });

    }

    // Método para exibir um diálogo de confirmação antes de excluir uma chave Pix
    private void mostrarDialogDeletarPix() {

        // Infla o layout de poup
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_confirm_delete_key_pix, null);

        // Cria o AlertDialog com o layout personalizado
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        // Deixa o fundo do AlertDialog transparente
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Botão de Excluir
        MaterialButton btnExcluir = dialogView.findViewById(R.id.btnConfirmarExclusao);
        btnExcluir.setOnClickListener(v -> {
            // Após clicar em Excluir, vai ir para tela de confirmar a exclusão digitando sua senha
            Intent intent = new Intent(PixKeyDetailsActivity.this, PixKeyConfirmDeleteActivity.class);
            startActivity(intent);
        });

        // Botão de Cancelar
        MaterialButton btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

}