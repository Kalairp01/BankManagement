//$Id$
package table;

import java.sql.Date;

public class Account {
	private int date;
	private String account_type;
	private int balance;
	private int interest;
	private int loans_no;
	private boolean pending_loan;
	private int user_id;
	private boolean is_active;
	private int bank_id;
	private Date created_date;
	public int getDate() {
		return date;
	}
	public String getAccount_type() {
		return account_type;
	}
	public int getBalance() {
		return balance;
	}
	public int getInterest() {
		return interest;
	}
	public int getLoans_no() {
		return loans_no;
	}
	public boolean isPending_loan() {
		return pending_loan;
	}
	public int getUser_id() {
		return user_id;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public int getBank_id() {
		return bank_id;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public void setInterest(int interest) {
		this.interest = interest;
	}
	public void setLoans_no(int loans_no) {
		this.loans_no = loans_no;
	}
	public void setPending_loan(boolean pending_loan) {
		this.pending_loan = pending_loan;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	
	
}
