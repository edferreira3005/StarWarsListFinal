package app.num.starwarslist.Customizacao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.num.barcodescannerproject.R;

/**
 * Created by edson.ferreira on 06/02/2017.
 */

@SuppressWarnings("deprecation")
public class CustomCursorAdapter extends CursorAdapter {

    String NomeBanco = "RelatorioProducao";
    SQLiteDatabase BancoDeDados = null;
    public CustomCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {



        return LayoutInflater.from(context).inflate(R.layout.lista_imagem, parent, false);

    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {


        // Find fields to populate in inflated template
        ImageView image = (ImageView) view.findViewById(R.id.imagem_filme);
        TextView descricao = (TextView) view.findViewById(R.id.Itemname);


        try {
            byte[] decodedString = Base64.decode(cursor.getString(3), Base64.DEFAULT);
            Bitmap myBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            //  Log.i("foto", myBitmap.toString());

            // Populate fields with extracted properties
            if(myBitmap != null) {
                image.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 800, 1200, true));
                descricao.setText(cursor.getString(1));

            }else{
                image.setImageDrawable(view.getResources().getDrawable(R.mipmap.logo_star_wars_app));
                descricao.setText(cursor.getString(1));

            }

            decodedString = null;


        }catch (NullPointerException e){


        }


    }

}