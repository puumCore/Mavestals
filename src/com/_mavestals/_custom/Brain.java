package com._mavestals._custom;

import com._mavestals.Main;
import com._mavestals._object.*;
import com._mavestals._outsoured.BCrypt;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public abstract class Brain extends Watchdog {

    protected static Account currentAccount;
    protected static boolean accountIsTotallyNew = false;
    private final String PATH_TO_ACCOUNTS = Main.RESOURCE_PATH.getAbsolutePath().concat("\\_config\\account.json");
    private final String PATH_TO_LOGS = Main.RESOURCE_PATH.getAbsolutePath().concat("\\_config\\log.json");

    public boolean write_to_file(JsonArray jsonArray, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new Gson().toJson(jsonArray));
            fileWriter.close();
            fileWriter.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            programmer_error(ex).show();
        }
        return false;
    }

    public JsonArray read_file(File file) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            final JsonArray jsonArray = new Gson().fromJson(bufferedReader, JsonArray.class);
            bufferedReader.close();
            if (jsonArray == null) {
                return new JsonArray();
            } else {
                return jsonArray;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            programmer_error(ex).show();
        }
        return new JsonArray();
    }

    public boolean the_active_tali_has_zero_ledgers() {
        Tali tali = get_active_tali();
        if (tali == null) {
            return true;
        }
        return tali.getLedgers() == null || tali.getLedgers().size() == 0;
    }

    public boolean the_active_tali_has_one_ledger() {
        Tali tali = get_active_tali();
        return tali.getLedgers().size() == 1;
    }

    public void update_account_username(String oldName) {
        final File file = new File(PATH_TO_ACCOUNTS);
        final JsonArray jsonArray = read_file(file);
        Account oldAccount = null;
        for (JsonElement jsonElement : jsonArray) {
            Account account = new Gson().fromJson(jsonElement, Account.class);
            if (account.getUsername().equals(oldName)) {
                oldAccount = account;
                break;
            }
        }
        if (oldAccount != null) {
            jsonArray.remove(new Gson().toJsonTree(oldAccount, Account.class));
            jsonArray.add(new Gson().toJsonTree(Brain.currentAccount, Account.class));
            write_to_file(jsonArray, new File(PATH_TO_ACCOUNTS));
        }
    }

    public boolean there_are_no_accounts() {
        final File file = new File(PATH_TO_ACCOUNTS);
        final JsonArray jsonArray = read_file(file);
        return jsonArray.size() == 0;
    }

    protected boolean update_active_tali(Tali tali) {
        Brain.currentAccount.getTalis().remove(new Gson().toJsonTree(get_active_tali(), Tali.class));
        Brain.currentAccount.getTalis().add(new Gson().toJsonTree(tali, Tali.class));
        return update_account();
    }

    public boolean deactivate_previous_tali(Tali before, Tali after) {
        Brain.currentAccount.getTalis().remove(new Gson().toJsonTree(before, Tali.class));
        Brain.currentAccount.getTalis().add(new Gson().toJsonTree(after, Tali.class));
        return update_account();
    }

    public boolean create_tali(Tali tali) {
        if (accountIsTotallyNew) {
            Brain.currentAccount.setTalis(new JsonArray());
            accountIsTotallyNew = false;
        }
        Brain.currentAccount.getTalis().add(new Gson().toJsonTree(tali, Tali.class));
        return update_account();
    }

    public Tali get_active_tali() {
        if (Brain.currentAccount.getTalis() != null) {
            for (JsonElement jsonElement : Brain.currentAccount.getTalis()) {
                final Tali tali = new Gson().fromJson(jsonElement, Tali.class);
                if (tali.getActive()) {
                    return tali;
                }
            }
        }
        return null;
    }

    public boolean activity_not_created(Activity activity) {
        if (Brain.currentAccount.getActivities() == null) {
            Brain.currentAccount.setActivities(new JsonArray());
        }
        Brain.currentAccount.getActivities().add(new Gson().toJsonTree(activity, Activity.class));
        return !update_account();
    }


    public final void write_log(String action, String description) {
        File file = new File(PATH_TO_LOGS);
        final JsonArray jsonArray = read_file(file);
        Log log = new Log();
        log.setTime(get_time());
        log.setDate(get_date());
        if (Brain.currentAccount == null) {
            log.setUsername("user not logged in");
        } else {
            log.setUsername(Brain.currentAccount.getUsername());
        }
        log.setAction(action);
        log.setDescription(description);
        jsonArray.add(new Gson().toJsonTree(log, Log.class));
        write_to_file(jsonArray, file);
    }

    public boolean update_account() {
        final File file = new File(PATH_TO_ACCOUNTS);
        final JsonArray oldList = read_file(file);
        final JsonArray newList = new JsonArray();
        for (JsonElement jsonElement : oldList) {
            Account account = new Gson().fromJson(jsonElement, Account.class);
            if (!account.getUsername().equals(Brain.currentAccount.getUsername())) {
                newList.add(jsonElement);
            }
        }
        if (oldList.size() > 0) {
            newList.add(new Gson().toJsonTree(Brain.currentAccount, Account.class));
            return write_to_file(newList, new File(PATH_TO_ACCOUNTS));
        }
        return false;
    }

    public boolean create_ledger(Ledger ledger) {
        for (JsonElement jsonElement : Brain.currentAccount.getTalis()) {
            final Tali tali = new Gson().fromJson(jsonElement, Tali.class);
            if (tali.getActive()) {
                if (tali.getLedgers() == null) {
                    tali.setLedgers(new JsonArray());
                }
                tali.getLedgers().add(new Gson().toJsonTree(ledger, Ledger.class));
                update_active_tali(tali);
                return true;
            }
        }
        return false;
    }

    public boolean username_is_taken(String username) {
        boolean duplicateFound = false;
        final File file = new File(PATH_TO_ACCOUNTS);
        final JsonArray jsonArray = read_file(file);
        for (JsonElement jsonElement : jsonArray) {
            Account account = new Gson().fromJson(jsonElement, Account.class);
            if (account.getUsername().equals(username)) {
                duplicateFound = true;
                break;
            }
        }
        return duplicateFound;
    }

    public final boolean has_created_a_new_account(Account account) {
        final File file = new File(PATH_TO_ACCOUNTS);
        final JsonArray jsonArray = read_file(file);
        jsonArray.add(new Gson().toJsonTree(account, Account.class));
        return write_to_file(jsonArray, file);
    }

    public final @Nullable Boolean the_user_has_an_account(String username, String password) {
        final File file = new File(PATH_TO_ACCOUNTS);
        final JsonArray jsonArray = read_file(file);
        for (JsonElement jsonElement : jsonArray) {
            Account account = new Gson().fromJson(jsonElement, Account.class);
            if (account.getUsername().equals(username)) {
                if (BCrypt.checkpw(password, account.getUser().getPassword())) {
                    Brain.currentAccount = account;
                    if (Brain.currentAccount.getTalis() == null) {
                        accountIsTotallyNew = true;
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
        return null;
    }

}
