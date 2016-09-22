package com.example.jeffmcnd.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.activity.BrewerActivity;
import com.example.jeffmcnd.myapp.fragments.BeverageFragment;
import com.example.jeffmcnd.myapp.fragments.BrewerFragment;
import com.example.jeffmcnd.myapp.models.Beverage;
import com.example.jeffmcnd.myapp.models.Brewer;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity
        extends FragmentActivity
        implements BrewerFragment.OnListFragmentInteractionListener,
        BeverageFragment.OnListFragmentInteractionListener {
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_lv) ListView drawerListView;

    private String[] drawerOptions;
    private MyFragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        drawerOptions = getResources().getStringArray(R.array.drawer_options);

        drawerListView.setAdapter(new DrawerListAdapter(this, new ArrayList<String>(Arrays.asList(drawerOptions))));

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

class DrawerListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> items;

    public DrawerListAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.drawer_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        String name = (String) getItem(position);

        holder.nameTextView.setText(name);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.drawer_item_name) TextView nameTextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
