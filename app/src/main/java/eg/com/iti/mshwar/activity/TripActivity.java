package eg.com.iti.mshwar.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.adapter.NotesAdapter;
import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.service.MyReceiver;
import eg.com.iti.mshwar.util.Utils;

public class TripActivity extends AppCompatActivity {
    public static final String TAG = "Error";

    EditText editTxtTripName, editTxtAddNote;
    Spinner spinnerTripType, spinnerTripRepetition;
    TextView txtViewDate, txtViewTime, txtViewTime2, txtViewDate2;
    Button btnAddTrip;
    ImageView imageViewAddNote;
    LinearLayout roundTripTimeAndDate;

    TripBean tripBean;
    TripDaoImpl tripImpl;
    Intent intent;

    Calendar calendar, calendar2;
    ArrayList<String> notesArrayList;
    NotesAdapter notesAdapter;
    ListView notesList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        notesList = findViewById(R.id.list_view_notes);
        editTxtTripName = findViewById(R.id.editTxt_trip_name);
        editTxtAddNote = findViewById(R.id.textView_add_note);
        spinnerTripType = findViewById(R.id.spinner_type_trip);
        spinnerTripRepetition = findViewById(R.id.spinner_trip_repetition);
        txtViewDate = findViewById(R.id.txtView_date);
        txtViewDate2 = findViewById(R.id.txtView_date2);
        txtViewTime = findViewById(R.id.txtView_time);
        txtViewTime2 = findViewById(R.id.txtView_time2);
        btnAddTrip = findViewById(R.id.btn_add_trip);
        imageViewAddNote = findViewById(R.id.image_add_note);
        roundTripTimeAndDate = findViewById(R.id.round_trip_layout);

        notesArrayList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesArrayList, this);
        notesList.setAdapter(notesAdapter);
        final String uid = user.getUid();

        tripBean = new TripBean();
        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code to start trip
                tripBean.setName(editTxtTripName.getText().toString());

                if (tripBean.getName() != null && tripBean.getStartPoint() != null
                        && tripBean.getEndPoint() != null && tripBean.getTime() != null
                        && tripBean.getDate() != null) {

                    tripBean.setStatus(Utils.UPCOMING);
                    tripBean.setNotes(notesArrayList);
                    tripBean.setUserId(uid);

                    if (spinnerTripType.getSelectedItem().equals("Round Trip")) {
                        String firstAlarmId = String.valueOf(setAlarm(calendar));
                        String secondAlarmId = String.valueOf(setAlarm(calendar2));

                        tripBean.addAlarmId(firstAlarmId);
                        tripBean.addAlarmId(secondAlarmId);
                    } else {
                        tripBean.addAlarmId(String.valueOf(setAlarm(calendar)));
                    }

                    tripImpl = new TripDaoImpl();

                    tripImpl.addTrip(tripBean);
                    Toast.makeText(TripActivity.this,
                            "Trip added successfully", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(TripActivity.this,
                            R.string.add_trip_error_message, Toast.LENGTH_LONG).show();
            }
        });

        txtViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog("single");
            }
        });

        txtViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog("single");
            }
        });

        txtViewDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog("round");
            }
        });

        txtViewTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog("round");
            }
        });

        spinnerTripType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tripType = adapterView.getItemAtPosition(i).toString();
                tripBean.setType(tripType);
                if (tripType.equals("Round Trip")) {
                    roundTripTimeAndDate.setVisibility(View.VISIBLE);
                } else
                    roundTripTimeAndDate.setVisibility(View.GONE);
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

        imageViewAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTxtAddNote.getText().toString().length() > 0) {
                    notesArrayList.add(editTxtAddNote.getText().toString());
                    editTxtAddNote.setText("");
                    notesAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    void showDatePickerDialog(final String tripDirection) {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(TripActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                datepicker.setMinDate(System.currentTimeMillis());
                String dateStr = selectedDay + "/" + selectedMonth + 1 + "/" + selectedYear;

                if (!tripDirection.equals("single")) {
                    calendar2.set(Calendar.YEAR, selectedYear);
                    calendar2.set(Calendar.MONTH, selectedMonth);
                    calendar2.set(Calendar.DAY_OF_MONTH, selectedDay);

                    txtViewDate2.setText(dateStr);
                    tripBean.addDate(dateStr);
                } else {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    txtViewDate.setText(dateStr);
                    tripBean.addDate(dateStr);
                }
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    void showTimePickerDialog(final String tripDirection) {
        Calendar calendar1 = Calendar.getInstance();
        final int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar1.get(Calendar.MINUTE);

        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TripActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String timeStr = selectedHour + ":" + selectedMinute;

                if (!tripDirection.equals("single")) {
                    calendar2.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar2.set(Calendar.MINUTE, selectedMinute);

                    txtViewTime2.setText(timeStr);
                    tripBean.addTime(timeStr);
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);

                    txtViewTime.setText(timeStr);
                    tripBean.addTime(timeStr);
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private int setAlarm(Calendar mCalendar) {
        intent = new Intent(TripActivity.this, MyReceiver.class);

        intent.putExtra(Utils.COLUMN_TRIP_NAME, tripBean.getName());
        intent.putExtra(Utils.COLUMN_TRIP_END_POINT, tripBean.getEndPoint());
        intent.putExtra(Utils.COLUMN_TRIP_START_POINT, tripBean.getStartPoint());
        intent.putExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, tripBean.getStartPointLatitude());
        intent.putExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, tripBean.getStartPointLongitude());
        intent.putExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, tripBean.getEndPointLatitude());
        intent.putExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, tripBean.getEndPointLongitude());

        int alarmID = (int) System.currentTimeMillis();

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        TripActivity.this,
                        alarmID, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) TripActivity.this.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                mCalendar.getTimeInMillis(),
                0,
                pendingIntent);

        return alarmID;
    }

    @Override
    protected void onStart() {
        super.onStart();
        PlaceAutocompleteFragment placeAutocompleteFragmentStartPoint = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.editTxt_start_point);
        if (placeAutocompleteFragmentStartPoint != null)
            placeAutocompleteFragmentStartPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "Place: " + place.getName());
                    tripBean.setStartPoint(place.getName().toString());
                    LatLng myLatLong = place.getLatLng();
                    tripBean.setStartPointLatitude(myLatLong.latitude);
                    tripBean.setStartPointLongitude(myLatLong.longitude);

                }

                @Override
                public void onError(com.google.android.gms.common.api.Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        else Toast.makeText(this, "Problem with loading page", Toast.LENGTH_LONG).show();


        PlaceAutocompleteFragment placeAutoCompleteFragmentEndPoint = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.editTxt_end_point);
        if (placeAutoCompleteFragmentEndPoint != null)
            placeAutoCompleteFragmentEndPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "Place: " + place.getName());
                    tripBean.setEndPoint(place.getName().toString());
                    LatLng myLatLong = place.getLatLng();
                    tripBean.setEndPointLatitude(myLatLong.latitude);
                    tripBean.setEndPointLongitude(myLatLong.longitude);
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        else Toast.makeText(this, "Problem with loading page", Toast.LENGTH_LONG).show();
    }
}

