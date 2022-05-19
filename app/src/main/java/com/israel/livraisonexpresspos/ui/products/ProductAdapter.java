package com.israel.livraisonexpresspos.ui.products;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemProductBinding;
import com.israel.livraisonexpresspos.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private List<Product> mProducts;
    private List<Product> mProductList;
    private final Activity mActivity;

    public ProductAdapter(List<Product> products, Activity activity) {
        mProducts = products;
        mProductList = new ArrayList<>(products);
        mActivity = activity;
    }

    public void setProducts(List<Product> products){
        mProductList = new ArrayList<>(products);
        mProducts = products;
        notifyDataSetChanged();
    }

    public void addProducts(List<Product> products){
        mProducts.addAll(products);
        mProductList.addAll(products);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        ItemProductBinding binding = ItemProductBinding.inflate(inflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProducts.get(position);
        holder.bind(product, mActivity);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public Filter getFilter(){
        return mProductsFilter;
    }

    private final Filter mProductsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mProductList);
            }else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (Product p : mProductList){
                    if (p.getLibelle().toLowerCase().trim().contains(pattern)){
                        filteredList.add(p);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mProducts.clear();
            if (results.values == null)return;
            mProducts.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
