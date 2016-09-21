package com.example.jeffmcnd.myapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.fragments.BeverageFragment.OnListFragmentInteractionListener;
import com.example.jeffmcnd.myapp.models.Beverage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BeverageRecyclerViewAdapter extends RecyclerView.Adapter<BeverageRecyclerViewAdapter.ViewHolder> {

    private final List<Beverage> mValues;
    private final OnListFragmentInteractionListener mListener;

    public BeverageRecyclerViewAdapter(List<Beverage> bevs, OnListFragmentInteractionListener listener) {
        mValues = bevs;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_beverage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBeverage = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).name);
        holder.mBrewerIdView.setText(String.valueOf(mValues.get(position).brewer_id));
        try {
            Picasso.with(holder.mImageView.getContext())
                    .load(mValues.get(position).imageurl)
                    .into(holder.mImageView);
        } catch (IllegalArgumentException e) {
            holder.mImageView.setVisibility(View.GONE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mBeverage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mBrewerIdView;
        public final ImageView mImageView;
        public Beverage mBeverage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mBrewerIdView = (TextView) view.findViewById(R.id.brewer_id);
            mImageView = (ImageView) view.findViewById(R.id.image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBrewerIdView.getText() + "'";
        }
    }
}
