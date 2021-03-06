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

    private ZXingScannerView QRCodeScanner;
    CriaBanco cria = new CriaBanco();
    ListView lvPersonagens;
    public static int id_personagem;

    boolean sair = false;
    ManipulaBanco inser = new ManipulaBanco();
    LoginInicioActivity login = new LoginInicioActivity();
    AlertDialog.Builder Erro;
    BuscaPermissoes set = new BuscaPermissoes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Criando Banco de dados
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
        //Verificando se tem internet antes de iniciar a leitura do QR Code
        if (temInternet()) {

            //Abrindo Leitura do QR Code através do ZXingScannerView
            QRCodeScanner = new ZXingScannerView(this);

            setContentView(QRCodeScanner);

            QRCodeScanner.setResultHandler(this);
            QRCodeScanner.startCamera();

        } else {

            Erro.setMessage("No Internet connection!\n\nThe connection is required for reading the QR Code.");
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

            final LocationManager GerenciaLocaliz = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locListener = new Localizacao();

            //Obrigatório verificação novamente dos acessos de localização
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            GerenciaLocaliz.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);



            Location Local = GerenciaLocaliz.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            double Latitude = 0;
            double Longitude = 0;


            //Inserindo informações buscadas no Banco de dados Interno
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            final ManipulaBanco inser = new ManipulaBanco();


            if (!GerenciaLocaliz.isProviderEnabled(LocationManager.GPS_PROVIDER)) {



                Erro = new AlertDialog.Builder(MainActivity.this);

                Erro.setMessage("GPS is off. Location data can not be collected.\n\n" +
                                "Wish to continue?\n");

                final double finalLatitude = Latitude;
                final double finalLongitude = Longitude;

                Erro.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //Sincronizando dados do personagem
                                new SincronizaDados(login.BancoDeDados, url, lvPersonagens, inser,
                                        MainActivity.this, login.idUs, finalLatitude, finalLongitude).execute();

                                dialog.cancel();
                            }
                        });

                Erro.setNegativeButton("No",
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

                //Sincronizando dados do personagem
                new SincronizaDados(login.BancoDeDados, url, lvPersonagens, inser,
                        MainActivity.this, login.idUs, Latitude, Longitude).execute();
            }

        }

        if(QRCodeScanner != null) {

            QRCodeScanner.stopCamera();

        }
    }


    @Override
    public void onBackPressed() {
        if (sair) {
            super.onBackPressed();
            return;
        }else{

            //Voltando para tela principal quando apertar o botão "voltar" do aparelho

            if(QRCodeScanner != null) {

                QRCodeScanner.stopCamera();

            }

            setContentView(R.layout.activity_main);

            //Atualizando Lista
            lvPersonagens = (ListView) findViewById(R.id.lvPersonagens);
            inser.atualizaLista(lvPersonagens, login.BancoDeDados, MainActivity.this);

        }

        //Verificando se clicou 2 vezes para sair do aplicativo
        this.sair = true;
        Toast.makeText(this, "Press the Button again to exit the Application", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                sair = false;
            }
        }, 2000);

    }

    //Processo que verifica se o aparelho está conectado a internet
    protected boolean temInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
