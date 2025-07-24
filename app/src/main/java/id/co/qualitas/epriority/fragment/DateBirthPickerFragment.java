package id.co.qualitas.epriority.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DateBirthPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DateSelectedListener listener;

    // Interface to communicate the selected date back
    public interface DateSelectedListener {
        void onDateSelected(int year, int month, int dayOfMonth);
    }

    public static DateBirthPickerFragment newInstance(DateSelectedListener listener) {
        DateBirthPickerFragment fragment = new DateBirthPickerFragment();
        fragment.listener = listener;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH); // Month is 0-based
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        // Parameters: context, OnDateSetListener, initial year, initial month, initial day
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        // Optional: Set a maximum date (e.g., today) or minimum date
//         datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//         Calendar minDate = Calendar.getInstance();
        // minDate.set(2000, 0, 1); // Year, Month (0-based), Day
//         datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // This is called when the user sets a date.
        // Month is 0-based, so you might want to add 1 for display.
        if (listener != null) {
            listener.onDateSelected(year, month, dayOfMonth);
        }
    }
}
