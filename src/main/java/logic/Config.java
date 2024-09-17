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

    //methods
    public static boolean isDeleteCSComments() {
        return deleteCSComments;
    }
    public static void setDeleteCSComments(boolean deleteCSComments) {
        Config.deleteCSComments = deleteCSComments;
    }

    public static boolean isAutoAssign() {return autoAssign;}
    public static void setAutoAssign(boolean autoAssign) {Config.autoAssign = autoAssign;}
}
