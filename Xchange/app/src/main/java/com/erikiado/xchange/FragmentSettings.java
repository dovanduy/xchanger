package com.erikiado.xchange;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by erikiado on 11/7/16.
 */

public class FragmentSettings extends Fragment {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = this.getActivity().getBaseContext();

        final View view = inflater.inflate(R.layout.fragment_explore, container, false);

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.deal_preferences), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


//        buttCalif = ((Button) view.findViewById(R.id.settings_calificacion));


        if (isOnline()) {


        } else {
            Snackbar.make(view, "No hay red", Snackbar.LENGTH_LONG).show();
        }


        return view;


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}