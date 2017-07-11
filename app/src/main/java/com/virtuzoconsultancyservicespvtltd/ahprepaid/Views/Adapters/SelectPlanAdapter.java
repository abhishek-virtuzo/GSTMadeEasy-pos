package com.virtuzoconsultancyservicespvtltd.ahprepaid.Views.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtuzoconsultancyservicespvtltd.ahprepaid.R;
import com.virtuzoconsultancyservicespvtltd.ahprepaid.modal.PlanClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiraj on 7/1/2017.
 */

public class SelectPlanAdapter extends RecyclerView.Adapter<SelectPlanAdapter.MyViewHolder> {

    private List<PlanClass> PlanList;
    private SelectPlanAdapter.OnClickListener listener;


    public SelectPlanAdapter(ArrayList<PlanClass> stateList, SelectPlanAdapter.OnClickListener onClickListener) {
        this.PlanList = stateList;
        this.listener = onClickListener;

    }

    @Override
    public SelectPlanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectPlanAdapter.MyViewHolder holder, final int position) {

        PlanClass plan = PlanList.get(position);

        String rechargeAmountString = "Recharge Amount - " + plan.getCurrencySymbol() + " " + plan.getRechargeAmount();
        String regulatoryString = "Regulatory(" + plan.getRegulatory() + "%) - " + plan.getCurrencySymbol() + " " + (plan.getRechargeAmount() * plan.getRegulatory() / 100);
        String discountString = "Discount(" + plan.getDiscount() + "%) - " + plan.getCurrencySymbol() + " " + (plan.getRechargeAmount() * plan.getDiscount() / 100);
        String totalAmountString = plan.getCurrencySymbol() + " " + plan.getTotalAmount();

        holder.bind(PlanList.get(position));
        holder.productDescriptionTextView.setText(plan.getProductDescription());
        holder.rechargeAmountTextView.setText(rechargeAmountString);
        holder.regulatoryTextView.setText(regulatoryString);
        holder.discountTextView.setText(discountString);
        holder.totalAmountTextView.setText(totalAmountString);

    }

    @Override
    public int getItemCount() {

        return PlanList.size();
    }


    public interface OnClickListener {
        void onItemClick(PlanClass plan);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productDescriptionTextView;
        public TextView rechargeAmountTextView;
        public TextView regulatoryTextView;
        public TextView discountTextView;
        public TextView totalAmountTextView;

        public View myView;


        public MyViewHolder(View itemView) {
            super(itemView);

            productDescriptionTextView = (TextView) itemView.findViewById(R.id.productDescriptionTextView);
            rechargeAmountTextView = (TextView) itemView.findViewById(R.id.rechargeAmountTextView);
            regulatoryTextView = (TextView) itemView.findViewById(R.id.regulatoryTextView);
            discountTextView = (TextView) itemView.findViewById(R.id.discountTextView);
            totalAmountTextView = (TextView) itemView.findViewById(R.id.totalAmountTextView);

            myView = itemView;
        }

        public void bind(final PlanClass hotelid) {
            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(hotelid);
                }
            });
        }

    }
}
