package com.example.jeffmcnd.myapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeffmcnd.myapp.R;
import com.example.jeffmcnd.myapp.fragment.BrewerListFragment.OnListFragmentInteractionListener;
import com.example.jeffmcnd.myapp.model.Brewer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrewerRecyclerViewAdapter extends RecyclerView.Adapter<BrewerRecyclerViewAdapter.ViewHolder> {

    private final List<Brewer> mValues;
    private final OnListFragmentInteractionListener mListener;

    public BrewerRecyclerViewAdapter(List<Brewer> brewers, OnListFragmentInteractionListener listener) {
        mValues = brewers;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_brewer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBrewer = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).name);
        holder.mLocationView.setText(mValues.get(position).location);
        holder.mBoothView.setText(TextUtils.join(", ", mValues.get(position).booths));
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
                    mListener.onListFragmentInteraction(holder.mBrewer);
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
        public final ImageView mImageView;
        public final TextView mNameView;
        public final TextView mLocationView;
        public final TextView mBoothView;
        public Brewer mBrewer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.image);
            mNameView = (TextView) view.findViewById(R.id.name);
            mLocationView = (TextView) view.findViewById(R.id.location);
            mBoothView = (TextView) view.findViewById(R.id.booth);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
