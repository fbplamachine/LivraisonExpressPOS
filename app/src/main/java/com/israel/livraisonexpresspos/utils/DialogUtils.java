package com.israel.livraisonexpresspos.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bugsnag.android.Bugsnag;
import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.DialogAddressDetailsBinding;
import com.israel.livraisonexpresspos.listeners.OnPerformAction;
import com.israel.livraisonexpresspos.models.Address;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * this interface is use for each date selection and only used in this file that's why it isn't located out this file
 */
interface onDateSelectedCallBack<T> {

    void onDateSelected(Calendar chosenDate, boolean isToday);

    void onTimeSelected(String selectedTime);
}

public class DialogUtils {

    public static final String WARNING_FOR_CLOSING_APPLICATION = "before leaving app";
    public static final String WARNING_FOR_REJECTING_ORDER = "while rejecting order";
    public static final String WARNING_FOR_RELAUNCHING_ORDER = "while relaunching order";
    public static final String INFO_FOR_DELIVERING_ORDER = "delivering_order";
    private static final String DATE_FOR_SYSTEM = "dateForSystem";
    private static final String DATE_TO_DISPLAY = "dateToDisplay";
    private static OnPerformAction actionPerformer;


    /**
     * Create a simple and customizable dialog to display that take the following params
     *
     * @param activity         the parent activity
     * @param drawable         the id of the drawable displayed before the title
     * @param title            text for the title
     * @param message          text for the message on the body
     * @param positiveBtnLabel texte to display on the positive button
     * @param negativeBtnLabel texte to display on the negative button
     * @return void
     **/
    public static void showAlertDialog(final Activity activity, @Nullable Fragment fragment, @Nullable int drawable, String title, String message, String positiveBtnLabel, String negativeBtnLabel, final String role, @Nullable final String deliveryDate) {
        if (!(activity instanceof MainActivity) && fragment != null) { //todo : check if this still up to date
            actionPerformer = (OnPerformAction) fragment;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setIcon(drawable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.equals(WARNING_FOR_CLOSING_APPLICATION, role)) {
                            activity.finish();
                            activity.moveTaskToBack(true);
                        }

                        if (TextUtils.equals(WARNING_FOR_RELAUNCHING_ORDER, role)) {
                            showMotivationCaptorDialog(activity, activity.getResources().getDrawable(R.drawable.ic_round_warning_24), "Reprogrammer la course", "Continuer", "Annuler", role, deliveryDate);
                        }

                        if (TextUtils.equals(WARNING_FOR_REJECTING_ORDER, role)) {
                            showMotivationCaptorDialog(activity, null, "Pourquoi rejettez-vous la course ?", "Valider", "annuler", role, null);
                        }

                        if (TextUtils.equals(INFO_FOR_DELIVERING_ORDER,role)){
                            actionPerformer.performAction(role,null,null,null);
                        }
                    }
                })
                .setNegativeButton(negativeBtnLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private static void showMotivationCaptorDialog(final Activity activity, @Nullable Drawable drawable, String title, String positiveBtnLabel, String negativeBtnLabel, final String role, String deliveryDate) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_fragment_reject_course, null);
        final boolean[] mIsToday = {false};
        final boolean[] judge = {true, false};
        final String[] judgeMessages = {"", ""};
        final Calendar[] mChosenDate = {null};
        final TextView tvTitle = view.findViewById(R.id.tv_motivation_dialog_title);
        final LinearLayout rawNewDate = view.findViewById(R.id.raw_new_date);
        final TextView tvNewDate = view.findViewById(R.id.tv_relaunch_selected_date);
        final EditText tvNewTime = view.findViewById(R.id.tv_relaunch_selected_time);
        final TextView tvNewDateSystem = view.findViewById(R.id.tv_relaunch_selected_date_system);
        final TextView tvNewTimeSystem = view.findViewById(R.id.tv_relaunch_selected_time_system);
        try {
            JSONObject date = getStrDate(Calendar.getInstance());
            tvNewDate.setText(date.get(DATE_TO_DISPLAY).toString());
            tvNewDateSystem.setText(date.get(DATE_FOR_SYSTEM).toString());
            mIsToday[0] = true;
        } catch (JSONException e) {
            e.printStackTrace();
            App.handleError(e);
        }

        final CardView relaunchDatePicker = view.findViewById(R.id.img_btn_relaunch_date_picker);
        final CardView relaunchTimePicker = view.findViewById(R.id.img_btn_relaunch_time_picker);
        if (TextUtils.equals(role, WARNING_FOR_RELAUNCHING_ORDER)) {
            rawNewDate.setVisibility(View.VISIBLE);
        }

        relaunchDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    pickDate(activity, new onDateSelectedCallBack<String>() {
                        @Override
                        public void onDateSelected(Calendar chosenDate, boolean isToday) {
                            try {
                                JSONObject jsonChosenDate = getStrDate(chosenDate);
                                tvNewDate.setText(jsonChosenDate.getString(DATE_TO_DISPLAY));
                                tvNewDateSystem.setText(jsonChosenDate.getString(DATE_TO_DISPLAY));
                                mIsToday[0] = isToday;
                                mChosenDate[0] = chosenDate;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                App.handleError(e);
                            }
                        }

                        @Override
                        public void onTimeSelected(String selectedTime) {

                        }

                    });
            }
        });

        relaunchTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    pickTime(activity, new onDateSelectedCallBack<String>() {
                        @Override
                        public void onDateSelected(Calendar chosenDate, boolean isToday) {
                            //todo : do nothing
                        }

                        @Override
                        public void onTimeSelected(String selectedTime) {
                            tvNewTime.setText(selectedTime);
                            tvNewTimeSystem.setText(selectedTime);
                            //todo : validate the selected time and immediately set errors the are some to display
                            if (mIsToday[0]) {
                                JSONObject result = validateTime(selectedTime);
                                try {
                                    judge[0] = result.getBoolean("valide");
                                    if (!result.getBoolean("valide")) {
                                        tvNewTime.requestFocus();
                                        tvNewTime.setError(result.get("message").toString());
                                        judgeMessages[0] = result.get("message").toString();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvNewTime.setError(null);
                                            }
                                        }, 4000);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    App.handleError(e);
                                }
                            }
                        }
                    });
            }
        });

        tvTitle.setText(title);
        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(view)
                .setIcon(drawable)
                .setPositiveButton(positiveBtnLabel, null)
                .setNegativeButton(negativeBtnLabel, null)
                .show();

        final EditText editTextMotif = view.findViewById(R.id.et_motif);
        editTextMotif.requestFocus();
        Button buttonSave = builder.getButton(AlertDialog.BUTTON_POSITIVE);

        editTextMotif.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(editTextMotif.getText().toString()) || editTextMotif.getText().toString().trim().length() < 10) {
                    judge[1] = false;
                    judgeMessages[1] = "Ce motif trop cours plus de détail serait apprécié!!!";
                } else {
                    judge[1] = true;
                    judgeMessages[1] = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {

                try {
                    if (TextUtils.equals(role, WARNING_FOR_REJECTING_ORDER)) {
                        if (judge[1]) {
                            actionPerformer.performAction(role, editTextMotif.getText().toString(),null,null);
                            builder.dismiss();
                        } else {
                            setErrors(null, judgeMessages[1]);
                        }
                    } else {
                        if (judge[0] && judge[1]) {
                            actionPerformer.performAction(role, editTextMotif.getText().toString(),tvNewDateSystem.getText().toString(),tvNewTimeSystem.getText().toString());
                            builder.dismiss();
                        } else {
                            setErrors(judgeMessages[0], judgeMessages[1]);
                        }
                    }

                } catch (Exception e) {
                    Bugsnag.notify(e);
                    App.handleError(e);
                }
            }


            private void setErrors(@Nullable String timeError, @Nullable String reasonError) {
                if (timeError != null && !TextUtils.isEmpty(timeError)) {
                    tvNewTime.setError(timeError);
                }
                if (reasonError != null && !TextUtils.isEmpty(reasonError)) {
                    editTextMotif.setError(reasonError);
                } else {
                    editTextMotif.setError("Champ requit");
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvNewTime.setError(null);
                        editTextMotif.setError(null);
                    }
                }, 4000);

            }
        });
    }


    private static JSONObject validateTime(String selectedTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            int deliveryDelay = 30;
            String[] selectedTimeComponents = selectedTime.split(":");
            int selectedHour = Integer.valueOf(selectedTimeComponents[0]);
            int selectedMinute = Integer.valueOf(selectedTimeComponents[1]);
            if ((selectedHour < Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
                jsonObject.put("valide", false);
                jsonObject.put("message", "Vous ne pouvez choisir une heure antérieure a l'heure actuelle !!!!");
            } else if (selectedHour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                //the user is setting a new delivery date withing the current hour, so it shouldn't be sooner means less than 30 mins
                int remainingMinutes = selectedMinute - Calendar.getInstance().get(Calendar.MINUTE);
                Log.e("remaining", String.valueOf(remainingMinutes));
                if (remainingMinutes < 0) {
                    //setting an anterior time then we stop him
                    jsonObject.put("valide", false);
                    jsonObject.put("message", "Vous ne pouvez choisir une heure antérieure a l'heure actuelle !!!!");
                } else if (remainingMinutes < deliveryDelay) {
                    jsonObject.put("valide", false);
                    jsonObject.put("message", "les courses doivent être reprogrammées avec un delais minimal de 30 minutes entre la date actuelle et la date de livraison !!");
                } else {
                    jsonObject.put("valide", true);
                }
            } else {
                jsonObject.put("valide", true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            App.handleError(e);
        }
        return jsonObject;
    }

    private static JSONObject getStrDate(Calendar calendarInstance) {
        JSONObject timeObject = new JSONObject();
        CharSequence dateForSystem = DateFormat.format("yyyy-MM-dd", calendarInstance.getTime());
        CharSequence dateToDisplay = DateFormat.format("E d MMM yyyy", calendarInstance.getTime());
        try {
            timeObject.put(DATE_FOR_SYSTEM, dateForSystem.toString());
            timeObject.put(DATE_TO_DISPLAY, dateToDisplay.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            App.handleError(e);
        }
        return timeObject;
    }

    private static void pickDate(final Activity activity, final onDateSelectedCallBack<String> onDateSelectedCallBack) { //todo add a callback
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(activity,  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar chosenDate = Calendar.getInstance();
                chosenDate.set(Calendar.DATE, dayOfMonth);
                chosenDate.set(Calendar.MONTH, month);
                chosenDate.set(Calendar.YEAR, year);
                try {
                    if (!chosenDate.before(calendar.getTime()) && TextUtils.equals(getStrDate(calendar).getString(DATE_FOR_SYSTEM), getStrDate(chosenDate).getString(DATE_FOR_SYSTEM))) {
                        onDateSelectedCallBack.onDateSelected(chosenDate, true);
                    } else {
                        onDateSelectedCallBack.onDateSelected(chosenDate, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    App.handleError(e);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis() - 1000);
        datePicker.show();
    }

    private static void pickTime(final Activity activity, final onDateSelectedCallBack<String> onDateSelectedCallBack) { //todo : add the callback here and call the him once the time is selected
        Calendar calendar = Calendar.getInstance();
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);
        boolean is24HoursFormat = DateFormat.is24HourFormat(activity);
        TimePickerDialog timePicker = new TimePickerDialog(activity,  new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String finalDateSystem = hourOfDay + ":" + minute + ":00";
                onDateSelectedCallBack.onTimeSelected(finalDateSystem);
            }
        }, currentHour, currentMinute, is24HoursFormat);
        timePicker.show();
    }

    public static Date stringToDate(String dateOfTheLastSearch) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfTheLastSearch);
        } catch (Exception e) {
            Log.e("Exception cause", String.valueOf(e.getCause()));
            Log.e("Exception message", e.getMessage());
            App.handleError(e);
        }
        return date;
    }

    public static AlertDialog getLoadDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_fragment_loading, null);
        AlertDialog dialogBuilder = new AlertDialog.Builder(context).setView(view).show();
        return dialogBuilder;
    }

    //========================== implemented once back from sickness ===========================
    public static void showAddressDetailsDialog(Activity activity, String positiveBtnLabel, Address address){
        DialogAddressDetailsBinding binding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.dialog_address_details, null, false);
        binding.setAddress(address);
        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(binding.getRoot())
                .setPositiveButton(positiveBtnLabel, null)
                .show();

        builder.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });
    }

}