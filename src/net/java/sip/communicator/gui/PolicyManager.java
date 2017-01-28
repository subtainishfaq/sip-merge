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

public class PolicyManager extends DatabaseServer{
	public PolicyManager(){
		super();
	}
	public ResultSet showPolicyList(String currentUser){
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		int current_policyID = getCurrentPolicyID(currentUser);
		Query="SELECT * " + "FROM POLICY WHERE id <> "+current_policyID ;
		// TODO Show all plans except user's current choice ?
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
	public boolean changePolicy(String username, String policyName) {
		boolean result = false;
		int policy_id = getPolicyIDfromName(policyName);
		if (policy_id > 0){

			try {
				this.stmt = con.createStatement();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			Query="UPDATE USERS SET policy_id = "+policy_id +" WHERE username = '"+username+"';";
			System.out.println(Query);
			try {
				if (stmt.executeUpdate(Query)>0)
					result = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		close();
		return result;
	}

	private int getPolicyIDfromName(String policyName){
		ResultSet rs = null;
		int ret = -1 ; 
		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM POLICY WHERE name ='"+policyName+"';"; 
		// TODO Show all plans except user's current choice ?
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* return resultSet w/o checking
		 * caller must check appropriately 
		 */ 
		try {
			if (rs.next()){
				ret = rs.getInt("id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	private String getPolicyNameFromID(int policy_id){
		ResultSet rs = null;
		String ret = "";
		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM POLICY WHERE id ='"+policy_id+"';"; 
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* return resultSet w/o checking
		 * caller must check appropriately 
		 */ 
		try {
			if (rs.next()){
				ret = rs.getString("name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	private int getCurrentPolicyID(String currentUser){
		ResultSet rs = null;
		int ret = -1 ; 
		try {
			stmt = con.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Query="SELECT * " + "FROM USERS WHERE username ='"+ currentUser+"';"; 

		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs.next()){
				ret = rs.getInt("policy_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public String getPolicyName(String currentUser) {
		int ret = getCurrentPolicyID(currentUser);
		return getPolicyNameFromID(ret);
	}

}
