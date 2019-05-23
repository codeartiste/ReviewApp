package com.sandippal.reviewapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ReviewModelData {

    private SimpleListCache reviewCache;
    private LruCache<String , Bitmap> imgCache;

    public ReviewModelData(){

        reviewCache = new SimpleListCache(ReviewConstants.LIST_CACHE_SIZE + 1);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/5th of the available memory
        final int cacheSize = maxMemory / 5 ;

        //Debug statements
        System.out.println("***********");
        System.out.println("Max memory and cacheSize  " + maxMemory + "    " + cacheSize);
        System.out.println("***********");


        imgCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    public List<ReviewStringObj> getData(int pageNo){

        List<ReviewStringObj> resList = new LinkedList<ReviewStringObj>();
        int sz = reviewCache.getSize();

        System.out.println("getData Page No =  " + pageNo);
        if(sz >= (pageNo + 1)*ReviewConstants.PAGE_SIZE){
            resList.addAll(reviewCache.getResl().subList(pageNo*ReviewConstants.PAGE_SIZE , (pageNo + 1)*ReviewConstants.PAGE_SIZE ));
        }
        else{
            //rebuild the  page of the cache and the other previous pages

            String resp;
            HttpRequestResponse hReq = new HttpRequestResponse(ReviewConstants.REVIEW_DATA_URL);
            hReq.setMethod(HttpRequestResponse.methods.GET);

            try {
                int startPgNo = sz / ReviewConstants.PAGE_SIZE;
                for (int i = startPgNo; i <= pageNo; i++) {
                    resp = hReq.buildRestRequest(i * ReviewConstants.PAGE_SIZE);
                    Log.i(ReviewConstants.TAG, "After Build request: " + resp);

                    reviewCache.insertAtEnd(hReq.getJsonResponse());
                    Log.i(ReviewConstants.TAG, "Current Cache Size : " + reviewCache.getSize());
                }
                resList.addAll(reviewCache.getResl().subList((pageNo) * ReviewConstants.PAGE_SIZE, (pageNo + 1) * ReviewConstants.PAGE_SIZE));
            }catch(Exception e){
                e.printStackTrace();
                return null;

            }

        }

        System.out.println("Img cache size ===== " + imgCache.size());
        System.out.println("After Cache List and Image update. Size = " + reviewCache.getSize());
        reviewCache.debugPrintCache();


        return resList;
    }


    public List<ReviewStringObj> refreshData() {

        String resp;
        int ret = 0;
        int pageNo = 0 ;
        try {
            do {
                HttpRequestResponse hReq = new HttpRequestResponse(ReviewConstants.REVIEW_DATA_URL);
                hReq.setMethod(HttpRequestResponse.methods.GET);
                resp = hReq.buildRestRequest(pageNo);
                Log.i(ReviewConstants.TAG, "refreshData: After Connection established: " + resp);

                ret = reviewCache.checkAndInsertFreshList(hReq.getJsonResponse());
                Log.i(ReviewConstants.TAG, "Current Cache Size : " + reviewCache.getSize());
                pageNo++;
            } while (ret == 0);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return getData(0);
    }

    public boolean populateImageCache(){


        ListIterator<ReviewStringObj>
                It = reviewCache.getResl().listIterator();

        while (It.hasNext()) {

            ReviewStringObj obj1 = It.next();
            try {
                if (getBitmapFromMemCache(obj1.multimedia_src) == null) {
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(obj1.multimedia_src).getContent());
                    System.out.println("***** Now fetching Image = " + obj1.multimedia_src );
                    System.out.println("*****  Image Width  Height = " + bitmap.getWidth() + "  " + bitmap.getHeight() + "  size = " + bitmap.getByteCount() );
                    if( bitmap != null) addBitmapToMemoryCache(obj1.multimedia_src, bitmap);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Img cache size ===== " + imgCache.size());

        return true;
    }

    public Bitmap populateOneImage( String src) {

        Bitmap bitmap = null ;
        try {
            if (getBitmapFromMemCache(src) == null) {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(src).getContent());
                System.out.println("***** Now fetching Image = " + src );
                if( bitmap != null) addBitmapToMemoryCache(src, bitmap);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imgCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return imgCache.get(key);
    }


}
