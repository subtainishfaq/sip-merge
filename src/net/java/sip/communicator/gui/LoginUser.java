/**
* Omada 20
*
* Sarlis Dimitris 03109078
* Stathakopoulou Chrysa 03109065
* Tzannetos Dimitris 03109010
*
*/

package net.java.sip.communicator.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.sql.*;

public class LoginUser extends DatabaseServer {

	public LoginUser() {
		super();
	}
	public boolean checkIfValidUser(String username,String password){

		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM USERS WHERE USERNAME ='"+username+"' and password='"+password+"'";
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			/* cursor is initially positioned before the first row; 
			 * the first call to the method next makes the first row 
			 * the current row
			 * */
			if (rs.next())
				return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


}
