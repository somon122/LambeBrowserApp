package com.world_tech_point.lambebrowser.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.world_tech_point.lambebrowser.R;


import java.util.List;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.ViewHolder> {

    private Context context;
    private List<WithdrawClass>list;
    private WithdrawClass withdrawClass;

    public WithdrawAdapter(Context context, List<WithdrawClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WithdrawAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.withdraw_show_model,parent,false);
        return new WithdrawAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawAdapter.ViewHolder holder, int position) {

        withdrawClass = list.get(position);
        holder.name.setText(withdrawClass.getUserName());
        holder.type.setText(withdrawClass.getType());
        holder.amount.setText(withdrawClass.getAmount());
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,type,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.withdrawUserName);
            type = itemView.findViewById(R.id.withdrawType);
            amount = itemView.findViewById(R.id.withdrawAmount);

        }
    }
}
