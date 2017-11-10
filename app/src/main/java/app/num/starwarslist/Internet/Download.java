package app.num.starwarslist.Internet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by edson.ferreira on 07/11/2017.
 * Classe que realiza o download das imagens(Posters) da MOVIE DB API
 */

public class Download {

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new java.net.URL(src);

            HttpURLConnection conexao = (HttpURLConnection) url
                    .openConnection();

            conexao.setDoInput(true);

            conexao.connect();

            InputStream input = conexao.getInputStream();

            Bitmap Imagem = BitmapFactory.decodeStream(input);

            return Imagem;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
