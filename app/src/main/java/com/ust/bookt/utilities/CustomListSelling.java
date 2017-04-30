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

public class CustomListSelling extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> bookTitle;
    private final ArrayList<String> bookAuthor;
    private final ArrayList<String> bookLocation;
    private final ArrayList<String> priceRangeStart;
    private final ArrayList<String> bookImgUrl;



    public CustomListSelling(Activity context, ArrayList<String> bookTitle, ArrayList<String> bookAuthor,
                             ArrayList<String> priceRangeStart,  ArrayList<String> bookLocation, ArrayList<String> bookImgUrl) {
        super(context, R.layout.template_selling, bookTitle);
        this.context = context;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookLocation = bookLocation;
        this.priceRangeStart = priceRangeStart;
        this.bookImgUrl = bookImgUrl;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.template_selling, null, true);

        TextView txtBookTitle = (TextView) rowView.findViewById(R.id.textView_bookTitleSelling);
        TextView txtBookAuthor = (TextView) rowView.findViewById(R.id.textView_bookAuthorSelling);
        TextView txtLocation = (TextView) rowView.findViewById(R.id.textView_locationSelling);
        TextView txtPriceRange = (TextView) rowView.findViewById(R.id.textView_priceRangeSelling);

        txtBookTitle.setText(bookTitle.get(position));
        txtBookAuthor.setText(bookAuthor.get(position));
        txtLocation.setText(bookLocation.get(position));
        txtPriceRange.setText("Php " + priceRangeStart.get(position));

        Log.d("url",bookImgUrl.get(position));


        Picasso.with(getContext()).load(bookImgUrl.get(position)).fit().centerCrop().into((ImageView) rowView.findViewById(R.id.imageViewSelling));


        return rowView;
    }
}
