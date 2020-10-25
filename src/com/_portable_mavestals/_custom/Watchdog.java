package com._portable_mavestals._custom;

import animatefx.animation.Shake;
import com._portable_mavestals.Main;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public abstract class Watchdog {

    private final String pathToErrorFolder = Main.RESOURCE_PATH.getAbsolutePath() + "\\_watchDog\\_error\\";
    private final String pathToInfoFolder = Main.RESOURCE_PATH.getAbsolutePath() + "\\_watchDog\\_info\\";

    @NotNull
    public final Boolean i_am_sure_of_it(String nameOfAction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(Main.stage);
        alert.setTitle("The app requires your confirmation to continue...");
        alert.setHeaderText("Are you sure you want to ".concat(nameOfAction).concat(" ?"));
        alert.setContentText("This can not be undone!");
        ButtonType yesButtonType = new ButtonType("YES");
        ButtonType noButtonType = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().setAll(yesButtonType, noButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get().equals(yesButtonType);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public final Task<Object> stack_trace_printing(Exception exception) {
        return new Task<Object>() {
            @Override
            public Object call() {
                write_stack_trace(exception);
                return false;
            }
        };
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public final Task<Object> write_log(String string) {
        return new Task<Object>() {
            @Override
            public Object call() {
                activityLog(string);
                return false;
            }
        };
    }

    public final void activityLog(String message) {
        BufferedWriter bw = null;
        try {
            File log = new File(pathToInfoFolder.concat("\\Account for {" + new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime()).replaceAll("-", " ") + "}.txt"));
            if (!log.exists()) {
                if (log.createNewFile()) {
                    if (log.canWrite() & log.canRead()) {
                        FileWriter fw = new FileWriter(log, true);
                        bw = new BufferedWriter(fw);
                        bw.write("\nThis is a newly created file [ " + time_stamp() + " ].");
                    }
                }
            }
            if (log.canWrite() & log.canRead()) {
                FileWriter fw = new FileWriter(log, true);
                bw = new BufferedWriter(fw);
                bw.write(message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            programmer_error(ex).show();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception ex) {
                new Thread(stack_trace_printing(ex)).start();
                ex.printStackTrace();
                programmer_error(ex).show();
            }
        }
    }

    public final void write_stack_trace(Exception exception) {
        BufferedWriter bw = null;
        try {
            File log = new File(pathToErrorFolder.concat(gate_date_for_file_name().concat(" stackTrace_log.txt")));
            if (!log.exists()) {
                FileWriter fw = new FileWriter(log);
                fw.write("\nThis is a newly created file [ " + time_stamp() + " ].");
            }
            if (log.canWrite() & log.canRead()) {
                FileWriter fw = new FileWriter(log, true);
                bw = new BufferedWriter(fw);
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                exception.printStackTrace(printWriter);
                String exceptionText = stringWriter.toString();
                bw.write("\n ##################################################################################################"
                        + " \n " + time_stamp()
                        + "\n " + exceptionText
                        + "\n\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            programmer_error(ex).show();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                programmer_error(ex).show();
            }
        }
    }

    private @NotNull String gate_date_for_file_name() {
        return get_date().replaceAll("-", " ");
    }

    @NotNull
    public final String time_stamp() {
        return get_date().concat(" at ").concat(get_time());
    }

    public final @NotNull String get_date() {
        return new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
    }

    public final @NotNull String get_time() {
        return new SimpleDateFormat("HH:mm:ss:SSS").format(Calendar.getInstance().getTime());
    }

    public final void information_message(@NotNull String message) {
        try {
            SystemTray systemTray = SystemTray.getSystemTray();
            java.awt.image.BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/com/_portable_mavestals/_image/_icon/png_logo.png"));
            TrayIcon trayIcon = new TrayIcon(bufferedImage);
            trayIcon.setImageAutoSize(true);
            systemTray.add(trayIcon);
            trayIcon.displayMessage("Information", message, TrayIcon.MessageType.INFO);
        } catch (IOException | AWTException exception) {
            exception.printStackTrace();
            programmer_error(exception).show();
        }
    }

    protected final Notifications warning_message(String title, String text) {
        Image image = new Image("/com/_portable_mavestals/_image/_icon/warn.png");
        return Notifications.create()
                .title(title)
                .text(text)
                .graphic(new ImageView(image))
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_LEFT);
    }

    protected final Notifications empty_and_null_pointer_message(Node node) {
        Image image = new Image("/com/_portable_mavestals/_image/_icon/warn.png");
        return Notifications.create()
                .title("Something is Missing")
                .text("Click Here to trace this Error.")
                .graphic(new ImageView(image))
                .hideAfter(Duration.seconds(7))
                .position(Pos.TOP_LEFT)
                .onAction(event -> {
                    new Shake(node).play();
                    node.requestFocus();
                });
    }

    public final void success_notification(@NotNull String about) {
        try {
            SystemTray systemTray = SystemTray.getSystemTray();
            java.awt.image.BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/com/_portable_mavestals/_image/_icon/ok.png"));
            TrayIcon trayIcon = new TrayIcon(bufferedImage);
            trayIcon.setImageAutoSize(true);
            systemTray.add(trayIcon);
            trayIcon.displayMessage("Success", about, TrayIcon.MessageType.NONE);
        } catch (IOException | AWTException exception) {
            exception.printStackTrace();
            programmer_error(exception).show();
        }
    }

    public final Notifications error_message(String title, String text) {
        Image image = new Image("/com/_portable_mavestals/_image/_icon/error.png");
        return Notifications.create()
                .title(title)
                .text(text)
                .graphic(new ImageView(image))
                .hideAfter(Duration.seconds(8))
                .position(Pos.TOP_RIGHT);
    }

    @NotNull
    public final Alert programmer_error(@NotNull Object object) {
        Exception exception = (Exception) object;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(Main.stage);
        alert.setTitle("WATCH DOG");
        alert.setHeaderText("ERROR TYPE : " + exception.getClass());
        alert.setContentText("This dialog is a detailed explanation of the error that has occurred");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        String exceptionText = stringWriter.toString();
        Label label = new Label("The exception stacktrace was: ");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(label, 0, 0);
        gridPane.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(gridPane);
        return alert;
    }

}
