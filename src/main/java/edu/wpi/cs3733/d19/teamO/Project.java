package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.controller.Controller;

public class Project extends Application {

  private final Controller controller = new Controller();

  private static final Logger logger
      = Logger.getLogger(Project.class.getName());

  private Parent root;

  @Override
  public void start(final Stage primaryStage) throws IOException {
    logger.config("Starting application");

    FXMLLoader loader = new FXMLLoader(getClass().getResource("controller/LoginWindow.fxml"));
    root = loader.load();

    primaryStage.setTitle("Team O Kiosk Application");

    primaryStage.setScene(new Scene(root));

    // Set original window size and position
    controller.minWindowSize(primaryStage);
    controller.setWindowSize(primaryStage, 1200, 900);
    controller.setWindowPosition(primaryStage, 0, 0);

    primaryStage.hide();
    primaryStage.show();
    logger.config("Startup complete");

    Thread.setDefaultUncaughtExceptionHandler(this::onThreadException);
  }

  /**
   * The version of this build.
   *
   * @return String representing the version
   */
  public static String getVersion() {
    String version = Project.class.getPackage().getImplementationVersion();
    if (version == null) {
      return "Development";
    }
    return version;
  }

  @SuppressWarnings("PMD.AvoidCatchingThrowable")
  private void onThreadException(Thread thread, Throwable throwable) {
    PlatformImpl.runAndWait(() -> {
      try {
        // Don't create more than one exception dialog at the same time
        final ExceptionAlert exceptionAlert = new ExceptionAlert(root, throwable,
            thread.getName() + ": " + throwable.getMessage(), getHostServices());
        exceptionAlert.setInitialFocus();
        exceptionAlert.showAndWait();
      } catch (Throwable ex) {
        // Well in this case something has gone very, very wrong
        // We don't want to create a feedback loop either.
        logger.log(Level.SEVERE, "Failed to show exception alert", throwable);
      }
    });
  }
}
