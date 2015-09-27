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

	private static final long serialVersionUID = 1L;
	private String name;
    TreeSet<Event> events = new TreeSet<>();

	public User(String name) {
		this.name = name;
	}

    public String getName(){
        return name;
    }

    public TreeSet<Event> getEvents(){
        return events;
    }

	public void createBitField (Calendar tStamp1, Calendar tStamp2, File file) throws IOException, FileNotFoundException{
		FileOutputStream fos = new FileOutputStream(file,false);
	    ObjectOutputStream bitFieldWriter = new ObjectOutputStream(fos);
	    int finalDiff = Math.abs(tStamp1.get(Calendar.DAY_OF_YEAR) - tStamp2.get(Calendar.DAY_OF_YEAR));
	    int hourDiff = Math.abs(tStamp1.get(Calendar.HOUR) - tStamp2.get(Calendar.HOUR));
	    int[] bitField = new int[finalDiff * hourDiff * 2];
        //System.out.printf("%d %d%n",tStamp1.get(Calendar.DAY_OF_YEAR) , tStamp1.get(Calendar.HOUR));
        for(Event e:events){
            //System.out.printf("%d %d%n", e.getStartTime().get(Calendar.DAY_OF_YEAR), e.getStartTime().get(Calendar.HOUR));
            for (int i = 0; i < finalDiff; ++i) {
                for (int j = 0; j < hourDiff * 2; ++j) {
                    if (e.getStartTime().get(Calendar.DAY_OF_YEAR) == tStamp1.get(Calendar.DAY_OF_YEAR) + i
                        && e.getStartTime().get(Calendar.HOUR) == tStamp1.get(Calendar.HOUR) + j) {
                        bitField[i + j] = 1;
                        bitField[i + j + 1] = 1;
                    }
                }
            }
        }
	    bitFieldWriter.writeObject(bitField);
<<<<<<< HEAD
	    //for (int i = 0; i < bitField.length; i++) {
	    	//System.out.print(bitField[i]);
	    //}
=======
	    for (int i = 0; i < bitField.length; i++) {
	    	//System.out.print(bitField[i]);
	    }
>>>>>>> refs/remotes/origin/Mason
        bitFieldWriter.close();
	}

	public Calendar[] readFiles(Calendar tStamp1, Calendar tStamp2, File[] fileArray, int meetingLength) throws IOException, ClassNotFoundException {
		Scanner fs = new Scanner("bitField.txt");
		FileInputStream fis = new FileInputStream("bitField.txt");
	    ObjectInputStream reader = new ObjectInputStream(fis);
	    ArrayList<int[]>bitFields = new ArrayList<int[]>();
	    while (fs.hasNext()) {
	    	bitFields.add((int[]) reader.readObject());
	    }
	    int counter = 0;
	    int flag = 0;
	    int finalDiff = Math.abs(tStamp1.get(Calendar.DAY_OF_YEAR) - tStamp2.get(Calendar.DAY_OF_YEAR));
	    int endTime = 0;
	    int startTime = 0;
	    int hourDiff = Math.abs((tStamp1.get(Calendar.HOUR) - tStamp2.get(Calendar.HOUR)));
        int timeDiff = hourDiff * 2;
	    for (int i = 0; i < finalDiff; ++i) {
	        for (int j = 0; j < hourDiff * 2; ++j) {
	            if (bitFields.get(i + j) == bitFields.get(i + j + 1)) {
	                counter++;
	                if (counter == meetingLength + 2) {
	                    endTime = i + j + 1;
	                    startTime = i + j - meetingLength - 1;
	                    flag = 1;
	                }
	            } else {
	            	counter = 0;
	            }
               if (i + j == hourDiff * 2) {
                   counter = 0;
               }
	        }
	    }
	    if (flag == 0) {
	        for (int i = 0; i < finalDiff; ++i) {
	            for (int j = 0; j < hourDiff; ++j) {
	                if (bitFields.get(i + j) == bitFields.get(i + j + 1)) {
	                    counter++;
	                    if (counter == meetingLength + 2) {
	                        endTime = i + j + 1;
	                        startTime = i + j - meetingLength - 1;
	                        flag = 1;
	                    } else {
                            counter = 0;
                        }
                        if (i + j == hourDiff * 2) {
                            counter = 0;
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
	    finalTimeStart.set(tStamp1.get(Calendar.YEAR), finalTimeStart.get(Calendar.DAY_OF_YEAR) + (int) startTime / (hourDiff * 2));
	    finalTimeStart.set(tStamp1.get(Calendar.HOUR), finalTimeStart.get(Calendar.HOUR) + (startTime % (hourDiff * 2)) / 2);
	    finalTimeEnd.set(tStamp2.get(Calendar.YEAR), finalTimeEnd.get(Calendar.DAY_OF_YEAR) + endTime / (hourDiff * 2));
	    finalTimeEnd.set(tStamp2.get(Calendar.HOUR), finalTimeEnd.get(Calendar.HOUR) + (endTime % (hourDiff * 2) /2));
	    Calendar[] finalTimings = {finalTimeStart , finalTimeEnd};
        reader.close();
        fs.close();
	    return finalTimings;
	}

	public static void main(String[] args) throws IOException, FileNotFoundException {
		User myUser = new User("Nikola");
		Calendar now = Calendar.getInstance();
		Calendar later = Calendar.getInstance();
		later.set(2015, 8, 25, 9, 0);
		Calendar moreLater = Calendar.getInstance();
		moreLater.set(2015, 8, 29, 12, 0);
        //System.out.println("now " + now.get(Calendar.DAY_OF_YEAR) + " later " + later.get(Calendar.DAY_OF_YEAR));
        //System.out.println("More later " + moreLater.get(Calendar.DAY_OF_YEAR));
		myUser.getEvents().add(new Event("To laundry", now, later));
		myUser.getEvents().add(new Event("To laundry", later, moreLater));
		myUser.createBitField(later, moreLater, new File("bitfield.txt"));
	}

    public void addEvent(Event e) {
        events.add(e);
    }
}
