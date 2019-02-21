package mx.gob.cdmx.ssc.mpt.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import mx.gob.cdmx.ssc.mpt.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class FragmentScan extends Fragment implements QRCodeView.Delegate  {

    private static final String TAG = FragmentScan.class.getSimpleName();

    ZBarView mZBarView;
    Toolbar toolbar;
    ActionBar actionBar;
    Context mContext;
    SharedPreferences prefs;
    String TOKEN;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_scan, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        mContext = container.getContext();

        ////ORIENTATION
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        prefs = mContext.getSharedPreferences("PreferenciasAdminPer", Context.MODE_PRIVATE);
        TOKEN=prefs.getString("token", "Default");

        activity.getSupportActionBar().setTitle("Escaner QR");

        mZBarView =(ZBarView)view.findViewById(R.id.zbarview);
        mZBarView.setDelegate(this);
        //mZBarView.startCamera();
        //mZBarView.changeToScanQRCodeStyle();
       // mZBarView.setType(BarcodeType.ALL, null);
        //mZBarView.startSpotAndShowRect();

        // Inflate the layout for this fragment
        return view;
    }




    @Override
    public void onStart() {
        super.onStart();
        mZBarView.startCamera();
        mZBarView.startSpotAndShowRect();
    }

    @Override
    public void onStop() {
        mZBarView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mZBarView.onDestroy();
        super.onDestroy();
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        //Toast.makeText( getActivity(), "result: "+result, Toast.LENGTH_SHORT).show();
        mZBarView.stopCamera();
        vibrate();

        if(!result.equals("")){
            initInfoScan(result.trim());
        }


    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "error");
    }

    private void initInfoScan(String result){


        Bundle bundle = new Bundle();
        bundle.putString("result",result); // Put anything what you want

        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        FragmentInfoScan fragmentInfoScan= new FragmentInfoScan();
        fragmentInfoScan.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment, fragmentInfoScan);
        fragmentTransaction.commit();
    }

}