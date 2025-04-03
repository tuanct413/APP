package com.example.asm_ad.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.asm_ad.Frafment.ExpenseFrafment;
import com.example.asm_ad.Frafment.HomeFrafment;
import com.example.asm_ad.Frafment.SetingFrafment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {

            case 1:
                return new ExpenseFrafment();
            case 2:
                return new SetingFrafment();
            default:
                return new HomeFrafment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Tổng số tab (fragment)
    }
}