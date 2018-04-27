package Book;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controller.GeneralController;
import Database.AlertHelper;
import View.MyController;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class BookDetailController implements Initializable, MyController, GeneralController {
	
private static Logger logger = LogManager.getLogger();
	
    @FXML private TextField Title;
    @FXML private TextField Summary;
    @FXML private TextField ISBN;
    @FXML private TextField YearPublished;
    @FXML private ComboBox<Publisher> Publisher;
    @FXML private DatePicker DateAdded;
    @FXML private Button Save;
   
    private Book book;
    private Publisher publisher;

    
    public BookDetailController() {
    	
    }
    
    public BookDetailController(Book book) {
    	this();
        this.book = book;
        logger.info("Now showing: " + book.toString());
    }
    @FXML
    public void saveBookClicked() {
    	
    	logger.info("Author's info is saved");
    	
    	if(!book.isValidTitle(book.getTitle())) {
    		logger.error("Invalid Author name " + book.getTitle());
    		
    		AlertHelper.showWarningMessage("ERROR", "Author's Name Invalid", "The name that you inputed is invalid, try again.");
    	return;
    	}
    	book.save();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Publisher.setItems(FXCollections.observableArrayList(publisher));

        Title.textProperty().bindBidirectional(book.titleProperty());
        ISBN.textProperty().bindBidirectional(book.isbnProperty());
        Summary.textProperty().bindBidirectional(book.summaryProperty());
        Publisher.valueProperty().bindBidirectional(book.publisherProperty());
        DateAdded.valueProperty().bindBidirectional(book.dateAddedProperty());
	}
}
