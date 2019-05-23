package com.sandippal.reviewapp;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
* Class to handle the Adapter List
*
 */

public class DynamicListAdapter extends ArrayAdapter<ReviewStringObj> {
    private final Activity context;


    static class ViewHolder {
        public TextView display_title;
        public TextView headline;
        public TextView summary_short;
        public TextView bylineDateRating;
        public ImageView image;
    }

    public DynamicListAdapter(Activity context) {
        super(context, R.layout.listview_activity);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        Log.i(ReviewConstants.TAG, "position = " + position );
        Log.i(ReviewConstants.TAG, "Get data count = " + this.getCount());

        // reuse views, but create it first ... time
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listview_activity, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.display_title = (TextView) rowView.findViewById(R.id.listview_item_display_title);
            viewHolder.headline = (TextView) rowView.findViewById(R.id.listview_item_headline);
            viewHolder.summary_short = (TextView) rowView.findViewById(R.id.listview_item_summary_short);
            viewHolder.bylineDateRating = (TextView) rowView.findViewById(R.id.listview_item_bylineDateRating);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.listview_image);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        ReviewStringObj s = getItem(position);
        holder.display_title.setText(s.display_title);
        holder.summary_short.setText(s.summary_short);
        holder.headline.setText(s.headline.trim());
        String authorDateRating = s.byline + " | " + s.publication_date ;;
        if(s.mpaa_rating.length() > 0) {
            authorDateRating += " | " + s.mpaa_rating;
        }
        holder.bylineDateRating.setText(authorDateRating);
        MainActivity mact = (MainActivity)context ;
        Bitmap bmp = mact.mPresenter.getBitmapData(s.multimedia_src) ;
        if( bmp != null){
            holder.image.setImageBitmap(bmp);
        }
        else{
            holder.image.setImageResource(R.drawable.ic_launcher_foreground);
            mact.mPresenter.backGroundFetchOneImage(s.multimedia_src, holder.image);
        }
        return rowView;
    }
}