package View;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Book.BATListController;
import Book.BookDetailController;
import Book.BookListController;
import Controller.AuthorDetailController;
import Controller.AuthorListController;
import Controller.GeneralController;
import Database.AppException;
import Database.AuthorTableGateway;
import Database.BookTableGateway;
import Database.PublisherTableGateway;
import Model.AuditTrailEntrys;
import Model.Author;
import Model.Book;
import Model.Publisher;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;


public class SingletonSwitcher {
	
	private ObservableList<Author> authors;
	private ObservableList<Book> books;
	private List<Publisher> publishers;
	private List<AuditTrailEntrys> audits;

	public static final int AUTHOR_LIST = 0;
	public static final int AUTHOR_DETAIL = 1;
	public static final int BOOK_LIST = 2;
	public static final int BOOK_DETAIL = 3;
	public static final int BOOK_AUDIT_TRAIL = 4;
	private static Logger logger = LogManager.getLogger(Launcher.class);
	static  SingletonSwitcher singletonController = null;
	private BorderPane rootPane;
	private Connection conn;
	private BookTableGateway bookGateway;
	private PublisherTableGateway pubGateway;
	
	private SingletonSwitcher() {}

	public static SingletonSwitcher getInstance() {
		if(singletonController == null) {
			singletonController = new SingletonSwitcher();
		}
		
		return singletonController;
	}
	
	public void setRootNode(BorderPane rootNode) {
		this.rootPane = rootNode;
	}
	
	public void setAuthors(ObservableList<Author> authorList){
		this.authors = authorList;

	}
	
	
    public void changeView(int viewType, Object arg) throws AppException {
		try {
			GeneralController controller = null;
			URL fxmlFile = null;
			switch(viewType) {
				case AUTHOR_LIST:
					fxmlFile = this.getClass().getResource("AuthorListView.fxml");
					controller = new AuthorListController(new AuthorTableGateway(conn));
					break;
				case AUTHOR_DETAIL:
					fxmlFile = this.getClass().getResource("AuthorDetailView.fxml");
					controller = new AuthorDetailController((Author) arg);
					break;
				case BOOK_LIST:
					fxmlFile = this.getClass().getResource("BookListView.fxml");
					controller = new BookListController(new BookTableGateway(conn), new PublisherTableGateway(conn));
					break;
				case BOOK_DETAIL:
					pubGateway = new PublisherTableGateway(conn);
					publishers = pubGateway.getPublisher();
					fxmlFile = this.getClass().getResource("BookDetailView.fxml");
					controller = new BookDetailController((Book) arg, publishers);
					break;
				case BOOK_AUDIT_TRAIL:
					bookGateway = new BookTableGateway(conn);
					audits = bookGateway.getAuditTrail((Book) arg);
					fxmlFile = this.getClass().getResource("AuditTrailView.fxml");
					controller = new BATListController((Book) arg, audits);
					break;
					
			}
		
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(controller);
		
			Parent viewNode = loader.load();
			rootPane.setCenter(viewNode);
		} catch (IOException e) {
			throw new AppException(e);
		}
	}

    public Connection getConnection() {
		return conn;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}


}
