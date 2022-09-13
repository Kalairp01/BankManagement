//$Id$
package dboperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import queryoperations.GenerateQuery;

public class Login {
	public static String login(String email,String password) {
		List<String> columns = new ArrayList<>();
		Map<String,String> conditions = new HashMap<>();
		columns.add("id");
		columns.add("password");
		conditions.put("email",email);
		ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("users", columns, conditions));
		try {
			if(rs.next()) {
				if(rs.getString("password").equals(password)) {
					return Integer.toString(rs.getInt("id"));
				}
				else {
					return "incorrect_password";
				}
			}
			else
				return "user not found";
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
