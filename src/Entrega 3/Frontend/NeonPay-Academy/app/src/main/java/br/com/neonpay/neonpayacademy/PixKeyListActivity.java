package br.com.neonpay.neonpayacademy;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.neonpay.neonpayacademy.adapter.PixKeyAdapter;
import br.com.neonpay.neonpayacademy.model.PixKey;

public class PixKeyListActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private RecyclerView recyclerPixKeys;
    private TextView txtTitulo;
    private ImageView imgVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_key_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        recyclerPixKeys = findViewById(R.id.recyclerPixKeys);
        txtTitulo = findViewById(R.id.txtTitulo);
        imgVoltar = findViewById(R.id.imgVoltar);

        // Botão (imagem) para retornar à tela anterior
        imgVoltar.setOnClickListener(view -> {
            finish();
        });

        // Recebe os dados da intent
        ArrayList<String> tipos = getIntent().getStringArrayListExtra("tipos");
        ArrayList<String> valores = getIntent().getStringArrayListExtra("valores");

        // Verifica se os arrays não são nulos e se possuem o mesmo número de elementos
        if (tipos == null || valores == null || tipos.size() != valores.size()) {
            txtTitulo.setText("Erro ao carregar chaves Pix");
            return;
        }

        // Converte listas de tipo e valor em lista de objetos PixKey
        List<PixKey> pixKeys = new ArrayList<>();
        for (int i = 0; i < tipos.size(); i++) {
            pixKeys.add(new PixKey(tipos.get(i), valores.get(i)));
        }

        // Usa PixKeyAdapter com a lista de objetos PixKey
        PixKeyAdapter adapter = new PixKeyAdapter(this, pixKeys);
        recyclerPixKeys.setLayoutManager(new LinearLayoutManager(this));
        recyclerPixKeys.setAdapter(adapter);

    }
}