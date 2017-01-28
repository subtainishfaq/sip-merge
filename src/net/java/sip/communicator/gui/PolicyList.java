/**
* Omada 20
*
* Sarlis Dimitris 03109078
* Stathakopoulou Chrysa 03109065
* Tzannetos Dimitris 03109010
*
*/

package net.java.sip.communicator.gui;
import java.awt.*;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;

import net.java.sip.communicator.common.*;

//import samples.accessory.StringGridBagLayout;

public class PolicyList extends JDialog implements ListSelectionListener   {

	String userName = null;
	JTextField userNameTextField = null;
	private PolicyManager policyman = null;

	private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
	private String CMD_SUBMIT = "cmd.submit";
	private String CMD_SELECT = "cmd.select";

	

	private JList list;
	private DefaultListModel listPolicy;
	private JButton policyButton = new JButton("Select");
	private JButton cancelButton = new JButton("Cancel");
	public PolicyList(Frame parent, boolean modal)
	{
		super(parent, modal);
		initComponents();
		pack();
		centerWindow();
	}

	private void centerWindow()
	{
		Rectangle screen = new Rectangle(
				Toolkit.getDefaultToolkit().getScreenSize());
		Point center = new Point(
				(int) screen.getCenterX(), (int) screen.getCenterY());
		Point newLocation = new Point(
				center.x - this.getWidth() / 2, center.y - this.getHeight() / 2);
		if (screen.contains(newLocation.x, newLocation.y,
				this.getWidth(), this.getHeight())) {
			this.setLocation(newLocation);
		}
	} // centerWindow()

	private void initComponents()
	{
		policyman = new PolicyManager();
		String policyName = null;
		ResultSet result = policyman.showPolicyList(Global.currentUser);
		listPolicy = new DefaultListModel();
		
		policyListener listener = new policyListener(policyButton);
		policyButton.setActionCommand(CMD_SELECT);
		policyButton.addActionListener(listener);
		policyButton.setEnabled(false);
		
		try {
			if (result.isBeforeFirst()){
				while (result.next()){
					policyName = result.getString("name");
					listPolicy.addElement(policyName);
				}
				policyButton.setEnabled(true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle("Favorites List");
		//Create the list and put it in a scroll pane.
		list = new JList(listPolicy);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(8);
		JScrollPane listScrollPane = new JScrollPane(list);

		cancelButton.setActionCommand(CMD_CANCEL);
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				dialogDone(event);
			}
		});
		final JPanel innerPanel = new JPanel(new GridLayout(2,0));
		policyman = new PolicyManager();
		String currentPlan = policyman.getPolicyName(Global.currentUser);
		String info  = "Your current plan is : "+ currentPlan;

		JLabel splashLabel = new JLabel(info);
		splashLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
		splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		innerPanel.add(splashLabel);
		
		String text  = "The alternative subscription plans are listed below ";

		JLabel splashLabel2 = new JLabel(text);
		splashLabel2.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		splashLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		splashLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
		innerPanel.add(splashLabel2);
		
		
		add(innerPanel, BorderLayout.NORTH);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane,
				BoxLayout.LINE_AXIS));
		buttonPane.add(Box.createRigidArea(new Dimension(15, 0)));
		buttonPane.add(policyButton);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		buttonPane.add(Box.createRigidArea(new Dimension(15, 0)));
		buttonPane.add(cancelButton);


		buttonPane.setPreferredSize(new Dimension(250,100));
		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
		equalizeButtonSizes();
	}



	//This listener is shared by the text field and the hire button.
	class policyListener implements ActionListener {


		private JButton button;

		public policyListener(JButton button) {
			this.button = button;
		}

		//Required by ActionListener.
		public void actionPerformed(ActionEvent e) {
			//This method can be called only if
			//there's a valid selection
			//so go ahead and policy whatever's selected.
			int index = list.getSelectedIndex();
			String name = listPolicy.getElementAt(index).toString();
			JFrame frame = new JFrame();
			if (policyman.changePolicy(Global.currentUser,name)){
				JOptionPane.showMessageDialog(frame, "Your plan has been changed to "+name,"Policy Plan Status", JOptionPane.INFORMATION_MESSAGE);
				dialogDone(e);
			}
			else {
				JOptionPane.showMessageDialog(frame, "An unexpected error has occured!Please try again","Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private void equalizeButtonSizes()
	{

		JButton[] buttons = new JButton[] {
				cancelButton, policyButton
		};

		String[] labels = new String[buttons.length];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = buttons[i].getText();
		}

		// Get the largest width and height
		int i = 0;
		Dimension maxSize = new Dimension(0, 0);
		Rectangle2D textBounds = null;
		Dimension textSize = null;
		FontMetrics metrics = buttons[0].getFontMetrics(buttons[0].getFont());
		Graphics g = getGraphics();
		for (i = 0; i < labels.length; ++i) {
			textBounds = metrics.getStringBounds(labels[i], g);
			maxSize.width =
					Math.max(maxSize.width, (int) textBounds.getWidth());
			maxSize.height =
					Math.max(maxSize.height, (int) textBounds.getHeight());
		}

		Insets insets =
				buttons[0].getBorder().getBorderInsets(buttons[0]);
		maxSize.width += insets.left + insets.right;
		maxSize.height += insets.top + insets.bottom;

		// reset preferred and maximum size since BoxLayout takes both
		// into account
		for (i = 0; i < buttons.length; ++i) {
			buttons[i].setPreferredSize( (Dimension) maxSize.clone());
			buttons[i].setMaximumSize( (Dimension) maxSize.clone());
		}
	} // equalizeButtonSizes()
	private void dialogDone(Object actionCommand)
	{
		String cmd = null;
		if (actionCommand != null) {
			if (actionCommand instanceof ActionEvent) {
				cmd = ( (ActionEvent) actionCommand).getActionCommand();
			}
			else {
				cmd = actionCommand.toString();
			}
		}
		if (cmd == null) {
			// do nothing
		}
		else if (cmd.equals(CMD_CANCEL)) {
			userName = null;
		}
		else if (cmd.equals(CMD_SUBMIT)) {
			userName = userNameTextField.getText();

		}
		setVisible(false);
		dispose();
	} // dialogDone()

	/**
	 * This main() is provided for debugging purposes, to display a
	 * sample dialog.
	 */
	public void main(String args[])
	{
		JFrame frame = new JFrame()
		{
			public Dimension getPreferredSize()
			{
				return new Dimension(300, 400);
			}
		};
		frame.setTitle("Debugging frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);

		JFrame dialog = new JFrame("Policy Plans");
		dialog.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{
				System.exit(0);
			}

			public void windowClosed(WindowEvent event)
			{
				System.exit(0);
			}
		});
		dialog.pack();
		dialog.setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}
} // class LoginSplash



