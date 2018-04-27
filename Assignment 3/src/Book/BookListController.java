package Book;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controller.GeneralController;
import Database.AppException;
import Database.BookTableGateway;
import View.MyController;
import View.SingletonSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class BookListController implements Initializable, MyController, GeneralController {
	
	 private static Logger logger = LogManager.getLogger();
	
	 @FXML private ListView<Book> bookList;
	 private ObservableList<Book> books;
	 @FXML private Button delete;

	private BookTableGateway bookGateway;

	 
	 public BookListController(BookTableGateway bookGateway) {
	    	this.bookGateway = bookGateway;
	    	fetchBook();
	 }
	 
	 @FXML void onDeleteClick(ActionEvent event) throws IOException {
		 Book book = bookList.getSelectionModel().getSelectedItem();
			 try {
				 this.bookGateway.deleteBook(book);
			 }catch (AppException e) {
				 
			 }
			 fetchBook();
	 }
	    
	 @FXML void switchToBookDetailView(MouseEvent event) throws IOException {
			try {
				if(event.getClickCount()==2) {
					logger.info("Book double clicked.");
					SingletonSwitcher.getInstance().changeView(1,bookList.getSelectionModel().getSelectedItem());
				}
			}catch(Exception e) {
				
			}
	    }
	 public void fetchBook() {
		 List<Book> books = bookGateway.getBooks();
		 if(this.books == null)
			 this.books = FXCollections.observableArrayList(books);
		 else
			 this.books.setAll(books);
	 }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
			this.bookList.setItems(books);
	}

}
