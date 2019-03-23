package eg.com.iti.mshwar.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import eg.com.iti.mshwar.beans.Trip;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.service.AlarmReceiver;
import eg.com.iti.mshwar.util.Utils;

public class TripActivity extends AppCompatActivity {
    public static final String TAG = "Error";

    EditText editTxtTripName, editTxtAddNote;
    Spinner spinnerTripType, spinnerTripRepetition;
    TextView txtViewDate, txtViewTime, txtViewTime2, txtViewDate2;
    Button btnAddTrip;
    ImageView imageViewAddNote;
    LinearLayout roundTripTimeAndDate;

    Trip trip;
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

        trip = new Trip();
        tripImpl = new TripDaoImpl();

        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

        // Setup upper toolbar with title and back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.add_trip));

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code to start trip
                trip.setName(editTxtTripName.getText().toString());

                if (trip.getName().length() > 0 && trip.getStartPoint().length() > 0
                        && trip.getEndPoint().length() > 0 && trip.getTime().size() > 0
                        && trip.getDate().size() > 0) {

                    trip.setStatus(Utils.UPCOMING);
                    trip.setNotes(notesArrayList);
                    trip.setUserId(uid);

                    String firstAlarmId, secondAlarmId;
                    firstAlarmId = String.valueOf((int) System.currentTimeMillis());
                    secondAlarmId = String.valueOf((int) System.currentTimeMillis() + 51655);

                    if (spinnerTripType.getSelectedItem().equals("Round Trip")) {

                        trip.addAlarmId(firstAlarmId);
                        trip.addAlarmId(secondAlarmId);

                        String key = tripImpl.addTrip(trip);
                        trip.setKey(key);

                        setAlarm(calendar, firstAlarmId);
                        setAlarm(calendar2, secondAlarmId);
                    } else {
                        trip.addAlarmId(String.valueOf(firstAlarmId));
                        String key = tripImpl.addTrip(trip);
                        trip.setKey(key);
                        setAlarm(calendar, firstAlarmId);
                    }

                    Toast.makeText(TripActivity.this,
                            "Trip added successfully", Toast.LENGTH_LONG).show();
                    finish();
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
                trip.setType(tripType);
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
                trip.setRepetition(tripRepetition);
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
                String dateStr = selectedDay + "/" + selectedMonth + 1 + "/" + selectedYear;

                if (!tripDirection.equals("single")) {
                    calendar2.set(Calendar.YEAR, selectedYear);
                    calendar2.set(Calendar.MONTH, selectedMonth);
                    calendar2.set(Calendar.DAY_OF_MONTH, selectedDay);

                    txtViewDate2.setText(dateStr);
                    trip.addDate(dateStr);
                } else {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    txtViewDate.setText(dateStr);
                    trip.addDate(dateStr);
                }
            }
        }, mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
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
                    trip.addTime(timeStr);
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);

                    txtViewTime.setText(timeStr);
                    trip.addTime(timeStr);
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setAlarm(Calendar mCalendar, String alarmID) {
        intent = new Intent(TripActivity.this, AlarmReceiver.class);

//        intent.putExtra(Utils.TRIP_TABLE, trip);
//        try {
//            byte[] arr = Utils.convertToBytes(trip);
//            intent.putExtra(Utils.TRIP_TABLE, arr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        intent.putExtra("key", trip.getKey());
        intent.putExtra(Utils.COLUMN_TRIP_NAME, trip.getName());
        intent.putExtra(Utils.COLUMN_TRIP_START_POINT, trip.getStartPoint());
        intent.putExtra(Utils.COLUMN_TRIP_END_POINT, trip.getEndPoint());
        intent.putExtra(Utils.COLUMN_TRIP_REPETITION, trip.getRepetition());
        intent.putExtra(Utils.COLUMN_TRIP_TRIP_TYPE, trip.getType());
        intent.putExtra(Utils.COLUMN_TRIP_STATUS, trip.getStatus());
        intent.putExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, trip.getStartPointLatitude());
        intent.putExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, trip.getStartPointLongitude());
        intent.putExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, trip.getEndPointLongitude());
        intent.putExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, trip.getEndPointLatitude());

        intent.putExtra(Utils.COLUMN_TRIP_NOTES, trip.getNotes());
        intent.putExtra(Utils.COLUMN_TRIP_ALARM_ID, trip.getAlarmIds());
        intent.putExtra(Utils.COLUMN_TRIP_Time, trip.getTime());
        intent.putExtra(Utils.COLUMN_TRIP_Date, trip.getDate());
        intent.putExtra(Utils.COLUMN_TRIP_USER_ID, trip.getUserId());

        int alarmId = Integer.valueOf(alarmID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TripActivity.this, alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) TripActivity.this.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 0, pendingIntent);
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
                    trip.setStartPoint(place.getName().toString());
                    LatLng myLatLong = place.getLatLng();
                    trip.setStartPointLatitude(myLatLong.latitude);
                    trip.setStartPointLongitude(myLatLong.longitude);
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
                    trip.setEndPoint(place.getName().toString());
                    LatLng myLatLong = place.getLatLng();
                    trip.setEndPointLatitude(myLatLong.latitude);
                    trip.setEndPointLongitude(myLatLong.longitude);
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        else Toast.makeText(this, "Problem with loading page", Toast.LENGTH_LONG).show();
    }

    @Override // For action bar
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

