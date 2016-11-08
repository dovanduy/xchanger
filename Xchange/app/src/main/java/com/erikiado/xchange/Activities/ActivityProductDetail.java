package com.erikiado.xchange.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikiado.xchange.R;

import java.io.InputStream;

/**
 * Created by erikiado on 11/7/16.
 */

public class ActivityProductDetail extends AppCompatActivity implements View.OnClickListener  {
    private final String[] article={
            "https://images-na.ssl-images-amazon.com/images/I/51OwBkohn2L._SX425_.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51fXqvIoQBL._SX425_.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51kUL%2Bt6G5L._SX425_.jpg",
            "https://images-na.ssl-images-amazon.com/images/I/51OwBkohn2L._SX425_.jpg"
    };

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ImageButton slider_next=(ImageButton)findViewById(R.id.next);
        ImageButton slider_prev=(ImageButton)findViewById(R.id.prev);
        ImageButton im_edit=(ImageButton)findViewById(R.id.edit);
        ImageButton im_cancel=(ImageButton)findViewById(R.id.cancel);
        TextView descr=(TextView)findViewById(R.id.description);
        //ImageView photos=(ImageView)findViewById(R.id.imageView);
        slider_next.setOnClickListener(this);
        slider_prev.setOnClickListener(this);
        im_edit.setOnClickListener(this);
        im_cancel.setOnClickListener(this);
        new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(article[i]);
        String descript=" Steel mountain frame geometry for easy riding\n" +
                "3-piece mountain crank offers wide gear range; Linear pull brakes provide great stopping power\n" +
                "SRAM drive twist shifters change gears smoothly\n" +
                "26\" Wheels; Alloy rims provide control and durability\n" +
                "Suspension fork smoothes bumps and increases control";
        descr.setText(descript);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next) {
            if (i < article.length-1) {
                i++;
            } else {
                i = 0;
            }
            Log.i("INFOOOOO","VALUE I= "+i);
        } else if (v.getId() == R.id.prev) {
            if (i == 0) {
                i = article.length - 1;
            } else {
                i--;
            }
        }
        Log.i("INFOOOOO","VALUE I= "+i);
        new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(article[i]);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
