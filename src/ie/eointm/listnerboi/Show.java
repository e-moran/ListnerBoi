package ie.eointm.listnerboi;

import java.util.Calendar;

public class Show {
    private String showName;
    private String emailAddress;
    private int day;
    private int startHr;
    private int startMin;
    private int startSec;
    private int runtime; // In seconds

    public Show(String showName, String emailAddress, int day, int startHr, int startMin, int startSec, int runtime) {
        this.showName = showName;
        this.emailAddress = emailAddress;
        this.day = convertDay(day);
        this.startHr = startHr;
        this.startMin = startMin;
        this.startSec = startSec;
        this.runtime = runtime;
    }

    public String getShowName() {
        return showName;
    }

    public String getEmailAddress() {
        return emailAddress;
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
                ", emailAddress='" + emailAddress + '\'' +
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
