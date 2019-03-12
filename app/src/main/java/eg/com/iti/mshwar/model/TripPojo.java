package eg.com.iti.mshwar.model;

import java.util.ArrayList;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.util.Utils;

enum Repetition{
    DAILY,
    WEEKLY,
    MONTHLY;
}

enum TripType{
    SINGLE, ROUNDED;
}

enum Status{
    UPCOMING (Utils.UPCOMING),
    DONE (Utils.DONE),
    CANCELED (Utils.CANCELED);

    private String text;

    Status(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}

public class TripPojo {
    private String tripId;
    private String userId;
    private String tripName;
    private String tripStartPoint;
    private String tripEndPoint;
    private Repetition tripRepetition;
    private TripType tripType;
    private Status tripStatus;
    private int tripStatusImage;
    private String[] tripNotes;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripStartPoint() {
        return tripStartPoint;
    }

    public void setTripStartPoint(String tripStartPoint) {
        this.tripStartPoint = tripStartPoint;
    }

    public String getTripEndPoint() {
        return tripEndPoint;
    }

    public void setTripEndPoint(String tripEndPoint) {
        this.tripEndPoint = tripEndPoint;
    }

    public Repetition getTripRepetition() {
        return tripRepetition;
    }

    public void setTripRepetition(Repetition tripRepetition) {
        this.tripRepetition = tripRepetition;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public String getTripStatus() {
        return tripStatus.getText();
    }

    public void setTripStatus(Status tripStatus) {
        this.tripStatus = tripStatus;
    }

    public int getTripStatusImage() {
        switch (tripStatus){
            case UPCOMING:
                tripStatusImage = R.drawable.upcoming;
                break;
            case DONE:
                tripStatusImage = R.drawable.done;
                break;
            case CANCELED:
                tripStatusImage = R.drawable.canceled;
                break;
            default:
                tripStatusImage = R.drawable.logo;
                break;
        }
        return tripStatusImage;
    }

    public String[] getTripNotes() {
        return tripNotes;
    }

    public void setTripNotes(String[] tripNotes) {
        this.tripNotes = tripNotes;
    }

    TripPojo(){

    }

    public static ArrayList<TripPojo> getTripData(String userId){
        ArrayList<TripPojo> tripsArrayList = new ArrayList<>();

        for (int i = 0; i < 4; i++){
            TripPojo trip = new TripPojo();
            trip.setTripId("" + i);
            trip.setTripName("Trip " + i);
            trip.setTripStartPoint("Start point " + i);
            trip.setTripEndPoint("End point " + i);
            trip.setTripStatus(Status.UPCOMING);
            tripsArrayList.add(trip);
        }

        TripPojo trip1 = new TripPojo();
        trip1.setTripId("");
        trip1.setTripName("Trip ");
        trip1.setTripStartPoint("Start point ");
        trip1.setTripEndPoint("End point ");
        trip1.setTripStatus(Status.CANCELED);
        tripsArrayList.add(trip1);

        TripPojo trip2 = new TripPojo();
        trip2.setTripId("");
        trip2.setTripName("Trip ");
        trip2.setTripStartPoint("Start point ");
        trip2.setTripEndPoint("End point ");
        trip2.setTripStatus(Status.DONE);
        tripsArrayList.add(trip2);
        return tripsArrayList;
    }

}
