package com.example.recyclerview2.products;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.appDataBase.ProductClass;
import com.example.recyclerview2.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.recyclerview2.R.drawable.item_background;
import static com.example.recyclerview2.R.drawable.item_background_selected;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.productViewHolder> {
    private List<ProductClass> productList;
    private final OnProductItemCL onProductClickListener;


    ProductAdapter(OnProductItemCL onProductClickListener){
        productList = new ArrayList<>();
        this.onProductClickListener = onProductClickListener;

    }

    public class productViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout itemContainer;
        private final ImageView productCategoryIcon;
        private final TextView pName;
        private final CheckBox checkBox;

        public productViewHolder(View itemView, OnProductItemCL onProductItemCL) {
            super(itemView);
            productCategoryIcon = itemView.findViewById(R.id.productCategoryIcon);
            pName = itemView.findViewById(R.id.itemName);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemContainer = itemView.findViewById(R.id.shoppingListItemContainer);
            productCategoryIcon.setImageResource(R.drawable.ic_dairy_products);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(onProductClickListener != null && position != RecyclerView.NO_POSITION) {
                        onProductClickListener.onProductClick(productList.get(position));
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
        final ProductClass productClass = productList.get(position);

        switch (productClass.getProductCategory()) {
            case "Gyümölcs és Zöldség":
                holder.productCategoryIcon.setImageResource(R.drawable.ic_fruit_and_veg);
                break;
            case "Pékáru":
                holder.productCategoryIcon.setImageResource(R.drawable.ic_bakery);
                break;
            case "Ital":
                holder.productCategoryIcon.setImageResource(R.drawable.ic_beverage);
                break;
            case "Tejtermék":
                holder.productCategoryIcon.setImageResource(R.drawable.ic_dairy_products);
                break;
            case "Hús":
                holder.productCategoryIcon.setImageResource(R.drawable.ic_meat_category_icon);
                break;
            default:
                holder.productCategoryIcon.setImageResource(R.drawable.ic_unknown_category_icon);
        }

        if (productClass.getQuantity().length() > 0) holder.pName.setText(productClass.getName() + " - " + productClass.getQuantity() + " " + productClass.getQuantityType());
        else holder.pName.setText(productClass.getName());

        if (productClass.isChecked()) holder.pName.setPaintFlags(holder.pName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else holder.pName.setPaintFlags(holder.pName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        if (productClass.isSelected()) holder.itemContainer.setBackgroundResource(item_background_selected);
        else holder.itemContainer.setBackgroundResource(item_background);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(productClass.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onProductClickListener.saveCheckedStatus(productClass);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public ProductClass getItem(int position){   return productList.get(position);    }

    public void setProducts(List<ProductClass> product){
        this.productList = product;
        this.notifyDataSetChanged();
    }

    private boolean categorizeProduct(String[] category, ProductClass product) {
        for (String s : category) {
            if (product.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }
}

