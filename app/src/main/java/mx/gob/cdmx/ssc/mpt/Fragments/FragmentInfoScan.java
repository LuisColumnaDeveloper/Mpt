package mx.gob.cdmx.ssc.mpt.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mx.gob.cdmx.ssc.mpt.R;

public class FragmentInfoScan extends Fragment {

    Toolbar toolbar;
    ActionBar actionBar;
    Context mContext;
    SharedPreferences prefs;
    String TOKEN;
    TextView tvResult;
    Button btnSave, btnBack;

    String result="";

    private static final int RC_CAMERA = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info_scan, container, false);
        mContext = container.getContext();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("MPT");

        prefs = mContext.getSharedPreferences("PreferenciasMPT", Context.MODE_PRIVATE);
        TOKEN = prefs.getString("token", "Default");


        tvResult = (TextView) view.findViewById(R.id.result);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        Bundle bundle =this.getArguments();

        if(bundle != null) {

            result = bundle.getString("result");

            tvResult.setText(result);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(mContext);
                alertDialog2.setTitle("Configuraciones");
                alertDialog2.setMessage("Si ya contabas con un token se remplazara por este, ¿Estas seguro de continuar?");
                alertDialog2.setIcon(R.mipmap.ic_launcher);
                alertDialog2.setPositiveButton("ACEPTAR",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                save(result);
                            }
                        }
                       );

                alertDialog2.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        back();
                    }
                });

                alertDialog2.show();

            }
        });


        return view;
    }

    private void back(){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        FragmentToken fragmentToken= new FragmentToken();
        fragmentTransaction.replace(R.id.fragment, fragmentToken);
        fragmentTransaction.commit();
    }


    private void save(String token){


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.commit();

        back();
    }

}
