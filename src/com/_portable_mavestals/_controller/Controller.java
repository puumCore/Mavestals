package com._portable_mavestals._controller;

import animatefx.animation.*;
import com._portable_mavestals.Main;
import com._portable_mavestals._custom.Brain;
import com._portable_mavestals._object.*;
import com._portable_mavestals._outsoured.BCrypt;
import com._portable_mavestals._outsoured.PasswordDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.UnaryOperator;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class Controller extends Brain {

    public static final String INPUT_STREAM_TO_NO_IMAGE = "/com/_portable_mavestals/_image/noImage.jpeg";
    protected static HashMap<String, List<Activity>> myActivityHashMap = new HashMap<>();
    private final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([1-9][0-9]*)?")) {
            return change;
        } else if ("-".equals(change.getText())) {
            if (change.getControlText().startsWith("-")) {
                change.setText("");
                change.setRange(0, 1);
                change.setCaretPosition(change.getCaretPosition() - 2);
                change.setAnchor(change.getAnchor() - 2);
            } else {
                change.setRange(0, 0);
            }
            return change;
        }
        return null;
    };
    private final StringConverter<Integer> converter = new IntegerStringConverter() {
        @Override
        public Integer fromString(@NotNull String string) {
            if (string.isEmpty()) {
                return 0;
            }
            return super.fromString(string);
        }
    };
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    private Tali onlyNewTali;
    private Tali modifiedTaliAwaitingUpdate;
    private String encodedUpdatedImage;
    private boolean donShowNotification;

    @FXML
    private StackPane phaseTwoPane;

    @FXML
    private Label currentDateLbl;

    @FXML
    private Label versionInfoLbl;

    @FXML
    private Label appBuildDateLbl;

    @FXML
    private HBox menuBox;

    @FXML
    private StackPane myAccountPane;

    @FXML
    private JFXTextField newUsernameTF1;

    @FXML
    private JFXTextField newPlainSiriTF1;

    @FXML
    private JFXPasswordField newSiriPF1;

    @FXML
    private JFXButton hideShowSiriBtnC;

    @FXML
    private JFXTextField confirmNewPlainSiriTF1;

    @FXML
    private JFXPasswordField confirmNewSiriPF1;

    @FXML
    private JFXButton hideShowSiriBtnD;

    @FXML
    private JFXTextField newFirstnameTF;

    @FXML
    private JFXTextField newSurnameTF;

    @FXML
    private JFXDatePicker newDateOfBirthDP;

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane historyPane;

    @FXML
    private VBox historyBox;

    @FXML
    private StackPane withdrawPane;

    @FXML
    private Label totalSavingsLbl;

    @FXML
    private JFXTextArea savingsPurposeTA;

    @FXML
    private JFXTextField savingsPurposeAmtTF;

    @FXML
    private StackPane taliPane;

    @FXML
    private StackPane editPane;

    @FXML
    private JFXTextField editPocketMoneyTF;

    @FXML
    private JFXTextField editAmountToSaveTF;

    @FXML
    private StackPane createPane;

    @FXML
    private JFXTextField pocketMoneyTF;

    @FXML
    private JFXTextField amountToSaveTF;

    @FXML
    private JFXButton forCreatedTali;

    @FXML
    private Label cashAmtOfCurrentTaliLbl;

    @FXML
    private Label mpesaAmtOfCurrentTaliLbl;

    @FXML
    private StackPane ledgerPane;

    @FXML
    private JFXTextArea purposeTA;

    @FXML
    private JFXTextField purposeAmtTF;

    @FXML
    private JFXButton cashLedger;

    @FXML
    private StackPane dashboardPane;

    @FXML
    private AreaChart<String, Double> ledgerAreaChart;

    @FXML
    private AreaChart<String, Double> taliAreaChart;

    @FXML
    private Label currentTaliBalanceLbl;

    @FXML
    private VBox accountMenuBox;

    @FXML
    private StackPane phaseOnePane;

    @FXML
    private StackPane exitAppPane;

    @FXML
    private StackPane successPane;

    @FXML
    private Label helloUserLbl;

    @FXML
    private Label progressStatusLbl;

    @FXML
    private JFXProgressBar workstationPrgBar;

    @FXML
    private StackPane mainPane;

    @FXML
    private StackPane signUpPane;

    @FXML
    private JFXTextField firstnameTF;

    @FXML
    private JFXTextField surnameTF;

    @FXML
    private JFXDatePicker dateOfBirthDP;

    @FXML
    private JFXTextField newUsernameTF;

    @FXML
    private JFXTextField newPlainSiriTF;

    @FXML
    private JFXPasswordField newSiriPF;

    @FXML
    private JFXButton hideShowSiriBtnA;

    @FXML
    private JFXTextField confirmNewPlainSiriTF;

    @FXML
    private JFXPasswordField confirmNewSiriPF;

    @FXML
    private JFXButton hideShowSiriBtnB;

    @FXML
    private StackPane loginPane;

    @FXML
    private StackPane paneTwo;

    @FXML
    private JFXTextField plainSiriTF;

    @FXML
    private JFXPasswordField siriPF;

    @FXML
    private JFXButton hideShowBtn;

    @FXML
    private StackPane paneOne;

    @FXML
    private JFXTextField usernameTF;

    @FXML
    private StackPane splashScreenPane;

    @FXML
    void edit_profile_picture(MouseEvent event) {
        try {
            final File imageFile = get_selected_image_file();
            if (imageFile == null) {
                if (Brain.currentAccount.getImage() == null) {
                    put_profile_pic(new Image(getClass().getResourceAsStream(INPUT_STREAM_TO_NO_IMAGE)));
                }
                return;
            }
            try {
                byte[] imageAsBytes = FileUtils.readFileToByteArray(imageFile);
                encodedUpdatedImage = Base64.getEncoder().encodeToString(imageAsBytes);
                put_profile_pic(new Image(new FileInputStream(imageFile)));
                write_log("Profile picture", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just selected their desired picture."));
            } catch (IOException exception) {
                write_log("Profile picture", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" got a system error."));
                exception.printStackTrace();
                new Thread(stack_trace_printing(exception)).start();
                Platform.runLater(() -> programmer_error(exception).show());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void back_from_exit_pane(ActionEvent event) {
        new FadeOut(exitAppPane).play();
        exitAppPane.toBack();
        if (!phaseTwoPane.isDisable()) {
            new FadeOut(phaseOnePane).play();
            phaseOnePane.setDisable(true);
            phaseTwoPane.toFront();
            new FadeIn(phaseTwoPane).setDelay(Duration.seconds(0.5)).play();
        }
        if (!phaseOnePane.isDisable()) {
            StackPane stackPane = (StackPane) exitAppPane.getParent();
            CopyOnWriteArrayList<Node> nodeCopyOnWriteArrayList = new CopyOnWriteArrayList<>(stackPane.getChildren());
            for (int i = 0, nodeCopyOnWriteArrayListSize = nodeCopyOnWriteArrayList.size(); i < nodeCopyOnWriteArrayListSize; i++) {
                Node node = nodeCopyOnWriteArrayList.get(i);
                if (node instanceof StackPane) {
                    if (i == 3) {
                        if (node.getOpacity() < 1) {
                            new FadeIn(node).setDelay(Duration.seconds(0.5)).play();
                        }
                    } else {
                        if (node.getOpacity() > 0) {
                            new FadeOut(node).play();
                        }
                    }
                }
            }
        }
    }

    @FXML
    void back_to_login(ActionEvent event) {
        new FadeOut(signUpPane).play();
        loginPane.toFront();
        new FadeIn(loginPane).setDelay(Duration.seconds(0.5)).play();
    }

    @FXML
    void request_to_close_application(ActionEvent event) {
        if (!phaseTwoPane.isDisable() && phaseTwoPane.getOpacity() > 0) {
            new FadeOut(phaseTwoPane).play();
            phaseOnePane.setDisable(false);
            phaseOnePane.toFront();
            new FadeIn(phaseOnePane).setDelay(Duration.seconds(0.5)).play();
            if (exitAppPane.getOpacity() < 1) {
                exitAppPane.toFront();
                new FadeIn(exitAppPane).setDelay(Duration.seconds(0.5)).play();
            }
            return;
        }
        if (!phaseOnePane.isDisable() && phaseOnePane.getOpacity() > 0) {
            StackPane stackPane = (StackPane) exitAppPane.getParent();
            CopyOnWriteArrayList<Node> copyOnWriteArrayList = new CopyOnWriteArrayList<>(stackPane.getChildren());
            for (Node node : copyOnWriteArrayList) {
                if (node.equals(exitAppPane)) {
                    if (exitAppPane.getOpacity() < 1) {
                        exitAppPane.toFront();
                        new FadeIn(exitAppPane).setDelay(Duration.seconds(0.5)).play();
                    }
                } else {
                    if (node.getOpacity() > 0) {
                        new FadeOut(node).play();
                    }
                }
            }
        }
    }

    @FXML
    void create_cash_partition_of_the_new_tali(ActionEvent event) {
        try {
            if (pocketMoneyTF.getText().equals("0") || pocketMoneyTF.getText().trim().length() == 0 || pocketMoneyTF.getText() == null) {
                empty_and_null_pointer_message(pocketMoneyTF.getParent()).show();
                return;
            }
            if (Double.parseDouble(pocketMoneyTF.getText()) < 0) {
                error_message("Unexpected amount of pocket money was spent!", "Ensure that the money is not below zero").show();
                return;
            }
            final double amountOfPocketMoneyAsCash = Double.parseDouble(pocketMoneyTF.getText());
            double amountToSaveFromThePocketMoney = Double.parseDouble(amountToSaveTF.getText());
            if (onlyNewTali == null) {
                onlyNewTali = new Tali();
            }
            onlyNewTali.setCashAmount((amountOfPocketMoneyAsCash - amountToSaveFromThePocketMoney));
            if (onlyNewTali.getSavings() != null) {
                amountToSaveFromThePocketMoney += onlyNewTali.getSavings();
            }
            onlyNewTali.setSavings(amountToSaveFromThePocketMoney);
            pocketMoneyTF.setText("");
            amountToSaveTF.setText("");
            information_message("Your wallet has a new cash partition and your savings has been auto incremented");
            write_log("Tali creation", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just partitioned their wallet by creating a cash section, if the previous tali balance was above Zero then it wil be automatically added to the new tali balance."));
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void create_mpesa_partition_of_the_new_tali(ActionEvent event) {
        try {
            if (pocketMoneyTF.getText().equals("0") || pocketMoneyTF.getText().trim().length() == 0 || pocketMoneyTF.getText() == null) {
                empty_and_null_pointer_message(pocketMoneyTF.getParent()).show();
                return;
            }
            if (Double.parseDouble(pocketMoneyTF.getText()) < 0) {
                error_message("Unexpected amount of pocket money was spent!", "Ensure that the money is not below zero").show();
                return;
            }
            final double amountOfPocketMoneyAsMpesa = Double.parseDouble(pocketMoneyTF.getText());
            double amountToSaveFromThePocketMoney = Double.parseDouble(amountToSaveTF.getText());
            if (onlyNewTali == null) {
                onlyNewTali = new Tali();
            }
            onlyNewTali.setMpesaAmount((amountOfPocketMoneyAsMpesa - amountToSaveFromThePocketMoney));
            if (onlyNewTali.getSavings() != null) {
                amountToSaveFromThePocketMoney += onlyNewTali.getSavings();
            }
            onlyNewTali.setSavings(amountToSaveFromThePocketMoney);
            pocketMoneyTF.setText("");
            amountToSaveTF.setText("");
            information_message("Your wallet has a new M-Pesa partition and your savings has been auto incremented");
            write_log("Tali creation", Brain.currentAccount.getUser().getFirstname().concat(" ")
                    .concat(Brain.currentAccount.getUser().getSurname()).concat(" just partitioned their wallet by creating a m-pesa section, if the previous tali balance was above Zero then it wil be automatically added to the new tali balance."));
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void create_new_ledger(ActionEvent event) {
        try {
            if (accountIsTotallyNew) {
                error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
                return;
            }
            if (purposeTA.getText().trim().length() == 0 || purposeTA.getText() == null) {
                empty_and_null_pointer_message(purposeTA.getParent()).show();
                return;
            }
            if (purposeAmtTF.getText().equals("0") || purposeAmtTF.getText().trim().length() == 0 || purposeAmtTF.getText() == null) {
                empty_and_null_pointer_message(purposeAmtTF.getParent()).show();
                return;
            }
            if (Double.parseDouble(purposeAmtTF.getText()) < 0) {
                error_message("Unexpected amount was spent!", "Ensure that the money is not below zero").show();
                return;
            }
            Tali myTali = get_active_tali();
            if (myTali == null) {
                error_message("Incomplete!", "No active tali was found").show();
                return;
            }
            final Ledger ledger = new Ledger();
            ledger.setDate(get_date());
            ledger.setTime(get_time());
            ledger.setPurpose(purposeTA.getText());
            ledger.setAmountSpent(Double.parseDouble(purposeAmtTF.getText().trim()));
            ledger.setPaidAsCash(event.getSource().equals(cashLedger));
            if (ledger.getPaidAsCash()) {
                if (myTali.getCashAmount() < ledger.getAmountSpent()) {
                    warning_message("Insufficient!", "Your Cash balance is low").show();
                    return;
                }
            } else {
                if (myTali.getMpesaAmount() < ledger.getAmountSpent()) {
                    warning_message("Insufficient!", "Your M-Pesa balance is low").show();
                    return;
                }
            }
            if (create_ledger(ledger)) {
                if (the_active_tali_has_one_ledger() || !the_active_tali_has_zero_ledgers()) {
                    plot_tali_and_expenditure_charts();
                }
                purposeTA.setText("");
                purposeAmtTF.setText("");
                success_notification("This expenditure has been indicated in your ledger.");
                write_log("Ledger creation", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just spent some money."));
                if (ledger.getPaidAsCash()) {
                    if (myTali.getCashAmount() >= ledger.getAmountSpent()) {
                        myTali.setCashAmount((myTali.getCashAmount() - ledger.getAmountSpent()));
                        if (update_active_tali(myTali)) {
                            Activity activity = new Activity();
                            activity.setDate(get_date());
                            activity.setTime(get_time());
                            activity.setAction("Ledger");
                            activity.setDescription("Ksh ".concat(String.format("%,.1f", ledger.getAmountSpent())).concat("  was spent from cash money."));
                            if (activity_not_created(activity)) {
                                error_message("Incomplete!", "Your history has not been updated").show();
                            }
                            success_notification("You are all set");
                            new Thread(update_workstation_display_on_account_details()).start();
                        } else {
                            error_message("Incomplete!", "Your current tali account has not been updated on this expenditure").show();
                        }
                    }
                } else {
                    if (myTali.getMpesaAmount() >= ledger.getAmountSpent()) {
                        myTali.setMpesaAmount((myTali.getMpesaAmount() - ledger.getAmountSpent()));
                        if (update_active_tali(myTali)) {
                            Activity activity = new Activity();
                            activity.setDate(get_date());
                            activity.setTime(get_time());
                            activity.setAction("Ledger");
                            activity.setDescription("Ksh ".concat(String.format("%,.1f", ledger.getAmountSpent())).concat("  was spent from M-Pesa money."));
                            if (activity_not_created(activity)) {
                                error_message("Incomplete!", "Your history has not been updated").show();
                            }
                            new Thread(update_workstation_display_on_account_details()).start();
                        } else {
                            error_message("Incomplete!", "Your current tali account has not been updated on this expenditure").show();
                        }
                    }
                }
            } else {
                error_message("Failed!", "The ledger was not updated, something went wrong somewhere.").show();
                write_log("Ledger creation", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" was not able to update their ledger."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void create_new_tali(ActionEvent event) {
        try {
            if (!i_am_sure_of_it("create a NEW TALI")) {
                return;
            }
            Tali tali = new Tali();
            Tali taliBeforeModification = null;
            if (!accountIsTotallyNew) {
                tali = get_active_tali();
                taliBeforeModification = new Gson().fromJson(tali.toString(), Tali.class);
                double previousTaliBalance = (tali.getCashAmount() + tali.getMpesaAmount());
                double previousSavingsAmount = tali.getSavings();
                onlyNewTali.setSavings((previousSavingsAmount + previousTaliBalance) + onlyNewTali.getSavings());
            }
            tali.setCashAmount(0.0);
            tali.setMpesaAmount(0.0);
            tali.setSavings(0.0);
            tali.setActive(false);
            onlyNewTali.setActive(true);
            if (create_tali(onlyNewTali)) {
                Activity activity = new Activity();
                activity.setDate(get_date());
                activity.setTime(get_time());
                activity.setAction("New Tali");
                activity.setDescription("The tali was created. The previous tali has its cash and mpesa money balances transferred to the new talis' savings pot");
                if (activity_not_created(activity)) {
                    write_log("Tali creation", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" was not able to create an a new tali."));
                    warning_message("Partially saved!", "This will not appear on History").show();
                }
                if (taliBeforeModification != null) {
                    if (!deactivate_previous_tali(taliBeforeModification, tali)) {
                        error_message("Incomplete!", "Your previous tali has not been updated").show();
                    }
                }
                onlyNewTali = null;
                pocketMoneyTF.setText("");
                amountToSaveTF.setText("");
                new Thread(update_workstation_display_on_account_details()).start();
                write_log("Tali creation", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just performed an activity."));
                success_notification("The final state of your wallet has created a new tali and the app has been updated.");
            } else {
                write_log("Tali creation", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" was not able to create a new tali."));
                error_message("Failed!", "The wallet could not be saved, please retry").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void edit_cash_partition_of_the_current_tali(ActionEvent event) {
        try {
            if (accountIsTotallyNew) {
                error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
                return;
            }
            Tali tali = get_active_tali();
            if ((editPocketMoneyTF.getText().equals("0") || editPocketMoneyTF.getText().trim().length() == 0 || editPocketMoneyTF.getText() == null)
                    && (editAmountToSaveTF.getText().equals("0") || editAmountToSaveTF.getText().trim().length() == 0 || editAmountToSaveTF.getText() == null)) {
                empty_and_null_pointer_message(editPocketMoneyTF.getParent()).show();
                empty_and_null_pointer_message(editAmountToSaveTF.getParent()).show();
                return;
            }
            final double amountOfPocketMoneyAsCash = Double.parseDouble(editPocketMoneyTF.getText());
            double amountToSaveFromThePocketMoney = Double.parseDouble(editAmountToSaveTF.getText());
            if (amountToSaveFromThePocketMoney > 0) {
                if (!i_am_sure_of_it("increase your savings by Ksh ".concat(String.format("%,.1f", amountToSaveFromThePocketMoney)))) {
                    return;
                }
            } else if (amountToSaveFromThePocketMoney < 0) {
                if (!i_am_sure_of_it("reduce your savings by Ksh ".concat(String.format("%,.1f", amountToSaveFromThePocketMoney)))) {
                    return;
                }
            }
            if (modifiedTaliAwaitingUpdate == null) {
                modifiedTaliAwaitingUpdate = new Tali();
            }
            if ((tali.getCashAmount() + amountOfPocketMoneyAsCash) >= 0) {
                modifiedTaliAwaitingUpdate.setCashAmount(amountOfPocketMoneyAsCash);
            } else {
                warning_message("Cash going below Zero!", "The amount you have provided will drop your Cash balance to Ksh "
                        .concat(String.format("%,.1f", (tali.getCashAmount() + amountOfPocketMoneyAsCash))).concat(". Please retry with a different and better amount")).show();
                modifiedTaliAwaitingUpdate.setCashAmount(null);
                return;
            }
            if (modifiedTaliAwaitingUpdate.getSavings() != null) {
                amountToSaveFromThePocketMoney += modifiedTaliAwaitingUpdate.getSavings();
            }
            if ((tali.getSavings() + amountToSaveFromThePocketMoney) < 0) {
                warning_message("Savings going below Zero!", "The amount you have provided will drop your Savings balance to Ksh "
                        .concat(String.format("%,.1f", (tali.getSavings() + amountToSaveFromThePocketMoney))).concat(". Please retry with a different and better amount")).show();
                modifiedTaliAwaitingUpdate.setSavings(null);
                return;
            }
            if (amountToSaveFromThePocketMoney != 0) {
                modifiedTaliAwaitingUpdate.setSavings(amountToSaveFromThePocketMoney);
            }
            editPocketMoneyTF.setText(null);
            editAmountToSaveTF.setText(null);
            write_log("Tali editing", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just edited the cash section of their current tali."));
            information_message("The cash partition of your wallet has been reconstructed and your savings has been auto incremented");
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void edit_current_tali(ActionEvent event) {
        try {
            if (accountIsTotallyNew) {
                error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
                return;
            }
            if (modifiedTaliAwaitingUpdate == null) {
                warning_message("Incomplete!", "when you edit your any partition of your wallet we will be able to update these settings").show();
                return;
            }
            if (!i_am_sure_of_it("edit the CURRENT TALI")) {
                return;
            }
            Tali tali = get_active_tali();
            Activity activity = new Activity();
            activity.setDate(get_date());
            activity.setTime(get_time());
            activity.setAction("Tali Modification");
            activity.setDescription("Something occurred while editing the current tali but can't identify");
            if (modifiedTaliAwaitingUpdate.getCashAmount() != null) {
                tali.setCashAmount(tali.getCashAmount() + modifiedTaliAwaitingUpdate.getCashAmount());
                activity.setDescription("Tali's Cash balance was adjusted by Ksh ".concat(String.format("%,.1f", modifiedTaliAwaitingUpdate.getCashAmount())));
            }
            if (modifiedTaliAwaitingUpdate.getMpesaAmount() != null) {
                tali.setMpesaAmount(tali.getMpesaAmount() + modifiedTaliAwaitingUpdate.getMpesaAmount());
                activity.setDescription("Tali's M-Pesa balance was adjusted by Ksh ".concat(String.format("%,.1f", modifiedTaliAwaitingUpdate.getMpesaAmount())));
            }
            if (modifiedTaliAwaitingUpdate.getSavings() != null) {
                tali.setSavings((tali.getSavings() + modifiedTaliAwaitingUpdate.getSavings()));
                activity.setDescription("Tali's Savings balance was adjusted Ksh ".concat(String.format("%,.1f", modifiedTaliAwaitingUpdate.getSavings())));
            }
            if (activity_not_created(activity)) {
                write_log("Tali editing", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" was not able to update their current tali."));
                error_message("Incomplete!", "The changes could not be saved, please retry").show();
            }
            if (update_active_tali(tali)) {
                modifiedTaliAwaitingUpdate = null;
                editPocketMoneyTF.setText(null);
                editAmountToSaveTF.setText(null);
                new Thread(update_workstation_display_on_account_details()).start();
                write_log("Tali editing", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just edited their current tali."));
                success_notification("The changes have been saved and the app has been updated.");
            } else {
                error_message("Incomplete!", "Your tali has not been updated").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void edit_mpesa_partition_of_the_current_tali(ActionEvent event) {
        try {
            if (accountIsTotallyNew) {
                error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
                return;
            }
            if ((editPocketMoneyTF.getText().equals("0") || editPocketMoneyTF.getText().trim().length() == 0 || editPocketMoneyTF.getText() == null)
                    && (editAmountToSaveTF.getText().equals("0") || editAmountToSaveTF.getText().trim().length() == 0 || editAmountToSaveTF.getText() == null)) {
                empty_and_null_pointer_message(editPocketMoneyTF.getParent()).show();
                empty_and_null_pointer_message(editAmountToSaveTF.getParent()).show();
                return;
            }
            Tali tali = get_active_tali();
            final double amountOfPocketMoneyAsMpesa = Double.parseDouble(editPocketMoneyTF.getText());
            double amountToSaveFromThePocketMoney = Double.parseDouble(editAmountToSaveTF.getText());
            if (amountToSaveFromThePocketMoney > 0) {
                if (!i_am_sure_of_it("increase your savings by Ksh ".concat(String.format("%,.1f", amountToSaveFromThePocketMoney)))) {
                    return;
                }
            } else if (amountToSaveFromThePocketMoney < 0) {
                if (!i_am_sure_of_it("reduce your savings by Ksh ".concat(String.format("%,.1f", amountToSaveFromThePocketMoney)))) {
                    return;
                }
            }
            if (modifiedTaliAwaitingUpdate == null) {
                modifiedTaliAwaitingUpdate = new Tali();
            }
            if (modifiedTaliAwaitingUpdate.getMpesaAmount() == null) {
                modifiedTaliAwaitingUpdate.setMpesaAmount(0.0);
            }
            if ((tali.getMpesaAmount() + amountOfPocketMoneyAsMpesa) < 0) {
                warning_message("M-Pesa going below Zero!", "The amount you have provided will drop your M-Pesa balance to Ksh "
                        .concat(String.format("%,.1f", (tali.getMpesaAmount() + amountOfPocketMoneyAsMpesa))).concat(". Please retry with a different and better amount")).show();
                modifiedTaliAwaitingUpdate.setMpesaAmount(null);
                return;
            }
            modifiedTaliAwaitingUpdate.setMpesaAmount(amountOfPocketMoneyAsMpesa);
            if (modifiedTaliAwaitingUpdate.getSavings() != null) {
                amountToSaveFromThePocketMoney += modifiedTaliAwaitingUpdate.getSavings();
            }
            if ((tali.getSavings() + amountToSaveFromThePocketMoney) < 0) {
                warning_message("Savings going below Zero!", "The amount you have provided will drop your Savings balance to Ksh "
                        .concat(String.format("%,.1f", (tali.getSavings() + amountToSaveFromThePocketMoney))).concat(". Please retry with a different and better amount")).show();
                modifiedTaliAwaitingUpdate.setSavings(null);
                return;
            }
            if (amountToSaveFromThePocketMoney != 0) {
                modifiedTaliAwaitingUpdate.setSavings(amountToSaveFromThePocketMoney);
            }
            editPocketMoneyTF.setText(null);
            editAmountToSaveTF.setText(null);
            write_log("Tali editing", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just edited the m-pesa section of their current tali."));
            information_message("The cash partition of your wallet has been reconstructed and your savings has been auto incremented");
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void exit_from_application(ActionEvent event) {
        write_log("Application", "Shutting down...");
        System.exit(0);
    }

    @FXML
    void get_started(ActionEvent event) {
        try {
            write_log("Application", "Starting up...");
            start_up();
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void hide_application(ActionEvent event) {
        Main.stage.setIconified(true);
        if (Brain.currentAccount == null) {
            return;
        }
        write_log("Application", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" minimized this application."));
    }

    @FXML
    void reset_tali(@NotNull ActionEvent event) {
        try {
            if (accountIsTotallyNew) {
                error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
                return;
            }
            final JFXButton jfxButton = (JFXButton) event.getSource();
            if (jfxButton.equals(forCreatedTali)) {
                if (onlyNewTali != null) {
                    onlyNewTali = null;
                    write_log("Tali editing", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just reset the tali ui for new info."));
                    information_message("The reset is complete");
                }
            } else {
                if (modifiedTaliAwaitingUpdate != null) {
                    modifiedTaliAwaitingUpdate = null;
                    write_log("Tali editing", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just reset the tali ui for new info."));
                    information_message("The reset is complete");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            hide_menu();
            if (encodedUpdatedImage != null) {
                if (!i_am_sure_of_it("ignore the recently selected picture")) {
                    update_account_details(new ActionEvent());
                }
            }
            if (i_am_sure_of_it("logout")) {
                write_log("Log out", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" is about to log out."));
                reset_ui();
                clear_for_next_user();
                new FadeOut(phaseTwoPane).play();
                phaseTwoPane.setDisable(true);
                phaseOnePane.setDisable(false);
                phaseOnePane.toFront();
                new FadeIn(phaseOnePane).setDelay(Duration.seconds(0.5)).play();
                if (splashScreenPane.getOpacity() < 1) {
                    splashScreenPane.toFront();
                    new FadeIn(splashScreenPane).setDelay(Duration.seconds(0.5)).play();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void remove_profile_picture(ActionEvent event) {
        try {
            final String imageEncoding = Brain.currentAccount.getImage();
            if (imageEncoding == null) {
                warning_message("Failed!", "No profile picture to delete was found").show();
                return;
            }
            if (i_am_sure_of_it("remove your profile picture")) {
                Brain.currentAccount.setImage(null);
                if (update_account()) {
                    put_profile_pic(new Image(getClass().getResourceAsStream(INPUT_STREAM_TO_NO_IMAGE)));
                    write_log("Profile picture", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just removed their profile picture."));
                    information_message("Profile picture has been deleted");
                } else {
                    Brain.currentAccount.setImage(imageEncoding);
                    write_log("Profile picture", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" was not able to remove their profile picture."));
                    error_message("Failed!", "Your image was not updated").show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void show_account_settings(ActionEvent event) {
        show_menu();
    }

    @FXML
    void show_my_account_pane(ActionEvent event) {
        try {
            hide_menu();
            display_selected_window(myAccountPane);
            for (Node node : menuBox.getChildren()) {
                if (node.getClass().equals(JFXButton.class)) {
                    JFXButton jfxButton = (JFXButton) node;
                    jfxButton.setStyle("-fx-background-radius: 0px;" +
                            "-fx-text-fill: #ababab;");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void show_create_pane(@NotNull ActionEvent event) {
        final JFXButton jfxButton = (JFXButton) event.getSource();
        highlight_selected_menu_button(jfxButton);
        display_selected_window_in_tali(createPane);
    }

    @FXML
    void show_dashboard(@NotNull ActionEvent event) {
        if (!donShowNotification) {
            if (accountIsTotallyNew) {
                error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
                return;
            }
        }
        final JFXButton jfxButton = (JFXButton) event.getSource();
        highlight_selected_menu_button(jfxButton);
        display_selected_window(dashboardPane);
    }

    @FXML
    void show_edit_pane(@NotNull ActionEvent event) {
        if (accountIsTotallyNew) {
            error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
            return;
        }
        final JFXButton jfxButton = (JFXButton) event.getSource();
        highlight_selected_menu_button(jfxButton);
        display_selected_window_in_tali(editPane);
    }

    @FXML
    void show_history(@NotNull ActionEvent event) {
        if (accountIsTotallyNew) {
            error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
            return;
        }
        final JFXButton jfxButton = (JFXButton) event.getSource();
        highlight_selected_menu_button(jfxButton);
        display_selected_window(historyPane);
    }

    @FXML
    void show_ledger(@NotNull ActionEvent event) {
        if (accountIsTotallyNew) {
            error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
            return;
        }
        final JFXButton jfxButton = (JFXButton) event.getSource();
        highlight_selected_menu_button(jfxButton);
        display_selected_window(ledgerPane);
    }

    @FXML
    void show_or_hide_password(@NotNull ActionEvent event) {
        final JFXButton jfxButton = (JFXButton) event.getSource();
        if (jfxButton.equals(hideShowBtn)) {
            show_or_hide_any_password(jfxButton, plainSiriTF, siriPF);
        } else if (jfxButton.equals(hideShowSiriBtnA)) {
            show_or_hide_any_password(jfxButton, newPlainSiriTF, newSiriPF);
        } else if (jfxButton.equals(hideShowSiriBtnB)) {
            show_or_hide_any_password(jfxButton, confirmNewPlainSiriTF, confirmNewSiriPF);
        } else if (jfxButton.equals(hideShowSiriBtnC)) {
            show_or_hide_any_password(jfxButton, newPlainSiriTF1, newSiriPF1);
        } else if (jfxButton.equals(hideShowSiriBtnD)) {
            show_or_hide_any_password(jfxButton, confirmNewPlainSiriTF1, confirmNewSiriPF1);
        }
    }

    @FXML
    void show_signUp_pane(ActionEvent event) {
        new FadeOut(loginPane).play();
        signUpPane.toFront();
        new FadeIn(signUpPane).setDelay(Duration.seconds(0.5)).play();
    }

    @FXML
    void show_tali(@NotNull ActionEvent event) {
        final JFXButton jfxButton = (JFXButton) event.getSource();
        highlight_selected_menu_button(jfxButton);
        display_selected_window(taliPane);
    }

    @FXML
    void show_where_the_user_can_enter_the_password(ActionEvent event) {
        if (there_are_no_accounts()) {
            error_message("Zero accounts found!", "Please Sign up to continue").show();
            return;
        }
        if (usernameTF.getText().trim().length() == 0 || usernameTF.getText() == null) {
            empty_and_null_pointer_message(usernameTF.getParent()).show();
            return;
        }
        new FadeOut(paneOne).play();
        paneTwo.toFront();
        new FadeIn(paneTwo).setDelay(Duration.seconds(0.5)).play();
    }

    @FXML
    void show_where_the_user_can_enter_the_username(ActionEvent event) {
        new FadeOut(paneTwo).play();
        paneOne.toFront();
        new FadeIn(paneOne).setDelay(Duration.seconds(0.5)).play();
    }

    @FXML
    void show_withdraw(@NotNull ActionEvent event) {
        if (accountIsTotallyNew) {
            error_message("Incomplete!", "Please CREATE a new tali first to continue").show();
            return;
        }
        final JFXButton jfxButton = (JFXButton) event.getSource();
        highlight_selected_menu_button(jfxButton);
        display_selected_window(withdrawPane);
    }

    @FXML
    void sign_up_new_user(ActionEvent event) {
        try {
            final String firstName, surname, dateOfBirth, username, password;
            if (firstnameTF.getText().trim().length() == 0 || firstnameTF.getText() == null) {
                empty_and_null_pointer_message(firstnameTF).show();
                return;
            }
            firstName = firstnameTF.getText().trim();
            if (surnameTF.getText().trim().length() == 0 || surnameTF.getText() == null) {
                empty_and_null_pointer_message(surnameTF).show();
                return;
            }
            surname = surnameTF.getText().trim();
            if (dateOfBirthDP.getValue().format(dateTimeFormatter).trim().length() == 0 || dateOfBirthDP.getValue().format(dateTimeFormatter).isEmpty()) {
                empty_and_null_pointer_message(dateOfBirthDP).show();
                return;
            }
            dateOfBirth = dateOfBirthDP.getValue().format(dateTimeFormatter);
            if (newUsernameTF.getText().trim().length() == 0 || newUsernameTF.getText() == null) {
                empty_and_null_pointer_message(newUsernameTF).show();
                return;
            }
            username = newUsernameTF.getText().trim();
            String newPassword_typeA = get_provided_password(hideShowSiriBtnA, newSiriPF, newPlainSiriTF);
            String newPassword_typeB = get_provided_password(hideShowSiriBtnB, confirmNewSiriPF, confirmNewPlainSiriTF);
            if (newPassword_typeA == null) {
                empty_and_null_pointer_message(newSiriPF.getParent().getParent()).show();
                return;
            }
            if (newPassword_typeB == null) {
                empty_and_null_pointer_message(confirmNewSiriPF.getParent().getParent()).show();
                return;
            }
            if (!newPassword_typeA.equals(newPassword_typeB)) {
                warning_message("Mismatch", "Your passwords are not the same").show();
                return;
            }
            password = newPassword_typeB;
            final User user = new User();
            user.setFirstname(firstName);
            user.setSurname(surname);
            user.setDateOfBirth(dateOfBirth);
            user.setPassword((BCrypt.hashpw(password, BCrypt.gensalt(14))));
            final Account accountToAdd = new Account();
            accountToAdd.setUsername(username);
            accountToAdd.setUser(user);
            if (username_is_taken(accountToAdd.getUsername())) {
                warning_message("Change required!", "The username you have provided is already taken, please enter another one").show();
                return;
            }
            if (has_created_a_new_account(accountToAdd)) {
                firstnameTF.setText("");
                surnameTF.setText("");
                dateOfBirthDP.setValue(LocalDate.of(2000, Month.JANUARY, 1));
                newSiriPF.setText("");
                newPlainSiriTF.setText("");
                confirmNewSiriPF.setText("");
                confirmNewPlainSiriTF.setText("");
                write_log("Account creation", "Attempt to create a new account succeeded");
                success_notification("Your account has been successfully created, you may now login.");
                new FadeOut(signUpPane).play();
                loginPane.toFront();
                new FadeIn(loginPane).setDelay(Duration.seconds(0.5)).play();
            } else {
                write_log("Account creation", "Attempt to create a new account failed");
                error_message("Failed!", "You account was not successfully saved, please retry...").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void switch_user_to_another(ActionEvent event) {
        try {
            hide_menu();
            if (encodedUpdatedImage != null) {
                if (!i_am_sure_of_it("ignore the recently selected picture")) {
                    update_account_details(new ActionEvent());
                }
            }
            if (i_am_sure_of_it("switch to another account")) {
                write_log("Logout", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just logged out."));
                reset_ui();
                clear_for_next_user();
                new FadeOut(phaseTwoPane).play();
                phaseTwoPane.setDisable(true);
                phaseOnePane.setDisable(false);
                phaseOnePane.toFront();
                new FadeIn(phaseOnePane).setDelay(Duration.seconds(0.5)).play();
                if (mainPane.getOpacity() < 1) {
                    mainPane.toFront();
                    new FadeIn(mainPane).setDelay(Duration.seconds(0.5)).play();
                    if (loginPane.getOpacity() < 1) {
                        loginPane.toFront();
                        new FadeIn(loginPane).setDelay(Duration.seconds(0.5)).play();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void update_account_details(ActionEvent event) {
        try {
            int zeroEditCount = 0;
            if (encodedUpdatedImage == null) {
                zeroEditCount++;
            }
            if (newFirstnameTF.getText().trim().equals(Brain.currentAccount.getUser().getFirstname())) {
                zeroEditCount++;
            }
            if (newSurnameTF.getText().trim().equals(Brain.currentAccount.getUser().getSurname())) {
                zeroEditCount++;
            }
            if (newDateOfBirthDP.getValue().format(dateTimeFormatter).equals(Brain.currentAccount.getUser().getDateOfBirth())) {
                zeroEditCount++;
            }
            if (newUsernameTF1.getText().trim().equals(Brain.currentAccount.getUsername())) {
                zeroEditCount++;
            }
            if (zeroEditCount == 5) {
                warning_message("Incomplete!", "No detail of your account was edited").show();
                return;
            }

            Account account = new Account();
            account.setUser(new User());
            if (encodedUpdatedImage != null) {
                account.setImage(encodedUpdatedImage);
            }
            if (!newFirstnameTF.getText().trim().equals(Brain.currentAccount.getUser().getFirstname())) {
                account.getUser().setFirstname(newFirstnameTF.getText().trim());
            }
            if (!newSurnameTF.getText().trim().equals(Brain.currentAccount.getUser().getSurname())) {
                account.getUser().setSurname(newSurnameTF.getText().trim());
            }
            if (!newDateOfBirthDP.getValue().format(dateTimeFormatter).equals(Brain.currentAccount.getUser().getDateOfBirth())) {
                account.getUser().setDateOfBirth(newDateOfBirthDP.getValue().format(dateTimeFormatter));
            }
            if (!newUsernameTF1.getText().trim().equals(Brain.currentAccount.getUsername())) {
                account.setUsername(newUsernameTF1.getText().trim());
            }
            final String passwordA = get_provided_password(hideShowSiriBtnC, newSiriPF1, newPlainSiriTF1);
            final String passwordB = get_provided_password(hideShowSiriBtnD, confirmNewSiriPF1, confirmNewPlainSiriTF1);
            if (passwordA != null && passwordB != null) {
                if (!passwordA.equals(passwordB)) {
                    warning_message("Mismatch", "Your passwords are not the same").show();
                    return;
                }
                if (BCrypt.checkpw(passwordA, Brain.currentAccount.getUser().getPassword())) {
                    warning_message("Confusion", "The password you have provided is your current one").show();
                    return;
                } else {
                    account.getUser().setPassword(BCrypt.hashpw(passwordA, BCrypt.gensalt(14)));
                }
            } else if (passwordA != null) {
                warning_message("Check new passwords", "Type your new password at least twice").show();
                return;
            } else if (passwordB != null) {
                warning_message("Check new passwords", "Type your new password at least twice").show();
                return;
            }

            PasswordDialog passwordDialog = new PasswordDialog();
            Optional<String> password_copy = passwordDialog.showAndWait();
            password_copy.ifPresent(password -> {
                if (password.isEmpty()) {
                    error_message("PASSWORD IS NULL", "Please enter a password to confirm the actions you request to be done.").show();
                    return;
                }
                if (BCrypt.checkpw(password_copy.get(), Brain.currentAccount.getUser().getPassword())) {
                    String[] previousAccData = new String[6];
                    if (account.getUsername() != null) {
                        previousAccData[0] = Brain.currentAccount.getUsername();
                        Brain.currentAccount.setUsername(account.getUsername());
                        update_account_username(previousAccData[0]);
                    }
                    if (account.getImage() != null) {
                        previousAccData[1] = Brain.currentAccount.getImage();
                        Brain.currentAccount.setImage(account.getImage());
                    }
                    if (account.getUser().getFirstname() != null) {
                        previousAccData[2] = Brain.currentAccount.getUser().getFirstname();
                        Brain.currentAccount.getUser().setFirstname(account.getUser().getFirstname());
                    }
                    if (account.getUser().getSurname() != null) {
                        previousAccData[3] = Brain.currentAccount.getUser().getSurname();
                        Brain.currentAccount.getUser().setSurname(account.getUser().getSurname());
                    }
                    if (account.getUser().getDateOfBirth() != null) {
                        previousAccData[4] = Brain.currentAccount.getUser().getDateOfBirth();
                        Brain.currentAccount.getUser().setDateOfBirth(account.getUser().getDateOfBirth());
                    }
                    if (account.getUser().getPassword() != null) {
                        previousAccData[5] = Brain.currentAccount.getUser().getPassword();
                        Brain.currentAccount.getUser().setPassword(account.getUser().getPassword());
                    }
                    if (update_account()) {
                        encodedUpdatedImage = null;
                        write_log("Account update", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just updated their account details."));
                        information_message("You are all set!");
                    } else {
                        if (previousAccData[0] != null) {
                            final String nameBeforeError = Brain.currentAccount.getUsername();
                            Brain.currentAccount.setUsername(previousAccData[0]);
                            update_account_username(nameBeforeError);
                        }
                        if (previousAccData[1] != null) {
                            Brain.currentAccount.setImage(previousAccData[1]);
                        }
                        if (previousAccData[2] != null) {
                            Brain.currentAccount.getUser().setFirstname(previousAccData[2]);
                        }
                        if (previousAccData[3] != null) {
                            Brain.currentAccount.getUser().setSurname(previousAccData[3]);
                        }
                        if (previousAccData[4] != null) {
                            Brain.currentAccount.getUser().setDateOfBirth(previousAccData[4]);
                        }
                        if (previousAccData[5] != null) {
                            Brain.currentAccount.getUser().setPassword(previousAccData[5]);
                        }
                        write_log("Account update", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" was not able to update their details."));
                        error_message("Failed!", "Your details have not been updated, please retry").show();
                    }
                    new Thread(update_workstation_display_on_account_details()).start();
                } else {
                    error_message("Failed!", "Invalid password, please retry").show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void withdraw_some_money_from_savings(ActionEvent event) {
        try {
            if (savingsPurposeTA.getText().trim().length() == 0 || savingsPurposeTA.getText() == null) {
                empty_and_null_pointer_message(savingsPurposeTA.getParent()).show();
                return;
            }
            if (savingsPurposeAmtTF.getText().trim().length() == 0 || savingsPurposeAmtTF.getText() == null) {
                empty_and_null_pointer_message(purposeAmtTF.getParent()).show();
                return;
            }
            if (Double.parseDouble(savingsPurposeAmtTF.getText()) <= 0) {
                error_message("Unexpected amount was spent!", "Ensure that the money is not below zero").show();
                return;
            }
            final double amountToWithdraw = Double.parseDouble(savingsPurposeAmtTF.getText());
            Tali tali = get_active_tali();
            if (tali == null) {
                error_message("Failed!", "Please create a tali to continue").show();
                return;
            }
            if (tali.getSavings() > 0) {
                if (amountToWithdraw <= tali.getSavings()) {
                    tali.setSavings((tali.getSavings() - amountToWithdraw));
                    Activity activity = new Activity();
                    activity.setDate(get_date());
                    activity.setTime(get_time());
                    activity.setAction("Withdrawal");
                    activity.setDescription("Ksh".concat(String.format("%,.1f", amountToWithdraw)).concat(" was withdrawn from your savings."));
                    if (activity_not_created(activity)) {
                        write_log("Withdraw money", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" was not able to withdraw from savings."));
                        error_message("Failed!", "The changes could not be saved, please retry").show();
                    }
                    if (update_active_tali(tali)) {
                        savingsPurposeTA.setText("");
                        savingsPurposeAmtTF.setText("");
                        new Thread(update_workstation_display_on_account_details()).start();
                        write_log("Withdraw money", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just withdrew some money."));
                        success_notification("You have withdrawn Ksh ".concat(String.format("%,.1f", amountToWithdraw)).concat(" and the app has been updated."));
                    } else {
                        error_message("Incomplete!", "Your tali has not been updated").show();
                    }
                } else {
                    write_log("Withdraw money", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" was trying to withdraw excess money."));
                    error_message("Failed!", "The amount you have requested is too high to withdraw, check your savings balance then retry").show();
                }
            } else {
                write_log("Withdraw money", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" attempted to withdraw but is quite low on savings."));
                error_message("Failed!", "It seems you don't have sufficient savings to continue.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    @FXML
    void verify_user_to_login(ActionEvent event) {
        try {
            final String password = get_provided_password(hideShowBtn, siriPF, plainSiriTF);
            if (password == null) {
                empty_and_null_pointer_message(siriPF.getParent().getParent()).show();
                return;
            }
            if (usernameTF.getText() == null) {
                empty_and_null_pointer_message(usernameTF.getParent().getParent()).show();
                return;
            }
            final String username = usernameTF.getText().trim();
            usernameTF.setText(null);
            Boolean usernameAndPasswordVerificationStatus = the_user_has_an_account(username, password);
            if (usernameAndPasswordVerificationStatus != null) {
                if (usernameAndPasswordVerificationStatus) {
                    write_log("Login", Brain.currentAccount.getUser().getFirstname().concat(" ").concat(Brain.currentAccount.getUser().getSurname()).concat(" just logged in."));
                    new FadeOut(mainPane).play();
                    successPane.toFront();
                    new FadeIn(successPane).setDelay(Duration.seconds(0.5)).play();
                    reset_login_screen();
                    workstationPrgBar.setProgress(0);
                    workstationPrgBar.progressProperty().unbind();
                    final Task<Object> objectTask = initiate_a_valid_login();
                    workstationPrgBar.progressProperty().bind(objectTask.progressProperty());
                    objectTask.setOnSucceeded(event1 -> {
                        new FadeOut(successPane).play();
                        workstationPrgBar.progressProperty().unbind();
                        new FadeOut(phaseOnePane).play();
                        phaseTwoPane.setDisable(false);
                        phaseTwoPane.toFront();
                        new FadeIn(phaseTwoPane).setDelay(Duration.seconds(0.7)).play();
                        phaseOnePane.setDisable(true);
                        if (accountIsTotallyNew) {
                            int index = 0;
                            for (Node node : menuBox.getChildren()) {
                                if (index == 2) {
                                    if (node.getClass().equals(JFXButton.class)) {
                                        final JFXButton jfxButton = (JFXButton) node;
                                        jfxButton.fire();
                                    }
                                }
                                ++index;
                            }
                        }
                    });
                    objectTask.setOnFailed(event1 -> {
                        workstationPrgBar.progressProperty().unbind();
                        error_message("Awww!", "Something went wrong while loading application.").show();
                    });
                    objectTask.exceptionProperty().addListener(((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            Exception e = (Exception) newValue;
                            e.printStackTrace();
                            programmer_error(e).show();
                            new Thread(stack_trace_printing(e)).start();
                        }
                    }));
                    Thread thread = new Thread(objectTask);
                    thread.setName("set up login for valid user");
                    thread.start();
                } else {
                    write_log("Login", "Someone entered a wrong username or password");
                    error_message("Failed!", "Your username or password is wrong").show();
                }
            } else {
                write_log("Login", "Someone entered a username that does not exist");
                error_message("Failed!", "Your username you provided does not exist").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
    }

    //  # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

    private void plot_tali_and_expenditure_charts() {
        plot_ledger_chart();
        plot_tali_chart();
    }

    private void plot_ledger_chart() {
        Task<XYChart.Series<String, Double>> ledger_details = get_ledger_details();
        ledger_details.setOnSucceeded(event1 -> {
            if (!ledgerAreaChart.getData().isEmpty()) {
                ledgerAreaChart.getData().clear();
            }
            ledgerAreaChart.getData().add(ledger_details.getValue());
        });

        Thread thread = new Thread(ledger_details);
        thread.setName("plot_ledger");
        thread.start();
    }

    private void plot_tali_chart() {
        Task<XYChart.Series<String, Double>> taliDetails = get_tali_details();
        taliDetails.setOnSucceeded(event1 -> {
            if (!taliAreaChart.getData().isEmpty()) {
                taliAreaChart.getData().clear();
            }
            taliAreaChart.getData().add(taliDetails.getValue());
        });

        Thread thread1 = new Thread(taliDetails);
        thread1.setName("plot_tali");
        thread1.start();
    }

    private void reset_ui() {
        if (!ledgerAreaChart.getData().isEmpty()) {
            ledgerAreaChart.getData().clear();
        }
        if (!taliAreaChart.getData().isEmpty()) {
            taliAreaChart.getData().clear();
        }

        put_profile_pic(new Image(getClass().getResourceAsStream(INPUT_STREAM_TO_NO_IMAGE)));
        int index = 0;
        for (Node node : menuBox.getChildren()) {
            if (index == 0) {
                if (node.getClass().equals(JFXButton.class)) {
                    final JFXButton jfxButton = (JFXButton) node;
                    donShowNotification = true;
                    jfxButton.fire();
                    break;
                }
            }
            ++index;
        }
        donShowNotification = false;
    }

    private @Nullable File get_selected_image_file() {
        try {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Profile pictures", "*.png", "*.jpg", "*.jpeg"));
            final File SOURCE_FILE = fileChooser.showOpenDialog(Main.stage);
            if (SOURCE_FILE == null) {
                error_message("No image selected", "Try again..").show();
            } else {
                return SOURCE_FILE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            programmer_error(e).show();
            new Thread(stack_trace_printing(e)).start();
        }
        return null;
    }

    private void put_profile_pic(Image image) {
        imageView.setImage(image);
        Rectangle rectangle = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        rectangle.setArcWidth(200);
        rectangle.setArcHeight(200);
        imageView.setClip(rectangle);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage writableImage = imageView.snapshot(parameters, null);
        imageView.setClip(null);
        imageView.setImage(writableImage);
    }

    private void clear_for_next_user() {
        Brain.currentAccount = null;
        modifiedTaliAwaitingUpdate = null;
        onlyNewTali = null;
        Controller.myActivityHashMap.clear();
        encodedUpdatedImage = null;
        donShowNotification = false;
    }

    private void hide_menu() {
        if (accountMenuBox.getOpacity() > 0) {
            accountMenuBox.toBack();
            new FadeOutRight(accountMenuBox).play();
        }
    }

    private void show_menu() {
        if (accountMenuBox.getOpacity() < 1) {
            accountMenuBox.toFront();
            new FadeInRight(accountMenuBox).play();
        } else {
            hide_menu();
        }
    }

    private @NotNull Task<Object> initiate_a_valid_login() {
        helloUserLbl.setText("Hello ".concat(Brain.currentAccount.getUser().getFirstname()).concat(","));
        final String[] appInfo = new String[]{
                "Please wait as i freshen up :)",
                "Please wait.",
                "Please wait..",
                "Please wait...",
                "Getting tali information.",
                "You are good to go!"};
        return new Task<Object>() {
            @Override
            protected Object call() {
                int steps = 0;
                while (steps <= 100) {
                    try {
                        updateProgress(steps, 100);
                        switch (steps) {
                            case 0:
                                show_build_and_version_info();
                                Platform.runLater(() -> progressStatusLbl.setText(appInfo[0]));
                                break;
                            case 20:
                                if (!accountIsTotallyNew || the_active_tali_has_zero_ledgers()) {
                                    plot_ledger_chart();
                                }
                                Platform.runLater(() -> progressStatusLbl.setText(appInfo[1]));
                                break;
                            case 40:
                                if (!accountIsTotallyNew || the_active_tali_has_zero_ledgers()) {
                                    plot_tali_chart();
                                }
                                Platform.runLater(() -> progressStatusLbl.setText(appInfo[2]));
                                break;
                            case 60:
                                new Thread(update_workstation_display_on_account_details()).start();
                                Platform.runLater(() -> progressStatusLbl.setText(appInfo[3]));
                                break;
                            case 80:
                                new Thread(get_current_date()).start();
                                make_a_textField_numeric(new JFXTextField[]{purposeAmtTF, pocketMoneyTF, amountToSaveTF, editPocketMoneyTF, editAmountToSaveTF, savingsPurposeAmtTF});
                                Platform.runLater(() -> progressStatusLbl.setText(appInfo[4]));
                                break;
                            case 90:
                                plot_tali_and_expenditure_charts();
                                Platform.runLater(() -> {
                                    hide_menu();
                                    progressStatusLbl.setText(appInfo[5]);
                                });
                        }
                        Thread.sleep(100);
                        steps++;
                    } catch (InterruptedException e) {
                        new Thread(stack_trace_printing(e)).start();
                        Platform.runLater(() -> programmer_error(e).show());
                        break;
                    }
                }
                return null;
            }
        };
    }

    @Contract(pure = true)
    private Task<XYChart.Series<String, Double>> get_ledger_details() {
        return new Task<XYChart.Series<String, Double>>() {
            @Override
            protected XYChart.Series<String, Double> call() {
                return ledger_history();
            }
        };
    }

    @Contract(pure = true)
    private Task<XYChart.Series<String, Double>> get_tali_details() {
        return new Task<XYChart.Series<String, Double>>() {
            @Override
            protected XYChart.Series<String, Double> call() {
                double taliBalance = get_balance_of_the_active_tali();
                XYChart.Series<String, Double> stringDoubleSeries1 = ledger_history();
                return tali_history(stringDoubleSeries1, taliBalance);
            }
        };
    }

    private Task<Object> update_workstation_display_on_account_details() {
        final double taliBalance = get_balance_of_the_active_tali();
        return new Task<Object>() {
            @Override
            protected Object call() {
                try {
                    File imageFile = null;
                    if (Brain.currentAccount.getImage() != null) {
                        byte[] decodedImage = Base64.getDecoder().decode(Brain.currentAccount.getImage());
                        imageFile = new File(FileUtils.getTempDirectoryPath().concat("\\_mavestals\\_dp\\").concat(RandomStringUtils.randomAlphabetic(10)).concat(".png"));
                        FileUtils.writeByteArrayToFile(imageFile, decodedImage);
                        imageFile.deleteOnExit();
                    }
                    File finalImageFile = imageFile;
                    Platform.runLater(() -> {
                        currentTaliBalanceLbl.setOnMouseClicked(event -> display_selected_window(taliPane));
                        currentTaliBalanceLbl.setText("Ksh ".concat(String.format("%,.1f", taliBalance)));
                        if (!accountIsTotallyNew) {
                            totalSavingsLbl.setText("Ksh ".concat(String.format("%,.1f", get_savings_balance())));
                            cashAmtOfCurrentTaliLbl.setText("Ksh ".concat(String.format("%,.1f", get_pocket_money_amount_from_current_tali(true))));
                            mpesaAmtOfCurrentTaliLbl.setText("Ksh ".concat(String.format("%,.1f", get_pocket_money_amount_from_current_tali(false))));
                            display_activities_of_the_active_user();
                        }
                        if (finalImageFile != null) {
                            try {
                                put_profile_pic(new Image(new FileInputStream(finalImageFile)));
                            } catch (FileNotFoundException e) {
                                put_profile_pic(new Image(getClass().getResourceAsStream(INPUT_STREAM_TO_NO_IMAGE)));
                            }
                        } else {
                            put_profile_pic(new Image(getClass().getResourceAsStream(INPUT_STREAM_TO_NO_IMAGE)));
                        }
                        newFirstnameTF.setText(Brain.currentAccount.getUser().getFirstname());
                        newSurnameTF.setText(Brain.currentAccount.getUser().getSurname());
                        newDateOfBirthDP.setValue(LocalDate.parse(Brain.currentAccount.getUser().getDateOfBirth(), dateTimeFormatter));
                        newUsernameTF1.setText(Brain.currentAccount.getUsername());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    new Thread(stack_trace_printing(e)).start();
                    programmer_error(e).show();
                }
                return null;
            }
        };
    }


    private XYChart.Series<String, Double> tali_history(XYChart.@NotNull Series<String, Double> ledgerSeries, double currentTaliBal) {
        final XYChart.Series<String, Double> stringDoubleSeries = new XYChart.Series<>();
        stringDoubleSeries.setName("Tali");
        final double[] originalTotalTali = {ledgerSeries.getData().stream().mapToDouble(XYChart.Data::getYValue).sum() + currentTaliBal};
        for (XYChart.Data<String, Double> xyStringDoubleData : ledgerSeries.getData()) {
            stringDoubleSeries.getData().add(new XYChart.Data<>(xyStringDoubleData.getXValue(), (originalTotalTali[0] -= xyStringDoubleData.getYValue())));
        }
        return stringDoubleSeries;
    }

    private XYChart.Series<String, Double> ledger_history() {
        final XYChart.Series<String, Double> stringDoubleSeries = new XYChart.Series<>();
        stringDoubleSeries.setName("Expenditure");
        final HashMap<String, Double> stringDoubleHashMap = get_expenditure_of_the_active_tali();
        stringDoubleHashMap.forEach((date, amountSpent) -> stringDoubleSeries.getData().add(new XYChart.Data<>(date, amountSpent)));
        return stringDoubleSeries;
    }

    private @NotNull HashMap<String, Double> get_expenditure_of_the_active_tali() {
        final HashMap<String, Double> stringDoubleHashMap = new HashMap<>();
        Tali tali = get_active_tali();
        if (tali != null) {
            if (tali.getLedgers() != null) {
                for (JsonElement jsonElement : tali.getLedgers()) {
                    Ledger ledger = new Gson().fromJson(jsonElement, Ledger.class);
                    if (stringDoubleHashMap.isEmpty()) {
                        stringDoubleHashMap.put(ledger.getDate(), ledger.getAmountSpent());
                    } else if (stringDoubleHashMap.containsKey(ledger.getDate())) {
                        double totalExpense = stringDoubleHashMap.get(ledger.getDate());
                        stringDoubleHashMap.replace(ledger.getDate(), (totalExpense + ledger.getAmountSpent()));
                    } else {
                        stringDoubleHashMap.put(ledger.getDate(), ledger.getAmountSpent());
                    }
                }
            }
        }
        return stringDoubleHashMap;
    }

    private void display_activities_of_the_active_user() {
        if (currentAccount != null) {
            final Map<String, List<Activity>> stringListTreeMap = new TreeMap<>();
            JsonArray jsonArray = currentAccount.getActivities();
            if (jsonArray == null) {
                return;
            }
            for (JsonElement jsonElement : jsonArray) {
                Activity activity = new Gson().fromJson(jsonElement, Activity.class);
                if (stringListTreeMap.isEmpty()) {
                    List<Activity> list = new ArrayList<>();
                    list.add(activity);
                    stringListTreeMap.put(activity.getDate(), list);
                } else if (stringListTreeMap.containsKey(activity.getDate())) {
                    List<Activity> activities = stringListTreeMap.get(activity.getDate());
                    activities.add(activity);
                    stringListTreeMap.replace(activity.getDate(), activities);
                } else {
                    List<Activity> list = new ArrayList<>();
                    list.add(activity);
                    stringListTreeMap.put(activity.getDate(), list);
                }
            }
            final Set<String> stringSet = stringListTreeMap.keySet();
            if (!myActivityHashMap.isEmpty()) {
                myActivityHashMap.clear();
            }
            myActivityHashMap.putAll(stringListTreeMap);
            if (historyBox.getChildren().size() > 0 || !historyBox.getChildren().isEmpty()) {
                ObservableList<Node> nodeObservableList = historyBox.getChildren();
                for (Node node : nodeObservableList) {
                    Platform.runLater(() -> {
                        VBox.clearConstraints(node);
                        historyBox.getChildren().remove(node);
                    });
                }
            }
            stringSet.forEach(date -> {
                HistoryUI.date = date;
                try {
                    final Node node = FXMLLoader.load(getClass().getResource("/com/_portable_mavestals/_fxml/historyUI.fxml"));
                    Platform.runLater(() -> historyBox.getChildren().add(node));
                    new SlideInRight(node).play();
                    HistoryUI.date = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    new Thread(stack_trace_printing(e)).start();
                    programmer_error(e).show();
                }
            });
        }
    }

    private double get_pocket_money_amount_from_current_tali(boolean checkCash) {
        if (Brain.currentAccount != null) {
            Tali tali = get_active_tali();
            if (tali == null) {
                return 0;
            }
            if (checkCash) {
                if (tali.getCashAmount() == null) {
                    tali.setCashAmount(0.0);
                    update_active_tali(tali);
                }
                return tali.getCashAmount();
            } else {
                if (tali.getMpesaAmount() == null) {
                    tali.setMpesaAmount(0.0);
                    update_active_tali(tali);
                }
                return tali.getMpesaAmount();
            }
        }
        return 0;
    }

    private double get_savings_balance() {
        Tali tali = get_active_tali();
        if (tali == null) {
            return 0;
        }
        if (tali.getSavings() == null) {
            tali.setSavings(0.0);
            update_active_tali(tali);
        }
        return tali.getSavings();
    }

    private double get_balance_of_the_active_tali() {
        Tali tali = get_active_tali();
        if (tali == null) {
            return 0;
        }
        if (tali.getCashAmount() == null) {
            tali.setCashAmount(0.0);
            update_active_tali(tali);
        }
        if (tali.getMpesaAmount() == null) {
            tali.setMpesaAmount(0.0);
            update_active_tali(tali);
        }
        return (tali.getCashAmount() + tali.getMpesaAmount());
    }

    private void make_a_textField_numeric(@NotNull JFXTextField[] jfxTextFields) {
        for (JFXTextField jfxTextField : jfxTextFields) {
            TextFormatter<Integer> textFormatter = new TextFormatter<>(converter, 0, integerFilter);
            jfxTextField.setTextFormatter(textFormatter);
        }
    }

    private @Nullable String get_provided_password(@NotNull JFXButton jfxButton, JFXPasswordField
            jfxPasswordField, JFXTextField jfxTextField) {
        String password;
        if (jfxButton.getText().startsWith("Show")) {
            if (jfxPasswordField.getText().trim().length() == 0 || jfxPasswordField.getText() == null) {
                return null;
            }
            password = jfxPasswordField.getText().trim();
            jfxPasswordField.setText(null);
        } else {
            if (jfxTextField.getText().trim().length() == 0 || jfxTextField.getText() == null) {
                return null;
            }
            password = jfxTextField.getText().trim();
            jfxTextField.setText(null);
        }
        return password;
    }

    private void show_or_hide_any_password(@NotNull final JFXButton jfxButton,
                                           final JFXTextField jfxTextField, final JFXPasswordField jfxPasswordField) {
        if (jfxButton.getText().startsWith("Show")) {
            if (jfxPasswordField.getText().trim().length() > 0 || jfxPasswordField.getText() != null) {
                jfxTextField.setText(jfxPasswordField.getText().trim());
            }
            new FadeOut(jfxPasswordField).play();
            jfxTextField.toFront();
            new FadeIn(jfxTextField).setDelay(Duration.seconds(0.5)).play();
            jfxButton.setText("Hide password");
        } else {
            if (jfxTextField.getText().trim().length() > 0 || jfxTextField.getText() != null) {
                jfxPasswordField.setText(jfxTextField.getText().trim());
            }
            new FadeOut(jfxTextField).play();
            jfxPasswordField.toFront();
            new FadeIn(jfxPasswordField).setDelay(Duration.seconds(0.5)).play();
            jfxButton.setText("Show password");
        }
    }

    private void display_selected_window_in_tali(StackPane requiredStackPane) {
        final StackPane[] stackPanes = new StackPane[]{createPane, editPane};
        for (StackPane stackPane : stackPanes) {
            if (stackPane.equals(requiredStackPane)) {
                if (stackPane.getOpacity() < 1) {
                    new FadeIn(stackPane).setDelay(Duration.seconds(0.5)).play();
                    stackPane.toFront();
                }
            } else {
                if (stackPane.getOpacity() > 0) {
                    new FadeOut(stackPane).play();
                }
            }
        }
    }

    private void display_selected_window(StackPane requiredStackPane) {
        final StackPane[] stackPanes = new StackPane[]{dashboardPane, ledgerPane, taliPane, withdrawPane, historyPane, myAccountPane};
        for (StackPane stackPane : stackPanes) {
            if (stackPane.equals(requiredStackPane)) {
                if (stackPane.getOpacity() < 1) {
                    new FadeIn(stackPane).setDelay(Duration.seconds(0.5)).play();
                    stackPane.toFront();
                }
            } else {
                if (stackPane.getOpacity() > 0) {
                    new FadeOut(stackPane).play();
                }
            }
        }
    }

    private void highlight_selected_menu_button(@NotNull JFXButton selectedButton) {
        ObservableList<Node> nodeObservableList;
        if (selectedButton.getParent().getClass().equals(HBox.class)) {
            HBox hBox = (HBox) selectedButton.getParent();
            nodeObservableList = hBox.getChildren();
        } else {
            VBox vBox = (VBox) selectedButton.getParent();
            nodeObservableList = vBox.getChildren();
        }
        for (Node node : nodeObservableList) {
            if (node.getClass().equals(JFXButton.class)) {
                JFXButton jfxButton = (JFXButton) node;
                if (jfxButton.equals(selectedButton)) {
                    jfxButton.setStyle("-fx-background-radius: 0px;" +
                            "-fx-text-fill: #000000;");
                } else {
                    jfxButton.setStyle("-fx-background-radius: 0px;" +
                            "-fx-text-fill: #ababab;");
                }
            }
        }
    }

    private void show_build_and_version_info() {
        final JsonObject jsonObject = get_app_version_and_info();
        if (jsonObject != null) {
            Platform.runLater(() -> {
                versionInfoLbl.setText(jsonObject.get("version").getAsString());
                appBuildDateLbl.setText(jsonObject.get("build").getAsString());
            });
        }
    }

    private @Nullable JsonObject get_app_version_and_info() {
        try {
            FileReader fileReader = new FileReader(new File(Main.RESOURCE_PATH.getAbsolutePath().concat("\\_config\\app_buid_info.properties")));
            Properties properties = new Properties();
            properties.load(fileReader);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("version", properties.getProperty("versionText"));
            jsonObject.addProperty("build", properties.getProperty("buildInfo"));
            fileReader.close();
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();

            new Thread(stack_trace_printing(e)).start();
            programmer_error(e).show();
        }
        return null;
    }

    @Contract(value = " -> new", pure = true)
    private @NotNull Task<Object> get_current_date() {
        return new Task<Object>() {
            @Override
            protected Object call() {
                long oneMinuteInMillSeconds = (long) Duration.minutes(1).toMillis();
                while (true) {
                    try {
                        Platform.runLater(() -> currentDateLbl.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE")).concat(", ")
                                .concat(LocalDate.now().format(DateTimeFormatter.ofPattern("dd"))).concat(" ")
                                .concat(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM"))).concat(" ")
                                .concat(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))).concat(" ")));
                        Thread.sleep(oneMinuteInMillSeconds);
                    } catch (Exception e) {
                        e.printStackTrace();

                        new Thread(stack_trace_printing(e)).start();
                        programmer_error(e).show();
                        break;
                    }
                }
                return null;
            }
        };
    }

    private void format_datePickers_to_show_my_preferred_date_style(@NotNull JFXDatePicker jfxDatePicker) {
        final StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            @NotNull
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateTimeFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        };
        jfxDatePicker.setConverter(converter);
    }

    private void reset_login_screen() {
        new FadeOut(paneTwo).play();
        paneOne.toFront();
        new FadeIn(paneOne).setDelay(Duration.seconds(0.5)).play();
    }

    private void start_up() {
        hide_splash_screen_and_show_login_screen();
        dateOfBirthDP.setValue(LocalDate.of(2000, Month.JANUARY, 1));
        format_datePickers_to_show_my_preferred_date_style(dateOfBirthDP);
        format_datePickers_to_show_my_preferred_date_style(newDateOfBirthDP);
    }

    private void hide_splash_screen_and_show_login_screen() {
        new FadeOut(splashScreenPane).play();
        mainPane.toFront();
        new FadeIn(mainPane).setDelay(Duration.seconds(0.5)).play();
    }
}
