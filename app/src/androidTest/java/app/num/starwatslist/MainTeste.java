package app.num.starwatslist;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

import app.num.barcodescannerproject.R;
import app.num.starwarslist.Telas.MainActivity;

/**
 * Created by edson.ferreira on 10/11/2017.
 */

public class MainTeste  extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity main;
    private Button abrirLeitorQRCode;


    public MainTeste() {

        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        main = getActivity();
        abrirLeitorQRCode = (Button) main.findViewById(R.id.button);

    }

    @UiThreadTest
    public void testeBotaoQRCode() {
        abrirLeitorQRCode.performClick();
        assertNotNull("Botão Funcionando",abrirLeitorQRCode.getText());
    }

}
