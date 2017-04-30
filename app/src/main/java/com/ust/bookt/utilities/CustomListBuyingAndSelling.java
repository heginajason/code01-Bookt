package com.ust.bookt.utilities;

import android.app.Activity;
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
 * Created by Jace on 3/19/2017.
 */

public class CustomListBuyingAndSelling extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> bookTitle;
    private final ArrayList<String> bookAuthor;
    private final ArrayList<String> bookLocation;
    private final ArrayList<String> priceRangeStart;
    private final ArrayList<String> priceRangeEnd;
    private final ArrayList<String> bookStatus;
    private final ArrayList<String> bookPostStatus;
    private final ArrayList<String> bookImgUrl;

    public CustomListBuyingAndSelling(Activity context, ArrayList<String> bookTitle, ArrayList<String> bookAuthor,
                             ArrayList<String> priceRangeStart,  ArrayList<String> bookLocation,
                                      ArrayList<String> priceRangeEnd, ArrayList<String> bookStatus,
                                      ArrayList<String> bookPostStatus, ArrayList<String> bookImgUrl) {

        super(context, R.layout.template_buyingselling, bookTitle);
        this.context = context;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookLocation = bookLocation;
        this.priceRangeStart = priceRangeStart;
        this.priceRangeEnd = priceRangeEnd;
        this.bookStatus =  bookStatus;
        this.bookPostStatus = bookPostStatus;
        this.bookImgUrl = bookImgUrl;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.template_buyingselling, null, true);

        TextView txtBookTitle = (TextView) rowView.findViewById(R.id.textView_bookTitleBuyingSelling);
        TextView txtBookAuthor = (TextView) rowView.findViewById(R.id.textView_bookAuthorBuyingSelling);
        TextView txtLocation = (TextView) rowView.findViewById(R.id.textView_locationBuyingSelling);
        TextView txtPriceRange = (TextView) rowView.findViewById(R.id.textView_priceRangeBuyingSelling);
        TextView txtBuyingSellingStatus = (TextView) rowView.findViewById(R.id.textView_bookTitleBuyingSellingStatus);


        txtBookTitle.setText(bookTitle.get(position));
        txtBookAuthor.setText(bookAuthor.get(position));
        txtLocation.setText(bookLocation.get(position));


        if(bookPostStatus.get(position).equalsIgnoreCase("Selling")){
            txtPriceRange.setText("Selling: Php " + priceRangeStart.get(position));
        } else if(bookPostStatus.get(position).equalsIgnoreCase("Buying")){
            txtPriceRange.setText("Buying: Php " + priceRangeStart.get(position) + " - " + priceRangeEnd.get(position));
        }

        txtBuyingSellingStatus.setText(bookStatus.get(position));

        Picasso.with(getContext()).load(bookImgUrl.get(position)).fit().centerCrop().into((ImageView) rowView.findViewById(R.id.imageViewBuyingSelling));

        return rowView;
    }

}
