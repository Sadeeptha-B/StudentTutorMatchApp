package com.example.studenttutormatchapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

public class ListOffersAdapter extends RecyclerView.Adapter<ListOffersAdapter.ViewHolder> {
    @NonNull
    @Override
    public ListOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOffersAdapter.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Offer" + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tutorName;
        private TextView bidDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tutorName = itemView.findViewById(R.id.tutorName);
            this.bidDate = itemView.findViewById(R.id.bidDate);
        }
    }
}
