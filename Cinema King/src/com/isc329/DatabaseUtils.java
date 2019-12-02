package com.isc329;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.isc329.model.Movie;
import com.isc329.model.Rating;

public class DatabaseUtils {

	public static final String USERS_TABLE = "users";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "Admin329";
	private static String insertUser = "INSERT into users (first_name, last_name, email, password, member_since) VALUES " +
										"(?, ?, ?, ?, NOW())";
	private static String signinUser = "SELECT first_name FROM users WHERE email = ? AND password = ?";
	private static final String CONN_STRING = "jdbc:mysql://localhost/cinema_king";
	
	public static void createNewUser(String name, String email, String password) throws SQLException {
			
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(insertUser);
			stmt.setString(1,  name);
			stmt.setString(2, "");
			stmt.setString(3, email);
			stmt.setString(4, password);
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
	}
	
	public static String attemptSigninUser(String email, String password) throws SQLException {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(signinUser, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			stmt.setString(1, email);
			stmt.setString(2, password);
			rs = stmt.executeQuery();

			if(rs.first())
				return rs.getString("first_name");
			else
				return null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		
		return null;
	}
	
	public static ArrayList<Movie> getByGenre(String genre) throws SQLException {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT DISTINCT movies.title, rating, runtime, studio, dateReleased, synopsis, posterLocation FROM movies INNER JOIN moviegenres WHERE moviegenres.genre = ? AND moviegenres.title = movies.title;";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, genre);
			rs = stmt.executeQuery();
			
			ArrayList<Movie> movies = new ArrayList<Movie>();
			
			while(rs.next()) {
				Movie movie = new Movie();
				
				movie.setTitle(rs.getString("movies.title"));
				movie.setRating(rs.getString("rating"));
				movie.setRuntime(rs.getString("runtime"));
				movie.setStudio(rs.getString("studio"));
				movie.setDateReleased(rs.getString("dateReleased"));
				movie.setSynopsis(rs.getString("synopsis"));
				movie.setPosterLocation(rs.getString("posterLocation"));
				movies.add(movie);
			}
			
			return movies;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		
		return null;
	}
	
	public static ArrayList<Movie> getByGenre(String genre, String butNotThisOne) throws SQLException {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT DISTINCT movies.title, rating, runtime, studio, dateReleased, synopsis, posterLocation FROM movies INNER JOIN moviegenres WHERE moviegenres.genre = ? "
				+ "AND moviegenres.title = movies.title "
				+ "AND moviegenres.title != ?;";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, genre);
			stmt.setString(2, butNotThisOne);
			rs = stmt.executeQuery();
			
			ArrayList<Movie> movies = new ArrayList<Movie>();
			
			while(rs.next()) {
				Movie movie = new Movie();
				
				movie.setTitle(rs.getString("movies.title"));
				movie.setRating(rs.getString("rating"));
				movie.setRuntime(rs.getString("runtime"));
				movie.setStudio(rs.getString("studio"));
				movie.setDateReleased(rs.getString("dateReleased"));
				movie.setSynopsis(rs.getString("synopsis"));
				movie.setPosterLocation(rs.getString("posterLocation"));
				movies.add(movie);
			}
			
			return movies;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		
		return null;
	}
	
	public static ArrayList<Movie> search(String search) throws SQLException {
		
		ArrayList<Movie> movies = isGenre(search);
		if(movies.size() == 0)
			System.out.println("Not a genre");
		else
			return movies;
		
		movies = isYear(search);
		
		if(movies.size() == 0)
			System.out.println("Not a year");
		else
			return movies;
		
		Movie movie = isMovie(search);
		
		if(movie != null) {
			System.out.println("Movie found! - " + movie.getTitle());
			ArrayList<String> genres = getGenres(movie);
			System.out.println("Getting by genre: " + genres.get(0));
			movies = getByGenre(genres.get(0), movie.getTitle());
			movies.add(0, movie);
		}
			
		return movies;
	}
	
	
	private static ArrayList<Movie> isYear(String search) throws SQLException{
		
		search = search.trim();
		int year = 0;
		ArrayList<Movie> movies = new ArrayList<Movie>();
		
		try {
			year = Integer.parseInt(search);
			System.out.println(year);
		} catch(NumberFormatException e) {
			return movies;
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT movies.title, rating, runtime, studio, dateReleased, synopsis, posterLocation FROM movies WHERE SUBSTRING(dateReleased FROM INSTR( dateReleased, ?) FOR 4 );";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, search);
			rs = stmt.executeQuery();
			
			
			while(rs.next()) {
				Movie movie = new Movie();
				
				movie.setTitle(rs.getString("movies.title"));
				movie.setRating(rs.getString("rating"));
				movie.setRuntime(rs.getString("runtime"));
				movie.setStudio(rs.getString("studio"));
				movie.setDateReleased(rs.getString("dateReleased"));
				movie.setSynopsis(rs.getString("synopsis"));
				movie.setPosterLocation(rs.getString("posterLocation"));
				movies.add(movie);
			}
			
			return movies;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		
		return null;
	}
	private static ArrayList<Movie> isGenre(String search) throws SQLException {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT DISTINCT movies.title, rating, runtime, studio, dateReleased, synopsis, posterLocation FROM movies INNER JOIN moviegenres WHERE "
				+ "? LIKE CONCAT('%', SUBSTRING(genre, 2, (LENGTH(genre) - 2)), '%') AND moviegenres.title = movies.title;";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, search);
			rs = stmt.executeQuery();
			
			ArrayList<Movie> movies = new ArrayList<Movie>();
			
			while(rs.next()) {
				Movie movie = new Movie();
				
				movie.setTitle(rs.getString("movies.title"));
				movie.setRating(rs.getString("rating"));
				movie.setRuntime(rs.getString("runtime"));
				movie.setStudio(rs.getString("studio"));
				movie.setDateReleased(rs.getString("dateReleased"));
				movie.setSynopsis(rs.getString("synopsis"));
				movie.setPosterLocation(rs.getString("posterLocation"));
				movies.add(movie);
			}
			
			return movies;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		
		return null;
	}
	
	public static Movie isMovie(String search) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT DISTINCT movies.title, rating, runtime, studio, dateReleased, synopsis, posterLocation FROM movies INNER JOIN moviegenres WHERE moviegenres.title = "
				+ "? AND moviegenres.title = movies.title;";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, search);
			rs = stmt.executeQuery();
			
			Movie movie = null;
			if(rs.next()) {
				
				movie = new Movie();
				movie.setTitle(rs.getString("movies.title"));
				movie.setRating(rs.getString("rating"));
				movie.setRuntime(rs.getString("runtime"));
				movie.setStudio(rs.getString("studio"));
				movie.setDateReleased(rs.getString("dateReleased"));
				movie.setSynopsis(rs.getString("synopsis"));
				movie.setPosterLocation(rs.getString("posterLocation"));
			}
			
			return movie;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		return null;
	}
	
	public static void updateRating(String title, double rating) throws SQLException {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String updateQuery = "UPDATE movieratings SET rating = ?, numberOfRatings = numberOfRatings + 1 WHERE title = ? ;";
		String insertQuery = "INSERT INTO movieratings (title, rating, numberOfRatings) VALUES (?, ?, 1);";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(updateQuery);
			stmt.setDouble(1, rating);
			stmt.setString(2, title);
			int numAffected = stmt.executeUpdate();
			
			if(numAffected == 0) {
				stmt = conn.prepareStatement(insertQuery);
				stmt.setString(1, title);
				stmt.setDouble(2, rating);
				stmt.executeUpdate();
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
	}
	
	public static Rating getRating(String title) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT rating, numberOfRatings FROM movieratings WHERE title = ?;";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, title);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				Rating rating = new Rating();
				rating.setTitle(title);
				rating.setRating(rs.getDouble("rating"));
				rating.setNumberOfRatings(rs.getInt("numberOfRatings"));
				return rating;
			}	
			else
				return null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		return null;
	}
	
	public static ArrayList<String> getGenres(Movie movie) throws SQLException {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT DISTINCT genre FROM moviegenres WHERE moviegenres.title =  ? ;";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, movie.getTitle());
			rs = stmt.executeQuery();
			
			ArrayList<String> genres = new ArrayList<String>();
			while(rs.next()) {
				genres.add(rs.getString("genre"));
			}
			
			return genres;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		return null;
	}
}
