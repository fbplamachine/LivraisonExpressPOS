package com.israel.livraisonexpresspos.ui.filter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.FragmentFilterBinding;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men.DeliveryMenDialogFragment;
import com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men.OnDeliveryMenSelectedListener;
import com.israel.livraisonexpresspos.ui.shops.OnShopSelectedListener;
import com.israel.livraisonexpresspos.ui.shops.ShopsDialogFragment;
import com.israel.livraisonexpresspos.utils.CourseStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterDialogFragment extends AppCompatDialogFragment implements ChipGroup.OnCheckedChangeListener, View.OnClickListener, OnDeliveryMenSelectedListener, OnShopSelectedListener {

    private FragmentFilterBinding mBinding;
    private NavController mNavController;
    private String status = "";
    private ShopsDialogFragment mShopDialog;
    private String mDeliveryMan;
    private int mShopId;
    private String mShopName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentFilterBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        initUI();
        return builder.create();
    }

    private void initUI() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setLastConstraint();
        mBinding.groupDate.setOnCheckedChangeListener(this);
        mBinding.groupCity.setOnCheckedChangeListener(this);
        mBinding.ibClose.setOnClickListener(this);
        mBinding.ibFilter.setOnClickListener(this);
        mBinding.tvDateType.setOnClickListener(this);
        mBinding.tvStatus.setOnClickListener(this);
        mBinding.tvResetFilter.setOnClickListener(this);
        mBinding.tvDeliveryMan.setOnClickListener(this);
        mBinding.tvShop.setOnClickListener(this);
    }

    private void setLastConstraint() {
        if (getArguments() == null || getArguments().getString("constraint").trim().length() == 0) return;
        JSONObject constraint = null;
        try {
            constraint = new JSONObject(getArguments().getString("constraint"));
            Log.e("le json",getArguments().getString("constraint"));
            mBinding.etFilterPattern.setText(constraint.getString("search_customer"));
            if (constraint.getString("ville").equals("1")){
                mBinding.chipDouala.setChecked(true);
            }else if (constraint.getString("ville").equals("2")){
                mBinding.chipYaounde.setChecked(true);
            }else {
                mBinding.chipAll.setChecked(true);
            }
            if (constraint.has("shop") && constraint.has("magasins")){
                mShopName = constraint.getString("shop");
                mShopId = Integer.parseInt(constraint.getString("magasins"));
                mBinding.tvShop.setText(mShopName);
                mBinding.tvShop.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_90));
            }
            if (constraint.has("search_coursier")){
                mDeliveryMan = constraint.getString("search_coursier");
                mBinding.tvDeliveryMan.setText(mDeliveryMan);
                mBinding.tvDeliveryMan.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_90));
            }
            setLastStatus(constraint.getString("status"));
            setLastDate(constraint.getString("from_date"));
        } catch (JSONException e) {
            e.printStackTrace();
            App.handleError(e);
        }
    }

    private void setLastDate(String lastDate) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-1);
        String strYesterdayDate = dateFormat.format(calendar.getTime());
        calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+2);
        String strTomorrowDate = dateFormat.format(calendar.getTime());

        if (TextUtils.equals(lastDate, strYesterdayDate)){
            mBinding.chipYesterday.setChecked(true);
        }else if (TextUtils.equals(lastDate,strTomorrowDate)){
            mBinding.chipTomorrow.setChecked(true);
        }else {
            mBinding.chipToday.setChecked(true);
        }
    }

    private void setLastStatus(String statusCode) {
        int code = Integer.parseInt(statusCode.isEmpty()? "10" : statusCode);
        status = statusCode;

        if (code == CourseStatus.CODE_UNASSIGNED){
            mBinding.tvStatus.setText(R.string.menu_unassigned);
        }else if (code == CourseStatus.CODE_CANCELED){
            mBinding.tvStatus.setText(R.string.menu_none);
        }else if (code == CourseStatus.CODE_CANCELED){
            mBinding.tvStatus.setText(R.string.menu_cancelled);
        }else if (code == CourseStatus.CODE_RELAUNCH){
            mBinding.tvStatus.setText(R.string.menu_relaunch);
        }else if (code == CourseStatus.CODE_DELIVERED){
            mBinding.tvStatus.setText(R.string.menu_delivered);
        }else if (code == CourseStatus.CODE_INPROGRESS){
            mBinding.tvStatus.setText(R.string.menu_charged);
        }else if (code == CourseStatus.CODE_STARTED){
            mBinding.tvStatus.setText(R.string.menu_started);
        }else if (code == CourseStatus.CODE_ASSIGNED){
            mBinding.tvStatus.setText(R.string.menu_pendind);
        }
    }

    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == mBinding.ibClose.getId()) {
            dismiss();
        } else if (id == mBinding.ibFilter.getId()) {
            try {
                MainActivity activity = (MainActivity) requireActivity();
                activity.performFilter(buildFilterConstraint().toString());
            } catch (JSONException e) {
                e.printStackTrace();
                App.handleError(e);
            }

            dismiss();
        } else if (id == mBinding.tvDateType.getId()) {
            displayDateTypeMenu();
        } else if (id == mBinding.tvStatus.getId()){
            displayStatusMenu();
        } else if (id == mBinding.tvResetFilter.getId()) {
            resetFilter();
        } else if (id == mBinding.tvDeliveryMan.getId()) {
            DeliveryMenDialogFragment fragment = new DeliveryMenDialogFragment();
            fragment.setListener(this);
            fragment.show(requireActivity().getSupportFragmentManager(), fragment.getTag());
        }else if (id == mBinding.tvShop.getId()) {
            mShopDialog = new ShopsDialogFragment();
            mShopDialog.setListener(this);
            mShopDialog.show(requireActivity().getSupportFragmentManager(), mShopDialog.getTag());
        }
    }

    private void resetFilter() {
        mBinding.etFilterPattern.setText("");
        mBinding.tvDeliveryMan.setText("");
        mBinding.tvShop.setText("");
        mBinding.chipAll.setChecked(true);
        mBinding.tvStatus.setText("");
        status = "";
        mBinding.tvDateType.setText("Livraison");
        mBinding.chipToday.setChecked(true);
        MainActivity activity = (MainActivity) requireActivity();
        activity.performFilter("");
    }

    private void displayStatusMenu() {
        PopupMenu menu = new PopupMenu(requireContext(),mBinding.tvStatus);
        menu.inflate(R.menu.menu_status);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.itemUnassigned){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = String.valueOf(CourseStatus.CODE_UNASSIGNED);
                    return true;
                }else if (id == R.id.itemToValidate){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = String.valueOf(CourseStatus.CODE_TO_VALIDATE);
                    return true;
                }else if (id == R.id.itemNone){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = "";
                    return true;
                }else if (id == R.id.itemPending){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = String.valueOf(CourseStatus.CODE_ASSIGNED);
                    return true;
                }else if (id == R.id.itemStarted){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = String.valueOf(CourseStatus.CODE_STARTED);
                    return true;
                }else if (id == R.id.itemPicked){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = String.valueOf(CourseStatus.CODE_INPROGRESS);
                    return true;
                }else if (id == R.id.itemDelivered){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = String.valueOf(CourseStatus.CODE_DELIVERED);
                    return true;
                }else if (id == R.id.itemRelaunch){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = String.valueOf(CourseStatus.CODE_RELAUNCH);
                    return true;
                }else if (id == R.id.itemCancelled){
                    mBinding.tvStatus.setText(menuItem.getTitle());
                    status = String.valueOf(CourseStatus.CODE_CANCELED);
                    return true;
                }
                return false;
            }
        });
        menu.show();
    }

    private JSONObject buildFilterConstraint() throws JSONException {
        JSONObject constraint = new JSONObject();

//        {
//            "search_customer":"",
//                "ville":"",
//                "quartier_type":"0",
//                "quartier":"",
//                "from_date":"01/12/2020",
//                "to_date":"01/12/2020",
//                "status":"",
//                "tri_date":"desc",
//                "is_manager":false,
//                "per_page":10
//        }

        constraint.put("search_customer", getClient());
        constraint.put("ville", getSelectedCity());
        constraint.put("quartier_type", "0");
        constraint.put("quartier", "");
        constraint.put("tri_date","desc");
        constraint.put("is_manager", User.isManager() || User.isPartner());
        constraint.put("status",status);
        constraint.put("from_date", getSelectedDate());
        constraint.put("to_date", getSelectedDate());
        constraint.put("per_page", 200);
        constraint.put("search_coursier", mDeliveryMan);
        constraint.put("shop", mShopName);
        constraint.put("magasins", mShopId == 0 ? null : String.valueOf(mShopId));
        User user = User.getCurrentUser(requireContext());
        if (User.isPartner()){
            StringBuilder shopIds = new StringBuilder();
            for(int i = 0; i < user.getMagasins_ids().size(); i++){
                int id = user.getMagasins_ids().get(i);
                shopIds.append(id);
                if (i < (user.getMagasins_ids().size() - 1)){
                    shopIds.append(",");
                }
            }
            constraint.put("magasins", shopIds.toString());
        }

        return constraint;
    }


    private String getClient() {
        if (mBinding.etFilterPattern.getText().toString() != null) {
            return mBinding.etFilterPattern.getText().toString();
        } else {
            return "";
        }
    }

    private String getSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (mBinding.chipTomorrow.isChecked()) {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            return dateFormat.format(calendar.getTime());
        } else if (mBinding.chipYesterday.isChecked()) {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
            return dateFormat.format(calendar.getTime());
        } else {
            mBinding.chipToday.setChecked(true);
            return dateFormat.format(calendar.getTime());
        }
    }

    private String getSelectedCity() {
        if (mBinding.chipDouala.isChecked()) {
            return "1";
        } else if (mBinding.chipYaounde.isChecked()) {
            return "2";
        } else {
            mBinding.chipDouala.setChecked(true);
            return "";
        }
    }

    private void displayDateTypeMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), mBinding.tvDateType);
        menu.inflate(R.menu.menu_date_type);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.item_delivery_date) {
                    mBinding.tvDateType.setText(menuItem.getTitle());
                    //modifier le champ dans le json
                    return true;
                } else if (id == R.id.item_creation_date) {
                    //modifier le champ dans le json
                    mBinding.tvDateType.setText(menuItem.getTitle());
                    return true;
                } else {
                    return false;
                }
            }
        });
        menu.show();
    }


    private void pickDate() { /*disable for the moment*/
        Calendar calendar = Calendar.getInstance();
        final long start = calendar.getTimeInMillis();

        CalendarConstraints.Builder constrainsBuilder = new CalendarConstraints.Builder();
        constrainsBuilder.setStart(start);
        constrainsBuilder.setEnd(start);

        //todo : display date range picker
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Sélectionner la plage de date");
        builder.setCalendarConstraints(constrainsBuilder.build());

        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(selection.first);
                int startDay = date.get(Calendar.DAY_OF_MONTH);
                date.setTimeInMillis(selection.second);
                int endDay = date.get(Calendar.DAY_OF_MONTH);
                StringBuilder formattedSelection = new StringBuilder();

                if (startDay != endDay) {
                    formattedSelection.append(" : du ").append(startDay).append(" au ").append(endDay).append(" janvier");
                } else {
                    formattedSelection.append(" : le ").append(startDay).append(" janvier ");
                }
                mBinding.selection.setText(formattedSelection.toString());
            }
        });
        datePicker.show(getChildFragmentManager(), "date_picker");

    }


    @Override
    public void onDeliveryMenSelected(User user) {
        mBinding.tvDeliveryMan.setText(user.getFullname());
        mBinding.tvDeliveryMan.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_90));
        mDeliveryMan = user.getFullname();
    }

    @Override
    public void onShopSelected(String name, int id) {
        mBinding.tvShop.setText(name);
        mBinding.tvShop.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_90));
        mShopDialog.dismiss();
        mShopId = id;
        mShopName = name;
    }
}
