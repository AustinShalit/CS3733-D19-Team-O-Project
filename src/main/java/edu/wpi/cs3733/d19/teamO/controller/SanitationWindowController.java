package edu.wpi.cs3733.d19.teamO.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.controller.exception.InvalidUserInputException;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class SanitationWindowController extends Controller {

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TextArea descriptionTextArea;
  @FXML
  private ComboBox locationComboBox;
  @FXML
  private ComboBox<SanitationRequest.SanitationRequestType> categoryComboBox;

  private Database db;

  @FXML
  void initialize() throws SQLException, IOException {
    db = new Database();
    //    Set<Node> nodes = db.getAllNodes();

    // read from file for now, until database is populated
    FileChooser chooser = new FileChooser();
    File file = chooser.showOpenDialog(new Stage());
    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
    List<Node> nodes = ncrw.readNodes(Files.newBufferedReader(file.toPath()));
    for (Node n : nodes) {
      locationComboBox.getItems().add(n.getNodeId());
      db.insertNode(n);
    }

    categoryComboBox.getItems().setAll(SanitationRequest.SanitationRequestType.values());
  }


  @FXML
  void onBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", backButton.getScene().getWindow());
  }


  @FXML
  void onSubmitButtonAction() {
    try {
      SanitationRequest sr = parseUserSanitationRequestTest();
      if (db.insertSanitationRequest(sr)) {
        String message = "Successfully submitted sanitation request.";
        showInformationAlert("Success!", message);
      } else {
        showErrorAlert("Error.", "Unable to submit sanitation request.");
      }
    } catch (InvalidUserInputException ex) {
      Logger logger = Logger.getLogger(SanitationWindowController.class.getName());
      logger.log(Level.WARNING, "Unable to parse Sanitation Request.", ex);
    }
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return A SanitationRequest representing the users input.
   */
  private SanitationRequest parseUserSanitationRequestTest() throws InvalidUserInputException {
    // if input is valid, parse it and return a new SanitationRequest
    if (!descriptionTextArea.getText().isEmpty()
        && Objects.nonNull(locationComboBox.getValue())
        && Objects.nonNull(categoryComboBox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      String nodeId = locationComboBox.getValue().toString();
      Node node = db.getNode(nodeId).get();

      String type = categoryComboBox.getValue().toString().toUpperCase(new Locale("EN"));
      SanitationRequest.SanitationRequestType srt =
          SanitationRequest.SanitationRequestType.valueOf(type);

      String description = descriptionTextArea.getText();

      return new SanitationRequest(now, node, srt, description);
    }

    // otherwise, some input was invalid
    showErrorAlert("Error.", "Please make sure all fields are filled out.");
    throw new InvalidUserInputException("Unable to parse Sanitation Request.");
  }

}
