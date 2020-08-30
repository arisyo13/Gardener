package com.example.gardener.Introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gardener.Login.LoginActivity;
import com.example.gardener.Model.Intro;
import com.example.gardener.utils.introductionAdapter;
import com.example.gardener.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class IntroductionActivity extends AppCompatActivity {

    private introductionAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private MaterialButton buttonOnboardingAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        if(restorePreferencesData()){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }


        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnBoardingAction);

        setupOnboardingItems();

        final ViewPager2 onBoardingViewPager = findViewById(R.id.onBoardingViewPager);
        onBoardingViewPager.setAdapter(onboardingAdapter);

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferencesData();
                if(onBoardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem()+1);
                }
                else{
                    savePreferencesData();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        });

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);
        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });
    }

    private boolean restorePreferencesData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isActivityOpened = pref.getBoolean("isOpened", false);
        return isActivityOpened;
    }

    private void setupOnboardingItems(){
        List<Intro> onboardingItems = new ArrayList<>();
        Intro findaGardener = new Intro();
        findaGardener.setTitle("Find A Gardener");
        findaGardener.setDescription("Join the application and find an available Gardener");
        findaGardener.setImage(R.drawable.gardener);

        Intro yourHomeReg = new Intro();
        yourHomeReg.setTitle("Register your Home Garden");
        yourHomeReg.setDescription("Register the garden of your own home and let the available gardeners to find you");
        yourHomeReg.setImage(R.drawable.home);

        Intro yourFlowers = new Intro();
        yourFlowers.setTitle("Search For Available Shifts");
        yourFlowers.setDescription("You can search and see the available shifts and make money");
        yourFlowers.setImage(R.drawable.flowers);

        Intro yourFlowers2 = new Intro();
        yourFlowers2.setTitle("Search For Available Shifts");
        yourFlowers2.setDescription("You can search and see the available shifts and make money");
        yourFlowers2.setImage(R.drawable.flowers);

        onboardingItems.add(findaGardener);
        onboardingItems.add(yourHomeReg);
        onboardingItems.add(yourFlowers);
        onboardingItems.add(yourFlowers2);

        onboardingAdapter = new introductionAdapter(onboardingItems);
    }

    private void setupOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(12,0,12,0);
        for (int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }

    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if ( i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));
            }
            else{
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            }
        }
        if(index == onboardingAdapter.getItemCount()-1){
            buttonOnboardingAction.setText("Start");
        }
        else{
            buttonOnboardingAction.setText("Next");
        }
    }

    private void savePreferencesData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isOpened",true);
        editor.commit();
    }
}