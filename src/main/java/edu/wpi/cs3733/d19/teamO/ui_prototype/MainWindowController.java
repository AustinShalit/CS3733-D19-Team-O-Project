package edu.wpi.cs3733.d19.teamO.ui_prototype;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainWindowController {

  @FXML
  private Button navigationButton;

  @FXML
  private Button mapToolButton;

  @FXML
  private Button sanitationRequestButton;

  @FXML
  private Button securityRequestButton;

  @FXML
  private Button schedulingButton;

  @FXML
  void navigationButtonAction() throws IOException {

  }

  @FXML
  void mapToolButtonAction() {

  }

  @FXML
  void sanitationRequestButtonAction(ActionEvent event) throws IOException {
    if (event.getSource() == sanitationRequestButton) {
      Stage stage = (Stage) sanitationRequestButton.getScene().getWindow();
      Parent root = FXMLLoader.load(getClass().getResource("SanitationWindow.fxml"));

      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
  }

  @FXML
  void securityRequestButtonAction() {

  }

  @FXML
  void schedulingButtonButtonAction() {

  }
}
