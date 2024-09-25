package exceptions;

/**
 * <h1>BatchFormatException</h1>
 * <p>throw when using auto assign, see if the format is from 1-int max or the word max</p>
 *
 * @author D4vsus
 */
public class BatchFormatException extends Exception{
    @Override
    public String toString() {
        return "Error: Batch must be a natural number, or the word 'max'";
    }
}
