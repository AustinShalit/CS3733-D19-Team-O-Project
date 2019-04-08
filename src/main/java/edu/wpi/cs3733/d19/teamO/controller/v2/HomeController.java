package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;

@FxmlController(url = "Home.fxml")
public class HomeController implements Controller {

  @FXML
  private VBox root;
  @FXML
  private JFXButton navigationButton;
  @FXML
  private JFXButton requestButton;
  @FXML
  private JFXButton scheduleButton;
  @FXML
  private JFXButton securityButton;
  @Inject
  private EventBus eventBus;
  @Inject
  private RequestController.Factory requestControllerFactory;
  @Inject
  private SchedulingController.Factory schedulingControllerFactory;


  @FXML
  void navigationOnAction() {

  }

  @FXML
  void requestOnAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void scheduleOnAction() {
    eventBus.post(new ChangeMainViewEvent(schedulingControllerFactory.create()));
  }

  @FXML
  void securityOnAction() {

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    HomeController create();
  }
}
