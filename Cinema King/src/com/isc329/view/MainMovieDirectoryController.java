package com.isc329.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.isc329.DatabaseUtils;
import com.isc329.Main;
import com.isc329.model.GridPaneCoordinates;
import com.isc329.model.Movie;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class MainMovieDirectoryController {
	
	@FXML private ListView listOne;
	@FXML private ListView listTwo;
	@FXML private ListView listThree;
	@FXML private ListView listFour;
	
	private Label labelOne;
	private Label labelTwo;
	private Label labelThree;
	private Label labelFour;
	
	@FXML private GridPane grid;
	@FXML private ScrollPane scroll;
	@FXML private AnchorPane pane;
	@FXML private AnchorPane root;
	
	@FXML private TextField searchField;
	private ArrayList<Movie> moviesForCategoryOne;
	private ArrayList<Movie> moviesForCategoryTwo;
	private ArrayList<Movie> moviesForCategoryThree;
	private ArrayList<Movie> moviesForCategoryFour;
	@FXML private AnchorPane searchPane;
	
	@FXML private Hyperlink closeSearchPane;
	
	private GridPane returnedMovies;
	private GridPaneCoordinates coordinates;
	private boolean searchPaneIsOpen = false;
	private Label relatedMoviesLabel;
	private ScrollPane scrollForRelatedMovies;
	
	@FXML
	private void initialize() {

			scroll.setFitToWidth(true);
			
			searchPane.setLayoutY(-Main.getHeight() + 70);
			searchPane.setLayoutX(0);
			searchPane.setPrefSize(Main.getWidth(), Main.getHeight());
			searchPane.setId("searchPane");
			
			searchField.setLayoutX(Main.width / 2 - (searchField.getPrefWidth() / 2));
			searchField.setLayoutY(Main.getHeight() - 60);
			searchPane.setLeftAnchor(searchField, Main.width / 2 - (searchField.getPrefWidth() / 2));
			searchField.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					try {
						if(!searchPaneIsOpen)
							slideSearchPane();
						
						returnedMovies.getChildren().clear();
						populateReturnedMovies(DatabaseUtils.search(searchField.getText()));
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			searchField.setId("searchField");
			
			searchPane.setTopAnchor(closeSearchPane, 50.0);
			searchPane.setLeftAnchor(closeSearchPane, 70.0);
			closeSearchPane.setId("X");
			closeSearchPane.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					closeSearchPane();
				}
			});
			
			relatedMoviesLabel = new Label("Related Movies");
			relatedMoviesLabel.setId("labels");
			searchPane.getChildren().add(relatedMoviesLabel);
			searchPane.setTopAnchor(relatedMoviesLabel, 170.0);
			searchPane.setLeftAnchor(relatedMoviesLabel, 400.0);
			
			
			returnedMovies = new GridPane();
			
			scrollForRelatedMovies = new ScrollPane();
			scrollForRelatedMovies.setFitToWidth(false);
			scrollForRelatedMovies.setMaxHeight(750);
			scrollForRelatedMovies.setHbarPolicy(ScrollBarPolicy.NEVER);
			scrollForRelatedMovies.setVbarPolicy(ScrollBarPolicy.NEVER);
			searchPane.setTopAnchor(scrollForRelatedMovies, 200.0);
			searchPane.setLeftAnchor(scrollForRelatedMovies, 350.0);
			scrollForRelatedMovies.setContent(returnedMovies);
			searchPane.getChildren().add(scrollForRelatedMovies);
			
			grid.setPrefHeight(Main.height + 500);
			grid.setPrefWidth(Main.width); 
			
			labelOne = new Label("Animation");
			labelOne.setId("labels");
			labelOne.setLabelFor(listOne);
			root.setTopAnchor(labelOne, 300.0);
			root.setLeftAnchor(labelOne, 30.0);
			
			listOne.setPrefWidth(Main.width);
			listOne.setPrefHeight(250);
			listOne.setMinHeight(250);
			listOne.setOrientation(Orientation.HORIZONTAL);
			
			listTwo.setPrefWidth(Main.width);
			listTwo.setPrefHeight(250);
			listTwo.setMinHeight(250);
			listTwo.setOrientation(Orientation.HORIZONTAL);

			listThree.setPrefWidth(Main.width);
			listThree.setPrefHeight(250);
			listThree.setMinHeight(250);
			listThree.setOrientation(Orientation.HORIZONTAL);

			listFour.setPrefWidth(Main.width);
			listFour.setPrefHeight(250);
			listFour.setMinHeight(250);
			listFour.setOrientation(Orientation.HORIZONTAL);
			
			populateListOne();
			populateListTwo();
			populateListThree();
			populateListFour();
			Main.getStage().widthProperty().addListener((obs, oldVal, newVal)->{
				searchField.setLayoutX(Main.width / 2 - (searchField.getPrefWidth() / 2));
			});
			
	}
	
	public void populateListOne() {
		
		
		try {
			moviesForCategoryOne= DatabaseUtils.getByGenre("Animation");
			
			for(Movie m : moviesForCategoryOne) {
				
				ImageView view = new ImageView(new Image(new FileInputStream(new File(m.getPosterLocation()))));
				view.setFitHeight(240);
				view.setFitWidth(162);
				view.setId("image");
				listOne.getItems().add(view);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateListTwo() {
		
		try {
			moviesForCategoryTwo = DatabaseUtils.getByGenre("Comedy");
			
			for(Movie m : moviesForCategoryTwo) {
				
				ImageView view = new ImageView(new Image(new FileInputStream(new File(m.getPosterLocation()))));
				view.setFitHeight(240);
				view.setFitWidth(162);
				view.setId("image");
				listTwo.getItems().add(view);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateListThree() {
		
		try {
			moviesForCategoryThree = DatabaseUtils.getByGenre("Classics");
			
			for(Movie m : moviesForCategoryThree) {
				
				ImageView view = new ImageView(new Image(new FileInputStream(new File(m.getPosterLocation()))));
				view.setFitHeight(240);
				view.setFitWidth(162);
				view.setId("image");
				listThree.getItems().add(view);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateListFour() {
		
		try {
			moviesForCategoryFour = DatabaseUtils.getByGenre("Action & Adventure");
			
			for(Movie m : moviesForCategoryFour) {
				
				ImageView view = new ImageView(new Image(new FileInputStream(new File(m.getPosterLocation()))));
				view.setFitHeight(240);
				view.setFitWidth(162);
				view.setId("image");
				listFour.getItems().add(view);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void listOneMoviePressed() {
		
		MoviePreviewViewController.initialize(moviesForCategoryOne.get(listOne.getSelectionModel().getSelectedIndex()));
	}
	
	@FXML
	private void listTwoMoviePressed() {
		
		MoviePreviewViewController.initialize(moviesForCategoryTwo.get(listTwo.getSelectionModel().getSelectedIndex()));
	}
	
	@FXML
	private void listThreeMoviePressed() {
		
		MoviePreviewViewController.initialize(moviesForCategoryThree.get(listThree.getSelectionModel().getSelectedIndex()));
	}
	
	@FXML
	private void listFourMoviePressed() {
		
		MoviePreviewViewController.initialize(moviesForCategoryFour.get(listFour.getSelectionModel().getSelectedIndex()));
	}
	
	public void slideSearchPane() {
		 
		FadeTransition ft = new FadeTransition(Duration.millis(500), searchPane);
	    ft.setAutoReverse(true);
	    ft.setFromValue(.01);
	    ft.setToValue(.98);
	    
	    TranslateTransition tt = new TranslateTransition(Duration.millis(500));
	    tt.setFromY(searchPane.getLayoutY());
	    tt.setToY(Main.getHeight() - 100);
	    
	    TranslateTransition tt2 = new TranslateTransition(Duration.millis(500), searchField);
	    tt2.setFromY(Main.getHeight() - 60);
	    tt2.setToY(-Main.getHeight() + searchField.getPrefHeight() + 60);
		ParallelTransition pt = new ParallelTransition(searchPane, ft, tt, tt2);
		pt.play();
		searchPaneIsOpen = true;
	}
	
	public void closeSearchPane() {
		
	    TranslateTransition tt = new TranslateTransition(Duration.millis(500));
	    tt.setFromY(Main.getHeight());
	    tt.setToY(-Main.getHeight() + 70);
	    
	    TranslateTransition tt2 = new TranslateTransition(Duration.millis(500), searchField);
	    tt2.setFromY(-Main.getHeight() + searchField.getPrefHeight() + 60);
	    tt2.setToY(Main.getHeight() - 60);

		ParallelTransition pt = new ParallelTransition(searchPane, tt, tt2);
		pt.play();
		searchPaneIsOpen = false;
	}
	
	public void populateReturnedMovies(ArrayList<Movie> movies) {
		
		int movieCount = 0;
		int row = 0;
		int column = 0;
		
		coordinates = new GridPaneCoordinates();
		
		if(movies == null)
			return;
		
		while(movieCount < movies.size()) {
			
			try {
				ImageView view = null;
				if(column < 7) {
						view = new ImageView(new Image(new FileInputStream(new File(movies.get(movieCount).getPosterLocation()))));
						view.setFitHeight(240);
						view.setFitWidth(162);
						view.setId("image");
						GridPane.setConstraints(view, column, row);
						returnedMovies.getChildren().add(view);
						returnedMovies.setMargin(view, new Insets(8));
						view.setOnMouseClicked(new EventHandler<Event>() {

							@Override
							public void handle(Event event) {
						        Node source = (Node)event.getSource() ;
						        coordinates.setColumnIndex(GridPane.getColumnIndex(source));
						        coordinates.setRowIndex(GridPane.getRowIndex(source));	
						        MoviePreviewViewController.initialize(movies.get(coordinates.getColumnIndex() + (coordinates.getRowIndex() * 7)));
							}
						});
						++column;
				}
				else {
					++row;
					column = 0;
					view = new ImageView(new Image(new FileInputStream(new File(movies.get(movieCount).getPosterLocation()))));
					view.setId("image");
					view.setFitHeight(240);
					view.setFitWidth(162);
					GridPane.setConstraints(view, column, row);
					returnedMovies.getChildren().add(view);
					returnedMovies.setMargin(view, new Insets(8));
					view.setOnMouseClicked(new EventHandler<Event>() {

						@Override
						public void handle(Event event) {
					        Node source = (Node)event.getSource();
					        coordinates.setColumnIndex(GridPane.getColumnIndex(source));
					        coordinates.setRowIndex(GridPane.getRowIndex(source));
					        MoviePreviewViewController.initialize(movies.get(coordinates.getColumnIndex() + (coordinates.getRowIndex() * 7)));
						}
					});
					++column;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			++movieCount;
		}
	}
}
