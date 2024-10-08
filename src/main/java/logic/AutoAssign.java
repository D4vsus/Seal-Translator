package logic;

import UI.LoadingScreen;
import exceptions.ArffAttributeNotRecognisedException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>AutoAssign</h1>
 * <p>Auto assigns the attributes</p>
 *
 * @author D4vsus
 */
public class AutoAssign {

    //variables and objects
    private static String batch = "max";

    //methods
    /**
     * <h1>autoAssignAttributes()</h1>
     * <p>Auto assigns all the attribute in the window</p>
     *
     * @param dataTable {@link DataTable}
     */
    public static @NotNull SwingWorker<Void, Void> autoAssignAttributes(DataTable dataTable) {
        //initialize the loading screen
        LoadingScreen loadingScreen = new LoadingScreen(dataTable.numberOfCells());

        return new SwingWorker<>() {
            //set the work in the background
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Iterate over the map
                    int iteration = dataTable.getAttributes().size();
                    for (int i = 0; i < iteration; i++) {
                        HashMap<Integer, String> column = dataTable.getColumn(i);
                        if (isNumber(column)) {
                            dataTable.getAttributeItem(i).setAttributeTypeARFF("numeric");
                        } else if (isString(column)) {
                            dataTable.getAttributeItem(i).setAttributeTypeARFF("string");
                        } else if (isDate(column)) {
                            dataTable.getAttributeItem(i).setAttributeTypeARFF("date", "dd-mm-yyyy");
                        } else {
                            // Create a set to save all the possibles types of nominal data
                            Set<String> set = new HashSet<>();

                            // See the number of iterations
                            int batch = (AutoAssign.batch.equals("max") || Integer.parseInt(AutoAssign.batch) >= column.size())?column.size():Integer.parseInt(AutoAssign.batch);

                            // Iterate over the column to see if matches with the pattern
                            HashMap<Integer, String> nominalColumn = dataTable.getColumn(i);
                            for (int x = 0; x < batch; x++) {
                                String possibleNewValue = nominalColumn.get(x);
                                if (!possibleNewValue.equals("?") && !possibleNewValue.isBlank()) {
                                    set.add(nominalColumn.get(x).replace(" ", "-"));
                                }
                            }
                            dataTable.getAttributeItem(i).setAttributeTypeARFF("nominal", String.join(",", set));
                        }
                        loadingScreen.update();
                    }
                } catch (ArffAttributeNotRecognisedException ignore){
                    loadingScreen.dispose();
                }
                return null;
            }

            //when they finish, delete the window
            @Override
            protected void done() {
                loadingScreen.dispose();
            }
        };
    }

    /**
     * <h1>isNumber()</h1>
     * <p>See if the column is a number</p>
     *
     * @param column {@link HashMap}<{@link Integer},{@link String}>
     * @return boolean
     */
    private static boolean isNumber(HashMap<Integer,String> column){
        String stringPattern = "^[+-]?\\d+(\\.\\d+)?$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(stringPattern);

        // See the number of iterations
        int batch = (AutoAssign.batch.equals("max") || Integer.parseInt(AutoAssign.batch) >= column.size())?column.size():Integer.parseInt(AutoAssign.batch);

        // Iterate over the column to see if matches with the pattern
        for (int i = 0;i < batch;i++) {
            Matcher matcher = pattern.matcher(column.get(i));
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * <h1>isString()</h1>
     * <p>See if the column is a String</p>
     *
     * @param column {@link HashMap}<{@link Integer},{@link String}>
     * @return boolean
     */
    private static boolean isString(HashMap<Integer,String> column){
        String stringPattern = "^'[^']*'$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(stringPattern);

        // See the number of iterations
        int batch = (AutoAssign.batch.equals("max") || Integer.parseInt(AutoAssign.batch) >= column.size())?column.size():Integer.parseInt(AutoAssign.batch);

        // Iterate over the column to see if matches with the pattern
        for (int i = 0;i < batch;i++) {
            Matcher matcher = pattern.matcher(column.get(i));
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * <h1>isDate()</h1>
     * <p>See if the column is a Date</p>
     *
     * @param column {@link HashMap}<{@link Integer},{@link String}>
     * @return boolean
     */
    private static boolean isDate(HashMap<Integer,String> column){
        String datePattern = "^(0[1-9]|[12][0-9]|3[01])[/-](0[1-9]|1[0-2])[/-](\\d{2,4})$|^(\\d{4})[/-](0[1-9]|1[0-2])[/-](0[1-9]|[12][0-9]|3[01])$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(datePattern);

        // See the number of iterations
        int batch = (AutoAssign.batch.equals("max") || Integer.parseInt(AutoAssign.batch) >= column.size())?column.size():Integer.parseInt(AutoAssign.batch);

        // Iterate over the column to see if matches with the pattern
        for (int i = 0;i < batch; i++) {
            Matcher matcher = pattern.matcher(column.get(i));
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    public static String getBatch() {return batch;}
    public static void setBatch(String batch) {AutoAssign.batch = batch;}
}
