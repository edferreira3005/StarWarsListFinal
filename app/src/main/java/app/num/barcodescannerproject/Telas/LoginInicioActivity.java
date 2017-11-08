package app.num.barcodescannerproject.Telas;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import app.num.barcodescannerproject.DB.CriaBanco;
import app.num.barcodescannerproject.DB.ManipulaBanco;
import app.num.barcodescannerproject.R;

/**
 * Created by edson.ferreira on 08/11/2017.
 */

public class LoginInicioActivity extends ActionBarActivity {

    public static SQLiteDatabase BancoDeDados = null;
    static String NomeBanco = "StarWarsList";
    public static int idUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_usuario);
        BancoDeDados = openOrCreateDatabase(NomeBanco, Context.MODE_PRIVATE, null);
        final ManipulaBanco inser = new ManipulaBanco();
        CriaBanco cria = new CriaBanco();

        ImageButton btnLogin = (ImageButton) findViewById(R.id.btnLogin);
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


                    //Retorna o Id do usuário. (Caso Mude o nome, irá gerar outro Id)
                    idUs = inser.Adc_Us(BancoDeDados, Nome_Us.getText().toString().trim());

                    startActivity(new Intent(LoginInicioActivity.this, MainActivity.class));

                    finish();

                }
            });
        }


    }
}
