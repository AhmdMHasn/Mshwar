package eg.com.iti.mshwar.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.dialog.AddNoteDialog;
import eg.com.iti.mshwar.service.MyReceiver;

public class TripActivity extends AppCompatActivity implements AddNoteDialog.AddNoteDialogListener {
    EditText editTxtTripName, editTxtStartPoint, editTxtEndPoint;
    Spinner spinnerTripType, spinnerTripRepetition;
    TextView txtViewDate, txtViewTime;
    Button btnAddTrip;
    ImageView addNote;

    TripBean tripBean;
    TripDaoImpl tripImpl;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        editTxtTripName = findViewById(R.id.editTxt_trip_name);
        editTxtStartPoint = findViewById(R.id.editTxt_start_point);
        editTxtEndPoint = findViewById(R.id.editTxt_end_point);
        spinnerTripType = findViewById(R.id.spinner_type_trip);
        spinnerTripRepetition = findViewById(R.id.spinner_trip_repetition);
        txtViewDate = findViewById(R.id.txtView_date);
        txtViewTime = findViewById(R.id.txtView_time);
        btnAddTrip = findViewById(R.id.btn_add_trip);
        addNote = findViewById(R.id.add_note);

        tripBean = new TripBean();
        calendar = Calendar.getInstance();

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code to start trip
                tripBean.setName(editTxtTripName.getText().toString());
                tripImpl = new TripDaoImpl();
                tripImpl.addTrip(tripBean);
                setAlarm();
            }
        });


        txtViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        txtViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        spinnerTripType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String tripType = adapterView.getItemAtPosition(i).toString();
                tripBean.setType(tripType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTripRepetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tripRepetition = adapterView.getItemAtPosition(i).toString();
                tripBean.setRepetition(tripRepetition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                     // No thing to do here
            }

        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddNoteDialog dialog = new AddNoteDialog();

                dialog.show(getSupportFragmentManager(), "dialog_add_note");

            }
        });

    }

    @Override
    public void makeNote(String noteDescription) {
   tripBean.appendNotes(noteDescription);
    }

    void showDatePickerDialog(){
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(TripActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {

                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                selectedMonth = selectedMonth + 1;
                txtViewDate.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    void showTimePickerDialog(){
        Calendar calendar1 = Calendar.getInstance();
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        int minute = calendar1.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TripActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                txtViewTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    void setAlarm(){
        Intent intent = new Intent(TripActivity.this, MyReceiver.class);
        int alarmID = (int)System.currentTimeMillis();

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        TripActivity.this,
                        alarmID, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) TripActivity.this.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                0,
                pendingIntent);

        tripBean.setAlarmId(String.valueOf(alarmID));
    }
}
