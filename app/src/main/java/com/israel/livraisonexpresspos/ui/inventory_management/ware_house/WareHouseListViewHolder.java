package com.israel.livraisonexpresspos.ui.inventory_management.ware_house;

import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ItemWareHouseBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.Site;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

public class WareHouseListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemWareHouseBinding mBinding;
    private int itemPosition;
    private Site mSite;
    private StockInventoryActivity activity;
    private PopupMenu popupMenu;

    public WareHouseListViewHolder(@NonNull ItemWareHouseBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    public void bindViewHolder(Site site, int position) {
        this.activity = (StockInventoryActivity) itemView.getContext();
        this.itemPosition = position;
        this.mSite = site;
        this.mBinding.setSite(mSite);
        buildPopUpMenu();
        mBinding.btnMenu.setOnClickListener(this);
    }

    private void buildPopUpMenu() {
        popupMenu = new PopupMenu(itemView.getContext(), mBinding.btnMenu);
        String[] menuItems = new String[]{"Voir le details", "Modifier le site"};
        for (int i = 0; i < menuItems.length; i++) {
            popupMenu.getMenu().add(0, i, 0, menuItems[i]);
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == 0) {
                activity.onSiteItemDetailsIntent(itemPosition);
            }

            if (itemId == 2) {
                activity.onSiteItemEditIntent(itemPosition);
            }
            return true;
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_menu) {
            popupMenu.show();
        }
    }
}
