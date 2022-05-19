package com.israel.livraisonexpresspos.ui.inventory_management.move_details;

import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ItemMoveDetailsShopBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveDetailsShopProduct;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MoveDetailsShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemMoveDetailsShopBinding mBinding;
    private HashMap<String, Object> shopItem;
    private int itemPosition;
    private PopupMenu popUpMenu;
    private StockInventoryActivity activity;
    private MoveDetailsShopProductListAdapter shopProductAdapter;

    public MoveDetailsShopViewHolder(@NonNull ItemMoveDetailsShopBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bindViewHolder(HashMap<String, Object> shopItem, int position) {
        this.shopItem = shopItem;
        this.itemPosition = position;
        mBinding.setShopName(String.valueOf(shopItem.get("shop_name")));
        initPopUpMenu();
        activity = (StockInventoryActivity) itemView.getContext();
        mBinding.btnMenu.setOnClickListener(this);
        shopProductAdapter = new MoveDetailsShopProductListAdapter(activity);
        mBinding.rvProducts.setAdapter(shopProductAdapter);
        JSONObject json = new JSONObject(shopItem);
        try {
            shopProductAdapter.setProduct(new Gson().fromJson(String.valueOf(json.getJSONArray("products")), new TypeToken<List<MoveDetailsShopProduct>>(){}.getType()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_menu){
            popUpMenu.show();
        }
    }

    private void initPopUpMenu(){
        popUpMenu = new PopupMenu(itemView.getContext(),mBinding.btnMenu);
        String[] menuItems = new String []{"Supprimer le(s) produit(s) de ce magasin"};
        for (int i = 0; i < menuItems.length; i++) {
            popUpMenu.getMenu().add(0, i, 0, menuItems[i]);
        }
        popUpMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == 0){
                /*todo : add this call back function*/
//                activity.onMoveProductItemDelete(getAdapterPosition());
            }
            return true;
        });
    }
}
