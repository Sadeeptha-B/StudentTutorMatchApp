package com.example.studenttutormatchapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.Activities.MakeOfferFormActivity;
import com.example.studenttutormatchapp.Activities.MessageListActivity;
import com.example.studenttutormatchapp.model.Bid;
import com.google.gson.Gson;

import java.util.List;

public class FindBidsAdapter extends RecyclerView.Adapter<FindBidsAdapter.ViewHolder>{

    List<Bid> bids;
    Context context;

    public FindBidsAdapter(Context context){
        this.context = context;
    }


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
        holder.bidStudent.setText(bid.getInitiator().getUserName());
        holder.bidType.setText(bid.getType());
        holder.bidCompetency.setText(bid.getAdditionalInfo().getCompetency());
        holder.bidPreferredDay.setText(bid.getAdditionalInfo().getPreferredDateTime());
        holder.bidPreferredRate.setText(bid.getAdditionalInfo().getPreferredRate());
        holder.bidRateType.setText(bid.getAdditionalInfo().getRateType());


        holder.btnMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String bidJson = gson.toJson(bid);

                Intent intent = new Intent(context, MakeOfferFormActivity.class);
                intent.putExtra("bidJson", bidJson);
                context.startActivity(intent);
            }
        });

        holder.btnBuyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (bids == null)
            return 0;
        return bids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bidSubject;
        TextView bidStudent;
        TextView bidType;
        TextView bidCompetency;
        TextView bidPreferredDay;
        TextView bidPreferredRate;
        TextView bidRateType;

        Button btnMakeOffer;
        Button btnBuyout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bidSubject = itemView.findViewById(R.id.BidSubject);
            bidStudent = itemView.findViewById(R.id.textViewStudentName);
            bidType = itemView.findViewById(R.id.BidType);
            bidCompetency = itemView.findViewById(R.id.textViewBidCardCompetency);
            bidPreferredDay = itemView.findViewById(R.id.textViewBidCardPrefDay);
            bidPreferredRate = itemView.findViewById(R.id.textViewBidCardPrefRate);
            bidRateType = itemView.findViewById(R.id.textViewBidCardRateType);

            btnMakeOffer = itemView.findViewById(R.id.btnMakeOffer);
            btnBuyout = itemView.findViewById(R.id.btnBuyout);
        }
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<Bid> getBids() {
        return bids;
    }
}
