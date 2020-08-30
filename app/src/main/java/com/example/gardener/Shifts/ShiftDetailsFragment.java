package com.example.gardener.Shifts;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gardener.Model.Shift;
import com.example.gardener.R;
import com.example.gardener.utils.ShiftAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShiftDetailsFragment extends Fragment {

    private TextView titleView, descView, cityView;
    private ImageView imageView;
    private ListView listView;
    private ArrayList<Shift> mList;
    private DatabaseReference reference;
    private String[] array = {"2020", "2019","2041","2041","2041","2041", "1992", "1993", "1987"};
    private MaterialButton applyButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public ShiftDetailsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shifts_details, container, false);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.pictureView);
        titleView = view.findViewById(R.id.titleTextView);
        descView = view.findViewById(R.id.descView);
        cityView = view.findViewById(R.id.cityView);
        listView = view.findViewById(R.id.listviewfirst);
        applyButton = view.findViewById(R.id.applyButton);


        titleView.setText(getArguments().getString("title"));
        descView.setText(getArguments().getString("description"));
        cityView.setText(getArguments().getString("city") + ", " + getArguments().getString("country"));
        Picasso.get().load(getArguments().getString("image")).into(imageView);
        if(getArguments().getString("type").equals("customer")){
            applyButton.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "its invisible", Toast.LENGTH_SHORT).show();

        }

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Applied")
                        .child(mAuth.getUid())
                        .child(getArguments().getString("post_id"));
                reference.setValue(getArguments().getString("post_id"));





            }
        });

        DatabaseReference hasAppliedRef = FirebaseDatabase.getInstance().getReference().child("Shifts").child(getArguments().getString("post_id")).child("applicants");
        hasAppliedRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(mAuth.getUid())){
                        //Toast.makeText(getActivity(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        applyButton.setText("Applied");
                        applyButton.setClickable(false);
                        applyButton.setTextColor(R.color.colorTextSecondary);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.select_dialog_multichoice,array);
        listView.setAdapter(adapter);

    }
}
