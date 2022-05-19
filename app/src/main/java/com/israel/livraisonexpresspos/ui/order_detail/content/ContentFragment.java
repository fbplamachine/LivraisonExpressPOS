package com.israel.livraisonexpresspos.ui.order_detail.content;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.DialogAddProductToCartBinding;
import com.israel.livraisonexpresspos.databinding.DialogCartItemListBinding;
import com.israel.livraisonexpresspos.databinding.DialogConfirmOrderCancelationBinding;
import com.israel.livraisonexpresspos.databinding.DialogRelaunchBinding;
import com.israel.livraisonexpresspos.databinding.FragmentContenuDetailsBinding;
import com.israel.livraisonexpresspos.databinding.FragmentEditCartItemPriceBinding;
import com.israel.livraisonexpresspos.listeners.OnPerformAction;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.cart.OnCartItemChangeListener;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men.DeliveryMenDialogFragment;
import com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men.OnDeliveryMenSelectedListener;
import com.israel.livraisonexpresspos.ui.order_detail.content.edit_cart.CartItemAdapter;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.utils.CourseStatus;
import com.israel.livraisonexpresspos.utils.DialogUtils;

import java.util.HashMap;
import java.util.Map;

public class ContentFragment extends Fragment implements PopupMenu.OnMenuItemClickListener,
        OnPerformAction, View.OnClickListener, OnDeliveryMenSelectedListener, OnCartItemChangeListener {
    public static final String CART_LIST = "cartList";
    private FragmentContenuDetailsBinding mBinding;
    private OrderSteed mOrderSteed;
    private PopupMenu mPopupMenuSteed;
    private PopupMenu mPopupMenu;
    private ContentViewModel mViewModel;
    private ProgressDialog mDialog;
    private String mReason, mRelaunchReason, mRelaunchDate;
    private AlertDialog mCancelOrderDialog;
    private AlertDialog mRelaunchDialog;
    private AlertDialog mDialogAddToCart;
    private String mNewPrice;
    private AlertDialog mDialogCartItemList;
    private Cart mCart;
    private AlertDialog mDialogEditDeliveryPrice;
    private DialogRelaunchBinding mRelaunchBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contenu_details, container, false);
        mViewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        mOrderSteed = ((OrderDetailActivity) requireActivity()).getOrderSteed();
        initUI();
        stream();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
        mPopupMenu = null;
    }

    private void initUI() {
        initCancellationDialog();
        initRelaunchDialog();
        initAddToCartDialog();
        initCartListDialog();
        initEditDeliveryPriceDialog();
        mDialog = Utilities.getProgressDialog(requireContext());
        mBinding.setOrder(mOrderSteed);
        handleDeliveryManActions();
        initStatusMenu();
        initManagerActions();
        mBinding.cardViewSelectCourseStatus.setOnClickListener(this);
        mBinding.ivAdd.setOnClickListener(this);
        mBinding.ivEdit.setOnClickListener(this);
        mBinding.ivEditTime.setOnClickListener(this);
        mBinding.ivEditDeliveryPrice.setOnClickListener(this);
        toggleDeliveryManName();
    }

    private void initRelaunchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mRelaunchBinding = DialogRelaunchBinding.inflate(inflater);
        builder.setView(mRelaunchBinding.getRoot());
        mRelaunchDialog = builder.create();
        mRelaunchBinding.rgMessage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = mRelaunchDialog.findViewById(checkedId);
                if (button == null)return;
                mRelaunchReason = null;
                if (button.getId() == mRelaunchBinding.rbOther.getId()){
                    mRelaunchBinding.tilReason.setVisibility(View.VISIBLE);
                }else {
                    mRelaunchReason = button.getText().toString();
                    mRelaunchBinding.tilReason.setVisibility(View.GONE);
                }
            }
        });
        mRelaunchBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.showDatePicker();
            }
        });
        mRelaunchBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRelaunchDialog.dismiss();
            }
        });

        mRelaunchBinding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRelaunchBinding.rgMessage.getCheckedRadioButtonId() == mRelaunchBinding.rbOther.getId()){
                    if (mRelaunchBinding.etReason.getText() == null || mRelaunchBinding.etReason.getText().toString().length() < 1){
                        mRelaunchBinding.tilReason.setError("Veuillez remplir ce champ");
                    }else {
                        mRelaunchBinding.tilReason.setError(null);
                        mRelaunchReason = mRelaunchBinding.etReason.getText().toString();
                    }
                }
                if (mRelaunchReason != null && mRelaunchDate != null){
                    mRelaunchDialog.dismiss();
                    Map<String, Object> map = new HashMap<>();
                    map.put("comment", mRelaunchReason);
                    map.put("date", mRelaunchDate);
                    mViewModel.managerStatusChange(mOrderSteed.getInfos().getId(),
                            "relaunch", map);
                }
            }
        });
    }

    private void initCancellationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final DialogConfirmOrderCancelationBinding binding = DialogConfirmOrderCancelationBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        mCancelOrderDialog = builder.create();
        binding.rgMessage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = mCancelOrderDialog.findViewById(checkedId);
                if (button == null)return;
                mReason = null;
                if (button.getId() == binding.rbOther.getId()){
                    binding.tilReason.setVisibility(View.VISIBLE);
                }else {
                    mReason = button.getText().toString();
                    binding.tilReason.setVisibility(View.GONE);
                }
            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancelOrderDialog.dismiss();
            }
        });
        binding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.rgMessage.getCheckedRadioButtonId() == binding.rbOther.getId()){
                    if (binding.etReason.getText() == null || binding.etReason.getText().toString().length() < 1){
                        binding.tilReason.setError("Veuillez remplir ce champ");
                    }else {
                        binding.tilReason.setError(null);
                        mReason = binding.etReason.getText().toString();
                    }
                }
                if (mReason != null){
                    mCancelOrderDialog.dismiss();
                    Map<String, Object> map = new HashMap<>();
                    map.put("reason", mReason);
                    mViewModel.managerStatusChange(mOrderSteed.getInfos().getId(),
                            "canceled", map);
                }
            }
        });
    }

    private void initAddToCartDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final DialogAddProductToCartBinding binding = DialogAddProductToCartBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        mDialogAddToCart = builder.create();
        if (mCart != null){
            binding.etName.setText(mCart.getLibelle());
            binding.etPrice.setText(String.valueOf(mCart.getPrix_unitaire()));
            binding.etQuantity.setText(String.valueOf(mCart.getQuantite()));
        }
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.etName.getText()) && !TextUtils.isEmpty(binding.etPrice.getText())){
                    String name = binding.etName.getText().toString();
                    String price = binding.etPrice.getText().toString();
                    String quantity = binding.etQuantity.getText().toString();
                    Cart cart = new Cart();
                    if (mCart != null) cart.setId(mCart.getId());
                    int shopId;
                    try {
                        shopId = mOrderSteed.getOrders().getliste_articles().get(0).getMagasin_id();
                    }catch (Exception e){
                        shopId = 0;
                    }
                    cart.setMagasin_id(shopId);
                    cart.setLibelle(name);
                    cart.setPrix_unitaire(Integer.parseInt(price));
                    cart.setMontant_soustotal(price);
                    cart.setProduct_id(0);
                    cart.setMontant_total(price);
                    cart.setQuantite(TextUtils.isEmpty(quantity) ? 1 : Integer.parseInt(quantity));
                    mViewModel.upsertProduct(mOrderSteed.getInfos().getId(), cart);
                    Utilities.hideKeyBoard(requireActivity());
                }
            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogAddToCart.dismiss();
            }
        });
    }

    private void initCartListDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        if (mOrderSteed == null){
            builder.setIcon(R.drawable.ic_round_warning_24)
                    .setTitle(getString(R.string.warning))
                    .setMessage("Un problème est survenu. Vous allez être renvoyer à la page précédente")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        requireActivity().finish();
                    });
            builder.create().show();
        }else {
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            DialogCartItemListBinding binding = DialogCartItemListBinding.inflate(inflater);
            builder.setView(binding.getRoot());
            mDialogCartItemList = builder.create();
            binding.rvCartItem.setLayoutManager(new LinearLayoutManager(requireActivity()));
            CartItemAdapter adapter = new CartItemAdapter(mOrderSteed.getOrders().getliste_articles(), this);
            binding.rvCartItem.setAdapter(adapter);
            binding.ivClose.setOnClickListener(v -> mDialogCartItemList.dismiss());
        }
    }

    private void initEditDeliveryPriceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final FragmentEditCartItemPriceBinding binding = FragmentEditCartItemPriceBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        mDialogEditDeliveryPrice = builder.create();
        binding.tvTitle.setText("Entrez le nouveau montant de livraison");
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogEditDeliveryPrice.dismiss();
            }
        });
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etPrice.getText() == null)return;
                if (!TextUtils.isEmpty(binding.etPrice.getText())){
                    mViewModel.updateDeliveryPrice(mOrderSteed.getInfos().getId()
                            , Integer.parseInt(binding.etPrice.getText().toString()));
                }
            }
        });
    }

    private void handleDeliveryManActions(){
        int status = mOrderSteed.getInfos().getStatut();
        if (!User.isDeliveryMan()
                || status == CourseStatus.CODE_CANCELED
                || status == CourseStatus.CODE_DELIVERED
                || (status != CourseStatus.CODE_UNASSIGNED
                && !mOrderSteed.getInfos().getCoursiers_ids().contains(User.getCurrentUser(requireContext()).getId()))){
            mBinding.cardViewSelectCourseStatus.setVisibility(View.GONE);
        }else {
            mBinding.cardViewSelectCourseStatus.setVisibility(View.VISIBLE);
        }
    }

    private void toggleDeliveryManName(){
        boolean show = (User.isManager() && mOrderSteed.getInfos().getStatut() > CourseStatus.CODE_UNASSIGNED
                && !TextUtils.isEmpty(mOrderSteed.getInfos().getCoursier_name()));
        mBinding.layoutDeliveryMan.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void stream(){
        mViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == null)return;
                if(integer == 1){
                    mOrderSteed.getInfos().getCoursiers_ids().clear();
                    mOrderSteed.getInfos().getCoursiers_ids().add(User.getCurrentUser(requireContext()).getId());
                }
                mOrderSteed.getInfos().setStatut(integer);
                initStatusMenu();
                handleDeliveryManActions();
            }
        });

        mViewModel.getStatusHuman().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null)return;
                mBinding.tvCourseStatus.setText(getString(R.string.order_status_hint).concat(" ").concat(s));
                mOrderSteed.getInfos().setStatut_human(s);
            }
        });

        mViewModel.getLoad().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                if (aBoolean) {
                    if(!((OrderDetailActivity) requireContext()).isFinishing())mDialog.show();
                } else {
                    mDialog.dismiss();
                }
            }
        });

        mViewModel.getTime().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null)return;
                if (mRelaunchDialog != null && mRelaunchDialog.isShowing()){
                    mRelaunchBinding.tvDate.setText(s);
                    mRelaunchDate = s;
                } else {
                    mViewModel.updateDeliveryTime(requireContext()
                            , mOrderSteed.getInfos().getId(), s, mBinding.tvCourseDate);
                }
            }
        });

        mViewModel.getDeliveryDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null)return;
                mBinding.tvCourseDate.setText(s);
            }
        });

        mViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                mCart = null;
                if (cart == null)return;
                if (cart.isDeleted()){
                    int indexToDelete = 0;
                    for(int i = 0; i < mOrderSteed.getOrders().getliste_articles().size(); i++){
                        Cart item = mOrderSteed.getOrders().getliste_articles().get(i);
                        if (item.getId() == cart.getId()){
                            indexToDelete = i;
                            break;
                        }
                    }
                    mOrderSteed.getOrders().getliste_articles().remove(indexToDelete);
                }else {
                    if (cart.isNewProduct()){
                        mOrderSteed.getOrders().getliste_articles().add(cart);
                    }else {
                        for (Cart c : mOrderSteed.getOrders().getliste_articles()){
                            if (c.getId() == cart.getId()){
                                c.setLibelle(cart.getLibelle());
                                c.setPrix_unitaire(cart.getPrix_unitaire());
                                c.setMontant_total(cart.getMontant_total());
                                c.setMontant_soustotal(cart.getMontant_soustotal());
                                c.setQuantite(cart.getQuantite());
                                break;
                            }
                        }
                    }
                }
                mDialogAddToCart.dismiss();
                mDialogCartItemList.dismiss();
                buildDescription();
            }
        });
         mViewModel.getDeliveryCost().observe(getViewLifecycleOwner(), new Observer<Integer>() {
             @Override
             public void onChanged(Integer integer) {
                 if(integer == null)return;
                 mOrderSteed.getOrders().setMontant_livraison(integer);
                 mOrderSteed.getPaiement().setMontant_total(mOrderSteed.getOrders().getMontant_achat() + integer);
                 mBinding.setOrder(mOrderSteed);
                 mDialogEditDeliveryPrice.dismiss();
             }
         });
    }

    private void buildDescription(){
        StringBuilder description = new StringBuilder();
        int totalPrice = 0;
        for (Cart c : mOrderSteed.getOrders().getliste_articles()){
            totalPrice += c.getQuantite() * c.getMontantTotal();
            description.append("\u2022")
                    .append(c.getLibelle()).append(" : ")
                    .append(c.getMontant_total())
                    .append(" x ".concat(String.valueOf(c.getQuantite())))
                    .append(" = ".concat(String.valueOf(c.getQuantite() * c.getMontantTotal())))
                    .append(" FCFA").append("\n");
        }
        mNewPrice = String.valueOf(totalPrice).concat(" FCFA");
        mOrderSteed.getOrders().setMontant_achat(totalPrice);
        mOrderSteed.getPaiement().setMontant_total(totalPrice + mOrderSteed.getOrders().getMontant_livraison());
        mOrderSteed.getInfos().setContenu(description.toString());
        mBinding.setOrder(mOrderSteed);
    }

    private void initStatusMenu() {
        if (!User.isDeliveryMan())return;
        mPopupMenuSteed = new PopupMenu(requireContext(), mBinding.cardViewSelectCourseStatus);
        mPopupMenuSteed.inflate(R.menu.edit_race_status);

        Menu menu = mPopupMenuSteed.getMenu();
        switch (mOrderSteed.getInfos().getStatut()){
            case CourseStatus
                    .CODE_ASSIGNED:
                menu.findItem(R.id.demarrer).setVisible(true);
                break;
            case CourseStatus.CODE_STARTED:
                menu.findItem(R.id.charger).setVisible(true);
                break;
            case CourseStatus.CODE_INPROGRESS:
                menu.findItem(R.id.livrer).setVisible(true);
                break;
            case CourseStatus.CODE_UNASSIGNED:
                menu.findItem(R.id.refuser).setVisible(false);
                menu.findItem(R.id.relaunch).setVisible(false);
                if (User.isDeliveryMan()){
                    menu.findItem(R.id.prendre).setVisible(true);
                }
            case CourseStatus.CODE_CANCELED:
                menu.findItem(R.id.refuser).setVisible(false);
                menu.findItem(R.id.relaunch).setVisible(false);
            break;
        }
        mPopupMenuSteed.setOnMenuItemClickListener(this);
    }

    private void initManagerActions(){
        if (!User.isManager())return;
        mBinding.ivEditTime.setVisibility(View.VISIBLE);
        mBinding.ivEditDeliveryPrice.setVisibility(View.VISIBLE);
        mBinding.ivAdd.setVisibility(View.VISIBLE);
        mBinding.ivEdit.setVisibility(View.VISIBLE);
        mBinding.ivMore.setVisibility(View.VISIBLE);
        mViewModel.handleShippingTime(requireContext());
        mBinding.ivMore.setOnClickListener(this);
        mPopupMenu = new PopupMenu(requireContext(), mBinding.ivMore);
        mPopupMenu.inflate(R.menu.manager_order_menu);
        mPopupMenu.setOnMenuItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.cardViewSelectCourseStatus.getId()){
            mPopupMenuSteed.show();
        }else if (id == mBinding.ivMore.getId()){
            mPopupMenu.show();
        }else if (id == mBinding.ivAdd.getId()){
            mDialogAddToCart.show();
        }else if (id == mBinding.ivEditTime.getId()){
            mViewModel.showDatePicker();
        }else if (id == mBinding.ivEdit.getId()){
            mDialogCartItemList.show();
        }else if (id == mBinding.ivEditDeliveryPrice.getId()){
            mDialogEditDeliveryPrice.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.prendre){
            mViewModel.updateStatus(mOrderSteed, "Course assigné avec succès"
                    , "assign", CourseStatus.CODE_ASSIGNED, "Assignée");
            return true;
        }else if (id == R.id.demarrer){
            mViewModel.updateStatus(mOrderSteed, "Course démarré avec succès"
                    , "started", CourseStatus.CODE_STARTED, "Démarrée");
            return true;
        }else if (id == R.id.charger){
            mViewModel.updateStatus(mOrderSteed, "Chargement effectué", "picked-up"
                    , CourseStatus.CODE_INPROGRESS, "En cours de livraison");
            return true;
        }else if (id == R.id.livrer){
            if (receiverAddressIsMissingData()){
                DialogUtils.showAlertDialog(requireActivity(),this,R.drawable.ic_round_warning_24,"Attention !!", "Vous êtes sur le point terminer cette course alors qu'il maque des informations dans l'adresse du receiver, voulez vous continuer quand-même ?","Continuer","Annuler",DialogUtils.INFO_FOR_DELIVERING_ORDER,null);
            }else {
                mViewModel.updateStatus(mOrderSteed, "Course terminée avec succès"
                        , "delivered", CourseStatus.CODE_DELIVERED, "Terminée");
            }
            return true;
        }else if (id == R.id.refuser){
            DialogUtils.showAlertDialog(requireActivity(), this, R.drawable.ic_round_warning_24
                    , "Attention!", "Les courses rejetées seront remises parmi les courses non assignées et pourrons faire l'objet de nouvelles assignations. voulez-vous rejeter celle-ci ?"
                    , "Oui", "Non", DialogUtils.WARNING_FOR_REJECTING_ORDER, null);
            return true;
        }else if (id == R.id.relaunch){
            DialogUtils.showAlertDialog(requireActivity(), this, R.drawable.ic_round_warning_24
                    , "Attention!", "Les courses reprogrammées seront remises parmi les courses non assignées et pourrons faire l'objet de nouvelles assignations. voulez-vous reprogrammer celle-ci ?"
                    , "Oui", "Non", DialogUtils.WARNING_FOR_RELAUNCHING_ORDER, null);
            return true;
        }else if (id == R.id.manager_unAssign){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                    .setIcon(R.drawable.ic_round_warning_24)
                    .setTitle(getString(R.string.warning))
                    .setMessage("Voulez vous désassigner cette course?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mViewModel.managerStatusChange(mOrderSteed.getInfos().getId(),
                                    "reset", new HashMap<>());
                        }
                    })
                    .setNegativeButton("Annuler", null);
            builder.create().show();
        }else if (id == R.id.manager_assign){
            DeliveryMenDialogFragment dialogFragment = new DeliveryMenDialogFragment();
            dialogFragment.show(requireActivity().getSupportFragmentManager()
                    , DeliveryMenDialogFragment.class.getSimpleName());
        }else if (id == R.id.manager_deliver){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                    .setIcon(R.drawable.ic_round_warning_24)
                    .setTitle(getString(R.string.warning))
                    .setMessage("Voulez vous terminer cette course?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mViewModel.managerStatusChange(mOrderSteed.getInfos().getId(),
                                    "delivered", new HashMap<>());
                        }
                    })
                    .setNegativeButton("Annuler", null);
            builder.create().show();
        }else if (id == R.id.manager_to_validate){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                    .setIcon(R.drawable.ic_round_warning_24)
                    .setTitle(getString(R.string.warning))
                    .setMessage("Voulez vous renvoyer cette course au statut A VALIDER?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mViewModel.managerStatusChange(mOrderSteed.getInfos().getId(),
                                    "to_validate", new HashMap<>());
                        }
                    })
                    .setNegativeButton("Annuler", null);
            builder.create().show();
        }else if (id == R.id.manager_cancel){
            mCancelOrderDialog.show();
        }else if (id == R.id.manager_relaunch){
            mRelaunchDialog.show();
            mViewModel.initTime();
        }
        return false;
    }

    private boolean receiverAddressIsMissingData() {
        Address address = ((OrderDetailActivity) requireActivity()).getOrderSteed().getReceiver().getAdresses().get(0);
        if (address.getTitre() == null || address.getDescription() == null) return true;
        if((address.getLat() > 0 && address.getLon() > 0) && (address.getTitre().trim().length() > 0) && address.getDescription().trim().length() > 0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void performAction(String role, String motif, @Nullable String date, @Nullable String time) {
        if (role.equals(DialogUtils.WARNING_FOR_REJECTING_ORDER)) {
            mViewModel.rejectOrder(motif, mOrderSteed.getInfos().getId(), getActivity());
        } else if (role.equals(DialogUtils.WARNING_FOR_RELAUNCHING_ORDER)) {
            Map<String,Object> map = new HashMap<>();
            StringBuilder builder = new StringBuilder();
            builder.append(date).append(" ").append(time);
            map.put("date", builder.toString());
            map.put("comment", motif);
            Log.e("le map" , map.toString());
            mViewModel.relaunchOrder(mOrderSteed.getInfos().getId(),map, getActivity());
        }else if (role.equals(DialogUtils.INFO_FOR_DELIVERING_ORDER)){
            // todo move it to some where else (in the callback)
            mViewModel.updateStatus(mOrderSteed, "Course terminée avec succès"
                    , "delivered", CourseStatus.CODE_DELIVERED, "Terminée");
        }
    }


    @Override
    public void onDeliveryMenSelected(User user) {
        if (user == null)return;
        mViewModel.assignOrder(requireContext(), mOrderSteed.getInfos().getId(), user.getId());
    }

    @Override
    public void onDelete(Cart cart) {
        mViewModel.deleteProductFromCart(mOrderSteed.getInfos().getId(), cart);
    }

    @Override
    public void onChange(Cart cart) {
        mCart = cart;
        initAddToCartDialog();
        mDialogAddToCart.show();
    }
}
