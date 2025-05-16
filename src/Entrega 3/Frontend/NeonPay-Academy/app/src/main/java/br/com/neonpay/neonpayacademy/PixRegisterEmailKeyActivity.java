package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PixRegisterEmailKeyActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private EditText txtEmail;
    private Button btnRegistrarEmail;
    private ImageView imgVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_register_email_key);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        txtEmail = findViewById(R.id.txtEmail);
        btnRegistrarEmail = findViewById(R.id.btnRegistrarEmail);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> finish());

        // Quando clicar no botão vai chamar a função registrarChaveEmail()
        btnRegistrarEmail.setOnClickListener(view -> {
            registrarChaveEmail();
        });

    }

    // Função para registrar chave pix usando email
    private void registrarChaveEmail() {
        String email = txtEmail.getText().toString().trim();

        // Realiza validações antes fazer requisição ao servidor
        if (email.isEmpty()) {
            Toast.makeText(this, "Digite o email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realiza a validação do email chamando a função validarEmail()
        if (!validarEmail(email)) {
            Toast.makeText(this, "Email inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Passa os dados para a tela PixKeyConfirmationActivity
        Intent intent = new Intent(PixRegisterEmailKeyActivity.this, PixKeyConfirmationActivity.class);
        intent.putExtra("chave_pix", email);
        startActivity(intent);
    }

    // Validação do Email
    private boolean validarEmail(String email) {
        String regex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(regex);
    }

}