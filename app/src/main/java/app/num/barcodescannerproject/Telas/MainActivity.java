package app.num.barcodescannerproject.Telas;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.Result;

import app.num.barcodescannerproject.DB.CriaBanco;
import app.num.barcodescannerproject.DB.ManipulaBanco;
import app.num.barcodescannerproject.Permissoes.BuscaPermissoes;
import app.num.barcodescannerproject.R;
import app.num.barcodescannerproject.Sincronizacao.SincronizaDados;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    CriaBanco cria = new CriaBanco();
    ListView lvPersonagens;
    public static int id_personagem;

    boolean doubleBackToExitPressedOnce = false;
    ManipulaBanco inser = new ManipulaBanco();
    LoginInicioActivity login = new LoginInicioActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (cria.criaBanco(login.BancoDeDados)) {
            setContentView(R.layout.activity_main);

            //Atualizando Lista ao Abrir Aplicativos
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            inser.atualizaLista(lvPersonagens, login.BancoDeDados, MainActivity.this);


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
        if (cria.criaBanco(login.BancoDeDados)) {

            setContentView(R.layout.activity_main);

            String url = rawResult.getText();


            //Inserindo informações buscadas no Banco de dados Interno
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            ManipulaBanco inser = new ManipulaBanco();

            new SincronizaDados(login.BancoDeDados, url,lvPersonagens,inser,MainActivity.this).execute();
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
            inser.atualizaLista(lvPersonagens, login.BancoDeDados, MainActivity.this);
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
