package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Seller;
import model.services.SellerService;

public class ListSellerController implements Initializable, DataChangeListener {

	private SellerService service;

	private ObservableList<Seller> obsList;

	@FXML
	private Button newButton;
	@FXML
	private TableView<Seller> sellerTableView;
	@FXML
	private TableColumn<Seller, Integer> idSellerTableColumn;
	@FXML
	private TableColumn<Seller, String> nameSellerTableColumn;	
	@FXML
	private TableColumn<Seller, String> emailSellerTableColumn;
	@FXML
	private TableColumn<Seller, Date> birthDateSellerTableColumn;
	@FXML
	private TableColumn<Seller, Double> baseSalarySellerTableColumn;
	
	@FXML
	private TableColumn<Seller, Seller> editSellerTableColumn;
	@FXML
	private TableColumn<Seller, Seller> removeSellerTableColumn;


	@FXML
	public void onNewButtonAction(ActionEvent event) {
		Stage parentStage = Tools.currentStage(event);
		Seller seller = new Seller();
		createDialogForm(seller, parentStage, "FormSeller");
	}

	@Override
	public void initialize(URL uri, ResourceBundle resourceBundle) {
		initializeNodes();
	}

	private void initializeNodes() {
		idSellerTableColumn.setCellValueFactory(new PropertyValueFactory<Seller, Integer>("id"));
		nameSellerTableColumn.setCellValueFactory(new PropertyValueFactory<Seller, String>("name"));
		emailSellerTableColumn.setCellValueFactory(new PropertyValueFactory<Seller, String>("email"));
		birthDateSellerTableColumn.setCellValueFactory(new PropertyValueFactory<Seller, Date>("birthDate"));
		Tools.formatTableColumnDate(birthDateSellerTableColumn, "dd/MM/yyyy");
		baseSalarySellerTableColumn.setCellValueFactory(new PropertyValueFactory<Seller, Double>("baseSalary"));
		Tools.formatTableColumnDouble(baseSalarySellerTableColumn, 2);
		
		

		Stage stage = (Stage) Main.getMainScene().getWindow();
		sellerTableView.prefHeightProperty().bind(stage.heightProperty());
	}

	public void setSellerService(SellerService sellerService) {
		this.service = sellerService;
	}

	public void updateTableView() {
		if(service== null) {
			throw new IllegalStateException("Service is null");
		}		
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		sellerTableView.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Seller seller, Stage parentStage, String absoluteName) {
//		try {
//
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/"+absoluteName+ ".fxml"));			
//			Pane pane = loader.load();
//
//			FormSellerController controller = loader.getController();
//			controller.setSeller(seller);
//			controller.setSellerService(new SellerService());
//
//			controller.subscribeDataChangeListener(this);
//
//			controller.updateFromData();			
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Enter Seller data");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);	
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();			
//
//		} catch (IOException e) {
//			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}	
	}

	private void initEditButtons() {
		editSellerTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		editSellerTableColumn.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(	event -> createDialogForm(obj, Tools.currentStage(event),"FormSeller"));
			}
		});
	}

	private void initRemoveButtons() {
		removeSellerTableColumn.setCellValueFactory(x-> new ReadOnlyObjectWrapper<>(x.getValue()));
		removeSellerTableColumn.setCellFactory(x-> new TableCell<Seller, Seller>(){
			private final Button button = new Button("delete");

			@Override
			protected void updateItem(Seller seller, boolean empty) { 
				super.updateItem(seller, empty);
				if (seller == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(	event -> removeSeller(seller));
			}
		});


	}

	protected Object removeSeller(Seller seller) {
		Optional<ButtonType> result = Alerts.showConfirmationAlert("Confirmation", "Are you sure to delete?");

		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}

			try {
				service.remove(seller);		
				updateTableView();
			} catch (Exception e) {
				Alerts.showAlert("Error removing seller", null, e.getMessage(), AlertType.ERROR);
			}			
		}	

		return null;
	}

	@Override
	public void onDataChanged() {
		updateTableView();		
	}

}
