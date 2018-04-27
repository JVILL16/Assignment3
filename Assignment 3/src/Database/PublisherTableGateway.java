package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Book.Publisher;
import View.Launcher;

public class PublisherTableGateway {
	
	private static Logger logger = LogManager.getLogger(Launcher.class);
	private Connection conn;
	
	public PublisherTableGateway(Connection conn) {
		this.conn = conn;
	}
	public void deletePublisher(Publisher publisher) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from PublisherTable where id = ?");

			st.setInt(1, publisher.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	public void insertPublisher(Publisher publisher) throws AppException {
		PreparedStatement st = null;
		try {
			String sql = "insert into PublisherTable "
					+ " (publisher_name) "
					+ " values (?) ";
			st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, publisher.getPublisherName());
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			rs.first();
			publisher.setId(rs.getInt(1));
			
			logger.info("New id for publisher is " + publisher.getId());
			
			rs.close();

		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
	
	public void updatePublisher(Publisher publisher) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update PublisherTable set publisher_name = ? where id = ?");
			st.setString(1, publisher.getPublisherName());
			st.setInt(2, publisher.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	public List<Publisher> getPublisher() throws AppException {
		List<Publisher> publishers = new ArrayList<>();

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("select * from BookTable order by title");

			ResultSet rs = st.executeQuery();
			while(rs.next()) {

				Publisher publisher = new Publisher();
				publisher.setPublisherName(rs.getString("publisher_name"));

				publisher.setPublisherGateway(this);
				publisher.setId(rs.getInt("id"));
				publishers.add(publisher);
			}
		} catch (SQLException e) {
		
			///e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return publishers;
	}
	
}
