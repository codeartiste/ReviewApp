package com.sandippal.reviewapp;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class HttpRequestResponse {

    public enum methods {
        GET, POST
    }

    private HttpURLConnection myConnection;
    private methods m;
    private String murlString;
    public HttpRequestResponse(String uri){

        murlString = uri;
        myConnection = null;
    }

    public void setUrl(String uri){

        murlString = uri;
    }

    public void setMethod(methods m){
        this.m = m;

    }

    public List<ReviewStringObj> getJsonResponse() throws Exception{

        List<ReviewStringObj> resl = null;

        try {
            myConnection.connect();
            int responseCode = myConnection.getResponseCode();
            Log.i(ReviewConstants.TAG, "Resp = " + responseCode);
            if(responseCode == 200){
                InputStream content =  myConnection.getInputStream();
                JsonParser jp = new JsonParser();
                resl = jp.parseResponse(content);
                Log.i(ReviewConstants.TAG, "List size = "+ resl.size());

            }
            else{
                // Handle http error code

            }
            myConnection.disconnect();

        }catch (HttpRetryException e) {
            Log.i(ReviewConstants.TAG, "HttpRetryException in getResponse");
            e.printStackTrace();

            Log.i(ReviewConstants.TAG,  e.getMessage());
            throw e;
        } catch (Exception e) {
            Log.i(ReviewConstants.TAG, "Generic Exception in getResponse");
            e.printStackTrace();
            Log.i(ReviewConstants.TAG,  e.getMessage());
            throw e;
        }


        return resl;
    }

    public String buildRestRequest(int pageno){

        String resp= "";
        try {
            URL movieReview = new URL(murlString + ReviewConstants.OFFSET_STR_QUERY + pageno);

            // Create connection

            myConnection = (HttpURLConnection) movieReview.openConnection();

            myConnection.setRequestProperty("User-Agent", "sandip-rest-app-v0.1");

            myConnection.setRequestMethod("GET");

        } catch (HttpRetryException e) {
            Log.i(ReviewConstants.TAG, "HttpRetryException");
            e.printStackTrace();

            resp = e.getMessage();
        } catch (Exception e) {
            Log.i(ReviewConstants.TAG, "Generic Exception");
            e.printStackTrace();
            resp = e.getMessage();
        }
        return resp;
    }






}
