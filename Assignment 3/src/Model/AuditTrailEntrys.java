package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Database.AuditEntryTableGateway;
import Database.BookTableGateway;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class AuditTrailEntrys {
	private int id;
	private SimpleObjectProperty<LocalDateTime> batDateAdded;
	private SimpleStringProperty message;
	private Book book;
	private BookTableGateway bookGateway;
	private AuditEntryTableGateway auditGateway;
	
	public AuditTrailEntrys() {
		id = 0;
		batDateAdded = new SimpleObjectProperty<LocalDateTime>();
		message = new SimpleStringProperty();
		//book = new Book();
	}

	public AuditTrailEntrys(LocalDateTime batDateAdded, String message) 
	{
		this();
		setBatDate(batDateAdded);
		setMessage(message);
		//setBook(book);
	}
	
	public boolean isValidID(int id) {		
		if(id < 0)
			return false;
		return true;
	}
	
	public boolean isValidMessage(String message) {
		if(message == null || message.length() < 1 || message.length() > 255)
			return false;
		return true;
	}
	
	public boolean isValidDate(LocalDate batDateAdded) {
		if(batDateAdded == null || !batDateAdded.isBefore(LocalDate.now()))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "The Message is: " + getMessage();
	}

	/**
	 * Everything Below Here should be mutators/accesors/SimpleStringProperties, please feel free to ignore all of it.
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public SimpleStringProperty messageProperty() {
		return message;
	}
	public String getMessage() {
		return message.get();
	}
	public void setMessage(String message) {
		this.message.set(message);
	}
	
	public ObjectProperty<LocalDateTime> batDateAddedProperty() { 
		return batDateAdded; 
	}
	public LocalDateTime getBatDate() {
		return batDateAdded.get();
	}	
	public void setBatDate(LocalDateTime batDateAdded) {
		this.batDateAdded.set(batDateAdded);
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}

	
	public BookTableGateway getBookGateway() {
		return bookGateway;
	}
	public void setBookGateway(BookTableGateway bookGateway) {
		this.bookGateway = bookGateway;
	}
	
	public AuditEntryTableGateway getAuditGateway() {
		return auditGateway;
	}
	public void setAuditGateway(AuditEntryTableGateway auditGateway) {
		this.auditGateway = auditGateway;
	}

	

}
