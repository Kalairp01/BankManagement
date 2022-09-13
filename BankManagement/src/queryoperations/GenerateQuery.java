//$Id$
package queryoperations;


import java.util.List;
import java.util.Map;

public class GenerateQuery {
	public static String insert_query(String table,List<String> details) {
		int length = details.size(),value=1;
		String parameters ="";
		String columns ="";
		String query="insert into "+table+" ";
		parameters+="(";
		
		for(String detail : details) {
			if(value!=length) {
			columns+=detail+",";
			parameters+="?,";
			}
			else {
				columns+=detail;
				parameters+="?";
			}
			value++;
		}
		
		value=1;
		parameters+=")";
		query+="(" +columns +")" + " values "+ parameters+";";
		
		return query;
	}
	
	public static String select_query(String table,List<String> columns,Map<String,String> conditions) {
		String query ="select ";
		String where ="";
		int value = 1;
		if(columns==null) {
			query+="* from "+table+" ";
		}
		else {
			for(int i=0;i<columns.size();i++) {
				if(i!=columns.size()-1) {
					query+=columns.get(i)+",";
				}
				else
					query+=columns.get(i)+" from ";

			}
			query +=table+" ";
		}
		if(conditions!=null) {
			where="where "+hashtostr(conditions,"and");
		}
		query+=where+";";
		
		return query;
		
	}
	
	public static String update_query(String table,Map<String,String> values,Map<String,String> conditions) {
		String query="update "+table+" set ";
		String valuestr="";
		String where="";
		int num=1;
		valuestr=hashtostr(values,",");
		if(conditions!=null) {
			where="where "+hashtostr(conditions,"and");
		}
		query+=valuestr+where+";";
		return query;
	}
	
	private static String hashtostr(Map<String,String> details,String condition) {
		String query="";
		int value=1;
		for(Map.Entry<String, String> detail : details.entrySet()) {
			if(detail.getKey().endsWith("no") || detail.getKey().endsWith("date")
					|| detail.getKey().startsWith("is") || detail.getKey().endsWith("id")
					|| detail.getKey().endsWith("status") || detail.getKey().endsWith("balance")
					|| detail.getKey().endsWith("amount") || detail.getKey().endsWith("loan")) {
				if(value!=details.size())
					query+=detail.getKey()+"="+detail.getValue()+" "+ condition+" ";
				else
					query+=detail.getKey()+"="+detail.getValue();
			}
			else {
					if(value!=details.size())
						query+=detail.getKey()+"='"+detail.getValue()+"' "+condition+" ";
					else
						query+=detail.getKey()+"='"+detail.getValue()+"' ";
			}
				value++;
				
		}
		return query;
	}
		
}
