package com.ust.bookt.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.ust.bookt.R;
import com.ust.bookt.activities.MainActivity;
import com.ust.bookt.bean.BookBean;
import com.ust.bookt.bean.Config;
import com.ust.bookt.fragments.BuyingFragment;
import com.ust.bookt.fragments.SellingFragment;
import com.ust.bookt.utilities.CustomListBuying;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.*;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.content.Context.MODE_PRIVATE;
import static com.ust.bookt.bean.Config.FIREBASE_BOOK_RETRIEVE;
import static com.ust.bookt.bean.Config.FIREBASE_USER_RETRIEVE;


public class BookPage extends Fragment {
    private TextView tv_userName;
    private EditText et_bookTitle;
    private EditText et_bookAuthor;
    private EditText et_userContact;
    private EditText et_userLocation;
    private EditText et_priceRangeStart;
    private EditText et_priceRangeEnd;
    private EditText et_bookDescription;
    private ImageButton img_bookImage;
    private String postStatus;


    BookBean book = new BookBean();
    static String emailTo = "";

    public BookPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final  View view = inflater.inflate(R.layout.fragment_book_page, container, false);

        OneSignal.startInit(getContext());

        final Button postButton = (Button)view.findViewById(R.id.bn_type);
        final Button actionButton = (Button)view.findViewById(R.id.bn_action);

        Bundle bundle = this.getArguments();
        final String bookId = bundle.getString("bookId");
        Log.d("bookId", bookId);

        final String type = bundle.getString("type");


        if(type.equals("sell")){
            postButton.setText("Buy");
            actionButton.setVisibility(View.GONE);
        }else if(type.equals("buy")){
            postButton.setText("Reserve");
            actionButton.setVisibility(View.GONE);
        }else if(type.equals("myBooks")){
                actionButton.setText("Delete");
                postButton.setVisibility(View.GONE);
        }else if(type.equals("reservedBooks")){
            actionButton.setText("Cancel Reservation");
            postButton.setVisibility(View.GONE);
        }

        tv_userName = (TextView) view.findViewById(R.id.tv_userName);
        et_bookTitle = (EditText) view.findViewById(R.id.et_bookTitle);
        et_bookAuthor = (EditText) view.findViewById(R.id.et_bookAuthor);
        et_userContact = (EditText)view.findViewById(R.id.et_userContact);
        et_userLocation = (EditText)view.findViewById(R.id.et_userLocation);
        et_priceRangeStart = (EditText)view.findViewById(R.id.et_priceRangeStart);
        et_priceRangeEnd = (EditText)view.findViewById(R.id.et_priceRangeEnd);
        et_bookDescription = (EditText)view.findViewById(R.id.et_bookDescription);
        img_bookImage = (ImageButton)view.findViewById(R.id.user_profile_photo);

//        String uri = "https://firebasestorage.googleapis.com/v0/b/firebase-bookt.appspot.com/o/Photos%2F-KhLfLo4SrwUkLrq9iFK?alt=media&token=40826b54-f69d-4852-b076-ddd461724a0f";


        final Firebase ref = new Firebase(FIREBASE_BOOK_RETRIEVE);
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = "";
                String name = "";
                String author = "";
                String contact = "";
                String location = "";
                String priceStart = "";
                String priceEnd = "";
                String description = "";
                String title = "";

                SharedPreferences preferences = getContext().getSharedPreferences("IDOFUSER",MODE_PRIVATE) ;
                final String email = preferences.getString("email","");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    book = postSnapshot.getValue(BookBean.class);
                    id = postSnapshot.getKey().toString();

                    Log.d("Id", id+" "+bookId);

                    if(id.matches(bookId)){

                        if (book.getBookStatus().equalsIgnoreCase("Reserved") && (type.equals("myBooks"))) {
                            postButton.setVisibility(View.VISIBLE);
                            postButton.setText("Sold");
                        } else if(book.getBookSeller().equals(email) && (type.equals("sell") || type.equals("buy"))){
                            actionButton.setVisibility(View.GONE);
                            postButton.setVisibility(View.GONE);
                        } else if(type.equalsIgnoreCase("myBooks") && book.getBookStatus().equalsIgnoreCase("sold")){
                            actionButton.setVisibility(View.VISIBLE);
                        }

                        name = book.getBookSeller();
                        author = book.getBookAuthor();
                        contact = book.getBookContactNumber();
                        location = book.getBookLocation();
                        priceStart = book.getBookPriceStart();
                        priceEnd = book.getBookPriceEnd();
                        description = book.getBookDesc();
                        title = book.getBookTitle();
                        emailTo = name;

                        Log.d("name", name);
                        Log.d("author", author);
                        Log.d("contact", contact);
                        Log.d("location", location);
                        Log.d("priceStart", priceStart);
                        Log.d("priceEnd", priceEnd);
                        Log.d("desc", description);
                        Log.d("title", title);

                        tv_userName.setText(name);
                        et_bookAuthor.setText(author);
                        et_userContact.setText(contact);
                        et_userLocation.setText(location);
                        et_bookTitle.setText(title);
                        et_priceRangeStart.setText(priceStart);
                        et_priceRangeEnd.setText(priceEnd);
                        et_bookDescription.setText(description);
                        ref.removeEventListener(this);

                        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
                        mStorageReference.child("Photos/" + bookId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getContext()).load(uri).into(img_bookImage);
                            }
                        });

                        break;

                    }

                }




            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }



        });



        postButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getContext().getSharedPreferences("IDOFUSER",MODE_PRIVATE) ;
                String email = preferences.getString("email","");
                Firebase m_objFireBaseRef = new Firebase(FIREBASE_BOOK_RETRIEVE);
                Firebase objRef = m_objFireBaseRef.child(bookId);

                if(type.equalsIgnoreCase("buy")){
                    objRef.child("bookReservedBy").setValue(email);
                    objRef.child("bookStatus").setValue("Reserved");

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Bookt! Reservation");
                    intent.putExtra(Intent.EXTRA_TEXT, "A reservation has been made to one of your book titled '"
                    + book.getBookTitle() + "'. Reserved by: "  + email + "." +
                            "\n------------------" +
                            "\n Comments:");

                    //need this to prompts email client only
                    intent.setType("message/rfc822");

                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));

                    Log.d("emailto", emailTo);

                    //sending push notifs - using LoopJ
//                    try {
//
//
//                        StringEntity entity = new StringEntity("{'app_id': 'd5a5b2f4-d363-4aaa-8e7a-98c5ed505d8f','filters': [{'field': 'tag', 'key': 'email', 'relation': '=', 'value': '"+emailTo+"'}],'contents': {'en': 'English Message'}}");
//
//                        Log.d("URL", "https://onesignal.com/api/v1/notifications" + entity);
//                        final AsyncHttpClient client = new AsyncHttpClient();
//                        client.addHeader("Authorization", "Basic YjNhZWIyNjQtODIxNC00NjA0LTlhOWEtMGIyMDc4OTU1NmFm");
//                        client.post(getContext(),"https://onesignal.com/api/v1/notifications", entity, "application/json",new AsyncHttpResponseHandler() {
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                Log.d("request","success");
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                                Log.d("request",String.valueOf(statusCode));
//                            }
//                    });
//
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }

                    //push notif using Android AsyncTask
                    MyTask myTask = new MyTask();
                    myTask.execute();

                    Toast.makeText(getContext(),"Reservation Success. Please contact " + emailTo +
                            " for the transaction.",Toast.LENGTH_LONG).show();

//                    Intent refresh = new Intent(getActivity(), MainActivity.class);
//                    startActivity(refresh);

                } else if(type.equalsIgnoreCase("sell")){

                    objRef.child("bookReservedBy").setValue(email);
                    objRef.child("bookStatus").setValue("Reserved");

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Bookt! Reservation");
                    intent.putExtra(Intent.EXTRA_TEXT, "A reservation has been made to one of your 'Selling' tagged book titled '"
                            + book.getBookTitle() + "'. Reserved by: "  + email + "." +
                            "\n------------------" +
                            "\n Comments:");

                    //need this to prompts email client only
                    intent.setType("message/rfc822");

                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));



                    MyTask myTask = new MyTask();
                    myTask.execute();

                    Toast.makeText(getContext(),"Reservation Success. Please contact the person" +
                            " through email or contact number.",Toast.LENGTH_LONG).show();

                    Intent refresh = new Intent(getActivity(), MainActivity.class);
                    startActivity(refresh);
                }else if(type.equalsIgnoreCase("myBooks")){

                    objRef.child("bookStatus").setValue("Sold");

                    Toast.makeText(getContext(),"Book Sold! Book will now be removed from the" +
                            " Buying/Selling list.",Toast.LENGTH_LONG).show();

                    Intent refresh = new Intent(getActivity(), MainActivity.class);
                    startActivity(refresh);
//
//                    Toast.makeText(getContext(),"MyBooks: " + book.getBookReservedBy() + " "
//                            + book.getBookStatus(),Toast.LENGTH_LONG).show();
                }
            }
        });

        actionButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Firebase m_objFireBaseRef = new Firebase(FIREBASE_BOOK_RETRIEVE);
                Firebase objRef = m_objFireBaseRef.child(bookId);

                if(type.equalsIgnoreCase("myBooks")){
                    m_objFireBaseRef.child(bookId).removeValue();

                    Toast.makeText(getContext(),"Book Deleted! Book will now be removed from the" +
                            " Buying/Selling list.",Toast.LENGTH_LONG).show();

                    Intent refresh = new Intent(getActivity(), MainActivity.class);
                    startActivity(refresh);

                }else if(type.equalsIgnoreCase("reservedBooks")){
                    objRef.child("bookReservedBy").setValue("");
                    objRef.child("bookStatus").setValue("Available");

                    Toast.makeText(getContext(),"Reservation Cancelled.",Toast.LENGTH_LONG).show();

                    Intent refresh = new Intent(getActivity(), MainActivity.class);
                    startActivity(refresh);
                }
            }
        });



        return view;
    }


    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String jsonResponse;

                URL url = new URL("https://onesignal.com/api/v1/notifications");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", "Basic YjNhZWIyNjQtODIxNC00NjA0LTlhOWEtMGIyMDc4OTU1NmFm");
                con.setRequestMethod("POST");

                String strJsonBody =  "{"
                        +   "\"app_id\": \"d5a5b2f4-d363-4aaa-8e7a-98c5ed505d8f\","
                        +   "\"filters\": [{\"field\": \"tag\", \"key\": \"email\", \"relation\": \"=\", \"value\": \""+emailTo+"\"}],"
                        +   "\"data\": {\"foo\": \"bar\"},"
                        +   "\"contents\": {\"en\": \"Bookt! A reservation has been made on one of your books! See it in the app!\"}"
                        + "}";

                System.out.println("strJsonBody:\n" + strJsonBody);

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = con.getResponseCode();
                System.out.println("httpResponse: " + httpResponse);

                if (  httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                else {
                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                System.out.println("jsonResponse:\n" + jsonResponse);

            } catch(Throwable t) {
                t.printStackTrace();
            }

            return "success";
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
}

