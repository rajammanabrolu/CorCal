<<<<<<< HEAD
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;

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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

public class MonthView extends JPanel{
    private User user;
    private CalendarTableModel model;
    private Calendar now;
    private JLabel display;
    private static final SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");

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
        updateTable();
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
        JTable table=new JTable(model);
        table.setDefaultRenderer(Day.class, (t,v,i,h,r,c) -> (Day)v);
        constraint.gridx=0;
        constraint.gridy=1;
        constraint.gridwidth=3;
        constraint.weighty=1;
        constraint.fill=GridBagConstraints.BOTH;
        add(new JScrollPane(table),constraint);
    }

    private void updateTable(){
        display.setText(new DateFormatSymbols().getMonths()[now.get(Calendar.MONTH)] + " " + now.get(Calendar.YEAR));
        display.setPreferredSize(new Dimension(100, 50));
    }

    public static void main(String[] args){
        User user = new User("asdfadsfadsfasdf");
        Calendar start = Calendar.getInstance();
        start.add(Calendar.DAY_OF_MONTH, 1);
        Calendar end = (Calendar)start.clone();
        end.add(Calendar.HOUR_OF_DAY, 2);
        user.getEvents().add(new Event("adfaergwef", start, end));
        JFrame frame=new JFrame();
        frame.add(new MonthView(null));
        frame.show();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class Day extends JPanel{
        private NavigableSet<Event> events;
        private TreeMap<JButton,Event> buttons;
        private int number;

        public Day(int number, NavigableSet<Event> events){
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            this.number=number;
            this.events=events;

            add(new JLabel(String.valueOf(number)));
            buttons=new TreeMap<>();
            for(Event e : events){
                StringBuffer b=new StringBuffer();
                b.append(number == e.getStartDate().get(Calendar.DAY_OF_MONTH) ? timeFormat.format(e.getStartDate()) : "Yesterday")
                 .append(" - ")
                 .append(number == e.getEndDate().get(Calendar.DAY_OF_MONTH) ? timeFormat.format(e.getEndDate()) : "Tomorrow");
                JButton adding=new JButton(b.toString());
                adding.addActionListener(event -> System.out.println("Editing " + e));
                buttons.put(adding,e);
            }
        }
    }

    private class CalendarTableModel extends AbstractTableModel{
        private Day[][] days=new Day[4][7];

        public CalendarTableModel(){
        }

        @Override public int getColumnCount(){
            return 7;
        }

        @Override public int getRowCount(){
            return 4;
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
            NavigableSet<Event> events = user.getEvents().subset
            for(int row=0; row < 4; row++){
                for(int column=0; column < 7; column++){
                    days[row][column]=new Day(date.get(Calendar.DAY_OF_MONTH), new TreeSet<Event>());
                    date.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }

    }
}
=======
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;

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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

public class MonthView extends JPanel{
    private User user;
    private CalendarTableModel model;
    private Calendar now;
    private JLabel display;
    private static final SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");

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
        updateTable();
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
        JTable table=new JTable(model);
        table.setDefaultRenderer(Day.class, (t,v,i,h,r,c) -> (Day)v);
        constraint.gridx=0;
        constraint.gridy=1;
        constraint.gridwidth=3;
        constraint.weighty=1;
        constraint.fill=GridBagConstraints.BOTH;
        add(new JScrollPane(table),constraint);
    }

    private void updateTable(){
        display.setText(new DateFormatSymbols().getMonths()[now.get(Calendar.MONTH)] + " " + now.get(Calendar.YEAR));
        display.setPreferredSize(new Dimension(100, 50));
    }

    public static void main(String[] args){
        JFrame frame=new JFrame();
        frame.add(new MonthView(null));
        frame.show();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class Day extends JPanel{
        private NavigableSet<Event> events;
        private TreeMap<JButton,Event> buttons;
        private int number;

        public Day(int number, NavigableSet<Event> events){
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            this.number=number;
            this.events=events;

            add(new JLabel(String.valueOf(number)));
            buttons=new TreeMap<>();
            for(Event e : events){
                StringBuffer b=new StringBuffer();
                //b.append(number == e.getStartDate().get(Calendar.DAY_OF_MONTH) ? timeFormat.format(e.getStartDate()) : "Yesterday")
                // .append(" - ")
                // .append(number == e.getEndDate().get(Calendar.DAY_OF_MONTH) ? timeFormat.format(e.getEndDate()) : "Tomorrow");
                JButton adding=new JButton(b.toString());
                adding.addActionListener(event -> System.out.println("Editing " + e));
                buttons.put(adding,e);
            }
        }
    }

    private class CalendarTableModel extends AbstractTableModel{
        private Day[][] days=new Day[4][7];

        public CalendarTableModel(){
            Calendar date = (Calendar)now.clone();
            date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            for(int row=0; row < 4; row++){
                for(int column=0; column < 7; column++){
                    days[row][column]=new Day(date.get(Calendar.DAY_OF_MONTH), new TreeSet<Event>());
                    date.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }

        @Override public int getColumnCount(){
            return 7;
        }

        @Override public int getRowCount(){
            return 4;
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
    }
}
>>>>>>> refs/heads/Mason
