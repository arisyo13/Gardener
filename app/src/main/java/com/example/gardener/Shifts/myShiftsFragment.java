package com.example.gardener.Shifts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gardener.R;
import com.example.gardener.utils.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myShiftsFragment extends Fragment  {


    private FloatingActionButton floatingActionButton;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FirebaseAuth mAuth;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shifts, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();

        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        currentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot: snapshot.getChildren()){



                    if(snapshot.child("user_type").getValue().toString().equals("gardener")){
                        floatingActionButton.setVisibility(View.INVISIBLE);
                    }
                    if(snapshot.child("user_type").getValue().toString().equals("customer")){
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager2Shifts);

        viewPager2.setAdapter(new ViewPagerAdapter(getActivity()));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: {
                        tab.setText("Future");
                        break;
                    }
                    case 1: {
                        tab.setText("Past");
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_myShiftsFragment_to_postShiftsFragment);
            }
        });

    }

}
