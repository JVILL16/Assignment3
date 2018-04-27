package Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Book.Book;
import Book.Publisher;
import View.Launcher;

public class BookTableGateway {
	
	private static Logger logger = LogManager.getLogger(Launcher.class);
	private Connection conn;
	
	public BookTableGateway(Connection conn) {
		this.conn = conn;
	}
	public void deleteBook(Book book) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from BookTable where id = ?");

			st.setInt(1, book.getId());
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
	public void insertBook(Book book) throws AppException {
		PreparedStatement st = null;
		try {
			String sql = "insert into BookTable "
					+ " (title, summary, year_published, publisher_id, isbn, date_added) "
					+ " values (?, ?, ?, ?, ?, ?) ";
			st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPub());
			st.setObject(4, book.getPublisher());
			st.setString(5, book.getISBN());
			st.setDate(6, Date.valueOf(book.getDate()));
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			rs.first();
			book.setId(rs.getInt(1));
			
			logger.info("New id for book is " + book.getId());
			
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
	
	public void updateBook(Book book) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update BookTable set title = ?, summary = ?, year_published = ?, "
					+ "publisher_id = ?, isbn = ?, date_added = ? where id = ?");
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPub());
			st.setObject(4, book.getPublisher());
			st.setString(5, book.getISBN());
			st.setDate(6, Date.valueOf(book.getDate()));
			st.setInt(7, book.getId());
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
	public List<Book> getBooks() throws AppException {
		List<Book> books = new ArrayList<>();

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("select * from BookTable order by title");

			ResultSet rs = st.executeQuery();
			while(rs.next()) {

				Book book = new Book();
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setYearPub(rs.getInt("year_published"));
				book.setPublisher((Publisher) rs.getObject("publisher_id"));
				book.setISBN(rs.getString("isbn"));
				book.setDate(rs.getDate("date_added").toLocalDate());

				book.setBookGateway(this);
				book.setId(rs.getInt("id"));
				books.add(book);
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
		return books;
	}
	
}

