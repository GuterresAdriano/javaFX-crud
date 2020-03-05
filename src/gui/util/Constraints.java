package gui.util;

import javafx.scene.control.TextField;

public class Constraints {
	public static void limitTextFieldInteger(TextField field) {
		field.textProperty().addListener((observable, oldValue, newValue) -> {
			field.setText(newValue != null && !newValue.matches("\\d*")? oldValue: newValue);
		});		
	}
	
	public static void limitTextFieldMaxLength(TextField field, int max) {
		field.textProperty().addListener((observable, oldValue, newValue) -> {
			field.setText(newValue != null && newValue.length() > max? oldValue: newValue);
		});		
	}
	
	public static void limitTextFieldDouble(TextField field) {
		field.textProperty().addListener((observable, oldValue, newValue) -> {
			field.setText(newValue != null && !newValue.matches("\\d*([\\.]\\d*)")? oldValue: newValue);
		});		
	}

}
