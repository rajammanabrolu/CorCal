import java.io.Serializable;
import java.util.Calendar;
public class MeetingRequest implements Serializable{
    private static final long serialVersionUID=1L;
    public Calendar startTime;
    public Calendar endTime;
    int duration;

    public MeetingRequest(Calendar startTime, Calendar endTime,int duration){
        this.startTime=startTime;
        this.endTime=endTime;
        this.duration=duration;
    }
}