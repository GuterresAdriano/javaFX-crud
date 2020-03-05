package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable{

	@FXML
	private MenuItem sellerMenuItem;
	@FXML
	private MenuItem departmentMenuItem;
	@FXML
	private MenuItem aboutMenuItem;
	
	@FXML
	public void onSellerMenuItemAction() {
		System.out.println("Seller");
	}
	
	@FXML
	public void onDepartmentMenuItemAction() {
		System.out.println("Department");
	}
	
	@FXML
	public void onAboutMenuItemAction() {
		loadView("About");
	}
	
	private synchronized void  loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/"+absoluteName+ ".fxml"));			
			VBox newBox = loader.load();
			
			Scene  mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newBox);
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	

	@Override
	public void initialize(URL uri, ResourceBundle resourceBundle) {
		// TODO Auto-generated method stub
		
	}	
}
