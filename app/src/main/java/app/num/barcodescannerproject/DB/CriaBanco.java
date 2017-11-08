package app.num.barcodescannerproject.DB;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by edson.ferreira on 06/11/2017.
 */

public class CriaBanco {

    String SQL;
    public boolean criaBanco( SQLiteDatabase BancoDeDados ){

        //Tabela do Usuário
        SQL = " CREATE TABLE IF NOT EXISTS USUARIO ( _id INTEGER PRIMARY KEY,NOME_USUARIO TEXT, ULTIMO_US TEXT); \n\n ";
        BancoDeDados.execSQL(SQL);

        //Tabela do Personagem
        SQL = " CREATE TABLE IF NOT EXISTS PERSONAGEM ( _id INTEGER PRIMARY KEY,NOME_PERSONA TEXT ," +
        "ID_USUARIO INT, URL TEXT, GEOLOCALIZACAO TEXT, DATA_CAPTURA DATE); \n\n ";
        BancoDeDados.execSQL(SQL);

        //Tabela de Detalhes do Personagem Básicas
        SQL = " CREATE TABLE IF NOT EXISTS INFO_BASICA ( _id INTEGER PRIMARY KEY,ID_PERSONAGEM INT ," +
              "ALTURA DOUBLE, PESO DOUBLE, COR_CABELO TEXT, COR_PELE TEXT, COR_OLHO TEXT, " +
             " ANO_NASCIMENTO TEXT, GENERO TEXT, MUNDO_NATAL TEXT, ANO_CRIACAO DATE, ANO_EDIT DATE ); \n\n";
        BancoDeDados.execSQL(SQL);

        //Tabela de Informações do Filme
        SQL = " CREATE TABLE IF NOT EXISTS FILMES ( _id INTEGER PRIMARY KEY,NOME_FILME TEXT ," +
                "SITE_OFICI TEXT, POSTER TEXT); \n\n";

        BancoDeDados.execSQL(SQL);

        //Tabela de Veículos
        SQL = " CREATE TABLE IF NOT EXISTS VEICULOS ( _id INTEGER PRIMARY KEY,ID_PERSONAGEM INT ," +
                "NOME_VEICULO TEXT); \n\n";
        BancoDeDados.execSQL(SQL);

        //Tabela de Naves Espaciais
        SQL = " CREATE TABLE IF NOT EXISTS NAVES_ESPACIAIS ( _id INTEGER PRIMARY KEY,ID_PERSONAGEM INT ," +
                "NOME_NAVE TEXT); \n\n";

        BancoDeDados.execSQL(SQL);

        //Tabela de Especies
        SQL = " CREATE TABLE IF NOT EXISTS ESPECIE ( _id INTEGER PRIMARY KEY,ID_PERSONAGEM INT ," +
                "ESPECIE TEXT); \n\n";

        BancoDeDados.execSQL(SQL);


        //Tabela de Filmes por Personagem
        SQL = " CREATE TABLE IF NOT EXISTS PERSONA_FILMES ( _id INTEGER PRIMARY KEY,ID_PERSONAGEM INT ," +
                "ID_FILME INT, URL TEXT); \n\n";
        BancoDeDados.execSQL(SQL);


        return true;

    }
}
