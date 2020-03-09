package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;
import model.services.SellerService;

public class MainController implements Initializable{

	@FXML
	private MenuItem sellerMenuItem;
	@FXML
	private MenuItem departmentMenuItem;
	@FXML
	private MenuItem aboutMenuItem;
	
	@FXML
	public void onSellerMenuItemAction() {
		loadView("SellerList", (SellerListController sellerServCrtl)->{
			sellerServCrtl.setSellerService(new SellerService());
			sellerServCrtl.updateTableView();
		});
	}
	
	@FXML
	public void onDepartmentMenuItemAction() {
		loadView("DepartmentList", (DepartmentListController depServCrtl)->{
			depServCrtl.setDepartmentService(new DepartmentService());
			depServCrtl.updateTableView();
		});
	}
	
	@FXML
	public void onAboutMenuItemAction() {
		loadView("About", x-> {});
	}
	
	private  synchronized <T> void loadView(String viewName, Consumer<T> inicializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/"+viewName+ ".fxml"));			
			VBox newBox = loader.load();
			
			Scene  mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newBox);
			
			T controller = loader.getController();
			inicializingAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	

	@Override
	public void initialize(URL uri, ResourceBundle resourceBundle) {
		// TODO Auto-generated method stub
		
	}	
}
