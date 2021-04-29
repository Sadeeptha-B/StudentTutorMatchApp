package com.example.studenttutormatchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OngoingBidsAdapter extends RecyclerView.Adapter<OngoingBidsAdapter.ViewHolder> {

    private Context context;
    private List<OngoingBidData> data;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_bid_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public OngoingBidsAdapter(Context context){
        this.context = context;
    }

    public void setData(List<OngoingBidData> _data){
        this.data = _data;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.subjectName.setText(this.data.get(position).getSubjectName());
        holder.createdDate.setText(this.data.get(position).getDateCreated());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView subjectName;
        public TextView createdDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.subjectName = itemView.findViewById(R.id.textViewBidSubject);
            this.createdDate = itemView.findViewById(R.id.textViewCreatedTime);
        }
    }
}
