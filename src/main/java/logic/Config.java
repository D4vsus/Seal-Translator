package logic;

/**
 * <h1>Config</h1>
 * <p>Save the configuration from the model</p>
 *
 * @author D4vsus
 */
public class Config {

    //variables and objects
    private static boolean deleteCSComments = true;
    private static boolean autoAssign = false;
    private static boolean nullString = false;
    private static String nullStringText = "";
    private static int rowsToVisualize = 20;

    //methods
    public static boolean isDeleteCSComments() {
        return deleteCSComments;
    }
    public static void setDeleteCSComments(boolean deleteCSComments) {
        Config.deleteCSComments = deleteCSComments;
    }

    public static boolean isAutoAssign() {return autoAssign;}
    public static void setAutoAssign(boolean autoAssign) {Config.autoAssign = autoAssign;}

    public static boolean isNullString() {return nullString;}
    public static void setNullString(boolean nullString) {Config.nullString = nullString;}
    public static String getNullString() {return nullStringText;}
    public static void setNullString(String nullString) {Config.nullStringText = nullString;}

    public static int getRowsToVisualize() {return rowsToVisualize;}
    public static void setRowsToVisualize(int rowsToVisualize) {Config.rowsToVisualize = rowsToVisualize;}
}
