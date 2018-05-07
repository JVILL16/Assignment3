package Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.AuditTrailEntrys;
import Model.Book;
import Model.Publisher;
import View.Launcher;

public class BookTableGateway {
	
	private static Logger logger = LogManager.getLogger(Launcher.class);
	private Connection conn = null;
	
	public BookTableGateway(Connection conn) {
		this.conn = conn;
	}
//	public BookTableGateway() {
//
//	}
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
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getISBN());
			st.setDate(6, Date.valueOf(book.getDate()));
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			rs.first();
			book.setId(rs.getInt(1));
			book.getAudits();
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
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getISBN());
			st.setDate(6, Date.valueOf(book.getDate()));
			st.setInt(7, book.getId());
			book.getAudits();
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
	 public List<Book> bookSearch(String search) throws AppException {
			List<Book> books = new ArrayList<>();

			PreparedStatement st = null;

			try {
				
				String query = "select * from BookTable inner join PublisherTable p on publisher_id = p.id";
				if(search != null)
					query += " where title like ?";
				
				st = conn.prepareStatement(query);

				if (search != null)
					st.setString(1, "%" + search + "%");
				
				ResultSet rs = st.executeQuery();
				while(rs.next()) {

					int id = rs.getInt("id");
					String title = rs.getString("title");
					String summary = rs.getString("summary");
					int yearPublished = rs.getInt("year_published");
					String isbn = rs.getString("isbn");
					Date dateAdded = rs.getDate("date_added");

					Publisher publisher = new Publisher(rs.getString("publisher_name"));
					publisher.setId(rs.getInt("publisher_id"));
					
					Book book = new Book(title, summary, yearPublished, publisher, isbn, dateAdded.toLocalDate());
					book.setId(id);
					book.setBookGateway(this);
					books.add(book);
				
				}
			} catch (SQLException e) {
			
				///e.printStackTrace();
				throw new AppException("BookTable fetchBooks failed");
			}
			return books;
		}
	public List<AuditTrailEntrys> getAuditTrail(Book book) throws AppException {
		List<AuditTrailEntrys> audits = new ArrayList<>();
	
		PreparedStatement st = null;
	
		try {
			st = conn.prepareStatement("select * from BookAuditTrailTable where book_id = ? order by date_added ASC");
			st.setInt(1,book.getId());
	
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				Timestamp batDateAdded = rs.getTimestamp("date_added"); 
				String message = rs.getString("entry_msg");
				AuditTrailEntrys audit = new AuditTrailEntrys(batDateAdded.toLocalDateTime(), message);
				audit.setId(id);
				audit.setBook(book);
				audits.add(audit);
				
				/*audit.setMessage(rs.getString("entry_msg"));
				audit.setBatDate(rs.getDate("date_added").toLocalDate());
		
				//audit.setBookGateway(this);
				audit.setId(rs.getInt("id"));
				audits.add(audit);*/
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
		return audits;
	}
	/*public void add(AuditTrailEntrys obj) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into BookAuditTrailTable(id, book_id, entry_msg) values(null, ?, ?)");
			st.setInt(1, obj.getBook().getId());
			st.setString(2, obj.getMessage());
			st.executeUpdate();
			
			logger.info("Saving Audit: " + obj.getMessage());
		}  catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("AuditEntry save failed");
		}finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}*/
}

