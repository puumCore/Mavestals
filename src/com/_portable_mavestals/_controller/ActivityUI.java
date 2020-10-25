package com._portable_mavestals._controller;

import com._portable_mavestals._object.Activity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public class ActivityUI implements Initializable {

    protected static Activity activity;

    @FXML
    private Label timeLbl;

    @FXML
    private Label activityNameLbl;

    @FXML
    private Label activityInfoLbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Activity myActivity = activity;
        timeLbl.setText(strip_hour_and_minute(myActivity.getTime()));
        activityNameLbl.setText(myActivity.getAction());
        activityInfoLbl.setText(myActivity.getDescription());
    }

    public final @NotNull String strip_hour_and_minute(@NotNull String time) {
        return (time.split(":"))[0].concat(" : ").concat((time.split(":"))[1]);
    }

}
