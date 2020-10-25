package com._portable_mavestals;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Mandela
 * @version 1.0.0
 */
public class Main extends Application {

    /**
     * To work effectively a system variable named <strong>JAVAFX_DEV_APP_HOME</strong> should be created.
     * <code>
     * <strong>
     * public static final File RESOURCE_PATH = new File(System.getenv("JAVAFX_DEV_APP_HOME").concat("\\_mavestals_data"));
     * </strong>
     * </code>
     */

    public static final File RESOURCE_PATH = new File(System.getenv("JAVAFX_DEV_APP_HOME").concat("\\_mavestals_data"));
    public static Stage stage;
    private double xOffset, yOffset;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/_portable_mavestals/_fxml/sample.fxml"));
        Scene scene = new Scene(root);
        scene.setOnMousePressed(event2 -> {
            xOffset = event2.getSceneX();
            yOffset = event2.getSceneY();
        });
        scene.setOnMouseDragged(event1 -> {
            primaryStage.setX(event1.getScreenX() - xOffset);
            primaryStage.setY(event1.getScreenY() - yOffset);
        });
        scene.setFill(Color.web("#FFFFFF", 0));
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().addAll(
                new Image(getClass().getResource("/com/_portable_mavestals/_image/_icon/png_logo_x1.png").toExternalForm()),
                new Image(getClass().getResource("/com/_portable_mavestals/_image/_icon/png_logo_x2.png").toExternalForm()),
                new Image(getClass().getResource("/com/_portable_mavestals/_image/_icon/png_logo_x3.png").toExternalForm()),
                new Image(getClass().getResource("/com/_portable_mavestals/_image/_icon/png_logo_x4.png").toExternalForm())
        );
        primaryStage.setResizable(false);
        primaryStage.show();
        new FadeIn(root).play();
        stage = primaryStage;
    }

}
