package com.example.studenttutormatchapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.view.ChatActivity;
import com.example.studenttutormatchapp.view.ContractFormActivity;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.ContractAdditionalInfo;
import com.example.studenttutormatchapp.helpers.ContractLessonInfo;
import com.example.studenttutormatchapp.helpers.ContractPaymentInfo;
import com.example.studenttutormatchapp.helpers.DateClosedDownWrapper;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.model.pojo.Contract;

import com.example.studenttutormatchapp.remote.APIUtils;
import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOffersAdapter extends RecyclerView.Adapter<ListOffersAdapter.ViewHolder> {

    List<Offer> offers;

    String bidType;
    String bidId;
    String userId;

    Context context;

    public ListOffersAdapter(String bidId, String userId, Context context){
        this.bidId = bidId;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public ListOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_offer, parent,false);
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
                if (bidType.equals("closed")){
//                    Gson gson = new Gson();
//                    String offerJson = gson.toJson(offer);
                    Intent contractFormActivity = new Intent(v.getContext(), ContractFormActivity.class);
                    contractFormActivity.putExtra("tutorId", offer.getTutorId());
                    contractFormActivity.putExtra("subjectName", offer.getSubject());
                    contractFormActivity.putExtra("subjectId", offer.getSubjectId());
                    contractFormActivity.putExtra("competency", offer.getCompetency());

                    contractFormActivity.putExtra("isRenewal", false);
//                    contractFormActivity.putExtra("offerJson", offerJson);
                    v.getContext().startActivity(contractFormActivity);
                }
                else {
                    closeBid(offer);
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

    public void closeBid(Offer offer){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        closeBidWeb(offer);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Select Tutor?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void closeBidWeb(Offer offer){
        ZonedDateTime dateClosed = ZonedDateTime.now();
        String dateClosedStr = dateClosed.format(DateTimeFormatter.ISO_INSTANT);
        String dateExpired = dateClosed.plus(1, ChronoUnit.YEARS).format(DateTimeFormatter.ISO_INSTANT);


        Call<Void> call = APIUtils.getBidService().closeDownBid(bidId, new DateClosedDownWrapper(dateClosedStr));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("OFFER", String.valueOf(response.code()));
                if (response.code() == 200){
                    ContractPaymentInfo paymentInfo = new ContractPaymentInfo(offer.getOfferedRate(), offer.getRateType());
                    String[] day = offer.getOfferedDate().split(" ");
                    ContractLessonInfo lessonInfo = new ContractLessonInfo(day[0], day[1]);
                    ContractAdditionalInfo additionalInfo = new ContractAdditionalInfo(false, false);

                    Contract contract = new Contract(offer.getTutorId(), userId, offer.getSubjectId(), dateClosedStr, dateExpired, paymentInfo, lessonInfo, additionalInfo);
                    createContract(contract);
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void createContract(Contract contract){
        Call<Contract> call = APIUtils.getContractService().createContract(contract);

        call.enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(context, "Contract Created! \nYou can sign it in the dashboard", Toast.LENGTH_SHORT).show();
                    }
            }
            @Override
            public void onFailure(Call<Contract> call, Throwable t) {

            }
        });
    }
}
