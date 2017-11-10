package app.num.starwarslist.Internet;

import android.os.Build;

import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by edson.ferreira on 06/11/2017.
 */
@SuppressWarnings("deprecation")
public class ConexaoUrls {

    private static final long CONN_MGR_TIMEOUT = 30000;
    private static final int CONN_TIMEOUT = 30000;
    private static final int SO_TIMEOUT = 30000;


    public String chamadaGet(String url) {

        HttpParams Parametros_Conect = new BasicHttpParams();

        ConnManagerParams.setTimeout(Parametros_Conect, CONN_MGR_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(Parametros_Conect, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(Parametros_Conect, SO_TIMEOUT);

        HttpURLConnection conexao;
        String retorno = "";

        try {
            //Pegando String da Url e transformando em URL para conexao
            URL Transform_Url = new URL(url);

            //Conectando no site desejado e capturando informações
            conexao = (HttpURLConnection) Transform_Url.openConnection();
            conexao.setRequestProperty("User-Agent", "swapi-android-" + Build.VERSION.RELEASE);

            int responseCode = conexao.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){

                //Transformando o retorno em uma String para salvar o arquivo
                retorno = TraduzindoRetorno(conexao.getInputStream());



            }
            return retorno;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return retorno;
        } catch (IOException e) {
            e.printStackTrace();
            return retorno;
        }catch (Exception e){
            e.printStackTrace();
            return retorno;
        }


    }

    //Processo que tranduz o que vem da URL
    private String TraduzindoRetorno(InputStream in) {
        BufferedReader Ler = null;
        StringBuffer Traducao = new StringBuffer();
        try {
            Ler = new BufferedReader(new InputStreamReader(in));
            String Linha = "";

            while ((Linha = Ler.readLine()) != null) {

                Traducao.append(Linha);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Ler != null) {
                try {
                    Ler.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return Traducao.toString();
    }
}
