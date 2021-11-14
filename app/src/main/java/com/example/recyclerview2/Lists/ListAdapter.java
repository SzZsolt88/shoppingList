package com.example.recyclerview2.Lists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.R;
import com.example.recyclerview2.interfaces.OnListItemCL;

import java.util.List;

import static com.example.recyclerview2.R.drawable.item_background;
import static com.example.recyclerview2.R.drawable.item_background_selected;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.listViewHolder> {
    private List<ListClass> listShoppingLists;
    private OnListItemCL onListCL;

    ListAdapter(List<ListClass> listShoppingLists,  OnListItemCL onListCL){
        this.listShoppingLists = listShoppingLists;
        this.onListCL = onListCL;
    }

    public class listViewHolder extends RecyclerView.ViewHolder  {
        public CardView itemContainer;
        public TextView listName;

        public listViewHolder(View itemView, OnListItemCL onListCL) {
            super(itemView);
            listName = itemView.findViewById(R.id.listName);
            itemContainer = itemView.findViewById(R.id.shoppingListContainer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(onListCL != null && position != RecyclerView.NO_POSITION) {
                        onListCL.onListClick(listShoppingLists.get(position));
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(onListCL != null && position != RecyclerView.NO_POSITION) {
                        onListCL.onListLongClick(listShoppingLists.get(position));
                    }
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card,parent,false);
        return new listViewHolder(view, onListCL);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, int position) {
        final ListClass shoppingList = listShoppingLists.get(position);
        holder.listName.setText(shoppingList.getName());
        if (shoppingList.isSelected()) holder.itemContainer.setBackgroundResource(item_background_selected);
        else holder.itemContainer.setBackgroundResource(item_background);
    }

    @Override
    public int getItemCount() {
        return listShoppingLists.size();
    }

    public ListClass getItem(int position){
        return listShoppingLists.get(position);
    }

    public void setLists(List<ListClass> lists){
        this.listShoppingLists = lists;
        notifyDataSetChanged();
    }

}