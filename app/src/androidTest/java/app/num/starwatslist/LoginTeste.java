package app.num.starwatslist;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.TextView;

import app.num.barcodescannerproject.R;
import app.num.starwarslist.Telas.LoginInicioActivity;

public class LoginTeste extends ActivityInstrumentationTestCase2<LoginInicioActivity> {
    private LoginInicioActivity meuLogin;
    private Button login;
    private TextView user;
    public LoginTeste() {
        super(LoginInicioActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        meuLogin = getActivity();
        login = (Button) meuLogin.findViewById(R.id.btnLogin);
        user = (TextView) meuLogin.findViewById(R.id.tvUs);

    }


    @UiThreadTest
    public void testButtonClick() {
        login.performClick();
        assertNotNull("Botão Funcionando",login.getText());
    }
}