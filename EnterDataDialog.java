import javax.swing.JOptionPane;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import java.text.SimpleDateFormat;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import java.awt.Canvas;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

public class EnterDataDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtTime;
	private JTextField textField;
	private final JTextField txtTime_1 = new JTextField();
	private JTextField textField_1;
	private JTextField txtEnterDate;
	private JTextField textField_2;
	private JTextField txtEnterndDate;
	private JTextField textField_4;
	private JTextField txtEnterLengthOf;
	private JTextField textField_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EnterDataDialog dialog = new EnterDataDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EnterDataDialog(JFrame frame) {
        super(frame, "Create a Calendar Request", true);
		setBounds(100, 100, 601, 325);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JTextArea txtrEnterThe = new JTextArea();
			txtrEnterThe.setFont(new Font("Arial", Font.BOLD, 13));
			txtrEnterThe.setBackground(Color.LIGHT_GRAY);
			txtrEnterThe.setEditable(false);
			txtrEnterThe.setText("Enter the 2 dates between which you want to find times when no body is busy in during certain hours:");
			contentPanel.add(txtrEnterThe);
		}
		{
			txtEnterDate = new JTextField();
			txtEnterDate.setBackground(Color.LIGHT_GRAY);
			txtEnterDate.setEditable(false);
			txtEnterDate.setText("Enter 1st Date as YYYY/MM/DD :");
			contentPanel.add(txtEnterDate);
			txtEnterDate.setColumns(10);
		}
		{
			textField_2 = new JTextField();
			textField_2.setBackground(Color.LIGHT_GRAY);
			contentPanel.add(textField_2);
			textField_2.setColumns(10);
		}
		{
			txtEnterndDate = new JTextField();
			txtEnterndDate.setEditable(false);
			txtEnterndDate.setBackground(Color.LIGHT_GRAY);
			txtEnterndDate.setText("Enter 2nd Date as YYYY/MM/DD : ");
			contentPanel.add(txtEnterndDate);
			txtEnterndDate.setColumns(10);
		}
		{
			textField_4 = new JTextField();
			textField_4.setBackground(Color.LIGHT_GRAY);
			contentPanel.add(textField_4);
			textField_4.setColumns(10);
		}
		{
			txtTime = new JTextField();
			txtTime.setBackground(Color.LIGHT_GRAY);
			txtTime.setEditable(false);
			txtTime.setText("Enter the earliest during the day you can meet as HH:MM AM/PM");
			contentPanel.add(txtTime);
			txtTime.setColumns(10);
		}
		{
			textField_1 = new JTextField();
			textField_1.setBackground(Color.LIGHT_GRAY);
			contentPanel.add(textField_1);
			textField_1.setColumns(10);
		}
		txtTime_1.setEditable(false);
		txtTime_1.setBackground(Color.LIGHT_GRAY);
		txtTime_1.setText("Enter the latest the meeting can go as HH:MM AM/PM : ");
		contentPanel.add(txtTime_1);
		txtTime_1.setColumns(10);
		{
			textField_5 = new JTextField();
			textField_5.setBackground(Color.LIGHT_GRAY);
			contentPanel.add(textField_5);
			textField_5.setColumns(10);
		}
		{
			txtEnterLengthOf = new JTextField();
			txtEnterLengthOf.setBackground(Color.LIGHT_GRAY);
			txtEnterLengthOf.setEditable(false);
			txtEnterLengthOf.setText("Enter length of the free time slot that you want to find in minutes (will round up to the half hour): ");
			contentPanel.add(txtEnterLengthOf);
			txtEnterLengthOf.setColumns(10);
		}
		{
			textField = new JTextField();
			textField.setBackground(Color.LIGHT_GRAY);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
                okButton.addActionListener(e -> {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                        try{
                            Calendar start =Calendar.getInstance();
                            start.setTime(format.parse(textField_2.getText() + " " + textField_1.getText()));
                            Calendar end =Calendar.getInstance();
                            end.setTime(format.parse(textField_4.getText() + " " + textField_5.getText()));
                            int duration = Integer.parseInt(textField.getText());
                            duration=(int)Math.ceil(duration / 30.);
                            MeetingRequest request=new MeetingRequest(start,end,duration);
                            JFileChooser selector = new JFileChooser();
                            selector.setMultiSelectionEnabled(false);
                            //selector.setFileFilter(new FileNameExtensionFilter("Meeting Requests",".rqt"));
                            if(selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                                File file=selector.getSelectedFile();
                                ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream(file));
                                o.writeObject(request);
                            }
                        }catch(Exception error){
                            JOptionPane.showMessageDialog(this,"Oops, It looks like something went wrong: " + error.getMessage());
                        }
                    });
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(e -> this.dispose());
				buttonPane.add(cancelButton);
			}
		}
	}

}
