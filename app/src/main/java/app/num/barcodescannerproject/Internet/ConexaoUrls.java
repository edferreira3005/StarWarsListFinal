package app.num.barcodescannerproject.Internet;

import android.os.StrictMode;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;


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


        HttpClient httpclient = new DefaultHttpClient(httpParameters);

        HttpGet chamadaget = new HttpGet(url);

        String retorno = "";

        try {


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);


            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(chamadaget,
                    responseHandler);



            retorno = responseBody;

            return retorno;


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            retorno = "";
            return retorno;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            retorno = "";
            return retorno;
        } catch (Throwable t) {
            retorno = "";
            return retorno;
        }


    }
}
