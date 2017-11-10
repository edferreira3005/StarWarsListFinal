package app.num.starwarslist.Telas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.Result;

import app.num.barcodescannerproject.R;
import app.num.starwarslist.DB.CriaBanco;
import app.num.starwarslist.DB.ManipulaBanco;
import app.num.starwarslist.GeoLocalizacao.Localizacao;
import app.num.starwarslist.Permissoes.BuscaPermissoes;
import app.num.starwarslist.Sincronizacao.SincronizaDados;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    CriaBanco cria = new CriaBanco();
    ListView lvPersonagens;
    public int id_personagem;

    boolean doubleBackToExitPressedOnce = false;
    ManipulaBanco inser = new ManipulaBanco();
    LoginInicioActivity login = new LoginInicioActivity();
    AlertDialog.Builder Erro;
    BuscaPermissoes set = new BuscaPermissoes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (cria.criaBanco(login.BancoDeDados)) {
            setContentView(R.layout.activity_main);
            Erro = new AlertDialog.Builder(MainActivity.this);

            //Atualizando Lista ao Abrir Aplicativos
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            inser.atualizaLista(lvPersonagens, login.BancoDeDados, MainActivity.this);


            //Verificando permissões do aplicativo


            set.setPermitions(getBaseContext(), MainActivity.this);


        }

    }

    public void QrScanner(View view) {

        if (isOnline()) {


            mScannerView = new ZXingScannerView(this);

            setContentView(mScannerView);


            mScannerView.setResultHandler(this);
            mScannerView.startCamera();

        } else {

            Erro.setMessage("Sem conexão com a Internet!!\n\nA conexão é necessária para a leitura do QR Code.");
            Erro.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
            Erro.show();
        }

    }

    @Override
    public void handleResult(Result rawResult) {

        //Voltando para tela inicial
        if (cria.criaBanco(login.BancoDeDados)) {

            setContentView(R.layout.activity_main);

            final String url = rawResult.getText();

            final LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            final LocationListener mlocListener = new Localizacao();

            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);



            Location Local = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            double Latitude = 0;
            double Longitude = 0;


            //Inserindo informações buscadas no Banco de dados Interno
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            final ManipulaBanco inser = new ManipulaBanco();


            if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {



                Erro = new AlertDialog.Builder(MainActivity.this);

                Erro.setMessage("O GPS encontra-se desligado. Não será possível coletar dados de Localização.\n\n" +
                                "Deseja continuar?");
                final double finalLatitude = Latitude;
                final double finalLongitude = Longitude;
                Erro.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                new SincronizaDados(login.BancoDeDados, url, lvPersonagens, inser,
                                        MainActivity.this, login.idUs, finalLatitude, finalLongitude).execute();

                                dialog.cancel();
                            }
                        });

                Erro.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                inser.atualizaLista(lvPersonagens, login.BancoDeDados, MainActivity.this);

                                dialog.cancel();

                            }
                        });
                Erro.show();


            }else{

                if( Local != null){
                    Latitude = Local.getLatitude();
                    Longitude = Local.getLongitude();
                }

                new SincronizaDados(login.BancoDeDados, url, lvPersonagens, inser,
                        MainActivity.this, login.idUs, Latitude, Longitude).execute();
            }

        }

        mScannerView.stopCamera();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }else{
            mScannerView.stopCamera();
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

    protected boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
