package www;

import java.io.Serializable;

public class userAddress implements Serializable {
	
	//parameters
	private String street;
	private String town;
	private String state;
	private String zipCode;
	
	String[] stateList = {"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA",
			  "HI","ID","IL","IN","IA","KS","KY","LA","ME","MD",
			  "MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ",
			  "NM","NY","NC","ND","OH","OK","OR","PA","RI","SC",
			  "SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
	
	
	
	
	//Default Constructor
	public userAddress() {
		this("street","town","state","zipCode");
	}
	
	public userAddress(String street, String town, String state, String zipCode) {
		this.street = street;
		this.town = town;
		this.state = state;
		this.zipCode = zipCode;
	}
	
	//Setters
	public void setStreet(String street) {
		this.street = street;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	//Getters
	public String getStreet() {
		return street;
	}
	public String getTown() {
		return town;
	}
	public String getState() {
		return state;
	}
	public String getZipCode() {
		return zipCode;
	}
	
	
	public String toString() {
		return street + ", " + town + ", " + state + ", " + zipCode;
	}
	
	
	
	

}
