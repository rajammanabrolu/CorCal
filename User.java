import java.util.Calendar;
import java.util.NavigableSet;
import java.io.Serializable;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.io.File;
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
    public void createBitField (Calendar tStamp1, Calendar tStamp2, File f) throws IOException, FileNotFoundException{
        FileOutputStream fos = new FileOutputStream(f,false);
        ObjectOutputStream bitFieldWriter = new ObjectOutputStream(fos);
        int yearDiff = tStamp2.get(Calendar.YEAR)-tStamp1.get(Calendar.YEAR); 
        int dayDiff = tStamp2.get(Calendar.DAY_OF_YEAR) - tStamp1.get(Calendar.DAY_OF_YEAR);
        dayDiff+=(365*yearDiff);
        int halfHourDiff = 2*(tStamp2.get(Calendar.HOUR_OF_DAY) - tStamp1.get(Calendar.HOUR_OF_DAY));
        int[] bitField = new int[(dayDiff+1)*halfHourDiff];


        Calendar tempTime = (Calendar) tStamp1.clone();
        tempTime.add(Calendar.MINUTE, 15);
        NavigableSet<Event> spanEvents = new TreeSet<>();
        for(Event e: events){
            if (e.getEndTime().compareTo(tStamp1)>0 && e.getStartTime().compareTo(tStamp2) < 0)
                spanEvents.add(e);
        }

        for(int i=0; i<bitField.length; i++){
            for(Event e: spanEvents){
                if(e.spans(tempTime)){
                    bitField[i]=1;
                }
            }
            tempTime.add(Calendar.MINUTE, 30);
            if((i+1)%halfHourDiff==0){
                tempTime.set(Calendar.MINUTE, tStamp1.get(Calendar.MINUTE)+15);
                tempTime.set(Calendar.HOUR_OF_DAY, tStamp1.get(Calendar.HOUR_OF_DAY));
                tempTime.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        bitFieldWriter.writeObject(bitField);
        bitFieldWriter.close();
    }

    public Calendar[] readFiles(Calendar tStamp1, Calendar tStamp2, File[] fileArray, int meetingLength) throws IOException, ClassNotFoundException, ExecutionException {
        ArrayList<int[]> bfs = new ArrayList<int[]>();
        int halfHourDiff = 2*(tStamp2.get(Calendar.HOUR_OF_DAY) - tStamp1.get(Calendar.HOUR_OF_DAY));

        for (File f: fileArray){
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream reader = new ObjectInputStream(fis);
            bfs.add((int[])reader.readObject());
            fis.close();
            reader.close();
        }

        int globalMax = 0;
        int localMax = 0;
        int globalStart = 0;
        int localStart = 0;
        for(int i=0; i<bfs.get(0).length; i++){
            for(int j=1; j<bfs.size() && bfs.get(0)[i]==0; j++){
                if(bfs.get(j)[i]==1){
                    bfs.get(0)[i]=1;
                }
            }
            if(bfs.get(0)[i]==0){
                localMax++;
                if(localMax>=meetingLength){
                    globalMax = localMax;
                    globalStart = localStart;
                    break;
                }
            } 
            if(bfs.get(0)[i]==1 || (i+1)%halfHourDiff==0){
                if (localMax> globalMax){
                    globalMax = localMax;
                    globalStart = localStart;
                }
                localStart = i+1;
                localMax= 0;
            }
            
        }
        if (localMax > globalMax){
            globalMax = localMax;
            globalStart = localStart;
        }
        if(globalMax < meetingLength){
            throw new ExecutionException(new Throwable("No Valid Answer Found"));
        }
        
        Calendar start= (Calendar) tStamp1.clone();
        Calendar end = (Calendar) tStamp1.clone();
        int globalEnd = globalStart + globalMax;
        start.add(Calendar.MINUTE, 30*(globalStart%halfHourDiff));
        start.add(Calendar.DAY_OF_MONTH, globalStart/halfHourDiff);
        end.add(Calendar.MINUTE, 30*(globalEnd%halfHourDiff));
        end.add(Calendar.DAY_OF_MONTH, globalEnd/halfHourDiff);
        Calendar[] finalTimings = {start, end};
        return finalTimings;
        
    }

    public void addEvent(Event e) {
        events.add(e);
    }
}