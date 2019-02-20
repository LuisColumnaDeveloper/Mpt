package mx.gob.cdmx.ssc.mpt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    RippleBackground rippleBackground;
    ImageView imgViewTruck;
    Context context;


    LocationManager locationManager;
    double longitudeBest, latitudeBest;
    double longitudeGPS, latitudeGPS;
    double longitudeNetwork, latitudeNetwork;

    private static final int RC_CAMERA_AND_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        rippleBackground = (RippleBackground) findViewById(R.id.content);
        imgViewTruck = (ImageView) findViewById(R.id.truck);

        rippleBackground.startRippleAnimation();

        imgViewTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rippleBackground.stopRippleAnimation();

                locationManager.removeUpdates(locationListenerGPS);

            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            methodRequiresTwoPermission();
        }

        if (!checkLocation())
            return;


        getLocation();


    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1 * 10*1000, 0, locationListenerGPS);

        Toast.makeText(this, "GPS provider started running", Toast.LENGTH_LONG).show();
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(MainActivity.this, "GPS Provider update Lat: "+latitudeGPS+ "   Lng: "+longitudeGPS, Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Habilitar Ubicación")
                .setMessage("Su ubicación esta desactivada.\n por favor active su ubicación " +
                        "para utilizar esta App")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        Toast.makeText(this,"Permisos aceptados" , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //Toast.makeText(this,"Debe otorgar permisos de Ubicación para poder utilizar esta aplicación" , Toast.LENGTH_LONG).show();
        String[] per = { Manifest.permission.ACCESS_FINE_LOCATION};
        EasyPermissions.requestPermissions(this, "Debe otorgar permisos de Ubicación para poder utilizar esta aplicación",
                RC_CAMERA_AND_LOCATION, per);

    }

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        //Manifest.permission.CAMERA,
        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Debe otorgar permisos de Ubicación para poder utilizar esta aplicación",
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }
}
