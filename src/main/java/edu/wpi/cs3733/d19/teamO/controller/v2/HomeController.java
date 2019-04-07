package edu.wpi.cs3733.d19.teamO.controller.v2;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;

import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "Home.fxml")
public class HomeController extends Controller {

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

  Database databse;

  @FXML
  void navigationOnAction(){

  }

  @FXML
  void requestOnAction(){
    eventBus.post(new ChangeMainViewEvent(RequestController.class
        .getResource(RequestController.class.getAnnotation(FxmlController.class).url())));
  }

  @FXML
  void scheduleOnAction(){

  }

  @FXML
  void securityOnAction(){
    if(securityDialog("Security", "Security will be called. Are you sure?", null)) {
      //      Node node = new Node();
      //      database.insertSecurityRequest(new SecurityRequest(LocalDateTime.now(), node));
    }
  }
}
