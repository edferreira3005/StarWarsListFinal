package app.num.starwarslist.Sincronizacao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;

import app.num.starwarslist.DB.ManipulaBanco;

/**
 * Created by edson.ferreira on 08/11/2017.
 */

public class SincronizaDados  extends AsyncTask<Void, Void, Void> {


        SQLiteDatabase BancoDeDados;
        String url;
        ListView Lista_Personagem;
        ManipulaBanco inser;
        Context context;
        ProgressDialog carregar;
        String MessagemErro = "";
        double Latitude;
        double Longitude;

    int idUs;

        public SincronizaDados(SQLiteDatabase BancoDeDados, String url , ListView Lista_Personagem,
                               ManipulaBanco inser, Context context, int idUs, double Latitude,
                               double Longitude){


            //Setando variáveis necessárias para a inserção dos dados
            this.BancoDeDados = BancoDeDados;
            this.url = url;
            this.Lista_Personagem = Lista_Personagem;
            this.inser = inser;
            this.context = context;
            carregar = new ProgressDialog(context);
            this.idUs = idUs;
            this.Latitude = Latitude;
            this.Longitude = Longitude;

        }

        @Override
        protected Void doInBackground(Void... param) {

            //Inserindo no Bando de dados
            ManipulaBanco inser = new ManipulaBanco();
            if(!inser.InserePersonagem(BancoDeDados, url,idUs,Latitude,Longitude,context)){

                MessagemErro = "QR Code Inválido.\n\n" +
                        " Verifique se a URL está correta e se está com Internet.";

            }


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            inser.atualizaLista(Lista_Personagem, BancoDeDados,context);
            carregar.dismiss();

            if(!MessagemErro.isEmpty()){

                final AlertDialog.Builder Erro = new AlertDialog.Builder(context);
                Erro.setMessage(MessagemErro);
                Erro.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                               dialog.cancel();
                            }
                        });
                Erro.show();


            }
            super.onPostExecute(aVoid);

        }

        @Override
        protected  void onPreExecute()
        {

            carregar.setTitle("AGUARDE...");
            carregar.setMessage("Carregando dados.");
            carregar.setCancelable(false);
            carregar.show();



        }



}
