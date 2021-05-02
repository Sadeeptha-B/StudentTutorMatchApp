package com.example.studenttutormatchapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.MakeOfferFormActivity;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.ContractAdditionalInfo;
import com.example.studenttutormatchapp.helpers.ContractLessonInfo;
import com.example.studenttutormatchapp.helpers.ContractPaymentInfo;
import com.example.studenttutormatchapp.helpers.DateClosedDownWrapper;
import com.example.studenttutormatchapp.model.Bid;
import com.example.studenttutormatchapp.model.Contract;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.BidService;
import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindBidsAdapter extends RecyclerView.Adapter<FindBidsAdapter.ViewHolder>{

    List<Bid> bids;
    Context context;
    private String userId;
    ZonedDateTime timeNow;

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

        if (bid.getType().equals("closed")){
            holder.btnBuyout.setVisibility(View.GONE);
        }

        holder.btnMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String bidJson = gson.toJson(bid);

                Intent intent = new Intent(context, MakeOfferFormActivity.class);
                intent.putExtra("bidJson", bidJson);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });



        holder.btnBuyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               closeBid(bid);
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

    public void setUserId(String userId){
        this.userId = userId;
    }

    private void closeBid(Bid bid){
        timeNow = ZonedDateTime.now();
        String dateClosedStr = timeNow.format(DateTimeFormatter.ISO_INSTANT);

        Call<Void> call = APIUtils.getBidService().closeDownBid(bid.getId(), new DateClosedDownWrapper(dateClosedStr));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("CHECK", String.valueOf(response.code()));
                if (response.code() == 200){
                    createContract(bid, dateClosedStr);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void createContract(Bid bid, String dateOpened){
        String[] filter = bid.getAdditionalInfo().getPreferredDateTime().split(" ");

        String expiryDate = timeNow.plus(1, ChronoUnit.YEARS).format(DateTimeFormatter.ISO_INSTANT);
        ContractPaymentInfo contractPaymentInfo = new ContractPaymentInfo(bid.getAdditionalInfo().getPreferredRate(), bid.getAdditionalInfo().getRateType());
        ContractLessonInfo contractLessonInfo = new ContractLessonInfo(filter[0], filter[1]);
        ContractAdditionalInfo contractAdditionalInfo = new ContractAdditionalInfo(false, false);

        Contract contract = new Contract(userId, bid.getInitiator().getId(), bid.getSubject().getId(), dateOpened, expiryDate,contractPaymentInfo, contractLessonInfo, contractAdditionalInfo);

        Call<Contract> call = APIUtils.getContractService().createContract(contract);
        call.enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
                Log.d("CHECK", String.valueOf(response.code()));
                onSuccess();

            }

            @Override
            public void onFailure(Call<Contract> call, Throwable t) {

            }
        });
    }

    private void onSuccess(){
        Toast.makeText(context, "Contract has been created. You can sign it from the dashboard", Toast.LENGTH_LONG).show();
    }


}
