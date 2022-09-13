//$Id$
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

public class AccountActivity{
		public static List<String> fields = new ArrayList<String>(){
        {
            add("account_type");
            add("balance");
            add("interest");
            add("loans_no");
            add("pending_loan");
            add("user_id");
            add("is_active");
            add("bank_id");
            add("created_date");
            add("id");
        }
		};
	public static boolean create(Map<String,String> details , int user_id, int bank_id) {
		try {
			if(!has_account(user_id,bank_id)) {
				List<String> column = new ArrayList<>();
				column.add(fields.get(0));
				column.add(fields.get(1));
				column.add(fields.get(5));
				column.add(fields.get(7));
				column.add(fields.get(8));
				PreparedStatement ps = Utils.generate_preparedstatement(GenerateQuery.insert_query("account", column));
				ps.setString(1, details.get(fields.get(0)));
				ps.setInt(2, Integer.parseInt(details.get(fields.get(1))));
				ps.setInt(3, user_id);
				ps.setInt(4, bank_id);
				ps.setDate(5, Utils.get_date());
				ps.executeUpdate();
				ps.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void deposit(int account_id,int amount) {
		Map<String,String> conditions = new HashMap<>();
		conditions.put(fields.get(9),Integer.toString(account_id));
		List<String> columns = new ArrayList<>();
		columns.add(fields.get(1));
		ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("account", columns, conditions));
		try {
			if(rs.next()) {
				int balance = rs.getInt("balance");
				balance+=amount;
				Map<String,String> values = new HashMap<>();
				values.put(fields.get(1),Integer.toString(balance));
				Utils.execute_update(GenerateQuery.update_query("account", values, conditions));	
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		} 
	}
	
	public static boolean transaction(int account_id, Map<String,String> details) {
		List<String> holdercolumn = new ArrayList<>();
		Map<String,String> holderconditions = new HashMap<>();
		List<String> beneficiarycolumn = new ArrayList<>();
		Map<String,String> beneficiaryconditions = new HashMap<>();
		
		
		holderconditions.put(fields.get(9),Integer.toString(account_id));
		holdercolumn.add(fields.get(1));
		
		beneficiaryconditions.put(fields.get(6),"true");
		beneficiaryconditions.put(fields.get(9),details.get(fields.get(9)));
		beneficiarycolumn.add(fields.get(1));
		
		ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("account", holdercolumn, holderconditions));
		ResultSet rs1 = Utils.generate_resultset(GenerateQuery.select_query("account", beneficiarycolumn, beneficiaryconditions));
		
		try {
			if(rs.next() && rs1.next()) {
				if(rs.getInt((fields.get(1))) >= Integer.parseInt(details.get("amount"))){
					
					Map<String,String> beneficiaryupdateconditions = new HashMap<>();
					Map<String,String> holderupdateconditions = new HashMap<>();					
					Map<String,String> holdervalues = new HashMap<>();
					Map<String,String> beneficiaryvalues = new HashMap<>();
					
					holderupdateconditions.put(fields.get(9),Integer.toString(account_id));
					beneficiaryupdateconditions.put(fields.get(9),details.get(fields.get(9)));
					holdervalues.put(fields.get(1),Integer.toString(rs.getInt(fields.get(1))-Integer.parseInt(details.get("amount"))));
					beneficiaryvalues.put(fields.get(1),Integer.toString(rs1.getInt(fields.get(1))+Integer.parseInt(details.get("amount"))));
					
					
					Utils.execute_update(GenerateQuery.update_query("account", holdervalues, beneficiaryupdateconditions));
					Utils.execute_update(GenerateQuery.update_query("account", beneficiaryvalues, beneficiaryupdateconditions));
					
					Map<String,String> holdervalue = new HashMap<>();
					Map<String,String> beneficiaryvalue = new HashMap<>();
					
					holdervalue.put("transaction_type","debit");
					holdervalue.put("transacted_account",details.get("id"));
					holdervalue.put("amout",details.get("amount"));
					holdervalue.put("account_id",Integer.toString(account_id));
					holdervalue.put("balance",Integer.toString(rs.getInt(fields.get(1))-Integer.parseInt(details.get("amount"))));
					
					beneficiaryvalue.put("transaction_type", "credit");
					beneficiaryvalue.put("transacted_account",Integer.toString(account_id));
					beneficiaryvalue.put("amout",details.get("amount"));
					beneficiaryvalue.put("account_id",details.get("id"));
					beneficiaryvalue.put("balance",Integer.toString(rs1.getInt(fields.get(1))+Integer.parseInt(details.get("amount"))));
 					
					TransactionActivity.create(holdervalue);
					TransactionActivity.create(beneficiaryvalue);
					
					return true;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean delete(int id) {
		ResultSet rs = has_pendingloan(id);
		try {
			if(rs.next()) {
				Map<String,String> conditions = new HashMap<>();
				Map<String,String> values = new HashMap<>();
				conditions.put(fields.get(9),Integer.toString(id));
				values.put(fields.get(1),Integer.toString(rs.getInt(fields.get(9))-1000));
				values.put(fields.get(6),"false");
				Connection con = Utils.init_connection();
				Statement st = con.createStatement();
				st.executeUpdate(GenerateQuery.update_query("account", values, conditions));
				con.close();
				st.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	private static boolean has_account(int user_id , int bank_id) throws Exception {
		Map<String,String> conditions = new HashMap<>();
		conditions.put(fields.get(5),Integer.toString(user_id));
		conditions.put(fields.get(7),Integer.toString(bank_id));
		ResultSet rs = Utils.generate_resultset((GenerateQuery.select_query("account", null, conditions)));
		if (rs.next()) {
			if(rs.getBoolean(fields.get(6))) {
				rs.close();				
			return false;
			}
		}
		return true;
	}
	
	private static ResultSet has_pendingloan(int id){
		List<String> column = new ArrayList<>();
		column.add(fields.get(4));
		column.add(fields.get(1));
		Map<String,String> conditions = new HashMap<>();
		conditions.put("id",Integer.toString(id));
		ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("account", column, conditions));
		
		try {
			if(rs.next()) {
				if(rs.getBoolean(fields.get(4)) && rs.getInt(fields.get(1))<1000) {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	
}
