package com.example.recyclerview2;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.recyclerview2.R.drawable.item_background;
import static com.example.recyclerview2.R.drawable.item_background_selected;

public class listsAdapter extends RecyclerView.Adapter<listsAdapter.ViewHolder> {
    private List<listsShoppingListClass> listShoppingLists;
    private OnListItemCL onListCL;
    private OnListItemLongCL onListItemLongCL;

    listsAdapter(List<listsShoppingListClass> listShoppingLists, OnListItemCL onListCL, OnListItemLongCL onListLongCL){
        this.listShoppingLists = listShoppingLists;
        this.onListCL = onListCL;
        this.onListItemLongCL = onListLongCL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,parent,false);
        return new ViewHolder(view, onListCL, onListItemLongCL);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final listsShoppingListClass shoppingList = listShoppingLists.get(position);
        holder.listName.setText(shoppingList.getName());
        if (shoppingList.isSelected()){
            holder.itemContainer.setBackgroundResource(item_background_selected);
        }
        else holder.itemContainer.setBackgroundResource(item_background);

        if (shoppingList.isChecked()){
            holder.listName.setPaintFlags(holder.listName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else holder.listName.setPaintFlags(holder.listName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(shoppingList.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!shoppingList.isChecked()) {
                    shoppingList.setChecked(true);
                    holder.listName.setPaintFlags(holder.listName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else{
                    shoppingList.setChecked(false);
                    holder.listName.setPaintFlags(holder.listName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listShoppingLists.size();
    }

    public void setLists(List<listsShoppingListClass> lists){
        this.listShoppingLists = lists;
        notifyDataSetChanged();
    }

    public listsShoppingListClass getNoteAt(int position){
        return listShoppingLists.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView listName;
        public CheckBox checkBox;
        public LinearLayout itemContainer;
        OnListItemCL onListCL;
        OnListItemLongCL onListItemLongCL;

        public ViewHolder(View itemView, OnListItemCL onListCL, OnListItemLongCL onListLongCL) {
            super(itemView);
            listName = itemView.findViewById(R.id.itemName);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemContainer = itemView.findViewById(R.id.shoppingListItemContainer);
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

    public interface OnListItemCL {
        void onListClick(listsShoppingListClass list);
    }

    public interface OnListItemLongCL {
        void onLongItemClick(listsShoppingListClass list);
    }
}
