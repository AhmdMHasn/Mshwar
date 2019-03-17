package eg.com.iti.mshwar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.dialog.AddNoteDialog;

public class TripActivity extends AppCompatActivity implements AddNoteDialog.AddNoteDialogListener {
    private static final String TAG = "Error";
    EditText editTxtTripName;
    Spinner spinnerTripType, spinnerTripRepetition;
    TextView txtViewDate, txtViewTime;
    Button btnAddTrip;
    TripBean tripBean;
    TripDaoImpl tripImpl;
    ImageView addNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        tripBean = new TripBean();
        editTxtTripName = findViewById(R.id.editTxt_trip_name);
        spinnerTripType = findViewById(R.id.spinner_trip_repetition);
        spinnerTripRepetition = findViewById(R.id.spinner_trip_repetition);
        txtViewDate = findViewById(R.id.txtView_date);
        txtViewTime = findViewById(R.id.txtView_time);
        btnAddTrip = findViewById(R.id.btn_add_trip);
        addNote = findViewById(R.id.add_note);

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code to add trip
                tripBean.setName(editTxtTripName.getText().toString());
                if(tripBean.getName()!= null && tripBean.getStartPoint() != null && tripBean.getEndPoint() != null) {
                    /*
                    && tripBean.getTime()!= null && tripBean.getDate() != null
                     */
                    Toast.makeText(TripActivity.this, tripBean.getName()+"hello", Toast.LENGTH_LONG).show();
                    tripImpl = new TripDaoImpl();
                    tripImpl.addTrip(tripBean);
                }
                else Toast.makeText(TripActivity.this, R.string.add_trip_error_message, Toast.LENGTH_LONG).show();

            }
        });


        txtViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        txtViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
