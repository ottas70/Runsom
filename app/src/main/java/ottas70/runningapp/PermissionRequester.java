package ottas70.runningapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by ottovodvarka on 19.12.16.
 */

public class PermissionRequester implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int HAVE_PERMISSION = 1;

    private Context context;

    public PermissionRequester(Context context) {
        this.context = context;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case HAVE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Je možné pokračovat
                } else {
                    Toast toast = Toast.makeText(context, "This permission is necessery for this app", Toast.LENGTH_SHORT);
                    requestPermission(permissions);
                }
        }
    }

    public void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                HAVE_PERMISSION);
    }

}
