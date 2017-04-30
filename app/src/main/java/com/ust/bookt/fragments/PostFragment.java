package com.ust.bookt.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.ust.bookt.R;
import com.ust.bookt.activities.MainActivity;
import com.ust.bookt.bean.BookBean;
import com.ust.bookt.bean.Config;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.ust.bookt.bean.Config.FIREBASE_BOOK_RETRIEVE;

public class PostFragment extends Fragment {


    private ImageButton ib_imageButton;
    private Button postButton;
    private TextView tv_userName;
    private EditText et_bookTitle;
    private EditText et_bookAuthor;
    private EditText et_userContact;
    private EditText et_userLocation;
    private EditText et_priceRangeStart;
    private EditText et_priceRangeEnd;
    private EditText et_bookDescription;
    private String postStatus;

    private Switch sw_bookPostStatus;

    private StorageReference mStorageReference;
    private static final int GALLERY_INTENT = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.post_layout, null);

        Firebase.setAndroidContext(this.getActivity());
        mStorageReference = FirebaseStorage.getInstance().getReference();

        ib_imageButton = (ImageButton) view.findViewById(R.id.ib_bookPhoto);
        sw_bookPostStatus = (Switch) view.findViewById(R.id.sw_bookPostStatus);
        postButton = (Button)view.findViewById(R.id.bn_postBook);
        tv_userName = (TextView) view.findViewById(R.id.tv_userName);
        et_bookTitle = (EditText) view.findViewById(R.id.et_bookTitle);
        et_bookAuthor = (EditText) view.findViewById(R.id.et_bookAuthor);
        et_userContact = (EditText)view.findViewById(R.id.et_userContact);
        et_userLocation = (EditText)view.findViewById(R.id.et_userLocation);
        et_priceRangeStart = (EditText)view.findViewById(R.id.et_priceRangeStart);
        et_priceRangeEnd = (EditText)view.findViewById(R.id.et_priceRangeEnd);
        et_bookDescription = (EditText)view.findViewById(R.id.et_bookDescription);

        SharedPreferences preferences = this.getContext().getSharedPreferences("IDOFUSER",MODE_PRIVATE) ;
        tv_userName.setText(preferences.getString("email",""));

        sw_bookPostStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(sw_bookPostStatus.isChecked()){
                    postStatus = "Selling";
                    sw_bookPostStatus.setText("Selling");
                    Toast.makeText(getActivity(),"Selling",Toast.LENGTH_SHORT).show();
                }else{
                    postStatus = "Buying";
                    sw_bookPostStatus.setText("Buying");
                    Toast.makeText(getActivity(),"Buying",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ib_imageButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if(data != null){
            ib_imageButton.setImageURI(data.getData());
        }



        postButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                Firebase ref = new Firebase(Config.FIREBASE_BOOK);
                BookBean book = new BookBean();

                //Adding values
                book.setBookAuthor(et_bookAuthor.getText().toString());
                book.setBookContactNumber(et_userContact.getText().toString());
                book.setBookDesc(et_bookDescription.getText().toString());
                book.setBookPostStatus(postStatus);
                book.setBookPriceEnd(et_priceRangeEnd.getText().toString());
                book.setBookPriceStart(et_priceRangeStart.getText().toString());
                book.setBookSeller(tv_userName.getText().toString());
                book.setBookStatus("Available");
                book.setBookTitle(et_bookTitle.getText().toString());
                book.setBookLocation(et_userLocation.getText().toString());
                book.setBookReservedBy("");
                book.setBookImgUrl("");


                if((book.getBookAuthor().trim().isEmpty() || book.getBookTitle().trim().isEmpty() ||
                        book.getBookPriceStart().trim().isEmpty()) && postStatus.equalsIgnoreCase("Buying")){

                    Toast.makeText(getContext(),"Author, Title and Price is Required", Toast.LENGTH_LONG).show();

                }else if((book.getBookAuthor().trim().isEmpty() || book.getBookTitle().trim().isEmpty() ||
                        book.getBookPriceStart().trim().isEmpty() || book.getBookPriceEnd().trim().isEmpty())
                        && postStatus.equalsIgnoreCase("Selling")){

                    Toast.makeText(getContext(),"Author, Title and Price is Required", Toast.LENGTH_LONG).show();

                }else{

                    final Firebase newRef = ref.child("book").push();
                    newRef.setValue(book);

                    if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
                        final String bookId = newRef.getKey().toString();
                        Uri uri = data.getData();
                        StorageReference filePath = mStorageReference.child("Photos").child(newRef.getKey().toString());
                        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(),"Upload Success",Toast.LENGTH_LONG).show();
                                StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
                                mStorageReference.child("Photos/" + bookId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Firebase m_objFireBaseRef = new Firebase(FIREBASE_BOOK_RETRIEVE);
                                        Firebase objRef = m_objFireBaseRef.child(bookId);
                                        Log.d("uri", uri.toString());
                                            objRef.child("bookImgUrl").setValue(uri.toString());

                                        Intent refresh = new Intent(getActivity(), MainActivity.class);
                                        startActivity(refresh);
                                    }

                                });


                            }
                        });
                    }


                    Toast.makeText(getActivity(),"Posting success!",Toast.LENGTH_SHORT).show();
//
                }
            }
        });



    }

}
