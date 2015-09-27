import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.io.File;
import java.awt.event.WindowListener;

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

    public GUI(){
        File file=new File("user");
        try{
            if(file.exists()){
                System.out.println("Reading...");
                user = (User)new ObjectInputStream(new FileInputStream(file)).readObject();
            }else
                user = new User("adsf");
        }catch (Exception e){
            System.out.println(e);
            user = new User("asdf");
        }
        System.out.println(user.getEvents().size());
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
        frame.addWindowListener(new WindowListener(){
                @Override public void windowActivated(WindowEvent e){}

                @Override public void windowClosed(WindowEvent e){
                    try{
                        new ObjectOutputStream(new FileOutputStream("user")).writeObject(user);
                    }catch (IOException event){
                    }
                }

                @Override public void windowClosing(WindowEvent e){
                    try{
                        new ObjectOutputStream(new FileOutputStream("user")).writeObject(user);
                    }catch (IOException event){
                    }
                }
                @Override public void windowDeactivated(WindowEvent e){}
                @Override public void windowDeiconified(WindowEvent e){}
                @Override public void windowIconified(WindowEvent e){}
                @Override public void windowOpened(WindowEvent e){}
            });
    }
    public static void main(String[] args){
        GUI g = new GUI();
        g.setUpGUI();
    }
}
