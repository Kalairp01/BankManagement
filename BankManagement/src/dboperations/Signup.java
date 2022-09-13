//$Id$
package dboperations;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import queryoperations.GenerateQuery;

public class Signup {
	public static boolean signup(Map<String,String> details) {
		List<String> columns = new ArrayList<>();
		for(Map.Entry<String, String> detail : details.entrySet())
			columns.add(detail.getKey());
		PreparedStatement ps = Utils.generate_preparedstatement(GenerateQuery.insert_query("users", columns));
			try {
				ps.setString(1, details.get("name"));
				ps.setString(2, details.get("phone"));
				ps.setString(3, details.get("email"));
				ps.setString(4, details.get("password"));
				ps.setString(5, details.get("address"));
				if(details.size()==7)
					ps.setInt(7, Integer.parseInt(details.get("bank_id")));
				ps.executeUpdate();
				ps.close();
				return true;
				} catch (SQLException e) {
				e.printStackTrace();
				return false;
				
			}
			
	}
}
