import java.util.Calendar;
import java.io.Serializable;
import java.util.TreeSet;
import java.io.File;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

	public void createBitField (Calendar tStamp1, Calendar tStamp2) throws IOException, FileNotFoundException{
		FileOutputStream fos = new FileOutputStream("bitField.txt");
	    ObjectOutputStream bitFieldWriter = new ObjectOutputStream(fos);
	
	    int finalDiff = Math.abs(tStamp1.DAY_OF_YEAR - tStamp2.DAY_OF_YEAR);
	    int hourDiff = Math.abs(tStamp1.HOUR - tStamp2.HOUR);
	    int[] bitField = new int[48 * finalDiff + 2 * hourDiff];
	
	    for (int i = 0; i < 48 * finalDiff + 2 * hourDiff; ++i) {
	        bitField[i] = 0;
	    }
	
	    for (Event e : events) {
            if (e.getStartTime().DAY_OF_YEAR != 0) {
                bitField[e.getStartTime().DAY_OF_YEAR - tStamp1.DAY_OF_YEAR * 48 + (e.getStartTime().HOUR - tStamp1.DAY_OF_YEAR) * 2] = 1;
            }
	    }
	    bitFieldWriter.writeObject(bitField);	
	}
	
	public Calendar[] readFiles(Calendar tStamp1, Calendar tStamp2, File[] fileArray) throws IOException, ClassNotFoundException {
		Scanner fs = new Scanner("bitField.txt");
		FileInputStream fis = new FileInputStream("bitField.txt");
	    ObjectInputStream reader = new ObjectInputStream(fis);
	    ArrayList<int[]>bitFields = new ArrayList<int[]>();
	    while (fs.hasNext()) {
	    	bitFields.add((int[]) reader.readObject());
	    }
	    int counter = 0;
	    int flag = 0;
	    int finalDiff = Math.abs(tStamp1.DAY_OF_YEAR - tStamp2.DAY_OF_YEAR);
	    int endTime = 0;
	    int startTime = 0;
	    int freeHours = Math.abs((tStamp1.HOUR - tStamp2.HOUR)) + finalDiff * 24;
	    int timeDiff = freeHours * 2;
	    for (int i = 0; i < bitFields.size(); ++i) {
	        for (int j = 0; j < freeHours * 2; ++j) {
	            if (bitFields.get(i + j) == bitFields.get(i + j + 1)) {
	                counter++;
	                if (counter == timeDiff + 2) {
	                    endTime = i + j;
	                    startTime = i + j - timeDiff;
	                    flag = 1;
	                } 
	            }
	        }
	    }
	    if (flag == 0) {
	        for (int i = 0; i < bitFields.size(); ++i) {
	            for (int j = 0; j < freeHours * 2; ++j) {
	                if (bitFields.get(i + j) == bitFields.get(i + j + 1)) {
	                    counter++;
	                    if (counter == timeDiff + 2) {
	                        endTime = i + j + 1;
	                        startTime = i + j - timeDiff - 1;
	                        flag = 1;
	                    }
	                }
	            }
	        }
	    }
	    if (flag == 0) {
	        endTime = startTime = 0;
	    }
	
	    Calendar finalTimeStart = tStamp1;
	    Calendar finalTimeEnd = tStamp2;
	    finalTimeStart.set(tStamp1.YEAR, finalTimeStart.DAY_OF_YEAR + startTime / 48);
	    finalTimeStart.set(tStamp1.HOUR, finalTimeStart.HOUR + (startTime % 48) / 2);
	    finalTimeEnd.set(tStamp2.YEAR, finalTimeEnd.DAY_OF_YEAR + endTime / 48);
	    finalTimeEnd.set(tStamp2.HOUR, finalTimeEnd.HOUR + (endTime % 48) /2);
	    Calendar[] finalTimings = {finalTimeStart , finalTimeEnd};
	    return finalTimings;
	}

    public void addEvent(Event e){
        events.add(e);
    }
}