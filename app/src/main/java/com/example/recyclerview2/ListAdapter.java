package com.example.recyclerview2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.recyclerview2.R.drawable.item_background;
import static com.example.recyclerview2.R.drawable.item_background_selected;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListClass> listShoppingLists;
    private OnListItemCL onListCL;
    private OnListItemLongCL onListItemLongCL;

    ListAdapter(List<ListClass> listShoppingLists, OnListItemCL onListCL, OnListItemLongCL onListLongCL){
        this.listShoppingLists = listShoppingLists;
        this.onListCL = onListCL;
        this.onListItemLongCL = onListLongCL;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView listName;
        public CardView itemContainer;
        OnListItemCL onListCL;
        OnListItemLongCL onListItemLongCL;

        public ViewHolder(View itemView, OnListItemCL onListCL, OnListItemLongCL onListLongCL) {
            super(itemView);
            listName = itemView.findViewById(R.id.listName);
            itemContainer = itemView.findViewById(R.id.shoppingListContainer);
            this.onListCL = onListCL;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(onListCL != null && position != RecyclerView.NO_POSITION) {
                        onListCL.onListClick(listShoppingLists.get(position));
                    }
                }
            });
            this.onListItemLongCL = onListLongCL;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(onListLongCL != null && position != RecyclerView.NO_POSITION) {
                        onListLongCL.onLongItemClick(listShoppingLists.get(position));
                    }
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_card,parent,false);
        return new ViewHolder(view, onListCL, onListItemLongCL);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListClass shoppingList = listShoppingLists.get(position);
        holder.listName.setText(shoppingList.getName());
        if (shoppingList.isSelected()) holder.itemContainer.setBackgroundResource(item_background_selected);
        else holder.itemContainer.setBackgroundResource(item_background);
    }

    @Override
    public int getItemCount() {
        return listShoppingLists.size();
    }

    public void setLists(List<ListClass> lists){
        this.listShoppingLists = lists;
        notifyDataSetChanged();
    }

    public interface OnListItemCL {
        void onListClick(ListClass list);
    }

    public interface OnListItemLongCL {
        void onLongItemClick(ListClass list);
    }
}