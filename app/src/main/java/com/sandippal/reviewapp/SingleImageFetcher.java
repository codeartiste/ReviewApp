package com.sandippal.reviewapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;


public class SingleImageFetcher extends AsyncTask<String, String, Bitmap> {

    Context ctx;
    ImageView miView;


    SingleImageFetcher(Context c,  ImageView iView){
        miView = iView;
        ctx = c;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        MainActivity mact = (MainActivity) ctx ;
        return mact.mPresenter.populateOneImage(params[0]);

    }


    @Override
    protected void onPostExecute(Bitmap result) {

        MainActivity mact = (MainActivity)ctx ;
        if(result != null)
            miView.setImageBitmap(result);

    }


    @Override
    protected void onPreExecute() {
        MainActivity mact = (MainActivity)ctx ;

    }


    @Override
    protected void onProgressUpdate(String... text) {

    }
}
