/**
* Omada 20
*
* Sarlis Dimitris 03109078
* Stathakopoulou Chrysa 03109065
* Tzannetos Dimitris 03109010
*
*/


package net.java.sip.communicator.gui;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForwardManager extends DatabaseServer{
	public ForwardManager(){
		super();
	}

	public boolean checkIfUserExists(String forwardee) {
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM USERS WHERE USERNAME ='"+ forwardee+"'";
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs.next())
				return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean addForwardingToDB(String forwarder, String forwardee) {
		boolean result = false;

		try {
			this.stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="INSERT INTO FORWARDING_LIST (FORWARDER,FORWARDEE) " +"VALUES ('"+forwarder+"','"+forwardee+"') "
				+ "ON DUPLICATE KEY UPDATE FORWARDEE = '"+forwardee+"';";
		System.out.println(Query);
		try {
			if (stmt.executeUpdate(Query)>0)
				result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
		return result;
	}
	public String getForwardee(String forwarder){
		ResultSet rs = null;
		String ret = "";
		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM FORWARDING_LIST WHERE FORWARDER ='"+ forwarder+"'";
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs.next())
				ret = rs.getString("forwardee");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public boolean removeForwardingFromDB(String forwarder) {
		boolean result = false;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="DELETE FROM FORWARDING_LIST WHERE FORWARDER ='"+ forwarder+"';";
		System.out.println(Query);
		try {
			result = stmt.execute(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// true returned when ResultSet is an object
		return !result;
	}
}
