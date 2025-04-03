package com.example.asm_ad;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.asm_ad.Adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {

    private BottomNavigationView mNavigationView;
    private ViewPager2 mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Button btnBudget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        mNavigationView = findViewById(R.id.bottom_nav);
        mViewPager = findViewById(R.id.view_pager);

        // Thiết lập ViewPager2
        setUpViewPager();


        // Xử lý khi chọn menu trong BottomNavigationView
        mNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_home) {
                    mViewPager.setCurrentItem(0);
                    return true;
                } else if (id == R.id.menu_expense) {
                    mViewPager.setCurrentItem(1);
                    return true;
                } else if (id == R.id.menu_setting) {
                    mViewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });
    }
    private void setUpViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(viewPagerAdapter);
    }

}