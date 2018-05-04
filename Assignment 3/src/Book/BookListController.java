package Book;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import Controller.GeneralController;
import Database.AppException;
import Database.BookTableGateway;
import Database.ConnectionFactory;
import Database.PublisherTableGateway;
import View.MyController;
import View.SingletonSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class BookListController implements Initializable, MyController, GeneralController {
	
	 private static Logger logger = LogManager.getLogger();
	
	 @FXML private ListView<Book> bookList;
	 private ObservableList<Book> books;
	 @FXML private Button delete;
	 @FXML private Button searchButton;
	 @FXML private TextField searchTitle;
	private BookTableGateway bookGateway;
	private PublisherTableGateway pubGateway;

	 
//	 public BookListController(ObservableList<Book> books) {
//		 	this(bookGateway, pubGateway);
//		 	this.books = books;
//	 }
	 
	 public BookListController(BookTableGateway bookGateway, PublisherTableGateway pubGateway) {
		this.bookGateway = bookGateway;
	    	this.pubGateway = pubGateway;
	    	fetchBook(null);
	 }

	@FXML void onDeleteClick(ActionEvent event) throws IOException {
		 Book book = bookList.getSelectionModel().getSelectedItem();
			 try {
				 this.bookGateway.deleteBook(book);
			 }catch (AppException e) {
				 
			 }
			 fetchBook(searchTitle.getText());
	 }
	    
	 @FXML void switchToBookDetailView(MouseEvent event) throws IOException {
			try {
				if(event.getClickCount()==2) {
					logger.info("Book double clicked.");
					SingletonSwitcher.getInstance().changeView(3,bookList.getSelectionModel().getSelectedItem());
				}
			}catch(Exception e) {
				
			}
	    }
	 @FXML void onSearchClick(ActionEvent event) {
		 fetchBook(searchTitle.getText());
	 }
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.bookList.setItems(books);
	}
	 
	 public void fetchBook(String search) {
		 try {
			 List<Book> books = bookGateway.bookSearch(search);
			 if(this.books == null)
				 this.books = FXCollections.observableArrayList(books);
			 else
				 this.books.setAll(books);
		 } catch (Exception e) {
			 
		 }
	 }

	

}
