package com.isc329.view;

import java.io.IOException;
import java.sql.SQLException;

import com.isc329.DatabaseUtils;
import com.isc329.Main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class CreateAccountViewController {
	
	@FXML private TextField nameField;
	@FXML private TextField emailField;
	
	@FXML private PasswordField passwordField;
	@FXML private PasswordField passwordConfirmationField;
	@FXML private Label pleaseCreatePassword;
	@FXML private Label passwordsDoNotMatch;

	@FXML private Button createButton;
	
	@FXML private Hyperlink back;
	
	private String name, email, password, passwordConf;
	private double nameYPos, emailYPos, passYPos, pConfYPos, createYPos;
	private double fieldXPos;
	
	private BorderPane root = null;
	
	@FXML
	private void initialize() {
		name = null;
		email = null;
		password = null;
		
		nameYPos = Main.height - (Main.height * .84);
		emailYPos = nameYPos + nameField.getPrefHeight() + 5;
		passYPos = emailYPos + emailField.getPrefHeight() + 5;
		pConfYPos = passYPos + passwordField.getPrefHeight()  + 5;
		createYPos = pConfYPos + passwordConfirmationField.getPrefHeight() + 20;
		fieldXPos = Main.width / 2 - (nameField.getPrefWidth() / 2);
		
		nameField.setLayoutY(nameYPos);
		nameField.setLayoutX(fieldXPos);
		
		emailField.setLayoutY(emailYPos);
		emailField.setLayoutX(fieldXPos);
		
		passwordField.setLayoutY(passYPos);
		passwordField.setLayoutX(fieldXPos);
	
		passwordConfirmationField.setLayoutY(pConfYPos);
		passwordConfirmationField.setLayoutX(fieldXPos);
		
		createButton.setLayoutY(createYPos);
		createButton.setLayoutX(fieldXPos);
		
		back.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				displayLoginView();
			}
		});
		
		Font f = new Font("Arial", 20);
		back.setFont(f);
	}
	
	@FXML
	private void onMouseClick() {
		pleaseCreatePassword.setText("");
		passwordsDoNotMatch.setText("");
		
		if(nameField.getText().compareTo("") == 0) 
			nameField.setText("Please enter a name!");
		else
			name = nameField.getText();
		
		if(emailField.getText().compareTo("") == 0) 
			emailField.setText("Please enter an email!");
		else
			email = emailField.getText();
		
		if(passwordField.getText().compareTo("") == 0)
			pleaseCreatePassword.setText("Please create a password!");
		else {
			if(passwordField.getText().compareTo(passwordConfirmationField.getText()) != 0)
				passwordsDoNotMatch.setText("Passwords do not match!");
			else
				password = passwordField.getText();
		}
		
		if(name != null && email != null && password != null) {
			storeInDatabase(name, email, password);
			displayMainMovieDirectoryView();
		}
	}

	private void displayMainMovieDirectoryView() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MainMovieDirectoryView.fxml"));
			AnchorPane mainMovieDirectory = (AnchorPane) loader.load();
			mainMovieDirectory.setPrefSize(Main.width, Main.height);
			MainMovieDirectoryController controller = loader.getController();
			root.setCenter(mainMovieDirectory);
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void storeInDatabase(String name, String email, String password) {
		try {
			DatabaseUtils.createNewUser(name, email, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		clearFields();
	}
	
	private void clearFields() {
		nameField.setText("");
		emailField.setText("");
		passwordField.setText("");
		passwordConfirmationField.setText("");
	}
	
	private void displayLoginView() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LoginView.fxml"));
			AnchorPane loginView = (AnchorPane) loader.load();
			loginView.setPrefSize(Main.width, Main.height);
			LoginViewController controller = loader.getController();
			controller.setRoot(root);
			root.setCenter(loginView);
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void setRoot(BorderPane root) {
		this.root = root;
	}
}
