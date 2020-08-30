package com.example.gardener.Profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.example.gardener.Model.Profile;
import com.example.gardener.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditProfileFragment extends Fragment {

    private EditText phone_number, city, country;
    private Button backbtn, postbtn35;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private String name, userID, profileImageID, description, iban, address;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private float rating;


    public EditProfileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile_edit, container, false);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        phone_number = view.findViewById(R.id.phone_number);
        city = view.findViewById(R.id.city);
        country = view.findViewById(R.id.country);
        backbtn = view.findViewById(R.id.backProfile);
        postbtn35 = view.findViewById(R.id.postbtn35);

        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbar);

        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_editProfileFragment_to_profileFragment);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        postbtn35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_id = FirebaseDatabase.getInstance().getReference().child("Users").child(getArguments().getString("user_type"));

                Profile profile = new Profile();
                profile.setName(name);
                profile.setEmail(mAuth.getCurrentUser().getEmail());
                profile.setUser_id(userID);
                profile.setProfile_image_id(profileImageID);
                profile.setRating(rating);
                profile.setProfile_desription(description);
                profile.setIban(iban);
                profile.setPhone_number(phone_number.getText().toString());
                profile.setCity(city.getText().toString());
                profile.setCountry(country.getText().toString());
                profile.setAddress(address);

                current_user_id.child(user_id).setValue(profile);

            }
        });

        DatabaseReference current_user_id = FirebaseDatabase.getInstance().getReference().child("Users").child(getArguments().getString("userType")).child(userID);
        current_user_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Profile profile = snapshot.getValue(Profile.class);

                    name = profile.getName();

                    if(profile.getProfile_image_id().equals("")){

                    }
                    else{
                        Picasso.get().load(profileImageID).into(imageView);
                        profileImageID = profile.getProfile_image_id();
                    }
                    rating = profile.getRating();
                    description = profile.getProfile_desription();
                    address = profile.getAddress();
                    iban = profile.getIban();

                    country.setText(profile.getCountry());
                    city.setText(profile.getCity());
                    phone_number.setText(profile.getPhone_number());
                    collapsingToolbarLayout.setTitle(name);
                    //collapsingToolbarLayout.setBackground(imageView.getDrawable());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

}
