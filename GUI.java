import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class GUI{
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu view, request;
    private JMenuItem monthly, weekly, create;
    private JPanel rootPanel;
    private WeeklyPanel weeklyPanel;
    private MonthView monthlyPanel;
    private User user;
    
    private final String MONTHVIEW = "monthview";
    private final String WEEKVIEW = "weekview";
    
    public GUI(User u){
        user = u;
    }
    
    public void setUpGUI(){
        frame = new JFrame("Calendar");
        monthlyPanel = new MonthView(user);
        weeklyPanel = new WeeklyPanel(user);
        rootPanel = new JPanel();
        menuBar= new JMenuBar();
        view = new JMenu("View");
        request = new JMenu("Request");
        monthly = new JMenuItem("Monthly");
        weekly = new JMenuItem("Weekly");
        create = new JMenuItem("Create");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,1000));
        frame.setSize(1000, 1000);
        rootPanel.setPreferredSize(new Dimension(1000,1000));
        
        
        rootPanel.setLayout(new CardLayout());
        rootPanel.add(monthlyPanel, MONTHVIEW);
        rootPanel.add(weeklyPanel, WEEKVIEW);
        frame.add(rootPanel);
        monthly.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout)(rootPanel.getLayout())).show(rootPanel, MONTHVIEW);
            }
        });
        weekly.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout)(rootPanel.getLayout())).show(rootPanel, WEEKVIEW);
            }
        });
        create.addActionListener(event -> new EnterDataDialog(frame).show());
        
        menuBar.add(view);
        menuBar.add(request);
        view.add(monthly);
        view.add(weekly);
        request.add(create);
        
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main (String[] args){
        User u = new User("Name");
        Calendar endTime= Calendar.getInstance();
        endTime.add(Calendar.MINUTE, 780);
        Event temp = new Event("HackGT", Calendar.getInstance(), endTime);
        temp.setColor(Color.RED);
        u.addEvent(temp);
        GUI g = new GUI(u);
        g.setUpGUI();
    }
}
