package eg.com.iti.mshwar.Beans;

public class TripBean {

    private String tripKey;
    private String nameOfTrip;
    private String startPoint;
    private String endPoint;
    private String Repetition;
    private String typeOFTrip;
    private String date;
    private String time;
    private Double langitutdeOfStartPoint;
    private  Double latitudeOfStartPoint;
    private Double langitutdeOfEndPoint;
    private  Double latitudeOfEndPoint;
    public void setNameOfTrip(String nameOfTrip) {
        this.nameOfTrip = nameOfTrip;
    }

    public void setTripKey(String tripKey) {
        this.tripKey = tripKey;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setTypeOFTrip(String typeOFTrip) {
        this.typeOFTrip = typeOFTrip;
    }

    public void setRepetition(String repetition) {
        Repetition = repetition;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLangitutdeOfStartPoint(Double langitude) {
        this.langitutdeOfStartPoint = langitude;
    }

    public void setLatitudeOfStartPoint(Double latitude) {
        this.latitudeOfStartPoint = latitude;
    }

    public void setLangitutdeOfEndPoint(Double langitutdeOfEndPoint) {
        this.langitutdeOfEndPoint = langitutdeOfEndPoint;
    }

    public void setLatitudeOfEndPoint(Double latitudeOfEndPoint) {
        this.latitudeOfEndPoint = latitudeOfEndPoint;
    }

    public String getTripKey() {
        return tripKey;
    }

    public String getNameOfTrip() {
        return nameOfTrip;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getTypeOFTrip() {
        return typeOFTrip;
    }

    public String getRepetition() {
        return Repetition;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public Double getLangitutdeOfStartPoint() {
        return langitutdeOfStartPoint;
    }

    public Double getLatitudeOfStartPoint() {
        return latitudeOfStartPoint;
    }

    public Double getLangitutdeOfEndPoint() {
        return langitutdeOfEndPoint;
    }

    public Double getLatitudeOfEndPoint() {
        return latitudeOfEndPoint;
    }
}
