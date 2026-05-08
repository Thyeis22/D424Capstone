package com.example.vacationscheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> excursionList;
    private Context context;
    private int vacationID;

    public ExcursionAdapter(Context context, List<Excursion> excursionList, int vacationID) {
        this.context = context;
        this.excursionList = excursionList;
        this.vacationID = vacationID;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excursion_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        Excursion excursion = excursionList.get(position);

        holder.title.setText(excursion.getExcursionTitle());
        holder.date.setText(excursion.getExcursionDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExcursionDetails.class);
            intent.putExtra("id", excursion.getExcursionID());
            intent.putExtra("vacationID", vacationID);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return excursionList.size();
    }

    public static class ExcursionViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.excursionTitle);
            date = itemView.findViewById(R.id.excursionDate);
        }
    }
}