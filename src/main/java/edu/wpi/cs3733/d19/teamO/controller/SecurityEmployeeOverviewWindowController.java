package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class SecurityEmployeeOverviewWindowController extends Controller {

  @FXML
  private Button assignButton;

  @FXML
  private Button completedButton;

  @FXML
  private TextField employeeNameField;

  @FXML
  private TableView<SecurityRequest> securityRequestTableView;

  @FXML
  private TableColumn<SecurityRequest, Integer> securityRequestTableColumn;

  @FXML
  private TableColumn<SecurityRequest, String> assignedEmployee;

  @FXML
  private Button backButton;

  @FXML
  void initialize() throws SQLException {
    final Database db = new Database();
    securityRequestTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    assignedEmployee.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));

    System.out.println(db.getAllSecurityRequests().size());
    securityRequestTableView.getItems().setAll(db.getAllSecurityRequests());
    System.out.println(db.getAllSecurityRequests());
  }

  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("SecurityWindow.fxml", backButton.getScene().getWindow());
    }
  }

  @FXML
  void onAssignButtonAction(ActionEvent event) throws SQLException {
    if (event.getSource() == assignButton) {
      TextInputDialog dialog = new TextInputDialog(null);
      dialog.setTitle(null);
      dialog.setHeaderText("Assign Security Request to Employee");
      dialog.setContentText("Enter Employee Name:");

      // Traditional way to get the response value.
      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()) {
        String string = result.get();
        Database db = new Database();
        SecurityRequest sr = db.getSecurityRequest(101).get(); // todo get id
        sr.setWhoCompleted(string);
        System.out.println(sr.getWhoCompleted());
        db.updateSecurity(sr);

        securityRequestTableView.getItems().removeAll();
        securityRequestTableView.getItems().setAll(db.getAllSecurityRequests());
      }
    }
  }


}
