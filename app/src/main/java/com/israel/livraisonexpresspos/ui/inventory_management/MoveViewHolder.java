package com.israel.livraisonexpresspos.ui.inventory_management;

import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ItemMoveBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.Move;

public class MoveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemMoveBinding mBinding;
    private Move move;
    private int itemPosition;
    private PopupMenu popUpMenu;
    private StockInventoryActivity activity;

    public MoveViewHolder(@NonNull ItemMoveBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bindViewHolder(Move move, int position){
        this.move = move;
        this.itemPosition = position;
        mBinding.setMove(this.move);
        initPopUpMenu();
        toggleProductList();
        activity = (StockInventoryActivity) itemView.getContext();
        this.itemView.setOnClickListener(this);
        mBinding.btnMenu.setOnClickListener(this);
        mBinding.tvMoveDetailsLabel.setOnClickListener(this);
    }

    private void initPopUpMenu(){
        popUpMenu = new PopupMenu(itemView.getContext(),mBinding.btnMenu);
        String[] menuItems = new String []{"Voir le details",  "Supprimer le mouvement"};
        for (int i = 0; i < menuItems.length; i++) {
            popUpMenu.getMenu().add(0, i, 0, menuItems[i]);
        }
        popUpMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == 0){
                activity.onItemDetailsIntent(itemPosition);
            }

            if (itemId == 2){ /*todo : delete move process*/
                activity.onItemDelete(getAdapterPosition());
                }
            return true;
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.move_list_item){
            activity.onItemDetailsIntent(itemPosition);
        }

        if (viewId == R.id.btn_menu){
            popUpMenu.show();
        }

        /*todo : to remove */
//        if (viewId == R.id.move_details_label){
//            this.move.setIsExpanded(!this.move.isExpanded());
//            toggleProductList();
//        }
    }

    private void toggleProductList(){
        mBinding.rvProducts.setVisibility(this.move.isExpanded()? View.VISIBLE : View.GONE);
    }
}
