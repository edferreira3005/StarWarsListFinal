package app.num.starwatslist;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import app.num.barcodescannerproject.R;
import app.num.starwarslist.Telas.InfoActivity;
import app.num.starwarslist.Telas.MainActivity;

/**
 * Created by edson.ferreira on 10/11/2017.
 */

public class MainClickListTeste extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity main;
    private ListView listaPersona;


    public MainClickListTeste() {

        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        main = getActivity();
        listaPersona = (ListView) main.findViewById(R.id.lvPersonagens);

    }

    @UiThreadTest
    public void testeBotaoQRCode() {
        listaPersona.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                getActivity().startActivity(new Intent(getActivity(), InfoActivity.class));

            }
        });
        assertNotNull("Abrindo informações",listaPersona.isClickable());
    }

}
