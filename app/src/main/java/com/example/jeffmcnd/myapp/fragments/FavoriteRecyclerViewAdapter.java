package com.example.jeffmcnd.myapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.models.Beverage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {

    private final List<Beverage> beverages;
    private final FavoriteFragment.OnFavoriteListItemClicked listener;

    public FavoriteRecyclerViewAdapter(List<Beverage> items, FavoriteFragment.OnFavoriteListItemClicked listener) {
        beverages = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorite_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.beverage = beverages.get(position);
        holder.countTextView.setText(String.valueOf(holder.beverage.count));
        holder.nameTextView.setText(holder.beverage.name);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onFavoriteListItemClicked(holder.beverage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return beverages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.count_tv) TextView countTextView;
        @BindView(R.id.name_tv) TextView nameTextView;

        public View view;
        public Beverage beverage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameTextView.getText() + "'";
        }
    }
}
