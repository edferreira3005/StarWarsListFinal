package app.num.starwarslist.Permissoes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by edson.ferreira on 06/11/2017.
 */

public class BuscaPermissoes {

    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE
    };
    int PERMISSION_ALL = 1;

    public void setPermitions(Context context , Activity main){

        if(!hasPermissions(context , PERMISSIONS)){
            ActivityCompat.requestPermissions(main, PERMISSIONS, PERMISSION_ALL);
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
