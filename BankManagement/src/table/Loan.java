
package table;

import java.sql.Date;

public class Loan {
	private int id;
	private String loan_type;
	private int status;
	private int loan_amount;
	private int interest;
	private Date applied_date;
	private Date start_date;
	private Date end_date;
	private Date due_date;
	private int due_amount;
	private int paid_amount;
	private int total_amount;
	private int account_id;
	private int period;
	public int getId() {
		return id;
	}
	public String getLoan_type() {
		return loan_type;
	}
	public int getStatus() {
		return status;
	}
	public int getLoan_amount() {
		return loan_amount;
	}
	public int getInterest() {
		return interest;
	}
	public Date getApplied_date() {
		return applied_date;
	}
	public Date getStart_date() {
		return start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public Date getDue_date() {
		return due_date;
	}
	public int getDue_amount() {
		return due_amount;
	}
	public int getPaid_amount() {
		return paid_amount;
	}
	public int getTotal_amount() {
		return total_amount;
	}
	public int getAccount_id() {
		return account_id;
	}
	public int getPeriod() {
		return period;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLoan_type(String loan_type) {
		this.loan_type = loan_type;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setLoan_amount(int loan_amount) {
		this.loan_amount = loan_amount;
	}
	public void setInterest(int interest) {
		this.interest = interest;
	}
	public void setApplied_date(Date applied_date) {
		this.applied_date = applied_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}
	public void setDue_amount(int due_amount) {
		this.due_amount = due_amount;
	}
	public void setPaid_amount(int paid_amount) {
		this.paid_amount = paid_amount;
	}
	public void setTotal_amount(int total_amount) {
		this.total_amount = total_amount;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	
	
}
