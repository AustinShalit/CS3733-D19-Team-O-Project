package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import edu.wpi.cs3733.d19.teamO.controller.exception.InvalidUserInputException;
import edu.wpi.cs3733.d19.teamO.entity.Login;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class LoginWindowController extends Controller {

  @FXML
  private JFXButton loginButton;
  @FXML
  private JFXTextField username;
  @FXML
  private JFXPasswordField password;
  @FXML
  private Label loginFail;

  private Database db;

  @FXML
  void initialize() {

  }

  @FXML
  void loginButtonAction() {
    try {
      // gets the user input
      Login login = parseUserLogin();
      Set<Login> info = db.getAllLogin();
      boolean check = false;

      // checks every Login info in set
      for (Login l : info) {
        if (l.equals(login)) {
          check = true;
        }
      }

      // TODO: User privilege check

      // if info typed was right, you go to main window screen
      if (check) {
        switchScenes("./v2/Main.fxml", loginButton.getScene().getWindow());
      } else {
        loginFail.setText("Incorrect username or password");
        bounceTextAnimation(loginFail);
      }
    } catch (InvalidUserInputException ex) {
      Logger logger = Logger.getLogger(SanitationWindowController.class.getName());
      logger.log(Level.WARNING, "Unable to parse Username and Password.", ex);
    }
  }

  private Login parseUserLogin() throws InvalidUserInputException {
    // checks if input is valid, parses it and returns a new Login
    if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
      return new Login(username.getText(), password.getText());
    }

    // otherwise
    loginFail.setText("Incorrect username or password");
    bounceTextAnimation(loginFail);

    throw new InvalidUserInputException("Unable to parse User Login info");
  }
}
