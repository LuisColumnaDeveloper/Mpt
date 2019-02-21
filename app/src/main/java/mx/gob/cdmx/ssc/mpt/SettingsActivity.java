package mx.gob.cdmx.ssc.mpt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import mx.gob.cdmx.ssc.mpt.Fragments.BaseFragment;
import mx.gob.cdmx.ssc.mpt.Fragments.FragmentInfoScan;
import mx.gob.cdmx.ssc.mpt.Fragments.FragmentToken;

public class SettingsActivity extends AppCompatActivity {


    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        prefs = getSharedPreferences("PreferenciasMPT", Context.MODE_PRIVATE);

        if(savedInstanceState == null) {
            setFragment(0);
        }
    }

    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentToken fragmentDashboard = new FragmentToken();
                fragmentTransaction.replace(R.id.fragment, fragmentDashboard);
                fragmentTransaction.commit();
                break;

        }
    }


}
