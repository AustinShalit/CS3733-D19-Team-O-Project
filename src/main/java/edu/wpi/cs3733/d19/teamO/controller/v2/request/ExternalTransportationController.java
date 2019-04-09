package edu.wpi.cs3733.d19.teamO.controller.v2.request;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.Controller;
import edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.v2.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.v2.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.ExternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "ExternalTransportation.fxml")
public class ExternalTransportationController implements Controller {

  private static final Logger logger =
      Logger.getLogger(ExternalTransportationController.class.getName());

  @FXML
  private BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private JFXComboBox<Node> locationbox;
  @FXML
  private JFXComboBox<ExternalTransportationRequest.ExternalTransportationRequestType> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXButton backbtn;
  @FXML
  private Label reportLabel;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(db, locationbox);
    categorybox.getItems().setAll(ExternalTransportationRequest.ExternalTransportationRequestType
        .values());
  }

  @FXML
  void onbackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void onSubmitButtonAction() {
    ExternalTransportationRequest external = parseUserITRequest();
    if (external == null) {
      logger.log(Level.WARNING,
          "Unable to parse External transportation Request.",
          "Unable to parse External Transportation Request.");
      return;
    }

    if (db.insertExternalTransportationRequest(external)) {
      String message = "Successfully submitted External transportation request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit External transportation request.");
    }
  }
  
  @FXML
  void onReportAction(){
    int aSize = db.getExternalTransportationRequestsByCategory("AMBULANCE").size();
    int hSize = db.getExternalTransportationRequestsByCategory("HELICOPTER").size();
    int oSize = db.getExternalTransportationRequestsByCategory("OTHERS").size();
    reportLabel.setText("The number of requests for transporting by ambulance: " + aSize
        + "by helicopter: " + hSize
        + "by other transportation: " + oSize);
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
   */
  private ExternalTransportationRequest parseUserITRequest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (!descriptiontxt.getText().isEmpty()
        && !nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = (Node) locationbox.getValue();

      String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
      ExternalTransportationRequest.ExternalTransportationRequestType externalRequestType =
          ExternalTransportationRequest.ExternalTransportationRequestType.valueOf(type);

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new ExternalTransportationRequest(now, node, externalRequestType, description, name);
    }

    // otherwise, some input was invalid
    DialogHelper.showErrorAlert("Error.", "Please make sure all fields are filled out.");
    return null;
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    ExternalTransportationController create();
  }
}
