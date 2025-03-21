package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    // Campos do Formulario de Cadastro
    private EditText txtNome, txtCpf, txtDataNasc, txtEmail, txtCelular, txtSenha;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNome = findViewById(R.id.txtNome);
        txtCpf = findViewById(R.id.txtCpf);
        txtDataNasc = findViewById(R.id.txtDataNasc);
        txtEmail = findViewById(R.id.txtEmail);
        txtCelular = findViewById(R.id.txtCelular);
        txtSenha = findViewById(R.id.txtSenha);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(View -> validarCampos());

        ImageView imgVoltar = (ImageView) findViewById(R.id.imgVoltar);
        imgVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validarCampos() {
        String nome = txtNome.getText().toString().trim();
        String cpf = txtCpf.getText().toString().trim();
        String dataNascimento = txtDataNasc.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String celular = txtCelular.getText().toString().trim();
        String senha = txtSenha.getText().toString().trim();

        if (nome.isEmpty() || cpf.isEmpty() || dataNascimento.isEmpty() ||
                email.isEmpty() || celular.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else if (!validarCPF(cpf)) {
            Toast.makeText(this, "CPF inválido!", Toast.LENGTH_SHORT).show();
        } else if (!validarEmail(email)) {
            Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
        } else {
            String dataNascimentoConvertida = converterDataNascimento(dataNascimento);
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    // Validação de CPF
    private boolean validarCPF(String cpf) {

        // Vai remover pontos e traços digitados pelo usuario
        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int[] pesos1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int digito1 = calcularDigito(cpf.substring(0, 9), pesos1);
        int digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesos2);

        return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
    }

    private int calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += (str.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    // Validação do Email
    private boolean validarEmail(String email) {
        String regex = "^[\\w-\\.]+@edu\\.[\\w-]+\\.[a-zA-Z]{2,4}$";
        return email.matches(regex);
    }

    // Função para converter data de nascimento (dd/MM/YYYY) para (yyyy-MM-dd)
    private String converterDataNascimento(String dataNascimento) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date data = formatoEntrada.parse(dataNascimento);
            return formatoSaida.format(data);
        } catch (ParseException e) {
            return null;
        }
    }



}