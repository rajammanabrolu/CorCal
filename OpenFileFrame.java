import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class OpenFileFrame extends JFrame{
    JFileChooser fc;
    JButton findBit, findMr, calc;
    JTextArea log;
    JScrollPane scroller;
    ArrayList<File> files;
    User user;
    MeetingRequest mr;
    ObjectInputStream in;

    public OpenFileFrame(User u){
        user=u;
        setUp();
    }

    private void setUp(){
        files = new ArrayList<>();
        findBit = new JButton("Add BitFields");
        findMr = new JButton("Add Meeting Request");
        calc = new JButton("Calculate!");
        log = new JTextArea("Select Bitfields and Meeting Request to Open \n");
        scroller = new JScrollPane(log);
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        setSize(400,400);
        setPreferredSize(new Dimension(400,400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        log.setEditable(false);
        findBit.addActionListener(e->{
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                files.add(file);
                log.append("Added Bitfield: " + file.getName() + ".\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
        findMr.addActionListener(e->{
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    FileInputStream temp= new FileInputStream(file);
                    in = new ObjectInputStream(temp);
                    mr= (MeetingRequest) in.readObject();
                    log.append("Added Meeting Request: " + file.getName() + ".\n");
                    temp.close();
                    in.close();
                } catch (Exception e1) {
                    log.append("Error: Not a Meeting Request" + ".\n");
                    e1.printStackTrace();
                } 
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
        calc.addActionListener(e->{
            if(mr != null){
                try {
                    Calendar[] result= user.readFiles(mr.startTime, mr.endTime, (File[]) files.toArray(), mr.duration);
                    log.append("Free time found between \n"+ new DateFormatSymbols().getMonths()[result[0].get(Calendar.MONTH)]
                            + " " + result[0].get(Calendar.DAY_OF_MONTH) + " " + result[0].get(Calendar.YEAR) + " "
                            + result[0].get(Calendar.HOUR_OF_DAY)+ ":" + result[0].get(Calendar.MINUTE) + "and \n" +
                            new DateFormatSymbols().getMonths()[result[1].get(Calendar.MONTH)]
                            + " " + result[1].get(Calendar.DAY_OF_MONTH) + " " + result[1].get(Calendar.YEAR) + " "
                            + result[1].get(Calendar.HOUR_OF_DAY)+ ":" + result[1].get(Calendar.MINUTE));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                log.append("Please add the required files first.\n");
            }
            log.setCaretPosition(log.getDocument().getLength());
        });

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        add(findBit, c);
        c.gridx=1;
        add(findMr, c);
        c.gridx=2;
        add(calc, c);
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=3;
        c.fill=GridBagConstraints.BOTH;
        c.ipady=200;
        add(scroller, c);
        pack();
        setVisible(true);
    }
    public static void main(String[] args){
        OpenFileFrame b= new OpenFileFrame(new User("User1"));
    }
}
