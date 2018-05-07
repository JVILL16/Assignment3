package Book;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controller.GeneralController;
import Database.AlertHelper;
import Database.AppException;
import Model.AuditTrailEntrys;
import Model.Author;
import Model.Book;
import Model.Publisher;
import View.MyController;
import View.SingletonSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    @FXML private Button AuditTrail;
    
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
    
    @FXML
    public void auditTrailViewClicked(ActionEvent event) {
    	logger.info("Audit Trail View for Book");
    	try {
	    	List<AuditTrailEntrys> audits = book.getAudits();
	    	if(audits.size() > 0) {
	    		logger.info("Audits: " + audits);
	    	
	        	SingletonSwitcher.getInstance().changeView(4, book);
	    	}
	    	else {
	    		logger.error("No Audit Trail Exist");
	    		AlertHelper.showWarningMessage("ERROR", "Book has no Audit Trail", "The audit trail does not exist");
	    	}
    	}catch(AppException e) {
	    		e.printStackTrace();
	    }
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Publisher.setItems(FXCollections.observableArrayList(publishers));

        Title.textProperty().bindBidirectional(book.titleProperty());
        ISBN.textProperty().bindBidirectional(book.isbnProperty());
        YearPublished.textProperty().bindBidirectional(book.yearPublishedProperty(), new ConvertYear());
        Summary.textProperty().bindBidirectional(book.summaryProperty());
        Publisher.valueProperty().bindBidirectional(book.publisherProperty());
        DateAdded.valueProperty().bindBidirectional(book.dateAddedProperty());
	}
}
