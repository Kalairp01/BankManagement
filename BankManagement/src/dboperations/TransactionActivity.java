//$Id$
package dboperations;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import queryoperations.GenerateQuery;

public class TransactionActivity {
	public static List<String> fields = new ArrayList<String>(){
        {
            add("transaction_type");
            add("transacted_account");
            add("amount");
            add("transacted_date");
            add("account_id");
            add("balance");
        }
		};
	public static void create(Map<String,String> values) {
		PreparedStatement ps = null;
		ps=Utils.generate_preparedstatement(GenerateQuery.insert_query("transaction", fields));
		try {
			ps.setString(1, values.get(fields.get(0)));
			ps.setInt(2, Integer.parseInt(values.get(fields.get(1))));
			ps.setInt(3, Integer.parseInt(values.get(fields.get(2))));
			ps.setDate(4, Utils.get_date());
			ps.setInt(5, Integer.parseInt(values.get(fields.get(4))));
			ps.setInt(6, Integer.parseInt(values.get(fields.get(5))));
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
}
