package com.example.gardener.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.gardener.R;


public class DecisionFragment extends Fragment {
    private ImageView forestImage, gardenerImage, customerImage;
    private Button backButton;





    public DecisionFragment(){

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decision, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        forestImage = view.findViewById(R.id.forestImage);
        gardenerImage = view.findViewById(R.id.gardenerImage);
        customerImage = view.findViewById(R.id.customerImage);
        backButton = view.findViewById(R.id.goback);


        forestImage.setImageResource(R.drawable.green_gradient);
        gardenerImage.setImageResource(R.drawable.worker);
        customerImage.setImageResource(R.drawable.customer);

        customerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("userType", "customer");
                navController.navigate(R.id.action_decisionFragment_to_registerFragment, bundle);



            }
        });
        gardenerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userType", "gardener");
                navController.navigate(R.id.action_decisionFragment_to_registerFragment, bundle);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_decisionFragment_to_loginFragment);

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
