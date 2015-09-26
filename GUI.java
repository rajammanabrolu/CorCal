import javax.swing.*;

public class GUI{
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu view, settings;
    private JMenuItem monthly, weekly;
    
    public void setUpGUI(){
        frame = new JFrame("Calendar");
        menuBar= new JMenuBar();
        view = new JMenu("View");
        settings = new JMenu("Settings");
        monthly = new JMenuItem("Monthly");
        weekly = new JMenuItem("Weekly");
        menuBar.add(view);
        menuBar.add(settings);
        view.add(monthly);
        view.add(weekly);
        frame.setSize(1000,1000);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }
    public static void main (String[] args){
        GUI g = new GUI();
        g.setUpGUI();
    }
}