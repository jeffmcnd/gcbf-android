package com.example.jeffmcnd.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.activity.BeverageActivity;
import com.example.jeffmcnd.myapp.activity.BrewerActivity;
import com.example.jeffmcnd.myapp.fragments.BeverageListFragment;
import com.example.jeffmcnd.myapp.fragments.BrewerListFragment;
import com.example.jeffmcnd.myapp.fragments.FavoriteListFragment;
import com.example.jeffmcnd.myapp.models.Beverage;
import com.example.jeffmcnd.myapp.models.Brewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity
        extends FragmentActivity
        implements BrewerListFragment.OnListFragmentInteractionListener,
        BeverageListFragment.OnListFragmentInteractionListener,
        FavoriteListFragment.OnFavoriteListItemClicked {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.toolbar_tv) public TextView toolbarTextView;
    @BindView(R.id.content_frame) FrameLayout contentFrameLayout;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer_ll) LinearLayout drawerLinearLayout;
    @BindView(R.id.drawer_lv) ListView drawerListView;

    private String[] drawerOptions;
    private BrewerListFragment brewerListFragment = BrewerListFragment.newInstance(1);
    private BeverageListFragment beverageListFragment = BeverageListFragment.newInstance(1);
    private FavoriteListFragment favoriteListFragment = FavoriteListFragment.newInstance(1);
    private List<Fragment> fragments = new ArrayList(Arrays.asList(brewerListFragment, beverageListFragment, favoriteListFragment));
    private String[] fragmentTags = new String[] {"brewer_list", "beverage_list", "favorite_list"};
    private int curFragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeDrawer();

        drawerOptions = getResources().getStringArray(R.array.drawer_options);

        drawerListView.setAdapter(new DrawerListAdapter(this, new ArrayList<String>(Arrays.asList(drawerOptions))));
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                curFragmentIndex = position;
                if (getSupportFragmentManager().findFragmentByTag(fragmentTags[position]) == null) {
                    transaction.add(R.id.content_frame, fragments.get(position), fragmentTags[position]);
                }
                showFragment(position, transaction);
                transaction.commit();
                drawerLayout.closeDrawer(drawerLinearLayout);
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragments.get(0), fragmentTags[0])
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Brewer brewer) {
        Intent intent = new Intent(this, BrewerActivity.class);
        intent.putExtra("brewer", brewer);
        startActivity(intent);
//        BrewerFragment fragment = BrewerFragment.newInstance(brewer);
//        presentFragmentPage(fragment, brewer.name);
    }

    @Override
    public void onListFragmentInteraction(Beverage bev) {
        Intent intent = new Intent(this, BeverageActivity.class);
        intent.putExtra("beverage", bev);
        startActivity(intent);
//        BeverageFragment fragment = BeverageFragment.newInstance(bev);
//        presentFragmentPage(fragment, bev.name);
    }

    @Override
    public void onFavoriteListItemClicked(Beverage bev) {
        Intent intent = new Intent(this, BeverageActivity.class);
        intent.putExtra("beverage", bev);
        startActivity(intent);
//        BeverageFragment fragment = BeverageFragment.newInstance(bev);
//        presentFragmentPage(fragment, bev.name);
    }

    private void presentFragmentPage(Fragment fragment, String toolbarTitle) {
        fragments.add(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, fragment, "page");
        showFragment(fragments.size() - 1, transaction);
        transaction.commit();

        toolbarTextView.setText(toolbarTitle);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnFromFragmentPage();
            }
        });
    }

    private void showFragment(int position, FragmentTransaction transaction) {
        for(int i = 0; i < fragments.size(); i++) {
            if (i == position) {
                transaction.show(fragments.get(i));
            } else if (getSupportFragmentManager().findFragmentByTag(fragmentTags[i]) != null) {
                transaction.hide(fragments.get(i));
            }
        }
    }

    public void returnFromFragmentPage() {
        fragments.remove(fragments.size() - 1);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(getSupportFragmentManager().findFragmentByTag("page"));
        showFragment(curFragmentIndex, transaction);
        transaction.commit();

        initializeDrawer();
    }

    private void initializeDrawer() {
        toolbarTextView.setText(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerLinearLayout);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fragments.size() > fragmentTags.length) {
            returnFromFragmentPage();
        } else {
            super.onBackPressed();
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
