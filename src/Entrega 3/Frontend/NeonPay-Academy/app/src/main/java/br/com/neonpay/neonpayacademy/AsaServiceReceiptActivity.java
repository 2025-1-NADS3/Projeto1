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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.neonpay.neonpayacademy.utils.SharedPrefsHelper;

public class AsaServiceReceiptActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface
    private ImageView imgVoltar;
    private TextView txtStatus, txtValor;
    private Button btnBaixarComprovante, btnMenu;
    private String valor, chavePix, nomeServico, data;
    private String nome, email, cpf;
    private File pdfFile;
    private ArrayList<File> pdfFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asa_service_receipt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        txtStatus = findViewById(R.id.txtStatus);
        txtValor = findViewById(R.id.txtValor);
        btnBaixarComprovante = findViewById(R.id.btnBaixarComprovante);
        btnMenu = findViewById(R.id.btnMenu);

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

        // Receber dados da Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            chavePix = bundle.getString("chave_pix", "Não disponível");
            data = bundle.getString("data", "Data não informada");
            nome = bundle.getString("nome", "Nome não informado");
            cpf = bundle.getString("cpf", "CPF não informado");
            email = bundle.getString("email", "Email não informado");

            // Recebe os serviços e gera os PDFs
            String servicosJsonString = bundle.getString("servicos");
            if (servicosJsonString != null) {
                try {
                    JSONArray servicosArray = new JSONArray(servicosJsonString);

                    for (int i = 0; i < servicosArray.length(); i++) {
                        JSONObject servicoObj = servicosArray.getJSONObject(i);
                        String titulo = servicoObj.getString("titulo");
                        double preco = servicoObj.getDouble("preco");

                        String precoFormatado = formatarValor(preco);

                        // Gera o PDF para cada serviço e guarda na lista
                        File arquivoPdf = gerarComprovantePDF(titulo, precoFormatado, chavePix, data);
                        if (arquivoPdf != null) {
                            pdfFiles.add(arquivoPdf);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao processar serviços.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        // Exibe o valor total na tela
        int valorTotal = bundle.getInt("valor_total", 0);
        txtValor.setText(formatarValor(valorTotal));

        // Botão para compartilhar o pdf
        btnBaixarComprovante.setOnClickListener(v -> {
            if (!pdfFiles.isEmpty()) {
                compartilharPDF(pdfFiles.get(pdfFiles.size() - 1));
            } else {
                Toast.makeText(this, "Nenhum arquivo PDF gerado.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Função para gerar o comprovante em pdf
    private File gerarComprovantePDF(String servico, String valorPago, String chavePix, String data) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Configuração do layout do PDF
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(14);

        int y = 50;
        canvas.drawText("Comprovante de Pagamento", 50, y, paint);
        y += 20;
        canvas.drawText("────────────────────────────────────", 50, y, paint);

        y += 30;
        canvas.drawText("Nome: " + nome, 50, y, paint);
        y += 20;
        canvas.drawText("CPF: " + cpf, 50, y, paint);
        y += 20;
        canvas.drawText("Email: " + email, 50, y, paint);
        y += 30;
        canvas.drawText("Status: PAGO", 50, y, paint);
        y += 20;
        canvas.drawText("Serviço: " + servico, 50, y, paint);
        y += 20;
        canvas.drawText("Valor: " + valorPago, 50, y, paint);
        y += 20;
        canvas.drawText("Chave PIX: " + chavePix, 50, y, paint);
        y += 20;
        canvas.drawText("Data: " + converterData(data), 50, y, paint);
        y += 30;
        canvas.drawText("────────────────────────────────────", 50, y, paint);
        y += 30;
        canvas.drawText("Obrigado por utilizar nossos serviços!", 50, y, paint);

        pdfDocument.finishPage(page);

        // Salva o PDF
        File pdfFile = null;
        try {
            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (dir != null && !dir.exists()) dir.mkdirs();

            pdfFile = new File(dir, "comprovante_" + servico.replace(" ", "_") + ".pdf");
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

    // Função para formatar o valor em R$
    private String formatarValor(double valor) {
        DecimalFormat df = new DecimalFormat("R$ #,##0.00");
        return df.format(valor);
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