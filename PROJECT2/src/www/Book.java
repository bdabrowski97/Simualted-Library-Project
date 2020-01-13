package www;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {
	
	//parameters
	private String title;
	private String author;
	private String isbn; //id
	private String bookid; //used to differentiate duplicate books
	private String category; //genre
	private String status; //used to check if a book is in, out(withdrawn)
	private Date date; //the actual date
	private boolean due; //if the book is due
	
	//default constructor
	public Book() {
		this.title = "defaultTitle";
		this.author = "defaultAuthor";
		this.isbn = "defaultISBN";
		this.category = "defaultCategory";
		this.status = "in";
		this.due = false;
	}
	
	public Book(String title, String author, String isbn, String category,String status,String bookid) { //used in-app book creation
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.category = category;
		this.status = status;
		this.bookid = bookid;
	}
	
	/*public Book(String title, String author, String isbn, String bookid, String category,String status) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.bookid = bookid;
		this.category = category;
		this.status = status;
	}*/
	
	
	
	//Setters && getters
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean getDue() {
		return due;
	}
	public void setDue(boolean due) {
		this.due = due;
	}
	
	

	public String toString() {
		return title + " " + "by " + author + " " + isbn + " " + bookid + " " + category;
	}


}
