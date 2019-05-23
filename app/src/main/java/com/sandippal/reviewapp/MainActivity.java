package com.sandippal.reviewapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sandippal.reviewapp.R;




public class MainActivity extends AppCompatActivity {



    ListView mListView ;
    DynamicListAdapter dAdapter ;
    ReviewPresenter mPresenter;
    boolean flag_loading = false;
    View mProgressBarFooter;
    View mNoInternetHeader;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dAdapter = new DynamicListAdapter(this );
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(dAdapter);
        mProgressBarFooter = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar_footer_header, null, false);

        mNoInternetHeader =  ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.no_internet_header_footer, null, false);

        mPresenter = new ReviewPresenter(MainActivity.this);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(totalItemCount < ReviewConstants.LIST_CACHE_SIZE) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        mPresenter.backGroundFetchItems(totalItemCount / ReviewConstants.PAGE_SIZE);

                    }
                } //Restricting it to LIST_CACHE_SIZE elements
            }

        });




    }




}
