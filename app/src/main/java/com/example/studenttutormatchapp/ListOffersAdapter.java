package com.example.studenttutormatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.Activities.ChatActivity;
import com.example.studenttutormatchapp.Activities.ContractFormActivity;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOffersAdapter extends RecyclerView.Adapter<ListOffersAdapter.ViewHolder> {

    List<Offer> offers;

    String bidType;
    String bidId;
    String userId;

    public ListOffersAdapter(String bidId, String userId){
        this.bidId = bidId;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ListOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOffersAdapter.ViewHolder holder, int position) {

        Offer offer = offers.get(position);

        holder.tutorName.setText(offer.getTutorName());
        holder.competency.setText("Competency:" + offer.getCompetency());
        holder.offeredDate.setText(offer.getOfferedDate());
        holder.rateType.setText(offer.getRateType());
        holder.rate.setText(offer.getOfferedRate());
        holder.description.setText(offer.getDescription());

        if (bidType.equals("closed")){
            holder.chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatActivity = new Intent(v.getContext(), ChatActivity.class);
                    chatActivity.putExtra("bid_id", bidId);
                    chatActivity.putExtra("user_id", userId);
                    v.getContext().startActivity(chatActivity);
                }
            });
        }
        else {
            holder.chatButton.setVisibility(View.GONE);
        }

        holder.selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO close down bid and make the tutor the winner... after a prompt

                if (bidType.equals("closed")){
                    Gson gson = new Gson();
                    String offerJson = gson.toJson(offer);

                    Intent contractFormActivity = new Intent(v.getContext(), ContractFormActivity.class);
                    contractFormActivity.putExtra("offerJson", offerJson);
                    v.getContext().startActivity(contractFormActivity);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (offers == null){
            return 0;
        }
        return offers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tutorName;
        private TextView offeredDate;
        private TextView competency;
        private TextView rateType;
        private TextView rate;
        private TextView description;
        private Button chatButton;
        private Button selectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tutorName = itemView.findViewById(R.id.tutorName);
            this.offeredDate = itemView.findViewById(R.id.offeredDate);
            this.competency = itemView.findViewById(R.id.CompetencyLevel);
            this.offeredDate = itemView.findViewById(R.id.offeredDate);
            this.rateType = itemView.findViewById(R.id.RateType);
            this.rate = itemView.findViewById(R.id.Rate);
            this.description = itemView.findViewById(R.id.descritpion);
            this.chatButton = itemView.findViewById(R.id.chatButton);
            this.selectButton = itemView.findViewById(R.id.SelectTutor);
        }
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public void closeBid(){
        Call call = APIUtils.getBidService().closeDownBid(bidId, new Date());

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}
