//$Id$
package table;

public class Bank {
	private int id;
	private String name;
	private String address;
	private boolean is_main;
	private int main_id;
	private boolean is_active;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public boolean isIs_main() {
		return is_main;
	}
	public int getMain_id() {
		return main_id;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setIs_main(boolean is_main) {
		this.is_main = is_main;
	}
	public void setMain_id(int main_id) {
		this.main_id = main_id;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	
	
}
