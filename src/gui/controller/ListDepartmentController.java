package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Tools;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class ListDepartmentController implements Initializable, DataChangeListener {

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
	private TableColumn<Department, Department> editDepartmentTableColumn;
	@FXML
	private TableColumn<Department, Department> removeDepartmentTableColumn;


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
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Department department, Stage parentStage, String absoluteName) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/"+absoluteName+ ".fxml"));			
			Pane pane = loader.load();

			FormDepartmentController controller = loader.getController();
			controller.setDepartment(department);
			controller.setDepartmentService(new DepartmentService());

			controller.subscribeDataChangeListener(this);

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

	private void initEditButtons() {
		editDepartmentTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		editDepartmentTableColumn.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");
			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(	event -> createDialogForm(obj, Tools.currentStage(event),"FormDepartment"));
			}
		});
	}

	private void initRemoveButtons() {
		removeDepartmentTableColumn.setCellValueFactory(x-> new ReadOnlyObjectWrapper<>(x.getValue()));
		removeDepartmentTableColumn.setCellFactory(x-> new TableCell<Department, Department>(){
			private final Button button = new Button("delete");

			@Override
			protected void updateItem(Department department, boolean empty) { 
				super.updateItem(department, empty);
				if (department == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(	event -> removeDepartment(department));
			}
		});


	}

	protected Object removeDepartment(Department department) {
		Optional<ButtonType> result = Alerts.showConfirmationAlert("Confirmation", "Are you sure to delete?");

		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}

			try {
				service.remove(department);		
				updateTableView();
			} catch (Exception e) {
				Alerts.showAlert("Error removing department", null, e.getMessage(), AlertType.ERROR);
			}			
		}	

		return null;
	}

	@Override
	public void onDataChanged() {
		updateTableView();		
	}

}
