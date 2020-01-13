package www;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TestingFeaturesMain {
	
	public static void main(String[] args) {
	
		//In theory. this is how we should access all these files
		
		/*File file = new File("masterFile.bin");
		MyLibrary myLibrary = new MyLibrary();
		
		if (file.exists() == true) {
			try {
				MyLibrary test = (MyLibrary) readObjectFromFile("masterFile.bin");
				myLibrary.setMasterBookList(test.getMasterBookList());
			}
			catch (ClassNotFoundException e) {
				System.out.println("file not found");
			}
		}
		else {
			//do nothing
		} 
		User test = myLibrary.getUserList().get(0);
		Book book = test.getRentedBooks().get(0);*/
		
		
		

		        Date currentDate = new Date();
		        

		        // convert date to calendar
		        Calendar c = Calendar.getInstance();
		        c.setTime(currentDate);

		        // manipulate date
		      
		        c.add(Calendar.DATE, 7); //same with c.add(Calendar.DAY_OF_MONTH, 1);
		        

		        // convert calendar to date
		        Date currentDatePlusOne = c.getTime();
		        System.out.println(currentDate);
		        System.out.println(currentDatePlusOne);

		     

		    
		
		
		
		
		
		
		
		
		//Use this chain of code to convert ints to strings and back, we'll use this for bookid
		/*String new1 = "111";
		int new2 = Integer.parseInt(new1);
		new2+=25;
		new1= String.valueOf(new2);
		System.out.println(new1);*/
		
		
	
		
		
		
		
		
		//These two static methods work to encrypt and decrypt strings with a char+10 and char-10 difference

		/*String abc = "abc";
		System.out.println(encryptPassword(abc));
		String bcd = encryptPassword(abc);
		boolean equals = false;
		if (abc.equals(decryptPassword(bcd))) {
			equals = true;
		}
		
		System.out.println(equals);*/
		
	
		
		
		
		
		
		
		
		
		
		
	}
	
	//Password Encryption
	public static String encryptPassword(String password) {
		StringBuilder sB = new StringBuilder();
		for (int i = 0; i < password.length(); i++) {
			char ogChar = password.charAt(i);
			int changeInt = (int) ogChar;
			changeInt += 10;
			char changeChar = (char) changeInt;
			sB.append(changeChar);
		}
		String result = sB.toString();
		return result;
	}
	
	//Password Decryption
	public static String decryptPassword(String password) {
		StringBuilder sB = new StringBuilder();
		for (int i = 0; i < password.length(); i++) {
			char ogChar = password.charAt(i);
			int changeInt = (int) ogChar;
			changeInt -= 10;
			char changeChar = (char) changeInt;
			sB.append(changeChar);
		}
		String result = sB.toString();
		return result;
	}
	
	
	
	//SAVING&&LOADING
		public static Object readObjectFromFile(String filePath) throws ClassNotFoundException {
			try {
					FileInputStream fileIn = new FileInputStream(filePath);
					ObjectInputStream objectIn = new ObjectInputStream(fileIn);
					
					Object obj =  objectIn.readObject();
					
					System.out.printf("%s loaded\n", filePath);
					objectIn.close();
					return obj;
			}
			catch (IOException e) {
				System.out.printf("could not load %s\n",filePath);
				return null;
			}
		}
		
		public static void writeObjectToFile(Object book, String fileName) {
			try {
					FileOutputStream fileOut = new FileOutputStream(fileName);
					ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
					objectOut.writeObject(book);
					objectOut.close();
					System.out.printf("saved %s\n",fileName);
			}
			catch (IOException e) {
				System.out.printf("could not save %s\n", fileName);
			}
		}
	
	

}
