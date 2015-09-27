import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.AbstractCellEditor;

import javax.swing.table.TableCellEditor;
import javax.swing.table.AbstractTableModel;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

public class MonthView extends JPanel{
    private User user;
    private CalendarTableModel model;
    private Calendar now;
    private JLabel display;
    private JScrollPane pane;
    private JTable table;
    private EventFrame eFrame;
    private JButton eventCreator;
    private CalEvent selectedEvent;
    private static final SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");
    private static final SimpleDateFormat dateFormat=new SimpleDateFormat("MMM d");


    public MonthView(User user){
        super(new GridBagLayout());
        this.user=user;
        now=Calendar.getInstance();
        now.set(Calendar.DAY_OF_MONTH,1);
        GridBagConstraints constraint=new GridBagConstraints();
        BasicArrowButton left=new BasicArrowButton(SwingConstants.WEST);
        left.addActionListener(e -> {
                now.add(Calendar.MONTH,-1);
                updateTable();
                });
        constraint.gridy=0;
        constraint.anchor=GridBagConstraints.EAST;
        constraint.fill=GridBagConstraints.VERTICAL;
        constraint.weightx=1;
        constraint.ipady=20;
        constraint.ipadx=20;

        add(left,constraint);
        display=new JLabel();
        display.setHorizontalAlignment(SwingConstants.CENTER);
        constraint.anchor=GridBagConstraints.CENTER;
        constraint.fill=GridBagConstraints.VERTICAL;
        constraint.weightx=0;
        add(display,constraint);

        BasicArrowButton right=new BasicArrowButton(SwingConstants.EAST);
        right.addActionListener(e -> {
                now.add(Calendar.MONTH,1);
                updateTable();
                });
        constraint.anchor=GridBagConstraints.WEST;
        constraint.fill=GridBagConstraints.VERTICAL;
        constraint.weightx=1;
        constraint.ipadx=20;
        add(right,constraint);

        eventCreator= new JButton("Create Event");
        eventCreator.addActionListener(e->{
            if ((eFrame==null || !eFrame.isVisible())) {
                selectedEvent = new CalEvent("New Event", Calendar.getInstance(), Calendar.getInstance());
                eFrame = new EventFrame("Event Options", selectedEvent);
                eFrame.addCloseListener(close->{
                    this.user.getEvents().add(selectedEvent);
                    updateTable();
                });
            }
        });
        constraint.weightx=0;
        constraint.gridx=0;
        constraint.ipadx=0; //TODO
        add(eventCreator, constraint);

        model=new CalendarTableModel();
        table=new JTable(model);
        table.setDefaultRenderer(Day.class, (t,v,i,h,r,c) -> (Day)v);
        table.setDefaultEditor(Day.class, new RenderCell());
        table.setRowHeight(100);
        table.getTableHeader().setReorderingAllowed(false);
        pane=new JScrollPane(table);
        constraint.gridx=0;
        constraint.gridy=1;
        constraint.gridwidth=3;
        constraint.weighty=1;
        constraint.fill=GridBagConstraints.BOTH;
        add(pane,constraint);
        updateTable();
    }

    public void updateTable(){
        display.setText(new DateFormatSymbols().getMonths()[now.get(Calendar.MONTH)] + " " + now.get(Calendar.YEAR));
        display.setPreferredSize(new Dimension(300, 50));
        table.getDefaultEditor(Day.class).stopCellEditing();
        model.update();
    }

    public static boolean isSameDay(Calendar a, Calendar b){
        return a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)
               && a.get(Calendar.MONTH) == b.get(Calendar.MONTH);
    }
    public Calendar getTime(){
        return now;
    }
    public void updateTime(Calendar c){
        now.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
    }

    private class RenderCell extends AbstractCellEditor implements TableCellEditor{
        @Override public Day getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
            return model.getValueAt(row,column);
        }

        @Override public Object getCellEditorValue(){
            return null;
        }
    }


    private class Day extends JPanel{
        private NavigableSet<CalEvent> events;
        private ArrayList<JButton> buttons;
        private int number;

        public Day(int number, NavigableSet<CalEvent> events) {
            setLayout(new GridBagLayout());
            this.number=number;
            this.events=events;
            GridBagConstraints c=new GridBagConstraints();
            c.anchor=GridBagConstraints.NORTHWEST;
            c.gridx=0;
            c.weightx=1;
            c.weighty=.00001;
            //c.fill=GridBagConstraints.NONE;
            add(new JLabel(String.valueOf(number)),c);
            buttons=new ArrayList<>();
            c.fill=GridBagConstraints.BOTH;
            for(CalEvent e : events){
                StringBuffer b=new StringBuffer().append(e.getName());
                JButton adding=new JButton(b.toString());
                c.weighty=e.getDuration();
                adding.addActionListener(event -> {
                    if (eFrame==null || !eFrame.isVisible()) {
                        selectedEvent=e;
                        eFrame = new EventFrame("Event Options", selectedEvent);
                        eFrame.addCloseListener(close->{
                            if(close.delete)
                                user.getEvents().remove(selectedEvent);
                            updateTable();
                        });
                    }
                });
                buttons.add(adding);
                add(adding,c);
            }

        }
    }

    private class CalendarTableModel extends AbstractTableModel{
        private Day[][] days=new Day[5][7];

        public CalendarTableModel(){
        }

        @Override public int getColumnCount(){
            return 7;
        }

        @Override public int getRowCount(){
            return 5;
        }

        @Override public String getColumnName(int column){
            return new DateFormatSymbols().getWeekdays()[column + 1];
        }

        @Override public Day getValueAt(int row, int column){
            return days[row][column];
        }

        @Override public Class getColumnClass(int column){
            return Day.class;
        }

        public void update(){
            Calendar date = (Calendar)now.clone();
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            NavigableSet<CalEvent> events = new TreeSet(new CalEvent.byStartTime());
            events.addAll(user.getEvents().tailSet(new CalEvent("",date,date)));
            ArrayList<CalEvent> multipleDays = new ArrayList<>();
            Iterator<CalEvent> iter=events.iterator();
            CalEvent next=null;
            if(iter.hasNext())
                next=iter.next();
            for(int row=0; row < 5; row++){
                for(int column=0; column < 7; column++){
                    TreeSet<CalEvent> eventsForDay=new TreeSet<>();

                    eventsForDay.addAll(multipleDays);
                    multipleDays.removeIf(e -> isSameDay(e.getEndTime(),date));
                    while(next != null && isSameDay(next.getStartTime(),date)){
                        eventsForDay.add(next);
                        if(!isSameDay(next.getEndTime(),date))
                            multipleDays.add(next);
                        if(iter.hasNext())
                            next=iter.next();
                        else
                            next=null;
                    }
                    days[row][column]=new Day(date.get(Calendar.DAY_OF_MONTH), eventsForDay);
                    date.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            fireTableDataChanged();
        }

        @Override public boolean isCellEditable(int row, int column){
            return true;
        }
    }
}
