package Book;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controller.GeneralController;
import Database.AppException;
import Database.BookTableGateway;
import Database.PublisherTableGateway;
import Model.Book;
import Model.AuditTrailEntrys;
import View.MyController;
import View.SingletonSwitcher;

public class BATListController implements Initializable, MyController, GeneralController {
	
	private static Logger logger = LogManager.getLogger();
	
	@FXML private Label BookTitle;
	@FXML private Button Back;
	//@FXML private ListView<BookAuditTrail> audits;
	@FXML private TableView<AuditTrailEntrys> tableView;
	
	private Book book;
	private ObservableList<AuditTrailEntrys> audits;
	
	
	public BATListController() {
		
	}
	
	public BATListController(Book book, List<AuditTrailEntrys> audits) {
		this();
		this.book = book;
		this.audits = FXCollections.observableArrayList(audits);		
	}
	
	@FXML 
	void backButtonPressed(ActionEvent event) {
		logger.info("Back Button Pressed");
		try {
			SingletonSwitcher.getInstance().changeView(3, book);
		}catch(AppException e) {
			e.printStackTrace();
		}
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		logger.info("Initialize is working");
		BookTitle.setText("Audit Trail for " + book.getTitle());

		TableColumn<AuditTrailEntrys, LocalDateTime> columnDate = new TableColumn<>("Date Added");	
		columnDate.setCellValueFactory(new PropertyValueFactory<AuditTrailEntrys, LocalDateTime>("batDateAdded"));
		
		TableColumn<AuditTrailEntrys, String> columnMessage = new TableColumn<>("Message");
		columnMessage.setCellValueFactory(new PropertyValueFactory<AuditTrailEntrys, String>("message"));
		
		tableView.getColumns().setAll(columnDate, columnMessage);
		tableView.setItems(audits);
	}
	

}
