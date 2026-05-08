package com.example.vacationscheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacationList;
    private Context context;

    public VacationAdapter(Context context, List<Vacation> vacationList) {
        this.context = context;
        this.vacationList = vacationList;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation currentVacation = vacationList.get(position);

        holder.title.setText(currentVacation.getVacationTitle());
        holder.dates.setText(currentVacation.getStartDate() + " - " + currentVacation.getEndDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VacationDetails.class);
            intent.putExtra("id", currentVacation.getVacationID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vacationList.size();
    }

    public static class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView title, dates;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.vacationTitle);
            dates = itemView.findViewById(R.id.vacationDates);
        }
    }
}