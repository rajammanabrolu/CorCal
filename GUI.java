import java.io.ObjectInputStream;
import java.io.FileInputStream;
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
    private JMenuItem monthly, weekly, create, respond, process;
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
        respond = new JMenuItem("Respond");
        process = new JMenuItem("Process");

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
                monthlyPanel.updateTime(weeklyPanel.getTime());
                monthlyPanel.updateTable();
                ((CardLayout)(rootPanel.getLayout())).show(rootPanel, MONTHVIEW);
            }
        });
        weekly.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                weeklyPanel.updateTime(monthlyPanel.getTime());
                weeklyPanel.updateAll();
                ((CardLayout)(rootPanel.getLayout())).show(rootPanel, WEEKVIEW);
            }
        });
        create.addActionListener(event -> new EnterDataDialog(frame).show());
        respond.addActionListener(event -> {
                try{
                    JFileChooser choice=new JFileChooser();
                    if(choice.showDialog(frame,"Choose a Meeting Request")==JFileChooser.APPROVE_OPTION){
                        MeetingRequest req=(MeetingRequest)new ObjectInputStream(new FileInputStream(choice.getSelectedFile())).readObject();
                        if(choice.showDialog(frame,"Choose a place to save your response")==JFileChooser.APPROVE_OPTION){
                            user.createBitField(req.startTime,req.endTime,choice.getSelectedFile());
                        }
                    }
                }catch(ClassCastException e){
                    JOptionPane.showMessageDialog(frame, "The file selected was not a Meeting Request", "Error", JOptionPane.ERROR_MESSAGE);
                }catch(Exception error){
                    JOptionPane.showMessageDialog(frame,"Oops, It looks like something went wrong: " + error.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            });
        process.addActionListener(event ->{
                    new OpenFileFrame(user).show();
                });
        menuBar.add(view);
        menuBar.add(request);
        view.add(monthly);
        view.add(weekly);
        request.add(create);
        request.add(respond);
        request.add(process);

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args){
        GUI g = new GUI(new User("User1"));
        g.setUpGUI();
    }
}
