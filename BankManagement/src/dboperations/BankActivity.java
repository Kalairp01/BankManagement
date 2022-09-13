package dboperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import queryoperations.GenerateQuery;

public class BankActivity {
	public static List<String> fields = new ArrayList<String>(){
        {
            add("name");
            add("address");
            add("is_main");
            add("is_active");
            add("id");
        }
		};
	public static boolean create(int main_id, Map<String,String> details) {
		List<String> columns = new ArrayList<>();
		columns.add(fields.get(0));
		columns.add(fields.get(1));
		
		PreparedStatement ps = Utils.generate_preparedstatement(GenerateQuery.insert_query("bank", columns));
		try {
			ps.setString(1, details.get(fields.get(0)));
			ps.setString(2,details.get(fields.get(1)));
			ps.executeUpdate();
			ps.close();
			
			Map<String,String> conditions = new HashMap<>();
			conditions.put("name", details.get("name"));
			columns.clear();
			columns.add("id");
			
			ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("bank", columns, conditions));
			columns.clear();
			conditions.clear();
			
			if(rs.next()) {
				columns.add("main_id");
				columns.add("branch_id");
				ps = Utils.generate_preparedstatement(GenerateQuery.insert_query("branch", columns));
				ps.setInt(1, main_id);
				ps.setInt(2, rs.getInt("id"));
				ps.executeUpdate();
				ps.close();
			}
			Utils.execute_update("update bank set is_main=true where id="+main_id);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean delete(int id) {
		
		String query = "select count(branch_id) from branch where main_id="+id;
		ResultSet rs = Utils.generate_resultset(query);
		try {
			if(rs.next()) {
				return false;
			}
			else {
				Map<String,String> conditions = new HashMap<>();
				Map<String,String> values = new HashMap<>();
				
				values.put("is_active","false");
				conditions.put("id",Integer.toString(id));
				
				Utils.execute_update(GenerateQuery.update_query("bank", values, conditions));
				move_accounts(id);
				
				 String delete = "delete from branch where branch_id="+id;
				 Connection con = Utils.init_connection();
				 Statement st = con.createStatement();
				 st.executeUpdate(delete);
				 st.close();
				 con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private static void move_accounts(int id) {
		String query = "select main_id from branch where branch_id="+id;
		ResultSet rs = Utils.generate_resultset(query);
		try {
			if(rs.next()) {
				int bank_id = rs.getInt("main_id");
				Map<String,String> conditions = new HashMap<>();
				Map<String,String> values = new HashMap<>();
				conditions.put("bank_id",Integer.toString(id));
				conditions.put("bank_id", Integer.toString(bank_id));
				Utils.execute_update(GenerateQuery.update_query("acount", values, conditions));
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
