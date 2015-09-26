import java.awt.Color;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Calendar;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.border.Border;
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
    private User user;
    
    private final Border UPPERBOARDER= BorderFactory.createMatteBorder(1, 2, 0, 2, Color.BLACK);
    private final Border LOWERBOARDER= BorderFactory.createMatteBorder(0, 2, 1, 2, Color.BLACK);
    private final Border MIDDLEBOARDER= BorderFactory.createMatteBorder(0, 2, 0, 2, Color.BLACK);
    private final Border ALLBOARDER= BorderFactory.createMatteBorder(1, 2, 1, 2, Color.BLACK);
    public WeeklyPanel(User u){
        user = u;
        setUpPanel();
    }

    public void setUpPanel(){
        currentTime=Calendar.getInstance();
        dates= new String[7];
        data= new HalfHour[48][7];
        title = new JLabel();
        for (int i=0; i<data.length; i++){
            for (int j=0; j<data[i].length; j++){
                data[i][j] = new HalfHour();
            }
        }
        updateDates();
        updateData();
        updateTitle();
        table = new JTable(data, dates);
        model = new CalendarModel();
        scrollPane = new JScrollPane(table);
        left = new BasicArrowButton(BasicArrowButton.WEST);
        right = new BasicArrowButton(BasicArrowButton.EAST);
        left.addActionListener(e -> {
            int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);
            int currentMonth = currentTime.get(Calendar.MONTH);
            int currentYear = currentTime.get(Calendar.YEAR);
            currentDay-=7;
            if(currentDay < 0){
                currentMonth--;
                if (currentMonth < Calendar.JANUARY){
                    currentMonth = Calendar.DECEMBER;
                    currentYear--;
                }
                currentTime.set(Calendar.MONTH, currentMonth);
                currentDay+= currentTime.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            currentTime.set(currentYear, currentMonth, currentDay);
            updateDates();
            updateData();
            updateTitle();
        });
        right.addActionListener(e -> {
            int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);
            int currentMonth = currentTime.get(Calendar.MONTH);
            int currentYear = currentTime.get(Calendar.YEAR);
            currentDay+=7;
            if(currentDay > currentTime.getActualMaximum(Calendar.DAY_OF_MONTH)){
                currentMonth++;
                if (currentMonth > Calendar.DECEMBER){
                    currentMonth = Calendar.JANUARY;
                    currentYear++;
                }
                currentTime.set(Calendar.MONTH, currentMonth);
                currentDay-= currentTime.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            currentTime.set(currentYear, currentMonth, currentDay);
            updateDates();
            updateData();
            updateTitle();
        });
        table.setModel(model);
        table.setDefaultRenderer(Object.class, new CalendarRenderer());
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
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
        c.gridy=1;
        c.gridwidth=3;
        c.weighty=1;
        c.fill=GridBagConstraints.BOTH;
        add(scrollPane,c);
    }
    
    private void updateData(){
        TreeSet<Event> events = user.getEvents();
        TreeSet<Event> dayEvents;
        for(int i=0; i<data[0].length; i++){
                
            for (int j=0; j<data.length; j++){
                
            }
        }
        
    }
    
    private void updateTitle(){
        title.setText(getMonthString(currentTime.get(Calendar.MONTH)) + " " + currentTime.get(Calendar.YEAR));
    }
    private void updateDates(){
        Calendar c = currentTime;
        int day = c.get(Calendar.DAY_OF_WEEK)-1;
        int date = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int tempMonth= month;
        int tempDate = date;
        int tempDay = day+1;
        for (int i= day; i<7; i++){
            dates[i]=getDayString(tempDay) + " " + (tempMonth+1) + "/" + tempDate;
            tempDate++;
            tempDay++;
            if(tempDate > c.getActualMaximum(Calendar.DAY_OF_MONTH)){
                tempMonth++;
                if (tempMonth > Calendar.DECEMBER)
                    tempMonth = Calendar.JANUARY;
                c.set(Calendar.MONTH, tempMonth);
                tempDate-=c.getActualMaximum(Calendar.DAY_OF_MONTH);
                c.set(Calendar.MONTH, month);
            }
        }
        tempMonth = month;
        tempDate=date;
        tempDay=day+1;
        for (int i = day; i>=0; i--){
            dates[i]=getDayString(tempDay) + " " + (tempMonth+1) + "/" + tempDate;
            tempDate--;
            tempDay--;
            if(tempDate<=0){
                tempMonth--;
                if (tempMonth < Calendar.JANUARY)
                    tempMonth = Calendar.DECEMBER;
                c.set(Calendar.MONTH, tempMonth);
                tempDate+=c.getActualMaximum(Calendar.DAY_OF_MONTH);
                c.set(Calendar.MONTH, month);
            }
        }
        if(table != null){
            for(int i=0; i< dates.length; i++){
                table.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(dates[i]);
            }
        }
        repaint();
    }
    private String getMonthString(int i){
        switch(i){
        case Calendar.JANUARY:
            return "January";
        case Calendar.FEBRUARY:
            return "February";
        case Calendar.MARCH:
            return "March";
        case Calendar.APRIL:
            return "April";
        case Calendar.MAY:
            return "May";
        case Calendar.JUNE:
            return "June";
        case Calendar.JULY:
            return "July";
        case Calendar.AUGUST:
            return "August";
        case Calendar.SEPTEMBER:
            return "September";
        case Calendar.OCTOBER:
            return "October";
        case Calendar.NOVEMBER:
            return "November";
        case Calendar.DECEMBER:
            return "December";
        default: 
            return "NULL MONTH";
        }
    }
    private String getDayString(int i){
        switch(i){
        case Calendar.MONDAY:
            return "Monday";
        case Calendar.TUESDAY:
            return "Tuesday";
        case Calendar.WEDNESDAY:
            return "Wednesday";
        case Calendar.THURSDAY:
            return "Thursday";
        case Calendar.FRIDAY:
            return "Friday";
        case Calendar.SATURDAY:
            return "Saturday";
        case Calendar.SUNDAY:
            return "Sunday";
        default: 
            return "NULL DAY";
        }
    }
    private class CalendarRenderer extends JLabel implements TableCellRenderer {
        
        public CalendarRenderer(){
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            HalfHour h = (HalfHour)value;
            setBorder(ALLBOARDER);
            setBackground(h.color);
            return this;
        }

    }
    private class CalendarModel extends DefaultTableModel{
        public CalendarModel(){
            setDataVector(data, dates);
        }
        @Override
        public boolean isCellEditable(int row, int column) {
           return false;
        }
    }
    private class HalfHour{
        private boolean isFull;
        private Color color;
        private Event event;
        public HalfHour(){
            empty();
        }
        private void set(Color c, Event e){
            isFull = true;
            color = c;
            event = e;
        }
        private void empty(){
            isFull = false;
            color = Color.WHITE;
            event = null;
        }
    }
}
