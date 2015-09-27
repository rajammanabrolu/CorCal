import java.awt.BorderLayout;
import java.awt.FlowLayout;

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
			EnterDataDialog dialog = new EnterDataDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EnterDataDialog() {
		setBounds(100, 100, 501, 325);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JTextArea txtrEnterThe = new JTextArea();
			txtrEnterThe.setFont(new Font("Arial", Font.BOLD, 13));
			txtrEnterThe.setBackground(Color.LIGHT_GRAY);
			txtrEnterThe.setEditable(false);
			txtrEnterThe.setText("Enter the 2 time stamps between which you want to find free slots:");
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
			txtTime = new JTextField();
			txtTime.setBackground(Color.LIGHT_GRAY);
			txtTime.setEditable(false);
			txtTime.setText("Enter the corresponding time for 1st date as HH:MM AM/PM : ");
			contentPanel.add(txtTime);
			txtTime.setColumns(10);
		}
		{
			textField_1 = new JTextField();
			textField_1.setBackground(Color.LIGHT_GRAY);
			contentPanel.add(textField_1);
			textField_1.setColumns(10);
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
		txtTime_1.setEditable(false);
		txtTime_1.setBackground(Color.LIGHT_GRAY);
		txtTime_1.setText("Enter the corresponding time for 2nd date as HH:MM AM/PM : ");
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
			txtEnterLengthOf.setText("Enter length of the free time slot that you want to find : ");
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
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
