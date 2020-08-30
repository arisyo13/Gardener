package com.example.gardener.Shifts;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardener.Model.Shift;
import com.example.gardener.R;
import com.example.gardener.utils.ShiftAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class FutureShiftsFragment extends Fragment  {

    private RecyclerView recyclerView;
    private ArrayList<Shift> mList;
    private ShiftAdapter ShiftAdapter;
    private FirebaseAuth mAuth;
    private String userID, selection;
    private int shiftDate, currentDate;
    private Calendar calendar;
    private Boolean executed, executed2;
    private TabLayout tabLayout;
    private ArrayList<String> keyArray = new ArrayList<String>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shifts_future, container, false);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList=new ArrayList<Shift>();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        currentDate = Integer.parseInt(dateTime);
        executed = false;
        executed2 = false;
        selection = "Choosed";

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView=view.findViewById(R.id.recyclerViewShiftFuture);


        tabLayout = view.findViewById(R.id.futureTabLayout);
        mList=new ArrayList<Shift>();
        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        currentDate = Integer.parseInt(dateTime);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    selection = "Choosed";
                    gardenerShiftCheck(selection);
                    executed = false;
                    executed2 = false;
                    keyArray = new ArrayList<String>();

                }
                if(tab.getPosition()==1){
                    selection = "Applied";
                    gardenerShiftCheck(selection);
                    executed = false;
                    executed2 = false;
                    keyArray = new ArrayList<String>();

                }
                if(tab.getPosition()==2){
                    selection = "Declined";
                    gardenerShiftCheck(selection);
                    executed = false;
                    executed2 = false;
                    keyArray = new ArrayList<String>();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        findUser();

        ShiftAdapter = new ShiftAdapter(getContext(), mList,2);
        recyclerView.setAdapter((ShiftAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void findUser(){

        DatabaseReference currentCustomer = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        currentCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if(snapshot.child("user_type").getValue().toString().equals("gardener")){
                        gardenerShiftCheck("Choosed");

                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void gardenerShiftCheck(String select){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(select).child(mAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!executed2){
                    for(final DataSnapshot dataSnapshot: snapshot.getChildren()){
                        keyArray.add(dataSnapshot.getKey());
                    }
                    checkTheShift();
                    executed2 = true;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void checkTheShift(){



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shifts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(executed.equals(false)){
                    mList=new ArrayList<Shift>();

                    for(DataSnapshot data: snapshot.getChildren()){
                        if(!executed){
                            int a = keyArray.size();

                            for(int i=0; i < a; i++){
                                if(keyArray.get(i).equals(data.getKey())){
                                    shiftDate = Integer.parseInt(data.child("date").getValue().toString());
                                    if (shiftDate > currentDate){
                                        Shift s = data.getValue(Shift.class);
                                        mList.add(s);
                                    }
                                }
                            }
                        }
                    }
                }
                Collections.sort(mList, Shift.shiftDate);
                ShiftAdapter = new ShiftAdapter(getContext(),mList, 2);
                recyclerView.setAdapter(ShiftAdapter);
                executed = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
