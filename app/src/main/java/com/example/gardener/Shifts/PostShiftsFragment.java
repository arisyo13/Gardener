package com.example.gardener.Shifts;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gardener.Model.Shift;
import com.example.gardener.R;
import com.example.gardener.utils.Permissions;
import com.example.gardener.utils.RotateBitmap;
import com.example.gardener.utils.SelectPhotoDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class PostShiftsFragment extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener {

    @Override
    public void getImagePath(Uri imagePath) {

        Glide.with(this).load(imagePath).into(mPostImage);
        //assign to global variable
        mSelectedBitmap = null;
        mSelectedUri = imagePath;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {

        Glide.with(this).load(bitmap).into(mPostImage);

        //assign to a global variable
        mSelectedUri = null;
        mSelectedBitmap = bitmap;
    }


    private ImageView mPostImage;
    private EditText mTitle, mDescription, mPrice, mCountry, mCity;
    private Button mPost;
    private TextView mDate , mTime;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private static final int VERIFY_PERMISSION_REQUEST = 1;

    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;
    private byte[] mUploadBytes;

    private String date, time;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shifts_post, container, false);
        mPostImage = view.findViewById(R.id.post_image);
        mTitle = view.findViewById(R.id.input_title);
        mDescription = view.findViewById(R.id.input_description);
        mPrice = view.findViewById(R.id.input_price);
        mCountry = view.findViewById(R.id.input_country);
        mCity = view.findViewById(R.id.input_city);
        mPost = view.findViewById(R.id.btn_post);
        mDate = view.findViewById(R.id.dateInput);
        mTime = view.findViewById(R.id.timeInput);

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        dateSetListener,
                        year,month,day);
                datePickerDialog.show();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String currentMonth= month+"";
                String currentDay =day+"";

                if (month < 10){
                    currentMonth = "0" + month;


                }if(day < 10){
                    currentDay = "0" + day;
                }

                date = year + currentMonth + currentDay;

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        timeSetListener, 0,0,false);
                timePickerDialog.show();

            }
        };

        timeSetListener = new  TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String currentHour = hourOfDay+"";
                String currentMinute = minute+"";


                if (hourOfDay < 10){
                    currentHour = "0" + hourOfDay;
                }if(minute < 10){
                    currentMinute = "0" +  minute;
                }
                time = currentHour + currentMinute;

                mDate.setText(date);
            }
        };

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        init();

        return view;
    }


    private void init(){

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectPhotoDialog dialog = new SelectPhotoDialog();

                dialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                dialog.setTargetFragment(PostShiftsFragment.this, 1);

                Permissions permissions = new Permissions();

                if(permissions.checkPermissionsArray(getActivity(),Permissions.PERMISIONS)){

                }
                else{
                    permissions.verifyPermissions(getActivity(), Permissions.PERMISIONS);;
                }
            }
        });

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(mTitle.getText().toString())
                        && !isEmpty(mDescription.getText().toString())
                        && !isEmpty(mPrice.getText().toString())
                        && !isEmpty(mCountry.getText().toString())
                        && !isEmpty(mCity.getText().toString())
                        && !isEmpty(mDate.getText().toString())
                        && !isEmpty(mTime.getText().toString())
                ){

                    //we have a bitmap and no Uri
                    if(mSelectedBitmap != null && mSelectedUri == null){
                        uploadNewPhoto(mSelectedBitmap);

                    }
                    //we have no bitmap and a uri
                    else if(mSelectedBitmap == null && mSelectedUri != null){
                        uploadNewPhoto(mSelectedUri);

                    }
                }else{
                    Toast.makeText(getActivity(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadNewPhoto(Bitmap bitmap){

        BackgroundImageResize resize = new BackgroundImageResize(bitmap);
        Uri uri = null;
        resize.execute(uri);
    }

    private void uploadNewPhoto(Uri imagePath){

        BackgroundImageResize resize = new BackgroundImageResize(null);
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

            bytes = getBytesFromBitmap(mBitmap, 40);

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

        final String postId = FirebaseDatabase.getInstance().getReference().push().getKey();
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("posts/users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() +
                        "/" + postId);

        UploadTask uploadTask = storageReference.putBytes(mUploadBytes);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> taskSnapshot) throws Exception {
                if(!taskSnapshot.isSuccessful()){
                    throw taskSnapshot.getException();
                }return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Shift shift = new Shift();
                    shift.setImage(downloadUri.toString());
                    shift.setCity(mCity.getText().toString());
                    shift.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    shift.setCountry(mCountry.getText().toString());
                    shift.setDescription(mDescription.getText().toString());
                    shift.setPost_id(postId);
                    shift.setPrice(mPrice.getText().toString()+"€");
                    shift.setTitle(mTitle.getText().toString());
                    shift.setUser_id(userID);
                    shift.setDate(mDate.getText().toString());
                    reference.child("Shifts")
                            .child(postId)
                            .setValue(shift);





                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), "could not upload photo", Toast.LENGTH_SHORT).show();
            }
        });


        /*.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double currentProgress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                if( currentProgress > (mProgress + 15)){
                    mProgress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    Toast.makeText(getActivity(), mProgress + "%", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }


    private void resetFields(){

        mTitle.setText("");
        mDescription.setText("");
        mPrice.setText("");
        mCountry.setText("");
        mCity.setText("");

    }


    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }

}

