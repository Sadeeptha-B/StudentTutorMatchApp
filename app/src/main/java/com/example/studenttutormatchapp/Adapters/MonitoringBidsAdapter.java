package com.example.studenttutormatchapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.model.Bid;

import java.util.ArrayList;
import java.util.List;

public class MonitoringBidsAdapter extends RecyclerView.Adapter<MonitoringBidsAdapter.ViewHolder>{

    List<Bid> monitoringBids = new ArrayList<>();

    @NonNull
    @Override
    public MonitoringBidsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_monitoring_bid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonitoringBidsAdapter.ViewHolder holder, int position) {
        Bid bid = monitoringBids.get(position);

        String subject = bid.getSubject().getName()+ " " + bid.getSubject().getDescription();

        holder.studentName.setText(bid.getInitiator().getUserName());
        holder.subject.setText(subject);
    }

    @Override
    public int getItemCount() {
        if (monitoringBids == null)
            return 0;
        return monitoringBids.size();
    }

    public void addMonitoringBid(Bid bid){
        monitoringBids.add(bid);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView studentName;
        private TextView subject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            subject = itemView.findViewById(R.id.monitoringSubject);
        }
    }
}
