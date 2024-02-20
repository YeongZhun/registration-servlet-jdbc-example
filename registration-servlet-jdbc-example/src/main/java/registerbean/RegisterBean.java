package registerbean;

import java.io.Serializable;

public class RegisterBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String phone_number;
	private String address;

	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhoneNumber() {
		return phone_number;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phone_number = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}