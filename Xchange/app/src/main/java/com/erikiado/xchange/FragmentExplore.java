package com.erikiado.xchange;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.google.api.client.util.Value;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentExplore extends Fragment {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private View baseView;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private DatabaseReference products;
    private ValueEventListener productListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        baseView = inflater.inflate(R.layout.fragment_explore, container, false);
        context = this.getActivity().getBaseContext();

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.deal_preferences), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


//        buttCalif = ((Button) view.findViewById(R.id.settings_calificacion));


        return baseView;


    }

    @Override
    public void onResume(){
        super.onResume();

        if (isOnline()) {
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {
                    Snackbar.make(baseView,account.getPhoneNumber().toString(),Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError(AccountKitError accountKitError) {

                }
            });


            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if (mFirebaseUser == null) {
                // Not signed in, launch the Sign In activity
//            startActivity(new Intent(this, ActivityLogin.class));
//            finish();
//            return;
                Snackbar.make(baseView, "No hay USUARIO", Snackbar.LENGTH_LONG).show();

            } else {
                String mUsername = mFirebaseUser.getDisplayName();
                if(mUsername != null){
                    Snackbar.make(baseView, mUsername, Snackbar.LENGTH_LONG).show();
                }
                Snackbar.make(baseView, mFirebaseUser.getUid(), Snackbar.LENGTH_LONG).show();

                if (mFirebaseUser.getPhotoUrl() != null) {
//                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
                }
            }

            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            products = mFirebaseDatabaseReference.child("products");
            productListener = products.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((TextView)getActivity().findViewById(R.id.text)).setText(dataSnapshot.getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Snackbar.make(baseView, "No hay red", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        products.removeEventListener(productListener);
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