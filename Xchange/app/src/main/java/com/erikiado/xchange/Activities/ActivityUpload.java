package com.erikiado.xchange.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.erikiado.xchange.Product;
import com.erikiado.xchange.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ActivityUpload extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabaseReference;
    private Context context;
    private ImageView productImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        productImage = ((ImageView)findViewById(R.id.product_image));

        Picasso.with(context)
                .load("http://www.bicicletasenrique.com.ar/productos/254/dinamico/0132--BICICLETA-PUBLICA-222c.jpg")
                .resize(50, 50)
                .centerCrop()
                .into(productImage);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference products = mFirebaseDatabaseReference.child("products");
                String key = mFirebaseDatabaseReference.child("products").push().getKey();
                writeNewProduct(key,"Bicicleta",500);
                Snackbar.make(view, "Product Added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    private void writeNewProduct(String productId, String name, int price) {
        Product product = new Product(name, price);
        mFirebaseDatabaseReference.child("products").child(productId).setValue(product);
    }

}
