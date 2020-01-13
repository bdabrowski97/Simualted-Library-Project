package www;

import java.io.Serializable;
import java.util.ArrayList;

public class MyLibrary implements Serializable {
	private ArrayList<Book> masterBookList;
	private ArrayList<User> userList;
	
	private String adminUserName = "ADMIN";
	private String adminPassword = "ADMINPASSWORD";
	
	public MyLibrary() {
		masterBookList = new ArrayList<>();
		userList = new ArrayList<>();
	}
	
	//Setters
	public void setMasterBookList(ArrayList<Book> saveThisBookList) {
		masterBookList.clear();
		for (int i = 0; i < saveThisBookList.size(); i++) {
			masterBookList.add(saveThisBookList.get(i));
		}
	}
	public void setMasterUserList(ArrayList<User> saveThisUserList) {
		userList.clear();
		for (int i = 0; i < saveThisUserList.size(); i++) {
			userList.add(saveThisUserList.get(i));
		}
	}
	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	
	
	//getters
	public ArrayList<Book> getMasterBookList() {
		return masterBookList;
	}
	public ArrayList<User> getUserList() {
		return userList;
	}
	public User grabUserByUserName(String userName) {
		for (int i = 0; i < userList.size(); i++) {
			if (userName.equals(userList.get(i).getUserName())) {
				User findUser = userList.get(i);
				return findUser;
			}
		}
		return null;
	}
	
	//Functions
	public void registerUser(User registerUser) { //in GUI, make sure the username being made doesn't exist before doing this
		userList.add(registerUser);				//this would be the final step, as all the user creation and nusername checks happen on the user class and gui side
	}
	public void registerBook(Book bookToAdd) {
		masterBookList.add(bookToAdd);
	}
	
	
	public boolean deleteUser(String userToDelete) { //returns true if the user was deleted, false if user not found
		boolean userDeleted = false;
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUserName().equals(userToDelete)) {
				userList.remove(i);
				userDeleted = true;
			}
		}
		return userDeleted;
	}
	
	public void deleteBook(String isbn, String bookid) {
		for (int i = 0; i < masterBookList.size(); i++) {
			if ((masterBookList.get(i).getIsbn().equals(isbn)) && (masterBookList.get(i).getBookid().equals(bookid))) {
				masterBookList.remove(masterBookList.get(i));
			}
		}
	}


	

	
	

}
