import java.awt.Color;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.NavigableSet;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class WeeklyPanel extends JPanel{
    private JTable table;
    private CalendarModel model;
    private String[] dates;
    private HalfHour[][] data;
    private BasicArrowButton left, right;
    private JLabel title;
    private Calendar currentTime;
    private JScrollPane scrollPane;
    private EventFrame eFrame;
    private User user;
    private JButton eventCreator;
    private Event selectedEvent;

    private final Border UPPERBORDER= BorderFactory.createMatteBorder(1, 2, 0, 2, Color.BLACK);
    private final Border SIDEBORDER= BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK);
    private final Border LOWERBORDER= BorderFactory.createMatteBorder(0, 2, 1, 2, Color.BLACK);
    private final Border MIDDLEBORDER= BorderFactory.createMatteBorder(0, 2, 0, 2, Color.BLACK);
    private final Border ALLBORDER= BorderFactory.createMatteBorder(1, 2, 1, 2, Color.BLACK);
    public WeeklyPanel(User u){
        user = u;
        setUpPanel();
    }

    public void setUpPanel(){
        currentTime=Calendar.getInstance();
        dates= new String[8];
        data= new HalfHour[48][8];
        title = new JLabel();
        for (int j=0; j<data.length; j++){
            data[j][0] = new HalfHour();
        }
        for (int i=0; i<data.length; i++){
            for (int j=1; j<data[i].length; j++){
                data[i][j] = new HalfHour();
            }
        }
        dates[0] = "Time";
        updateDates();
        updateData();
        updateTitle();
        table = new JTable(data, dates);
        model = new CalendarModel();
        scrollPane = new JScrollPane(table);
        left = new BasicArrowButton(BasicArrowButton.WEST);
        right = new BasicArrowButton(BasicArrowButton.EAST);
        eventCreator = new JButton("Add Event");
        eventCreator.addActionListener(e -> {
            if ((eFrame==null || !eFrame.isVisible())) { 
                selectedEvent = new Event("New Event", Calendar.getInstance(), Calendar.getInstance());
                eFrame = new EventFrame("Event Options", selectedEvent);
                eFrame.addCloseListener(close->{
                    if(selectedEvent != null)
                        user.addEvent(selectedEvent);
                    selectedEvent = null;
                    updateData();
                });
            }
        });
        left.addActionListener(e -> {
            currentTime.add(Calendar.DAY_OF_MONTH, -7);
            updateDates();
            updateData();
            updateTitle();
        });
        right.addActionListener(e -> {
            currentTime.add(Calendar.DAY_OF_MONTH, 7);
            updateDates();
            updateData();
            updateTitle();
        });
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (e.getClickCount() == 2 && (eFrame==null || !eFrame.isVisible()) && data[row][col].event!=null) { 
                    eFrame = new EventFrame("Event Options", data[row][col].event);
                    selectedEvent = null;
                    eFrame.addCloseListener(close->{
                        updateData();
                    });
                }
            }
        });
        table.setModel(model);
        table.setDefaultRenderer(Object.class, new CalendarRenderer());
        table.setIntercellSpacing(new Dimension(0,0));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        EventFrame.setUpExtras(this);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy=0;
        c.anchor=GridBagConstraints.EAST;
        c.fill=GridBagConstraints.VERTICAL;
        c.weightx=1;
        c.ipady=20;
        c.ipadx=20;
        add(left,c);
        c.anchor=GridBagConstraints.CENTER;
        c.fill=GridBagConstraints.VERTICAL;
        c.weightx=0;
        add(title,c);
        c.anchor=GridBagConstraints.WEST;
        c.fill=GridBagConstraints.VERTICAL;
        c.weightx=1;
        c.ipadx=20;
        add(right,c);
        c.gridx=0;
        c.ipadx=0;
        c.weightx=0;
        add(eventCreator, c);
        c.gridwidth=2;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=3;
        c.weighty=1;
        c.fill=GridBagConstraints.BOTH;
        add(scrollPane,c);
    }

    public void updateData(){
        TreeSet<Event> events = user.getEvents();
        NavigableSet<Event> dayEvents = new TreeSet<>();
        Calendar startTime;
        Calendar endTime;
        Calendar tempTime;
        HalfHour h;
        startTime = (Calendar) currentTime.clone();
        startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        startTime.add(Calendar.DAY_OF_MONTH, 1-currentTime.get(Calendar.DAY_OF_WEEK));
        endTime = (Calendar) currentTime.clone();
        endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        endTime.add(Calendar.DAY_OF_MONTH, 2-currentTime.get(Calendar.DAY_OF_WEEK));

        for(int i=1; i<data[0].length; i++){
            dayEvents.clear();
            for (Event e : events){
                if (e.getEndTime().compareTo(startTime)>0 && e.getStartTime().compareTo(endTime) < 0)
                    dayEvents.add(e);
            }
            tempTime = (Calendar) startTime.clone();
            tempTime.add(Calendar.MINUTE, 30);
            for (int j=0; j<data.length; j++){
                h = data[j][i];
                h.empty();
                for(Event e: dayEvents){
                    if(e.spans(tempTime)){
                        h.set(e);
                        break;
                    }
                }
                tempTime.add(Calendar.MINUTE, 30);
            }
            startTime.add(Calendar.DAY_OF_MONTH, 1);
            endTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        repaint();
    }
    private void updateTitle(){
        title.setText(new DateFormatSymbols().getMonths()[currentTime.get(Calendar.MONTH)] + " " + currentTime.get(Calendar.YEAR));
    }

    private void updateDates(){
        Calendar c= (Calendar) currentTime.clone();
        for (int i= c.get(Calendar.DAY_OF_WEEK); i<8; i++){
            dates[i]= new DateFormatSymbols().getWeekdays()[c.get(Calendar.DAY_OF_WEEK)] + " " + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DATE);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        c= (Calendar) currentTime.clone();
        for (int i= c.get(Calendar.DAY_OF_WEEK); i>0; i--){
            dates[i]= new DateFormatSymbols().getWeekdays()[c.get(Calendar.DAY_OF_WEEK)] + " " + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DATE);
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        if(table != null){
            for(int i=0; i< dates.length; i++){
                table.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(dates[i]);
            }
        }
        repaint();
    }

    private class CalendarRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            HalfHour h = (HalfHour)value;
            if(column > 0){
                h.setText(null);
                if(data[row][column].isFull && row>0 && data[row-1][column].isFull && row<data.length-1 && data[row+1][column].isFull){
                    h.setBorder(new CompoundBorder(MIDDLEBORDER, BorderFactory.createMatteBorder(2, 0, 2, 0, h.event.getColor())));
                } else if(data[row][column].isFull && row<data.length-1 && data[row+1][column].isFull){
                    if(h.event != null){
                        h.setHorizontalAlignment(SwingConstants.CENTER);
                        h.setText(h.event.getName());
                    }
                    h.setBorder(new CompoundBorder(UPPERBORDER, BorderFactory.createMatteBorder(0, 0, 2, 0, h.event.getColor())));
                } else if(data[row][column].isFull && row>0 && data[row-1][column].isFull){
                    h.setBorder(new CompoundBorder(LOWERBORDER, BorderFactory.createMatteBorder(2, 0, 0, 0, h.event.getColor())));
                } else{
                    h.setBorder(ALLBORDER);
                }
                if(h.event == null){
                    h.setBackground(Color.WHITE);
                } else{
                    h.setBackground(h.event.getColor());
                    if(h.event.getDescription()!= null){
                        h.setToolTipText(h.event.getDescription());
                    }
                }
            } else{
                h.setBorder(SIDEBORDER);
                switch (row){
                case 0:
                    h.setText("12:00 AM");
                    break;
                case 6:
                    h.setText("3:00 AM");
                    break;
                case 12:
                    h.setText("6:00 AM");
                    break;
                case 18:
                    h.setText("9:00 AM");
                    break;
                case 24:
                    h.setText("12:00 PM");
                    break;
                case 30:
                    h.setText("3:00 PM");
                    break;
                case 36:
                    h.setText("6:00 PM");
                    break;
                case 42:
                    h.setText("9:00 PM");
                    break;
                default:
                    h.setBorder(null);
                }
            }
            return h;
        }
    }

    public class CalendarModel extends DefaultTableModel{
        public CalendarModel(){
            setDataVector(data, dates);
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        public void update(){
            fireTableChanged(new TableModelEvent(this));
        }
    }
    private class HalfHour extends JLabel{
        private boolean isFull;
        private Event event;

        public HalfHour(){
            empty();
            setOpaque(true);
        }
        private void set(Event e){
            isFull = true;
            event = e;
        }
        private void empty(){
            isFull = false;
            event = null;
        }
    }
}
