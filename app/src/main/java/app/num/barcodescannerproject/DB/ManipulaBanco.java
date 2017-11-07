package app.num.barcodescannerproject.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.num.barcodescannerproject.Conexao.ConexaoUrls;

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

                SQL = " INSERT INTO FILMES (ID_PERSONAGEM,NOME_FILME)" +
                        "VALUES(" + Id_Personagem + ",'" + Filme + "') ;";
                BancoDeDados.execSQL(SQL);

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

    public Cursor Select_Filmes(int id_personagem,SQLiteDatabase BancoDeDados){

        Cursor cursor;

        cursor = BancoDeDados.rawQuery("SELECT " +
                "                            A._id," +
                "                            A.ID_PERSONAGEM," +
                "                            NOME_FILME," +
                "                            URL " +
                "                        FROM " +
                "                           FILMES A" +
                "                        WHERE " +
                "                             A.ID_PERSONAGEM = " + id_personagem +
                "                        ORDER " +
                "                            BY A.NOME_FILME", null);

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

}
