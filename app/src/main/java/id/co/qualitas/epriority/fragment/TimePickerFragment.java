package id.co.qualitas.epriority.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimeSelectedListener listener;

    // Interface to communicate the selected time back to the calling Fragment/Activity
    public interface TimeSelectedListener {
        void onTimeSelected(int hourOfDay, int minute);
    }

    public static TimePickerFragment newInstance(TimeSelectedListener listener) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.listener = listener;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        // Parameters: context, OnTimeSetListener, initial hour, initial minute, is24HourView
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // This is called when the user sets a time.
        // You can process the hourOfDay and minute here.
        if (listener != null) {
            listener.onTimeSelected(hourOfDay, minute);
        }
    }

    // Optional: You can attach the listener in onAttach if preferred,
    // but passing via newInstance is common for simple cases.
    /*
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TimeSelectedListener) {
            listener = (TimeSelectedListener) context;
        } else {
            // Fallback for direct instantiation if Activity/Fragment doesn't implement listener directly
            // This is less common now with newInstance pattern.
            // Consider if the parent fragment should implement the listener.
            if (getParentFragment() instanceof TimeSelectedListener) {
                listener = (TimeSelectedListener) getParentFragment();
            } else {
                 throw new RuntimeException(context.toString()
                         + " or parent fragment must implement TimeSelectedListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    */
}
