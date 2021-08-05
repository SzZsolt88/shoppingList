package com.example.recyclerview2;

import android.graphics.Paint;
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

public class shoppingListAdapter extends RecyclerView.Adapter<shoppingListAdapter.ViewHolder> {
    private List<shoppingListProductClass> shoppingListProductClassList;
    private OnListCL onProductClickListener;

    shoppingListAdapter(List<shoppingListProductClass> productsList, OnListCL onProductClickListener){
        this.shoppingListProductClassList = productsList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,parent,false);
        return new ViewHolder(view, onProductClickListener);
    }

    @Override
    public int getItemCount() {
        return shoppingListProductClassList.size();
    }

    public void setProducts(List<shoppingListProductClass> product){
        this.shoppingListProductClassList = product;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView pName;
        public CheckBox checkBox;
        public LinearLayout itemContainer;
        OnListCL onProductClickListener;

        public ViewHolder(View itemView, OnListCL onProductClickListener) {
            super(itemView);
            pName = itemView.findViewById(R.id.itemName);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemContainer = itemView.findViewById(R.id.shoppingListItemContainer);
            this.onProductClickListener = onProductClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProductClickListener.onClick(getAbsoluteAdapterPosition());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final shoppingListProductClass shoppingListProductClass = shoppingListProductClassList.get(position);
        if (shoppingListProductClass.getQuantity().length() > 0) {
            holder.pName.setText(shoppingListProductClass.getName() + " - " + shoppingListProductClass.getQuantity() + " " + shoppingListProductClass.getQuantityType());
        }
        else {
            holder.pName.setText(shoppingListProductClass.getName());
        }

        if (shoppingListProductClass.isChecked()){
            holder.pName.setPaintFlags(holder.pName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else holder.pName.setPaintFlags(holder.pName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        if (shoppingListProductClass.isSelected()){
            holder.itemContainer.setBackgroundResource(item_background_selected);
        }
        else holder.itemContainer.setBackgroundResource(item_background);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(shoppingListProductClass.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!shoppingListProductClass.isChecked()) {
                    shoppingListProductClass.setChecked(true);
                    holder.pName.setPaintFlags(holder.pName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else{
                    shoppingListProductClass.setChecked(false);
                    holder.pName.setPaintFlags(holder.pName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
    }
}

