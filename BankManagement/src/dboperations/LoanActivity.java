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

public class LoanActivity {
	public static List<String> fields = new ArrayList<String>(){
        {
            add("loan_type");
            add("loan_amount");
            add("interest");
            add("applied_date");
            add("start_date");
            add("end_date");
            add("due_date");
            add("due_amount");
            add("paid_amount");
            add("total_amount");
            add("account_id");
            add("period");
            add("status");
            add("id");
        }
		};
	public static boolean create(Map<String,String> details , int account_id) {
		if(validate(account_id)){
			List<String> columns = new ArrayList<>();
			for(Map.Entry<String,String> detail : details.entrySet()) {
				columns.add(detail.getKey());
			}
			columns.add(fields.get(10));
			columns.add(fields.get(12));
			columns.add(fields.get(3));
			PreparedStatement ps = Utils.generate_preparedstatement(GenerateQuery.insert_query("loan", columns));
			try {
				ps.setString(1,details.get(fields.get(0)));
				ps.setInt(2, Integer.parseInt(fields.get(1)));
				ps.setInt(3, Integer.parseInt(fields.get(11)));
				ps.setInt(4, account_id);
				ps.setInt(5, 1);
				ps.setDate(6, Utils.get_date());
				ps.executeUpdate();
				
				Map<String,String> values = new HashMap<>();
				Map<String,String> conditions = new HashMap<>();
				
				values.put("pending_loan","true");
				conditions.put("id",Integer.toString(account_id));
				
				Utils.execute_update(GenerateQuery.update_query("account", values, conditions));
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return true;
		}
		return false;
	}
	
	public static boolean approve(int loan_id , boolean approve) throws Exception {
		if(approve) {
			ResultSet rs = null;

			Map<String,String> values = new HashMap<>();
			Map<String,String> conditions = new HashMap<>();
			int dueamount;
			
			conditions.put(fields.get(13),Integer.toString(loan_id));
			values.put(fields.get(12),"2");
			values.put(fields.get(4),Utils.get_date().toString());
			values.put(fields.get(8), Integer.toString(0));
			
			rs = Utils.generate_resultset("select amount,period from loan where id="+loan_id);
			values.put(fields.get(5), end_date(rs.getInt(11)));
			int amount=0;
			
			if(update_account(loan_id)>1) {
				values.put(fields.get(2), Integer.toString(11));
				if(rs.next()) {
					amount = rs.getInt("amount");
					amount+=Math.ceil(amount*0.11);
					values.put(fields.get(9),Integer.toString(amount));
				}
			}
			else {
				values.put(fields.get(2), Integer.toString(15));
				if(rs.next()) {
					amount = rs.getInt("amount");
					amount+=Math.ceil(amount*0.15);
					values.put(fields.get(9),Integer.toString(amount));
				}
			}
			
			if(rs.next()) {
				int period = rs.getInt("period");
				dueamount  = (int) Math.ceil(amount/(double)(period*12));
				values.put(fields.get(7),Integer.toString(dueamount));	
			}
			rs.close();
			values.put(fields.get(6),get_duedate(Utils.get_date()).toString());
			Utils.execute_update(GenerateQuery.update_query("loan", values, conditions));
		}
		else {
			Map<String,String> conditions = new HashMap<>();
			Map<String,String> values = new HashMap<>();
			List<String> column = new ArrayList<>();
			conditions.put(fields.get(13),Integer.toString(loan_id));
			values.put(fields.get(12),"3");
			
			Utils.execute_update(GenerateQuery.update_query("loan", values, conditions));
			conditions.clear();
			values.clear();
			column.add(fields.get(10));
			conditions.put(fields.get(13),Integer.toString(loan_id));
			ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("loan", column, conditions));
			
			values.put("pendin_loan","false");
			conditions.put("id",Integer.toString(rs.getInt(fields.get(10))));
			
			Utils.execute_update(GenerateQuery.update_query("account", values, conditions));
			rs.close();
			
		}
		return approve;
	}
	
	public static boolean pay_due(int loan_id) {
		Map<String,String> conditions = new HashMap<>();
		Map<String,String> values = new HashMap<>();
		List<String> columns = new ArrayList<>();
		
		conditions.put(fields.get(13),Integer.toString(loan_id));
		columns.add(fields.get(7));
		columns.add(fields.get(8));
		columns.add(fields.get(6));
		columns.add(fields.get(10));
		columns.add(fields.get(6));
		
		ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("loan", columns, conditions));
		try {
			if(rs.next()) {
			ResultSet rs1 = Utils.generate_resultset("select balance from account where id="+rs.getInt(fields.get(10)));
			if(rs1.getInt("balance")>rs.getInt(fields.get(7))) {
				Map<String,String> accountconditions = new HashMap<>();
				Map<String,String> accountvalues = new HashMap<>();
				accountconditions.put("id",Integer.toString(rs.getInt(fields.get(10))));
				accountvalues.put("balance",Integer.toString(rs.getInt("due_amount")-rs1.getInt("balance")));
				Utils.execute_update(GenerateQuery.update_query("account", accountvalues, accountconditions));
				
				values.put(fields.get(8),Integer.toString(rs.getInt(fields.get(8))+rs.getInt(fields.get(7))));
				values.put(fields.get(6) , get_duedate(rs.getDate(fields.get(6))));
				
				rs1.close();
				rs.close();
				return true;
				
			}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}
	
	public static void close(int loan_id) {
		Map<String,String> conditions = new HashMap<>();
		Map<String,String> values = new HashMap<>();
		List<String> columns = new ArrayList<>();
		columns.add(fields.get(10));
		
		conditions.put(fields.get(13),Integer.toString(loan_id));
		values.put(fields.get(12),"4");
		Utils.execute_update(GenerateQuery.update_query("loan", values, conditions));
		
		ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("loan", columns, conditions));
		try {
			if(rs.next()) {
				Map<String,String> accountconditions = new HashMap<>();
				Map<String,String> accountvalues = new HashMap<>();
				
				accountconditions.put("id",Integer.toString(rs.getInt("account_id")));
				accountvalues.put("pending_loan","false");
				
				Utils.execute_update(GenerateQuery.update_query("account", accountvalues, accountconditions));
				rs.close();
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	private static boolean validate(int account_id) {
		List<String> columns = new ArrayList<>();
		Map<String,String> conditions = new HashMap<>();
		columns.add("pending_loan");
		conditions.put("account_id",Integer.toString(account_id));
		ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("account", columns, conditions));
		try {
			if(rs.next()) {
				if(rs.getBoolean("pendingLoan"))
					return false;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;

	}
	
	private static String end_date(int period) {
		long millis = System.currentTimeMillis()+31556952000L*period;
		java.sql.Date end_date = new java.sql.Date(millis) ;
		return end_date.toString();
		
	}
	
	private static int update_account(int loan_id) {
		List<String> column = new ArrayList<>();
		Map<String,String> conditions = new HashMap<>();
		
		column.add(fields.get(10));
		conditions.put(fields.get(13),Integer.toString(loan_id));
		
		ResultSet rs = Utils.generate_resultset(GenerateQuery.select_query("loan", column, conditions));
		
		try {
			if(rs.next()) {
				String account_id =  Integer.toString(rs.getInt(fields.get(10)));
				String query = "select loans_no from account inner join loan on loan.account_id = "
						+account_id+" and account.id ="+account_id+" and loan.status =1 ;";
				ResultSet rs1 = Utils.generate_resultset(query);
				if(rs1.next()) {
					String loans_no = Integer.toString(rs.getInt("loans_no")+1);
					Map<String,String> updateconditions = new HashMap<>();
					Map<String,String> values = new HashMap<>();
					
					updateconditions.put("id",account_id);
					values.put("loans_no", loans_no);
					Utils.execute_update(GenerateQuery.update_query("account", values, updateconditions));
					rs1.close();
					return Integer.parseInt(loans_no);
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
		
	}
	
	private static String get_duedate(java.sql.Date date) {
		long millis = date.getTime()+31l*24l*60l*60l*1000l;
		return new java.sql.Date(millis).toString();
		
	}
}
