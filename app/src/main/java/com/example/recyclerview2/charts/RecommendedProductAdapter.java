package com.example.recyclerview2.charts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview2.R;

import java.util.ArrayList;
import java.util.List;

public class RecommendedProductAdapter extends RecyclerView.Adapter<RecommendedProductAdapter.recommendedProductViewHolder> {
    private List<String> recommendedProductsList;

    RecommendedProductAdapter() {
        recommendedProductsList = new ArrayList<>();
    }

    public static class recommendedProductViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout recommendedProductContainer;
        private final TextView recommendedProductName;

        public recommendedProductViewHolder(View itemView) {
            super(itemView);
            recommendedProductContainer = itemView.findViewById(R.id.recommendedProductContainer);
            recommendedProductName = itemView.findViewById(R.id.recommendedProductName);
        }
    }

    @NonNull
    @Override
    public recommendedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_product_card,parent,false);
        return new recommendedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recommendedProductViewHolder holder, int position) {
        final String recommendedProduct = recommendedProductsList.get(position);
        holder.recommendedProductContainer.setBackgroundResource(R.drawable.item_background);
        holder.recommendedProductName.setText(recommendedProduct);
    }

    @Override
    public int getItemCount() {
        return recommendedProductsList.size();
    }


    public void setRecommendedProductsList(List<String> recommendedProducts) {
        this.recommendedProductsList = recommendedProducts;
        this.notifyDataSetChanged();
    }
}
