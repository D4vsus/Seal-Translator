package exceptions;

/**
 * <h1>BatchFormatTableViewException</h1>
 * <p>throw when using auto assign, see if the format is from 1-int max</p>
 *
 * @author D4vsus
 */
public class BatchFormatTableViewException extends Exception{
    @Override
    public String toString() {
        return "Error: Batch must be a natural number";
    }
}
