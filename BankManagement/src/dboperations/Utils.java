//$Id$
package dboperations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {
	public static Connection init_connection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/kalai-15140";
		String username = "kalai-15140";
		String password ="1234";
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}
	
	public static ResultSet generate_resultset(String query)  {
		Connection con=null;
		Statement st = null;
		ResultSet rs = null;
		try {
			 con = init_connection();
			 st = con.createStatement();
			 rs = st.executeQuery(query);
			 
			return rs;
			 
		} catch (ClassNotFoundException | SQLException e) {
			return null;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static PreparedStatement generate_preparedstatement(String query) {
		Connection con=null;
		PreparedStatement ps=null;
		try {
			con = Utils.init_connection();
		    ps = con.prepareStatement(query);
			return ps;
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	public static void execute_update(String query){
		try {
			Connection con = Utils.init_connection();
			Statement st = con.createStatement();
			st.executeUpdate(query);
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Date get_date() {
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		return date;
	}
	
	
}
