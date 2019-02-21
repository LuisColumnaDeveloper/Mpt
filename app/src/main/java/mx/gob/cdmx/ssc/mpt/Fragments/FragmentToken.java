package mx.gob.cdmx.ssc.mpt.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mx.gob.cdmx.ssc.mpt.R;


public class FragmentToken extends Fragment {

    Toolbar toolbar;
    ActionBar actionBar;
    Context mContext;
    SharedPreferences prefs;

    String TOKEN;

    TextView tvToken;

    Button btnScan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_token, container, false);
        mContext = container.getContext();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("MPT");

        prefs = mContext.getSharedPreferences("PreferenciasMPT", Context.MODE_PRIVATE);
        TOKEN=prefs.getString("token", "Default");


        tvToken = (TextView)view.findViewById(R.id.token);
        btnScan = (Button)view.findViewById(R.id.btnScan);

        tvToken.setText(TOKEN);

        return view;
    }
}
