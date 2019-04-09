package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;
import java.sql.SQLException;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
@FxmlController(url = "MapEditMain.fxml")
public class MapEditController implements Controller {

  Database database = new Database();
  @FXML
  private GridPane root;
  @FXML
  private Button add;
  @FXML
  private Button delete;
  @FXML
  private Button connect;
  @FXML
  private Button update;
  @FXML
  private Button backButton;
  @FXML
  private Button cancel;
  private ComboBox<Node.NodeType> nodeType;
  @FXML
  private MapView map;
  @Inject
  private EventBus eventBus;


  public MapEditController() throws SQLException {
  }

  @FXML
  void initialize() throws IOException {
    if (map != null) {
      Image image = new Image(getClass().getResource("01_thefirstfloor.png").openStream());
      map.setMapImage(image);
      map.addEdgesToPane(database.getAllEdges());
    }
  }

  @FXML
  private void comboBox() {
    nodeType.getItems().addAll(Node.NodeType.values());
  }

  @FXML
  void mainButtonAction(ActionEvent event) throws IOException {
    Stage stage;
    Parent root;
    if (event.getSource() == add) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditAdd.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(add.getScene().getWindow());
      stage.showAndWait();
      map.addNodesToPane(database.getAllNodes());
    } else if (event.getSource() == delete) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditDelete.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(delete.getScene().getWindow());
      stage.showAndWait();
      map.clearNodes();
      map.addNodesToPane(database.getAllNodes());
    } else if (event.getSource() == connect) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditConnect.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(connect.getScene().getWindow());
      stage.showAndWait();
      map.clearEdges();
      map.addEdgesToPane(database.getAllEdges());
    } else if (event.getSource() == update) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditUpdate.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(update.getScene().getWindow());
      stage.showAndWait();
      map.clearNodes();
      map.addNodesToPane(database.getAllNodes());
    }
  }



  @FXML
  void cancelButtonAction() {
    Stage stage = (Stage) cancel.getScene().getWindow();
    stage.close();
  }


  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    MapEditController create();
  }



}
