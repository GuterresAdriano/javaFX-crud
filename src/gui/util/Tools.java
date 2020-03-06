package gui.util;

import com.mysql.jdbc.StringUtils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Tools {
	
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node)event.getSource()).getScene().getWindow();
	}
	
	public static Integer tryParseToInt(String number) {
		try {
			return Integer.parseInt(StringUtils.isNullOrEmpty(number)?"0":number );
		} catch (NumberFormatException e) {
			Alerts.showAlert("Error", "Parse number erro", e.getMessage(), AlertType.ERROR);
			return null;
		}		
	}

}
