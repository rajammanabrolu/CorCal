import java.awt.Color;
import java.util.Calendar;
import java.util.Comparator;
import java.io.Serializable;

public class CalEvent implements Comparable<CalEvent>,Serializable{
    private static final long serialVersionUID=1L;
    private String name;
    private Calendar startTime;
    private Calendar endTime;
    private Color color;
    private String description;

    public CalEvent(String name, Calendar startTime, Calendar endTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Calendar endTime) {
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

    public boolean spans(Calendar c){
        return endTime.compareTo(c)>0 && startTime.compareTo(c)<0;
    }

    @Override
    public int compareTo(CalEvent e) {
        int comp=endTime.compareTo(e.endTime);
        if(comp != 0)
            return comp;
        comp=startTime.compareTo(e.startTime);
        if(comp != 0)
            return comp;
        return name.compareTo(e.name);
    }

    public static class byStartTime implements Comparator<CalEvent>{
        @Override public int compare(CalEvent a, CalEvent b){
            int comp=a.startTime.compareTo(b.startTime);
            if(comp != 0)
                return comp;
            comp=a.endTime.compareTo(b.endTime);
            if(comp != 0)
                return comp;
            return a.name.compareTo(b.name);
        }
    }
}
