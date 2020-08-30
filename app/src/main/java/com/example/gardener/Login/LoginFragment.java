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
import com.example.gardener.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button loginButton;
    private EditText email, password;
    private Boolean isLoggingOut = false;
    private ProgressBar progress;
    private TextView goToRegister;




    public LoginFragment(){

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        final NavController navController = Navigation.findNavController(view);

        loginButton = view.findViewById(R.id.loginButton);
        goToRegister = view.findViewById(R.id.goToRegister);

        email = view.findViewById(R.id.logEmailInput);
        password = view.findViewById(R.id.logPasswordInput);
        progress = view.findViewById(R.id.progressBarLogin);


        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_decisionFragment);

            }
        });

        mAuth = FirebaseAuth.getInstance();
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = email.getText().toString();
                String pass = password.getText().toString();
                if(login.isEmpty() || pass.isEmpty()){
                    Toast.makeText(getActivity(), "Add email and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    progress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(login, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(getActivity(), "Sign in error", Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);
                            }
                            else if(task.isSuccessful()){

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                return;
                            }
                        }

                    });
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
