package com.ust.bookt.utilities;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ust.bookt.R;

import java.util.ArrayList;


/**
 * Created by Jace on 11/10/2016.
 */

public class CustomListBuying extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> bookTitle;
    private final ArrayList<String> bookAuthor;
    private final ArrayList<String> location;
    private final ArrayList<String> priceRangeStart;
    private final ArrayList<String> priceRangeEnd;
    private final ArrayList<String> bookImgUrl;



    public CustomListBuying(Activity context, ArrayList<String> bookTitle, ArrayList<String> bookAuthor,
                            ArrayList<String> location, ArrayList<String> priceRangeStart,
                            ArrayList<String> priceRangeEnd,ArrayList<String> bookImgUrl) {
        super(context, R.layout.template_buying, bookTitle);
        this.context = context;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.location = location;
        this.priceRangeStart = priceRangeStart;
        this.priceRangeEnd = priceRangeEnd;
        this.bookImgUrl = bookImgUrl;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.template_buying, null, true);

        TextView txtBookTitle = (TextView) rowView.findViewById(R.id.textView_booktitle);
        TextView txtBookAuthor = (TextView) rowView.findViewById(R.id.textView_bookAuthor);
        TextView txtLocation = (TextView) rowView.findViewById(R.id.textView_location);
        TextView txtPriceRange = (TextView) rowView.findViewById(R.id.textView_priceRange);

        txtBookTitle.setText(bookTitle.get(position));
        txtBookAuthor.setText(bookAuthor.get(position));
        txtLocation.setText(location.get(position));
        txtPriceRange.setText("Php " + priceRangeStart.get(position) + " - Php " + priceRangeEnd.get(position));

        Log.d("url",bookImgUrl.get(position));

        if(rowView.findViewById(R.id.imageViewBuying) != null){
            Picasso.with(getContext()).load(bookImgUrl.get(position)).fit().centerCrop().into((ImageView) rowView.findViewById(R.id.imageViewBuying));

        }else{
            Log.d("null", "null");
        }


        return rowView;
    }
}
