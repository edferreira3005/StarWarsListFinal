package app.num.starwarslist;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import org.junit.Test;

import app.num.starwarslist.DB.CriaBanco;
import app.num.starwarslist.Internet.ConexaoUrls;
import app.num.starwarslist.Internet.Download;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ComponentsTest {
    @Test
    public void TesteBanco() throws Exception {
        CriaBanco banco = new CriaBanco();
        final SQLiteDatabase testeBanco = SQLiteDatabase.openOrCreateDatabase("Teste",null,null);

        boolean retorno = banco.criaBanco(testeBanco);

        assertTrue(retorno);

    }

    @Test
    public void TesteDownloadImagem() throws Exception{

        Download down = new Download();

        Bitmap imagemPbaixar = down.getBitmapFromURL("http://image.tmdb.org/t/p/w185//n8V09dDc02KsSN6Q4hC2BX6hN8X.jpg");

        assertNull(imagemPbaixar);

    }

    @Test
    public void TesteUrl(){

        ConexaoUrls url = new ConexaoUrls();

        String Resultado = url.chamadaGet("https://swapi.co/api/people/1");

        assertNotNull("Pegou String",Resultado);

    }
}