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

public class FavoriteManager extends DatabaseServer{
	public FavoriteManager(){
		super();
	}

	public boolean checkIfUserExists(String faved) {
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM USERS WHERE USERNAME ='"+ faved+"'";
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
	public boolean addFavoriteToDB(String username,String faved) {
		boolean result = false;

		try {
			this.stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="INSERT INTO FAVORITES (username,faved) " +"VALUES ('"+username+"','"+faved+"');";
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

	public boolean removeFavoriteFromDB(String user, String faved) {
		boolean result = false;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="DELETE FROM FAVORITES WHERE USERNAME ='"+ user+"' AND FAVED ='"+faved+"';";
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

	public ResultSet showFavoriteList(String user) {
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM FAVORITES WHERE USERNAME ='"+ user+"';";
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
}

