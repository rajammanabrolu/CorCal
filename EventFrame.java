import java.awt.Color;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class EventFrame extends JFrame{
    private Event event;
    private JLabel nameL, startL, endL, descripL, colorL, errorMsg;
    private JTextField nameTF, descripTF, startDCB, endDCB;
    private JComboBox<String> colorCB, startTCB, startACB, startMCB, endTCB, endACB, endMCB;
    private JComboBox<Integer> startYCB, endYCB;
    private JButton confirm, cancel;
    private List<CloseListener> listeners;

    private static final HashMap<Color, Integer> TRANSLATOR = new HashMap<>();
    private static final HashMap<Integer, Color> DETRANSLATOR = new HashMap<>();

    public EventFrame(String s, Event e){
        super(s);
        event=e;
        setUpFrame();
    }
    public static void setUpExtras(WeeklyPanel p){
        TRANSLATOR.put(Color.BLUE, 0);
        TRANSLATOR.put(Color.CYAN, 1);
        TRANSLATOR.put(Color.GREEN, 2);
        TRANSLATOR.put(Color.MAGENTA, 3);
        TRANSLATOR.put(Color.ORANGE, 4);
        TRANSLATOR.put(Color.PINK, 5);
        TRANSLATOR.put(Color.RED, 6);
        TRANSLATOR.put(Color.YELLOW, 7);
        DETRANSLATOR.put(0, Color.BLUE);
        DETRANSLATOR.put(1, Color.CYAN);
        DETRANSLATOR.put(2, Color.GREEN);
        DETRANSLATOR.put(3, Color.MAGENTA);
        DETRANSLATOR.put(4, Color.ORANGE);
        DETRANSLATOR.put(5, Color.PINK);
        DETRANSLATOR.put(6, Color.RED);
        DETRANSLATOR.put(7, Color.YELLOW);
    }
    private void setUpFrame(){
        nameL= new JLabel("Event Name:");
        startL= new JLabel("Starting Time:");
        endL= new JLabel("Ending Time:");
        descripL= new JLabel("Event Description:");
        colorL= new JLabel("Display Color:");
        nameTF= new JTextField();
        descripTF = new JTextField();
        errorMsg = new JLabel("Invalid Input");
        listeners = new ArrayList<>();
        colorCB = new JComboBox<String>(new String[]{"Blue", "Cyan", "Green", "Magenta", "Orange", "Pink", "Red", "Yellow"});
        startMCB = new JComboBox<String>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        startDCB = new JTextField();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        startYCB = new JComboBox<Integer>(new Integer[]{year-2, year-1, year, year+1, year+2, year+3,
                year+4, year+5, year+6, year+7, year+8, year+9, year+10});
        startTCB = new JComboBox<String>(new String[]{"12:00", "12:30", "1:00","1:30", "2:00", "2:30", "3:00", "3:30",
                "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", 
                "7:00", "7:30", "8:00", "8:30", "9:00", "9:30",
                "10:00", "10:30", "11:00", "11:30"});
        startACB = new JComboBox<String>(new String[]{"AM", "PM"});
        endMCB = new JComboBox<String>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        endDCB = new JTextField();
        endYCB = new JComboBox<Integer>(new Integer[]{year-2, year-1, year, year+1, year+2, year+3,
                year+4, year+5, year+6, year+7, year+8, year+9, year+10});
        endTCB = new JComboBox<String>(new String[]{"12:00", "12:30", "1:00","1:30", "2:00", "2:30", "3:00", "3:30",
                "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", 
                "7:00", "7:30", "8:00", "8:30", "9:00", "9:30",
                "10:00", "10:30", "11:00", "11:30"});
        endACB = new JComboBox<String>(new String[]{"AM", "PM"});
        confirm = new JButton("Confirm");
        cancel = new JButton("Cancel");
        errorMsg.setVisible(false);
        if(event.getName()!= null){
            nameTF.setText(event.getName());
        }
        if(event.getDescription() != null){
            descripTF.setText(event.getDescription());
        }
        if(event.getStartTime() !=null){
            startDCB.setText(Integer.toString(event.getStartTime().get(Calendar.DAY_OF_MONTH)));
            startMCB.setSelectedIndex(event.getStartTime().get(Calendar.MONTH));
            startYCB.setSelectedItem(event.getStartTime().get(Calendar.YEAR));
            startTCB.setSelectedIndex(2*event.getStartTime().get(Calendar.HOUR)+((event.getStartTime().get(Calendar.MINUTE)<30)?0:1));
            startACB.setSelectedIndex((event.getStartTime().get(Calendar.AM_PM)== Calendar.AM)?0:1);
        }
        if(event.getEndTime() !=null){
            endDCB.setText(Integer.toString(event.getEndTime().get(Calendar.DAY_OF_MONTH)));
            endMCB.setSelectedIndex(event.getEndTime().get(Calendar.MONTH));
            endYCB.setSelectedItem(event.getEndTime().get(Calendar.YEAR));
            endTCB.setSelectedIndex(2*event.getEndTime().get(Calendar.HOUR)+((event.getEndTime().get(Calendar.MINUTE)<30)?0:1));
            endACB.setSelectedIndex((event.getEndTime().get(Calendar.AM_PM)== Calendar.AM)?0:1);
        }
        if(event.getColor() != null){
            colorCB.setSelectedIndex(TRANSLATOR.get(event.getColor()));   
        }
        cancel.addActionListener(e->{
            setVisible(false);
            dispose();
        });
        confirm.addActionListener(e->{
            try{
                updateEvent();
                setVisible(false);
                dispose();
            } catch(NumberFormatException err){
                errorMsg.setVisible(true);
            }
        });
        setSize(400,400);
        setPreferredSize(new Dimension(400,400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx= 0;
        c.gridy= 0;
        c.anchor=GridBagConstraints.EAST;
        c.fill=GridBagConstraints.BOTH;
        add(nameL,c);
        c.gridy=1;
        add(startL, c);
        c.gridy=3;
        add(endL, c);
        c.gridy=5;
        add(colorL, c);
        c.gridy=6;
        add(descripL, c);
        c.gridy=7;
        add(confirm, c);
        c.gridx=1;
        c.gridy=0;
        add(nameTF, c);
        c.gridy=1;
        add(startMCB, c);
        c.gridy=2;
        add(startTCB, c);
        c.gridy=3;
        add(endMCB, c);
        c.gridy=4;
        add(endTCB, c);
        c.gridy=5;
        add(colorCB, c);
        c.gridy=6;
        c.gridwidth=GridBagConstraints.REMAINDER;
        c.weighty=1;
        add(descripTF, c);
        c.weighty=0;
        c.gridwidth=1;
        c.gridy=7;
        add(cancel, c);
        c.gridy=1;
        c.gridx=2;
        add(startDCB, c);
        c.gridy=2;
        add(startACB, c);
        c.gridy=3;
        add(endDCB, c);
        c.gridy=4;
        add(endACB, c);
        c.gridx=3;
        c.gridy=1;
        add(startYCB, c);
        c.gridy=3;
        add(endYCB, c);
        c.gridy=7;
        add(errorMsg, c);
        pack();
        setVisible(true);
    }

    private void updateEvent(){
        event.setName(nameTF.getText());
        event.setDescription(descripTF.getText());
        event.setColor(DETRANSLATOR.get(colorCB.getSelectedIndex()));
        Calendar start = event.getStartTime();
        int day = Integer.parseInt(startDCB.getText());
        if (day>31) //TODO
            throw new NumberFormatException();
        start.set((Integer)startYCB.getSelectedItem(), startMCB.getSelectedIndex(), Integer.parseInt(startDCB.getText()), startTCB.getSelectedIndex()/2 + ((startACB.getSelectedIndex()==1)?12:0), 30*(startTCB.getSelectedIndex()%2));
        Calendar end = event.getEndTime();
        day = Integer.parseInt(endDCB.getText());
        if (day>31) //TODO
            throw new NumberFormatException();
        end.set((Integer)endYCB.getSelectedItem(), endMCB.getSelectedIndex(), Integer.parseInt(endDCB.getText()), endTCB.getSelectedIndex()/2 + ((endACB.getSelectedIndex()==1)?12:0), 30*(endTCB.getSelectedIndex()%2));
        fireCloseEvent();
    }
    public synchronized void addCloseListener(CloseListener l) {
        listeners.add(l);
    }

    public synchronized void removeCloseListener(CloseListener l) {
        listeners.remove(l);
    }

    private synchronized void fireCloseEvent() {
        for(CloseListener l : listeners){
            l.onClose(new CloseEvent(this));
        }
    }
    public class CloseEvent extends EventObject{
        public CloseEvent(Object source) {
            super(source);
        }
    }
    public interface CloseListener{
        public void onClose(CloseEvent e);
    }

}
