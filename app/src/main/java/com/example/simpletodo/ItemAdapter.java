package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public interface OnLongClickListner
    {
        void onItemLongClicked(int iPosition);
    }
    public interface OnClickListner{
        void onItemClicked(int iPosition);
    }
    List<String> lstrItems;
    OnLongClickListner objOnLongClickListner;
    OnClickListner objOnClickListner;


    public ItemAdapter(List<String> lstrItems, OnLongClickListner onLongClickListner, OnClickListner onClickListner) {
        this.lstrItems = lstrItems;
        this.objOnLongClickListner = onLongClickListner;
        this.objOnClickListner = onClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String strItem = lstrItems.get(position);
        holder.bind(strItem);
    }

    @Override
    public int getItemCount() {
        return lstrItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String strItem) {
            tvItem.setText(strItem);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    objOnClickListner.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    objOnLongClickListner.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
