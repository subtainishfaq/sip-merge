/**
* Omada 20
*
* Sarlis Dimitris 03109078
* Stathakopoulou Chrysa 03109065
* Tzannetos Dimitris 03109010
*
*/

package net.java.sip.communicator.gui;

import java.sql.*;


public class RegisterUser extends DatabaseServer {



	public RegisterUser() {
		super();
	}
	public boolean addToDB (String username,String password) {


		Integer policy_id=1;
		boolean result = false;

		try {
			this.stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="INSERT INTO USERS " +"VALUES (NULL,'"+username+"', '"+password+"', "+policy_id+",0)";

		try {
			if (stmt.executeUpdate(Query)>0)
				result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// RegisterUser Server closes DB connection
		close();
		return result;


	}
	public boolean checkIfUsernameAvailable(String username){

		ResultSet rs = null;
		String result="";



		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT username "+"FROM USERS ";
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while (rs.next()) {
				result=rs.getString("username");
				if (result.equals(username)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
