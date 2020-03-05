package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	private DepartmentService service;
	
	private ObservableList<Department> obsList;
	
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
		idDepartmentTableColumn.setCellValueFactory(new PropertyValueFactory<Department, Integer>("id"));
		nameDepartmentTableColumn.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		departmentTableView.prefHeightProperty().bind(stage.heightProperty());
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.service = departmentService;
	}
	
	public void updateTableView() {
		if(service== null) {
			throw new IllegalStateException("Service is null");
		}
		
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		departmentTableView.setItems(obsList);
	}

}
