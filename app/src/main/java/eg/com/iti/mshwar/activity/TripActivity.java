package eg.com.iti.mshwar.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.fragment.DatePickerFragment;
import eg.com.iti.mshwar.fragment.TimePickerFragment;
import eg.com.iti.mshwar.model.TripDao;

public class TripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText nameOfTrip, startPoint, endPoint;
    Spinner typeOfTrip, repetition;
    TextView Date, Time;
    Button addButton;
    TripBean tripBean;
    TripDao tripDao;
    String tripNameText, startPointText, endpointText,
            dateText, timeText, repetitionText, typeoftripText;
    Double langitutdeOfStartPoint, latitudeOfStartPoint, langitutdeOfEndPoint, latitudeOfEndPoint;
    int REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        tripBean = new TripBean();

        nameOfTrip = findViewById(R.id.TripNameId);
        startPoint = findViewById(R.id.startpointId);
        endPoint = findViewById(R.id.endpoint);
        typeOfTrip = findViewById(R.id.typeoftrip);
        repetition = findViewById(R.id.repetitionid);
        Date = findViewById(R.id.dateId);
        Time = findViewById(R.id.TimeId);
        addButton = findViewById(R.id.addBtn);
        addButton.setOnClickListener(onClickListener);
        startPoint.setOnClickListener(onClickListenerpoint);
        endPoint.setOnClickListener(onClickListenerpoint);


        Time.setOnClickListener(onClickListenerTimeAndDate);
        Date.setOnClickListener(onClickListenerTimeAndDate);


        typeOfTrip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeoftripText = adapterView.getItemAtPosition(i).toString();
                tripBean.setType(typeoftripText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        repetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                repetitionText = adapterView.getItemAtPosition(i).toString();
                tripBean.setRepetition(repetitionText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tripNameText = nameOfTrip.getText().toString();
            if (tripNameText == null || startPoint.getText() == null || endPoint.getText() == null) {
                Toast.makeText(TripActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
            tripBean.setName(tripNameText);
            tripDao = new TripDao(tripBean);
        }
    };
    View.OnClickListener onClickListenerpoint = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setCountry("EG")
                        .build();
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter)
                                .build(TripActivity.this);
                switch (view.getId()) {
                    case R.id.startpointId:
                        REQUEST_CODE = 1;

                        break;

                    case R.id.endpoint:
                        REQUEST_CODE = 2;

                        break;
                }
                startActivityForResult(intent, REQUEST_CODE);


            } catch (GooglePlayServicesRepairableException |
                    GooglePlayServicesNotAvailableException e) {
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(TripActivity.this, data);
                startPointText = place.getName().toString();
                startPoint.setText(startPointText);
                tripBean.setStartPoint(startPointText);
                latitudeOfStartPoint = place.getLatLng().latitude;
                langitutdeOfStartPoint = place.getLatLng().longitude;
                tripBean.setStartPointLatitude(latitudeOfStartPoint);
                tripBean.setStartPointLongitude(langitutdeOfStartPoint);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                Status status = PlaceAutocomplete.getStatus(TripActivity.this, data);
                Toast.makeText(TripActivity.this, status + "", Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(TripActivity.this, data);
                endpointText = place.getName().toString();
                endPoint.setText(endpointText);
                tripBean.setEndPoint(endpointText);
                latitudeOfEndPoint = place.getLatLng().latitude;
                langitutdeOfEndPoint = place.getLatLng().longitude;
                tripBean.setEndPointLatitude(latitudeOfEndPoint);
                tripBean.setEndPointLongitude(langitutdeOfEndPoint);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                Status status = PlaceAutocomplete.getStatus(TripActivity.this, data);
                Toast.makeText(TripActivity.this, status + "", Toast.LENGTH_SHORT).show();

            }
        }
    }

    View.OnClickListener onClickListenerTimeAndDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.TimeId:
                    DialogFragment TimedialogFragment = new TimePickerFragment();
                    TimedialogFragment.show(getSupportFragmentManager(), "time picker");
                    break;
                case R.id.dateId:
                    DialogFragment DatedialogFragment = new DatePickerFragment();
                    DatedialogFragment.show(getSupportFragmentManager(), "date picker");
                    break;
            }
        }
    };

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {

        String pm_am = "AM";
        if (hour >= 12) {
            pm_am = "PM";
        }
        if ((hour == 12 || hour == 0)) {
            hour = 12;
        } else
            hour = hour % 12;

        timeText = hour + ":" + String.format("%02d", minutes) + " " + pm_am;

        Time.setText(timeText);
        tripBean.setTime(timeText);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        dateText = day + "/" + month + "/" + year;
        Date.setText(dateText);
        tripBean.setDate(dateText);
    }
}
