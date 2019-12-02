package com.isc329.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.controlsfx.control.Rating;

import com.isc329.DatabaseUtils;
import com.isc329.Main;
import com.isc329.model.Movie;
import com.sun.glass.ui.Size;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MoviePreviewViewController {
	
	private static Button watch;
	private static Label synopsis, numberOfRatings, genresLabel, genres, release, studioLabel, studio, title, runtimeLabel, runtime;
	private static ImageView poster;
	private static Rating userRating;
	private static final double TITLE_FONT_SIZE = 60;
	private static final double VERTICAL_MARGIN = 10.0;
	private static final int VIEW_WIDTH = 900;
	private static final int SYNOPSIS_WIDTH = 430;
	private static final int SYNOPSIS_HEIGHT = 600;
	private static final double GENERAL_SIDE_ANCHOR = 35.0;
	private static final double GENERAL_TOP_ANCHOR = 40.0;
	private static final double BUTTON_LEFT_ANCHOR = 35.0;
	private static final double BUTTON_TOP_ANCHOR = GENERAL_TOP_ANCHOR + 320 + 10;
	private static final Size BUTTON_SIZE = new Size(300, 51);
	private static final double RATING_RIGHT_ANCHOR = 430 - 265 + 35.0;
	private static final double RATING_TOP_ANCHOR = GENERAL_TOP_ANCHOR + TITLE_FONT_SIZE + VERTICAL_MARGIN + VERTICAL_MARGIN;
	private static final int SIZE_OF_RATING_STARS = 200;
	private static final double VALUES_LEFT_ALIGN = GENERAL_SIDE_ANCHOR + 105;
	
	public static void initialize(Movie movie){
		
		Stage newStage = new Stage();
		newStage.initStyle(StageStyle.UNDECORATED);
		newStage.setAlwaysOnTop(true);
		
		AnchorPane moviePreviewView = new AnchorPane();
		moviePreviewView.setPrefSize(VIEW_WIDTH, Main.getHeight() - 100);
		moviePreviewView.setId("preview");
		
		Scene stageScene = new Scene(moviePreviewView, moviePreviewView.getPrefWidth(), moviePreviewView.getPrefHeight());
		stageScene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
		newStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (! isNowFocused)
				newStage.hide();
		});
		
		title = new Label(movie.getTitle());
		title.setMaxWidth(500);
		title.setId("title");
		moviePreviewView.getChildren().add(title);
		moviePreviewView.setTopAnchor(title, GENERAL_TOP_ANCHOR);
		moviePreviewView.setLeftAnchor(title, GENERAL_SIDE_ANCHOR);
		
		Text titleText = new Text(movie.getTitle());
		titleText.setFont(new Font("graphite", 35));
		double widthOfTitle = titleText.getLayoutBounds().getWidth();
		
		synopsis = new Label(movie.getSynopsis());
		synopsis.setWrapText(true);
		synopsis.setAlignment(Pos.TOP_LEFT);
		synopsis.setMaxWidth(SYNOPSIS_WIDTH);
		synopsis.setPrefHeight(SYNOPSIS_HEIGHT);
		synopsis.setId("synopsis");
		
		Text synText = new Text(movie.getSynopsis());
		synText.setWrappingWidth(430);
		double synopsisHeight = synText.getLayoutBounds().getHeight();
		double synopsisTopAnchor = GENERAL_TOP_ANCHOR + TITLE_FONT_SIZE + VERTICAL_MARGIN;
		moviePreviewView.getChildren().add(synopsis);
		moviePreviewView.setTopAnchor(synopsis, synopsisTopAnchor);
		moviePreviewView.setRightAnchor(synopsis, GENERAL_SIDE_ANCHOR);
		
		try {
			poster = new ImageView(new Image(new FileInputStream(new File(movie.getPosterLocation())), 324, 480, true, true));			
			moviePreviewView.getChildren().add(poster);
			moviePreviewView.setLeftAnchor(poster, GENERAL_SIDE_ANCHOR);
			moviePreviewView.setTopAnchor(poster, GENERAL_TOP_ANCHOR + TITLE_FONT_SIZE + VERTICAL_MARGIN);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		watch = new Button("Watch now");
		watch.setPrefSize(BUTTON_SIZE.width, BUTTON_SIZE.height);
		moviePreviewView.getChildren().add(watch);
		moviePreviewView.setRightAnchor(watch, GENERAL_SIDE_ANCHOR);
		moviePreviewView.setTopAnchor(watch, synopsisHeight + synopsisTopAnchor + (5 * VERTICAL_MARGIN));
		
		userRating = new Rating();
		userRating.setPartialRating(true);
		com.isc329.model.Rating cumulativeRating = null;
		
		try {
			cumulativeRating = DatabaseUtils.getRating(movie.getTitle());
			if(cumulativeRating == null)
				userRating.setRating(0);
			else
				userRating.setRating(cumulativeRating.getRating());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		userRating.setPartialRating(true);
		userRating.ratingProperty().addListener(new ChangeListener<Number>() {
            
			@Override 
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                System.out.println("Rating = " + t1);
               
                try {
                	com.isc329.model.Rating newRating = DatabaseUtils.getRating(movie.getTitle());
                	double rateValue;
                	
                	if(newRating != null) {
                		System.out.print(newRating.getRating() + " + " + userRating.getRating() + " / " + (newRating.getNumberOfRatings() + 1));
                		rateValue = ((newRating.getRating() + userRating.getRating()) / 2);
                		numberOfRatings.setText("(" + (newRating.getNumberOfRatings() + 1) + ")");
                		System.out.println(" = " + rateValue);
                	}
                	else {
                		rateValue = userRating.getRating();
                		numberOfRatings.setText("(" + 1 + ")");
                	}
					DatabaseUtils.updateRating(movie.getTitle(), rateValue);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		
		moviePreviewView.getChildren().add(userRating);
		moviePreviewView.setLeftAnchor(userRating, GENERAL_SIDE_ANCHOR + widthOfTitle + 30);
		moviePreviewView.setTopAnchor(userRating, GENERAL_TOP_ANCHOR + VERTICAL_MARGIN);
		
		if(cumulativeRating != null)
			numberOfRatings = new Label("(" + cumulativeRating.getNumberOfRatings() + ")");
		else
			numberOfRatings = new Label("(0)");
		
		
		numberOfRatings.setId("numRatings");
		Text numRatingsText = new Text(numberOfRatings.getText());
		double widthOfNumRatings = numRatingsText.getLayoutBounds().getWidth();
		double numRatingsLeftAnchor = GENERAL_SIDE_ANCHOR + SIZE_OF_RATING_STARS + 20 + widthOfTitle;
		moviePreviewView.getChildren().add(numberOfRatings);
		moviePreviewView.setLeftAnchor(numberOfRatings, numRatingsLeftAnchor);
		moviePreviewView.setTopAnchor(numberOfRatings, GENERAL_TOP_ANCHOR  + VERTICAL_MARGIN + 2);
		
		release = new Label(movie.getDateReleased().replace(" wide", ""));
		String year = release.getText().substring((release.getText().indexOf(",") + 1), release.getText().length());
		release.setText(year);
		release.setId("labels");
		moviePreviewView.getChildren().add(release);
		moviePreviewView.setLeftAnchor(release, numRatingsLeftAnchor + widthOfNumRatings + VERTICAL_MARGIN);
		moviePreviewView.setTopAnchor(release, GENERAL_TOP_ANCHOR  + VERTICAL_MARGIN + 3);
		
		double posterHeight = poster.getImage().getHeight();
		double genresTopAnchor = GENERAL_TOP_ANCHOR + posterHeight + (3 * VERTICAL_MARGIN) + titleText.getLayoutBounds().getHeight() + 5;
		genresLabel = new Label("Genres");
		genresLabel.setId("labels");
		genresLabel.setMaxWidth(100);
		moviePreviewView.getChildren().add(genresLabel);
		moviePreviewView.setLeftAnchor(genresLabel, GENERAL_SIDE_ANCHOR);
		moviePreviewView.setTopAnchor(genresLabel, genresTopAnchor);
		
		try {
			ArrayList<String> genresReturned = DatabaseUtils.getGenres(movie);
			StringBuilder genreBuilder = new StringBuilder();
			
			if(genresReturned.size() == 1)
				genreBuilder.append(genresReturned.get(0));
			else {
				for(int i = 0; i < genresReturned.size() - 1; ++i)
					genreBuilder.append(genresReturned.get(i) + ", ");
				genreBuilder.append(genresReturned.get(genresReturned.size() - 1));
			}
			
			genres = new Label(genreBuilder.toString());
			genres.setId("value");
			moviePreviewView.getChildren().add(genres);
			moviePreviewView.setLeftAnchor(genres, VALUES_LEFT_ALIGN);
			moviePreviewView.setTopAnchor(genres, genresTopAnchor  + 3);
			
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		double runtimeTopAnchor = genresTopAnchor + (2 * VERTICAL_MARGIN) + 5;
		runtimeLabel = new Label("Runtime");
		runtimeLabel.setId("labels");
		runtimeLabel.setMaxWidth(100);
		moviePreviewView.getChildren().add(runtimeLabel);
		moviePreviewView.setLeftAnchor(runtimeLabel, GENERAL_SIDE_ANCHOR);
		moviePreviewView.setTopAnchor(runtimeLabel, runtimeTopAnchor);
		
		runtime = new Label(movie.getRuntime());
		runtime.setId("value");
		runtime.setMaxWidth(200);
		moviePreviewView.getChildren().add(runtime);
		moviePreviewView.setLeftAnchor(runtime, VALUES_LEFT_ALIGN);
		moviePreviewView.setTopAnchor(runtime, runtimeTopAnchor + 3);
		
		double studioTopAnchor = runtimeTopAnchor + (2 * VERTICAL_MARGIN) + 5;
		studioLabel = new Label("Studio");
		studioLabel.setId("labels");
		studioLabel.setMaxWidth(100);
		moviePreviewView.getChildren().add(studioLabel);
		moviePreviewView.setLeftAnchor(studioLabel, GENERAL_SIDE_ANCHOR);
		moviePreviewView.setTopAnchor(studioLabel, studioTopAnchor);
		
		
		studio = new Label(movie.getStudio());
		studio.setId("value");
		studio.setMaxWidth(400);
		moviePreviewView.getChildren().add(studio);
		moviePreviewView.setLeftAnchor(studio, VALUES_LEFT_ALIGN);
		moviePreviewView.setTopAnchor(studio, studioTopAnchor + 3);
		
		newStage.setX(Main.getWidth() / 2 - (moviePreviewView.getWidth() / 2) + Main.getXPos());
		newStage.setY(Main.getHeight() / 2 - (moviePreviewView.getHeight() / 2) + Main.getYPos());
		
        FadeTransition ft = new FadeTransition(Duration.millis(200), stageScene.getRoot());
        ft.setAutoReverse(true);
        ft.setFromValue(.7);
        ft.setToValue(1);
         
		newStage.setScene(stageScene);
		newStage.show();
		ft.play();

	}
}
