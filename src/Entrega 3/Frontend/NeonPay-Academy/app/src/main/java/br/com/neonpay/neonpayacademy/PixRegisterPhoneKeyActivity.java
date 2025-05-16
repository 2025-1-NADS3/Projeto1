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

import br.com.neonpay.neonpayacademy.utils.CPFTextWatcher;
import br.com.neonpay.neonpayacademy.utils.PhoneTextWatcher;

public class PixRegisterPhoneKeyActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private EditText txtCelular;
    private Button btnRegistrarCelular;
    private ImageView imgVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_register_phone_key);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        txtCelular = findViewById(R.id.txtCelular);
        btnRegistrarCelular = findViewById(R.id.btnRegistrarCelular);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> finish());

        // Quando clicar no botão vai chamar a função registrarChaveCelular()
        btnRegistrarCelular.setOnClickListener(view -> {
            registrarChaveCelular();
        });

        // Adicionando mascara ao usuario digitar celular
        txtCelular.addTextChangedListener(new PhoneTextWatcher(txtCelular));
    }

    // Função para registrar chave pix usando celular
    private void registrarChaveCelular() {
        String celular = txtCelular.getText().toString().trim().replaceAll("[^\\d]", ""); // Remover pontos e traço do celular

        // Realiza validações antes fazer requisição ao servidor
        if (celular.isEmpty()) {
            Toast.makeText(this, "Digite o celular", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realiza a validação do celular chamando a função validarCelular()
        if (!validarCelular(celular)) {
            Toast.makeText(this, "Celular inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Passa os dados para a tela PixKeyConfirmationActivity
        Intent intent = new Intent(PixRegisterPhoneKeyActivity.this, PixKeyConfirmationActivity.class);
        intent.putExtra("chave_pix", celular);
        startActivity(intent);
    }

    // Função para validar celular aceitando apenas numeros de 10 ou 11 digitos
    private boolean validarCelular(String celular) {
        return celular.replaceAll("[^0-9]", "").matches("^\\d{10,11}$");
    }

}