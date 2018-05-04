package Book;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controller.GeneralController;
import Database.AlertHelper;
import Model.Author;
import View.MyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class BookDetailController implements Initializable, MyController, GeneralController {
	
private static Logger logger = LogManager.getLogger();
	
    @FXML private TextField Title;
    @FXML private TextArea Summary;
    @FXML private TextField ISBN;
    @FXML private TextField YearPublished;
    @FXML private ComboBox<Publisher> Publisher;
    @FXML private DatePicker DateAdded;
    @FXML private Button Save;
   
    private Book book;
    private List<Publisher> publishers;

    
    public BookDetailController() {
    	
    }
    
    public BookDetailController(Book book, List<Publisher> publishers) {
    	this();
        this.book = book;
        this.publishers = publishers;
        logger.info("Now showing: " + book.toString());
    }
    @FXML
    public void saveBookClicked() {
    	
    	logger.info("Book's info is saved");
    	
    	if(!book.isValidTitle(book.getTitle())) {
    		logger.error("Invalid Book " + book.getTitle());
    		
    		AlertHelper.showWarningMessage("ERROR", "Book Invalid", "The book that you inputed is invalid, try again.");
    	return;
    	}
    	book.save();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Publisher.setItems(FXCollections.observableArrayList(publishers));

        Title.textProperty().bindBidirectional(book.titleProperty());
        ISBN.textProperty().bindBidirectional(book.isbnProperty());
        Summary.textProperty().bindBidirectional(book.summaryProperty());
        Publisher.valueProperty().bindBidirectional(book.publisherProperty());
        DateAdded.valueProperty().bindBidirectional(book.dateAddedProperty());
	}
}
