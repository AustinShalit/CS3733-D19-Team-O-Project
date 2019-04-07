package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class Controller {

  public Controller() {
  }

  /**
   * Try to switch scenes to the fxml file. Enforces minimum window size.
   *
   * @param fxmlFile   String representing the fxml file to switch scenes to
   * @param currWindow The current window
   */
  protected void switchScenes(String fxmlFile, Window currWindow) {
    Stage stage = (Stage) currWindow.getScene().getWindow();
    Parent root = null;
    try {
      root = FXMLLoader.load(getClass().getResource(fxmlFile));
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    Scene scene = new Scene(root);
    stage.setScene(scene);

    // Window size and position
    minWindowSize(stage);
    setWindowSize(stage, currWindow.getWidth(), currWindow.getHeight());
    setWindowPosition(stage, currWindow.getX(), currWindow.getY());

    stage.hide();
    stage.show();
  }

  /**
   * Show an information alert, and wait for user to close it.
   */
  protected void showInformationAlert(String titleText, String contentText) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titleText);
    alert.setHeaderText(null);
    alert.setContentText(contentText);

    alert.showAndWait();
  }

  /**
   * Show an error alert, and wait for user to close it.
   */
  protected void showErrorAlert(String titleText, String headerText) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(titleText);
    alert.setHeaderText(headerText);
    alert.setContentText(null);

    alert.showAndWait();
  }

  /**
   * Show a dialog that asks a user a prompt. Reurns the user input as a String, or an empty
   * string if no input.
   */
  protected String textInputDialog(String titleText, String headerText, String promptText) {
    TextInputDialog dialog = new TextInputDialog(null);
    dialog.setTitle(titleText);
    dialog.setHeaderText(headerText);
    dialog.setContentText(promptText);

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      return result.get();
    }
    return "";
  }

  /**
   * Returns true if the user presses OK, false otherwise.
   */
  protected boolean securityDialog(String title, String header, String content) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){
      return true;
    } else {
      return false;
    }
  }

  protected void populateComboBox(Database database, ComboBox<Node> comboBox) throws SQLException {
    comboBox.getItems().addAll(database.getAllNodes());
    Callback<ListView<Node>, ListCell<Node>> cellFactory =
        new Callback<ListView<Node>, ListCell<Node>>() {
          @Override
          public ListCell<Node> call(ListView<Node> param) {
            return new ListCell<Node>() {

              @Override
              protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                  setGraphic(null);
                } else {
                  setText(item.getLongName());
                }
              }
            };
          }
        };
    comboBox.setCellFactory(cellFactory);
    comboBox.setButtonCell(cellFactory.call(null));
  }

  /**
   * The method bounces the label to the left and right rapidly.
   */
  void bounceTextAnimation(Label label) {
    KeyFrame init = new KeyFrame(Duration.ZERO, new KeyValue(label.translateXProperty(),
        0));

    KeyFrame left = new KeyFrame(Duration.seconds(.1), new KeyValue(label.translateXProperty(),
        -10));
    KeyFrame right = new KeyFrame(Duration.seconds(.1), new KeyValue(label.translateXProperty(),
        10));

    Timeline timelineLeft = new Timeline(init, left);
    timelineLeft.setCycleCount(2);

    Timeline timelineRight = new Timeline(init, right);
    timelineRight.setCycleCount(2);

    for (int i = 2; i < 12; i++) {
      if (i % 2 == 0) {
        timelineLeft.play();
      } else if (i % 3 == 0) {
        timelineRight.play();
      }
    }
  }

  /**
   * Given a stage, set its minimum size to prevent the user from scaling the window
   * too small.
   */
  public void minWindowSize(Stage stage) {
    stage.setMinWidth(1280);
    stage.setMinHeight(720);
  }

  /**
   * Set the size of the window given a width and height.
   */
  public void setWindowSize(Stage stage, double width, double height) {
    stage.setWidth(width);
    stage.setHeight(height);
  }

  /**
   * Set the position of the window.
   */
  public void setWindowPosition(Stage stage, double xpos, double ypos) {
    stage.setX(xpos);
    stage.setY(ypos);
  }
}

