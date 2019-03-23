package eg.com.iti.mshwar.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

/**
 * TODO:
 * - update the trip info in the database.
 * - cancel the old alarms using the ids in the database.
 * - set new alarms using same ids.
 **/

public class EditTripActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_edit_trip);

        tripBean = (TripBean) getIntent().getSerializableExtra(Utils.TRIP_TABLE);

        notesList = findViewById(R.id.list_view_notes_EditActivity);
        editTxtTripName = findViewById(R.id.editTxt_trip_name_EditActivity);
        editTxtAddNote = findViewById(R.id.textView_add_note_EditActivity);
        spinnerTripType = findViewById(R.id.spinner_type_trip_EditActivity);
        spinnerTripRepetition = findViewById(R.id.spinner_trip_repetition_EditActivity);
        txtViewDate = findViewById(R.id.txtView_date_EditActivity);
        txtViewDate2 = findViewById(R.id.txtView_date2_EditActivity);
        txtViewTime = findViewById(R.id.txtView_time_EditActivity);
        txtViewTime2 = findViewById(R.id.txtView_time2_EditActivity);
        btnAddTrip = findViewById(R.id.btn_save_changes);
        imageViewAddNote = findViewById(R.id.image_add_note_EditActivity);
        roundTripTimeAndDate = findViewById(R.id.round_trip_layout_EditActivity);

        editTxtTripName.setText(tripBean.getName());
        spinnerTripType.setSelection(((ArrayAdapter) spinnerTripType.getAdapter()).getPosition(tripBean.getType()));
        spinnerTripRepetition.setSelection(((ArrayAdapter) spinnerTripRepetition.getAdapter()).getPosition(tripBean.getRepetition()));

        txtViewDate.setText(tripBean.getDate().get(0));
        txtViewTime.setText(tripBean.getTime().get(0));

        if (tripBean.getTime().size() > 1) {
            txtViewTime2.setText(tripBean.getTime().get(1));
        }
        if (tripBean.getDate().size() > 1) {
            txtViewDate2.setText(tripBean.getDate().get(1));
        }
        notesArrayList = tripBean.getNotes();
        notesAdapter = new NotesAdapter(notesArrayList, this);
        notesList.setAdapter(notesAdapter);
        final String uid = tripBean.getUserId();

        tripImpl = new TripDaoImpl();

        calendar = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

        getTimeDetails(tripBean.getTime().get(0), calendar);
        getDateDetails(tripBean.getDate().get(0), calendar);

        if (spinnerTripType.getSelectedItem().equals("Round Trip")) {
            getTimeDetails(tripBean.getTime().get(1), calendar2);
            getDateDetails(tripBean.getDate().get(1), calendar2);
        }

            // Setup upper toolbar with title and back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_Edit_EditActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.add_trip));

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

                    String firstAlarmId, secondAlarmId;
                    firstAlarmId = String.valueOf(tripBean.getAlarmIds().get(0));

                    if (spinnerTripType.getSelectedItem().equals("Round Trip")) {
                        if (tripBean.getAlarmIds().size() > 1)
                            secondAlarmId = String.valueOf(tripBean.getAlarmIds().get(1));
                        else {
                            secondAlarmId = String.valueOf((int) System.currentTimeMillis());
                            tripBean.addAlarmId(secondAlarmId);
                        }

                        tripImpl.updateTripInfo(tripBean);

                        setAlarm(calendar, firstAlarmId);
                        setAlarm(calendar2, secondAlarmId);
                    } else {
                        tripBean.addAlarmId(String.valueOf(firstAlarmId));
                        tripImpl.updateTripInfo(tripBean);
                        setAlarm(calendar, firstAlarmId);
                    }

                    Toast.makeText(EditTripActivity.this,
                            "Trip added successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else
                    Toast.makeText(EditTripActivity.this,
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
        mDatePicker = new DatePickerDialog(EditTripActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
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
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    void showTimePickerDialog(final String tripDirection) {
        Calendar calendar1 = Calendar.getInstance();
        final int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar1.get(Calendar.MINUTE);

        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(EditTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    private void setAlarm(Calendar mCalendar, String alarmID) {
        intent = new Intent(EditTripActivity.this, MyReceiver.class);

        intent.putExtra(Utils.TRIP_TABLE, tripBean);
        int alarmId = Integer.valueOf(alarmID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(EditTripActivity.this, alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) EditTripActivity.this.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 0, pendingIntent);

    }


    @Override
    protected void onStart() {
        super.onStart();
        PlaceAutocompleteFragment placeAutocompleteFragmentStartPoint = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.editTxt_start_point_EditActivity);
        if (placeAutocompleteFragmentStartPoint != null) {
            placeAutocompleteFragmentStartPoint.setText(tripBean.getStartPoint());
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
        } else Toast.makeText(this, "Problem with loading page", Toast.LENGTH_LONG).show();

        PlaceAutocompleteFragment placeAutoCompleteFragmentEndPoint = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.editTxt_end_point_EditActivity);
        if (placeAutoCompleteFragmentEndPoint != null) {
            placeAutoCompleteFragmentEndPoint.setText(tripBean.getEndPoint());
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
        } else Toast.makeText(this, "Problem with loading page", Toast.LENGTH_LONG).show();
    }

    void getTimeDetails(String time, Calendar calendar5){
        String[] details = time.split(":");
        int hourOfDay = Integer.valueOf(details[0]);
        int minute = Integer.valueOf(details[1]);

        calendar5.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar5.set(Calendar.MINUTE, minute);
    }

    void getDateDetails(String date, Calendar calendar5){
        String[] details = date.split("/");
        int dayOfMonth = Integer.valueOf(details[0]);
        int month = Integer.valueOf(details[1]) - 1;
        int year = Integer.valueOf(details[2]);

        calendar5.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar5.set(Calendar.MONTH, month);
        calendar5.set(Calendar.YEAR, year);
    }
}
