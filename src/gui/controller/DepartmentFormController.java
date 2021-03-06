package gui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department department;	
	private DepartmentService departmentService;
	private List<DataChangeListener> changeListeners = new ArrayList<DataChangeListener>();

	@FXML
	private TextField idTextField;
	@FXML
	private TextField nameTextField;
	@FXML
	private Button    saveButton ;
	@FXML
	private Button    cancelButon;	
	@FXML
	private Label     errorNameLabel;	

	@FXML
	public void onSaveButtonAction(ActionEvent event) {
		if(department == null) {
			throw new IllegalStateException("Department was null!");
		}
		if(departmentService == null) {
			throw new IllegalStateException("Department Service was null!");
		}		
		try {
			this.department = getFormData();		
			this.departmentService.saveOrUpdat(department);
			notifyDataChangeListeners();
			Tools.currentStage(event).close();
			
		} catch (DbException e) {
			Alerts.showAlert("Error saving department", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessageInLabel(e.getErrors());
		}
		
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener x : changeListeners) {
			x.onDataChanged();
		}
		
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		this.changeListeners.add(listener);
	}
	
	private Department getFormData() {
		
		ValidationException exception = new ValidationException("Validation error");
		
		Department department = new Department();
		department.setId(Tools.tryParseToInt(idTextField.getText()));
		String name = nameTextField.getText();
		if (name == null || name.trim().equals("")) {
			exception.addErrors("name", "Field can't be empty");
		}
		department.setName(nameTextField.getText());
		
		if(exception.getErrors().size() > 0){
			throw exception;
		}
		return department;
	}


	@FXML
	public void onCancelButtonAction(ActionEvent event) {
		Tools.currentStage(event).close();
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	

	public void updateFromData() {
		if(department == null) {
			throw new IllegalStateException("Department is null.");

		}
		idTextField.setText(String.valueOf(department.getId()));	
		nameTextField.setText(department.getName());		
	}

	@Override
	public void initialize(URL uri, ResourceBundle resourceBundle) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.limitTextFieldInteger(idTextField);
		Constraints.limitTextFieldMaxLength(nameTextField, 30);
	}
	
	private void setErrorMessageInLabel(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			errorNameLabel.setText(errors.get("name"));
		}
		
	}


}
