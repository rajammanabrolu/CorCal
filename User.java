import java.util.Calendar;
import java.util.TreeSet;

public class User {
    
    private String name;
    TreeSet<Event> events=new TreeSet<>;
    
    public User(name) {
        this.name = name;
        this.calendar = calendar;
    }

    public String getName(){
        return name;
    }

    public TreeSet<Event> getEvents(){
        return events;
    }
}