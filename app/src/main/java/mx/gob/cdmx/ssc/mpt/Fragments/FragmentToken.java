package mx.gob.cdmx.ssc.mpt.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.gob.cdmx.ssc.mpt.MainActivity;
import mx.gob.cdmx.ssc.mpt.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class FragmentToken extends Fragment implements EasyPermissions.PermissionCallbacks {

    Toolbar toolbar;
    ActionBar actionBar;
    Context mContext;
    SharedPreferences prefs;

    String TOKEN;

    TextView tvToken;

    Button btnScan;


    private static final int RC_CAMERA = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_token, container, false);
        mContext = container.getContext();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);



        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("MPT");

        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        prefs = mContext.getSharedPreferences("PreferenciasMPT", Context.MODE_PRIVATE);
        TOKEN=prefs.getString("token", "Default");


        tvToken = (TextView)view.findViewById(R.id.token);
        btnScan = (Button)view.findViewById(R.id.btnScan);

        tvToken.setText(TOKEN);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {

                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        methodRequiresPermission();
                    }
                    return;
                }

                initScan();
            }
        });



        return view;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @AfterPermissionGranted(RC_CAMERA)
    private void methodRequiresPermission() {
        //Manifest.permission.CAMERA,
        String[] perms = { Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(mContext, perms)) {

            initScan();

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Debe otorgar permisos de la Camara para poder utilizar este apartado",
                    RC_CAMERA, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        initScan();

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        String[] per = { Manifest.permission.CAMERA};
        EasyPermissions.requestPermissions(this, "Debe otorgar permisos de la Camara para poder utilizar este apartado",
                RC_CAMERA, per);

    }

    private void initScan(){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        FragmentScan fragmentScan= new FragmentScan();
        fragmentTransaction.replace(R.id.fragment, fragmentScan);
        fragmentTransaction.commit();
    }




  //  @Override
//    public void onResume() {
//
//        super.onResume();
//
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
//
//                    // handle back button
//
//                    Intent intent = new Intent(mContext, MainActivity.class);
//
//                    startActivity(intent);
//
//                    return true;
//
//                }
//
//                return false;
//            }
//        });
//    }

}
