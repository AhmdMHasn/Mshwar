package eg.com.iti.mshwar.beans;

import java.util.ArrayList;
import java.util.List;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.util.Utils;

public class TripBean {

    // please add alarm id
    private String alarmId;
    private String key;
    private String name;
    private String startPoint;
    private String endPoint;
    private String repetition;
    private String type;
    private String date;
    private String time;
    private String status;
    private  ArrayList<String> notes = new ArrayList<>();

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public  void appendNotes(String note){
        this.notes.add(note);
    }

    private Double startPointLongitude;
    private Double startPointLatitude;
    private Double endPointLongitude;
    private Double endPointLatitude;

    public TripBean(){

    }

    public int getStatusImage() {
        switch (status){
            case Utils.UPCOMING:
                statusImage = R.drawable.upcoming;
                break;
            case Utils.DONE:
                statusImage = R.drawable.done;
                break;
            case Utils.CANCELED:
                statusImage = R.drawable.canceled;
                break;
            default:
                statusImage = R.drawable.logo;
                break;
        }
        return statusImage;
    }

    public void setStatusImage(int statusImage) {
        this.statusImage = statusImage;
    }

    private int statusImage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getStartPointLongitude() {
        return startPointLongitude;
    }

    public void setStartPointLongitude(Double startPointLongitude) {
        this.startPointLongitude = startPointLongitude;
    }

    public Double getStartPointLatitude() {
        return startPointLatitude;
    }

    public void setStartPointLatitude(Double startPointLatitude) {
        this.startPointLatitude = startPointLatitude;
    }

    public Double getEndPointLongitude() {
        return endPointLongitude;
    }

    public void setEndPointLongitude(Double endPointLongitude) {
        this.endPointLongitude = endPointLongitude;
    }

    public Double getEndPointLatitude() {
        return endPointLatitude;
    }

    public void setEndPointLatitude(Double endPointLatitude) {
        this.endPointLatitude = endPointLatitude;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public static List<TripBean> getTripData(String userId) {
        ArrayList<TripBean> tripsArrayList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            TripBean trip = new TripBean();
            trip.setKey("" + i);
            trip.setName("Trip " + i);
            trip.setStartPoint("Start point " + i);
            trip.setEndPoint("End point " + i);
            trip.setStatus(Utils.UPCOMING);
            trip.setStartPointLatitude(30.6210726);
            trip.setStartPointLongitude(32.2690443);
            trip.setEndPointLatitude(31.033831);
            trip.setEndPointLongitude(31.383138);
            tripsArrayList.add(trip);
        }

        TripBean trip1 = new TripBean();
        trip1.setKey("");
        trip1.setName("Trip ");
        trip1.setStartPoint("Start point ");
        trip1.setEndPoint("End point ");
        trip1.setStatus(Utils.CANCELED);
        tripsArrayList.add(trip1);

        TripBean trip2 = new TripBean();
        trip2.setKey("");
        trip2.setName("Trip ");
        trip2.setStartPoint("Start point ");
        trip2.setEndPoint("End point ");
        trip2.setStartPointLatitude(30.037948);
        trip2.setStartPointLongitude(31.216393);
        trip2.setEndPointLatitude(30.0410286);
        trip2.setEndPointLongitude(31.2172423);
        trip2.setStatus(Utils.DONE);
        tripsArrayList.add(trip2);
        return tripsArrayList;
    }

    @Override
    public String toString() {
        return "TripBean{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", repetition='" + repetition + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                ", startPointLongitude=" + startPointLongitude +
                ", startPointLatitude=" + startPointLatitude +
                ", endPointLongitude=" + endPointLongitude +
                ", endPointLatitude=" + endPointLatitude +
                ", statusImage=" + statusImage +
                '}';
    }
}
