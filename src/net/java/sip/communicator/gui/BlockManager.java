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

public class BlockManager extends DatabaseServer{

	public BlockManager(){
		super();
	}
	public ResultSet showBlockedList(String blocker){
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM BLOCKING_LIST WHERE BLOCKER ='"+ blocker+"';";
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* return resultSet w/o checking
		 * caller must check appropriately 
		 */ 
		return rs;

	}
	
	public boolean addBlockToDB(String blocker, String blockee) {
		boolean result = false;

		try {
			this.stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="INSERT INTO BLOCKING_LIST (BLOCKER,BLOCKEE) " +"VALUES ('"+blocker+"','"+blockee+"');";
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
	
	
	public boolean removeBlockFromDB(String blocker,String blockee) {
		boolean result = false;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="DELETE FROM BLOCKING_LIST WHERE BLOCKER ='"+ blocker+"' AND BLOCKEE ='"+blockee+"';";
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
	public boolean checkIfUserExists(String blockee) {
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM USERS WHERE USERNAME ='"+ blockee+"'";
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
	

}

