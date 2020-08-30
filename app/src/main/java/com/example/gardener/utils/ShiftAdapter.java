package com.example.gardener.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gardener.Model.Shift;
import com.example.gardener.Model.Profile;
import com.example.gardener.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Shift> mShift;
    int mPage;
    String userType;


    public ShiftAdapter(Context context, ArrayList<Shift> shift, int page) {
        mContext = context;
        mShift = shift;
        mPage = page;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shift_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Shift currentItem = mShift.get(position);

        holder.titleView.setText(currentItem.getTitle());
        holder.cityView.setText(currentItem.getCity());
        holder.priceView.setText(currentItem.getPrice());
        holder.dateView.setText(currentItem.getDate());

        Picasso.get()
                .load(currentItem.getImage())
                .into(holder.imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                        holder.gradientView.setVisibility(View.VISIBLE);
                        holder.shiftConstraint.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError(Exception ex) {
                        //do smth when there is picture loading error
                    }
                });


        final DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentItem.getUser_id());

        currentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Profile profile = snapshot.getValue(Profile.class);
                    holder.nameView.setText(profile.getName());
                    userType = profile.getUser_type();

                    Glide.with(mContext).load(profile.getProfile_image_id()).into(holder.userProfileImageView);

                    if(profile.getProfile_image_id().equals("")){
                        //holder.userProfileImageView.setImageResource(R.drawable.ic_baseline_person_outline_24);
                    }
                    else{
                        Picasso.get().load(profile.getProfile_image_id()).into(holder.userProfileImageView);
                        Glide.with(mContext).load(profile.getProfile_image_id()).into(holder.userProfileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    @Override
    public int getItemCount() {

        return mShift.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout shiftConstraint;

        ImageView imageView, gradientView, userProfileImageView;
        TextView titleView, cityView, priceView, nameView, dateView;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            shiftConstraint=itemView.findViewById(R.id.shiftConstraint);
            titleView=itemView.findViewById(R.id.titleShiftView);
            cityView=itemView.findViewById(R.id.ctView);
            imageView=itemView.findViewById(R.id.imageView);
            gradientView=itemView.findViewById(R.id.gradientView);
            priceView=itemView.findViewById(R.id.priceView);
            nameView=itemView.findViewById(R.id.userFullNameTextView);
            userProfileImageView=itemView.findViewById(R.id.userProfileImageView);
            dateView=itemView.findViewById(R.id.shiftHourView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Shift shift = mShift.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("city", shift.getCity());
            bundle.putString("image", shift.getImage());
            bundle.putString("title", shift.getTitle());
            bundle.putString("description", shift.getDescription());
            bundle.putString("country", shift.getCountry());
            bundle.putString("date", shift.getDate());
            bundle.putString("post_id", shift.getPost_id());
            bundle.putString("type", userType);
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);

            if(mPage == 1){
                navController.navigate(R.id.action_searchFragment_to_shiftDetailsFragment, bundle);
            }else if(mPage == 2){
                navController.navigate(R.id.action_myShiftsFragment_to_shiftDetailsFragment, bundle);
            }

        }
    }
}
