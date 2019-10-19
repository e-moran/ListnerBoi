package ie.eointm.listnerboi;

public class Show {
    enum Day {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    private String showName;
    private String emailAddress;
    private Day day;
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

    public Day getDay() {
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
                "}\n";
    }

    public static Day convertDay(int day) {
        switch(day) {
            case 1:
                return Day.MONDAY;
            case 2:
                return Day.TUESDAY;
            case 3:
                return Day.WEDNESDAY;
            case 4:
                return Day.THURSDAY;
            case 5:
                return Day.FRIDAY;
            case 6:
                return Day.SATURDAY;
            case 7:
                return Day.SUNDAY;
            default:
                throw new IllegalArgumentException("Not a valid day");
        }
    }
}
