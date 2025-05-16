package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PixChooseKeyTypeActivity extends AppCompatActivity {

    // Declaração dos elementos da interface (View)
    private RadioGroup radioGroupChavePix;
    private Button btnCriarChavePix;
    private RadioButton radioCPF, radioChaveAleatoria, radioCelular, radioEmail;
    private ImageView imgVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_choose_key_type);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vinculando os elementos com os Views
        radioGroupChavePix = findViewById(R.id.radioGroupChavePix);
        btnCriarChavePix = findViewById(R.id.btnCriarChavePix);
        radioCPF = findViewById(R.id.radioCPF);
        radioChaveAleatoria = findViewById(R.id.radioChaveAleatoria);
        radioCelular = findViewById(R.id.radioCelular);
        radioEmail = findViewById(R.id.radioEmail);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Desativa o botão
        btnCriarChavePix.setEnabled(false);

        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> finish());

        radioGroupChavePix.setOnCheckedChangeListener((group, checkedId) -> {

            // Habilita o botão
            btnCriarChavePix.setEnabled(true);
            btnCriarChavePix.setBackgroundColor(ContextCompat.getColor(this, R.color.appColorRoxo));

            // Resetar todos para preto antes de aplicar a cor ao selecionado
            radioCPF.setTextColor(ContextCompat.getColor(this, R.color.appColorPreto));
            radioChaveAleatoria.setTextColor(ContextCompat.getColor(this, R.color.appColorPreto));
            radioCelular.setTextColor(ContextCompat.getColor(this, R.color.appColorPreto));
            radioEmail.setTextColor(ContextCompat.getColor(this, R.color.appColorPreto));

            // Aplicar branco somente no botão selecionado
            if (checkedId == R.id.radioCPF) {
                radioCPF.setTextColor(ContextCompat.getColor(this, R.color.appColorBranco));
                Intent intent = new Intent(PixChooseKeyTypeActivity.this, PixRegisterCpfKeyActivity.class);
                startActivity(intent);
            } else if (checkedId == R.id.radioChaveAleatoria) {
                radioChaveAleatoria.setTextColor(ContextCompat.getColor(this, R.color.appColorBranco));
                Intent intent = new Intent(PixChooseKeyTypeActivity.this, PixRegisterRandomKeyActivity.class);
                startActivity(intent);
            } else if (checkedId == R.id.radioCelular) {
                radioCelular.setTextColor(ContextCompat.getColor(this, R.color.appColorBranco));
                Intent intent = new Intent(PixChooseKeyTypeActivity.this, PixRegisterPhoneKeyActivity.class);
                startActivity(intent);
            } else if (checkedId == R.id.radioEmail) {
                radioEmail.setTextColor(ContextCompat.getColor(this, R.color.appColorBranco));
                Intent intent = new Intent(PixChooseKeyTypeActivity.this, PixRegisterEmailKeyActivity.class);
                startActivity(intent);
            }
        });

    }
}