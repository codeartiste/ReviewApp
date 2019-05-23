package com.sandippal.reviewapp;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import java.util.List;


public class ListFetcher extends AsyncTask<CmdData, String, String> {

    MainActivity mact ;
    ReviewPresenter mPres;
    List<ReviewStringObj> resl = null;
    ListFetcher(Context c, ReviewPresenter pres){
        mPres = pres;
        mact = (MainActivity)c ;
    }

    @Override
    protected String doInBackground(CmdData... params) {
        publishProgress("Fetching..."); // Calls onProgressUpdate()

        String resp = "";
        resl = mPres.getData(params[0].pageNo);

        return "Resp= " + "\n"+ resp;

    }


    @Override
    protected void onPostExecute(String result) {


        mact.flag_loading = false;
        mact.mListView.removeFooterView(mact.mProgressBarFooter);
        if(resl != null) {
            //update view
            mact.dAdapter.addAll(resl);
            mact.dAdapter.notifyDataSetChanged();
            // now use presenter again to fetch images
            // this will fetch all 20 images, but we will do the single image fetch now
            //mact.mPresenter.backGroundFetchImages();
        }else{

            mact.mListView.addHeaderView(mact.mNoInternetHeader);

        }



    }


    @Override
    protected void onPreExecute() {
        mact.mListView.addFooterView(mact.mProgressBarFooter);
    }


    @Override
    protected void onProgressUpdate(String... text) {

        // we will not update anything yet
    }
}
