package com.example.studenttutormatchapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.DateSignedWrapper;
import com.example.studenttutormatchapp.model.Contract;
import com.example.studenttutormatchapp.model.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.ContractService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractListAdapter extends RecyclerView.Adapter<ContractListAdapter.ViewHolder> {

    private List<Contract> contracts;
    private String userId;
    private boolean isStudent;
    Context context;

    ContractService APIContractInterface = APIUtils.getContractService();

    public ContractListAdapter(Context context, boolean isStudent){
        this.context = context;
        this.isStudent = isStudent;
    }

    @NonNull
    @Override
    public ContractListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contract_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractListAdapter.ViewHolder holder, int position) {
        Contract contract = contracts.get(position);
        User otherParty = contract.getSecondParty();
        if (isStudent){
            otherParty = contract.getFirstParty();
        }
        holder.otherParty.setText(otherParty.getGivenName());
        holder.subject.setText(contract.getSubject().getDescription());

        if (contract.getDateSigned() == null){
            if (isStudent && contract.getAdditionalInfo().isSecondPartySigned()){
                holder.signButton.setVisibility(View.GONE);
                holder.signed.setText("Unsigned");
                holder.signed.setVisibility(View.VISIBLE);
            }
            else if (!isStudent && contract.getAdditionalInfo().isFirstPartySigned()){
                holder.signButton.setVisibility(View.GONE);
                holder.signed.setText("Unsigned");
                holder.signed.setVisibility(View.VISIBLE);
            }
            else{
                holder.signButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signContract(contract, !isStudent);
                        holder.signButton.setVisibility(View.GONE);
                    }
                });
            }
        }
        else {
            holder.signButton.setVisibility(View.GONE);
            holder.signed.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (contracts == null){
            return 0;
        }
        return contracts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView otherParty;
        private TextView subject;
        private TextView signed;
        private Button signButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.otherParty = itemView.findViewById(R.id.otherParty);
            this.subject = itemView.findViewById(R.id.subject);
            this.signed = itemView.findViewById(R.id.Signed);
            this.signButton = itemView.findViewById(R.id.signContract);
        }
    }

    public void signContract(Contract contract, boolean firstParty){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (firstParty){
                            contract.getAdditionalInfo().setFirstPartySigned(true);
                        }
                        else {
                            contract.getAdditionalInfo().setSecondPartySigned(true);

                        }

                        if (contract.getAdditionalInfo().isFirstPartySigned() && contract.getAdditionalInfo().isSecondPartySigned()){
                            signContractWeb(contract);
                        }
                        updateContract(contract);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Cancel clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Sign Contract?").setPositiveButton("Sign", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    public void updateContract(Contract contract){
        Call<Contract> call = APIContractInterface.updateContract(contract.getId(), contract);

        call.enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<Contract> call, Throwable t) {

            }
        });
    }

    public void signContractWeb(Contract contract){
        ZonedDateTime dateSigned = ZonedDateTime.now();
        String dateSignedStr = dateSigned.format(DateTimeFormatter.ISO_INSTANT);


        Call<Void> call = APIContractInterface.signContract(contract.getId(), new DateSignedWrapper(dateSignedStr));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public void clearContracts(){
        this.contracts.clear();
    }
}
