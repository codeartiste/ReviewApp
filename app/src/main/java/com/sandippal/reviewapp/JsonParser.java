package com.sandippal.reviewapp;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class JsonParser {


    List<ReviewStringObj>  parseResponse(InputStream resp){

        long num_results = 0;
        boolean has_more = false;
        List<ReviewStringObj> results;
        results = null;
        try {
            InputStreamReader responseBodyReader =
                    new InputStreamReader(resp, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys

                String name = jsonReader.nextName();
                if (name.equals(ReviewConstants.NUM_RESULTS)) {
                    num_results = jsonReader.nextLong();
                } else if (name.equals(ReviewConstants.HAS_MORE)) {
                    has_more = jsonReader.nextBoolean();
                }else if (name.equals(ReviewConstants.RESULTS)) {
                    results = readMessagesArray(jsonReader);

                 } else {
                    jsonReader.skipValue();
                }

            }
            jsonReader.endObject();

            Log.i(ReviewConstants.TAG , "Num Results=  " + num_results);
            Log.i(ReviewConstants.TAG , "has_more=  " + has_more);

        }catch(Exception e){

            e.printStackTrace();
        }

        return results;
    }

    public List<ReviewStringObj> readMessagesArray(JsonReader reader) throws IOException {
        List<ReviewStringObj> messages = new LinkedList<ReviewStringObj>() ;

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public ReviewStringObj readMessage(JsonReader reader) throws IOException {
        ReviewStringObj pckt = new ReviewStringObj();


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(ReviewConstants.DISPLAY_TITLE)) {
                pckt.display_title = reader.nextString();
            } else if (name.equals(ReviewConstants.MPAA_RATING)) {
                pckt.mpaa_rating = reader.nextString();
            } else if (name.equals(ReviewConstants.BYLINE)) {
                pckt.byline = reader.nextString();
            } else if (name.equals(ReviewConstants.HEADLINE)) {
                String interm = reader.nextString();
                String [] out = interm.split(ReviewConstants.REVIEW_COLON);
                if(out.length >=2 ) {
                    pckt.headline = out[1];
                }else{
                    pckt.headline = interm;
                }

            }else if (name.equals(ReviewConstants.SUMMARY_SHORT)) {
                pckt.summary_short = reader.nextString();
            }else if (name.equals(ReviewConstants.PUBLICATION_DATE)) {
                pckt.publication_date = reader.nextString();
            }
            else if (name.equals(ReviewConstants.MULTIMEDIA)) {

                reader.beginObject();
                while (reader.hasNext()) {
                    name = reader.nextName();

                    if(name.equals(ReviewConstants.SRC)){
                        pckt.multimedia_src = reader.nextString();
                    }
                    else {
                        reader.skipValue();
                    }

                }
                reader.endObject();

            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return pckt;
    }




}
