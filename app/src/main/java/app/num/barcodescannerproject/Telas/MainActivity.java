package app.num.barcodescannerproject.Telas;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.zxing.Result;

import app.num.barcodescannerproject.DB.CriaBanco;
import app.num.barcodescannerproject.DB.ManipulaBanco;
import app.num.barcodescannerproject.Permissoes.BuscaPermissoes;
import app.num.barcodescannerproject.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    public static SQLiteDatabase BancoDeDados = null;
    static String NomeBanco = "StarWarsList";
    SimpleCursorAdapter AdapterLista;
    CriaBanco cria = new CriaBanco();
    ListView lvPersonagens;
    public static int id_personagem;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Criando o Banco de dados
        BancoDeDados = openOrCreateDatabase(NomeBanco, Context.MODE_PRIVATE, null);


        if (cria.criaBanco(BancoDeDados)) {
            setContentView(R.layout.activity_main);

            //Atualizando Lista ao Abrir Aplicativos
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            atualizaLista(lvPersonagens, BancoDeDados);


            //Verificando permissões do aplicativo

            BuscaPermissoes set = new BuscaPermissoes();

            set.setPermitions(getBaseContext(), MainActivity.this);


        }

    }

    public void QrScanner(View view){


        mScannerView = new ZXingScannerView(this);

        setContentView(mScannerView);


        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    public void handleResult(Result rawResult) {

        //Voltando para tela inicial
        if (cria.criaBanco(BancoDeDados)) {

            setContentView(R.layout.activity_main);

            String url = rawResult.getText();


            //Inserindo informações buscadas no Banco de dados Interno

            ManipulaBanco inser = new ManipulaBanco();

            inser.InserePersonagem(BancoDeDados, url);


            //Atualizando Lista ao Adicionar Personagens
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            atualizaLista(lvPersonagens, BancoDeDados);
        }
    }


    private void atualizaLista(ListView listaPersonagem, SQLiteDatabase BancoDeDados){

        final Cursor personagens = BancoDeDados.rawQuery("SELECT A._id, A.NOME_PERSONA FROM PERSONAGEM A" +
                " ORDER BY A.NOME_PERSONA", null);

        final String[] coluna = new String[]{"NOME_PERSONA"};

        if (personagens.getCount() > 0) {

            personagens.moveToFirst();

            AdapterLista = new SimpleCursorAdapter(this, R.layout.mostra_banco, personagens,
                    coluna, new int[]{R.id.tvCarregaDado});

            listaPersonagem.setAdapter(AdapterLista);

            listaPersonagem.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {


                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            id_personagem = personagens.getInt(0);

                            startActivity(new Intent(MainActivity.this, InfoActivity.class));

                        }
                    }

            );

        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }else{
            setContentView(R.layout.activity_main);
            //Atualizando Lista
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            atualizaLista(lvPersonagens, BancoDeDados);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Aperte o Botão novamente para sair do Aplicativo", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

}
