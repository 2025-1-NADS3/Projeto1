package br.com.bankpay.bankpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.bankpay.bankpayacademy.utils.SharedPrefsHelper;

public class PixAddAmountActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private EditText txtValor;
    private ImageView imgLimparTexto;
    private ImageView imgVoltar;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_add_amount);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        txtValor = findViewById(R.id.txtValor);
        imgLimparTexto = findViewById(R.id.imgLimparTexto);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Recupera o ID do usuário a partir do SharedPrefsHelper
        idUsuario = SharedPrefsHelper.getUsuarioId(this);

        // Validação para caso não encontre o usuario
        if (idUsuario == -1) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela se não estiver logado
        }

        txtValor.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    txtValor.removeTextChangedListener(this);

                    String limparString = s.toString().replaceAll("[R$,.\\s]", "");

                    try {
                        // Converte para double e divide por 100 para pegar as casas decimais
                        double parsed = Double.parseDouble(limparString) / 100;

                        // Formata o valor usado em Reais
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                        String formatted = format.format(parsed);

                        // Atualiza a variável com o novo valor formatado
                        current = formatted;
                        txtValor.setText(formatted);
                        txtValor.setSelection(formatted.length());
                    } catch (NumberFormatException e) {
                        s.clear();
                    }

                    txtValor.addTextChangedListener(this);
                }
            }
        });

        imgLimparTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtValor.getText().clear();
            }
        });

        // Botão (imagem) para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

    }

    // Função para validar os campos de chave pix, valor e encaminhar os dados para a próxima tela de confirmação
    public void avancar(View view) {
        String valorString = txtValor.getText().toString().trim().replaceAll("[^\\d]", "");

        // Verifica se o campo de chave pix ou valor está vazio, caso esteja ele vai usar um return para o usuario não prosseguir sem digitar
        if (valorString.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Transformar o valorString em um valor double
        double valor;
        try {
            valor = Double.parseDouble(valorString) / 100;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Passa os dados para a tela PixConfirmAmountActivity
        Intent intent = new Intent(PixAddAmountActivity.this, PixConfirmAmountActivity.class);
        intent.putExtra("id_usuario", idUsuario);
        intent.putExtra("valor", valor);
        startActivity(intent);
    }

}