package Book;

import java.time.LocalDate;

import Database.AppException;
import Database.BookTableGateway;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 * 
 * @author CS 4743 Assignment 2 by Jheremi Villarreal
 *
 */
public class Book {
	
	private int id;
	private SimpleStringProperty title, summary, isbn;
	private SimpleIntegerProperty yearPublished;
	private SimpleObjectProperty<LocalDate> dateAdded;
	private SimpleObjectProperty<Publisher> publisher;
	
	private BookTableGateway bookGateway;

	public Book() {
		id = 0;
		title = new SimpleStringProperty();
		summary = new SimpleStringProperty();
		isbn = new SimpleStringProperty();
		yearPublished = new SimpleIntegerProperty();
		dateAdded = new SimpleObjectProperty<LocalDate>();
		publisher = new SimpleObjectProperty<Publisher>();
	}

	public Book(String title, String summary, Integer yearPublished, Publisher publisher, String isbn, LocalDate dateAdded) 
	{
		this();
		setTitle(title);
		setSummary(summary);
		setYearPub(yearPublished);
		setPublisher(publisher);
		setISBN(isbn);
		setDate(dateAdded);
	}
	
	public void save() throws AppException {
		if(this.getId() == 0) {
			bookGateway.insertBook(this);
		} else {
			bookGateway.updateBook(this);
		}
	}
	
	public boolean isValidID(int id) {		
		if(id < 0)
			return false;
		return true;
	}
	
	public boolean isValidTitle(String title) {
		if(title == null || title.length() < 1 || title.length() > 255)
			return false;
		return true;
	}

	public boolean isValidSummary(String summary) {
		if(summary == null)
			return true;
		if(summary.length() < 65536)
			return false;
		return true;
	}
	
	public boolean isValidYearPub(Integer yearPublished) {
		if(yearPublished == null || yearPublished > 2018)
			return false;
		return true;
	}

	public boolean isValidPublisher(Publisher publisher) {
		if(publisher == null)
			return false;
		return true;
	}
	
	public boolean isValidISBN(String isbn) {
		if(isbn == null || isbn.length() > 13)
			return false;
		return true;
	}

	public boolean isValidDate(LocalDate dateAdded) {
		if(dateAdded == null || !dateAdded.isBefore(LocalDate.now()))
			return false;
		return true;
	}
	

	@Override
	public String toString() {
		return "Title Name: " + getTitle();
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
	
	
	public BookTableGateway getBookGateway() {
		return bookGateway;
	}
	public void setBookGateway(BookTableGateway bookGateway) {
		this.bookGateway = bookGateway;
	}
	
	
	public SimpleStringProperty titleProperty() {
		return title;
	}
	public String getTitle() {
		return title.get();
	}
	public void setTitle(String title) {
		this.title.set(title);
	}

	
	public SimpleStringProperty summaryProperty() {
		return summary;
	}
	public String getSummary() {
		return summary.get();
	}
	public void setSummary(String summary) {
		this.summary.set(summary);
	}
	
	
	public SimpleIntegerProperty yearPublishedProperty() {
		return yearPublished;
	}	
	public Integer getYearPub() {
		return yearPublished.get();
	}
	public void setYearPub(Integer yearPublished) {
		this.yearPublished.set(yearPublished);
	}
	
	
	public ObjectProperty<Publisher> publisherProperty() { 
		return publisher; 
	}
	public Publisher getPublisher() {
		return publisher.get();
	}	
	public void setPublisher(Publisher publisher) {
		this.publisher.set(publisher);
	}
	
	
	public SimpleStringProperty isbnProperty() {
		return isbn;
	}
	public String getISBN() {
		return isbn.get();
	}
	public void setISBN(String isbn) {
		this.isbn.set(isbn);
	}
	
	
	public ObjectProperty<LocalDate> dateAddedProperty() { 
		return dateAdded; 
	}
	public LocalDate getDate() {
		return dateAdded.get();
	}	
	public void setDate(LocalDate dateAdded) {
		this.dateAdded.set(dateAdded);
	}
}
