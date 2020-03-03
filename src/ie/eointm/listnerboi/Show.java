package ie.eointm.listnerboi;

import java.util.Arrays;
import java.util.Calendar;

public class Show {
    private String showName;
    private String[] emailAddresses;
    private int day;
    private int startHr;
    private int startMin;
    private int startSec;
    private int runtime; // In seconds

    public Show(String showName, String[] emailAddresses, int day, int startHr, int startMin, int startSec, int runtime) {
        this.showName = showName;
        this.emailAddresses = emailAddresses;
        this.day = convertDay(day);
        this.startHr = startHr;
        this.startMin = startMin;
        this.startSec = startSec;
        this.runtime = runtime;
    }

    public String getShowName() {
        return showName;
    }

    public String[] getEmailAddress() {
        return emailAddresses;
    }

    public int getDay() {
        return day;
    }

    public int getStartHr() {
        return startHr;
    }

    public int getStartMin() {
        return startMin;
    }

    public int getStartSec() {
        return startSec;
    }

    public int getRuntime() {
        return runtime;
    }

    @Override
    public String toString() {
        return "Show{" +
                "showName='" + showName + '\'' +
                ", emailAddresses='" + Arrays.toString(emailAddresses) + '\'' +
                ", day=" + day +
                ", startHr=" + startHr +
                ", startMin=" + startMin +
                ", startSec=" + startSec +
                ", runtime=" + runtime +
                "}";
    }

    public static int convertDay(int day) {
        switch(day) {
            case 1:
                return Calendar.MONDAY;
            case 2:
                return Calendar.TUESDAY;
            case 3:
                return Calendar.WEDNESDAY;
            case 4:
                return Calendar.THURSDAY;
            case 5:
                return Calendar.FRIDAY;
            case 6:
                return Calendar.SATURDAY;
            case 7:
                return Calendar.SUNDAY;
            default:
                throw new IllegalArgumentException("Not a valid day");
        }
    }
}
