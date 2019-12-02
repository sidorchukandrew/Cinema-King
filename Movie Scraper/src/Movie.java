
public class Movie {

	private String rating;
	private String genres;
	private String dateReleased;
	private String runtime;
	private String studio;
	private String title;
	private String synopsis;
	private String posterLocation;
	
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String [] getGenres() {

		String [] genres = this.genres.split(",");
		for(int i = 0; i < genres.length; ++i)
			genres[i] = genres[i].trim();
		
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getDateReleased() {
		return dateReleased;
	}
	public void setDateReleased(String dateReleased) {
		this.dateReleased = dateReleased;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getStudio() {
		return studio;
	}
	public void setStudio(String studio) {
		this.studio = studio;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getPosterLocation() {
		return posterLocation;
	}
	public void setPosterLocation(String posterLocation) {
		this.posterLocation = posterLocation;
	}
	
}
