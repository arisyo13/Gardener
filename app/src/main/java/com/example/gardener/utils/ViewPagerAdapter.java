package com.example.gardener.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gardener.Shifts.FutureShiftsFragment;
import com.example.gardener.Shifts.PastShiftsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FutureShiftsFragment();
            case 1:
                return new PastShiftsFragment();
            default:
                return new FutureShiftsFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
