package app.num.starwarslist.Telas;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import app.num.barcodescannerproject.R;
import app.num.starwarslist.Customizacao.CustomCursorAdapter;
import app.num.starwarslist.DB.ManipulaBanco;

public class InfoActivity extends AppCompatActivity {

    SimpleCursorAdapter AdapterLista;
    public static CustomCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_info);

        //Adicionando especie, pois pode haver mais de uma
        String sEspecie = "";

        //Pegando Campos para preenchimento das informações
        TextView tvNomePersona = (TextView) findViewById(R.id.tvNomePersona);
        TextView tvAltura = (TextView) findViewById(R.id.tvAltura);
        TextView tvPeso = (TextView) findViewById(R.id.tvPeso);
        TextView tvCorCabelo = (TextView) findViewById(R.id.tvCorCabelo);
        TextView tvCorPele = (TextView) findViewById(R.id.tvCorPele);
        TextView tvCorOlho = (TextView) findViewById(R.id.tvCorOlho);
        TextView tvAnoNasc = (TextView) findViewById(R.id.tvAnoNasc);
        TextView tvGenero = (TextView) findViewById(R.id.tvGenero);
        TextView tvPlanetaNatal = (TextView) findViewById(R.id.tvPlanetaNatal);
        TextView tvDtCriacao = (TextView) findViewById(R.id.tvDtCriacao);
        TextView tvDtEdita = (TextView) findViewById(R.id.tvDtEdita);
        TextView tvVeiculos = (TextView) findViewById(R.id.tvVeiculos);
        TextView tvEspecie = (TextView) findViewById(R.id.tvEspecie);
        TextView tvGeoLocal = (TextView) findViewById(R.id.tvGeoLocal);
        TextView tvDataCap = (TextView) findViewById(R.id.tvDataCap);

        //Pegando Listas para preenchimento das Informações
        ListView lvFilmes = (ListView) findViewById(R.id.lvFilmes);
        ListView lvVeicNave = (ListView) findViewById(R.id.lvVeicNave);

        //Buscando Banco de Dados Principal e Campos no banco de dados para visualização
        LoginInicioActivity login = new LoginInicioActivity();
        MainActivity main = new MainActivity();
        ManipulaBanco select = new ManipulaBanco();

        final Cursor InfoBasic = select.Select_Info_Basica(main.id_personagem,login.BancoDeDados);
        final Cursor Filmes = select.Select_Persona_Filmes(main.id_personagem,login.BancoDeDados);
        Cursor Especie = select.Select_Especie(main.id_personagem,login.BancoDeDados);
        Cursor Veiculos = select.Select_Veiculos(main.id_personagem,login.BancoDeDados);
        Cursor Naves_Espaciais = select.Select_Nave_Espacial(main.id_personagem,login.BancoDeDados);
        Cursor Personagem = select.Select_Personagem(main.id_personagem,login.BancoDeDados);

        if(Personagem.getCount() > 0){

            Personagem.moveToFirst();
            tvNomePersona.setText(Personagem.getString(1));
            tvGeoLocal.setText(tvGeoLocal.getText() + " " + Personagem.getString(3).trim());
            tvDataCap.setText(tvDataCap.getText() + " " + Personagem.getString(4).trim());


        }

        if(InfoBasic.getCount() > 0){

            InfoBasic.moveToFirst();

            tvAltura.setText(tvAltura.getText() + " " + InfoBasic.getDouble(2));
            tvPeso.setText(tvPeso.getText() + " " + InfoBasic.getDouble(3));
            tvCorCabelo.setText(tvCorCabelo.getText() + " " + InfoBasic.getString(4));
            tvCorPele.setText(tvCorPele.getText() + " " + InfoBasic.getString(5));
            tvCorOlho.setText(tvCorOlho.getText() + " " + InfoBasic.getString(6));
            tvAnoNasc.setText(tvAnoNasc.getText() + " " + InfoBasic.getString(7));
            tvGenero.setText(tvGenero.getText() + " " + InfoBasic.getString(8));
            tvPlanetaNatal.setText(tvPlanetaNatal.getText() + " " + InfoBasic.getString(9));
            tvDtCriacao.setText(tvDtCriacao.getText() + " " + InfoBasic.getString(10));
            tvDtEdita.setText(tvDtEdita.getText() + " " + InfoBasic.getString(11));

        }

        //Filmes

        if (Filmes.getCount() > 0) {

            adapter = new CustomCursorAdapter(getBaseContext(), Filmes);
            lvFilmes.setAdapter(adapter);

            lvFilmes.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {


                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            try {
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Filmes.getString(2)));
                                startActivity(myIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(InfoActivity.this, "No application can handle this request."
                                        + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }
                    }

            );

        }

        //Juntando Veículos e Naves Espaciais em 1 única Lista
            MergeCursor TodosVeículos = new MergeCursor(new Cursor[] { Naves_Espaciais, Veiculos });

        if(TodosVeículos.getCount() > 0){

            try {

                TodosVeículos.moveToFirst();

                final String[] coluna = new String[]{"NOME_NAVE"};

                AdapterLista = new SimpleCursorAdapter(this, R.layout.lista, TodosVeículos,
                        coluna, new int[]{R.id.tvCarregaDado});

                lvVeicNave.setAdapter(AdapterLista);

                lvVeicNave.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {


                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            }
                        }

                );
            }catch (Exception e){

                TodosVeículos.moveToFirst();

                final String[] coluna = new String[]{"NOME_VEICULO"};

                AdapterLista = new SimpleCursorAdapter(this, R.layout.lista, TodosVeículos,
                        coluna, new int[]{R.id.tvCarregaDado});

                lvVeicNave.setAdapter(AdapterLista);

                lvVeicNave.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {


                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            }
                        }

                );



            }

        }else{
            tvVeiculos.setVisibility(View.INVISIBLE);
        }

        Especie.moveToFirst();

        do{

            if(Especie.isFirst()) {

                sEspecie += Especie.getString(2);

            }else{

                sEspecie += " / " + Especie.getString(2);

            }

        }while(Especie.moveToNext());

        tvEspecie.setText(tvEspecie.getText() + " " + sEspecie);

    }



}
