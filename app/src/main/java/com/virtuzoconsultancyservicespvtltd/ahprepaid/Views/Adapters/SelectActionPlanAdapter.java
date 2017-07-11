package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.ActivationPlanClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiraj on 7/1/2017.
 */

public class SelectActionPlanAdapter extends RecyclerView.Adapter<SelectActionPlanAdapter.MyViewHolder> {

    public List<ActivationPlanClass> PlanList;
    public SelectActionPlanAdapter.OnClickListener listener;


    public SelectActionPlanAdapter(ArrayList<ActivationPlanClass> stateList, OnClickListener onClickListener) {
        this.PlanList = stateList;
        this.listener = onClickListener;

    }

    @Override
    public SelectActionPlanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectActionPlanAdapter.MyViewHolder holder, final int position) {

        ActivationPlanClass plan = PlanList.get(position);

        holder.bind(PlanList.get(position));
        holder.planTextView.setText(plan.getTariffPlan());

    }

    @Override
    public int getItemCount() {

        return PlanList.size();
    }


    public interface OnClickListener {
        void onItemClick(ActivationPlanClass plan);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView planTextView;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);
            planTextView = (TextView) itemView.findViewById(R.id.language);

            myView = itemView;


        }

        public void bind(final ActivationPlanClass hotelid) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(hotelid);
                }
            });
        }

    }
}
