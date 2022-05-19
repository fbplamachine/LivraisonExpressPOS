package com.israel.livraisonexpresspos.ui.statistics.shop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.israel.livraisonexpresspos.databinding.DialogDateRangeBinding;

import java.util.Calendar;

public class DateRangeDialog extends AppCompatDialogFragment implements
        DatePickerDialog.OnDateSetListener, View.OnClickListener {
    public static final int START_DATE_CODE = 237;
    public static final int END_DATE_CODE = 277;
    private DialogDateRangeBinding mBinding;
    private int dateCode;
    private DatePickerDialog mDatePickerDialog;
    private boolean startDateSet, endDateSet;
    private Calendar mCalendar;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = DialogDateRangeBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        initUI();
        return builder.create();
    }

    private void initUI() {
        mBinding.ivStartDate.setOnClickListener(this);
        mBinding.ivEndDate.setOnClickListener(this);
        mBinding.ivClose.setOnClickListener(this);
        mBinding.buttonSearch.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(requireContext(), this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        @SuppressLint("DefaultLocale") String date = String.format("%02d/%02d", dayOfMonth, month + 1) + "/" + year;
        if (dateCode == START_DATE_CODE){
            mBinding.tvStartDate.setText(date);
            startDateSet = true;
        }else if (dateCode == END_DATE_CODE){
            mBinding.tvEndDate.setText(date);
            endDateSet = true;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.ivStartDate.getId()){
            dateCode = START_DATE_CODE;
            mDatePickerDialog.show();
        }else if (id == mBinding.ivEndDate.getId()){
            mDatePickerDialog.getDatePicker().init(mCalendar.get(Calendar.YEAR)
                    , mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), null);
            dateCode = END_DATE_CODE;
            mDatePickerDialog.show();
        }else if (id == mBinding.ivClose.getId()){
            dismiss();
        }else if (id == mBinding.buttonSearch.getId()){
            if (startDateSet && endDateSet){
                DateRangeListener rangeListener = (DateRangeListener) requireActivity();
                rangeListener.onDateRangeSet(mBinding.tvStartDate.getText().toString(), mBinding.tvEndDate.getText().toString());
                dismiss();
            }else {

            }
        }
    }
}
