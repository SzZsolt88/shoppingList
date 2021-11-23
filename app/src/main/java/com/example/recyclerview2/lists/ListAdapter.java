package com.example.recyclerview2.lists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.R;
import com.example.recyclerview2.appDataBase.ListClass;

import java.util.ArrayList;
import java.util.List;

import static com.example.recyclerview2.R.drawable.item_background;
import static com.example.recyclerview2.R.drawable.item_background_selected;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.listViewHolder> {
    private List<ListClass> listShoppingLists;
    private OnListItemCL onListCL;
    private String currentUserMail;


    ListAdapter(OnListItemCL onListCL, String currentUserMail){
        listShoppingLists = new ArrayList<>();
        this.onListCL = onListCL;
        this.currentUserMail = currentUserMail;
    }

    public class listViewHolder extends RecyclerView.ViewHolder  {
        public CardView itemContainer;
        public TextView listName;
        public TextView ownerName;
        public TextView membersList;

        public listViewHolder(View itemView, OnListItemCL onListCL) {
            super(itemView);
            itemContainer = itemView.findViewById(R.id.shoppingListContainer);
            listName = itemView.findViewById(R.id.listName);
            ownerName = itemView.findViewById(R.id.ownerName);
            membersList = itemView.findViewById(R.id.membersList);
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
        holder.listName.setText(shoppingList.getListName());
        if (shoppingList.isShared()) {
            String ownerString = "";
            String listMembersString = "Tagok: ";
            for (int i = 0; i < shoppingList.getSharedWith().size(); i++) {
                if (i < shoppingList.getSharedWith().size()-1) {
                    listMembersString += shoppingList.getSharedWith().get(i).getContactUserName() + ", ";
                } else listMembersString += shoppingList.getSharedWith().get(i).getContactUserName();
            }
            if (shoppingList.getOwner().equals(currentUserMail)) {
               ownerString += "Saját lista";
            } else ownerString += "Tulaj: " + shoppingList.getOwnerName();
            holder.ownerName.setText(ownerString);
            holder.ownerName.setVisibility(View.VISIBLE);
            holder.membersList.setText(listMembersString);
            holder.membersList.setVisibility(View.VISIBLE);
        } else {
            holder.ownerName.setVisibility(View.GONE);
            holder.membersList.setVisibility(View.GONE);
        }
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

    public void setLists(List<ListClass> lists) {
        this.listShoppingLists = lists;
        notifyDataSetChanged();
    }

}