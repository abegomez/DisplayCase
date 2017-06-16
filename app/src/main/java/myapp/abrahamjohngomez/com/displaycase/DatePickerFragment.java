package myapp.abrahamjohngomez.com.displaycase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Dialog to input date
 * Created by ryuhyoko on 6/14/2017.
 */

public class DatePickerFragment extends DialogFragment {

    public DatePickerFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(), (AddNewItemActivity) getActivity(), year, month, day);
    }
}


