package com.israel.livraisonexpresspos.ui.place_order;

import android.app.Application;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.data.Room.repository.ContactRemoteRepository;
import com.israel.livraisonexpresspos.databinding.ItemOrderBinding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Module;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.OrderWithCarts;
import com.israel.livraisonexpresspos.models.Shop;
import com.israel.livraisonexpresspos.ui.modules.ModuleActivity;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.ui.products.ProductActivity;
import com.israel.livraisonexpresspos.utils.OrderStatus;
import com.israel.livraisonexpresspos.utils.Values;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private final ItemOrderBinding mBinding;
    private Order mOrder;
    private OrderWithCarts mOrderWithCarts;
    private PopupMenu mPopupMenu;
    private PlaceOrderFragment mFragment;
    private final ContactRemoteRepository mContactRepository;
    private Contact mReceiver;
    private Contact mSender;
    public OrderViewHolder(@NonNull ItemOrderBinding binding, Application application) {
        super(binding.getRoot());
        mBinding = binding;
        mContactRepository = new ContactRemoteRepository(application);
    }

    public void bind(OrderWithCarts order, PlaceOrderFragment fragment){
        mOrderWithCarts = order;
        mOrder = order.mOrder;
        mBinding.setOrder(mOrder);
        mFragment = fragment;
        String cartContent = "";

        if (mOrder.getId() != 0 && !TextUtils.isEmpty(mOrder.getStringDelivery())){
            Delivery delivery = new Gson().fromJson(mOrder.getStringDelivery(), new TypeToken<Delivery>(){}.getType());
            if (delivery != null){
                mReceiver = delivery.getReceiver();
                mSender = delivery.getSender();
            }
            if (mReceiver != null){
                mBinding.tvReceiver.setText(mReceiver.getFullname());
            }
        }

        for (Cart c: order.mCarts) {
            cartContent = cartContent.concat("\u2022 " + c.getLibelle() + " x" + c.getQuantite());
            if (c != order.mCarts.get(order.mCarts.size() -1)){
                cartContent = cartContent.concat("\n");
            }
        }

        if (cartContent.equals(""))cartContent = itemView.getContext().getString(R.string.empty_cart);
        mBinding.tvCartContent.setText(cartContent);
        mBinding.tvDate.setText(getDate(order.mOrder.getDateTime()));
        mBinding.tvStatus.setText(mOrder.getStatus().equals(OrderStatus.done.toString())
                ? itemView.getContext().getString(R.string.sent) : mOrder.getStatus().equals(OrderStatus.pending.toString())
                ? itemView.getContext().getString(R.string.waiting_connection)
                : itemView.getContext().getString(R.string.draft));
        manageOrderStatus();
        initOptionMenu();
        mBinding.ivMore.setOnClickListener(this);
    }

    private void initOptionMenu() {
        mPopupMenu = new PopupMenu(itemView.getContext(), mBinding.ivMore);
        mPopupMenu.inflate(R.menu.order_menu);
        Menu menu = mPopupMenu.getMenu();
        if (mOrder.getStatus().equals(OrderStatus.saved.toString())){
            menu.findItem(R.id.action_duplicate).setVisible(false);
        }else {
            if (mOrder.getStatus().equals(OrderStatus.done.toString())){
                menu.findItem(R.id.action_detail).setVisible(true);
            }
            menu.findItem(R.id.action_continue).setVisible(false);
        }
        mPopupMenu.setOnMenuItemClickListener(this);
    }

    private void manageOrderStatus() {
        int color = R.color.whatsappGreen;
        if (mOrder.getStatus().equals(OrderStatus.pending.toString())){
            color = R.color.grey_40;
        }else if (mOrder.getStatus().equals(OrderStatus.saved.toString())){
            color = R.color.colorAccent;
        }
        ViewCompat.setBackgroundTintList(mBinding.tvStatus,
                ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), color)));
    }

    private String getDate(long millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.FRANCE);
        return format.format(calendar.getTime());
    }

    @Override
    public void onClick(View view) {
        mPopupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_continue){
            Values.order = mOrder;
            Values.sender = mSender;
            Values.receiver = mReceiver;
            Values.shop = new Gson().fromJson(mOrder.getShopObject(), new TypeToken<Shop>(){}.getType());
            Values.module = new Gson().fromJson(mOrder.getModuleObject(), new TypeToken<Module>(){}.getType());
            itemView.getContext().startActivity(new Intent(itemView.getContext(), ProductActivity.class));
        }else if (id == R.id.action_detail){
            Delivery delivery = new Gson().fromJson(mOrder.getStringDelivery(), new TypeToken<Delivery>(){}.getType());
            if (delivery != null){
                Intent intent = new Intent(itemView.getContext(), OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.ORDER_ID, delivery.getInfos().getId());
                itemView.getContext().startActivity(intent);
            }
        }else if (id == R.id.action_duplicate){
            mFragment.duplicateOrder(mOrderWithCarts);
        }else if (id == R.id.action_delete){
            mFragment.deleteOrder(mOrder);
        }
        return false;
    }
}
