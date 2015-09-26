import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI{
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu view, settings;
    private JMenuItem monthly, weekly;
    private JPanel monthlyPanel, weeklyPanel, rootPanel;
    private JTextField test;
    
    private final String MONTHVIEW = "monthview";
    private final String WEEKVIEW = "weekview";
    public void setUpGUI(){
        frame = new JFrame("Calendar");
        monthlyPanel = new JPanel();
        weeklyPanel = new JPanel();
        rootPanel = new JPanel();
        menuBar= new JMenuBar();
        view = new JMenu("View");
        settings = new JMenu("Settings");
        monthly = new JMenuItem("Monthly");
        weekly = new JMenuItem("Weekly");
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
        
        menuBar.add(view);
        menuBar.add(settings);
        view.add(monthly);
        view.add(weekly);
        
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main (String[] args){
        GUI g = new GUI();
        g.setUpGUI();
    }
}