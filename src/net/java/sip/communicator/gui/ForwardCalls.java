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
import java.util.*;

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import net.java.sip.communicator.common.*;

//import samples.accessory.StringGridBagLayout;

public class ForwardCalls extends JDialog
{

	String userName = null;
	JTextField userNameTextField = null;
	private ForwardManager forwardman = null;

	private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
	private String CMD_SUBMIT = "cmd.submit";

	private JButton cancelButton = null;
	private JButton submitButton = null;

	/**
	 * Creates new form BlockAction
	 */
	public ForwardCalls(Frame parent, boolean modal)
	{
		super(parent, modal);
		initComponents();
		pack();
		centerWindow();
	}

	/**
	 * Loads locale-specific resources: strings, images, et cetera
	 */


	/**
	 * Centers the window on the screen.
	 */
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
		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());

		setTitle("Forwarding Calls");
		setResizable(false);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{
				dialogDone(CMD_CANCEL);
			}
		});

		// Accessibility -- all frames, dialogs, and applets should
		// have a description
		//final JPanel upperPanel = new JPanel();
		//contents.add(upperPanel,BorderLayout.NORTH);
		final JPanel innerPanel = new JPanel(new GridLayout(2,0));
		
		
		
		getAccessibleContext().setAccessibleDescription("Forwarding");

		String authPromptLabelValue = Utils.getProperty("net.java.sip.communicator.gui.AUTHENTICATION_PROMPT");
		forwardman = new ForwardManager();
		String forwardee = forwardman.getForwardee(Global.currentUser);
		String info = "Currently your calls are being forwarded to : "+forwardee;
		if (forwardee.equals("")){
			info = "You do not forward your calls";
		}
		else if(Global.forwardIndex>0)
		{
			boolean isForwardLoop=false;
			for(int i=0;i<Global.forwardIndex;i++)
			{
				if(Global.forwardLoop[i].equalsIgnoreCase(forwardee))
				{
					isForwardLoop=true;
					break;
				}
			}
			if(!isForwardLoop)
			{

				Global.forwardLoop[Global.forwardIndex]=forwardee;	
				Global.forwardIndex++;
			}
			else
			{
				System.out.println("********* This is forward loop :"+forwardee+"********"  );
				
			}

		}
		JLabel splashLabel2 = new JLabel(info);
		//splashLabel2.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		splashLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		splashLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
		innerPanel.add(splashLabel2);
		
		
		authPromptLabelValue  = "Please enter the user's name to whom who wish to forward your calls";

		JLabel splashLabel = new JLabel(authPromptLabelValue );
		splashLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
		splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		innerPanel.add(splashLabel);
		
		contents.add(innerPanel, BorderLayout.NORTH);
		JPanel centerPane = new JPanel();
		centerPane.setLayout(new GridBagLayout());

		userNameTextField = new JTextField(); // needed below

		// user name label
		JLabel userNameLabel = new JLabel();
		// userNameLabel.setDisplayedMnemonic('U');
		// setLabelFor() allows the mnemonic to work
		userNameLabel.setLabelFor(userNameTextField);

		String userNameLabelValue = Utils.getProperty("net.java.sip.communicator.gui.USER_NAME_LABEL");

		//if(userNameLabelValue == null)
		userNameLabelValue = "username";

		int gridy = 0;

		userNameLabel.setText(userNameLabelValue);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=gridy;
		c.anchor=GridBagConstraints.WEST;
		c.insets=new Insets(12,12,0,0);
		centerPane.add(userNameLabel, c);

		// user name text
		c = new GridBagConstraints();
		c.gridx=1;
		c.gridy=gridy++;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		c.insets=new Insets(12,7,0,11);
		centerPane.add(userNameTextField, c);    

		// Buttons along bottom of window
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));

		// space
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.setActionCommand(CMD_SUBMIT);
		submitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{	
				String forwardee = userNameTextField.getText();
				String forwarder = Global.currentUser;
				JFrame frame = new JFrame();
				if (forwardee.equals("") || (forwardee==null))  {
					JOptionPane.showMessageDialog(frame, "The username cannot be left blank!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					forwardman = new ForwardManager();
					if (forwardman.checkIfUserExists(forwardee)){
						if (forwardman.addForwardingToDB(forwarder, forwardee)){
							JOptionPane.showMessageDialog(frame, "Your calls will be forwarded to "+forwardee,"Forwarding Status", JOptionPane.INFORMATION_MESSAGE);
							dialogDone(event);
						}
						else{
							JOptionPane.showMessageDialog(frame, "An unexpected error has occured!Please try again","Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(frame, "Invalid username!Please try again","Error", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		buttonPanel.add(submitButton);

		// space
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.setActionCommand(CMD_CANCEL);
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				dialogDone(event);
			}
		});
		buttonPanel.add(cancelButton);

		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.insets = new Insets(11, 12, 11, 11);

		centerPane.add(buttonPanel, c);

		contents.add(centerPane, BorderLayout.CENTER);
		equalizeButtonSizes();

		 

	} // initComponents()

	/**
	 * Sets the buttons along the bottom of the dialog to be the
	 * same size. This is done dynamically by setting each button's
	 * preferred and maximum sizes after the buttons are created.
	 * This way, the layout automatically adjusts to the locale-
	 * specific strings.
	 */
	private void equalizeButtonSizes()
	{

		JButton[] buttons = new JButton[] {
				cancelButton, submitButton
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

	/**
	 * The user has selected an option. Here we close and dispose the dialog.
	 * If actionCommand is an ActionEvent, getCommandString() is called,
	 * otherwise toString() is used to get the action command.
	 *
	 * @param actionCommand may be null
	 */
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
	public static void main(String args[])
	{
		JFrame frame = new JFrame()
		{
			public Dimension getPreferredSize()
			{
				return new Dimension(200, 100);
			}
		};
		frame.setTitle("Debugging frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);

		JFrame dialog = new JFrame();
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
	} // main()
} 


