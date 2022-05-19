package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemMovesByShopProductBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveByShopProduct;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

import java.util.ArrayList;
import java.util.List;

public class MoveByShopProductListAdapter extends RecyclerView.Adapter<MoveByShopProductViewHolder> {
    private List<MoveByShopProduct> productList;
    private StockInventoryActivity activity;

    public MoveByShopProductListAdapter(StockInventoryActivity activity) {
        this.activity = activity;
        productList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MoveByShopProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMovesByShopProductBinding binding = ItemMovesByShopProductBinding.inflate(inflater, parent, false);
        return new MoveByShopProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveByShopProductViewHolder holder, int position) {
        holder.bindViewHolder(productList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductList(List<MoveByShopProduct> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
}
