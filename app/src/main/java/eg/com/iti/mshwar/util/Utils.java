package eg.com.iti.mshwar.util;

public final class Utils {

    // Tables
    public static final String TRIP_TABLE = "trip";
    public static final String USER_TABLE = "user";

    // Trip Table
    public static final String COLUMN_TRIP_ID = "id";
    public static final String COLUMN_TRIP_NAME = "name";
    public static final String COLUMN_TRIP_START_POINT = "startPoint";
    public static final String COLUMN_TRIP_END_POINT = "endPoint";
    public static final String COLUMN_TRIP_REPETITION = "repetition";
    public static final String COLUMN_TRIP_STATUS = "status";
    public static final String COLUMN_TRIP_NOTES = "notes";
    public static final String COLUMN_TRIP_TRIP_TYPE = "type";
    public static final String COLUMN_TRIP_Date = "date";
    public static final String COLUMN_TRIP_Time = "time";
    public static final String COLUMN_TRIP_USER_ID = "user_id";
    public static final String COLUMN_TRIP_START_POINT_LATITUDE ="startPointLatitude";
    public static final String COLUMN_TRIP_START_POINT_LONGITUDE ="startPointLongitude";
    public static final String COLUMN_TRIP_END_POINT_LATITUDE ="endPointLatitude";
    public static final String COLUMN_TRIP_END_POINT_LONGITUDE ="endPointLongitude";

    // User Table
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PICTURE = "image";

    // Constants
    public static final int ADD = 0;
    public static final int REPLACE = 1;
    public static final String UPCOMING = "UPCOMING";
    public static final String CANCELED = "CANCELED";
    public static final String DONE = "DONE";
    public static final String ALL = "ALL";

    // Repetition
    public static final String DAILY = "DAILY";
    public static final String WEEKLY = "WEEKLY";
    public static final String MONTHLY = "MONTHLY";

    // Direction
    public static final int ONE_DIRECTION = 0;
    public static final int TWO_DIRECTIONS = 1;
}
