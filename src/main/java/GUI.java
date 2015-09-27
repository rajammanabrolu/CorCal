import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.text.ParseException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

public class GUI{
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu view, request, sync;
    private JMenuItem monthly, weekly, create, respond, process, google;
    private JPanel rootPanel;
    private WeeklyPanel weeklyPanel;
    private MonthView monthlyPanel;
    private User user;
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/calendar-java-quickstart");
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

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

    public void setUpGUI() throws IOException{
        frame = new JFrame("Calendar");
        monthlyPanel = new MonthView(user);
        weeklyPanel = new WeeklyPanel(user);
        rootPanel = new JPanel();
        menuBar= new JMenuBar();
        view = new JMenu("View");
        request = new JMenu("Request");
        sync = new JMenu("Sync");
        monthly = new JMenuItem("Monthly");
        weekly = new JMenuItem("Weekly");
        create = new JMenuItem("Create");
        respond = new JMenuItem("Respond");
        process = new JMenuItem("Process");
        google = new JMenuItem("Google Calendar");

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
        create.addActionListener(event -> new EnterDataDialog(frame).setVisible(true));
        respond.addActionListener(event -> {
                try{
                    JFileChooser choice=new JFileChooser();
                    if(choice.showDialog(frame,"Choose a Meeting Request")==JFileChooser.APPROVE_OPTION){
                        ObjectInputStream temp =new ObjectInputStream(new FileInputStream(choice.getSelectedFile()));
                        MeetingRequest req=(MeetingRequest) temp.readObject();
                        temp.close();
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
                    new OpenFileFrame(user).setVisible(true);
                });
        menuBar.add(view);
        menuBar.add(request);
        menuBar.add(sync);
        view.add(monthly);
        view.add(weekly);
        request.add(create);
        request.add(respond);
        request.add(process);
        sync.add(google);

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);

        google.addActionListener(event->{
                try {user.getEvents().addAll(new CalendarQuickstart().getCalEvents());}
                catch(Exception e) {e.printStackTrace();}
                });

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

    public static void main(String[] args) throws IOException{
        GUI g = new GUI();
        g.setUpGUI();
    }

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public static class CalendarQuickstart {

    public static Credential authorize() throws IOException {
        InputStream in = CalendarQuickstart.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    public static com.google.api.services.calendar.Calendar
        getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public ArrayList<CalEvent> getCalEvents() throws IOException, ParseException {
        com.google.api.services.calendar.Calendar service = getCalendarService();
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary").setMaxResults(30).setTimeMin(now).setOrderBy("startTime").setSingleEvents(true).execute();
        List<Event> items = events.getItems();
        SimpleDateFormat rc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ArrayList<CalEvent> calEvents = new ArrayList<CalEvent>();
        for (Event event : items) {
            Calendar start = Calendar.getInstance();
    		start.setTime(rc.parse(event.getStart().getDateTime().toString()));
            Calendar end = Calendar.getInstance();
    		end.setTime(rc.parse(event.getEnd().getDateTime().toString()));
            calEvents.add(new CalEvent(event.getSummary(), start, end));
        }
        return calEvents;
    }
}
}
