package com.example.droidcafe;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface DateSelectionListener {
        void onDateSelected(int year, int month, int day);
    }

    private DateSelectionListener mListener;
    private Calendar minDate;
    private Calendar maxDate;

    public static DatePickerFragment newInstance(Calendar minDate, Calendar maxDate) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("minDate", minDate);
        args.putSerializable("maxDate", maxDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DateSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DateSelectionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        minDate = (Calendar) getArguments().getSerializable("minDate");
        maxDate = (Calendar) getArguments().getSerializable("maxDate");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), this, year, month, day);

        if (minDate != null) {
            dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }
        if (maxDate != null) {
            dialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        }

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (mListener != null) {
            mListener.onDateSelected(year, month, day);
        }
    }
}