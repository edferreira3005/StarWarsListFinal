package app.num.barcodescannerproject.DB;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import app.num.barcodescannerproject.Internet.ConexaoUrls;
import app.num.barcodescannerproject.Internet.Download;
import app.num.barcodescannerproject.R;
import app.num.barcodescannerproject.Telas.InfoActivity;
import app.num.barcodescannerproject.Telas.MainActivity;

/**
 * Created by edson.ferreira on 06/11/2017.
 */

public class ManipulaBanco {

    String SQL;

    public boolean InserePersonagem(SQLiteDatabase BancoDeDados, String URL){
        try {

            //Buscando dados para adicionar
            final ConexaoUrls ar = new ConexaoUrls();
            String jsonStr = ar.chamadaGet(URL);
            JSONObject jsonObj = new JSONObject(jsonStr);


            //Verificando se o personagem já foi adicionado
            Cursor VerificaRegistro = BancoDeDados.rawQuery("SELECT A._id , A.NOME_PERSONA FROM PERSONAGEM A " +
                    " WHERE NOME_PERSONA = '" + jsonObj.getString("name").trim() + "'", null);

            if(VerificaRegistro.getCount() == 0) {

                //Adicionando Personagem
                SQL = " INSERT INTO PERSONAGEM (NOME_PERSONA, URL) " +
                        " VALUES('" + jsonObj.getString("name").trim() + "', '" + URL + "') ;";
                BancoDeDados.execSQL(SQL);

                Log.i("Personagem",SQL);

                Cursor PegarIdPersonagem = BancoDeDados.rawQuery("SELECT A._id  FROM PERSONAGEM A " +
                        " WHERE NOME_PERSONA = '" + jsonObj.getString("name").trim() + "'", null);

                if(PegarIdPersonagem.getCount() > 0){
                    PegarIdPersonagem.moveToFirst();

                    //Inserindo nas Outras tabelas de dados
                    InsereInfoPersonagem(PegarIdPersonagem.getInt(0),BancoDeDados, jsonObj,ar);
                }

            }

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void InsereInfoPersonagem(int Id_Personagem,SQLiteDatabase BancoDeDados,
                                      JSONObject Json_Personagem,ConexaoUrls ar){
        int i;

        try {
            //Verifica se as informações dos filmes estão adicionadas ou não
            if(Select_InfoFilmes(BancoDeDados).getCount() == 0) {
                insereInfoFilmes(BancoDeDados, ar);
            }

            //Pegando Informações complementares (Mundo, Naves Espaciais, Veículos e Filmes, Especie)
            //Mundo
            String jsonStr_Mundo = ar.chamadaGet(Json_Personagem.getString("homeworld").trim());
            JSONObject jsonObj_Mundo = new JSONObject(jsonStr_Mundo);
            String Mundo = jsonObj_Mundo.getString("name").trim();

            //Filmes
            JSONArray array_Filmes = Json_Personagem.getJSONArray("films");

            //Especie
            JSONArray array_Especie = Json_Personagem.getJSONArray("species");

            //Veículos
            JSONArray array_Veiculos = Json_Personagem.getJSONArray("vehicles");

            //Naves Espaciais
            JSONArray array_Nv_Espaci = Json_Personagem.getJSONArray("starships");


            //Adicionando Informações Básicas do Personagem
            SQL = " INSERT INTO INFO_BASICA (ID_PERSONAGEM  ,ALTURA, PESO, " +
                    "COR_CABELO, COR_PELE, COR_OLHO,ANO_NASCIMENTO," +
                    "GENERO, MUNDO_NATAL, ANO_CRIACAO, ANO_EDIT) " +
                    " VALUES(" + Id_Personagem + "," + Json_Personagem.getString("height").trim() +
                    "," + Json_Personagem.getString("mass").trim() + ",'" + Json_Personagem.getString("hair_color").trim() + "'" +
                    ",'" + Json_Personagem.getString("skin_color").trim() + "','" + Json_Personagem.getString("eye_color").trim() + "'" +
                    ",'" + Json_Personagem.getString("birth_year").trim() + "','" + Json_Personagem.getString("gender").trim() + "'" +
                    ",'" + Mundo + "','" + Json_Personagem.getString("created").trim() + "'" +
                    ",'" + Json_Personagem.getString("edited").trim() + "') ;";
            BancoDeDados.execSQL(SQL);
            Log.i("Informações Básicas",SQL);

            //Adicionando Filmes
            for (i = 0; i < array_Filmes.length(); i++) {

                String jsonStr_Filmes = ar.chamadaGet(array_Filmes.getString(i).trim());
                JSONObject jsonObj_Filmes = new JSONObject(jsonStr_Filmes);
                String Filme = jsonObj_Filmes.getString("title").trim();

                int id_filme = Select_id_Filme(BancoDeDados,Filme);

                if(id_filme != 0) {

                    SQL = " INSERT INTO PERSONA_FILMES (ID_PERSONAGEM,ID_FILME)" +
                            "VALUES(" + Id_Personagem + "," + id_filme + ") ;";
                    BancoDeDados.execSQL(SQL);

                }

                Log.i("Filmes",SQL);
            }

            //Adicionando Especie
            for (i = 0; i < array_Especie.length(); i++) {

                String jsonStr_Especie = ar.chamadaGet(array_Especie.getString(i).trim());
                JSONObject jsonObj_Especie = new JSONObject(jsonStr_Especie);
                String Especie = jsonObj_Especie.getString("name").trim();

                SQL = " INSERT INTO ESPECIE (ID_PERSONAGEM,ESPECIE)" +
                        "VALUES(" + Id_Personagem + ",'" + Especie + "') ;";
                BancoDeDados.execSQL(SQL);

                Log.i("Especie",SQL);
            }

            //Adicionando Veículos
            for (i = 0; i < array_Veiculos.length(); i++) {

                String jsonStr_Veiculos = ar.chamadaGet(array_Veiculos.getString(i).trim());
                JSONObject jsonObj_Veiculos = new JSONObject(jsonStr_Veiculos);
                String Veiculo = jsonObj_Veiculos.getString("name").trim();

                SQL = " INSERT INTO VEICULOS (ID_PERSONAGEM,NOME_VEICULO)" +
                        "VALUES(" + Id_Personagem + ",'" + Veiculo + "') ;";
                BancoDeDados.execSQL(SQL);

                Log.i("Veículo",SQL);
            }

            //Adicionando Naves Espaciais
            for (i = 0; i < array_Nv_Espaci.length(); i++) {

                String jsonStr_Nv_Espaci = ar.chamadaGet(array_Nv_Espaci.getString(i).trim());
                JSONObject jsonObj_Nv_Espaci = new JSONObject(jsonStr_Nv_Espaci);
                String Nave_Espacial = jsonObj_Nv_Espaci.getString("name").trim();

                SQL = " INSERT INTO NAVES_ESPACIAIS (ID_PERSONAGEM,NOME_NAVE)" +
                        "VALUES(" + Id_Personagem + ",'" + Nave_Espacial + "') ;";
                BancoDeDados.execSQL(SQL);

                Log.i("Nave Espacial",SQL);
            }

        }catch (Exception e){
            Log.e("erro", e.toString());
        }



    }

    private void insereInfoFilmes(SQLiteDatabase BancoDeDados, ConexaoUrls ar){

        Download down = new Download();

        //URLs de todos os filmes de StarWars da Movie DB API
        String InfoFilme1 = ar.chamadaGet("http://api.themoviedb.org/3/movie/1893?api_key=d223d6ab8902080ffddac5a06e71dba4");
        String InfoFilme2 = ar.chamadaGet("http://api.themoviedb.org/3/movie/1894?api_key=d223d6ab8902080ffddac5a06e71dba4");
        String InfoFilme3 = ar.chamadaGet("http://api.themoviedb.org/3/movie/1895?api_key=d223d6ab8902080ffddac5a06e71dba4");
        String InfoFilme4 = ar.chamadaGet("http://api.themoviedb.org/3/movie/11?api_key=d223d6ab8902080ffddac5a06e71dba4");
        String InfoFilme5 = ar.chamadaGet("http://api.themoviedb.org/3/movie/1891?api_key=d223d6ab8902080ffddac5a06e71dba4");
        String InfoFilme6 = ar.chamadaGet("http://api.themoviedb.org/3/movie/1892?api_key=d223d6ab8902080ffddac5a06e71dba4");
        String InfoFilme7 = ar.chamadaGet("http://api.themoviedb.org/3/movie/140607?api_key=d223d6ab8902080ffddac5a06e71dba4");
        String InfoFilme8 = ar.chamadaGet("http://api.themoviedb.org/3/movie/181808?api_key=d223d6ab8902080ffddac5a06e71dba4");

        try {
            //formando arquivo JSon com as informações dos filmes
            JSONObject jsonObj_Fil1 = new JSONObject(InfoFilme1);
            JSONObject jsonObj_Fil2 = new JSONObject(InfoFilme2);
            JSONObject jsonObj_Fil3 = new JSONObject(InfoFilme3);
            JSONObject jsonObj_Fil4 = new JSONObject(InfoFilme4);
            JSONObject jsonObj_Fil5 = new JSONObject(InfoFilme5);
            JSONObject jsonObj_Fil6 = new JSONObject(InfoFilme6);
            JSONObject jsonObj_Fil7 = new JSONObject(InfoFilme7);
            JSONObject jsonObj_Fil8 = new JSONObject(InfoFilme8);

            //Fazendo o download das imagens dos filmes
            Bitmap bPoster_Filme1 = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185/" + jsonObj_Fil1.getString("poster_path").trim());
            Bitmap bPoster_Filme2 = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185/" + jsonObj_Fil2.getString("poster_path").trim());
            Bitmap bPoster_Filme3 = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185/" + jsonObj_Fil3.getString("poster_path").trim());
            Bitmap bPoster_Filme4 = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185/" + jsonObj_Fil4.getString("poster_path").trim());
            Bitmap bPoster_Filme5 = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185/" + jsonObj_Fil5.getString("poster_path").trim());
            Bitmap bPoster_Filme6 = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185/" + jsonObj_Fil6.getString("poster_path").trim());
            Bitmap bPoster_Filme7 = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185/" + jsonObj_Fil7.getString("poster_path").trim());
            Bitmap bPoster_Filme8 = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185/" + jsonObj_Fil8.getString("poster_path").trim());

            //Verificando se todas as imagens foram baixadas
            if(bPoster_Filme1 != null && bPoster_Filme2 != null && bPoster_Filme3 != null
                    && bPoster_Filme4 != null && bPoster_Filme5 != null && bPoster_Filme6 != null
                    && bPoster_Filme7 != null && bPoster_Filme8 != null) {

                //Transformando os BitMaps das imagens para ByteArray
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bPoster_Filme1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray1 = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream = new ByteArrayOutputStream();
                bPoster_Filme2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray2 = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream = new ByteArrayOutputStream();
                bPoster_Filme3.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray3 = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream = new ByteArrayOutputStream();
                bPoster_Filme4.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray4 = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream = new ByteArrayOutputStream();
                bPoster_Filme5.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray5 = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream = new ByteArrayOutputStream();
                bPoster_Filme6.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray6 = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream = new ByteArrayOutputStream();
                bPoster_Filme7.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray7 = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream = new ByteArrayOutputStream();
                bPoster_Filme8.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray8 = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream = null;

                //Convertendo em Base64 para adicionar ao banco de dados
                String Poster_Filme1 = Base64.encodeToString(byteArray1, Base64.DEFAULT);
                String Poster_Filme2 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
                String Poster_Filme3 = Base64.encodeToString(byteArray3, Base64.DEFAULT);
                String Poster_Filme4 = Base64.encodeToString(byteArray4, Base64.DEFAULT);
                String Poster_Filme5 = Base64.encodeToString(byteArray5, Base64.DEFAULT);
                String Poster_Filme6 = Base64.encodeToString(byteArray6, Base64.DEFAULT);
                String Poster_Filme7 = Base64.encodeToString(byteArray7, Base64.DEFAULT);
                String Poster_Filme8 = Base64.encodeToString(byteArray8, Base64.DEFAULT);

                //Inserindo informações dos filme nas tabelas
                SQL = "INSERT INTO FILMES(NOME_FILME,SITE_OFICI,POSTER)" +
                        " VALUES('" + jsonObj_Fil1.get("original_title") + "','" + jsonObj_Fil1.get("homepage") + "'" +
                        ",'" + Poster_Filme1 + "');";
                BancoDeDados.execSQL(SQL);
                SQL = "INSERT INTO FILMES(NOME_FILME,SITE_OFICI,POSTER)" +
                        " VALUES('" + jsonObj_Fil2.get("original_title") + "','" + jsonObj_Fil2.get("homepage") + "'" +
                        ",'" + Poster_Filme2 + "');";
                BancoDeDados.execSQL(SQL);
                SQL = "INSERT INTO FILMES(NOME_FILME,SITE_OFICI,POSTER)" +
                        " VALUES('" + jsonObj_Fil3.get("original_title") + "','" + jsonObj_Fil3.get("homepage") + "'" +
                        ",'" + Poster_Filme3 + "');";
                BancoDeDados.execSQL(SQL);

                //O Título Original é "Star Wars" somente. Mas deve ser alterado para "Star Wars: A New Hope"
                SQL = "INSERT INTO FILMES(NOME_FILME,SITE_OFICI,POSTER)" +
                        " VALUES('" + jsonObj_Fil4.get("original_title") + ": A New Hope" + "','" + jsonObj_Fil4.get("homepage") + "'" +
                        ",'" + Poster_Filme4 + "');";
                BancoDeDados.execSQL(SQL);
                SQL = "INSERT INTO FILMES(NOME_FILME,SITE_OFICI,POSTER)" +
                        " VALUES('" + jsonObj_Fil5.get("original_title") + "','" + jsonObj_Fil5.get("homepage") + "'" +
                        ",'" + Poster_Filme5 + "');";
                BancoDeDados.execSQL(SQL);
                SQL = "INSERT INTO FILMES(NOME_FILME,SITE_OFICI,POSTER)" +
                        " VALUES('" + jsonObj_Fil6.get("original_title") + "','" + jsonObj_Fil6.get("homepage") + "'" +
                        ",'" + Poster_Filme6 + "');";
                BancoDeDados.execSQL(SQL);
                SQL = "INSERT INTO FILMES(NOME_FILME,SITE_OFICI,POSTER)" +
                        " VALUES('" + jsonObj_Fil7.get("original_title") + "','" + jsonObj_Fil7.get("homepage") + "'" +
                        ",'" + Poster_Filme7 + "');";
                BancoDeDados.execSQL(SQL);
                SQL = "INSERT INTO FILMES(NOME_FILME,SITE_OFICI,POSTER)" +
                        " VALUES('" + jsonObj_Fil8.get("original_title") + "','" + jsonObj_Fil8.get("homepage") + "'" +
                        ",'" + Poster_Filme8 + "');";
                BancoDeDados.execSQL(SQL);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public Cursor Select_Personagem(int id_personagem, SQLiteDatabase BancoDeDados){

        Cursor cursor;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id," +
                "                            A.NOME_PERSONA," +
                "                            ID_USUARIO," +
                "                            GEOLOCALIZACAO, " +
                "                            DATA_CAPTURA " +
                "                        FROM " +
                "                           PERSONAGEM A" +
                "                        WHERE " +
                "                             A._id = " + id_personagem +
                "                        ORDER " +
                "                            BY A.NOME_PERSONA", null);

        return cursor;
    }

    public Cursor Select_Info_Basica(int id_personagem,SQLiteDatabase BancoDeDados){

        Cursor cursor;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id," +
                "                            A.ID_PERSONAGEM," +
                "                            ALTURA," +
                "                            PESO, " +
                "                            COR_CABELO, " +
                "                            COR_PELE, " +
                "                            COR_OLHO, " +
                "                            ANO_NASCIMENTO, " +
                "                            GENERO, " +
                "                            MUNDO_NATAL, " +
                "                            ANO_CRIACAO, " +
                "                            ANO_EDIT" +
                "                        FROM " +
                "                           INFO_BASICA A" +
                "                        WHERE " +
                "                             A.ID_PERSONAGEM = " + id_personagem, null);

        return cursor;
    }

    public Cursor Select_Persona_Filmes(int id_personagem,SQLiteDatabase BancoDeDados){

        Cursor cursor;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id," +
                "                            B.NOME_FILME," +
                "                            B.SITE_OFICI," +
                "                            B.POSTER " +
                "                        FROM " +
                "                           PERSONA_FILMES A INNER JOIN FILMES B ON B._id = A.ID_FILME" +
                "                        WHERE " +
                "                             A.ID_PERSONAGEM = " + id_personagem +
                "                        ORDER " +
                "                            BY B._id", null);

        return cursor;
    }

    public Cursor Select_Especie(int id_personagem,SQLiteDatabase BancoDeDados){

        Cursor cursor;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id," +
                "                            A.ID_PERSONAGEM," +
                "                            ESPECIE" +
                "                        FROM " +
                "                           ESPECIE A" +
                "                        WHERE " +
                "                             A.ID_PERSONAGEM = " + id_personagem +
                "                        ORDER " +
                "                            BY A.ESPECIE", null);

        return cursor;
    }

    public Cursor Select_Nave_Espacial(int id_personagem,SQLiteDatabase BancoDeDados){

        Cursor cursor;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id," +
                "                            A.ID_PERSONAGEM," +
                "                            NOME_NAVE" +
                "                        FROM " +
                "                           NAVES_ESPACIAIS A" +
                "                        WHERE " +
                "                             A.ID_PERSONAGEM = " + id_personagem +
                "                        ORDER " +
                "                            BY A.NOME_NAVE", null);

        return cursor;
    }

    public Cursor Select_Veiculos(int id_personagem,SQLiteDatabase BancoDeDados){

        Cursor cursor;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id," +
                "                            A.ID_PERSONAGEM," +
                "                            NOME_VEICULO" +
                "                        FROM " +
                "                           VEICULOS A" +
                "                        WHERE " +
                "                             A.ID_PERSONAGEM = " + id_personagem +
                "                        ORDER " +
                "                            BY A.NOME_VEICULO", null);

        return cursor;
    }

    public Cursor Select_InfoFilmes(SQLiteDatabase BancoDeDados){

        Cursor cursor;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id," +
                "                            A.NOME_FILME," +
                "                            SITE_OFICI," +
                "                            POSTER " +
                "                        FROM " +
                "                           FILMES A" +
                "                        ORDER " +
                "                            BY A.NOME_FILME", null);

        return cursor;
    }

    private int Select_id_Filme(SQLiteDatabase BancoDeDados, String Nome_Filme){

        Cursor cursor;
        int id = 0;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id" +
                "                        FROM " +
                "                           FILMES A" +
                "                        WHERE " +
                "                            NOME_FILME LIKE '%" + Nome_Filme + "%'", null);

        if(cursor.getCount() > 0){

            cursor.moveToFirst();
            id = cursor.getInt(0);

        }

        return id;
    }

    public void atualizaLista(ListView listaPersonagem, SQLiteDatabase BancoDeDados
                               , final Context context){

        final Cursor personagens = BancoDeDados.rawQuery("SELECT A._id, A.NOME_PERSONA FROM PERSONAGEM A" +
                " ORDER BY A.NOME_PERSONA", null);

        final String[] coluna = new String[]{"NOME_PERSONA"};
        SimpleCursorAdapter AdapterLista;

        if (personagens.getCount() > 0) {

            personagens.moveToFirst();

            AdapterLista = new SimpleCursorAdapter(context, R.layout.mostra_banco, personagens,
                    coluna, new int[]{R.id.tvCarregaDado});

            listaPersonagem.setAdapter(AdapterLista);

            listaPersonagem.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {


                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MainActivity main = new MainActivity();

                            main.id_personagem = personagens.getInt(0);

                            context.startActivity(new Intent(context, InfoActivity.class));

                        }
                    }

            );

        }

    }

    public int Adc_Us(SQLiteDatabase BancoDeDados,String Nome_us){

        int idUs;

        Cursor Busca_Us = BancoDeDados.rawQuery("SELECT " +
                "         A._id,A.NOME_USUARIO" +
                "    FROM" +
                "       USUARIO A " +
                "    WHERE" +
                "        NOME_USUARIO = '" + Nome_us + "'",null);

        if(Busca_Us.getCount() == 0){

            SQL = "UPDATE USUARIO SET ULTIMO_US = null";
            BancoDeDados.execSQL(SQL);

            SQL = "INSERT INTO USUARIO(NOME_USUARIO,ULTIMO_US)" +
                    "VALUES('" + Nome_us + "','S');";
            BancoDeDados.execSQL(SQL);
            Log.i("inserindo",SQL);

            Busca_Us = BancoDeDados.rawQuery("SELECT " +
                    "         A._id,A.NOME_USUARIO" +
                    "    FROM" +
                    "       USUARIO A " +
                    "    WHERE" +
                    "        NOME_USUARIO = '" + Nome_us + "'",null);

            Busca_Us.moveToFirst();
            idUs= Busca_Us.getInt(0);
            Log.i("id","" + idUs);

        }else{
            Busca_Us.moveToFirst();
            idUs = Busca_Us.getInt(0);
            Log.i("id","" + idUs);

        }

        return idUs;

    }

    public String Verifica_Us(SQLiteDatabase BancoDeDados,String us){

        Log.i("nome",us);

        Cursor Busca_Us = BancoDeDados.rawQuery("SELECT " +
                "         A._id,A.NOME_USUARIO" +
                "    FROM" +
                "       USUARIO A " +
                "    WHERE" +
                "        ULTIMO_US = 'S'",null);



        if(Busca_Us.getCount() > 0){

            Busca_Us.moveToFirst();
            return Busca_Us.getString(1);
        }else{

            return "SemUs";

        }

    }

}
