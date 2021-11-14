package com.example.recyclerview2.products;

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

import com.example.recyclerview2.appDataBase.ListClass;
import com.example.recyclerview2.appDataBase.ProductClass;
import com.example.recyclerview2.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.recyclerview2.R.drawable.item_background;
import static com.example.recyclerview2.R.drawable.item_background_selected;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.productViewHolder> {
    private List<ProductClass> shoppingListProductClassList;
    private OnProductItemCL onProductClickListener;

    ProductAdapter(OnProductItemCL onProductClickListener){
        shoppingListProductClassList = new ArrayList<>();
        this.onProductClickListener = onProductClickListener;
    }

    public class productViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemContainer;
        private TextView pName;
        private CheckBox checkBox;

        public productViewHolder(View itemView, OnProductItemCL onProductItemCL) {
            super(itemView);
            pName = itemView.findViewById(R.id.itemName);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemContainer = itemView.findViewById(R.id.shoppingListItemContainer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(onProductClickListener != null && position != RecyclerView.NO_POSITION) {
                        onProductClickListener.onProductClick(shoppingListProductClassList.get(position));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card,parent,false);
        return new productViewHolder(view, onProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, int position) {
        final ProductClass ProductClass = shoppingListProductClassList.get(position);

        if (ProductClass.getQuantity() > 0) holder.pName.setText(ProductClass.getName() + " - " + ProductClass.getQuantity() + " " + ProductClass.getQuantityType());
        else holder.pName.setText(ProductClass.getName());

        if (ProductClass.isChecked()) holder.pName.setPaintFlags(holder.pName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else holder.pName.setPaintFlags(holder.pName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        if (ProductClass.isSelected()) holder.itemContainer.setBackgroundResource(item_background_selected);
        else holder.itemContainer.setBackgroundResource(item_background);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(ProductClass.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ProductClass.setChecked(!ProductClass.isChecked());
                onProductClickListener.saveCheckedStatus(ProductClass);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingListProductClassList.size();
    }

    public ProductClass getItem(int position){   return shoppingListProductClassList.get(position);
    }

    public void setProducts(List<ProductClass> product){
        this.shoppingListProductClassList = product;
        this.notifyDataSetChanged();
    }
}

