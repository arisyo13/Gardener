package com.example.gardener.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gardener.Model.Intro;
import com.example.gardener.R;

import java.util.List;

public class introductionAdapter extends RecyclerView.Adapter<introductionAdapter.OnBoardingViewHolder>{

    private List<Intro> onboardingItems;

    public introductionAdapter(List<Intro> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnBoardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_onboarding_layout, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        holder.setOnboardingData(onboardingItems.get(position));

    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnBoardingViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle, textDescription;
        private ImageView imageOnBoarding;

        OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageOnBoarding = itemView.findViewById(R.id.imageOnBoarding);
        }

        void setOnboardingData(Intro onBoardingItem){
            textTitle.setText(onBoardingItem.getTitle());
            textDescription.setText(onBoardingItem.getDescription());
            imageOnBoarding.setImageResource(onBoardingItem.getImage());
        }
    }

}
