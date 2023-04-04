package ca.future.home.it.secure.home.automation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountFragmentRecyclerViewAdapter extends RecyclerView.Adapter<AccountFragmentViewHolder> {
    private List<AccountFragmentData> accountFragmentDataset;
    private View.OnClickListener onClickListener;
    private final AccountRecyclerViewInterface accountRecyclerViewInterface;

    public AccountFragmentRecyclerViewAdapter(List<AccountFragmentData> dataset, AccountRecyclerViewInterface accountRecyclerViewInterface){
        this.accountRecyclerViewInterface = accountRecyclerViewInterface;
        accountFragmentDataset = dataset;

    }


    @NonNull
    @Override
    public AccountFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_recyclerview_items,parent,false);
        AccountFragmentViewHolder viewHolder = new AccountFragmentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountFragmentViewHolder holder, int position) {
        AccountFragmentData itemText = accountFragmentDataset.get(position);
        holder.accountRVIcon.setImageResource(itemText.getIcon());
        holder.accountRVTitle.setText(itemText.getTitle());
        holder.accountRVDetails.setText(itemText.getDetail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accountRecyclerViewInterface!=null){
                    int pos = holder.getAdapterPosition();
                   if(pos!= RecyclerView.NO_POSITION){
//                       AccountRecyclerViewInterface.onItemClick(pos);
                       accountRecyclerViewInterface.onItemClick(pos);
                   }
                }
            }
        });




//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickListener.onClick(position, itemText);
//            }
//        });

    }
//    public void setOnClickListener(View.OnClickListener onClickListener){
//        this.onClickListener = onClickListener;
//    }
//    public interface OnClickListener{
//        void onClick(int position, AccountFragmentData model);
//    }
//    static class ViewHolder extends RecyclerView.ViewHolder{
//        ItemRowBinding binding;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }

    @Override
    public int getItemCount() {
        return accountFragmentDataset.size();
    }
}
