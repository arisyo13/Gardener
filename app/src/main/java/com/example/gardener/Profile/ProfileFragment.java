package com.example.gardener.Profile;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.gardener.Login.LoginActivity;
import com.example.gardener.Main.SharedViewModel;
import com.example.gardener.Model.Profile;
import com.example.gardener.R;
import com.example.gardener.utils.RotateBitmap;
import com.example.gardener.utils.SelectPhotoDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ProfileFragment extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener{

    @Override
    public void getImagePath(Uri imagePath) {
        Glide.with(this).load(imagePath).into(profileImage);
        //assign to global variable
        mSelectedBitmap = null;
        mSelectedUri = imagePath;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        Glide.with(this).load(bitmap).into(profileImage);
        //assign to a global variable
        mSelectedUri = null;
        mSelectedBitmap = bitmap;
    }

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button sbutton, share;
    private ImageView profileImage;
    private TextView decisionText;

    private CardView cardViewBasic;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    String current_user_type;

    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;
    private byte[] mUploadBytes;
    private String userID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.userProfileImageView);

        profileImage.setImageResource(R.drawable.ic_baseline_person_outline_24);
        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);
        decisionText = view.findViewById(R.id.decisionText);
        cardViewBasic = view.findViewById(R.id.cardViewBasic);
        ratingBar = view.findViewById(R.id.ratingBar);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();

        init();


        sbutton = (Button) view.findViewById(R.id.sButton);
        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(), "You logged out", Toast.LENGTH_SHORT).show();
            }
        });

        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        currentUser.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    Profile profile = snapshot.getValue(Profile.class);
                    collapsingToolbarLayout.setTitle(profile.getName());
                    ratingBar.setRating(profile.getRating());

                    if(profile.getProfile_image_id().equals("")){
                    }
                    else{
                        Picasso.get().load(profile.getProfile_image_id()).into(profileImage);
                        //Glide.with(getActivity()).load(profile.getProfile_image_id()).into(profileImage);
                    }


                    if(profile.getUser_type().equals("gardener")){
                        decisionText.setText("Gardener");
                        cardViewBasic.setVisibility(View.VISIBLE);
                    }
                    if(profile.getUser_type().equals("customer")){
                        decisionText.setText("Customer");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void init(){

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectPhotoDialog dialog = new SelectPhotoDialog();

                dialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                dialog.setTargetFragment(ProfileFragment.this, 1);

            }


        });

        collapsingToolbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadStart();
            }
        });


    }

    private void uploadStart() {
        if(mSelectedBitmap != null && mSelectedUri == null){
            uploadNewPhoto(mSelectedBitmap);
        }
        //we have no bitmap and a uri
        else if(mSelectedBitmap == null && mSelectedUri != null){
            uploadNewPhoto(mSelectedUri);
        }
    }

    private void uploadNewPhoto(Bitmap bitmap){

        ProfileFragment.BackgroundImageResize resize = new ProfileFragment.BackgroundImageResize(bitmap);
        Uri uri = null;
        resize.execute(uri);
    }

    private void uploadNewPhoto(Uri imagePath){

        ProfileFragment.BackgroundImageResize resize = new ProfileFragment.BackgroundImageResize(null);
        resize.execute(imagePath);
    }

    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        Bitmap mBitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            if(bitmap != null){
                this.mBitmap = bitmap;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "compressing image", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected byte[] doInBackground(Uri... params) {


            if(mBitmap == null){
                try{
                    RotateBitmap rotateBitmap = new RotateBitmap();
                    mBitmap = rotateBitmap.HandleSamplingAndRotationBitmap(getActivity(), params[0]);
                }catch (IOException e){

                }
            }
            byte[] bytes = null;

            bytes = getBytesFromBitmap(mBitmap, 25);

            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mUploadBytes = bytes;

            //execute the upload task
            executeUploadTask();
        }
    }

    private void executeUploadTask(){
        Toast.makeText(getActivity(), "uploading image", Toast.LENGTH_SHORT).show();

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("posts/users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() +
                        "/" + "profile_picture");


        UploadTask uploadTask = storageReference.putBytes(mUploadBytes);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> taskSnapshot) throws Exception {
                if(!taskSnapshot.isSuccessful()){
                    throw taskSnapshot.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    DatabaseReference profileImageRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("profile_image_id");
                    profileImageRef.setValue(downloadUri.toString());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), "Could not upload photo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
        return stream.toByteArray();
    }

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(firebaseAuthListener);
    }

    /**
     *
     */


    @Override
    public void onStop() {
        super.onStop();


        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
