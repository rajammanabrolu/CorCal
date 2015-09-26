import java.util.Calendar;
import java.io.Serializable;
import java.util.TreeSet;

public class User implements Serializable{
    
    private static final long serialVersionUID=1L;
    private String name;
    TreeSet<Event> events=new TreeSet<>();
    
    public User(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public TreeSet<Event> getEvents(){
        return events;
    }
    
    public void addEvent(Event e){
        events.add(e);
    }
}