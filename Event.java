import java.util.Calendar;

public class Event {

	private String name;
	private Calendar startTime;
	private Calendar endTime;

	public Event(String name, Calendar startTime, Calendar endTime) {
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
}
