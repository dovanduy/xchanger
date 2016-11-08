package com.erikiado.xchange;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikiado.xchange.Activities.ActivityProductDetail;
import com.erikiado.xchange.Activities.ActivityUpload;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by erikiado on 11/7/16.
 */

public class FragmentSeller extends Fragment {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private View baseView;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;

    private Button addProduct,detailProduct;
    private ImageView productImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = this.getActivity().getBaseContext();

        baseView = inflater.inflate(R.layout.fragment_seller, container, false);

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.deal_preferences), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        addProduct = ((Button) baseView.findViewById(R.id.seller_add_product));
        detailProduct = ((Button) baseView.findViewById(R.id.seller_product_detail));
        productImage = ((ImageView) baseView.findViewById(R.id.product_image));

//        Picasso.with(context)
//                .load("http://www.bicicletasenrique.com.ar/productos/254/dinamico/0132--BICICLETA-PUBLICA-222c.jpg")
//                .resize(50, 50)
//                .centerCrop()
//                .into(productImage);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(context,ActivityUpload.class);
                startActivity(intent);
            }
        });

        detailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(context,ActivityProductDetail.class);
                startActivity(intent);
            }
        });

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
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference messages = mFirebaseDatabaseReference.child("messages");
            messages.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((TextView)getActivity().findViewById(R.id.text)).setText(dataSnapshot.getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

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


        } else {
            Snackbar.make(baseView, "No hay red", Snackbar.LENGTH_LONG).show();
        }


        return baseView;


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