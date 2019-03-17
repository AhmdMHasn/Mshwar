package eg.com.iti.mshwar.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.dialog.AddNoteDialog;
import eg.com.iti.mshwar.model.TripDao;

public class TripActivity extends AppCompatActivity implements AddNoteDialog.AddNoteDialogListener {
    EditText editTxtTripName, editTxtStartPoint, editTxtEndPoint;
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
        editTxtTripName = (EditText) findViewById(R.id.editTxt_trip_name);
        editTxtStartPoint = (EditText) findViewById(R.id.editTxt_start_point);
        editTxtEndPoint = (EditText) findViewById(R.id.editTxt_end_point);
        spinnerTripType = (Spinner) findViewById(R.id.spinner_trip_repetition);
        spinnerTripRepetition = (Spinner) findViewById(R.id.spinner_trip_repetition);
        txtViewDate = (TextView) findViewById(R.id.txtView_date);
        txtViewTime = (TextView) findViewById(R.id.txtView_time);
        btnAddTrip = (Button) findViewById(R.id.btn_add_trip);
        addNote = (ImageView) findViewById(R.id.add_note);

        // btnAddTrip.setOnClickListener(onClickListener);
        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code to start trip
                tripBean.setName(editTxtTripName.getText().toString());
                tripImpl = new TripDaoImpl();
                tripImpl.addTrip(tripBean);
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
}
