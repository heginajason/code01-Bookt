package com.ust.bookt.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.ust.bookt.bean.BookBean;
import com.ust.bookt.bean.Config;
import com.ust.bookt.utilities.CustomListBuying;
import com.ust.bookt.R;

import java.util.ArrayList;
import java.util.Collections;


public class BuyingFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.buying_layout,null);



        Firebase.setAndroidContext(getActivity());
        final Firebase ref = new Firebase(Config.FIREBASE_BOOK_RETRIEVE);

        ref.addValueEventListener(new ValueEventListener() {
            ListView list;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> bookTitle = new ArrayList<String> ();
                ArrayList<String> bookPriceEnd = new ArrayList<String> ();
                ArrayList<String> bookPriceStart = new ArrayList<String> ();
                ArrayList<String> bookLocation = new ArrayList<String> ();
                ArrayList<String> bookAuthor = new ArrayList<String> ();
                ArrayList<String> bookImgUrl =  new ArrayList<String>();
                final ArrayList<String> bookId = new ArrayList<String>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BookBean book = postSnapshot.getValue(BookBean.class);


                    if(book != null){
                        if (book.getBookPostStatus().equalsIgnoreCase("BUYING") && book.getBookStatus().equalsIgnoreCase("Available")){

                            bookId.add(postSnapshot.getKey());
                            bookTitle.add(book.getBookTitle());
                            bookPriceEnd.add(book.getBookPriceEnd());
                            bookPriceStart.add(book.getBookPriceStart());
                            bookAuthor.add(book.getBookAuthor());
                            bookLocation.add(book.getBookLocation());
                            bookImgUrl.add(book.getBookImgUrl());

                        }
                    }
                }


                list = (ListView) view.findViewById(R.id.listview_buying);
                CustomListBuying adapter = new CustomListBuying(getActivity(), bookTitle, bookAuthor,  bookLocation,
                        bookPriceStart, bookPriceEnd, bookImgUrl);

                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        BookPage bookPage = new BookPage();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "buy");
                        bundle.putString("bookId", bookId.get(position));
                        bookPage.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.containerView, bookPage)
                                .commit();

                    }
                });


                ref.removeEventListener(this);

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Button sortButton = (Button) view.findViewById(R.id.btn_sort);
        sortButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                View mView = inflater.inflate(R.layout.dialogue_sort,null);

                alert.setView(mView);
                final AlertDialog dialog = alert.create();
                dialog.show();

                final RadioButton rb_prices = (RadioButton) mView.findViewById(R.id.rb_prices);
                final RadioButton rb_title = (RadioButton) mView.findViewById(R.id.rb_title);
                final Switch sw_prices = (Switch) mView.findViewById(R.id.sw_prices);
                final Switch sw_title = (Switch) mView.findViewById(R.id.sw_title);
                final Button btn_sort = (Button) mView.findViewById(R.id.btn_sortDd);


                rb_prices.setOnClickListener(new RadioButton.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(rb_prices.isChecked()){
                            sw_title.setEnabled(false);
                            sw_prices.setEnabled(true);
                        }
                    }
                });

                rb_title.setOnClickListener(new RadioButton.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(rb_title.isChecked()){
                            sw_title.setEnabled(true);
                            sw_prices.setEnabled(false);
                        }
                    }
                });

                sw_prices.setOnClickListener(new Switch.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(sw_prices.isChecked()){
                            sw_prices.setText("Descending");
                        }else{
                            sw_prices.setText("Ascending");
                        }
                    }
                });

                sw_title.setOnClickListener(new Switch.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(sw_title.isChecked()){
                            sw_title.setText("Descending");
                        }else{
                            sw_title.setText("Ascending");
                        }
                    }
                });

                btn_sort.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View nview) {
                        Firebase.setAndroidContext(getActivity());
                        final Firebase ref = new Firebase(Config.FIREBASE_BOOK_RETRIEVE);

                        if(rb_title.isChecked() && !sw_title.isChecked()){
                            ref.orderByChild("bookTitle").addValueEventListener(new ValueEventListener() {
                                ListView list;

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<String> bookTitle = new ArrayList<String> ();
                                    ArrayList<String> bookPriceEnd = new ArrayList<String> ();
                                    ArrayList<String> bookPriceStart = new ArrayList<String> ();
                                    ArrayList<String> bookLocation = new ArrayList<String> ();
                                    ArrayList<String> bookAuthor = new ArrayList<String> ();
                                    ArrayList<String> bookImgUrl =  new ArrayList<String>();
                                    final ArrayList<String> bookId = new ArrayList<String>();

                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        BookBean book = postSnapshot.getValue(BookBean.class);

                                        if(book != null){
                                            if (book.getBookPostStatus().equalsIgnoreCase("BUYING")&& book.getBookStatus().equalsIgnoreCase("Available")){
                                                bookId.add(postSnapshot.getKey());
                                                bookTitle.add(book.getBookTitle());
                                                bookPriceEnd.add(book.getBookPriceEnd());
                                                bookPriceStart.add(book.getBookPriceStart());
                                                bookAuthor.add(book.getBookAuthor());
                                                bookLocation.add(book.getBookLocation());
                                                bookImgUrl.add(book.getBookImgUrl());


                                            }
                                        }
                                    }



                                    list = (ListView) view.findViewById(R.id.listview_buying);
                                    CustomListBuying adapter = new CustomListBuying(getActivity(), bookTitle, bookAuthor,  bookLocation,
                                            bookPriceStart, bookPriceEnd,bookImgUrl);

                                    list.setAdapter(adapter);
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {

                                            BookPage bookPage = new BookPage();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("type", "buy");
                                            bundle.putString("bookId", bookId.get(position));
                                            bookPage.setArguments(bundle);
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.containerView, bookPage)
                                                    .commit();

                                        }
                                    });

                                    ref.removeEventListener(this);

                                }
                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }


                            });
                            dialog.dismiss();
                        }else if (rb_prices.isChecked() && !sw_prices.isChecked()){

                            ref.orderByChild("bookPriceStart").addValueEventListener(new ValueEventListener() {
                                ListView list;

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<String> bookTitle = new ArrayList<String> ();
                                    ArrayList<String> bookPriceEnd = new ArrayList<String> ();
                                    ArrayList<String> bookPriceStart = new ArrayList<String> ();
                                    ArrayList<String> bookLocation = new ArrayList<String> ();
                                    ArrayList<String> bookAuthor = new ArrayList<String> ();
                                    ArrayList<String> bookImgUrl =  new ArrayList<String>();
                                    final ArrayList<String> bookId = new ArrayList<String>();

                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        BookBean book = postSnapshot.getValue(BookBean.class);

                                        if(book != null){
                                            if (book.getBookPostStatus().equalsIgnoreCase("BUYING")&& book.getBookStatus().equalsIgnoreCase("Available")){
                                                bookId.add(postSnapshot.getKey());
                                                bookTitle.add(book.getBookTitle());
                                                bookPriceEnd.add(book.getBookPriceEnd());
                                                bookPriceStart.add(book.getBookPriceStart());
                                                bookAuthor.add(book.getBookAuthor());
                                                bookLocation.add(book.getBookLocation());
                                                bookImgUrl.add(book.getBookImgUrl());


                                            }
                                        }
                                    }


                                    list = (ListView) view.findViewById(R.id.listview_buying);
                                    CustomListBuying adapter = new CustomListBuying(getActivity(), bookTitle, bookAuthor,  bookLocation,
                                            bookPriceStart, bookPriceEnd,bookImgUrl);

                                    list.setAdapter(adapter);
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {

                                            BookPage bookPage = new BookPage();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("type", "buy");
                                            bundle.putString("bookId", bookId.get(position));
                                            bookPage.setArguments(bundle);
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.containerView, bookPage)
                                                    .commit();

                                        }
                                    });
                                    ref.removeEventListener(this);

                                }
                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                            dialog.dismiss();
                        } else if(rb_title.isChecked() && sw_title.isChecked()){

                            ref.orderByChild("bookTitle").addValueEventListener(new ValueEventListener() {
                                ListView list;

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<String> bookTitle = new ArrayList<String> ();
                                    ArrayList<String> bookPriceEnd = new ArrayList<String> ();
                                    ArrayList<String> bookPriceStart = new ArrayList<String> ();
                                    ArrayList<String> bookLocation = new ArrayList<String> ();
                                    ArrayList<String> bookAuthor = new ArrayList<String> ();
                                    ArrayList<String> bookImgUrl =  new ArrayList<String>();
                                    final ArrayList<String> bookId = new ArrayList<String>();

                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        BookBean book = postSnapshot.getValue(BookBean.class);

                                        if(book != null){
                                            if (book.getBookPostStatus().equalsIgnoreCase("BUYING")&& book.getBookStatus().equalsIgnoreCase("Available")){
                                                bookId.add(postSnapshot.getKey());
                                                bookTitle.add(book.getBookTitle());
                                                bookPriceEnd.add(book.getBookPriceEnd());
                                                bookPriceStart.add(book.getBookPriceStart());
                                                bookAuthor.add(book.getBookAuthor());
                                                bookLocation.add(book.getBookLocation());
                                                bookImgUrl.add(book.getBookImgUrl());


                                            }
                                        }
                                    }


                                    Collections.reverse(bookId);
                                    Collections.reverse(bookTitle);
                                    Collections.reverse(bookPriceEnd);
                                    Collections.reverse(bookPriceStart);
                                    Collections.reverse(bookLocation);
                                    Collections.reverse(bookAuthor);
                                    Collections.reverse(bookImgUrl);

                                    list = (ListView) view.findViewById(R.id.listview_buying);
                                    CustomListBuying adapter = new CustomListBuying(getActivity(), bookTitle, bookAuthor,  bookLocation,
                                            bookPriceStart, bookPriceEnd, bookImgUrl);

                                    list.setAdapter(adapter);
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {

                                            BookPage bookPage = new BookPage();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("type", "buy");
                                            bundle.putString("bookId", bookId.get(position));
                                            bookPage.setArguments(bundle);
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.containerView, bookPage)
                                                    .commit();

                                        }
                                    });

                                    ref.removeEventListener(this);

                                }
                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }


                            });
                            dialog.dismiss();
                        } else if(rb_prices.isChecked() && sw_title.isChecked()){

                            ref.orderByChild("bookTitle").addValueEventListener(new ValueEventListener() {
                                ListView list;

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<String> bookTitle = new ArrayList<String> ();
                                    ArrayList<String> bookPriceEnd = new ArrayList<String> ();
                                    ArrayList<String> bookPriceStart = new ArrayList<String> ();
                                    ArrayList<String> bookLocation = new ArrayList<String> ();
                                    ArrayList<String> bookAuthor = new ArrayList<String> ();
                                    ArrayList<String> bookImgUrl =  new ArrayList<String>();
                                    final ArrayList<String> bookId = new ArrayList<String>();

                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        BookBean book = postSnapshot.getValue(BookBean.class);

                                        if(book != null){
                                            if (book.getBookPostStatus().equalsIgnoreCase("BUYING")&& book.getBookStatus().equalsIgnoreCase("Available")){
                                                bookId.add(postSnapshot.getKey());
                                                bookTitle.add(book.getBookTitle());
                                                bookPriceEnd.add(book.getBookPriceEnd());
                                                bookPriceStart.add(book.getBookPriceStart());
                                                bookAuthor.add(book.getBookAuthor());
                                                bookLocation.add(book.getBookLocation());
                                                bookImgUrl.add(book.getBookImgUrl());

                                            }
                                        }
                                    }

                                    Collections.reverse(bookId);
                                    Collections.reverse(bookTitle);
                                    Collections.reverse(bookPriceEnd);
                                    Collections.reverse(bookPriceStart);
                                    Collections.reverse(bookLocation);
                                    Collections.reverse(bookAuthor);
                                    Collections.reverse(bookImgUrl);

                                    list = (ListView) view.findViewById(R.id.listview_buying);
                                    CustomListBuying adapter = new CustomListBuying(getActivity(), bookTitle, bookAuthor,  bookLocation,
                                            bookPriceStart, bookPriceEnd, bookImgUrl);

                                    list.setAdapter(adapter);
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {

                                            BookPage bookPage = new BookPage();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("type", "buy");
                                            bundle.putString("bookId", bookId.get(position));
                                            bookPage.setArguments(bundle);
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.containerView, bookPage)
                                                    .commit();

                                        }
                                    });

                                    ref.removeEventListener(this);
                                }
                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                            dialog.dismiss();
                        }
                    }
                });

            }
        });


        Button btn_searchBuying = (Button) view.findViewById(R.id.btn_searchBuying);
        btn_searchBuying.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String et_search = ((EditText)view.findViewById(R.id.searchBuying)).getText().toString();

                if(!et_search.trim().isEmpty()){
                    ref.addValueEventListener(new ValueEventListener() {
                        ListView list;

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> bookTitle = new ArrayList<String> ();
                            ArrayList<String> bookPriceEnd = new ArrayList<String> ();
                            ArrayList<String> bookPriceStart = new ArrayList<String> ();
                            ArrayList<String> bookLocation = new ArrayList<String> ();
                            ArrayList<String> bookAuthor = new ArrayList<String> ();
                            ArrayList<String> bookImgUrl =  new ArrayList<String>();
                            final ArrayList<String> bookId = new ArrayList<String>();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                BookBean book = postSnapshot.getValue(BookBean.class);

                                if(book != null){
                                    if (book.getBookPostStatus().equalsIgnoreCase("BUYING") && book.getBookTitle().toLowerCase().contains(et_search.toLowerCase()) && book.getBookStatus().equalsIgnoreCase("Available")){

                                        bookId.add(postSnapshot.getKey());
                                        bookTitle.add(book.getBookTitle());
                                        bookPriceEnd.add(book.getBookPriceEnd());
                                        bookPriceStart.add(book.getBookPriceStart());
                                        bookAuthor.add(book.getBookAuthor());
                                        bookLocation.add(book.getBookLocation());
                                        bookImgUrl.add(book.getBookImgUrl());

                                    }
                                }
                            }


                            list = (ListView) view.findViewById(R.id.listview_buying);
                            CustomListBuying adapter = new CustomListBuying(getActivity(), bookTitle, bookAuthor,  bookLocation,
                                    bookPriceStart, bookPriceEnd, bookImgUrl);

                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {

                                    BookPage bookPage = new BookPage();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("type", "buy");
                                    bundle.putString("bookId", bookId.get(position));
                                    bookPage.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.containerView, bookPage)
                                            .commit();

                                }
                            });


                            ref.removeEventListener(this);

                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                } else if(et_search.isEmpty()){
                    ref.addValueEventListener(new ValueEventListener() {
                        ListView list;

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> bookTitle = new ArrayList<String> ();
                            ArrayList<String> bookPriceEnd = new ArrayList<String> ();
                            ArrayList<String> bookPriceStart = new ArrayList<String> ();
                            ArrayList<String> bookLocation = new ArrayList<String> ();
                            ArrayList<String> bookAuthor = new ArrayList<String> ();
                            ArrayList<String> bookImgUrl =  new ArrayList<String>();
                            final ArrayList<String> bookId = new ArrayList<String>();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                BookBean book = postSnapshot.getValue(BookBean.class);

                                if(book != null){
                                    if (book.getBookPostStatus().equalsIgnoreCase("BUYING")&& book.getBookStatus().equalsIgnoreCase("Available")){
                                        bookId.add(postSnapshot.getKey());
                                        bookTitle.add(book.getBookTitle());
                                        bookPriceEnd.add(book.getBookPriceEnd());
                                        bookPriceStart.add(book.getBookPriceStart());
                                        bookAuthor.add(book.getBookAuthor());
                                        bookLocation.add(book.getBookLocation());
                                        bookImgUrl.add(book.getBookImgUrl());


                                    }
                                }
                            }


                            list = (ListView) view.findViewById(R.id.listview_buying);
                            CustomListBuying adapter = new CustomListBuying(getActivity(), bookTitle, bookAuthor,  bookLocation,
                                    bookPriceStart, bookPriceEnd, bookImgUrl);

                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {

                                    BookPage bookPage = new BookPage();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("type", "buy");
                                    bundle.putString("bookId", bookId.get(position));
                                    bookPage.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.containerView, bookPage)
                                            .commit();

                                }
                            });


                            ref.removeEventListener(this);

                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });

        return view;
    }

}
