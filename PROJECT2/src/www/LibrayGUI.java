package www;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class LibrayGUI extends Application {
	
	Stage window;
	Scene startScreen, loginScreen,adminLogin,guestScreen,registerScreen;
	Scene userCreation, userPortal, userSettings, userDeletionRequest;
	Scene adminPortal, adminLibrary,bookCreation;
	
	ArrayList<User> grabUserList;
	ArrayList<Book> grabBookList;
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
		////BOOT UP ACTIONS
		MyLibrary myLibrary = new MyLibrary();
		File myLibraryExists = new File("masterFile.bin");
		
		if (myLibraryExists.exists() == true) {
			try { //if masterFile.bin is found
				MyLibrary grabMyLibrary = (MyLibrary) readObjectFromFile("masterFile.bin");
				myLibrary.setMasterUserList(grabMyLibrary.getUserList());
				myLibrary.setMasterBookList(grabMyLibrary.getMasterBookList());
			}
			catch (ClassNotFoundException e) {
				System.out.println("library not found"); //If not found
			}
		}
		else { //if Library Not Found and no errors
			System.out.println("new library created");
		}
		
		grabUserList = myLibrary.getUserList();
		grabBookList = myLibrary.getMasterBookList();
		window.setOnCloseRequest(e -> writeObjectToFile(myLibrary,"masterFile.bin"));
		
		
		//Bootup due date checks/update due booleans
		Date currentDate = new Date(); //get the current date
		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		
		//loops through all books
		try {
				for (int i = 0; i < grabBookList.size(); i++) {
					if (currentDate.after(grabBookList.get(i).getDate())) {
						grabBookList.get(i).setDue(true);
					}
				}
		} catch (Exception error) {}
		
		c.add(Calendar.DATE, 7); //adds seven days
		Date currentDatePlusSeven = c.getTime(); //Used for when someone takes out a book
		
		
		
		
		/////////////////////////////////////////////////////
		//////////////////////TEST AREA//////////////////////
		
		
		//////////////////////////////////////////////////////
		//////////////////////////////////////////////////////
		
		////START SCREEN - contains buttons to go to login, admin, guest, and quit
		
		//Top Layer
		BorderPane layout1 = new BorderPane();
		Image homeScreenImage = new Image("file:startScreen.png");
		BackgroundSize homeScreenImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background homeScreenBG = new Background(new BackgroundImage(homeScreenImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,homeScreenImageBGS));
		layout1.setBackground(homeScreenBG);
		
		
		//Bottom Layer
		HBox homeScreenOptions = new HBox(30);
		homeScreenOptions.setAlignment(Pos.CENTER);
		
		Button btnLogin = new Button("Login");
		btnLogin.setOnAction(e -> window.setScene(loginScreen));
		
		Button btnRegisterNewUser = new Button("Register");
		btnRegisterNewUser.setOnAction(e -> window.setScene(registerScreen));
		
		Button btnGuestLogin = new Button("Guest");
		TableView guestBookTable = new TableView(); //creates table for guest view
		ArrayList<Book> guestBookList = new ArrayList<>(); //table grabs books from this array and the library table in the user portal 
		
		btnGuestLogin.setOnAction(e -> { //every time you move to the guest screen, the list will refresh
			guestBookList.clear();
			guestBookTable.getItems().clear();
			for (int i = 0; i < grabBookList.size(); i++) {
				guestBookList.add(grabBookList.get(i));
			}
			for (int i = 0; i < grabBookList.size(); i++) {
				guestBookTable.getItems().add(guestBookList.get(i));
			}
			window.setScene(guestScreen);	
		});
		
		Button btnAdminLogin = new Button("Admin"); //sends you to admin login screen
		btnAdminLogin.setOnAction(e -> window.setScene(adminLogin));
		
		Button btnQuit = new Button("Quit"); //Saves masterFile and then quits
		btnQuit.setStyle("-fx-base:red");
		
		//LOOK INTO having this save function happen whenever you shutdown the program, regardless of using quit button or not
		btnQuit.setOnAction(e -> {
			writeObjectToFile(myLibrary,"masterFile.bin");
			System.exit(0);
		});
		
		
		homeScreenOptions.getChildren().addAll(btnLogin,btnRegisterNewUser, btnGuestLogin, btnAdminLogin,btnQuit);
		layout1.setBottom(homeScreenOptions);
		
		BorderPane.setAlignment(homeScreenOptions, Pos.BASELINE_CENTER);
		startScreen = new Scene(layout1,900,600);

		////////////////////////////////////////////////////////////////////////
		
		
		
		
		////LOGIN SCREEN - type into the the parameters to check for username and password
		BorderPane layout2 = new BorderPane(); //The top portion of the screen, split in two, with bottom bar for buttons
		VBox subLayout2 = new VBox(10); //contains the buttons and textfields for logging in
		
	
		Image loginBG = new Image("file:genericBGDiamond.png");
		BackgroundSize loginScreenImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background loginScreenBG = new Background(new BackgroundImage(loginBG, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, loginScreenImageBGS));
		layout2.setBackground(loginScreenBG);
		
		
		Image lockAndKey = new Image("file:lockAndKey.png");
		ImageView viewLockAndKey = new ImageView(lockAndKey);
		Image loginImage = new Image("file:LoginText.png");
		ImageView viewLoginImage = new ImageView(loginImage);
		layout2.setLeft(viewLockAndKey);
		
		Label lblIgnoreSpace = new Label("Note: Username and password ignores spaces");
		Button btnLoginUser = new Button("Login");
		TextField tfUserName = new TextField("User Name");
		TextField tfPassword = new TextField("Password");
		Label loginScreenFeedback = new Label();
		
		Label lblWelcomeUser = new Label(); //this is used if user advances to their personal screen
		MyInt storedUserInt = new MyInt(); //used to properly grab values for settings screen and user library
		
			//HOLD All User Settings Labels Here, so we can Up
			//Button/Label creation
			Label lblUSERNAMESTATIC = new Label("Change Information");
			Label lblChangePassword = new Label("PASSWORD: ");
			TextField tfChangePassword = new TextField();
			Label lblChangeFirstName = new Label("FIRST NAME: ");
			TextField tfChangeFirstName = new TextField();
			Label lblChangeLastName = new Label("LAST NAME: ");
			TextField tfChangeLastName = new TextField();
			Label lblChangeAge = new Label("AGE: ");
			TextField tfChangeAge = new TextField();
			Button btnSaveChanges = new Button("SAVE CHANGES");
			BorderPane.setAlignment(btnSaveChanges, Pos.BASELINE_CENTER);
			
			//Button/Label Creation for address
			Label lblChangeAddress = new Label("Address");
			Label lblChangeStreet = new Label("STREET: ");
			TextField tfChangeStreet = new TextField();
			Label lblChangeTown = new Label("TOWN: ");
			TextField tfChangeTown = new TextField();
			Label lblChangeState = new Label("STATE: ");
			TextField tfChangeState = new TextField();
			Label lblChangeZip = new Label("ZIP CODE: ");
			TextField tfChangeZip = new TextField();
			Button btnDeleteAccount = new Button("DELETE ACCOUNT");
			
			TableView myRentedBooks = new TableView(); //the books the user has rented
			ArrayList<Book> myBookList = new ArrayList<>(); //list for the table to use
			TableView libraryBooksTable = new TableView(); //the books available for rent in the library
		
		btnLoginUser.setOnAction(e -> { //This is the code to check if a user exists, and then if the password lines up
			
			String userNameCheck = tfUserName.getText().replaceAll("\\s+", "");
			String userPasswordCheck = tfPassword.getText().replaceAll("\\s+", "");
			boolean userFound = false;
			boolean illegalName = false;
			
			if (userNameCheck.equals("ADMIN")) { 
				illegalName = true;
				loginScreenFeedback.setText("ADMIN CANNOT BE USED");
			}
			
			if (illegalName == false) {
				if (userNameCheck.equalsIgnoreCase("guest")) {
					guestBookList.clear(); //the same funciton as clicking on the guest button on the start screen
					guestBookTable.getItems().clear();
					for (int i = 0; i < grabBookList.size(); i++) {
						guestBookList.add(grabBookList.get(i));
					}
					for (int i = 0; i < grabBookList.size(); i++) {
						guestBookTable.getItems().add(guestBookList.get(i));
					}
					loginScreenFeedback.setText("");
					window.setScene(guestScreen);
				}
				for (int i = 0; i < grabUserList.size(); i++) {
					if (userNameCheck.equals(grabUserList.get(i).getUserName())) { //if the user is found
						userFound = true;
						if (userPasswordCheck.equals(decryptPassword(grabUserList.get(i).getPassword().replaceAll("\\s+", "")))) { //if the password is correct EXPAND ON THIS LATER
							loginScreenFeedback.setText("");
							tfPassword.setText("");
							lblWelcomeUser.setText(tfUserName.getText().replaceAll("\\s+", ""));
							storedUserInt.setInt(i);
							
							guestBookList.clear(); //updates library information when entering user screen
							myBookList.clear(); //updates user booklist
							myRentedBooks.getItems().clear(); //updates library information when entering user screen
							libraryBooksTable.getItems().clear(); //updates user booklist
							
							for (int k = 0; k < grabBookList.size(); k++) {
								guestBookList.add(grabBookList.get(k));
							}
							for (int k = 0; k < grabBookList.size(); k++) {
								libraryBooksTable.getItems().add(guestBookList.get(k));
							}
							for (int k = 0; k < grabUserList.get(storedUserInt.getInt()).getRentedBooks().size(); k++) {
								myBookList.add(grabUserList.get(storedUserInt.getInt()).getRentedBooks().get(k));
							}
							for (int k = 0; k < myBookList.size(); k++) {
								myRentedBooks.getItems().add(myBookList.get(k));
							}
							
							window.setScene(userPortal);
							break;
						}
						else { //if the password is incorrect
							loginScreenFeedback.setText("USERNAME/PASSWORD MISMATCH");
							break;
						}
					}
				}
				if (userFound == false) {
					loginScreenFeedback.setText("USER NOT FOUND");
				}
			}
		});
		
		
		subLayout2.getChildren().addAll(viewLoginImage,lblIgnoreSpace,tfUserName,tfPassword,btnLoginUser,loginScreenFeedback);
		subLayout2.setAlignment(Pos.CENTER);
		layout2.setCenter(subLayout2);
		
		
		Button btnHome = new Button("Home Screen");
		btnHome.setOnAction(e -> window.setScene(startScreen));
		layout2.setBottom(btnHome);
		
		BorderPane.setMargin(subLayout2, new Insets(10,20,20,10));
		BorderPane.setAlignment(btnHome, Pos.BASELINE_CENTER);
		BorderPane.setAlignment(viewLockAndKey, Pos.CENTER);
		
		loginScreen = new Scene(layout2,900,600);
		
		//////////////////////////////////////
		
		////USER PORTAL - STEMS FROM USER LOGIN
		//here the user will be able to check out books, or go to another screen to adjust thier information, or delete themselves
		
		BorderPane layout8 = new BorderPane();
		VBox subLayout8 = new VBox(10);
		layout8.setPadding(new Insets(10,10,10,10));
		HBox bar8 = new HBox(10); //this is for the welcome message and home buttons
		//tableview for books for both the user books and the master library, these will be left and center respectively
		Button btnUserSettingsMenu = new Button("User Settings");
		HBox libraryButtons = new HBox(10);
		
		//Background
		Image loginPortalBG = new Image("file:genericBGDiamond.png");
		BackgroundSize loginPortalScreenImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background loginPortalScreenBG = new Background(new BackgroundImage(loginPortalBG, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, loginPortalScreenImageBGS));
		layout8.setBackground(loginPortalScreenBG);
		
		//USER'S RENTED BOOKS
		myRentedBooks.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //makes the tableview only have the 
		
		TableColumn<String, Book> rentedTitle = new TableColumn<>("Your Books");
		rentedTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		rentedTitle.setMinWidth(300);
		
		TableColumn<String, Book> rentedAuthor = new TableColumn<>("Author");
		rentedAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
		rentedAuthor.setMinWidth(190);
		
		TableColumn<String, Book> rentedISBN = new TableColumn<>("ISBN");
		rentedISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		rentedISBN.setMinWidth(100);
		
		TableColumn<String, Book> rentedBookID = new TableColumn("BookID");
		rentedBookID.setCellValueFactory(new PropertyValueFactory<>("bookid"));
		rentedBookID.setMinWidth(100);
		
		TableColumn<String, Book> rentedCategory = new TableColumn("Category");
		rentedCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
		rentedCategory.setMinWidth(90);
		
		TableColumn<Boolean, Book> rentedDue = new TableColumn("DUE");
		rentedDue.setCellValueFactory(new PropertyValueFactory<>("due"));
		rentedDue.setMinWidth(50);
		
		myRentedBooks.getColumns().addAll(rentedTitle,rentedAuthor,rentedISBN,rentedBookID,rentedCategory,rentedDue);
		
		
		
		//LIBRARY'S BOOKS
		libraryBooksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //makes the tableview only have the 
		
		TableColumn<String, Book> libraryTitle = new TableColumn<>("Library's Books");
		libraryTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		libraryTitle.setMinWidth(300);
		
		TableColumn<String, Book> libraryAuthor = new TableColumn<>("Author");
		libraryAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
		libraryAuthor.setMinWidth(190);
		
		TableColumn<String, Book> libraryISBN = new TableColumn<>("ISBN");
		libraryISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		libraryISBN.setMinWidth(100);
		
		TableColumn<String, Book> libraryBookID = new TableColumn("BookID");
		libraryBookID.setCellValueFactory(new PropertyValueFactory<>("bookid"));
		libraryBookID.setMinWidth(100);
		
		TableColumn<String, Book> libraryCategory = new TableColumn("Category");
		libraryCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
		libraryCategory.setMinWidth(90);
		
		TableColumn<String, Book> libraryStatus = new TableColumn("Status");
		libraryStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		libraryStatus.setMinWidth(50);
		
		libraryBooksTable.getColumns().addAll(libraryTitle,libraryAuthor,libraryISBN,libraryBookID,libraryCategory,libraryStatus);

		ChoiceBox<String> librarySearchType = new ChoiceBox<>(); //creates a drop down menu for search categories
		TextField tfLibrarySearchBar = new TextField("Search..."); //search bar
		librarySearchType.getItems().addAll("Title","Author","Category","ISBN","Refresh");
		librarySearchType.setValue("Title"); //sets default search type
		Button btnLibrarySearchConfirm = new Button("Search");
		Button btnRent = new Button("Rent Book");
		btnRent.setOnAction(e -> { //user rents the book
			try {
				
				Book rented =  (Book) libraryBooksTable.getSelectionModel().getSelectedItem();
				if (rented.getStatus().equals("in")) {	
					
					grabUserList.get(storedUserInt.getInt()).rentBook(rented); //lets the user rent the book
			
					for (int i = 0; i < grabBookList.size(); i++) { //cycles through all books
						if (grabBookList.get(i).getIsbn().equals(rented.getIsbn())) { //checks if isbn matches choice
							if (grabBookList.get(i).getBookid().equals(rented.getBookid())) { //checks if bookid matches choice
								grabBookList.get(i).setStatus("out"); //changes status of book to out
								grabBookList.get(i).setDate(currentDatePlusSeven);
								grabBookList.get(i).setDue(false);
							}
						}
					}
					myBookList.clear(); //refreshes all the lists/tables
					guestBookList.clear();
					libraryBooksTable.getItems().clear();
					myRentedBooks.getItems().clear();
					
					for (int k = 0; k < grabUserList.get(storedUserInt.getInt()).getRentedBooks().size(); k++) { 
						myBookList.add(grabUserList.get(storedUserInt.getInt()).getRentedBooks().get(k));
					}
					for (int k = 0; k < myBookList.size(); k++) {
						myRentedBooks.getItems().add(myBookList.get(k));
					}
					for (int k = 0; k < grabBookList.size(); k++) {
						guestBookList.add(grabBookList.get(k));
					}
					for (int k = 0; k < grabBookList.size(); k++) {
						libraryBooksTable.getItems().add(guestBookList.get(k));
					}
					
				}
			}
			catch (Exception error) { }
		});
		
		Button btnReturn = new Button("Return Book");
		btnReturn.setOnAction(e -> { //user returns the book
			try {
				Book returned = (Book) myRentedBooks.getSelectionModel().getSelectedItem();	
				for (int i = 0; i < grabBookList.size(); i++) { //cycles through all books
					if (grabBookList.get(i).getIsbn().equals(returned.getIsbn())) { //checks if isbn matches choice
						if (grabBookList.get(i).getBookid().equals(returned.getBookid())) { //checks if bookid matches choice
							grabBookList.get(i).setStatus("in"); //changes status of book to in
							grabBookList.get(i).setDue(false);
							grabUserList.get(storedUserInt.getInt()).returnBook(returned);
						}
					}
				}
				myBookList.clear(); //refreshes all the lists/tables
				guestBookList.clear();
				libraryBooksTable.getItems().clear();
				myRentedBooks.getItems().clear();
				
				for (int k = 0; k < grabUserList.get(storedUserInt.getInt()).getRentedBooks().size(); k++) { 
					myBookList.add(grabUserList.get(storedUserInt.getInt()).getRentedBooks().get(k));
				}
				for (int k = 0; k < myBookList.size(); k++) {
					myRentedBooks.getItems().add(myBookList.get(k));
				}
				for (int k = 0; k < grabBookList.size(); k++) {
					guestBookList.add(grabBookList.get(k));
				}
				for (int k = 0; k < grabBookList.size(); k++) {
					libraryBooksTable.getItems().add(guestBookList.get(k));
				}
			}
			catch (Exception error ) {}
		});
		
		
		libraryButtons.getChildren().addAll(librarySearchType,tfLibrarySearchBar,btnLibrarySearchConfirm,btnRent, btnReturn);
		
		btnLibrarySearchConfirm.setOnAction(e -> {
			String guestChoice = librarySearchType.getValue();
			guestBookList.clear();
			libraryBooksTable.getItems().clear();
			for (int i = 0; i < grabBookList.size(); i++) {
				if (guestChoice.equals("Title")) { //searches by title
					if (tfLibrarySearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getTitle())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Author")) { //searches by author
					if (tfLibrarySearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getAuthor())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("ISBN")) { //searches by isbn
					if (tfLibrarySearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getIsbn())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Category")) { //searches by genre
					if (tfLibrarySearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getCategory())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Refresh")) {
					guestBookList.add(grabBookList.get(i));
				}
			}
			for (int i = 0; i < guestBookList.size(); i++) { //adds new searched books to the new table view
				libraryBooksTable.getItems().add(guestBookList.get(i));
			}
		});
		
		//guestSearchBarTools.getChildren().addAll(guestSearchType,tfGuestSearchBar,btnGuestSearchConfirm);
		
		layout8.setPadding(new Insets(10,10,10,10));
		BorderPane.setAlignment(myRentedBooks, Pos.CENTER);
		subLayout8.getChildren().addAll(myRentedBooks,libraryBooksTable);
		layout8.setCenter(subLayout8);
		
		
		btnUserSettingsMenu.setOnAction(e -> {
			
			//Update all settings menu labels
			tfChangePassword.setText(decryptPassword(grabUserList.get(storedUserInt.getInt()).getPassword().replaceAll("\\s+", "")));
			tfChangeFirstName.setText(grabUserList.get(storedUserInt.getInt()).getFirstName());
			tfChangeLastName.setText(grabUserList.get(storedUserInt.getInt()).getLastName());
			tfChangeAge.setText(grabUserList.get(storedUserInt.getInt()).getAge());
			
			tfChangeStreet.setText(grabUserList.get(storedUserInt.getInt()).getAddress().getStreet());
			tfChangeTown.setText(grabUserList.get(storedUserInt.getInt()).getAddress().getTown());
			tfChangeState.setText(grabUserList.get(storedUserInt.getInt()).getAddress().getState());
			
			tfChangeZip.setText(grabUserList.get(storedUserInt.getInt()).getAddress().getZipCode());
			window.setScene(userSettings);
		});
		
		
		
		
		btnUserSettingsMenu.setAlignment(Pos.CENTER_LEFT);
		
		Label lblWelcome = new Label("Welcome ");
		lblWelcome.setStyle("-fx-color: white");
		
		Button btnHome8 = new Button("Home Screen/Log Out");
		btnHome8.setOnAction(e -> window.setScene(startScreen));
		bar8.getChildren().addAll(btnUserSettingsMenu,btnHome8,lblWelcome,lblWelcomeUser);
		layout8.setTop(bar8);
		layout8.setBottom(libraryButtons);
		BorderPane.setAlignment(libraryBooksTable, Pos.BASELINE_CENTER);
		bar8.setAlignment(Pos.TOP_LEFT);
		
		
		userPortal = new Scene(layout8,900,600);
		
		
		///////////////////////////////////
		
		////USER SETTINGS - STEMS FROM USER PORTAL
		//lets user either change information or delete account
		
		///Create a method to grab the user from the userList, we need to fill in the textboxes by default with the pre-existing information
		
		
		BorderPane layout9 = new BorderPane();
		Image infoChangeImage = new Image("file:infoChangeBackGround.png");
		BackgroundSize infoChangeBackgroundSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background infoChangeBackground = new Background(new BackgroundImage(infoChangeImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,infoChangeBackgroundSize));
		layout9.setBackground(infoChangeBackground);
		
		
		
		layout9.setPadding(new Insets(10,10,10,10));
		GridPane subLayout9 = new GridPane();
		subLayout9.setAlignment(Pos.CENTER);
		subLayout9.setHgap(10);
		subLayout9.setVgap(5);
		GridPane subLayout9_9 = new GridPane();
		subLayout9_9.setAlignment(Pos.CENTER);
		subLayout9_9.setHgap(10);
		subLayout9_9.setVgap(5);
		HBox subLayout9_9_9 = new HBox(100);
		subLayout9_9_9.setAlignment(Pos.CENTER);
		Label settingsSavedFeedback = new Label();
		Button btnUserScreen = new Button("User Screen");
		btnUserScreen.setOnAction(e -> {
			settingsSavedFeedback.setText("");
			window.setScene(userPortal);
		});
		layout9.setBottom(btnUserScreen);
		BorderPane.setAlignment(btnUserScreen, Pos.BASELINE_CENTER);
		
		
		
	
		///Button/Label positions
		GridPane.setConstraints(lblUSERNAMESTATIC,0,1);
		GridPane.setConstraints(lblChangePassword,0,2);
		GridPane.setConstraints(tfChangePassword,1,2);
		GridPane.setConstraints(lblChangeFirstName,0,3);
		GridPane.setConstraints(tfChangeFirstName,1,3);
		GridPane.setConstraints(lblChangeLastName,0,4);
		GridPane.setConstraints(tfChangeLastName,1,4);
		GridPane.setConstraints(lblChangeAge,0,5);
		GridPane.setConstraints(tfChangeAge,1,5);
		GridPane.setConstraints(btnSaveChanges,0,6);
		GridPane.setConstraints(settingsSavedFeedback,1,6);
	
		subLayout9.getChildren().addAll(lblUSERNAMESTATIC,lblChangePassword,tfChangePassword,lblChangeFirstName,tfChangeFirstName,lblChangeLastName,tfChangeLastName,lblChangeAge,tfChangeAge,btnSaveChanges,settingsSavedFeedback);
		
		//Button/Label Positions for Address
		GridPane.setConstraints(lblChangeAddress,0,1);
		GridPane.setConstraints(lblChangeStreet,0,2);
		GridPane.setConstraints(tfChangeStreet,1,2);
		GridPane.setConstraints(lblChangeTown,0,3);
		GridPane.setConstraints(tfChangeTown,1,3);
		GridPane.setConstraints(lblChangeState,0,4);
		GridPane.setConstraints(tfChangeState,1,4);
		GridPane.setConstraints(lblChangeZip,0,5);
		GridPane.setConstraints(tfChangeZip,1,5);
		GridPane.setConstraints(btnDeleteAccount,0,6);
		
		btnDeleteAccount.setOnAction(e -> window.setScene(userDeletionRequest));
		
		
		btnSaveChanges.setOnAction(e -> {
			if ((!tfChangePassword.getText().equals("")) && (!tfChangeFirstName.getText().equals(""))   && (!tfChangeLastName.getText().equals("")) && (!tfChangeAge.getText().equals("")) && (!tfChangeStreet.getText().equals("")) && (!tfChangeTown.getText().equals("")) && (!tfChangeState.getText().equals("")) && (!tfChangeZip.getText().equals(""))   ) {
				grabUserList.get(storedUserInt.getInt()).setPassword(encryptPassword(tfChangePassword.getText().replaceAll("\\s+", "")));
				grabUserList.get(storedUserInt.getInt()).setFirstName(tfChangeFirstName.getText());
				grabUserList.get(storedUserInt.getInt()).setLastName(tfChangeLastName.getText());
				grabUserList.get(storedUserInt.getInt()).setAge(tfChangeAge.getText());
				
				grabUserList.get(storedUserInt.getInt()).getAddress().setStreet(tfChangeStreet.getText());
				grabUserList.get(storedUserInt.getInt()).getAddress().setTown(tfChangeTown.getText());
				grabUserList.get(storedUserInt.getInt()).getAddress().setState(tfChangeState.getText());
				grabUserList.get(storedUserInt.getInt()).getAddress().setZipCode(tfChangeZip.getText());
				
				settingsSavedFeedback.setText("SAVED CHANGES");
			
			}
			else {
				settingsSavedFeedback.setText("FILL ALL SPACES");
			}
			
		});
		
		subLayout9_9.getChildren().addAll(lblChangeAddress,lblChangeStreet,tfChangeStreet,lblChangeTown,tfChangeTown,lblChangeState,tfChangeState,lblChangeZip,tfChangeZip,btnDeleteAccount);
		subLayout9_9_9.getChildren().addAll(subLayout9,subLayout9_9);
	
		layout9.setTop(subLayout9_9_9);
		
		
		userSettings = new Scene(layout9,900,600);
		
		//////////////////////////////////////////
		
		///USER SELF DELETION REQUEST
		BorderPane layout10 = new BorderPane();
		Image userSelfDeleteScreen = new Image("file:userSelfDeletionScreen.png");
		BackgroundSize userSelfDeleteScreenBGSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background userSelfDeleteBG = new Background(new BackgroundImage(userSelfDeleteScreen, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,userSelfDeleteScreenBGSize));
		layout10.setBackground(userSelfDeleteBG);
		
		
		
		Button btnUserConfirmDelete = new Button("DELETE ACCOUNT");
		btnUserConfirmDelete.setStyle("-fx-base: red;");
		Button btnUserGoBackToSettingsScreen = new Button("NEVERMIND");
		layout10.setCenter(btnUserConfirmDelete);
		BorderPane.setAlignment(btnUserConfirmDelete, Pos.CENTER);
		layout10.setBottom(btnUserGoBackToSettingsScreen);
		BorderPane.setAlignment(btnUserGoBackToSettingsScreen, Pos.BASELINE_CENTER);
		
		btnUserGoBackToSettingsScreen.setOnAction(e -> window.setScene(userSettings));
		
		
		btnUserConfirmDelete.setOnAction(e -> { 
			String userToDelete = lblWelcomeUser.getText(); //grabs account username from user portal
			
			//RETURN YOUR BOOKS
			ArrayList<Book> returnThese = myLibrary.grabUserByUserName(userToDelete).getRentedBooks(); //grab all books
			for (int i = 0; i < returnThese.size(); i++) {
				for (int k = 0; k < grabBookList.size(); k++) {
					if (grabBookList.get(k).getIsbn().equals(returnThese.get(i).getIsbn())) {
						if (grabBookList.get(k).getBookid().equals(returnThese.get(i).getBookid())) {
							grabBookList.get(k).setStatus("in"); //returns all the books found on the user
						}
					}
				}
			}
			
			myLibrary.deleteUser(userToDelete); //use the string we obtained to delete the user in our actual userList
			grabUserList = myLibrary.getUserList(); //Update the userLsit to reflect this change
			window.setScene(startScreen); //automatically kicks you to the start screen
		});
		
		userDeletionRequest = new Scene(layout10,900,600);
		
		/////////////////////////////////////////////////////////////////////////////
		
		////REGISTER SCREEN - register a new username and password here
		BorderPane layout3 = new BorderPane(); //same setup as the login screen
		VBox subLayout3 = new VBox(10); //contains textfields and buttons for logging in
		
		//Background
		Image registerBG = new Image("file:genericBGDiamond.png");
		BackgroundSize registerScreenImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background registerScreenBG = new Background(new BackgroundImage(registerBG, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, registerScreenImageBGS));
		layout3.setBackground(registerScreenBG);
		
		
		Image registerTextImage = new Image("file:RegisterText.png");
		ImageView viewRegisterTextImage = new ImageView(registerTextImage);
		
		Label lblIgnoreSpace2 = new Label("Note: Username and password ignores spaces");
		TextField tfRegisterUser = new TextField("Enter New Username");
		Button btnRegisterUser = new Button("Register Account");
		Label registerScreenFeedBack = new Label();
		
		Label lblCreateUserName = new Label();
		
		btnRegisterUser.setOnAction(e -> {
			
			String registerNameCheck = tfRegisterUser.getText().replaceAll("\\s+", "");
			boolean userFound = false;
			boolean illegalName = false;
			
			if ((registerNameCheck.equals("ADMIN")) || (registerNameCheck.equals("")) || (registerNameCheck.equals(null)) ) {
				illegalName = true;
				registerScreenFeedBack.setText("NAME CANNOT BE USED");
			}
			if (registerNameCheck.equalsIgnoreCase("guest")) {
				illegalName = true;
				registerScreenFeedBack.setText("NAME CANNOT BE USED");
			}
			
			if (illegalName == false) {
				for (int i = 0; i < grabUserList.size(); i++) {
					if (registerNameCheck.equals(grabUserList.get(i).getUserName())) { //if the user is found
						registerScreenFeedBack.setText("USER NAME TAKEN");
						userFound = true;
						break;
					}
				}
				if (userFound == false) {
					registerScreenFeedBack.setText("");
					lblCreateUserName.setText(registerNameCheck);
					window.setScene(userCreation);
				}
			}
		});
		
		
		
		
		subLayout3.getChildren().addAll(viewRegisterTextImage,lblIgnoreSpace2,tfRegisterUser,btnRegisterUser,registerScreenFeedBack);
		layout3.setCenter(subLayout3);
		
		Button btnHome2 = new Button("Home Screen");
		btnHome2.setOnAction(e -> window.setScene(startScreen)); //sends home 
		layout3.setBottom(btnHome2);
		
		BorderPane.setMargin(subLayout3, new Insets(10,20,20,10));
		BorderPane.setAlignment(btnHome2, Pos.BASELINE_CENTER);
		
		registerScreen = new Scene(layout3,900,600);
		
		///////////////////////////////
		
		//////USER CREATION - STEMS FROM REGISTERSCREEN
		
		//Allow the user to create data for 
				//firstName,lastName,age,password
				//username is already decided from registration window
				//userAddress(created in a seperate class first) -- street,town,state,zipCode
		
		
		
		
		
		BorderPane layout6 = new BorderPane();
		VBox subLayout6_6_6_6 = new VBox(10);
		Button btnCancelRegistration = new Button("CANCEL");
		btnCancelRegistration.setOnAction(e -> window.setScene(registerScreen));
		layout6.setPadding(new Insets(10,10,10,10));
		GridPane subLayout6 = new GridPane();
		subLayout6.setAlignment(Pos.CENTER);
		subLayout6.setHgap(10);
		subLayout6.setVgap(5);
		GridPane subLayout6_6 = new GridPane();
		subLayout6_6.setAlignment(Pos.CENTER);
		subLayout6_6.setHgap(10);
		subLayout6_6.setVgap(5);
		HBox subLayout6_6_6 = new HBox(100);
		subLayout6_6_6.setAlignment(Pos.CENTER);
		
		//Background
		Image registerCreationBG = new Image("file:genericBGDiamond.png");
		BackgroundSize registerCreationScreenImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background registerCreationScreenBG = new Background(new BackgroundImage(registerCreationBG, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, registerCreationScreenImageBGS));
		layout6.setBackground(registerCreationScreenBG);
		
		
		//Button/Label creation
		Label lblUSERNAME = new Label("USERNAME: ");
			//lblCreateUserName was created in previous window to carry the data across
		Label lblPASSWORD = new Label("PASSWORD: ");
		TextField tfCreatePassword = new TextField();
		Label lblFIRSTNAME = new Label("FIRST NAME: ");
		TextField tfCreateFirstName = new TextField();
		Label lblLASTNAME = new Label("LAST NAME: ");
		TextField tfCreateLastName = new TextField();
		Label lblAGE = new Label("AGE: ");
		TextField tfCreateAge = new TextField();
		Button btnCREATEUSER = new Button("CREATE USER");
		Label lblRegisterScreenFeedBack = new Label();
		subLayout6_6_6_6.getChildren().addAll(lblRegisterScreenFeedBack,btnCREATEUSER,btnCancelRegistration);
		subLayout6_6_6_6.setAlignment(Pos.CENTER);
		layout6.setBottom(subLayout6_6_6_6);
		BorderPane.setAlignment(subLayout6_6_6_6, Pos.BASELINE_CENTER);
		
		
		///Button/Label positions
		GridPane.setConstraints(lblUSERNAME,0,1);
		GridPane.setConstraints(lblCreateUserName,1,1);
		GridPane.setConstraints(lblPASSWORD,0,2);
		GridPane.setConstraints(tfCreatePassword,1,2);
		GridPane.setConstraints(lblFIRSTNAME,0,3);
		GridPane.setConstraints(tfCreateFirstName,1,3);
		GridPane.setConstraints(lblLASTNAME,0,4);
		GridPane.setConstraints(tfCreateLastName,1,4);
		GridPane.setConstraints(lblAGE,0,5);
		GridPane.setConstraints(tfCreateAge,1,5);
		GridPane.setConstraints(btnCREATEUSER,0,6);
	
		subLayout6.getChildren().addAll(lblUSERNAME,lblCreateUserName,lblPASSWORD,tfCreatePassword,lblFIRSTNAME,tfCreateFirstName,lblLASTNAME,tfCreateLastName,lblAGE,tfCreateAge);
		
		
		//Button/Label Creation for address
		Label lblEnterAddress = new Label("Address");
		Label lblSTREET = new Label("STREET: ");
		TextField tfCreateStreet = new TextField();
		Label lblTOWN = new Label("TOWN: ");
		TextField tfCreateTown = new TextField();
		Label lblSTATE = new Label("STATE: ");
		TextField tfCreateState = new TextField();
		Label lblZIPCODE = new Label("ZIP CODE: ");
		TextField tfCreateZipCode = new TextField();
		
		//Button/Label Positions for Address
		GridPane.setConstraints(lblEnterAddress,0,1);
		GridPane.setConstraints(lblSTREET,0,2);
		GridPane.setConstraints(tfCreateStreet,1,2);
		GridPane.setConstraints(lblTOWN,0,3);
		GridPane.setConstraints(tfCreateTown,1,3);
		GridPane.setConstraints(lblSTATE,0,4);
		GridPane.setConstraints(tfCreateState,1,4);
		GridPane.setConstraints(lblZIPCODE,0,5);
		GridPane.setConstraints(tfCreateZipCode,1,5);
		
		
		btnCREATEUSER.setOnAction(e -> {
			if ((!tfCreatePassword.getText().equals("")) && (!tfCreateFirstName.getText().equals(""))   && (!tfCreateLastName.getText().equals("")) && (!tfCreateAge.getText().equals("")) && (!tfCreateStreet.getText().equals("")) && (!tfCreateTown.getText().equals("")) && (!tfCreateState.getText().equals("")) && (!tfCreateZipCode.getText().equals(""))   ) {
				User newUser = new User(lblCreateUserName.getText());
				newUser.setPassword(encryptPassword(tfCreatePassword.getText().replaceAll("\\s+", "")));
				newUser.setFirstName(tfCreateFirstName.getText());
				newUser.setLastName(tfCreateLastName.getText());
				newUser.setAge(tfCreateAge.getText());
				userAddress newAddress = new userAddress(tfCreateStreet.getText(),tfCreateTown.getText(),tfCreateState.getText(),tfCreateZipCode.getText());
				newUser.setAddress(newAddress);
				
				lblRegisterScreenFeedBack.setText("");
				myLibrary.registerUser(newUser);
				
				//Clears all textFields
				tfCreateStreet.setText("");
				tfCreateTown.setText("");
				tfCreateState.setText("");
				tfCreateZipCode.setText("");
				tfCreatePassword.setText("");
				tfCreateFirstName.setText("");
				tfCreateLastName.setText("");
				tfCreateAge.setText("");
				
				window.setScene(startScreen);
			}
			else {
				lblRegisterScreenFeedBack.setText("FILL ALL SPACES");
			}
			
		});
		
		subLayout6_6.getChildren().addAll(lblEnterAddress,lblSTREET,tfCreateStreet,lblTOWN,tfCreateTown,lblSTATE,tfCreateState,lblZIPCODE,tfCreateZipCode);
		subLayout6_6_6.getChildren().addAll(subLayout6,subLayout6_6);
		
		layout6.setTop(subLayout6_6_6);
		
		
		
		userCreation = new Scene(layout6,900,600);
		
		////////////////////////////////////////////////////////////////////////////////
		
		////GUEST SCREEN - Display book layout except without any of the renting buttons
		BorderPane layout5 = new BorderPane();
		VBox subLayout5 = new VBox(10);
		HBox guestSearchBarTools = new HBox(10);
		
		//Background
		Image guestScreenImage = new Image("file:guestBrowseScreen.png");
		BackgroundSize guestScreenImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background guestScreenBG = new Background(new BackgroundImage(guestScreenImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,guestScreenImageBGS));
		layout5.setBackground(guestScreenBG);
		
		
		//Make sure to copy this very carefully for any other table view we use in this project
	
		//The TableView instance is created w/ the main screen components so we can properly update the list every time you choose to view it, in case any changes are made via the admin or a user
		guestBookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //makes the tableview only have the 
		
		TableColumn<String, Book> guestTitle = new TableColumn<>("Title");
		guestTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		guestTitle.setMinWidth(300);
		
		TableColumn<String, Book> guestAuthor = new TableColumn<>("Author");
		guestAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
		guestAuthor.setMinWidth(190);
		
		TableColumn<String, Book> guestISBN = new TableColumn<>("ISBN");
		guestISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		guestISBN.setMinWidth(100);
		
		TableColumn<String, Book> guestBookID = new TableColumn("BookID");
		guestBookID.setCellValueFactory(new PropertyValueFactory<>("bookid"));
		guestBookID.setMinWidth(100);
		
		TableColumn<String, Book> guestCategory = new TableColumn("Category");
		guestCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
		guestCategory.setMinWidth(90);
		
		TableColumn<String, Book> guestStatus = new TableColumn("Status");
		guestStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		guestStatus.setMinWidth(50);
		
		guestBookTable.getColumns().addAll(guestTitle,guestAuthor,guestISBN,guestBookID,guestCategory,guestStatus);
		
		ChoiceBox<String> guestSearchType = new ChoiceBox<>(); //creates a drop down menu for search categories
		TextField tfGuestSearchBar = new TextField("Search..."); //search bar
		guestSearchType.getItems().addAll("Title","Author","Category","ISBN","Refresh");
		guestSearchType.setValue("Title"); //sets default search type
		Button btnGuestSearchConfirm = new Button("Search");
		
		btnGuestSearchConfirm.setOnAction(e -> {
			String guestChoice = guestSearchType.getValue();
			guestBookList.clear();
			guestBookTable.getItems().clear();
			for (int i = 0; i < grabBookList.size(); i++) {
				if (guestChoice.equals("Title")) { //searches by title
					if (tfGuestSearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getTitle())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Author")) { //searches by author
					if (tfGuestSearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getAuthor())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("ISBN")) { //searches by isbn
					if (tfGuestSearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getIsbn())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Category")) { //searches by genre
					if (tfGuestSearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getCategory())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Refresh")) {
					guestBookList.add(grabBookList.get(i));
				}
			}
			for (int i = 0; i < guestBookList.size(); i++) { //adds new searched books to the new table view
				guestBookTable.getItems().add(guestBookList.get(i));
			}
		});
		
		guestSearchBarTools.getChildren().addAll(guestSearchType,tfGuestSearchBar,btnGuestSearchConfirm);
		
		layout5.setPadding(new Insets(10,10,10,10));
		BorderPane.setAlignment(guestBookTable, Pos.CENTER);
		subLayout5.getChildren().addAll(guestBookTable,guestSearchBarTools);
		layout5.setTop(subLayout5);
		
		Button btnHome4 = new Button("Home Screen");
		btnHome4.setOnAction(e -> {
			tfGuestSearchBar.setText("Search...");
			guestSearchType.setValue("Title");
			window.setScene(startScreen);
		});
		
		BorderPane.setAlignment(btnHome4, Pos.BASELINE_CENTER);
		layout5.setBottom(btnHome4);
		
		guestScreen = new Scene(layout5,900,600);
		
		///////////////////////////////////////////////////////////////////////////////
		
		//use ListView to list items in javafx
		//labels can be updated live with info
		//info can be taken from other scenes, it is not limited by persistant objects/rooms like GML
		
		////ADMIN SCREEN - log in as admin
		BorderPane layout4 = new BorderPane();
		VBox subLayout4 = new VBox(10);
		
		//Background
		Image adminBG = new Image("file:genericBGDiamond.png");
		BackgroundSize adminScreenImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background adminScreenBG = new Background(new BackgroundImage(adminBG, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, adminScreenImageBGS));
		layout4.setBackground(adminScreenBG);
		
		Image adminTextImage = new Image("file:AdminText.png");
		ImageView viewAdminTextImage = new ImageView(adminTextImage);
		
		Label lblIgnoreSpace3 = new Label("Note: Username and password ignores spaces");
		TextField tfAdminUserName = new TextField("Admin Account");
		TextField tfAdminPassword = new TextField("Admin Passoword");
		Button btnAdminLoginConfirm = new Button("Admin Login");
		Label adminScreenFeedback = new Label();
		
		ListView adminUserList = new ListView(); //accessed in the Admin Portal, placed here to update on button press
		btnAdminLoginConfirm.setOnAction(e -> {
			if ((tfAdminUserName.getText().equals("ADMIN")) && (tfAdminPassword.getText().equals("ADMINPASSWORD")) ) {
				adminUserList.getItems().clear();
				for (int i = 0; i < grabUserList.size(); i++) {
					adminUserList.getItems().add(grabUserList.get(i).getUserName());
				}
				tfAdminPassword.setText("");
				window.setScene(adminPortal);
			}
			else {
				adminScreenFeedback.setText("ADMIN NAME/PASSWORD MISSMATCH");
			}
		});
		
		subLayout4.getChildren().addAll(viewAdminTextImage,lblIgnoreSpace3,tfAdminUserName,tfAdminPassword,btnAdminLoginConfirm,adminScreenFeedback);
		layout4.setCenter(subLayout4);
		
		Button btnHome3 = new Button("Home Screen");
		btnHome3.setOnAction(e -> { 
			adminScreenFeedback.setText("");
			window.setScene(startScreen);
			
		});
		layout4.setBottom(btnHome3);
		
		BorderPane.setMargin(subLayout4, new Insets(10,20,20,10));
		BorderPane.setAlignment(btnHome3, Pos.BASELINE_CENTER);
		
		adminLogin = new Scene(layout4, 900,600);
		
		/////////////////////////////////////
		
		//ADMIN PORTAL - STEMS FROM ADMIN LOGIN SCREEM
		BorderPane layout7 = new BorderPane();
		layout7.setPadding(new Insets(10,10,10,10));
		VBox subLayout7 = new VBox(10); //will hold the userList and controls for deleting users
		VBox adminUserGrid = new VBox(10); //holds gridpane and address info of user
		HBox adminUserButtons = new HBox(10);
		HBox adminUserInfoBox = new HBox(10);
	
		//Background
		Image adminPortalBG = new Image("file:adminPortal.png");
		BackgroundSize adminPortalScreenImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background adminPortalScreenBG = new Background(new BackgroundImage(adminPortalBG, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, adminPortalScreenImageBGS));
		layout7.setBackground(adminPortalScreenBG);
		
		BorderPane.setAlignment(subLayout7, Pos.CENTER);
		layout7.setPadding(new Insets(10,10,10,10));
		
		
		/*
		
		Grab user's books, match them with the grabBookList, then display their Title's and the due conditions.
		Store the books you find from the grabBookList in a seperate arraylist so we can keep em away maybe
		
		*/
		
		
		//View User's Books
		TableView adminHighLightUserBooks = new TableView();
		ArrayList<Book> adminUserBookList = new ArrayList<>();
		
		TableColumn<String, Book> adminUserTitle = new TableColumn<>("Title");
		adminUserTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		adminUserTitle.setMinWidth(100);
		
		TableColumn<Boolean, Book> adminUserDue = new TableColumn("DUE");
		adminUserDue.setCellValueFactory(new PropertyValueFactory<>("due"));
		adminUserDue.setMinWidth(50);
		
		adminHighLightUserBooks.getColumns().addAll(adminUserTitle,adminUserDue);
		adminHighLightUserBooks.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		adminHighLightUserBooks.setMaxHeight(242);
		
		
		
		
		
		Button adminDeleteSelectedUser = new Button("Delete Highlighted User");
		adminDeleteSelectedUser.setStyle("-fx-color:red");
	
		Button adminViewUserInfo = new Button("View Info");
		GridPane adminUserInfo = new GridPane();
		
		Label adminUserName = new Label("User Name:");
		TextField adminViewUserName = new TextField();
		Label adminPassword = new Label("Password: ");
		TextField adminViewPassword = new TextField();
		Label adminFirstName = new Label("First Name: ");
		TextField adminViewFirstName = new TextField();
		Label adminLastName = new Label("Last Name: ");
		TextField adminViewLastName = new TextField();
		Label adminAge = new Label("Age: ");
		TextField adminViewAge = new TextField();
		Label adminAddress = new Label("Address: ");
		Label adminStreet = new Label("Street:");
		TextField adminViewStreet = new TextField();
		Label adminTown = new Label("Town: ");
		TextField adminViewTown = new TextField();
		Label adminState = new Label("State: ");
		TextField adminViewState = new TextField();
		Label adminZip = new Label("Zip Code:");
		TextField adminViewZip = new TextField();
		
		//TextField tfAdminViewAddress = new TextField();
		
		GridPane.setConstraints(adminUserName,0,1);
		GridPane.setConstraints(adminViewUserName,1,1);
		GridPane.setConstraints(adminPassword,0,2);
		GridPane.setConstraints(adminViewPassword,1,2);
		GridPane.setConstraints(adminFirstName,0,3);
		GridPane.setConstraints(adminViewFirstName,1,3);
		GridPane.setConstraints(adminLastName,0,4);
		GridPane.setConstraints(adminViewLastName,1,4);
		GridPane.setConstraints(adminAge,0,5);
		GridPane.setConstraints(adminViewAge,1,5);
		GridPane.setConstraints(adminAddress,0,6);
		GridPane.setConstraints(adminStreet,0,7);
		GridPane.setConstraints(adminViewStreet,1,7);
		GridPane.setConstraints(adminTown,0,8);
		GridPane.setConstraints(adminViewTown,1,8);
		GridPane.setConstraints(adminState,0,9);
		GridPane.setConstraints(adminViewState,1,9);
		GridPane.setConstraints(adminZip,0,10);
		GridPane.setConstraints(adminViewZip,1,10);
		
		adminUserInfo.getChildren().addAll(adminUserName,adminViewUserName,adminPassword,adminViewPassword,adminFirstName,adminViewFirstName,adminLastName,adminViewLastName,adminAge,adminViewAge,adminAddress,adminStreet,adminViewStreet,adminTown,adminViewTown,adminState,adminViewState,adminZip,adminViewZip);
		
		BorderPane.setAlignment(adminViewUserInfo, Pos.CENTER);
		
		//FOR THE LIBRARY MANAGER SCREEN
		TableView adminBookTable = new TableView();
		
		adminBookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //makes the tableview only have the 
		
		TableColumn<String, Book> adminTitle = new TableColumn<>("Title");
		adminTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		adminTitle.setMinWidth(300);
		
		TableColumn<String, Book> adminAuthor = new TableColumn<>("Author");
		adminAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
		adminAuthor.setMinWidth(190);
		
		TableColumn<String, Book> adminISBN = new TableColumn<>("ISBN");
		adminISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		adminISBN.setMinWidth(100);
		
		TableColumn<String, Book> adminBookID = new TableColumn("BookID");
		adminBookID.setCellValueFactory(new PropertyValueFactory<>("bookid"));
		adminBookID.setMinWidth(100);
		
		TableColumn<String, Book> adminCategory = new TableColumn("Category");
		adminCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
		adminCategory.setMinWidth(90);
		
		TableColumn<String, Book> adminStatus = new TableColumn("Status");
		adminStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		adminStatus.setMinWidth(50);
		
		adminBookTable.getColumns().addAll(adminTitle,adminAuthor,adminISBN,adminBookID,adminCategory,adminStatus);
		
		Button btnManageLibrary = new Button("Manage Library");
		BorderPane.setAlignment(btnManageLibrary, Pos.CENTER_LEFT);
		layout7.setTop(btnManageLibrary);
		btnManageLibrary.setOnAction(e -> { //refresh the admin book library
		
			guestBookList.clear();
			adminBookTable.getItems().clear();
			for (int i = 0; i < grabBookList.size(); i++) {
				guestBookList.add(grabBookList.get(i));
			}
			for (int i = 0; i < grabBookList.size(); i++) {
				adminBookTable.getItems().add(guestBookList.get(i));
			}
			
			window.setScene(adminLibrary);
			
		});
		
		adminViewUserInfo.setOnAction(e -> { //Viewing the user's books/Information
			try {
				Object objectToView = adminUserList.getSelectionModel().getSelectedItem();
				String userToViewString = (String) objectToView;
				User userToView = myLibrary.grabUserByUserName(userToViewString);
				adminViewUserName.setText(userToView.getUserName());
				adminViewPassword.setText(decryptPassword(userToView.getPassword()));
				adminViewFirstName.setText(userToView.getFirstName());
				adminViewLastName.setText(userToView.getLastName());
				adminViewAge.setText(userToView.getAge());
				adminViewStreet.setText(userToView.getAddress().getStreet());
				adminViewTown.setText(userToView.getAddress().getTown());
				adminViewState.setText(userToView.getAddress().getState());
				adminViewZip.setText(userToView.getAddress().getZipCode());
				
				ArrayList<Book> useThese = userToView.getRentedBooks();
				
				//Look at their Books
				
				adminUserBookList.clear();
				adminHighLightUserBooks.getItems().clear(); //clears out the listview for each user the admin checks
				
				for (int k = 0; k < useThese.size(); k++) { 
					adminUserBookList.add(useThese.get(k));
				}
				for (int k = 0; k < adminUserBookList.size(); k++) {
					adminHighLightUserBooks.getItems().add(adminUserBookList.get(k));
				}
				
				}
			catch (NullPointerException error) {
				//do nothing
			}
		});
		
		adminDeleteSelectedUser.setOnAction(e -> { //allows admin to delete highlighted user in adminUserList
			try {
				Object objectToDelete = adminUserList.getSelectionModel().getSelectedItem(); //grabs string object
				String userToDelete = (String) objectToDelete; //makes it work as a string 
				adminUserList.getItems().clear(); //clear the javafx list
				//RETURN THEIR BOOKS
				ArrayList<Book> returnThese = myLibrary.grabUserByUserName(userToDelete).getRentedBooks(); //grab all books
				for (int i = 0; i < returnThese.size(); i++) {
					for (int k = 0; k < grabBookList.size(); k++) {
						if (grabBookList.get(k).getIsbn().equals(returnThese.get(i).getIsbn())) {
							if (grabBookList.get(k).getBookid().equals(returnThese.get(i).getBookid())) {
								grabBookList.get(k).setStatus("in"); //returns all the books found on the user
							}
						}
					}
				}
				myLibrary.deleteUser(userToDelete); //use the string we obtained to delete the user in our actual userList
				grabUserList = myLibrary.getUserList(); //Update the userLsit to reflect this change
				adminViewUserName.setText("");
				adminViewPassword.setText("");
				adminViewFirstName.setText("");
				adminViewLastName.setText("");
				adminViewAge.setText("");
				adminViewStreet.setText("");
				adminViewTown.setText("");
				adminViewState.setText("");
				adminViewZip.setText("");
				adminHighLightUserBooks.getItems().clear();
				adminScreenFeedback.setText("");
				for (int i = 0; i < grabUserList.size(); i++) { //redisplay the javafx list, now with the user removed
					adminUserList.getItems().add(grabUserList.get(i).getUserName());
				}
			} catch (NullPointerException error) {}
			
		});
		
		adminUserButtons.getChildren().addAll(adminDeleteSelectedUser,adminViewUserInfo);
		subLayout7.getChildren().addAll(adminUserList,adminUserButtons);
		adminUserGrid.getChildren().addAll(adminUserInfo);
		adminUserInfoBox.getChildren().addAll(adminHighLightUserBooks,adminUserGrid);
		layout7.setLeft(subLayout7);
		layout7.setRight(adminUserInfoBox);
		BorderPane.setAlignment(adminUserGrid, Pos.CENTER_RIGHT);
		
		Button btnHome6 = new Button("Home Screen/Log Out");
		btnHome6.setOnAction(e -> {
			adminViewUserName.setText("");
			adminViewPassword.setText("");
			adminViewFirstName.setText("");
			adminViewLastName.setText("");
			adminViewAge.setText("");
			adminViewStreet.setText("");
			adminViewTown.setText("");
			adminViewState.setText("");
			adminViewZip.setText("");
			adminHighLightUserBooks.getItems().clear();
			adminScreenFeedback.setText("");
			window.setScene(startScreen);
		});
		layout7.setBottom(btnHome6);
		BorderPane.setAlignment(btnHome6, Pos.BASELINE_CENTER);
		
		adminPortal = new Scene(layout7,900,600);
		
		///////////////////////////////////////////
		
		//ADMIN LIBRARY - Add/Remove books
		BorderPane layout11 = new BorderPane();
		VBox subLayout11 = new VBox(10);
		HBox adminSearchBarTools = new HBox(10);
		
		//Background
		Image adminScreenImageBook = new Image("file:manageLibrary.png");
		BackgroundSize adminScreenImageBGSBook = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background adminScreenBGBook = new Background(new BackgroundImage(adminScreenImageBook, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,adminScreenImageBGSBook));
		layout11.setBackground(adminScreenBGBook);
		
		ChoiceBox<String> adminSearchType = new ChoiceBox<>(); //creates a drop down menu for search categories
		TextField tfAdminSearchBar = new TextField("Search..."); //search bar
		adminSearchType.getItems().addAll("Title","Author","Category","ISBN","Refresh");
		adminSearchType.setValue("Title"); //sets default search type
		Button btnAdminSearchConfirm = new Button("Search");
		
		btnAdminSearchConfirm.setOnAction(e -> {
			String guestChoice = adminSearchType.getValue();
			guestBookList.clear();
			adminBookTable.getItems().clear();
			for (int i = 0; i < grabBookList.size(); i++) {
				if (guestChoice.equals("Title")) { //searches by title
					if (tfAdminSearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getTitle())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Author")) { //searches by author
					if (tfAdminSearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getAuthor())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("ISBN")) { //searches by isbn
					if (tfAdminSearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getIsbn())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Category")) { //searches by genre
					if (tfAdminSearchBar.getText().equalsIgnoreCase(grabBookList.get(i).getCategory())) {
						guestBookList.add(grabBookList.get(i));
					}
				}
				if (guestChoice.equals("Refresh")) {
					guestBookList.add(grabBookList.get(i));
				}
			}
			for (int i = 0; i < guestBookList.size(); i++) { //adds new searched books to the new table view
				adminBookTable.getItems().add(guestBookList.get(i));
			}
		});
		
		Button btnDeleteBook = new Button("DELETE BOOK");
		btnDeleteBook.setStyle("-fx-color: red");
		Button btnDuplicate = new Button("DUPLICATE BOOK");
		Button btnAddBook = new Button("NEW BOOK");
		adminSearchBarTools.getChildren().addAll(adminSearchType,tfAdminSearchBar,btnAdminSearchConfirm,btnAddBook,btnDuplicate,btnDeleteBook);
		
		btnDeleteBook.setOnAction(e -> {
			try {
				Object delete = adminBookTable.getSelectionModel().getSelectedItem();
				Book deleteBook = (Book) delete;
				if (deleteBook.getStatus().equals("in")) {
					for (int i = 0; i < grabBookList.size(); i++) {
						if (grabBookList.get(i).getIsbn().equals(deleteBook.getIsbn())) {
							if (grabBookList.get(i).getBookid().equals(deleteBook.getBookid())) {
								grabBookList.remove(i);
								break;
							}
						}
					}
					guestBookList.clear();
					adminBookTable.getItems().clear();
					for (int i = 0; i < grabBookList.size(); i++) {
						guestBookList.add(grabBookList.get(i));
					}
					for (int i = 0; i < guestBookList.size(); i++) {
						adminBookTable.getItems().add(guestBookList.get(i));
					}
				}
				
			} catch (NullPointerException error) {}
		});
		
		btnDuplicate.setOnAction(e -> { //duplicates the highlighted book
			try {
				Object dupe = adminBookTable.getSelectionModel().getSelectedItem();
				Book dupeBook = (Book) dupe;
				Book newBook = new Book();
				newBook.setTitle(dupeBook.getTitle());
				newBook.setAuthor(dupeBook.getAuthor());
				newBook.setCategory(dupeBook.getCategory());
				newBook.setIsbn(dupeBook.getIsbn());
				newBook.setStatus("in");
				int maxid = 0;
				for (int i = 0; i < grabBookList.size(); i++) {
					if (grabBookList.get(i).getIsbn().equals(dupeBook.getIsbn())) {
						if (Integer.parseInt(grabBookList.get(i).getBookid()) > maxid) {
							maxid = Integer.parseInt(grabBookList.get(i).getBookid());
						}
					}
				}
				maxid++;
				newBook.setBookid(String.valueOf(maxid));
				grabBookList.add(newBook);
				guestBookList.clear();
				adminBookTable.getItems().clear();
				for (int k = 0; k < grabBookList.size(); k++) {
					guestBookList.add(grabBookList.get(k));
				}
				for (int k = 0; k < guestBookList.size(); k++) {
					adminBookTable.getItems().add(guestBookList.get(k));
				}
				
				
			} catch (NullPointerException error) {}
		});
		
		btnAddBook.setOnAction(e -> window.setScene(bookCreation));
		
		layout11.setPadding(new Insets(10,10,10,10));
		BorderPane.setAlignment(adminBookTable, Pos.CENTER);
		subLayout11.getChildren().addAll(adminBookTable,adminSearchBarTools);
		layout11.setTop(subLayout11);
		
		Button btnHome11 = new Button("Admin Portal");
		btnHome11.setOnAction(e -> {
			tfAdminSearchBar.setText("Search...");
			adminSearchType.setValue("Title");
			window.setScene(adminPortal);
		});
		
		layout11.setBottom(btnHome11);
		BorderPane.setAlignment(btnHome11, Pos.BASELINE_CENTER);
		
		adminLibrary = new Scene(layout11,900,600);
		
		/////////////////////////////////////////////
		
		//CREATE NEW BOOK - Stems from Admin Portal
		BorderPane layout12 = new BorderPane();
		layout12.setPadding(new Insets(10,10,10,10));
		HBox bar12 = new HBox(50);
		GridPane subPane12 = new GridPane();
		
		//Background
		Image bookBG = new Image("file:addBook.png");
		BackgroundSize bookImageBGS = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
		Background bookScreenBG = new Background(new BackgroundImage(bookBG, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bookImageBGS));
		layout12.setBackground(bookScreenBG);
		
		Label lblCTitle = new Label("Title");
		Label lblCAuthor = new Label("Author");
		Label lblCISBN = new Label("ISBN");
		Label lblCCategory = new Label("Category");
		Label lblCAmount = new Label("AMOUNT"); //amount of books you'll make
		Label lblCBookFeedback = new Label();
		
		TextField tfCTitle = new TextField();
		TextField tfCAuthor = new TextField();
		TextField tfCISBN = new TextField();
		TextField tfCCategory = new TextField();
		ChoiceBox tfCAmount = new ChoiceBox();
		tfCAmount.getItems().addAll("One","Two","Three","Four","Five");
		tfCAmount.setValue("One");
		
		Button btnCreateBook = new Button("CREATE");
		Button btnManageLibraryBack = new Button("Manage Library");
		
		subPane12.setConstraints(lblCTitle,0,1);
		subPane12.setConstraints(tfCTitle,1,1);
		subPane12.setConstraints(lblCAuthor,0,2);
		subPane12.setConstraints(tfCAuthor,1,2);
		subPane12.setConstraints(lblCISBN,0,3);
		subPane12.setConstraints(tfCISBN,1,3);
		subPane12.setConstraints(lblCCategory,0,4);
		subPane12.setConstraints(tfCCategory,1,4);
		subPane12.setConstraints(lblCAmount,0,5);
		subPane12.setConstraints(tfCAmount,1,5);
		subPane12.setConstraints(btnCreateBook,0,6);
		subPane12.setConstraints(lblCBookFeedback,0,7);
		
		subPane12.getChildren().addAll(lblCTitle,tfCTitle,lblCAuthor,tfCAuthor,lblCISBN,tfCISBN,lblCCategory,tfCCategory,lblCAmount,tfCAmount,btnCreateBook,lblCBookFeedback);
		layout12.setCenter(subPane12);
		layout12.setBottom(btnManageLibraryBack);
		subPane12.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(subPane12,Pos.CENTER);
		BorderPane.setAlignment(btnManageLibraryBack, Pos.BASELINE_CENTER);
		
		btnCreateBook.setOnAction(e -> {
			boolean valid = true;
			boolean fillinblanks = true;
			if (tfCTitle.getText().equals("")) { fillinblanks = false;}
			if (tfCTitle.getText().equals(null)) { fillinblanks = false;}
			if (tfCAuthor.getText().equals("")) { fillinblanks = false;}
			if (tfCAuthor.getText().equals(null)) { fillinblanks = false;}
			if (tfCISBN.getText().equals("")) { fillinblanks = false; }
			if (tfCISBN.getText().equals(null)) { fillinblanks = false; }
			if (tfCCategory.getText().equals("")) { fillinblanks = false; }
			if (tfCCategory.getText().equals(null)) { fillinblanks = false; }
			for (int i = 0; i < grabBookList.size(); i++) {
				if (tfCISBN.getText().equals(grabBookList.get(i).getIsbn())) {
					valid = false;
				}
			}
			
			if ((valid == true) && (fillinblanks == true)) {
				int maxid = 111;
				int copies = 1;
				String ca = (String) tfCAmount.getValue();
				if (ca.equals("Two")) { copies = 2;}
				if (ca.equals("Three")) { copies = 3;}
				if (ca.equals("Four")) { copies = 4;}
				if (ca.equals("Five")) { copies = 5;}
				if (valid == true) {
					for (int i = 0; i < copies; i++) {
						Book book1 = new Book(tfCTitle.getText(),tfCAuthor.getText(),tfCISBN.getText(),tfCCategory.getText(),"in", Integer.toString(maxid));
						myLibrary.registerBook(book1);
						maxid++;
					}
					lblCBookFeedback.setText("");
					tfCTitle.setText("");
					tfCAuthor.setText("");
					tfCISBN.setText("");
					tfCCategory.setText("");
					guestBookList.clear();
					
					adminBookTable.getItems().clear(); //refresh the list
					for (int k = 0; k < grabBookList.size(); k++) {
						guestBookList.add(grabBookList.get(k));
					}
					for (int k = 0; k < guestBookList.size(); k++) {
						adminBookTable.getItems().add(guestBookList.get(k));
					}
					window.setScene(adminLibrary);
				}
			}
			if (valid == false) {
				lblCBookFeedback.setText("ISBN TAKEN");
			}
			if (fillinblanks == false) {
				lblCBookFeedback.setText("FILL IN BLANK SPACES");
			}
			
		});
		
		btnManageLibraryBack.setOnAction(e -> {
			tfCTitle.setText("");
			tfCAuthor.setText("");
			tfCISBN.setText("");
			tfCCategory.setText("");
			tfCAmount.setValue("One");
			lblCBookFeedback.setText("");
			window.setScene(adminLibrary);
		});
	
		
		bookCreation = new Scene(layout12,900,600);
		
		///////////////////////////////////////////////////////////////////////////////////
		
		window.setScene(startScreen);
		window.setTitle("Library Project");
		window.show();

	}
	
	public static void main(String[] args) {
		Application.launch();
	}
	
	//PASSWORD ENCRYPTION
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
	
	//PASSWORD DECRYPTION
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
	
	//SAVING&&LOADING&&CLOSING
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
	
	public static void writeObjectToFile(Object object, String fileName) { 
		try {
				FileOutputStream fileOut = new FileOutputStream(fileName);
				ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
				objectOut.writeObject(object);
				objectOut.close();
				System.out.printf("saved %s\n",fileName);
		}
		catch (IOException e) {
			System.out.printf("could not save %s\n", fileName);
		}
	}
}
