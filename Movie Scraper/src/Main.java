import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	public static final String USERS_TABLE = "users";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "Admin329";
	private static final String CONN_STRING = "jdbc:mysql://localhost/cinema_king";
	
	public static void main(String[] args) {

		Hashtable<Integer, String> sitesSoFar = new Hashtable<Integer, String>();
		String site = "https://www.rottentomatoes.com/top/bestofrt/";

		Document document;
		try {
			document = Jsoup.connect(site).get();
			Elements elements = document.select("a");
			for(Element e  : elements) {
				if(e.attr("href").matches("/m/\\w*")) {
				
					if(!sitesSoFar.containsKey(e.attr("abs:href").hashCode())) {
						sitesSoFar.put(e.attr("abs:href").hashCode(), e.attr("abs:href"));
						readWebsite(e.attr("abs:href"));
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private static void storeMovie(Movie movie) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String insertMovie = "INSERT INTO movies(title, rating, runtime, studio, dateReleased, synopsis, posterLocation) VALUES (?, ?, ?, ?, ?, ?, ?);";
		String insertGenres = "INSERT INTO moviegenres(title, genre) VALUES (?, ?)";
		
		try {
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
			stmt = conn.prepareStatement(insertMovie);
			stmt.setString(1,  movie.getTitle());
			stmt.setString(2, movie.getRating());
			stmt.setString(3, movie.getRuntime());
			stmt.setString(4,  movie.getStudio());
			stmt.setString(5, movie.getDateReleased());
			stmt.setString(6, movie.getSynopsis());
			stmt.setString(7, movie.getPosterLocation());
			stmt.executeUpdate();
			
			for(String genre : movie.getGenres()) {
				stmt = conn.prepareStatement(insertGenres);
				stmt.setString(1, movie.getTitle());
				stmt.setString(2, genre);
				stmt.executeUpdate();
			}
			
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
	
	private static String getImage(String src, String title){
		 
		String posterLocation = "C:\\Users\\Andrew Sidorchuk\\Databases Workspace\\Movie Scraper\\rsc\\";
        
        if(title.contains("/"))
        	title = title.replace("/", "");
        if(title.contains("?"))
        	title = title.replace("?", "");
        if(title.contains("\\"))
        	title = title.replace("\\", "");
        if(title.contains(":"))
        	title = title.replace(":", "_");
 
        //Open a URL Stream
        URL url;
		try {
			url = new URL(src);
			InputStream in = url.openStream();
			
			OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(posterLocation + title + ".gif")));
			
			for (int b; (b = in.read()) != -1;) {
				out.write(b);
			}
			out.close();
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return posterLocation + title + ".gif";
	 
	}
	
	private static void readWebsite(String url) {
	
		System.out.println(url);
		try {
			Document document = Jsoup.connect(url).get();
			Movie movie = new Movie();
			Elements values = document.select("div.meta-value");
			Elements labels = document.select("div.meta-label.subtle");
			Elements elements;
			
			for(int i = 0; i < labels.size(); ++i) {
				String label = labels.get(i).text();
				if(label.compareTo("Rating:") == 0)
					movie.setRating(values.get(i).text());
				else if(label.compareTo("Genre:") == 0)
					movie.setGenres(values.get(i).text());
				else if(label.compareTo("In Theaters:") == 0)
					movie.setDateReleased(values.get(i).text());
				else if(label.compareTo("Runtime:") == 0)
					movie.setRuntime(values.get(i).text());
				else if(label.compareTo("Studio:") == 0)
					movie.setStudio(values.get(i).text());
			}
			
			elements = document.getElementsByTag("h1");
			movie.setTitle(elements.get(0).text());
			
			elements = document.select("div#movieSynopsis");
			movie.setSynopsis(elements.get(0).text());
			
	        Elements img = document.select("img.posterImage.js-lazyLoad");
	
	        for (Element el : img) {
	            String src = el.absUrl("data-srcset");
	            if(src.compareTo("") == 0)
	            	src = el.absUrl("data-src");
	            String [] s = src.split(" ");
	            movie.setPosterLocation(getImage(s[0], movie.getTitle()));
	         } 
			
			storeMovie(movie);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception thrown here");
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}

