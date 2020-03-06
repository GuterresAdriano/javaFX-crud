package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Tools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class ListDepartmentController implements Initializable {
	
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
	public void onNewButtonAction(ActionEvent event) {
		Stage parentStage = Tools.currentStage(event);
		Department department = new Department();
		createDialogForm(department, parentStage, "FormDepartment");
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
	
	private void createDialogForm(Department department, Stage parentStage, String absoluteName) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/"+absoluteName+ ".fxml"));			
			Pane pane = loader.load();
			
			FormDepartmentController controller = loader.getController();
			controller.setDepartment(department);
			controller.setDepartmentService(new DepartmentService());
			controller.updateFromData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);	
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();			
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}	
	}

}
