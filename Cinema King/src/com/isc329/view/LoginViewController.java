package com.isc329.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LoginViewController {

	@FXML private Hyperlink createAccount;
	@FXML private Button signin;
	@FXML private TextField emailField;
	@FXML private TextField passwordField;
	@FXML private Label signinInfoIncorrect;
	@FXML private Label logo;
	
	private double logoYPos, emailYPos, passYPos, signinYPos, cAccountYPos;
	private double emailXPos, middleOfMain;
	
	BorderPane root = null;
	
	@FXML
	private void initialize() {
		
		Text text = new Text("CinemaKing");
		Text cAccount = new Text("Create an account!");
		
		try {
			final Font f = Font.loadFont(
				      new FileInputStream(new File("C:\\Users\\Andrew Sidorchuk\\Databases Workspace\\Cinema King\\graphite.ttf")), 120);
			text.setFont(f);
			logo.setFont(f);
			logo.setTextFill(Color.WHITE);
			logo.setStyle("-fx-text-weight: bold;");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		createAccount.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				displayCreateAccountView();
			}
		});
		
		
		logoYPos = Main.height - (Main.height * .84);
		emailYPos = logoYPos  + text.getLayoutBounds().getHeight() + 10;
		passYPos = emailYPos + emailField.getPrefHeight() + 5;
		signinYPos = passYPos + passwordField.getPrefHeight() + 20;
		cAccountYPos = signinYPos + signin.getPrefHeight() + 5;
		
		middleOfMain = Main.width / 2;
		emailXPos = middleOfMain - (emailField.getPrefWidth() / 2);
		
		logo.setLayoutX(middleOfMain - (text.getLayoutBounds().getWidth() / 2));
		logo.setLayoutY(logoYPos);
		
		emailField.setLayoutX(emailXPos);
		emailField.setLayoutY(emailYPos);
		
		passwordField.setLayoutX(emailXPos);
		passwordField.setLayoutY(passYPos);
		
		signin.setLayoutX(emailXPos);
		signin.setLayoutY(signinYPos);

		createAccount.setLayoutX(emailField.getLayoutX() + emailField.getPrefWidth() - cAccount.getLayoutBounds().getWidth());
		createAccount.setLayoutY(cAccountYPos);
	}
	
	private void displayCreateAccountView() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/CreateAccountView.fxml"));
			AnchorPane createAccountView = (AnchorPane) loader.load();
			createAccountView.setPrefSize(Main.width, Main.height);
			CreateAccountViewController controller = loader.getController();
			controller.setRoot(root);
			root.setCenter(createAccountView);
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void setRoot(BorderPane root) {
		this.root = root;
	}
	
	@FXML
	private void onSigninPressed() {
		String name = null;
		
		try {
			if(emailField.getText().compareTo("") != 0 && passwordField.getText().compareTo("") != 0)
				name = DatabaseUtils.attemptSigninUser(emailField.getText(), passwordField.getText());
				if(name == null)
					signinInfoIncorrect.setText("Either the username or password was incorrect!");
				else {
					signinInfoIncorrect.setText("");
					displayMainMovieDirectoryView();
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
}
