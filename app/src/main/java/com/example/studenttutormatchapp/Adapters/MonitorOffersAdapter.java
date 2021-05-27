package com.example.studenttutormatchapp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.MakeOfferFormActivity;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.model.Bid;
import com.google.gson.Gson;

import java.util.List;

public class MonitorOffersAdapter extends RecyclerView.Adapter<MonitorOffersAdapter.ViewHolder>{

    List<Offer> offers;
    String userId;
    Bid bid;

    @NonNull
    @Override
    public MonitorOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_monitoring_offer, parent, false);
        return new MonitorOffersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonitorOffersAdapter.ViewHolder holder, int position) {
        Offer offer = offers.get(position);

        holder.tutor.setText(offer.getTutorName());
        holder.rate.setText(offer.getOfferedRate()+ ": " + offer.getRateType());
        holder.time.setText(offer.getOfferedDate());

        if (userId.equals(offer.getTutorId())){
            holder.newOfferBtn.setVisibility(View.VISIBLE);
            holder.newOfferBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent activity = new Intent(v.getContext(), MakeOfferFormActivity.class);
                    Gson gson = new Gson();
                    activity.putExtra("bidJson", gson.toJson(bid));
                    activity.putExtra("userId", userId);
                    v.getContext().startActivity(activity);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (offers == null)
            return 0;
        return offers.size();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
        this.offers = bid.getAdditionalInfo().getOffers();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tutor;
        private TextView rate;
        private TextView time;

        private Button newOfferBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tutor = itemView.findViewById(R.id.offeringParty);
            rate = itemView.findViewById(R.id.offeredRate);
            time = itemView.findViewById(R.id.offeredTime);
            newOfferBtn = itemView.findViewById(R.id.renewOffer);
        }
    }
    }
