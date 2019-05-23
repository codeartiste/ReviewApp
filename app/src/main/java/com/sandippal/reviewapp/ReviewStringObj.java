package com.sandippal.reviewapp;

public class ReviewStringObj {

    String display_title = "";
    String mpaa_rating = "";
    String byline = "";
    String headline = "";
    String multimedia_src = "";
    String summary_short = "";
    String publication_date = "";

    public void print(){
        System.out.println("HeadLine : "
                + headline  + "...  byline = "
                + byline );
        if(multimedia_src != null) {
            System.out.println("Image Url = " + multimedia_src);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewStringObj myo = (ReviewStringObj) o;
        return display_title.equals(myo.display_title) &&
                byline.equals(myo.byline) &&
                headline.equals(myo.headline) &&
                publication_date.equals(myo.publication_date);
    }

}
