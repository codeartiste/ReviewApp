package com.sandippal.reviewapp;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;





public class ImageFetcher extends AsyncTask<String, String, String> {

    Context ctx;
    ReviewPresenter mPres;

    ImageFetcher(Context c, ReviewPresenter pres){
        mPres = pres;
        ctx = c;
    }

    @Override
    protected String doInBackground(String... params) {

       mPres.populateImages();
       return " ";
    }


    @Override
    protected void onPostExecute(String result) {

        MainActivity mact = (MainActivity)ctx ;
        mact.dAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onPreExecute() {
        MainActivity mact = (MainActivity)ctx ;
        //mact.flag_loading = true;
    }


    @Override
    protected void onProgressUpdate(String... text) {

    }
}
