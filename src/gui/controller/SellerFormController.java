package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.mysql.jdbc.StringUtils;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Tools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller seller;

	private SellerService sellerService;
	private DepartmentService departmentService;

	private List<DataChangeListener> changeListeners = new ArrayList<DataChangeListener>();
	private ObservableList<Department> obsListDepartment;

	@FXML
	private TextField idTextField;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField emailTextField;
	@FXML
	private DatePicker birthDateDatePicker;
	@FXML
	private TextField baseSalaryTextField;

	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButon;

	@FXML
	private Label errorNameLabel;
	@FXML
	private Label errorEmailLabel;
	@FXML
	private Label errorBirthDateLabel;
	@FXML
	private Label errorBaseSalaryLabel;
	@FXML
	private ComboBox<Department> departmentsComboBox;

	@FXML
	public void onSaveButtonAction(ActionEvent event) {
		if (seller == null) {
			throw new IllegalStateException("Seller was null!");
		}
		if (sellerService == null) {
			throw new IllegalStateException("Seller Service was null!");
		}
		try {
			this.seller = getFormData();
			this.sellerService.saveOrUpdat(seller);
			notifyDataChangeListeners();
			Tools.currentStage(event).close();

		} catch (DbException e) {
			Alerts.showAlert("Error saving seller", null, e.getMessage(), AlertType.ERROR);
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

	private Seller getFormData() {

		ValidationException exception = new ValidationException("Validation error");

		Seller seller = new Seller();
		seller.setId(Tools.tryParseToInt(idTextField.getText()));
		String name = nameTextField.getText();
		String email = emailTextField.getText();
		String baseSalary = baseSalaryTextField.getText();

		if (name == null || name.trim().equals("")) {
			exception.addErrors("name", "Field can't be empty");
		}
		if (email == null || email.trim().equals("")) {
			exception.addErrors("email", "Field can't be empty");
		}
		if (baseSalary == null || baseSalary.trim().equals("")) {
			exception.addErrors("base salarial", "Field can't be empty");
		}

		seller.setName(name);
		seller.setEmail(email);
		seller.setBaseSalary(!StringUtils.isEmptyOrWhitespaceOnly(baseSalary) ? Double.parseDouble(baseSalary) : 0);

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return seller;
	}

	@FXML
	public void onCancelButtonAction(ActionEvent event) {
		Tools.currentStage(event).close();
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public void setServices(SellerService sellerService, DepartmentService departmentService) {
		this.sellerService = sellerService;
		this.departmentService = departmentService;
	}

	public void updateFromData() {
		Locale.setDefault(Locale.US);
		if (seller == null) {
			throw new IllegalStateException("Seller is null.");
		}
		idTextField.setText(String.valueOf(seller.getId()));
		nameTextField.setText(seller.getName());
		emailTextField.setText(seller.getEmail());
		baseSalaryTextField.setText(String.format("%.2f", seller.getBaseSalary()));

		if (seller.getBirthDate() != null) {
			birthDateDatePicker
					.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if (seller.getDepartment() == null) {
			departmentsComboBox.getSelectionModel().selectFirst();
		}
		departmentsComboBox.setValue(seller.getDepartment());
	}

	@Override
	public void initialize(URL uri, ResourceBundle resourceBundle) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.limitTextFieldInteger(idTextField);
		Constraints.limitTextFieldMaxLength(nameTextField, 30);
		Constraints.limitTextFieldDouble(baseSalaryTextField);
		Constraints.limitTextFieldMaxLength(emailTextField, 50);
		Tools.formatDatePicker(birthDateDatePicker, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	private void setErrorMessageInLabel(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		HashSet<Label> errorLabels = new HashSet<Label>();
		errorLabels.add(errorBaseSalaryLabel);
		errorLabels.add(errorBirthDateLabel);
		errorLabels.add(errorEmailLabel);
		errorLabels.add(errorNameLabel);

		for (String x : fields) {
			errorNameLabel.setText(errors.get("name"));
		}
	}

	public void loadAssicietedObjects() {
		List<Department> list = 	departmentService.findAll();	
		if (departmentService == null) {
			throw new IllegalStateException("department Service was null");
		}

		FXCollections.observableArrayList(list);
		departmentsComboBox.setItems(obsListDepartment);
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		departmentsComboBox.setCellFactory(factory);
		departmentsComboBox.setButtonCell(factory.call(null));
	}

}
