package com._mavestals._controller;

import animatefx.animation.SlideInRight;
import com._mavestals._custom.Watchdog;
import com._mavestals._object.Activity;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public class HistoryUI extends Watchdog implements Initializable {

    protected static String date = null;

    @FXML
    private Label dateLbl;

    @FXML
    private VBox activitiesBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final String myDate = date;
        dateLbl.setText(date);
        final List<Activity> activities = Controller.myActivityHashMap.get(myDate);
        for (Activity activity : activities) {
            try {
                ActivityUI.activity = activity;
                final Node node = FXMLLoader.load(getClass().getResource("/com/_mavestals/_fxml/activityUI.fxml"));
                Platform.runLater(() -> activitiesBox.getChildren().add(node));
                new SlideInRight(node).play();
                ActivityUI.activity = null;
            } catch (IOException e) {
                e.printStackTrace();
                new Thread(write_log("\n\n" + time_stamp() + ": The following Exception occurred,\n" + e)).start();
                new Thread(stack_trace_printing(e)).start();
                programmer_error(e).show();
            }
        }

    }
}
