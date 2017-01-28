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




public class DatabaseServer {
	protected Connection con;
	protected Statement stmt;
	protected String Query;

	public DatabaseServer() {
		try {
    		Class.forName("com.mysql.jdbc.Driver");
    		String connectionURL = "jdbc:mysql://localhost:3306/softeng";
    		this.con = DriverManager.getConnection(connectionURL, "root", "");
    		 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}

	public Connection getCon() {
		return con;
	}
	public void close(){
		try {
			this.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
