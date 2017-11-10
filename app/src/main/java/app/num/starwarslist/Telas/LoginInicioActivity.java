package app.num.starwarslist.Telas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.num.starwarslist.DB.CriaBanco;
import app.num.starwarslist.DB.ManipulaBanco;
import app.num.barcodescannerproject.R;


public class LoginInicioActivity extends ActionBarActivity {

    public SQLiteDatabase BancoDeDados = null;
    static String NomeBanco = "StarWarsList";
    public int idUs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);
        final AlertDialog.Builder Erro = new AlertDialog.Builder(LoginInicioActivity.this);
        BancoDeDados = openOrCreateDatabase(NomeBanco, Context.MODE_PRIVATE, null);
        final ManipulaBanco inser = new ManipulaBanco();
        CriaBanco cria = new CriaBanco();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final EditText Nome_Us = (EditText) findViewById(R.id.etNomeUs);

        if(cria.criaBanco(BancoDeDados)) {

            String Us = inser.Verifica_Us(BancoDeDados, Nome_Us.getText().toString().trim());

            Log.i("us", Us);

            if (!Us.equals("SemUs")) {

                Nome_Us.setText(Us);

            }

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Nome_Us.getText().toString().isEmpty()) {

                        Erro.setTitle("Atenção!");
                        Erro.setMessage("Necessário preencher um nome de usuário antes de prosseguir.");
                        Erro.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                });
                        Erro.show();

                    }else if(Nome_Us.getText().toString().contains(" ")){

                        Erro.setTitle("Atenção!");
                        Erro.setMessage("Não é permisido colocar espaços no nome de Usuário.");
                        Erro.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                });
                        Erro.show();

                    }else{

                        //Retorna o Id do usuário. (Caso Mude o nome, irá gerar outro Id)
                        idUs = inser.Adc_Us(BancoDeDados, Nome_Us.getText().toString().trim());

                        startActivity(new Intent(LoginInicioActivity.this, MainActivity.class));

                        finish();
                    }

                }
            });
        }


    }


}
