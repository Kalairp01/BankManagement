//$Id$
package table;

import java.sql.Date;

public class User {
	private int id;
	private String name;
	private String phone_no;
	private String email;
	private String password;
	private String address;
	private boolean is_active;
	private boolean is_manager;
	private int bank_id;
	private Date date;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPhone_no() {
		return phone_no;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getAddress() {
		return address;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public boolean isIs_manager() {
		return is_manager;
	}
	public int getBank_id() {
		return bank_id;
	}
	public Date getDate() {
		return date;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public void setIs_manager(boolean is_manager) {
		this.is_manager = is_manager;
	}
	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
