package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Database.AlertHelper;
import Model.Author;
import View.MyController;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AuthorDetailController implements Initializable, MyController, GeneralController {
	
private static Logger logger = LogManager.getLogger();
	
    @FXML private TextField FirstName;
    @FXML private TextField LastName;
    @FXML private TextField Website;
    @FXML private ComboBox<String> gender;
    @FXML private DatePicker datePicker;
    @FXML private Button Save;
   
    private Author author;

    
    public AuthorDetailController() {
    	
    }
    
    public AuthorDetailController(Author author) {
    	this();
        this.author = author;
        logger.info("Now showing: " + author.toString());
    }
    @FXML
    public void saveAuthorClicked() {
    	
    	logger.info("Author's info is saved");
    	
    	if(!author.isValidFirstName(author.getAuthorFullName())) {
    		logger.error("Invalid Author name " + author.getAuthorFullName());
    		
    		AlertHelper.showWarningMessage("ERROR", "Author's Name Invalid", "The name that you inputed is invalid, try again.");
    	return;
    	}
    	author.save();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		gender.setItems(FXCollections.observableArrayList("M","F","U"));

        FirstName.textProperty().bindBidirectional(author.authorFirstNameProperty());
        LastName.textProperty().bindBidirectional(author.authorLastNameProperty());
        Website.textProperty().bindBidirectional(author.authorWebsiteProperty());
        gender.valueProperty().bindBidirectional(author.authorGenderProperty());
        
        //setGender();
        //setFirstName();
        //setLastName();
        //setDOB();
        //setWebsite();
        datePicker.valueProperty().bindBidirectional(author.authorBirthDateProperty());
	}
	/*private void setGender(){
		
		if(author.getAuthorGender() == null){
		        return;
		}
		if(author.getAuthorGender().equals("M") && Male.isPressed()) {
	       Male.setSelected(true);
	       Female.setSelected(false);
	       author.setAuthorGender("M");
	       Male.textProperty().bindBidirectional(author.authorGenderProperty());
		   
		   

		}else if(author.getAuthorGender().equals("F") && Female.isPressed()){
		   Male.setSelected(false);
		   Female.setSelected(true);
		   author.setAuthorGender(Female.getText());

		}
	}*/
	/*private void setFirstName() {
		if(author.getAuthorFirstName() == null) {
			return;
		}	
	}
	private void setLastName() {
		if(author.getAuthorLastName() == null) {
			return;
		}
	}
	private void setDOB() {
		if(author.getDOB() == null) {
			return;
		}
	}
	private void setWebsite() {
		if(author.getAuthorWebsite() == null) {
			return;
		}
	}*/
}
