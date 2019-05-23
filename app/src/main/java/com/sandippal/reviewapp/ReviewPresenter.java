package com.sandippal.reviewapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import java.util.List;

public class ReviewPresenter {

    private ReviewModelData mModel;
    private Context ctx;

    public ReviewPresenter(Context c) {
        ctx = c;
        mModel = new ReviewModelData();

    }

    public List<ReviewStringObj> getData(int pageNo) {

        List<ReviewStringObj> resl = null;
        if(isNetworkAvailable()) {
            if (mModel != null) {
                resl = mModel.getData(pageNo);
            }
        }
        return resl; // null is network error
    }

    public void refreshData() {

        mModel.refreshData();
    }

    public Bitmap getBitmapData(String key) {
        return mModel.getBitmapFromMemCache(key);

    }

    public boolean populateImages() {

        if(isNetworkAvailable()) {
            return mModel.populateImageCache();
        }
        return false;
    }

    public Bitmap populateOneImage( String src) {

        return mModel.populateOneImage(src);
    }


    public void backGroundFetchItems(int pageNo){

        if(isNetworkAvailable()){
            MainActivity mact =(MainActivity) ctx;
            if(mact.flag_loading == false)
            {
                mact.flag_loading = true;
                ListFetcher runner = new ListFetcher(ctx, this);
                CmdData cmd = new CmdData(pageNo);
                runner.execute(cmd);

            }
        }



    }

    public void backGroundFetchImages(){
        if(isNetworkAvailable()) {
            ImageFetcher runner2 = new ImageFetcher(ctx, this);
            runner2.execute(" ");
        }
    }

    public void backGroundFetchOneImage(String src , ImageView iView){
        if(isNetworkAvailable()) {
            SingleImageFetcher runner2 = new SingleImageFetcher(ctx, iView);
            runner2.execute(src);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                    = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
