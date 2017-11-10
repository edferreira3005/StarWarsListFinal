package app.num.starwarslist.Internet;

import android.os.Build;
import android.util.Log;

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

        HttpParams httpParameters = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParameters, CONN_MGR_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);

        HttpURLConnection connection;
        String retorno = "";

        try {
            URL url2 = new URL(url);
            connection = (HttpURLConnection) url2.openConnection();
            connection.setRequestProperty("User-Agent", "swapi-android-" + Build.VERSION.RELEASE);

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                retorno = readStream(connection.getInputStream());
                Log.v("CatalogClient", retorno);


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

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
