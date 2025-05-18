package br.com.bankpay.bankpayacademy;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RedeemPointsReceiptActivity extends AppCompatActivity {

    // Declaração das variáveis dos elementos da interface e armazenamento de valores
    private ImageView imgVoltar;
    private TextView txtPontos, txtCodigoPedido;
    private Button btnBaixarComprovante, btnMenu;
    private String chavePix, data;
    private String nome, email, cpf;
    private File pdfFile;
    private int senhaPedido, pontosTotal;
    private ArrayList<File> pdfFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_redeem_points_receipt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialização dos componentes da interface
        imgVoltar = findViewById(R.id.imgVoltar);
        txtCodigoPedido = findViewById(R.id.txtCodigoPedido);
        txtPontos = findViewById(R.id.txtPontos);
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
            senhaPedido = bundle.getInt("senha_pedido", 0);

            // Exibe o valor total de pontos e o codigo para resgate na tela
            pontosTotal = bundle.getInt("pontos_gastos", 0);
            txtPontos.setText(pontosTotal + " pontos");
            txtCodigoPedido.setText("Codigo para Regaste do Produto: " + senhaPedido);

            // Recebe os itens e gera os PDFs
            String itensJsonString = bundle.getString("itens");
            if (itensJsonString != null) {
                try {
                    JSONArray itensArray = new JSONArray(itensJsonString);
                    ArrayList<String> listaItensFormatados = new ArrayList<>();

                    for (int i = 0; i < itensArray.length(); i++) {
                        JSONObject itensObj = itensArray.getJSONObject(i);
                        String titulo = itensObj.getString("titulo");
                        int pontos = itensObj.getInt("pontos");

                        listaItensFormatados.add(titulo + " - " + pontos);
                    }

                    File arquivoPdf = gerarComprovantePDF(listaItensFormatados, chavePix, data);
                    if (arquivoPdf != null) {
                        pdfFiles.add(arquivoPdf);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao processar serviços.", Toast.LENGTH_SHORT).show();
                }
            }
        }

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
    private File gerarComprovantePDF(ArrayList<String> itens, String chavePix, String data) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 800, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(14);

        // Escreve no pdf as informações do comprovante
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
        canvas.drawText("Codigo para Regaste do Produto: " + senhaPedido, 50, y, paint);
        y += 30;
        canvas.drawText("Itens:", 50, y, paint);
        y += 20;

        for (String item : itens) {
            canvas.drawText("- " + item, 60, y, paint);
            y += 20;
        }

        y += 20;
        canvas.drawText("Pontos Total: " + pontosTotal, 50, y, paint);
        y += 20;
        canvas.drawText("Chave PIX: " + chavePix, 50, y, paint);
        y += 20;
        canvas.drawText("Data: " + converterData(data), 50, y, paint);
        y += 30;
        canvas.drawText("────────────────────────────────────", 50, y, paint);
        y += 30;
        canvas.drawText("Obrigado por utilizar nossos serviços!", 50, y, paint);

        pdfDocument.finishPage(page);

        File pdfFile = null;
        try {
            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (dir != null && !dir.exists()) dir.mkdirs();

            pdfFile = new File(dir, "comprovante_pedido.pdf");
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