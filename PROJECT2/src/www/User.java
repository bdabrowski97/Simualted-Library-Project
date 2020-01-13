package www;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	
	//parameters
	private String firstName;
	private String lastName;
	private String age;
	private userAddress address; //using the userAddress class
	
	private String userName;
	private String password;
	private ArrayList<Book> rentedBooks;
	
	//default constructors
	public User() {
		this.userName = null;
		rentedBooks = new ArrayList<>();
	}
	public User(String userName, String password) { //we only use these two parameters first,
		this.userName = userName; 				   //then once we know the username is available,
		this.password = password; 				  //we continue to other info entry on the next screen
		rentedBooks = new ArrayList<>();
	}
	public User(String userName) {
		this.userName = userName;
		rentedBooks = new ArrayList<>();
	}
	
	//Setters 
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setAddress(userAddress address) {
		this.address = address;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//Getters
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getAge() {
		return age;
	}
	public userAddress getAddress() {
		return address;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	public ArrayList<Book> getRentedBooks() {
		return rentedBooks;
	}
	
	//Functions
	
	public String toString() {
		return firstName +" " + lastName + " " + age + " " + userName + " " + password;
	}
	
	public void rentBook(Book book) {
		rentedBooks.add(book);
	}
	public void returnBook(Book book) {
		rentedBooks.remove(book);
	}
	
	public void saveBooks(ArrayList<Book> saveThis) {
		rentedBooks.clear();
		for (int i = 0; i < saveThis.size(); i++) {
			rentedBooks.add(saveThis.get(i));
		}
	}
	public void returnAllBooks() {
		rentedBooks.clear();
	}
	

}
