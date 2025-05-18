package br.com.bankpay.bankpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.bankpay.bankpayacademy.utils.CPFTextWatcher;
import br.com.bankpay.bankpayacademy.utils.DateTextWatcher;
import br.com.bankpay.bankpayacademy.utils.PhoneTextWatcher;

public class RegisterActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private EditText txtNome, txtCpf, txtDataNasc, txtEmail, txtCelular, txtSenha;
    private Button btnRegister;
    private ImageView imgVoltar;

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

        // Inicialização dos componentes da interface
        txtNome = findViewById(R.id.txtNome);
        txtCpf = findViewById(R.id.txtCpf);
        txtDataNasc = findViewById(R.id.txtDataNasc);
        txtEmail = findViewById(R.id.txtEmail);
        txtCelular = findViewById(R.id.txtCelular);
        txtSenha = findViewById(R.id.txtSenha);
        btnRegister = findViewById(R.id.btnRegister);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Adicionando mascara ao usuario digitar o cpf, celular e data de nascimento
        txtCpf.addTextChangedListener(new CPFTextWatcher(txtCpf));
        txtCelular.addTextChangedListener(new PhoneTextWatcher(txtCelular));
        txtDataNasc.addTextChangedListener(new DateTextWatcher(txtDataNasc));

        // Quando clicar no botão vai chamar a função registrarUsuario()
        btnRegister.setOnClickListener(view -> {
            registrarUsuario();
        });

        // Botão para retornar à tela de boas-vindas
        imgVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

    }

    // Função para realizar o cadastro do usuário através de uma requisição POST
    private void registrarUsuario() {
        String nome = txtNome.getText().toString().trim();
        String cpf = txtCpf.getText().toString().trim().replaceAll("[^\\d]", ""); // Remover pontos e traço do cpf
        String dataNascimento = txtDataNasc.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String celular = txtCelular.getText().toString().trim().replaceAll("[^\\d]", ""); // Remover pontos e traço do celular
        String senha = txtSenha.getText().toString().trim();

        // Realiza validações antes fazer requisição ao servidor
        if (nome.isEmpty() || cpf.isEmpty() || dataNascimento.isEmpty() ||
                email.isEmpty() || celular.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realiza a validação do CPF chamando a função validarCPF()
        if (!validarCPF(cpf)) {
            Toast.makeText(this, "CPF inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realiza a validação do Email chamando a função validarEmail()
        if (!validarEmail(email)) {
            Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realiza a validação do Celular chamando a função validarCelular()
        if (!validarCelular(celular)) {
            Toast.makeText(this, "Celular inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chama a função converterDataNascimento() e armazena numa String dataConvertida para poder enviar ao banco de dados
        String dataConvertida = converterDataNascimento(dataNascimento);
        if (dataConvertida == null) {
            Toast.makeText(this, "Data de nascimento inválida! ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Adicionando log para verficar se está sendo feito corretamento o armazenamento das informações nas variaveis
        Log.d("Registro", "Cadastro: ");
        Log.d("Registro", "Nome: " + nome);
        Log.d("Registro", "CPF: " + cpf);
        Log.d("Registro", "Data Nascimento: " + dataConvertida);
        Log.d("Registro", "Email: " + email);
        Log.d("Registro", "Celular: " + celular);
        Log.d("Registro", "Senha: " + senha);

        // Criando objeto JSON para enviar na requisição
        JSONObject dadosUsuario = new JSONObject();
        try {
            dadosUsuario.put("nome", nome);
            dadosUsuario.put("cpf", cpf);
            dadosUsuario.put("data_nasc", dataConvertida);
            dadosUsuario.put("email", email);
            dadosUsuario.put("telefone", celular);
            dadosUsuario.put("senha", senha);
            Log.d("Registro", "JSON criado: " + dadosUsuario.toString());
        } catch (JSONException e) {
            Log.e("Registro", "Erro ao criar JSON", e);
            return;
        }

        String url = "http://10.0.2.2:3000/api/cadastro";
        //String url = "https://plyhcd-3000.csb.app/api/cadastro";

        // Criando requisição POST para o servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dadosUsuario,
                response -> {
                    Log.d("Registro", "Resposta da API: " + response.toString());
                    Toast.makeText(RegisterActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                },
                error -> {
                    Log.e("Registro", "Erro ao cadastrar: " + error.toString());
                    Toast.makeText(RegisterActivity.this, "Erro ao cadastrar!", Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    // Validação de CPF (removendo caracteres especiais e verificando se é válido)
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

    // Calcula os dígitos verificadores do CPF
    private int calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += (str.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    // Validação do Email aceitando apenas dominio com @edu.fecap.br
    private boolean validarEmail(String email) {
        String regex = "^[\\w.-]+@edu\\.fecap\\.br$";
        return email.matches(regex);
    }
    // Função para converter data de nascimento (dd/MM/YYYY) para (yyyy-MM-dd)
    private String converterDataNascimento(String dataNascimento) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date data = formatoEntrada.parse(dataNascimento);
            return formatoSaida.format(data);
        } catch (ParseException e) {
            Log.e("Registro", "Erro ao converter data: " + dataNascimento, e);
            return null;
        }
    }

    // Função para validar celular aceitando apenas numeros de 10 ou 11 digitos
    private boolean validarCelular(String celular) {
        return celular.replaceAll("[^0-9]", "").matches("^\\d{10,11}$");
    }

}