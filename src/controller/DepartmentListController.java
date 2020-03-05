package controller;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {
	
	@FXML
	private Button newButton;
	@FXML
	private TableView<Department> departmentTableView;
	@FXML
	private TableColumn<Department, Integer> idDepartmentTableColumn;
	@FXML
	private TableColumn<Department, String> nameDepartmentTableColumn;
	
	
	@FXML
	public void onNewButtonAction() {
		System.out.println("onNewButtonAction");
	}

	@Override
	public void initialize(URL uri, ResourceBundle resourceBundle) {
		initializeNodes();
	}

	private void initializeNodes() {
		idDepartmentTableColumn.setCellFactory(new PropertyValueFactory("ID"));
		nameDepartmentTableColumn.setCellFactory(new PropertyValueFactory("Name"));
		
//		Stage stage = (Stage) Main.getMainScene().getWindow();
//		System.out.println(stage.heightProperty());
//		departmentTableView.prefHeightProperty().bind(stage.heightProperty());
	}

}
