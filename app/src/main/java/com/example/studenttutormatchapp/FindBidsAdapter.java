package com.example.studenttutormatchapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.model.Bid;

import java.util.List;

public class FindBidsAdapter extends RecyclerView.Adapter<FindBidsAdapter.ViewHolder>{

    List<Bid> bids;


    @NonNull
    @Override
    public FindBidsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.find_bid_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindBidsAdapter.ViewHolder holder, int position) {
        Bid bid = bids.get(position);

        String subjectString = bid.getSubject().getDescription() + " | " + bid.getSubject().getName();
        holder.bidSubject.setText(subjectString);
        holder.bidType.setText(bid.getType());
    }

    @Override
    public int getItemCount() {
        if (bids == null)
            return 0;
        return bids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bidSubject;
        TextView bidType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bidSubject = itemView.findViewById(R.id.BidSubject);
            bidType = itemView.findViewById(R.id.BidType);
        }
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<Bid> getBids() {
        return bids;
    }
}
