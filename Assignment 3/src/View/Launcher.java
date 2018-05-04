package View;



import java.net.URL;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Database.AppException;
import Database.ConnectionFactory;
import Model.Author;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Launcher extends Application{

	private static Logger logger = LogManager.getLogger();
	
	private ObservableList<Author> authors;
	private Connection conn;
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		URL fxmlFile = this.getClass().getResource("mainMenu.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);

		BorderPane rootNode = loader.load();
		Scene scene = new Scene(rootNode, 600, 500);
		primaryStage.setTitle("Book Inventory System");
		primaryStage.setScene(scene);
		primaryStage.show();		

		
		SingletonSwitcher viewSwitch = SingletonSwitcher.getInstance();
		viewSwitch.setConnection(conn);
		viewSwitch.setRootNode((BorderPane) rootNode);
		viewSwitch.setAuthors(authors);



	}
	@Override
	public void init() throws Exception {
		super.init();
		authors = FXCollections.observableArrayList();
		logger.info("Creating connection...");
		
		try {
			conn = ConnectionFactory.createConnection();
		} catch(AppException e) {
			logger.fatal("Cannot connect to db");
			Platform.exit();
		}
	}


	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		logger.info("Closing connection...");
		
		conn.close();
	}


	public static void main(String[] args) {
		launch(args);

	}

}
