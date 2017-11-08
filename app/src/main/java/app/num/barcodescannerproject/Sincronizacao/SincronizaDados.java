package app.num.barcodescannerproject.Sincronizacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;

import app.num.barcodescannerproject.DB.ManipulaBanco;

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

        public SincronizaDados(SQLiteDatabase BancoDeDados, String url , ListView Lista_Personagem,
                               ManipulaBanco inser, Context context){

            this.BancoDeDados = BancoDeDados;
            this.url = url;
            this.Lista_Personagem = Lista_Personagem;
            this.inser = inser;
            this.context = context;
            carregar = new ProgressDialog(context);

        }

        @Override
        protected Void doInBackground(Void... param) {

            ManipulaBanco inser = new ManipulaBanco();
            inser.InserePersonagem(BancoDeDados, url);


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            inser.atualizaLista(Lista_Personagem, BancoDeDados,context);
            carregar.dismiss();
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
