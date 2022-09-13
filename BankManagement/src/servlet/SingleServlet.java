package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dboperations.AccountActivity;
import dboperations.BankActivity;
import dboperations.JsonOperations;
import dboperations.LoanActivity;
import dboperations.Login;
import dboperations.Signup;

@WebServlet("/*")
public class SingleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SingleServlet() {
        super(); 
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		method_handler(request,response,"get");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		method_handler(request,response,"post");
	}
	
	private void method_handler(HttpServletRequest req, HttpServletResponse res, String type) {
		if(type.equals("get")) {
			getactions(req,res);
		}
		else {
			postactions(req,res);
		}
		
	}

	
	
	private void postactions(HttpServletRequest req, HttpServletResponse res) {
		String[] uri = req.getPathInfo().split("/");
		int last = uri.length-1;
		JsonObject obj = ServletActions.getjson(req);
		
		if(uri[last].equals("signup")) {
			Map<String,String> details = new HashMap<>();
			details.put("name",obj.get("name").getAsString());
			details.put("phone",obj.get("phone").getAsString());
			details.put("email",obj.get("email").getAsString());
			details.put("password",obj.get("password").getAsString());
			details.put("address",obj.get("address").getAsString());
			Signup.signup(details);
		}
		else if(uri[last].equals("create")) {
			if(uri[last-1].equals("branch")) {
				Map<String,String> details = new HashMap<>();
				int main_id = Integer.parseInt(uri[last-2]);
				details.put("name",obj.get("name").getAsString());
				details.put("address",obj.get("address").getAsString());
				BankActivity.create(main_id, details);
			}
			else if(uri[last-1].equals("loan")) {
				int account_id = Integer.parseInt(uri[last-2]);
				Map<String,String> details = new HashMap<>();
				details.put("loan_type", obj.get("loan_type").getAsString());
				details.put("loan_amount", obj.get("loan_amount").getAsString());
				details.put("period", obj.get("period").getAsString());
				LoanActivity.create(details, account_id);
			}
			
			else if(uri[last-1].equals("account")) {
				int bank_id = Integer.parseInt(uri[last-2]);
				int user_id = Integer.parseInt(uri[last-4]);
				Map<String,String> details = new HashMap<>();
				details.put("account_type",obj.get("account_type").getAsString());
				details.put("balance", obj.get("balance").getAsString());
				AccountActivity.create(details, user_id, bank_id);
			}
			
		}
		
		else if(uri[last].equals("transact")) {
			int account_id = Integer.parseInt(uri[last-1]); 
			Map<String,String> details = new HashMap<>();
			details.put("id", obj.get("id").getAsString());
			details.put("amount", obj.get("amount").getAsString());
			AccountActivity.transaction(account_id, details);
		}
		
		else if(uri[last].equals("pay")) {
			int loan_id = Integer.parseInt(uri[last-1]);
			LoanActivity.pay_due(loan_id);
		}
		
		else if (uri[last].equals("approve")) {
			boolean approve = obj.get("approve").getAsBoolean();
			int loan_id = Integer.parseInt(uri[last-1]);
			try {
				LoanActivity.approve(loan_id, approve);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if (uri[last].equals("close")) {
			int loan_id = Integer.parseInt(uri[last-1]);
			LoanActivity.close(loan_id);
		}
		
		else if (uri[last].equals("delete")) {
			if(uri[last-2].equals("account")){
				int account_id = Integer.parseInt(uri[last-1]);
				AccountActivity.delete(account_id);
			}
			
			if(uri[last-2].equals("branch")) {
				int bank_id = Integer.parseInt(uri[last-2]);
				BankActivity.delete(bank_id);
			}
		}
		
	}
	
	private void getactions(HttpServletRequest req, HttpServletResponse res) {
		String[] uri = req.getPathInfo().split("/");
		int last = uri.length-1;
		
		if(uri[last].equals("login")) {
			JsonObject obj = ServletActions.getjson(req);
			String email=obj.get("email").getAsString();
			String pass=obj.get("password").getAsString();
			String status = Login.login(email, pass);
			System.out.println(status);
		}
		
		else if(uri[last].equals("transaction")){
			int user_id = Integer.parseInt(uri[last-1]);
			JsonOperations.get_usertransaction(user_id);
		}
		
		else if (uri[last].equals("loan")) {
			if(uri[last-2].equals("user")) {
				int user_id = Integer.parseInt(uri[last-1]);
				JsonOperations.get_userloan(user_id);
			}
			
			if(uri[last-2].equals("bank")) {
				int bank_id = Integer.parseInt(uri[last-1]);
				JsonOperations.get_bankloan(bank_id);
			}
			
		}
		
		else if (uri[last].equals("account")) {
			if(uri[last-2].equals("user")) {
				int user_id = Integer.parseInt(uri[last-1]);
				JsonOperations.get_useraccount(user_id);
			}
			
			if(uri[last-2].equals("bank")) {
				int bank_id = Integer.parseInt(uri[last-1]);
				JsonOperations.get_bankaccount(bank_id);
			}
		}
		
			
	}
	
	
}
