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
                .inflate(R.layout.plan_activation_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectActionPlanAdapter.MyViewHolder holder, final int position) {

        ActivationPlanClass plan = PlanList.get(position);

        String rechargeAmountString = "Recharge Amount - $ " + plan.getAmount();
        String regulatoryString = "Regulatory(" + plan.getRegulatory() + "%) - $ " + (plan.getAmount() * plan.getRegulatory() / 100);
        String discountString = "Validity - " + plan.getValidityDays() + " days";
        String totalAmountString = "$ " + plan.getAmount();

        holder.bind(PlanList.get(position));
        holder.productDescriptionTextView.setText(plan.getTariffPlan());
        holder.rechargeAmountTextView.setText(rechargeAmountString);
        holder.regulatoryTextView.setText(regulatoryString);
        holder.validityTextView.setText(discountString);
        holder.totalAmountTextView.setText(totalAmountString);
    }

    @Override
    public int getItemCount() {

        return PlanList.size();
    }


    public interface OnClickListener {
        void onItemClick(ActivationPlanClass plan);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public View myView;

        public TextView productDescriptionTextView;
        public TextView rechargeAmountTextView;
        public TextView regulatoryTextView;
        public TextView validityTextView;
        public TextView totalAmountTextView;


        public MyViewHolder(View itemView) {

            super(itemView);

            productDescriptionTextView = (TextView) itemView.findViewById(R.id.productDescriptionTextView1);
            rechargeAmountTextView = (TextView) itemView.findViewById(R.id.rechargeAmountTextView1);
            regulatoryTextView = (TextView) itemView.findViewById(R.id.regulatoryTextView1);
            validityTextView = (TextView) itemView.findViewById(R.id.validityTextView);
            totalAmountTextView = (TextView) itemView.findViewById(R.id.totalAmountTextView1);

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
