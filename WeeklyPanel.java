import java.awt.Color;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.NavigableSet;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
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
        table.setModel(model);
        table.setDefaultRenderer(Object.class, new CalendarRenderer());
        table.setIntercellSpacing(new Dimension(0,0));
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
        NavigableSet<Event> dayEvents;
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
            dayEvents=events.subSet(new Event(null, null, startTime), true, new Event(null, null, endTime), true);
            tempTime = (Calendar) startTime.clone();
            for (int j=0; j<data.length; j++){
                h = data[j][i];
                h.empty();
                for(Event e: dayEvents){
                    if(e.spans(tempTime)){
                        h.set(Color.RED, e);
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

    private class CalendarRenderer extends JLabel implements TableCellRenderer {

        public CalendarRenderer(){
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            HalfHour h = (HalfHour)value;
            setHorizontalAlignment(SwingConstants.CENTER);
            if(column > 0){
                setText(null);
                setBackground(h.color);
                if(row>0 && data[row-1][column].isFull && row<data.length-1 && data[row+1][column].isFull){
                    setBorder(new CompoundBorder(MIDDLEBORDER, BorderFactory.createMatteBorder(2, 0, 2, 0, h.color)));
                } else if(row<data.length-1 && data[row+1][column].isFull){
                    try{
                        setText(h.event.getName());
                    } catch (NullPointerException e){
                        
                    }
                    setBorder(new CompoundBorder(UPPERBORDER, BorderFactory.createMatteBorder(0, 0, 2, 0, h.color)));
                } else if(row>0 && data[row-1][column].isFull){
                    setBorder(new CompoundBorder(LOWERBORDER, BorderFactory.createMatteBorder(2, 0, 0, 0, h.color)));
                } else{
                    setBorder(ALLBORDER);
                }

            } else{
                setBackground(Color.WHITE);
                setBorder(SIDEBORDER);
                switch (row){
                case 0:
                    setText("12:00 AM");
                    break;
                case 6:
                    setText("3:00 AM");
                    break;
                case 12:
                    setText("6:00 AM");
                    break;
                case 18:
                    setText("9:00 AM");
                    break;
                case 24:
                    setText("12:00 PM");
                    break;
                case 30:
                    setText("3:00 PM");
                    break;
                case 36:
                    setText("6:00 PM");
                    break;
                case 42:
                    setText("9:00 PM");
                    break;
                default:
                    setText(null);
                    setBorder(null);
                }
            }
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
