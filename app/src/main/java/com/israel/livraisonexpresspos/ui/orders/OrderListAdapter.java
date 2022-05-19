package com.israel.livraisonexpresspos.ui.orders;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.models.from_steed_app.Client;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.utils.CourseStatus;

import java.util.ArrayList;
import java.util.List;


public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int REQUEST_DETAILS = 1;
    public static final String CURRENT_ORDER = "current_order";
    public static final String CURRENT_LIST_POSITION = "current_list_position";
    private List<OrderSteed> mOrderSteeds;
    private List<OrderSteed> mOrderSteedList;
    private final Activity activity;


    public OrderListAdapter(Activity activity) {
        this.mOrderSteeds = new ArrayList<>();
        this.mOrderSteedList = new ArrayList<>();
        this.activity = activity;
    }

    public List<OrderSteed> getOrderSteeds() {
        return mOrderSteeds;
    }

    public void setOrderSteeds(List<OrderSteed> receivedCourseList) {
        mOrderSteeds = receivedCourseList;
        mOrderSteedList = new ArrayList<>(receivedCourseList);
        notifyDataSetChanged();
    }

    public void addOrders(List<OrderSteed> receivedCourseList) {
        mOrderSteeds.addAll(receivedCourseList);
        mOrderSteedList.addAll(receivedCourseList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        vh = new ListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ListViewHolder) {
            final ListViewHolder vh = (ListViewHolder) holder;
            final OrderSteed c = mOrderSteeds.get(position);
            vh.txt_order_code.setText(c.getInfos().getRef());
            vh.txt_amount.setText(String.valueOf(c.getPaiement().getMontant_total()).concat(" FCFA"));
            vh.txt_distantance_sender_receiver.setText(c.getInfos().getDistance_text());
            vh.txt_delivery_time_sender_receiver.setText(c.getInfos().getDuration_text());

            if (!mOrderSteeds.get(position).getSender().getAdresses().isEmpty()) {
                vh.txt_address_start.setText(c.getSender().getAdresses().get(0).getQuartier());
            } else {
                vh.txt_address_start.setText("Non indiqué");
            }
            if (!mOrderSteeds.get(position).getReceiver().getAdresses().isEmpty()) {
                vh.txt_address_stop.setText(c.getReceiver().getAdresses().get(0).getQuartier());
            } else {
                vh.txt_address_stop.setText("Non indiqué");
            }
            vh.tvOrderStatus.setText(CourseStatus.getStatusHuman(c.getInfos().getStatut()));
            ViewCompat.setBackgroundTintList(vh.tvOrderStatus, ColorStateList.valueOf(ContextCompat.getColor(activity, getColor(c.getInfos().getStatut()))));
            vh.txt_date_delivery.setText(c.getInfos().getDate_livraison());
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent moveToDetails = new Intent(activity, OrderDetailActivity.class);
                    moveToDetails.putExtra(CURRENT_ORDER, c);
                    moveToDetails.putExtra(CURRENT_LIST_POSITION, position);
                    activity.startActivityForResult(moveToDetails, REQUEST_DETAILS);
                    Utilities.showForwardTransition(activity);
                }
            });
        }
    }

    private int getColor(int statusCode) {
        switch (statusCode) {
            case CourseStatus.CODE_UNASSIGNED:
            default:
                return R.color.non_assignee;
            case CourseStatus.CODE_ASSIGNED:
                return R.color.en_attente;
            case CourseStatus.CODE_STARTED:
            case CourseStatus.CODE_INPROGRESS:
                return R.color.en_cours;
            case CourseStatus.CODE_DELIVERED:
                return R.color.terminee;
        }
    }

    @Override
    public int getItemCount() {
        return mOrderSteeds.size();
    }

    public Filter getFilter(){
        return mFilter;
    }

    private final Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OrderSteed> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mOrderSteedList);
            }else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (OrderSteed steed : mOrderSteedList){
                    Client receiver = steed.getReceiver();
                    if (receiver.getFullname().toLowerCase().trim().contains(pattern)
                            || receiver.getTelephone().toLowerCase().trim().contains(pattern)
                            || steed.getInfos().getRef().trim().toLowerCase().contains(pattern)
                            || (receiver.getTelephone_alt() != null
                            && receiver.getTelephone_alt().toLowerCase().trim().contains(pattern))){
                        filteredList.add(steed);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mOrderSteeds.clear();
            if (results.values == null)return;
            mOrderSteeds.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView txt_address_start;
        TextView txt_amount;
        TextView txt_distantance_sender_receiver;
        TextView txt_delivery_time_sender_receiver;
        TextView txt_address_stop;
        TextView txt_date_delivery;
        TextView txt_order_code;
        CardView cardItem;
        Button btn_status;
        TextView tvOrderStatus;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_amount = itemView.findViewById(R.id.amount);
            txt_delivery_time_sender_receiver = itemView.findViewById(R.id.tv_temps_sender_receiver);
            txt_distantance_sender_receiver = itemView.findViewById(R.id.tv_distance_sender_receiver);
            btn_status = itemView.findViewById(R.id.btn_status);
            txt_address_start = itemView.findViewById(R.id.depart);
            txt_address_stop = itemView.findViewById(R.id.arriver);
            txt_date_delivery = itemView.findViewById(R.id.date_delivry);
            txt_order_code = itemView.findViewById(R.id.tvOrderCode);
            cardItem = itemView.findViewById(R.id.card_order_item);
            tvOrderStatus = itemView.findViewById(R.id.textView_order_status);
        }
    }
}
