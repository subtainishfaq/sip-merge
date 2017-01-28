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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import net.java.sip.communicator.common.*;

//import samples.accessory.StringGridBagLayout;

public class FavoriteList extends JDialog implements ListSelectionListener   {

	String userName = null;
	JTextField userNameTextField = null;
	private FavoriteManager favoriteman = null;

	private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
	private String CMD_SUBMIT = "cmd.submit";
	private String CMD_REMOVE = "cmd.remove";

	private JButton cancelButton = null;

	private JList list;
	private DefaultListModel listUsername;
	private JButton removeButton = new JButton("Remove");

	public FavoriteList(Frame parent, boolean modal)
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
		favoriteman = new FavoriteManager();
		String faved = null;
		ResultSet result = favoriteman.showFavoriteList(Global.currentUser);
		listUsername = new DefaultListModel();
		
		removeListener listener = new removeListener(removeButton);
		removeButton.setActionCommand(CMD_REMOVE);
		removeButton.addActionListener(listener);
		removeButton.setEnabled(false);
		
		try {
			if (result.isBeforeFirst()){
				while (result.next()){
					faved = result.getString("faved");
					listUsername.addElement(faved);
				}
				removeButton.setEnabled(true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle("Favorites List");
		//Create the list and put it in a scroll pane.
		list = new JList(listUsername);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(8);
		JScrollPane listScrollPane = new JScrollPane(list);


		

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


		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane,
				BoxLayout.LINE_AXIS));
		buttonPane.add(Box.createRigidArea(new Dimension(15, 0)));
		buttonPane.add(removeButton);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		buttonPane.add(Box.createRigidArea(new Dimension(15, 0)));
		buttonPane.add(cancelButton);


		buttonPane.setPreferredSize(new Dimension(250,100));
		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
	}



	//This listener is shared by the text field and the hire button.
	class removeListener implements ActionListener {


		private JButton button;

		public removeListener(JButton button) {
			this.button = button;
		}

		//Required by ActionListener.
		public void actionPerformed(ActionEvent e) {
			//This method can be called only if
			//there's a valid selection
			//so go ahead and remove whatever's selected.
			int index = list.getSelectedIndex();
			String faved = listUsername.getElementAt(index).toString();
			System.out.println("Removing "+faved);
			JFrame frame = new JFrame();
			if (favoriteman.removeFavoriteFromDB(Global.currentUser,faved)){
				listUsername.remove(index);
				int size = listUsername.getSize();
				JOptionPane.showMessageDialog(frame, "User "+faved+" has been removed!","Favorites List Status", JOptionPane.INFORMATION_MESSAGE);
				//dialogDone(e);
				if (size == 0) { //Nobody's left, disable firing.
					button.setEnabled(false);
					JOptionPane.showMessageDialog(frame, "Your favorites list is empty","Favorites List Status", JOptionPane.INFORMATION_MESSAGE);
					dialogDone(e);

				} 
				else { //Select an index.
					if (index == listUsername.getSize()) {
						//removed item in last position
						index--;
					}

					list.setSelectedIndex(index);
					list.ensureIndexIsVisible(index);
				}
			}
			else {
				JOptionPane.showMessageDialog(frame, "An unexpected error has occured!Please try again","Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

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

		JFrame dialog = new JFrame("Favorites List");
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



