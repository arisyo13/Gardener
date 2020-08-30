package com.example.gardener.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.gardener.Main.MainActivity;
import com.example.gardener.Model.Profile;
import com.example.gardener.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button registerButton;
    private EditText email, password, passwordCheck, name;
    private ProgressBar progressBar;
    private TextView backToLogin;
    private Boolean isLoggingOut = false;




    public RegisterFragment(){

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        backToLogin = view.findViewById(R.id.backToLogin);
        registerButton = view.findViewById(R.id.registerButton);

        email = view.findViewById(R.id.regEmailInput);
        password = view.findViewById(R.id.regPasswordInput);
        passwordCheck = view.findViewById(R.id.regPasswordInputCheck);
        progressBar = view.findViewById(R.id.progressBarRegister);
        name = view.findViewById(R.id.fullNameInput);


        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);

            }
        });

        mAuth = FirebaseAuth.getInstance();

        Toast.makeText(getActivity(), getArguments().getString("userType"), Toast.LENGTH_SHORT).show();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
            }
        };


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                String pa = password.getText().toString();

                if(em.isEmpty()|| pa.isEmpty() || name.getText().toString().isEmpty()  || passwordCheck.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Complete all fields", Toast.LENGTH_SHORT).show();


                }
                else{

                    if(pa.equals(passwordCheck.getText().toString())){

                        progressBar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(em, pa).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Register Error", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);

                                }
                                else{
                                    String user_id = mAuth.getCurrentUser().getUid();

                                    DatabaseReference currentUID = FirebaseDatabase.getInstance().getReference().child("Users");

                                    Profile profile = new Profile();
                                    profile.setName(name.getText().toString());
                                    profile.setEmail(mAuth.getCurrentUser().getEmail());
                                    profile.setUser_id(mAuth.getCurrentUser().getUid());
                                    profile.setProfile_image_id("");
                                    profile.setRating(0);
                                    profile.setProfile_desription("");
                                    profile.setIban("");
                                    profile.setPhone_number("");
                                    profile.setCity("");
                                    profile.setCountry("Greece");
                                    profile.setAddress("");
                                    profile.setUser_type(getArguments().getString("userType"));

                                    currentUID.child(user_id).setValue(profile);

                                }
                            }

                        });

                    }
                    else{
                        Toast.makeText(getActivity(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!isLoggingOut){

        }
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }


}
