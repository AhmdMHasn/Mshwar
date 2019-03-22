package eg.com.iti.mshwar.fragment;

        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.TimePickerDialog;
        import android.os.Bundle;
        import android.support.v4.app.DialogFragment;
        import android.text.format.DateFormat;

        import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        Calendar calendar=Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
    }
}
