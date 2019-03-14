package mx.gob.cdmx.ssc.mpt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyfishjy.library.RippleBackground;

import java.util.List;
import java.util.regex.Pattern;


import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    String url ="https://mipolicia.ssp.cdmx.gob.mx/mpt/rest/publico/transporte/alertas/";
    //String TOKEN= "5b76f0d15e0c0a7fb00d09744e9951243a534dec";
    String TOKEN= "";
    RippleBackground rippleBackground;
    ImageView imgViewTruck;
    Context context;

    LocationManager locationManager;
    RequestQueue queue;

    double longitudeNetwork, latitudeNetwork;

    private static final int RC_CAMERA_AND_LOCATION = 1;

    SharedPreferences prefs;

    Toolbar toolbar;
    ActionBar actionBar;

    int kont = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name));


        prefs = getSharedPreferences("PreferenciasMPT", Context.MODE_PRIVATE);

        TOKEN = prefs.getString("token", "Default");

//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("token", TOKEN);
//        editor.commit();


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        queue = Volley.newRequestQueue(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        rippleBackground = (RippleBackground) findViewById(R.id.content);
        imgViewTruck = (ImageView) findViewById(R.id.truck);

        imgViewTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.removeUpdates(locationListenerNetwork);
                rippleBackground.stopRippleAnimation();
                kont = 0;
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

            methodRequiresTwoPermission();
            return;
        }

        if(TOKEN.equals("Default")){


            android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(context);
            alertDialog2.setTitle("Configuraciones");
            alertDialog2.setMessage("Debes de tener un token para poder usar esta aplicación correctamente");
            alertDialog2.setIcon(R.mipmap.ic_launcher);
            alertDialog2.setPositiveButton("ACEPTAR",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            alertDialog2.show();

            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1 * 10*1000, 0, locationListenerNetwork);

        //Toast.makeText(this, "Network provider started running TOKEN: "+TOKEN, Toast.LENGTH_LONG).show();
        rippleBackground.startRippleAnimation();
        if(kont<1){
            sendEmergency(TOKEN,"01");
        }


    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //Toast.makeText(MainActivity.this, "Network Provider update Lat: "+longitudeNetwork+ "   Lng: "+latitudeNetwork, Toast.LENGTH_SHORT).show();

                    String[] separatedLat = String.valueOf(latitudeNetwork).split(Pattern.quote("."));
                    String[] separatedLng = String.valueOf(longitudeNetwork).split(Pattern.quote("."));



                    if(separatedLat[1].length() >= 6 && separatedLng[1].length() >= 6){

                        String lat = separatedLat[1].substring(0,6);
                        String lng = separatedLng[1].substring(0,6);
                        String data = lat+lng;

                        Toast.makeText(MainActivity.this, " Data: "+data, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(MainActivity.this, " Data: "+data + "  lat: "+separatedLat[1].length()+"   lng: "+separatedLat[1].length(), Toast.LENGTH_SHORT).show();
                        sendEmergency(TOKEN,data);
                    }else{
                        Toast.makeText(MainActivity.this, " No cumple la longitud de data lat: "+separatedLat[0].length()+"   lng: "+separatedLat[1].length(), Toast.LENGTH_SHORT).show();
                    }









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

        getLocation();

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
            getLocation();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Debe otorgar permisos de Ubicación para poder utilizar esta aplicación",
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }


    public void sendEmergency(String token, String data) {

        kont++;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url+token+"/"+data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());
                        Toast.makeText(context,response.toString()+ "  "+kont , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        //Toast.makeText(context,error.toString() , Toast.LENGTH_LONG).show();
                        Toast.makeText(context,"Error al enviar", Toast.LENGTH_LONG).show();
                    }
                }
        );

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 15000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 15000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        // add it to the RequestQueue
        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.menu_setting:
                startActivity(new Intent(context, SettingsActivity.class));
                break;

            default:
                break;
        }
        return true;
    }


}
