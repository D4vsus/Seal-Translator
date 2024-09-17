package logic;

import exceptions.ArffAttributeNotRecognised;
import exceptions.TableOverflow;

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
     * @throws TableOverflow
     */
    public static void autoAssignAttributes(DataTable dataTable) throws TableOverflow {
        try {
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
                    Set<String> set = new HashSet<>();
                    int iterate = dataTable.size();
                    int batch = (AutoAssign.batch.equals("max"))?iteration:Integer.parseInt(AutoAssign.batch);
                    for (int x = 0; x < iterate && i < batch; x++) {
                        set.add(dataTable.getColumn(i).get(x));
                    }
                    dataTable.getAttributeItem(i).setAttributeTypeARFF("nominal", String.join(",", set));
                }
            }
        } catch (ArffAttributeNotRecognised ignore){

        }
    }

    /**
     * <h1>isNumber()</h1>
     * <p>See if the column is a number</p>
     *
     * @param column {@link HashMap}<{@link Integer},{@link String}>
     * @return boolean
     */
    private static boolean isNumber(HashMap<Integer,String> column){
        String realNumberPattern = "^[+-]?\\d+(\\.\\d+)?$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(realNumberPattern);

        int iteration = column.size();
        int batch = (AutoAssign.batch.equals("max"))?iteration:Integer.parseInt(AutoAssign.batch);
        for (int i = 0;i < iteration && i < batch;i++) {
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
        String realNumberPattern = "^'[^']*'$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(realNumberPattern);

        int iteration = column.size();
        int batch = (AutoAssign.batch.equals("max"))?iteration:Integer.parseInt(AutoAssign.batch);
        for (int i = 0;i < iteration && i < batch;i++) {
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
        String datePattern = "^(0[1-9]|[12][0-9]|3[01])[/-](0[1-9]|1[0-2])[/-](\\d{2,4})$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(datePattern);

        int iteration = column.size();
        int batch = (AutoAssign.batch.equals("max"))?iteration:Integer.parseInt(AutoAssign.batch);
        for (int i = 0; i < iteration && i < batch; i++) {
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
