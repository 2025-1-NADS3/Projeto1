package br.com.neonpay.neonpayacademy;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PixTransferReceiptActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private ImageView imgVoltar;
    private Button btnBaixarComprovante;
    private double valor;
    private TextView txtValor;
    private Button btnMenu;
    private TextView txtNomeDestinatario;
    private File pdfFile;
    private ArrayList<File> pdfFiles = new ArrayList<>();
    private String nomeDestinatario, chavePixDestinatario, nomeRemetente, cpfRemetente, chavePixRemetente, dataTransacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pix_transfer_receipt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        txtValor = findViewById(R.id.txtValor);
        txtNomeDestinatario = findViewById(R.id.txtNomeDestinatario);
        btnMenu = findViewById(R.id.btnMenu);
        btnBaixarComprovante = findViewById(R.id.btnBaixarComprovante);

        // Botão (imagem) para retornar à HomeActivity
        imgVoltar.setOnClickListener(view -> {
            // Invoca a "HomeActivity"
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        // Botão para retornar à HomeActivity
        btnMenu.setOnClickListener(view -> {
            // Invoca a "HomeActivity"
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        // Recebendo os dados da Tela que foram enviados pelo Intent:
        Bundle bundle = getIntent().getExtras();
        valor = bundle.getDouble("valor", 0);
        nomeDestinatario = bundle.getString("nome_destinatario");
        chavePixDestinatario = bundle.getString("chave_pix_destinatario");

        nomeRemetente = bundle.getString("nome_remetente");
        cpfRemetente = bundle.getString("cpf_remetente");
        chavePixRemetente = bundle.getString("chave_pix_remetente");

        dataTransacao = bundle.getString("data_transacao");

        // Mostrando dados na tela
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); // Formata o valor para o formato em Reais
        txtValor.setText(format.format(valor));
        txtNomeDestinatario.setText("Para: " + nomeDestinatario);

        // Botão para compartilhar o pdf
        btnBaixarComprovante.setOnClickListener(v -> {
            File file = gerarComprovantePDF();
            if (file != null) {
                compartilharPDF(file);
            } else {
                Toast.makeText(this, "Erro ao gerar PDF.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Função para gerar o comprovante em pdf
    private File gerarComprovantePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(14);

        int y = 50;
        canvas.drawText("Comprovante de Transferência Pix", 50, y, paint);
        y += 30;

        canvas.drawText("De:", 50, y, paint);
        y += 20;
        canvas.drawText("Nome: " + nomeRemetente, 50, y, paint);
        y += 20;
        canvas.drawText("Chave Pix: " + chavePixRemetente, 50, y, paint);
        y += 30;

        canvas.drawText("Para:", 50, y, paint);
        y += 20;
        canvas.drawText("Nome: " + nomeDestinatario, 50, y, paint);
        y += 20;
        canvas.drawText("Chave Pix: " + chavePixDestinatario, 50, y, paint);
        y += 30;

        // Formata o valor
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        canvas.drawText("Valor: " + format.format(valor), 50, y, paint);

        y += 30;
        canvas.drawText("Data Transação: " + converterData(dataTransacao), 50, y, paint);

        pdfDocument.finishPage(page);

        File pdfFile = null;
        try {
            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (dir != null && !dir.exists()) dir.mkdirs();

            String tempoAtual = new SimpleDateFormat("dd-MM-YYYY_HH-mm", Locale.getDefault()).format(new Date());
            pdfFile = new File(dir, "comprovante_pix_" + tempoAtual + ".pdf");

            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(this, "PDF gerado: " + pdfFile.getName(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao gerar PDF.", Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }

        return pdfFile;
    }

    // Função para compartilhar o PDF
    private void compartilharPDF(File pdfFile) {
        if (pdfFile != null && pdfFile.exists()) {
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", pdfFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(Intent.createChooser(shareIntent, "Compartilhar comprovante via"));
        } else {
            Toast.makeText(this, "Arquivo PDF não encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    // Função para converter data (yyyy-MM-dd) para (dd/MM/YYYY)
    private String converterData(String dataNascimento) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date data = formatoEntrada.parse(dataNascimento);
            return formatoSaida.format(data);
        } catch (ParseException e) {
            Log.e("Registro", "Erro ao converter data: " + dataNascimento, e);
            return null;
        }
    }
}