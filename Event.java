import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Event implements Comparable<Event>{

    private String name;
    private Calendar startTime;
    private Calendar endTime;

    public Event(String name, Calendar startTime, Calendar endTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1800000;
    }

    @Override
    public int compareTo(Event e) {
        return endTime.compareTo(e.endTime);
    }
}