package com.example.jeffmcnd.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.jeffmcnd.myapp.activity.BrewerActivity;
import com.example.jeffmcnd.myapp.fragments.BeverageFragment;
import com.example.jeffmcnd.myapp.fragments.BrewerFragment;
import com.example.jeffmcnd.myapp.models.Beverage;
import com.example.jeffmcnd.myapp.models.Brewer;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.HensonNavigable;


public class MainActivity
        extends FragmentActivity
        implements BrewerFragment.OnListFragmentInteractionListener,
        BeverageFragment.OnListFragmentInteractionListener {

    MyFragmentPagerAdapter mPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onListFragmentInteraction(Brewer brewer) {
        Intent intent = new Intent(this, BrewerActivity.class);
        intent.putExtra("brewer", brewer);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Beverage bev) {

    }
}

class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BrewerFragment.newInstance(1);
            case 1:
                return BeverageFragment.newInstance(1);
            default:
                return BrewerFragment.newInstance(1);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Brewers";
            case 1:
                return "Beverages";
            default:
                return "Brewers";
        }
    }
}
