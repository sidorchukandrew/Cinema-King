package com.isc329;
	
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.isc329.view.LoginViewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static BorderPane root;
	public static int width, height;
	private static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws SQLException {
		
		Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenDimensions.width - 5;
		height = screenDimensions.height - 100;
		
		try {
			primaryStage.setTitle("CinemaKing");
			primaryStage.getIcons().add(new Image(new FileInputStream(new File("C:\\Users\\Andrew Sidorchuk\\"
					+ "Databases Workspace\\Cinema King\\logo.png"))));	
	
			root = (BorderPane)FXMLLoader.load(getClass().getResource("view/RootView.fxml"));
			root.setPrefWidth(width);
			root.setPrefHeight(height);
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			displayLoginView();
			primaryStage.setScene(scene);
			primaryStage.show();
			
			this.primaryStage = primaryStage;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void displayLoginView() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LoginView.fxml"));
			AnchorPane loginView = (AnchorPane) loader.load();
			loginView.setPrefSize(width, height);
			LoginViewController controller = loader.getController();
			controller.setRoot(root);
			root.setCenter(loginView);
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static double getXPos() {
		return primaryStage.getX();
	}
	
	public static double getYPos() {
		return primaryStage.getY();
	}
	
	public static double getWidth() {
		return primaryStage.getWidth();
	}
	
	public static double getHeight() {
		return primaryStage.getHeight();
	}
	
	public static Stage getStage() {
		return primaryStage;
	}
	
}
