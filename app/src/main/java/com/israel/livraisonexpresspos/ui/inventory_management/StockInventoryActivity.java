package com.israel.livraisonexpresspos.ui.inventory_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ActivityStockInventoryBinding;
import com.israel.livraisonexpresspos.listeners.move_inventory_management.OnMoveFilterDialogEvent;
import com.israel.livraisonexpresspos.listeners.move_inventory_management.OnMoveItemPopUpMenuEvent;
import com.israel.livraisonexpresspos.listeners.move_inventory_management.OnMoveListFragmentToolbarMenuEvent;
import com.israel.livraisonexpresspos.listeners.move_inventory_management.OnSiteManagementEvent;
import com.israel.livraisonexpresspos.models.inventory_management_models.FilterShop;
import com.israel.livraisonexpresspos.ui.inventory_management.add_move.AddMoveFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.entry.EntryFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.exit.ExitFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.search.MovesFilterViewModel;
import com.israel.livraisonexpresspos.ui.inventory_management.search.StockSearchResultsFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.inventory.InventoryFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.move_details.MoveDetailsFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.returns.ReturnFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.ware_house.WareHouseFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.ware_house.details.WareHouseDetailsFragment;

public class StockInventoryActivity extends AppCompatActivity implements OnMoveItemPopUpMenuEvent, OnMoveListFragmentToolbarMenuEvent, OnMoveFilterDialogEvent, OnSiteManagementEvent {
    public static final String STR_ENTRY_MOVE_TYPE = "entree";
    public static final String STR_EXIT_MOVE_TYPE = "sortie";
    public static final String STR_RETURN_MOVE_TYPE = "Retour";
    public static final int SEARCH_FRAGMENT_INDEX = 0;
    public static final int ENTRY_FRAGMENT_INDEX = 1;
    public static final int EXIT_FRAGMENT_INDEX = 2;
    public static final int RETURN_FRAGMENT_INDEX = 3;
    public static final int WARE_HOUSE_FRAGMENT_INDEX = 4;
    public static final int WARE_HOUSE_DETAILS_FRAGMENT_INDEX = 9;
    public static final int INVENTORY_FRAGMENT_INDEX = 5;
    public static final int ADD_MOVE_FRAGMENT_INDEX = 6;
    public static final int SEARCH_MOVE_FRAGMENT_INDEX = 7;
    public static final int DETAILS_FRAGMENT_INDEX = 8;

    private MovesViewModel mMovesViewModel;
    private MovesFilterViewModel mFilterViewModel;
    private ActivityStockInventoryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_stock_inventory);
        initViewModel();
        initUi();
    }


    private void initViewModel() {
        mMovesViewModel = new ViewModelProvider(this).get(MovesViewModel.class);
        mFilterViewModel = new ViewModelProvider(this).get(MovesFilterViewModel.class);
    }

    private void initUi() {
        initBottomNavigation();
        replaceFragment(new WareHouseFragment());
        mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.WARE_HOUSE_FRAGMENT_INDEX);

        mMovesViewModel.loadEntryMoves();
        mMovesViewModel.loadExitMoves();
        mMovesViewModel.loadReturnMoves();
    }

    private void initBottomNavigation() {
        mBinding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.menu_search) {
//                mBinding.bottomNavigationView.setVisibility(View.GONE);
                if (mMovesViewModel.getCurrentFragmentIndex() != StockInventoryActivity.SEARCH_FRAGMENT_INDEX) {
                    replaceFragment(new StockSearchResultsFragment());
                    mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.SEARCH_FRAGMENT_INDEX);
                }
            }

            if (item.getItemId() == R.id.menu_moves) {
                /*todo  :display the bottom menu navigation */
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StockInventoryActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.stock_bottom_sheet,
                                (LinearLayoutCompat) findViewById(R.id.bottom_sheet_container));

                bottomSheetView.findViewById(R.id.bottom_sheet_close).setOnClickListener(view -> {
                    bottomSheetDialog.dismiss();
                });

                bottomSheetView.findViewById(R.id.ll_entries).setOnClickListener(view -> {
                    if (mMovesViewModel.getCurrentFragmentIndex() != StockInventoryActivity.ENTRY_FRAGMENT_INDEX) {
                        replaceFragment(new EntryFragment());
                        mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.ENTRY_FRAGMENT_INDEX);
                    }
                    bottomSheetDialog.dismiss();
                });

                bottomSheetView.findViewById(R.id.ll_outs).setOnClickListener(view -> {
                    if (mMovesViewModel.getCurrentFragmentIndex() != StockInventoryActivity.EXIT_FRAGMENT_INDEX) {
                        replaceFragment(new ExitFragment());
                        mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.EXIT_FRAGMENT_INDEX);
                    }
                    bottomSheetDialog.dismiss();
                });

                bottomSheetView.findViewById(R.id.ll_returns).setOnClickListener(view -> {
                    if (mMovesViewModel.getCurrentFragmentIndex() != StockInventoryActivity.RETURN_FRAGMENT_INDEX) {
                        replaceFragment(new ReturnFragment());
                        mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.RETURN_FRAGMENT_INDEX);
                    }
                    bottomSheetDialog.dismiss();
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }

            if (item.getItemId() == R.id.menu_ware_house) {
                if (mMovesViewModel.getCurrentFragmentIndex() != StockInventoryActivity.WARE_HOUSE_FRAGMENT_INDEX) {
                    replaceFragment(new WareHouseFragment());
                    mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.WARE_HOUSE_FRAGMENT_INDEX);
                }
            }

            if (item.getItemId() == R.id.menu_inventory) {
                if (mMovesViewModel.getCurrentFragmentIndex() != INVENTORY_FRAGMENT_INDEX){
                    replaceFragment(new InventoryFragment());
                    mMovesViewModel.setCurrentFragmentIndex(INVENTORY_FRAGMENT_INDEX);
                }
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    /* ================ move item popup menu Events */
    @Override
    public void onItemDelete(int itemId) {
        /*todo : fire the delete request from the VM the reset the rv accordingly*/
    }

    @Override
    public void onItemDetailsIntent(int itemId) {
        if (mMovesViewModel.getCurrentFragmentIndex() == StockInventoryActivity.ENTRY_FRAGMENT_INDEX) {
            mMovesViewModel.setCurrentViewedMove(mMovesViewModel.getEntryMoves().getValue().get(itemId));
            /*todo : fire the http request witch will retrieve the move details */
            mMovesViewModel.loadMoveDetails(mMovesViewModel.getEntryMoves().getValue().get(itemId).getId());
        }
        if (mMovesViewModel.getCurrentFragmentIndex() == StockInventoryActivity.EXIT_FRAGMENT_INDEX) {
            mMovesViewModel.setCurrentViewedMove(mMovesViewModel.getExitMoves().getValue().get(itemId));
        }
        if (mMovesViewModel.getCurrentFragmentIndex() == StockInventoryActivity.RETURN_FRAGMENT_INDEX) {
            mMovesViewModel.setCurrentViewedMove(mMovesViewModel.getReturnMoves().getValue().get(itemId));
        }
        mBinding.bottomNavigationView.setVisibility(View.GONE);
        replaceFragment(new MoveDetailsFragment());
        mMovesViewModel.setPreviousFragmentIndex(mMovesViewModel.getCurrentFragmentIndex());
        mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.DETAILS_FRAGMENT_INDEX);
    }

    @Override
    public void onCloseFullScreenFragment() {
        if (mMovesViewModel.getPreviousFragmentIndex() == StockInventoryActivity.ENTRY_FRAGMENT_INDEX) {
            replaceFragment(new EntryFragment());
            mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.ENTRY_FRAGMENT_INDEX);
        }
        if (mMovesViewModel.getPreviousFragmentIndex() == StockInventoryActivity.EXIT_FRAGMENT_INDEX) {
            replaceFragment(new ExitFragment());
            mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.EXIT_FRAGMENT_INDEX);
        }
        if (mMovesViewModel.getPreviousFragmentIndex() == StockInventoryActivity.RETURN_FRAGMENT_INDEX) {
            replaceFragment(new ReturnFragment());
            mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.RETURN_FRAGMENT_INDEX);
        }
        mMovesViewModel.setPreviousFragmentIndex(0);
        mBinding.bottomNavigationView.setVisibility(View.VISIBLE);
    }


    /* ============= move list fragment toolbar events*/
    @Override
    public void onAddMoveIntent(String moveType) {
        /*todo : display the add move fragment*/
        mBinding.bottomNavigationView.setVisibility(View.GONE);
        replaceFragment(new AddMoveFragment());
        mMovesViewModel.setPreviousFragmentIndex(mMovesViewModel.getCurrentFragmentIndex());
        mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.ADD_MOVE_FRAGMENT_INDEX);
    }

    @Override
    public void onSearchMoveIntent(String moveType) {
        /*todo : display the search move fragment*/
        mMovesViewModel.setPreviousFragmentIndex(mMovesViewModel.getCurrentFragmentIndex());
        mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.SEARCH_MOVE_FRAGMENT_INDEX);

    }


    /*=========== the move filter dialog events ===============*/
    @Override
    public void onFilterShopSelected(FilterShop filterShop) {
        /*todo : adjust filter constraint with the given shop data*/
        Log.e("filter shop", "onFilterShopSelected: "+ filterShop.toString());
    }


    /* ==================== site management events =================*/
    @Override
    public void onAddSiteIntent() {
        /*todo : display the add site dialog */
    }

    @Override
    public void onSiteItemDetailsIntent(long itemPosition) {
        replaceFragment(new WareHouseDetailsFragment());
        mMovesViewModel.setCurrentFragmentIndex(StockInventoryActivity.WARE_HOUSE_DETAILS_FRAGMENT_INDEX);
    }

    @Override
    public void onSiteItemEditIntent(long itemPosition) {
        /*todo : reuse the add site fragment but with prefilled with the given move details  */
    }
}