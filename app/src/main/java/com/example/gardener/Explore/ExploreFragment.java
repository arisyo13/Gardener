package com.example.gardener.Explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardener.Main.SharedViewModel;
import com.example.gardener.Model.Shift;
import com.example.gardener.utils.ShiftAdapter;
import com.example.gardener.R;

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

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Shift> mList;
    private ShiftAdapter ShiftAdapter;
    private DatabaseReference reference;
    private Calendar calendar;
    private int a, b;
    private FirebaseAuth mAuth;
    private Boolean executed;
    private SharedViewModel viewModel;
    private Button button;
    private EditText editText;

    public ExploreFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerView=view.findViewById(R.id.recyclerViewLayout);
        ShiftAdapter = new ShiftAdapter(getContext(), mList,1);
        recyclerView.setAdapter((ShiftAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        executed = false;

        b = Integer.parseInt(dateTime);
        //Toast.makeText(getActivity(),dateTime,Toast.LENGTH_SHORT).show();

        mList=new ArrayList<Shift>();
        reference = FirebaseDatabase.getInstance().getReference().child("Shifts");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!executed){
                    mList=new ArrayList<Shift>();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        a = Integer.parseInt(dataSnapshot.child("date").getValue().toString());

                        if (a > b){
                            Shift s = dataSnapshot.getValue(Shift.class);
                            mList.add(s);

                        }
                    }

                    Collections.sort(mList, Shift.shiftDate);
                    ShiftAdapter = new ShiftAdapter(getContext(),mList, 1);
                    recyclerView.setAdapter(ShiftAdapter);
                    executed = true;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
