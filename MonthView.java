import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JFrame;
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
import javax.swing.BoxLayout;

import java.util.Calendar;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Iterator;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

public class MonthView extends JPanel{
    private User user;
    private CalendarTableModel model;
    private Calendar now;
    private JLabel display;
    private JScrollPane pane;
    private JTable table;
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

        model=new CalendarTableModel();
        table=new JTable(model);
        table.setDefaultRenderer(Day.class, (t,v,i,h,r,c) -> (Day)v);
        table.setDefaultEditor(Day.class, new RenderCell());
        table.setRowHeight(100);
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
        model.update();
    }

    public static void main(String[] args){
        User user = new User("asdfadsfadsfasdf");
        Calendar start = Calendar.getInstance();
        start.add(Calendar.DAY_OF_MONTH, 1);
        Calendar end = (Calendar)start.clone();
        end.add(Calendar.HOUR_OF_DAY, 2);
        user.getEvents().add(new Event("adfaergwef", start, end));
        JFrame frame=new JFrame();
        frame.add(new MonthView(user));
        frame.show();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static boolean isSameDay(Calendar a, Calendar b){
        return a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)
               && a.get(Calendar.MONTH) == b.get(Calendar.MONTH);
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
        private NavigableSet<Event> events;
        private ArrayList<JButton> buttons;
        private int number;

        public Day(int number, NavigableSet<Event> events){
            setLayout(new GridBagLayout());
            this.number=number;
            this.events=events;
            GridBagConstraints c=new GridBagConstraints();
            c.anchor=GridBagConstraints.WEST;
            c.gridx=0;
            c.weightx=1;
            add(new JLabel(String.valueOf(number)),c);
            buttons=new ArrayList<>();
            c.fill=GridBagConstraints.BOTH;
            for(Event e : events){
                System.out.println("Adding...");
                StringBuffer b=new StringBuffer().append(e.getName());
                //b.append(number == e.getStartTime().get(Calendar.DAY_OF_MONTH) ? timeFormat.format(e.getStartTime().getTime()) : "Yesterday")
                // .append(" - ")
                // .append(number == e.getEndTime().get(Calendar.DAY_OF_MONTH) ? timeFormat.format(e.getEndTime().getTime()) : "Tomorrow");
                JButton adding=new JButton(b.toString());
                c.weighty=e.getDuration();
                adding.addActionListener(event -> System.out.println("Editing " + e));
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

            NavigableSet<Event> events = new TreeSet(new Event.byStartTime());
            events.addAll(user.getEvents().tailSet(new Event("",date,date)));
            ArrayList<Event> multipleDays = new ArrayList<>();
            Iterator<Event> iter=events.iterator();
            Event next=null;
            if(iter.hasNext())
                next=iter.next();
            for(int row=0; row < 5; row++){
                for(int column=0; column < 7; column++){
                    TreeSet<Event> eventsForDay=new TreeSet<>();

                    eventsForDay.addAll(multipleDays);
                    multipleDays.removeIf(e -> isSameDay(e.getEndTime(),date));
                    if(next != null && isSameDay(next.getStartTime(),date)){
                        System.out.println("Added to " + dateFormat.format(date.getTime()));
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
