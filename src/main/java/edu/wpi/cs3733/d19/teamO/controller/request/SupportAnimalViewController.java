package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;

import animatefx.animation.Shake;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.SupportAnimalRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "SupportAnimalView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class SupportAnimalViewController implements Controller {

  @FXML
  public BorderPane root;
  @FXML
  private JFXButton assignButton;
  @FXML
  private JFXButton completedButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<SupportAnimalRequest> requestsTableView;
  @FXML
  private TableColumn<SupportAnimalRequest, Integer> idTableCol;
  @FXML
  private TableColumn<SupportAnimalRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<SupportAnimalRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<SupportAnimalRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<SupportAnimalRequest, String> locationTableCol;
  @FXML
  private TableColumn<SupportAnimalRequest,
      SupportAnimalRequest.SupportAnimalRequestType> categoryTableCol;
  @FXML
  private TableColumn<SupportAnimalRequest, String> descriptionCol;
  @FXML
  private TableColumn<SupportAnimalRequest, String> nameCol;

  @Inject
  private Database db;
  @Inject
  private SupportAnimalPopupController.Factory supportAnimalPopupFactory;

  private JFXPopup addPopup;

  @FXML
  void initialize() {
    idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    //timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompletedString"));
    whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNodeIdString"));
    categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("person"));

    requestsTableView.getItems().setAll(db.getAllSupportAnimalRequests());

    // sort by id
    requestsTableView.getSortOrder().add(idTableCol);

    // make columns auto-resize
    requestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : requestsTableView.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }

    // Initialize the Add Request Popup
    addPopup = new JFXPopup(supportAnimalPopupFactory.create().root);
    addPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
          requestsTableView.getItems().clear();
          requestsTableView.getItems().setAll(db.getAllSupportAnimalRequests());
        }
    );

    assignButton.setDisable(true);
    completedButton.setDisable(true);

    // Disable complete request and assign employee button if no request is selected
    requestsTableView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue == null) {
            assignButton.setDisable(true);
            completedButton.setDisable(true);
          } else {
            assignButton.setDisable(false);
            completedButton.setDisable(false);
          }
        }
    );
  }

  @FXML
  void onAddButtonAction() {
    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setBrightness(-0.2);
    root.setEffect(colorAdjust);
    addPopup.show(getRoot());
    addPopup.setX(
        (getRoot().getScene().getWidth() - addPopup.getWidth()) / 2
    );
    addPopup.setY(
        (getRoot().getScene().getHeight() - addPopup.getHeight()) / 2
    );
  }

  @FXML
  void onAssignButtonAction() {
    List<String> choices = new ArrayList<>();
    Set<Employee> emps = db.getAllEmployee();
    for (Employee employee : emps) {
      if (employee.getEmployeeAttributes().getCanFulfillSupportAnimal()) {
        System.out.println(employee.getType());
        choices.add(employee.getName());
      }
    }

    String name = DialogHelper.choiceInputDialog(choices, "Assign",
        "Assign a Support Animal Employee", "Select an Employee");

    SupportAnimalRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    selectedItem.setWhoCompleted(name);
    db.updateSupportAnimalRequest(selectedItem);

    requestsTableView.getItems().setAll(db.getAllSupportAnimalRequests());
    requestsTableView.getSortOrder().add(idTableCol); //sort
  }

  @FXML
  void onCompletedButtonAction() {
    if (requestsTableView.getSelectionModel().isEmpty()) {
      new Shake(completedButton).play();
      return;
    }

    SupportAnimalRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    requestsTableView.getItems().remove(selectedItem);

    db.deleteSupportAnimalRequest(selectedItem);

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SupportAnimalViewController create();
  }
}
